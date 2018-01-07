package com.g2forge.enigma.document.sandbox.doc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.function.IPredicate1;

import lombok.Getter;

public class ReflectiveDynamicDispatch<T> {
	protected class MethodSet {
		protected final Collection<Method> methods;

		@SafeVarargs
		public MethodSet(IPredicate1<Method>... predicates) {
			this.methods = Stream.of(type.getDeclaredMethods()).filter(m -> {
				for (IPredicate1<Method> predicate : predicates) {
					if (!predicate.test(m)) return false;
				}
				return true;
			}).collect(Collectors.toList());
		}

		public Method find(Object... parameters) {
			if (parameters.length != 1) throw new UnsupportedOperationException(/* TODO */);
			final List<Method> methods = this.methods.stream().filter(m -> m.getParameterTypes()[0].isInstance(parameters[0])).collect(Collectors.toList());
			Collections.sort(methods, new Comparator<Method>() {
				@Override
				public int compare(Method o1, Method o2) {
					final Class<?> p1 = o1.getParameterTypes()[0];
					final Class<?> p2 = o2.getParameterTypes()[0];
					if (p1.equals(p2)) return 0;
					if (p1.isAssignableFrom(p2)) return 1;
					if (p2.isAssignableFrom(p1)) return -1;
					throw new UnsupportedOperationException(/* TODO */);
				}
			});
			return methods.get(0);
		}
	}

	@Getter
	protected final T object;

	protected final Class<?> type;

	public ReflectiveDynamicDispatch(T object) {
		this.object = object;
		this.type = object.getClass();
	}

	public <I, O> Function<I, O> dispatch(Class<I> input, Class<O> output, String name) {
		final MethodSet methodSet = new MethodSet(m -> m.getParameterTypes().length == 1, m -> m.getName().equals(name), m -> input.isAssignableFrom(m.getParameterTypes()[0]), m -> output.isAssignableFrom(m.getReturnType()));
		return i -> {
			final Method method = methodSet.find(i);
			try {
				method.setAccessible(true);
				@SuppressWarnings("unchecked")
				final O retVal = (O) method.invoke(object, i);
				return retVal;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new Error(e);
			}

		};
	}
}
