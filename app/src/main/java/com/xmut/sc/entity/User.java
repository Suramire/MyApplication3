package com.xmut.sc.entity;

import java.util.Date;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer uid;
	private String img;
	private String username;
	private String password;
	private String sex;
	private Date birth;
	private String email;
	private String phone;
	private String hobbyone;
	private String hobbytwo;
	private String hobbythree;
	private String personality;
	private Integer count;

	// Constructors

	/** def constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String username, String password, String hobbyone,
			String personality) {
		this.username = username;
		this.password = password;
		this.hobbyone = hobbyone;
		this.personality = personality;
	}

	/** full constructor */
	public User(String img, String username, String password, String sex,
			Date birth, String email, String phone, String hobbyone,
			String hobbytwo, String hobbythree, String personality,
			Integer count) {
		this.img = img;
		this.username = username;
		this.password = password;
		this.sex = sex;
		this.birth = birth;
		this.email = email;
		this.phone = phone;
		this.hobbyone = hobbyone;
		this.hobbytwo = hobbytwo;
		this.hobbythree = hobbythree;
		this.personality = personality;
		this.count = count;
	}

	// Property accessors

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

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirth() {
		return this.birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHobbyone() {
		return this.hobbyone;
	}

	public void setHobbyone(String hobbyone) {
		this.hobbyone = hobbyone;
	}

	public String getHobbytwo() {
		return this.hobbytwo;
	}

	public void setHobbytwo(String hobbytwo) {
		this.hobbytwo = hobbytwo;
	}

	public String getHobbythree() {
		return this.hobbythree;
	}

	public void setHobbythree(String hobbythree) {
		this.hobbythree = hobbythree;
	}

	public String getPersonality() {
		return this.personality;
	}

	public void setPersonality(String personality) {
		this.personality = personality;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}