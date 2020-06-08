var clinicAdminsTable;
var clinicalCenterAdminsTable;
var clinicsTable;
var requestsTable;
var diagnosisTable;
var medicineTable;
var choosenRequest;
var declineId;

//ako su personalni podaci editovani, ponovo saljemo upit serveru
var edited = false;
$(document).ready(function() {
	
	// postavljanje maksimalnog datuma rodjenja koji se moze odabrati na danas
	document.getElementById("dateOfBirthEdit").max = new Date().toISOString().split("T")[0];
	document.getElementById("dateBirthCA").max = new Date().toISOString().split("T")[0];
	document.getElementById("dateBirthC").max = new Date().toISOString().split("T")[0];

	//select2 ne radi bez ovoga
	$.fn.modal.Constructor.prototype.enforceFocus = function() {};
	
	/*------------------------------------------------------------------*/
	/*View personal information*/
	$('.cca-profile').on('click', function(e){
		e.preventDefault()
		viewPersonalInformation()
	})
	
	/*Edit personal information*/
	$('#editClinicalCentreAdminProfile').click(function(e) {
		e.preventDefault();
		$('.content').hide()
        $('.clinical-centre-admin-edit-profile').show()
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
		$('.clinical-centre-admin-profile').show()
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
			url : "../../theGoodShepherd/clinicalCenterAdmin/changePassword/"+passwordV,
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
	
	/*------------------------------------------------------------------*/
	/*Clinic options when adding clinic admin*/
	$('#addClinicAdmin').click(function() {
		$.ajax({
            type : "GET",
            url : "/theGoodShepherd/clinics",
            dataType: "json",
            success : function(data)  {
            	$("#availableClinics").empty()
            	$.each(data, function(index, clinic) {
					$("#availableClinics").append(new Option(clinic.name, clinic.id));
				})
            },
            error : function(response) {
            	alert(response.responseJSON.message)
            }
        })
	})
	
	/*View all clinic admins*/
	$('#clinicAdmins').click(function(event) {
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#clinicAdminsTable')) {
			clinicAdminsTable = $('#clinicAdminsTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/clinicAdmin",
					dataSrc: ''
				},
				columns: [
					{ data: 'email'},
					{ data: 'name'},
					{ data: 'surname'},
					{ data: 'gender'},
					{ data: 'dateOfBirth'},
					{ data: null,
						render: function (data) {
							if(data.address == ''){
								return '/'
							}else{
								return data.address
							}
						}
					},
					{ data: null,
						render: function (data) {
							if(data.city == ''){
								return '/'
							}else{
								return data.city
							}
						}
					},
					{ data: null,
						render: function (data) {
							if(data.country == ''){
								return '/'
							}else{
								return data.country
							}
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
					{ data: 'securityNumber'},
					{
						data: null,
						render: function (data) {
							return data.clinicName;
						}
					}]
				})
		} else {
			clinicAdminsTable.ajax.reload()
		}
	})
	/*Add new clinic admin*/
	$('#add_clinicAdmin').click(function(e){
        e.preventDefault()
        
        var name = $('#nameC').val()
        var surname = $('#surnameC').val()
        var country = $('#countryC').val()
        var city = $('#cityC').val()
        var address = $('#addressC').val()
        var phone = $('#phoneC').val()
        var security = $('#securityC').val()
        var username = $('#emailC').val()
        var password = $('#passwordC').val()
        var clinicId = $('#availableClinics').val()
        var gender = $('#genderC').val()
        var birth = $('#dateBirthC').val()
        
        if(!name || !surname || !security || !username ||
        		!password || !clinicId || !gender || !birth) {
        	alert("All required fields must be filled!")
        	return;
        }
        
        if(isNaN(security)){
			alert("Security number must be a number!")
        	return;
        }
        var dateObject = new Date(birth);
        var currentDate = new Date();
        
        if(currentDate < dateObject){
            alert("Wrong date of birth!")
            return;
        }
        
        $.ajax({
            type : "POST",
            url : "/theGoodShepherd/clinicAdmin/addNewClinicAdmin/" + clinicId,
            contentType : "application/json",
            dataType: "json",
            data : JSON.stringify({
                "email" : username,
                "password" : password,
                "name" : name,
                "surname" : surname,
                "gender" : gender,
                "dateOfBirth" : birth,
                "address" : address,
                "city": city,
                "country" : country,
                "phoneNumber" : phone,
                "securityNumber" : security
            }),
            success : function(response)  {
            	alert("New clinic admin added!")
            	
            	$('#emailC').val('')
		    	$('#passwordC').val('')
		    	$("#passwordRepeatC").val('')
		    	$('#nameC').val('')
		    	$('#surnameC').val('')
		        $('#countryC').val('')
		        $('#cityC').val('')
		        $('#addressC').val('')
		        $('#phoneC').val('')
		        $('#securityC').val('')
		        $('#clinicName').val('')
		        $('#genderC').val('')
		        $('#dateBirthC').val('')
		        document.body.scrollTop = 0
		        document.documentElement.scrollTop = 0
            	$('.content').hide()
        		$('.clinic-admins').show()
            	clinicAdminsTable.ajax.reload();
            },
            error : function(response) {
            	alert(response.responseJSON.message)
            }
        })
    })
	
	/*Check if securityNumber is number*/
	$("#securityC").on('blur', function(e){
		e.preventDefault()
		if(isNaN($("#securityC").val())){
			showValidate($("#securityC"))
		} else {
			hideValidate($("#securityC"))
		}
	})
	$("#securityC").on('click', function(e){
		hideValidate($("#securityC"))
	})
    
    $('#cancel_clinicAdmin').click(function(e){
		e.preventDefault()
		hideValidate($("#securityC"))
    	$('#emailC').val('')
    	$('#passwordC').val('')
    	$("#passwordRepeatC").val('')
    	$('#nameC').val('')
    	$('#surnameC').val('')
        $('#countryC').val('')
        $('#cityC').val('')
        $('#addressC').val('')
        $('#phoneC').val('')
        $('#securityC').val('')
        $('#clinicName').val('')
        $('#genderC').val('')
        $('#dateBirthC').val('')
        $('.addClinicAdmin').hide()
		$('.clinic-admins').show()
	    document.body.scrollTop = 0
	    document.documentElement.scrollTop = 0
    })
	
    
	/*View all clinics*/
    $('#clinics').click(function(event) {
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#clinicTable')) {
			clinicsTable = $('#clinicTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/clinics",
					dataSrc: ''
				},
				columns: [
					{ data: 'name'},
					{ data: 'address'},
					{ data: 'city'},
					{ data: 'country'},
					{ data: null,
						render: function (data) {
							if(data.description == ''){
								return '/'
							}else{
								return data.description
							}
						}
					}
				]
			})
		} else {
			clinicsTable.ajax.reload()
		}
	})
	
    /*Add new clinic*/
	$('#add_clinic').click(function(e){
        e.preventDefault()
        var name = $('#nameClinic').val()
        var country = $('#countryClinic').val()
        var city = $('#cityClinic').val()
        var address = $('#addressClinic').val()
        var description = $('#descriptionClinic').val().trim()
        
        if( !name || !country || !city || !address){
        	alert("All required fields must be filled!")
        	return;
        }
        
        $.ajax({
            type : "POST",
            url : "/theGoodShepherd/clinics/addNewClinic",
            contentType : "application/json",
            dataType: "json",
            data : JSON.stringify({
                "name" : name,
                "address" : address,
                "city": city,
                "country" : country,
                "description" : description
            }),
            success: function(data){
            	alert("Successfully added new clinic!")
            	$('#nameClinic').val('')
		        $('#countryClinic').val('')
		        $('#cityClinic').val('')
		        $('#addressClinic').val('')
		        $('#descriptionClinic').val('')
            	$('.content').hide()
        		$('.clinics').show()
				clinicsTable.ajax.reload();
				document.body.scrollTop = 0
				document.documentElement.scrollTop = 0
            },
            error : function(response) {
            	alert(response.responseJSON.message)
            }
        })
    })
    
    $('#cancel_clinic').click(function(e){
    	e.preventDefault()
    	$('#nameClinic').val('')
        $('#countryClinic').val('')
        $('#cityClinic').val('')
        $('#addressClinic').val('')
        $('#descriptionClinic').val('')
        $('.content').hide()
		$('.clinics').show()
        document.body.scrollTop = 0
        document.documentElement.scrollTop = 0
    })
	
    /*View all clinical center admins*/
     $('#centerAdmins').click(function(event) {
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#clinicalCenterAdmins')) {
			clinicalCenterAdmins = $('#clinicalCenterAdmins').DataTable({
				ajax: {
					url: "../../theGoodShepherd/clinicalCenterAdmin",
					dataSrc: ''
				},
				columns: [
					{ data: 'email'},
					{ data: 'name'},
					{ data: 'surname'},
					{ data: 'gender'},
					{ data: 'dateOfBirth'},
					{ data: null,
						render: function (data) {
							if(data.address == ''){
								return '/'
							}else{
								return data.address
							}
						}
					},
					{ data: null,
						render: function (data) {
							if(data.city == ''){
								return '/'
							}else{
								return data.city
							}
						}
					},
					{ data: null,
						render: function (data) {
							if(data.country == ''){
								return '/'
							}else{
								return data.country
							}
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
					{ data: 'securityNumber'}]
			})
		} else {
			clinicalCenterAdmins.ajax.reload()
		}
	})
    /*Add new clinical center admin*/
    $('#addClinicalCenterAdmin').click(function(e) {
    	e.preventDefault()
    	$('.content').hide()
    	$('.addClinicalCenterAdmin').show()
    })
    
    $('#add_centreAdmin').click(function(e){
        e.preventDefault()
        
        var name = $('#nameCA').val()
        var surname = $('#surnameCA').val()
        var country = $('#countryCA').val()
        var city = $('#cityCA').val()
        var address = $('#addressCA').val()
        var phone = $('#phoneCA').val()
        var security = $('#securityCA').val()
        var username = $('#emailCA').val()
        var password = $('#passwordCA').val()
        var clinicalCentre = $('#clinicalCentre').val()
        var gender = $('#genderCA').val()
        var birth = $('#dateBirthCA').val()
        
        if( !name || !surname || !security || !username || !password ||
        		!gender || !birth){
        	alert("All required fields must be filled!")
        	return;
        }

        var currentDate = new Date();
        var birthDate = new Date(birth);
        if(currentDate < birthDate){
            alert("Wrong date of birth!")
            return;
        }
        
        if(isNaN(security)){
			alert("Security number must be a number!")
			return;
		}
        
        
        $.ajax({
            type : "POST",
            url : "/theGoodShepherd/clinicalCenterAdmin/addNewClinicalCentreAdmin",
            contentType : "application/json",
            dataType: "json",
            data : JSON.stringify({
                "email" : username,
                "password" : password,
                "name" : name,
                "surname" : surname,
                "gender" : gender,
                "dateOfBirth" : birth,
                "address" : address,
                "city": city,
                "country" : country,
                "phoneNumber" : phone,
                "securityNumber" : security
            }),
            success : function(response)  {
            	$('.content').hide()
        		$('.center-admins').show()
                alert("New clinical center admin added!")
                clearAddClinicalCenterAdmin()
                clinicalCenterAdmins.ajax.reload()
            },
            error : function(response) {
            	alert(response.responseJSON.message)
            }
        })
    })
    
    $('#cancel_centreAdmin').click(function(e){
    	e.preventDefault()
    	clearAddClinicalCenterAdmin()
    })
    
    /*Check if securityNumber is number*/
	$("#securityCA").on('blur', function(e){
		e.preventDefault()
		if(isNaN($("#securityCA").val())){
			showValidate($("#securityCA"))
		}
	})
	$("#securityCA").on('click', function(e){
		hideValidate($("#securityCA"))
	})
    
    /*View all registration requests*/
    $('#registrationReq').click(function(event) {
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#registrationReqTable')) {
			requestsTable = $('#registrationReqTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/registrationReq",
					dataSrc: ''
				},
				columns: [
					{ data: 'user.email'},
					{ data: 'user.name'},
					{ data: 'user.surname'},
					{ data: 'user.gender'},
					{ data: 'user.dateOfBirth'},
					{ data: null,
						render: function(data){
							return '<button name="acceptRequest" class="btn btn-info add-button" onclick="acceptReq('+ data.id +')">Accept</button>' +
							'<button name="declineRequest" class="btn btn-info add-button" onclick="declineReq('+ data.id +')">Decline</button>'
						}
					}
				]
			})
		} else {
			requestsTable.ajax.reload()
		}
	})
	
	/*View all code books*/
    $('#diagnosisBook').click(function(event) {
		event.preventDefault()
		// inicijalizujemo je ako vec nismo
		if (!$.fn.DataTable.isDataTable('#diagnosisTable')) {
			diagnosisTable = $('#diagnosisTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/diagnosis",
					dataSrc: ''
				},
				columns: [
					{ data: 'id'},
					{ data: 'name'},
					{ 
						data: null,
						render: function (data) {
							return data.dpDto.id;}
					}]
			})
		} else {
			diagnosisTable.ajax.reload()
		}
	})
	$('#prescriptionBook').click(function(event) {
		if (!$.fn.DataTable.isDataTable('#medicineTable')) {
			medicineTable = $('#medicineTable').DataTable({
				ajax: {
					url: "../../theGoodShepherd/prescription",
					dataSrc: ''
				},
				columns: [
					{ data: 'id'},
					{ data: 'name'}]
			})
		} else {
			medicineTable.ajax.reload()
		}
	})
	
	//Fill in the form for adding new diagnose
	$('#addDiagnose').click(function() {
		$.ajax({
            type : "GET",
            url : "/theGoodShepherd/diagnosePrescription",
            dataType: "json",
            success: function(data){
            	$("#codeBookId").empty()
            	$.each(data, function(index, dp) {
					$("#codeBookId").append(new Option(dp.id, dp.id));
				})
            },
            error : function(response) {
            	alert(response.responseJSON.message)
            }
        })
	})
	
	//Add new diagnose
	$('#add_diagnose').click(function(e){
		e.preventDefault()
		
		var name = $('#nameDiagnose').val()
		var codeBook = $("#codeBookId").val()
		
		if(!name){
			alert("All required fields must be filled!")
        	return;
		}
		
		$.ajax({
            type : "POST",
            url : "/theGoodShepherd/diagnosis/addNewDiagnose",
            contentType : "application/json",
            dataType: "json",
            data : JSON.stringify({
                "name" : name,
                "diagnosePerscription" : {
                	"id" : codeBook
                }
            }),
            success: function(data){
            	alert("Successfully added new diagnose!")
            	$('#nameDiagnose').val('')
            	$('.content').hide()
        		$('.diagnosis-book').show()
            	diagnosisTable.ajax.reload();
            },
            error : function(response) {
            	alert(response.responseJSON.message)
            }
        })
	})
	
	//Cancel adding new diagnose
	$('#cancel_diagnose').click(function(e){
		e.preventDefault()
		$('#nameDiagnose').val('')
    	$('.content').hide()
		$('.diagnosis-book').show()
	})
	
	$('#addMedicine').click(function(e){
		e.preventDefault()
		$('#medicineDiagnosis').val(null).trigger('change')
		$('#medicineDiagnosis').select2({ width: '100%' })
		
		$.ajax({
            type : "GET",
            url : "../../theGoodShepherd/diagnosis",
            dataType: "json",
            success: function(data){
            	$("#medicineDiagnosis").empty()
            	$.each(data, function(index, diagnose) {
					$("#medicineDiagnosis").append('<option id="'+diagnose.id+'">'+diagnose.name+'</option>').trigger('change');;
				})
            },
            error : function(response) {
            	alert(response.responseJSON.message)
            }
        })
	})
	
	//Add new medicine
	$('#add_medicine').click(function(e){
		e.preventDefault()
		
		var name = $('#nameMedicine').val()
		var diagnose = $('#medicineDiagnosis').val()
		
		if(!name){
			alert("All required fields must be filled!")
        	return;
		}
		
		$.ajax({
            type : "POST",
            url : "/theGoodShepherd/prescription/addNewPrescription",
            contentType : "application/json",
            dataType: "json",
            data : JSON.stringify({
                "medicine" : name,
                "diagnosis" : diagnose
            }),
            success: function(data){
            	alert("Successfully added new medicine!")
            	$('#nameMedicine').val('')
            	$('.content').hide()
        		$('.prescription-book').show()
            	medicineTable.ajax.reload(); 
    			//setDiagnosisToPrescription(data)
            },
            error : function(response) {
            	alert(response.responseJSON.message)
            }
        })
	})
	
	//Cancel adding new medicine
	$('#cancel_medicine').click(function(e){
		e.preventDefault()
		$('#medicineDiagnosis').val(null).trigger('change')
		$('#nameMedicine').val('')
    	$('.content').hide()
		$('.prescription-book').show()
	})
	
	$('#decline_regreq').click(function(e){
		var desc = $('#descriptionRegReq').val().trim()
		
		if(!desc){
			alert("All required fields must be filled!")
	    	return;
		}
		//kursor ceka
		$("body").css("cursor", "progress")
		$.ajax({
	        type : "POST",
	        url : "../../theGoodShepherd/clinicalCenterAdmin/declineRegistrationRequest",
	        contentType : "application/json",
	        //dataType: "json",
	        data : JSON.stringify({
	            "id" : declineId,
	            "description": desc
	        }),
	        success : function()  {
	        	requestsTable.ajax.reload();
	        	$('.content').hide()
	    		$('.registration-req').show()
				alert("Registration request rejected!")
				$("body").css("cursor", "default");
	        },
	        error : function(response) {
	        	alert(response.responseJSON.message)
	        }
		})
	})
	
	$('#cancel_decline').click(function(e){
		e.preventDefault()
		
		$('.content').hide()
		$('.registration-req').show()
		$('#descriptionRegReq').val('')
	})
})

function logInClinicalCentreAdmin(email, password){
	
	 $.ajax({
		type : "POST",
		async: false,
		url : "theGoodShepherd/clinicalCenterAdmin/logIn//" + email + "//" + password ,
		dataType: "json",
		success : function(output, status, xhr)  {
			sessionStorage.setItem('nameSurname', output.name + ' ' + output.surname);
			window.location.href = "html/home-pages/centar_admin_hp.html"
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function showValidate(input) {
    var thisAlert = $(input).parent();

    $(thisAlert).addClass('alert-validate');
}

function hideValidate(input) {
    var thisAlert = $(input).parent();

    $(thisAlert).removeClass('alert-validate');
}

function viewPersonalInformation(){
	// obracamo se serveru samo prvi put
	if ($("#fullNameBig").text() == "" || edited == true) {
		$.ajax({
			type : "GET",
			async: false,
			url : "../../theGoodShepherd/clinicalCenterAdmin/personalInformation" ,
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
		url : "../../theGoodShepherd/clinicalCenterAdmin/personalInformation" ,
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

	var newClinicalCentreAdmin = {
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
		url : "../../theGoodShepherd/clinicalCenterAdmin/editPersonalInformation" ,
		contentType : "application/json",
		dataType : "json",
		data : JSON.stringify(newClinicalCentreAdmin),
		success : function(response)  {
			edited = true
			$(".cca-profile").click()
			alert("Succesfully edited personal information.")
		},
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}

function clearAddClinicalCenterAdmin() {
	$('#emailCA').val('')
	$('#passwordCA').val('')
	$('#nameCA').val('')
	$('#surnameCA').val('')
    $('#countryCA').val('')
    $('#cityCA').val('')
    $('#addressCA').val('')
    $('#phoneCA').val('')
    $('#securityCA').val('')
    $('#clinicalCentreCA').val('')
    $('#genderCA').val('')
    $('#dateBirthCA').val('')
    hideValidate($("#securityCA"))
    $('.content').hide()
    $('.center-admins').show()
    document.body.scrollTop = 0
    document.documentElement.scrollTop = 0
}

function acceptReq(id){
	
	//kursor ceka
	$("body").css("cursor", "progress")
	$.ajax({
        type : "POST",
        url : "/theGoodShepherd/clinicalCenterAdmin/acceptRegistrationRequest",
        contentType : "application/json",
        //dataType: "json",
        data : JSON.stringify({
            "id" : id
        }),
        success : function()  {
        	requestsTable.ajax.reload();
        	$('.content').hide()
    		$('.registration-req').show()
    		$("body").css("cursor", "default");
            alert("Registration request accepted!")
        },
        error : function(response) {
        	alert(response.responseJSON.message)
        }
    })
}

function declineReq(id){
	declineId = id;
	$('.content').hide()
	$('.registration-req-decline').show()
}

function setDiagnosisToPrescription(data){
	
	$.ajax({
		type : "POST",
		async: false,
		url : "../../theGoodShepherd/diagnosis/addDiagnosePrescription",
		contentType: "application/json",
		data: JSON.stringify(data),
		error : function(response) {
			alert(response.responseJSON.message)
		}
	})
}