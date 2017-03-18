$(document).ready(function () {
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

	$("#search-results span").click(function(event) {
		var thisId = event.target.id;
		currentImage = thisId.substring(13, thisId.length);
		setPopUpImage();
		$("#popup-image-wrapper").css("display", "flex");
		$("#black-back").css("display", "block");
	});

	$(".opacity-wrapper").click(function(event) {//edit event target to give proper image data to #search-results span : click
		event.target.id = $(this).parent().attr('id');
	});

	$(".inner-text").click(function(event) { //same as last one but for when grandchildren are fired
		event.target.id = $(this).parent().parent().attr('id');
	});


	$("#black-back").click(function(event) {
		closePopUp();
	});
	$("#popup-image-container").mouseover(function(event) {
		$("#popup-image-next").css("opacity", "0.8");
		$("#popup-image-previous").css("opacity", "0.8");
	});
	$("#popup-image-container").mouseout(function(event) {
		$("#popup-image-next").css("opacity", "0");
		$("#popup-image-previous").css("opacity", "0");
	});
	$("#popup-close-button").click(function(event) {
		closePopUp();
	});
	$("#popup-image-previous").click(function(event) {
		setPopUpImage(currentImage--);
	});
	$("#popup-image-next").click(function(event) {
		setPopUpImage(currentImage++);
	});
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

	$("#collections-togglebutton").click(function() {
		$(".side-nav").animate({width: 'toggle'}, "fast");
		$("#page-wrapper").toggleClass("collections-menu-margin", "fast");
	});

	$(".dnd-source").on("dragstart",handleDragStart);
	$(".dnd-target-collection").on("dragenter",handleDragEnter);
	$(".dnd-target").on("dragover",handleDragOver);
	$(".dnd-target").on("drop",handleDrop);

	$(document).keyup(function(e) {
		if (e.keyCode == 27)
			closePopUp();
		else if (e.keyCode == 37)
			setPopUpImage(currentImage--);
		else if (e.keyCode == 39)
			setPopUpImage(currentImage++);
	});

	function setPopUpImage()
	{
		if(currentImage < 0)
			currentImage = searchresults.length-1;
		else if(currentImage > searchresults.length-1)
			currentImage = 0;
		$("#popup-image").attr("src", searchresults[currentImage]);
	}

	function closePopUp()
	{
		$("#black-back").hide();
		$("#popup-image-wrapper").hide();
	}

});
