var express=require('express');
var db=require('../../config/database.js');
var constant=require('../../constants/constant.js');
var uniqid = require('uniqid'); 
var router=express.Router();
var base_url=constant.base_url;
router.post('/addFavourite',function(req,res){
if(!req.body.pro_pub_id ||!req.body.user_pub_id){
res.send({
	"status":0,
	"message":constant.CHKFIELD
})
}
else{
	var user_pub_id=req.body.user_pub_id;
	db.query('SELECT * FROM users WHERE user_pub_id=?',[user_pub_id],function(err,results){
		var result=JSON.parse(JSON.stringify(results))
		console.log(result)
		if(!err){
		console.log(result)
			
			if(result.length==1){
	

	db.query('SELECT * FROM favourite WHERE isFavourite="1" AND user_pub_id="'+req.body.user_pub_id+'" AND pro_pub_id="'+req.body.pro_pub_id+'"',function(err,results){
var result=JSON.parse(JSON.stringify(results))
console.log(result)
console.log(results)

if(result.length==0){
		var today=new Date();;
		console.log(today);	
			db.query('INSERT INTO favourite SET pro_pub_id="'+req.body.pro_pub_id+'",created_at=?,user_pub_id="'+req.body.user_pub_id+'",isFavourite="1"',[today],function(err,results){
		if(!err){
			console.log('5')
			res.send({
				"status":1,
				"message":constant.AddFAV
			})

		}else{
			console.log('4')
			res.send({
				"status":0,
				"message":err
			})
		}

	})
		}else{
			console.log('6')
			res.send({
				"status":0,
				"message":constant.AlreadyAdd
			})
		}
		
	})
	
	}else{
		console.log('7')
		res.send({
			"status":0,
			"message":constant.Incur
		})
	}		
		}else{
			console.log('8')
			res.send({
				"status":0,
				"message":err
			})
		}
	})	
}
})
router.post('/unFavourite',function(req,res){
var pro_pub_id=req.body.pro_pub_id;
 var user_pub_id=req.body.user_pub_id;
if(!req.body.pro_pub_id||!req.body.user_pub_id){
	res.send({
		"status":0,
		"message":constant.CHKFIELD
	})

}
	else{
	db.query('DELETE FROM favourite WHERE user_pub_id = "'+user_pub_id+'" AND pro_pub_id="'+pro_pub_id+'" ',function(err,results){
		if(err){
			console.log('err',err);
			res.send({
				"status":0,
				"message":err
			})
		}else{

			res.send({
				"status":1,
				"message":constant.UNFAV
			})
		}
	})
}
})

router.post('/getMyFavourite',function(req,res){
	var user_pub_id=req.body.user_pub_id;
	var result_array = [];
	var counter = 0;
	db.query('SELECT f.* ,p.title,p.pro_pub_id,p.price,c.currency_code, CONCAT("'+base_url+'",p.image) as image FROM ((favourite f INNER JOIN auction_pro p ON f.pro_pub_id=p.pro_pub_id ) JOIN currency_setting c) where c.status="1" AND f.user_pub_id=? ',[user_pub_id],function(err,rows){
		if(err){
			res.send({
				"status":0,
				"message":err
			})

		}
		else{
			
			
		if(rows.length>0){

			   console.log(rows)
            	var row=JSON.parse(JSON.stringify(rows))
            	console.log('Length::'+rows.length)
           row.forEach(function(row) {
           		db.query('SELECT CONCAT("'+base_url+'",image) as image,pro_pub_id FROM auction_pro_img WHERE pro_pub_id="'+row.pro_pub_id+'"',function(err,images){
           			if(err){
           				console.log(err);
           			}
           			else{
           			var images=JSON.parse(JSON.stringify(images))
row.image_cust = images;
result_array.push(row);
counter++;
console.log(row.image_cust)
if(counter== rows.length){

res.send({
		"status":1,
		"message":constant.get_All_Aunc,
		"data":result_array
	});
}
        }
  		})
		});	
      }else{
			res.send({
				"status":0,
				"message":constant.NOT_FOUND
			})
		}
		}
	})
})
module.exports=router;