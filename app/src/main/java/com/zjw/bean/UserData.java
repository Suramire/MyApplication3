package com.zjw.bean;

import java.util.Date;


public class UserData {
	private int uid;
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
	private int count;
	public  UserData(String username, String password, String hobbyone, String personality) {
		this.username=username;
		this.password=password;
		this.hobbyone=hobbyone;
		this.personality=personality;
		
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getHobbyone() {
		return hobbyone;
	}
	public void setHobbyone(String hobbyone) {
		this.hobbyone = hobbyone;
	}
	public String getHobbytwo() {
		return hobbytwo;
	}
	public void setHobbytwo(String hobbytwo) {
		this.hobbytwo = hobbytwo;
	}
	public String getHobbythree() {
		return hobbythree;
	}
	public void setHobbythree(String hobbythree) {
		this.hobbythree = hobbythree;
	}
	public String getPersonality() {
		return personality;
	}
	public void setPersonality(String personality) {
		this.personality = personality;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
