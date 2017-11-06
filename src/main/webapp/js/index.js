"use strict";

document.getElementById("submit").addEventListener("click", newRequest);

function newRequest() {
	
	console.log("in newRequest")
	
	let request = {};
	request.cost = document.getElementById("cost").value;
	request.description = document.getElementById("desc").value;
	request.streetAddress = document.getElementById("streetAddress").value;
	request.city = document.getElementById("city").value;
	request.state = document.getElementById("state").value;
	request.zip = document.getElementById("zip").value;
	request.eventType = document.getElementById("eventType").value;
	request.gradingFormat = document.getElementById("gradingFormat").value;
	request.daysMissed = document.getElementById("daysMissed").value;
	request.justification = document.getElementById("justification").value;
	
	addRequest(request);
}

/**
 * Use AJAX to POST info to request servlet.
 * @param request the request to create on the server.
 * @returns
 */
function addRequest(request) {
	
	console.log(request);
	
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {

			let response = JSON.parse(xhr.responseText);
			
			document.getElementById("alertView").innerHTML = '<font color="blue">Successfully placed reimbursement request.</font>';
			
		} else if(xhr.readyState == 4 && xhr.status != 200) {
			document.getElementById("alertView").innerHTML = '<font color="red">Error with registration.</font>';
		}
	}
	xhr.open("POST", "Request", true);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.send("request="+JSON.stringify(request));
}