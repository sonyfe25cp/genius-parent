<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>pdf_show</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2><c:out value="${title}"></c:out></h2>
		</div>
		</div>
		<div class="contentbox">
			<div align="center"><img alt="Logo" src="/images/logo_small.png" width="50%" align="middle">
			<br><br><br>
			<font size="6"><b>欢迎使用${title}</b></font><br><br>
			<font size="6"><b>${currentime}</b></font><br>
			</div>
			<div>
			<br><br>
			<font size="4">BIT Genius小组竭诚为您服务，如有任何意见或者建议请直接与我们联系！</font>
			<br><br>
			<table width="75%" border = 2 >
			<tr>
			<th width="50%">Email</th>
			<td>${email}</td>
			</tr>
			<tr>
			<th width="50%">联系地址</th>
			<td>${address}</td>
			</tr>
			<tr>
			<th width="50%">联系电话</th>
			<td>${telephone}</td>
			</tr>
			</table>
			</div>
			<h2><label class="spacer"><span class="red">1. 概要简述</span></label></h2>
			<label><strong>您现在选择的${startime}至${currentime}这一段时间内查询记录的统计情况，统计的形式会分为图表和文字向您呈现。</strong></label>
			<h2><label class="spacer"><span class="red">2. 柱状图显示</span></label></h2>
			<img alt="柱状图" src="${barimage}" width="720" height="480"><br><br>
			<h2><label class="spacer"><span class="red">3. 饼状图显示</span></label></h2>
			<img alt="柱状图" src="${pieimage}" width="720" height="480"><br><br>
			<h2><label class="spacer"><span class="red">4. 最热门搜索</span></label></h2>
			<p>该时间段内出现频率最高的项如下表所示:</p>
			<table width="75%" border = 2 >
			<tr>
			<th width="50%">关键词</th>
			<th>计数</th>
			</tr>
			<c:forEach var="querycountx" items="${querycount}">
<%-- 			<c:set var="string" items="${querycount.query}"></c:set>
			<c:set var="count" items="${querycount.count }"></c:set>
 --%>			<tr>
			<td width="50%"><c:out value="${querycountx.query}"></c:out></td>
			<td><c:out value="${querycountx.count}"></c:out></td>
			</tr>
			</c:forEach>
			</table>
					<br><br><a href="${path}"><font size="4" color="red"><b>点此下载PDF版报告</b></font></a>
		</div>
		<p/>
</body>
</html>