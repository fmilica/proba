
// ako su personalni podaci editovani, ponovo saljemo upit serveru
var edited = false;
var verifyMedicine;
var vacationTable;
var current_secNum;
var medicalRecord_id;
var patientsTable;
var medicalReportTable;

var calendarNurse;

$(document).ready(function(){

	// postavljanje maksimalnog datuma rodjenja koji se moze odabrati na danas
	document.getElementById("dateOfBirthEdit").max = new Date().toISOString().split("T")[0];

	/*------------------------------------------------------------------------*/
	/*View profile*/
	$('.n-profile').on('click', function(e){
		e.preventDefault()
		viewPersonalInformation()
	})
	
	/*View all patients*/
	$('#nursePatients').click(function(event) {
		event.preventDefault()
		viewAllPatients()
	})
	
	/*Filter patients*/
	$('#filterPatients').click(function(e) {
		e.preventDefault()
		filterPatients()
	});
	
	// vracanje na prikaz svih pacijenata
	$('#patientBack').click(function() {
		$('.content').hide()
		$('.nurse-patients').show()
		// scroll to top of page
		document.body.scrollTop = 0
		document.documentElement.scrollTop = 0
	})
	
	// pretplata svih elemenata tabele na dogadjaj
	$('body').on('click', 'button.table-button-patient', function() {
		var secNum = $(this).attr('id')
		current_secNum = secNum
		viewPatientProfile(secNum)
	});

	/*Change personal information*/
	$('#editNurseProfile').on('click', function(e){
		e.preventDefault()
		$('.content').hide()
		$('.nurse-edit-profile').show()
		document.body.scrollTop = 0
        document.documentElement.scrollTop = 0
		editPersonalInformation()
	})

	$('#confirmEdit').click(function(e) {
		e.preventDefault()
		saveUpdatedProfile()
	})

	$('#cancelEdit').click(function(e) {
		e.preventDefault()
		$('.content').hide()
		$('.nurse-profile').show()
		document.body.scrollTop = 0
        document.documentElement.scrollTop = 0
	})

	/*Change password*/
	$('#changePasswordBtn').click(function(e) {
		e.preventDefault()
		changePassword()
	})

	$("#cancelChangePasswordBtn").click(function(e) {
		e.preventDefault()
		$('.content').hide()
		$('.home-page').show()
		$("#password").val("")
		$("#passwordConfirm").val("")
		hideValidate($("#password"))
		hideValidate($("#passwordConfirm"))
	})
	
	/*Check if passwords match*/
	$("#password").on('blur', function(event){
		event.preventDefault()
		var pass = $("#password").val()
		var rep = $("#passwordConfirm").val()
		if(pass != rep){
			showValidate($("#password"))
			showValidate($("#passwordConfirm"))
		}else{
			hideValidate($("#password"))
			hideValidate($("#passwordConfirm"))
		}
	})
	
	/**/
	$("#passwordConfirm").on('blur', function(event){
		event.preventDefault()
		var pass = $("#password").val()
		var rep = $("#passwordConfirm").val()
		if(pass != rep){
			showValidate($("#password"))
			showValidate($("#passwordConfirm"))
		}else{
			hideValidate($("#password"))
			hideValidate($("#passwordConfirm"))
		}
	})
	
	/**/
	$("#password").on('focus', function(event){
		event.preventDefault()
		hideValidate($("#password"))
		hideValidate($("#passwordConfirm"))
	})
	
	/**/
	$("#passwordConfirm").on('focus', function(event){
		event.preventDefault()
		hideValidate($("#password"))
		hideValidate($("#passwordConfirm"))
	})
	
	/*Verify patient's prescription*/
	$('body').on('click', 'button.table-button-verify', function() {
		
		var id = $(this).attr('id')
		
		$.ajax({
			type : "POST",
			async: false,
			url : "../../theGoodShepherd/medicalReport/verify/"+id,
			success : function()  {
				alert("You successfully verified patient's prescription!");
				verifyMedicine.ajax.reload()
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	
	/*Prescriptions waiting to be verified*/
	$('#nurseVerifyPrescription').click(function(e){
		e.preventDefault()
		
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#verifyMedicine')) {
			verifyMedicine = $('#verifyMedicine').DataTable({
				ajax: {
					url: "../../theGoodShepherd/medicalReport",
					dataSrc: ''
				},
				columns: [
					{ data: 'patient'},
					{ data: 'doctor'},
					{ data: 'diagnose'},
					{
						data: null,
						render: function (data) {
							var button = '<button id="'+data.id+'" class="btn btn-info table-button-verify" name="'+data.id+'">Verify</button>';
						  	return button;
						}
					}
					]
			})
		} else {
			verifyMedicine.ajax.reload()
		}
	});
	
	/*View all currentUser's leave requests*/
	$('#medicalVacation').click(function(e){
		e.preventDefault()
		
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#vacationTable')) {
			vacationTable = $('#vacationTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/leaveRequest",
					dataSrc: ''
				},
				columns: [
					{ data: 'startDate'},
					{ data: 'endDate'},
					{ data: 'type'},
					{ data: 'approved'}]
			})
		} else {
			vacationTable.ajax.reload()
		}
	});
	
	/*Show form for creating leave request*/
	$('#makeVacationRequest').click(function(e){
		e.preventDefault()
		
		$('.content').hide()
		$('.nurse-create-leave-request').show()
	});
	
	/*Create leave request*/
	$('#create_leaveRequest').click(function(e){
		e.preventDefault()
		
		var startDate = $('#startDate').val()
		var endDate = $('#endDate').val()
		var type = $('#leaveType').val()
		
		var leaveReq = {
			startDate: startDate,
			endDate: endDate,
			type: type
		}
		
		$.ajax({
			type : "POST",
			async: false,
			url : "../../theGoodShepherd/leaveRequest/addNewLeaveRequest",
			contentType: "application/json",
			data: JSON.stringify(leaveReq),
			success : function()  {
				alert("You successfully sent a leave request!");
				vacationTable.ajax.reload()
				$('.content').hide()
				$('.medical-vacation').show()
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	});
	
	/*Cancel adding leave request*/
	$('#cancel_leaveRequest').click(function(e){
		e.preventDefault()
		
		$('.content').hide()
		$('.medical-vacation').show()
		$('#startDate').val('')
		$('#endDate').val('')
	})
	/*------------------------------------------------------------------------*/
})

function viewPersonalInformation(){
	// obracamo se serveru samo prvi put
	if ($("#fullNameBig").text() == "" || edited == true) {
		$.ajax({
			type : "GET",
			async: false,
			url : "../../theGoodShepherd/nurse/personalInformation" ,
			dataType: "json",
			success : function(output)  {
				$("#fullNameBig").text(output.name + " " + output.surname)
				$("#fullName").text(output.name + " " + output.surname)
				$("#email").text(output.email)
				$("#gender").text(output.gender)
				$("#dateOfBirth").text(output.dateOfBirth)
				if(output.phoneNumber == ''){
					$("#phoneNumber").text('/')
				}else{
					$("#phoneNumber").text(output.phoneNumber)
				}
				$("#securityNumber").text(output.securityNumber)
				var adresa = output.address + ", " + output.city + ", " + output.country
				if(adresa == ', , '){
					$("#address").text('/')
				}else{
					$("#address").text(adresa)
				}
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	}
}

function editPersonalInformation(){
	$.ajax({
		type : "GET",
		async: false,
		url : "../../theGoodShepherd/nurse/personalInformation" ,
		dataType: "json",
		success : function(output)  {
			$("#emailEdit").val(output.email)
			$("#nameEdit").val(output.name)
			$("#surnameEdit").val(output.surname)
			$("#genderEdit").val(output.gender);
			$("#dateOfBirthEdit").val(output.dateOfBirth)
			$("#phoneNumberEdit").val(output.phoneNumber)
			$("#securityNumberEdit").val(output.securityNumber)
			$("#addressEdit").val(output.address)
			$("#cityEdit").val(output.city)
			$("#countryEdit").val(output.country)
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function saveUpdatedProfile(){
	var emailV = $("#emailEdit").val()
	var nameV = $("#nameEdit").val()
	var surnameV = $("#surnameEdit").val()
	var genderV = $("#genderEdit").val();
	var dateOfBirthV = $("#dateOfBirthEdit").val()
	var phoneNumberV = $("#phoneNumberEdit").val()
	var securityNumberV = $("#securityNumberEdit").val()
	var addressV = $("#addressEdit").val()
	var cityV = $("#cityEdit").val()
	var countryV = $("#countryEdit").val()
	
	if(!nameV || !surnameV || !genderV || !dateOfBirthV){
		alert("Not all required fields are filled!")
		return;
	}

	var dateObject = new Date(dateOfBirthV);
	var currentDate = new Date();

	if(currentDate < dateObject){
		alert("Wrong date of birth!")
		return;
	}

	var newNurse = {
		name: nameV,
		surname: surnameV,
		email: emailV,
		gender: genderV,
		dateOfBirth: dateOfBirthV,
		phoneNumber: phoneNumberV,
		securityNumber: securityNumberV,
		address: addressV,
		city: cityV,
		country: countryV
	}
	
	$.ajax({
		type : "POST",
		url : "../../theGoodShepherd/nurse/editPersonalInformation" ,
		contentType : "application/json",
		dataType : "json",
		data : JSON.stringify(newNurse),
		success : function(response)  {
			edited = true
			$(".n-profile").click()
			alert("Succesfully edited personal information.")
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function changePassword() {
	var passwordV = $("#password").val()
	var confirmPasswordV = $("#passwordConfirm").val()

	if(passwordV != confirmPasswordV){
		alert("Passwords do not match!")
		return;
	}
	
	if(!passwordV || !confirmPasswordV){
		alert("All fields must be filled!")
		return;
	}

	$.ajax({
		type : "POST",
		async: false,
		url : "../../theGoodShepherd/nurse/changePassword/"+passwordV,
		success : function(data)  {
			alert("Succesfully changed password.")
			$('.content').hide()
			$('.home-page').show()
			$("#password").val("")
			$("#passwordConfirm").val("")
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function logInNurse(email, password){
	$.ajax({
		type : "POST",
		async: false,
		url : "theGoodShepherd/nurse/logIn//" + email + "//" + password ,
		dataType: "json",
		success : function(output)  {
			sessionStorage.setItem('nameSurname', output.name + ' ' + output.surname);
			window.location.href = "html/home-pages/nurse_hp.html"
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

/*Validation and forms*/
function showValidate(input) {
	var thisAlert = $(input).parent();

	$(thisAlert).addClass('alert-validate');
}
function hideValidate(input) {
	var thisAlert = $(input).parent();

	$(thisAlert).removeClass('alert-validate');
}

/*View all patients*/
function viewAllPatients(){
	// inicijalizujemo je ako vec nismo
	if (!$.fn.DataTable.isDataTable('#patientsTable')) {
		patientsTable = $('#patientsTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/patient",
				dataSrc: ''
			},
			columns: [
				{ data: 'name'},
				{ data: 'surname'},
				{ data: 'securityNumber'},
				{
					data: null,
					render: function (data) {
						var button = '<button id="'+data.securityNumber+'" class="btn btn-info table-button-patient" name="'+data.securityNumber+'">View profile</button>';
					  	return button;
					}
				}
				]
		})
	} else {
		patientsTable.ajax.reload()
	}
}

function viewPatientProfile(secNum) {
	$.ajax({
		type : "POST",
		async: false,
		url : "../../theGoodShepherd/patient/viewProfile/nurse/" + secNum,
		dataType: "json",
		success : function(data)  {
			if (data.medicalRecords != null) {
				medicalRecord_id = data.medicalRecords.id
			}
			if (data.appointment != null) {
				appointment_id = data.appointment.id
			} else {
				appointment_id = null
			}
			viewMedicalReports(data)
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function viewMedicalReports(data){
	$('.content').hide()
    $('.nurse-patient-profile').show()
    $("#fullNamePatient").text(data.name + " " + data.surname)
    $("#emailPatient").text(data.email)
    $("#genderPatient").text(data.gender)
    $("#dateOfBirthPatient").text(data.dateOfBirth)
    $("#phoneNumberPatient").text(data.phoneNumber)
    $("#securityNumberPatient").text(data.securityNumber)
    $("#addressPatient").text(data.address + ", " + data.city + ", " + data.country)
    if(data.medicalRecords == null){
		$('#medicalReport').hide()
		$('h5').hide()
		// sakrivanje dugmadi za pregled
		$('.app-btns').hide()
        $("#generalReport").text("You do not have access to patients medical record.")
    }else{
		$('#medicalReport').show()
		$('h5').show()
		if (appointment_id == null) {
			// sakrivanje dugmadi za pregled
			$('.app-btns').hide()
		} else {
			// prikaz dugmadi za pregled
			$('.app-btns').show()
		}
		$("#generalReport").text("")
    	$("#height").text(data.medicalRecords.height)
    	$("#weight").text(data.medicalRecords.weight)
    	$("#bloodPressure").text(data.medicalRecords.bloodPressure)
    	$("#bloodType").text(data.medicalRecords.bloodType)
    	$("#allergies").text(data.medicalRecords.allergies)
    	if (!$.fn.DataTable.isDataTable('#medicalReports')) {
    		medicalReportTable = $('#medicalReports').DataTable({
				data: data.medicalRecords.medicalReports,
    			columns: [
    				{ data: 'description'},
					{ data: 'diagnosisName'},
					{
						data: null,
						render: function (data) {
							var allMedicine = ""
							if (data.prescriptionMedicines != null) {
								for (var i = 0; i < data.prescriptionMedicines.length; i++) {
									allMedicine += data.prescriptionMedicines[i]
									if (i != data.prescriptionMedicines.length - 1) {
										allMedicine += ", "
									}
								}
							}
							return allMedicine
						}
					}]
    		})
    	} else {
			if (data.medicalRecords.medicalReports != null) {
				medicalReportTable.clear().rows.add(data.medicalRecords.medicalReports).draw();
			}
		}
	}
}

function filterPatients(){
	var nameV = $('#patientName').val()
	var surnameV = $('#patientSurname').val()
	var secNumV = $('#patientSecurityNumber').val()
	
	patientsTable.ajax.url("../../theGoodShepherd/patient/filterPatients?name=" + nameV + "&surname=" + surnameV + "&securityNumber=" + secNumV)
	patientsTable.ajax.reload()
	
	if (isNaN(secNumV)) {
		alert("Security number must be a number!")
		return
	}
}