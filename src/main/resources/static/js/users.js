function setView(id) {
    switch (id) {
        case "activeButton":
            $("#activeAccountsDiv").css("display","block");
            $("#bannedAccountsDiv").css("display","none");
            $("#removedAccountsDiv").css("display","none");
            $("#removedButton").removeClass("btn-primary");
            $("#bannedButton").removeClass("btn-primary");
            break;
        case "bannedButton":
            $("#bannedAccountsDiv").css("display","block");
            $("#activeAccountsDiv").css("display","none");
            $("#removedAccountsDiv").css("display","none");
            $("#removedButton").removeClass("btn-primary");
            $("#activeButton").removeClass("btn-primary");
            break;
        case "removedButton":
            $("#removedAccountsDiv").css("display","block");
            $("#bannedAccountsDiv").css("display","none");
            $("#activeAccountsDiv").css("display","none");
            $("#activeButton").removeClass("btn-primary");
            $("#bannedButton").removeClass("btn-primary");
            break;
    }
    $("#"+id).addClass("btn-primary");
}