<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<script language="javascript" type="text/javascript" src="/js/atuocomplete.js"></script>
	
<script language= "JavaScript">
//	function getStrAndToPage(currentCategory,currentPageNo,wantedPageNo,operation){
//	var str="";
//	//alert("currentCategory="+currentCategory+"|currentPageNo="+
//	//	currentPageNo+"|wantedPageNo="+wantedPageNo);
//	for(var i=0;i<document.form1.length;i++)
//		if(document.form1.elements[i].type=="checkbox")
//		{
//			if(form1.elements[i].checked)
//				str=str+"1";
//			else str=str+"0";
//			if(i!=(document.form1.length-1))
//				str=str+"/";
//		}
//	//alert("stateChangeStr built completed"+currentPageNo+"|str="+str);
//	window.location.href="/admin/category/update/changePage/"+operation+"?wantedCategory="
//		+currentCategory+"&currentPageNo="+currentPageNo+"&wantedPageNo="+wantedPageNo
//		+"&stateChangeStr="+str;
//	}

	function printTBody(TF41CatAndPage,cbCheckState){
		var cbarray = "";
		var trainingFiles = eval("(" + TF41CatAndPage + ")");
		var checkboxState = eval("(" + cbCheckState + ")");
		//alert("get into printTBody,TF41CatAndPage="+trainingFiles+"cbCheckState="+cbCheckState);
		$.each(trainingFiles,function(i,trainingFile){
			var enabledState="";
			if(trainingFile.enabled==true)
				enabledState = "Enabled";
			else enabledState = "Disabled";
			var cbState="";
			if (checkboxState[i] == true) {
				cbState = "<input type='checkbox' name='checkbox' value=" + (i + 1) + " checked='checked' />";
				cbarray += "1/";
			}
			else {
				cbState = "<input type='checkbox' name='checkbox' value=" + (i + 1) + " />";
				cbarray += "0/";
			}
			var trHTML ="<tr><td>"+(i+1)+"</td><td>"+trainingFile.id+"</td><td>"+trainingFile.title+
						 "</td><td>"+trainingFile.author+"</td><td>"+enabledState+"</td><td>"+cbState+"</td></tr>";
			//alert("trHTML="+trHTML);
			$("#table1 tbody").append(trHTML);
		});
	//	alert("printTBody时的cbarray="+cbarray);
//		for(var i=0;i<TF41CatAndPage.length;i++){
//			var cbState1 = "<input type='checkbox' name='checkbox' value="+(i+1)+" checked='checked' />";
//	 		var cbState2 = "<input type='checkbox' name='checkbox' value="+(i+1)+" />";
//			
//			var enabledState;
//			if(TF41CatAndPage[i].enabled==true)
//				enabledState = "Enabled";
//			else enabledState = "Disabled";
//			var cbState;
//			if(cbCheckState[i]==true)
//			 	cbState = "<input type='checkbox' name='checkbox' value="+(i+1)+" checked='checked' />";
//			 else cbState = "<input type='checkbox' name='checkbox' value="+(i+1)+" />";
//			
//			var trHTML ="<tr><td>"+i+1+"</td><td>"+TF41CatAndPage[i].id+"</td><td>"+TF41CatAndPage[i].title+
//						 "</td><td>"+TF41CatAndPage[i].author+"</td><td>"+enabledState+"</td><td>"+cbState+"</td></tr>";
//			alert("trHTML="+trHTML);
//			$("#table1 tbody").append(trHTML);
//		}
	}

	function printPagination(pageNo,totalP){
		var currentPageNum = parseInt(pageNo);
		//alert("in printPagination,currentPageNum="+currentPageNum+",totalP="+totalP);
		var ulHTML="";
		if(currentPageNum!=1)
			ulHTML+="<li class='text' id='previous'>"+"<a href=''>Previous</a></li>";
		var beginValue = currentPageNum>5?(currentPageNum-5):1;
		for (var i=beginValue;i<=(currentPageNum+5);i++) {
			if (i==currentPageNum)
				ulHTML+="<li><a href=''>"+i+"</a></li>";
			else 
				if (i>0&&i<totalP)
					ulHTML+="<li class='page' name="+i+"><a href='#' title=''>"+i+"</a></li>";
		}
		if(currentPageNum!=totalP&&totalP!=0)
		ulHTML+="<li class='text' id='next'>"+"<a href=''>Next</a></li>";
		//alert("3 ulHTML="+ulHTML);
		$("#pagination1").html(ulHTML);
		
		$(".page").each(function(){
			$(this).click(function(){
				getStrAndToPage('${currentCategory}',pageNo,$(this).attr("name"),'${operation}');
				return false;
			});
		});
		$("#previous").click(function(){
				getStrAndToPage('${currentCategory}',pageNo,(pageNo-1),'${operation}');
				return false;
			});
		$("#next").click(function(){
				getStrAndToPage('${currentCategory}',pageNo,(pageNo+1),'${operation}');
				return false;
			});
	}
	
	function load(operation){
		//alert("in load");
		printTBody('${TF41CatAndPage}','${cbCheckState}');
		printPagination('${pageNo}','${totalP}');
		switch ('${operation}') {
			case ("view"):
				//alert("2");
				$('input[type=checkbox]').attr("disabled","disabled");
				$('#select1').attr("disabled","disabled");
				$("#updateType h2").html("查看类别");
				break;
			case ("modify"):
				//alert("3");
				$('#select1').attr("disabled", "disabled");
				$("#updateType h2").html("修改类别");
				break;
			case ("new"):
				//alert("4");
				$("#updateType h2").html("新增类别");
				break;
		}
	}
	
	function getStrAndToPage(currentCategory,currentPageNo,wantedPageNo,operation){
		//alert("in getStrAndToPage,currentCategory="+currentCategory+",currentPageNo="
		//+currentPageNo+",wantedPageNo="+wantedPageNo+",operation="+operation);
	var str="";
	$("input[name='checkbox']").each(function(){
//		alert("$(this).prop('checked')="+$(this).prop("checked"));
		if($(this).prop("checked")==true)
			str=str+"1";
		else
			str=str+"0";
		str=str+"/";
	});
	//alert("pre-post:str="+str);
	$.post('/admin/category/update/changePage/'+operation,{
		wantedCategory:currentCategory,currentPageNo:currentPageNo,
		wantedPageNo:wantedPageNo,stateChangeStr:str},
		function(data){
			//alert("2");
			//alert("success1,data.currentCategory="+data.currentCategory);
			var cbarray = "";
			//alert("cbCheckState="+data.cbCheckState);
			$("#table1 tbody tr").each(function(i,n){
					//alert("n="+n);
				 $(this).find("td").eq(0).html(i+1);
				 $(this).find("td").eq(1).html(data.TF41CatAndPage[i].id);
				 $(this).find("td").eq(2).html(data.TF41CatAndPage[i].title);
				 $(this).find("td").eq(3).html(data.TF41CatAndPage[i].author);
				 $(this).find("td").eq(4).html(data.TF41CatAndPage[i].enabled==true?"Enabled":"Disabled");
				 
				 if (data.cbCheckState[i] == true) {
				 	cbarray += "1";
				 	cbState = "<input type='checkbox' name='checkbox' value=" + (i + 1) + " checked='checked' />";
				 }
				 else {
				 	cbarray += "0";
				 	cbState = "<input type='checkbox' name='checkbox' value=" + (i + 1) + " />";
				 }
				 $(this).find("td").eq(5).html(cbState);
			});
			printPagination(data.pageNo,'${totalP}');
			//alert("cbarray="+cbarray);
			},"json");
	}
	
	function submitUpload(currentCategory,currentPageNo,operation){
	//	getStrAndToPage(currentCategory,currentPageNo,currentPageNo,operation);
		var str="";
	$("input[name='checkbox']").each(function(){
//		alert("$(this).prop('checked')="+$(this).prop("checked"));
		if($(this).prop("checked")==true)
			str=str+"1";
		else
			str=str+"0";
		str=str+"/";
	});
	//alert("pre-post:str="+str);
	$.post('/admin/category/update/changePage/'+operation,{
	wantedCategory:currentCategory,currentPageNo:currentPageNo,
	wantedPageNo:currentPageNo,stateChangeStr:str},
	function(){
		window.location.href="/admin/category/update/submit/"+operation+"?submitedCategory="
		+currentCategory;
		},"json");
	}
</script>
</head>

<body style="background: none" onLoad="load('${operation}');">
	<%String[] isSelected=(String[])request.getAttribute("isSelected");
	//  session.setAttribute("isSelected", isSelected); 
	%>

	<div class="contentcontainer">
		<div class="headings altheading" id="updateType">
			<h2>default</h2>
		</div>
		<form name="form1" action="/update" method="get">
		<div class="contentbox">
					<c:if test="${operation=='view'}">
						<h2>类别名称</h2>
					</c:if>
					<c:if test="${operation=='modify'}">
						<h2>类别名称</h2>
					</c:if>
					<c:if test="${operation=='new'}">
						<h2>可添加的类别</h2>
					</c:if>
					<select name="catName" id="select1" onchange="window.location.href=
					'/admin/category/update/'+${operation}+'?category='+this.options[this.selectedIndex].value">
						<c:forEach var="cat" items="${cats}">
							<c:if test="${cat==currentCategory}">
							<option selected="selected">${cat}</option>
							</c:if>
							<c:if test="${cat!=currentCategory}">
							<option >${cat}</option>
							</c:if>
						</c:forEach>
					</select>
				<div>
				<p></p>
				<h3>训练集资源列表</h3> 
				<c:out value="${fn:length(TF41CatAndPage)}"></c:out>
				<table id="table1" width="100%">
				<thead>
					<tr>
						<th>序号</th>
						<th>Id</th>
						<th>标题</th>
						<th>作者</th>
						<th>状态</th>
						<th>启用/禁用</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				</table>
				<p></p>	
			<ul class="pagination" id="pagination1">
			</ul>
				</div>
				<p></p>
				<button type="button" class="btn" onClick="submitUpload('${currentCategory}','${pageNo}','${operation}');">提交</button>
				<button type="button" class="btn" onClick="window.location.href='/admin/category/list'">返回</button>
		</div>
		</form>
	</div>
</body>
</html>


<c:if test="${pageNo!=1}">
					<li class="text"><a href="" onClick="getStrAndToPage('${currentCategory}','${pageNo}','${pageNo-1}','${operation}');return false;">Previous</a></li>
				</c:if>
				<c:if test="${pageNo-5>0}">
				<c:set var="beginValue" value="${pageNo-5}"/>
				</c:if>
				<c:if test="${pageNo-5<=0}">
				<c:set var="beginValue" value="1"/>
				</c:if>
				<c:forEach var="num" begin="${beginValue}" end="${pageNo+5}" step="1">				
					<c:if test="${num == pageNo}">
						<li class="page"><a href="#" title="">${num}</a></li>
					</c:if>
					<c:if test="${num!=pageNo&&num>0&&num<totalP}">
						<li><a href="" onClick="getStrAndToPage('${currentCategory}','${pageNo}','${num}','${operation}');return false;">${num}</a></li>
					</c:if>
				</c:forEach>
				<c:if test="${pageNo != totalP && totalP !=0}">
					<li class="text"><a href="" onClick="getStrAndToPage('${currentCategory}','${pageNo}','${pageNo+1}','${operation}');return false;">Next</a></li>
				</c:if>