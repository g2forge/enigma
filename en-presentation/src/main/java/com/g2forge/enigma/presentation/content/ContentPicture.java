package com.g2forge.enigma.presentation.content;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContentPicture implements IContent {
	protected final Path path;
}
