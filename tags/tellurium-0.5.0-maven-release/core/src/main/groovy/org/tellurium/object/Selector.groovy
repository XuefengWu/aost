package org.tellurium.object

/**
 *  Selector
 *
 *  @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 */
class Selector extends UiObject {
    public static final String TAG = "select"

    def selectByLabel(String target, Closure c){
        c(locator, "label=${target}", respondToEvents)
    }

    def selectByValue(String value, Closure c){

        c(locator, "value=${value}", respondToEvents)()
    }

    def addSelectionByLabel(String target, Closure c){
         c(locator, "label=${target}")
    }

    def addSelectionByValue(String value, Closure c){
          c(locator, "value=${value}")
    }

    def removeSelectionByLabel(String target, Closure c){
          c(locator, "label=${target}")
    }

    def removeSelectionByValue(String value, Closure c){
        c(locator, "value=${value}")
    }    

    def removeAllSelections(Closure c){
         c(locator)
    }

    String[] getSelectOptions(Closure c){
        c(locator)
    }

    String[] getSelectedLabels(Closure c){
        c(locator)
    }

    String getSelectedLabel(Closure c){
        c(locator)
    }

    String[] getSelectedValues(Closure c){
        c(locator)
    }

    String getSelectedValue(Closure c){
         c(locator)
    }

    String[] getSelectedIndexes(Closure c){
         c(locator)
    }

    String getSelectedIndex(Closure c){
         c(locator)
    }

    String[] getSelectedIds(Closure c){
         c(locator)
    }

    String getSelectedId(Closure c){
         c(locator)
    }

    boolean isSomethingSelected(Closure c){
         c(locator)
    }
}