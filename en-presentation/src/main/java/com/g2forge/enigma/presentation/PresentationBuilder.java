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
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.type.function.TypeSwitch2;
import com.g2forge.enigma.presentation.content.ContentDoc;
import com.g2forge.enigma.presentation.content.ContentPicture;
import com.g2forge.enigma.presentation.content.ContentText;
import com.g2forge.enigma.presentation.content.IContent;
import com.g2forge.enigma.presentation.content.IContentContext;
import com.g2forge.enigma.presentation.content.IContentExplicit;
import com.g2forge.enigma.presentation.content.document.XSLFRenderer;
import com.g2forge.enigma.presentation.layout.ILayoutPresentation;
import com.g2forge.enigma.presentation.layout.ILayoutSlide;
import com.g2forge.enigma.presentation.layout.StandardLayoutPresentation;
import com.g2forge.enigma.presentation.slide.ISlide;
import com.g2forge.enigma.presentation.slide.ISlideContext;
import com.g2forge.enigma.presentation.slide.ISlideContextGenerator;
import com.g2forge.enigma.presentation.slide.ISlideExplicit;
import com.g2forge.enigma.presentation.slide.ISlideStandard;
import com.g2forge.enigma.presentation.slide.SlideContent1;
import com.g2forge.enigma.presentation.slide.SlideContent2;
import com.g2forge.enigma.presentation.slide.SlideSection;
import com.g2forge.enigma.presentation.slide.SlideTitle;
import com.g2forge.enigma.presentation.text.Font;
import com.g2forge.enigma.presentation.text.Paragraph;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class PresentationBuilder implements IPresentationBuilder {
	@AllArgsConstructor
	@Getter
	@ToString
	protected static class ContentContext implements IContentContext {
		protected final ISlideContext slide;

		protected final XSLFTextShape text;

		@Getter(lazy = true)
		private final Rectangle2D anchor = computeAnchor();

		protected Rectangle2D computeAnchor() {
			final Rectangle2D anchor = getText().getAnchor();
			getSlide().getSlide().removeShape(getText());
			return anchor;
		}
	}

	@AllArgsConstructor
	@Getter
	@ToString
	public static class PresentationContext {
		protected final ILayoutPresentation layout;

		protected final XMLSlideShow show;

		@Getter(lazy = true)
		private final XSLFSlideMaster master = getShow().getSlideMasters().get(0);

		public void add(ISlide slide) {
			slideConsumer.accept(this, slide);
		}

		public SlideContext createContentSlide(ISlideStandard slide, final SlideLayout layout) {
			final XSLFSlide actual = getShow().createSlide(getMaster().getLayout(layout));
			actual.getPlaceholder(0).setText(slide.getTitle());
			if (slide.getSubtitle() != null) {
				final XSLFTextRun subtitle = actual.getPlaceholder(0).addNewTextParagraph().addNewTextRun();
				subtitle.setFontSize(subtitle.getFontSize() * 0.5);
				subtitle.setText(slide.getSubtitle());
			}

			return new SlideContext(getLayout().getSlide(slide), getShow(), actual);
		}

		public XSLFSlide createTitleSlide(ISlideStandard slide, final SlideLayout layout) {
			final XSLFSlide actual = getShow().createSlide(getMaster().getLayout(layout));
			actual.getPlaceholder(0).setText(slide.getTitle());
			actual.getPlaceholder(1).setText(slide.getSubtitle() == null ? " " : slide.getSubtitle());
			return actual;
		}
	}

	@AllArgsConstructor
	@Getter
	@ToString
	protected static class SlideContext implements ISlideContext, ICloseable {
		protected final ILayoutSlide layout;

		protected final XMLSlideShow show;

		protected final XSLFSlide slide;

		@Override
		public void addContent(int placeholder, final IContent content) {
			final XSLFTextShape shape = getSlide().getPlaceholder(placeholder);
			contentConsumer.accept(new ContentContext(this, shape), content);
		}

		@Override
		public void close() {
			getLayout().close();
		}
	}

	@RequiredArgsConstructor
	protected static class SlideContextGenerator implements ISlideContextGenerator, ICloseable {
		protected final PresentationContext presentation;

		protected final ISlide slide;

		protected SlideContext context = null;

		@Override
		public void close() {
			if (context != null) context.close();
		}

		@Override
		public ISlideContext create(SlideLayout layout) {
			if (context == null) create(presentation.getMaster().getLayout(layout));
			return context;
		}

		@Override
		public ISlideContext create(String layout) {
			if (context == null) create(presentation.getMaster().getLayout(layout));
			return context;
		}

		private void create(XSLFSlideLayout layout) {
			final XMLSlideShow show = presentation.getShow();
			context = new SlideContext(presentation.getLayout().getSlide(slide), show, show.createSlide(layout));
		}
	}

	protected static final IConsumer2<IContentContext, IContent> contentConsumer = new TypeSwitch2.ConsumerBuilder<IContentContext, IContent>().with(builder -> {
		builder.add(IContentContext.class, ContentText.class, (c, t) -> {
			final XSLFTextShape body = c.getText();
			body.clearText();

			final XSLFTextParagraph paragraph = body.addNewTextParagraph();
			paragraph.setBullet(false);
			final Paragraph paragraphControl = t.getParagraph();
			if (paragraphControl != null) {
				if (paragraphControl.getSpaceBefore() > 0.0) paragraph.setSpaceBefore(paragraphControl.getSpaceBefore());
			}

			final XSLFTextRun run = paragraph.addNewTextRun();
			final Font fontControl = t.getFont();
			if (fontControl != null) {
				if (fontControl.getFamily() != null) run.setFontFamily(fontControl.getFamily());
				if (fontControl.getSize() > 0.0) run.setFontSize(fontControl.getSize());
			}

			run.setText(t.getText());
		});
		builder.add(IContentContext.class, ContentPicture.class, (c, t) -> {
			final byte[] data;
			try (final InputStream stream = Files.newInputStream(t.getPath())) {
				data = IOUtils.toByteArray(stream);
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
			final XSLFPictureData picture = c.getSlide().getShow().addPicture(data, PictureType.PNG);
			final XSLFPictureShape shape = c.getSlide().getSlide().createPicture(picture);
			c.getSlide().getLayout().getContent(t, shape).anchor(c.getAnchor(), picture.getImageDimension(), true);
		});
		builder.add(IContentContext.class, ContentDoc.class, (c, t) -> {
			final XSLFTextShape body = c.getText();
			XSLFRenderer.create().render(body, t.getDoc());
		});
		builder.add(IContentContext.class, IContentExplicit.class, (c, t) -> t.apply(c));
	}).build();

	protected static final IConsumer2<PresentationContext, ISlide> slideConsumer = new TypeSwitch2.ConsumerBuilder<PresentationContext, ISlide>().with(builder -> {
		builder.add(PresentationContext.class, SlideTitle.class, (p, s) -> p.createTitleSlide(s, SlideLayout.TITLE));
		builder.add(PresentationContext.class, SlideContent1.class, (p, s) -> {
			try (final SlideContext context = p.createContentSlide(s, SlideLayout.TITLE_AND_CONTENT)) {
				context.addContent(1, s.getContent());
			}
		});
		builder.add(PresentationContext.class, SlideContent2.class, (p, s) -> {
			try (final SlideContext context = p.createContentSlide(s, SlideLayout.TWO_OBJ)) {
				context.addContent(1, s.getLeft());
				context.addContent(2, s.getRight());
			}
		});
		builder.add(PresentationContext.class, SlideSection.class, (p, s) -> {
			// Create section header slide
			p.createTitleSlide(s, SlideLayout.SECTION_HEADER);
			// Add the individual slides
			s.getSlides().forEach(p::add);
		});
		builder.add(PresentationContext.class, ISlideExplicit.class, (p, s) -> {
			try (final SlideContextGenerator generator = new SlideContextGenerator(p, s)) {
				s.apply(generator);
			}
		});
	}).build();

	@Getter(AccessLevel.PROTECTED)
	protected final ILayoutPresentation layout;

	@Getter(AccessLevel.PROTECTED)
	protected final XMLSlideShow show;

	public PresentationBuilder() {
		this(null, null);
	}

	public PresentationBuilder(Path template, ILayoutPresentation layout) {
		this.layout = layout == null ? StandardLayoutPresentation.create() : layout;
		try {
			this.show = template == null ? new XMLSlideShow() : new XMLSlideShow(new FileInputStream(template.toFile()));
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	@Override
	public IPresentationBuilder add(ISlide slide) {
		slideConsumer.accept(new PresentationContext(getLayout(), getShow()), slide);
		return this;
	}

	@Override
	public void close() {
		try {
			getShow().close();
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	@Override
	public IPresentationBuilder write(Path path) {
		layout.layout();
		try (OutputStream out = Files.newOutputStream(path)) {
			getShow().write(out);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		return this;
	}
}
