package com.g2forge.enigma.document.sandbox.doc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import com.g2forge.enigma.document.sandbox.doc.doc.DocKeyedList;
import com.g2forge.enigma.document.sandbox.doc.doc.DocText;
import com.g2forge.enigma.document.sandbox.doc.doc.IDocElement;
import com.g2forge.enigma.document.sandbox.doc.doc.IDocKeyedList;
import com.g2forge.enigma.document.sandbox.doc.doc.IDocText;
import com.g2forge.enigma.document.sandbox.doc.md.IMDRenderable;
import com.g2forge.enigma.document.sandbox.doc.md.MDBlock;
import com.g2forge.enigma.document.sandbox.doc.md.MDList;
import com.g2forge.enigma.document.sandbox.doc.md.MDText;

public class GenerateMDCommandLineHelp {
	public static class Converter {
		protected static final MDText COLON = new MDText(": ");

		protected final Function<IDocElement, IMDRenderable> convert = new ReflectiveDynamicDispatch<>(this).dispatch(IDocElement.class, IMDRenderable.class, "internal");

		public IMDRenderable convert(IDocElement element) {
			return convert.apply(element);
		}

		protected MDList internal(IDocKeyedList<? extends IDocElement, ? extends IDocElement> list) {
			final Collection<IMDRenderable> retVal = new ArrayList<>();
			for (IDocKeyedList.IEntry<? extends IDocElement, ? extends IDocElement> entry : list.getElements()) {
				retVal.add(new MDBlock(convert(entry.getKey()), COLON, convert(entry.getValue())));
			}
			return new MDList(retVal);
		}

		protected MDText internal(IDocText text) {
			return new MDText(text.getText());
		}
	}

	public static void main(String[] args) {
		final DocKeyedList<IDocElement, IDocElement> list = new DocKeyedList<>();
		list.add(new DocText("-x"), new DocText("This is a sentence about the 'x' option!"));
		list.add(new DocText("-y"), new DocText("Some description of another option"));
		final IMDRenderable md = new Converter().convert(list);
		System.out.println(md.toMD());
	}
}
