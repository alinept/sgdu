package br.ufma.sgdu.util;

public class FileSupportVerificator {
	
	/** Constante que define o tipo: Documento do Microsoft Word */
	public static final short DOC_TYPE = 0x01;
	
	/** Constante que define o tipo: Documento XML do Microsoft Word */
	public static final short DOCX_TYPE = 0x02;
	
	/** Constante que define o tipo: Portable Document Format */
	public static final short PDF_TYPE = 0x03;
	
	/** Constante que define o tipo: Hypertext Markup Language */
	public static final short HTML_TYPE = 0x04;
	
	/** Constante que define o tipo: Portable Network Graphics */
	public static final short PNG_TYPE = 0x05;
	
	/** Constante que define o tipo: Imagem JPEG */
	public static final short JPEG_TYPE = 0x06;
	
	/** Constante que define o tipo: Graphics Interchange File Format */
	public static final short GIF_TYPE = 0x07;
	
	/** Constante que define o tipo: Windows OS/2 Bitmap Graphics */
	public static final short BMP_TYPE = 0x08;
	
	/** Constante que define o tipo: Tagged Image Format File 
	public static final short TIF_TYPE = 0x09; */
	
	/** Constante que define o tipo: PDF (imagem)
	public static final short PDFi_TYPE = 0x10; */
	
	/**
	 * Retorna se o arquivo e suportado pelo sistema.
	 */
	public static boolean isValid(String filename){
		return
			isDocumentValid(filename) || isImageValid(filename);
	}
	
	/**
	 * Retorna se o arquivo e um documento suportado pelo sistema.
	 */
	public static boolean isDocumentValid(String filename){
		return
			isPDFType(filename) || isDOCType(filename) ||
			isDOCXType(filename) || isHTMLType(filename);
	}
	
	/**
	 * Retorna se o arquivo e uma imagem suportada pelo sistema.
	 */
	public static boolean isImageValid(String filename){
		return
			isPNGType(filename) || isJPEGType(filename) ||
			isGIFType(filename) || isBMPType(filename);
	}
	
	/**
	 * Retorna se o arquivo e um Documento do Microsoft Word.
	 */
	public static boolean isDOCType(String filename){
		return filename.endsWith(".doc") || filename.endsWith(".DOC");
	}
	
	/**
	 * Retorna se o arquivo e um Documento XML do Microsoft Word
	 */
	public static boolean isDOCXType(String filename){
		return filename.endsWith(".docx") || filename.endsWith(".DOCX");
	}
	
	/**
	 * Retorna se o arquivo e um Portable Document Format
	 */
	public static boolean isPDFType(String filename){
		return filename.endsWith(".pdf") || filename.endsWith(".PDF");
	}
	
	/**
	 * Retorna se o arquivo e um Hypertext Markup Language
	 */
	public static boolean isHTMLType(String filename){
		return filename.endsWith(".html") || filename.endsWith(".HTML") ||
				filename.endsWith(".htm") || filename.endsWith(".HTM");
	}
	
	public static boolean isPNGType(String filename){
		return filename.endsWith(".png") || filename.endsWith(".PNG");
	}
	
	public static boolean isJPEGType(String filename){
		return filename.endsWith(".jpg") || filename.endsWith(".jpeg") ||
				filename.endsWith(".JPG") || filename.endsWith(".JPEG");
	}
	
	public static boolean isGIFType(String filename){
		return filename.endsWith(".gif") || filename.endsWith(".GIF");
	}
	
	public static boolean isBMPType(String filename){
		return filename.endsWith(".bmp") || filename.endsWith(".BMP");
	}
	
	
	/* public static boolean isTIFType(String filename){
		return filename.endsWith(".tif") || filename.endsWith(".tiff") ||
				filename.endsWith(".TIF") || filename.endsWith(".TIFF");
	} */

	public static String DocDescription() {
		return
			"Documento do Microsoft Word DOC\n" +
			"Documento XML do Microsoft Word DOCX\n" +
			"Portable Document Format PDF\n" +
			"Hypertext Markup Language HTML HTM";
	}
	
	public static String ImageDescription(){
		return
			"Imagem JPEG JPG\n" +
			"Portable Network Graphics PNG\n" +
			"Graphics Interchange File Format GIF\n" +
			"Windows OS/2 Bitmap Graphics BMP";
	}
}
