function autocomplete(){
	var highlightindex=-1;
	$("#userinfo").hide();
	
	if($("#uname").val()=="null"||$("#uname").val()==""){
//		 if(getCookie("username")!=null&&getCookie("password")!=null){
//		    	
//		    	//alert("1234");
//		    	var uname=getCookie("username");
//		    	var pwd=getCookie("password");
//		    	
//		    	$.ajax({
//					type : "GET",
//					url : "/user/verify",
//					data :  {username:uname,password:pwd},
//					success : function(back) {
//						//alert(back);
//						if(back=="success"){
//							 $("#login").hide();
//							 $("#userinfo").show();
//						     $("#WelcomeUser").text(getCookie("username"));
//						}
//						else {
//							$("#WelcomeUser").hide();
//							$("#userinfo").hide();
//						}
//							
//						
//					},
//				error:function(){
//					}
//				});
//		    	
//		    	//alert(uname+" "+pwd);
//		       
//		    }
		
	}
	 else{
		 //alert($("#uname").val().equals("null"));
		 $("#login").hide();
		 $("#userinfo").show();
	     $("#WelcomeUser").text($("#uname").val());
	 }

		
	$(document).ready(
					function() {

						$("#logout").click(function() {

							$.ajax({
								type : "GET",
								url : "/user/logout",
								// data : "q="+$('#query').val(),
								success : function(back) {
									// alert("111");
									if (back == "success")
										window.location.href = "index.jsp";
								},

								error : function() {
								}
							});
						});
				
//				$("#setcookie").click(function(){
//					alert("111");
//					SetCookie(uname, shiyulong);
//					
//				});
				
				// 隐藏自动补全框
				$("#ajaxresult").hide();
				
				// 给查询框添加blur事件，隐藏提示层
				$("#query").blur(function() {
					$("#ajaxresult").hide();
					highlightindex = -1;
				});

				// 输入框的id为search，这里监听输入框的keyup事件
				$('#query').keyup(		
						function() {
							//alert($('#query').val()+"11");
							if (event.keyCode != 13 && event.keyCode != 37&& event.keyCode != 38 && event.keyCode != 39&& event.keyCode != 40) { 
								highlightindex = -1;
								$.ajax({
								type : "GET",
								url : "/helper/autoQ",
								data :  "q="+$('#query').val(),
								success : function(back) { // 成功后执行的方法
									if (back != "") {
										$("#ajaxresult").show();
										var layer;
										layer = "<table><tbody  id='r'>"; // 创建一个table
										for ( var i = 0; i < back.length; i++) {
											layer += "<tr id='"+i+"'><td class='line'>"
													+ back[i] + "</td></tr>";
										}
										layer += "</tbody></table>";
										$('#ajaxresult').empty(); // 先清空#ajaxresult下的所有子元素
										$('#ajaxresult').append(layer);// 将刚才创建的table插入到#ajaxresult内

										$('.line').mouseover(function() {
											$(this).addClass("hover");
											highlightindex = $(this).attr("id");
											// $(this).css({background: "#cef"});
										});

										// 添加光标移出事件,取消高亮
										$('.line').mouseout(function() {
											$(this).removeClass("hover");
											highlightindex = -1;
										});

										// 添加光标mousedown事件
										// 点击事件newDivNode.click可能不起作用?
										$('.line').mousedown(function() {
											var selected = $(this).text();
											$('#query').val(selected);
											highlightindex = -1;		
										});
									} else {
										$('#ajaxresult').empty();
										$('#ajaxresult').hide();
									}
								},
								error : function() {
								}
							});
						}else if (event.keyCode == 38) {//输入向上,选中文字高亮
				            var autoNodes = $("tbody#r").children("tr");
				            if (highlightindex == -1) {
				                highlightindex = 1;
				            }else{
				            autoNodes.eq(highlightindex).removeClass("hover");}
				            highlightindex--;
				            highlightindex = (highlightindex+autoNodes.length)%autoNodes.length
			                autoNodes.eq(highlightindex).addClass("hover");
				        }
				        else if (event.keyCode == 40) {//输入向下,选中文字高亮
				        	 var autoNodes = $("tbody#r").children("tr");
					            if (highlightindex != -1) {
					            autoNodes.eq(highlightindex).removeClass("hover");}
					            highlightindex++;
					            highlightindex = (highlightindex+autoNodes.length)%autoNodes.length
				                autoNodes.eq(highlightindex).addClass("hover");

				        }
				        else if (event.keyCode == 13) {//输入回车
				            if (highlightindex != -1) {
				                var comText = $("tbody#r").hide().children("tr").eq(highlightindex).text();
				                highlightindex = -1;
				                $("#query").val(comText);
				                return false;
				            }
				      
				        }
							});
			});
}

function SetCookie(name, value)//两个参数，一个是cookie的名子，一个是值  

{

	var Days = 30; //此 cookie 将被保存 30 天  

	var exp = new Date(); //new Date("December 31, 9998");  

	exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);

	document.cookie = name + "=" + escape(value) + ";expires="
			+ exp.toGMTString();

}

function getCookoieName(){}

function getCookie(objName) {// 获取指定名称的cookie的值

	var arrStr = document.cookie.split("; ");
	if (arrStr.length < 1)
		return null;
	else {
		for ( var i = 0; i < arrStr.length; i++) {

			var temp = arrStr[i].split("=");

			if (temp[0] == objName)
				return unescape(temp[1]);

		}

	}
	return null;

}