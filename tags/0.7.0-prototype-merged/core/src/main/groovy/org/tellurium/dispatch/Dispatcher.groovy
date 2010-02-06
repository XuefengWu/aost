package org.tellurium.dispatch

import org.tellurium.client.SeleniumClient
import org.tellurium.config.Configurable
import org.tellurium.i18n.InternationalizationManager
import org.tellurium.test.crosscut.DefaultExecutionTracer
import org.tellurium.test.crosscut.ExecutionTracer
import org.tellurium.framework.Environment;



class Dispatcher implements Configurable {
    public static final String PLACE_HOLDER = "\\?"
    protected static InternationalizationManager i18nManager = new InternationalizationManager();

//    private boolean captureScreenshot = Environment.instance.&useScreenshot;
    private String filenamePattern = "Screenshot?.png";
//    private boolean trace = Environment.instance.&useTrace;

    private SeleniumClient sc = new SeleniumClient();
    private ExecutionTracer tracer = new DefaultExecutionTracer();

    private boolean isUseScreenshot(){
      return Environment.instance.isUseScreenshot();
    }

    private boolean isUseTrace(){
      return Environment.instance.isUseTrace();
    }

    def methodMissing(String name, args) {

    //sometimes, the selenium client is not singleton ??
    //here reset selenium client to use the new singleton instance which has the client set
    if (sc.client == null || sc.client.getActiveSeleniumSession() == null)
      sc = new SeleniumClient()

    try {
      long beforeTime = System.currentTimeMillis()
      def result = sc.client.getActiveSeleniumSession().metaClass.invokeMethod(sc.client.getActiveSeleniumSession(), name, args)
      long duration = System.currentTimeMillis() - beforeTime
      if (isUseTrace())
        tracer.publish(name, beforeTime, duration)

      return result
    } catch (Exception e) {
      if (isUseScreenshot()) {
        long timestamp = System.currentTimeMillis()
        String filename = filenamePattern.replaceFirst(PLACE_HOLDER, "${timestamp}")
        sc.client.getActiveSeleniumSession().captureScreenshot(filename)
        println i18nManager.translate("Dispatcher.ExceptionMessage", e.getMessage(), filename)
      }
      throw e
    }
  }                                                    

 /*
 protected def methodMissing(String name, args) {
    return invokeMethod(name, args)
  }
*/

  def showTrace() {
    tracer.report()
  }
}