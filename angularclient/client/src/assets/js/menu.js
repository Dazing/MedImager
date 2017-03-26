var advancedBar = false;
var settingsBar = false;

function showExtraBar(bar)
{
	if(bar == "advanced")
	{
		toggleAdvancedBar();
		toggleSettingsBar(true);
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
		advancedBar = false;
	}
	else
	{
		$("#advanced-bar").css("margin-top", "0px");
		setPageWrapperHeight(104);
		advancedBar = true;
	}
}
function toggleSettingsBar(hide)
{
	if(settingsBar || hide)
	{
		$("#settings-bar").css("margin-top", "-50px");
		setPageWrapperHeight(0);
		settingsBar = false;
	}
	else
	{
		$("#settings-bar").css("margin-top", "0px");
		setPageWrapperHeight(52);
		settingsBar = true;
	}
}

function setPageWrapperHeight(extra)
{
	$("#page-wrapper").css("margin-top", $("#menu-bar").outerHeight() + extra + "px");
	$(".collections-menu").css("margin-top", $("#menu-bar").outerHeight() + extra + "px");
}