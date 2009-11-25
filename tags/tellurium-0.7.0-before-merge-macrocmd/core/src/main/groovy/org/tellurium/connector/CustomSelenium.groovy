package org.tellurium.connector

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.CommandProcessor
import org.tellurium.exception.*
import org.tellurium.grid.GridSupport
import org.tellurium.i18n.InternationalizationManager;


/**
 * Customize Selenium RC so that we can add custom methods to Selenium RC
 * Added Selenium Grid support.
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 * @author Haroon Rasheed (haroonzone@gmail.com)
 *
 * Date: Oct 21, 2008
 * 
 */
class CustomSelenium extends DefaultSelenium {

    protected InternationalizationManager i18nManager = new InternationalizationManager();

    protected CustomCommand customClass = null
    protected String userExtension = null

    CustomSelenium(String host, int port, String browser, String url){
      super(host, port, browser, url)      
    }

    CustomSelenium(CommandProcessor commandProcessor) {
      super (commandProcessor)
    }

    public void setUserExt(String userExt){
      this.userExtension = userExt
    }
  
    protected void passCommandProcessor(CommandProcessor commandProcessor){
      if(customClass != null){
        customClass.setProcessor(this.commandProcessor) 
      }
    }

    protected def methodMissing(String name, args) {

         if(customClass != null && customClass.metaClass.respondsTo(customClass, name, args)){
              return customClass.invokeMethod(name, args)
         }

        throw new MissingMethodException(name, CustomSelenium.class, args)
    }
  
    // Added for the selenium grid support.
    // Start the selenium session specified by the arguments.
    // and register the selenium rc with Selenium HUB
    def void startSeleniumSession(String host, int port, String browser, String url) throws Exception{
      try{
        GridSupport.startSeleniumSession(host, port, browser, url)
      }catch (Exception e){
    	  
        throw new TelluriumException (i18nManager.translate("CustomSelenium.CannotStartSelenium" , e.getMessage()))
      }
    }

    def void startSeleniumSession(String host, int port, String browser, String url, String options) throws Exception{
      try{
        GridSupport.startSeleniumSession(host, port, browser, url, options)
      }catch (Exception e){
        throw new TelluriumException (i18nManager.translate("CustomSelenium.CannotStartSelenium" , e.getMessage()))
      }
    }

    // Close the selenium session and unregister the Selenium RC
    // from Selenium Hub
    def void closeSeleniumSession() throws Exception{
      try{
        GridSupport.closeSeleniumSession()
      }catch (Exception e){
    	  
        throw new TelluriumException (i18nManager.translate("CustomSelenium.CannotCloseSelenium" , e.getMessage()))        
      }


    }

    // Get the active Selenium RC session
    def CustomSelenium getActiveSeleniumSession(){
//      DefaultSelenium sel =  com.thoughtworks.selenium.grid.tools.ThreadSafeSeleniumSessionStorage.session()
//      CommandProcessor processor = sel.commandProcessor
//      CustomSelenium csel = new CustomSelenium(processor)
      
      CustomSelenium csel = GridSupport.session()
/*
      if(this.userExtension != null && this.userExtension.trim().length() > 0){
        File userExt = new File(this.userExtension);
//        processor.setExtensionJs(userExt.getAbsolutePath())
        processor.setExtensionJs(this.userExtension)
        println "Add user-extensions.js found at given path: " + userExt.getAbsolutePath() + " to Command Processor";
      }
 */
      if(csel != null){
        csel.customClass = this.customClass
        csel.passCommandProcessor(csel.commandProcessor)        
      }
//      csel.passCommandProcessor(processor)

      return csel
    }

    /*Please add custom methods here for Selenium RC after you add user extension to Selenium Core

   For instance,

       public void typeRepeated(String locator, String text) {

         commandProcessor.doCommand("typeRepeated", new String[]{locator, text});

       }
    */

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

    def Number getListSize(String locator, String separators){
      String[] arr = [locator, separators];
      Number num = commandProcessor.getNumber("getListSize", arr);
      return num;
    }

    def boolean isDisabled(String locator){
      String[] arr = [locator];
      boolean result = commandProcessor.getBoolean("isDisabled", arr);
      return result;
    }

    public void enableSelectorCache(){
      String[] arr = [];
      commandProcessor.doCommand("enableCache", arr);
    }

    public void disableSelectorCache(){
      String[] arr = [];
      commandProcessor.doCommand("disableCache",  arr);
    }

    public void cleanSelectorCache(){
      String[] arr = [];
      commandProcessor.doCommand("cleanCache", arr);
    }
  
    def boolean getCacheState(){
      String[] arr = [];
      boolean result = commandProcessor.getBoolean("getCacheState", arr);
      return result;
    }

    public void setCacheMaxSize(int size){
//      String[] arr = [Integer.toString(size)];
      String[] arr = [size];
      commandProcessor.doCommand("setCacheMaxSize",  arr);
    }

    public Number getCacheSize(){
    	String[] arr = [];
        Number num = commandProcessor.getNumber("getCacheSize", arr);
		return num;
    }

    public Number getCacheMaxSize(){
    	String[] arr = [];
        Number num = commandProcessor.getNumber("getCacheMaxSize", arr);
		return num;
    }

    public String getCacheUsage(){
       	String[] arr = [];
		String st = commandProcessor.doCommand("getCacheUsage", arr);
		return st;
    }

    public void addNamespace(String prefix, String namespace){
       String[] arr = [prefix, namespace];
       commandProcessor.doCommand("addNamespace", arr);
    }

    public String getNamespace(String prefix){
       String[] arr = [prefix];
       String st = commandProcessor.getString("getNamespace", arr);

       return st;
    }

    public void useDiscardNewCachePolicy(){
        String[] arr = [];
        commandProcessor.doCommand("useDiscardNewPolicy", arr);
    }

    public void useDiscardOldCachePolicy(){
        String[] arr = [];
        commandProcessor.doCommand("useDiscardOldPolicy", arr);
    }

    public void useDiscardLeastUsedCachePolicy(){
        String[] arr = [];
        commandProcessor.doCommand("useDiscardLeastUsedPolicy", arr);
    }
  
    public void useDiscardInvalidCachePolicy(){
        String[] arr = [];
        commandProcessor.doCommand("useDiscardInvalidPolicy", arr);
    }

    public String getCurrentCachePolicy(){
        String[] arr = [];
        return commandProcessor.getString("getCachePolicyName", arr);
    }

    public void typeKey(String locator, String key){
        String[] arr = [locator, key];
        commandProcessor.doCommand("typeKey", arr);
    }

    public void triggerEvent(String locator, String event){
        String[] arr = [locator, event];
        commandProcessor.doCommand("triggerEvent", arr);      
    }

    public String diagnose(String locator, String request){
		String[] arr = [locator, request];
		String st = commandProcessor.doCommand("getDiagnosisResponse", arr);
		return st;
    }

    public void deleteAllCookies(){
        String[] arr = [];
        commandProcessor.doCommand("deleteAllCookies", arr);
    }
}