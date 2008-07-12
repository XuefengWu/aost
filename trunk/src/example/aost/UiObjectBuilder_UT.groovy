package example.aost
/**
 *
 *  Test all default UI object builder
 *
 *   @author Jian Fang (John.Jian.Fang@gmail.com)
 */
class UiObjectBuilder_UT extends GroovyTestCase{

    public void testBaseLocator(){
        SampleUI sample = new SampleUI()
        sample.defineUi()
        Map registry = sample.ui.registry
        assertNotNull(registry)
        assertEquals(1, registry.size())
    }

    public void testCompositeLocator(){
        SampleUI sample = new SampleUI()
        sample.defineCompositeUi()
        Map registry = sample.ui.registry
        assertNotNull(registry)
        assertEquals(1, registry.size())
    }

    public void testMultiUiModules(){
        SampleUI sample = new SampleUI()
        sample.defineMultipleUiModules()
        Map registry = sample.ui.registry
        assertNotNull(registry)
        assertEquals(4, registry.size())
    }
}