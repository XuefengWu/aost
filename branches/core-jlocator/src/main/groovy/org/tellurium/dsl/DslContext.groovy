package org.tellurium.dsl

import org.tellurium.widget.Widget

abstract class DslContext extends BaseDslContext{

//    def defUi = ui.&Container

    //Must implement this method to define UI
//    remove this constraint so that DSL script does not need to define this method
//    public abstract void defineUi()

	/**
	 * Pass in a jquery selector, and a list of DOM properties to gather from each selected element.
	 * returns an arraylist of hashmaps with the requested properties as 'key->value'
	 */
	def ArrayList getSelectorProperties(String jqSelector, List<String> props){
		return accessor.getSelectorProperties(jqSelector, props);
	}
	/**
	 * pass in a jquery selector, and get back an arraylist of inner text of all elements selected,
	 * one string per element
	 */
	def ArrayList getSelectorText(String jqSelector){
		return accessor.getSelectorText(jqSelector);
	}

	/**
	 * pass in a jquery selector, and a javascript function as a string. the function will be called within
	 * the context of the wrapped set, ie, the wrapped set will be 'this' in the function.
	 * the function must return JSON
	 *
	 * NOTE: the function CAN NOT have any comments or you will get a syntax error inside of selenium core.
	 * NOTE: each line of the function must be ended with a semicolin ';'
	 */
	def Object getSelectorFunctionCall(String jqSelector, String fn){
		return accessor.getSelectorFunctionCall(jqSelector, fn);
	}

	def getWidget(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(!(obj instanceof Widget)){
             println "Warning, Ui object ${uid} is not a widget"
         }

         return obj
    }

    def onWidget(String uid, String method, Object[] args){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        def obj = ui.walkTo(context, uid)
        if (!(obj instanceof Widget)) {
            println "Error, Ui object ${uid} is not a widget"
            
            throw new RuntimeException("Ui object ${uid} is not a widget")
        } else {
            if (obj.metaClass.respondsTo(obj, method, args)) {

                return obj.invokeMethod(method, args)
            }else{

                throw new MissingMethodException(method, obj.metaClass.class, args)
            }
        }
    }

    def findObject(String uid){
        def obj = ui.findUiObjectFromRegistry(uid)
        if(obj == null)
            println("Cannot find UI Object ${uid}")
        return obj
    }

    protected String locatorMapping(WorkflowContext context, loc ){
        //get ui object's locator
        String locator = locatorProcessor.locate(loc)

        //get the reference locator all the way to the ui object
        if(context.getReferenceLocator() != null)
            locator = context.getReferenceLocator() + locator

		//MK: removed this for jquery support
		//make sure the xpath starts with "//"
        if(locator != null && (!locator.startsWith("//") && (!locator.startsWith("jquery=")))){
            locator = "/" + locator
        }

        return locator
    }

    def selectFrame(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.selectFrame() {String loc ->
            eventHandler.selectFrame(loc)
        }
    }

    def selectParentFrameFrom(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.selectParentFrame() {String loc ->
            eventHandler.selectFrame(loc)
        }
    }

    def selectTopFrameFrom(String uid) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.selectTopFrame() {String loc ->
            eventHandler.selectFrame(loc)
        }
    }

    boolean getWhetherThisFrameMatchFrameExpression(String uid, String target) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.getWhetherThisFrameMatchFrameExpression(target) {String loc ->
            accessor.getWhetherThisFrameMatchFrameExpression(loc, target)
        }
    }

    void waitForFrameToLoad(String uid, int timeout) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.waitForFrameToLoad(timeout) {String loc ->
            accessor.waitForFrameToLoad(loc, Integer.toString(timeout))
        }
    }

    def mouseOver(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.mouseOver(){ loc, String[] events ->
            String locator = locatorMapping(context, loc)
            eventHandler.mouseOver(locator, events)
        }
    }
	def mouseDown(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.mouseDown(){ loc, String[] events ->
            String locator = locatorMapping(context, loc)
            eventHandler.mouseDown(locator, events)
        }
    }
	def mouseUp(String uid){
		WorkflowContext context = WorkflowContext.getDefaultContext()
		ui.walkTo(context, uid)?.mouseUp(){ loc, String[] events ->
			String locator = locatorMapping(context, loc)
			eventHandler.mouseUp(locator, events)
		}
	}

	def openWindow(String uid, String url){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.openWindow(url) {String loc, String aurl ->
            eventHandler.openWindow(aurl, loc)
        }
    }

    def selectWindow(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.selectWindow() {String loc ->
            eventHandler.selectWindow(loc)
        }
    }

    def selectMainWindow(){
        eventHandler.selectWindow(null)
    }

    def selectParentWindow(){
        eventHandler.selectWindow(".")
    }
    
    def waitForPopUp(String uid, int timeout){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.waitForPopUp(timeout) {String loc ->
              accessor.waitForPopUp(loc, Integer.toString(timeout))
        }
    }
    
    boolean getWhetherThisWindowMatchWindowExpression(String uid, String target) {
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.getWhetherThisWindowMatchWindowExpression(target) {String loc ->
            accessor.getWhetherThisWindowMatchWindowExpression(loc, target)
        }
    }

    def windowFocus(){
       eventHandler.windowFocus()
    }

    def windowMaximize(){
       eventHandler.windowMaximize()
    }

    String getBodyText(){
        return accessor.getBodyText()
    }

    boolean isTextPresent(String pattern){
       return accessor.isTextPresent(pattern)
    }

    String getHtmlSource(){
        return accessor.getHtmlSource()
    }

    String getExpression(String expression){
        return accessor.getExpression(expression)
    }

    String getCookie(){
        return accessor.getCookie()
    }

    void runScript(String script){
        accessor.runScript(script)
    }

    void captureScreenshot(String filename){
        accessor.captureScreenshot(filename)
    }

    void chooseCancelOnNextConfirmation(){
        eventHandler.chooseCancelOnNextConfirmation()
    }

    void chooseOkOnNextConfirmation(){
        eventHandler.chooseOkOnNextConfirmation()
    }

    void answerOnNextPrompt(String answer){
        eventHandler.answerOnNextPrompt(answer)
    }

    void goBack(){
        eventHandler.goBack()
    }

    void refresh(){
        eventHandler.refresh()
    }

    boolean isAlertPresent(){
        return accessor.isAlertPresent()
    }

    boolean isPromptPresent(){
        return accessor.isPromptPresent()
    }

    boolean isConfirmationPresent(){
        return accessor.isConfirmationPresent()
    }

    String getAlert(){
        return accessor.getAlert()
    }

    String getConfirmation(){
        return accessor.getConfirmation()
    }

    String getPrompt(){
        return accessor.getPrompt()
    }

    String getLocation(){
        return accessor.getLocation()
    }

    String getTitle(){
        return accessor.getTitle()
    }
    String[] getAllButtons(){
       return accessor.getAllButtons()
    }

    String[] getAllLinks(){
       return accessor.getAllLinks()
    }

    String[] getAllFields(){
        return accessor.getAllFields()
    }

    String[] getAllWindowIds(){
        return accessor.getAllWindowIds()
    }

    String[] getAllWindowNames(){
        return accessor.getAllWindowNames()
    }

    String[] getAllWindowTitles(){
        return accessor.getAllWindowTitles()
    }

    //let the missing property return the a string of the properity, this is useful for the onWidget method
    //so that we can pass in widget method directly, instead of passing in the method name as a String
    def propertyMissing(String name) {
        println "Warning: property ${name} is missing, treat it as a String. "
        name
    }
}