$(document).ready(function () {
	$('#searchSideWrapper').width(
		$('body').width()-500
	);

	/* Event Listeners */
	$('#toggleAdvSearch').click(function(){
		$('#advancedSearch').toggle();
	});

	/* DOM Manipulation */

	$(window).resize(function(){
		$('#searchSideWrapper').width(
			$('body').width()-500
		);
	})

});
