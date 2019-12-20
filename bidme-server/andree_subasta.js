var express=require('express');
var app=express();
var router=express.Router();
var path=require('path');
var cors=require('cors');
var cookeParser =require('cookie-parser');
var bodyparser =require('body-parser');
var db=require('./config/database.js');
const users = require("./routes/api/users.js");
const admin = require("./routes/api/admin.js");
const auction=require("./routes/api/aunctions.js");
const product=require("./routes/api/post_ads.js");
const category=require("./routes/api/category.js");
const bid=require('./routes/api/bid_info.js');
const register=require('./routes/api/register.js');	
const subscription=require('./routes/api/userAddPackage.js');
const favourite=require('./routes/api/favourite.js');
const notification=require('./routes/api/notification.js')
const support=require('./routes/api/support.js')
const rating=require('./routes/api/ratting.js')
const search=require('./routes/api/searchAuction.js');
const chat=require('./routes/api/chat.js');
const getMyAuction=require('./routes/api/GetMyAuctions.js');
const report=require('./routes/api/reports.js');
const BASEURL="http://localhost/"
app.use(cors());
app.use(bodyparser.json());

app.use(bodyparser.urlencoded({
	extended:true
})
);
app.use(express.static(path.join(__dirname, 'public')));	
//app.use(fileUpload());
app.use(cookeParser());

router.get("/GetAllSubsPackage",function(req,res){
   db.query('SELECT * ,currency_setting.currency_code FROM subscription_package INNER JOIN currency_setting  ON subscription_package.curr_pub_id=currency_setting.curr_pub_id',function(err,results)
   {
 
    if(err){
      res.send({
        "status":0,
        "message":err
      });
    }else{
      var result=JSON.parse(JSON.stringify(results));
      res.send({
        "status":1,
        "message":"Get All Package",
        "data":result
      })
    }
  })
});
router.get('/getAllData',function(req,res){
  db.query('SELECT * ,CONCAT("'+BASEURL+'", image) as image from auction_cat;SELECT *  FROM auction_sub_cat;SELECT *  FROM brand;SELECT *  FROM model',function(err,results){
    if(err){
      res.json({
        "status":0,
        "message":err
      })
    }
      else{
          var result,category,subcategory,brand,model;
              var result1=JSON.parse(JSON.stringify(results[0]));
              var result2=JSON.parse(JSON.stringify(results[1]));
              var result3=JSON.parse(JSON.stringify(results[2]));
              var result4=JSON.parse(JSON.stringify(results[3]))
        //result1[0].category=result1;

        res.send({
          "status":1,
          "message":"all data fetch",
          "data":{category:result1,subcategory:result2,brand:result3,model:result4}
        })

      }

    
  })
})
// Routes
// router.post('/register',users.register);
router.post('/login',users.login);
router.post('/adminRegister',admin.adminRegister);
router.post('/adminLogin',admin.adminLogin)
//router.post('/addAuction',auction.addAuction);
//router.get('/getAllAuctions',auction.getAllAuctions);
//router.post('/addAuctionCat',category.auctionCat);
// router.post('/addProduct',product.addproduct);
//router.get('/getAllAdvertise',product.getAllAdvertise);
//router.get('/getAllCategory',category.getAllCategory);
// router.post('/addBid',bid.addBid);
// router.post('/getMyBid',bid.getMyBid);
// router.post('/delete_bid',bid.delete_bid);
// router.post('/update_bid',bid.update_bid);
app.use('/api',bid);
app.use('/api',report);
app.use('/api',search)
app.use('/api',category);
app.use('/api',product);
app.use('/api',register);
app.use('/api',auction);
app.use('/api',router);
app.use('/api',subscription);
app.use('/api',notification);
app.use('/api',favourite)
app.use('/api',rating);
app.use('/api',support);
app.use('/api', chat);
app.use('/api',getMyAuction);
app.listen(7017,function(err){
if (!err) 
  console.log('listening port no 7017');
else{
	console.log(err);
}
});
