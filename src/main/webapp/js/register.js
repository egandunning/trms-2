"use strict";

$(document).ready(function() {
	console.log("loading material select");
    $('select').material_select();
});

window.onload = function() {
	populateDepartments();
}

function populateDepartments() {
	let dropdown = document.getElementById("department");
	
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {
			console.log(xhr.responseText);
			addDepartmentsToList(xhr.responseText);
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
		htmlString += "<option value=\"" + item.id + "\">" + item.name + "</option>";
		
	}
	console.log(htmlString);
	document.getElementById("department").innerHTML = htmlString;

	console.log("loading material select");
    $('select').material_select();
}

document.getElementById("submit").addEventListener("click", newEmployee);

function newEmployee() {
	
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
	
	addEmployee(employee);
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

			let response = JSON.parse(xhr.responseText);
			if(response.info == "Registration complete.") {
				window.location.href = "index.html";
			} else {
				document.getElementById("alertView").innerHTML = '<font color="red">Error with registration.</font>';
			}
			
		} else if(xhr.readyState == 4 && xhr.status != 200) {
			document.getElementById("alertView").innerHTML = '<font color="red">Error with registration.</font>';
		}
	}
	xhr.open("POST", "Employee", true);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.send("employee="+JSON.stringify(employee));
}

