package com.g2forge.enigma.stringtemplate.record.reflection;

import java.lang.reflect.Field;

import com.g2forge.enigma.stringtemplate.record.IPropertyType;

import lombok.Data;

@Data
class FieldPropertyType implements IPropertyType {
	protected final Field field;

	public FieldPropertyType(Field field) {
		this.field = field;
		getField().setAccessible(true);
	}

	@Override
	public String getName() {
		return getField().getName();
	}

	@Override
	public Object getValue(Object object) {
		try {
			return getField().get(object);
		} catch (IllegalArgumentException | IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public void setValue(Object object, Object value) {
		try {
			getField().set(object, value);
		} catch (IllegalArgumentException | IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}
}
