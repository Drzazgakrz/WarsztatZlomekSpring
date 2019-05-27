function setView(id) {
    switch (id) {
        case "currentButton":
            $("#currentDiv").css("display","block");
            $("#formerDiv").css("display","none");
            $("#formerButton").removeClass("btn-primary");
            break;
        case "formerButton":
            $("#formerDiv").css("display","block");
            $("#currentDiv").css("display","none");
            $("#currentButton").removeClass("btn-primary");
            break;
    }
    $("#"+id).addClass("btn-primary");
}