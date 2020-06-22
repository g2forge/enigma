package com.g2forge.enigma.document.convert.md;

import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.document.model.IDocElement;

@FunctionalInterface
public interface IExplicitMDRenderable extends IDocElement, IExplicitRenderable<IMDRenderContext> {}
