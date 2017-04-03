$(document).ready(function () {
	var currentImage = 0;
	var advancedBar = false;
	var settingsBar = false;
	
	var searchresults = ["http://cdn.modernfarmer.com/wp-content/uploads/2014/09/innercowhero.jpg", "https://petterssonorg.files.wordpress.com/2013/08/surikat.jpg", "http://4.bp.blogspot.com/-b_CoEyL7Y4E/VeCTOqEqdwI/AAAAAAAAJ1A/0kigqVeRPnM/s1600/TrumpPhenomenon2.jpg", "http://vignette3.wikia.nocookie.net/disney/images/f/f8/Timon_and_Pumbaa_Lion_Guard.jpg/revision/latest?cb=20151009185849", "https://s-media-cache-ak0.pinimg.com/originals/91/d9/7e/91d97e06cdc41757cfb421dab4eabd30.jpg", "http://www.konbini.com/en/files/2016/09/cow-triangle.jpg", "http://i2.irishmirror.ie/incoming/article5979450.ece/ALTERNATES/s1200/Blosom-Tallest-Cow.jpg", "http://www.healthyfoodelements.com/wp-content/uploads/2015/09/white-teeth.jpg", "http://www.sbf.se/globalassets/svenska-bilsportforbundet/bilder/racing4.jpg", "https://static01.nyt.com/images/2016/10/01/sports/01prix-web/01prix-web-master768.jpg", "https://scontent-fra3-1.xx.fbcdn.net/v/t31.0-0/p526x296/16722497_1917407805156563_3159559885330646095_o.jpg?oh=9da568a689c0b645eb7598fc2473896b&oe=5931AF31", "https://scontent-fra3-1.xx.fbcdn.net/v/t31.0-8/s960x960/16178428_618225748388740_4582847272464874647_o.jpg?oh=80cfde983b8e240157a52ce73f5efec2&oe=5928255D", "https://scontent-fra3-1.xx.fbcdn.net/v/t31.0-8/15385408_887975597901331_648368677598689041_o.jpg?oh=c322e32fbcd5cbe63bde5281efc1b43e&oe=593E090D", "https://scontent-fra3-1.xx.fbcdn.net/v/t1.0-9/15027579_579697472241568_5653370222063429241_n.jpg?oh=bdd22e5c61f2307056af6282ab3f0000&oe=593AB2AE"];
	
	var selectedTagId = document.cookie.split("selectedTagId=").pop();
	if(selectedTagId != "")
	{
		document.cookie = "selectedTagId=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
		selectTag(selectedTagId);
	}

	$('#searchSideWrapper').width(
		$('body').width()-500
	);

	/* Event Listeners */
	$('#toggleAdvSearch').click(function(){
		$('#advancedSearch').toggle();
	});

	// TODO set correct class
	$('.img').click(function(){
		var imgId = this.id;
		var url = "www.medimager.com/images/"+imgId;
		$.get(url).then(function(res){
			console.log(res);
		});
	});

	var count = $('#resultLength').val();
	if (count > 0) {
		$('#results-count span').html(count);
		$('#results-count').toggle();
	}
	$('#resultLength').change(function(){
		var count = $('#resultLength').val();
		if (count > 0) {
			$('#results-count span').html(count);
			$('#results-count').toggle();
		}
	});

	/* DOM Manipulation */

	$(window).resize(function(){
		$('#searchSideWrapper').width(
			$('body').width()-500
		);
	})

	$("#menu-bar").resize(function() {
		console.log("resized menu bar");
		setPageWrapperHeight((advancedBar ? 104 : 0) + (settingsBar ? 52 : 0));
	})

	//$("#search-results span").on('mouseup', function(event) {
	//	var thisId = event.target.id;
	//	currentImage = thisId;
	//	if(event.which == 2)
	//	{
	//		event.preventDefault();
	//		var newTab = window.open("image?id=" + thisId, '_blank');
	//	}
	//	else
	//	{
	//		setPopUpImage();
	//		$("#popup-image-wrapper").css("display", "flex");
	//		$("#black-back").css("display", "block");
	//	}
	//});

	//$(".opacity-wrapper").click(function(event) {//edit event target to give proper image data to #search-results span : click
	//	event.target.id = $(this).parent().attr('id');
	//});

	//$(".inner-text").click(function(event) { //same as last one but for when grandchildren are fired
	//	event.target.id = $(this).parent().parent().attr('id');
	//});


	//$("#black-back").click(function(event) { 
	//	closePopUp();						 ////// moved to angular
	//});
	//$("#popup-image-container").mouseover(function(event) {
	//	$("#popup-image-next").css("opacity", "0.8");
	//	$("#popup-image-previous").css("opacity", "0.8");
	//});
	//$("#popup-image-container").mouseout(function(event) {
	//	$("#popup-image-next").css("opacity", "0");
	//	$("#popup-image-previous").css("opacity", "0");
	//});
	//$("#popup-close-button").click(function(event) {
	//	closePopUp();
	//});
	//$("#popup-image-previous").click(function(event) {
	//	setPopUpImage(currentImage--);
	//});
	//$("#popup-image-next").click(function(event) {
	//	setPopUpImage(currentImage++);
	//});
	$('#search-input').click(function() {
		displayTagList($(this).val());
	});
	$('#search-input').keydown(function(event) {
		if(event.keyCode == 40)
			currentAutoSearchPos++;
		else if(event.keyCode == 38)
			currentAutoSearchPos--;
		else if(event.keyCode == 13)
			selectTagByMarkedPos();
		else
			displayTagList($(this).val());

		if(currentAutoSearchPos < 0)
			currentAutoSearchPos = $("#auto-search-results").find("span").length-1;
		else if(currentAutoSearchPos > $("#auto-search-results").find("span").length-1)
			currentAutoSearchPos = 0;
		markAutoSearch();
	});

	//$(".dnd-source").on("dragstart",handleDragStart);
	//$(".dnd-target").on("dragenter",handleDragEnter);
	//$(".dnd-target").on("dragover",handleDragOver);
	//$(".dnd-target").on("drop",handleDrop);

	//$(document).keyup(function(e) {
	//	if (e.keyCode == 27)
	//		closePopUp();
	//	else if (e.keyCode == 37)
	//		setPopUpImage(currentImage--);
	//	else if (e.keyCode == 39)
	//		setPopUpImage(currentImage++);
	//});

	//function setPopUpImage()
	//{
	//	if(currentImage < 0)
	//		currentImage = searchresults.length-1;
	//	else if(currentImage > searchresults.length-1)
	//		currentImage = 0;
	//	$("#popup-image").attr("src", searchresults[currentImage]);
	//}

	//function closePopUp()
	//{
	//	$("#black-back").hide();
	//	$("#popup-image-wrapper").hide();
	//}
});
