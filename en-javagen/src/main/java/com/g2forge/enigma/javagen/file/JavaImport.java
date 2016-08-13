package com.g2forge.enigma.javagen.file;

import com.g2forge.enigma.javagen.type.JavaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaImport {
	protected static final String TEMPLATE = "import <type>;";

	protected JavaType type;
}
