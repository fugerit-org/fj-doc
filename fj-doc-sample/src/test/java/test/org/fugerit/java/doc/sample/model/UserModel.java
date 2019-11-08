package test.org.fugerit.java.doc.sample.model;

import java.io.Serializable;

public class UserModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6700479196493275852L;

	public UserModel(String title, String name, String surname) {
		super();
		this.title = title;
		this.name = name;
		this.surname = surname;
	}

	private String title;
	
	private String name;
	
	private String surname;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	
}
