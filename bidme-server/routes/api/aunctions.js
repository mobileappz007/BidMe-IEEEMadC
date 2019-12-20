	var express=require('express');
var uniqid = require('uniqid'); 
var bodyParser=require('body-parser')
var app=express();
var router=express.Router();
var cors=require('cors');
var path=require('path');
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true })); 
var constant=require('../../constants/constant.js')
var db=require('../../config/database.js');
var multer = require('multer');
var base_url =constant.base_url;

var storage = multer.diskStorage({ //multers disk storage settings
   destination: function (req, file, cb) {
     cb(null, '../../../../../var/www/html/subasta_assets/images')
    },
    filename: function(req, file, cb) {
        var datetimestamp = Date.now();
        cb(null, file.fieldname + '-' + datetimestamp + '.' + file.originalname.split('.')[file.originalname.split('.').length - 1]);
    }
});


var upload = multer({storage: storage});

router.post("/addImageAuction",upload.array('image',5), function(req, res) {
        if (!req.body.pro_pub_id && !req.body.user_pub_id) {
            res.json({
                status: 0,
                message:constant.CHKAllFIELD
            });
            return;
        } else {
        	var pro_pub_id=req.body.pro_pub_id;
        	var user_pub_id=req.body.user_pub_id;
    db.query("SELECT * FROM auction_pro WHERE user_pub_id=? AND pro_pub_id=?",[user_pub_id,pro_pub_id], function(err, results) {
                    if (err) {
                        res.send({
                            "status": 0,
                            "message": err
                        });
                    } 
                    else 
                    {
                    	var result=JSON.parse(JSON.stringify(results));
                    	if(result.length == 1)
                    	{
                    		var length=req.files.length;
                    		if(length>0)
                    		{
                    			for(i=0;i<length;i++)
		                    	{
		                    		console.log(req.files[i].filename);
			                    	req.body.image = "subasta_assets/images/"+req.files[i].filename
			                    	var data={
			                    	pro_pub_id:req.body.pro_pub_id,
			                    	image:req.body.image
			                    	}
		                        	db.query('INSERT INTO auction_pro_img SET ?',[data],function(err,results){
		           					});
		            			}

		            			res.send({
	                        		"status":1,
	                        		"message":constant.AddImage
	                        	})
                    		}
                    		else
                    		{
                    			res.send({
	                        		"status":0,
	                        		"message":constant.NoImage
	                        	})
                    		}
	                    	
                    	}
                    	else
                    	{
                    		res.send({
                        		"status":0,
                        		"message":constant.Incur
                        	})
                    	}
                    	//console.log(result)
                    	
}});
            }
   
});

// router.post("/addAuction" ,upload.single('image'), function(req,res){
// 	if (!req.body.title || !req.body.price || !req.body.address || !req.body.latitude|| !req.body.longitude|| !req.body.description||!req.body.s_date||!req.body.e_date||!req.body.cat_id||!req.body.sub_cat_id||!req.body.brand_id||!req.body.model_id || !req.body.user_pub_id) {
// 		res.send({
// 			"status":0,
// 			"message":"Check all fields"
// 		})
// 	}
// 	// if(!true){
// 	// 	return;
// 	// }
// 	else{
// req.body.image = "subasta_assets/images/" + req.file.filename
// 	var today=new Date();
// 	var data={
// 		"pro_pub_id":uniqid(),
// 		"user_pub_id":req.body.user_pub_id,
// 		"cat_id":req.body.cat_id,
// 		"sub_cat_id":req.body.sub_cat_id,
//    	 	"brand_id":req.body.brand_id,
//     	"model_id":req.body.model_id,
// 		"image":req.body.image,
// 		"title":req.body.title,
// 		"price":req.body.price,
// 		"address":req.body.address,
// 		"latitude":req.body.latitude,
// 		"longitude":req.body.longitude,
// 		"description":req.body.description,
// 		"s_date":req.body.s_date,
// 		"e_date":req.body.e_date,
// 		"no_of_owner":req.body.no_of_owner,
// 		"insured":req.body.insured,
// 		"created_at":today,
// 	}
// 	db.query('INSERT INTO auction_pro SET ?',data,function(err,results){
// 		if(!err){
// 			//console.log(results.insertId);
// 			var ids=results.insertId;
// 			//console.log(ids);
// db.query('SELECT * FROM auction_pro WHERE id=?',[ids],function(err,result){
// 	if(!err){
// 			var result=JSON.parse(JSON.stringify(result[0]));
			
// 		var data1={
// 		"pro_pub_id":result.pro_pub_id,
// 		"image":req.body.image,
// 		}

// 		db.query('INSERT INTO auction_pro_img SET ?',data1,function(err,results){
			
// 		})
// 	}
// })
// 				res.send({
// 				"status":1,
// 				"message":constant.addAunction
// 		});
// 		}
// 		else{

// 			res.send({
// 				"status":0,
// 				"message":err
// 			});
// 		}
// 	})
// }
// });

router.post("/addAuction" ,upload.array('image',5), function(req,res){
	if (!req.body.title || !req.body.price || !req.body.address || !req.body.latitude|| !req.body.longitude|| !req.files ||!req.body.s_date||!req.body.e_date||!req.body.cat_id||!req.body.sub_cat_id|| !req.body.user_pub_id) {
		res.send({
			"status":0,
			"message":constant.CHKAllFIELD
		})
	}
	// if(!true){
	// 	return;
	// }
	else{
req.body.image = "subasta_assets/images/" +req.files.filename
	var today=new Date();
	if(!req.body.brand_id||!req.body.model_id){
		console.log('balram')
		req.body.brand_id="",
		req.body.model_id=""
	}
	var data={
		pro_pub_id:uniqid(),
		user_pub_id:req.body.user_pub_id,
		cat_id:req.body.cat_id,
		sub_cat_id:req.body.sub_cat_id,
   	 	brand_id:req.body.brand_id,
    	model_id:req.body.model_id,
		title:req.body.title,
		price:req.body.price,
		address:req.body.address,
		latitude:req.body.latitude,
		longitude:req.body.longitude,
		sort_description:req.body.sort_description,	
		s_date:req.body.s_date,
		e_date:req.body.e_date,
		no_of_owner:req.body.no_of_owner,
		insured:req.body.insured,
		created_at:today,
	}
	db.query('INSERT INTO auction_pro SET ?',[data],function(err,results){
		if(!err){

			//console.log(results.insertId);
			var ids=results.insertId;
			console.log(ids);
db.query('SELECT * FROM auction_pro WHERE id=?',[ids],function(err,result){
	if(!err){
			var result=JSON.parse(JSON.stringify(result[0]));
			console.log(result.pro_pub_id);
			var length=req.files.length;
		
			var id=result.pro_pub_id;
	if(length>0)
                    		{
                    			for(i=0;i<length;i++)
		                    	{
		                    		console.log(req.files[i].filename);
			                    	req.body.image= "subasta_assets/images/"+req.files[i].filename
			                    	var data={
			                    	pro_pub_id:id,
			                    	image:req.body.image
			                    	}
		                        	db.query('INSERT INTO auction_pro_img SET ?',[data],function(err,results){
		                        		if(err){
		                        			console.log(err);
		                        		}
		           					});
		            			}
	}
}
})
				res.send({
				"status":1,
				"message":constant.addAunction
		});
		}
		else{

			res.send({
				"status":0,
				"message":err
			});
		}
	})
}
});

router.post('/getSingleAuction',function(req,res){
	var pro_pub_id=req.body.pro_pub_id;
	db.query('SELECT p.*,ca.cat_title,COALESCE(b.brand_name,"") as brand_name, COALESCE(m.model_name,"") as model_name,c.currency_code FROM ((((auction_pro p JOIN currency_setting c ) LEFT JOIN brand b ON b.brand_id=p.brand_id) LEFT JOIN model m ON m.model_id=p.model_id)JOIN auction_cat ca ON ca.cat_id=p.cat_id ) WHERE c.status="1" AND p.pro_pub_id="'+pro_pub_id+'" ;SELECT b.user_pub_id,b.bid_pub_id,b.pro_pub_id,b.price,b.status,b.isWin,b.created_at,u.name,u.image ,c.currency_code FROM ((bid_information b INNER JOIN users u ON b.user_pub_id=u.user_pub_id) JOIN currency_setting c) WHERE c.status="1" AND b.pro_pub_id="'+pro_pub_id+'"  ;SELECT *, CONCAT("'+base_url+'",image)as image from auction_pro_img WHERE pro_pub_id ="'+pro_pub_id+'"',function(err,results,fields){
		if(err){
			//console.log('err',err);
			res.send({
				"status":0,
				"message":err
			})
		}else{
			console.log(results);
			var result=JSON.parse(JSON.stringify(results[0]));
			
			if(result!=0){
				//console.log(result)
				var uuu=result[0].user_pub_id
				console.log(uuu);
				var user_pub_id=req.body.user_pub_id;
				var pro_pub_id=result[0].pro_pub_id;

db.query('SELECT *,CONCAT("'+base_url+'", image) as image from users WHERE user_pub_id="'+uuu+'";SELECT isFavourite FROM favourite WHERE user_pub_id ="'+user_pub_id+'" AND pro_pub_id="'+pro_pub_id+'";SELECT star FROM rating WHERE user_pub_id ="'+user_pub_id+'" AND pro_pub_id="'+pro_pub_id+'";SELECT COALESCE(ROUND(AVG(star),2),0)as Allrating from rating r WHERE pro_pub_id="'+pro_pub_id+'";select * from auction_pro where pro_pub_id="'+pro_pub_id+'"',function(err,responces){
if(err){ 
	res.send({
		"status":0,
		"message":err
	})
}else{		

		var responce=JSON.parse(JSON.stringify(responces[0]));
			
			var result1=JSON.parse(JSON.stringify(results[1])); 
			for(i=0;i<result1.length;i++){
				result1[i].image = base_url + result1[i].image;
				}

		var favourite=JSON.parse(JSON.stringify(responces[1]));
		var rating=JSON.parse(JSON.stringify(responces[2]));
		var responce1=JSON.parse(JSON.stringify(responces[3]));	
		// console.log()
				var row=responces[4];
			           		var end_date=row[0].e_date;
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
db.query("select TIMESTAMPDIFF(MINUTE,STR_TO_DATE('"+req.body.ddd+"','%W %M %d %Y %H:%i:%s'),STR_TO_DATE('"+req.body.date1+"','%W %M %d %Y %H:%i:%s')) as timediffrence from auction_pro t",function(err,timediffrence){
  if(err){
    console.log(err);
  }else{
    console.log(timediffrence);
    var diff=timediffrence[0].timediffrence
    console.log(diff)
    var total_Min=1440;
    var difference=total_Min-diff;
    console.log(difference);
//    row.remaining_time=Math.abs(difference);    

   // console.log(difference);
    result[0].remaining_time=Math.abs(difference);
    console.log(result[0].remaining_time)
		var gallery=JSON.parse(JSON.stringify(results[2]));
			result[0].users=responce[0];
			result[0].bids = result1;
			result[0].gallery = gallery;
			result[0].image = base_url + result[0].image;
			result[0].rating=rating;
			result[0].Allrating=responce1[0].Allrating;
			result[0].gallery_count = gallery.length;
			if(favourite.length==0){
		result[0].favourite=0;
		}	
else{
				result[0].favourite=favourite[0].isFavourite;
	}
	if(rating.length==0){
		result[0].rating=0.0;
		}	
else{
				result[0].rating=rating[0].star;
	}	
				// console.log(result[0].remaining_time);
			res.send({
					"status":1,
					"message":constant.get_All_Aunc,
					"data":result[0]
				})
}
})
  }
})

}else{
				res.send({
					"status":0,
					"message":constant.NOT_FOUND
				})
			}
		}
	})
})

// router.post('/getMyAuction',function(req,res){	
// 	var id=req.body.user_pub_id;
// 	if(!req.body.user_pub_id){
// 			res.send({
// 				"status":0,
// 				"message":constant.CHKAllFIELD
// 			})
// 	}else{	
// 			db.query('SELECT p.*,COALESCE(b.brand_name,"") as brand_name, COALESCE(m.model_name,"") as model_name ,CONCAT("http://52.77.32.209/",p.image) as image ,c.currency_code from (((auction_pro p INNER JOIN currency_setting c) LEFT JOIN brand b ON p.brand_id=b.brand_id) LEFT JOIN model m ON p.model_id=m.model_id) WHERE p.user_pub_id="'+id+'" AND c.status="1" ',[id],function(err,results){
// 				if(err){
// 				res.send({
// 					"status":0,
// 					"message":err
// 				})
// 				}else{
// 					var results=JSON.parse(JSON.stringify(results));
// 					if(results.length>0){
// 						res.send({
// 							"status":1,
// 							"mesage":constant.get_All_Aunc,
// 							"data":results
// 						})
// 					}else{
// 							res.send({
// 								"status":0,
// 								"message":constant.NOT_FOUND
// 							})
// 						}
// 		}
// 	})
// }
// })	
// router.post('/getMyAuction',function(req,res){	
// 	var id=req.body.user_pub_id;
// 	if(!req.body.user_pub_id){
// 			res.send({
// 				"status":0,
// 				"message":"check all fields"
// 			})
// 	}else{
// 			db.query('SELECT p.*,COALESCE(b.brand_name,"") as brand_name, COALESCE(m.model_name,"") as model_name , CONCAT("http://52.77.32.209/",p.image) as image ,c.currency_code from (((auction_pro p INNER JOIN currency_setting c) LEFT JOIN brand b ON p.brand_id=b.brand_id) LEFT JOIN model m ON p.model_id=m.model_id) WHERE p.user_pub_id="'+id+'" AND c.status="1";',[id],function(err,results){
// 				if(err){
// 					res.send({
// 						"status":0,
// 						"message":err
// 					})
// 				}else{
// 						var result=JSON.parse(JSON.stringify(results));
					
// 						var length=result.length;
// 						console.log(length);
// 					if(length>0)
// 		                    {
// 								var results=''
// 		    					for(i=0;i<length;i++)
// 		                		{
// 									var id=result[i].pro_pub_id;
// 		            				console.log(id)
// 		            				db.query('SELECT * from auction_pro_img WHERE pro_pub_id=?',[id],function(err,responce){
// 		            				console.log('ssss')
// 		            				var respon=JSON.parse(JSON.stringify(responce));
// 		            			//console.log(responce)
// 		            				console.log(respon)
// 		            				})
// 		            			}
// 		            			res.send({
// 		            				"status":1,
// 		            				"message":"get_All_Aunc",
// 		            				"data":result
// 		            			})
// 							}else{
// 									res.send({
// 										"status":0,
// 										"message":constant.NOT_FOUND
// 									})
// 								}		                    			
// 			        }	
// 					})
// }
//})
// function getMyAuction(){
// 	var id=req.body.user_pub_id;
// 	if(!req.body.user_pub_id){
// 			res.send({
// 				"status":0,
// 				"message":"check all fields"
// 			})
// 	}else{
// 		var id=req.body.user_pub_id;
// 		db.query( 'SELECT * FROM auction_pro WHERE user_pub_id="'+id+'" ')
//         .then(function(rows){
//     // Query the items for a ring that Frodo owns.
//     var result = db.query('select * from auction_pro_img where `pro_pub_id`="' + rows[0].pro_pub_id + '"');
//     connection.end();
//     return result;
// }).then(function(rows){
//     // Logs out a ring that Frodo owns
//     console.log(rows);
// })
// }

// }


// router.post('/getAllAuctions',function(req,res){
// 	var user_pub_id=req.body.user_pub_id;
// 	var cat_id=req.body.cat_id;
// 	var lat=req.body.latitude;
// 	var long=req.body.longitude;
// 	var max_price=req.body.max_price;
// 	var min_price=req.body.min_price;
// 	var distance=req.body.distance;

// //console.log(req.body)
// 	if(!user_pub_id & !cat_id){
// 		res.send({
// 			"status":0,
// 			"message":constant.CHKAllFIELD
// 		})
// 	}
// 	else if(user_pub_id && cat_id && lat && long&& max_price && min_price && distance){
// 		//console.log(min_price);
// 	db.query('SELECT p.*,c.currency_code, CONCAT("http://52.77.32.209/", p.image) as image,( 3959* acos( cos( radians("'+lat+'") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("'+long+'") ) + sin( radians("'+lat+'") ) * sin( radians( latitude ) ) ) ) AS distance  FROM `auction_pro` p JOIN currency_setting c WHERE c.status="1" AND user_pub_id !=? AND cat_id=? AND price BETWEEN '+min_price+' AND '+max_price+' HAVING distance <= "'+distance+'"',[user_pub_id,cat_id],function(err,results,fields){
// 		if(err){
// 			//console.log('err',err);
// 			res.send({
// 				"status":0,
// 				"message":err
// 			})
// 		}else{
// 			var result=JSON.parse(JSON.stringify(results))
// 			if(result.length!=0){
// 				res.send({
// 					"status":1,
// 					"message":constant.get_All_Aunc,
// 					"data":result
// 				})
// 			}else{
// 				res.send({
// 						"status":0,
// 					"message":constant.NOT_FOUND
// 				})
// 			}
// 		}
// 	})
// }
//  else if(user_pub_id &&cat_id && long && lat && distance && !max_price && !min_price){
//  	//console.log('3')
//  	db.query('SELECT p.*,c.currency_code,CONCAT("http://52.77.32.209/",p.image) as image,( 3959* acos( cos( radians("'+lat+'") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("'+long+'") ) + sin( radians("'+lat+'") ) * sin( radians( latitude ) ) ) ) AS distance FROM `auction_pro` p JOIN currency_setting c WHERE c.status ="1" AND user_pub_id!=? AND cat_id=? HAVING distance <= "'+distance+'" ',[user_pub_id,cat_id],function(err,results,fields){
// 		if(err){
// 			//console.log('err',err);
// 			res.send({
// 				"status":0,
// 				"message":err
// 			})
// 		}else{
// 			var result=JSON.parse(JSON.stringify(results))
// 			//console.log(result);
			
// 			if(result.length>0){
// 				res.send({
// 					"status":1,
// 					"message":constant.get_All_Aunc,
// 					"data":result
// 				})
// 			}else{
// 				res.send({
// 					"status":0,
// 					"message":constant.NOT_FOUND
// 				})
// 			}
// 		}
// 	})
// }
// else if(user_pub_id && cat_id && !lat && !long && !max_price && !min_price && !distance) {
// 	//console.log("4")
// 	db.query('SELECT p.*,c.currency_code,CONCAT("http://52.77.32.209/",p.image) as image FROM `auction_pro` p JOIN currency_setting c WHERE c.status="1" AND user_pub_id!=? AND cat_id=? ',[user_pub_id,cat_id] ,function(err,results,fields){
// 		if(err){
// 			//console.log('err',err);
// 			res.send({
// 				"status":0,
// 				"message":err
// 			})
// 		}else{
// 			var result=JSON.parse(JSON.stringify(results))
// 			//console.log(result);
			
// 			if(result.length>0){
// 				res.send({
// 					"status":1,
// 					"message":constant.get_All_Aunc,
// 					"data":result
// 				})
// 			}else{
// 				res.send({
// 					"status":0,
// 					"message":constant.NOT_FOUND
// 				})
// 			}
// 		}
// 	})
// }
// else if(user_pub_id  && !lat && !long && !distance && !cat_id && !max_price && !min_price ) {
// 	//console.log("5")
// 	db.query('SELECT p.*,c.currency_code,CONCAT("http://52.77.32.209/",p.image) as image FROM `auction_pro` p JOIN currency_setting c WHERE c.status ="1" AND user_pub_id!=? ',[user_pub_id] ,function(err,results,fields){
// 		if(err){
// 			//console.log('err',err);
// 			res.send({
// 				"status":0,
// 				"message":err
// 			})
// 		}else{
// 			var result=JSON.parse(JSON.stringify(results))
// 			//console.log(result);
			
// 			if(result.length>0){
// 				res.send({
// 					"status":1,
// 					"message":constant.get_All_Aunc,
// 					"data":result
// 				})
// 			}else{
// 				res.send({
// 					"status":0,
// 					"message":constant.NOT_FOUND
// 				})
// 			}
// 		}
// 	})
// }
// else if(user_pub_id && cat_id && !lat && !long && !distance && max_price && min_price) {
// 	//console.log("user_pub_id && cat_id && lat && long && distance");
// 	db.query('SELECT p.*,c.currency_code, CONCAT("http://52.77.32.209/", p.image) as image from auction_pro p JOIN currency_setting c  WHERE c.status="1" AND user_pub_id !=? AND cat_id=? AND price BETWEEN '+min_price+' AND '+max_price+' ',[user_pub_id,cat_id] ,function(err,results,fields){
// 		if(err){
// 			//console.log('err',err);
// 			res.send({
// 				"status":0,
// 				"message":err
// 			})
// 		}else{
// 			var result=JSON.parse(JSON.stringify(results))
// 			//console.log(result);
			
// 			if(result.length>0){
// 				res.send({
// 					"status":1,
// 					"message":constant.get_All_Aunc,
// 					"data":result
// 				})
// 			}else{
// 				res.send({
// 					"status":1,
// 					"message":constant.NOT_FOUND
// 				})
// 			}
// 		}
// 	})
// }
// else{
// 	res.send({
// 		"status":0,
// 		"message":constant.CHKAllFIELD
// 	})
// }
// })

	
app.get("/edit_auction/:pro_pub_id", function(req,res){
    var id = req.params.pro_pub_id;
    //console.log(id);
   db.query("SELECT * FROM auction_pro WHERE pro_pub_id=?",[id], function(err,result){
  
         if(err)
         {
          //console.log("Finding error",err);
           return; 
         }
         result= JSON.parse(JSON.stringify(result[0]));
          //console.log(result);

          
         res.render("edit_auction", {result:result});
        });
 });


/* url of update user */
// router.post("/update_auction", upload.single('image'),function(req,res){
// 	if(!req.body.user_pub_id||!req.body.pro_pub_id ){
// 		res.send({
// 			"status":0,
// 			"message":"check all fields"
// 		})
// 	}
	
//             var today = new Date();
//              var user_id = req.body.user_pub_id;
//                 delete req.body.user_pub_id;
//                  var pro_id = req.body.pro_pub_id;
//                 delete req.body.pro_pub_id;
//  //   var sql = "UPDATE auction_pro set title =? ,brand_id=?, model_id=? ,price=?, cat_id=?,sub_cat_id=?,e_date=?,s_date=?,no_of_owner=?,insured=?,address =?,description=?,updated_at =? WHERE pro_pub_id = ? AND user_pub_id=?";
 // var data={
 // 	title :req.body.title ,
 // 	brand_id:req.body.brand_id, 
 // 	model_id:req.body.model_id ,
 // 	price:req.body.price, 
 // 	cat_id:req.body.cat_id,
 // 	sub_cat_id:req.body.sub_cat_id,
 // 	e_date:req.body.e_date,
 // 	s_date:req.body.s_date,
 // 	no_of_owner:req.body.no_of_owner,
 // 	insured:req.body.insured,
 // 	address :req.body.address,
 // 	description:req.body.description,
 // 	updated_at:today
 // }
//    // var query = db.query(sql, [req.body.title,req.body.brand_id, req.body.model_name,req.body.price,req.body.cat_id,req.body.sub_cat_id,req.body.e_date,req.body.s_date,req.body.no_of_owner,req.body.insured,req.body.address,req.body.description,today,pro_id,user_id], function(err, result) {
//   db.query('UPDATE auction_pro SET ? WHERE pro_pub_id = "'+pro_id +'" AND user_pub_id="'+user_id+'"',[data],function(err,result){
//      if(err)
//   {
//     //req.flash("msg", "Error In User update");
//     res.send({
//     	"status":0,
//     	"message":err
//     });
//   }
//   else
//   {

//     //req.flash("msg", "Successfully User UPDATE");
//  	res.send({
//  		"status":1,
//  		"message":constant.updateAuc
//  	})
//   }
     
// });
// });

// app.post("/updateProfile", function(req, res) {
//     upload_image(req, res, function(err) {
//         if (err, !req.files, !req.body.user_pub_id) {
//             res.json({
//                 status: 0,
//                 message: constant.CHKAllFIELD
//             });
//             return;
//         } else {

//             // console.log(req.files);
//             if (!req.files) {
//                 var user_id = req.body.user_pub_id;
//                 delete req.body.user_pub_id;
//                 cm.update('user', {
//                     pub_id: user_id
//                 }, req.body, function(err, result) {
//                     if (err) {
//                         console.log(err);
//                     } else {
//                         cm.getallDataWhere('user', {
//                             pub_id: user_id
//                         }, function(err, result) {
//                             if (err) {
//                                 console.log(err);
//                             } else {
//                                 //console.log('PROFILE IMAGE::::'+JSON.stringify(result));
//                                 // var profile_image_url = result[0].profile_image.split('./');

//                                 result[0].profile_image = base_url + result[0].profile_image;

//                                 // var data = JSON.parse(JSON.stringify(result[0]));
//                                 res.send({
//                                     "status": 1,
//                                     "message": constant.PROFILE_UPDATED,
//                                     "data": result[0]
//                                 });
//                             }

//                         });
//                     }

//                 })
//             } else {
//                 req.body.profile_image = "assets/images/profile/" + req.files[0].filename
//                 console.log(JSON.stringify(req.body));

//                 var user_id = req.body.user_pub_id;
//                 delete req.body.user_pub_id;
//                 cm.update('user', {
//                     pub_id: user_id
//                 }, req.body, function(err, result) {
//                     if (err) {
//                         console.log(err);
//                     } else {
//                          cm.getallDataWhere('user', {
//                             pub_id: user_id
//                         }, function(err, result) {
//                             if (err) {
//                                 console.log(err);
//                             } else {
//                                 //console.log('PROFILE IMAGE::::'+JSON.stringify(result));

//                                 // var profile_image_url = result[0].profile_image.split('./')

//                                 result[0].profile_image = base_url + result[0].profile_image;
//                                 // var data = JSON.parse(JSON.stringify(result[0]));
//                                 res.send({
//                                     "status": 1,
//                                     "message": constant.PROFILE_UPDATED,
//                                     "data": result[0]
//                                 });
//                             }

//                         });
//                     }

//                 })
//             }
//         }
//     });
// });
var cm=require('../../model/common_model.js')
router.post("/update_auction",upload.single('image'), function(req, res) {
     
        if (!req.body.user_pub_id||!req.body.pro_pub_id) {
            res.json({
                status: 0,
                message: constant.chkFields
            });
            return;
        } else {
          var user_pub_id=req.body.user_pub_id;
          console.log(req.body);
          var pro_pub_id=req.body.pro_pub_id;
   db.query('SELECT * FROM auction_pro WHERE pro_pub_id=?', [pro_pub_id], function(err, result) {
                    if (err) {
                        res.send({
                            "status": 0,
                            "message": err
                        });
                    } else {
                        if (result.length == 0) {
                            res.json({
                                status: 0,
                                message: constant.USER_NOT_FOUND
                            });
                        } else {
                          console.log(result)
         if (!req.body.title && !req.body.brand_id && !req.body.model_id 
          && !req.body.price && !req.body.cat_id && !req.body.sub_cat_id 
          && !req.body.e_date &&!req.body.no_of_owner &&!req.body.insured &&!req.body.address && !req.body.description) 
         {
            res.json({
                      status: 0,
                      message: constant.NO_VALUE_SEND
                  });
         }else{
         	var result =JSON.parse(JSON.stringify(result));
         	var end_date=result[0].e_date;
         	datee=new Date();
         	 var month = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
"Jul", "Aug", "Sept", "Oct", "Nov", "Dec"][datee.getMonth()];
         	  console.log(datee);
         	  var s='0'+datee.getDate()+'-'+month+'-'+datee.getFullYear();
         	console.log(end_date);
         	console.log(s);
         	if(end_date<s){
          if(req.file) { 
           req.body.image ="subasta_assets/images/" + req.file.filename
                console.log(JSON.stringify(req.body));
            console.log('vakraam')
           var today = new Date();
                    var user_id = req.body.user_pub_id;
                delete req.body.user_pub_id;
                    var pro_id = req.body.pro_pub_id;
                delete req.body.pro_pub_id;
            	cm.update('auction_pro', {
                    user_pub_id: user_id,
                    pro_pub_id:pro_id
                }, req.body, function(err, result1) {
                //db.query('UPDATE auction_pro SET ? WHERE user_pub_id="'+user_id+'" AND pro_pub_id="'+pro_id+'"',[data],function(err, result1) {
                    if (err) {
                        console.log(err);
                    } else {
                      var result1=JSON.parse(JSON.stringify(result1));
                      console.log(result1)
                         db.query('SELECT * FROM auction_pro WHERE pro_pub_id=?',[pro_id], function(err, result) {
                            if (err) {
                                console.log(err);
                            } else {
                              // console.log(result)
                                var data = JSON.parse(JSON.stringify(result));
                              console.log(data)
                                data[0].image = base_url + data[0].image;
                                console.log('balram1')
                                res.send({
                                    "status": 1,
                                    "message": constant.updateAuc,
                                    "data": data[0]
                                });
                            }

                        });
                    }

                })

          }else{
          	        var today = new Date();
                    var user_id = req.body.user_pub_id;
                delete req.body.user_pub_id;
                      var pro_id = req.body.pro_pub_id;
                delete req.body.pro_pub_id;
                cm.update('auction_pro', {
                    user_pub_id: user_id,
                    pro_pub_id:pro_id
                }, req.body, function(err, result1) {
                //db.query('UPDATE auction_pro SET ? WHERE pro_pub_id="'+pro_id+'" AND user_pub_id="'+user_id+'"',[data],function(err, result1) {
                    if (err) {
                        console.log(err);
                    } else {
                    	console.log('balram111111')
                      var result1=JSON.parse(JSON.stringify(result1));
                      console.log(result1)
                         db.query('SELECT * FROM auction_pro WHERE pro_pub_id=?',[pro_id], function(err, result) {
                            if (err) {
                                console.log(err);
                            } else {
                              // console.log(result)
                                var data = JSON.parse(JSON.stringify(result));
                              console.log(data)
                                data[0].image = base_url + data[0].image;
                                console.log('balram1')
                                res.send({
                                    "status": 1,
                                    "message": constant.updateAuc,
                                    "data": data[0]
                                });
                            }

                        });
                    }

                })
          }
           
           }else{
           	 	res.send({
           	 		"status":0,
           	 		"message":"You can't update your auction,because its running mode."
           	 	})
           }
              }
                }
}});
            }
   
});

router.post("/delete_auction",function(req,res){
console.log(req.body);
 if(!req.body.pro_pub_id || !req.body.user_pub_id){
 	res.send({
 		"status":0,
 		"message":constant.CHKAllFIELD
 	})
 }else{
  var pro_pub_id = req.body.pro_pub_id;
  var user_pub_id=req.body.user_pub_id;

  // var sql = "DELETE FROM auction_pro WHERE pro_pub_id = ? and user_pub_id=?";
  var data={
  	"status":2
  }
 db.query('UPDATE auction_pro SET ? WHERE pro_pub_id="'+pro_pub_id+'" AND user_pub_id="'+user_pub_id+'"',[data],function(err, result){
  // db.query(sql, [pro_pub_id,user_pub_id], function(err, result) {
    if (err)
  {
    //req.flash("msg", "Error In User delete");
   console.log(err);
   res.send({
   	"status": 0,
   	"message":err
   })
  }
  else
  {	

  	

  	// db.query('DELETE FROM auction_pro_img WHERE pro_pub_id = ?',[id],function(err,ressss){
  	// 	console.log('ssss');
  	// })
    //req.flash("msg", "Successfully User Deleted");

if(result.affectedRows>0){
	console.log('bbb');
// 	db.query('DELETE FROM auction_pro_img WHERE pro_pub_id = "'+req.body.pro_pub_id+'"',function(err,results){
// if(err){
// 	res.send({
// 		"status":0,
// 		"message":err
// 	})
// }else{
// console.log(result);
// 	if(results.affectedRows>0){
// 		res.send({
// 	"status":1,
// 	"message":constant.deleteAuc
// })
// 	}else{
// 		res.send({
// 			"status":0,
// 			"message":constant.deleteAuc +"but image is not found."
// 		})
// 	}
// }
// 	})
	res.send({
		"status":1,
		"message":constant.deleteAuc
	})
  }else{
  	res.send({
  		"status":0,
  		"message":"Auction is not found."
  	})
  }
}
});
}
  });

router.post('/remove_image',function(req,res){
	var pro_pub_id=req.body.pro_pub_id;
	var user_pub_id=req.body.user_pub_id;
	var id=req.body.id;
	db.query('DELETE FROM auction_pro_img WHERE pro_pub_id = "'+req.body.pro_pub_id+'" AND id="'+req.body.id+'"',function(err,result){
		if(err){
			res.send({
				"status":0,
				"message":err
			})
		}else{
			res.send({
				"status":1,
				"message":"Delete image.",
			})
		}
	})
})
// router('/uploadMultiImage',function(req,res,next){
//  let formidable=require('formidable');
//  var form=new formidable.IncomingForm();
//  form.uploadDir='../../../../../var/www/html/subasta_assets/images';
//  form.keepExtensions=true;
//  form.maxFieldsSize=10*1024*1024;
//  form.multiples=true;
//  form.parse(req,function(err,fields,files){
//  	if(err){
//  		res.send({
//  			"status":0,
//  			"message":err
//  		})
//  	}
//  	var arrayOfFiles=files[""];
//  	if(arrayOfFiles.length>0){
//  		var fileName=[];
//  		arrayOfFiles.forEach((eachFile)=>{
//  //		db.query('INSERT INTO auction_pro_img SET image=? WHERE pro_pub_id=?',[eachFile.path,id]);
// 		filename.push(eachFile.path.split('/')[1]);
//  		});
//  		res.send({
//  			"status":1,
//  			"message":"uploaded"
//  		})

//  	}else{
//  		res.send({
//  			"status":0,
//  			"message":"nofilesend"
//  		})
//  	}
//  })
// })
module.exports=router;