$(document).ready(function(){
	$('#trainModel_plsa').click(function(){
//		$('#trainModel_plsa').attr('disabled',"true");
		$.ajax({
			beforeSend: function(){
				$('#trainModel_plsa').attr('disabled',"true");
				$('#trainModel_plsa').removeClass("btn");
				$('#trainModel_plsa').addClass("btnalt");
			},
			type : "GET",
			url : "/admin/category/trainModel_plsa",
			data: "trainingState=training",
			success : function(){
				alert("plsa training finish");
				location.href="/admin/category/list";
				$('#trainModel_plsa').removeAttr("disabled");
				$('#trainModel_plsa').removeClass("btnalt");
				$('#trainModel_plsa').addClass("btn");
			},
			erro : function(){
				alert("erro");
			}
		});
	});
});