package com.g2forge.enigma.document.convert;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.mylyn.wikitext.parser.Attributes;
import org.eclipse.mylyn.wikitext.parser.DocumentBuilder;
import org.eclipse.mylyn.wikitext.parser.ImageAttributes;
import org.eclipse.mylyn.wikitext.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.parser.markup.MarkupLanguage;

import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IPredicate1;
import com.g2forge.enigma.document.Block;
import com.g2forge.enigma.document.DocList;
import com.g2forge.enigma.document.Emphasis;
import com.g2forge.enigma.document.IBlock;
import com.g2forge.enigma.document.ISpan;
import com.g2forge.enigma.document.Image;
import com.g2forge.enigma.document.Link;
import com.g2forge.enigma.document.Section;
import com.g2forge.enigma.document.Text;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class WikitextDocumentBuilder extends DocumentBuilder {
	protected static class BlockDOMBuilder implements IDOMBuilder {
		@Getter
		protected final Block.Type type;

		protected final Block.BlockBuilder builder = Block.builder();

		public BlockDOMBuilder(Block.Type type) {
			this.type = type;
			builder.type(type);
		}

		@Override
		public void accept(IDocElement element) {
			builder.content((IBlock) element);
		}

		@Override
		public IDocElement build() {
			return builder.build();
		}
	}

	protected static class BulletedListDOMBuilder implements IBlockDOMBuilder {
		protected final DocList.DocListBuilder builder = DocList.builder();

		public BulletedListDOMBuilder(DocList.Marker marker) {
			builder.marker(marker);
		}

		@Override
		public void accept(IDocElement element) {
			builder.item((IBlock) element);
		}

		@Override
		public IDocElement build() {
			return builder.build();
		}
	}

	protected static class EmphasisDOMBuilder implements IDOMBuilder {
		@Getter
		protected final Emphasis.Type type;

		protected final Emphasis.EmphasisBuilder builder = Emphasis.builder();

		public EmphasisDOMBuilder(Emphasis.Type type) {
			this.type = type;
			builder.type(type);
		}

		@Override
		public void accept(IDocElement element) {
			builder.span((ISpan) element);
		}

		@Override
		public IDocElement build() {
			return builder.build();
		}
	}

	protected interface IBlockDOMBuilder extends IDOMBuilder {}

	protected interface IDOMBuilder extends IConsumer1<IDocElement> {
		public void accept(IDocElement element);

		public IDocElement build();
	}

	@RequiredArgsConstructor
	@Getter
	protected static class SectionDOMBuilder implements IDOMBuilder {
		protected final int level;

		protected ISpan title = null;

		protected final List<IBlock> body = new ArrayList<>();

		@Override
		public void accept(IDocElement element) {
			if (getTitle() == null) title = ((ISpan) element);
			else body.add((IBlock) element);
		}

		@Override
		public IDocElement build() {
			final IBlock body = getBody().size() == 1 ? HCollection.getOne(getBody()) : new Block(Block.Type.Block, getBody());
			return new Section(title, body);
		}
	}

	public static Block parse(MarkupLanguage language, Reader content) throws IOException {
		final WikitextDocumentBuilder builder = new WikitextDocumentBuilder();
		final MarkupParser parser = new MarkupParser(language, builder);
		parser.parse(content);
		return builder.getDocument();
	}

	public static Block parse(MarkupLanguage language, String content) {
		final WikitextDocumentBuilder builder = new WikitextDocumentBuilder();
		final MarkupParser parser = new MarkupParser(language, builder);
		parser.parse(content);
		return builder.getDocument();
	}

	protected final Stack<IDOMBuilder> stack = new Stack<>();

	@Getter
	protected Block document;

	@Override
	public void acronym(String text, String definition) {
		throw new NotYetImplementedError();
	}

	@Override
	public void beginBlock(BlockType type, Attributes attributes) {
		switch (type) {
			case BULLETED_LIST:
				stack.push(new BulletedListDOMBuilder(DocList.Marker.Ordered));
				break;
			case NUMERIC_LIST:
				stack.push(new BulletedListDOMBuilder(DocList.Marker.Numbered));
				break;
			case LIST_ITEM:
				stack.push(new BlockDOMBuilder(Block.Type.ListItem));
				break;
			case PARAGRAPH:
				stack.push(new BlockDOMBuilder(Block.Type.Paragraph));
				break;
			default:
				throw new NotYetImplementedError(String.format("Block type \"%1$s\" is not supported yet!", type));
		}
	}

	@Override
	public void beginDocument() {
		if (!stack.isEmpty()) throw new IllegalStateException();
		stack.push(new BlockDOMBuilder(Block.Type.Document));
	}

	@Override
	public void beginHeading(int level, Attributes attributes) {
		final IDOMBuilder top = stack.peek();
		if (top instanceof SectionDOMBuilder) {
			if (level <= ((SectionDOMBuilder) top).getLevel()) {
				pop();
			}
		}
		stack.push(new SectionDOMBuilder(level));
	}

	@Override
	public void beginSpan(SpanType type, Attributes attributes) {
		switch (type) {
			case EMPHASIS:
				stack.push(new EmphasisDOMBuilder(Emphasis.Type.Emphasis));
				break;
			case STRONG:
				stack.push(new EmphasisDOMBuilder(Emphasis.Type.Strong));
				break;
			case CODE:
				stack.push(new EmphasisDOMBuilder(Emphasis.Type.Code));
				break;
			default:
				throw new NotYetImplementedError(String.format("Span type \"%1$s\" is not supported yet!", type));
		}
	}

	@Override
	public void characters(String text) {
		stack.peek().accept(new Text(text));
	}

	@Override
	public void charactersUnescaped(String literal) {
		throw new NotYetImplementedError();
	}

	@Override
	public void endBlock() {
		pop();
	}

	@Override
	public void endDocument() {
		if (document != null) throw new IllegalStateException();
		popTo(b -> (b instanceof BlockDOMBuilder) && ((BlockDOMBuilder) b).getType().equals(Block.Type.Document));
		if (stack.size() > 1) throw new IllegalStateException();
		document = (Block) stack.pop().build();
	}

	@Override
	public void endHeading() {}

	@Override
	public void endSpan() {
		pop();
	}

	@Override
	public void entityReference(String entity) {
		throw new NotYetImplementedError();
	}

	@Override
	public void image(Attributes attributes, String url) {
		stack.peek().accept(new Image(((ImageAttributes) attributes).getAlt(), url));
	}

	@Override
	public void imageLink(Attributes linkAttributes, Attributes imageAttributes, String href, String imageUrl) {
		throw new NotYetImplementedError();
	}

	@Override
	public void lineBreak() {
		throw new NotYetImplementedError();
	}

	@Override
	public void link(Attributes attributes, String hrefOrHashName, String text) {
		stack.peek().accept(new Link(hrefOrHashName, new Text(text)));
	}

	protected void pop() {
		final IDocElement element = stack.pop().build();
		stack.peek().accept(element);
	}

	protected void popTo(IPredicate1<? super IDOMBuilder> predicate) {
		while (!stack.isEmpty() && !predicate.test(stack.peek())) {
			pop();
		}
	}
}
