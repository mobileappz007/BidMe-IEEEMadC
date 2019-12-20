// var express=require('express');
// var router=express.Router();
// var uniqid=require('uniqid')
// var db=require('../../config/database.js');
// var constant=require('../../constants/constant.js');
// var _ = require('lodash');
// var base_url=constant.base_url;
// var cm=require('../../model/common_model.js')
// router.post('/sendMessage',function(req,res){
// 	if(!req.body.sender_user_pub_id||!req.body.receiver_user_pub_id){
// 		res.send({
// 			"status":0,
// 			"message":constant.chkFields
// 		})
// 	}else{
// 		var today=new Date()
// 		var data={
// 			chat_id:uniqid(),
// 			sender_user_pub_id:req.body.sender_user_pub_id,
// 			receiver_user_pub_id:req.body.receiver_user_pub_id,
// 			message:req.body.message,
// 			sender_name:req.body.sender_name,
// 			date:(new Date()).valueOf().toString()
// 		}
// 		db.query('INSERT INTO chat SET ?',[data],function(err,result){
// 			if(err){
// 				res.send({
// 					"status":0,
// 					"message":err
// 				})
// 			}else{
// 				res.send({
// 					"status":1,
// 					"message":constant.MessageSent
// 				})
// 				// db.query('SELECT * FROM users WHERE user_pub_id="'+receiver_user_pub_id+'"',function(err, result) {
//     //                         if (err) {
//     //                             console.log(err);
//     //                         } else {
//     //                             var receiver_data = JSON.parse(JSON.stringify(result[0]));
//     //                             var receiver_token = receiver_data.device_token
//     //                             console.log(receiver_token);
//     //                            db.query('SELECT * FROM users WHERE user_pub_id="'+sender_user_pub_id+'"', function(err, result) {
//     //                                 if (err) {
//     //                                     console.log(err);
//     //                                 } else {
//     //                                     var user_data = JSON.parse(JSON.stringify(result[0]));
//     //                                     var user_name = user_data.name;
//     //                                     console.log(user_name);
//     //                                     console.log(receiver_token);
//     //                                     console.log(req.body.message);
//     //                                     cm.pushnotification(user_name, req.body.message, receiver_token);
//     //                                 }
//     //                             });
//                             }
//                         })
// 			}
// 		})
	
// router.post('/getChatHistory',function(req,res){
// 	if(!req.body.sender_user_pub_id){
// 		res.send({
// 			"status":0,
// 			"message":constant.chkFields
// 		})
// 	}else{
// 		db.query('SELECT c.*,(u.name) as receiver_name,CONCAT("'+base_url+'",us.image) as sender_image,CONCAT("'+base_url+'",u.image) as receiver_image FROM ((chat c JOIN users u ON c.receiver_user_pub_id=u.user_pub_id ) LEFT JOIN users us ON c.sender_user_pub_id=us.user_pub_id)  WHERE c.sender_user_pub_id="'+req.body.sender_user_pub_id+'" OR c.receiver_user_pub_id="'+req.body.sender_user_pub_id+'" ORDER BY c.date DESC',function(err,result)
// 		{
// 			if(err){
// 					res.send({
// 						"status":0,
// 						"message":err
// 					})
// 					}
// 			else{
// 				console.log(result)
// 				var final_result = _.uniqBy(result, 'receiver_user_pub_id');
// 				res.send({
// 					"status":1,
// 					"message":constant.SeeAllFriend,
// 					"data":final_result
// 				})
// 				}
// 		})
// 	}
// })
// router.post('/getSingleChatHistory',function(req,res){
// 	if(!req.body.sender_user_pub_id||!req.body.receiver_user_pub_id){
// 		res.send({
// 			"status":0,
// 			"message":constant.chkFields
// 		})
// 	}else{
// 		db.query('SELECT u.name,CONCAT("'+base_url+'",u.image) as image,c.* from chat c INNER JOIN users u ON u.user_pub_id=c.receiver_user_pub_id WHERE c.receiver_user_pub_id="'+req.body.receiver_user_pub_id+'" AND c.sender_user_pub_id="'+req.body.sender_user_pub_id+'" OR c.sender_user_pub_id="'+req.body.receiver_user_pub_id+'" AND c.receiver_user_pub_id="'+req.body.sender_user_pub_id+'"',function(err,result){
// 			if(err){
// 				res.send({
// 					"status":0,
// 					"message":err
// 				})
// 			}else{
// 				if(result.length>0){
// 				res.send({
// 					"status":1,
// 					"message":constant.AllMessages,
// 					"data":result
// 				})
// 			}else{
// 				res.send({
// 					"status":0,
// 					"message":constant.NoChat
// 				})
// 			}
// 		}
// 		})
// 	}

// })
// module.exports=router;

var express=require('express');
var router=express.Router();
var uniqid=require('uniqid')
var db=require('../../config/database.js');
var constant=require('../../constants/constant.js');
var _ = require('lodash');
var base_url=constant.base_url;
var cm=require('../../model/common_model.js')

router.post('/getThreadId',function(req,res){
var thread_id;
if(!req.body.sender_user_pub_id||!req.body.receiver_user_pub_id){
	res.send({
		"status":"0",
		"message":constant.chkFields
	})
}
else{
db.query('SELECT COALESCE(c.thread_id,"") as thread_id FROM chat c WHERE (c.sender_user_pub_id="'+req.body.sender_user_pub_id+'" and c.receiver_user_pub_id="'+req.body.receiver_user_pub_id+'")||(c.sender_user_pub_id="'+req.body.receiver_user_pub_id+'" and c.receiver_user_pub_id="'+req.body.sender_user_pub_id+'")',function(err,result){
	if(err)
	{

console.log(err);
		res.send(err);
	}
	 if(result[0]==undefined){
	
thread_id=uniqid();
var data={
	 thread_id:thread_id,
	 sender_user_pub_id:req.body.sender_user_pub_id,
	 receiver_user_pub_id: req.body.receiver_user_pub_id,
	 message:constant.message,
	 date:(new Date()).valueOf().toString()
	 	}
	 	db.query( 'INSERT INTO chat SET ?',[data],function(err,result){
	 		if(err){
	 			res.send({
	 				"status":0,
	 				"message":err
	 			})
	 		}else{
	 			var id=result.insertId
	 			db.query('SELECT * FROM chat WHERE id="'+id+'"',function(err,result){
	 				if(!err){
	 					var result1=JSON.parse(JSON.stringify(result[0]));
	 					console.log(result1);
	 					var th_id = { thread_id: result1.thread_id}
	 					res.send({
	 				"status":1,
	 				"message":constant.th_id,
	 				"data": th_id
	 			})
	 				}

	 			})
	 			
	 		}
	 	})
	 }
	 else{
	 	console.log(result);
	 	var result=JSON.parse(JSON.stringify(result));
	 	res.send({
	 		"status":1,
	 		"data":result[0]
	 	})
	 }
		})
}
})

router.post('/sendMessage',function(req,res){
	var sender_user_pub_id=req.body.sender_user_pub_id;
	var receiver_user_pub_id=req.body.receiver_user_pub_id

	if(!req.body.sender_user_pub_id||!req.body.receiver_user_pub_id ){
		res.send({
			"status":0,
			"message":constant.chkFields
		})
	}else{
		
		var data={
			chat_id:uniqid(),
			thread_id:req.body.thread_id,
			sender_user_pub_id:req.body.sender_user_pub_id,
			receiver_user_pub_id:req.body.receiver_user_pub_id,
			message:req.body.message,
			
			date:(new Date()).valueOf().toString()
		}
		db.query('INSERT INTO chat SET ?',[data],function(err,result){
			if(err){
				res.send({
					"status":0,
					"message":err
				})
			}else{
				console.log(result);
							res.send({
					"status":1,
					"message":constant.MessageSent
				})
				db.query('SELECT * FROM users WHERE user_pub_id="'+receiver_user_pub_id+'"',function(err, result) {
                            if (err) {
                                console.log(err);
                            } else {
                            	
                                var receiver_data = JSON.parse(JSON.stringify(result[0]));
                                var receiver_token = receiver_data.device_token
                            
                               db.query('SELECT * FROM users WHERE user_pub_id="'+sender_user_pub_id+'"', function(err, result) {
                                    if (err) {
                                        console.log(err);
                                    } else {
                                        var user_data = JSON.parse(JSON.stringify(result[0]));
                                        var user_name = user_data.name;
                                        var type=constant.CHAT_TYPE;
                                        cm.pushnotification(user_name,req.body.message,receiver_token,type);
                                    }
                                });
                            }
                        })
			}
		})
			 }
			
		})

	// 	if(req.body.thread_id==null){
		// var today=new Date()
		// var data={
		// 	chat_id:uniqid(),
		// 	thread_id:uniqid(),
		// 	sender_user_pub_id:req.body.sender_user_pub_id,
		// 	receiver_user_pub_id:req.body.receiver_user_pub_id,
		// 	message:req.body.message,
			
		// 	date:(new Date()).valueOf().toString()
		// }
		// db.query('INSERT INTO chat SET ?',[data],function(err,result){
		// 	if(err){
		// 		res.send({
		// 			"status":0,
		// 			"message":err
		// 		})
		// 	}else{
		// 		res.send({
		// 			"status":1,
		// 			"message":constant.MessageSent
		// 		})
		// 		db.query('SELECT * FROM users WHERE user_pub_id="'+receiver_user_pub_id+'"',function(err, result) {
  //                           if (err) {
  //                               console.log(err);
  //                           } else {
                            	
  //                               var receiver_data = JSON.parse(JSON.stringify(result[0]));
  //                               console.log(receiver_data);
  //                               var receiver_token = receiver_data.device_token
  //                               console.log(receiver_token);
  //                              db.query('SELECT * FROM users WHERE user_pub_id="'+sender_user_pub_id+'"', function(err, result) {
  //                                   if (err) {
  //                                       console.log(err);
  //                                   } else {
  //                                       var user_data = JSON.parse(JSON.stringify(result[0]));
  //                                       var user_name = user_data.name;
  //                                       console.log(user_name);
  //                                       console.log(receiver_token);
  //                                       console.log(req.body.message);
  //                                       cm.pushnotification(user_name,req.body.message,receiver_token);
  //                                   }
  //                               });
  //                           }
  //                       })
		// 	}
		// })
	// }else{
		// var today=new Date()
		// var data={
		// 	chat_id:uniqid(),
		// 	thread_id:req.body.thread_id,
		// 	sender_user_pub_id:req.body.sender_user_pub_id,
		// 	receiver_user_pub_id:req.body.receiver_user_pub_id,
		// 	message:req.body.message,
			
		// 	date:(new Date()).valueOf().toString()
		// }
		// db.query('INSERT INTO chat SET ?',[data],function(err,result){
		// 	if(err){
		// 		res.send({
		// 			"status":0,
		// 			"message":err
		// 		})
		// 	}else{
		// 		res.send({
		// 			"status":1,
		// 			"message":constant.MessageSent
		// 		})
		// 		db.query('SELECT * FROM users WHERE user_pub_id="'+receiver_user_pub_id+'"',function(err, result) {
  //                           if (err) {
  //                               console.log(err);
  //                           } else {
                            	
  //                               var receiver_data = JSON.parse(JSON.stringify(result[0]));
  //                               console.log(receiver_data);
  //                               var receiver_token = receiver_data.device_token
  //                               console.log(receiver_token);
  //                              db.query('SELECT * FROM users WHERE user_pub_id="'+sender_user_pub_id+'"', function(err, result) {
  //                                   if (err) {
  //                                       console.log(err);
  //                                   } else {
  //                                       var user_data = JSON.parse(JSON.stringify(result[0]));
  //                                       var user_name = user_data.name;
  //                                       console.log(user_name);
  //                                       console.log(receiver_token);
  //                                       console.log(req.body.message);
  //                                       cm.pushnotification(user_name,req.body.message,receiver_token);
  //                                   }
  //                               });
  //                           }
  //                       })
		// 	}
		// })

	// }

// router.post('/getChatHistory',function(req,res){
// 	if(!req.body.sender_user_pub_id){
// 		res.send({
// 			"status":0,
// 			"message":constant.chkFields
// 		})
// 	}else{
// 		var sender_user_pub_id=req.body.sender_user_pub_id;
// 		//SELECT c.*,(u.name) as user_name,CONCAT("'+base_url+'",u.image) as user_image FROM chat c JOIN users u ON u.user_pub_id=c.sender_user_pub_id || u.user_pub_id=c.sender_user_pub_id  WHERE c.receiver_user_pub_id="'+req.body.sender_user_pub_id+'" || c.sender_user_pub_id="'+req.body.sender_user_pub_id+'" GROUP BY c.thread_id ORDER BY c.date DESC
// 		db.query('select m.* ,(u.name) as user_name,CONCAT("'+base_url+'",u.image) as user_image from chat  m inner join (select max(id) as maxid from chat where chat.sender_user_pub_id = "'+sender_user_pub_id+'" OR chat.receiver_user_pub_id = "'+sender_user_pub_id+'" group By (if(sender_user_pub_id > receiver_user_pub_id,  sender_user_pub_id, receiver_user_pub_id)), (if(sender_user_pub_id > receiver_user_pub_id,  receiver_user_pub_id, sender_user_pub_id)) order by chat.date) t1 on m.id=t1.maxid join users u  ON u.user_pub_id = (CASE WHEN m.sender_user_pub_id = "'+sender_user_pub_id+'" THEN m.receiver_user_pub_id ELSE m.sender_user_pub_id END) order by m.date' ,function(err,result)
// 		{
// 			if(err){
// 					res.send({
// 						"status":0,
// 						"message":err
// 					})
// 					}
// 			else{

// 				if(result.length>0){
// 					var result=JSON.parse(JSON.stringify(result));
// 				console.log(result)
// 			//	var final_result = _.uniqBy( result,'receiver_user_pub_id');
// 				res.send({
// 					"status":1,
// 					"message":constant.SeeAllFriend,
// 					"data":result
// 				})
// 				}else{
// 				res.send({
// 					"status":0,
// 					"message":constant.NoChat
// 				})
// 			}
// 			}
// 		})
// 	}
// })

// router.post("/getChatHistory", function(req, res) {
//     if (!req.body.sender_user_pub_id) {
//         res.json({
//             status: 0,
//             massage: constant.CHKAllFIELD
//         });
//         return;
//     } else {
//     	db.query('SELECT * from users WHERE user_pub_id="'+req.body.sender_user_pub_id+'"',function(err,result){

    
//             if (err) {
//                 console.log(err);
//             } else {

//                 if (result.length > 0) {
//                 	db.query('SELECT * FROM chat WHERE sender_user_pub_id="'+req.body.sender_user_pub_id+'"',function(err,result){
                    
//                         if (err) {
//                             console.log(err);
//                         } else {
//                             if (result.length > 0) {
//                                 for (i = 0; i < result.length; i++) {
                                
//     	                            		var arrayList1=[];
//     	                            			var counter = 1;

//                                    	db.query('SELECT c.*,(u.name) as receiver_name,CONCAT("'+base_url+'",u.image) as receiver_image FROM (chat c JOIN users u ON u.user_pub_id=c.receiver_user_pub_id || c.sender_user_pub_id  )   WHERE c.receiver_user_pub_id="'+result[i].sender_user_pub_id+'" || c.sender_user_pub_id="'+result[i].sender_user_pub_id+'" ORDER BY c.date DESC',function(err,result){
//                                         if (err) {
//                                             console.log(err);
//                                         } else {
//                                         	var result1=JSON.parse(JSON.stringify(result))
                                        		
//                                         	  arrayList1.push(result1);
//                                         	  counter++;
                                        	 
// 	                                        	  if(counter==result.length){
// 	                                var final_result = _.uniqBy(arrayList1,'receiver_user_pub_id');
// 	                                console.log(final_result)
										                               
// 				res.send({
// 					"status":1,
// 					"message":constant.SeeAllFriend,
// 					"data":final_result
//  				})  
                    	
// 	                                }
// 	                                }
                
                                        	 
                                        
                                        

//                                     });
                                  
					
//                                 }
//                             }


//                         }


//                     });

//                 } else {
//                     res.send({
//                         "status": 0,
//                         "massage": constant.USER_NOT_FOUND
//                     });
//                 }
//             }
//         });
//     }

// // });
// router.post("/getChatHistory", function(req, res) {
//     if (!req.body.sender_user_pub_id) {
//         res.json({
//             status: 0,
//             massage: constant.CHKAllFIELD
//         });
//         return;
//     } else {
//     	db.query('SELECT * from users WHERE user_pub_id="'+req.body.sender_user_pub_id+'"',function(err,result){

    
//             if (err) {
//                 console.log(err);
//             } else {

//                 if (result.length > 0) {
//                 	db.query('SELECT * FROM chat WHERE sender_user_pub_id="'+req.body.sender_user_pub_id+'"',function(err,result){
                    
//                         if (err) {
//                             console.log(err);
//                         } else {
//                             if (result.length > 0) {
//                                 for (i = 0; i < result.length; i++) {
                                
//     	                            		var arrayList1=[];
//     	                            			var counter = 1;

//                                    	db.query('SELECT c.*,(u.name) as receiver_name,CONCAT("'+base_url+'",u.image) as receiver_image FROM (chat c JOIN users u ON u.user_pub_id=c.receiver_user_pub_id || c.sender_user_pub_id  )   WHERE c.receiver_user_pub_id="'+result[i].sender_user_pub_id+'" || c.sender_user_pub_id="'+result[i].sender_user_pub_id+'" ORDER BY c.date DESC',function(err,result){
//                                         if (err) {
//                                             console.log(err);
//                                         } else {
//                                         	var result1=JSON.parse(JSON.stringify(result))
                                        		
//                                         	  arrayList1.push(result1);
//                                         	  counter++;
                                        	 
// 	                                        	  if(counter==result.length){
// 	                                var final_result = _.uniqBy(arrayList1,'receiver_user_pub_id');
// 	                                console.log(final_result)
										                               
// 				res.send({
// 					"status":1,
// 					"message":constant.SeeAllFriend,
// 					"data":final_result[0]
//  				})  
// 	                                }
// 	                                }
//                                     });
                                  
					
//                                 }
//                             }else{
//                             	res.send({
//                             		"status":0,
//                             		"message":"No data."
//                             	})
//                             }


//                         }


//                     });

//                 } else {
//                     res.send({
//                         "status": 0,
//                         "massage": constant.USER_NOT_FOUND
//                     });
//                 }
//             }
//         });
//     }

// });
// var Promise = require('bluebird');
// 	router.post("/getChatHistory", function(req, res) {
  
//     if (!req.body.sender_user_pub_id) {
//         res.json({
//             status: 0,
//             message:"chekall fields"
//         });
//         return;
//     } else {
//   		db.query('SELECT * FROM chat WHERE sender_user_pub_id= "'+req.body.sender_user_pub_id+'"',function(err,result){
//   			console.log("chat user found")
//             if (result.length == 0) {
//                 res.send({
//                     "status": 0,
//                     "message": "No Data."
//                 });
//             } else {

//             	db.query('SELECT * FROM chat WHERE sender_user_pub_id="'+req.body.sender_user_pub_id+'"  or  receiver_user_pub_id ="'+req.body.sender_user_pub_id+'" order by date desc',function(err,chat_result){
//             		  			console.log("chat user found2")

//                     if (err) {
//                         res.send({
//                             "status": 0,
//                             "message":err


//                         });
//                     } else
//                     if (chat_result.length == 0) {
//                         res.send({
//                             "status": 0,
//                             "message": "No chat."
//                         });
//                     } else {
//                         var chat_row_arr = {};
//                         var chat_row = [];

//                         chat_result
//                             .reduce(function(promiesRes, chatdata, index) {
//                                 return promiesRes

//                                     .then(function(data) {

//                                         return new Promise(function(resolve, reject) {
//                                         	  			console.log("promise 1")
//                                             if (req.body.sender_user_pub_id == chatdata.receiver_user_pub_id) {
//                                                var  sender_user_pub_id = chatdata.sender_user_pub_id;
//                                                console.log(sender_user_pub_id);
//                                                 chatdata.receiver_user_pub_id = chatdata.sender_user_pub_id;
//                                             } else {
//                                                 var sender_user_pub_id = chatdata.receiver_user_pub_id;
//                                                 console.log(sender_user_pub_id);
//                                             }
//                                             db.query('SELECT * FROM users WHERE user_pub_id="'+sender_user_pub_id+'"',function(err, result1) {
//                                                 	                                 	console.log("User found 1")
//                                               if(err){
//                                               	reject(err);
//                                               	// res.redirect()
//                                               }else{
//                                                 if (result1.length > 0) {
//                                                 	console.log(result1[0].name)
//                                                 	console.log(result1[0].user_pub_id)
//                                                 	console.log(result1[0].image)
                                                   
//                                                     chatdata.userName = result1[0].name;
//                                                     chatdata.userImage = "http://139.59.45.232/" + result1[0].image;
//                                                 }
//                                             }
//                                                 // if (chatdata.user_pub_id_receiver != req.body.user_pub_id)
//                                                     // chat_row.push(chatdata);
//                                                 resolve(chatdata);

//                                             })
//                                         })
//                                     })
//                                     .then(function(chatdata) {

//                                         return new Promise(function(resolve, reject) {
                                           
//                                         	db.query('SELECT * FROM chat WHERE sender_user_pub_id = "'+receiver_user_pub_id+'" AND receiver_user_pub_id = "'+sender_user_pub_id+'" and  chat_state="0"', function(err, unread) {
//                                               console.log("chat found 3")

//                                                 chatdata.unReadMsg=unread.length;
//                                                 // if (chatdata.user_pub_id_receiver != req.body.user_pub_id)
//                                                     // chat_row.push(chatdata);
//                                                 resolve(chatdata);

//                                             })
//                                         })
//                                     })
                                    
                                    
//                                     .then(function(chatdata) {

//                                         return new Promise(function(resolve, reject) {
//                                         	db.query('SELECT * FROM users WHERE user_pub_id="'+chatdata.receiver_user_pub_id+'"', function(err, user_mode) {
//                                             console.log("User found 2")

//                                                 if(user_mode[0].status==1)
//                                                 {
//                                                 chatdata.status='1';
//                                                 }
//                                                 else
//                                                 {
//                                                     chatdata.status='0';
//                                                 }

//                                                  resolve(chatdata);

//                                             })
//                                         })
//                                     })
//                                     .then(function(chatdata) {

//                                         return new Promise(function(resolve, reject) {
//                                               //	var que="SELECT * FROM `chat` WHERE (`user_pub_id` = '"+sender_id+"' AND `user_pub_id_receiver` = '"+receiver_id+"' ) or( `user_pub_id` = '"+receiver_id+"' and `user_pub_id_receiver` = '"+sender_id+"' )";
//                                               	db.query("SELECT * FROM chat WHERE (sender_user_pub_id = '"+req.body.sender_user_pub_id+"' AND receiver_user_pub_id = '"+chatdata.receiver_user_pub_id+"' ) or( sender_user_pub_id = '"+chatdata.receiver_pub_id+"' and receiver_user_pub_id = '"+req.body.sender_user_pub_id+"')", function(err, blankchat) {
//                                                	console.log('chat found 4')
//                                                 if(blankchat.length>0)
//                                                 {
//                                                   if (chatdata.receiver_user_pub_id != req.body.sender_user_pub_id)
//                                                     chat_row.push(chatdata);
//                                                 resolve(chatdata);  
//                                                 }
//                                                 else
//                                                 {
//                                                   resolve(chatdata);   
//                                                 }
//                                                });
//                                         })
//                                     })
//                                     .catch(function(error) {

//                                         res.send({
//                                             "status": 0,
//                                             "message": error
//                                      });
                                        
//                                     })
//                             }, Promise.resolve(null)).then(arrayOfResults => { // Do something with all results
//                                 var final_result = [];
//                                 final_result = _.uniqBy(chat_row, 'receiver_user_pub_id');
//    								db.query("SELECT * FROM chat WHERE sender_user_pub_id='KEYMARKETSUPER' and receiver_user_pub_id ='KEYMARKETSUPER' order by date desc ",function(err,admin_chat)
//                                 {
//                                 		console.log("chat found 5");
//                                     if(admin_chat !=0)
//                                     {
//                                         admin_chat[0].unReadMsg=0;
//                                         admin_chat[0].isBlock=0;
//                                         admin_chat[0].is_follow=0;
//                                         admin_chat[0].userStatus=0;
//                                         admin_chat[0].userName="Admin";
//                                         admin_chat[0].userImage="";
//                                         final_result.push(admin_chat[0]);
//                                     }
                                    
//                                     res.send({
//                                         "status": 1,
//                                         "message": "Get all chat.",
//                                         "my_chat": final_result
//                                     });
//                                 });
//                             });
//                     }
//                 });
//             }
//         });
//     }

// });


router.post('/getChatHistory',function(req,res){
	if(!req.body.sender_user_pub_id){
		res.send({
			"status":0,
			"message":constant.chkFields
		})
	}else{
		var sender_user_pub_id=req.body.sender_user_pub_id;
		// select m.* ,(u.name) as user_name,CONCAT("'+base_url+'",u.image) as user_image from chat  m inner join (select max(id) as maxid from chat where chat.sender_user_pub_id = "'+sender_user_pub_id+'" OR chat.receiver_user_pub_id = "'+sender_user_pub_id+'" group By (if(sender_user_pub_id > receiver_user_pub_id,  sender_user_pub_id, receiver_user_pub_id)), (if(sender_user_pub_id > receiver_user_pub_id,  receiver_user_pub_id, sender_user_pub_id)) order by chat.date ASC) t1 on m.id=t1.maxid join users u  ON u.user_pub_id = (CASE WHEN m.sender_user_pub_id = "'+sender_user_pub_id+'" THEN m.receiver_user_pub_id ELSE m.sender_user_pub_id END) order by m.date
		db.query('select m.* ,(u.name) as user_name,CONCAT("'+base_url+'",u.image) as user_image from chat  m inner join (select max(id) as maxid from chat where chat.sender_user_pub_id = "'+sender_user_pub_id+'" OR chat.receiver_user_pub_id = "'+sender_user_pub_id+'" group By (if(sender_user_pub_id > receiver_user_pub_id,  sender_user_pub_id, receiver_user_pub_id)), (if(sender_user_pub_id > receiver_user_pub_id,  receiver_user_pub_id, sender_user_pub_id))  ) t1 on m.id=t1.maxid join users u  ON u.user_pub_id = (CASE WHEN m.sender_user_pub_id = "'+sender_user_pub_id+'" THEN m.receiver_user_pub_id ELSE m.sender_user_pub_id END) order by m.date' ,function(err,result)
		{
			if(err){
					res.send({
						"status":0,
						"message":err
					})
					}
			else{
				console.log(result);

				if(result.length>0){
					var result=JSON.parse(JSON.stringify(result));
				
				console.log(result)
				var final_result=result.reverse();
			//	var final_result = _.uniqBy( result,'receiver_user_pub_id');
				res.send({
					"status":1,
					"message":constant.SeeAllFriend,
					"data":final_result
				})
				}else{
				res.send({
					"status":0,
					"message":constant.NoChat
				})
			}
			}
		})
	}
})



router.post('/getSingleChatHistory',function(req,res){
	if(!req.body.sender_user_pub_id||!req.body.receiver_user_pub_id){
		res.send({
			"status":0,
			"message":constant.chkFields
		})
	}else{
		db.query('SELECT u.name,CONCAT("'+base_url+'",u.image) as image,c.* from chat c INNER JOIN users u ON u.user_pub_id=c.receiver_user_pub_id WHERE c.receiver_user_pub_id="'+req.body.receiver_user_pub_id+'" AND c.sender_user_pub_id="'+req.body.sender_user_pub_id+'" OR c.sender_user_pub_id="'+req.body.receiver_user_pub_id+'" AND c.receiver_user_pub_id="'+req.body.sender_user_pub_id+'" order by c.date',function(err,result){
			if(err){
				res.send({
					"status":0,
					"message":err
				})
			}else{
				if(result.length>0){
					var blockStatus=result[0].status;
					
				res.send({
					"status":1,
					"message":constant.AllMessages,
					"data":result,blockStatus
				})
			}else{
				res.send({
					"status":0,
					"message":constant.NoChat
				})
			}
		}
		})
	}


})

router.post('/blockUser',function(req,res){
	if(!req.body.thread_id){
		res.send({
			"status":0,
			"message":constant.chkFields
		})
	}else{
		var status=0;
  db.query('UPDATE chat SET status="'+status+'" WHERE thread_id=?',[req.body.thread_id],function(err,results){
  	if(err){
  		res.send({
  			"status":0,
  			"message":err
  		})
  	}else{
  		res.send({
  			"status":1,
  			"message":"User is blocked"
  		})
  	}
	})
}
})

router.post('/unBlockUser',function(req,res){
	if(!req.body.thread_id){
		res.send({
			"status":0,
			"message":constant.chkFields
		})
	}else{
		var status=1;
  db.query('UPDATE chat SET status="'+status+'" WHERE thread_id=?',[req.body.thread_id],function(err,results){
  	if(err){
  		res.send({
  			"status":0,
  			"message":err
  		})
  	}else{
  		res.send({
  			"status":1,
  			"message":"User is blocked"
  		})
  	}
	})
}
})
module.exports=router;