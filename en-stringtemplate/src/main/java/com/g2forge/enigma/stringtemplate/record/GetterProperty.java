package com.g2forge.enigma.stringtemplate.record;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Data;

@Data
public class GetterProperty implements IProperty {
	public static boolean isGetter(Method method) {
		if (method.getParameterCount() != 0) return false;

		final String name = method.getName();
		if (name.startsWith("get") && !Void.TYPE.equals(method.getGenericReturnType())) return true;
		if (name.startsWith("is") && (Boolean.TYPE.equals(method.getGenericReturnType()) || Boolean.class.equals(method.getGenericReturnType()))) return true;
		return false;
	}

	protected final Method getter;

	public GetterProperty(Method getter) {
		this.getter = getter;
		getGetter().setAccessible(true);
	}

	@Override
	public String getName() {
		final String name = getGetter().getName();
		if (name.startsWith("get")) return name.substring(3, 4).toLowerCase() + name.substring(4);
		if (name.startsWith("is")) return name.substring(2, 3).toLowerCase() + name.substring(3);
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
