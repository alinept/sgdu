package br.ufma.sgdu.entity;

/**
 * <b>Administrator</b></br>
 * Classe que representa uma conta de adminstrador cadastrada no sistema.
 * Armazena o login, o hash md5 da senha, nome, dentre outras informações. Um objeto
 * dessa classe é instanciado toda vez que um administrador faz login no sistema e é
 * repassado para todas as classes detentoras das funcionalidades que ele requerer
 * durante o período que ele estiver logado.
 */
public class Administrator {
	
	/** Constante que define o tipo super administrador. */
	public static final short SUPER_ADM_TYPE = 0x01;
	
	/** Constante que define o tipo administrador normal. */
	public static final short COMMOM_ADM_TYPE = 0x02;
	
	
	private String login;
	private short type;
	private String name;
	private String email;
	private String comment;
	private String md5Passwd;
	private String typeString;
	
	public Administrator(String login, short type, String name, String email,
			String comment, String md5Passwd) {
		super();
		this.login = login;
		this.type = type;
		this.name = name;
		this.email = email;
		this.comment = comment;
		this.md5Passwd = md5Passwd;
		setTypeString();
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getComment() {
		return comment;
	}

	public String getMd5Passwd() {
		return md5Passwd;
	}
	
	public boolean isCommomAdministrator(){
		return type == COMMOM_ADM_TYPE;
	}

	public boolean isSuperAdministrator(){
		return type == SUPER_ADM_TYPE;
	}
	
	public String getTypeString(){
		return typeString;
	}
		
	public void setType(short type) {
		this.type = type;
		setTypeString();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setMd5Passwd(String md5Passwd) {
		this.md5Passwd = md5Passwd;
	}
	
	public String getFormattedName(){
		if( name.length() > 25 ){
			return name.substring(0, 22) + "...";
		}
		return name;
	}
	
	public String getFormattedLogin(){
		if( login.length() > 15 ){
			return login.substring(0, 12) + "...";
		}
		return login;
	}
	
	public String getFormattedEmail(){
		if( email.length() > 23 ){
			return email.substring(0, 20) + "...";
		}
		return email;
	}
	
	private void setTypeString(){
		typeString = isSuperAdministrator()? "Super" : isCommomAdministrator()? "Comum" : "";
	}
	
	

	@Override
	public String toString() {
		return name + " " + login + " " + " " + type + " " + email + " " + comment;
	}
}
