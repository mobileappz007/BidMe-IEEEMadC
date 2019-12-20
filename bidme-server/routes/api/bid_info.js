var express=require('express');
var db=require('../../config/database.js');
var constant=require('../../constants/constant.js');
var uniqid = require('uniqid'); 
var base_url=constant.base_url;
var router=express.Router();

var cm=require('../../model/common_model.js')
router.post('/addBid',function(req,res){
if(!req.body.pro_pub_id ||!req.body.user_pub_id||!req.body.price){
res.send({
	"status":0,
	"message":constant.CHKFIELD
})
}
else{
	db.query('SELECT * from block_user_bid where pro_pub_id="'+req.body.pro_pub_id+'" and block_user_pub_id="'+req.body.user_pub_id+'" and status="1"',function(err,results){
		if(err){
			res.send({
				"status":0,
				"message":err
			})
		}else{
			var result=JSON.parse(JSON.stringify(results));
			if(result.length>0){
				res.send({
					"status":0,
					"message":"You cann't add bid because, you have been blocked."
				})
			}else{
	db.query('SELECT * from bid_information WHERE pro_pub_id="'+req.body.pro_pub_id+'" AND user_pub_id="'+req.body.user_pub_id+'"',function(err,results){
		if(err){

		}else{
			var result=JSON.parse(JSON.stringify(results))
		
				db.query('SELECT * from auction_pro WHERE pro_pub_id="'+req.body.pro_pub_id+'" ',function(err,response){
			//var response=JSON.parse(JSON.stringify(response))
			// console.log(response[0].price);
			if(response.length>0){
				console.log(req.body.price)
			if(response[0].price<req.body.price){
	var today=new Date();
	var data={
		"bid_pub_id":uniqid(),
		"pro_pub_id":req.body.pro_pub_id,
		"user_pub_id":req.body.user_pub_id,
		"price":  req.body.price,
		"created_at":today,
		"updated_at":today
	}
	db.query('INSERT INTO bid_information SET ?',data,function(err,results){
		if(!err){
			res.send({
				"status":1,
				"message":constant.AddBid
			})
				db.query('SELECT * FROM auction_pro WHERE pro_pub_id="'+req.body.pro_pub_id+'"',function(err,result){
					if(!err){
						var result=JSON.parse(JSON.stringify(result));
						console.log(result);
						var user_id=result[0].user_pub_id;
						console.log(user_id);
							db.query('SELECT * FROM users WHERE user_pub_id="'+user_id+'"',function(err, result) {
                            if (err) {
                                console.log(err);
                            } else {
                       
                                var receiver_data = JSON.parse(JSON.stringify(result));
                                var receiver_token = receiver_data[0].device_token
                               
                               db.query('SELECT * FROM users WHERE user_pub_id="'+req.body.user_pub_id+'"', function(err, result) {
                                    if (err) {
                                        console.log(err);
                                    } else {
                                       	var user_data = JSON.parse(JSON.stringify(result));
                                        var user_name = user_data[0].name;
                                       	var type=constant.BID_TYPE;
                                       	var msg=constant.addbidmessage;
                                   		var today=new Date();
                                   	var data={
                                   		notication_id:uniqid(),
                                   		user_pub_id:user_id,
                                   		pro_pub_id:req.body.pro_pub_id,
                                   		tittle:user_name,
                                   		message:msg,
                                   		type:type,
                                   		created_at:today,
                                   		updated_at:today
                                   	}
                                       	db.query('INSERT INTO notification SET ?',[data],function(err,result){
                                   	
	 		if(err){
	 			res.send({
	 				"status":0,
	 				"message":err
	 			})
	 		}else{
	 				
	 				cm.pushnotification1(user_name,msg,receiver_token,type,req.body.pro_pub_id);
	 		db.query('update auction_pro set price="'+req.body.price+'" where pro_pub_id="'+req.body.pro_pub_id+'"' ,function(err,result){
if(err){
	console.log(err);
}else{
		
}
                     })
	 		}
	 	})
                                    }
                                });
                            }
                        })
					}
				})
			
		}else{
			res.send({
				"status":0,
				"message":err
			})
		}
	})
}else{
	res.send({
		"status":0,
		"message":"Your bid should be greater than before bid."
	})
}
}else{
	var today=new Date();
	var data={
		"bid_pub_id":uniqid(),
		"pro_pub_id":req.body.pro_pub_id,
		"user_pub_id":req.body.user_pub_id,
		"price":  req.body.price,
		"created_at":today,
		"updated_at":today
	}
	db.query('INSERT INTO bid_information SET ?',data,function(err,results){
		if(!err){
			res.send({
				"status":1,
				"message":constant.AddBid
			})
				db.query('SELECT * FROM auction_pro WHERE pro_pub_id="'+req.body.pro_pub_id+'"',function(err,result){
					if(!err){
						var result=JSON.parse(JSON.stringify(result));
						console.log(result);
						var user_id=result[0].user_pub_id;
						console.log(user_id);
							db.query('SELECT * FROM users WHERE user_pub_id="'+user_id+'"',function(err, result) {
                            if (err) {
                                console.log(err);
                            } else {
                            	
                                var receiver_data = JSON.parse(JSON.stringify(result));
                                var receiver_token = receiver_data[0].device_token
                               
                               db.query('SELECT * FROM users WHERE user_pub_id="'+req.body.user_pub_id+'"', function(err, result) {
                                    if (err) {
                                        console.log(err);
                                    } else {
                                       var user_data = JSON.parse(JSON.stringify(result));
                                        var user_name = user_data[0].name;
                                       	var type=constant.BID_TYPE;
                                       	var msg=constant.addbidmessage;
                                   		var today=new Date();
                                   	var data={
                                   		notication_id:uniqid(),
                                   		user_pub_id:user_id,
                                   		pro_pub_id:req.body.pro_pub_id,
                                   		tittle:user_name,
                                   		message:msg,
                                   		type:type,
                                   		created_at:today,
                                   		updated_at:today
                                   	}

        
                                	db.query( 'INSERT INTO notification SET ?',[data],function(err,result){
                                   	
	 		if(err){
	 			res.send({
	 				"status":0,
	 				"message":err
	 			})
	 		}else{
	 				
	 				cm.pushnotification1(user_name,msg,receiver_token,type,req.body.pro_pub_id);
	 				db.query('update auction_pro set price="'+req.body.price+'" where pro_pub_id="'+req.body.pro_pub_id+'" ' ,function(err,result){
if(err){
	console.log(err);
}else{
		
}
                     })

	 		}
	 	})
                                    }
                                });
                            }
                        })
					}
				})
			
		}else{
			res.send({
				"status":0,
				"message":err
			})
		}
	})

}
 })

		// }
	}
})
			}
		}
	})
}

});

router.post('/getAllBid',function(req,res){
var pro_pub_id=req.body.pro_pub_id;
 var user_pub_id=req.body.user_pub_id;

	db.query('SELECT p.title ,p.price as auction_price,p.pro_pub_id,b.bid_pub_id,b.price,b.bid_pub_id,b.user_pub_id,b.created_at,b.updated_at  From bid_information b INNER JOIN auction_pro p ON p.pro_pub_id=b.pro_pub_id WHERE b.user_pub_id != "'+user_pub_id+'" AND b.pro_pub_id="'+pro_pub_id+'" and b.status="1"',function(err,results,fields){
		if(err){
			console.log('err',err);
			res.send({
				"status":0,
				"message":err
			})
		}else{
			var result=JSON.parse(JSON.stringify(results))
			
			
			if(result!=0){
				res.send({
					"status":1,
					"message":constant.get_All_Bid,
					"data":results
				})
			}else{
				res.send({
					"status":0,
					"message":constant.NOT_FOUND
				})
			}
		}
	})
});


router.post('/getMyBid',function(req,res){
	var id=req.body.pro_pub_id;
	var user_pub_id=req.body.user_pub_id;
	var result_array = [];
	var counter = 0;
	db.query('SELECT b.* ,p.price as auction_price,p.title,c.currency_code, CONCAT("'+base_url+'",p.image) as image FROM ((bid_information b INNER JOIN auction_pro p ON p.pro_pub_id=b.pro_pub_id ) JOIN currency_setting c) where c.status="1" AND b.user_pub_id=?',[user_pub_id],function(err,rows,fields){
		if(err){
			console.log('err',err);
			res.send({
				"status":0,
				"message":err
			})
		}else{
			var row=JSON.parse(JSON.stringify(rows))
			console.log(row);
			
			if(row!=0){
          row.forEach(function(row) {
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
					"message":constant.get_All_Bid,
					"data":result_array
				})
			}
		}
	})
});
}else{
				res.send({
					"status":0,
					"message":constant.NOT_FOUND
				})
			}
}
})
})
router.post('/update_bid',function(req,res){
	var today=new Date();      
db.query('SELECT * from bid_information WHERE pro_pub_id="'+req.body.pro_pub_id+'" order by created_at DESC LIMIT 1',function(err,response){
			//var response=JSON.parse(JSON.stringify(response))
			// console.log(response[0].price);
			if(response.length>0){
				console.log(req.body.price)
			if(response[0].price<req.body.price){

   var sql = "UPDATE bid_information set price =? ,updated_at=? WHERE pro_pub_id = ? AND user_pub_id=? AND bid_pub_id=? ";
 
    var query = db.query(sql, [req.body.price,today,req.body.pro_pub_id,req.body.user_pub_id,req.body.bid_pub_id], function(err, result) {
   if(err)
  {
    //req.flash("msg", "Error In User update");
    res.send({
    	"status":0,
    	"message":err
    });
  }
  else
  {
  	db.query('update auction_pro set price="'+req.body.price+'" where pro_pub_id="'+req.body.pro_pub_id+'"' ,function(err,result){
if(err){
	console.log(err);
}else{

		res.send({
 		"status":1,
 		"message":constant.update_bid
 	})
 	
}
                     })
	 	
    //req.flash("msg", "Successfully User UPDATE");
 	
  }
     
});
    		}else{
    			res.send({
    				"status":0,
    				"message":"Your bid should be greater than before bid."
    			})
    		}
		}
	})
});

router.post('/delete_bid',function(req,res){
  var id = req.body.bid_pub_id;
  console.log(id);
  var sql = "DELETE FROM bid_information WHERE bid_pub_id = ?";
 
  db.query(sql, [id], function(err, result) {
    if (err)
  {
    //req.flash("msg", "Error In User delete");
   res.send({
   	"status":0,
   	"message":err
   })
  }
  else
  {
    //req.flash("msg", "Successfully User Deleted");
res.send({
	"status":1,
	"message":constant.delete_bid
})
  }
});
  });

router.post('/blockUserBid',function(req,res){
var today=new Date();
                                  			var data={
		                                   		"user_pub_id":req.body.user_pub_id,
		                                   		"block_user_pub_id":req.body.block_user_pub_id,
		                                   		"pro_pub_id":req.body.pro_pub_id,
		                                   		"status":"1"
		                                   	}
                                       	db.query('INSERT INTO block_user_bid SET ?',[data],function(err,result){
 // db.query(sql, [id], function(err, result) {
   if (err)
  {
   res.send({
   	"status":0,
   	"message":err
   })
  }
  else
  {
   db.query('update bid_information set status="2" where pro_pub_id="'+req.body.pro_pub_id+'" and user_pub_id="'+req.body.block_user_pub_id+'"',function(err,results){
	})
res.send({
	"status":1,
	"message":constant.block_user
})
 }
});
  });

router.post('/unBlockUserBid',function(req,res){
	console.log(req.body);
var today=new Date();
                                  			// var data={
		                                   // 		"user_pub_id":req.body.user_pub_id,
		                                   		// "block_user_pub_id"=req.body.block_user_pub_id,
		                                   		// "pro_pub_id"=req.body.pro_pub_id,
		                                   // 		"status":"1"
		                                   // 	}
//                                       	db.query('INSERT INTO block_user_bid SET ?',[data],function(err,result){
 // db.query(sql, [id], function(err, result) {
     db.query('update block_user_bid set status="0" where pro_pub_id="'+req.body.pro_pub_id+'" and block_user_pub_id="'+req.body.block_user_pub_id+'"',function(err,results){
  if (err)
  {
   res.send({
   	"status":0,
   	"message":err
   })
  }
  else
  {
 console.log(results);
  	if(results.affectedRows>0){

   db.query('update bid_information set status="1" where pro_pub_id="'+req.body.pro_pub_id+'" and user_pub_id="'+req.body.block_user_pub_id+'"',function(err,results){
	if(err){
		console.log(err);
	}
	})

res.send({
	"status":1,
	"message":constant.unBlock_user
})
}
 }
});
  });




var schedule = require('node-schedule');
var rule = new schedule.RecurrenceRule();
 rule.hour = 23;
 rule.minute = 59;
// rule.second=1
 schedule.scheduleJob(rule, function(req,res){
db.query('SELECT * from auction_pro where status="0"',function(err,response){
		var response=JSON.parse(JSON.stringify(response));
		
		if(response.length>0){
				var date=new Date();
				 var monthNames = [
    "Jan", "Feb", "Mar",
    "Apr", "May", "Jun", "Jul",
    "Aug", "Sep", "Oct",
    "Nov", "Dec"
  ];
  var day = date.getDate();
  var monthIndex = date.getMonth();
  var year = date.getFullYear();
  var date1="0"+day+"-"+monthNames[monthIndex]+"-"+year;

				var reslen=response.length;
				 for(var i=0;i<reslen;i++){
					if(response[i].e_date==date1){
						var pro_pub_id=response[i].pro_pub_id;
			
						db.query('SELECT b.* ,u.* from bid_information b JOIN users u ON b.user_pub_id = u.user_pub_id WHERE b.pro_pub_id="'+pro_pub_id+'" and b.status="1" order by b.created_at DESC LIMIT 1',function(err,response1){
							if(!err){
								if(response1.length>0){
								var response=JSON.parse(JSON.stringify(response1));		
									var user_id=response[0].user_pub_id;
									console.log(user_id);
								  	var user_name = response[0].name;
								 
                                       	var receiver_token=response[0].device_token;
                                       		console.log(receiver_token)
                                       	var msg=constant.winAuctionMessage;
                                   		var title=constant.wonMessage;
                                   		var type=constant.winAuctionType;
                                   		var today=new Date();
                                  			var data={
		                                   		"notication_id":uniqid(),
		                                   		"user_pub_id":user_id,
		                                   		"tittle":title,
		                                   		"message":msg,
		                                   		"type":type,
		                                   		"pro_pub_id":pro_pub_id,
		                                   		"created_at":today,
		                                   		"updated_at":today
		                                   	}
                                       	db.query('INSERT INTO notification SET ?',[data],function(err,result){
                                       		if(!err){
                                       					cm.pushnotification1(user_name,msg,receiver_token,type,pro_pub_id);

                                       					db.query('SELECT user_pub_id from auction_pro WHERE pro_pub_id="'+pro_pub_id+'"',function(err,result){

                                       						if(!err){
                                       							console.log('df')
                                       							var result=JSON.parse(JSON.stringify(result));
                                       							
                                       							var owner_user_pub_id=result[0].user_pub_id;
                                       							console.log(owner_user_pub_id);
                        					var data1={
                                   		won_id:uniqid(),
                                   		owner_user_pub_id:owner_user_pub_id,
                                   		pro_pub_id:pro_pub_id,
                                   		won_user_pub_id:user_id
                                   	}
                                   	db.query('INSERT INTO won_users SET ?',[data1],function(err,result){
                                   		if(!err){
                                   			console.log('data added')
                                   			var data={
                                   				"status":1
                                   			}
                                   			db.query('UPDATE auction_pro SET ? WHERE pro_pub_id="'+pro_pub_id+'"',[data],function(err,res){
                                   				if(!err){
                                   					console.log("auction update");
                                   					var data1={
                                   						"isWin":1
                                   					}
                                   					db.query('UPDATE bid_information SET ? WHERE pro_pub_id="'+pro_pub_id+'" AND user_pub_id="'+user_id+'"',[data1],function(err,res){
                                   						if(!err){
                                   							console.log("bid updated");
                                   						}else{
                                   							console.log(err);
                                   						}
                                   					})
                                   				}
                                   				else{
                                   					console.log(err);
                                   				}
                                   			})
                                   		}else{
                                   			console.log(err);
                                   		}
                                   	})
                                       						}	
                                       					})

                                       		}else{
                                       			console.log(err);
                                       		}
                                       	})
                                       }else{
                                       	console.log("no bid in this product")
                                       }

							}else{
								console.log(err);
							}
						})
					}else{
						
					}
					
				}
		}else{
			// res.send({
			// 	"status":0,
			// 	"message":constant.NOT_FOUND
			// })
			console.log('no data found');
		}
})
});
router.post('/allWonAuction',function(req,res){
	var user_pub_id=req.body.user_pub_id;

	var result_array = [];
	var counter = 0;
	// db.query('SELECT f.* ,p.title,p.pro_pub_id,p.price,c.currency_code, CONCAT("'+base_url+'",p.image) as image FROM ((won_users f INNER JOIN auction_pro p ON f.pro_pub_id=p.pro_pub_id ) JOIN currency_setting c) where c.status="1" AND f.won_user_pub_id=?',[user_pub_id],function(err,rows){
		db.query('SELECT f.* ,p.title,p.pro_pub_id,p.price as pro_price,c.currency_code,b.*, CONCAT("'+base_url+'",p.image) as image FROM (((won_users f INNER JOIN auction_pro p ON f.pro_pub_id=p.pro_pub_id ) JOIN currency_setting c) JOIN bid_information b on f.pro_pub_id=b.pro_pub_id ) where c.status="1" AND f.won_user_pub_id="'+user_pub_id+'" AND b.user_pub_id ="'+user_pub_id+'"',function(err,rows){
		if(err){
			res.send({
				"status":0,
				"message":err
			})

		}
		else{
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
				"message":constant.NOT_FOUND
			})
		}
		}
	})

})

var rule = new schedule.RecurrenceRule();
rule.hour = 22;
rule.minute = 59;
 // rule.second=1;
schedule.scheduleJob(rule, function(req,res){
db.query('SELECT * from auction_pro where status="0"',function(err,response){		
		if(err){
			res.send({
				"status":0,
				"message":err
			})
		}else{
		if(response.length>0){
					var response=JSON.parse(JSON.stringify(response));
				var date=new Date();
				 var monthNames = [
    "Jan", "Feb", "Mar",
    "Apr", "May", "Jun", "Jul",
    "Aug", "Sep", "Oct",
    "Nov", "Dec"
  ];
  var day = date.getDate();
  var monthIndex = date.getMonth();
  var year = date.getFullYear();
  var date1=day+"-"+monthNames[monthIndex]+"-"+year;
		var reslen=response.length;
				 for(var i=0;i<reslen;i++){
					if(response[i].e_date==date1){
						var pro_pub_id=response[i].pro_pub_id;
							console.log(pro_pub_id);
						db.query('SELECT b.* ,u.* from bid_information b JOIN users u ON b.user_pub_id = u.user_pub_id WHERE b.pro_pub_id="'+pro_pub_id+'" and b.status="1"',function(err,response1){
							if(!err){
								console.log('bid enter');
								if(response1.length>0){
									console.log('data is available');
								var response=JSON.parse(JSON.stringify(response1));		
									var user_id=response[0].user_pub_id;
								
								  	var user_name = response[0].name;		 
                                       	var receiver_token=response[0].device_token;
          				    	var msg="You added bid on auction that auction is being over after 1 hour if you want to raise your bid price.";
                                   		var title="Auction message";
                                   		var type=constant.beforeMessageType;
                                   		var today=new Date();
                                  			var data={
		                                   		"notication_id":uniqid(),
		                                   		"user_pub_id":user_id,
		                                   		"tittle":title,
		                                   		"message":msg,
		                                   		"type":type,
		                                   		"pro_pub_id":pro_pub_id,
		                                   		"created_at":today,
		                                   		"updated_at":today
		                                   	}
                                       	db.query('INSERT INTO notification SET ?',[data],function(err,result){
                                       		if(!err){
                                       					cm.pushnotification1(user_name,msg,receiver_token,type,pro_pub_id);
                                       					db.query('SELECT f.* ,u.* from favourite f JOIN users u ON f.user_pub_id = u.user_pub_id WHERE f.pro_pub_id="'+pro_pub_id+'" and isFavourite="1"',function(err,response1){
							if(!err){
								if(response1.length>0){
								console.log('enter favirite one ');
								var response=JSON.parse(JSON.stringify(response1));		
									var user_id=response[0].user_pub_id;
								  	var user_name = response[0].name;
                                       	var receiver_token=response[0].device_token;
                                       	//	console.log(receiver_token)
                                       	var msg="Your favourite auction is being over after 1 hour.If,you wanna give bid so you can give on the auction which you favourited.";
                                   		var title="Auction message.";
                                   		var type=constant.beforeMessageType;
                                   		var today=new Date();
                                  			var data={
		                                   		"notication_id":uniqid(),
		                                   		"user_pub_id":user_id,
		                                   		"tittle":title,
		                                   		"message":msg,
		                                   		"type":type,
		                                   		"pro_pub_id":pro_pub_id,
		                                   		"created_at":today,
		                                   		"updated_at":today
		                                   	}
                                       	db.query('INSERT INTO notification SET ?',[data],function(err,result){
                                       		if(!err){
                                       			console.log('sss');
                                       					cm.pushnotification1(user_name,msg,receiver_token,type,pro_pub_id);
}else{

	res.send({
		"status":0,
		"message":err
	})
}
})
}
else{
	console.log('no data available')
}
}else{
	console.log(err);
}
})

                                       		}else{
                                       			console.log(err);
                                       		}
                                       	})
                                       }else{
                                       	db.query('SELECT f.* ,u.* from favourite f JOIN users u ON f.user_pub_id = u.user_pub_id WHERE f.pro_pub_id="'+pro_pub_id+'" and isFavourite="1"',function(err,response1){
							if(!err){
								if(response1.length>0){
									console.log('enter favourite TWO');
								var response=JSON.parse(JSON.stringify(response1));		
									
									var user_id=response[0].user_pub_id;
									var user_name = response[0].name;
                                       	var receiver_token=response[0].device_token;
                                       	//	console.log(receiver_token)
                                       	var msg="Your favourite auction is being over after 1 hour.If,you wanna give bid so you can give on the auction which you favourited.";
                                   		var title="Auction message.";
                                   		var type=constant.beforeMessageType;
                                   		var today=new Date();
                                  			var data={
		                                   		"notication_id":uniqid(),
		                                   		"user_pub_id":user_id,
		                                   		"tittle":title,
		                                   		"message":msg,
		                                   		"type":type,
		                                   		"pro_pub_id":pro_pub_id,
		                                   		"created_at":today,
		                                   		"updated_at":today
		                                   	}
                                       	db.query('INSERT INTO notification SET ?',[data],function(err,result){
                                       		if(!err){
                                       					cm.pushnotification1(user_name,msg,receiver_token,type,pro_pub_id);
}else{
	res.send({
		"status":0,
		"message":err
	})
}
})
}else{
	console.log('no favourite')
}
}
})
                                       }

							}else{
							 console.log(err);
								// res.send({
								// 	"status":0,
								// 	"message":err
								// })
							}
						})
					}else{
						
					}
					
				}
		}else{
			console.log('no data');
		}
	}
})
});

 module.exports=router;