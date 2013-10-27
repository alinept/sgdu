package br.ufma.sgdu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class Reader2StringConverter {

	public static String get(Reader reader) throws IOException{
		BufferedReader br = new BufferedReader(reader);
		String r = "", cur;
		cur = br.readLine();
		while( cur != null ){
			r += cur;
		}
		return r;
	}
}
