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
//http://52.77.32.209:3003/assets/images/undefined-1557571837658.png
//http://52.77.32.209:3003/assets/images/undefined-1557571837658.png
var base_url_admin=constant.base_url_admin;
var base_url=constant.base_url;
var storage = multer.diskStorage({
   destination: (req, file, cb) => {
     cb(null, './assets/images')
   },
   filename: (req, file, cb) => {
     cb(null, file.image + '-' + Date.now() + path.extname(file.originalname))
   }
});
var upload = multer({storage: storage});


router.post("/addRating", function(req,res){
 console.log(req.body);
   if(!req.body.pro_pub_id || !req.body.user_pub_id || !req.body.star || !req.body.comment)
  {
    res.send({
      "status":0,
      "message":constant.chkFields
    })
  }
  else{
    db.query('SELECT * from rating WHERE user_pub_id=? AND pro_pub_id=?',[req.body.user_pub_id,req.body.pro_pub_id],function(err,result){
      if(err){
        res.send({
          "status":0,
          "message":err
        })
      }else{
        var result=JSON.parse(JSON.stringify(result))
        if(result.length>0){
          db.query('Update rating SET star=? ,comment=? WHERE user_pub_id=? AND pro_pub_id=?',[req.body.star,req.body.comment,req.body.user_pub_id,req.body.pro_pub_id],function(err,result){
            if(err){
              res.send({
                "status":0,
                "message":err
              })
            }else{
              res.send({
                "status":1,
                "message":constant.update_rat
              })
            }

          })
        }else{
      var today = new Date();
      var data={
    "rating_id":uniqid(),
    "pro_pub_id":req.body.pro_pub_id,
    "user_pub_id":req.body.user_pub_id,
    "star":req.body.star,
    "comment":req.body.comment,
    "created_at":today
  }
          
     db.query("INSERT INTO rating SET ?",data,function(err,results,fields){
     if(err){
      res.send({
        "status":0,
        "message":err
      });
     }else{
      res.send({
        "status":1,
        "message":constant.AddRat
    
      })
     }
  });
}
      }
    })
   } 
  
});

// router.post("/getMyRating", function(req,res){
//  var user_pub_id = req.body.user_pub_id ;
//  var pro_pub_id = req.body.pro_pub_id ;
//         if (!req.body.user_pub_id || !req.body.pro_pub_id) {
//         res.json({
//             status: 0,
//             message: constant.chkFields
//         });
//         return;
//        } else {
//        db.query('SELECT r.*,u.name, CONCAT("'+base_url+'", u.image) as user_image from rating r JOIN users u ON r.user_pub_id=u.user_pub_id WHERE r.user_pub_id=? AND r.pro_pub_id=?',[user_pub_id,pro_pub_id],function(err,result){
//       if(err){
//         res.send({
//           "status":0,
//           "message":err
//         });
//       }
//         else
//         { 
//           if(result.length > 0){

//           result = JSON.parse(JSON.stringify(result));
//           res.send({
//           "status":1,
//           "message":constant.getRat,
//           "data":result
//         });
//         }
//         else{
//           res.send({
//           "status":0,
//           "message":constant.NODATA
//                  });
//             }
//         }
// });
// }
// });

router.post("/getAllRating", function(req,res){
 var user_pub_id = req.body.user_pub_id ;
 var pro_pub_id = req.body.pro_pub_id ;
        if (!req.body.pro_pub_id) {
        res.json({
            status: 0,
            message: constant.chkFields
        });
        return;
       } else {
       db.query('SELECT r.*,u.name, CONCAT("'+base_url+'", u.image) as user_image from rating r JOIN users u ON r.user_pub_id=u.user_pub_id WHERE r.pro_pub_id=?',[pro_pub_id],function(err,result){
      if(err){
        res.send({
          "status":0,
          "message":err
        });
      }
        else
        { 
          if(result.length > 0){

          result = JSON.parse(JSON.stringify(result));
          res.send({
          "status":1,
          "message":constant.getRat,
          "data":result
        });
        }
        else{
          res.send({
          "status":0,
          "message":constant.NODATA
                 });
            }
        }
});
}
});


module.exports=router;