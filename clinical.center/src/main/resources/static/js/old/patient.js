window.onload = function() {
// prikaz pri ucitavanju
}

$(document).ready(function() {
    // dodavanje pacijenta
    $('#addPatient').click(function() {
    	var pUsername = $('#iUsername').val()
    	var pPassword = $('#iPassword').val()
    	var pRole = $('#iRole').val()
    	var pName = $('#iName').val()
    	var pSurname = $('#iSurname').val()
    	var pCountry = $('#iCountry').val()
    	var pCity = $('#iCity').val()
    	var pAddress = $('#iAddress').val()
    	var pPhoneNumber = $('#iPhoneNum').val()
    	var pSecurityNumber = $('#securityNumber').val()
    	
    	patient = {
    		username: pUsername,
    		password: pPassword,
    		role: pRole,
    		name: pName,
    		surname: pSurname,
    		country: pCountry,
    		city: pCity,
    		address: pAddress,
    		phoneNumber: pPhoneNumber,
    		securityNumber: pSecurityNumber
    	}
    	
    	console.log(JSON.stringify(patient))
    	
    	$.ajax({
    		type : "POST",
    		url : "/api/users/" + "addPatient",
    		contentType : "application/json",
    		dataType: "json",
    		data : JSON.stringify(patient),
    		success : function(response)  {
    			alert(response)
    		},
    		error : function(response) {
    			alert(response)
    		}
    	})
    })
})