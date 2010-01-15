package org.telluriumsource.ut

import org.telluriumsource.dsl.DslContext

/**
 * example standard table for testing purpose
 *
 * @author John.Jian.Fang@gmail.com
 * 
 * Date: Mar 6, 2009
 *
 */

public class StandardTable1 extends DslContext {
  public void defineUi() {
    ui.StandardTable(uid: "table1", clocator: [id: "std"]) {
      UrlLink(uid: "header: 2", clocator: [text: "%%Filename"])
      UrlLink(uid: "header: 3", clocator: [text: "%%Uploaded"])
      UrlLink(uid: "header: 4", clocator: [text: "%%Size"])
      TextBox(uid: "header: all", clocator: [:])

      Selector(uid: "tbody: 1, row:1, column: 3", clocator: [name: "can"])
      SubmitButton(uid: "tbody: 1, row:1, column:4", clocator: [value: "Search", name: "btn"])
      InputBox(uid: "tbody: 1, row:2, column:3", clocator: [name: "words"])
      InputBox(uid: "tbody: 2, row:2, column:3", clocator: [name: "without"])
      InputBox(uid: "tbody: 2, row:*, column:1", clocator: [name: "labels"])

      TextBox(uid: "foot: all", clocator: [tag: "td"])
    }

    ui.StandardTable(uid: "table2", clocator: [id: "normal"]) {
      Selector(uid: "row: 1, column: 1", clocator: [name: "can", id: "can"])
      TextBox(uid: "row: 2, column: 1", clocator: [tag: "span"])
      InputBox(uid: "row: 3, column: 1", clocator: [name: "q"])
      SubmitButton(uid: "row: 4, column: 1", clocator: [value: "Search"])
    }
  }

  public String getTableLocator(String uid) {
    return getXPath(uid)
  }
}