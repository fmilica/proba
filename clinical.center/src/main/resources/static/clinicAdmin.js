var ordinationTable;
var appTypeTable;
var doctorTable;
var examReqTable;
var examRoomTable;
var predefinedAppointmentTable;
var upcomingAppointmentTable;
var createAppTable;
var operReqTable;
var operRoomTable;
var doctorRating;
var vacationTable;
var declineId;

var changedExamTime = false;

var availableOrdinations;

var newAppointment = {
	date: "",
	appTypeName: "",
	doctorId: "",
	time: "",
	discount: "",
	ordinationId: ""
}

var examReq = {
	reqId: "",
	ordId: "",
	date: "",
	time: ""
}

var editedAppTypeId;
var editedOrdinationId;

//ako su personalni podaci editovani, ponovo saljemo upit serveru
var edited = false;

$(document).ready(function() {

	// postavljanje maksimalnog datuma rodjenja koji se moze odabrati na danas
	document.getElementById("dateOfBirthEdit").max = new Date().toISOString().split("T")[0];
	document.getElementById("dateOfBirthDoctor").max = new Date().toISOString().split("T")[0];
	
	//select2 radi zbog ovoga
	$.fn.modal.Constructor.prototype.enforceFocus = function() {};
	
	/*------------------------------------------------------------------------*/
	/*View personal information*/
	$('.ca-profile').on('click', function(e){
		e.preventDefault()
		viewPersonalInformation()
	})
	
	/*Edit personal information*/
	$('#editClinicAdminProfile').click(function(e) {
		e.preventDefault();
		$('.content').hide()
        $('.clinic-admin-edit-profile').show()
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
		$('.clinic-admin-profile').show()
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
			url : "../../theGoodShepherd/clinicAdmin/changePassword/"+passwordV,
			success : function(data)  {
				alert("Succesfully changed password.")
				$('.content').hide()
				$('.home-page').show()
				$("#password").val("")
				$("#passwordConfirm").val("")
				logger = true
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

	/* Izmena podataka klinike */
	$('#editClinic').click(function(e) {
		e.preventDefault()
		$('.content').hide()
		$('.edit-clinic').show()
	})
	/*-------------------------*/

	/*------------------------------------------------------------------------*/
	
	/* Kreiranje novog predefinisanog pregleda - administrator klinike */
	// postavljanje minimalne vrednosti koju pacijent moze da odabere za datum
	// (danas)
	document.getElementById("filterAppDate").min = new Date().toISOString().split("T")[0];
	// dobavljanje svih tipova pregleda za odredjenu kliniku
	$('#createClinicApp').click(function() {
		$.ajax({
			type : "GET",
			url : "../../theGoodShepherd/appointmentType/getClinicsTypes",
			success : function(data) {
				$('#filterAppType').find('option').remove()
				$.each(data, function(index, app) {
					$("#filterAppType").append(new Option(app.name, app.name));
				})
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	/*Create new appointment*/
	$('#chooseParams').click(function(e) {
		e.preventDefault()
		// postavljanje parametara pregleda
		var appDate = $('#filterAppDate').val()
		newAppointment.date = appDate
		var appTypeName = $('#filterAppType').val()
		newAppointment.appTypeName = appTypeName
		// poziv na bek
		if (appDate) {
			availableDoctorsOrdinations(appDate, appTypeName)
		} else {
			alert("Date must be selected!")
		}
	})
	// kada se promeni vrednost selekta vremena u redu, abdejtuju se ordinacije
	$('#createAppTable tbody').on('change', 'select.available-times', function() {
		var row = $(this).parents('tr')
		//var rowData = createAppTable.row($(this).parents('tr')).data();
		var ordinationCell = row.find("td:eq(7)")
		var time = $(this).val()
		newAppointment.time = time
		// dodavanje select-a za odabir ordinacije
		ordinationCell.empty()
		var ordinationSelect = $('<select class="form-control input-height ordination">')
		// provera koje ordinacije zadovoljavaju odabrano vreme i dodavanje ih
		var timeInt = parseInt(time.substring(0, 2))
		$.each(availableOrdinations, function(index, ordination) {
			if (ordination.availableTimes.indexOf(timeInt) != -1) {
				ordinationSelect.append('<option value="'+ordination.id+'">'+ordination.name+" "+ordination.ordinationNumber+'</option>')
			}
		})
		ordinationCell.append(ordinationSelect)
		// dodavanje dugmeta za kreiranje pregleda
		var scheduleCell = row.find("td:eq(8)")
		scheduleCell.empty()
		var scheduleBtn = $('<button class="btn btn-info schedule-btn">Create</button>')
		scheduleCell.append(scheduleBtn)
	});
	// kada se promeni vrednost selekta ordinacije u redu, prikazuje se dugme za zakazivanje
	$('#createAppTable tbody').on('input', 'input.discount', function() {
		var discount = $(this).val()
		newAppointment.discount = discount
	});
	$('#createAppTable tbody').on('click', 'button.schedule-btn', function() {
		// pokupe se svi podaci iz reda na koji je kliknuto
		var row = $(this).parents('tr')
		var doctorId = row.find("select.available-times").attr("id")
		newAppointment.doctorId = doctorId
		var time = row.find("select.available-times").val()
		newAppointment.time = parseInt(time.substring(0, 2))
		var discount = row.find("input.discount").val()
		newAppointment.discount = discount
		var ordinationId = row.find("select.ordination").val()
		newAppointment.ordinationId = ordinationId
		// kreiranje predefinisanog pregleda na beku
		$.ajax({
			type : "POST",
			url : "../../theGoodShepherd/appointment/createPredefined",
			contentType: "application/json",
			data: JSON.stringify(newAppointment),
			success : function() {
				alert("Succesfully created new predefined appointment!")
				// osvezavanje podataka u tabeli!
				availableDoctorsOrdinations(newAppointment.date, newAppointment.appTypeName)
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	/********************************************/
	
	/*View clinic reports*/
	$("#clinicReports").on('click', function(e){
		e.preventDefault()
		viewClinicReport()
	})

	/*View clinic predefined appointments*/
	$("#clinicPredefinedAppointments").on('click', function(e){
		e.preventDefault()
		viewAppointments()
	})
	
	/*View clinic upcoming appointments*/
	$("#clinicUpcomingAppointments").on('click', function(e){
		e.preventDefault()
		viewUpcomingAppointments()
	})
	
	/*View all ordinations*/
	$("#clinicOrdinations").on('click', function(event){
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#ordinationTable')) {
			ordinationTable = $('#ordinationTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/ordination/getClinicsOrdinations",
					dataSrc: ''
				},
				columns: [
					{ data: 'name'},
					{ data: 'ordinationNumber'},
					{ data: 'type'},
					{
						data: null,
						render: function(data) {
							return  '<div class="table-action-btns">'+
										'<span id="'+data.id+'" class="table-action edit-ordination"><i class="fas fa-edit"></i></span>'+
										'<span id="'+data.id+'" class="table-action delete-ordination"><i class="fas fa-trash"></i></span>'+
									'</div>'
							}
					}]
				})
		}
	})

	/*Adding new ordination*/
	$("#submit_ordination").on('click', function(event){
		event.preventDefault()
		
		var nameV = $("#ordination_name").val()
		var numberV = $("#ordination_number").val()
		var typeV = $("#ordination_type").val()
		
		if (!nameV || !numberV || !typeV) {
			alert("Not all required fields are filled!")
			return
		}

		if (isNaN(numberV)) {
			alert("Ordination number must be a number!")
			return
		}

		var ordination = {
			name: nameV,
			ordinationNumber: numberV,
			type: typeV
		}
		
		$.ajax({
			type : "POST",
			async: false,
			url : "../../theGoodShepherd/ordination/addNewOrdination",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(ordination),
			success : function()  {
				alert("Successfully added new ordination!")
				$('.content').hide()
				$('.clinic-ordinations').show()
				// ciscenje forme za novo dodavanje
				clearOrdinationForm()
				ordinationTable.ajax.reload();
				// scroll to top of page
				document.body.scrollTop = 0
				document.documentElement.scrollTop = 0
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	
	/*Cancel add ordination*/
	$("#cancel-ordination").on('click', function(event){
		event.preventDefault()
		clearOrdinationForm()
		$('.clinic-addOrdination').hide()
		$('.clinic-ordinations').show()
		hideValidate($("#ordination_number"))
		// scroll to top of page
		document.body.scrollTop = 0
  		document.documentElement.scrollTop = 0
	})

	/*Check if ordination number is number*/
	$("#ordination_number").on('blur', function(e){
		e.preventDefault()
		if(isNaN($("#ordination_number").val())){
			showValidate($("#ordination_number"))
		}
	})
	
	$("#ordination_number").on('click', function(e){
		hideValidate($("#ordination_number"))
	})
	
	/**********/

	/*View all appointment types*/
	$("#clinicAppTypes").on('click', function(event){
		
		$('#certifiedDoctors').select2({ width: '100%' });
		
		$.ajax({
			type : "GET",
			url : "../../theGoodShepherd/clinicAdmin/getDoctors",
			success : function(data) {
				$('#certifiedDoctors').empty();
				$.each(data, function(index, doctor) {
					$('#certifiedDoctors').append('<option value="'+doctor.id+'">'+doctor.name+' '+doctor.surname+'</option>')
					//$("#certifiedDoctors").append(new Option(doctor.name + " " + doctor.surname, doctor.id));
				})
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
		
		event.preventDefault()
		if (!$.fn.DataTable.isDataTable('#appTypeTable')) {
			appTypeTable = $('#appTypeTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/appointmentType/getClinicsTypes",
				dataSrc: ''
			},
			columns: [
				{ data: 'name'},
				{
					data: null,
					render: function (data) {
						return data.duration + " h";
					}
				},
				{
					data: null,
					render: function (data) {
						return data.price + " &euro;";
					}
				},
				{
					data: null,
					render: function(data) {
						return  '<div class="table-action-btns">'+
									'<span id="'+data.id+'" class="table-action edit-appType"><i class="fas fa-edit"></i></span>'+
									'<span id="'+data.id+'" class="table-action delete-appType"><i class="fas fa-trash"></i></span>'+
								'</div>'
						}
				}]
			})
		}
	})
	
	/*Adding new appointment type*/
	$("#submit_appointment_type").on('click', function(event){
		
		event.preventDefault()
		
		var nameV = $("#appointment_name").val()
		var durationV = $("#appointment_duration").val()
		var priceV = $("#appointment_price").val()
		var doctorsV1 = $("#certifiedDoctors").val()

		if (!nameV || !durationV || !priceV) {
			alert("Not all required fields are filled!")
			return
		}

		if (isNaN(durationV)) {
			alert("Duration must be a number!")
			return
		}

		if (isNaN(priceV)) {
			alert("Price must be a number!")
			return
		}

		var doctorsV = []
		for (var i = 0; i < doctorsV1.length; i++) {
			doctorsV.push(parseInt(doctorsV1[i]))
		}

		var appType = {
			name: nameV,
			duration: durationV,
			price: priceV,
			doctors: doctorsV
		}

		$.ajax({
			type : "POST",
			async: false,
			url : "../../theGoodShepherd/appointmentType/addNewAppointmentType",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(appType),
			success : function()  {
				alert("Successfully added new appointment type!")
				$('.content').hide()
				$('.clinic-appTypes').show()
				// ciscenje forme za novo dodavanje
				clearAppTypeForm()
				appTypeTable.ajax.reload()
				// scroll to top of page
				document.body.scrollTop = 0
				document.documentElement.scrollTop = 0
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	
	/*Cancel add appointment*/
	$("#cancel-appointment-type").on('click', function(event){
		event.preventDefault()
		clearAppTypeForm()
		$('.clinic-addAppType').hide()
		$('.clinic-appTypes').show()
		hideValidate($("#appointment_duration"))
		hideValidate($("#appointment_price"))
		// scroll to top of page
		document.body.scrollTop = 0
  		document.documentElement.scrollTop = 0
	})

	/*Check if appointment type duration and price are numbers*/
	$("#appointment_duration").on('blur', function(e){
		e.preventDefault()
		if(isNaN($("#appointment_duration").val())){
			showValidate($("#appointment_duration"))
		}
	})
	$("#appointment_duration").on('click', function(e){
		hideValidate($("#appointment_duration"))
	})
	$("#appointment_price").on('blur', function(e){
		e.preventDefault()
		if(isNaN($("#appointment_price").val())){
			showValidate($("#appointment_price"))
		}
	})
	$("#appointment_price").on('click', function(e){
		hideValidate($("#appointment_price"))
	})
	
	/**********/

	/*View all doctors */
	$("#clinicDoctors").on('click', function(event){
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#ordinationTable')) {
			doctorTable = $('#doctorTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/clinicAdmin/getDoctors",
				dataSrc: ''
			},
			columns: [
				{ data: 'email'},
				{ data: 'name'},
				{ data: 'surname'},
				{ data: 'gender'},
				{ data: 'specialization'},
				{ 
					data: null,
						render: function(data) {
							return data.startWork + ":00"
						}
				},
				{ 
					data: null,
						render: function(data) {
							return data.endWork + ":00"
						}
				},
				{ data: null,
					render: function (data) {
						if(data.phoneNumber == ''){
							return '/'
						}else{
							return data.phoneNumber
						}
					}
				},
				{
					data: null,
					render: function(data) {
						var adresa=  data.address + ", " + data.city + ", " + data.country
						if(adresa == ', , '){
							return '/'
						}else{
							return adresa
						}
					}
				},
				{
					data: null,
					render: function(data) {
						return  '<div class="table-action-btns">'+
									'<span id="'+data.id+'" class="table-action delete-doctor"><i class="fas fa-trash"></i></span>'+
								'</div>'
					}
				}]
			})
		}
	})
	
	/*Adding new doctor*/
	$("#submit_doctor").on('click', function(event){
		event.preventDefault()
		
		var emailV = $("#doctorEmail").val()
		var nameV = $("#firstNameDoctor").val()
		var surnameV = $("#lastNameDoctor").val()
		var passwordV = $("#passwordDoctor").val()
		var genderV = $("#genderDoctor").val()
		var dateOfBirthV = $("#dateOfBirthDoctor").val()
		var specializationV = $("#specializationDoctor").val()
		var securityNumV = $("#securityNumberDoctor").val()
		var startWorkV = $("#startWorkDoctor").val()
		var endWorkV = $("#endWorkDoctor").val()
		var phoneNumberV = $("#phoneNumberDoctor").val()
		var addressV = $("#addressDoctor").val()
		var cityV = $("#cityDoctor").val()
		var countryV = $("#countryDoctor").val()
		
		if(!emailV || !nameV || !surnameV || !passwordV || !genderV ||
				!dateOfBirthV || !specializationV || !securityNumV || !startWorkV || !endWorkV){
			alert("Not all required fields are filled!")
			return;
		}
		
		if(isNaN(securityNumV)){
			alert("Security number must be a number!")
			return;
		}

		if(isNaN(startWorkV)){
			alert("Start work time must be a number!")
			return;
		} else {
			if (parseInt(startWorkV) <0 || parseInt(startWorkV) > 24) {
				alert("Start work time must be a number greater than 0 and less than 24!")
				return;
			}
		}
		if(isNaN(endWorkV)){
			alert("End work time must be a number!")
			return;
		} else {
			if (parseInt(endWorkV) <0 || parseInt(endWorkV) > 24) {
				alert("End work time must be a number greater than 0 and less than 24!")
				return;
			}
		}
		if (!isNaN(startWorkV) && !isNaN(endWorkV)) {
			if(parseInt(startWorkV) >= parseInt(endWorkV)) {
				alert("Start work time must be before end work time!")
				return;
			}
		}
		
		var dateObject = new Date(dateOfBirthV);
        var currentDate = new Date();
        
        if(currentDate < dateObject){
            alert("Wrong date of birth!")
            return;
        }
		
		var newDoctor = {
			email: emailV,
			name: nameV,
			surname: surnameV,
			password: passwordV,
			gender: genderV,
			dateOfBirth: dateOfBirthV,
			securityNumber: securityNumV,
			startWork: startWorkV,
			endWork: endWorkV,
			phoneNumber: phoneNumberV,
			address: addressV,
			city: cityV,
			country: countryV,
			specialization: specializationV
		}
		
		$.ajax({
			type : "POST",
			url : "../../theGoodShepherd/doctor/addNewDoctor",
			contentType : "application/json",
			dataType : "json",
			data : JSON.stringify(newDoctor),
			success : function(response){
				// vrati ga na pregled svih doktora
				alert("New doctor saved!")
				$('.content').hide()
				$('.clinic-doctors').show()
				// ciscenje forme za novo dodavanje
				clearDoctorForm()
				doctorTable.ajax.reload()
				// scroll to top of page
				document.body.scrollTop = 0
				document.documentElement.scrollTop = 0
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})

	$("#cancel_doctor").on('click', function(event){
		event.preventDefault()
		clearDoctorForm()
		$('.clinic-addDoctor').hide()
		$('.clinic-doctors').show()
		hideValidate($("#passwordDoctor"))
		hideValidate($("#confirm-passwordDoctor"))
		hideValidate($("#securityNumberDoctor"))
		hideValidate($("#startWorkDoctor"))
		hideValidate($("#endWorkDoctor"))
		// scroll to top of page
		document.body.scrollTop = 0
  		document.documentElement.scrollTop = 0
	})
	
	/*Check if securityNumber is number*/
	$("#securityNumberDoctor").on('blur', function(e){
		e.preventDefault()
		if(isNaN($("#securityNumberDoctor").val())){
			showValidate($("#securityNumberDoctor"))
		}
	})
	$("#securityNumberDoctor").on('click', function(e){
		hideValidate($("#securityNumberDoctor"))
	})
	/*Check if start work is number*/
	$("#startWorkDoctor").on('blur', function(e){
		e.preventDefault()
		if(isNaN($("#startWorkDoctor").val())){
			showValidate($("#startWorkDoctor"))
		}
	})
	$("#startWorkDoctor").on('click', function(e){
		hideValidate($("#startWorkDoctor"))
	})
	/*Check if end work is number*/
	$("#endWorkDoctor").on('blur', function(e){
		e.preventDefault()
		if(isNaN($("#endWorkDoctor").val())){
			showValidate($("#endWorkDoctor"))
		}
	})
	$("#endWorkDoctor").on('click', function(e){
		hideValidate($("#endWorkDoctor"))
	})
	
	/*Delete doctor*/
	/*--------------------------------------------------*/
	$('body').on('click', 'span.delete-doctor', function() {
		//preslatko Eva hvala ti :3 <3 <333
		var doctorId = $(this).attr('id')
		$.ajax({
			type : "GET",
			async: false,
			url : "../../theGoodShepherd/doctor/delete/" + doctorId ,
			success : function(output)  {
				alert("Doctor successfuly deleted!")
				doctorTable.ajax.reload()
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	/********************************************************************************/
	
	/*Edit and delete appTypes*/
	$('body').on('click', 'span.edit-appType', function() {
		editedAppTypeId = $(this).attr('id')
		$.ajax({
			type : "GET",
			url : "../../theGoodShepherd/appointmentType/viewAppType/"+editedAppTypeId,
			contentType : "application/json",
			success : function(output)  {
				$('.content').hide()
				$('.clinic-editAppType').show()
				$('#edit_appointment_name').val(output.name)
				$('#edit_appointment_duration').val(output.duration)
				$('#edit_appointment_price').val(output.price)
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
		
		// OVO JE KUL NE TREBA OVO DA ZABORAVIMO MOZDA ZATREBA
		/*var tr = $(this).closest('tr')
		var selectedAppType = appTypeTable.row(tr).data()*/
	})
	$('#submit_edit_appointment_type').click(function(e) {
		e.preventDefault()
		var nameV = $('#edit_appointment_name').val()
		var durationV = $('#edit_appointment_duration').val()
		var priceV = $('#edit_appointment_price').val()
		
		if(!nameV || !durationV || priceV){
			alert("All fields must be filled")
			return
		}
		
		var editedAppType = {
			id: editedAppTypeId,
			name: nameV,
			price: priceV,
			duration: durationV
		}
		
		$.ajax({
			type : "POST",
			url : "../../theGoodShepherd/appointmentType/edit",
			contentType : "application/json",
			data : JSON.stringify(editedAppType),
			success : function()  {
				alert("Appointment type successfuly edited!")
				$(".content").hide()
				$(".clinic-appTypes").show()
				appTypeTable.ajax.reload()
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	$('#cancel-edit-appointment-type').click(function(e) {
		e.preventDefault()
		$('.content').hide()
		$('.clinic-appTypes').show()
	})
	$('body').on('click', 'span.delete-appType', function() {
		if (confirm('Are you sure you want to delete selected appointment type ?')) {
			var appTypeId = $(this).attr('id')
			$.ajax({
				type : "GET",
				url : "../../theGoodShepherd/appointmentType/delete/" + appTypeId ,
				success : function(output)  {
					alert("Appointment type successfuly deleted!")
					appTypeTable.ajax.reload()
				},
				error : function(response) {
					alert(response.responseJSON.message)
				}
			})
		} else {
	}
		
	})
	/************************************************************************************/

	/*Edit and delete ordinations*/
	$('body').on('click', 'span.edit-ordination', function() {
		ordinationId = $(this).attr('id')
		editedOrdinationId = $(this).attr('id')
		$.ajax({
			type : "GET",
			url : "../../theGoodShepherd/ordination/viewOrdination/"+ordinationId,
			contentType : "application/json",
			success : function(output)  {
				$('.content').hide()
				$('.clinic-edit-ordination').show()
				$('#edit_ordination_name').val(output.name)
				$('#edit_ordination_number').val(output.ordinationNumber)
				$('#edit_ordination_type').val(output.type)
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	$('#submit_edit_ordination').click(function(e) {
		e.preventDefault()
		var nameV = $('#edit_ordination_name').val()
		var numberV = $('#edit_ordination_number').val()
		var ordinationTypeV = $('#edit_ordination_type').val()
		
		if(!nameV || !numberV || !ordinationTypeV){
			alert("All fields must be filled!")
			return
		}
		
		var editedOrdination = {
			id: editedOrdinationId,
			name: nameV,
			ordinationNumber: numberV,
			type: ordinationTypeV
		}
		
		$.ajax({
			type : "POST",
			url : "../../theGoodShepherd/ordination/edit",
			contentType : "application/json",
			data : JSON.stringify(editedOrdination),
			success : function()  {
				alert("Ordination type successfuly edited!")
				$(".content").hide()
				$(".clinic-ordinations").show()
				ordinationTable.ajax.reload()
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	$('#cancel_edit_ordination').click(function(e) {
		e.preventDefault()
		$('.content').hide()
		$('.clinic-ordinations').show()
	})
	$('body').on('click', 'span.delete-ordination', function() {
		if (confirm('Are you sure you want to delete selected ordination?')) {
			var ordinationId = $(this).attr('id')
			$.ajax({
				type : "GET",
				url : "../../theGoodShepherd/ordination/delete/" + ordinationId ,
				success : function(output)  {
					alert("Ordinatio successfuly deleted!")
					ordinationTable.ajax.reload()
				},
				error : function(response) {
					alert(response.responseJSON.message)
				}
			})
		} else {
		}
		
	})
	
	
	/************************************************************************************/
	/*Appointment requests*/

	// pretplata svih elemenata sa klasom na klik
	$('body').on('click', 'button.table-button', function() {
		// odabrao je da zakaze neki pregled, sada je to zapamceno
		// podesi parametre koji je aktivan
		//TODO DA SKOCI NA POCETAK TABELE SA EXAMINATION ROOMS
		$('.clinic-exam-rooms').show()
		var examReqId = $(this).attr('id')
		examReq.reqId = examReqId
		var dateTime = $(this).attr('name')
		examReq.date = dateTime.split(" ")[0]
		examReq.time = dateTime.split(" ")[1]
		//da se popuni tabela slobodnim klinikama za taj dan
		viewExaminationRoomsForAppointmentRequests()
	});
	$('body').on('click', 'button.operation', function() {
		// odabrao je da zakaze neki pregled, sada je to zapamceno
		// podesi parametre koji je aktivan
		//TODO DA SKOCI NA POCETAK TABELE SA EXAMINATION ROOMS
		var examReqId = $(this).attr('id')
		examReq.reqId = examReqId
		var dateTime = $(this).attr('name')
		examReq.date = dateTime.split(" ")[0]
		examReq.time = dateTime.split(" ")[1]
		//da se popuni tabela slobodnim klinikama za taj dan
		viewOperationRoomsForAppointmentRequests()
		$('.clinic-oper-rooms').show()
	});
	$('body').on('click', 'button.table-button-schedule', function() {
		// prikupljanje podataka i kreiranje pregleda
		var ordinationId = $(this).attr('id')
		var currentDate = $(this).attr('name')
		var time = $('#time'+ordinationId).val()
		console.log("time "+time)
		scheduleOrdination(ordinationId, currentDate, time)
	});
	$('body').on('click', 'button.table-button-schedule-operation', function() {
		// prikupljanje podataka i kreiranje pregleda
		var ordinationId = $(this).attr('id')
		var currentDate = $(this).attr('name')
		var time = $('#time'+ordinationId).val()
		var doctors = $('#operation'+ordinationId).val()
		var doctorsV = []
		
		$('#operation'+ ordinationId + '> option:selected').each(function() {
		    //alert($(this).text() + ' ' + $(this).val());
			doctorsV.push(parseInt($(this).attr("id")))
		});
		
		console.log(doctorsV)
		scheduleOperation(ordinationId, currentDate, time, doctorsV)
	});
	
	$('#clinicExamReq').click(function(event) {
		// tabela sa svim zahtevima
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#examReqTable')) {
			examReqTable = $('#examReqTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/appointmentRequest",
					dataSrc: ''
				},
				columns: [
					{ data: 'doctorFullName'},
					{ data: 'patientFullName'},
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
					{
						data: null,
						render: function (data) {
							var button = '<button id="'+data.id+'" name="'+data.date+" "+data.startTime+'" class="btn btn-info table-button">Choose request</button>';
							return button;
						}
					}]
			})
		}
	})
	
	
	$('#clinicOperReq').click(function(event) {
		// tabela sa svim zahtevima
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#operReqTable')) {
			operReqTable = $('#operReqTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/appointmentRequest/operations",
					dataSrc: ''
				},
				columns: [
					{ data: 'doctorFullName'},
					{ data: 'patientFullName'},
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
					{
						data: null,
						render: function (data) {
							var button = '<button id="'+data.id+'" name="'+data.date+" "+data.startTime+'" class="btn btn-info table-button operation">Choose request</button>';
							return button;
						}
					}]
			})
		}
	})
	
	
	/*********************************************************************/
	/*Clinic information*/
	/*View clinic information*/
	$("#clinicInformation").on('click', function(e){
		$.ajax({
			type : "GET",
			url : "../../theGoodShepherd/clinics/viewClinicInformation",
			contentType : "application/json",
			success : function(output)  {
				fillClinicInformation(output)
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	
	/*Edit clinic information*/
	$("#editClinic").on('click', function(e){
		$('.content').hide()
		$('.edit-clinic').show()
	})
	
	/*Save edited clinic information*/
	$("#saveEditClinic").on('click', function(e){
		
		var nameV = $("#nameClinic").val()
		var addressV = $("#addressClinic").val()
		var cityV = $("#cityClinic").val()
		var countryV = $("#countryClinic").val()
		var descriptionV = $("#descriptionClinic").val()
		
		if( !nameV || !addressV || !cityV || !countryV){
			alert("All required fields must be filled!")
			return
		}
		
		var editedClinic = {
			name: nameV,
			address: addressV,
			city: cityV,
			country: countryV,
			description: descriptionV
		}
		
		$.ajax({
			type : "POST",
			url : "../../theGoodShepherd/clinics/editClinicInformation",
			contentType : "application/json",
			data : JSON.stringify(editedClinic),
			success : function(output)  {
				$('.content').hide()
				$('.clinic-information').show()
				fillClinicInformation(output)
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	
	$("#cancelEditClinic").on('click', function(e){
		
		$('.content').hide()
		$('.clinic-information').show()
		
		$("#nameClinic").val($(".clinicName").text())
		$("#addressClinic").val($("#clinicAddress").text())
		$("#cityClinic").val($("#clinicCity").text())
		$("#countryClinic").val($("#clinicCountry").text())
		$("#descriptionClinic").val($("#clinicDescription").text())
	})
	
	
	/*********************************************************************/
	/* Delete and edit appointments*/
	$('body').on('click', 'span.delete-appointment', function() {
		var appointmentId = $(this).attr('id')
		$.ajax({
			type : "GET",
			async: false,
			url : "../../theGoodShepherd/appointment/delete/" + appointmentId ,
			success : function(output)  {
				alert("Appointment successfuly deleted!")
				predefinedAppointmentTable.ajax.reload()
			},
			error : function(response) {
				alert(response.responseJSON.message)
			}
		})
	})
	
	$('#clinicVacation').click(function(e){
		e.preventDefault()
		
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#vacationTable')) {
			vacationTable = $('#vacationTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/leaveRequest/currentAdmin",
					dataSrc: ''
				},
				columns: [
					{ data: 'user'},
					{ data: 'startDate'},
					{ data: 'endDate'},
					{ data: 'type'},
					{ data: null,
						render: function(data){
							return '<button name="acceptRequest" class="btn btn-info add-button" onclick="acceptReq('+ data.id +')">Accept</button>' +
							'<button name="declineRequest" class="btn btn-info add-button" onclick="declineReq('+ data.id +')">Decline</button>'
						}
					}]
			})
		} else {
			vacationTable.ajax.reload()
		}
	});
	
	$('#decline_leareq').click(function(e){
		var desc = $('#descriptionLeaReq').val().trim()
		
		if(!desc){
			alert("All required fields must be filled!")
	    	return;
		}
		//kursor ceka
		$("body").css("cursor", "progress")
		$.ajax({
	        type : "POST",
	        url : "../../theGoodShepherd/leaveRequest/declineLeaveRequest",
	        contentType : "application/json",
	        //dataType: "json",
	        data : JSON.stringify({
	            "id" : declineId,
	            "description": desc
	        }),
	        success : function()  {
	        	vacationTable.ajax.reload();
	        	$('.content').hide()
	    		$('.clinic-vacation').show()
				alert("Leave request rejected!")
				$("body").css("cursor", "default");
	        },
	        error : function(response) {
	        	alert(response.responseJSON.message)
	        }
		})
	});
	
	$('#cancel_decline').click(function(e){
		$('.content').hide()
		$('.clinic-vacation').show()
		$('#descriptionLeaReq').empty()
	});
	
	/*********************************************************************/
	
	/*Front filter for examination rooms*/
	$("#filterExamRoom").on('click', function(e){
		e.preventDefault()
		filterExaminationRooms()
	})

	/*Front filter for operating rooms*/
	$("#filterOperRoom").on('click', function(e){
		e.preventDefault()
		filterOperationRooms()
	})
})

function fillClinicInformation(output){
	$(".clinicName").text(output.name)
	$("#nameClinic").val(output.name)
	
	$("#clinicRating").text(output.rating)
	
	$("#clinicAddress").text(output.address)
	$("#addressClinic").val(output.address)
	
	$("#clinicCity").text(output.city)
	$("#cityClinic").val(output.city)
	
	$("#clinicCountry").text(output.country)
	$("#countryClinic").val(output.country)
	
	$("#clinicDescription").text(output.description)
	$("#descriptionClinic").val(output.description)
	
}

function viewPersonalInformation(){
	// obracamo se serveru samo prvi put
	if ($("#fullNameBig").text() == "" || edited == true) {
		$.ajax({
			type : "GET",
			async: false,
			url : "../../theGoodShepherd/clinicAdmin/personalInformation" ,
			dataType: "json",
			success : function(output)  {
				$("#fullNameBig").text(output.name + " " + output.surname)
				$("#fullName").text(output.name + " " + output.surname)
				$("#email").text(output.email)
				$("#gender").text(output.gender)
				$("#dateOfBirth").text(output.dateOfBirth)
				$("#securityNumber").text(output.securityNumber)
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
		url : "../../theGoodShepherd/clinicAdmin/personalInformation" ,
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

	var newClinicAdmin = {
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
		url : "../../theGoodShepherd/clinicAdmin/editPersonalInformation" ,
		contentType : "application/json",
		dataType : "json",
		data : JSON.stringify(newClinicAdmin),
		success : function(response)  {
			edited = true
			$(".ca-profile").click()
			alert("Succesfully edited personal information.")
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

/*Dobavljanje slobodnih doktora i operacionih sala*/
function availableDoctorsOrdinations(appDate, appTypeName) {
	$.ajax({
		type : "GET",
		url : "../../theGoodShepherd/doctor/availableDoctorOrdinations/"+appDate+"/"+appTypeName,
		dataType: "json",
		success : function(output)  {
			// dobavljanje ordinacija
			availableOrdinations = output.availableOrdinations
			initialiseCreateAppTable(output.availableDoctors)
			$('select.available-times').trigger('change')
			$('.clinic-appDoctors').show()
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}
/*Kreiranje pregleda - administrator klinike*/
function initialiseCreateAppTable(availableDoctors) {
	if (!$.fn.DataTable.isDataTable('#createAppTable')) {
		createAppTable = $('#createAppTable').DataTable({
			data: availableDoctors,
			columns: [
				{
					data: null,
					render: function(data) {
						return data.name + " " + data.surname
					}
				},
				{ 
					data: null,
					render: function() {
						return newAppointment.appTypeName
					}
				},
				{ 
					data: null,
					render: function() {
						return newAppointment.date
					}
				},
				{
					data: null,
					render: function(data) {
						return data.price + " &euro;"
					}
				},
				{
					data: null,
					render: function() {
						return '<input type="number" class="form-control input-height discount" min="0" max="100" value="0"/>'
					}
				},
				{
					data: null,
					render: function (data) {
						var availableTimes = data.availableTimes == null ? [] : (data.availableTimes instanceof Array ? data.availableTimes : [data.availableTimes])
						var options = ""
						for (var i = 0; i < availableTimes.length; i++) {
							options += '<option value="'+availableTimes[i]+'">'+availableTimes[i]+':00</option>'
						}
						return '<select class="form-control input-height available-times" id="'+data.id+'">' + 
								options + '</select>'
					}
				},
				{
					data: null,
					render: function(data) {
						return data.duration + " h"
					}
				},
				{
					data: null,
					render: function() {
						return "Choose start time first."
					}
				},
				{
					data: null,
					render: function (data) {
						if (newAppointment.appTypeName && newAppointment.date && newAppointment.time) {
							var button = '<button id="'+data.id+'" class="btn btn-info">Create</button>';
							return button;
						} else {
							return "Choose all parameters first."
						}
					}
				}]
		})
	}else {
		// jeste inicijalizovana
		/*createAppTable.ajax.url("../../theGoodShepherd/doctor/availableDoctorOrdinations/"+appDate+"/"+appTypeName)
		createAppTable.ajax.reload()*/
		createAppTable.clear().rows.add(availableDoctors).draw();
	}
}
/*******************************************/

function viewAppointments(){
	//nije inicijalizovana tabela
	if (!$.fn.DataTable.isDataTable('#predefinedAppointmentTable')) {
		predefinedAppointmentTable = $('#predefinedAppointmentTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/appointment/freePredefined",
				dataSrc: ''
			},
			columns: [
				{ data: 'appType'},
				{ data: 'doctor'},
				{ data : 'ordination'},
				{ data: 'date'},
				{ data: null,
					render: function(data) {
						return data.startTime + ":00"
					}
				},
				{ data: null,
					render: function(data) {
						return data.endTime + ":00"
					}
				},
				{
					data: null,
					render: function(data) {
						return  '<div class="table-action-btns">'+
									'<span id="'+data.id+'" class="table-action delete-appointment"><i class="fas fa-trash"></i></span>'+
								'</div>'
					}
				}
				]
		})
	}else {
		// jeste inicijalizovana
		predefinedAppointmentTable.ajax.url( "../../theGoodShepherd/appointment/freePredefined")
		predefinedAppointmentTable.ajax.reload()
	}
}

function viewUpcomingAppointments(){
	//nije inicijalizovana tabela
	if (!$.fn.DataTable.isDataTable('#upcomingAppointmentsTable')) {
		upcomingAppointmentTable = $('#upcomingAppointmentsTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/appointment/upcoming",
				dataSrc: ''
			},
			columns: [
				{ data: 'appType'},
				{ data: 'doctor'},
				{ data: 'patient'},
				{ data : 'ordination'},
				{ data: 'date'},
				{ data: null,
					render: function(data) {
						return data.startTime + ":00"
					}
				},
				{ data: null,
					render: function(data) {
						return data.endTime + ":00"
					}
				}
				]
		})
	}else {
		// jeste inicijalizovana
		upcomingAppointmentTable.ajax.url( "../../theGoodShepherd/appointment/upcoming")
		upcomingAppointmentTable.ajax.reload()
	}
}

/*******************************************/

//TREBA DA SE POPUNE I GRAFIKONI I NE ZNAM NI JA STA
function viewClinicReport(){
	$.ajax({
		type : "GET",
		url : "../../theGoodShepherd/clinics/viewClinicInformation",
		dataType : "application/json",
		success : function(output){
			$("imeKlinike").val(output.name)
			$("ocenaKlinike").val(output.rating)
			if (!$.fn.DataTable.isDataTable('#nazivTabele')) {
				doctorRating = $('#nazivTabele').DataTable({
				data: output.doctorRatings,
				columns: [
					{ data: 'fullName'},
					{ data: 'rating'}]
				})
			} else{
				doctorRating.clear().rows.add(output.doctorRatings).draw();
			}
			var ctx = $('nazivCharta');
			myLineChart = new Chart(ctx, {
			    type: 'line',
			    options: {
			    	tooltips: {
			    		mode: 'index',
			    		intersect: false,
			    	},
				    scales: {
				        yAxes: [{
				          scaleLabel: {
				            display: true,
				            labelString: 'Number of Appointments'
				          }
				        }],
				        xAxes: [{
				          scaleLabel: {
				            display: true,
				            labelString: 'hours'
				          }
					    }]
				    }
			    }   
			 })
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

/*******************************************/

function scheduleOrdination(ordinationId, currentDate, time) {
	examReq.ordId = ordinationId
	examReq.date = currentDate
	examReq.time = time
	alert("You scheduled an examination room for an appointment!")
	$.ajax({
		type : "POST",
		url : "../../theGoodShepherd/clinicAdmin/acceptAppointmentRequest",
		contentType : "application/json",
		data : JSON.stringify(examReq),
		success : function(){
			$('.content').hide()
			$('.clinic-clinicExamReq').show()
			$('.clinic-exam-rooms').hide()
			examReqTable.ajax.reload()
			// scroll to top of page
			document.body.scrollTop = 0
			document.documentElement.scrollTop = 0
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function scheduleOperation(ordinationId, currentDate, time, doctors) {
	examReq.ordId = ordinationId
	examReq.date = currentDate
	examReq.time = time
	
	alert("You scheduled an operation room for an operation!")
	$.ajax({
		type : "POST",
		url : "../../theGoodShepherd/clinicAdmin/acceptOperationRequest",
		contentType : "application/json",
		data : JSON.stringify({
			reqId: examReq.reqId,
			ordId : ordinationId,
			date: examReq.date,
			time: examReq.time,
			doctors: doctors
		}),
		success : function(){
			operReqTable.ajax.reload()
			$('#operRoomBody').empty()
			// scroll to top of page
			document.body.scrollTop = 0
			document.documentElement.scrollTop = 0
			$('.content').hide()
			$('.clinic-clinicOperReq').show()
			$('.clinic-oper-rooms').hide()
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function filterExaminationRooms(){
	
	var nameV = $('#examRoomName').val()
	var roomNumberV = $('#examRoomNumber').val()
	
	examRoomTable
    .column(0)
    .search(nameV)
    .draw();
	
	examRoomTable
    .column(1)
    .search(roomNumberV)
    .draw();
}

function viewExaminationRoomsForAppointmentRequests(){
	// tabela sa sobama za pregled
	if (!$.fn.DataTable.isDataTable('#examRoomTable')) {
		examRoomTable = $('#examRoomTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/ordination/getAvailableExaminationRooms/"+examReq.reqId,
				dataSrc: '',
				async: false
			},
			columns: [
				{ data: 'name'},
				{ data: 'ordinationNumber'},
				{ data: 'date'},
				{
					data: null,
					render: function (data) {
						var availableTimes = data.availableTimes == null ? [] : (data.availableTimes instanceof Array ? data.availableTimes : [data.availableTimes])
						
						if (data.date != examReq.date) {
							changedExamTime = true
						} else {
							if (availableTimes.length > 1) {
								changedExamTime = true
							} else {
								if (availableTimes[0] != examReq.time) {
									changedExamTime = true
								} else {
									changedExamTime = false
								}
							}
						}
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
						if (!examReq.reqId) {
							return "Specify examination request to schedule"
						}
						return '<button id="'+data.id+'" name="'+data.date+'" class="btn btn-info table-button-schedule">Schedule ordination</button>';
					}
				}]
		})
	} else {
		examRoomTable.ajax.url("../../theGoodShepherd/ordination/getAvailableExaminationRooms/"+examReq.reqId)
		examRoomTable.ajax.reload()
	}
	if (changedExamTime) {
		alert("No examination rooms available for given date and time.\nPlease choose new examination time.")
	}
}

function filterOperationRooms(){
	
	var nameV = $('#operRoomName').val()
	var roomNumberV = $('#operRoomNumber').val()
	
	operRoomTable
    .column(0)
    .search(nameV)
    .draw();
	
	operRoomTable
    .column(1)
    .search(roomNumberV)
    .draw();
}

function viewOperationRoomsForAppointmentRequests(){
	// tabela sa sobama za operacije
	if (!$.fn.DataTable.isDataTable('#operRoomTable')) {
		operRoomTable = $('#operRoomTable').DataTable({
			ajax: {
				url: "../../theGoodShepherd/ordination/getAvailableOperationRooms/"+examReq.reqId,
				dataSrc: '',
				async: false
			},
			columns: [
				{ data: 'name'},
				{ data: 'ordinationNumber'},
				{ data: 'date'},
				{
					data: null,
					render: function (data) {
						var availableTimes = data.availableTimes == null ? [] : (data.availableTimes instanceof Array ? data.availableTimes : [data.availableTimes])
						
						if (data.date != examReq.date) {
							changedOperTime = true
						} else {
							if (availableTimes.length > 1) {
								changedOperTime = true
							} else {
								if (availableTimes[0] != examReq.time) {
									changedOperTime = true
								} else {
									changedOperTime = false
								}
							}
						}
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
					render: function(data) {
						var options = ""
							$.ajax({
								type : "GET",
								async: false,
								url : "../../theGoodShepherd/doctor/certified/operations/"+examReq.reqId,
								dataType: "json",
								success : function(output)  {
									$.each(output, function(index, doctor){
										options += '<option id="'+ doctor.id +'">'+ doctor.name + ' ' + doctor.surname +'</option>'
									})
									$('#operation'+data.id).select2()
								}
							})
						return '<select class="form-control input-height available-times overflow" multiple="multiple" id="operation'+data.id+'">'+ options +'</select>'
					}
				},
				{
					data: null,
					render: function (data) {
						if (!examReq.reqId) {
							return "Specify operation request to schedule"
						}
						return '<button id="'+data.id+'" name="'+data.date+'" class="btn btn-info table-button-schedule-operation">Schedule ordination</button>';
					}
				}]
		})
	} else {
		operRoomTable.ajax.url("../../theGoodShepherd/ordination/getAvailableOperationRooms/"+examReq.reqId)
		operRoomTable.ajax.reload()
	}
	if (changedOperTime) {
		alert("No operation rooms available for given date and time.\nPlease choose new operation time.")
	}
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

function clearOrdinationForm() {
	$("#ordination_name").val('')
	$("#ordination_number").val('')
}

function clearAppTypeForm() {
	$("#appointment_name").val('')
	$("#appointment_duration").val('')
	$("#appointment_price").val('')
	$('#certifiedDoctors').val(null).trigger('change')
}

function clearDoctorForm() {
	$("#doctorEmail").val('')
	$("#firstNameDoctor").val('')
	$("#lastNameDoctor").val('')
	$("#passwordDoctor").val('')
	$("#dateOfBirthDoctor").val('')
	$("#securityNumberDoctor").val('')
	$("#startWorkDoctor").val('')
	$("#endWorkDoctor").val('')
	$("#phoneNumberDoctor").val('')
	$("#addressDoctor").val('')
	$("#cityDoctor").val('')
	$("#countryDoctor").val('')
}


function logInClinicAdmin(email, password){
	
    $.ajax({
		type : "POST",
		async: false,
		url : "theGoodShepherd/clinicAdmin/logIn//" + email + "//" + password ,
		dataType: "json",
		success : function(output)  {
			sessionStorage.setItem('nameSurname', output.name + ' ' + output.surname);
			window.location.href = "html/home-pages/clinic_admin_hp.html"
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function acceptReq(id){
	
	//kursor ceka
	$("body").css("cursor", "progress")
	$.ajax({
        type : "POST",
        url : "/theGoodShepherd/leaveRequest/acceptLeaveRequest",
        contentType : "application/json",
        //dataType: "json",
        data : JSON.stringify({
            "id" : id
        }),
        success : function()  {
        	vacationTable.ajax.reload();
        	$('.content').hide()
    		$('.clinic-vacation').show()
    		$("body").css("cursor", "default");
            alert("Leave request accepted!")
        },
        error : function(response) {
        	alert(response.responseJSON.message)
        }
    })
}
function declineReq(id){
	declineId = id;
	$('.content').hide()
	$('.leave-req-decline').show()
}
