package de.akquinet.jbosscc.needle.injection;

import java.util.Queue;

public class CustomeInjectionTestComponent {

	@CustomInjectionAnnotation1
	private Queue queue1;

	@CustomInjectionAnnotation2
	private Queue queue2;

	public Queue getQueue1() {
		return queue1;
	}

	public Queue getQueue2() {
		return queue2;
	}

}
