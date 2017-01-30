var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');

var express = require('express');
var app = express();
// Connection URL
var url = 'mongodb://localhost:27017/database';

// Use connect method to connect to the server
var medDB;

MongoClient.connect(url, function(err, db) {
	assert.equal(null, err);
	medDB = db;
});


module.exports.get = function() {
	return medDB;
}
