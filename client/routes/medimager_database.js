var MongoClient = require('mongodb').MongoClient
  , assert = require('assert');

// Connection URL
var url = 'mongodb://localhost:27017/database';

// Use connect method to connect to the server
var database;

MongoClient.connect(url, function(err, db) {
	assert.equal(null, err);
	console.log("Connected successfully to server");

	database = db;
	console.log("medimager_database:"+database);

	var c = db.collection('movie');


	c.find({}).toArray(function(err,docs){
		assert.equal(err,null);
		console.log(docs);
	})



	module.exports = database;
});
