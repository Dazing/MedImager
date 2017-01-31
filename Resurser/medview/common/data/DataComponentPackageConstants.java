/**
 * @(#) DataComponentPackageConstants.java
 */

package medview.common.data;

public interface DataComponentPackageConstants
{
	// SYNC FLAG
	
	/**
	 * Switched between true and false, to indirectly notify
	 * preference listeners that the global package data has
	 * changed. This needs to be done, since listening individually
	 * for each package's preference change events is no good, since 
	 * there is no way of knowing how many will further be added or
	 * removed. 
	 */
	public static final String GLOBAL_PACKAGE_SYNC_FLAG = "globalPackageSyncFlag";
	
	/**
	 * Switched between true and false, to indirectly notify
	 * preference listeners that the included package has changed
	 * for some application. It is then up to each listening application
	 * to see if it is their included package data that has changed,
	 * and if they need to update themselves.
	 */
	public static final String INCLUDED_PACKAGE_SYNC_FLAG = "includedPackageSyncFlag";
}
