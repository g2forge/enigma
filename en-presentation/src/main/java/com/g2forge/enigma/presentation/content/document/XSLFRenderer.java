package com.g2forge.enigma.presentation.content.document;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Stack;

import org.apache.poi.sl.usermodel.AutoNumberingScheme;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.iface.ISingleton;
import com.g2forge.alexandria.java.enums.EnumException;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.typeswitch.TypeSwitch1;
import com.g2forge.enigma.document.Block;
import com.g2forge.enigma.document.DocList;
import com.g2forge.enigma.document.Emphasis;
import com.g2forge.enigma.document.IBlock;
import com.g2forge.enigma.document.IDocListItem;
import com.g2forge.enigma.document.ISpan;
import com.g2forge.enigma.document.Text;

import lombok.Data;
import lombok.EqualsAndHashCode;

public class XSLFRenderer implements ISingleton {
	@Data
	protected static class ListItemParagraphFormatter implements IConsumer1<XSLFTextParagraph> {
		protected final int level;

		protected final int index;

		@Override
		public void accept(XSLFTextParagraph p) {
			p.setIndentLevel(getLevel());
			if (index <= 0) p.setBullet(true);
			else p.setBulletAutoNumber(AutoNumberingScheme.alphaLcParenRight, index);
		}
	}

	@Data
	@EqualsAndHashCode(of = "shape")
	protected static class XSLFRenderContext implements IXSLFRenderContext {
		protected static final IFunction1<Object, IExplicitXSLFElement> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitXSLFElement>().with(builder -> {
			builder.add(IExplicitXSLFElement.class, IFunction1.identity());
			builder.add(Text.class, xslf -> c -> {
				c.openParagraph(false);
				c.createRun().setText(xslf.getText());
			});
			builder.add(Emphasis.class, xslf -> c -> {
				try (final ICloseable closeable = c.openRunFormatter(formatters -> run -> {
					switch (xslf.getType()) {
						case Strong:
							run.setBold(true);
							break;
						case Monospace:
						case Code:
							run.setFontFamily("Courier New");
							break;
						default:
							throw new EnumException(Emphasis.Type.class, xslf.getType());
					}
				})) {
					c.toExplicit(xslf.getSpan(), ISpan.class).render(c);
				}
			});
			builder.add(Block.class, xslf -> c -> {
				for (IBlock content : xslf.getContents()) {
					c.toExplicit(content, IBlock.class).render(c);
				}
			});
			builder.add(DocList.class, xslf -> c -> {
				for (int i = 0; i < xslf.getItems().size(); i++) {
					final IDocListItem item = xslf.getItems().get(i);
					final int index = (DocList.Marker.Numbered.equals(xslf.getMarker())) ? i + 1 : 0;
					try (final ICloseable formatter = c.openParagraphFormatter(list -> new ListItemParagraphFormatter((int) list.stream().filter(f -> f instanceof ListItemParagraphFormatter).count(), index))) {
						try (final ICloseable closeable = c.openParagraph(true)) {
							c.toExplicit(item, IDocListItem.class).render(c);
						}
					}
				}
			});
		}).fallback(xslf -> {
			throw new NotYetImplementedError(String.format("Cannot translate \"%1$s\" with type %2$s into office text (yet)!", xslf, xslf != null ? xslf.getClass() : null));
		}).build();

		protected final XSLFTextShape shape;

		protected XSLFTextParagraph paragraph;

		protected final Stack<IConsumer1<XSLFTextParagraph>> paragraphFormatters = new Stack<>();

		protected final Stack<IConsumer1<XSLFTextRun>> runFormatters = new Stack<>();

		@Override
		public XSLFTextRun createRun() {
			final XSLFTextRun retVal = getParagraph().addNewTextRun();
			if (!runFormatters.isEmpty()) runFormatters.peek().accept(retVal);
			return retVal;
		}

		@Override
		public ICloseable openParagraph(boolean forceNew) {
			if (!forceNew && (paragraph != null)) return () -> {};

			final XSLFTextParagraph mine = shape.addNewTextParagraph();
			if (!paragraphFormatters.isEmpty()) paragraphFormatters.peek().accept(mine);
			paragraph = mine;
			return () -> paragraph = null;
		}

		@Override
		public ICloseable openParagraphFormatter(IFunction1<? super java.util.List<? extends IConsumer1<XSLFTextParagraph>>, ? extends IConsumer1<XSLFTextParagraph>> function) {
			final IConsumer1<XSLFTextParagraph> formatter = function.apply(Collections.unmodifiableList(paragraphFormatters));
			paragraphFormatters.push(formatter);
			return () -> {
				if (paragraphFormatters.peek() != formatter) throw new IllegalStateException();
				paragraphFormatters.pop();
			};
		}

		@Override
		public ICloseable openRunFormatter(IFunction1<? super java.util.List<? extends IConsumer1<XSLFTextRun>>, ? extends IConsumer1<XSLFTextRun>> function) {
			final IConsumer1<XSLFTextRun> formatter = function.apply(Collections.unmodifiableList(runFormatters));
			runFormatters.push(formatter);
			return () -> {
				if (runFormatters.peek() != formatter) throw new IllegalStateException();
				runFormatters.pop();
			};
		}

		@Override
		public IExplicitXSLFElement toExplicit(Object element, Type type) {
			return toExplicit.apply(element);
		}
	}

	protected static final XSLFRenderer instance = new XSLFRenderer();

	public static XSLFRenderer create() {
		return instance;
	}

	public void render(XSLFTextShape shape, Object element) {
		shape.clearText();
		final XSLFRenderContext context = new XSLFRenderContext(shape);
		context.toExplicit(element, null).render(context);
	}
}
