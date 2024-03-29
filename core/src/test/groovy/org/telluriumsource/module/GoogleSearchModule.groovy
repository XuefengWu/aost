package org.telluriumsource.module

import org.telluriumsource.dsl.DslContext

public class GoogleSearchModule extends DslContext {

  public void defineUi() {

    ui.Container(uid: "Sftab", clocator: [tag: "table", id: "sftab"]) {
      Container(uid: "Table", clocator: [tag: "table"]) {
        InputBox(uid: "Q", clocator: [tag: "input", type: "text", title: "Search", name: "q", class: "lst"])
        InputBox(uid: "Grey", clocator: [tag: "input", type: "text", id: "grey", class: "lst"])
      }
      SubmitButton(uid: "Search", clocator: [tag: "input", type: "submit", value: "Search", class: "lsb", name: "btnG"])
    }

//    ui.Image(uid: "Logo", clocator: [tag: "img", src: "*/intl/en_ALL/images/logo.gif"])
      ui.Image(uid: "Logo", clocator: [tag: "img", src: "*.gif", alt: "Google"])
//    ui.Image(uid: "Logo", clocator: [tag: "img", alt: "Google"])

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
    keyType "Google.Input", input
    pause 500
    click "Google.Search"
    waitForPageToLoad 30000
  }

  public void doKeyType(def input) {
    clearText "Google.Input"
    keyType "Google.Input", input
    pause 500
  }

  public void doType(def input) {
    clearText "Google.Input"
    type "Google.Input", input
    pause 500
  }

  public void doImFeelingLucky(String input) {
    type "Google.Input", input
    pause 500
    click "Google.ImFeelingLucky"
    waitForPageToLoad 30000
  }

  //Test jQuery selector for attributes
  public String getLogoAlt(){
    return getImageAlt("Logo")
  }

  boolean isInputDisabled() {
    return isDisabled("Google.Input")
  }

  public void doTypeRepeated(String input){
    customUiCall "Google.Input", typeRepeated, input
    pause 500
    click "Google.Search"
    waitForPageToLoad 30000
  }
}