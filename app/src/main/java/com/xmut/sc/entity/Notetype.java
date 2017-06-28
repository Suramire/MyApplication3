package com.xmut.sc.entity;

/**
 * Notetype entity. @author MyEclipse Persistence Tools
 */

public class Notetype implements java.io.Serializable {

	// Fields

	private Integer nid;
	private String ntype;

	// Constructors

	/** def constructor */
	public Notetype() {
	}

	/** full constructor */
	public Notetype(String ntype) {
		this.ntype = ntype;
	}

	// Property accessors

	public Integer getNid() {
		return this.nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public String getNtype() {
		return this.ntype;
	}

	public void setNtype(String ntype) {
		this.ntype = ntype;
	}

}