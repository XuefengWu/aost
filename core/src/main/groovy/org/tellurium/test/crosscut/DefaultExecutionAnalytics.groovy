package org.tellurium.test.crosscut
/**
 * 
 * @author Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: May 21, 2009
 * 
 */

public class DefaultExecutionAnalytics implements ExecutionAnalytics, ExecutionReporter {
  private static int SB_LEN = 64;

  private long startTime = 0;

  private long endTime = 0;

  private Map<String, ExecutionResult> map = new HashMap<String, ExecutionResult>();

  public void analysize(ExecutionTime etime) {

    long ltime = etime.startTime + etime.duration;
    if(endTime < ltime){
      endTime = ltime;
    }
    if(startTime == 0)
      startTime = etime.startTime;

    ExecutionResult result = map.get(etime.testName);
    if(result != null){
      ++result.count;
      result.total += etime.duration;
    }else{
      result = new ExecutionResult(etime.testName, 1, etime.duration);
    }

    map.put(etime.testName, result);
  }

  def report() {
    StringBuffer sb = new StringBuffer(SB_LEN);
    sb.append("Start Time: ").append(startTime).append("\n")
      .append("End Time: ").append(endTime).append("\n")
      .append("Total Runtime: ").append(endTime-startTime).append("ms\n");
    map.each {String key, ExecutionResult value ->
      sb.append("Name: ").append(value.name).append(", count: ").append(value.count)
              .append(", total: ").append(value.total).append("ms, average: ")
              .append(value.total/value.count).append("ms\n");
    }

    return sb.toString();
  }
}