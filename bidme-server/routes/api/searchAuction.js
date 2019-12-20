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
var base_url=constant.base_url+"subasta_assets/images/";
var storage = multer.diskStorage({
   destination: (req, file, cb) => {
     cb(null, './assets/images')
   },
   filename: (req, file, cb) => {
     cb(null, file.image + '-' + Date.now() + path.extname(file.originalname))
   }
});
var upload = multer({storage: storage});



router.post('/getAllSearchAuction',function(req,res){

	var search = req.body.search;
	var search1=search+'%'
db.query('select auction_cat.*,auction_pro.*,c.currency_code, CONCAT("'+base_url+'", auction_pro.image) as image,CONCAT("'+base_url+'", auction_cat.image) as cat_image FROM ((auction_cat INNER JOIN auction_pro ON auction_cat.cat_id = auction_pro.cat_id)JOIN currency_setting c ) where auction_pro.title LIKE ? OR auction_cat.cat_title LIKE ? AND c.status="1"',[search1,search1],function(err,results){
	//var result=JSON.parse(JSON.stringify(results))
	console.log(results)
	if(err){
		res.send({
			"status":0,
			"message":err
		});
	}else if(!results.length>0){
		res.send({
			"status":0,
			"message":constant.NOT_FOUND,
			"data":results
		});
	}else{

		res.send({
			"status":1,
			"message":constant.GET_ALL_DATA,
			"data":results
		})
	}
})
});





module.exports=router;