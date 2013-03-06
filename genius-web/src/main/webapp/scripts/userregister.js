$(document).ready(function(){
    $("#username").keyup(function(){
        $(this).siblings("#state").html(CheckUserName($(this).attr("value")));
    });
    $("#password").keyup(function(){
        $(this).siblings("#state").html(CheckPassWord($(this).attr("value")));
    });
    $("#submit").click(function(){
        if (CheckUserName($("#username").attr("value")) != "invaild username" &&
        CheckPassWord($("#password").attr("value")) != "invaild password") {
            $("#submit").siblings("#state").html("send now!");
            $.post("register.action", $("#regform").serialize(), function(data){
                $("#submit").siblings("#state").html(data);
            });
        }
        else {
            $("#username").siblings("#state").html(CheckUserName($("#username").attr("value")));
            $("#password").siblings("#state").html(CheckPassWord($("#password").attr("value")));
        }
    });
    
});
function CheckUserName(user){
    if (user.match("^[A-Za-z0-9_]{3,16}") != user) {
        return "invaild username";
    }
    return true;
}

function CheckPassWord(password){
    if (password.match("^[A-Za-z0-9_]{6,18}") != password) {
        return "invaild password";
    };
    return true;
}
