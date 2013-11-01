package br.ufma.sgdu.entity;

public class User {
	
	private Integer id;
	private String login;
	private String name;
	private String email;
	private String password;
	private AcessType acessType;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public AcessType getAcessType() {
		return acessType;
	}
	public void setAcessType(AcessType acessType) {
		this.acessType = acessType;
	}

}
