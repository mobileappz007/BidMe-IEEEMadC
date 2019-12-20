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
var base_url_admin=constant.base_url+"subasta_assets/images/";
var storage = multer.diskStorage({
   destination: (req, file, cb) => {
     cb(null, './assets/images')
   },
   filename: (req, file, cb) => {
     cb(null, file.image + '-' + Date.now() + path.extname(file.originalname))
   }
});
var upload = multer({storage: storage});
router.post("/addCategory" ,upload.single('image'), function(req,res){
 console.log(req.body);
 console.log(req.file);
  if(!req.body.cat_title || !req.body.cat_desc )
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
		"cat_id":uniqid(),
		"cat_title":req.body.cat_title,
		"cat_desc":req.body.cat_desc,
		"image":req.file.filename,
		"status":req.body.status,
		"created_at":today
	}
          
     db.query("INSERT INTO auction_cat SET ?",data,function(err,results,fields){
		 if(err){
		 	res.send({
		 		"status":0,
		 		"message":err
		 	});
		 }else{
		 	res.send({
		 		"status":1,
		 		"message":constant.addAdvertise,
		 	})
		 }
	});
}
});
router.get('/getAllCategory',function(req,res){
db.query('SELECT *,CONCAT("'+base_url_admin+'", image) as image FROM  auction_cat ',function(err,results){
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
		res.send({
			"status":1,
			"message":constant.getAllCategory,
			"data":results
		})
	}
})
});

router.post("/subAddCategory" ,upload.single('image'), function(req,res){
 console.log(req.body);
 console.log(req.file);
  if(!req.body.sub_cat_title || !req.body.sub_cat_desc )
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
		"cat_id":uniqid(),
		"sub_cat_title":req.body.cat_title,
		"sub_cat_desc":req.body.cat_desc,
		"sub_cat_image":req.file.filename,
		"status":req.body.status,
		"created_at":today
	}
          
     db.query("INSERT INTO sub_auction_cat SET ?",data,function(err,results,fields){
		 if(err){
		 	res.send({
		 		"status":0,
		 		"message":err
		 	});
		 }else{
		 	res.send({
		 		"status":1,
		 		"message":constant.addAdvertise,
		 	})
		 }
	});
}
});

router.post('/getSubAllCategory',function(req,res){
db.query('SELECT * FROM  auction_sub_cat where cat_id=?',[req.body.cat_id],function(err,results){
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
		res.send({
			"status":1,
			"message":constant.getAllCategory,
			"data":results
		})
	}
})
});
router.post('/getAllBrands',function(req,res){
db.query('SELECT * FROM  brand where sub_cat_id=?',[req.body.sub_cat_id],function(err,results){
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
		res.send({
			"status":1,
			"message":constant.getAllBrands,
			"data":results
		})
	}
})
});

router.post('/getAllModels',function(req,res){
db.query('SELECT * FROM  model where brand_id=?',[req.body.brand_id],function(err,results){
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
		res.send({
			"status":1,
			"message":constant.getAllModels,
			"data":results
		})
	}
})
});


module.exports=router;