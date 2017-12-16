package com.g2forge.enigma.frontend;

import org.junit.Assert;
import org.junit.Test;

public class TestTypeSwitch {
	public interface A {}

	public interface B {}

	public class C implements A, B {}

	@Test
	public void basic() {
		final TypeSwitch<Object, Object, String> typeSwitch = new TypeSwitch.FunctionBuilder<Object, Object, String>().with(builder -> {
			builder.add(String.class, (c, t) -> c + t);
			builder.add(Integer.class, (c, t) -> c + Integer.toString(t + 1));
		}).build("a");

		Assert.assertEquals("ab", typeSwitch.apply("b"));
		Assert.assertEquals("a2", typeSwitch.apply(1));
	}

	@Test(expected = TypeSwitch.Exception.class)
	public void error() {
		final TypeSwitch<Object, Object, String> typeSwitch = new TypeSwitch.FunctionBuilder<Object, Object, String>().with(builder -> {
			builder.add(A.class, (c, t) -> null);
			builder.add(B.class, (c, t) -> null);
		}).build("a");

		typeSwitch.apply(new C());
	}

	@Test
	public void shadow() {
		final TypeSwitch<Object, Object, String> typeSwitch = new TypeSwitch.FunctionBuilder<Object, Object, String>().with(builder -> {
			builder.add(String.class, (c, t) -> t);
			builder.add(Object.class, (c, t) -> null);
		}).build("a");

		typeSwitch.apply("b");
	}
}
