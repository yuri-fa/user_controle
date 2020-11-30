package com.yuri.spring.angular.mongo.ws.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Verificatiotoken")
public class VerificationToken implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final int EXPIRATION = 60*60*24;
	
	@Id
	private String id;
	private String token;
	private Date expiryDate;
	@DBRef(lazy=true)
	private User user;
	
	public VerificationToken() {

	}
	
	public VerificationToken(String token) {
		super();
		this.token = token;
		this.expiryDate = calculeteExpiryDate(EXPIRATION);
	}
	
	public VerificationToken(final String token,final User user) {
		super();
		this.token = token;
		this.user = user;
		this.expiryDate = calculeteExpiryDate(EXPIRATION);
	}
	
	public void updateToken(final String token) {
		this.token = token;
		this.expiryDate = calculeteExpiryDate(EXPIRATION);
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true; 
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VerificationToken other = (VerificationToken) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	private Date calculeteExpiryDate(final int expiryTime) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTimeInMillis(new Date().getTime());
		calendario.add(Calendar.MINUTE, expiryTime);
		return new Date(calendario.getTime().getTime());
	}

}
