package org.telluriumsource.test;

import org.telluriumsource.module.TelluriumIssuesPage;
import org.telluriumsource.test.java.TelluriumTestNGTestCase;
import org.telluriumsource.util.Helper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Test case for Tellurium project issues page
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 * @author Haroon Rasheed (haroonzone@gmail.com)
 *
 *         Date: Jan 18, 2010
 */
public class TelluriumIssuesPageTestNGTestCase extends TelluriumTestNGTestCase {
    private static TelluriumIssuesPage tisp;

    @BeforeClass
    public static void initUi() {
        tisp = new TelluriumIssuesPage();
        tisp.defineUi();
        useCssSelector(false);
        connectSeleniumServer();
    }

    @BeforeMethod
    public void setUpForMethod(){
        connectUrl("http://code.google.com/p/aost/issues/list");
    }


    @Test
    public void testGetIssueTypes(){

        String[] ists = tisp.getIsssueTypes();
        assertNotNull(ists);
        assertTrue(ists[2].contains("Open issues"));
        tisp.selectIssueType(ists[2]);
        tisp.searchIssue("Alter");
    }

    @Test
    public void testAdvancedSearch(){
        String[] ists = tisp.getAdvancedIsssueTypes();
        assertNotNull(ists);
        assertTrue(ists[1].contains("All issues"));
        tisp.selectIssueType(ists[1]);

        tisp.advancedSearchIssue(ists[1], "table", null, null, null, null, null, null, null);
    }

    @Test
    public void testAdvancedSearchTips(){
        tisp.clickMoreSearchTips();
    }

    @Test
    public void testIssueData(){
        int mcolumn = tisp.getTableHeaderNum();
        assertEquals(10, mcolumn);
        List<String> list = tisp.getHeaderNames();
        assertNotNull(list);
        assertEquals(10, list.size());
        assertTrue(Helper.include(list, "Status"));
        list = tisp.getDataForColumn(7);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(Helper.include(list, "John.Jian.Fang"));
    }

    @Test
    public void testClickIssueResult(){
        tisp.clickTable(1,2);
        connectUrl("http://code.google.com/p/aost/issues/list");
        tisp.clickTable(1,3);
        connectUrl("http://code.google.com/p/aost/issues/list");
        tisp.clickTable(1,4);
        connectUrl("http://code.google.com/p/aost/issues/list");
        tisp.clickTable(1,5);
        connectUrl("http://code.google.com/p/aost/issues/list");
        tisp.clickTable(1,6);
        connectUrl("http://code.google.com/p/aost/issues/list");
        tisp.clickTable(1,7);
        connectUrl("http://code.google.com/p/aost/issues/list");
        tisp.clickTable(1,9);
    }

    @Test
    public void testClickHeader(){
        tisp.clickOnTableHeader(2);
        tisp.clickOnTableHeader(3);
        tisp.clickOnTableHeader(4);
        tisp.clickOnTableHeader(5);
        tisp.clickOnTableHeader(6);
        tisp.clickOnTableHeader(7);
        tisp.clickOnTableHeader(9);
    }

    @Test
    public void testIdMenu(){
        useCssSelector(false);
        tisp.clickOnTableHeader(2);
        tisp.mouseMoveIdMenu();
        tisp.clickIdMenuSortDown();
        tisp.clickIdMenuSortUp();
    }

    @Test
    public void testSelectColumnMenu(){
        useCssSelector(false);
        tisp.toggleIdColumn("ID");
        tisp.toggleIdColumn("Owner");
    }

    @Test
    public void testSelectDataLayout(){
        tisp.selectDataLayout("Grid");
        tisp.selectDataLayout("List");
    }

    @Test
    public void testGetCellCount(){
        useCssSelector(true);
        int count = tisp.getTableCellCount();
        assertTrue(count > 0);
        System.out.println("Cell size: " + count);
        String[] details = tisp.getAllText();
        assertNotNull(details);
        assertEquals(details.length, count);
    }

    @Test
    public void testSearchIssueTypes(){
        useCssSelector(true);
        useCache(true);
        setCacheMaxSize(10);
        String[] ists = tisp.getIsssueTypes();
        tisp.selectIssueType(ists[2]);
        tisp.searchIssue("Alter");
        showCacheUsage();
    }

    @Test
    public void testDump(){
        useCssSelector(false);
        tisp.dump("issueAdvancedSearch");

        useCssSelector(true);
        useCache(false);
        tisp.dump("issueAdvancedSearch");

        useCssSelector(true);
        useCache(true);
        tisp.dump("issueAdvancedSearch");
    }

    protected void showCacheUsage(){
        int size = getCacheSize();
        int maxSize = getCacheMaxSize();
        System.out.println("Cache Size: " + size + ", Cache Max Size: " + maxSize);
        System.out.println("Cache Usage: " + getCacheUsage());
    }
}
