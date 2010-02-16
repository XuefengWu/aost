package module

import org.telluriumsource.dsl.DslContext

/**
 * This UI module file is automatically generated by TrUMP 0.1.0
 * for the Google Search module at
 *
 *  www.google.com
 * 
 */

public class GoogleSearchModule extends DslContext {
  public void defineUi() {
    ui.Image(uid: "Logo", clocator: [tag: "img", src: "*.gif"])

    ui.Container(uid: "Google", clocator: [tag: "table"]) {
      InputBox(uid: "Input", clocator: [tag: "input", title: "Google Search", name: "q"])
      SubmitButton(uid: "Search", clocator: [tag: "input", type: "submit", value: "Google Search", name: "btnG"])
      SubmitButton(uid: "ImFeelingLucky", clocator: [tag: "input", type: "submit", value: "I'm Feeling Lucky", name: "btnI"])
    }

    ui.Container(uid: "ProblematicGoogle", clocator: [tag: "table"]) {
      InputBox(uid: "Input", clocator: [tag: "input", title: "Google Search", name: "p"])
      SubmitButton(uid: "Search", clocator: [tag: "input", type: "submit", value: "Google Search", name: "btns"])
      SubmitButton(uid: "ImFeelingLucky", clocator: [tag: "input", type: "submit", value: "I'm Feeling Lucky", name: "btnf"])
    }
  }

  public void doProblematicGoogleSearch(String input) {
    keyType "ProblematicGoogle.Input", input
    pause 500
    click "ProblematicGoogle.Search"
    waitForPageToLoad 30000
  }
  
  public void doGoogleSearch(String input) {
    type "Google.Input", input
    pause 500
    click "Google.Search"
    waitForPageToLoad 30000
  }

  public void doImFeelingLucky(String input) {
    keyType "Google.Input", input
    pause 500
    click "Google.ImFeelingLucky"
    waitForPageToLoad 30000
  }

  //Test jQuery selector for attributes
  public String getLogoAlt() {
    return getImageAlt("Logo")
  }

  boolean isInputDisabled() {
    return isDisabled("Google.Input")
  }

}
