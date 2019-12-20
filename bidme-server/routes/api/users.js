var express=require('express');
var uniqid = require('uniqid'); 
var bcrypt=require('bcrypt');
//var User=require('../../models/User.js');
var bodyParser=require('body-parser')
var app=express();
var router=express.Router();
var cors=require('cors');
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true })); 
var constant=require('../../constants/constant.js')
var db=require('../../config/database.js');

process.env.SECRET_KEY='secret';
var multer = require('multer');

var base_url =constant.base_url;

var path=require('path');
var storage = multer.diskStorage({
   destination: (req, file, cb) => {
     cb(null, './uploads')
   },
   filename: (req, file, cb) => {
    console.log(file)
     cb(null, file.image + '-' + Date.now() + path.extname(file.originalname))
   }
});
var upload = multer({storage: storage});

var upload = multer({storage: storage});
router.post("/register" ,upload.single('image'), function(req,res){
 console.log(req.body);
 console.log(req.file);
  if(!req.body.email || !req.body.country_code || !req.body.name || !req.body.mobile_no ||!req.body.password)
  {
    res.send({
      "status":0,
      "message":"Please check all fields."
    })
  }
  else
  {
      bcrypt.hash(req.body.password, 5, function( err, bcryptedPassword) {
      var email=req.body.email;
      var mobile_no=req.body.mobile_no;
      db.query("SELECT * FROM users WHERE mobile_no=? AND email = ?",[mobile_no,email], function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  if(rows.length!=0) {
                           res.send({
                    "status":0,
                    "message":constant.checkEmail
                    })
                    } else {
      var today = new Date;
      console.log(req.file);
      var users={
        "user_pub_id":uniqid(),
        "country_code":req.body.country_code,   
        "image":req.file,
        "name":req.body.name, 
        "email":req.body.email,
        "password":bcryptedPassword,
        "mobile_no":req.body.mobile_no,
        "created_at":today
      }    
      db.query('INSERT INTO users SET ?',users, function (error, results) {
      if (error) {
        console.log("error ocurred",error)
        res.send({
          "status":0,
          "message":error
        })
      }
      else{
       // var result=JSON.parse(JSON.stringify(results[0]));
        console.log('The solution is: ',results);
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
}); 





exports.login=function(req,res){
    var name=req.body.name;
     var password=req.body.password;
     var deviceid=req.body.device_id;
     var devicetype=req.body.device_type;
     var devicetoken=req.body.device_token;
     var verified=1
  db.query('SELECT * FROM users WHERE name = ?',[name], function (error, respnose) {
    if(respnose.length >0){
       var respnose1=JSON.parse(JSON.stringify(respnose));
   if(respnose1[0].status==1){
  
      bcrypt.compare(password, respnose[0].password, function(err, doesMatch){
        if (doesMatch){
          var result=JSON.parse(JSON.stringify(respnose[0]));
          result.image = base_url + result.image;
        db.query("UPDATE users SET device_token=? WHERE name=?",[req.body.device_token,name],function(err,results){
          if (err) {
                    res.send({
       "status":1,
       "message":err
        });
          }else{
            var result1=JSON.parse(JSON.stringify(respnose[0]));
            result1.image =  result.image;
               res.send({
       "status":1,
       "message":constant.LOGIN,
       "data":result1
        });
          }

        })
      }
      else {
        res.send({
       "status": 0,
       "message": constant.LOGIN_FLASH
         });
      }
    
    });
    }else {
       res.send({
          "status": 0,
          "message":"Please, Approve your profile by admin."
        });
        
      }
    }else{
      res.send({
          "status": 0,
          "message":constant.NOEMAIL
        });
       
      
    }
    });
};
