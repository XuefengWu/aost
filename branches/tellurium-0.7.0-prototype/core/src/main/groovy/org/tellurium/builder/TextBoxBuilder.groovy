package org.tellurium.builder

import org.tellurium.object.TextBox
import org.tellurium.locator.BaseLocator

/**
 *    Text Box builder
 *
 *    @author Jian Fang (John.Jian.Fang@gmail.com)
 */
class TextBoxBuilder extends UiObjectBuilder{

    public build(Map map, Closure c) {
       //add default parameters so that the builder can useString them if not specified
        def df = [:]
        TextBox textbox = this.internBuild(new TextBox(), map, df)

        return textbox  
    }
}