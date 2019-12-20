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

process.env.SECRET_KEY='secret';
var multer = require('multer');

var storage = multer.diskStorage({
   destination: (req, file, cb) => {
     cb(null, './assets/images')
   },
   filename: (req, file, cb) => {
     cb(null, file.image + '-' + Date.now() + path.extname(file.originalname))
   }
});
var upload = multer({storage: storage});
router.post("/addAdvertise" ,upload.single('image'), function(req,res){
 console.log(req.body);
 console.log(req.file);
  if(!req.body.category || !req.body.title || !req.body.price || !req.body.description )
  {
    res.send({
      "status":0,
      "message":"Please check all fields."
    })
  }
  else
  {
      var today = new Date();
      console.log(req.file);
      var data={
        "add_pro_id":uniqid(),
        "category":req.body.category,   
        "image":req.file.filename,
        "title":req.body.title, 
        "price":req.body.price,
        "description":req.body.description,
        "created_at":today
      }    
      db.query('INSERT INTO add_product SET ?',data, function (error, results) {
      if (error) {
        console.log("error ocurred",error)
        res.send({
          "status":0,
          "message":error
        })
      }
      else{
        console.log('The solution is: ',results);
        res.send({
          "status":1,
          "message":constant.addAdvertise,        
          });
      }
      });
    }
    });
    

router.get('/getAllAdvertise',function(req,res){
  db.query('Select * from add_product',function(err,results,fields){
    if(err){
      res.send({
        "status":0,
        "message":err
      })
    }else{
      if(results!=0){
        res.send({
          "status":1,
          "message":constant.get_all_pro,
          "data":results
        })
      }else{
        res.send({
          "status":0,
          "message":constant.get_pro
        
        })
      }
    }
  })
  })
module.exports=router;