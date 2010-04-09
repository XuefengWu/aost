package org.telluriumsource.entity.config

import org.json.simple.JSONObject

/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Apr 7, 2010
 * 
 * 
 */
class EmbeddedServer {
  //port number
  public static String PORT = "port";
  String port = "4444";

  //whether to use multiple windows
  public static String USE_MULTI_WINDOWS = "useMultiWindows";
  boolean useMultiWindows = false;

  //whether to trust all SSL certs, i.e., option "-trustAllSSLCertificates"
  public static String TRUST_ALL_SSL_CERTIFICATES = "trustAllSSLCertificates";
  boolean trustAllSSLCertificates = true;

  //whether to run the embedded selenium server. If false, you need to manually set up a selenium server
  public static String RUN_INTERNALLY = "runInternally";
  boolean runInternally = false;

  //By default, Selenium proxies every browser request; set this flag to make the browser use proxy only for URLs containing '/selenium-server'
  public static String AVOID_PROXY = "avoidProxy";
  boolean avoidProxy = false;

  //stops re-initialization and spawning of the browser between tests
  public static String BROWSER_SESSION_REUSE = "browserSessionReuse";
  boolean browserSessionReuse = false;

  //enabling this option will cause all user cookies to be archived before launching IE, and restored after IE is closed.
  public static String ENSURE_CLEAN_SESSION = "ensureCleanSession";
  boolean ensureCleanSession = false;

  //debug mode, with more trace information and diagnostics on the console
  public static String DEBUG_MODE = "debugMode";
  boolean debugMode = false;

  //interactive mode
  public static String INTERACTIVE = "interactive";
  boolean interactive = false;

  //an integer number of seconds before we should give up
  public static String TIMEOUT_IN_SECONDS = "timeoutInSeconds";
  int timeoutInSeconds = 30;

  //profile location
  public static String PROFILE = "profile";
  String profile = "";

  //user-extension.js file
  public static String USER_EXTENSION = "userExtension";
  String userExtension = "";

  def EmbeddedServer(){

  }

  def EmbeddedServer(Map map) {
    this.port = map.get(PORT);
    this.useMultiWindows = map.get(USE_MULTI_WINDOWS);
    this.trustAllSSLCertificates = map.get(TRUST_ALL_SSL_CERTIFICATES);
    this.runInternally = map.get(RUN_INTERNALLY);
    this.avoidProxy = map.get(AVOID_PROXY);
    this.browserSessionReuse = map(BROWSER_SESSION_REUSE);
    this.ensureCleanSession = map(ENSURE_CLEAN_SESSION);
    this.debugMode = map.get(DEBUG_MODE);
    this.interactive = map.get(INTERACTIVE);
    this.timeoutInSeconds = map.get(TIMEOUT_IN_SECONDS);
    this.profile = map.get(PROFILE);
    this.userExtension = map.get(USER_EXTENSION);
  }

  public JSONObject toJSON(){
    JSONObject obj = new JSONObject();
    obj.put(PORT, this.port);
    obj.put(USE_MULTI_WINDOWS, this.useMultiWindows);
    obj.put(TRUST_ALL_SSL_CERTIFICATES, this.trustAllSSLCertificates);
    obj.put(RUN_INTERNALLY, this.runInternally);
    obj.put(AVOID_PROXY, this.avoidProxy);
    obj.put(BROWSER_SESSION_REUSE, this.browserSessionReuse);
    obj.put(ENSURE_CLEAN_SESSION, this.ensureCleanSession);
    obj.put(DEBUG_MODE, this.debugMode);
    obj.put(INTERACTIVE, this.interactive);
    obj.put(TIMEOUT_IN_SECONDS, this.timeoutInSeconds);
    obj.put(PROFILE, this.profile);
    obj.put(USER_EXTENSION, this.userExtension);

    return obj;
  }

}
