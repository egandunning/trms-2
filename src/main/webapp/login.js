"use strict";

var emailAddress = document.getElementById("email");
emailAddress.addEventListener("click", getDisplayName);

function getDisplayName() {
	
	document.cookie = emailAddress;
	
}