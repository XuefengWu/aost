package telluriumworks

import java.awt.Color
import org.jdesktop.swingx.painter.GlossPainter
import net.miginfocom.swing.MigLayout
import javax.swing.JTabbedPane
import static javax.swing.SwingConstants.*

gloss = glossPainter(paint: new Color(1f, 1f, 1f, 0.2f),
        position: GlossPainter.GlossPosition.TOP)
stripes = pinstripePainter(paint: new Color(1f, 1f, 1f, 0.17f),
        spacing: 5f)
matte = mattePainter(fillPaint: new Color(51, 51, 51))
compound = compoundPainter(painters: [matte, stripes, gloss])
actions {
  action(id: 'openAction',
          name: 'Open',
          mnemonic: 'O',
          accelerator: shortcut('O'),
          closure: controller.openFile)
  action(id: 'quitAction',
          name: 'Quit',
          mnemonic: 'Q',
          accelerator: shortcut('Q'),
          closure: controller.quit)
  action(id: 'goMainPage',
          shortDescription: "Go to main page",
          smallIcon: crystalIcon(size: 32, category: "apps", icon: "kcmdf"),
          closure: controller.goMainPage)
  action(id: 'goServerPage',
          shortDescription: "Go to Selenium server page",
          smallIcon: crystalIcon(size: 32, category: "apps", icon: "tutorials"),
          closure: controller.goServerPage)
  action(id: 'goConfigPage',
          shortDescription: "Go to Configuration page",
          smallIcon: crystalIcon(size: 32, category: "apps", icon: "kdmconfig"),
          closure: controller.goConfigPage)
  action(id: 'exitAction',
          shortDescription: "Exit the IDE",
          smallIcon: crystalIcon(size: 32, category: "actions", icon: "exit"),
          closure: controller.quit)
}

fileChooserWindow = fileChooser()
fileViewerWindow = application(title: 'TelluriumWorks',
        size: [480, 320], locationByPlatform: true,
        iconImage: imageIcon('/tellurium.png').image,
        iconImages: [imageIcon('/tellurium.png').image]) {
  lookAndFeel('system')

  menuBar {
    menu('File') {
      menuItem openAction
      separator()
      menuItem quitAction
    }
  }

  borderLayout()

  tabbedPane(constraints: CENTER, tabPlacement: JTabbedPane.LEFT) {

    panel(title: "Script", id: 'tabGroup', layout: new MigLayout('fill'), border: titledBorder('Tellurium Script'), constraints: "grow 100 1, wrap") {
      vbox() {
        panel(constraints: CENTER) {
          gridLayout(cols: 1, rows: 1)
          button("Choose a file...",
                  enabled: bind { model.enabled },
                  actionPerformed: {controller.openFile}
          )
        }
        scrollPane(constraints: CENTER) {
          textArea(id: "editor",
                  editable: false,
                  lineWrap: true,
                  text: bind { model.fileText },
                  caretPosition: bind(source: model, sourceProperty: "text")
          )
        }

        panel(constraints: CENTER, border: titledBorder('Console')) {
          button(id: 'clearConsole',
                  label: "Clear",
                  actionPerformed: {this.consoleTxt.text = ""},
                  constraints: "right,width 10px ,shrinkx,shrinky,wrap"
          )
          scrollPane(constraints: 'growx,width 100%,height 80%,span') {
            textArea(id: "consoleTxt",
                    editable: false,
                    lineWrap: true,
                    wrapStyleWord: true)
          }
        }
      }

    }

    panel(title: "Server", layout: new MigLayout('fill'), border: titledBorder('Tellurium Server'), constraints: 'grow, wrap') {
      gridLayout(cols: 1, rows: 4)
      hbox() {

        comboBox(id: 'serverType',
                items: ["Local", "Remote"],
                selectedIndex: 0,
                constraints: "growx,width 20px"
        )

        hbox() {
          label(id: 'portlbl',
                  text: "Port:",
                  constraints: "shrinkx,height 25px,width 20px"
          )

          textField(id: 'portValue',
                  constraints: "shrink,height 25px,width 50px"
          )

        }
      }

      hbox() {
        label(id: 'profilelbl',
                text: "Profile:",
                constraints: "shrinkx,height 25px,width 20px"
        )

        textField(id: 'profileValue',
                constraints: "shrink,height 25px,width 50px"
        )
      }

      checkBox(id: "multipleWindow", selected: false, text: "Multiple Windows", constraints: 'wrap')

      hbox(constraints: 'center') {
        button(id: 'runBtn',
                label: "Run",
                actionPerformed: {controller.runSeleniumServer()},
                constraints: "width 20px,shrinkx,wrap"
        )

        button(id: 'stopBtn',
                label: "Stop",
                actionPerformed: {controller.stopSeleniumServer()},
                constraints: "width 20px,shrinkx,wrap"
        )
      }
    }

    panel(title: "Config", layout: new MigLayout('fill'), border: titledBorder('Tellurium Configuration'), constraints: 'grow') {
      gridLayout(cols: 1, rows: 7)

      hbox() {
        label(id: 'browserlbl',
                text: "Browser:",
                constraints: "shrinkx,height 25px,width 20px"
        )
        comboBox(id: 'browserType',
                items: ["*chrome", "*firefox", "*iexplore", "*iehta"],
                selectedIndex: 0,
                constraints: "growx,width 20px"
        )
      }

      hbox() {
        hbox() {
          label(id: 'selServerlbl',
                  text: "Server Host:",
                  constraints: "shrinkx,height 25px,width 20px"
          )

          textField(id: 'selServerValue',
                  constraints: "shrink,height 25px,width 50px"
          )

        }

        hbox() {
          label(id: 'selPortlbl',
                  text: "Server Port:",
                  constraints: "shrinkx,height 25px,width 20px"
          )

          textField(id: 'selPortValue',
                  constraints: "shrink,height 25px,width 50px"
          )
        }

      }

      hbox() {
        label(id: 'maxMacroCmdbl',
                text: "Macro Command:",
                constraints: "shrinkx,height 25px,width 20px"
        )

        textField(id: 'macCmdValue',
                constraints: "shrink,height 25px,width 50px"
        )
      }

      hbox() {
        label(id: 'optiondbl',
                text: "option:",
                constraints: "shrinkx,height 25px,width 20px"
        )

        textField(id: 'optionValue',
                constraints: "shrink,height 25px,width 50px"
        )
      }

      checkBox(id: "useTrace", selected: false, text: "Trace", constraints: 'wrap')

      checkBox(id: "useScreenShot", selected: false, text: "ScreenShot", constraints: 'wrap')

      button(id: 'applyBtn',
              label: "Apply",
              actionPerformed: {controller.setTelluriumConfig()},
              constraints: "width 20px ,shrinkx,wrap"
      )

    }
  }

   toolBar(constraints: 'left', floatable: false, opaque: false) {
    button(goMainPage, constraints: 'left')
    button(goServerPage, constraints: 'left')
    button(goConfigPage, constraints: 'left')
    button(exitAction, constraints: 'left')
  } 
}
/*  panel(layout: new MigLayout('fill')) {
    panel(layout: new MigLayout('fill'), border: titledBorder('Tellurium Script Runner'), constraints: 'grow 100 1, wrap, height 70%') {

      hbox(constraints: "south") {

        tabbedPane(id: 'tabGroup', constraints: "grow 75 1, wrap")

        vbox {
          panel(layout: new MigLayout('fill'), border: titledBorder('Tellurium Server'), constraints: 'right, width 200:260:25%)') {
            gridLayout(cols: 1, rows: 4)
            hbox() {

              comboBox(id: 'serverType',
                      items: ["Local", "Remote"],
                      selectedIndex: 0,
                      constraints: "growx,width 20px"
              )

              hbox() {
                label(id: 'portlbl',
                        text: "Port:",
                        constraints: "shrinkx,height 25px,width 20px"
                )

                textField(id: 'portValue',
                        constraints: "shrink,height 25px,width 50px"
                )

              }
            }

            hbox() {
              label(id: 'profilelbl',
                      text: "Profile:",
                      constraints: "shrinkx,height 25px,width 20px"
              )

              textField(id: 'profileValue',
                      constraints: "shrink,height 25px,width 50px"
              )
            }

            checkBox(id: "multipleWindow", selected: false, text: "Multiple Windows", constraints: 'wrap')

            hbox(constraints: 'center') {
              button(id: 'runBtn',
                      label: "Run",
                      actionPerformed: {controller.runSeleniumServer()},
                      constraints: "width 20px,shrinkx,wrap"
              )

              button(id: 'stopBtn',
                      label: "Stop",
                      actionPerformed: {controller.stopSeleniumServer()},
                      constraints: "width 20px,shrinkx,wrap"
              )
            }
          }

          panel(layout: new MigLayout('fill'), border: titledBorder('Tellurium Configuration'), constraints: 'right, width 200:260:25%') {
            gridLayout(cols: 1, rows: 7)

            hbox() {
              label(id: 'browserlbl',
                      text: "Browser:",
                      constraints: "shrinkx,height 25px,width 20px"
              )
              comboBox(id: 'browserType',
                      items: ["*chrome", "*firefox", "*iexplore", "*iehta"],
                      selectedIndex: 0,
                      constraints: "growx,width 20px"
              )
            }

            hbox() {
              hbox() {
                label(id: 'selServerlbl',
                        text: "Server Host:",
                        constraints: "shrinkx,height 25px,width 20px"
                )

                textField(id: 'selServerValue',
                        constraints: "shrink,height 25px,width 50px"
                )

              }

              hbox() {
                label(id: 'selPortlbl',
                        text: "Server Port:",
                        constraints: "shrinkx,height 25px,width 20px"
                )

                textField(id: 'selPortValue',
                        constraints: "shrink,height 25px,width 50px"
                )
              }

            }

            hbox() {
              label(id: 'maxMacroCmdbl',
                      text: "Macro Command:",
                      constraints: "shrinkx,height 25px,width 20px"
              )

              textField(id: 'macCmdValue',
                      constraints: "shrink,height 25px,width 50px"
              )
            }

            hbox() {
              label(id: 'optiondbl',
                      text: "option:",
                      constraints: "shrinkx,height 25px,width 20px"
              )

              textField(id: 'optionValue',
                      constraints: "shrink,height 25px,width 50px"
              )
            }

            checkBox(id: "useTrace", selected: false, text: "Trace", constraints: 'wrap')

            checkBox(id: "useScreenShot", selected: false, text: "ScreenShot", constraints: 'wrap')

            button(id: 'applyBtn',
                    label: "Apply",
                    actionPerformed: {controller.setTelluriumConfig()},
                    constraints: "width 20px ,shrinkx,wrap"
            )

          }
        }

      }
    }

    panel(layout: new MigLayout('fill'), border: titledBorder('Console'), constraints: 'grow 100 1, wrap, height 30%') {
      button(id: 'clearConsole',
              label: "Clear",
              actionPerformed: {this.consoleTxt.text = ""},
              constraints: "right,width 10px ,shrinkx,shrinky,wrap"
      )
      scrollPane(constraints: 'growx,width 100%,height 80%,span') {
        textArea(id: "consoleTxt",
                editable: false,
                lineWrap: true,
                wrapStyleWord: true)
      }
    }
    toolBar(constraints: 'left', floatable: false, opaque: false) {
        button(goMainPage, constraints: 'left')
        button(goServerPage, constraints: 'left')
        button(goConfigPage, constraints: 'left')
        button(exitAction, constraints: 'left')
    }

  }
*/

/*    jxheader(constraints: NORTH,
   title: "Tellurium Works",
   description: "Tellurium IDE to run Tellurium DSL script",
   titleForeground: Color.WHITE,
   descriptionForeground: Color.WHITE,
   icon: imageIcon("/tellurium.png"),
   preferredSize: [480,80],
   backgroundPainter: compound
)
jxtaskPaneContainer(constraints: CENTER) {
   jxtaskPane(title: "Task group 1") {
       jxlabel("Action 1")
   }
   jxtaskPane(title: "Task group 2", expanded: false) {
       label("Action 2")
   }
} */

/*tabbedPane(tabPlacement: JTabbedPane.LEFT) {
  label('One', title:'One', tabToolTip:'Uno!')
  label('Green', title:'Green', tabBackground:java.awt.Color.GREEN)
//  label('Stop Operation', title:'Stop Operation', tabMnemonic:'O')
//  label('Stop Operation', title:'Stop Operation', tabDisplayedMnemonicIndex:5)
  panel(name:'One') {
        label('One')
  }
  panel(name:'Two') {
        label('Two')
  }

}*/
//}