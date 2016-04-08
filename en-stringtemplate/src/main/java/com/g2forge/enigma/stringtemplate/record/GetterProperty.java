package com.g2forge.enigma.stringtemplate.record;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Data;

@Data
public class GetterProperty implements IProperty {
	protected final Method getter;

	public GetterProperty(Method getter) {
		this.getter = getter;
		getGetter().setAccessible(true);
	}

	@Override
	public String getName() {
		final String name = getGetter().getName();
		if (name.startsWith("get")) return name.substring(3, 4).toLowerCase() + name.substring(4);
		return name;
	}

	@Override
	public Object getValue(Object object) {
		try {
			return getGetter().invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeException(exception);
		}
	}
}
