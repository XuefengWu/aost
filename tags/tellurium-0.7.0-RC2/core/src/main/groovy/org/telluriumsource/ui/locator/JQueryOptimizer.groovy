package org.telluriumsource.ui.locator


/**
 * Try to optimize the jQuery Selector generated by the jQuery builder to improve the speed
 *
 *  @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Apr 15, 2009
 *
 */
public class JQueryOptimizer {
  
  protected static final String JQUERY_PREFIX = "jquery="
  protected static final String JQUERY_SEPARATOR = " "
  protected static final int LENGTH = 64

  private ContextAwareSplitter splitter = new ContextAwareSplitter()
   
  public String optimize(String jqs){
    if(jqs == null || jqs.trim().length() == 0)
      return jqs

    String jsel = removePrefix(jqs)
    String selected = pickIdSelector(jsel)

    //do not add prefix so that DslContext can handle it
    return selected
//    return addPrefix(selected)
  }

//example:
//
// CASE 1:
// jquery=form[method=get]:has(select#can, span:contains(for), input[type=text][name=q], input[value=Search][type=submit]) select#can
//
// CASE 2:
// jquery=table#resultstable > tbody > tr > td
//

  //pick the last useful ID selector and ignore everything before it since ID selector is unique in jQuery
  //For example in
  //  jquery=form[method=get]:has(select#can, span:contains(for), input[type=text][name=q], input[value=Search][type=submit]) select#can
  //select#can is the real useful part, everything before it can be ignored/removed.
  protected String pickIdSelector(String jqs){
    Stack<String> stack = new Stack<String>()
    Stack<String> reverse = new Stack<String>()

//    String[] splitted = jqs.split(JQUERY_SEPARATOR)
    String[] splitted = splitter.split(jqs)
    for(String str: splitted){
      stack.push(str)
    }

    for(int i=0; i<splitted.length; i++){
      String sel = stack.pop()
      reverse.push(sel)
      if(containIdSelector(sel))
        break
    }

    StringBuffer sb = new StringBuffer(LENGTH)

    while(reverse.size() > 0){
      String sel = reverse.pop()
      sb.append(JQUERY_SEPARATOR).append(sel)
    }

    return sb.toString().trim()
  }

  protected boolean containIdSelector(String jqs){
    return  jqs ==~ /^(\w)*#(\w)+(.)*/
  }

  protected String removePrefix(String jqs){
    String input = jqs.trim()

    if(input.startsWith(JQUERY_PREFIX)){
      return input.substring(JQUERY_PREFIX.length())
    }

    return input
  }

  protected String addPrefix(String jqs){
    return JQUERY_PREFIX + jqs
  }
}