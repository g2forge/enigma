package com.g2forge.enigma.frontend.typeswitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class TypeSwitch1<I, O> implements IFunction1<I, O> {
	public static class Builder<I, O> {
		protected final Collection<TypedFunction1<?, O>> functions = new ArrayList<>();

		public <T> Builder<I, O> add(Class<T> type, IFunction1<? super T, ? extends O> function) {
			functions.add(new TypedFunction1<T, O>(type, function));
			return this;
		}

		public TypeSwitch1<I, O> build() {
			return new TypeSwitch1<>(functions);
		}
	}

	@RequiredArgsConstructor
	@ToString(callSuper = true)
	@Getter
	protected static class Node<O> extends ANode<O, Node<O>> {
		protected final ITypedFunction1<?, O> function;

		protected <I> O apply(I input) {
			return get(n -> n.getFunction().isApplicable(input)).getFunction().apply(input);
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
	protected final Node<O> root;

	public TypeSwitch1(Collection<? extends ITypedFunction1<?, O>> functions) {
		this.root = Node.computeRoot(functions, Node::new);
	}

	@SafeVarargs
	public TypeSwitch1(TypedFunction1<I, O>... functions) {
		this(Arrays.asList(functions));
	}

	@Override
	public O apply(I input) {
		return getRoot().apply(input);
	}
}
