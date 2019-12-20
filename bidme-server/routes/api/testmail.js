var express=require('express');
var uniqid = require('uniqid'); 
var bcrypt=require('bcrypt');
//var User=require('../../models/User.js');
var nodemailer = require('nodemailer');
var bodyParser=require('body-parser')
var app=express();
var router=express.Router();
var cors=require('cors');
var path=require('path');
var common=require('./common.js');
var Email=require('./email.js');
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true })); 


var constant=require('../../constants/constant.js')
var db=require('../../config/database.js');

process.env.SECRET_KEY='secret';
var multer = require('multer');

var storage = multer.diskStorage({
   destination: (req, file, cb) => {
     cb(null, './assets/images')
   },
   filename: (req, file, cb) => {
     cb(null, file.picture + '-' + Date.now() + path.extname(file.originalname))
   }
});
var upload = multer({storage: storage});

router.get("/verify-email/:email/:token", (req, res) => {
    var email = req.params.email;
    console.log(email);
    var token = req.params.token;
    console.log(token);
    // if(email === undefined || token === undefined){
    //     return res.json({success: false, 'message': "Invalid link"});
    // }
    db.query('SELECT * FROM users WHERE email=? AND verify_email_token=?',[email,token],function(err,results){
        if(err ){
            return res.json({success: false, 'message': err});
        }else{
          var result=JSON.parse(JSON.stringify(results[0]))
          console.log(result)
          console.log(result.user_pub_id);
          var email_value=1;
        db.query('UPDATE users set email_verified =? WHERE user_pub_id = ? ',[email_value, result.user_pub_id], function(err){
            if(err){
                return res.json({success: false, 'message': "There was an error while trying to verif your email. Please try again"});       
            }else{
                    return res.json({success: true, 'message': "Your email has been verified successfully. Thank You!"});       
                    }
                });
            }
        });
});

router.get("/testmail" , function(req,res){
 
      Email.sendEmail({
                                    from: "samyotechindore@gmail.com", 
                                    to: 'samyotech@gmail.com', 
                                    subject: "Registration Successful", 
                                    body: 'Hi samyotech , You have been registered successfully. Thank You!', 
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


})
module.exports=router;