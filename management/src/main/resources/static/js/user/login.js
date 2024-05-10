$(document).ready(function(){})
$("#loginButton").on("click",function(e){
        e.preventDefault();
        if($('#loginForm').valid())
        {

        console.log(window);
        var data={
            userName:$("#userName").val(),
            password:$("#password").val()
        }
        console.log($("#userName").val());
       $.ajax({
                   type: "POST",
                   contentType: "application/json",
                   url: window.origin + "/api/auth/login",
                   dataType: 'json',
                   data: JSON.stringify(data),
                   success: function(result) {
                        console.log(result)
                        if(result.isErrorMessage)
                        {
                            window.location.href="/user/index"
                       } else {
                           handleLoginError(result.errorMessage);
                       }
                   },
                   error: function(xhr, status, error) {
                    swal({
                        title: 'Login Message',
                        text: xhr.responseJSON.errorMessage,
                        icon: 'info',
                        });
                       console.log("STATUS:",status)
                       console.log("ERROR: ", xhr.responseJSON.errorMessage);

                   }
               });
               }
               else
               {    return false;
               }
       })

       $("#register").on("click",function(){
            window.location.href="/user/register"

       })
$('#loginForm').validate({
    rules: {
        userName: {
            required: true
        },
        password:{
            required:true,
        }
    },
    messages: {
       userName: {
                   required: "Please Enter the UserName"
               },
               password:{
                 required:"Please Enter the password",

               }

    },
    errorElement: 'div',
    errorPlacement: function(error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function(element, errorClass, validClass) {
        $(element).addClass('is-invalid').removeClass('is-valid');
    },


});
$('.form-control').on('keyup', function() {
    $(this).removeClass('is-invalid');
});