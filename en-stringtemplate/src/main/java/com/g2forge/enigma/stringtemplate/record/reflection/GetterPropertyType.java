package com.g2forge.enigma.stringtemplate.record.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.g2forge.alexandria.java.StringHelpers;
import com.g2forge.enigma.stringtemplate.record.IPropertyType;

import lombok.Data;

@Data
class GetterPropertyType implements IPropertyType {
	public static boolean isGetter(Method method) {
		if (method.getParameterCount() != 0) return false;

		final String name = method.getName();
		if (name.startsWith("get") && !Void.TYPE.equals(method.getGenericReturnType())) return true;
		if (name.startsWith("is") && (Boolean.TYPE.equals(method.getGenericReturnType()) || Boolean.class.equals(method.getGenericReturnType()))) return true;
		return false;
	}

	protected final Method getter;

	public GetterPropertyType(Method getter) {
		this.getter = getter;
		getGetter().setAccessible(true);
	}

	@Override
	public String getName() {
		return StringHelpers.lowercase(StringHelpers.stripPrefix(getGetter().getName(), "get", "is"));
	}

	@Override
	public Object getValue(Object object) {
		try {
			return getGetter().invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public void setValue(Object object, Object value) {
		throw new UnsupportedOperationException();
	}
}
