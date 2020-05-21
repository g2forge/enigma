package com.g2forge.enigma.backend.text.model.modifier;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.text.TextUpdate;

public interface ITextModifier {
	/**
	 * Compute the updates to make based on this modifier.
	 * 
	 * The caller should generally validate that the updates aren't overlapping (e.g. that one removal doesn't remove another). To do this one might sort the
	 * updates and then look for an offset less than offset+length of the previous update.
	 * 
	 * @param list A list of character sequences to be considered as one. Any "gaps" in this list, which the caller may introduce but we cannot discover,
	 *            correspond to regions where other modifiers apply we should not therefore not take action.
	 * @return A list of updates to make.
	 */
	public List<? extends List<? extends TextUpdate<?>>> computeUpdates(List<CharSequence> list);

	/**
	 * When {@code true} the {@link TextNestedModified.TextNestedModifiedBuilder} will ensure that there is always some content in this modifier, even if it is
	 * {@link com.g2forge.enigma.backend.text.model.expression.TextNothing}.
	 * 
	 * @return {@code true} to ensure this modifier is always called.
	 */
	public default boolean isRequireSomething() {
		return false;
	}

	/**
	 * Try to merge as many as possible together. Use an RLE-like scheme to create a minimal modifier.
	 * 
	 * @param modifiers
	 * @return
	 */
	public ITextModifier merge(Iterable<? extends ITextModifier> modifiers);

	public default ITextModifier merge(ITextModifier... modifiers) {
		return merge(HCollection.asList(modifiers));
	}
}
