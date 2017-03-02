var advancedBar = false;
function showAdvancedBar()
{
	if(advancedBar)
	{
		$("#advanced-bar").css("margin-top", "-50px");
		$("#page-wrapper").css("margin-top", $("#menu-bar").outerHeight() + "px");
	}
	else
	{
		$("#advanced-bar").css("margin-top", "0px");
		$("#page-wrapper").css("margin-top", $("#menu-bar").outerHeight() + 64 + "px");
	}
	advancedBar = !advancedBar;
}

function setPageWrapperHeight()
{
	var advancedBarHeight = 0;
	if(advancedBar)
		advancedBarHeight = $("#advanced-bar").outerHeight();
	var menuHeight = $("#menu-bar").outerHeight() + advancedBarHeight + "px";
	$("#page-wrapper").css("margin-top", menuHeight);
}