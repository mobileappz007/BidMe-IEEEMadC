exports.sendEmail=function(params, callback){
		if(params.from == undefined || params.to == undefined || params.subject == undefined || params.body == undefined){
			return false;
		}
		if(params.type == 'html'){
			params.html = params.body;
		}else{
			params.text = params.body;
		}
		if(true){
			var nodemailer = require('nodemailer');
			var transporter = nodemailer.createTransport({
				service:'Gmail',
				auth: {
					user: "getrid2020@gmail.com",
					pass: "getrid1961"
				}
			});
			transporter.sendMail(params, function(err, info){
			  if (err) {
			    return callback(err);
			  }
			  return callback(null, info);
			});
		}else{
			var sendmail = require("sendmail")();
			sendmail(params, function(err, reply) {
			  	if(err)
			    	return callback(err);
			    return callback(null, reply);
			});	
		}
		
	}