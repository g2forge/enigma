package com.g2forge.enigma.frontend.typeswitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.g2forge.alexandria.java.function.IFunction3;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class TypeSwitch3<I0, I1, I2, O> implements IFunction3<I0, I1, I2, O> {
	public static class Builder<I0, I1, I2, O> {
		protected final Collection<TypedFunction3<?, ?, ?, O>> functions = new ArrayList<>();

		public <T0, T1, T2> Builder<I0, I1, I2, O> add(Class<T0> type0, Class<T1> type1, Class<T2> type2, IFunction3<? super T0, ? super T1, ? super T2, ? extends O> function) {
			functions.add(new TypedFunction3<T0, T1, T2, O>(type0, type1, type2, function));
			return this;
		}

		public TypeSwitch3<I0, I1, I2, O> build() {
			return new TypeSwitch3<>(functions);
		}
	}

	@RequiredArgsConstructor
	@ToString(callSuper = true)
	@Getter
	protected static class Node<O> extends ANode<O, Node<O>> {
		protected final ITypedFunction3<?, ?, ?, O> function;

		protected <I0, I1, I2> O apply(I0 input0, I1 input1, I2 input2) {
			return get(n -> n.getFunction().isApplicable(input0, input1, input2)).getFunction().apply(input0, input1, input2);
		}

		protected boolean isAncestor(Node<O> node) {
			final ITypedFunction3<?, ?, ?, O> thisFunction = getFunction();
			if (thisFunction == null) return true;
			if (!thisFunction.getInput0Type().isAssignableFrom(node.getFunction().getInput0Type())) return false;
			if (!thisFunction.getInput1Type().isAssignableFrom(node.getFunction().getInput1Type())) return false;
			if (!thisFunction.getInput2Type().isAssignableFrom(node.getFunction().getInput2Type())) return false;
			return true;
		}

		protected boolean isDescendant(Node<O> node) {
			final ITypedFunction3<?, ?, ?, O> thisFunction = getFunction();
			if (thisFunction == null) return false;
			if (!node.getFunction().getInput0Type().isAssignableFrom(thisFunction.getInput0Type())) return false;
			if (!node.getFunction().getInput1Type().isAssignableFrom(thisFunction.getInput1Type())) return false;
			if (!node.getFunction().getInput2Type().isAssignableFrom(thisFunction.getInput2Type())) return false;
			return true;
		}

		@Override
		protected boolean isObjectRoot() {
			return getFunction() == null;
		}
	}

	@Getter(AccessLevel.PROTECTED)
	protected final Node<O> root;

	public TypeSwitch3(Collection<? extends ITypedFunction3<?, ?, ?, O>> functions) {
		this.root = Node.computeRoot(functions, Node::new);
	}

	@SafeVarargs
	public TypeSwitch3(TypedFunction3<I0, I1, I2, O>... functions) {
		this(Arrays.asList(functions));
	}

	@Override
	public O apply(I0 input0, I1 input1, I2 input2) {
		return getRoot().apply(input0, input1, input2);
	}
}
