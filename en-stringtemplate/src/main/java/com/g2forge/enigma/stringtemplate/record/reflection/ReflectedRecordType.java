package com.g2forge.enigma.stringtemplate.record.reflection;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.function.CachingSupplier;
import com.g2forge.alexandria.java.reflection.JavaClass;
import com.g2forge.alexandria.java.reflection.JavaScope;
import com.g2forge.enigma.stringtemplate.record.IPropertyType;
import com.g2forge.enigma.stringtemplate.record.IRecordType;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ReflectedRecordType implements IRecordType {
	protected final JavaClass<?> reflection;

	protected Supplier<Map<String, IPropertyType>> properties = new CachingSupplier<>(() -> {
		final Map<String, IPropertyType> properties = new LinkedHashMap<>();
		properties.putAll(getReflection().getFields(JavaScope.Inherited, null).map(FieldPropertyType::new).collect(Collectors.toMap(IPropertyType::getName, Function.identity())));
		properties.putAll(getReflection().getMethods(JavaScope.Inherited, null).filter(GetterPropertyType::isGetter).collect(Collectors.toList()).stream().map(GetterPropertyType::new).collect(Collectors.toMap(IPropertyType::getName, Function.identity())));
		return properties;
	});

	public ReflectedRecordType(Class<?> type) {
		this.reflection = new JavaClass<>(type);
	}

	public Collection<IPropertyType> getProperties() {
		return properties.get().values();
	}

	protected JavaClass<?> getReflection() {
		return reflection;
	}
}
