package com.g2forge.enigma.stringtemplate.record;

import java.lang.reflect.Field;

import lombok.Data;

@Data
public class FieldProperty implements IProperty {
	protected final Field field;

	public FieldProperty(Field field) {
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
}
