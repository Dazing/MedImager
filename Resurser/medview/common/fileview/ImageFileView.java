/*
 * @(#)ImageFileView.java
 *
 * $Id: ImageFileView.java,v 1.4 2002/11/19 00:06:09 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.fileview;

import java.io.*;
import javax.swing.*;

import medview.datahandling.*;

public class ImageFileView extends javax.swing.filechooser.FileView implements MedViewMediaConstants
{
	public Icon getIcon(File f)
	{
		if (f.getName().toLowerCase().endsWith(".gif") ||
			f.getName().toLowerCase().endsWith(".jpg") ||
			f.getName().toLowerCase().endsWith(".jpeg"))
		{
			return new ImageIcon(); // dummy implementation...
		}
		else { return null; }
	}
}