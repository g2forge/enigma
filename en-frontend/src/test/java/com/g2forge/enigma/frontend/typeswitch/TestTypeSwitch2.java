package com.g2forge.enigma.frontend.typeswitch;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.function.IFunction2;

public class TestTypeSwitch2 {
	@Test
	public void basic() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(String.class, Integer.class, (i0, i1) -> "si: " + i0 + i1);
		builder.add(Integer.class, String.class, (i0, i1) -> "is: " + i0 + i1);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();

		Assert.assertEquals("si: hello1", typeSwitch.apply("hello", 1));
		Assert.assertEquals("is: 2world", typeSwitch.apply(2, "world"));
	}

	@Test
	public void fallback() {
		final TypeSwitch2.FunctionBuilder<Object, Object, Object> builder = new TypeSwitch2.FunctionBuilder<>();
		builder.add(String.class, Integer.class, (i0, i1) -> "si: " + i0 + i1);
		builder.add(Integer.class, String.class, (i0, i1) -> "is: " + i0 + i1);
		builder.fallback((i0, i1) -> "oo: " + i0 + i1);
		final IFunction2<Object, Object, Object> typeSwitch = builder.build();
		Assert.assertEquals("oo: helloworld", typeSwitch.apply("hello", "world"));
	}
}
