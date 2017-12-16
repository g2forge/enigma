package com.g2forge.enigma.frontend.typeswitch;

import org.junit.Assert;
import org.junit.Test;

public class TestTypeSwitch2 {
	@Test
	public void basic() {
		final TypeSwitch2.Builder<Object, Object, Object> builder = new TypeSwitch2.Builder<>();
		builder.add(String.class, Integer.class, (i0, i1) -> "si: " + i0 + i1);
		builder.add(Integer.class, String.class, (i0, i1) -> "is: " + i0 + i1);
		final TypeSwitch2<Object, Object, Object> typeSwitch = builder.build();

		Assert.assertEquals("si: hello1", typeSwitch.apply("hello", 1));
		Assert.assertEquals("is: 2world", typeSwitch.apply(2, "world"));
	}
}
