<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统统计报告</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>统计报告配置</h2>
		</div>
		</div>
			<div class="contentbox">
			<form action="optionpdf" method="get">
			<p>
			<label for="textfield"><strong>请输入报表表头</strong></label>
			<input type="text"  name="head" class="inputbox" value="Welcome To Genius" /></p>
			<p>
			<label for="textfield"><strong>请输入报表标题</strong></label>
			<input type="text"  name="title" class="inputbox"  value="Genius 统计报告"/></p>
				<p>
			<label for="textfield"><strong>请输入联系地址</strong></label>
			<input type="text"  name="address" class="inputbox" value="北京海淀区中关村南大街5号北京理工大学"/></p>
				<p>
			<label for="textfield"><strong>请输入Email地址</strong></label>
			<input type="text"  name="email" class="inputbox" value="genius2012@bit.edu" /></p>
				<p>
			<label for="textfield"><strong>请输入电话号码</strong></label>
			<input type="text"  name="telephone" class="inputbox smallbox" value="010-68912400" /></p>
			<table border="2" width="25%">
			<tr>
			<th>选择查看报表</th>
			</tr>
			<tr>
			<td><input type="radio" name="option" value ="0">日报表</td>
			</tr>
			<tr>
			<td><input type="radio" name="option" value="1" checked>周报表</td>
			</tr>
			<tr>
			<td><input type="radio" name="option" value="2">月报表</td>
			</tr>
			</table>
			<input type="submit" class="btn" value="提交">
			</form>
			<br><br><br><br>
			<div>
			<p class="spacer" ><font size="4"><b>您也可以通过日历来选择日期</b></font></p>
			<a href = "/admin/pdf/getcalendarpdf" target="mainFrame">点此进行设置</a>
			</div>
			</div>
</body>
</html>