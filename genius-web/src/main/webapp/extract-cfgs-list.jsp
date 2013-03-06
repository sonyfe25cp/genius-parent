<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.genius.model.ExtractConfiguration"%>
<%@ page import="java.util.ArrayList"%>
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
			<h2>信息解析配置列表</h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th width="10%">序号</th>
						<th width="30%">种子名称</th>
						<th width="30%">抽取模板名称</th>
						<th width="10%">当前状态</th>
						<th width="20%">操作</th>
					</tr>
				</thead>
				<tbody>
					<%
						int i = 0;
					%>
					<c:set var="index" value="${(pageNo-1)*10+1}"/>
					<c:forEach var="extract_cfg" items="${extract_cfgs}">
						<tr>
							<td>${index}</td>
							<c:set var="index" value="${index+1 }"/>
							<td>
							<%=((ArrayList<ExtractConfiguration>) request
						.getAttribute("extract_cfgs")).get(i).getSeed()
						.replace(">", "&gt;").replace("<", "&lt;")%>
							</td>
							<td>
							<%=((ArrayList<ExtractConfiguration>) request
						.getAttribute("extract_cfgs")).get(i).getName()
						.replace(">", "&gt;").replace("<", "&lt;")%>
							</td>
							<%
								i++;
							%>
							<td><c:if test="${extract_cfg.enabled == true }">激活</c:if> <c:if
									test="${extract_cfg.enabled == false }">关闭</c:if>
							</td>
							<td><a href="/admin/extract/${extract_cfg.id}" title="编辑"><img
									src="/img/icons/icon_edit.png" alt="编辑" /> </a> <c:if
									test="${extract_cfg.enabled == true }">
									<a href="/admin/extract/set-disabled/${extract_cfg.id}"
										title="禁用"><img src="/img/icons/icon_unapprove.png"
										alt="禁用" /> </a>
								</c:if> <c:if test="${extract_cfg.enabled == false }">
									<a href="/admin/extract/set-enabled/${extract_cfg.id}"
										title="启用"><img src="/img/icons/icon_approve.png" alt="启用" />
									</a>
								</c:if> <a href="/admin/extract/delete/${extract_cfg.id}" title="删除"><img
									src="/img/icons/icon_delete.png" alt="删除" /> </a>
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
				</ul>
			</div>
			<c:if test="${!empty sname}">
				<input type="button"
					onclick="location.href='/admin/extract/add/${sname}'"
					value="添加新抽取配置" />
			</c:if>
			<c:if test="${empty sname}">
				<input type="button" onclick="location.href='/admin/extract/add'"
					value="添加新抽取配置" />
			</c:if>
			<ul class="pagination">
				<c:if test="${pageNo != 1 }">
					<li class="text"><a
						href="/admin/extract/list?pageNo=${pageNo-1 }">Previous</a>
					</li>
				</c:if>
				<c:forEach var="num" begin="1" end="${total }" step="1">
					<c:if test="${num == pageNo }">
						<li class="page"><a href="#" title="">${num}</a></li>
					</c:if>
					<c:if test="${num != pageNo }">
						<li><a href="/admin/extract/list?pageNo=${num }" title="">${num
								}</a></li>
					</c:if>
				</c:forEach>
				<c:if test="${pageNo != total }">
					<li class="text"><a
						href="/admin/extract/list?pageNo=${pageNo+1 }">Next</a>
					</li>
				</c:if>
			</ul>
			<div style="clear: both;"></div>
		</div>
	</div>


</body>
</html>