package com.g2forge.enigma.web.html.convert;

import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.web.html.model.IHTMLElement;

@FunctionalInterface
public interface IExplicitHTMLRenderable extends IHTMLElement, IExplicitRenderable<IHTMLRenderContext> {}
