package org.tellurium.dsl

import org.tellurium.dsl.UiDslParser
import org.tellurium.util.Helper
import org.tellurium.event.EventHandler
import org.tellurium.access.Accessor
import org.tellurium.locator.LocatorProcessor
import org.tellurium.object.Table
import org.tellurium.object.List

abstract class DslContext {
    UiDslParser ui = new UiDslParser()

    //decoupling eventhandler, locateProcessor, and accessor from UI objects
    //and let DslContext to handle all of them directly. This will also make
    //the framework reconfiguration much easier.
    EventHandler eventHandler = new EventHandler()
    Accessor accessor = new Accessor()
    LocatorProcessor locatorProcessor = new LocatorProcessor()

//    def defUi = ui.&Container

    //Must implement this method to define UI
//    remove this constraint so that DSL script does not need to define this method
//    public abstract void defineUi()

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

        //make sure the xpath starts with "//"
        if(locator != null && (!locator.startsWith("//"))){
            locator = "/" + locator
        }

        return locator
    }

    def mouseOver(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.mouseOver(){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.mouseOver(locator)
        }
    }

    def mouseOut(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.mouseOut(){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.mouseOut(locator)
        }
    }

    def click(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.click(){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.click(locator)
        }
    }

    def doubleClick(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.doubleClick(){ loc ->
           String locator = locatorMapping(context, loc)
            eventHandler.doubleClick(locator)
        }
    }

    def clickAt(String uid, String coordination){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.clickAt(coordination){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.clickAt(locator, coordination)
        }
    }

    def check(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.check(){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.check(locator)
        }
    }

    def uncheck(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.uncheck(){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.uncheck(locator)
        }
    }

    def type(String uid, String input){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.type(input){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.type(locator, input)
        }
    }

    def keyType(String uid, String input){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.keyType(input){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.keyType(locator, input)
        }
    }

    def typeAndReturn(String uid, String input){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.typeAndReturn(input){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.typeAndReturn(locator, input)
        }
    }

    def clearText(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.clearText(){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.clearText(locator)
        }
    }

    def select(String uid, String target){
        selectByLabel(uid, target)
    }

    def selectByLabel(String uid, String target){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.selectByLabel(target){ loc, optloc ->
            String locator = locatorMapping(context, loc)
            eventHandler.select(locator, optloc)
        }
    }

    def selectByValue(String uid, String target){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.selectByValue(target){ loc, optloc ->
            String locator = locatorMapping(context, loc)
            eventHandler.select(locator, optloc)
        }
    }

    def addSelectionByLabel(String uid, String target){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.addSelectionByLabel(target){ loc, optloc ->
            String locator = locatorMapping(context, loc)
            eventHandler.addSelection(locator, optloc)
        }
    }

    def addSelectionByValue(String uid, String target){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.addSelectionByValue(target){ loc, optloc ->
            String locator = locatorMapping(context, loc)
            eventHandler.addSelection(locator, optloc)
        }
    }

    def removeSelectionByLabel(String uid, String target){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.removeSelectionByLabel(target){ loc, optloc ->
            String locator = locatorMapping(context, loc)
            eventHandler.removeSelection(locator, optloc)
        }
    }

    def removeSelectionByValue(String uid, String target){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.removeSelectionByValue(target){ loc, optloc ->
            String locator = locatorMapping(context, loc)
            eventHandler.removeSelection(locator, optloc)
        }
    }

    def removeAllSelections(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.removeAllSelections(){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.removeAllSelections(locator)
        }
    }

    String[] getSelectOptions(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectOptions(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectOptions(locator)
             }
         }

        return null
    }

    String[] getSelectedLabels(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectedLabels(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectedLabels(locator)
             }
         }

        return null
    }

    String getSelectedLabel(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectedLabel(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectedLabel(locator)
             }
         }

        return null
    }

    String[] getSelectedValues(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectedValues(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectedValues(locator)
             }
         }

        return null
    }

    String getSelectedValue(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectedValue(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectedValue(locator)
             }
         }

        return null
    }

    String[] getSelectedIndexes(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectedIndexes(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectedIndexes(locator)
             }
         }

        return null
    }

    String getSelectedIndex(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectedIndex(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectedIndex(locator)
             }
         }

        return null
    }

    String[] getSelectedIds(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectedIds(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectedIds(locator)
             }
         }

        return null
    }

    String getSelectedId(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getSelectedId(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.getSelectedId(locator)
             }
         }

        return null
    }

    boolean isSomethingSelected(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.isSomethingSelected(){ loc ->
                String locator = locatorMapping(context, loc) 
                accessor.isSomethingSelected(locator)
             }
         }

        return false
    }

    String waitForText(String uid, int timeout){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.waitForText(timeout){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.waitForText(locator, timeout)
        }
    }

    int getTableHeaderColumnNum(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         Table obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getTableHeaderColumnNum{ loc ->
                String locator = locatorMapping(context, loc)
                locator
             }
         }

         println("Cannot find table ${uid}")
         return 0
    }

    int getTableMaxRowNum(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         Table obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getTableMaxRowNum(){ loc ->
                String locator = locatorMapping(context, loc)
                locator
             }
         }
         
         println("Cannot find table ${uid}")
         return 0
    }

    int getTableMaxColumnNum(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         Table obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getTableMaxColumnNum(){ loc ->
                String locator = locatorMapping(context, loc)
                locator
             }
         }

         println("Cannot find table ${uid}")
         return 0
    }

    int getListSize(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         List obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.getListSize(){ loc ->
                String locator = locatorMapping(context, loc)
                locator
             }
         }

         println("Cannot find list ${uid}")
         return 0
    }

    //uid should useString the format table2[2][3] for Table or list[2] for List
    def getUiElement(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)

         return obj
    }

    boolean isElementPresent(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.isElementPresent(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.isElementPresent(locator)
             }
         }

         return false
    }

    boolean isVisible(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.isVisible(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.isVisible(locator)
             }
         }

         return false
    }

    boolean isChecked(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.isChecked(){ loc ->
                String locator = locatorMapping(context, loc)
                accessor.isChecked(locator)
             }
         }
        
         throw RuntimeException("Cannot find UI Object ${uid})")
    }

    def isDisabled(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.isDisabled (){loc ->
                 String locator = locatorMapping(context, loc)
                 accessor.isDisabled(locator);
             }
         }
    }

    def isEnabled(String uid){
        return !isDisabled(uid);
    }


    boolean waitForElementPresent(String uid, int timeout){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.waitForElementPresent(timeout){ loc ->
                String locator = locatorMapping(context, loc) 
                accessor.waitForElementPresent(locator, timeout)
             }
         }

         return false
    }

    boolean waitForElementPresent(String uid, int timeout, int step){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         def obj = ui.walkTo(context, uid)
         if(obj != null){
             return obj.waitForElementPresent(timeout, step){ loc ->
                String locator = locatorMapping(context, loc) 
                accessor.waitForElementPresent(locator, timeout, step)
             }
         }

         return false
   }

    boolean waitForCondition(String script, int timeoutInMilliSecond){
       Accessor.waitForCondition(script, timeoutInMilliSecond)
    }

    String getText(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.getText(){ loc ->
            String locator = locatorMapping(context, loc)
            accessor.getText(locator)
        }
    }

    String getValue(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.getValue(){ loc ->
            String locator = locatorMapping(context, loc)
            accessor.getValue(locator)
        }
    }

    def pause(int milliseconds){
        Helper.pause(milliseconds)
    }

    String getLink(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         ui.walkTo(context, uid)?.getLink(){ loc, attr ->
            String locator = locatorMapping(context, loc)
            accessor.getAttribute(locator + attr )
        }
    }

    String getImageSource(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         ui.walkTo(context, uid)?.getImageSource(){ loc, attr ->
            String locator = locatorMapping(context, loc)
            accessor.getAttribute(locator + attr )
        }
    }

    String getImageAlt(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         ui.walkTo(context, uid)?.getImageAlt(){ loc, attr ->
            String locator = locatorMapping(context, loc)
            accessor.getAttribute(locator + attr )
        }
    }

    String getImageTitle(String uid){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         ui.walkTo(context, uid)?.getImageTitle(){ loc, attr ->
            String locator = locatorMapping(context, loc)
            accessor.getAttribute(locator + attr )
        }
    }

    def getAttribute(String uid, String attribute){
         WorkflowContext context = WorkflowContext.getDefaultContext()
         ui.walkTo(context, uid)?.getAttribute(attribute){ loc, attr ->
            String locator = locatorMapping(context, loc)
            accessor.getAttribute(locator + attr )
         }
    }

    def submit(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.submit(){ loc ->
            String locator = locatorMapping(context, loc)
            eventHandler.submit(locator)
        }
    }

    def openWindow(String url, String windowID){
        eventHandler.openWindow(url, windowID)
    }

    def selectWindow(String windowID){
        eventHandler.selectWindow(windowID)
    }

    def selectFrame(String locator){
        eventHandler.selectFrame(locator)
    }

    def waitForPopUp(String windowID, int timeout){
        accessor.waitForPopUp(windowID, Integer.toString(timeout))
    }

    String getBodyText(){
        return accessor.getBodyText()
    }

    boolean isTextPresent(String pattern){
       return accessor.isTextPresent(pattern)
    }

    boolean isEditable(String uid){
        WorkflowContext context = WorkflowContext.getDefaultContext()
        ui.walkTo(context, uid)?.isEditable(){ loc ->
            String locator = locatorMapping(context, loc)
            return accessor.isEditable(locator)
        }
        
        return false
    }

    String getHtmlSource(){
        return accessor.getHtmlSource()
    }

    String getExpression(String expression){
        return accessor.getExpression(expression)
    }

    Number getXpathCount(String xpath){
        return accessor.getXpathCount(xpath)
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

    void waitForPageToLoad(int timeout){
         accessor.waitForPageToLoad(Integer.toString(timeout))
    }

    void waitForFrameToLoad(String frameAddress, int timeout){
        accessor.waitForFrameToLoad(frameAddress, Integer.toString(timeout))
    }
}