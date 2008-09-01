package example.test

import example.google.GoogleStartPage
import aost.test.StandaloneAostSeleneseTestCase

class GoogleStartPageTestCase extends StandaloneAostSeleneseTestCase{

    protected static GoogleStartPage gsp

    public void initUi() {
        gsp = new GoogleStartPage()
        gsp.defineUi()

    }

    public void setUp(){
        setUpForClass()
    }

    public void tearDown(){
        tearDownForClass()
    }

//    @Test
    void testTypeGoogle(){
        connectUrl("http://www.google.com")
        gsp.type("google")
    }

//    public static junit.framework.Test suite(){
//	    return new JUnit4TestAdapter(GoogleStartPageTestCase.class)
//    }
}