package org.telluriumsource.ast;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.objectweb.asm.Opcodes;

import java.util.*;

/**
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *         
 *         Date: Sep 30, 2010
 */

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class InjectASTTransformation implements ASTTransformation, Opcodes {

    private static final Expression NULL_EXPR = ConstantExpression.NULL;

    private static final Token ASSIGN = Token.newSymbol("=", -1, -1);
    private static final Token COMPARE_NOT_EQUAL = Token.newSymbol("!=", -1, -1);

    private static final List<InjectInfo> list = new ArrayList<InjectInfo>();

    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (nodes.length != 2 || !(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
        }

        AnnotatedNode parent = (AnnotatedNode) nodes[1];
        AnnotationNode node = (AnnotationNode) nodes[0];
        if (parent instanceof FieldNode) {
            final FieldNode fieldNode = (FieldNode) parent;

            String name;
            final Expression nameExpr = node.getMember("name");
            if(nameExpr != null && nameExpr instanceof ConstantExpression){
                name = (String) ((ConstantExpression)nameExpr).getValue();
            }else{
                name = fieldNode.getType().getName();
            }

            boolean lazy = false;
            final Expression lazyExpr = node.getMember("lazy");
            if (lazyExpr != null && lazyExpr instanceof ConstantExpression) {
                lazy = (Boolean) ((ConstantExpression) lazyExpr).getValue();
            }

            ClassNode injector = InjectorASTTransformation.getInjector();

            if(injector == null){
                InjectInfo info = new InjectInfo(fieldNode, name, lazy);
                list.add(info);
            }else{
                if(!list.isEmpty()){
                    for(InjectInfo inf: list){
                        inject(inf.getFieldNode(), inf.getName(), inf.isLazy());
                    }
                    list.clear();
                }
                inject(fieldNode, name, lazy);
            }
        }
    }
    
    private void inject(FieldNode fieldNode, String name, boolean lazy){
        if(lazy){
//                addMethodPointer(name, fieldNode);
            renameFieldNodeAndAddGetter(name, fieldNode);
        }else{
            addMethodToConstructor(name, fieldNode);
        }
    }

    private void renameFieldNodeAndAddGetter(String name, FieldNode fieldNode){
        fieldNode.setInitialValueExpression(null);
        fieldNode.rename("$" + fieldNode.getName());
        fieldNode.setModifiers(ACC_PRIVATE | (fieldNode.getModifiers() & (~(ACC_PUBLIC | ACC_PROTECTED))));

        int visibility = ACC_PUBLIC;
        if (fieldNode.isStatic()) visibility |= ACC_STATIC;
        final String getterName = "get" + MetaClassHelper.capitalize(fieldNode.getName().substring(1));
        final BlockStatement body = new BlockStatement();
        Expression initExpr = getInitExpr(name);
        addNonThreadSafeBody(body, fieldNode, initExpr);

        fieldNode.getDeclaringClass().addMethod(getterName, visibility,  fieldNode.getType(), Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, body);

        // Lazy not meaningful with primitive so convert to wrapper if needed
        if (ClassHelper.isPrimitiveType(fieldNode.getType())) {
            fieldNode.setType(ClassHelper.getWrapper(fieldNode.getType()));
        }
    }

    private void addNonThreadSafeBody(BlockStatement body, FieldNode fieldNode, Expression initExpr) {
        final Expression fieldExpr = new VariableExpression(fieldNode);
        body.addStatement(new IfStatement(
                new BooleanExpression(new BinaryExpression(fieldExpr, COMPARE_NOT_EQUAL, NULL_EXPR)),
                new ExpressionStatement(fieldExpr),
                new ExpressionStatement(new BinaryExpression(fieldExpr, ASSIGN, initExpr))
        ));
    }

    private Expression getInitExpr(String name){
        ClassNode injector = InjectorASTTransformation.getInjector();

        return new MethodCallExpression(
            new MethodCallExpression(
                    new ClassExpression(injector),
                    new ConstantExpression("getInstance"),
                    new ArgumentListExpression()
            ),
            new ConstantExpression("getByName"),
            new ArgumentListExpression(
                new ConstantExpression(name)
            )
        );
    }

    private void addMethodPointer(String name, FieldNode fieldNode){
        ClassNode classNode = fieldNode.getType();
        final String methodName = "inject" + MetaClassHelper.capitalize(fieldNode.getName());
        ClassNode injector = InjectorASTTransformation.getInjector();

        BlockStatement body = new BlockStatement();
        body.addStatement(
                new ReturnStatement(
                    new MethodCallExpression(
                        new MethodCallExpression(
                                new ClassExpression(injector),
                                new ConstantExpression("getInstance"),
                                new ArgumentListExpression()
                        ),
                        new ConstantExpression("getByName"),
                        new ArgumentListExpression(
                            new ConstantExpression(name)
                        )
                    )
                )                     

        );

        addMethod(methodName, fieldNode, body, classNode);

        addLazyMethodToConstructor(fieldNode, methodName);

    }

    private void addLazyMethodToConstructor(FieldNode fieldNode, String methodName){
        ClassNode classNode = fieldNode.getDeclaringClass();

        List list = classNode.getDeclaredConstructors();
        if(list == null || list.isEmpty()){
            final BlockStatement emptyBody = new BlockStatement();
            classNode.addConstructor(new ConstructorNode(ACC_PUBLIC, emptyBody));
        }

        list = classNode.getDeclaredConstructors();
        for(int i=0; i<list.size(); i++){
            MethodNode mn = (MethodNode) list.get(i);
            BlockStatement body = ((BlockStatement)mn.getCode());
            if(body == null){
                body = new BlockStatement();
            }
            List existingStatements = body.getStatements();
            Statement stm = getLazyAssignmentStatement(methodName, fieldNode);
            existingStatements.add(stm);
        }
    }

    private Statement getLazyAssignmentStatement(String methodName, FieldNode fieldNode) {
        final Expression fieldExpr = new VariableExpression(fieldNode);
        ClassNode clazz = fieldNode.getType();
        return new ExpressionStatement(
                new BinaryExpression(
                        fieldExpr,
                        ASSIGN,
                        new CastExpression(
                                clazz,
                                new MethodCallExpression(
                                        new VariableExpression("this"),
                                        new ConstantExpression(methodName),
                                        new ArgumentListExpression()
                                ),
                                true
                        )

                )

        );
    }

    private void addMethod(String name, FieldNode fieldNode, BlockStatement body, ClassNode type) {
        int visibility = ACC_PUBLIC;
        if (fieldNode.isStatic()) visibility |= ACC_STATIC;
        fieldNode.getDeclaringClass().addMethod(name, visibility, type, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, body);
    }

    private void addMethodToConstructor(String name, FieldNode fieldNode){

        ClassNode classNode = fieldNode.getDeclaringClass();

        List list = classNode.getDeclaredConstructors();
        if(list == null || list.isEmpty()){
            final BlockStatement emptyBody = new BlockStatement();
            classNode.addConstructor(new ConstructorNode(ACC_PUBLIC, emptyBody));
        }

        list = classNode.getDeclaredConstructors();
        for(int i=0; i<list.size(); i++){
            MethodNode mn = (MethodNode) list.get(i);
            BlockStatement body = ((BlockStatement)mn.getCode());
            if(body == null){
                body = new BlockStatement();
            }
            List existingStatements = body.getStatements();
            Statement stm = getInjectStatement(name, fieldNode);
            existingStatements.add(stm);
        }
    }

    private Statement getInjectStatement(String name, FieldNode fieldNode) {
        final Expression fieldExpr = new VariableExpression(fieldNode);
        ClassNode injector = InjectorASTTransformation.getInjector();

        return new ExpressionStatement(
                new BinaryExpression(
                        fieldExpr,
                        ASSIGN,
                        new MethodCallExpression(
                                new MethodCallExpression(
                                        new ClassExpression(injector),
                                        new ConstantExpression("getInstance"),
                                        new ArgumentListExpression()
                                ),
                                new ConstantExpression("getByName"),
                                new ArgumentListExpression(
                                        new ConstantExpression(name)
                                )
                        )


                )

        );
    }

    class InjectInfo{
        
        private String name;

        private boolean lazy;

        private FieldNode fieldNode;

        InjectInfo(FieldNode fieldNode, String name, boolean lazy) {
            this.fieldNode = fieldNode;
            this.lazy = lazy;
            this.name = name;
        }

        public FieldNode getFieldNode() {
            return fieldNode;
        }

        public void setFieldNode(FieldNode fieldNode) {
            this.fieldNode = fieldNode;
        }

        public boolean isLazy() {
            return lazy;
        }

        public void setLazy(boolean lazy) {
            this.lazy = lazy;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
