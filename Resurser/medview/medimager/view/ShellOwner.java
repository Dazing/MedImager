package medview.medimager.view;

import java.awt.*;

public interface ShellOwner
{
	public ShellState getCurrentState();

	public void pluginState(ShellState state);

	public boolean isOwnerComponent(Component comp);

	public ActionShell getShellForID(int id);
}
