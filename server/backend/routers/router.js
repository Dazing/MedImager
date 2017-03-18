var express = require('express');
var router = express.Router();

router.get('/api/search', function(req, res, next){
	var searchResult = [
		{
			id: "string1",
	    	url: "string1",
		    name: "string1"
		},
		{
			id: "string2",
	    	url: "string2",
		    name: "string2"
		},
		{
			id: "string3",
	    	url: "string3",
		    name: "string3"
		},
		{
			id: "string4",
	    	url: "string4",
		    name: "string4"
		},
		{
			id: "string5",
	    	url: "string5",
		    name: "string5"
		},
		{
			id: "string6",
	    	url: "string6",
		    name: "string6"
		}
	];

	res.json(searchResult);
});

module.exports = router;
