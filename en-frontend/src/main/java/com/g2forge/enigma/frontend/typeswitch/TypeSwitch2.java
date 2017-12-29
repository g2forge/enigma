package com.g2forge.enigma.frontend.typeswitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction2;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class TypeSwitch2<I0, I1, O> implements IFunction2<I0, I1, O> {
	public static class ConsumerBuilder<I0, I1> {
		protected final Collection<TypedFunction2<?, ?, Void>> functions = new ArrayList<>();

		protected IConsumer2<? super I0, ? super I1> fallback = null;

		public <T0, T1> ConsumerBuilder<I0, I1> add(Class<T0> type0, Class<T1> type1, IConsumer2<? super T0, ? super T1> consumer) {
			functions.add(new TypedFunction2<T0, T1, Void>(type0, type1, (i0, i1) -> {
				consumer.accept(i0, i1);
				return null;
			}));
			return this;
		}

		public IConsumer2<I0, I1> build() {
			final TypeSwitch2<I0, I1, Void> ts = new TypeSwitch2<>(fallback == null ? null : (i0, i1) -> {
				fallback.accept(i0, i1);
				return null;
			}, functions);
			return (i0, i1) -> ts.apply(i0, i1);
		}

		public ConsumerBuilder<I0, I1> fallback(IConsumer2<? super I0, ? super I1> consumer) {
			if (this.fallback != null) throw new IllegalStateException("Cannot set more than one fallback consumer!");
			this.fallback = consumer;
			return this;
		}

		public ConsumerBuilder<I0, I1> with(IConsumer1<? super ConsumerBuilder<I0, I1>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	public static class FunctionBuilder<I0, I1, O> {
		protected final Collection<TypedFunction2<?, ?, O>> functions = new ArrayList<>();

		protected IFunction2<? super I0, ? super I1, ? extends O> fallback = null;

		public <T0, T1> FunctionBuilder<I0, I1, O> add(Class<T0> type0, Class<T1> type1, IFunction2<? super T0, ? super T1, ? extends O> function) {
			functions.add(new TypedFunction2<T0, T1, O>(type0, type1, function));
			return this;
		}

		public IFunction2<I0, I1, O> build() {
			return new TypeSwitch2<>(fallback, functions);
		}

		public FunctionBuilder<I0, I1, O> fallback(IFunction2<? super I0, ? super I1, ? extends O> function) {
			if (this.fallback != null) throw new IllegalStateException("Cannot set more than one fallback function!");
			this.fallback = function;
			return this;
		}

		public FunctionBuilder<I0, I1, O> with(IConsumer1<? super FunctionBuilder<I0, I1, O>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	@RequiredArgsConstructor
	@ToString(callSuper = true)
	@Getter
	protected static class Node<O> extends ANode<O, Node<O>> {
		protected final ITypedFunction2<?, ?, O> function;

		protected <I0, I1> O apply(IFunction2<? super I0, ? super I1, ? extends O> fallback, I0 input0, I1 input1) {
			return get(n -> n.getFunction().isApplicable(input0, input1), collection -> {
				if (collection.isEmpty()) return new Node<O>(new TypedFunction2<>(null, null, fallback));
				return HCollection.getOne(collection);
			}).getFunction().apply(input0, input1);
		}

		protected boolean isAncestor(Node<O> node) {
			final ITypedFunction2<?, ?, O> thisFunction = getFunction();
			if (thisFunction == null) return true;
			if (!thisFunction.getInput0Type().isAssignableFrom(node.getFunction().getInput0Type())) return false;
			if (!thisFunction.getInput1Type().isAssignableFrom(node.getFunction().getInput1Type())) return false;
			return true;
		}

		protected boolean isDescendant(Node<O> node) {
			final ITypedFunction2<?, ?, O> thisFunction = getFunction();
			if (thisFunction == null) return false;
			if (!node.getFunction().getInput0Type().isAssignableFrom(thisFunction.getInput0Type())) return false;
			if (!node.getFunction().getInput1Type().isAssignableFrom(thisFunction.getInput1Type())) return false;
			return true;
		}

		@Override
		protected boolean isObjectRoot() {
			return getFunction() == null;
		}
	}

	@Getter(AccessLevel.PROTECTED)
	protected final IFunction2<? super I0, ? super I1, ? extends O> fallback;

	@Getter(AccessLevel.PROTECTED)
	protected final Node<O> root;

	public TypeSwitch2(IFunction2<? super I0, ? super I1, ? extends O> fallback, Collection<? extends ITypedFunction2<?, ?, O>> functions) {
		this.fallback = fallback;
		this.root = Node.computeRoot(functions, Node::new);
	}

	@SafeVarargs
	public TypeSwitch2(IFunction2<? super I0, ? super I1, ? extends O> fallback, TypedFunction2<I0, I1, O>... functions) {
		this(fallback, Arrays.asList(functions));
	}

	@Override
	public O apply(I0 input0, I1 input1) {
		return getRoot().apply(getFallback(), input0, input1);
	}
}
