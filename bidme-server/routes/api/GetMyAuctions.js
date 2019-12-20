var Promise = require('promise');
var express=require('express');
var app=express();
var mysql = require('promise-mysql');
var router=express.Router();
var path=require('path');
var cors=require('cors');
var cookeParser =require('cookie-parser');
var bodyparser =require('body-parser');
var db=require('../../config/database.js');

var constant=require('../../constants/constant.js')
var base_url=constant.base_url;
router.post('/getMyAuction',function(req,res){
	var result_array = [];
	var counter = 0;
        // The Promise constructor should catch any errors thrown on
        // this tick. Alternately, try/catch and reject(err) on catch.
        db.query('SELECT p.*,COALESCE(b.brand_name,"") as brand_name, COALESCE(m.model_name,"") as model_name,CONCAT("'+base_url+'",image) as image,c.currency_code FROM (((auction_pro p JOIN currency_setting c) LEFT JOIN brand b ON b.brand_id=p.brand_id) LEFT JOIN model m ON m.model_id=p.model_id) WHERE c.status="1" AND p.user_pub_id="'+req.body.user_pub_id+'"  AND p.status!="2"',function (err, rows, fields){

            // Call reject on error states,
            // call resolve with results
           // console.log(rows)
            if (err) {
                   res.send({
               	"status":0,
               	"message":err
               })
            }else{
              if(rows.length>0){
              console.log(rows)
            	var row=JSON.parse(JSON.stringify(rows))
            	console.log('Length::'+rows.length)
           row.forEach(function(row) {
           		db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
           			if(err){
           				console.log(err);
           			}
           			else{
           			var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;
result_array.push(row);
counter++;
console.log(row.image_cust)
if(counter== rows.length){
res.send({
		"status":1,
		"message":constant.get_All_Aunc,
		"data":result_array
	});
}
        }
  		})
		});	
      }else{
        res.send({
          "status":0,
          "message":"No data found "
        })
      }   
      }
        });
})

router.post('/getAllAuctions',function(req,res){
	var user_pub_id=req.body.user_pub_id;
	var cat_id=req.body.cat_id;
	var lat=req.body.latitude;
	var long=req.body.longitude;
	var max_price=req.body.max_price;
	var min_price=req.body.min_price;
	var distance=req.body.distance;

	if(!user_pub_id & !cat_id){
		res.send({
			"status":0,
			"message":constant.CHKAllFIELD
		})
	}
	else if(user_pub_id && cat_id && lat && long&& max_price && min_price && distance){
	
		//console.log(min_price);
	
var result_array = [];
	var counter = 0;
        // The Promise constructor should catch any errors thrown on
        // this tick. Alternately, try/catch and reject(err) on catch.
        db.query('SELECT p.*,c.currency_code, CONCAT("'+base_url+'", p.image) as image,( 3959* acos( cos( radians("'+lat+'") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("'+long+'") ) + sin( radians("'+lat+'") ) * sin( radians( latitude ) ) ) ) AS distance  FROM `auction_pro` p JOIN currency_setting c WHERE c.status="1" AND user_pub_id !=? AND cat_id=? AND p.status="0" AND price BETWEEN '+min_price+' AND '+max_price+' HAVING distance <= "'+distance+'"',[user_pub_id,cat_id],function (err, rows){
            // Call reject on error states,
            // call resolve with results
           // console.log(rows)
            if (err) {
               res.send({
               	"status":0,
               	"message":err
               })
            }else{
              console.log('1');
                if(rows.length>0){
            	var row=JSON.parse(JSON.stringify(rows))
           row.forEach(function(row) {
                        var end_date=row.e_date;
                      var date1=new Date();
   var day = new Date(end_date).getDate();
   var monthIndex = new Date(end_date).getMonth();
   var year = new Date(end_date).getFullYear();
 var date11=year+"-"+(monthIndex+1)+"-"+day;
 req.body.ddd=new Date(date11);
req.body.date1=date1;
db.query("select TIMESTAMPDIFF(MINUTE,STR_TO_DATE('"+req.body.ddd+"','%W %M %d %Y %H:%i:%s'),STR_TO_DATE('"+req.body.date1+"','%W %M %d %Y %H:%i:%s')) as timediffrence from auction_pro t",function(err,results){
  if(err){
    console.log(err);
  }else{
    var diff=results[0].timediffrence
    var total_Min=1440;
    var difference=total_Min-diff;

    row.remaining_time=Math.abs(difference);
  }
})

           		db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
           			if(err){
           				throw err
           			}
           			else{
           			var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;
//row.remaining_time=date1;
result_array.push(row);
counter++;
console.log(row.image_cust)
if(counter== rows.length){
	console.log('Send response');
	res.send({
		"status":1,
		"message":constant.get_All_Aunc,
		"data":result_array
	});
}

           			}

           		})
		});
            } else{
              res.send({
                "status":0,
                "message":constant.NODATA
              })
            }
           } 
        });
       
}
else if(user_pub_id && cat_id && long && lat && !distance && max_price && min_price){
  //console.log('3')
  var result_array = [];
  var counter = 0;
        // The Promise constructor should catch any errors thrown on
        // this tick. Alternately, try/catch and reject(err) on catch.
        var data=320;
        db.query('SELECT p.*,c.currency_code,CONCAT("'+base_url+'",p.image) as image,( 3959* acos( cos( radians("'+lat+'") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("'+long+'") ) + sin( radians("'+lat+'") ) * sin( radians( latitude ) ) ) ) AS distance FROM `auction_pro` p JOIN currency_setting c WHERE c.status ="1" AND user_pub_id!=? AND cat_id=? AND p.status="0" AND price BETWEEN '+min_price+' AND '+max_price+' HAVING distance <= "'+data+'" ',[user_pub_id,cat_id],function (err, rows){

            // Call reject on error states,
            // call resolve with results
           // console.log(rows)
            if (err) {
                    res.send({
                "status":0,
                "message":err
               })
            }else{
              console.log('3')
                if(rows.length>0){
              var row=JSON.parse(JSON.stringify(rows))
             // console.log('Length::'+rows.length)
           row.forEach(function(row) {
              var end_date=row.e_date;
                      var date1=new Date();
   var day = new Date(end_date).getDate();
   var monthIndex = new Date(end_date).getMonth();
   var year = new Date(end_date).getFullYear();
 var date11=year+"-"+(monthIndex+1)+"-"+day;
 req.body.ddd=new Date(date11);
req.body.date1=date1;
db.query("select TIMESTAMPDIFF(MINUTE,STR_TO_DATE('"+req.body.ddd+"','%W %M %d %Y %H:%i:%s'),STR_TO_DATE('"+req.body.date1+"','%W %M %d %Y %H:%i:%s')) as timediffrence from auction_pro t",function(err,results){
  if(err){
    console.log(err);
  }else{
    var diff=results[0].timediffrence
    var total_Min=1440;
    var difference=total_Min-diff;

    row.remaining_time=Math.abs(difference);
  }
})

              db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
                if(err){
                  throw err
                }
                else{
                var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;
//row.remaining_time=date1;
result_array.push(row);
counter++;
if(counter== rows.length){


  console.log('Send response');
  res.send({
    "status":1,
    "message":constant.get_All_Aunc,
    "data":result_array
  });
}

                }

              })

    
    });
              
            } else{
              res.send({
                "status":0,
                "message":constant.NODATA
              })
            }
          }
            
        });

}
else if(user_pub_id && cat_id && long && lat && !distance && !max_price && !min_price){
  //console.log('3')
  var result_array = [];
  var counter = 0;
        // The Promise constructor should catch any errors thrown on
        // this tick. Alternately, try/catch and reject(err) on catch.
        var data=320;
        db.query('SELECT p.*,c.currency_code,CONCAT("'+base_url+'",p.image) as image,( 3959* acos( cos( radians("'+lat+'") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("'+long+'") ) + sin( radians("'+lat+'") ) * sin( radians( latitude ) ) ) ) AS distance FROM `auction_pro` p JOIN currency_setting c WHERE c.status ="1" AND user_pub_id!=? AND p.status="0" AND cat_id=? HAVING distance <= "'+data+'" ',[user_pub_id,cat_id],function (err, rows){

            // Call reject on error states,
            // call resolve with results
           // console.log(rows)
            if (err) {
                    res.send({
                "status":0,
                "message":err
               })
            }else{
                if(rows.length>0){
              var row=JSON.parse(JSON.stringify(rows))
           //   console.log('Length::'+rows.length)

           row.forEach(function(row) {
                        var end_date=row.e_date;
                      var date1=new Date();
   var day = new Date(end_date).getDate();
   var monthIndex = new Date(end_date).getMonth();
   var year = new Date(end_date).getFullYear();
 var date11=year+"-"+(monthIndex+1)+"-"+day;
 req.body.ddd=new Date(date11);
req.body.date1=date1;
db.query("select TIMESTAMPDIFF(MINUTE,STR_TO_DATE('"+req.body.ddd+"','%W %M %d %Y %H:%i:%s'),STR_TO_DATE('"+req.body.date1+"','%W %M %d %Y %H:%i:%s')) as timediffrence from auction_pro t",function(err,results){
  if(err){
    console.log(err);
  }else{
    var diff=results[0].timediffrence
    var total_Min=1440;
    var difference=total_Min-diff;

    row.remaining_time=Math.abs(difference);
  }
})
              db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
                if(err){
                  throw err
                }
                else{
                var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;
//row.remaining_time=date1;
result_array.push(row);
counter++;
console.log(row.image_cust)
if(counter== rows.length){
  res.send({
    "status":1,
    "message":constant.get_All_Aunc,
    "data":result_array
  });
}

                }

              })

    
    });
              
            } else{
              res.send({
                "status":0,
                "message":constant.NODATA
              })
            }
          }
            
        });

}
 else if(user_pub_id &&cat_id && long && lat && distance && !max_price && !min_price){
 	//console.log('3')
 	var result_array = [];
	var counter = 0;
        // The Promise constructor should catch any errors thrown on
        // this tick. Alternately, try/catch and reject(err) on catch.
        db.query('SELECT p.*,c.currency_code,CONCAT("'+base_url+'",p.image) as image,( 3959* acos( cos( radians("'+lat+'") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("'+long+'") ) + sin( radians("'+lat+'") ) * sin( radians( latitude ) ) ) ) AS distance FROM `auction_pro` p JOIN currency_setting c WHERE c.status ="1" AND user_pub_id!=? AND p.status="0" AND cat_id=? HAVING distance <= "'+distance+'" ',[user_pub_id,cat_id],function (err, rows){

            // Call reject on error states,
            // call resolve with results
           // console.log(rows)
            if (err) {
                    res.send({
               	"status":0,
               	"message":err
               })
            }else{
                if(rows.length>0){
            	var row=JSON.parse(JSON.stringify(rows))
            	//console.log('Length::'+rows.length)
          
           row.forEach(function(row) {
            var end_date=row.e_date;
                      var date1=new Date();
   var day = new Date(end_date).getDate();
   var monthIndex = new Date(end_date).getMonth();
   var year = new Date(end_date).getFullYear();
 var date11=year+"-"+(monthIndex+1)+"-"+day;
 req.body.ddd=new Date(date11);
req.body.date1=date1;
db.query("select TIMESTAMPDIFF(MINUTE,STR_TO_DATE('"+req.body.ddd+"','%W %M %d %Y %H:%i:%s'),STR_TO_DATE('"+req.body.date1+"','%W %M %d %Y %H:%i:%s')) as timediffrence from auction_pro t",function(err,results){
  if(err){
    console.log(err);
  }else{
    var diff=results[0].timediffrence
    var total_Min=1440;
    var difference=total_Min-diff;

    row.remaining_time=Math.abs(difference);
  }
})

           		db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
           			if(err){
           				throw err
           			}
           			else{
           			var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;
result_array.push(row);
counter++;
console.log(row.image_cust)
if(counter== rows.length){
	console.log('Send response');
	res.send({
		"status":1,
		"message":constant.get_All_Aunc,
		"data":result_array
	});
}

           			}

           		})

		
		});
            	
            } else{
              res.send({
                "status":0,
                "message":constant.NODATA
              })
            }
          }
            
        });

} 
else if(user_pub_id && cat_id && !lat && !long && !max_price && !min_price && !distance) {
	//console.log("4")
	var result_array = [];
	var counter = 0;
        // The Promise constructor should catch any errors thrown on
        // this tick. Alternately, try/catch and reject(err) on catch.
        db.query('SELECT * from auction_pro WHERE cat_id=?',[cat_id],function(err,results){
          if(err){
            res.send({
            "status":0,
            "message":err
           })
          }else{
            var result=JSON.parse(JSON.stringify(results));
            if(results.length>0){
              db.query('SELECT p.*,c.currency_code,CONCAT("'+base_url+'",p.image) as image FROM `auction_pro` p JOIN currency_setting c WHERE c.status="1" AND user_pub_id!=? AND cat_id=? AND p.status="0"',[user_pub_id,cat_id],function (err, rows){

            // Call reject on error states,
            // call resolve with results
           // console.log(rows)
            if (err) {
                   res.send({
                "status":0,
                "message":err
               })
            }else{
                if(rows.length>0){
              var row=JSON.parse(JSON.stringify(rows))
              console.log('Length::'+rows.length)
           row.forEach(function(row) {
            var end_date=row.e_date;
                      var date1=new Date();
   var day = new Date(end_date).getDate();
   var monthIndex = new Date(end_date).getMonth();
   var year = new Date(end_date).getFullYear();
 var date11=year+"-"+(monthIndex+1)+"-"+day;
 req.body.ddd=new Date(date11);
req.body.date1=date1;
db.query("select TIMESTAMPDIFF(MINUTE,STR_TO_DATE('"+req.body.ddd+"','%W %M %d %Y %H:%i:%s'),STR_TO_DATE('"+req.body.date1+"','%W %M %d %Y %H:%i:%s')) as timediffrence from auction_pro t",function(err,results){
  if(err){
    console.log(err);
  }else{
    var diff=results[0].timediffrence
    var total_Min=1440;
    var difference=total_Min-diff;

    row.remaining_time=Math.abs(difference);
  }
})

              db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
                if(err){
                  throw err
                }
                else{
                var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;
result_array.push(row);
counter++;
console.log(row.image_cust)
if(counter== rows.length){


  console.log('Send response');
  res.send({
    "status":1,
    "message":constant.get_All_Aunc,
    "data":result_array
  });
}
                }

              })
    });
              
            } else{
              res.send({
                "status":0,
                "message":constant.NODATA
              })
            }
          }
            
        });
            }else{
             res.send({
              "status":0,
              "message":constant.NODATA
             }) 
            }
          }
        })

        

}
else if(user_pub_id  && !lat && !long && !distance && !cat_id && !max_price && !min_price ) {
	//console.log("5")
		var result_array = [];
	var counter = 0;
        // The Promise constructor should catch any errors thrown on
        // this tick. Alternately, try/catch and reject(err) on catch.
        db.query('SELECT p.*,c.currency_code,CONCAT("'+base_url+'",p.image) as image FROM `auction_pro` p JOIN currency_setting c WHERE c.status ="1" AND user_pub_id!=? AND p.status="0" ',[user_pub_id],function (err, rows){
        if (err) {
           res.send({
               	"status":0,
               	"message":err
               })
            }else{
                if(rows.length>0){
              var row=JSON.parse(JSON.stringify(rows))
              row.forEach(function(row) {
           		var end_date=row.e_date;
                      var date1=new Date();
                     // console.log(date1);
   var day = new Date(end_date).getDate();
   var monthIndex = new Date(end_date).getMonth();
   var year = new Date(end_date).getFullYear();
 var date11=year+"-"+(monthIndex+1)+"-"+day;
 req.body.ddd=new Date(date11);
req.body.date1=date1;
console.log(req.body.date1);
console.log(req.body.ddd)
db.query("select TIMESTAMPDIFF(MINUTE,STR_TO_DATE('"+req.body.ddd+"','%W %M %d %Y %H:%i:%s'),STR_TO_DATE('"+req.body.date1+"','%W %M %d %Y %H:%i:%s')) as timediffrence from auction_pro t",function(err,results){
  if(err){
    console.log(err);
  }else{
    console.log(results);
    var diff=results[0].timediffrence
    console.log(diff)
    var total_Min=1440;
    var difference=total_Min-diff;
    console.log(difference);
    row.remaining_time=Math.abs(difference);    
    
  }
})

              db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
           			if(err){
           				throw err
           			}
           			else{
           			var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;

result_array.push(row);
counter++;
console.log(row.image_cust)
if(counter== rows.length){


	console.log('Send response');
	res.send({
		"status":1,
		"message":constant.get_All_Aunc,
		"data":result_array
	});
}

           			}

           		})

		
		});
            	
            } else{
              res.send({
                "status":0,
                "message":constant.NODATA
              })
            }
          }
            
        });
}
else if(user_pub_id && cat_id && !lat && !long && !distance && max_price && min_price) {
	//console.log("user_pub_id && cat_id && lat && long && distance");
	var result_array = [];
	var counter = 0;
        // The Promise constructor should catch any errors thrown on
        // this tick. Alternately, try/catch and reject(err) on catch.

        db.query('SELECT p.*,c.currency_code, CONCAT("'+base_url+'", p.image) as image from auction_pro p JOIN currency_setting c  WHERE c.status="1" AND user_pub_id !=? AND cat_id=?  AND p.status="0" AND price BETWEEN '+min_price+' AND '+max_price+' ',[user_pub_id,cat_id] ,function (err, rows){

            // Call reject on error states,
            // call resolve with results
           // console.log(rows)
            if (err) {
                res.send({
               	"status":0,
               	"message":err
               })
            }else{
              console.log('6');
              if(rows.length>0){
            	var row=JSON.parse(JSON.stringify(rows))
           row.forEach(function(row) {
              var end_date=row.e_date;
                      var date1=new Date();
   var day = new Date(end_date).getDate();
   var monthIndex = new Date(end_date).getMonth();
   var year = new Date(end_date).getFullYear();
 var date11=year+"-"+(monthIndex+1)+"-"+day;
 req.body.ddd=new Date(date11);
req.body.date1=date1;
db.query("select TIMESTAMPDIFF(MINUTE,STR_TO_DATE('"+req.body.ddd+"','%W %M %d %Y %H:%i:%s'),STR_TO_DATE('"+req.body.date1+"','%W %M %d %Y %H:%i:%s')) as timediffrence from auction_pro t",function(err,results){
  if(err){
    console.log(err);
  }else{
    
        var diff=results[0].timediffrence
    var total_Min=1440;
    var difference=total_Min-diff;

    row.remaining_time=Math.abs(difference);    
  }
})

           		db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
           			if(err){
           				throw err
           			}
           			else{
           			var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;
result_array.push(row);
counter++;
console.log(row.image_cust)
if(counter== rows.length){
	res.send({
		"status":1,
		"message":constant.get_All_Aunc,
		"data":result_array
	});
}
           			}
           		})
		});
            	
            }
            else{
              res.send({
                "status":0,
                "message":constant.NODATA
              })
            }
          }
        });

}
else{
	res.send({
		"status":0,
		"message":constant.CHKAllFIELD
	})
}
})

module.exports=router;