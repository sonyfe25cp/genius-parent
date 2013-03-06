<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统统计报告</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">  
function HS_DateAdd(interval,number,date){  
   number = parseInt(number);  
   if (typeof(date)=="string"){var date = new Date(date.split("-")[0],date.split("-")[1],date.split("-")[2])}  
   if (typeof(date)=="object"){var datedate = date}  
   switch(interval){  
   case "y":return new Date(date.getFullYear()+number,date.getMonth(),date.getDate()); break;  
   case "m":return new Date(date.getFullYear(),date.getMonth()+number,checkDate(date.getFullYear(),date.getMonth()+number,date.getDate())); break;  
   case "d":return new Date(date.getFullYear(),date.getMonth(),date.getDate()+number); break;  
   case "w":return new Date(date.getFullYear(),date.getMonth(),7*number+date.getDate()); break;  
   }  
}  
function checkDate(year,month,date){  
   var enddate = ["31","28","31","30","31","30","31","31","30","31","30","31"];  
   var returnDate = "";  
   if (year%4==0){enddate[1]="29"}  
   if (date>enddate[month]){returnDate = enddate[month]}else{returnDate = date}  
   return returnDate;  
}  
  
function WeekDay(date){  
   var theDate;  
   if (typeof(date)=="string"){theDate = new Date(date.split("-")[0],date.split("-")[1],date.split("-")[2]);}  
   if (typeof(date)=="object"){theDate = date}  
   return theDate.getDay();  
}  
function HS_calender(){  
   var lis = "";  
   var style = "";  
   /* http://www.webdm.cn*/  
   style +="<style type='text/css'>";  
   style +=".calender { width:170px; height:auto; font-size:12px; margin-right:14px; background:url(calenderbg.gif) no-repeat right center #fff; border:1px solid #397EAE; padding:1px}";  
   style +=".calender ul {list-style-type:none; margin:0; padding:0;}";  
   style +=".calender .day { background-color:#EDF5FF; height:20px;}";  
   style +=".calender .day li,.calender .date li{ float:left; width:14%; height:20px; line-height:20px; text-align:center}";  
   style +=".calender li a { text-decoration:none; font-family:Tahoma; font-size:11px; color:#333}";  
   style +=".calender li a:hover { color:#f30; text-decoration:underline}";  
   style +=".calender li a.hasArticle {font-weight:bold; color:#f60 !important}";  
   style +=".lastMonthDate, .nextMonthDate {color:#bbb;font-size:11px}";  
   style +=".selectThisYear a, .selectThisMonth a{text-decoration:none; margin:0 2px; color:#000; font-weight:bold}";  
   style +=".calender .LastMonth, .calender .NextMonth{ text-decoration:none; color:#000; font-size:18px; font-weight:bold; line-height:16px;}";  
   style +=".calender .LastMonth { float:left;}";  
   style +=".calender .NextMonth { float:right;}";  
   style +=".calenderBody {clear:both}";  
   style +=".calenderTitle {text-align:center;height:20px; line-height:20px; clear:both}";  
   style +=".today { background-color:#ffffaa;border:1px solid #f60; padding:2px}";  
   style +=".today a { color:#f30; }";  
   style +=".calenderBottom {clear:both; border-top:1px solid #ddd; padding: 3px 0; text-align:left}";  
   style +=".calenderBottom a {text-decoration:none; margin:2px !important; font-weight:bold; color:#000}";  
   style +=".calenderBottom a.closeCalender{float:right}";  
   style +=".closeCalenderBox {float:right; border:1px solid #000; background:#fff; font-size:9px; width:11px; height:11px; line-height:11px; text-align:center;overflow:hidden; font-weight:normal !important}";  
   style +="</style>";  
  
   var now;  
   if (typeof(arguments[0])=="string"){  
      selectDate = arguments[0].split("-");  
      var year = selectDate[0];  
      var month = parseInt(selectDate[1])-1+"";  
      var date = selectDate[2];  
      now = new Date(year,month,date);  
   }else if (typeof(arguments[0])=="object"){  
      now = arguments[0];  
   }  
   var lastMonthEndDate = HS_DateAdd("d","-1",now.getFullYear()+"-"+now.getMonth()+"-01").getDate();  
   var lastMonthDate = WeekDay(now.getFullYear()+"-"+now.getMonth()+"-01");  
   var thisMonthLastDate = HS_DateAdd("d","-1",now.getFullYear()+"-"+(parseInt(now.getMonth())+1).toString()+"-01");  
   var thisMonthEndDate = thisMonthLastDate.getDate();  
   var thisMonthEndDay = thisMonthLastDate.getDay();  
   var todayObj = new Date();  
   today = todayObj.getFullYear()+"-"+todayObj.getMonth()+"-"+todayObj.getDate();  
     
   for (i=0; i<lastMonthDate; i++){  // Last Month's Date  
      lis = "<li class='lastMonthDate'>"+lastMonthEndDate+"</li>" + lis;  
      lastMonthEndDate--;  
   }  
   for (i=1; i<=thisMonthEndDate; i++){ // Current Month's Date  
  
      if(today == now.getFullYear()+"-"+now.getMonth()+"-"+i){  
         var todayString = now.getFullYear()+"-"+(parseInt(now.getMonth())+1).toString()+"-"+i;  
         lis += "<li><a href=javascript:void(0) class='today' onclick='_selectThisDay(this)' title='"+now.getFullYear()+"-"+(parseInt(now.getMonth())+1)+"-"+i+"'>"+i+"</a></li>";  
      }else{  
         lis += "<li><a href=javascript:void(0) onclick='_selectThisDay(this)' title='"+now.getFullYear()+"-"+(parseInt(now.getMonth())+1)+"-"+i+"'>"+i+"</a></li>";  
      }  
        
   }  
   var j=1;  
   for (i=thisMonthEndDay; i<6; i++){  // Next Month's Date  
      lis += "<li class='nextMonthDate'>"+j+"</li>";  
      j++;  
   }  
   lis += style;  
  
   var CalenderTitle = "<a href='javascript:void(0)' class='NextMonth' onclick=HS_calender(HS_DateAdd('m',1,'"+now.getFullYear()+"-"+now.getMonth()+"-"+now.getDate()+"'),this) title='Next Month'>»</a>";  
   CalenderTitle += "<a href='javascript:void(0)' class='LastMonth' onclick=HS_calender(HS_DateAdd('m',-1,'"+now.getFullYear()+"-"+now.getMonth()+"-"+now.getDate()+"'),this) title='Previous Month'>«</a>";  
   CalenderTitle += "<span class='selectThisYear'><a href='javascript:void(0)' onclick='CalenderselectYear(this)' title='Click here to select other year' >"+now.getFullYear()+"</a></span>年<span class='selectThisMonth'><a href='javascript:void(0)' onclick='CalenderselectMonth(this)' title='Click here to select other month'>"+(parseInt(now.getMonth())+1).toString()+"</a></span>月";   
  
   if (arguments.length>1){  
      arguments[1].parentNode.parentNode.getElementsByTagName("ul")[1].innerHTML = lis;  
      arguments[1].parentNode.innerHTML = CalenderTitle;  
  
   }else{  
      var CalenderBox = style+"<div class='calender'><div class='calenderTitle'>"+CalenderTitle+"</div><div class='calenderBody'><ul class='day'><li>日</li><li>一</li><li>二</li><li>三</li><li>四</li><li>五</li><li>六</li></ul><ul class='date' id='thisMonthDate'>"+lis+"</ul></div><div class='calenderBottom'><a href='javascript:void(0)' class='closeCalender' onclick='closeCalender(this)'>×</a><span><span><a href=javascript:void(0) onclick='_selectThisDay(this)' title='"+todayString+"'>Today</a></span></span></div></div>";  
      return CalenderBox;  
   }  
}  
function _selectThisDay(d){  
   var boxObj = d.parentNode.parentNode.parentNode.parentNode.parentNode;  
      boxObj.targetObj.value = d.title;  
      boxObj.parentNode.removeChild(boxObj);  
}  
function closeCalender(d){  
   var boxObj = d.parentNode.parentNode.parentNode;  
      boxObj.parentNode.removeChild(boxObj);  
}  
  
function CalenderselectYear(obj){  
      var opt = "";  
      var thisYear = obj.innerHTML;  
      for (i=1970; i<=2020; i++){  
         if (i==thisYear){  
            opt += "<option value="+i+" selected>"+i+"</option>";  
         }else{  
            opt += "<option value="+i+">"+i+"</option>";  
         }  
      }  
      opt = "<select onblur='selectThisYear(this)' onchange='selectThisYear(this)' style='font-size:11px'>"+opt+"</select>";  
      obj.parentNode.innerHTML = opt;  
}  
  
function selectThisYear(obj){  
   HS_calender(obj.value+"-"+obj.parentNode.parentNode.getElementsByTagName("span")[1].getElementsByTagName("a")[0].innerHTML+"-1",obj.parentNode);  
}  
  
function CalenderselectMonth(obj){  
      var opt = "";  
      var thisMonth = obj.innerHTML;  
      for (i=1; i<=12; i++){  
         if (i==thisMonth){  
            opt += "<option value="+i+" selected>"+i+"</option>";  
         }else{  
            opt += "<option value="+i+">"+i+"</option>";  
         }  
      }  
      opt = "<select onblur='selectThisMonth(this)' onchange='selectThisMonth(this)' style='font-size:11px'>"+opt+"</select>";  
      obj.parentNode.innerHTML = opt;  
}  
function selectThisMonth(obj){  
   HS_calender(obj.parentNode.parentNode.getElementsByTagName("span")[0].getElementsByTagName("a")[0].innerHTML+"-"+obj.value+"-1",obj.parentNode);  
}  
function HS_setDate(inputObj){  
   var calenderObj = document.createElement("span");  
   calenderObj.innerHTML = HS_calender(new Date());  
   calenderObj.style.position = "absolute";  
   calenderObj.targetObj = inputObj;  
   inputObj.parentNode.insertBefore(calenderObj,inputObj.nextSibling);  
}  
  </script>  
<style>  
  body {font-size:12px}  
  td {text-align:center}  
  h1 {font-size:26px;}  
  h4 {font-size:16px;}  
  em {color:#999; margin:0 10px; font-size:11px; display:block}  
  </style>  
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>配置文件</h2>
		</div>
		</div>
			<div class="contentbox">
			<form action="calendarpdf" method="get">
			<p>
			<label for="textfield"><strong>请输入报表表头</strong></label>
			<input type="text"  name="head" class="inputbox" value="Welcome To Genius" /></p>
			<p>
			<label for="textfield"><strong>请输入报表标题</strong></label>
			<input type="text"  name="title" class="inputbox"  value="Genius 统计报告"/></p>
				<p>
			<label for="textfield"><strong>请输入联系地址</strong></label>
			<input type="text"  name="address" class="inputbox" value="北京海淀区中关村南大街5号北京理工大学"/></p>
				<p>
			<label for="textfield"><strong>请输入Email地址</strong></label>
			<input type="text"  name="email" class="inputbox" value="genius2012@bit.edu" /></p>
				<p>
			<label for="textfield"><strong>请输入电话号码</strong></label>
			<input type="text"  name="telephone" class="inputbox smallbox" value="010-68912400" /></p>
			<table border="2" width="50%">  
			<tr align="center">  
			  <th width="50%">选择开始日期</th>  
			  <th width="50%">选择结束日期</th>  
  			  <th width="10%">提交</th>
			</tr>  
			<tr>  
			  <td><input type="text" width="70%" onfocus="HS_setDate(this)" name="startime"></td>  
  			  <td><input type="text" width="70%" onfocus="HS_setDate(this)" name="currentime"></td>  
			  <td><input type="submit" class="btn" value="查看报表"></td>
			</tr>  
			</table> 	
			</form>
			<br><br><br><br>
			</div>
				
		
</body>
</html>