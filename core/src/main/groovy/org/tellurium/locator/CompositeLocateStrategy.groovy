package org.tellurium.locator

import org.tellurium.locator.CompositeLocator
import org.tellurium.locator.XPathBuilder

/**
 *   Automcatically generate relate xpath based on composite elements in the
 *   UI object
 *
 *   @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 */
class CompositeLocateStrategy {

  def static boolean canHandle(locator) {
    if (locator instanceof CompositeLocator)
      return true
    else
      return false
  }

  public static String locate(CompositeLocator locator) {

//    String xpath = XPathBuilder.buildXPath(locator.tag, locator.text, locator.position, locator.attributes)
//    String xpath = XPathBuilder.buildOptionalXPath(locator.tag, locator.text, locator.position, locator.direct, locator.attributes)
    String xpath = XPathBuilder.buildOptionalXPathWithHeader(locator.tag, locator.text, locator.position, locator.direct, locator.attributes, locator.header)
//    if (locator.header != null && (locator.header.trim().length() > 0)) {
//      xpath = locator.header + xpath
//    }

    if (locator.trailer != null && (locator.trailer.trim().length() > 0)) {
      xpath = xpath + locator.trailer
    }

    return xpath
  }

  //use jQuery Selector instead of xpath
  public static String select(CompositeLocator locator){

    String jqsel = JQueryBuilder.buildJQuerySelector(locator.tag, locator.text, locator.position, locator.direct, locator.attributes)

    return jqsel
  }

}