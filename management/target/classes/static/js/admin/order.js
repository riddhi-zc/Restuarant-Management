const jwtToken = getCookie('SESSION_ADMIN');
    $(document).ready(function() {
        //  $('#example-getting-started').multiselect();
    });
    var price;

    $("#updateButton").click(function(e) {
        e.preventDefault()
        if (($("#orderForm").valid())) {
            //	ajaxPut()
        }
    })
    $('.modal .close').click(function() {
        $(this).closest('.modal').modal('hide');
    });
    $("#categoryName").change(function() {
        var categoryId = $(this).find(':selected').val();
        $.ajax({
            url: window.origin + '/api/menu/getMenuByCategoryId/' + categoryId, // Endpoint to fetch data from
            type: 'GET',
             headers: {
                            "Authorization": "Bearer "+jwtToken
                        },
            success: function(data) {
                if (data.isErrorMessage) {
                    var response = data.response;
                    console.log(response)
                    var items = response.itemList;
                    console.log(items);
                    $('#itemName').empty()
                    $.each(items, function(val, text) {
                        $('#itemName').append($('<option price="' + text.price + '"></option>').val(text.id).html(text.item));
                    })
                } else {
                    console.error(data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                console.error(error);
            }
        });
    });

    $("#itemName").on('change', function() {
        var price = $(this).find(':selected').attr('price');
        console.log(price);
        $("#itemPrice").val(price)
        $("#itemPrice").attr('disabled','disabled')

    })
    $("#itemQuantity").on('change', function() {
        if ($("#itemQuantity").val() > 1) {
            $("#itemPrice").val($("#itemPrice").val() * $("#itemQuantity").val());
        } else {
            $("#itemPrice").val($("#itemName").find(':selected').attr('price'))
        }
    })
    $('#addMenuButton').click(function(e) {
        e.preventDefault();
        $('#orderForm')[0].reset();
        $("#orderForm").validate().resetForm()
        $('.form-control').removeClass('is-invalid');
        $("#updateButton").css("display", "none");
        $("#addButton").css("display", "");
        $("#addItemModalLabel").text("Add Menu");
        $('#placeOrder').modal('show');
    });
    $('#orderForm').validate({
        rules: {
            categoryInput: {
                required: true,
            },
            itemName: {
                required: true
            },
            itemPrice: {
                required: true,
                number: true
            },
            itemQuantity: {
                required: true,
                number: true
            }
        },
        messages: {
            categoryInput: {
                required: 'Please enter a category.'

            },
            itemName: {
                required: 'Please enter a menu item.',

            },
            itemPrice: {
                required: 'Please enter a price.',
                number: 'Please enter a valid number.'
            },
            itemQuantity: {
                required: 'Please enter a description.',
                number: 'Please enter quantity in number'
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
    $("#viewTotal").on('click',function(){
body+='<tr>'
        body+='<td colspan="2">Total Price :</td>';
        body+='<td colspan="2">'+totalPrice+'</td>';
        body+='</tr>'
          $("#itemList").append(body)
    })
    $('.form-control').on('keyup', function() {
        $(this).removeClass('is-invalid');
    });
    let totalPrice=0;
    $("#addButton").on('click', function(e) {
        e.preventDefault();


        var body = '<tr>';
        if (($("#orderForm").valid())) {
            totalPrice=Number(totalPrice)+Number($("#itemPrice").val())
            console.log(totalPrice)
            body += '<td>' + $('#categoryName :selected').text() + '</td>';
            body += '<td>' + $("#itemName :selected").text() + '</td>';
            body += '<td>' + $("#itemQuantity").val() + '</td>';
            body += '<td>' + $("#itemPrice").val() + '</td>';
            body += '<td><i class="fa fa-trash deleteItem" ></i><i style= "margin-left:10px;" class="fas fa-edit updateMenu"></i></td>';
            body += '</tr>';
           $("#itemList").append(body)

        }


             $("#orderForm")[0].reset();
    })
    $(document).on('click', '.deleteItem', function() {
        console.log(
            $(this).closest("tr").remove())
        })
            $(document).on('click', '.updateMenu', function() {
                console.log($(this).closest('tr').children('td:eq(0)').text())
                console.log($(this).closest('tr').children('td:eq(1)').text())
                console.log($(this).closest('tr').children('td:eq(2)').text())
                console.log($(this).closest('tr').children('td:eq(3)').text())

                })

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