var instance; //for validation instance.isValid()
var pageNo=0;
var pageSize=10;
const jwtToken = getCookie('SESSION_ADMIN');
 $(document).ready(
     function() {
         ajaxGet();
         if(localStorage.getItem("menupageNo")==null)
         {
            localStorage.setItem("menupageNo",pageNo+1)
         }
         else
         {
             localStorage.removeItem("menupageNo");
         }
      loadDataTable(pageNo, pageSize)
     }
 )

 function buildCategorie(val) {
     var p = new Array()
     $.each(val, function(i, val) {

         p.push(val.categoryNames)
     })
     return p
 }

 function ajaxGet() {
     $.ajax({
         type: "GET",
         contentType: "application/json",
         url: window.origin + "/api/category/getCategories",
          headers: {
                         "Authorization": "Bearer "+jwtToken
                     },
         dataType: 'json',
         success: function(result) {
             if (result.isErrorMessage) {
                 var response = result.response;
                 instance = $('#categoryInput').magicSuggest({
                     data: buildCategorie(response)
                 });
             }
         },
         error: function(e) {
             swal({
                 title: 'Menu Message',
                 text: e,
                 icon: 'info',
             })
             console.log("ERROR: ", e);
         }
     });
 }

 function ajaxPost() {
     var categories = instance.getValue()
     var formData = new FormData();
     formData.append("categoryName", categories);
     formData.append("item", $("#itemName").val());
     formData.append("price", $("#itemPrice").val());
     formData.append("description", $("#description").val());
     formData.append('menuImage', $('#menuImage')[0].files[0])
     formData.append("menuImageName", $("#menuImage")[0].files[0].name)
     $.ajax({
         type: "POST",
         contentType: false,
         url: window.origin + "/api/menu/addMenu",
          headers: {
                         "Authorization": "Bearer "+jwtToken
                     },
         data: formData,
         processData: false,
         success: function(result) {
             if (result.isErrorMessage) {
                 $("#menuForm").trigger("reset");
                 $('#addItemModal').modal('hide');
                 swal({
                     title: 'Menu Message',
                     text: result.response,
                     icon: 'info',
                 })
                 loadDataTable(pageNo,pageSize)
             } else {
                 swal({
                     title: 'Menu Message',
                     text: 'error',
                     icon: 'info',
                 })
             }
             console.log(result);
         },
         error: function(e) {
             swal({
                 title: 'Menu Message',
                 text: e,
                 icon: 'info',
             })
             console.log("ERROR: ", e);
         }
     });
 }
 $(document).on('click', '.updateMenu', function() {
     $('#menuForm')[0].reset();
     $("#menuForm").validate().resetForm()
     $('.form-control').removeClass('is-invalid');
     instance.clear(true)
     var itemsArray = $(this).attr("catName").split(',');
     instance.setValue(itemsArray)
     $("#itemName").val($(this).attr("menuItem"));
     $("#description").val($(this).attr("description"))
     $("#itemPrice").val($(this).attr("price"))
     $("#updateButton").css("display", "");
     $("#addButton").css("display", "none");
     $("#addItemModalLabel").text("Edit Menu");
     $("#menuForm").append('<input type="hidden" id="menuId"  value="' + $(this).attr("menuId") + '" >');
     $('#addItemModal').modal('show');
 });

 function ajaxPut() {
     var mid = $("#menuId").val();
     var categories = instance.getValue()
     var formData = new FormData();
     if ($('#menuImage')[0].files.length > 0) {
         formData.append('menuImage', $('#menuImage')[0].files[0])
         formData.append("menuImageFileName", $("#menuImage")[0].files[0].name)
     }
     formData.append('item', $("#itemName").val().toString())
     formData.append("description", $("#description").val().toString())
     formData.append("categoryName", categories);
     formData.append("price", parseFloat($("#itemPrice").val()));
     $.ajax({
         type: "PUT",
         contentType: false,
         url: window.origin + "/api/menu/updateMenu/" + Number(mid),
         data: formData,
         processData: false,
         success: function(result) {
             if (result.isErrorMessage) {
                 $("#menuForm").trigger("reset");
                 $('#addItemModal').modal('hide');
                 swal({
                     title: 'Menu Message',
                     text: result.response,
                     icon: 'info',
                 })
             } else {
                 $('#addItemModal').modal('hide');
                 swal({
                     title: 'Menu Message',
                     text: result.response,
                     icon: 'error',
                 })
             }
         },
         error: function(xhr, status, error) {
             $('#addItemModal').modal('hide');
             swal({
                 title: 'Menu Message',
                 text: error,
                 icon: 'error',
             })
         }
     });
 }
 $('.modal .close').click(function() {
     $(this).closest('.modal').modal('hide');
 });
 function buildDataTable(pageNo, pageSize) {
     if ($.fn.DataTable.isDataTable('#menuInfo')) {
         $('#menuInfo').DataTable().destroy();
     }
     $.ajax({
         url: window.origin + '/api/menu/getMenus?pageNo=' + pageNo + '&pageSize=' + pageSize, // Endpoint to fetch data from
         type: 'GET',
          headers: {
                         "Authorization": "Bearer "+jwtToken
                     },
         success: function(data) {
             if (data.isErrorMessage) {
               let menuData=data.response;
               $('#menuInfo tbody').empty();
          $.each(menuData.response, function(index, item) {
                     var row = '<tr>';
                     row += '<td> <img class="img-fluid  rounded-circle" width="100" height="100" src="data:image/*;base64,' + item.menuImage + '"</td>'
                     row += '<td>' + buildCategory(item.categoryName) + '</td>';
                     row += '<td>' + item.menuItem + '</td>';
                     row += '<td>' + item.price + '</td>';
                     row += '<td>' + item.description + '</td>';
                     row += '<td> <i class="fa fa-trash deleteMenu" catId="' + item.id + '" ></i><i style= "margin-left:10px;" class="fas fa-edit updateMenu" menuId="' + item.id + '"  menuImage="data:image/*;base64,' + item.menuImage + '" menuImageName="' + item.menuImageName + '" price="' + item.price + '" catName="' + item.categoryName + '"  description="' + item.description + '" menuItem="' + item.menuItem + '"></i></td>';
                     row += '</tr>';
                     $('#menuInfo tbody').append(row);
                 });

                 $('#menuInfo').DataTable({
                     paging: false,
                     searching: true,
                     lengthChange: false
                 });


             } else {
                 console.error(data.errorMessage);
             }
         },
         error: function(xhr, status, error) {
             console.error(error);
         }
     });

 }
 function loadDataTable(pageNo, pageSize) {
     if ($.fn.DataTable.isDataTable('#menuInfo')) {
         $('#menuInfo').DataTable().destroy();
     }
     $.ajax({
         url: window.origin + '/api/menu/getMenus?pageNo=' + pageNo + '&pageSize=' + pageSize, // Endpoint to fetch data from
         type: 'GET',
          headers: {
                         "Authorization": "Bearer "+jwtToken
                     },
         success: function(data) {
             if (data.isErrorMessage) {
               let menuData=data.response;
               $('#menuInfo tbody').empty();
              var totalRecords=data.response.totalRecords;
              var numberOfPages=Math.ceil(totalRecords / pageSize);
               $('#light-pagination').pagination({
                                      pages: numberOfPages,
                                       selectOnClick:true,
                                      onPageClick: function(event) {

                                      pageNo=event-1;
                                      localStorage.setItem("menupageNo",pageNo)
                                   buildDataTable(pageNo,pageSize)

                                  }
                 })
          $.each(menuData.response, function(index, item) {
                     var row = '<tr>';
                     row += '<td> <img class="img-fluid  rounded-circle" width="100" height="100" src="data:image/*;base64,' + item.menuImage + '"</td>'
                     row += '<td>' + buildCategory(item.categoryName) + '</td>';
                     row += '<td>' + item.menuItem + '</td>';
                     row += '<td>' + item.price + '</td>';
                     row += '<td>' + item.description + '</td>';
                     row += '<td> <i class="fa fa-trash deleteMenu" catId="' + item.id + '" ></i><i style= "margin-left:10px;" class="fas fa-edit updateMenu" menuId="' + item.id + '"  menuImage="data:image/*;base64,' + item.menuImage + '" menuImageName="' + item.menuImageName + '" price="' + item.price + '" catName="' + item.categoryName + '"  description="' + item.description + '" menuItem="' + item.menuItem + '"></i></td>';
                     row += '</tr>';
                     $('#menuInfo tbody').append(row);
                 });

                 $('#menuInfo').DataTable({
                     paging: false,
                     searching: true,
                     lengthChange: false
                 });


             } else {
                 console.error(data.errorMessage);
             }
         },
         error: function(xhr, status, error) {
             console.error(error);
         }
     });

 }

 function buildCategory(categoryName) {
     let concatenatedString = categoryName.map(category => category + ',').join('');
     concatenatedString = concatenatedString.slice(0, -1);
     return concatenatedString;
 }
 $(document).on('click', '.deleteMenu', function() {
     var id = $(this).attr('catId');
     swal({
         title: 'Menu Message',
         text: "Are you sure you want to delete ?",
         icon: 'warning',
         showCancelButton: true,
         confirmButtonColor: '#d33',
         cancelButtonColor: '#3085d6',
         confirmButtonText: 'Yes, delete it!'
     }).then(function(result) {
         if (result.value) {
             $.ajax({
                 url: window.origin + '/api/menu/removeMenu/' + id,
                 type: 'DELETE',
                  headers: {
                                 "Authorization": "Bearer "+jwtToken
                             },
                 success: function(data) {
                     swal({
                         title: 'Menu Message',
                         text: data.response,
                         icon: 'success',
                         confirmButtonText: 'OK'
                     }).then(function() {
                         loadDataTable(0, 20);
                     });
                 }
             });
         }
     });

 });
 $('#addMenuButton').click(function(e) {
     e.preventDefault();
     $('#menuForm')[0].reset();
     $("#menuForm").validate().resetForm()
     $('.form-control').removeClass('is-invalid');
     $("#updateButton").css("display", "none");
     $("#addButton").css("display", "");
     $("#addItemModalLabel").text("Add Menu");
     instance.clear(true)
     ajaxGet()
     $('#addItemModal').modal('show');
 });
 $("#addButton").click(function() {
     if (($("#menuForm").valid())) {
         ajaxPost()
     }
 })
 $("#updateButton").click(function() {
     if (($("#menuForm").valid())) {
         ajaxPut()
     }
 })
 $('#menuForm').validate({
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
         description: {
             required: true,

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
         description: {
             required: 'Please enter a description.',

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