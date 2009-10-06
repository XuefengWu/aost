parser grammar T3;
@header {
package p;
}
@members {
int i;
public TParser(TokenStream input, int foo) {
this(input);
i = foo;
}
}
a[int x] returns [int y]
@init {int z=0;}
@after {System.out.println("after matching rule; before finally");}
: {«action1 »} {«action2 »}
;
catch[RecognitionException re] {
System.err.println("error");
}
finally { «do-this-no-matter-what » }
      
