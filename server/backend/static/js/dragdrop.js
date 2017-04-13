var dragdropDebugmode = false;

$(document).ready(function () {
	$('body').append('<div id="ghost-image-container" style="position: absolute; top: 0px; left: -100px;"></div>');
});

function handleDragStart(event) {	
	if (dragdropDebugmode) {console.log("started dragging, from data id: " + event.target.getAttribute("data-id"));}
	event.originalEvent.dataTransfer.setData("sourceId", event.target.getAttribute("data-id"));
	event.originalEvent.dataTransfer.setData("sourceType", "image");
	
	
	var url = $(this).css("background-image");
	url = url.substr(5, url.length - 7);
	
	$("#ghost-image-container").html("");
	$("#ghost-image-container").append('<img src="' + url + '" style="width: 75px; height: 53px;">');
    event.originalEvent.dataTransfer.setDragImage($("#ghost-image-container").get(0), 0, 0);
}

function handleDragEnter(event) {
	event.preventDefault();
	if (dragdropDebugmode) {console.log("dragging over, to data id: " + event.target.getAttribute("data-id"));}
	return true;
}

function handleDragOver(event) {	
	event.preventDefault();
	return false;
}

function handleDrop(event) {	
	event.preventDefault();
	var source = event.originalEvent.dataTransfer.getData("sourceId");
	var target = event.target.getAttribute("data-id");
	var type = event.originalEvent.dataTransfer.getData("sourceType");
	if (dragdropDebugmode) {console.log("dropped, target: " + target + ", source: " + source + ", type: " + type);}
	if (type == "image") {
		alert("dragged from data-id " + source + " to data-id " + target);
	}
	
	
}

