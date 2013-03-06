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

	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>添加/修改解析配置${error}</h2>
		</div>
		<div class="contentbox">
			<c:if test="${noSeed== true&&empty sname}">
				<h3>
					<a href="/admin/seed/add"><strong>请先添加种子</strong> </a>
				</h3>
			</c:if>
			<c:if test="${noSeed== false||!empty sname}">
				<form action="/admin/extract/update" method="get">
					<p>
						<label for="textfield"><strong>对应种子名称</strong> </label> <select
							name="seed">
							<c:forEach var="s" items="${seeds}">
								<c:if test="${sname==s.name}">
									<option value="${s.name}" selected="selected">${s.name}</option>
								</c:if>
								<c:if test="${sname!=s.name}">
									<option value="${s.name}">${s.name}</option>
								</c:if>
							</c:forEach>
						</select>
					</p>
					<p>
						<label for="textfield"><strong>抽取配置名称</strong> </label> <input
							type="text" id="textfield" class="inputbox" name="name"
							value="${extract_cfgs.name}" />
					</p>
					<p>
						<label for="smallbox"><strong>抽取目标类型:</strong> </label>
						<c:set var="news" value="news" />
						<c:set var="forum" value="forum" />
						<c:if test="${extract_cfgs.type == 'NEWS'}">
							<input type="radio" id="radio-type" name="type" value="NEWS"
								checked=checked>新闻
							<input type="radio" id="radio-type" name="type" value="FORUM">论坛
						</c:if>
						<c:if test="${extract_cfgs.type == 'FORUM'}">
							<input type="radio" id="radio-type" name="type" value="NEWS">新闻
							<input type="radio" id="radio-type" name="type" value="FORUM"
								checked=checked>论坛
						</c:if>
						<c:if test="${empty extract_cfgs.type}">
							<input type="radio" id="radio-type" name="type" value="NEWS">新闻
							<input type="radio" id="radio-type" name="type" value="FORUM">论坛
						</c:if>
					</p>
					<p>
						<label for="textfield"><strong>对应uri正则</strong> </label> <input
							type="text" id="textfield" class="inputbox" name="uriRegx"
							value="${extract_cfgs.uriRegx}" />
					</p>
					<p>
						<c:if test="${extract_cfgs.enabled==true}">
							<br />
							<input type="checkbox" name="enabled" checked="checked" />
					启用
				</c:if>
						<c:if test="${extract_cfgs.enabled==false}">
							<br />
							<input type="checkbox" name="enabled" />
					启用
				</c:if>

					</p>
					<p>
						<label for="textfield"><strong>抽取配置XML</strong> </label>
						<textarea class="inputbox" id="textfield-xml" name="xml" rows="5"
							cols="75">${extract_cfgs.xml}</textarea>
					</p>

					<input type="submit" value="提交" class="btn" />
				</form>
			</c:if>
		</div>
	</div>
</body>

</html>