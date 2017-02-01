/**
 * @(#) DataComponentPackagePanelEvent.java
 */

package medview.common.components.datapackage;

import java.util.*;

import medview.common.data.*;

public class DataComponentPackagePanelEvent extends EventObject
{
	private DataComponentPackage pack;
	
	public DataComponentPackagePanelEvent( Object source )
	{
		super(source);
	}
	
	public void setPackage( DataComponentPackage pack )
	{
		this.pack = pack;	
	}
	
	public DataComponentPackage getPackage( )
	{
		return pack;
	}
}
