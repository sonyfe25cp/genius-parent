$(document).ready(function(){
//    $.post("GetSession", {
//        Attribute: "UserName"
//    }, function(data){
//        if (data != "!NU") {
//            $("a#user").html(data);
//        }
//        else {
//            $("a#user").html("登录");
//        }
//    });
    
    $("li.ccl").mouseover(function(){
        $(this).children(".cclc").css("padding", "6px 8px");
        $(this).children(".cclc").css("border-color", "#FFFFFF");
        //$(this).children(".cclc").css("outline-color", "#FFFFFF");
        $(this).children(".cclc").css("background", "#FEFEFE");
        //$(this).children(".cclbs").css("visibility", "visible");
    });
    $("li.ccl").mouseout(function(){
        $(this).children(".cclc").css("padding", "4px 6px");
        $(this).children(".cclc").css("border-color", "#FFF");
        $(this).children(".cclc").css("border-color", "transparent");
        //$(this).children(".cclc").css("outline-color", "#FFF");
        //$(this).children(".cclc").css("outline-color", "transparent");
        $(this).children(".cclc").css("background", "transparent");
        //$(this).children(".cclbs").css("visibility", "hidden");
    });
    
    $(".cclbs").click(function(){
		var $this = $(this);
		//alert("send: addcollection"+$(this).siblings(".cclc").find(".ccldmi").html());
        $.post("addcollection", {
            sourceId: $(this).siblings(".cclc").find(".ccldmi").html()
        }, function(data){
			//alert(data);
			//$this.siblings(".cclc").find(".ccldmi").html(data);
        	//$this.siblings(".cclc").find(".ccldmi").html("collection:"+data);
        });
    });
	
	 $(".cclha").click(function(){
		var $this = $(this);
		//alert("send:addhistory "+$(this).closest(".cclc").find(".ccldmi").html());
        $.post("addhistory", {
            sourceId: $(this).closest(".cclc").find(".ccldmi").html()
        }, function(data){
			//alert(data);
			//$this.closest(".cclc").find(".ccldmi").html("history:"+data);
        });
    });
	
    $(".cclbs").mouseover(function(){
		//$(this).css("visibility","visible");
        $(this).css("cursor", "pointer");
		$(this).css("background", "aqua");
    });
    $(".cclbs").mouseout(function(){
		//$(this).css("visibility","hidden");
        $(this).css("cursor", "default");
		$(this).css("background", "#FFF");
    });
});
