function setVisibility() {
    if($("#invoicePositions").css("display")==="none"){
        $("#invoicePositions").css("display","table");
    }else {
        $("#invoicePositions").css("display","none");
    }
}

var visitsArray;
var companiesArray;

function storeData(visits, companies) {
    visitsArray = visits;
    companiesArray = companies;
}