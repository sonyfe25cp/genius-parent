<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>新闻${xnews.id}信息</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<link href="/css/genius-admin.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery-1.7.1.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.name{
	font-size: 1.2em;
	font-weight: bold;
}
div.contentbox div{
	margin:10px auto;
	padding:10px 15px;
	border:thin;
	border-style: dashed;
}
.tools {
	margin: 3px auto;
	padding:10px 15px;
}
.tools span{
	font-weight: bold;
	font-size: larger;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
    $("#createcluster").click(function(){
    	$("#createcluster").siblings("#state").html("wait for processing...");
    	//alert($("#createcluster").siblings("#threadhold").find("#smallbox").val());
    	$.post("/admin/xnews/addcluster", {id:$("#id").children(".value").html(),
    	threadhold:$("#createcluster").siblings("#threadhold").find("#smallbox").val()}, function(data){
       	$("#createcluster").siblings("#state").html(data);
       });
     });    
});
</script>
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h1>新闻:${xnews.id}</h1>
		</div>
		<div class="tools">
			<div id = "threadhold">
				<span >threadhold:</span>
				<input type="text" id="smallbox" class="inputbox smallbox" value="0.08">
			</div>
			<input type="submit" value="Set As A New Cluster Center" class="btn" id="createcluster">
			<span id="state"></span>
		</div>
		<div class="contentbox">
			<div id="title">
				<span class="name">title:</span>
				<span class="value">${xnews.title}</span>
			</div>
			<div id="id">
				<span class="name">id:</span>
				<span class="value">${xnews.id}</span>
			</div>
			<div id="url">
				<span class="name">url:</span>
				<span class="value">${xnews.url}</span>
			</div>
			<div id="author">
				<span class="name">author:</span>
				<span class="value">${xnews.author}</span>
			</div>
			<div id="date">
				<span class="name">date:</span>
				<span class="value">${xnews.publishTime}</span>
			</div>
			<div id="content">
				<span class="name">content:</span>
				<span class="value">${xnews.content}</span>
			</div>
			<div id="comments">
				<span class="name">comments:</span>
				<span class="value">
					<c:forEach var="comment" items="${xnews.comments}">
						<br/>${comment}
					</c:forEach>
				</span>
			</div>
			<div id="keywords">
				<span class="name">keywords:</span>
				<span class="value">
					<c:forEach var="keyword" items="${userinf.keywords}">
						${keyword}
					</c:forEach>
				</span>
			</div>
		</div>
	</div>
</body>
</html>