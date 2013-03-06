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
			<table>
				<tr>
					<td width="50%">
						<h2 align="left">有关'${hotsearch }'的热点词显示列表</h2>
					</td>
					<td width="50%">
						<form action="/admin/hot/search" method="get">
							<input name="hotsearch" class="inputbox-1" type="text" /><input
								type="submit" class="btn" value="搜索" />
						</form>
					</td>
				</tr>
			</table>
		</div>
		<div class="contentbox">
			<form action="/admin/hot/ignore" method="get">
				<table align="left" cellspacing="0" cellpadding="0" width="100%">
					<tr>
						<th width="15%">序号</th>
						<th width="50%">热度词</th>
						<th width="20%">次数</th>
						<th width="15%"><input name="" type="checkbox" value=""
							id="checkboxall" /></th>
					</tr>
					<c:set var="i" value="${(pageNo-1)*15}" />

					<c:forEach var="t" items="${terms}">
						<tr>
							<c:set var="i" value="${i+1}" />
							<td>${i}</td>
							<td><a href="/admin/hot/list-one/${t.term}">${t.term}</a>
							</td>
							<td>${t.count}</td>
							<td><input type="checkbox" value=“${t.term}“ name="checkbox"
								id="Item${i}" />
							</td>
						</tr>
					</c:forEach>
				</table>
				<div class="extrabottom">
					<input type="button"
						onclick="location.href='/admin/hot/ignore_show'" value="查看已屏蔽" />
					<input type="submit" value="屏蔽" />
				</div>
			</form>

			<ul class="pagination">
				<c:if test="${pageNo != 1 }">
					<li class="text"><a href="/admin/hot/search?pageNo=${pageNo-1 }&hotsearch=${hotsearch}">Previous</a>
					</li>
				</c:if>
				<c:forEach var="num" begin="${pb}" end="${pe}" step="1">
					<c:if test="${num == pageNo }">
						<li class="page"><a href="#" title="">${num}</a></li>
					</c:if>
					<c:if test="${num != pageNo }">
						<li><a href="/admin/hot/search?pageNo=${num }&hotsearch=${hotsearch}" title="">${num
								}</a></li>
					</c:if>
				</c:forEach>
				<c:if test="${(pageNo != totalP)&&totalP!=0 }">
					<!-- syl -->
					<li class="text"><a href="/admin/hot/search?pageNo=${pageNo+1 }&hotsearch=${hotsearch}">Next</a>
					</li>
				</c:if>
			</ul>
		</div>
	</div>

</body>
</html>