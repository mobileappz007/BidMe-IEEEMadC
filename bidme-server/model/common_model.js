var fcm = require('fcm-notification'); 
var path = require("path");
//var ss=path.resolve(__dirname, './aaa.json') 
//var FCM = new fcm(ss);
var admin=require('firebase-admin')
// var serviceAccount = require("./aaa.json");
// var FCM=admin.initializeApp({
//   credential: admin.credential.cert(serviceAccount),
//   databaseURL: "https://subasta-38cf6.firebaseio.com"
// });

// ...
var db=require('../config/database.js')
var serviceAccount = require('./privatekey.json');


module.exports.pushnotification=function(user_name,msg,receiver_token,type)
{
if (!admin.apps.length) {

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
 // databaseURL: "https://fcm.googleapis.com/fcm/send"
   databaseURL: "https://myApp.firebaseio.com"

});
}
console.log(user_name);

console.log(receiver_token);
// var registrationToken = 'YOUR_REGISTRATION_TOKEN'; <-- token of user
 var message = {
 android :{
          ttl:10000,
          priority:'high',
          data: {
                 title : user_name,
                 type : type,
                 body : decodeURIComponent(msg) 
                 } 
      },
      apns: {

             payload: {
                        ttl:10000,
                        priority:'high',
                        data: {
                             title : user_name,
                             type : type,
                             body : decodeURIComponent(msg) 
                            },
                        aps: {
                            badge: 0,
                            sound : "bingbong.aiff",
                            alert : decodeURIComponent(msg),
                            "content-available" : 1
                            }
                     },
    },
// notification: {
//   title: user_name,
//   body: msg
//   },

//token: "fU1Wn6omars:APA91bE6Eg5fgZ2iQE7y4hpsMlg7VpUoiVMdu5NjSyAeqNqUnGFoeyKUMQ3OyTCZH4kpw0axsBckNGiiceLMV2dhKvGDgZ5_meRD6QI4cAb-ls1wEuC78DV_E768fd2oi4EIWgweMuC0"
token:receiver_token
};
   
admin.messaging().send(message).then(res=>{
    console.log("Success",res)
}).catch(err=>{

    console.log("Error:",err)
})


}

module.exports.pushnotification1=function(user_name,msg,receiver_token,type,pro_pub_id)
{
if (!admin.apps.length) {

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
 // databaseURL: "https://fcm.googleapis.com/fcm/send"
   databaseURL: "https://myApp.firebaseio.com"

});
}
console.log(pro_pub_id);
// var registrationToken = 'YOUR_REGISTRATION_TOKEN'; <-- token of user
 var message = {
 android :{
          ttl:10000,
          priority:'high',
          data: {
                 title : user_name,
                 type : type,
                 body : decodeURIComponent(msg),
                 pro_pub_id: pro_pub_id 
                 } 
      },
      apns: {

             payload: {
                        ttl:10000,
                        priority:'high',
                        data: {
                             title : user_name,
                             type : type,
                             body : decodeURIComponent(msg) ,
                             pro_pub_id: pro_pub_id 
                            },
                        aps: {
                            badge: 0,
                            sound : "bingbong.aiff",
                            alert : decodeURIComponent(msg),
                            "content-available" : 1
                            }
                     },
    },
// notification: {
//   title: user_name,
//   body: msg
//   },

//token: "fU1Wn6omars:APA91bE6Eg5fgZ2iQE7y4hpsMlg7VpUoiVMdu5NjSyAeqNqUnGFoeyKUMQ3OyTCZH4kpw0axsBckNGiiceLMV2dhKvGDgZ5_meRD6QI4cAb-ls1wEuC78DV_E768fd2oi4EIWgweMuC0"
token:receiver_token
};
   
admin.messaging().send(message).then(res=>{
    console.log("Success",res)
}).catch(err=>{

    console.log("Error:",err)
})


}
module.exports.update=function(table, where, obj, cb)
{
    var que = "UPDATE "+table+" SET ";
    var counter=1;
    for(var k in obj){
    if(counter==1){
         que += k+" = '"+obj[k]+"'"
    }
    else{
    que += ", "+k+" = '"+obj[k]+"'"
    }
     counter++;
    }
    var key = Object.keys(where);
    que += " WHERE "+key[0]+" = '"+where[key[0]]+"' AND "+key[1]+" = '"+where[key[1]]+"' ";
    console.log()
    db.query(que, cb);
}
module.exports.update1=function(table, where, obj, cb)
{
    var que = "UPDATE "+table+" SET ";
    var counter=1;
    for(var k in obj){
    if(counter==1){
         que += k+" = '"+obj[k]+"'"
    }
    else{
    que += ", "+k+" = '"+obj[k]+"'"
    }
     counter++;
    }
    var key = Object.keys(where);
    que += " WHERE "+key[0]+" = '"+where[key[0]]+"' ";
    console.log()
    db.query(que, cb);
}
