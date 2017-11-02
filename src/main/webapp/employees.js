"use strict";

var employee;

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
	xhr.send();
}

function setEmployeeInfo() {
	//TODO: Use DOM manipulation to populate form with employee data.
	console.log(employee);
}

