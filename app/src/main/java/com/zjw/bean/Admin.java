package com.zjw.bean;

public class Admin {
	private String aid;
	private String adminname;
	private String password;
	private Admin(String adminname, String password){
		
		this.adminname=adminname;
		this.password=password;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
