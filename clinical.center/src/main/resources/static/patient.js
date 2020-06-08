var price;
var duration;

var newAppointment = {
	date: "",
	appTypeName: "",
	appDuration: "",
	clinicId: "",
	clinicName: "",
	doctorId: "",
	doctorName: "",
	time: ""
}

var allClinicsTable;
var predefinedApps;
var clinicsTable;
var doctorsClinicTable;
var medicalReportTable;
var finishedAppsTable;
var confirmedAppsTable;
var unconfirmedAppsTable;

$(document).ready(function() {

	// postavljanje maksimalnog datuma rodjenja koji se moze odabrati na danas
	document.getElementById("dateOfBirthEdit").max = new Date().toISOString().split("T")[0];

	/*------------------------------------------------------------------------------*/
	// pregled profila
	$('.profile').click(function() {
		$.ajax({
			type : "GET",
			url : "../../theGoodShepherd/patient/viewProfile",
			dataType: "json",
			success : function(data)  {
				fillProfile(data)
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	
	/*Edit personal information*/
	$('#editPatientProfile').click(function(e) {
		e.preventDefault();
		$('.content').hide()
        $('.patient-edit-profile').show()
        document.body.scrollTop = 0
        document.documentElement.scrollTop = 0
	})
	
	$('#confirmEdit').click(function(e) {
		e.preventDefault()
		saveUpdatedProfile()
	})

	$('#cancelEdit').click(function(e) {
		e.preventDefault()
		$('.content').hide()
		fillEditForm()
		$('.patient-profile').show()
		document.body.scrollTop = 0
        document.documentElement.scrollTop = 0
	})
	
	/*Change password*/
	$('#changePasswordBtn').click(function(e) {
		e.preventDefault()
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
			url : "../../theGoodShepherd/patient/changePassword/"+passwordV,
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
	$("#password").on('focus', function(event){
		event.preventDefault()
		hideValidate($("#password"))
		hideValidate($("#passwordConfirm"))
	})
	$("#passwordConfirm").on('focus', function(event){
		event.preventDefault()
		hideValidate($("#password"))
		hideValidate($("#passwordConfirm"))
	})
	
	/*------------------------------------------------------------------------------*/
	
	/* Pregled svih klinika i njihovih predefinisanih pregleda */
	// pregled svih klinika
	$('#viewAllClinics').on('click', function(e){
		e.preventDefault()
		viewAllClinics()
	})

	// vracanje na pregled svih klinika
	$('#clinicBack').click(function(e) {
		e.preventDefault()
		$('.content').hide()
		$('.patient-viewAllClinics').show()
	})

	// filtriranje svih klinika po imenu i adresi
	$('#filterAllClinicsByAtributes').click(function(e){
		e.preventDefault()
		filterAllClinics()
	})

	// pregled svih slobodnih predefinisanih pregleda
	$('#viewPredefined').click(function(e) {
		e.preventDefault()
		$('.content').hide()
		$('.view-predefined').show()
	})

	// vracanje na pregled klinike
	$('#predefinedBack').click(function(e) {
		e.preventDefault()
		$('.content').hide()
		$('.view-clinic').show()
	})

	// filtriranje predefinisanih pregleda
	$('#filterPredefined').click(function(e) {
		e.preventDefault()
		filterPredefinedApps()
	})

	// pretplata na klik na dugmad u tabeli svih klinika
	// (Visit clinic)
	$('body').on('click', 'button.visit-clinic-btn', function() {
		// popunjavanje podataka o odabranoj klinici
		var clinicId = $(this).attr('id')
		var clinicName = $(this).attr('name')
		fillClinicInfo(clinicId, clinicName)
		$('.content').hide()
		$('.view-clinic').show()
	});
	// pretplata na klik na dugmad u tabeli slobodnih pregleda
	// (Schedule)
	$('body').on('click', 'button.schedule-app-btn', function() {
		// popunjavanje podataka o odabranoj klinici
		var appId = $(this).attr('id')
		schedulePredefinedApp(appId)
	});

	/*********************************/

	/* Kreiranje novog pregleda - pacijent */
	// postavljanje minimalne vrednosti koju pacijent moze da odabere za datum
	// (danas)
	document.getElementById("filterAppDate").min = new Date().toISOString().split("T")[0];

	// pretplata svih elemenata sa klasom na klik
	$('body').on('click', 'button.table-button', function() {
		// inicijalizacija tabele sa doktorima odabrane klinike
		var clinicId = $(this).attr('id')
		var atributes = $(this).attr('name')
		var tokens = atributes.split('|')
		newAppointment.appDuration = tokens[2]
		newAppointment.clinicName = tokens[0]
		initialiseClinicDoctors(clinicId, tokens[0])
	});
	$('body').on('click', 'button.table-button-doctor', function() {
		// prikupljanje podataka i kreiranje pregleda
		var doctorId = $(this).attr('id')
		newAppointment.doctorName = $(this).attr('name')
		var time = $('#time'+doctorId).val()
		createAppointment(doctorId, time)
	});
	
	$('#confirmApp').click(function(e) {
		e.preventDefault()

		var app = {
			clinic: {
				id: newAppointment.clinicId
			}
		}
		
		// prvo upise novi pregled i zahtev za pregled u bazu
		$.ajax({
			type: "POST",
			url: "../../theGoodShepherd/appointment/createPatientApp",
			contentType: "application/json",
			data: JSON.stringify(newAppointment),
			success: function() {
				$("body").css("cursor", "default");
				alert("Appointment request sent successfully!\n" +
					  "You will get an email when a clinic administrator confirms it.\n" +
					  "The appointment then becomes visible in 'Unconfirmed appointments' tab.")
				$('.content').hide()
				$('.patient-createNewApp').show()
			},
			error: function(response) {
				alert(response.responseJSON.message)
			}
		})

		// salje se mejl svim administratorima klinike
		//kursor ceka
		$("body").css("cursor", "progress")
		$.ajax({
			type : "POST",
			url : "../../theGoodShepherd/patient/sendAppointment",
			contentType: "application/json",
			data: JSON.stringify(app),
			success : function() {
	    		// ovde ne mora nista
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})

	$('#cancelApp').on('click', function(e){
		e.preventDefault()
		$('.patient-confirm-app').hide()
		$('.patient-createNewApp').show()
        document.body.scrollTop = 0
        document.documentElement.scrollTop = 0
	})

	// dobavljanje vrednosti tipova pregleda i dodavanje u select
	$('#scheduleNewAppointment').click(function() {
		$.ajax({
			type : "GET",
			url : "../../theGoodShepherd/appointmentType/getAllTypesNames",
			success : function(data) {
				$('#filterAppType').find('option').remove()
				$.each(data, function(index, appTypeName) {
					$("#filterAppType").append(new Option(appTypeName, appTypeName));
				})
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})

	// ponistavanje filtera klinika
	$('#clearClinicsFilter').click(function(e) {
		e.preventDefault()
		// skidanje parametra pregleda
		newAppointment.date = ""
		newAppointment.appTypeName = ""
		newAppointment.appDuration = ""
		// dobavljanje svih klinika ponovo
		clinicsTable.ajax.url("../../theGoodShepherd/patient/clinics")
		clinicsTable.ajax.reload()
	})

	// filtriranje klinika
	$('#filterClinics').click(function(e) {
		e.preventDefault()
		// postavljanje parametara pregleda
		var date = $('#filterAppDate').val()
		newAppointment.date = date
		var appTypeName = $('#filterAppType').val()
		newAppointment.appTypeName = appTypeName

		// filtriranje klinika na beku
		clinicsTable.ajax.url("../../theGoodShepherd/clinics/filterClinics/"+appTypeName+"/"+date)
		clinicsTable.ajax.reload()
		// sakrije doktore
		$('#doctorTableDiv').hide()
		// doda novu vrednost
		clinicsTable.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
			var data = this.data();
			data[4] = newAppointment.appTypeName
			this.data(data)
		 } )
	})

	/*View clinics*/
	$("#scheduleNewAppointment").on('click', function(e){
		e.preventDefault()
		viewClinics()
	})
	
	/*Filter given clinics by name and address*/
	$("#filterClinicsByAtributes").on('click', function(e){
		e.preventDefault()
		filterClinics()
	})
	
	/*Filter doctors by name and surname*/
	$("#filterDoctorsByAtributes").on('click', function(e){
		e.preventDefault()
		filterDoctors()
	})
	/**************/

	/*---------------------------------------------------------------*/
	/* View my appointments */
	// view finished appointments
	$('#finishedApps').click(function() {
		initialiseFinishedApps()
		$('.content').hide()
		$('.patient-finished-apps').show()
	})
	// view confirmed appointments
	$('#confirmedApps').click(function() {
		initialiseConfirmedApps()
		$('.content').hide()
		$('.patient-confirmed-apps').show()
	})
	// view unconfirmed appointments
	$('#unconfirmedApps').click(function() {
		initialiseUnconfirmedApps()
		$('.content').hide()
		$('.patient-unconfirmed-apps').show()
	})
	/*---------------------------------------------------------------*/
})

/*Profile*/
/*
function editPersonalInformation(){
	$.ajax({
		type : "GET",
		async: false,
		url : "../../theGoodShepherd/patient/viewProfile" ,
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
*/
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

	var newPatient = {
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
		url : "../../theGoodShepherd/patient/editPersonalInformation" ,
		contentType : "application/json",
		dataType : "json",
		data : JSON.stringify(newPatient),
		success : function(response)  {
			fillProfile(response)
			document.body.scrollTop = 0
			document.documentElement.scrollTop = 0
			alert("Succesfully edited personal information.")
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

/* Predefinisani pregledi */
// inicijalizacija tabele svih klinika
function viewAllClinics() {
	if (!$.fn.DataTable.isDataTable('#allClinicsTable')) {
		allClinicsTable = $('#allClinicsTable').DataTable({
		responsive: true,
		ajax: {
			url: "../../theGoodShepherd/patient/clinics",
			dataSrc: ''
		},
		columns: [
			{ data: 'name'},
			{ data: 'address'},
			{ data: 'rating'},
			{
				data: null,
				render: function (data) {
					return '<button id="'+data.id+'" name="'+data.name+'" class="btn btn-info visit-clinic-btn">Visit clnic</button>';
				}
			}]
		})
		// specijalizovano filtriranje
		$.fn.DataTable.ext.search.push(
			function( settings, data, dataIndex ) {
				// omogucava da radi samo za zeljenu tabelu
				// za sve druge vraca da uvek zadovoljava uslov
				// tako tabele ostanu nepromenjene
				if ( settings.nTable.id !== 'allClinicsTable' ) {
					return true;
				}
				var minRatingV = $('#allClinicsMinRating').val()
				var rating = parseFloat(data[2]) || 0; // use data for the age column
			
				if ((isNaN( minRatingV )) || ( minRatingV <= rating)) {
					return true;
				}
				return false;
			}
		)
	} else {
		allClinicsTable.ajax.reload()
	}
}
// filtriranje svih klinika po imenu i adresi
function filterAllClinics(){
	
	var nameV = $('#allClinicsName').val()
	var addressV = $('#allClinicsAddress').val()

	allClinicsTable
	.column(0).search(nameV)
	.column(1).search(addressV)
	.draw()
}
// popunjavanje podataka o klinici
function fillClinicInfo(clinicId, clinicName) {
	$.ajax({
		type : "POST",
		url : "../../theGoodShepherd/clinics/"+clinicId,
		dataType: "json",
		success : function(output)  {
			$('.clinicName').text(output.name)
			$('#clinicRating').text(output.rating)
			$('#clinicAddress').text(output.address)
			$('#clinicCity').text(output.city)
			$('#clinicCountry').text(output.country)
			$('#clinicDescription').text(output.description)
			viewAvailableApps(clinicId, clinicName)
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}
// pregled svih slobodnih pregleda klinike
function viewAvailableApps(clinicId, clinicName) {
	$('#clinicPredefined').text(clinicName + "'s predefined appointments")
	if (!$.fn.DataTable.isDataTable('#predefinedApps')) {
		predefinedApps = $('#predefinedApps').DataTable({
		responsive: true,
		ajax: {
			url: "../../theGoodShepherd/appointment/available/"+clinicId,
			dataSrc: ''
		},
		columns: [
			{ data: 'date'},
			{
				data: null,
				render: function(data) {
					return data.startTime + ":00"
				}
			},
			{
				data: null,
				render: function(data) {
					return data.endTime + ":00"
				}
			},
			{ data: 'appType'},
			{ data: 'doctor'},
			{ data: 'ordination'},
			{ 
				data: null,
				render: function(data) {
					return data.price + " &euro;"
				}
			},
			{ 
				data: null,
				render: function(data) {
					if (data.discount == null) {
						return "/"
					} else {
						return data.discount + " %"
					}
				}
			},
			{
				data: null,
				render: function (data) {
					return '<button id="'+data.id+'" class="btn btn-info schedule-app-btn">Schedule</button>';
				}
			}]
		})
	} else {
		predefinedApps.ajax.url("../../theGoodShepherd/appointment/available/"+clinicId)
		predefinedApps.ajax.reload()
	}
}
// filtriranje svih klinika po imenu i adresi
function filterPredefinedApps(){
	
	var dateV = $('#filterPredefinedDate').val()
	var typeV = $('#filterPredefinedType').val()
	var doctorV = $('#filterPredefinedDoctor').val()

	predefinedApps
	.column(0).search(dateV)
	.column(3).search(typeV)
	.column(4).search(doctorV)
	.draw()
}
// zakazivanje pregleda
function schedulePredefinedApp(appId) {
	var colorEvent;
	$.ajax({
		type : "POST",
		url : "../../theGoodShepherd/appointment/schedule/"+appId,
		dataType: "json",
		success : function(output)  {
			if(output.typeOf == "appointment"){
				colorEvent = '#48baf7' 
			}else{
				colorEvent = '#2aebb4'
			}
	        calendar.addEvent({
				id: output.id,
		        title: output.appType + " " + output.typeOf + ' with doctor ' + output.doctor,
		        description: output.appType + " " + output.typeOf + ' with doctor ' + output.doctor,
		        start: output.date+"T0"+output.startTime+":00:00",
		        end: output.date+"T0"+output.endTime+":00:00",
		        color: colorEvent
		    });
	        //calendar.render();
			alert("Succesfully scheduled an appointment!\nAll appointments are visible in 'My appointments' tab.")	
			predefinedApps.ajax.reload()
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

/*************************/

function filterDoctors(){
	var nameV = $('#doctorName').val()
	var surnameV = $('#doctorSurname').val()
	var ratingV = $('#doctorRating').val()
	
	doctorsClinicTable
	.column(0).search(nameV)
	.column(1).search(surnameV)
    .draw();
}

function filterClinics(){
	
	var nameV = $('#clinicName').val()
	var addressV = $('#clinicAddress').val()
	
	clinicsTable
    .column(0)
    .search(nameV)
    .draw();
	
	clinicsTable
    .column(1)
    .search(addressV)
    .draw();
}

function viewClinics(){
	if (!$.fn.DataTable.isDataTable('#clinicsTable')) {
		clinicsTable = $('#clinicsTable').DataTable({
		responsive: true,
		ajax: {
			url: "../../theGoodShepherd/patient/clinics",
			dataSrc: ''
		},
		columns: [
			{ data: 'name'},
			{ data: 'address'},
			{ data: 'rating'},
			{
				// price
				data: null,
				render: function (data) {
					if (newAppointment.appTypeName) {
						for (var i = 0; i < data.appointmentTypes.length; i++) {
							if (data.appointmentTypes[i].name == newAppointment.appTypeName) {
								return data.appointmentTypes[i].price + " &euro;"
							}
						}
						return ""
					} else {
						return "Not specified"
					}
				}
			},
			{
				// appointmentType
				data: null,
				render: function () {
					if (newAppointment.appTypeName) {
						return newAppointment.appTypeName
					} else {
						return "Not specified"
					}
				}
			},
			{
				// duration
				data: null,
				render: function (data) {
					if (newAppointment.appTypeName) {
						for (var i = 0; i < data.appointmentTypes.length; i++) {
							if (data.appointmentTypes[i].name == newAppointment.appTypeName) {
								duration = data.appointmentTypes[i].duration + "h"
								return data.appointmentTypes[i].duration + "h"
							}
						}
						return ""
					} else {
						return "Not specified"
					}
				}
			},
			{
				data: null,
				render: function (data) {
					if (!newAppointment.date || !newAppointment.appTypeName) {
						return "Choose all parameters to view available doctors"
					}
					var button = '<button id="'+data.id+'" class="btn btn-info table-button" name="'+data.name+ '|' + price + '|' + duration + '">View doctors</button>';
				  	return button;
				}
			}]
		})
	}
}

function initialiseClinicDoctors(clinicId, clinicName) {
	// popunjavamo podatke o pregledu
	newAppointment.clinicId = clinicId
	newAppointment.clinicName = clinicName

	// dodavanje imena klinike u naslov tabele
	$('#doctorsClinic').text("Doctors from " + clinicName)

	// provara da je tabela vec inicijalizovana
	if (!$.fn.DataTable.isDataTable('#doctorsClinicTable')) {
		// nije inicijalizovana
		// inicijalizacija same tabele
		doctorsClinicTable = $('#doctorsClinicTable').DataTable({
			responsive: true,
			ajax: {
				url: "../../theGoodShepherd/doctor/certified/clinic/"+newAppointment.appTypeName+"/"+newAppointment.date+"/"+clinicId,
				dataSrc: ''
			},
			columns: [
				{ data: 'name'},
				{ data: 'surname'},
				{ data: 'rating'},
				/*{
					data: null,
					render: function () {
						return "";
					}
				},*/
				{
					data: null,
					render: function (data) {
						var availableTimes = data.availableTimes == null ? [] : (data.availableTimes instanceof Array ? data.availableTimes : [data.availableTimes])
						var options = ""
						for (var i = 0; i < availableTimes.length; i++) {
							options += '<option value="'+availableTimes[i]+'">'+availableTimes[i]+':00</option>'
						}
						return '<select class="form-control input-height available-times" id="time'+data.id+'">' + 
								options + '</select>'
					}
				},
				{
					data: null,
					render: function (data) {
						if (newAppointment.appTypeName && newAppointment.date) {
							var button = '<button id="'+data.id+'" name="' + data.name + ' ' + data.surname + '"class="btn btn-info table-button-doctor">Schedule</button>';
							return button;
						} else {
							return "Choose all parameters to schedule"
						}
					}
				}
			]
		})
		$('#doctorTableDiv').show()
	} else {
		// jeste inicijalizovana
		doctorsClinicTable.ajax.url( "../../theGoodShepherd/doctor/certified/clinic/"+newAppointment.appTypeName+"/"+newAppointment.date+"/"+clinicId)
		doctorsClinicTable.ajax.reload()
		$('#doctorTableDiv').show()
	}
}

function createAppointment(doctorId, time) {
	newAppointment.doctorId = doctorId;
	newAppointment.time = time
	// prikaz svih podataka
	$('#date').text(newAppointment.date)
	$('#time').text(newAppointment.time + ":00")
	$('#duration').text(newAppointment.appDuration)
	$('#appType').text(newAppointment.appTypeName)
	$('#doctor').text(newAppointment.doctorName)
	$('#clinic').text(newAppointment.clinicName)
	$('.content').hide()
	$('.patient-confirm-app').show()
	document.body.scrollTop = 0
    document.documentElement.scrollTop = 0
}

function fillProfile(data){
	// basic information
	$('.content').hide()
    $('.patient-profile').show()
	$(".fullName").text(data.name + " " + data.surname)
    $("#email").text(data.email)
    $("#gender").text(data.gender)
    $("#dateOfBirth").text(data.dateOfBirth)
    $("#securityNumber").text(data.securityNumber)
    if(data.phoneNumber == ''){
		$("#phoneNumber").text('/')
	}else{
		$("#phoneNumber").text(data.phoneNumber)
	}
	$("#securityNumber").text(data.securityNumber)
	var adresa = data.address + ", " + data.city + ", " + data.country
	if(adresa == ', , '){
		$("#address").text('/')
	}else{
		$("#address").text(adresa)
	}
	// medical reports
	$('#medicalReport').show()
	$('h5').show()
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
						for (var i = 0; i < data.prescriptionMedicines.length; i++) {
							allMedicine += data.prescriptionMedicines[i]
							if (i != data.prescriptionMedicines.length - 1) {
								allMedicine += ", "
							}
						}
						return allMedicine
					}
				}]
		})
	} else {
		medicalReportTable.clear().rows.add(data.medicalRecords.medicalReports).draw();
	}
	$("#emailEdit").val(data.email)
	$("#nameEdit").val(data.name)
	$("#surnameEdit").val(data.surname)
	$("#genderEdit").val(data.gender);
	$("#dateOfBirthEdit").val(data.dateOfBirth)
	$("#phoneNumberEdit").val(data.phoneNumber)
	$("#securityNumberEdit").val(data.securityNumber)
	$("#addressEdit").val(data.address)
	$("#cityEdit").val(data.city)
	$("#countryEdit").val(data.country)
}

function fillEditForm() {
	// fill edit form
	$("#emailEdit").val($("#email").text())
	var fullName = $(".fullName").text().split(" ")
	$("#nameEdit").val(fullName[0])
	$("#surnameEdit").val(fullName[2])
	$("#genderEdit").val($("#gender").text());
	$("#dateOfBirthEdit").val($("#dateOfBirth").text())
	$("#phoneNumberEdit").val($("#phoneNumber").text())
	var fullAddress = $("#address").text().split(", ")
	$("#addressEdit").val(fullAddress[0])
	$("#cityEdit").val(fullAddress[1])
	$("#countryEdit").val(fullAddress[2])
}

/*-----------------------------------------------------*/
function initialiseFinishedApps() {
	if (!$.fn.DataTable.isDataTable('#finishedAppsTable')) {
		finishedAppsTable = $('#finishedAppsTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/appointment/allPatientFinished",
				dataSrc: ''
			},
			columns: [
				{ data: 'appType'},
				{ data: 'date'},
				{
					data: null,
					render: function(data) {
						return data.startTime + ":00"
					}
				},
				{
					data: null,
					render: function(data) {
						return data.endTime + ":00"
					}
				},
				{ data: 'ordination'},
				{
					data: null,
					render: function(data) {
						return data.price + " &euro;"
					}
				},
				{
					data: null,
					render: function(data) {
						if (data.discount) {
							return data.discount + "%"
						} else {
							return "0%"
						}
					}
				},
				{ data: 'doctor'}
			]
		})
	} else {
		finishedAppsTable.ajax.reload()
	}
}
function initialiseConfirmedApps() {
	if (!$.fn.DataTable.isDataTable('#confirmedAppsTable')) {
		confirmedAppsTable = $('#confirmedAppsTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/appointment/allPatientConfirmed",
				dataSrc: ''
			},
			columns: [
				{ data: 'appType'},
				{ data: 'date'},
				{
					data: null,
					render: function(data) {
						return data.startTime + ":00"
					}
				},
				{
					data: null,
					render: function(data) {
						return data.endTime + ":00"
					}
				},
				{ data: 'ordination'},
				{
					data: null,
					render: function(data) {
						return data.price + " &euro;"
					}
				},
				{
					data: null,
					render: function(data) {
						if (data.discount) {
							return data.discount + "%"
						} else {
							return "0%"
						}
					}
				},
				{ data: 'doctor'}
			]
		})
	} else {
		confirmedAppsTable.ajax.reload()
	}
}

function initialiseUnconfirmedApps() {
	if (!$.fn.DataTable.isDataTable('#unconfirmedAppsTable')) {
		unconfirmedAppsTable = $('#unconfirmedAppsTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/appointment/allPatientUnconfirmed",
				dataSrc: ''
			},
			columns: [
				{ data: 'appType'},
				{ data: 'date'},
				{
					data: null,
					render: function(data) {
						return data.startTime + ":00"
					}
				},
				{
					data: null,
					render: function(data) {
						return data.endTime + ":00"
					}
				},
				{ data: 'ordination'},
				{
					data: null,
					render: function(data) {
						return data.price + " &euro;"
					}
				},
				{
					data: null,
					render: function(data) {
						if (data.discount) {
							return data.discount + "%"
						} else {
							return "0%"
						}
					}
				},
				{ data: 'doctor'},
				{ data: null,
					render: function(data){
						return '<button name="confirmApp" class="btn btn-info add-button" onclick="confirmApp('+ data.id +')">Confirm</button>' +
						'<button name="declineApp" class="btn btn-info add-button" onclick="declineApp('+ data.id +')">Decline</button>'
					}
				}
			]
		})
	} else {
		unconfirmedAppsTable.ajax.reload()
	}
}
// prihvatanje pregleda
function confirmApp(appId) {
	$.ajax({
		type : "POST",
		url : "../../theGoodShepherd/appointment/confirmApp/"+appId,
		contentType: "application/json",
		success : function(output) {
			if(output.typeOf == "appointment"){
				colorEvent = '#48baf7' 
			}else{
				colorEvent = '#2aebb4'
			}
	        calendar.addEvent({
				id: output.id,
		        title: output.appType + " " + output.typeOf + ' with doctor ' + output.doctor,
		        description: output.appType + " " + output.typeOf + ' with doctor ' + output.doctor,
		        start: output.date+"T0"+output.startTime+":00:00",
		        end: output.date+"T0"+output.endTime+":00:00",
		        color: colorEvent
		    });
			alert("Succesfully accepted an appointment!\nYou can view all your upcoming appointments in the 'Upcoming appointments' tab.")
			//calendar.render();
			unconfirmedAppsTable.ajax.reload()
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}
// odbijanje pregleda
function declineApp(appId) {
	$.ajax({
		type : "POST",
		url : "../../theGoodShepherd/appointment/declineApp/"+appId,
		contentType: "application/json",
		success : function() {
			alert("Succesfully declined an appointment!\nThis appointment does no longer exist among yours.")
			unconfirmedAppsTable.ajax.reload()
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}
/*-----------------------------------------------------*/

function logInPatient(email, password) {
	$.ajax({
		type : "POST",
		async: false,
		url : "theGoodShepherd/patient/logIn//" + email + "//" + password ,
		dataType: "json",
		success : function(output)  {
			currentIsPatient = true;
			sessionStorage.setItem('nameSurname', output.name + ' ' + output.surname);
			window.location.href = "html/home-pages/patient_hp.html"
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