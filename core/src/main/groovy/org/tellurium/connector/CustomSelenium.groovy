package org.tellurium.connector

import com.thoughtworks.selenium.DefaultSelenium

/**
 * Customize Selenium RC so that we can add custom methods to Selenium RC
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Oct 21, 2008
 * 
 */
class CustomSelenium extends DefaultSelenium {
    
    CustomSelenium(String host, int port, String browser, String url) {
        super(host, port, browser, url)
    }

    /*Please add custom methods here for Selenium RC after you add user extension to Selenium Core

   For instance,

       public void typeRepeated(String locator, String text) {

         commandProcessor.doCommand("typeRepeated", new String[]{locator, text});

       }
    */
  
	def String getSelectorProperties(String jqSelector, String props){
		String[] arr = [jqSelector, props];
		String st = commandProcessor.doCommand("getSelectorProperties", arr);
		return st;
	}

	def String getSelectorText(String jqSelector){
		String[] arr = [jqSelector];
		String st = commandProcessor.doCommand("getSelectorText", arr);
		return st;
	}

	def String getSelectorFunctionCall(String jqSelector, String args){
		String[] arr = [jqSelector, args];
		String st = commandProcessor.doCommand("getSelectorFunctionCall", arr);
		return st;
	}

    def String getAllText(String locator){
		String[] arr = [locator];
		String st = commandProcessor.doCommand("getAllText", arr);
		return st;
	}

    def String getCSS(String locator, String cssName){
		String[] arr = [locator, cssName];
		String st = commandProcessor.doCommand("getCSS", arr);
		return st;
	}

    def Number getJQuerySelectorCount(String locator){
		String[] arr = [locator];
		Number num = commandProcessor.getNumber("getJQuerySelectorCount", arr);
		return num;
	}

    def boolean isDisabled(String locator){
      String[] arr = [locator];
      boolean result = commandProcessor.getBoolean("isDisabled", arr);
      return result;
    }

    public void useSelectorCache(){
      String[] arr = [];
      commandProcessor.doCommand("doUseCache", arr);
    }

    public void disableSelectorCache(){
      String[] arr = [];
      commandProcessor.doCommand("doDisableCache", arr);
    }

    def boolean getCacheState(){
      String[] arr = [];
      boolean result = commandProcessor.getBoolean("getCacheState", arr);
      return result;
    }
}