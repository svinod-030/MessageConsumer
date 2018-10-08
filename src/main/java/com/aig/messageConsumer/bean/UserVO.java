package com.aig.messageConsumer.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserVO  implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
    private String id;
	
	@JsonProperty("userName")
    private String userName;
	
	@JsonProperty("mail")
	private String mail;

	public UserVO() {
		super();
	}
	
	public UserVO(String id, String userName, String mail) {
		super();
		this.id = id;
		this.userName = userName;
		this.mail = mail;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public String toString() {
		return "UserVO [id=" + id + ", userName=" + userName + ", mail=" + mail + "]";
	}
	
	
}
