package br.ufma.sgdu.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class InputStream2ByteConverter {
	
	public static byte [] get(InputStream inputStream) throws IOException{
		ArrayList<Byte> byteList = new ArrayList<Byte>();
		
		int b = inputStream.read();
		while(b != -1){
			byteList.add( (byte)b );
			b = inputStream.read();
		}
		
		byte [] byteStream = new byte[byteList.size()];
		Iterator<Byte> iterator = byteList.iterator();
		for(int i=0; iterator.hasNext(); i++){
			byteStream[i] = iterator.next();
		}
		
		return byteStream;
	}

}
