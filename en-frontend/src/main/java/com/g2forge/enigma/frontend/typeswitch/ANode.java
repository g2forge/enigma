package com.g2forge.enigma.frontend.typeswitch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IPredicate1;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
abstract class ANode<O, N extends ANode<O, N>> {
	protected static <O, N extends ANode<O, N>> N computeRoot(Collection<? extends ITypedFunction1<?, O>> functions, IFunction1<? super ITypedFunction1<?, O>, ? extends N> createNode) {
		N root = null;
		for (ITypedFunction1<?, O> function : functions) {
			final N node = createNode.apply(function);
			if (root == null) {
				root = node;
				continue;
			}

			if (root.isDescendant(node)) {
				// We're adding a new root above the existing one
				node.getChildren().add(root);
				root = node;
			} else {
				final List<N> exploreDescendants = new ArrayList<>();

				if (root.isAncestor(node)) {
					// Walk down the tree and insert the new node into the appropriate location
					final Collection<N> parents = new LinkedHashSet<>();
					// Everything in the "exploreAncestors" list is definitely an ancestor of the new node to create
					final List<N> exploreAncestors = new ArrayList<>();
					exploreAncestors.add(root);
					while (!exploreAncestors.isEmpty()) {
						final N current = exploreAncestors.remove(exploreAncestors.size() - 1);
						exploreAncestors.addAll(ANode.split(node, current, exploreDescendants::add));
					}

					parents.forEach(parent -> parent.getChildren().add(node));
				} else {
					// Adding a new node next to the existing root
					final N prev = root;
					root = createNode.apply(null);
					root.getChildren().add(prev);
					root.getChildren().add(node);
					exploreDescendants.add(prev);
				}

				{// Find all the pre-existing children of the new node
					final HashSet<N> explored = new HashSet<>(exploreDescendants);
					while (!exploreDescendants.isEmpty()) {
						final N current = exploreDescendants.remove(exploreDescendants.size() - 1);
						if (current.isDescendant(node)) {
							// The current node is a descendant
							ANode.split(current, node, sibling -> {
								if (!explored.contains(sibling)) {
									explored.add(sibling);
									exploreDescendants.add(sibling);
								}
							});
						} else {
							final Set<N> currentChildren = current.getChildren();
							if (currentChildren != null) exploreDescendants.addAll(currentChildren);
						}
					}
				}
			}
		}
		root.clean();
		return root;
	}

	protected static <O, N extends ANode<O, N>> List<N> split(N newChild, final N existingParent, final IConsumer1<N> siblings) {
		if (existingParent == newChild) return HCollection.emptyList();

		final List<N> above = new ArrayList<>(), below = new ArrayList<>();
		if (existingParent.getChildren() != null) for (N child : existingParent.getChildren()) {
			// Loop over all the children and split them into above, below and sibling
			if (child.isAncestor(newChild)) above.add(child);
			else if (child.isDescendant(newChild)) below.add(child);
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

	protected Set<N> children = new LinkedHashSet<>();

	protected void clean() {
		if (children.isEmpty()) children = HCollection.emptySet();
		else children.forEach(ANode::clean);
	}

	protected N get(IPredicate1<N> applies) {
		final Collection<N> applicable = new LinkedHashSet<>();

		final List<N> explore = new ArrayList<>();
		@SuppressWarnings("unchecked")
		final N n = (N) this;
		if (isObjectRoot()) explore.add(n);
		else if (applies.test(n)) explore.add(n);

		while (!explore.isEmpty()) {
			final N current = explore.remove(explore.size() - 1);
			final List<N> children = current.getChildren().stream().filter(applies).collect(Collectors.toList());
			if (children.isEmpty()) {
				if (!isObjectRoot()) applicable.add(current);
			} else explore.addAll(children);
		}

		return HCollection.getOne(applicable);
	}

	protected abstract boolean isAncestor(N node);

	protected abstract boolean isDescendant(N node);

	protected abstract boolean isObjectRoot();
}
