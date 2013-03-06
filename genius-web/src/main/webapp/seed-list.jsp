<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="bit.mirror.data.Seed"%>
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
			<h2>数据采集器设置列表</h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th width="15%">序号</th>
						<th width="25%">种子名称</th>
						<th width="20%">健康状态</th>
						<th width="20%">激活状态</th>
						<th width="20%">操作</th>

						</th>
					</tr>
				</thead>
				<tbody>
					<%
						int i = 0;
					%>
					<c:set var="index" value="${(pageNo-1)*10+1}" />

					<c:forEach var="seed" items="${seeds}">
						<tr>
							<td>${index}</td>
							<c:set var="index" value="${index+1 }" />

							<td><%=((ArrayList<Seed>) request.getAttribute("seeds"))
						.get(i).getName().replace(">", "&gt;")
						.replace("<", "&lt;")%></td>
							<%
								i++;
							%>
							<td><c:if test="${seed.healthy == 0 }">无异常</c:if> <c:if
									test="${seed.healthy == 1 }">帐号存在异常</c:if></td>
							<td><c:if test="${seed.enabled == true }">激活</c:if> <c:if
									test="${seed.enabled == false }">关闭</c:if></td>
							<td><a href="/admin/seed/${seed.name}" title="编辑"><img
									src="/img/icons/icon_edit.png" alt="编辑" /> </a> <c:if
									test="${seed.enabled == true }">
									<a href="/admin/seed/set-disabled/${seed.name}" title="禁用"><img
										src="/img/icons/icon_unapprove.png" alt="禁用" /> </a>
								</c:if> <c:if test="${seed.enabled == false }">
									<a href="/admin/seed/set-enabled/${seed.name}" title="启用"><img
										src="/img/icons/icon_approve.png" alt="启用" /> </a>
								</c:if> <a href="/admin/seed/delete/${seed.name}" title="删除"><img
									src="/img/icons/icon_delete.png" alt="删除" /> </a> <a
								href="/admin/extract/slist/${seed.name}" title="添加规则"><img
									src="/img/icons/icon_addrule.png" alt="添加规则" /> </a>
							</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="extrabottom">
				<ul>
					<li><img src="/img/icons/icon_edit.png" alt="Edit" /> 编辑</li>
					<li><img src="/img/icons/icon_approve.png" alt="Approve" />
						启用</li>
					<li><img src="/img/icons/icon_unapprove.png" alt="Unapprove" />
						停用</li>
					<li><img src="/img/icons/icon_delete.png" alt="Delete" /> 删除</li>
					<li><img src="/img/icons/icon_addrule.png" alt="Add" /> 添加规则</li>
				</ul>
			</div>
			<input type="button" onclick="location.href='/admin/seed/add'"
				value="添加新种子" />
			<ul class="pagination">
				<c:if test="${pageNo != 1 }">
					<li class="text"><a href="/admin/seed/list/${pageNo-1 }">Previous</a>
					</li>
				</c:if>
				<c:forEach var="num" begin="1" end="${total }" step="1">
					<c:if test="${num == pageNo }">
						<li class="page"><a href="#" title="">${num}</a>
						</li>
					</c:if>
					<c:if test="${num != pageNo }">
						<li><a href="/admin/seed/list/${num }" title="">${num }</a>
						</li>
					</c:if>
				</c:forEach>
				<c:if test="${pageNo != total }">
					<li class="text"><a href="/admin/seed/list/${pageNo+1 }">Next</a>
					</li>
				</c:if>
			</ul>
			<div style="clear: both;"></div>
		</div>
	</div>


</body>
</html>