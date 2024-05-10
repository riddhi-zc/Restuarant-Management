console.log("demo");
$(document).ready(function(){

})
 $.validator.addMethod("passwordPattern", function(value, element) {
                return this.optional(element) || /^(?=.*[A-Z]).{6,}$/.test(value);
            })
$("#register").on("click",function(e){
        e.preventDefault();
        console.log($('#registerForm').valid())
        if($('#registerForm').valid())
        {
            var data={
                  fullName:$("#fullname").val(),
                userName:$("#userName").val(),
                password:$("#password").val()
            }
              console.log($("#userName").val());

             $.ajax({
                   type: "POST",
                   contentType: "application/json",
                   url: window.origin + "/api/auth/register",
                   dataType: 'json',
                   data: JSON.stringify(data),
                   success: function(result) {
                        console.log(result)
                        if(result.isErrorMessage)
                        {
                            console.log(result.response)
                            window.location.href="/user/login"
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

       })
$("#login").on("click",function(){
    window.location.href=window.origin+"/user/login"
})
$('#registerForm').validate({
    rules: {
        fullname:{
        required:true},
        userName: {
            required: true,
            email:true
        },
        password:{
            required:true,
            minlength: 6,
            passwordPattern: true
        }
    },
    messages: {
        fullname:{
            required:"Please Enter the Full name"
        },
       userName: {
                   required: "Please Enter the UserName",
                   email:"Please Enter Valid UserName"
               },
               password:{
                 required:"Please Enter the password",
                 minlength:"Please Enter valid length of the password",
                 passwordPattern:"Please enter atleast one Uppercase, lowercase , digit"
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