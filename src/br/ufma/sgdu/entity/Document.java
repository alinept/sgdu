package br.ufma.sgdu.entity;

import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;

import br.ufma.sgdu.util.FileSupportVerificator;

/**
 * <b>Document</b><br/>
 * Classe que representa um documento indexado pelo sistema.
 * Armazena o ID, título, descrição, conteúdo, dentre outras informações.
 * Um objeto dessa classe é instanciado toda vez que o usuário, ou o sistema precisa
 * ter acesso a um documento armazenado no banco.
 */
public class Document {
	
	public static final short MICROSOFT_WORD_TYPE = FileSupportVerificator.DOC_TYPE;
	public static final short HIPERTEXT_MARKUP_TYPE = FileSupportVerificator.HTML_TYPE;
	public static final short PORTABLE_DOCUMENT_FILE_TYPE = FileSupportVerificator.PDF_TYPE;
	public static final short IMAGE_TYPE = FileSupportVerificator.JPEG_TYPE;
	
	protected int id;
	protected String title;
	protected String description;
	protected Date expeditionDate;
	protected Date uploadDate;
	protected String owner;
	protected short type;
	protected List<Tag> tags;	
	
	public Document(int id, String title, String description,
			Date expeditionDate, Date uploadDate, String owner, short type) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.expeditionDate = expeditionDate;
		this.uploadDate = uploadDate;
		this.owner = owner;
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExpeditionDate(Date expeditionDate) {
		this.expeditionDate = expeditionDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Date getExpeditionDate() {
		return expeditionDate;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public String getOwner() {
		return owner;
	}
	
	public String getUploadCalendar(){
		return DateFormat.getDateInstance().format(uploadDate);
	}
	
	public String getExpeditionCalendar(){
		return DateFormat.getDateInstance().format(expeditionDate);
	}
	
	public String getFormatedTitle(){
		if(title.length() > 32){
			return title.substring(0, 29) + "...";
		}
		else return title;
	}
	
	public String getFormatedOwner(){
		if(owner.length() > 11){
			return owner.substring(0,8) + "...";
		}
		else return owner;
	}
	
	public String adminImageSrc(){
		if( type == MICROSOFT_WORD_TYPE ){
			return "images/download.png";
		}
		else return "images/lupa.png";
	}
	
	public short getType(){
		return type;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	
}
