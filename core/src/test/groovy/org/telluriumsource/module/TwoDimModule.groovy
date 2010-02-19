package org.telluriumsource.module

import org.telluriumsource.dsl.DslContext

/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Feb 19, 2010
 * 
 */
class TwoDimModule  extends DslContext {
  public void defineUi(){
    ui.StandardTable(uid: "Table", clocator: [id: "table"], bt: "div", brt: "div", bct: "div"){
      TextBox(uid: "tbody: 1, row: *, column: 1", clocator: [tag: "div"])
      Image(uid: "tbody: 1, row: *, column: 2", clocator: [:])
      TextBox(uid: "tbody: 2, row: *, column: 1", clocator: [tag: "div"])
      TextBox(uid: "tbody: 2, row: *, column: 2", clocator: [tag: "div"])
      TextBox(uid: "tbody: 2, row: *, column: 3", clocator: [tag: "div"])
    }
  }
}
