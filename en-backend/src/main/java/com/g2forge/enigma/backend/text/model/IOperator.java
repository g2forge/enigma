package com.g2forge.enigma.backend.text.model;

import java.lang.reflect.Type;

import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.IRenderContext;

public interface IOperator {
	public static <R, T extends IRenderContext<? super R, ?> & ITextAppender<?>> void render(IOperator _this, T context, Iterable<? extends R> operands, Type type) {
		if (_this.getPrefix() != null) context.append(_this.getPrefix());
		final String infix = _this.getInfix();
		boolean first = true;
		for (R operand : operands) {
			if (first) first = false;
			else if (infix != null) context.append(infix);
			context.render(operand, type);
		}
		if (_this.getPostfix() != null) context.append(_this.getPostfix());
	}

	public Object builder();

	public String getInfix();

	public String getPostfix();

	public String getPrefix();

	public boolean isValidNumArguments(int numArguments);
}
