"use strict";

//Populate header with buttons and register event listeners.
var header = document.getElementsByTagName("header")[0];

header.innerHTML = '<button id="headerInfoButton" class="btn waves-effect waves-light">User Info</button>' +
	'<button id="headerBalanceButton" class="btn waves-effect waves-light">My Balance</button>' +
	'<button id="headerLogoutButton" class="btn waves-effect waves-light">Log Out</button>' +
	'<button id="headerViewRequestButton" class="btn waves-effect waves-light">View Requests</button><hr>';

//Go to user info page 
document.getElementById("headerInfoButton").addEventListener("click", function() {
	window.location.href = "employee.html";
});

//View reimbursement balance
document.getElementById("headerBalanceButton").addEventListener("click", function() {
	alert("Reimbursement Balance\nConfirmed: " + "TODO" +
			"\nPending: " + "TODO" +
			"\nTotal: " + "TODO");
});

//Logout
document.getElementById("headerLogoutButton").addEventListener("click", function() {
	let xhr = new XMLHttpRequest();
	xhr.onreadystatechanged = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {
			window.location.href = "login.html";
		}
		if(xhr.readyState == 4 && xhr.status != 200) {
			alert("Logout failed...");
		}
	}
	xhr.open("POST", "Logout", true);
	xhr.send();
});

//View requests
document.getElementById("headerViewRequestButton").addEventListener("click", function() {
	window.location.href = "view.html";
});
