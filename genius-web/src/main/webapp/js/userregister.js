
$(document).ready(function(){
	$("#username").blur(function(){
		
		//alert("sssss");
		var uname=$("#username").val();
		//alert(CheckUserName(uname));
		if(!CheckUserName(uname)){
			$("#checkuser").text("*用户名不合法！");
		}
		else
			$("#checkuser").text("");
//		$.ajax({
//			type : "GET",
//			url : "/user/verifyuname",
//			data :  "username="+$("#username").val(),
//			success : function(back) {
//				if(back=="falied")
//					$("#checkuser").text("*用户名不存在！！");
//			},
//		error:function(){
//			}
//		});
		
	});
	
	$("#password").blur(function(){
		var pwd=$("#password").val();
		if(!CheckPassWord(pwd)){
			$("#checkpwd").text("*密码不合法！");
			
		}
		else
			$("#checkpwd").text("");
		
		
	});
	
$("#submit").click(function(){
		
		var uname=$("#username").val();
		var pwd=$("#password").val();
		//alert(uname+"  "+pwd);
		if(CheckUserName(uname)&&CheckPassWord(pwd)){
			$.ajax({
				type : "GET",
				url : "/user/add",
				data :  {username:uname,password:pwd},
				success : function(back) {
					//alert(back);
					if(back=="username exist")
						$("#checkuser").text("*用户已存在！！");
					else if(back=="success"){
						window.location.href="index.jsp?username="+uname;
						//alert("1234214124");
						}					
				},
			error:function(){
				}
			});
		}
		else{
			if(!CheckUserName(uname))
				$("#checkuser").text("*用户名不合法！");
			if(!CheckPassWord(pwd))
				$("#checkpwd").text("*密码不合法！");
		}
		
		
		
	});
});



//$(document).ready(function(){
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
//            $.post("/user/add", $("#regform").serialize(), function(data){
//                $("#submit").siblings("#state").html(data);
//                if(data=="success")
//                {
//                	window.history.back(-2);
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
function CheckUserName(user){
    if (user.match("^[A-Za-z0-9_]{3,16}") != user) {
        return false;
    }
    return true;
}

function CheckPassWord(password){
    if (password.match("^[A-Za-z0-9_]{6,18}") != password) {
        return false;
    };
    return true;
}
