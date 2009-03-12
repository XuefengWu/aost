package module

import org.tellurium.dsl.DslContext

/**
 * This UI module file is automatically generated by TrUMP 0.1.0
 * for the Google Search module at
 *
 *  www.google.com
 * 
 */

public class GoogleSearchModule extends DslContext {

  public void defineUi() {
    ui.Container(uid: "Google", clocator: [tag: "table"]) {
      InputBox(uid: "Input", clocator: [tag: "input", title: "Google Search", name: "q"])
      SubmitButton(uid: "Search", clocator: [tag: "input", type: "submit", value: "Google Search", name: "btnG"])
      SubmitButton(uid: "ImFeelingLucky", clocator: [tag: "input", type: "submit", value: "I'm Feeling Lucky", name: "btnI"])
    }
  }

  public void doGoogleSearch(String input) {
    keyType "Google.Input", input
    pause 500
    click "Google.Search"
    waitForPageToLoad 30000
  }

  public void doImFeelingLucky(String input) {
    type "Google.Input", input
    pause 500
    click "Google.ImFeelingLucky"
    waitForPageToLoad 30000
  }

}
