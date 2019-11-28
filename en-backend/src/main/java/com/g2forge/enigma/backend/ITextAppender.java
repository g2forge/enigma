package com.g2forge.enigma.backend;

public interface ITextAppender<T> {
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
