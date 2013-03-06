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
<script type="text/javascript" src="/js/jquery-1.7.1.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $(".delete").click(function(){
    	var $this = $(this);
    	//alert("send: remove "+$this.closest("tr").find(".newsId").html());
    	$.post("/admin/xnews/remove",{
    		newsId:$this.closest("tr").find(".newsId").html()
        	}, function(data){
        	//alert(data);
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
			<h2>资源显示列表</h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th>序号</th>
						<!-- <th>新闻id</th> -->
						<th>标题</th>
						<th>站点</th>
						<th>抓取时间</th>
						<th>删除</th>
					</tr>
				</thead>
				<tbody>
					<c:set var="index" value="1" />
					<c:forEach var="xnews" items="${list}">
						<tr>
							<%-- <td>${news.id.toStringMongod()}</td> --%>
							<td>${index}</td>
							<c:set var="index" value="${index+1}" />
							<!--  <td hidden="true"><a href="#" class="newsId">${xnews.id}</a></td> -->
							<td><a href="/admin/xnews/showone?id=${xnews.id}" target="_blank">${xnews.title}</a></td>
							<td><a href="http://${xnews.sourceHost}" target="_blank">${xnews.sourceHost}</a></td>
							<td>${xnews.publishTime}</td>
							<td>
								<a href="#" title=""><img src="/img/icons/icon_delete.png" alt="删除" class="delete"/></a>
							</td>
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<ul class="pagination">
				<li class="text">
					<c:if test="${pageNo!=1}">
						<a href="/admin/xnews/show?pageNo=${pageNo-1}&pageSize=${ps}">Previous</a>
					</c:if>
				</li>
				
				<c:forEach   var="num"   begin="${pb}"   end="${pe}"   step="1">   
					<c:choose>
						<c:when test="${num==pageNo}">
							<li class="page"><a href="#" title="">${num}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/admin/xnews/show?pageNo=${num}&pageSize=${ps}" title="">${num}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<li class="text">
					<c:if test="${pageNo!=totalP}">
						<a href="/admin/xnews/show?pageNo=${pageNo+1}&pageSize=${ps}" title="">Next</a>
					</c:if>
				</li>
			</ul>
			<div style="clear: both;"></div>
		</div>
	</div>
</body>
</html>