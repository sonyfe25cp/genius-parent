<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>Insert title here</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<link href="/css/genius-admin.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.tools {
	margin: 30px auto 3px auto;
	padding:10px 15px;
}
.tools span{
	font-weight: bold;
	font-size: larger;
}
</style>
<script type="text/javascript" src="/js/jquery-1.7.1.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $(".delete").click(function(){
    	var $this = $(this);
    	//alert("send: remove "+$this.closest("tr").find(".clusterId").html());
    	$.post("/admin/xnewscluster/remove",{
    		clusterId:$this.closest("tr").find(".clusterId").html()
        	}, function(data){
        	//alert(data);
        	location.reload();
       });
     });
    $("#createcluster").click(function(){
    	$("#createcluster").siblings("#state").html("wait for processing...");
    	//alert($("#createcluster").siblings("#clusternumber").find("#smallbox").val());
    	//alert($("#createcluster").siblings("#threadhold").find("#smallbox").val());
    	$.post("/admin/xnewscluster/resetcluster", {
    		sNumber:$("#createcluster").siblings("#clusternumber").find("#smallbox").val(),
    		sThreadhold:$("#createcluster").siblings("#threadhold").find("#smallbox").val()
    		}, function(data){
       	$("#createcluster").siblings("#state").html(data);
       	location.reload();
       });
     });  
});
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>聚类显示列表</h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th>ID</th>
						<th>cluster id</th>
						<th>center xnews id</th>
						<th>threadhold</th>
						<th>删除</th>
					</tr>
				</thead>
				<tbody>
					<c:set var="index" value="1" />
					<c:forEach var="xnewscluster" items="${list}">
						<tr>
							<td>${index}</td>
							<c:set var="index" value="${index+1}" />
							
							<td><a href="/admin/xnewscluster/showone?clusterId=${xnewscluster.clusterId}" target="_blank" class = "clusterId">${xnewscluster.clusterId}</a></td>
							<td><a href="/admin/xnews/showone?id=${xnewscluster.centerXnews.id}" target="_blank">${xnewscluster.centerXnews.title}</a></td>
							<td>${xnewscluster.threadhold}</td>
							<td>
								<a href="#" title=""><img src="/img/icons/icon_delete.png" alt="删除" class = "delete"/></a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div style="clear: both;"></div>
			<div class="tools">
				<div id = "clusternumber">
					<span >cluster number:</span>
					<input type="text" id="smallbox" class="inputbox smallbox" value="10">
				</div>
				<div id = "threadhold">
					<span >threadhold:</span>
					<input type="text" id="smallbox" class="inputbox smallbox" value="0.08">
				</div>
				<input type="submit" value="Reset Clusters" class="btn" id="createcluster">
				<span id="state"></span>
			</div>
		</div>
	</div>
</body>
</html>