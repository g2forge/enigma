package com.g2forge.enigma.frontend.typeswitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class TypeSwitch<I, O> implements IFunction1<I, O> {
	public static class Builder<I, O> {
		protected final Collection<TypedFunction1<?, O>> functions = new ArrayList<>();

		public <T> Builder<I, O> add(Class<T> type, IFunction1<? super T, ? extends O> function) {
			functions.add(new TypedFunction1<T, O>(type, function));
			return this;
		}

		public TypeSwitch<I, O> build() {
			return new TypeSwitch<>(functions);
		}
	}

	@RequiredArgsConstructor
	@ToString
	@Getter
	protected static class Node<O> {
		protected final TypedFunction1<?, O> function;

		protected Set<Node<O>> children = new LinkedHashSet<>();

		public void clean() {
			if (children.isEmpty()) children = HCollection.emptySet();
			else children.forEach(Node::clean);
		}

		protected boolean isAncestor(TypedFunction1<?, O> function) {
			final TypedFunction1<?, O> thisFunction = getFunction();
			if (thisFunction == null) return true;
			return thisFunction.getInputType().isAssignableFrom(function.getInputType());
		}

		protected boolean isDescendant(TypedFunction1<?, O> function) {
			final TypedFunction1<?, O> thisFunction = getFunction();
			if (thisFunction == null) return false;
			return function.getInputType().isAssignableFrom(thisFunction.getInputType());
		}
	}

	@Getter(AccessLevel.PROTECTED)
	protected final Node<O> root;

	public TypeSwitch(Collection<? extends TypedFunction1<?, O>> functions) {
		Node<O> root = null;
		for (TypedFunction1<?, O> function : functions) {
			final Node<O> node = new Node<>(function);
			if (root == null) {
				root = node;
				continue;
			}

			if (root.isDescendant(function)) {
				// We're adding a new root above the existing one
				node.getChildren().add(root);
				root = node;
			} else {
				final List<Node<O>> exploreDescendants = new ArrayList<>();

				if (root.isAncestor(function)) {
					// Walk down the tree and insert the new node into the appropriate location
					final Collection<Node<O>> parents = new LinkedHashSet<>();
					// Everything in the "exploreAncestors" list is definitely an ancestor of the new node to create
					final List<Node<O>> exploreAncestors = new ArrayList<>();
					exploreAncestors.add(root);
					while (!exploreAncestors.isEmpty()) {
						final Node<O> current = exploreAncestors.remove(exploreAncestors.size() - 1);
						exploreAncestors.addAll(split(node, current, exploreDescendants::add));
					}

					parents.forEach(parent -> parent.getChildren().add(node));
				} else {
					// Adding a new node next to the existing root
					final Node<O> prev = root;
					root = new Node<O>(null);
					root.getChildren().add(prev);
					root.getChildren().add(node);
					exploreDescendants.add(prev);
				}

				{// Find all the pre-existing children of the new node
					final HashSet<Node<O>> explored = new HashSet<>(exploreDescendants);
					while (!exploreDescendants.isEmpty()) {
						final Node<O> current = exploreDescendants.remove(exploreDescendants.size() - 1);
						if (current.isDescendant(function)) {
							// The current node is a descendant
							split(current, node, sibling -> {
								if (!explored.contains(sibling)) {
									explored.add(sibling);
									exploreDescendants.add(sibling);
								}
							});
						} else {
							final Set<Node<O>> currentChildren = current.getChildren();
							if (currentChildren != null) exploreDescendants.addAll(currentChildren);
						}
					}
				}
			}
		}
		root.clean();
		this.root = root;
	}

	@SafeVarargs
	public TypeSwitch(TypedFunction1<I, O>... functions) {
		this(Arrays.asList(functions));
	}

	@Override
	public O apply(I input) {
		final Collection<IFunction1<? super I, ? extends O>> applicable = new LinkedHashSet<>();

		final List<Node<O>> explore = new ArrayList<>();
		if (getRoot().getFunction() == null) explore.add(getRoot());
		else if (getRoot().getFunction().isApplicable(input)) explore.add(root);

		while (!explore.isEmpty()) {
			final Node<O> current = explore.remove(explore.size() - 1);
			final List<Node<O>> children = current.getChildren().stream().filter(child -> child.getFunction().isApplicable(input)).collect(Collectors.toList());
			if (children.isEmpty()) {
				if (current.getFunction() != null) applicable.add(current.getFunction().cast(input));
			} else explore.addAll(children);
		}

		return HCollection.getOne(applicable).apply(input);
	}

	/**
	 * @param newChild
	 * @param existingParent
	 * @param exploreParents
	 * @param siblings
	 * @param parents
	 */
	protected List<Node<O>> split(Node<O> newChild, final Node<O> existingParent, final IConsumer1<Node<O>> siblings) {
		if (existingParent == newChild) return HCollection.emptyList();

		final List<Node<O>> above = new ArrayList<>(), below = new ArrayList<>();
		if (existingParent.getChildren() != null) for (Node<O> child : existingParent.getChildren()) {
			// Loop over all the children and split them into above, below and sibling
			if (child.isAncestor(newChild.getFunction())) above.add(child);
			else if (child.isDescendant(newChild.getFunction())) below.add(child);
			else siblings.accept(child);
		}
		// Anything below the new node moves from the existing node to the new node
		newChild.getChildren().addAll(below);
		existingParent.getChildren().removeAll(below);
		// If any of the children were above the new node, then we'll keep looking down the tree
		if (above.isEmpty()) {
			// None of the children were above the new node
			existingParent.getChildren().add(newChild);
			return HCollection.emptyList();
		} else return above;
	}
}
