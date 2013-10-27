package net.sourceforge.vietocr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

public class OCRInputStream {
	
	private String imageFormat;
	
	
	public OCRInputStream(String format){
		imageFormat = format;
	}
	
	public List<IIOImage> getIIOImageListIS(InputStream input) throws IOException {
        File workingTiffFile = null;

        ImageReader reader = null;
        ImageInputStream iis = null;

        try {
            List<IIOImage> iioImageList = new ArrayList<IIOImage>();

            if (imageFormat.matches("(pbm|pgm|ppm)")) {
                imageFormat = "pnm";
            } else if (imageFormat.equals("jp2")) {
                imageFormat = "jpeg2000";
            }
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageFormat);
            reader = readers.next();
            if (reader == null) {
                throw new RuntimeException("Need to install JAI Image I/O package.\nhttps://jai-imageio.dev.java.net");
            }
            iis = new MemoryCacheImageInputStream(input);
            reader.setInput(iis);

            int imageTotal = reader.getNumImages(true);

            for (int i = 0; i < imageTotal; i++) {
              
                IIOImage oimage = reader.readAll(i, reader.getDefaultReadParam());
                iioImageList.add(oimage);
            }

            return iioImageList;
        } finally {
            try {
                if (iis != null) {
                    iis.close();
                }
                if (reader != null) {
                    reader.dispose();
                }
            } catch (Exception e) {
                // ignore
            }
            if (workingTiffFile != null && workingTiffFile.exists()) {
                workingTiffFile.delete();
            }
        }
    }

}
