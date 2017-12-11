package com.g2forge.enigma.javagen;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public class TreeHelpers {
	public static <N> Stream<N> dfs(N node, final Function<N, Collection<N>> getChildren, boolean postorder) {
		final Collection<N> children = getChildren.apply(node);
		if ((children == null) || children.isEmpty()) return Stream.of(node);
		else return children.stream().map(child -> dfs(child, getChildren, postorder)).reduce(Stream.of(node), postorder ? (s0, s1) -> Stream.concat(s1, s0) : Stream::concat);
	}
}
