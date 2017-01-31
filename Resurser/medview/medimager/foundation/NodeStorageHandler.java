package medview.medimager.foundation;

import medview.medimager.model.*;

public interface NodeStorageHandler
{
	public abstract void exportNode(String filePath, FolderNodeModel root) throws CouldNotExportException;

	public abstract FolderNodeModel importNode(String filePath) throws CouldNotImportException;
}
