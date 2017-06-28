package com.xmut.sc.entity;

/**
 * Personality entity. @author MyEclipse Persistence Tools
 */

public class Personality implements java.io.Serializable {

	// Fields

	private Integer cid;
	private String personality;

	// Constructors

	/** def constructor */
	public Personality() {
	}

	/** full constructor */
	public Personality(String personality) {
		this.personality = personality;
	}

	// Property accessors

	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getPersonality() {
		return this.personality;
	}

	public void setPersonality(String personality) {
		this.personality = personality;
	}

}