var constant=require('../../../constants/constant');
var base_url_admin1=constant.base_url_admin1;
   $( document ).ready(function() {
       
       console.log("hello");
       $('#class').change(function(){
         var  cat_id=$(this).val();
         
         console.log(cat_id);
       var cat_id =cat_id;
       $.ajax({
       type:'post',
       url:base_url_admin1+'addAuction1',
       data:{'cat_id':cat_id},
       success: function(data){
       var htm = "";
       if(data!='0'){
       var result = JSON.parse(JSON.stringify(data));

       console.log(result);
       $.each(result, function(key, result){
           htm +=  '<option value="'+result.sub_cat_id+'">'+result.sub_cat_title+'</option>';
           });
       }
       $('#subcategory').html(htm);
       },
       error: function(e){
      // alert(e.toSourceCode);
       }
       });
   });
   });

   $( document ).ready(function() {
      
       $('#subcategory').change(function(){
         var  sub_cat_id=$(this).val();

         console.log(sub_cat_id);
       var sub_cat_id =sub_cat_id;
       $.ajax({
       type:'post',
       url:base_url_admin1+'addBrand1',
       data:{'sub_cat_id':sub_cat_id},
       success: function(data){
       var htm = "";
       if(data!='0'){
       var result = JSON.parse(JSON.stringify(data));

       console.log(result);
       $.each(result, function(key, result){
           htm +=  '<option value="'+result.brand_id+'">'+result.brand_name+'</option>';
           });
       }
       $('#brand_cat').html(htm);
       },
       error: function(e){
      // alert(e.toSourceCode);
       }
       });
   });
   });


 
   $( document ).ready(function() {
       $('#brand_cat').change(function(){
         var  brand_id=$(this).val();

       var brand_id =brand_id;
       $.ajax({
       type:'post',
       url:base_url_admin1+'addModel1',
       data:{'brand_id':brand_id},
       success: function(data){
       var htm = "";
       if(data!='0'){
       var result = JSON.parse(JSON.stringify(data));

       $.each(result, function(key, result){
           htm +=  '<option value="'+result.model_id+'">'+result.model_name+'</option>';
           });
       }
       $('#model_cat').html(htm);
       },
       error: function(e){
      // alert(e.toSourceCode);
       }
       });
   });
   });

$(document).ready(function(){
  $( ".selected" ).each(function(index) {
    $(this).on("click", function(){
       
        var boolKey = $(this).data('selected');
        var mammalKey = $(this).attr('id');
        var atten= $(this).attr('value');
      // alert(mammalKey);
      // alert(atten);
    setId(mammalKey,atten);
});
});

});
var status="";
    var support_pub_id="";
    function setId(x,y){
       // alert("setId");
        status = x;
        support_pub_id = y;
     
       $.ajax({
 url: base_url_admin1+"updateSupport",
 method: "POST",
 data:{status:status,support_pub_id:support_pub_id},
success:function(data){
    
}
})
    }


    $(document).ready(function(){
  $( ".bbb" ).each(function(index) {
    $(this).on("click", function(){
      alert('sss');   
         var boolKey = $(this).data('bbb');
        var mammalKey = $(this).attr('id');
        var atten= $(this).attr('value');
      alert(mammalKey);
      alert(atten);
    setId(mammalKey,atten);
});
});

});
var status="";
    var support_pub_id="";
    function setId(x,y){
       // alert("setId");
        status = x;
        report_user_pub_id = y;
     
       $.ajax({
 url: base_url_admin1+"updateReport",
 method: "POST",
 data:{status:status,report_user_pub_id:report_user_pub_id},
success:function(data){
    
}
})
    }