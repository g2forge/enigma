package com.g2forge.enigma.frontend.typeswitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class TypeSwitch1<I, O> implements IFunction1<I, O> {
	public static class ConsumerBuilder<I> {
		protected final Collection<TypedFunction1<?, Void>> functions = new ArrayList<>();

		protected IConsumer1<? super I> fallback = null;

		public <T> ConsumerBuilder<I> add(Class<T> type, IConsumer1<? super T> consumer) {
			functions.add(new TypedFunction1<T, Void>(type, i -> {
				consumer.accept(i);
				return null;
			}));
			return this;
		}

		public IConsumer1<I> build() {
			final TypeSwitch1<I, Void> ts = new TypeSwitch1<>(fallback == null ? null : i -> {
				fallback.accept(i);
				return null;
			}, functions);
			return i -> ts.apply(i);
		}

		public ConsumerBuilder<I> fallback(IConsumer1<? super I> consumer) {
			if (this.fallback != null) throw new IllegalStateException("Cannot set more than one fallback consumer!");
			this.fallback = consumer;
			return this;
		}

		public ConsumerBuilder<I> with(IConsumer1<? super ConsumerBuilder<I>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	public static class FunctionBuilder<I, O> {
		protected final Collection<TypedFunction1<?, O>> functions = new ArrayList<>();

		protected IFunction1<? super I, ? extends O> fallback = null;

		public <T> FunctionBuilder<I, O> add(Class<T> type, IFunction1<? super T, ? extends O> function) {
			functions.add(new TypedFunction1<T, O>(type, function));
			return this;
		}

		public IFunction1<I, O> build() {
			return new TypeSwitch1<>(fallback, functions);
		}

		public FunctionBuilder<I, O> fallback(IFunction1<? super I, ? extends O> function) {
			if (this.fallback != null) throw new IllegalStateException("Cannot set more than one fallback function!");
			this.fallback = function;
			return this;
		}

		public FunctionBuilder<I, O> with(IConsumer1<? super FunctionBuilder<I, O>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	@RequiredArgsConstructor
	@ToString(callSuper = true)
	@Getter
	protected static class Node<O> extends ANode<O, Node<O>> {
		protected final ITypedFunction1<?, O> function;

		protected <I> O apply(IFunction1<? super I, ? extends O> fallback, I input) {
			return get(n -> n.getFunction().isApplicable(input), collection -> {
				if (collection.isEmpty()) return new Node<O>(new TypedFunction1<>(null, fallback));
				return HCollection.getOne(collection);
			}).getFunction().apply(input);
		}

		protected boolean isAncestor(Node<O> node) {
			final ITypedFunction1<?, O> thisFunction = getFunction();
			if (thisFunction == null) return true;
			return thisFunction.getInputType().isAssignableFrom(node.getFunction().getInputType());
		}

		protected boolean isDescendant(Node<O> node) {
			final ITypedFunction1<?, O> thisFunction = getFunction();
			if (thisFunction == null) return false;
			return node.getFunction().getInputType().isAssignableFrom(thisFunction.getInputType());
		}

		@Override
		protected boolean isObjectRoot() {
			return getFunction() == null;
		}
	}

	@Getter(AccessLevel.PROTECTED)
	protected final IFunction1<? super I, ? extends O> fallback;

	@Getter(AccessLevel.PROTECTED)
	protected final Node<O> root;

	public TypeSwitch1(IFunction1<? super I, ? extends O> fallback, Collection<? extends ITypedFunction1<?, O>> functions) {
		this.fallback = fallback;
		this.root = Node.computeRoot(functions, Node::new);
	}

	@SafeVarargs
	public TypeSwitch1(IFunction1<? super I, ? extends O> fallback, TypedFunction1<I, O>... functions) {
		this(fallback, Arrays.asList(functions));
	}

	@Override
	public O apply(I input) {
		return getRoot().apply(getFallback(), input);
	}
}
