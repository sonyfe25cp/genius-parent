<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title>Genius --From BIT DLDE</title>
<link href="css/index.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script language="javascript" type="text/javascript"
	src="/js/atuocomplete.js"></script>
</head>

<body onload="autocomplete()">

 <a href='user_login.jsp' id=login class="login">登陆/注册</a>
 <div id="userinfo">
   <p>
      <span id="WelcomeUser" class="username"></span>&nbsp &nbsp 
      <a href="#" id="logout">退出</a>
   </p>
   </div>
 <%
    String uname=request.getParameter("username"); 
 %>
 <input type="hidden" id="uname" value="<%=uname%>"/>
 <input type="hidden" id="test" value="${CurrentUser}"/>
 
 <div id="search_body">

	<table align="center">
		<tr>
			<td>
				<div id="logo"></div>
			</td>
		</tr>
		<tr>
			<td><div id="search">
					<form action="g" method="get">
						<input id="query" type="text" name="q" class="keyword"
							autocomplete="off" /> <input type="submit" value="bingo"
							class="btn" />
					</form>
				</div></td>
		</tr>
		<tr>
			<td><div id="ajaxresult"></div>
			</td>
		</tr>
	</table>
	<div id = "footer"> 
		<p>
		<span><a href="#">关于我们</a></span> |
		<span><a href="#">联系我们</a></span> |
		<span><a href="/xnews/list">新闻推荐</a></span> |
		<span><a href="/loginasadmin">后台登录</a></span> 
		</p>
	</div>
</div>
</body>
</html>