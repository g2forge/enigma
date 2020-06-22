package com.g2forge.enigma.web.css.convert;

import com.g2forge.enigma.backend.convert.IExplicitRenderable;

@FunctionalInterface
public interface IExplicitCSSRenderable extends ICSSRenderable, IExplicitRenderable<ICSSRenderContext> {}
