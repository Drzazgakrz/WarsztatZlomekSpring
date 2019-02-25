function checkToken() {
    var token = sessionStorage.getItem("accessToken");
    if (token ==="" && window.location.href !== window.location.origin+"/WarsztatZlomek/") {
        window.location = window.location.origin + "/WarsztatZlomek/"
    }
}

function setPath(endpoint){
    var url = window.location.origin+"/WarsztatZlomek/"+endpoint;
    $.ajax({
        url:url,
        data:{
            accessToken:sessionStorage.getItem("accessToken")
        }
    });
}