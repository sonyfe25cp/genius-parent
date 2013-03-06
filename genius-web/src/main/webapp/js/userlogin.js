$(document).ready(function(){
//	$("#username").blur(function(){
//		
//		//alert("sssss");
//		var uname=$("#username").val();
//		//alert(uname);
//		if(!CheckUserName(uname)){
//			$("#checkuser").text("*用户名不合法！");
//		}
//		else
//			$("#checkuser").text("");
////		$.ajax({
////			type : "GET",
////			url : "/user/verifyuname",
////			data :  "username="+$("#username").val(),
////			success : function(back) {
////				if(back=="falied")
////					$("#checkuser").text("*用户名不存在！！");
////			},
////		error:function(){
////			}
////		});
//		
//	});
//	
//	$("#password").blur(function(){
//		var pwd=$("#password").val();
//		if(!CheckPassWord(pwd)){
//			$("#checkpwd").text("*密码不合法！");
//			
//		}
//		else
//			$("#checkpwd").text("");
//		
//		
//	});
	
$("#submit").click(function(){
		
		var uname=$("#username").val();
		var pwd=$("#password").val();
		//alert(uname+"  "+pwd);
		if(CheckUserName(uname)&&CheckPassWord(pwd)){
			$.ajax({
				type : "GET",
				url : "/user/LoginVerify",
				data :  {username:uname,password:pwd},
				success : function(back) {
					//alert(back);
					if(back=="user not exist"){
						$("#checkuser").text("*用户不存在！！");
						$("#checkpwd").text("");
					}
					else if(back=="error password"){
						$("#checkuser").text("");
						$("#checkpwd").text("*密码错误！！");
					}
					else if(back=="success"){
						window.location.href="index.jsp?username="+uname;
					}
						//SetCookie("username", uname);
					   // SetCookie("password", pwd);
						
					    //alert(back=="success");
				},
			error:function(){
				}
			});
		}
		else{
			if(!CheckUserName(uname))
				$("#checkuser").text("*用户名不合法！");
			else if(!CheckPassWord(pwd)){
				$("#checkuser").text("");
				$("#checkpwd").text("*密码不合法！");
			}
		}
		
		
		
	});
$("#setcookie").click(function(){
	
	//SetCookie("username", "shiyulong");
	//SetCookie("password", "123456");
	//getCookie("username");
	alert(getCookie("username"));
});
	
//    $("#username").keyup(function(){
//        $(this).siblings("#state").html(CheckUserName($(this).attr("value")));
//    });
//    $("#password").keyup(function(){
//        $(this).siblings("#state").html(CheckPassWord($(this).attr("value")));
//    });
//    $("#submit").click(function(){
//        if (CheckUserName($("#username").attr("value")) != "invaild username" &&
//        CheckPassWord($("#password").attr("value")) != "invaild password") {
//            $("#submit").siblings("#state").html("send now!");
//            $.post("/user/verify", $("#logform").serialize(), function(data){
//                $("#submit").siblings("#state").html(data);
//                if(data=="success")
//                {
//                	window.history.back(-1);
//                }                
//            });
//        }
//        else {
//            $("#username").siblings("#state").html(CheckUserName($("#username").attr("value")));
//            $("#password").siblings("#state").html(CheckPassWord($("#password").attr("value")));
//        }
//    });
//    
//});

});
function CheckUserName(user){
    if (user.match("^[A-Za-z0-9_]{3,16}") != user) {
        return false;
    }
    return true;
}

function CheckPassWord(password){
    if (password.match("^[A-Za-z0-9_]{6,18}") != password) {
        return false;
    }
    return true;
}
function SetCookie(name, value)//两个参数，一个是cookie的名子，一个是值  

{

	var Days = 30; //此 cookie 将被保存 30 天  

	var exp = new Date(); //new Date("December 31, 9998");  

	exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);

	document.cookie = name + "=" + escape(value) + ";expires="
			+ exp.toGMTString();

}
//function getCookie(name)//取cookies函数          
//
//{
//
//	var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
//
//	if (arr != null)
//		return unescape(arr[2]);
//	return null;
//
//}
function getCookie(objName) {//获取指定名称的cookie的值  

	var arrStr = document.cookie.split("; ");

	for ( var i = 0; i < arrStr.length; i++) {

		var temp = arrStr[i].split("=");

		if (temp[0] == objName)
			return unescape(temp[1]);

	}

}
