"use strict";

window.onload = function() {
	populateDepartments();

	document.getElementById("submit").addEventListener("click", updateEmployee);
}

var alertView = document.getElementById("alertView");

function populateDepartments() {
	let dropdown = document.getElementById("department");
	
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {
			console.log(xhr.responseText);
			addDepartmentsToList(xhr.responseText);
			setEmployeeInfo();
		}
	}
	
	xhr.open("GET", "Department", true);
	xhr.send();
}

function addDepartmentsToList(data) {
	
	//Get javascript object from JSON data
	data = JSON.parse(data);
	
	//Check type of data
	if(data.__proto__ != [].__proto__) {
		console.log("Bad data from department servlet");
		return;
	}
	
	//Create inner html for the select input
	let htmlString = "";
	
	let i = 0;
	let listSize = data.length;
	for(i; i<listSize; i++) {
		let item = data[i];
		//<option value="department id">Department name</option>
		htmlString += "<option id=\"" + item.id + "\" value=\"" + item.id + "\">" + item.name + "</option>";
		console.log(htmlString);
	}
	
	document.getElementById("department").innerHTML = htmlString;
}

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

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function setEmployeeInfo() {
	
	let empData = document.cookie;
	let employee = JSON.parse(empData);
	document.getElementById("firstName").setAttribute("value", employee.firstname);
	document.getElementById("lastName").setAttribute("value", employee.lastname);
	document.getElementById("address").setAttribute("value", employee.streetAddress);
	document.getElementById("city").setAttribute("value", employee.city);
	document.getElementById("state").setAttribute("value", employee.state);
	document.getElementById("zipcode").setAttribute("value", employee.zip);
	document.getElementById("superId").setAttribute("value", employee.superId);
	document.getElementById("email").setAttribute("value", employee.email);
	document.getElementById("title").setAttribute("value", employee.title);
	document.getElementById('"' + employee.departmentId +'"').setAttribute("selected");
	console.log(empData);
	document.cookie = "";
}

/**
 * From fields in form, create and return employee object.
 * @returns an employee object
 */
function getEmployeeInfo() {
	
	console.log("in getEmployeeInfo");
	
	let pass1 = document.getElementById("password").value;
	let pass2 = document.getElementById("password2").value;
	
	if(pass1 != pass2) {
		document.getElementById("alertView").innerHTML = '<font color="red">Passwords must match</font>';
		return;
	}
	
	let employee = {};
	employee.firstname = document.getElementById("firstName").value;
	employee.lastname =  document.getElementById("lastName").value;
	employee.streetAddress = document.getElementById("address").value;
	employee.city = document.getElementById("city").value;
	employee.state = document.getElementById("state").value;
	employee.zip = document.getElementById("zipcode").value;
	employee.superId = document.getElementById("superId").value;
	employee.departmentId = document.getElementById("department").value;
	employee.email = document.getElementById("email").value;
	employee.plainPassword = pass1;
	employee.title = document.getElementById("title").value;
	return employee;
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

function updateEmployee() {
	
	console.log("in updateEmployee");
	
	let employee = getEmployeeInfo();
	console.log("employee: " + JSON.stringify(employee));
	
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {
			alertView.innerHTML = '<font color="blue">Successful update.</font>';
		} else if(xhr.readyState == 4 && xhr.status != 200) {
			alertView.innerHTML = '<font color="red">Failed to update info.</font>';
		}
	}
	xhr.open("POST", "UpdateEmployee", true);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.send("employee="+JSON.stringify(employee));
}
