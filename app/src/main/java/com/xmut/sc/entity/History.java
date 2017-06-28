package com.xmut.sc.entity;

import java.util.Date;

/**
 * History entity. @author MyEclipse Persistence Tools
 */

public class History implements java.io.Serializable {

	// Fields

	private Integer hid;
	private Integer uid;
	private Integer nid;
	private Date viewtime;

	// Constructors

	/** def constructor */
	public History() {
	}

	/** full constructor */
	public History(Integer uid, Integer nid, Date viewtime) {
		this.uid = uid;
		this.nid = nid;
		this.viewtime = viewtime;
	}

	// Property accessors

	public Integer getHid() {
		return this.hid;
	}

	public void setHid(Integer hid) {
		this.hid = hid;
	}

	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getNid() {
		return this.nid;
	}

	public void setNid(Integer nid) {
		this.nid = nid;
	}

	public Date getViewtime() {
		return this.viewtime;
	}

	public void setViewtime(Date viewtime) {
		this.viewtime = viewtime;
	}

}