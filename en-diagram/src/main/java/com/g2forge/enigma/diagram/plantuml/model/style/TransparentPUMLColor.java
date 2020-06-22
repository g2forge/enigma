package com.g2forge.enigma.diagram.plantuml.model.style;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class TransparentPUMLColor implements IPUMLColor, ISingleton {
	protected static final TransparentPUMLColor INSTANCE = new TransparentPUMLColor();
	
	public static TransparentPUMLColor create() {
		return INSTANCE;
	}
	
	private TransparentPUMLColor() {}
}
