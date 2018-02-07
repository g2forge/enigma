package com.g2forge.enigma.presentation.content.document;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Stack;

import org.apache.poi.sl.usermodel.AutoNumberingScheme;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.iface.ISingleton;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.typeswitch.TypeSwitch1;
import com.g2forge.enigma.document.Block;
import com.g2forge.enigma.document.IBlock;
import com.g2forge.enigma.document.IListItem;
import com.g2forge.enigma.document.List;
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
				try (final ICloseable closeable = c.openParagraph(false)) {
					c.getParagraph().addNewTextRun().setText(xslf.getText());
				}
			});
			builder.add(Block.class, xslf -> c -> {
				for (IBlock content : xslf.getContents()) {
					c.toExplicit(content, IBlock.class).render(c);
				}
			});
			builder.add(List.class, xslf -> c -> {
				for (int i = 0; i < xslf.getItems().size(); i++) {
					final IListItem item = xslf.getItems().get(i);
					final int index = (List.Marker.Numbered.equals(xslf.getMarker())) ? i + 1 : 0;
					try (final ICloseable formatter = c.openParagraphFormatter(list -> new ListItemParagraphFormatter((int) list.stream().filter(f -> f instanceof ListItemParagraphFormatter).count(), index))) {
						c.toExplicit(item, IListItem.class).render(c);
					}
				}
			});
		}).fallback(xslf -> {
			throw new NotYetImplementedError();
		}).build();

		protected final XSLFTextShape shape;

		protected XSLFTextParagraph paragraph;

		protected final Stack<IConsumer1<XSLFTextParagraph>> formatters = new Stack<>();

		@Override
		public ICloseable openParagraph(boolean forceNew) {
			if (!forceNew && (paragraph != null)) return () -> {};

			if (paragraph != null) throw new IllegalStateException();
			final XSLFTextParagraph mine = shape.addNewTextParagraph();
			if (!formatters.isEmpty()) formatters.peek().accept(mine);
			paragraph = mine;
			return () -> {
				if (getParagraph() != mine) throw new IllegalStateException();
				paragraph = null;
			};
		}

		@Override
		public ICloseable openParagraphFormatter(IFunction1<? super java.util.List<? extends IConsumer1<XSLFTextParagraph>>, ? extends IConsumer1<XSLFTextParagraph>> function) {
			final IConsumer1<XSLFTextParagraph> formatter = function.apply(Collections.unmodifiableList(formatters));
			formatters.push(formatter);
			return () -> {
				if (formatters.peek() != formatter) throw new IllegalStateException();
				formatters.pop();
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
