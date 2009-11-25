package org.tellurium.bundle

import org.tellurium.bundle.BaseCmd
import org.tellurium.bundle.ReturnType
/**
 *
 * Response from a selenium call
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: May 26, 2009
 * 
 */

public class CmdResponse extends BaseCmd{

  //return type
  public static final String RETURN_TYPE = "returnType";
  protected ReturnType returnType;

  //return result
  public static final String RETURN_RESULT = "returnResult";
  def returnResult;

}