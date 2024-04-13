package com.g2forge.enigma.frontend.antlr;

import java.util.Objects;

import com.g2forge.alexandria.java.core.error.UnreachableCodeError;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IFunction2;

/**
 * Strategies for reducing fields of POJOs.
 */
public enum ReduceStrategy {
	NullIsNone {
		@Override
		public <T, V> void reduce(T t0, T t1, IFunction1<? super T, ? extends V> getter, IConsumer1<? super V> setter, IFunction2<? super V, ? super V, ? extends V> merge) {
			final V v0 = getter.apply(t0);
			final V v1 = getter.apply(t1);
			switch (computeIndex(v0, v1)) {
				case 0:
					break;
				case 1:
					setter.accept(v0);
					break;
				case 2:
					setter.accept(v1);
					break;
				case 3:
					if (merge != null) {
						final V merged = merge.apply(v0, v1);
						setter.accept(merged);
						break;
					} else if (Objects.equals(v0, v1)) {
						setter.accept(v0);
						break;
					} else throw new IllegalArgumentException(String.format("Cannot merge non-null values (%1$s and %2$s), as no merging function as supplied", v0, v1));
				default:
					throw new UnreachableCodeError();
			}
		}
	},
	NullIsNoneSkip0 {
		@Override
		public <T, V> void reduce(T t0, T t1, IFunction1<? super T, ? extends V> getter, IConsumer1<? super V> setter, IFunction2<? super V, ? super V, ? extends V> merge) {
			final V v0 = getter.apply(t0);
			final V v1 = getter.apply(t1);
			switch (computeIndex(v0, v1)) {
				case 0:
				case 1:
					break;
				case 2:
				case 3:
					setter.accept(v1);
					break;
				default:
					throw new UnreachableCodeError();
			}
		}
	};

	protected <V> int computeIndex(final V v0, final V v1) {
		return ((v1 != null) ? 2 : 0) | ((v0 != null) ? 1 : 0);
	}

	public abstract <T, V> void reduce(T t0, T t1, IFunction1<? super T, ? extends V> getter, IConsumer1<? super V> setter, IFunction2<? super V, ? super V, ? extends V> merge);
}