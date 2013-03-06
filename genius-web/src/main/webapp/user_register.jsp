<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>dlde register</title>
<script type="text/javascript" src="/js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="/js/userregister.js"></script>
<style type="text/css">
html,body,h1,div,span{
	margin: 0;	
	border: 0;
	padding: 0;
}
label{
	font-weight: bold;
	margin: 0 0 .5em;
	display: block;
}
input#username ,input#password {
	width: 100%;
	height: 32px;
	font-size: 15px;
}
input#submit{
	margin: 0 1.5em 1.2em 0;
	height: 32px;
	font-size: 13px;
	font-weight : bold;
	background: blue;
	color: white;
	border: 1px;
	border-color: navy;
}
#content label,#content input,#content span {
	display:block;
}
#state{
	color: red;
}
#prompt{
	color: #6F6F6F;
}
#wapper{
	width: 100%;
	height: auto;
}
#head{
	width: 100%;
	height: auto;
	background-color: #E5E5E5;
}
#head h1{
 color:#333;
 font-size: 4em;
}
#content{
	
}
#box{
	margin: 12px 0 0;
	padding: 20px 25px 15px;
	width: 500px;
	background: #F1F1F1;
	border: 1px solid #E5E5E5;
	margin: 160px auto;
}
#dun,#dpw{
	height: 100px;
	margin: 10px auto;
}

#checkuser,#checkpwd{
    color:red;
    
    padding-left:5px;

}

</style>
</head>
<body>
<div id = "wapper">
<div id = "head"><h1>DLDE</h1></div>
<div id = "content">
	<div id = info></div>
	<div id = "box">
		<form id="regform">
		
		<div id="dun">
			   <table>
			     <tr>
				    <td width="100px"><label>username: </label></td> 
				    <td align="left" ><input id="username" type="text" name="username" align="bottom"  /></td>
				    <td><span id="checkuser"></span></td>
				 </tr>
				 <tr>
				    <td colspan="3"><span id="prompt">由3~16位字母、数字和下划线组成</span></td>
				 </tr>
				 
				</table>
			</div>
			<div id="dpw">
			 <table>
			    <tr>
				   <td width="100px"><label>password: </label></td> 
				   <td align="left" ><input id="password" type="text" name="password" align="bottom"  /></td> 
				   <td><span id="checkpwd"></span></td>
			    </tr>
			    <tr>
				   <td colspan="3"><span id="prompt">由6~18位字母、数字和下划线组成</span></td> 
			    </tr>
			   
			 </table>
			</div>
			<div id="dsm">
			    <input id="submit" type="button" value="register" />
			    <span id="state"></span>
			</div>
		
		<!--  
			<div id="dun">
				<label>username: </label> 
				<input id="username" type="text" name="username" /> 
				<span id="prompt">由3~16位字母、数字和下划线组成</span> 
				<span id="state"></span>
			</div>
			<div id="dpw">
				<label>password:</label>
				<input id="password" type="password" name="password" /> 
				<span id="prompt">由6~18位字母、数字和下划线组成</span> 
				<span id="state"></span>
			</div>
			<div id="dsm"> 
				<input id="submit" type="button" value="register" />
				<span id="state"></span>
			</div>
			-->
		</form>
</div>
</div>
</div>
</body>
</html>