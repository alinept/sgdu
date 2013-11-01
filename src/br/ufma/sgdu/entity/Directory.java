package br.ufma.sgdu.entity;

public class Directory {
	
	private Integer id;
	private String name;
	private Date createDate;
	private User owner;
	private Directory father;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Directory getFather() {
		return father;
	}
	public void setFather(Directory father) {
		this.father = father;
	}
	
	
	
	
}
