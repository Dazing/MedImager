/*

    Router for path /

    *** NOTHING TO DO HERE ***

*/

var db = require('./medimager_database.js');
var assert = require('assert');
var express = require('express');
var router = express.Router();



// Redirect to application
router.get('/', function(req, res, next) {
	console.log("indexjs"+db);
	try {
	  	var c = db.collection('movie');
		c.find({}).toArray(function(err,docs){
			assert.equal(err,null);
			console.log(docs);
		})
	} catch (err) {
		console.log(err);
	}
	res.render('index',{});
});

// Redirect to application
router.post('/search', function(req, res, next) {

    res.render('index',{});
});



module.exports = router;
