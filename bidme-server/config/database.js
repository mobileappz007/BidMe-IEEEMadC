var mysql=require('mysql');
var connection=mysql.createConnection({
	multipleStatements: true,
	host:'localhost',
	user:'root',
	password:'123',
	database:'BidMe'
});
connection.connect(function(err){
	if(err){
		console.log(err);
	}
	else{
		console.log('Database connected');
	}
});
module.exports=connection;