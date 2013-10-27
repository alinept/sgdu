package br.ufma.sgdu.entity;

public class DocumentFile {
	
	private String filename;
	private byte[] byteStream;
	
	public DocumentFile(String filename, byte[] byteStream){
		this.filename = filename;
		this.byteStream = byteStream;
	}
	
	public String getFilename(){
		return filename;
	}
	
	public byte[] getByteStream(){
		return byteStream;
	}

}
