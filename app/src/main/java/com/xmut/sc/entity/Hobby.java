package com.xmut.sc.entity;

/**
 * Hobby entity. @author MyEclipse Persistence Tools
 */

public class Hobby implements java.io.Serializable {

	// Fields

	private Integer hid;
	private String hobby;

	// Constructors

	/** def constructor */
	public Hobby() {
	}

	/** full constructor */
	public Hobby(String hobby) {
		this.hobby = hobby;
	}

	// Property accessors

	public Integer getHid() {
		return this.hid;
	}

	public void setHid(Integer hid) {
		this.hid = hid;
	}

	public String getHobby() {
		return this.hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

}