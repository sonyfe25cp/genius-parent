<%@ page language="java" contentType="textml;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员 -- 登录页面</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/login.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<!-- Theme End -->

</head>
<body>
	<div id="logincontainer">
    	<div id="loginbox">
        	<div id="loginheader">
            	<img src="/themes/blue/img/cp_logo_login.png" alt="Control Panel Login" />
            </div>
            <div id="innerlogin">
            	<form action="/verifyadmin" method="post">
                	<p>用户名:</p>
                	<input type="text" class="logininput" name="username" autocomplete="off"/>
                    <p>密码:</p>
                	<input type="password" class="logininput" name="password" />
                   	<input type="submit" class="loginbtn" value="提交" /><br />
<!--                     <p><a href="#" title="Forgoteen Password?">Forgotten Password?</a></p> -->
                </form>
            </div>
        </div>
        <img src="/img/login_fade.png" alt="Fade" />
    </div>
</body>
</html>