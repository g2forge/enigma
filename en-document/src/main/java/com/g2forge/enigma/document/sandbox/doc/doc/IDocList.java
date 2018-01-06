package com.g2forge.enigma.document.sandbox.doc.doc;

import java.util.Collection;

public interface IDocList<T extends IDocElement> extends IDocElement {
	public Collection<T> getElements();
}
