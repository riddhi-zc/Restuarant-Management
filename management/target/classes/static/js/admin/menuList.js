var pageNo=0;
var pageSize=10;
var token;
const jwtToken = getCookie('SESSION_USER');
 $(document).ready(
     function() {
         if(localStorage.getItem("menupageNo")==null)
         {
            localStorage.setItem("menupageNo",pageNo+1)
         }
         else
         {
             localStorage.removeItem("menupageNo");
         }
      loadData(pageNo, pageSize)
     }
 )
 function loadData(pageNo,pageSize){

       $.ajax({
                url: window.origin + '/api/menu/getMenuByCategories?pageNo=' + pageNo + '&pageSize=' + pageSize, // Endpoint to fetch data from
                type: 'GET',
                headers: {
                        'Authorization': 'Bearer ' + jwtToken // Replace 'yourTokenHere' with the actual token value
                    },
                success: function(data) {
                    if (data.isErrorMessage) {
                      let menuData=data.response;
                        var numberOfPages=Math.ceil(menuData.length / 2);
                      $('#light-pagination').pagination({
                                                                                         pages: numberOfPages,
                                                                                          selectOnClick:true,
                                                                                         onPageClick: function(event) {
                                                                                         pageNo=event-1;
                                                                                         localStorage.setItem("menupageNo",pageNo)
                                                                                      buildDataTable(pageNo,pageSize)

                                                                                     }
                                                                    })
                        console.log(menuData)

             var menusContainer = $('.menus');

                $.each(menuData, function(index, item) {
                    var categoryHtml = '<h3 class="fw-bold">' + item.categories + '</h3>';
                    var itemListHtml = '<ul class="list-group list-group-flush">';

                    $.each(item.itemList, function(i, menu) {
                        var menuHtml = '<li class="list-group-item d-flex justify-content-between align-items-start">' +
                                            '<div class="ms-2 me-auto">' +
                                                '<h6 style="font-weight:700">' + menu.item + '</h6>' +
                                                '<div style="margin-left:10px;">' + menu.description + '</div>' +
                                            '</div>' +
                                            '<span class="badge bg-primary rounded-pill" style="color:white;">' + menu.price + '</span>' +'</li>';
                                            //'<span type="button" class="btn bg-primary" style="color: black;">Add</span>' +

                        itemListHtml += menuHtml;
                    });

                    itemListHtml += '</ul>';

                    var sectionHtml = '<section class="menus">' + categoryHtml + itemListHtml + '</section>';
                    menusContainer.append(sectionHtml);
       })

        }
    }
    })
 }
 function buildDataTable(pageNo,pageSize)
 {

       $.ajax({
                url: window.origin + '/api/menu/getMenuByCategories?pageNo=' + pageNo + '&pageSize=' + pageSize, // Endpoint to fetch data from
                type: 'GET',
                headers: {     'Authorization': 'Bearer ' + jwtToken // Replace 'yourTokenHere' with the actual token value
                                    },
                success: function(data) {
                    if (data.isErrorMessage) {
                      let menuData=data.response;
             var menusContainer = $('.menus');
                menusContainer.empty();
                $.each(menuData, function(index, item) {
                    var categoryHtml = '<h3 class="fw-bold">' + item.categories + '</h3>';
                    var itemListHtml = '<ul class="list-group list-group-flush">';

                    $.each(item.itemList, function(i, menu) {
                        var menuHtml = '<li class="list-group-item d-flex justify-content-between align-items-start">' +
                                            '<div class="ms-2 me-auto">' +
                                                '<h6 style="font-weight:700">' + menu.item + '</h6>' +
                                                '<div style="margin-left:10px;">' + menu.description + '</div>' +
                                            '</div>' +
                                            '<span class="badge bg-primary rounded-pill" style="color:white;">' + menu.price + '</span>' +'</li>';
                        itemListHtml += menuHtml;
                    });
                    itemListHtml += '</ul>';
                    var sectionHtml = '<section class="menus">' + categoryHtml + itemListHtml + '</section>';
                    menusContainer.append(sectionHtml);
       })

        }
    }
    })
 }
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
