package de.akquinet.jbosscc.needle.injection;

import java.util.Map;
import java.util.Queue;

public class CustomInjectionTestComponent {

  @CustomInjectionAnnotation1
  private Queue<String> queue1;

  @CustomInjectionAnnotation2
  private Queue<String> queue2;

  @CustomInjectionAnnotation1
  private Map<String, String> map;

  public Queue<String> getQueue1() {
    return queue1;
  }

  public Queue<String> getQueue2() {
    return queue2;
  }

  public Map<String, String> getMap() {
    return map;
  }
}
