package com.xmut.sc.entity;

import java.util.Date;

/**
 * Receive entity. @author MyEclipse Persistence Tools
 */

public class Receive implements java.io.Serializable {

	// Fields

	private Integer rid;
	private Integer nid;
	private Integer uid;
	private Date receivetime;
	private String content;

	// Constructors

	/** def constructor */
	public Receive() {
	}

	/** minimal constructor */
	public Receive(Integer nid, Integer uid) {
		this.nid = nid;
		this.uid = uid;
	}

	/** full constructor */
	public Receive(Integer nid, Integer uid, Date receivetime, String content) {
		this.nid = nid;
		this.uid = uid;
		this.receivetime = receivetime;
		this.content = content;
	}

	// Property accessors

	public Integer getRid() {
		return this.rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public Integer getNid() {
		return this.nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Date getReceivetime() {
		return this.receivetime;
	}

	public void setReceivetime(Date receivetime) {
		this.receivetime = receivetime;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}