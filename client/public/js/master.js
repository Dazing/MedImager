$(document).ready(function () {
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

	/* DOM Manipulation */

	$(window).resize(function(){
		$('#searchSideWrapper').width(
			$('body').width()-500
		);
	})




});
