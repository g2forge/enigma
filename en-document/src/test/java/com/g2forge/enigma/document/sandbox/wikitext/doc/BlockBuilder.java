package com.g2forge.enigma.document.sandbox.wikitext.doc;

import org.eclipse.mylyn.wikitext.parser.Attributes;
import org.eclipse.mylyn.wikitext.parser.DocumentBuilder.BlockType;
import org.eclipse.mylyn.wikitext.parser.DocumentBuilder.SpanType;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.enigma.document.sandbox.wikitext.Block;
import com.g2forge.enigma.document.sandbox.wikitext.IBlock;
import com.g2forge.enigma.document.sandbox.wikitext.Text;

public class BlockBuilder extends AWikitextBuilder<Block> {
	protected final Block.BlockBuilder builder = Block.builder();

	public BlockBuilder(IFunction1<? super Block, ? extends IWikitextBuilder<?>> closer) {
		super(closer);
	}

	public IWikitextBuilder<Block> beginBlock(BlockType type, Attributes attributes) {
		ensureOpen();
		return new BlockBuilder(closer(block -> builder.content(block)));
	}

	public IWikitextBuilder<Text> beginSpan(SpanType type, Attributes attributes) {
		ensureOpen();
		return new TextBuilder(closer(text -> builder.content(text)));
	}

	@Override
	public Block build() {
		final Block retVal = builder.build();
		// Remove unnecessary nesting of blocks
		if (retVal.getContents().size() == 1) {
			final IBlock only = retVal.getContents().get(0);
			if (only instanceof Block) return (Block) only;
		}
		return retVal;
	}

	@Override
	public IWikitextBuilder<Block> characters(String text) {
		ensureOpen();
		builder.content(new Text(text));
		return this;
	}

	public IWikitextBuilder<?> endBlock() {
		return close();
	}
}
