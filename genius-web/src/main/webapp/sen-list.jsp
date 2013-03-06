<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.genius.model.SensitiveTerm"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<script src="/js/jquery-1.1.1.js" type="text/javascript"></script>
<script src="/scripts/jquery.min.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
	$(document)
			.ready(
					function() {
						//全选或全不选
						$("#checkboxall").click(function() {//当点击全选框时
							var flag = $("#checkboxall").attr("checked");//判断全选按钮的状态
							$("[id^='Item']").each(function() {//查找每一个Id以Item结尾的checkbox
								$(this).attr("checked", flag);//选中或者取消选中
							});
						});
						//如果全部选中勾上全选框，全部选中状态时取消了其中一个则取消全选框的选中状态
						$("[id^='Item']")
								.each(
										function() {
											$(this)
													.click(
															function() {
																if ($("[id^='Item']:checked").length == $("[id^='Item']").length) {
																	$(
																			"#checkboxall")
																			.attr(
																					"checked",
																					"checked");
																} else
																	$(
																			"#checkboxall")
																			.removeAttr(
																					"checked");
															});

										});
					});
</script>

</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>敏感词列表</h2>
		</div>
		<div class="contentbox">
			<form action="/admin/sensitive/delete?pageNo=${pageNo }" method="get">
				<table width="100%">
					<thead>
						<tr>
							<th width="10%">序号</th>
							<th width="40%">敏感词名称</th>
							<th width="20%">总共出现</th>
							<th width="20%">上一次出现</th>
							<th width="10%"><input name="" type="checkbox" value="" id="checkboxall" />
							</th>
						</tr>
					</thead>
					<tbody>
						<%
							int i = 0;
						%>
						<c:set var="index" value="${(pageNo-1)*10+1}" />
						<c:forEach var="t" items="${sterm}">
							<tr>
								<td>${index}</td>
								<c:set var="index" value="${index+1 }" />
								<td><a href="/admin/sensitive/list-one?id=${t.id}"><%=((List<SensitiveTerm>) request.getAttribute("sterm"))
						.get(i).getHtmlFitableTerm()%></a>
								</td>

								<td>${t.ttlCount}</td>

								<td>${t.prevCount}</td>
								<%
									i++;
								%>
								<td><input type="checkbox" value=“${t.id}“ name="checkbox"
									id="Item<%=i%>" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="extrabottom">
					<input type="button" onclick="location.href='/admin/sensitive/add'"
						value="添加" /> <input type="submit" value="删除" />
				</div>
			</form>
			<ul class="pagination">
				<c:if test="${pageNo != 1 }">
					<li class="text"><a
						href="/admin/sensitive/list?pageNo=${pageNo-1 }">Previous</a></li>
				</c:if>
				<c:forEach var="num" begin="1" end="${totalP }" step="1">
					<c:if test="${num == pageNo }">
						<li class="page"><a href="#" title="">${num}</a></li>
					</c:if>
					<c:if test="${num != pageNo }">
						<li><a href="/admin/sensitive/list?pageNo=${num }" title="">${num
								}</a></li>
					</c:if>
				</c:forEach>
				<c:if test="${pageNo != totalP }">
					<li class="text"><a
						href="/admin/sensitive/list?pageNo=${pageNo+1 }">Next</a></li>
				</c:if>
			</ul>
			<div style="clear: both;"></div>

		</div>
	</div>


</body>
</html>