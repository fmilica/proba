$(document).ready(function() {
	$( "#doctorForm" ).submit(function( event ) {
		event.preventDefault();
		var start = $("#start").val()
		var end = $("#end").val() 
		var doctor = {
				username: $("#username").val(),
				password: $("#password").val(),
				role: $("#role").val(),
	            name:$("#name").val(),
	            surname:$("#surname").val(),
	            address: $("#address").val(),
	            city: $("#city").val(),
	            country: $("#country").val(),
	            phoneNumber: $("#phone").val(),
	            securityNumber: $("#security").val(),
	            workingHours: start + "-" + end
	     }
		
		$.ajax({
			type : 'POST',
			url : "http://localhost:8081/api/doctors",
			contentType: "application/json",
			data : JSON.stringify(doctor),
			success : function(response){
				alert("Uspesno prosao poziv")
			},
			error : function() {
				alert("Neuspesno prosao poziv")
			}
		});
	});
});