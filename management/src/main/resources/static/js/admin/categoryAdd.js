const jwtToken = getCookie('SESSION_ADMIN');
$(document).ready(function() {

    loadDataTable(0, 40);
})

$("#addButton").on('click', function() {
    console.log($("#categoriesForm").valid());
    if ($("#categoriesForm").valid()) {
        submitForm(true);
    }
})

function ajaxUpdate() {
    var cid = $("#categoryId").val();
    var formData = {
        category: $("#categoryName").val(),
    }
    $.ajax({
        type: "PUT",
        contentType: "application/json",
        url: window.origin + "/api/category/updateCategory/" + cid,
        data: JSON.stringify(formData),
        dataType: 'json',
         headers: {
                "Authorization": "Bearer "+jwtToken
            },
        success: function(result) {
            if (result.isErrorMesaage) {
                $("#categoriesForm").trigger("reset");
                swal({
                    title: 'Category Message',
                    text: "The category Name  updated Succesfully..!",
                    icon: 'info',
                })
                $('#addCategoryModal').modal('hide');
                loadDataTable(0, 20);
            } else {
                $("#categoriesForm").trigger("reset");
            }
            console.log(result);
        },
        error: function(e) {
            $("#categoriesForm").trigger("reset");
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });

}

function ajaxPost() {
    var formData = {
        category: $("#categoryName").val(),
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: window.origin + "/api/category/addCategory",
        data: JSON.stringify(formData),
        dataType: 'json',
         headers: {
                        "Authorization": "Bearer "+jwtToken
                    },
        success: function(result) {
            if (result.isErrorMesaage) {
                $("#categoriesForm").trigger("reset");
                swal({
                    title: 'Category Message',
                    text: "The category Name  inserted Succesfully..!",
                    icon: 'info',
                })
                $('#addCategoryModal').modal('hide');
                loadDataTable(0, 20);
            } else {
                $("#categoriesForm").trigger("reset");
            }
            console.log(result);
        },
        error: function(e) {
            $("#categoriesForm").trigger("reset");
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });

}
$(document).on('click', '.deleteCategory', function(e) {
    e.preventDefault();
    var id = $(this).attr('catId');

    swal({
        title: 'Category Message',
        text: "Are you sure you want to delete ?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Yes, delete it!'

    }).then(function(result) {

        if (result.value) {
            $.ajax({
                url: window.origin + '/api/category/removeCategory/' + id,
                type: 'DELETE',
                 headers: {
                                "Authorization": "Bearer "+jwtToken
                            },
                success: function(data) {
                    loadDataTable(0, 20);

                }
            });
        }
    });

});
$('.modal .close').click(function() {
    $(this).closest('.modal').modal('hide');
});
$(document).on('click', '.updateCategory', function() {
    var cid = $(this).attr("catId");
    var cName = $(this).attr("catName");
    $('#categoriesForm')[0].reset();
    $('#categoryName').val(cName);
    $('.form-control').removeClass('is-invalid')
    $("#updateButton").css("display", "");
    $("#addButton").css("display", "none");
    $("#addItemModalLabel").text("Edit Categories");
    $("#categoriesForm").append('<input type="hidden" id="categoryId"  value="' + cid + '" >');
    $('#addCategoryModal').modal('show');
});
$("#updateButton").on("click", function() {
    console.log($("#categoriesForm").valid());
    if ($("#categoriesForm").valid()) {
        submitForm(false)
    }
})

function submitForm(isNew) {
    if (isNew) {
        ajaxPost();
    } else {
        ajaxUpdate();
    }
}

function loadDataTable(pageNo, pageSize) {
    if ($.fn.DataTable.isDataTable('#categoryInfo')) {
        $('#categoryInfo').DataTable().destroy();
    }
    $.ajax({
        url: window.origin + '/api/category/getCategories',
        type: 'GET',
        success: function(data) {

            if (data.isErrorMessage) {
                var response = data.response;
                $('#categoryInfo tbody').empty();
                $.each(response, function(index, item) {
                    var row = '<tr>';
                    row += '<td>' + item.categoryNames + '</td>';
                    row += '<td> <i class="fa fa-trash deleteCategory" catId="' + item.cid + '" ></i><i style= "margin-left:10px;" class="fas fa-edit updateCategory"  catName="' + item.categoryNames + '" catId="' + item.cid + '"></i></td>';
                    row += '</tr>';
                    $('#categoryInfo tbody').append(row);
                });
                $('#categoryInfo').DataTable({
                    paging: false,
                    searching: true,
                    lengthChange: false
                });
            } else {
                //console.error(data.errorMessage);
            }
        },
        error: function(xhr, status, error) {}
    });

}
$('#addCategoryButton').click(function(e) {
    e.preventDefault();
    $('#categoriesForm')[0].reset(); // Reset the form
    $("#categoriesForm").validate().resetForm()
    $('.form-control').removeClass('is-invalid')
    $("#updateButton").css("display", "none");
    $("#addButton").css("display", "");
    $("#addItemModalLabel").text("Add Categories");
    $('#addCategoryModal').modal('show');
});

$('#categoriesForm').validate({
    rules: {
        categoryName: {
            required: true
        },
    },
    messages: {
        categoryName: {
            required: 'Please enter a category.'
        },

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
function getCookie(cookieName) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(cookieName + '=')) {
            return cookie.substring(cookieName.length + 1);
        }
    }
    return null;
}