var express=require('express');
var db=require('../../config/database.js');
var constant=require('../../constants/constant.js');
var uniqid = require('uniqid'); 
var router=express.Router();
var base_url=constant.base_url;
router.post('/addReports',function(req,res){
// var data={
// 	user_pub_id:req.body.user_pub_id,
// 	report_user_pub_id:req.body.report_user_pub_id,
// 	comment:req.body.comment
// }
db.query('INSERT INTO reports_tbl SET ?',req.body,function(err,results){
	if(err){
		res.send({
			"status":0,
			"message":err
		})
	}else{
		res.send({
				"status":1,
				"message":"Add report successfully."
		})
	}
})
})

module.exports=router;