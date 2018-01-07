package com.g2forge.enigma.presentation;

import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSimpleShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.typeswitch.TypeSwitch1;
import com.g2forge.alexandria.java.typeswitch.TypeSwitch2;
import com.g2forge.enigma.presentation.content.ContentPicture;
import com.g2forge.enigma.presentation.content.ContentText;
import com.g2forge.enigma.presentation.content.IContent;
import com.g2forge.enigma.presentation.content.IContentManual;
import com.g2forge.enigma.presentation.layout.ILayoutContent;
import com.g2forge.enigma.presentation.layout.ILayoutPresentation;
import com.g2forge.enigma.presentation.layout.ILayoutSlide;
import com.g2forge.enigma.presentation.layout.StandardLayoutPresentation;
import com.g2forge.enigma.presentation.slide.ISlide;
import com.g2forge.enigma.presentation.slide.SlideContent;
import com.g2forge.enigma.presentation.slide.SlideContent2;
import com.g2forge.enigma.presentation.slide.SlideSection;
import com.g2forge.enigma.presentation.slide.SlideTitle;
import com.g2forge.enigma.presentation.text.Font;
import com.g2forge.enigma.presentation.text.Paragraph;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class PresentationBuilder implements ICloseable {
	@AllArgsConstructor
	@Getter
	@ToString
	public static class ContentContext {
		protected final IFunction1<XSLFSimpleShape, ILayoutContent> layout;

		protected final XMLSlideShow ppt;

		protected final XSLFSlide slide;

		protected final Rectangle2D anchor;
	}

	@AllArgsConstructor
	@Getter
	@ToString
	public class SlideContext {
		protected final ILayoutSlide layout;

		protected final XMLSlideShow ppt;

		protected final XSLFSlide slide;

		protected void addContent(int placeholder, final IContent content) {
			final XSLFTextShape shape = getSlide().getPlaceholder(placeholder);
			final Rectangle2D anchor = shape.getAnchor();
			slide.removeShape(shape);
			contentConsumer.accept(new ContentContext(t -> getLayout().getContent(content, t), getPpt(), slide, anchor), content);
		}
	}

	protected final IConsumer2<ContentContext, IContent> contentConsumer = new TypeSwitch2.ConsumerBuilder<ContentContext, IContent>().with(builder -> {
		builder.add(ContentContext.class, ContentText.class, (c, t) -> {
			final XSLFTextBox body = c.getSlide().createTextBox();
			body.setAnchor(c.getAnchor());

			final XSLFTextParagraph paragraph = body.getTextParagraphs().get(0);
			final Paragraph paragraphControl = t.getParagraph();
			if (paragraphControl != null) {
				if (paragraphControl.getSpaceBefore() > 0.0) paragraph.setSpaceBefore(paragraphControl.getSpaceBefore());
			}

			final XSLFTextRun run = paragraph.getTextRuns().get(0);
			final Font fontControl = t.getFont();
			if (fontControl != null) {
				if (fontControl.getFamily() != null) run.setFontFamily(fontControl.getFamily());
				if (fontControl.getSize() > 0.0) run.setFontSize(fontControl.getSize());
			}

			run.setText(t.getText());
		});
		builder.add(ContentContext.class, ContentPicture.class, (c, t) -> {
			final byte[] data;
			try (final InputStream stream = Files.newInputStream(t.getPath())) {
				data = IOUtils.toByteArray(stream);
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
			final XSLFPictureData picture = c.getPpt().addPicture(data, PictureType.PNG);
			final XSLFPictureShape shape = c.getSlide().createPicture(picture);
			c.getLayout().apply(shape).anchor(c.getAnchor(), picture.getImageDimension(), true);
		});
		builder.add(ContentContext.class, IContentManual.class, (c, t) -> t.apply(c));
	}).build();

	@Getter(AccessLevel.PROTECTED)
	protected final ILayoutPresentation layout;

	@Getter(AccessLevel.PROTECTED)
	protected final XMLSlideShow ppt;

	@Getter(AccessLevel.PROTECTED)
	protected final XSLFSlideMaster master;

	protected final IConsumer1<ISlide> slides = new TypeSwitch1.ConsumerBuilder<ISlide>().with(builder -> {
		builder.add(SlideTitle.class, s -> {
			final XSLFSlideLayout layout = getMaster().getLayout(SlideLayout.TITLE);
			final XSLFSlide actual = getPpt().createSlide(layout);
			actual.getPlaceholder(0).setText(s.getTitle());
			actual.getPlaceholder(1).setText(s.getSubtitle());
		});
		builder.add(SlideContent.class, s -> {
			final XSLFSlide actual = createSlideWithTitle(s, SlideLayout.TITLE_AND_CONTENT);
			try (final ILayoutSlide layout = getLayout().getSlide(s)) {
				new SlideContext(layout, getPpt(), actual).addContent(1, s.getContent());
			}
		});
		builder.add(SlideContent2.class, s -> {
			final XSLFSlide actual = createSlideWithTitle(s, SlideLayout.TWO_OBJ);
			try (final ILayoutSlide layout = getLayout().getSlide(s)) {
				final SlideContext context = new SlideContext(layout, getPpt(), actual);
				context.addContent(1, s.getLeft());
				context.addContent(2, s.getRight());
			}
		});
		builder.add(SlideSection.class, s -> {
			{ // Create section header slide
				final XSLFSlideLayout layout = getMaster().getLayout(SlideLayout.SECTION_HEADER);
				final XSLFSlide actual = getPpt().createSlide(layout);
				actual.getPlaceholder(0).setText(s.getTitle());
				actual.getPlaceholder(1).setText(s.getSubtitle());
			}

			// Add the individual slides
			s.getSlides().forEach(PresentationBuilder.this::add);
		});
	}).build();

	public PresentationBuilder(Path template, ILayoutPresentation layout) {
		this.layout = layout == null ? StandardLayoutPresentation.create() : layout;
		try {
			this.ppt = template == null ? new XMLSlideShow() : new XMLSlideShow(new FileInputStream(template.toFile()));
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		this.master = ppt.getSlideMasters().get(0);
	}

	public PresentationBuilder add(ISlide slide) {
		slides.accept(slide);
		return this;
	}

	@Override
	public void close() {
		try {
			ppt.close();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	protected XSLFSlide createSlideWithTitle(ISlide slide, final SlideLayout layout) {
		final XSLFSlide actual = ppt.createSlide(master.getLayout(layout));
		actual.getPlaceholder(0).setText(slide.getTitle());
		if (slide.getSubtitle() != null) {
			final XSLFTextRun subtitle = actual.getPlaceholder(0).addNewTextParagraph().addNewTextRun();
			subtitle.setFontSize(subtitle.getFontSize() * 0.5);
			subtitle.setText(slide.getSubtitle());
		}
		return actual;
	}

	public PresentationBuilder write(Path path) {
		layout.layout();
		try (OutputStream out = Files.newOutputStream(path)) {
			ppt.write(out);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		return this;
	}
}
