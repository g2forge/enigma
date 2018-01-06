package com.g2forge.enigma.document.sandbox.doc;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.enigma.document.sandbox.doc.ReflectiveDynamicDispatch;

public class TestReflectiveDynamicDispatch {
	public interface A {}

	public interface B extends A {}

	public static class Worker {
		protected String last;

		protected Object value;

		public void operate(A a) {
			last = "a";
			value = a;
		}

		public void operate(B b) {
			last = "b";
			value = b;
		}
	}

	@Test
	public void a() {
		test("a", new A() {});
	}

	@Test
	public void b() {
		test("b", new B() {});
	}

	protected void test(final String type, final A value) {
		final ReflectiveDynamicDispatch<Worker> dispatch = new ReflectiveDynamicDispatch<>(new Worker());
		dispatch.dispatch(A.class, Void.TYPE, "operate").apply(value);
		Assert.assertEquals(value, dispatch.getObject().value);
		Assert.assertEquals(type, dispatch.getObject().last);
	}
}
