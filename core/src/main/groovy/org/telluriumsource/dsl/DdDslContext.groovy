package org.telluriumsource.dsl

import org.telluriumsource.test.ddt.mapping.type.TypeHandlerRegistry
import org.telluriumsource.test.ddt.mapping.FieldSetRegistry
import org.telluriumsource.test.ddt.mapping.FieldSetParser
import org.telluriumsource.test.ddt.mapping.type.TypeHandlerRegistryConfigurator
import org.telluriumsource.test.ddt.DataProvider
import org.telluriumsource.test.ddt.mapping.mapping.FieldSetMapResult
import org.telluriumsource.test.ddt.mapping.FieldSet
import org.telluriumsource.test.ddt.mapping.TestField
import org.telluriumsource.test.ddt.TestRegistry
import org.telluriumsource.test.report.DefaultResultListener
import org.telluriumsource.test.report.TestResult
import org.telluriumsource.test.report.ResultListener
import org.telluriumsource.test.report.StepStatus
import org.telluriumsource.test.report.AssertionResult
import junit.framework.AssertionFailedError
import org.telluriumsource.framework.SessionManager
import org.telluriumsource.test.report.ComparisonAssertionValue

/**
 *
 * Extended DslContext for Data Driven Test
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Jul 24, 2008
 *
 */
abstract class DdDslContext extends DslContext{
    
//    protected TypeHandlerRegistry thr  = new TypeHandlerRegistry()
//    protected FieldSetRegistry fsr = new FieldSetRegistry()

//    protected DataProvider dataProvider = new DataProvider(fsr, thr)

//    protected FieldSetParser fs = new FieldSetParser(fsr)

//    protected TestRegistry ar = new TestRegistry()

//    protected ResultListener listener = new DefaultResultListener()


    public TypeHandlerRegistry getTypeHandlerRegistry(){
      return SessionManager.getSession().getByClass(TypeHandlerRegistry.class);
    }

    public FieldSetRegistry getFieldSetRegistry(){
      return SessionManager.getSession().getByClass(FieldSetRegistry.class);
    }

    public DataProvider getDataProvider(){
      return SessionManager.getSession().getByClass(DataProvider.class);
    }

    public FieldSetParser getFieldSetParser(){
      return SessionManager.getSession().getByClass(FieldSetParser.class); 
    }

    public TestRegistry getTestRegistry(){
      return SessionManager.getSession().getByClass(TestRegistry.class);
    }

    public ResultListener getDefaultResultListener(){
      return SessionManager.getSession().getByClass(ResultListener.class);
    }

    //the count of number of steps can also be used to identify the ith run of the test
    protected int stepCount = 0

    // DSL to define your customer type handler such as
    // typeHandler "simpleDate", "tellurium.example.simpleDateTypeHandler"
    // here we assume that you have defined the tellurium.example.simpleDateTypeHandler class
    // it extends the TypeHandler interface
    public void typeHandler(String typeName, String fullClassName){
        TypeHandlerRegistryConfigurator.addCustomTypeHandler(getTypeHandlerRegistry(), typeName, fullClassName)
    }

    //DSL to bind variables to data read from the file
    // def var1 = bind("dataset1.username")
    public def bind(String dataFieldId){

        return getDataProvider().bind(dataFieldId)
    }

    //flow control
    //read the file and run the test script until it reaches the end of the file
    public void stepToEnd(Closure c){
        boolean hasMore = true
        while(hasMore){
            hasMore = step(c)
        }
    }

    public void stepToEnd(){
        stepToEnd(null)
    }

//    def stepToEnd = this.&stepToEnd

    //read one line from the file and run the test script so that you can have different
    //test scripts for each line
    public boolean step(Closure c){
        //get data from the data stream
        FieldSetMapResult fsmr = getDataProvider().nextFieldSet()
        //check if we reach the end of data stream
        if(fsmr != null && (!fsmr.isEmpty())){
            //check if the field set includes test name
            String test = getTestForFieldSet(fsmr.getFieldSetName())
            TestResult result = new TestResult()
            result.setProperty("testName", test)
            result.setProperty("stepId", ++stepCount)
            result.setProperty("start", System.nanoTime())
            result.setProperty("input", fsmr.getResults())
 //           result.setProperty("passed", true)

            try{
                if(test != null){
                    //if the field set includes test
                    //get the pre-defined test and run it
                    Closure closure = getTestRegistry().getTest(test)
                    closure()
                }

                //if there is other user defined closure, run it
                if(c != null){
                    c()
                }
                result.setProperty("status", StepStatus.PROCEEDED)
            }catch(Exception e){
                result.setProperty("status", StepStatus.EXECPTION)
//                result.setProperty("passed", false)
                result.setProperty("exception", e)
            }
            result.setProperty("end", System.nanoTime())
            getDefaultResultListener().listenForInput(result)

            return true
        }

        return false
    }

    public void step(){
        step(null)
    }

//    def step = this.&step

    //read one from the file but do not run the test script. This may apply to the scenario
    //that you need to read multiple lines before you can run the test
    //If the next line is of the same Field set as the current one, the data reading in will
    //be overwritten after this command
    public boolean stepOver(){
        FieldSetMapResult fsmr = getDataProvider().nextFieldSet()
        //check if we reach the end of data stream
        if(fsmr != null && (!fsmr.isEmpty())){
            //check if the field set includes action name
            String action = getTestForFieldSet(fsmr.getFieldSetName())

            TestResult result = new TestResult()
            result.setProperty("testName", action)
            result.setProperty("stepId", ++stepCount)
            result.setProperty("input", fsmr.getResults())
            result.setProperty("status", StepStatus.SKIPPED)
//            result.setProperty("passed", true)

            getDefaultResultListener().listenForInput(result)

            return true
        }

        return false
    }

    //make DSL more expressive, instead of put stepOver(), but define the following
    //you can simply write stepOver
//    def stepOver = this.&stepOver

    public void loadData(String filePath){
        getDataProvider().useFile(filePath)
    }

    //useString data defined in the script file
    public void useData(String data){
        getDataProvider().useString(data)
    }

    public void closeData(){
        getDataProvider().stop()
        getDefaultResultListener().report()
    }

//    def closeData = this.&closeData

    public void defineTest(String name, Closure c){
        getTestRegistry().addTest(name, c)
    }

    protected String getTestForFieldSet(String fieldSetName){
        FieldSet tfs = getFieldSetRegistry().getFieldSetByName(fieldSetName)
        if(tfs != null){
            TestField taf = tfs.getActionField()
            if(taf != null){
                String tid = fieldSetName + "." + taf.getName()
                return getDataProvider().bind(tid)
            }
        }

        return null
    }

    public void listenForResult(org.telluriumsource.test.report.TestResult result ){
        getDefaultResultListener().listenForResult(result)
    }

    public boolean compareResult(expected, actual){
        return compareResult(expected, actual, null)
    }

    public boolean compareResult(expected, actual, Closure c){
        boolean passed = true

        //TODO: The current implementation does not work, need to figure out how to pass the expected and actual to result listener
        TestResult result = new TestResult()
        AssertionResult assertResult = new AssertionResult()

        result.setProperty("stepId", stepCount + 1)
        ComparisonAssertionValue value = new ComparisonAssertionValue();
        value.expected = expected;
        value.actual = actual;
//        assertResult.setProperty("expected", expected)
//        assertResult.setProperty("actual", actual)
        assertResult.value = value;

        try{
            //allow user to override the default assertion use
            //closure to define comparison
            if(c != null){
                c()
            }else{
                //if the closure is not defined, use the default Junit assertion
                junit.framework.Assert.assertEquals(expected, actual)
            }
        }catch(AssertionFailedError e){
            passed = false
            assertResult.setProperty("error", e)
        }

        assertResult.setProperty("passed", passed)
        result.addAssertationResult(assertResult)
        listenForResult(result)

        return passed
    }

    //add assertions here so that user can add custom compare result code in the closure
    public void assertTrue(boolean condition){
        junit.framework.Assert.assertTrue(condition)
    }

    public void assertFalse(boolean condition){
        junit.framework.Assert.assertFalse(condition)
    }

    public void fail(String message){
        junit.framework.Assert.fail(message)
    }

    public void assertEquals(expected, actual){
        junit.framework.Assert.assertEquals(expected, actual)
    }

    public void assertNotNull(object){
        junit.framework.Assert.assertNotNull(object)
    }

    public void assertNull(object){
        junit.framework.Assert.assertNull(object)
    }

    public void assertSame(expected, actual){
        junit.framework.Assert.assertSame(expected, actual)
    }

    public void assertNotSame(expected, actual){
        junit.framework.Assert.assertNotSame(expected, actual)
    }

}