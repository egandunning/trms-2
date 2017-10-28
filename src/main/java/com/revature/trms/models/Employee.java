package com.revature.trms.models;

public class Employee {

	private String firstname;
	private String lastname;
	private String streetAddress;
	private String city;
	private String state;
	private String zip;
	private int superId;
	private String superFirstname;
	private String superLastname;
	private int departmentId;
	private String department;
	private String email;
	private byte[] password;
	private String title;
	
	public Employee() {
		super();
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public int getSuperId() {
		return superId;
	}

	public void setSuperId(int superId) {
		this.superId = superId;
	}

	public String getSuperFirstname() {
		return superFirstname;
	}

	public void setSuperFirstname(String superFirstname) {
		this.superFirstname = superFirstname;
	}

	public String getSuperLastname() {
		return superLastname;
	}

	public void setSuperLastname(String superLastname) {
		this.superLastname = superLastname;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
