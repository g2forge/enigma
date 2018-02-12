package com.g2forge.enigma.presentation.content.document;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;

public interface IXSLFRenderContext {
	public XSLFTextRun createRun();

	public XSLFTextParagraph getParagraph();

	public XSLFTextShape getShape();

	public ICloseable openParagraph(boolean forceNew);

	public ICloseable openParagraphFormatter(IFunction1<? super List<? extends IConsumer1<XSLFTextParagraph>>, ? extends IConsumer1<XSLFTextParagraph>> function);

	public ICloseable openRunFormatter(IFunction1<? super List<? extends IConsumer1<XSLFTextRun>>, ? extends IConsumer1<XSLFTextRun>> function);

	public IExplicitXSLFElement toExplicit(Object object, Type type);
}
