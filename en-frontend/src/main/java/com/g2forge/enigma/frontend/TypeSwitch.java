package com.g2forge.enigma.frontend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IFunction2;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
@AllArgsConstructor
public class TypeSwitch<I, O, C> implements IFunction1<I, O> {
	@AllArgsConstructor
	public static class ConsumerBuilder<I, C> {
		protected final Collection<Implementation<Void, ?, C>> implementations = new ArrayList<>();

		public <T> ConsumerBuilder<I, C> add(Class<T> type, IConsumer2<? super C, ? super T> consumer) {
			implementations.add(Implementation.of(type, consumer));
			return this;
		}

		public TypeSwitch<I, Void, C> build(C context) {
			return new TypeSwitch<>(context, implementations);
		}

		public ConsumerBuilder<I, C> with(IConsumer1<? super ConsumerBuilder<I, C>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	public static class Exception extends RuntimeException {
		private static final long serialVersionUID = -3885906785685652095L;

		public Exception() {}

		public Exception(String message) {
			super(message);
		}

		public Exception(String message, Throwable cause) {
			super(message, cause);
		}

		public Exception(Throwable cause) {
			super(cause);
		}
	}

	@AllArgsConstructor
	public static class FunctionBuilder<I, O, C> {
		protected final Collection<Implementation<O, ?, C>> implementations = new ArrayList<>();

		public <T> FunctionBuilder<I, O, C> add(Class<T> type, IFunction2<? super C, ? super T, ? extends O> function) {
			implementations.add(new Implementation<>(type, function));
			return this;
		}

		public TypeSwitch<I, O, C> build(C context) {
			return new TypeSwitch<>(context, implementations);
		}

		public FunctionBuilder<I, O, C> with(IConsumer1<? super FunctionBuilder<I, O, C>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	@Data
	@Builder
	@AllArgsConstructor
	protected static class Implementation<O, T, C> implements IFunction2<C, T, O> {
		public static final <T, C> Implementation<Void, T, C> of(Class<T> type, IConsumer2<? super C, ? super T> consumer) {
			return new Implementation<>(type, (c, t) -> {
				consumer.accept(c, t);
				return null;
			});
		}

		protected final Class<? super T> type;

		protected final BiFunction<? super C, ? super T, ? extends O> implementation;

		@Override
		public O apply(C c, T t) {
			return implementation.apply(c, t);
		}

		public <_T> Implementation<O, _T, C> cast(_T object) {
			if (getType().isInstance(object)) {
				@SuppressWarnings("unchecked")
				final Class<? super _T> type = (Class<? super _T>) getType();
				@SuppressWarnings("unchecked")
				final BiFunction<? super C, ? super _T, ? extends O> implementation = (BiFunction<? super C, ? super _T, ? extends O>) getImplementation();
				return new Implementation<O, _T, C>(type, implementation);
			} else return null;
		}
	}

	protected final C context;

	protected final Collection<? extends Implementation<O, ?, C>> interpreters;

	@Override
	public O apply(I value) {
		return internal(value);
	}

	protected <_T extends I> O internal(_T input) {
		Collection<Implementation<O, _T, C>> implementations = getInterpreters().stream().map(t -> t.cast(input)).filter(Objects::nonNull).collect(Collectors.toList());
		if (implementations.size() != 1) {
			final Set<Implementation<O, _T, C>> roots = new LinkedHashSet<>();
			for (Implementation<O, _T, C> current : implementations) {
				final Class<? super _T> currentType = current.getType();

				final Set<Implementation<O, _T, C>> remove = new LinkedHashSet<>();
				boolean add = true;
				for (Implementation<O, _T, C> root : roots) {
					final Class<? super _T> rootType = root.getType();
					if (currentType.isAssignableFrom(rootType)) add = false;
					else if (rootType.isAssignableFrom(currentType)) remove.add(root);
				}
				roots.removeAll(remove);
				if (add) roots.add(current);
			}

			if (roots.size() != 1) throw new Exception("Found " + roots.size() + " interpreters for input \"" + input + "\" rather than exactly 1!");
			implementations = roots;
		}

		return implementations.iterator().next().apply(getContext(), input);
	}
}
