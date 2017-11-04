"use strict";

var employee = {};
employee.firstname = "Egan"
employee.lastname = "Dunning"
employee.streetAddress="123 street"
employee.city="Herndon"
employee.state="VA"
employee.zip="12343"
employee.superId=4
employee.departmentId=0
employee.email="egan@website.com"
employee.plainPassword="pass"
employee.title="sales lead"

/**
 * AJAX for getting employee info from Employee servlet
 * @param id the employee info to get
 * @returns
 */
function getEmployee(id) {
	if(typeof id == "number") {
		return;
	}
	let xhr = new XMLHttpRequest();
	xhr.onstatechange = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {
			employee = JSON.parse(xhr.responseText);
			setEmployeeInfo();
		}
	}
	xhr.open("GET", "Employee", true);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.send("employeeId=" + id);
}

function setEmployeeInfo() {
	//TODO: Use DOM manipulation to populate form with employee data.
	console.log(employee);
}

/**
 * From fields in form, create and return employee object.
 * @returns an employee object
 */
function getEmployeeInfo() {
	//TODO: grab fields from form to create employee object.
	let emp = {};
	
	return emp;
}

/**
 * Use AJAX to POST info to Employee servlet.
 * @param employee the employee to create on the server.
 * @returns
 */
function addEmployee(employee) {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {
			//TODO: display message
		}
	}
	xhr.open("POST", "Employee", true);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.send("employee="+JSON.stringify(employee));
}

function updateEmployee(employee) {
	
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {
			//TODO: display message
		}
	}
	xhr.open("PUT", "Employee", true);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.send("employee="+JSON.stringify(employee));
}
