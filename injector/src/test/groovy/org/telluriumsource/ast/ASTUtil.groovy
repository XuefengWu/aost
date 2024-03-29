package org.telluriumsource.ast

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.telluriumsource.mock.MockInjector

/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Sep 30, 2010
 * 
 */
class ASTUtil {
  private String aVar;

  public static List<ASTNode> getProviderNodes(String name, Class clazz, String scope, boolean singleton){
    List<ASTNode> nodes = new AstBuilder().buildFromCode {
      MockInjector.instance.addBean(name, clazz, clazz, scope, singleton, null);
    }

    return nodes;
  }

  public static List<ASTNode> getInjectNodeByClass(Class clazz){
    List<ASTNode> nodes = new AstBuilder().buildFromCode {
      Object var;

      var = MockInjector.getInstance().getByClass(clazz);
    }

    return nodes;
  }

  public static List<ASTNode> getInjectNodeByName(String name){
    List<ASTNode> nodes = new AstBuilder().buildFromCode {
      Object var;

      var = MockInjector.getInstance().getByName(name);
    }

    return nodes;
  }

  private Object injectVar(){
     String name = "good";
    return name;
  }

  public void setAVar(){
    this.aVar = this.&injectVar as String;
  }

  public List<ASTNode> getInjectNodeByNameLazy(String name){
    List<ASTNode> nodes = new AstBuilder().buildFromCode {
        aVar = this.&injectVar as String;
    }

    return nodes;
  }

  public List<ASTNode> getInjectMethodLazy(){
    List<ASTNode> nodes = new AstBuilder().buildFromCode {
      String name = "good";
      return MockInjector.getInstance().getByName(name);
    }

    return nodes;
  }

}
