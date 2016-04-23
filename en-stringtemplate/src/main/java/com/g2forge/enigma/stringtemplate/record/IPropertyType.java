package com.g2forge.enigma.stringtemplate.record;

public interface IPropertyType {
	public String getName();

	public Object getValue(Object object);

	public void setValue(Object object, Object value);
}
