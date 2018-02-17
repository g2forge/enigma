package com.g2forge.enigma.diagram;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.g2forge.enigma.stringtemplate.EmbeddedTemplateRenderer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Data
@Builder
@AllArgsConstructor
public class PUMLContent {
	protected static final String TEMPLATE = "@startuml<\\n><if(control)><control><endif><diagram>@enduml";

	public static Path toPNG(final IPUMLDiagram diagram, final Path file) throws IOException, FileNotFoundException {
		final PUMLContent.PUMLContentBuilder builder = PUMLContent.builder().diagram(diagram);
		builder.control(PUMLControl.builder().shadowing(false).dpi(600).background(PUMLControl.Color.Transparent).build());
		return builder.build().toFile(file, FileFormat.PNG);
	}

	protected final PUMLControl control;

	protected final IPUMLDiagram diagram;

	public Path toFile(Path path, FileFormat format) throws IOException, FileNotFoundException {
		final String filename = path.getFileName().toString() + format.getFileSuffix();
		final Path retVal = path.getParent() != null ? path.getParent().resolve(filename) : Paths.get(filename);

		final String string = EmbeddedTemplateRenderer.DEFAULT.render(this);
		final SourceStringReader reader = new SourceStringReader(string);
		try (final OutputStream os = new BufferedOutputStream(new FileOutputStream(retVal.toFile()))) {
			reader.generateImage(os, new FileFormatOption(format));
		}
		return retVal;
	}
}
