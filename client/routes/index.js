/*

    Router for path /

    *** NOTHING TO DO HERE ***
*/

var express = require('express');
var router = express.Router();


// Redirect to application
router.get('/', function(req, res, next) {
	res.render('index',{});
});

router.get('/index', function(req, res, next) {
	res.render('index',{});
});

// Redirect to application
router.get('/search', function(req, res, next) {
    res.render('search',{});
});

// Redirect to application
router.post('/search', function(req, res, next) {
    res.render('search',{});
});

router.get('/photo', function(req, res, next){
	res.render('photo',{})
});

module.exports = router;
