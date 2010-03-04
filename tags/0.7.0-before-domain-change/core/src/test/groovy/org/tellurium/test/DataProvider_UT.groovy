package org.tellurium.test

import org.tellurium.config.TelluriumConfigurator;
import org.tellurium.config.TelluriumConfiguratorMetaClass;
import org.tellurium.ddt.DataProvider
import org.tellurium.ddt.object.mapping.FieldSetParser
import org.tellurium.ddt.object.mapping.FieldSetRegistry
import org.tellurium.ddt.object.mapping.mapping.FieldSetMapResult
import org.tellurium.ddt.object.mapping.type.TypeHandlerRegistry
import org.tellurium.ddt.object.mapping.type.TypeHandlerRegistryConfigurator

/**
 *
 *
 * @author: Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Jul 24, 2008
 *
 */
class DataProvider_UT extends GroovyTestCase{
    protected TypeHandlerRegistry thr  = new TypeHandlerRegistry()
    protected FieldSetRegistry fsr = new FieldSetRegistry()

    protected DataProvider dataProvider = new DataProvider(fsr, thr)

    protected FieldSetParser fs = new FieldSetParser(fsr)

    public void setUp(){
        TypeHandlerRegistryConfigurator.addCustomTypeHandler(thr, "phoneNumber", "org.tellurium.test.PhoneNumberTypeHandler")

        fs.FieldSet(name: "fs4googlesearch", description: "example field set for google search"){
            Field(name: "regularSearch", type: "boolean", description: "whether we should use regular search or use I'm feeling lucky")
            Field(name: "phoneNumber", type: "phoneNumber", description: "Phone number")
            Field(name: "input", description: "input variable")
        }

        def registry = GroovySystem.metaClassRegistry
        registry.setMetaClass(TelluriumConfigurator, new TelluriumConfiguratorMetaClass())
    }

    public void testFetchData(){
        String data = """
            true | 865-692-6000 | tellurium groovy
            false| 865-123-4444 | tellurium selenium.test
        """
//        dataProvider.start("src/example/test/ddt/googlesearchpullinput.txt")
        dataProvider.useString(data)
        
        FieldSetMapResult result = dataProvider.nextFieldSet()
        assertNotNull(result)
        assertFalse(result.isEmpty())
        boolean var1 = dataProvider.bind("regularSearch")
        def var2 = dataProvider.bind("fs4googlesearch.phoneNumber")
        String var3 = dataProvider.bind("fs4googlesearch.input")
        assertTrue(var1)
        assertEquals("8656926000", var2)
        assertEquals("tellurium groovy", var3)
        result = dataProvider.nextFieldSet()
        var1 = dataProvider.bind("regularSearch")
        var2 = dataProvider.bind("fs4googlesearch.phoneNumber")
        var3 = dataProvider.bind("fs4googlesearch.input")
        assertFalse(var1)
        assertEquals("8651234444", var2)
        assertEquals("tellurium selenium.test", var3)
        assertNotNull(result)
        assertFalse(result.isEmpty())
        result = dataProvider.nextFieldSet()
        assertNull(result)
        dataProvider.stop()
    }

    public void testFetchExcelData(){   
    	TelluriumConfigurator telluriumConfigurator = new TelluriumConfigurator()
        telluriumConfigurator.parse(ClassLoader.getSystemResource("config/TelluriumConfigForExcelReader.groovy").getFile())
        
    	dataProvider.useFile(ClassLoader.getSystemResource("data/excelDataReaderTest.xls").getFile())
        
        FieldSetMapResult result = dataProvider.nextFieldSet()
        assertNotNull(result)
        assertFalse(result.isEmpty())
        boolean var1 = dataProvider.bind("regularSearch")
        def var2 = dataProvider.bind("fs4googlesearch.phoneNumber")
        String var3 = dataProvider.bind("fs4googlesearch.input")
        assertTrue(var1)
        assertEquals("8656926000", var2)
        assertEquals("tellurium", var3)
        result = dataProvider.nextFieldSet()
        var1 = dataProvider.bind("regularSearch")
        var2 = dataProvider.bind("fs4googlesearch.phoneNumber")
        var3 = dataProvider.bind("fs4googlesearch.input")
        assertFalse(var1)
        assertEquals("8651234444", var2)
        assertEquals("tellurium selenium test", var3)
        assertNotNull(result)
        assertFalse(result.isEmpty())
        result = dataProvider.nextFieldSet()
        assertNull(result)
        dataProvider.stop()
    }

    public void tearDown(){

    }

}