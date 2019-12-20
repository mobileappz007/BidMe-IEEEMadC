var baseurl='http://localhost:3004/';

   $(document).ready(function() {
         $('#class').change(function(){
     
         var cat_id=$(this).val();
         var cat_id =cat_id;
       $.ajax({
       type:'post',
       url:baseurl+'addAuction1',
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
       alert(e.toSourceCode);
       }
       });
   });
   });

   $( document ).ready(function() {
       $('#subcategory').change(function(){
         var  sub_cat_id=$(this).val();
       var sub_cat_id =sub_cat_id;
       $.ajax({
       type:'post',
       url:baseurl+'addBrand1',
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
       alert(e.toSourceCode);
       }
       });
   });
   });


   $( document ).ready(function() {
       console.log("hello");
       $('#brand_cat').change(function(){
         var  brand_id=$(this).val();

         console.log(brand_id);
       var brand_id =brand_id;
       $.ajax({
       type:'post',
       url:baseurl+'addModel1',
       data:{'brand_id':brand_id},
       success: function(data){
       var htm = "";
       if(data!='0'){
       var result = JSON.parse(JSON.stringify(data));

       console.log(result);
       $.each(result, function(key, result){
           htm +=  '<option value="'+result.model_id+'">'+result.model_name+'</option>';
           });
       }
       $('#model_cat').html(htm);
       },
       error: function(e){
       alert(e.toSourceCode);
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
      //  alert("setId");
        status = x;
        support_pub_id = y;
     
       $.ajax({
 url: baseurl+"updateSupport",
 method: "POST",
 data:{status:status,support_pub_id:support_pub_id},
success:function(data){
    window.location.href='/viewAllSupport';
}
})
    }
      
  
 

    $(document).ready(function(){
  $( ".bbb" ).each(function(index) {
    $(this).on("click", function(){
         var boolKey = $(this).data('bbb');
        var mammalKey = $(this).attr('id');
        var atten= $(this).attr('value');
      
    setId1(mammalKey,atten);
});
});

});
var status="";
    var support_pub_id="";
    function setId1(x,y){
        status = x;
        report_user_pub_id = y;
       $.ajax({
 url: baseurl+"updateReport",
 method: "POST",
 data:{status:status,report_user_pub_id:report_user_pub_id},
success:function(data){
    window.location.href='/viewAllReports';
}
})
}


function setStatus(id){
//    var user_id=document.getElementById('changeStatus').value;
       $.ajax({
       
       url:'http://localhost:3004/changeUserStatus',
       method:"POST",
       data:{user_pub_id:id},
       success: function(data){
         // window.location.href='/userDetails';
         console.log('sss');
        location.reload();  

       },
       error: function(e){
       alert(e.toSourceCode);
       }
       });
}
 

function setEmailStatus(id){
//    var user_id=document.getElementById('changeStatus').value;
       $.ajax({
       
       url:'http://localhost:3004/changeUserEmailStatus',
       method:"POST",
       data:{user_pub_id:id},
       success: function(data){
         // window.location.href='/userDetails';
         console.log('sss');
        location.reload();  

       },
       error: function(e){
       alert(e.toSourceCode);
       }
       });
}
