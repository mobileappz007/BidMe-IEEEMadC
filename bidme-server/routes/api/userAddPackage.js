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

router.post("/userAddPackage",function(req,res){
 var today=new Date();
  var data={
    "invoice_id":uniqid(),
    "package_pub_id":req.body.package_pub_id,
    "user_pub_id":req.body.user_pub_id,
    "name":req.body.name,
    "curr_pub_id":req.body.curr_pub_id,
    "discount":req.body.discount,
    "total_price":req.body.total_price,
    "final_price":req.body.final_price,
    "tax":req.body.tax,
    "start_date":req.body.start_date,
    "end_date":req.body.end_date,
    "created_at":today,
    "updated_at":today
  }
  db.query('INSERT INTO subscription_history SET ?',[data],function(err,results){
    if(err){
      res.send({
        "status":0,
        "message":err
      });
    }else{
      res.send({
        "status":1,
        "message":constant.addPackage
      })
    }
  })
});
router.get("/GetAllSubsPackage",function(req,res){
  db.query('SELECT currency_setting.currency_code FROM subscription_package INNER JOIN currency_setting ON subscription_package.curr_pub_id=currency_setting.curr_pub_id',function(err,results){
    if(err){
      res.send({
        "status":0,
        "message":err
      });
    }else{
      console.log(result)
      var result=JSON.parse(JSON.stringify(results));
      res.send({
        "status":1,
        "message":constant.getPackage,
        "data":result
      })
    }
  })
});
router.post('/addsubscription',function(req,res){
  if(!req.body.package_pub_id||!req.body.user_pub_id){
    res.send({
      "status":0,
      "message":constant.CHKFIELD
    })
  }
  else{
    var id =req.body.package_pub_id;
    db.query('SELECT * FROM subscription_package WHERE package_pub_id=?',[id],function(err,results){
      if(err){
        res.send({
          "status":0,
          "message":err
        })
}else{
        var result=JSON.parse(JSON.stringify(results[0]))
console.log(result.package_name)
console.log(result.price)
        if(result.length!=0){
          console.log('ssss')


var total_days=result.total_days;
var firstDate=new Date();
console.log(total_days)
 
//console.log(v)
//var secondDate=new Date(0000,00,v);
// console.log(secondDate)
  var data={
  invoice_id:uniqid(),
  package_pub_id:req.body.package_pub_id,
  user_pub_id:req.body.user_pub_id,
  total_price :result.price,
  start_date:firstDate,
  end_date :total_days,
  created_at  :firstDate,
  updated_at:firstDate
}
db.query('INSERT INTO subscription_history SET ?',[data],function(err,results){
  if(err){
    res.send({
      "status":0,
      "message":err
    })
  }else{
    res.send({
      "status":1,
      "message":constant.buyPackage
    })
  }
})
}else{
     res.send({
            "status":0,
            "message":"no data found"
          })
        }
      }
      
    })
}
})
router.post("/subscription_history",function(req,res){
  var id=req.body.user_pub_id;
  
 db.query('SELECT subscription_history.*,DATE_ADD(subscription_history.start_date,INTERVAL 31 DAY) as end_date,subscription_history.invoice_id,subscription_history.total_price,subscription_history.discount,subscription_history.tax,subscription_history.final_price,subscription_history.start_date,subscription_package.package_name,currency_setting.currency_code FROM ((subscription_package RIGHT JOIN subscription_history ON subscription_history.package_pub_id=subscription_package.package_pub_id) INNER JOIN currency_setting) WHERE currency_setting.status="1" AND subscription_history.user_pub_id=?',[id],function(err,results){
   if (err)
  {
    //req.flash("msg", "Error In User update");
    console.log(err);
    res.send({
      "status":0,
      "message":err
    })
  }
  else
  {
    console.log('subscription_package')
    //req.flash("msg", "Successfully User UPDATE");
    var result=JSON.parse(JSON.stringify(results))
    if(result.length>0){
    
    res.send({
      "status":1,
      "message":"Get all subscription package.",
      "data":result
    })
  }else{
    res.send({
      "status":0,
      "message":constant.NODATA
    })
  }
  }
 })
});

// router.post("/current_subscription",function(req,res){
//   var id=req.body.user_pub_id;
  
//  db.query('SELECT subscription_history.*,DATE_ADD(subscription_history.start_date,INTERVAL 31 DAY) as end_date from subscription_history  WHERE  subscription_history.user_pub_id=?',[id],function(err,results){
//    if (err)
//   {
//     //req.flash("msg", "Error In User update");
//     console.log(err);
//     res.send({
//       "status":0,
//       "message":err
//     })
//   }
//   else
//   {
//     console.log('subscription_package')
//     //req.flash("msg", "Successfully User UPDATE");
//     var result=JSON.parse(JSON.stringify(results))
//     if(result.length>0){
//       db.query('SELECT subscription_history.*,DATE_ADD(subscription_history.start_date,INTERVAL 31 DAY) as end_date,subscription_package.package_name,currency_setting.currency_type FROM ((subscription_package RIGHT JOIN subscription_history ON subscription_history.package_pub_id=subscription_package.package_pub_id) INNER JOIN currency_setting) WHERE  start_date >= "'+today+'" AND end_date <= 'DATE_ADD(subscription_history.start_date,INTERVAL 31 DAY)' AND currency_setting.status="1" AND subscription_history.user_pub_id=?',[req.body.user_pub_id],function(err,results){
//         if(err){
//           res.send({
//             "status":0,
//             "message":err
//           })
//         }else{
//           var result1=JSON.parse(JSON.stringify(results))
//           if(result1.length>0){
//            res.send({
//       "status":1,
//       "message":"All subscription",
//       "data":result1
//     })
// }else{
//   "status":0,
//   "message":"No Data"
// }
//         }
//       })
  
//   }else{
//     res.send({
//       "status":0,
//       "message":"No data found"
//     })
//   }
//   }
//  })
// });

router.post('/current_subscription',function(req,res){
  var user_pub_id=req.body.user_pub_id;
 var d=new Date();
 var yyyy=d.getFullYear();
 var mm=d.getMonth();
 var m1=mm+1;
 var dd=d.getDate()
 var today=yyyy + '-' + m1 + '-' + dd;
  console.log(today);
db.query('SELECT * from subscription_history WHERE user_pub_id="'+user_pub_id+'" ORDER BY created_at DESC limit 1',function(err,results){
if(!err){
  var result=JSON.parse(JSON.stringify(result));
   var da=result[0].start_date;
   var yyyy=da.getFullYear();
 var mm=da.getMonth();
 var m1=mm+1;
 var dd=da.getDate()
 var start_date=yyyy + '-' + m1 + '-' + dd;

}  
})
  db.query("SELECT s1.package_name ,DATE_ADD(s.start_date,INTERVAL 31 DAY) as end_date,s1.auction_count ,s.invoice_id,s.total_price,s.start_date,currency_setting.currency_code FROM ((subscription_package s1 RIGHT JOIN subscription_history s ON s.package_pub_id=s1.package_pub_id) INNER JOIN currency_setting) WHERE currency_setting.status='1' AND  s.user_pub_id='"+user_pub_id+"' AND '"+today+"' BETWEEN s.start_date AND 'DATE_ADD(s.start_date,INTERVAL 31 DAY)' ORDER BY s.created_at DESC limit 1 ",function(err,results){
if(err){
  res.send({
    "status":0,
    "message":err
  })
}else{
  var result=JSON.parse(JSON.stringify(results))
  console.log(result[0]);
  if(result.length>0){
    res.send({
      "status":1,
      "message":constant.CurrHis,
      "data":result[0]
    })
  }else{
    res.send({
      "status":0,
      "message":constant.NODATA      
  })
}
}
})

})
module.exports=router;