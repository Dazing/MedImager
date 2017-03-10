var dragdropDebugmode = false;

function handleDragStart(event) {	
	if (dragdropDebugmode) {console.log("started dragging, from data id: " + event.target.getAttribute("data-id"));}
	event.originalEvent.dataTransfer.setData("sourceId", event.target.getAttribute("data-id"));
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
	if (dragdropDebugmode) {console.log("dropped, target: " + target + ", source: " + source);}
	//TODO: add some code that makes sure you can't just drag anything into drop zones
	alert("dragged from data-id " + source + " to data-id " + target);
	
}

