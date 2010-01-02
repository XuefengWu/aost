package org.telluriumsource.bundle

import org.telluriumsource.dispatch.Dispatcher
import org.telluriumsource.config.Configurable
import org.telluriumsource.dsl.WorkflowContext
import org.telluriumsource.locator.MetaCmd
import org.stringtree.json.JSONReader
import org.telluriumsource.dsl.DslContext
import org.telluriumsource.dsl.UiID

import org.telluriumsource.framework.Environment
import org.telluriumsource.entity.UiModuleValidationRequest
import org.telluriumsource.entity.UiModuleValidationResponse
import org.telluriumsource.util.Helper

/**
 * Command Bundle Processor
 *
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: May 19, 2009
 * 
 */

@Singleton
public class BundleProcessor implements Configurable {

  public static final String OK = "ok";
  public static final String NAME = "name";
  public static final String[] EXCLUSIVE_LIST = ["getDiagnosisResponse", "getValidateUiModule"];

  //sequence number for each command
  private int sequence = 1;

  private Dispatcher dispatcher = new Dispatcher();

  private MacroCmd bundle = new MacroCmd();

  private JSONReader reader = new JSONReader();

  private Map<String, UiModuleState> states = new HashMap<String, UiModuleState>();

  //maximum number of commands in a bundle
  private int maxMacroCmd(){
    return Environment.instance.myMaxMacroCmd();
  }

  //whether to use the bundle feature
//  private boolean exploitBundle = Environment.instance.&useBundle;
  private boolean exploitBundle(){
    return Environment.instance.isUseBundle();
  }

  public boolean isUiModulePublished(String id){
    //TODO: how to invalidate ui module cache, by commands or by exception code returning from Engine?
    UiModuleState state = states.get(id);
    if(state != null){
      return state.hasBeenPublished();
    }

    return false;
  }

  public void publishUiModule(String id){
    UiModuleState state = states.get(id);
    if(state != null){
      state.setState(true);
    }else{
      state = new UiModuleState(id, true);
    }

    states.put(id, state);
  }

  public int nextSeq(){
    return ++sequence;
  }

  protected def parseUseUiModuleResponse(String result){

    if(result != null && result.trim().length() > 0){
      Map map = reader.read(result);
      UiModuleValidationResponse response = new UiModuleValidationResponse(map);
      if(!response.found && response.relaxed){
          dispatcher.warn("The exact match for UI Module '${response.id}' cannot be found, found the closest match: " + result);
      }else if(response.found){
          dispatcher.log("Found exact match for UI Module '${response.id}': " + result);
      }

      UiModuleState state = states.get(response.id);
      if(state != null){
        state.setLocatingResult(response);
      }else{
        state = new UiModuleState();
        state.id = response.id;
        state.published = true;
        state.setLocatingResult(response);
      }
      
      states.put(response.id, state);
    }
  }

  //TODO: how to parse the returning result?
  protected def parseReturnValue(String value){

    if(value.startsWith("OK,")){
      value = value.substring(3);
    } else {
      return null;
    }

    List list = reader.read(value);
    if(list != null && list.size() > 0){
      //only return one result for the time being since call is always sychronized
      //The result type is a map from JSON
//      CmdResponse resp = list.get(0);
      def resp = list.get(0);

      //check the UI Module locating command and do not propagate it up
      String name = resp.get(NAME);
      if (UiModuleValidationRequest.CMD_NAME.equals(name)) {
        parseUseUiModuleResponse(resp.get(CmdResponse.RETURN_RESULT));
        if (list.size() > 1) {
          resp = list.get(1);
        } else {
          return;
        }
      }

      def result = resp.get(CmdResponse.RETURN_RESULT);

      ReturnType type = ReturnType.valueOf(resp.get(CmdResponse.RETURN_TYPE).toUpperCase());

      switch(type){
        case ReturnType.VOID:
          break;
        case ReturnType.BOOLEAN:
          return "true".equals(result);
          break;
        case ReturnType.NUMBER:
          return (int)result;
          break;
        case ReturnType.STRING:
          return result;
          break;
        case ReturnType.ARRAY:
          //TODO: need to really convert the String array to the object array 
          return result;
          break;
        default:
          return result;
          break;
      }
    }

    return null;
  }

  protected boolean inExclusiveList(String cmd){
    boolean result = false;
    EXCLUSIVE_LIST.each {String elem ->
      if(elem.equals(cmd)){
        result = true;
      }
    }

    return result;
  }

  public boolean needCacheUiModule(WorkflowContext context, String cmd, String uid){
    if(uid != null && uid.trim().length() > 0 && context.isUseSelectorCache()){
        if(inExclusiveList(cmd)){
          return false;
        }
      
        String uimid = getUiModuleId(uid);
        return !this.isUiModulePublished(uimid);
    }

    return false;
  }

  public CmdRequest getUseUiModuleRequest(WorkflowContext context, String uid){
    DslContext dslcontext = context.getContext(WorkflowContext.DSLCONTEXT);
    String json = dslcontext.jsonify(uid);
    def args = [json]
//    CmdRequest cmd = new CmdRequest(nextSeq(), uid, UiModuleValidationRequest.CMD_NAME , args);
    //Use zero for the sequence ID so that it can be processed first
    CmdRequest cmd = new CmdRequest(0, uid, UiModuleValidationRequest.CMD_NAME , args);

    return cmd;
  }

  protected String getUiModuleId(String uid){
    UiID uiid = UiID.convertToUiID(uid);
    return uiid.pop();
  }

  def issueCommand(WorkflowContext context, String uid, String name, args){
    context.setApiName("getBundleResponse");  
    CmdRequest cmd = new CmdRequest(nextSeq(), uid, name, Helper.removeFirst(args));
    if(bundle.shouldAppend(cmd)){
      //TODO: need to extract the root uid
      if(this.needCacheUiModule(context, name, uid)){
        String id = this.getUiModuleId(uid);
        CmdRequest uum = this.getUseUiModuleRequest(context, id);
        bundle.addToBundle(uum);
        this.publishUiModule(id);
      }
      bundle.addToBundle(cmd);
      if(bundle.size() >= this.maxMacroCmd()){
        //need to issue the command bundle since it reaches the maximum limit
        String json = bundle.extractAllAndConvertToJson();

        String val = dispatcher.getBundleResponse(context, json);
        return parseReturnValue(val);
      }

      return OK;
    }else{
      String json = bundle.extractAllAndConvertToJson();
      if(this.needCacheUiModule(context, name, uid)){
        String id = this.getUiModuleId(uid);
        CmdRequest uum = this.getUseUiModuleRequest(context, id);
        bundle.addToBundle(uum);
        this.publishUiModule(id);
      }

      bundle.addToBundle(cmd);
      
      String val = dispatcher.getBundleResponse(context, json);
      return parseReturnValue(val);
    }
  }

  def passBundledCommand(WorkflowContext context, String uid, String name, args){
    context.setApiName(name);
    if(this.needCacheUiModule(context, name, uid)) {
      String id = this.getUiModuleId(uid);
      CmdRequest uum = this.getUseUiModuleRequest(context, id);
      bundle.addToBundle(uum);
      this.publishUiModule(id);
    }
    //there are commands in the bundle, pigback this command with the commands in a bundle and issue it
    CmdRequest cmd = new CmdRequest(nextSeq(), uid, name,  Helper.removeFirst(args));
    bundle.addToBundle(cmd);
    String json = bundle.extractAllAndConvertToJson();

    String val = dispatcher.getBundleResponse(context, json);
    return parseReturnValue(val);
  }

  def passThrough(WorkflowContext context, String uid, String name, args){
    context.setApiName(name);
    //if no command on the bundle, call directly
    if(bundle.size() == 0){
       if(this.needCacheUiModule(context, name, uid)){
        String id = this.getUiModuleId(uid);
        CmdRequest uum = this.getUseUiModuleRequest(context, id);
        dispatcher.metaClass.invokeMethod(dispatcher, uum.name, uum.args);
        this.publishUiModule(id);
     }
      return dispatcher.metaClass.invokeMethod(dispatcher, name, args);
    }else{
       if(this.needCacheUiModule(context, name, uid)){
        String id = this.getUiModuleId(uid);
        CmdRequest uum = this.getUseUiModuleRequest(context, id);
        bundle.addToBundle(uum);
        this.publishUiModule(id);
      }
      //there are commands in the bundle, pigback this command with the commands in a bundle and issue it
      CmdRequest cmd = new CmdRequest(nextSeq(), uid, name, Helper.removeFirst(args));
      bundle.addToBundle(cmd);
      String json = bundle.extractAllAndConvertToJson();
      
      String val = dispatcher.getBundleResponse(context, json);
      return parseReturnValue(val);
    }
  }

  //push remaining commands in the bundle to the Engine before disconnect from it
  def flush(){
    if(bundle.size() > 0){
      WorkflowContext context = WorkflowContext.getDefaultContext();
      if(bundle.size() == 1){
        context.setApiName(bundle.getFirstCmdName());
      }else{
        context.setApiName("getBundleResponse"); 
      }
      String json = bundle.extractAllAndConvertToJson();

      String val = dispatcher.getBundleResponse(context, json);
      return parseReturnValue(val);      
    }

    return null;
  }

  def process(String name, args) {
    WorkflowContext context = args[0]
    
    String uid = null;
    MetaCmd cmd = context.extraMetaCmd();
    if(cmd != null)
      uid = cmd.uid;

    if(this.exploitBundle() && context.isBundlingable()){
       return issueCommand(context, uid, name, args);
    }

//    return passThrough(context, uid, name, args);
      return passBundledCommand(context, uid, name, args);
  }

  protected def methodMissing(String name, args) {
    return process(name, args)
  }
}