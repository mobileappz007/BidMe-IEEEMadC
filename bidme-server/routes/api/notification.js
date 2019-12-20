var express = require("express");
var routes = express.Router();


var constant=require('../../constants/constant.js')
var db=require('../../config/database.js');

var mysql = require("mysql"); 

routes.post("/getNotifications", function(req, res) {
console.log(req.body.user_pub_id);
    if (!req.body.user_pub_id) {
        res.json({
            status: 0,
            message: constant.CHKAllFIELD
        });
        return;
    } else {
        var user_pub_id=req.body.user_pub_id;
    db.query('SELECT * from notification where user_pub_id=?',[user_pub_id], function(err, result) {
            if (result.length > 0) {
                var result=JSON.parse(JSON.stringify(result));
                var final_result=result.reverse();
                    
                res.send({
                    "status": 1,
                    "message": constant.NOTIFICATIONS,
                    "my_notifications": final_result
                });
            } else {
                res.send({
                    "status": 0,
                    "message": constant.NODATA
                });
            }
        });
    }
});


module.exports = routes;