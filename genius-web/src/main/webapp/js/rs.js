//function autocomplete(){
$(document).ready(

		function(){
		
$('#rs').hide();

var s=$('#relatedSearch').val();
//s=s.replace(']','');
if(s!=null&&s!=""){
	var rs=s.split(',');
	//s=["11111","22222","33333","44444","55555"];
	//alert(s);
	if(rs.length>0){
		var r="<table cellpadding='0'><tr><th rowspan='2'>相关搜索</th><td style='width:10px' rowspan='2'></td>";
		for(var i=0;i<rs.length;i++){
			r=r+"<th style='text-align:left'><a href='g?q="+rs[i]+"'"+">"+rs[i]+"</a></th><td style='width:40px'></td>";
			if(i==4)
				r=r+"</tr><tr>";
		}
		r=r+"</tr><table>";
		$('#rs').append(r);
		$('#rs').show();
	}
}


		}
);
//}