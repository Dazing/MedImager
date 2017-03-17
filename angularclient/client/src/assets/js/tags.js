function tag(title)
{
	this.title = title;
	this.selected = false;
}


var tags = ["tandtråd", "tandtroll", "tandvärk", "tandsten", "tandpetare", "tandkött", "herpes", "tandlös", "blomkål", "tandkossa", "kossan säger mu", "karies", "baktus"];
var tagObjects = [];
var currentAutoSearchPos = 0;

for (var i = 0; i < tags.length; i++)
{
	tagObjects.push(new tag(tags[i]));
}


$('document').ready(function(){
	// Fetch search terms (tags) from server.
	var url = "www.medimager.com/api/tags";
	/*$.get(url).then(function(res){
		console.log(res);
		for (var i = 0; i < res.tags.length; i++) {
			tagObjects.push(new tag(res.tags[i]));
		}
	});*/
});


function displayTagList(string)
{
	currentAutoSearchPos = 0;
	string = string.toLowerCase();
	if(string.length > 1)
	{
		$("#auto-search-results").html("");
		for(var i=0; i<tagObjects.length; i++)
		{
			if(tagObjects[i].title.includes(string) && !tagObjects[i].selected)
				$("#auto-search-results").append("<span id='sugg" + i + "'>" + tagObjects[i].title + "</span>");
		}
		$("#auto-search-results").find("span").click(function(event) {
			selectTagByMarkedPos();
		});
		$("#auto-search-results").css("display", "block");
		$("#auto-search-results").find("span").mouseover(function(event) {
			currentAutoSearchPos = listIdToTagId(event);
			markAutoSearch();
		});
	}
	else
		$("#auto-search-results").hide();
}
function markAutoSearch()
{
	var results = $("#auto-search-results").find("span");
	for(var i=0; i<results.length; i++)
		$(results[i]).css("background-color", "transparent");
	$(results[currentAutoSearchPos]).css("background-color", "#eeeeee");
}

function listIdToTagId(event)
{
	var results = $("#auto-search-results").find("span");
	for(var i=0; i<results.length; i++)
	{
		if($(results[i]).html() == $(event.target).html())
			break;
	}
	return i;
}

function selectTagByMarkedPos()
{
	var results = $("#auto-search-results").find("span");
	var thisTagId = $(results[currentAutoSearchPos]).attr("id").substring(4, $(results[currentAutoSearchPos]).attr("id").length);
	selectTag(thisTagId);
}

function selectTag(selectedTagId)
{
	var filename = window.location.pathname;
	filename = filename.split("/").pop();
	if(filename != "search" && filename != "search.html")
	{
		document.cookie = "selectedTagId=" + selectedTagId;
		window.location.href = "search.html";
	}
	
	tagObjects[selectedTagId].selected = true;
	$("#selected-tags").append("<div class='chip'>" + tagObjects[selectedTagId].title + "<i id='tag" + selectedTagId + "' class='removeTag close material-icons'>close</i></div>");
	$("#search-input").val("");
	$("#auto-search-results").hide();
	currentAutoSearchPos = null;
	setPageWrapperHeight();
	$("#search-input").focus();
}

$("html").click(function(event) {
	if(event.target.id != "search-input")
		$("#auto-search-results").hide();
});

$(document).on("click", ".removeTag", function(){
	$(this).parent().remove();
	tagObjects[$(this).attr("id").substring(3, $(this).attr("id").length)].selected = false;
	setPageWrapperHeight();
});
