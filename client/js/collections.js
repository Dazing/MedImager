var myCollections = [{name:"samling 1", id:"111111"}, {name:"samling 2", id:"222222"},{name:"samling 3", id:"3333"}];
var sharedWithMe = [{name:"extern samling 1", id:"1232123"},{name:"extern samling 1", id:"1232123"}];
var inboxImages = [{name:"bild 1", id:"41233"},{name:"bild 2", id:"32644"},{name:"bild 3", id:"243546"}];


$('document').ready(function(){
	// Fetch collections from server.
	var url = "www.medimager.com/api/tags";
	/*$.get(url).then(function(res){
		console.log(res);
		for (var i = 0; i < res.tags.length; i++) {
			tagObjects.push(new tag(res.tags[i]));
		}
	});*/
});


function displayCollectionsMenu() {	
	$(".collections-menu").html("");
	
	if (myCollections.length > 0) {
		$(".collections-menu").append('<li><a class="subheader">Mina Samlingar</a></li>');
		for(var i=0; i<myCollections.length; i++){
			$(".collections-menu").append('<li><a class="image-collection my-collection waves-effect dnd-target" data-id="' + myCollections[i].id + '">' + myCollections[i].name + "</a></li>");
		}
		$(".collections-menu").append('<li><div class="divider"></div></li>');
	}
	
	if (sharedWithMe.length > 0) {
		$(".collections-menu-content").append('<li><a class="subheader">Delade med mig</a></li>');
		for(var i=0; i<sharedWithMe.length; i++){
			$(".collections-menu-content").append('<li><a class="image-collection shared-collection waves-effect dnd-target" href="#" data-id="' + sharedWithMe[i].id + '">' + sharedWithMe[i].name + "</a></li>");
		}
		$(".collections-menu-content").append('<li><div class="divider"></div></li>');
	}
	
	if (inboxImages.length > 0) {
		$(".collections-menu-content").append('<li><a class="subheader">Inbox</a></li>');
		for(var i=0; i<inboxImages.length; i++){
			$(".collections-menu-content").append('<li><a class="inbox-image waves-effect" href="#" data-id="' + inboxImages[i].id + '"><i class="material-icons">perm_media</i>' + inboxImages[i].name + "</a></li>");
		}
		$(".collections-menu-content").append('<li><div class="divider"></div></li>');
	}
	
}
