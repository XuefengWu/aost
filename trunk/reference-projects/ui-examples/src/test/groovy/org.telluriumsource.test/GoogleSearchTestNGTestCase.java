package org.telluriumsource.test;

import org.telluriumsource.module.GoogleSearchModule;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import org.telluriumsource.test.java.TelluriumTestNGTestCase;
import org.telluriumsource.entity.CachePolicy;

import java.util.List;
import java.util.ArrayList;



/**
 *    Test cases created based on the GoogleSearchModule UI module
 *
 *
 */
public class GoogleSearchTestNGTestCase extends TelluriumTestNGTestCase{
    
    private static GoogleSearchModule gsm;
    private static String te_ns = "http://telluriumsource.org/ns";

    @BeforeClass
    public static void initUi() {
        gsm = new GoogleSearchModule();
        gsm.defineUi();
        connectSeleniumServer();
        useCssSelector(true);
        useTelluriumEngine(true);
        useTrace(true);
    }

    @BeforeMethod
    public void connectToGoogle() {

        connectUrl("http://www.google.com");
    }

    @Test
    public void testJsonfyUiModule(){
        String json = gsm.toString("Google");
        System.out.println(json);
    }

    @Test
    public void testGoogleSearch() {
        gsm.doGoogleSearch("tellurium . ( Groovy ) Test");
    }

    @Test
    public void testGoogleSearchFeelingLucky() {
        gsm.doImFeelingLucky("tellurium automated Testing");
    }

    @Test
    public void testLogo(){
        gsm.diagnose("Logo");
        String alt = gsm.getLogoAlt();
        assertNotNull(alt);
//        assertEquals("Google", alt);
//        assertEquals("E.C. Segar's Birthday", alt);
    }

    @Test
    public void testClosestMatch(){
        useClosestMatch(true);
        String alt = gsm.getLogoAlt();
        assertNotNull(alt);
        useClosestMatch(false);
    }

    @Test
    public void testIsDisabled(){
        useCssSelector(true);
        boolean result = gsm.isInputDisabled();
        assertFalse(result);
        useCssSelector(false);
        result = gsm.isInputDisabled();
        assertFalse(result);
    }

    @Test
    public void testRegisterNamespace(){
        registerNamespace("te", te_ns);
        String ns = getNamespace("te");
        assertNotNull(ns);
        assertEquals(te_ns, ns);
        ns = getNamespace("x");
        assertNotNull(ns);
        assertEquals("http://www.w3.org/1999/xhtml", ns);
        ns = getNamespace("mathml");
        assertNotNull(ns);
        assertEquals("http://www.w3.org/1998/Math/MathML", ns);
    }

    @Test
    public void testCachePolicy(){
        useCssSelector(true);
        useTelluriumEngine(true);
        String policy = getCurrentCachePolicy();
        assertEquals("DiscardOldPolicy", policy);
        useCachePolicy(CachePolicy.DISCARD_LEAST_USED);
        policy = getCurrentCachePolicy();
        assertEquals("DiscardLeastUsedPolicy", policy);
        useCachePolicy(CachePolicy.DISCARD_INVALID);
        policy = getCurrentCachePolicy();
        assertEquals("DiscardInvalidPolicy", policy);
        useCachePolicy(CachePolicy.DISCARD_NEW);
        policy = getCurrentCachePolicy();
        assertEquals("DiscardNewPolicy", policy);
        useCachePolicy(CachePolicy.DISCARD_OLD);
        policy = getCurrentCachePolicy();
        assertEquals("DiscardOldPolicy", policy);
    }

    @Test
    public void testCustomDirectCall(){
        List<String> list  = new ArrayList<String>();
        list.add("//input[@title='Google Search']");
        gsm.customDirectCall("click", list.toArray());
    }

    @Test
    public void testDump(){
        gsm.dump("Google");
    }

    @AfterClass
    public static void tearDown(){
        showTrace();
    }
}
