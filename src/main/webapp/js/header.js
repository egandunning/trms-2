"use strict";

//Populate header with buttons and register event listeners.
var header = document.getElementsByTagName("header")[0];

header.innerHTML = '<button id="headerInfoButton" class="btn waves-effect waves-light">User Info</button>' +
	'<button id="headerBalanceButton" class="btn waves-effect waves-light">My Balance</button>' +
	'<span style="float:left"><form action="Logout" method="POST"><button id="headerLogoutButton" class="btn waves-effect waves-light">Log Out</button></form></span>' +
	'<button id="headerViewRequestButton" class="btn waves-effect waves-light">View Requests</button><hr>';

//Go to user info page 
document.getElementById("headerInfoButton").addEventListener("click", function() {
	window.location.href = "Employee";
});

//View reimbursement balance
document.getElementById("headerBalanceButton").addEventListener("click", function() {
	alert("Reimbursement Balance\nConfirmed: " + "TODO" +
			"\nPending: " + "TODO" +
			"\nTotal: " + "TODO");
});

//Logout
document.getElementById("headerLogoutButton").addEventListener("click", function() {
	console.log("invalidating cookie")
	document.cookie = '';
});

//View requests
document.getElementById("headerViewRequestButton").addEventListener("click", function() {
	window.location.href = "view.html";
});
