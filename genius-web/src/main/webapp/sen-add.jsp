<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
</head>
<body style="background: none">
	<%
		String[] isSelected = (String[]) request.getAttribute("isSelected");
		//  session.setAttribute("isSelected", isSelected);
	%>

	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>添加敏感词</h2>
		</div>
		<div class="contentbox">
			<form action="/admin/sensitive/update" method="post" enctype="multipart/form-data">
				<p>
					<label for="textfield"><strong>敏感词输入（不同词以‘空格’或‘换行’分隔;同一词的不同表示以‘|’分隔）</strong> </label>
					<textarea class="inputbox" id="textfield-xml" name="sterm" rows="9"
						cols="120"></textarea>
				</p>
				<br/>
				<p>
				<strong>敏感词文件上传（不同词以‘空格’或‘换行’分隔;同一词的不同表示以‘|’分隔；大小&lt;20M）</strong> <br/>
					<input type="file" name="file" id="uploader" />
				</p>
				
				<input type="submit" value="提交" class="btn" />
			</form>
		</div>
	</div>
</body>
</html>