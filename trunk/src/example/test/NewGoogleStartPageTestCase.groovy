package example.test

import org.aost.test.StandaloneAostSeleneseTestCase
import example.google.NewGoogleStartPage

/**
 *  Test case for Google start page with new UI definition features such as composite locator and group information
 *
 *  @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 */
class NewGoogleStartPageTestCase  extends StandaloneAostSeleneseTestCase{

    protected static NewGoogleStartPage ngsp

    public void initUi() {
        ngsp = new NewGoogleStartPage()
        ngsp.defineUi()

    }

    public void setUp(){
        setUpForClass()
    }

    public void tearDown(){
        tearDownForClass()
    }

    void testGoogleSearch(){
        connectUrl("http://www.google.com")
        ngsp.doGoogleSearch("aost selenium")
        connectUrl("http://www.google.com")
        ngsp.doImFeelingLucky("aost selenium")
   }

}