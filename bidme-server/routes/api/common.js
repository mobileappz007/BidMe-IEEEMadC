var express=require('express');
var app=express();
exports.getRandomString=function(length=8){
	var num = Math.random().toString(36);
		var string = num.replace(/[^a-z]+/g, 'HJ').substr(0, length);
		return string;
}
