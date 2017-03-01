var advancedBar = false;
function showAdvancedBar()
{
	if(advancedBar)
	{
		$("#advanced-bar").css("margin-top", "-45px");
		$("#page-wrapper").css("margin-top", "64px");
	}
	else
	{
		$("#advanced-bar").css("margin-top", "0px");
		$("#page-wrapper").css("margin-top", "130px");
	}
	advancedBar = !advancedBar;
}