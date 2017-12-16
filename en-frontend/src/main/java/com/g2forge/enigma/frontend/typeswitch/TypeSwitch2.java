package com.g2forge.enigma.frontend.typeswitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

		public <T0, T1> ConsumerBuilder<I0, I1> add(Class<T0> type0, Class<T1> type1, IConsumer2<? super T0, ? super T1> consumer) {
			functions.add(new TypedFunction2<T0, T1, Void>(type0, type1, (i0, i1) -> {
				consumer.accept(i0, i1);
				return null;
			}));
			return this;
		}

		public IConsumer2<I0, I1> build() {
			final TypeSwitch2<I0, I1, Void> ts = new TypeSwitch2<>(functions);
			return (i0, i1) -> ts.apply(i0, i1);
		}

		public ConsumerBuilder<I0, I1> with(IConsumer1<? super ConsumerBuilder<I0, I1>> consumer) {
			consumer.accept(this);
			return this;
		}
	}

	public static class FunctionBuilder<I0, I1, O> {
		protected final Collection<TypedFunction2<?, ?, O>> functions = new ArrayList<>();

		public <T0, T1> FunctionBuilder<I0, I1, O> add(Class<T0> type0, Class<T1> type1, IFunction2<? super T0, ? super T1, ? extends O> function) {
			functions.add(new TypedFunction2<T0, T1, O>(type0, type1, function));
			return this;
		}

		public IFunction2<I0, I1, O> build() {
			return new TypeSwitch2<>(functions);
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

		protected <I0, I1> O apply(I0 input0, I1 input1) {
			return get(n -> n.getFunction().isApplicable(input0, input1)).getFunction().apply(input0, input1);
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
	protected final Node<O> root;

	public TypeSwitch2(Collection<? extends ITypedFunction2<?, ?, O>> functions) {
		this.root = Node.computeRoot(functions, Node::new);
	}

	@SafeVarargs
	public TypeSwitch2(TypedFunction2<I0, I1, O>... functions) {
		this(Arrays.asList(functions));
	}

	@Override
	public O apply(I0 input0, I1 input1) {
		return getRoot().apply(input0, input1);
	}
}
