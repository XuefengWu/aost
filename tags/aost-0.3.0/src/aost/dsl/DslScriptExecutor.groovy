package aost.dsl

class DslScriptExecutor {

    static void main(String[] args){
       if(args != null && args.length == 1){
            def dsl = new File(args[0]).text
            def script = """
                import aost.dsl.DslScriptEngine

                class DslTest extends DslScriptEngine{
                    def test(){
                        init()
                        ${dsl}
                        shutDown()
                    }
                }

                DslTest instance = new DslTest()
                instance.test()
            """

            new GroovyShell().evaluate(script)

       }else{
           println("Usage: DslScriptExecutor dsl_file")
       }

    }
}