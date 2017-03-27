var advancedBar = false;
var settingsBar = false;

function showExtraBar(bar)
{
	if(bar == "advanced")
	{
		toggleSettingsBar(true);
		toggleAdvancedBar();
	}
	else if(bar == "settings")
	{
		toggleAdvancedBar(true);
		toggleSettingsBar();
	}
}
function toggleAdvancedBar(hide)
{
	if(advancedBar || hide)
	{
		$("#advanced-bar").css("margin-top", "-100px");
		setPageWrapperHeight(0);
		$("#advanced-button").css("transform", "rotate(0deg)");
		advancedBar = false;
	}
	else
	{
		$("#advanced-bar").css("margin-top", "0px");
		setPageWrapperHeight(104);
		$("#advanced-button").css("transform", "rotate(180deg)");
		advancedBar = true;
	}
}
function toggleSettingsBar(hide)
{
	if(settingsBar || hide)
	{
		$("#settings-bar").css("margin-top", "-50px");
		setPageWrapperHeight(0);
		$("#settings-button").css("transform", "rotate(0deg)");
		settingsBar = false;
	}
	else
	{
		$("#settings-bar").css("margin-top", "0px");
		setPageWrapperHeight(52);
		$("#settings-button").css("transform", "rotate(180deg)");
		settingsBar = true;
	}
}

function setPageWrapperHeight(extra)
{
	$("#page-wrapper").css("margin-top", $("#menu-bar").outerHeight() + extra + "px");
	$(".collections-menu").css("margin-top", $("#menu-bar").outerHeight() + extra + "px");
}