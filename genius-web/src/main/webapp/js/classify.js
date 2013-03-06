$(document).ready(function(){
	$("#classify_plsa").click(function(){
		$('#classify_plsa').attr('disabled',"true");
		$('#classify_plsa').removeClass("btn");
		$('#classify_plsa').addClass("btnalt");
		$.ajax({
			type : "POST",
			url : "/admin/classification_jxt/classify_plsa",
			data :  "pageIdsString="+$("#wysiwyg").val(),
			success : function(data){
				alert("plsa classify finish");
				location.href="/admin/classification_jxt/list";
				$('#classify_plsa').removeAttr("disabled");
				$('#classify_plsa').removeClass("btnalt");
				$('#classify_plsa').addClass("btn");
			},
			erro : function(){
				alert("erro");
			},
			complete : function(){
				
			}
		});
	});
});