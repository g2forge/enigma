package com.g2forge.enigma.stringtemplate.record;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.reflection.JavaScope;
import com.g2forge.alexandria.java.reflection.ReflectionHelpers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Record {
	@Getter
	protected final Class<?> type;

	protected Map<String, IProperty> properties = null;

	public Collection<IProperty> getProperties() {
		if (properties == null) {
			properties = new LinkedHashMap<>();
			properties.putAll(ReflectionHelpers.getFields(type, JavaScope.Inherited, null).map(FieldProperty::new).collect(Collectors.toMap(IProperty::getName, Function.identity())));
			properties.putAll(ReflectionHelpers.getMethods(type, JavaScope.Inherited, null).filter(GetterProperty::isGetter).collect(Collectors.toList()).stream().map(GetterProperty::new).collect(Collectors.toMap(IProperty::getName, Function.identity())));
		}
		return properties.values();
	}
}
