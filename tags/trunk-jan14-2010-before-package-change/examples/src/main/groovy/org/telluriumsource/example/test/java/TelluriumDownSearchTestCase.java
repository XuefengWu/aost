package org.telluriumsource.example.test.java;

import org.telluriumsource.test.java.TelluriumJavaTestCase;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.telluriumsource.example.other.TelluriumDownSearchModule;

public class TelluriumDownSearchTestCase extends TelluriumJavaTestCase {
    private static TelluriumDownSearchModule app;

    @BeforeClass
    public static void initUi() {
        app = new TelluriumDownSearchModule();
        app.defineUi();
        app.enableCssSelector();
//        app.enableCache();
    }

    @Before
    public void setUpForTest() {
        connectUrl("http://code.google.com/p/aost/downloads/list");
    }

    @Test
    public void testDownloadSearch() {
        String[] allTypes = app.getAllDownloadTypes();
        assertNotNull(allTypes);
        assertTrue(allTypes[1].contains("All downloads"));
        app.selectDownloadType(allTypes[1]);
        app.searchDownload("TrUMP");
    }

    @Ignore
    @Test
    public void testHelp(){
        app.clickHelp();    
    }

    @Test
    public void testCSS(){
        String[] css = app.getTableCSS("font-size");
        assertNotNull(css);
    }
}