package org.telluriumsource.component.bundle

import org.telluriumsource.entity.EngineState
import org.telluriumsource.framework.SessionManager
import org.telluriumsource.framework.RuntimeEnvironment
import org.telluriumsource.annotation.Provider

/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Jan 19, 2010
 * 
**/

@Provider
public class EngineStateTracer {
  private int numStateUpdate = 0;

  private EngineState state = new EngineState();

  public void callStateUpdate(){
    this.numStateUpdate++;
  }

  public boolean haveStateUpdate(){
    return this.numStateUpdate > 0;
  }

  public void cleanStateUpdate(){
    this.numStateUpdate = 0;    
  }

  public EngineState getEngineState(){
    return this.state;      
  }

  public void setEngineState(EngineState newstate){
    this.state = newstate;
  }

  public void setEngineState(boolean isUseCache, boolean isUseTeApi, boolean isUseClosestMatch){
    this.state.useCache(isUseCache);
    this.state.useTelluriumApi(isUseTeApi);
    this.state.useClosestMatch(isUseClosestMatch);
  }

  public void updateEngineStateFromEnvironment(){
    RuntimeEnvironment env = SessionManager.getSession().getEnv();

//    this.state.useCache(env.isUseCache());
    this.state.useTelluriumApi(env.isUseNewEngine());
    this.state.useClosestMatch(env.isUseClosestMatch());
  }

  public EngineState getEngineStateUpdate(){
    RuntimeEnvironment env = SessionManager.getSession().getEnv();

    if(this.state.isUseTelluriumApi() == env.isUseNewEngine() && this.state.isUseClosestMatch() == env.isUseClosestMatch()){
      return null;
    }else{
      this.updateEngineStateFromEnvironment();

      return this.state;
    }
  }
}