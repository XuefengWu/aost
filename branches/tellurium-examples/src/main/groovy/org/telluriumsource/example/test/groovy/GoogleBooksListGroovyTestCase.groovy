package org.telluriumsource.example.test.groovy

import org.telluriumsource.example.google.GoogleBooksList
import org.telluriumsource.example.google.NewGoogleBooksList
import org.telluriumsource.ui.object.UiObject
import org.telluriumsource.ui.object.UrlLink
import org.telluriumsource.test.groovy.TelluriumGroovyTestCase

/**
 * 
 * Sample test cases for Google book list page at:
 *
 *        http://books.google.com/
 *   to demostrate the List UI object
 *
 *   @author Jian Fang (John.Jian.Fang@gmail.com)
 */
class GoogleBooksListGroovyTestCase extends TelluriumGroovyTestCase {

    public void initUi() {
    }

    public void setUp(){
        setUpForClass()
    }

    public void tearDown(){
        tearDownForClass()
    }

    void testBookCategory(){
        //test google book list using Selenium way, i.e., absolute xpath
        GoogleBooksList gbl = new GoogleBooksList()
    	openUrl("http://books.google.com/")

        gbl.defineUi()
        bookListTest(gbl)

        //test google book list using composite locators
        NewGoogleBooksList ngbl = new NewGoogleBooksList()
        ngbl.defineUi()
        bookListTest(ngbl)
    }

    void testGetListSize(){
        NewGoogleBooksList ngbl = new NewGoogleBooksList()
        ngbl.defineUi()
        openUrl("http://books.google.com/")
        ngbl.disableCssSelector()
        int xsize = ngbl.getBookListSize()
        ngbl.enableCssSelector()
        int jsize = ngbl.getBookListSize()
        assertEquals(jsize, xsize)
    }

    void bookListTest(gbl){
    	
        String category = gbl.getCategory()
        assertEquals("� Fiction", category)

        int size = gbl.getBookListSize()

        assertEquals(8, size)

        List lst = gbl.getAllObjectInList()

        assertNotNull(lst)

        int index = -1
        String search =  "Poetry"
        int i = 1
        for(UiObject obj : lst){
            assertTrue(obj instanceof UrlLink)
            if(search.equals(gbl.getText(i))){
                index = i
                break
            }
            i++
        }

        gbl.clickList(index)
    }

    void testGetSeparatorAttribute(){
      NewGoogleBooksList ngbl = new NewGoogleBooksList()
      ngbl.defineUi()
      openUrl("http://books.google.com/")
      ngbl.dump("NGoogleBooksList")
      ngbl.disableCssSelector()
      println ngbl.getSeparatorAttribute()
      ngbl.enableCssSelector()
      ngbl.dump("NGoogleBooksList")
      println ngbl.getSeparatorAttribute()
    }
}