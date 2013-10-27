package br.ufma.sgdu.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import javax.imageio.IIOImage;

import net.sourceforge.vietocr.OCR;
import net.sourceforge.vietocr.OCRInputStream;
import net.sourceforge.vietocr.utilities.ImageIOHelper;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.htmlparser.beans.StringBean;

/**
 * <b>DocumentAnalizer</b><br/>
 * Classe especializada na leitura e extração de dados de arquivos de formatos comerciais
 * de documentos de texto, tais como pdf, doc e docx e também de imagens de documentos
 * escaneados, para tal tarefa essa classe utiliza bibliotecas open-source de manipulação
 * de documentos e OCR.
 */
public class DocumentAnalizer {
	
	/**
	 * Retorna o texto do documento. Nao aconselhavel para tipos HTML, para tais prefira
	 * {@link #getText(String, short)}
	 * @param inputStream stream de bytes do arquivo a ser analizado.
	 * @param type tipo do arquivo: uma das constantes de {@link FileSupportVerificator}
	 * @see FileSupportVerificator#isDocumentValid(String)
	 */
	public static String getText(InputStream inputStream, short type) throws IOException{
		switch (type) {
			case FileSupportVerificator.DOC_TYPE:	return DOC2Text(inputStream);
			case FileSupportVerificator.DOCX_TYPE:	return DOCX2Text(inputStream);
			case FileSupportVerificator.PDF_TYPE:	return PDF2Text(inputStream);
			case FileSupportVerificator.HTML_TYPE:
				return getText( new String(InputStream2ByteConverter.get(inputStream)), type);
			default: return null;
		}
	}
	
	/**
	 * Retorna o texto do documento. Adequado para documentos HTML.
	 * @param inputStream stream de bytes do arquivo a ser analizado.
	 * @param type tipo do arquivo, precisa ser
	 * {@link FileSupportVerificator#HTML_TYPE}
	 * @see FileSupportVerificator#isDocumentValid(String)
	 */
	public static String getText(String stream, short type) throws IOException{
		switch (type) {
			case FileSupportVerificator.HTML_TYPE:	return HTML2Text(stream);
			case FileSupportVerificator.DOC_TYPE:
			case FileSupportVerificator.DOCX_TYPE:
			case FileSupportVerificator.PDF_TYPE:
				byte [] bytes = stream.getBytes();
				return getText(new ByteArrayInputStream(bytes), type);
			default: return null;
		}
	}
	
	/**
	 * Extrai texto de documento.<br/>
	 * Prefira {@link #getText(InputStream, short) ou {@link #getText(String, short)}}
	 * @see FileSupportVerificator#isDocumentValid(String)
	 */
	@Deprecated
	public static String getText(Reader reader, short type) throws IOException{
		switch(type){
			case FileSupportVerificator.HTML_TYPE:
			case FileSupportVerificator.DOC_TYPE:
			case FileSupportVerificator.DOCX_TYPE:
			case FileSupportVerificator.PDF_TYPE:
				return getText(Reader2StringConverter.get(reader), type);
			
			default: return null;
		}
	}
	
	/**
	 * Extrai o texto da imagem.
	 * @see FileSupportVerificator#isImageValid(String)
	 */
	public static String getTextOfImage(byte [] byteStream, short type) throws Exception{
		ByteArrayInputStream imageStream = new ByteArrayInputStream(byteStream);
		return getTextOfImage(imageStream, type);
	}
	
	/**
	 * Extrai o texto da imagem.
	 * @see FileSupportVerificator#isImageValid(String)
	 */
	public static String getTextOfImage(InputStream imageStream, short type) throws Exception{
		switch (type) {
			case FileSupportVerificator.PNG_TYPE:	return PNG2Text(imageStream);
			case FileSupportVerificator.JPEG_TYPE:	return JPEG2Text(imageStream);
			case FileSupportVerificator.GIF_TYPE:	return GIF2Text(imageStream);
			case FileSupportVerificator.BMP_TYPE:	return BMP2Text(imageStream);
			default: return null;
		}
	}
		
	// DOC
	private static String DOC2Text(InputStream inputStream) throws IOException{
		HWPFDocument wdoc = new HWPFDocument(inputStream);
		WordExtractor extractor = new WordExtractor(wdoc);
		return extractor.getText();
	}
	
	// DOCX
	private static String DOCX2Text(InputStream inputStream) throws IOException{
		XWPFDocument wdoc = new XWPFDocument(inputStream);
		XWPFWordExtractor extractor = new XWPFWordExtractor(wdoc);
		return extractor.getText();
	}
	
	// PDF (texto)
	private static String PDF2Text(InputStream inputStream) throws IOException{
		PDFParser parser = new PDFParser(inputStream);
		parser.parse();
		PDDocument pdfDocument = parser.getPDDocument();
		PDFTextStripper stripper = new PDFTextStripper();
		return stripper.getText(pdfDocument);
	}
	
	// HTML
	private static String HTML2Text(String stream) throws IOException{
		StringBean sb = new StringBean();
		sb.setLinks(false);
		sb.setReplaceNonBreakingSpaces(true);
		sb.setCollapse(true);
		sb.setURL(stream);
		return sb.getStrings();
	}
	
	// PNG
	private static String PNG2Text(InputStream imageStream) throws Exception{
		return OCR(imageStream, "png");
	}
	
	// JPEG
	private static String JPEG2Text(InputStream imageStream) throws Exception{
		return OCR(imageStream, "jpg");
	}
	
	// BMP
	private static String BMP2Text(InputStream imageStream) throws Exception{
		return OCR(imageStream, "bmp");
	}
	
	// GIF
	private static String GIF2Text(InputStream imageStream) throws Exception{
		return OCR(imageStream, "gif");
	}
	
	// IMAGEM
	private static String OCR(InputStream imageStream, String format) throws Exception{
		String curLangCode = "por";
		String tessPath = "/usr/bin";
		
		List<File> tempTiffFiles = null;
		OCR ocrEngine = new OCR(tessPath);
		OCRInputStream ocrIS = new OCRInputStream(format);
		List<IIOImage> iioImageList = ocrIS.getIIOImageListIS(imageStream);
		tempTiffFiles = ImageIOHelper.createTiffFiles(iioImageList, -1);
		String result = ocrEngine.recognizeText(tempTiffFiles, curLangCode);
		return result;
	}
}
