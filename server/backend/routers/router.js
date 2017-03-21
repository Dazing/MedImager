var express = require('express');
var router = express.Router();

router.get('/api/search', function(req, res, next){
	var list1 = ["http://cdn.modernfarmer.com/wp-content/uploads/2014/09/innercowhero.jpg", "https://petterssonorg.files.wordpress.com/2013/08/surikat.jpg", "http://4.bp.blogspot.com/-b_CoEyL7Y4E/VeCTOqEqdwI/AAAAAAAAJ1A/0kigqVeRPnM/s1600/TrumpPhenomenon2.jpg", "http://vignette3.wikia.nocookie.net/disney/images/f/f8/Timon_and_Pumbaa_Lion_Guard.jpg/revision/latest?cb=20151009185849", "https://s-media-cache-ak0.pinimg.com/originals/91/d9/7e/91d97e06cdc41757cfb421dab4eabd30.jpg"]

	var list2 = ["http://www.konbini.com/en/files/2016/09/cow-triangle.jpg", "http://i2.irishmirror.ie/incoming/article5979450.ece/ALTERNATES/s1200/Blosom-Tallest-Cow.jpg", "http://www.healthyfoodelements.com/wp-content/uploads/2015/09/white-teeth.jpg", "http://www.sbf.se/globalassets/svenska-bilsportforbundet/bilder/racing4.jpg", "https://static01.nyt.com/images/2016/10/01/sports/01prix-web/01prix-web-master768.jpg", "https://scontent-fra3-1.xx.fbcdn.net/v/t31.0-0/p526x296/16722497_1917407805156563_3159559885330646095_o.jpg?oh=9da568a689c0b645eb7598fc2473896b&oe=5931AF31"]
	var list3 = ["https://scontent-fra3-1.xx.fbcdn.net/v/t31.0-8/s960x960/16178428_618225748388740_4582847272464874647_o.jpg?oh=80cfde983b8e240157a52ce73f5efec2&oe=5928255D", "https://scontent-fra3-1.xx.fbcdn.net/v/t31.0-8/15385408_887975597901331_648368677598689041_o.jpg?oh=c322e32fbcd5cbe63bde5281efc1b43e&oe=593E090D", "https://scontent-fra3-1.xx.fbcdn.net/v/t1.0-9/15027579_579697472241568_5653370222063429241_n.jpg?oh=bdd22e5c61f2307056af6282ab3f0000&oe=593AB2AE"];

	console.log("GET search, params = "+(req.query.query));
	var query = JSON.parse(req.query.query);
	if (query.alder != "") {
		res.json(list3);
	}
	else if (query.rokare != "") {
		res.json(list2);
	}
	else {
		res.json(list1);
	}
});

module.exports = router;
