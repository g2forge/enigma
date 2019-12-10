package com.g2forge.enigma.backend.model.modifier;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.enigma.backend.model.expression.ITextExpression;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TextNestedModified implements ITextExpression {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Element {
		protected final Modifier modifier;

		protected final Object expression;
	}

	public static interface IModifierHandle extends ICloseable {
		public IModifierHandle reactivate();
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Modifier implements Cloneable {
		protected final Modifier parent;

		protected final ITextModifier modifier;

		@Override
		public Modifier clone() {
			return new Modifier(getParent(), getModifier());
		}
	}

	public static class TextNestedModifiedBuilder implements IBuilder<TextNestedModified> {
		@Getter(AccessLevel.PROTECTED)
		protected abstract class AModifierHandle implements IModifierHandle {
			protected boolean open = true;

			@Override
			public void close() {
				ensureOpen();
				if (getCurrentModifier() != getModifier()) throw new IllegalStateException();
				open = false;
				setCurrentModifier(getPrevious());
			}

			protected void ensureOpen() {
				if (!isOpen()) throw new IllegalStateException();
			}

			protected abstract Modifier getModifier();

			protected abstract Modifier getPrevious();
		}

		@Getter(AccessLevel.PROTECTED)
		protected class OpenModifierHandle extends AModifierHandle {
			protected final Modifier modifier;

			protected OpenModifierHandle(ITextModifier textModifier) {
				this.modifier = new Modifier(getCurrentModifier(), textModifier);
				setCurrentModifier(getModifier());
			}

			@Override
			protected Modifier getPrevious() {
				return getModifier().getParent();
			}

			@Override
			public IModifierHandle reactivate() {
				ensureOpen();
				return new ReactivatedModifierHandle(this);
			}
		}

		@Getter(AccessLevel.PROTECTED)
		protected class ReactivatedModifierHandle extends AModifierHandle {
			protected final AModifierHandle original;

			protected final Modifier previous;

			protected final Modifier modifier;

			public ReactivatedModifierHandle(AModifierHandle original) {
				this.original = original;
				this.previous = getCurrentModifier();
				// Clone the modifier so that we can tell the activations apart
				this.modifier = getOriginal().getModifier().clone();
				ensureOpenOkay();
				setCurrentModifier(getModifier());
			}

			protected void ensureOpenOkay() {
				// Make sure the original modifier is in the parent chain of the current modifier
				final Modifier originalModifier = getOriginal().getModifier();
				Modifier current = getCurrentModifier();
				while (current != null) {
					if (current.equals(originalModifier)) return;
					current = current.getParent();
				}
				throw new IllegalStateException("Can't reactivate a non-ancestor modifier!");
			}

			@Override
			public IModifierHandle reactivate() {
				ensureOpen();
				return new ReactivatedModifierHandle(this);
			}
		}

		@Getter(AccessLevel.PROTECTED)
		@Setter(AccessLevel.PROTECTED)
		protected Modifier currentModifier;

		@Getter
		protected final IModifierHandle root = new OpenModifierHandle(null);

		public TextNestedModifiedBuilder expression(Object expression) {
			this.element(new Element(getCurrentModifier(), expression));
			return this;
		}

		public IModifierHandle open(ITextModifier modifier) {
			return new OpenModifierHandle(modifier);
		}
	}

	@Singular
	protected final List<Element> elements;

	public Map<Modifier, Set<Element>> getApplicableMap() {
		final Map<Modifier, Map<Element, Object>> retVal = new IdentityHashMap<>();

		for (Element element : getElements()) {
			Modifier current = element.getModifier();
			while (current != null) {
				if (current.getModifier() != null) {
					retVal.computeIfAbsent(current, m -> new IdentityHashMap<>()).put(element, null);
				}
				current = current.getParent();
			}
		}

		return retVal.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().keySet(), HCollector.mergeFail(), IdentityHashMap::new));
	}

	public Map<Element, List<Modifier>> getClosureMap() {
		final Map<Modifier, Object> closed = new IdentityHashMap<>();
		final Map<Element, List<Modifier>> retVal = new IdentityHashMap<>();

		// Scan the elements backward
		for (int i = getElements().size() - 1; i >= 0; i--) {
			final Element element = getElements().get(i);
			// Look at the all the new modifiers from this element
			final List<Modifier> found = new ArrayList<>();
			// Once we have seen a modifier, we never look at it, or any of it's parents again
			// This works because Modifiers are unique to each ITextModifier activation
			Modifier current = element.getModifier();
			while ((current != null) && !closed.containsKey(current)) {
				if (current.getModifier() != null) found.add(current);
				current = current.getParent();
			}

			if (!found.isEmpty()) {
				retVal.put(element, found);
				found.forEach(m -> closed.put(m, null));
			}
		}

		return retVal;
	}
}
