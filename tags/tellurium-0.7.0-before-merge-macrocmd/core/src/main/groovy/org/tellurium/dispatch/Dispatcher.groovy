package org.tellurium.dispatch

import org.tellurium.client.SeleniumClient
import org.tellurium.config.Configurable
import org.tellurium.i18n.InternationalizationManager;



class Dispatcher implements GroovyInterceptable, Configurable {
    public static final String PLACE_HOLDER = "\\?"
    protected static InternationalizationManager i18nManager = new InternationalizationManager();

    private boolean captureScreenshot = false
    private String filenamePattern = "Screenshot?.png"

    private SeleniumClient sc = new SeleniumClient()

    java.lang.Object invokeMethod(java.lang.String name, java.lang.Object args) {
        // If this class automatically throws MissingMethodException, then we only need the following one line
//		return sc.client.metaClass.invokeMethod(this, name, args)

        //sometimes, the selenium client is not singleton ??
        //here reset selenium client to use the new singleton instance which has the client set
        if (sc.client == null || sc.client.getActiveSeleniumSession() == null)
            sc = new SeleniumClient()

        try {
            return sc.client.getActiveSeleniumSession().metaClass.invokeMethod(sc.client.getActiveSeleniumSession(), name, args)
        } catch (Exception e) {
            if (this.captureScreenshot) {
                long timestamp = System.currentTimeMillis()
                String filename = filenamePattern.replaceFirst(PLACE_HOLDER, "${timestamp}")
                sc.client.getActiveSeleniumSession().captureScreenshot(filename)
                println i18nManager.translate("Dispatcher.ExceptionMessage" , e.getMessage() , filename)
            }
            throw e
        }
    }

    protected def methodMissing(String name, args) {
       return invokeMethod(name, args)
    }
}