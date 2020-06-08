$(document).ready(function() {
    $('#addCA').click(function(e){
        e.preventDefault()
        
        var name = $('#name').val()
        var surname = $('#surname').val()
        var country = $('#country').val()
        var city = $('#city').val()
        var address = $('#address').val()
        var phone = $('#phone').val()
        var security = $('#security').val()
        var username = $('#username').val()
        var password = $('#password').val()
        
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
                "address" : address,
                "city": city,
                "country" : country,
                "phoneNumber" : phone,
                "securityNumber" : security,
                "clinicalCentre" : {
                	"name" : "The Good Shepherd"
                }
            }),
            success : function(response)  {
                alert("uspelo")
            },
            error : function(response) {
                alert("nije uspelo")
            }
        })
    })
})
