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

<!--[if lt IE 9]><script type="text/javascript" src="excanvas.js"></script><![endif]-->
<script src="/js/tagcanvas.js" type="text/javascript"></script>
<script type="text/javascript">
	window.onload = function() {
		try {
			TagCanvas.Start('myCanvas', 'tags', {
				textColour : '#ff0000',
				outlineColour : '#ff00ff',
				reverse : true,
				depth : 0.8,
				maxSpeed : 0.05
			});
		} catch (e) {
			// something went wrong, hide the canvas container
			document.getElementById('myCanvasContainer').style.display = 'none';
		}
	};
</script>
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>近期热点词显示列表</h2>
		</div>
		<div class="contentbox">

			<table id="myCanvasContainer" width="100% ">
				<tr>
					<td width="65% "><table align="left" cellspacing="0"
							cellpadding="0" width="100%">
							<tr>
								<th>序号</th>
								<th>热度词</th>
								<th>次数</th>
							</tr>
							<c:set var="i" value="${(pageNo-1)*15}" />

							<c:forEach var="t" items="${terms}">
								<tr>
									<c:set var="i" value="${i+1}" />
									<td>${i}.</td>
									<td><a href="/admin/hot/list-one/${t.term}">${t.term}</a>
									</td>
									<td>${t.count}</td>
								</tr>
							</c:forEach>
						</table>
					</td>

					<td width="35% ">
						<table cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td><canvas width="400" height="400" id="myCanvas">
									</canvas>
								</td>
							</tr>
						</table></td>
				</tr>
			</table>


			<div id="tags">
				<ul>
					<c:forEach var="t" items="${terms}">
						<li><a href="/admin/hot/list-one/${t.term}">${t.term}</a>
						</li>
					</c:forEach>
				</ul>
			</div>
			<ul class="pagination">
				<c:if test="${pageNo != 1 }">
					<li class="text"><a href="/admin/hot/list?pageNo=${pageNo-1 }">Previous</a>
					</li>
				</c:if>
				<c:forEach var="num" begin="${pb}" end="${pe}" step="1">
					<c:if test="${num == pageNo }">
						<li class="page"><a href="#" title="">${num}</a></li>
					</c:if>
					<c:if test="${num != pageNo }">
						<li><a href="/admin/hot/list?pageNo=${num }" title="">${num
								}</a></li>
					</c:if>
				</c:forEach>
				<c:if test="${(pageNo != totalP)&&totalP!=0 }">
					<!-- syl -->
					<li class="text"><a href="/admin/hot/list?pageNo=${pageNo+1 }">Next</a>
					</li>
				</c:if>
			</ul>
		</div>
	</div>

</body>
</html>