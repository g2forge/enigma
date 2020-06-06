package com.g2forge.enigma.diagram.dot.model;

import java.util.List;

public interface IDotAttributed {
	public static interface IDotAttributedBuilder<B> {
		public B attribute(IDotAttribute attribute);

		public default B attribute$(String name, String value) {
			return attribute(new DotAttribute(name, value));
		}
	}

	public List<IDotAttribute> getAttributes();
}
