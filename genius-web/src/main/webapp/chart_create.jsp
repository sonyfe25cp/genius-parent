<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>chart_create</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>热度统计</h2>
		</div>
		</div>
		<div class="contentbox">
		<p>
		<label for="textfield" class="spacer"><font size="3"><b>查看日报表统计</b></font></label>
		<table border=2 >
			<tr>
			<th>查询热度</th>
			<td><a href="/admin/chart/daybarchart" target="mainFrame">查看柱状图</a></td>
			<td><a href="/admin/chart/daypiechart" target="mainFrame">查看饼状图</a></td>
		</tr>
		
		<tr>	
			<th>统计热度</th>
			<td><a href="/admin/chart/daybarchart4hot" target="mainFrame">查看柱状图</a></td>
			<td><a href="/admin/chart/daypiechart4hot" target="mainFrame">查看饼状图</a></td>
		</tr>
		</table>
		<br/>
		</p>
		<p><label for="textfield" class="spacer"><font size="3"><b>查看周报表统计</b></font></label>
		<table border=2>
		<tr>
			<th>查询热度</th>
			<td><a href="/admin/chart/weekbarchart" target="mainFrame">查看柱状图</a></td>
			<td><a href="/admin/chart/weekpiechart" target="mainFrame">查看饼状图</a></td>
		</tr>
		<tr>	 
 			<th>统计热度</th> 
			<td><a href="/admin/chart/weekbarchart4hot" target="mainFrame">查看柱状图</a></td> 
			<td><a href="/admin/chart/weekpiechart4hot" target="mainFrame">查看饼状图</a></td> 
 		</tr>
		</table>
		<br/>
		</p>
			<p><label for="textfield" class="spacer"><font size="3"><b>查看月报表统计</b></font></label>
		<table border=2>
		<tr>
		<th>查询热度</th>
			<td><a href="/admin/chart/monthbarchart" target="mainFrame">查看柱状图</a></td>
			<td><a href="/admin/chart/monthpiechart" target="mainFrame">查看饼状图</a></td>
		</tr>
		<tr>
			<th>统计热度</th> 
			<td><a href="/admin/chart/monthbarchart4hot" target="mainFrame">查看柱状图</a></td> 
			<td><a href="/admin/chart/monthpiechart4hot" target="mainFrame">查看饼状图</a></td>
		</tr> 
		</table>
		</p>
		</div>
</body>
</html>
