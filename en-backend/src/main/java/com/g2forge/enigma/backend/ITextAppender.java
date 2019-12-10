package com.g2forge.enigma.backend;

import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;

public interface ITextAppender<T> {
	interface IExplicitFactory<C, E> {
		public <T> IFunction1<? super T, ? extends E> create(IConsumer2<? super C, ? super T> consumer);
	}

	public static <E> void addToBuilder(TypeSwitch1.FunctionBuilder<Object, E> builder, IExplicitFactory<? extends ITextAppender<?>, E> factory) {
		builder.add(CharSequence.class, factory.create(ITextAppender::append));
		builder.add(Object.class, factory.create(ITextAppender::append));
		builder.add(Byte.class, factory.create(ITextAppender::append));
		builder.add(Short.class, factory.create(ITextAppender::append));
		builder.add(Integer.class, factory.create(ITextAppender::append));
		builder.add(Long.class, factory.create(ITextAppender::append));
		builder.add(Float.class, factory.create(ITextAppender::append));
		builder.add(Double.class, factory.create(ITextAppender::append));
		builder.add(char[].class, factory.create(ITextAppender::append));
	}

	public T append(boolean bool);

	public T append(byte number);

	public T append(char character);

	public default T append(char[] characters) {
		return append(characters, 0, characters.length);
	}

	public default T append(char[] characters, int offset, int length) {
		return append(new String(characters, offset, length));
	}

	public T append(CharSequence characters);

	public T append(double number);

	public T append(float number);

	public T append(int number);

	public T append(long number);

	public T append(Object object);

	public T append(short number);
}
