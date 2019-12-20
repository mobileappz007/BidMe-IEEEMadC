var express=require('express');
var db=require('../../config/database.js');
var uniqid = require('uniqid'); 
var bcrypt=require('bcrypt');
//var User=require('../../models/User.js');
var bodyParser=require('body-parser')
var app=express();
var multer=require('multer');
var cors=require('cors');
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true })); 
var constant=require('../../constants/constant.js')
var db=require('../../config/database.js');
process.env.SECRET_KEY='secret';
exports.adminRegister = function(req,res){
  bcrypt.hash(req.body.password, 5, function( err, bcryptedPassword) {
  var email=req.body.email;
  db.query("SELECT * FROM admin WHERE email = ?",[email], function(err, rows) {
               if(err){
                console.log(err)
              }
              if(rows.length!=0) {
                       res.send({
                "status":0,
                "message":constant.checkEmail
                })
                } else {
  var today = new Date();
  console.log(req.file);
  var users={
    "name":req.body.name,
    "email":req.body.email,
    "password":bcryptedPassword,
    "created_at":today,
    "updated_at":today
  }
  db.query('INSERT INTO admin SET ?',users, function (error, results) {
  if (error) {
    console.log("error ocurred",error)
    res.send({
      "status":0,
      "message":error
    })
  }
  else{
   // var result=JSON.parse(JSON.stringify(results[0]));
    res.send({
      "status":1,
      "message":constant.userSuc,

      });
  }
  });
}
});
});
}
exports.adminLogin=function(req,res){
  console.log(req.body);
    var email=req.body.email;
     var password=req.body.password;
  db.query('SELECT * FROM admin WHERE email = ?',[email], function (error, respnose, fields) {
    if(respnose.length >0){
      bcrypt.compare(password, respnose[0].password, function(err, doesMatch){
        if (doesMatch){
          var result=JSON.parse(JSON.stringify(respnose[0]));
        res.send({
       "status":1,
       "message":constant.LOGIN,
       "data":result
        });
      }
      else{
      res.send({
       "status": 0,
       "message": constant.LOGIN_FLASH
         });
      }
    });
    }else {
        res.send({
          "status": 0,
          "message":constant.NOEMAIL
        });
      }
    });
};
