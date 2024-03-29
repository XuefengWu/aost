package org.telluriumsource.test.java;

import org.telluriumsource.framework.bootstrap.TelluriumSupport;
import org.telluriumsource.component.connector.SeleniumConnector;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

/**
 * Tellurium Test Case to support TestNG
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 *         Date: Aug 20, 2008
 */
public abstract class TelluriumTestNGTestCase extends BaseTelluriumJavaTestCase {

    @BeforeTest(alwaysRun = true)
    public static void setUpForTest() {
        if(tellurium == null){
            tellurium = TelluriumSupport.addSupport();
            tellurium.startServer(customConfig);
//            tellurium.connectServer();
            connector = (SeleniumConnector) getCurrentConnector();
        }
    }

    @AfterTest(alwaysRun = true)
    public static void tearDownForTest() {
        if(tellurium != null){
            tellurium.disconnectServer();
            tellurium.stopServer();
        }

    }
}
