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
<script type="text/javascript" src="/js/jquery-1.7.1.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(".delete")
								.click(
										function() {
											var $this = $(this);
											alert("send: remove "
													+ $this.closest("tr").find(
															".newsId").html());
											$
													.post(
															"/admin/xnews/remove",
															{
																newsId : $this
																		.closest(
																				"tr")
																		.find(
																				".newsId")
																		.html()
															},
															function(data) {
																//alert(data);
																location.href = "/admin/sensitive/list-one/${sterm}";
															});
										});
					});
</script>
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>敏感词"${sterm}"显示列表</h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th width="10%">序号</th>
						<th width="35%">标题</th>
						<th width="25%">站点</th>
						<th width="20%">抓取时间</th>
						<th width="10%">删除</th>
					</tr>
				</thead>
				<tbody>
					<!--<c:set var="index" value="1" />-->
					<c:set var="index" value="${(pageNo-1)*10+1}" />
					<c:forEach var="news" items="${list}">
						<tr>
							<%-- <td>${news.id.toStringMongod()}</td> --%>
							<td>${index}</td>
							<c:set var="index" value="${index+1}" />
							<!--  <td hidden="true"><a href="#" class="newsId">${news.id}</a>	</td> -->
							<td><a href="${news.url}" target="_blank">${news.title}</a>
							</td>
							<td><a href="http://${news.sourceHost}" target="_blank">${news.sourceHost}</a>
							</td>
							<td>${news.publishTime}</td>
							<td><a href="#" title=""><img
									src="/img/icons/icon_delete.png" alt="删除" class="delete" />
							</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="extrabottom">
				<ul>
					<li><img src="/img/icons/icon_delete.png" alt="Delete" /> 删除</li>
				</ul>
			</div>
			<ul class="pagination">
				<c:if test="${pageNo!=1}">
					<li class="text"><a
						href="/admin/sensitive/list-one/${term}?pageNo=${pageNo-1}">Previous</a>
					</li>
				</c:if>

				<c:forEach var="num" begin="${pb}" end="${pe}" step="1">
					<c:choose>
						<c:when test="${num==pageNo}">
							<li class="page"><a href="#" title="">${num}</a>
							</li>
						</c:when>
						<c:otherwise>
							<li><a
								href="/admin/sensitive/list-one/${sterm}?pageNo=${num}" title="">${num}</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${pageNo!=total}">
					<li class="text"><a
						href="/admin/sensitive/list-one/${sterm}?pageNo=${pageNo+1}"
						title="">Next</a>
					</li>
				</c:if>

			</ul>
			<div style="clear: both;"></div>
		</div>
	</div>
</body>
</html>