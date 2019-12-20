	var express=require('express');
var uniqid = require('uniqid'); 
var bcrypt=require('bcrypt');
//var User=require('../../models/User.js');
var nodemailer = require('nodemailer');
var bodyParser=require('body-parser')
var app=express();
var router=express.Router();
var cors=require('cors');
var common=require('./common.js');
var Email=require('./email.js');
var randomstring = require("randomstring");
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true })); 
var constant=require('../../constants/constant.js')
var db=require('../../config/database.js');
var cm=require('../../model/common_model.js');
process.env.SECRET_KEY='secret';
var path = require('path');
var multer = require('multer');
// var multer = require('multer');
var email_url='http://139.59.45.232:7017/api/verify_email';
var base_url = constant.base_url;
var storage = multer.diskStorage({ //multers disk storage settings
   destination: function (req, file, cb) {
     cb(null, '../../../../../var/www/html/subasta_assets/images')
    },
    filename: function(req, file, cb) {
        var datetimestamp = Date.now();
        cb(null, file.fieldname + '-' + datetimestamp + '.' + file.originalname.split('.')[file.originalname.split('.').length - 1]);
    }
});

router.get("/verify-email/:email/:token", (req, res) => {
    var email = req.params.email;
    console.log(email);
    var token = req.params.token;
    console.log(token);
    // if(email === undefined || token === undefined){
    //     return res.json({success: false, 'message': "Invalid link"});
    // }
    db.query('SELECT * FROM users WHERE email=? AND verify_email_token=?',[email,token],function(err, results){
        if(err ){
            return res.json({success: false, 'message': err});
        }else{
          var result=JSON.parse(JSON.stringify(results))
          console.log(result);
          var user_id=result[0].user_pub_id;
          console.log(result[0].user_pub_id);
          var status=0;
          var email_verified=1;
        db.query('UPDATE users set status =?,email_verified=? WHERE user_pub_id = ? ',[ status,email_verified,user_id], function(err){
            // if(err){
            //     return res.json({success: false, 'message': "There was an error while trying to verif your email. Please try again"});       
            // }else{
            //         return res.json({success: true, 'message': "Your email has been verified successfully. Thank You!"});       
            //         }
            res.redirect('/api');
                });
            }
        });
});
var path = require("path");

router.get('/',function(req,res){
 res.sendFile(path.join(__dirname + '/html/test.html'));
  //res.sendM("Your email has been verified successfully. Thank You!")
})
var upload = multer({storage: storage});


router.post("/checkUser" , function(req,res){
 
if(!req.body.email|| !req.body.mobile_no || !req.body.name)
  {
    res.send({
      "status":0,
      "message":constant.chkFields
    })
  }
  else
  {
        var email=req.body.email;
      db.query("SELECT * FROM users WHERE email = '"+req.body.email+"' ", function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  if(rows.length>0) {
                           res.send({
                    "status":0,
                    "message":"Email is already taken please, Use unique email id."
                    })
                    } else {
                             db.query("SELECT * FROM users WHERE name = '"+req.body.name+"' ", function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  if(rows.length>0) {
                           res.send({
                    "status":0,
                    "message":"Username is already taken please, Use unique username."
                    })
                    } else {
                         db.query("SELECT * FROM users WHERE mobile_no = '"+req.body.mobile_no+"' ", function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  if(rows.length>0) {
                           res.send({
                    "status":0,
                    "message":"Mobile Number is already taken please, Use unique mobile no."
                    })
                    } else {
                      res.send({
                        "status":1,
                        "message":"Email Id , Username and Mobile Number are consirderable for creating new user."
                      })

}
})
}
})
}
})
}
})
router.post("/register" ,upload.single('image'), function(req,res){
 
if(!req.body.email || !req.body.first_name ||!req.body.last_name ||!req.body.zip||!req.body.mobile_no || !req.file ||  !req.body.name ||!req.body.password ||!req.body.town||!req.body.country_code)
  {
    res.send({
      "status":0,
      "message":constant.chkFields
    })
  }
  else
  {
      bcrypt.hash(req.body.password, 5, function( err, bcryptedPassword) {
      var email=req.body.email;
      db.query("SELECT * FROM users WHERE email = '"+req.body.email+"' ", function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  if(rows.length>0) {
                           res.send({
                    "status":0,
                    "message":"Email is already taken please, Use unique email id."
                    })
                    } else {
                             db.query("SELECT * FROM users WHERE name = '"+req.body.name+"' ", function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  if(rows.length>0) {
                           res.send({
                    "status":0,
                    "message":"Username is already taken please, Use unique username."
                    })
                    } else {
                         db.query("SELECT * FROM users WHERE mobile_no = '"+req.body.mobile_no+"' ", function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  if(rows.length>0) {
                           res.send({
                    "status":0,
                    "message":"Mobile Number is already taken please, Use unique mobile no."
                    })
                    } else {
                       var today = new Date();
      console.log(req.file);
       req.body.image = "subasta_assets/images/" + req.file.filename
      var verifyToken=common.getRandomString();
      //       var users={
      //   "user_pub_id":uniqid(),
      //   "country_code":req.body.country_code,   
      //   "image":req.body.image,
      //   "name":req.body.name, 
      //   "email":req.body.email,
      //   "password":bcryptedPassword,
      //   "verify_email_token":verifyToken,
      //   "mobile_no":req.body.mobile_no,
      //   "created_at":today
      // }    
      req.body.user_pub_id=uniqid();
      req.body.created_at=today;
      req.body.password=bcryptedPassword;
      req.body.verify_email_token=verifyToken;
      db.query('INSERT INTO users SET ?',req.body, function (error, results) {
      if (error) {
        console.log("error ocurred",error)
        res.send({
          "status":0,
          "message":error
        })
      }
          else{
       // var result=JSON.parse(JSON.stringify(results[0]));
        if(true){
                                Email.sendEmail({
                                    from: "getrid2020@gmail.com", 
                                    to: req.body.email, 
                                    subject: "Getrid Registration Successfully", 
                                    body: 'Hi ' + req.body.name + ', <br/>You have been registered successfully. Please verify your email by clicking on the link below <br/> <a href=" http://139.59.45.232:7017/api/verify-email/'+ req.body.email +'/'+ verifyToken +'">Click Here</a> to verify your email.<br/> Thank You!', 
  //                                  body: 'Hi ' + req.body.name + ', <br/>You have been registered successfully. Please,Approve your request by admin.<br/> Thank You!', 
                                    
                                    type: 'html'
                                }, function(err, response){
                                  if(err)
                                  {
                                    res.send({
                                      "status":0,
                                      "message":err
                                    })
                                  }
                                 else{       
                                res.send({
                                  "status":1,
                                  "message":constant.userSuc     
                                  });
                              }
                          });
  
    }else{
      res.send({
        "status":0,
        "message":error
      })
    }
  }
      });
                    }
                  })
                    }
                  })

     
    }
    });
})
}
})
router.post("/changePassword",function(req,res)
{
   if (!req.body.user_pub_id || !req.body.old_password || !req.body.new_password) 
   {
        res.send({
            "status": 0,
            "message": constant.CHKFIELD
        });
    }
    else
    {
      var user_pub_id=req.body.user_pub_id;
      var old_password=req.body.old_password;
      var new_password=req.body.new_password;
      db.query('SELECT * FROM users WHERE user_pub_id = ?',[user_pub_id], function (error, respnose, fields) 
      {
         if(respnose.length >0)
         {
           bcrypt.compare(old_password, respnose[0].password, function(err, doesMatch)
           {
                if (doesMatch)
                {
                  bcrypt.hash(req.body.new_password, 5, function( err, bcryptedPassword) {

                    if(bcryptedPassword.length>0)
                    {
                      var password =bcryptedPassword;

                    }
                     var updateUserData = {
                      password: password,
                      updated_at: (new Date()).valueOf().toString(),
                  }
                  db.query('UPDATE users SET password=? WHERE user_pub_id=?', [password,req.body.user_pub_id], function(err, result,fields) {
                      if (err) {
                          res.send({
                              "status": 0,
                              "message": err
                          });
                      } 
                      else 
                      {
                          if (result.length != 0) {
                              res.send({
                                  "status": 1,
                                  "message": constant.FORGETPASS
                              });
                          }
                          else {
                              res.send({
                                  "status": 0,
                                  "message": constant.NOT_FOUND,
                              });
                          }
                      }
                  });
                  });
              }
              else
              {
                 res.send({
                     "status": 0,
                     "message": constant.old
                  });
              }
            });
         }
         else 
         {
            res.send({
               "status": 0,
               "message":constant.Incur
            });
         }
      });
   }
});
router.post("/forgotPassword", function(req, res) {
    if (!req.body.email) {
        res.json({
            status: 0,
            message: constant.CHKFIELD
        });
        return;
    } else {
        db.query('SELECT * FROM users WHERE email=?',[req.body.email],function(err, result) {

                if (err) {
                    res.send({
                        "status": 0,
                        "message": err
                    });
                } else {
                  result=JSON.parse(JSON.stringify(result))
                  console.log(result)
                    if (result.length > 0) {
                        const newPass = randomstring.generate(7);
                      bcrypt.hash(newPass, 5, function(err, bcryptedPassword){
                          if(bcryptedPassword.length>0)
                      {
                        var password =bcryptedPassword;
                      }

                      var rand_paasword = password;
                        var msg = constant.MSGUPDATEDPASSWORD+' '+ newPass;
                       // mail.sendPasswordReset(req.body.email, result[0].name, newPass);
                          Email.sendEmail({
                                    from: "getrid2020@gmail.com", 
                                    to: req.body.email, 
                                    subject: "changePassword Successfully", 
                                    body: 'Hi ' + result[0].name + ', <br/>You have got new password successfully. this is your New password "'+newPass+'".<br/> Thank You!', 
                                    type: 'html'
                                }, function(err, response){
                                  if(err){
                                    console.log(err);
                                  }
                                  else{
                          var email=req.body.email;
                         var password=rand_paasword
                         var updated_at=(new Date()).valueOf().toString()
                        db.query('UPDATE users set password=?,updated_at=? WHERE email=?', [password,updated_at,req.body.email], function(err, result) {
                            if (err) {
                                console.log(err);
                            } else {
                                res.send({
                                    "status": 1,
                                    "message": constant.FORGETPASS1
                                });
                            }
                        });
         }
     }
     )
});
    }
   else{
                        res.send({
                        "status": 0,
                        "message": constant.NOEMAIL
                    });
                    }
                }
            });
    }
});
// router.post("/updateProfile",upload.single('image'), function(req, res) {
//       console.log('sssssssssss')
//         if (!req.body.user_pub_id||!req.body.email) {
//             res.json({
//                 status: 0,
//                 message: constant.chkFields
//             });
//             return;
//         } else {
//           var user_pub_id=req.body.user_pub_id;
//           console.log(req.body);
//    db.query('SELECT * FROM users WHERE user_pub_id=?', [user_pub_id], function(err, result) {
//                     if (err) {
//                         res.send({
//                             "status": 0,
//                             "message": err
//                         });
//                     } else {
//                         if (result.length == 0) {
//                             res.json({
//                                 status: 0,
//                                 message: constant.USER_NOT_FOUND
//                             });
//                         } else {
//                           console.log(result)
//          if (!req.body.name && !req.body.address && !req.body.mobile_no 
//           && !req.body.gender && !req.body.latitude && !req.body.longitude 
//           && !req.body.country_code ) 

//          {
//             res.json({
//                       status: 0,
//                       message: constant.NO_VALUE_SEND
//                   });
//          }else{
//           if(req.file) { 
//            req.body.image = "subasta_assets/images/" + req.file.filename
//                 console.log(JSON.stringify(req.body));
//             console.log('vakraam')
          
//           }
           
//             var today = new Date();
//                     var user_id = req.body.user_pub_id;
//                 //delete req.body.user_pub_id;
//                name:req.body.name,
//                 address:req.body.address,
//                 gender:req.body.gender,
//                 latitude:req.body.latitude,
//                 longitude:req.body.longitude,
//                 country_code:req.body.country_code,
//                 updated_at:today,
//                 image:req.body.image,
//                 email:req.body.email,
//                 user_pub_id:req.body,user_pub_id
//               var data={

//               }
//                 db.query('UPDATE users SET name=?,address=?,gender=? ,latitude=?,longitude=?,country_code=?,updated_at=? WHERE email=? AND user_pub_id=?',[req.body.name,req.body.address,req.body.gender,req.body.latitude,req.body.longitude,req.body.country_code,today,req.body.image,req.body.email,req.body.user_id],function(err, result1) {
//                     if (err) {
//                         console.log(err);
//                     } else {
//                       var result1=JSON.parse(JSON.stringify(result1));
//                       console.log(result1)
//                          db.query('SELECT * FROM users WHERE user_pub_id=?',[user_id], function(err, result) {
//                             if (err) {
//                                 console.log(err);
//                             } else {
//                               // console.log(result)
//                                 var data = JSON.parse(JSON.stringify(result));
//                               console.log(data)
//                                 data[0].image = base_url + data[0].image;
//                                 console.log('balram1')
//                                 res.send({
//                                     "status": 1,
//                                     "message": "updateProfile",
//                                     "data": data[0]
//                                 });
//                             }

//                         });
//                     }

//                 })
//               }
//                 }
// }});
//             }
   
// });
router.post("/updateProfile",upload.single('image'), function(req, res) {
        if (!req.body.user_pub_id||!req.body.name) {
            res.json({
                status: 0,
                message: constant.chkFields
            });
            return;
        } else {
          var user_pub_id=req.body.user_pub_id;
        //  console.log(req.body);
   db.query('SELECT * FROM users WHERE user_pub_id=?', [user_pub_id], function(err, result) {
                    if (err) {
                        res.send({
                            "status": 0,
                            "message": err
                        });
                    } else {
                        if (result.length == 0) {
                            res.send({
                                "status": 0,
                                "message": constant.USER_NOT_FOUND
                            });
                        } else {
         if (!req.body.name && !req.body.address && !req.body.mobile_no 
          && !req.body.gender && !req.body.latitude && !req.body.longitude 
          && !req.body.country_code ) 
         {
            res.send({
                      "status": 0,
                      "message": constant.NO_VALUE_SEND
                  });
         }else{
          if(req.file) { 
           req.body.image = "subasta_assets/images/" + req.file.filename
          
              var today = new Date();
                    var user_id = req.body.user_pub_id;
                delete req.body.user_pub_id;
                  var name=req.body.name;
                  delete req.body.name;
               cm.update('users', {
                    user_pub_id: user_id,
                    name:name,
                }, req.body, function(err, result1) {
                //db.query('UPDATE users SET ? WHERE email="'+req.body.email+'" AND user_pub_id="'+user_id+'"',[data],function(err, result1) {
                    if (err) {
                        console.log(err);
                    } else {
                      var result1=JSON.parse(JSON.stringify(result1));
                      console.log(result1)
                         db.query('SELECT * FROM users WHERE user_pub_id=?',[user_id], function(err, result) {
                            if (err) {
                                console.log(err);
                            } else {
                              // console.log(result)
                                var data = JSON.parse(JSON.stringify(result));
                                data[0].image = base_url + data[0].image;
                                res.send({
                                    "status": 1,
                                    "message": "updateProfile",
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
                  var name=req.body.name;
                  delete req.body.name;
               cm.update('users', {
                    user_pub_id: user_id,
                    name:name,
                }, req.body, function(err, result1) {
                 if (err) {
                        console.log(err);
                    } else {
                      var result1=JSON.parse(JSON.stringify(result1));
                      //console.log(result1)
                         db.query('SELECT * FROM users WHERE user_pub_id=?',[user_id], function(err, result) {
                            if (err) {
                                console.log(err);
                            } else {
                              // console.log(result)
                                var data = JSON.parse(JSON.stringify(result));
                                data[0].image = base_url + data[0].image;
                                res.send({
                                    "status": 1,
                                    "message": "updateProfile",
                                    "data": data[0]
                                });
                            }

                        });
                    }

                })
            
          	      
          }
           }
                }
}
});
            }
   
});

router.post("/signupSocialMedia" , function(req,res){
 console.log(req.body);

if( !req.body.name || !req.body.email )
  {
    res.send({
      "status":0,
      "message":constant.chkFields
    })
  }
  else
  {
      var email=req.body.email;
      db.query('SELECT *,CONCAT("'+base_url+'", image) as image FROM users WHERE email = ?',[email], function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  else{
                  if(rows.length!=0) {
                    var result = JSON.parse(JSON.stringify(rows))
                           res.send({
                    "status":0,
                    "message":constant.checkEmail,
                    "data": result
                    })
                    } 
                    else {

      
      if(!req.file){
      	  var today = new Date();
            var data={
        "user_pub_id":uniqid(),
        "name":req.body.name, 
        "email":req.body.email,
        "image":'subasta_assets/images/',
        "created_at":today
      } 
      db.query('INSERT INTO users SET ?',[data], function (error, results) {

      if (error) {
        console.log("error ocurred",error)
        res.send({
          "status":0,
          "message":error

        })
      }
          else{
           res.send({
        "status":1,
        "message":constant.addSuc
      })

    }
  })
      }   
      
    }
  }
})
    }
  })

router.post('/getProfile',function(req,res){
  var id=req.body.user_pub_id;
  db.query('SELECT *, CONCAT("'+base_url+'", image) as image FROM users WHERE user_pub_id=?',[id],function(err,result){
    if(err){
      res.send({
        "status":0,
        "message":err
      })
    }else{
      var result=JSON.parse(JSON.stringify(result[0]))
      console.log(result)
      res.send({
        "status":1,
        "message":constant.userDetails,
        "data":result
      })
    }
  })
})

module.exports=router;