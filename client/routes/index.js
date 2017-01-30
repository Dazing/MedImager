/*

    Router for path /

    *** NOTHING TO DO HERE ***

*/

var medDB = require('./medimager_database.js');
var express = require('express');
var router = express.Router();

var db;
// Redirect to application
router.get('/', function(req, res, next) {
	if (!db) {
		db = medDB.get();
	}

	res.render('index',{});
});

router.get('/index', function(req, res, next) {
	if (!db) {
		db = medDB.get();
	}
	db.collection('medimger').find({}).toArray(function(err, result){
		console.log(result);
	});
	res.render('index',{});
});

// Redirect to application
router.post('/search', function(req, res, next) {
    res.render('index',{});
});

// Redirect to application
router.get('/setup/:number', function(req, res, next) {
	if (!db) {
		db = medDB.get();
	}
	var name;
	for (var i = 0; i < req.params.number; i++) {
		name = 'a_'+i;
		db.collection('medimger').insertOne({name:1},function(err, r) {
			if (err) {
				console.log(err);
			}
			else {
				console.log("Inserted: "+name);
			}
		})
	}
	res.redirect('/index');
});

module.exports = router;
