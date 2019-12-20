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
var storage = multer.diskStorage({
   destination: (req, file, cb) => {
     cb(null, './assets/images')
   },
   filename: (req, file, cb) => {
     cb(null, file.image + '-' + Date.now() + path.extname(file.originalname))
   }
});
var upload = multer({storage: storage});

router.post("/addSupport", function(req,res){
 console.log(req.body);
  if(!req.body.user_pub_id || !req.body.sup_title || !req.body.sup_desc )
  {
    res.send({
      "status":0,
      "message":constant.chkFields
    })
  }
  else
  {
      var today = new Date();
      console.log(req.file);
      var data={
		"support_pub_id":uniqid(),
		"user_pub_id":req.body.user_pub_id,
		"title":req.body.sup_title,
		"description":req.body.sup_desc,
		"created_at":today,
		"updated_at" : today
	}
          
     db.query("INSERT INTO support_tbl SET ?",data,function(err,results,fields){
		 if(err){
		 	res.send({
		 		"status":0,
		 		"message":err
		 	});
		 }else{
		 	res.send({
		 		"status":1,
		 		"message":constant.ADD_SUPP,
		 	})
		 }
	});
}
});
router.post('/getAllSupport',function(req,res){
	var user_pub_id = req.body.user_pub_id;
db.query('SELECT * FROM  support_tbl Where user_pub_id=?',[user_pub_id],function(err,results){
	if(err){
		res.send({
			"status":0,
			"message":err
		});
	}else if(!results.length>0){
		res.send({
			"status":0,
			"message":constant.NOT_FOUND
		});
	}else{
		var results = JSON.parse(JSON.stringify(results));
		res.send({
			"status":1,
			"message":constant.GET_ALL_SUPP,
			"data":results
		})
	}
})
});




module.exports=router;