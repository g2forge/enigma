package com.g2forge.alexandria.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamHelpers {
	@SafeVarargs
	public static <T> Stream<T> concat(Stream<T>... streams) {
		return Arrays.stream(streams).reduce(Stream::concat).get();
	}

	public static <T> Collector<T, List<T>, List<T>> interleave(T separator) {
		return Collector.of(ArrayList::new, (list, element) -> {
			if (!list.isEmpty()) list.add(separator);
			list.add(element);
		} , (list0, list1) -> {
			if (!list0.isEmpty()) list0.add(separator);
			list0.addAll(list1);
			return list0;
		});
	}

	public static <T> Collection<T> toList(Iterator<T> iterator, Supplier<? extends Collection<T>> constructor) {
		final Collection<T> retVal = constructor.get();
		iterator.forEachRemaining(retVal::add);
		return retVal;
	}

	public static <T> Stream<T> toStream(Iterator<T> iterator) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
	}
}
