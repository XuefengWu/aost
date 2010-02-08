package aost.builder

import aost.object.UrlLink
import aost.locator.BaseLocator

/**
 *    URL Link builder
 *
 *    @author Jian Fang (John.Jian.Fang@gmail.com)
 */
class UrlLinkBuilder extends UiObjectBuilder{

    public build(Map map, Closure c) {
        //add default parameters so that the builder can use them if not specified
        def df = [:]
        df.put(TAG, UrlLink.TAG)
        UrlLink link = this.internBuild(new UrlLink(), map, df)

        return link
    }
}