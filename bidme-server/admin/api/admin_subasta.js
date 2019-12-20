var express = require('express');
var bodyParser = require('body-parser');
var app = express();
var session = require('express-session');
var flash=require('express-flash');
var uniqid=require('uniqid');
var db=require('../../config/database.js');
var bcrypt=require('bcryptjs')
app.set('view engine','ejs');
app.use(express.static(__dirname ));

app.use(bodyParser.urlencoded({ 
  extended: true
}));
app.use(bodyParser.json());

app.use(session({
  secret: 'secret',
  resave: true,
  saveUninitialized: true
  }));
app.use(flash());
var constant=require('../../constants/constant.js')

app.get('/',(req,res) => {
    sess = req.session;
    if(sess.email) {
        return res.redirect('/dashboard');
    }
        return res.redirect('/adminLogin');
});


app.get('/adminLogin',function(req,res){
  var pageData={msg1:req.flash("msg1"),msg2:req.flash("msg2")};
  res.render('login',pageData);
});

app.get('/logout',(req,res) => {
    req.session.destroy((err) => {
        if(err) {
            return console.log(err);
        }
        res.redirect('/');
    });

});

// app.get('/dashboard',function(req,res){
//   db.query('SELECT count(id) as id from auction_pro;SELECT count(user_pub_id) as id from users;SELECT count(package_pub_id) as id from subscription_package;SELECT * from users',function(err,results){
//     if(!err){
// var totalRec = 0;
// var start       = 0;
// var currentPage = 1;
// var pageSize  = 10;
// var pageCount = 0;

//       var result=JSON.parse(JSON.stringify(results[0]));
//       var result1=JSON.parse(JSON.stringify(results[1]));
//       var result2=JSON.parse(JSON.stringify(results[2]));
//       var result3=JSON.parse(JSON.stringify(results[3]));
//   res.render("dash",{result:result[0].id,result1:result1[0].id,result2:result2[0].id,result3:result3});
// }
// }); 
// }); 

app.get('/dashboard',function(req,res){
  if(req.session.email){
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 7;
var pageCount = 0;

db.query('SELECT count(*) as count from users',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);

         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }
       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
      }
  db.query('SELECT count(id) as id from auction_pro;SELECT count(user_pub_id) as id from users;SELECT count(package_pub_id) as id from subscription_package;SELECT * from users LIMIT '+pageSize+' OFFSET '+start+' ;SELECT count(invoice_id) as id from subscription_history',function(err,results){
    if(err){
      console.log(err);
      
    }
    else{

      var result=JSON.parse(JSON.stringify(results[0]));
      var result1=JSON.parse(JSON.stringify(results[1]));
      var result2=JSON.parse(JSON.stringify(results[2]));
      var result3=JSON.parse(JSON.stringify(results[3]));
      var result4=JSON.parse(JSON.stringify(results[4]));
  res.render("dash",{result:result[0].id,result1:result1[0].id,result2:result2[0].id,result3:result3,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage,result4:result4[0].id});
}
}); 

}
}); 
}else{
  res.redirect('/adminLogin');
}
});


app.post('/adminRegister',function(req,res){
   bcrypt.hash(req.body.password, 5, function( err, bcryptedPassword) {
  var email=req.body.email;
  db.query("SELECT * FROM admin WHERE email = ?",[email], function(err, rows) {
               if(err){
                console.log(err)
              }
              if(rows.length!=0) {
                       res.send({
                "status":0,
                "message":constant.checkEmail
                })
                } else {
        var today = new Date();
        var users={
    "name":req.body.name,
    "email":req.body.email,
    "password":bcryptedPassword,
    "cnfm_pswrd":req.body.mobile_no,
    "created_at":today,
    "updated_at":today
  }
  db.query('INSERT INTO admin SET ?',users, function (error, results) {
  if (error) {
    res.send({
      "status":0,
      "message":error
    })
  }
  else{
    res.redirect('/');
  }
  });
}
});
});
});

app.post("/adminLogin", function(req, res){
  // console.log(req.body);
  var sess=req.session;
  sess.email=req.body.email;
  var email =  req.body.email ;
  db.query("SELECT * FROM admin WHERE email=?",[email], function(error, result, fields) {


    if(result.length==0)
    {
      req.flash("msg1", "This email is Incorrect");
      res.redirect("/adminLogin");
    }
    else
    {
      var result=JSON.parse(JSON.stringify(result[0]));
  
      if(result.password == req.body.password)
      {
        req.session.email = result.email;
        req.session.is_user_logged_in=true;
        res.redirect("/dashboard");
      }
      else
      {
        req.flash("msg2", "This Password is Incorrect");
        res.redirect("/adminLogin");
      }

    }
  });
});
app.get('/addModel',function(req,res){
 db.query('SELECT * FROM auction_cat',function(err,results){
  if(!err){
    var result=JSON.parse(JSON.stringify(results))
    res.render('addModel',{result:result});
  }
 else{
  console.log(err);
 }
})
});
app.get('/addBrand',function(req,res){
  db.query('SELECT * FROM auction_cat',function(err,results){
    if(!err){
      var result=JSON.parse(JSON.stringify(results))
      res.render('addBrand',{resultB:result})
    }
    else{
     console.log(err);    
    }
  })
})
app.post('/addBrand1',function(req,res){
var id=req.body.sub_cat_id;
db.query('SELECT * FROM brand WHERE sub_cat_id=?',[id],function(err,results){
  if(!err){
   res.header('Access-Control-Allow-Origin', req.headers.origin || "*");
          res.header('Access-Control-Allow-Methods', 'GET,POST,PUT,HEAD,DELETE,OPTIONS');
          res.header('Access-Control-Allow-Headers', 'content-Type,x-requested-with');
         res.send(results);
  }
 else{
  console.log(err);
 }
})
})

app.post("/addBrand" , function(req,res){
 console.log(req.body);
 var today = new Date();
      var data={
    "brand_id":uniqid(),
    "cat_id":req.body.cat_id,
    "sub_cat_id":req.body.sub_cat_id,
    "brand_name":req.body.brand_name,  
  }
   db.query("INSERT INTO brand SET ?",data,function(err,results,fields){
     if(!err){
       //res.flash("msg","Error in adding auction");
       console.log(req.body);
        res.redirect("/addBrand")
    }
    else{
     //res.flash("msg","Error in adding auction");
      console.log(err); 
        res.redirect("/addBrand");
    }
  });
});

//-----------AUCTION---------------
app.get('/addModel',function(req,res){
 db.query('SELECT * FROM auction_cat',function(err,results){
  if(!err){
    var result=JSON.parse(JSON.stringify(results))
    res.render('addModel',{result:result});
  }
 else{
  console.log(err);
 }
})
});
app.post('/addModel1',function(req,res){
var id=req.body.brand_id;
db.query('SELECT * FROM model WHERE brand_id=?',[id],function(err,results){
  if(!err){
   res.header('Access-Control-Allow-Origin', req.headers.origin || "*");
          res.header('Access-Control-Allow-Methods', 'GET,POST,PUT,HEAD,DELETE,OPTIONS');
          res.header('Access-Control-Allow-Headers', 'content-Type,x-requested-with');
         res.send(results);
  }
 else{
  console.log(err);
 }
})
});
app.post("/addModel",function(req,res){
 console.log(req.body);
 var today = new Date();
      var data={
    "model_id":uniqid(),
    "cat_id":req.body.cat_id,
    "sub_cat_id":req.body.sub_cat_id,
    "brand_id":req.body.brand_id,
    "model_name":req.body.model_name,  
  }
   db.query("INSERT INTO model SET ?",data,function(err,results,fields){
     if(!err){
       //res.flash("msg","Error in adding auction");
       console.log(req.body);
        res.redirect("/addModel")
    }
    else{
     //res.flash("msg","Error in adding auction");
      console.log(err); 
        res.redirect("/addModel");
    }
  });
});

// app.get('/viewAuction',function(req,res){
//   db.query('SELECT * FROM auction_pro',function(err,results){
// if(!err){
//   var result=JSON.parse(JSON.stringify(results));
//   res.render("viewAuction",{result:result});
// }
//   })
  
// }); 

app.get('/viewAuction',function(req,res){
  console.log('balram')
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
db.query('SELECT count(*) as count from auction_pro',function(err,results){
  if(err){
    console.log(err);
  } else{
    var results=JSON.parse(JSON.stringify(results))
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }
       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
      }
     db.query('SELECT *  from auction_pro LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        console.log('sssss')
        var result=JSON.parse(JSON.stringify(result));
        console.log(result);
         res.render('viewAuction',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
    }
    })
    }
     }   
  })
})

app.get('/viewCat',function(req,res){
  console.log('balram')
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
db.query('SELECT count(*) as count from auction_cat',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }
       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
      }
     db.query('SELECT *,CONCAT("'+constant.base_url_admin_for_image+'",image) as image from auction_cat LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        console.log('sssss')
        var result=JSON.parse(JSON.stringify(result));
        console.log(result);
         res.render('viewCat',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
    }
    })
    }
        
  })
})
app.get('/viewAllSupport',function(req,res){
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
db.query('SELECT count(*) as count from support_tbl',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }
       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
      }
     db.query('SELECT * from support_tbl LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        console.log('sssss')
        var result=JSON.parse(JSON.stringify(result));
        console.log(result);
         res.render('viewAllSupports',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
    }
    })
    }
        
  })
})

app.get('/viewAllReports',function(req,res){
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
console.log('ss')
db.query('SELECT count(*) as count from reports_tbl',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }
       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
      }
     db.query('SELECT r.*,u.name as user_name from reports_tbl r JOIN users u On r.user_pub_id=u.user_pub_id LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        var result=JSON.parse(JSON.stringify(result));
         res.render('viewAllReports',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
    }else{
      console.log(err);
    }
    })
    }
        
  })
})



app.post('/updateSupport',function(req,res){
  var status=req.body.status;
  console.log(req.body);
  var id=req.body.support_pub_id;
  db.query('UPDATE support_tbl set status="'+req.body.status+'" WHERE support_pub_id="'+id+'"',function(err,results){
    console.log(err);
    if(!err){

      console.log('540')
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
db.query('SELECT count(*) as count from support_tbl',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }
       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
      }
     db.query('SELECT * from support_tbl LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        var result=JSON.parse(JSON.stringify(result));
        console.log(result);
         res.render('viewAllSupports',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
    }
    })
    }
        
  })
    }
  })
})


app.post('/updateReport',function(req,res){
  var status=req.body.status;
 console.log(req.body);
  var id=req.body.report_user_pub_id;
  db.query('UPDATE reports_tbl set status="'+req.body.status+'" WHERE report_user_pub_id="'+id+'"',function(err,results){
    if(!err){
      var statuss=0;
      db.query('UPDATE users set status="'+statuss+'" WHERE user_pub_id="'+id+'"',function(err,results){
        if(!err){
        db.query('UPDATE auction_pro set status="1" WHERE user_pub_id="'+id+'"',function(err,results){

        })
      }
      })
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
db.query('SELECT count(*) as count from reports_tbl',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
       
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }
       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
      }
     db.query('SELECT r.* ,u.user_name from reports_tbl r JOIN users u ON u.user_pub_id=r.user_pub_id LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        var result=JSON.parse(JSON.stringify(result));
         res.render('viewAllReports',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
    }
    })
    }
  })
  }
  })
})



app.get('/viewSubCat',function(req,res){
  console.log('balram')
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
db.query('SELECT count(*) as count from auction_sub_cat',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }
       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
      }
     db.query('SELECT * from auction_sub_cat LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        console.log('sssss')
        var result=JSON.parse(JSON.stringify(result));
        console.log(result);
         res.render('viewSubCat',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
    }
    })
    }
        
  })
})

app.get('/viewUsers',function(req,res){
         console.log('balram')
         
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
      db.query('SELECT count(*) as count from users',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }

       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
    }
     db.query('SELECT  *,CONCAT("'+constant.base_url+'", image) as image from users LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        console.log('sssss')
        var result=JSON.parse(JSON.stringify(result));
        console.log(result);
         res.render('viewUsers',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
      }
     })
        }else{
          console.log(err);
        }
        
      })
})
// app.get('/viewUsers',function(req,res){
//   db.query('SELECT * FROM users',function(err,results){
// if(!err){
//   var result=JSON.parse(JSON.stringify(results));
//   res.render("viewUsers",{result:result});
// }
//   })
  
// });
app.get('/addAuction',function(req,res){
db.query('SELECT * FROM auction_cat;SELECT * FROM auction_pro',function(err,results){
  if(!err){
    var result=JSON.parse(JSON.stringify(results))
    res.render('addAuction',{result:result[0],resultC:result[1]});
  }
 else{
  console.log(err);
 }
})
 });
app.post('/addAuction1',function(req,res){
  var id=req.body.cat_id;
db.query('SELECT * FROM auction_sub_cat WHERE cat_id=?',[id],function(err,results){
  if(!err){
   res.header('Access-Control-Allow-Origin', req.headers.origin || "*");
          res.header('Access-Control-Allow-Methods', 'GET,POST,PUT,HEAD,DELETE,OPTIONS');
          res.header('Access-Control-Allow-Headers', 'content-Type,x-requested-with');
         res.send(results);
  }
 else{
  console.log(err);
 }
})
 });
// app.get('/addAuction',function(req,res){
 
//  db.query('SELECT * FROM  auction_cat',function(err,results){
//   if(!err){
//     var result=JSON.parse(JSON.stringify(results));
//     res.render('addAuction',{result:result});
//   }
//   else{
//    console.log(err);
//   res.redirect('/addAuction');
//   }
//  })
// });
var multer = require('multer');
var path=require('path');
var storage = multer.diskStorage({ //multers disk storage settings
   destination: function (req, file, cb) {
     cb(null, '../../../../../var/www/html/subasta_assets/images')
    },
    filename: function(req, file, cb) {
        var datetimestamp = Date.now();
        cb(null, file.fieldname + '-' + datetimestamp + '.' + file.originalname.split('.')[file.originalname.split('.').length - 1]);
    }
});

var upload = multer({storage: storage});
app.post("/addAuction" ,upload.single('image'), function(req,res){
  console.log(req.body);
  var today=new Date();
  var data={
    "pro_pub_id":uniqid(),
    "cat_id":req.body.cat_id,
    "sub_cat_id":req.body.sub_cat_id,
    "brand_id":req.body.brand_id,
    "model_id":req.body.model_id,
    "image":req.file.filename,
    "title":req.body.title,
    "price":req.body.price,
    "address":req.body.address,
    // "latitude":req.body.latitude,
    // "longitude":req.body.longitude,
    "description":req.body.description,
    "s_date":req.body.s_date,
    "e_date":req.body.e_date,
    // "no_of_owner":req.body.no_of_owner,
    "insured":req.body.insured,
    "created_at":today,
  }
  db.query('INSERT INTO auction_pro SET ?',data,function(err,results){
    if(!err){
       //res.flash("msg","Error in adding auction");
       console.log(req.body);
        res.redirect("/addAuction")
    }
    else{
     //res.flash("msg","Error in adding auction");
      console.log(err); 
        res.redirect("/addAuction");
    }
  });

});
//=================edit auction=======================

app.get("/edit_auction/:pro_pub_id", function(req,res){
    var id = req.params.pro_pub_id;
    console.log(id);
   db.query("SELECT * FROM auction_pro WHERE pro_pub_id=?",[id], function(err,result){
  
         if(err)
         {
          console.log("Finding error",err);
           return; 
         }
         result= JSON.parse(JSON.stringify(result[0]));
          console.log(result);

          
         res.render("edit_auction", {result:result});
        });
 });


/* url of update user */
app.post("/update_auction", upload.single('image'),function(req,res){

    var sql = "UPDATE auction_pro set title =? ,image=?,brand_name=?, model_name=? ,price=?, address =?,description=? WHERE pro_pub_id = ?";
  req.body.image = "subasta_assets/images/" + req.file.filename
    var query = db.query(sql, [req.body.title,req.file.filename,req.body.brand_name, req.body.model_name,req.body.price,req.body.address,req.body.description, req.body.pro_pub_id], function(err, result) {
   if (err)
  {
    //req.flash("msg", "Error In User update");
    res.redirect("/edit_auction");
  }
  else
  {
    //req.flash("msg", "Successfully User UPDATE");
    res.redirect("/addAuction");
  }
     
});
});

app.get("/delete_auction/:pro_pub_id",function(req,res){
  var id = req.params.pro_pub_id;
  console.log(id);
  var sql = "DELETE FROM auction_pro WHERE pro_pub_id = ?";
 
  db.query(sql, [id], function(err, result) {
    if (err)
  {
    //req.flash("msg", "Error In User delete");
    console.log(err);
    res.redirect("/addAuction");
  }
  else
  {
    //req.flash("msg", "Successfully User Deleted");
    console.log('delete Successfully')
    res.redirect("/addAuction");
  }
});
  });

//==============add Category==========================
app.get('/addCat',function(req,res){
  db.query('SELECT * FROM auction_cat',function(err,results){
    if(!err){
    var result=JSON.parse(JSON.stringify(results))
    res.render('addCategory',{result:result});
  }
 else{
  console.log(err);
 }
  })
});
app.get("/edit_cat/:cat_id", function(req,res){
    var id = req.params.cat_id;
    console.log(id);
   db.query("SELECT *,CONCAT('"+constant.base_url_admin_for_image+"',image) as image FROM auction_cat WHERE cat_id=?",[id], function(err,result){
  
         if(err)
         {
          console.log("Finding error",err);
           return; 
         }
         result= JSON.parse(JSON.stringify(result[0]));
          console.log(result);

          
         res.render("edit_cat", {result:result});
        });
 });
app.post("/update_cat", upload.single('image'),function(req,res){

    var sql = "UPDATE auction_cat SET cat_title =?,image=? WHERE cat_id = ?";
  console.log(req.file)
   req.body.image = req.file.filename
    var query = db.query(sql, [req.body.cat_title, req.body.image, req.body.cat_id], function(err, result) {
   if (err)
  {
    //req.flash("msg", "Error In User update");
    console.log(err);
    res.redirect("/edit_cat");
  }
  else
  {
    //req.flash("msg", "Successfully User UPDATE");
    res.redirect("/addCat");
  }
     
});
});


app.get("/delete_cat/:cat_id",function(req,res){
  var id = req.params.cat_id;
  console.log(id);
  var sql = "DELETE FROM auction_cat WHERE cat_id = ?";
 
  db.query(sql, [id], function(err, result) {
    if (err)
  {
    //req.flash("msg", "Error In User delete");
    console.log(err);
    res.redirect("/addCat");
  }
  else
  {
    //req.flash("msg", "Successfully User Deleted");
    console.log('delete Successfully')
    res.redirect("/addCat");
  }
});
  });

app.post("/addCat" ,upload.single('image'), function(req,res){
 console.log(req.body);
 console.log(req.file);
  
      var today = new Date();
      var data={
    "cat_id":uniqid(),
    "cat_title":req.body.cat_title,
    "image":req.file.filename,
    "created_at":today
  }
          
     db.query("INSERT INTO auction_cat SET ?",data,function(err,results,fields){
     if(!err){
       //res.flash("msg","Error in adding auction");
       console.log(req.body);
        res.redirect("/addCat")
    }
    else{
     //res.flash("msg","Error in adding auction");
      console.log(err); 
        res.redirect("/addCat");
    }


  });

});
app.get('/addSubCat',function(req,res){
db.query('SELECT * FROM auction_cat;SELECT * FROM auction_sub_cat',function(err,results){
  if(!err){
    var result=JSON.parse(JSON.stringify(results))
    res.render('addSubCat',{result:result[0],resultP:result[1]});
  }
 else{
  console.log(err);
 }
})
});

app.post("/addSubCat",function(req,res){
 console.log(req.body);
 //console.log(req.file);
  
        var today = new Date();
      console.log(req.file);
      var data={
    sub_cat_id:uniqid(),
    cat_id:req.body.cat_subCat,
    sub_cat_title:req.body.sub_cat_title,
    created_at:today
  }
          
     db.query("INSERT INTO auction_sub_cat SET ?",data,function(err,results,fields){
     if(!err){
       //res.flash("msg","Error in adding auction");
       console.log(req.body);
        res.redirect("/addSubCat")
    }
    else{
     //res.flash("msg","Error in adding auction");
      console.log(err); 
        res.redirect("/addSubCat");
    }


  });
});


app.get("/delete_sub_cat/:sub_cat_id",function(req,res){
  var id = req.params.sub_cat_id;
  console.log(id);
  var sql = "DELETE FROM auction_sub_cat WHERE sub_cat_id = ?";
 
  db.query(sql, [id], function(err, result) {
    if (err)
  {
    //req.flash("msg", "Error In User delete");
    console.log(err);
    res.redirect("/addSubCat");
  }
  else
  {
    //req.flash("msg", "Successfully User Deleted");
    console.log('delete Successfully')
    res.redirect("/addSubCat");
  }
});
  });
app.get("/edit_sub_cat/:sub_cat_id", function(req,res){
    var id = req.params.sub_cat_id;
    console.log(id);
   db.query("SELECT * FROM auction_sub_cat WHERE sub_cat_id=?",[id], function(err,result){
  
         if(err)
         {
          console.log("Finding error",err);
           return; 
         }
         result= JSON.parse(JSON.stringify(result[0]));
          console.log(result);

          
         res.render("edit_sub_cat", {result:result});
        });
 });
app.post("/update_sub_cat", upload.single('image'),function(req,res){

    var sql = "UPDATE auction_sub_cat SET sub_cat_title =?,image=? WHERE sub_cat_id = ?";
 
    var query = db.query(sql, [req.body.sub_cat_title,req.file.filename, req.body.sub_cat_id], function(err, result) {
   if (err)
  {
    //req.flash("msg", "Error In User update");
    console.log(err);
    res.redirect("/edit_sub_cat");
  }
  else
  {
    //req.flash("msg", "Successfully User UPDATE");
    res.redirect("/addSubCat");
  }
     
});
});

app.get('/userDetails',function(req,res){
  db.query('SELECT * FROM users',function(err,results){
    if(err){
      console.log(err);
    }
    else{
      var result=JSON.parse(JSON.stringify(results));

      res.render('userDetails',{result:result});
    }
  })
});


app.post('/userDetails',upload.single('image'),function(req,res){
console.log(req.body);
  if(!req.body.email || !req.body.name || !req.body.mobile_no ||!req.body.password)
  {
    console.log('all fields') 
    res.send("please all fields")
  }
  else
  {
      bcrypt.hash(req.body.password, 5, function( err, bcryptedPassword) {
      var email=req.body.email;
      db.query("SELECT * FROM users WHERE email = ?",[email], function(err, rows) {
                   if(err){
                    console.log(err)
                  }
                  if(rows.length!=0) {
                           res.send('email is already inserted')
                    } 
                    else 
                    {
      var today = new Date();
      console.log(req.file);
      var users={
        user_pub_id:uniqid(),
        name:req.body.name, 
        image:req.file.filename,
        email:req.body.email,
        password:bcryptedPassword,
        address:req.body.address,
        mobile_no:req.body.mobile_no,
        created_at:today
      }    
      db.query('INSERT INTO users SET ?',users, function (error, results) {
      if (error) {
        console.log(error);
        res.redirect('/userDetails'); 
      }
      else{
       // var result=JSON.parse(JSON.stringify(results[0]));
       console.log(results);
       res.redirect('/userDetails');
      }
      });
    }
    });
    });
}
});
//-------------------update user----------

app.get("/edit_user/:user_pub_id", function(req,res){
    var id = req.params.user_pub_id;
    console.log(id);
   db.query("SELECT *,CONCAT('"+constant.base_url+"',image) as image FROM users WHERE user_pub_id=?",[id], function(err,result){
  
         if(err)
         {
          console.log("Finding error",err);
           return; 
         }
         result= JSON.parse(JSON.stringify(result[0]));
          console.log(result);

          
         res.render("edit_user", {result:result});
        });
 });


/* url of update user */
app.post("/update_user", upload.single('image'),function(req,res){

    var sql = "UPDATE users set name =? ,image=?,email=?, mobile_no=? , address =? WHERE user_pub_id = ?";
 
    var query = db.query(sql, [req.body.name,req.file.filename,req.body.email, req.body.mobile_no,  req.body.address, req.body.user_pub_id], function(err, result) {
   if (err)
  {
    //req.flash("msg", "Error In User update");
    res.redirect("/edit_user");
  }
  else
  {
    //req.flash("msg", "Successfully User UPDATE");
    res.redirect("/userDetails");
  }
     
});
});


/* url of delete user */
app.get("/delete_user/:user_pub_id",function(req,res){
  var id = req.params.user_pub_id;
  console.log(id);
  var sql = "DELETE FROM users WHERE user_pub_id = ?";
 
  db.query(sql, [id], function(err, result) {
    if (err)
  {
    //req.flash("msg", "Error In User delete");
    console.log(err);
    res.redirect("/userDetails");
  }
  else
  {
    //req.flash("msg", "Successfully User Deleted");
    console.log('delete Successfully')
    res.redirect("/userDetails");
  }
});
  });

app.get('/addSubs',function(req,res){
  db.query('SELECT * FROM subscription_package;SELECT * FROM currency_setting',function(err,results){
    if(err){
      res.redirect('/addSubs');
    }
    else{
      var result=JSON.parse(JSON.stringify(results));
      res.render('addSubs',{result:result[0],resultS : result[1]});
    }
  });
});

app.post("/addSubs",function(req,res){
 console.log(req.body);
 //console.log(req.file);
  
      var today = new Date();
      var data={
    package_pub_id:uniqid(),
    package_name:req.body.packageName,
    auction_count:req.body.auctionCount,
    total_days:req.body.totalDays,
    price:req.body.price,
    curr_pub_id:req.body.currency,
    created_at:today,
    updated_at:today
  }
          
     db.query("INSERT INTO subscription_package SET ?",data,function(err,results,fields){
     if(!err){
       //res.flash("msg","Error in adding auction");
        // console.log(req.body);
        res.redirect("/addSubs")
    }
    else{
     //res.flash("msg","Error in adding auction");
        console.log(err); 
        res.redirect("/addSubs");
    }


  });
});

app.get("/edit_subscri/:package_pub_id", function(req,res){
    var id = req.params.package_pub_id;
    console.log(id);
   db.query("SELECT * FROM subscription_package WHERE package_pub_id=?",[id], function(err,result){
  
         if(err)
         {
          console.log("Finding error",err);
           return; 
         }
         result= JSON.parse(JSON.stringify(result[0]));
          console.log(result);

          
         res.render("edit_subscri", {result:result});
        });
 });

 app.post("/update_subscri", function(req,res){

    var sql = "UPDATE subscription_package set package_name =? ,price=?,currency=?, auction_count=? , total_days =? WHERE package_pub_id = ?";
 
    var query = db.query(sql, [req.body.packageName,req.body.price,req.body.currency, req.body.auction_count,  req.body.totalDays, req.body.package_pub_id], function(err, result) {
   if (err)
  {
    //req.flash("msg", "Error In User update");
    console.log(err);
    res.redirect("/addSubs");
  }
  else
  {
    //req.flash("msg", "Successfully User UPDATE");
    res.redirect("/addSubs");
  }
     
});
});

app.get("/delete_subscri/:package_pub_id",function(req,res){
  var id = req.params.package_pub_id;
  console.log(id);
  var sql = "DELETE FROM subscription_package WHERE package_pub_id = ?";
 
  db.query(sql, [id], function(err, result) {
    if (err)
  {
    //req.flash("msg", "Error In User delete");
    console.log(err);
    res.redirect("/addSubs");
  }
  else
  {
    //req.flash("msg", "Successfully User Deleted");
    console.log('delete Successfully')
    res.redirect("/addSubs");
  }
});
  });


/* url of currency */

app.get('/addCurr',function(req,res){
  db.query('SELECT * FROM currency_setting',function(err,results){
    if(err){
      console.log(err);
    }
    else{
      var result=JSON.parse(JSON.stringify(results));

      res.render('addCurr',{result:result});
    }
  })
    
    
  });

app.get('/currency_setting',function(req,res){
  db.query('SELECT * FROM currency_setting',function(err,results){
    if(err){
      res.send({
        "status":0,
        "message":err
      })
    }
    else{
      var result=JSON.parse(JSON.stringify(results))
      res.render('currency_setting',{result:result})
    }
  })
})

app.post('/currency_setting',function(req,res){
  var curr_pub_id=req.body.curr_pub_id;
  var status=1;
  db.query('UPDATE currency_setting SET status="'+status+'" WHERE curr_pub_id=?',[curr_pub_id],function(err,results){
    if(err){
      res.send({
        "status":0,
        "message":err
      })
    }
    else{
      res.redirect('/currency_setting')
    }
  })
})
app.post("/addCurr",function(req,res){
 console.log(req.body);
 //console.log(req.file);
  
      var today = new Date();
      var data={
    curr_pub_id:uniqid(),
    currency_name:req.body.curr_name,
    code:req.body.curr_code,
    created_at:today,
    updated_at:today
  }
          
     db.query("INSERT INTO currency_setting SET ?",data,function(err,results,fields){
     if(!err){
       //res.flash("msg","Error in adding auction");
        // console.log(req.body);
        res.redirect("/addCurr")
    }
    else{
     //res.flash("msg","Error in adding auction");
        console.log(err); 
        res.redirect("/addCurr");
    }


  });
});

app.get("/edit_curr/:curr_pub_id", function(req,res){
    var id = req.params.curr_pub_id;
    console.log(id);
   db.query("SELECT * FROM currency_setting WHERE curr_pub_id=?",[id], function(err,result){
  
         if(err)
         {
          console.log("Finding error",err);
           return; 
         }
         result= JSON.parse(JSON.stringify(result[0]));
          console.log(result);

          
         res.render("edit_curr", {result:result});
        });
 });


 app.get("/delete_curr/:curr_pub_id",function(req,res){
  var id = req.params.curr_pub_id;
  console.log(id);
  var sql = "DELETE FROM currency_setting WHERE curr_pub_id = ?";
 
  db.query(sql, [id], function(err, result) {
    if (err)
  {
    //req.flash("msg", "Error In User delete");
    console.log(err);
    res.redirect("/addCurr");
  }
  else
  {
    //req.flash("msg", "Successfully User Deleted");
    console.log('delete Successfully')
    res.redirect("/addCurr");
  }
});
  });



 app.post("/update_curr", function(req,res){

    var sql = "UPDATE currency_setting set currency_name =? ,code=?  WHERE curr_pub_id = ?";
 
    var query = db.query(sql, [req.body.curr_name,req.body.curr_code,req.body.curr_pub_id], function(err, result) {
   if (err)
  {
    //req.flash("msg", "Error In User update");
    console.log(err);
    res.redirect("/addCurr");
  }
  else
  {
    //req.flash("msg", "Successfully User UPDATE");
    res.redirect("/addCurr");
  }
     
});
});


// app.get("/subscription_history",function(req,res){
//   // var id=req.body.package_pub_id;
  
//  db.query('SELECT subscription_package.package_name,subscription_package.price,subscription_package.total_days,currency_setting.currency_name FROM subscription_package INNER JOIN currency_setting ON subscription_package.curr_pub_id=currency_setting.curr_pub_id WHERE package_pub_id="1m222e49juscij8f"',function(err,results){
//   if(err){
//     res.send({
//       "status":1,
//       "message":err
//     })
//   }
//   else{
//     var result=JSON.parse(JSON.stringify(results));
//     if(result.length>=0)
// {    
//     res.send({
//       "status":1,
//       "message":"get subscription_history",
//       "data":results
//     })
//   }else{
//     res.send({
//       "status":1,
//       "message":"no data"
//     })
//   }
//   }
//  })
// })

app.get("/subscription_history",function(req,res){
  //var id=req.body.package_pub_id;
  
 db.query('SELECT subscription_history.invoice_id,subscription_history.total_price,subscription_history.discount,subscription_history.tax,subscription_history.final_price,subscription_history.start_date,DATE_ADD(subscription_history.start_date,INTERVAL 31 DAY) as end_date,subscription_package.package_name,currency_setting.currency_type,users.name FROM (((subscription_package INNER JOIN currency_setting ON subscription_package.curr_pub_id=currency_setting.curr_pub_id) INNER JOIN subscription_history ON subscription_history.package_pub_id=subscription_package.package_pub_id) INNER JOIN users ON users.user_pub_id=subscription_history.user_pub_id)',function(err,results){
   if (err)
  {
    //req.flash("msg", "Error In User update");
    console.log(err);
    res.redirect("/subscription_history");
  }
  else
  {
    console.log('subscription_package')
    //req.flash("msg", "Successfully User UPDATE");
    var result=JSON.parse(JSON.stringify(results))
    res.render("subscription_history",{result:result});
  }
 })
})
app.get('/send_notification',function(req,res){
  console.log('balram')
         
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
      db.query('SELECT count(*) as count from users',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }

       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
    }
     db.query('SELECT  *,CONCAT("'+constant.base_url+'", image) as image from users LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        console.log('sssss')
        var result=JSON.parse(JSON.stringify(result));
        console.log(result);
         res.render('send_notification',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
      }
     })
        }else{
          console.log(err);
        }
        
      })
  // db.query('SELECT user_pub_id,name,email,CONCAT("'+constant.base_url+'",image) as ima,mobile_no FROM users',function(err,results){
  //   if(err){
  //     res.send({
  //       "status":0,
  //       "message":err
  //     })
  //   }
  //   else{
  //     var result=JSON.parse(JSON.stringify(results))
  //   res.render("send_notification",{result:result});
  //   }
  // })
})
// app.post('/send_notification',function(req,res){
//   var today=new Date();
//   var arraylist=[];
//   var ll=req.body.uid;
//   var lem=ll.length;
//   for(i=0;i<lem;i++){
//    arraylist.push(ll[i]);
//   }
 
//  var x=arraylist.toString();
//  for(var j=0;j<x.length;i++){
//  var y=x.split(",");

//   console.log(req.body.uid);
//   var body={
//     notication_id:uniqid(),
//     user_pub_id:req.body.uid,
//     tittle:req.body.title,
//     message:req.body.msg,
//     created_at:today,
//     updated_at:today
//    }
//    db.query('INSERT INTO notification SET ?',[body],function(err,results){
//     if(err){
//       res.send({
//         "status":0,
//         "message":err
//       })
//     }
//     else{
//       db.query('SELECT user_pub_id,name,email,image,mobile_no FROM users',function(err,results){
//     if(err){
//       res.send({
//         "status":0,
//         "message":err
//       })
//     }
//     else{
//       var result=JSON.parse(JSON.stringify(results))
//     res.render("send_notification",{result:result});
//     }
//   })
//     }
//    })

// }
// })
app.post('/send_notification',function(req,res){
  var today=new Date();
  var arraylist=[];
  var ll=req.body.uid;
  var lem=req.body.uid.toString();
  console.log(lem);
  var s=lem.split(",");
  console.log(s);
console.log(s.length);
  for(var i=0;i<s.length-1;i++){
 var body={
    notication_id:uniqid(),
    user_pub_id:s[i],
    tittle:req.body.title,
    message:req.body.msg,
    type:constant.ADMIN_NOTIFICATION_TYPE,
    created_at:today,
    updated_at:today
   }
   db.query('INSERT INTO notification SET ?',[body],function(err,results){
  
   }) 
  }
  console.log('balram')
         
var totalRec = 0;
var start       = 0;
var currentPage = 1;
var pageSize  = 10;
var pageCount = 0;
      db.query('SELECT count(*) as count from users',function(err,results){
        if(results.length>0){
          var result1=JSON.parse(JSON.stringify(results));
          var count=result1[0].count

           totalRec      = count;
         pageCount     =  Math.ceil(totalRec /  pageSize);
         console.log(pageCount);
         if (typeof req.query.page !== 'undefined') {
    currentPage = +req.query.page;
      }

       if(currentPage >1){
       start = (currentPage - 1) * pageSize;
    }
     db.query('SELECT  *,CONCAT("'+constant.base_url+'", image) as image from users LIMIT '+pageSize+' OFFSET '+start+'',function(err,result){
      if(!err){
        console.log('sssss')
        var result=JSON.parse(JSON.stringify(result));
        console.log(result);
         res.render('send_notification',{result:result ,pageSize:pageSize,pageCount:pageCount,currentPage:currentPage});
      }
     })
        }else{
          console.log(err);
        }
        
      })
  //    db.query('SELECT user_pub_id,name,email,image,mobile_no FROM users',function(err,results){
  //   if(err){
  //     res.send({
  //       "status":0,
  //       "message":err
  //     })
  //   }
  //   else{
  //     var result=JSON.parse(JSON.stringify(results))
  //   res.render("send_notification",{result:result});
  //   }
  // })

 //  console.log(arraylist);
 // var x=arraylist.toString();
 //  console.log(x);
//  for(var j=0;j<x.length;i++){
//  var y=x.split(",");

//   console.log(req.body.uid);
//   var body={
//     notication_id:uniqid(),
//     user_pub_id:y,
//     tittle:req.body.title,
//     message:req.body.msg,
//     created_at:today,
//     updated_at:today
//    }
//    db.query('INSERT INTO notification SET ?',[body],function(err,results){
//    console.log(y)
//    }) 

// }
})



app.listen(3004,function(err){
  if(!err){
    console.log('listening port no 3004');
  }
})