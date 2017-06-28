package com.xmut.sc.entity;

import java.util.Date;

/**
 * Note entity. @author MyEclipse Persistence Tools
 */

public class Note implements java.io.Serializable {

	// Fields

	private Integer nid;
	private Integer uid;
	private String img;
	private String title;
	private String content;
	private Date publishtime;
	private Integer count;
	private Date edittime;
	private String type;
	private String tag;
	private Date viewtime;
	private Boolean isshare;

	// Constructors

	/** def constructor */
	public Note() {
	}

	/** minimal constructor */
	public Note(String title, String content, Date publishtime, Integer count,
			Date edittime, String type, String tag, Date viewtime) {
		this.title = title;
		this.content = content;
		this.publishtime = publishtime;
		this.count = count;
		this.edittime = edittime;
		this.type = type;
		this.tag = tag;
		this.viewtime = viewtime;
	}

	/** full constructor */
	public Note(Integer uid, String img, String title, String content,
			Date publishtime, Integer count, Date edittime, String type,
			String tag, Date viewtime, Boolean isshare) {
		this.uid = uid;
		this.img = img;
		this.title = title;
		this.content = content;
		this.publishtime = publishtime;
		this.count = count;
		this.edittime = edittime;
		this.type = type;
		this.tag = tag;
		this.viewtime = viewtime;
		this.isshare = isshare;
	}

	// Property accessors

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

	public String getImg() {
		return this.img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishtime() {
		return this.publishtime;
	}

	public void setPublishtime(Date publishtime) {
		this.publishtime = publishtime;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getEdittime() {
		return this.edittime;
	}

	public void setEdittime(Date edittime) {
		this.edittime = edittime;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Date getViewtime() {
		return this.viewtime;
	}

	public void setViewtime(Date viewtime) {
		this.viewtime = viewtime;
	}

	public Boolean getIsshare() {
		return this.isshare;
	}

	public void setIsshare(Boolean isshare) {
		this.isshare = isshare;
	}

}