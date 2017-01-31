//
//  TermEditDataHandlingSCP.java
//  MedView
//
//  Created by Olof Torgersson on Fri Dec 05 2003.
//  $Id: TermEditDataHandlingSCP.java,v 1.3 2008/11/13 17:03:34 oloft Exp $.
//

package medview.termedit.view.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.termedit.model.*;
import medview.termedit.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class TermEditDataHandlingSCP extends SettingsContentPanel implements MedViewLanguageConstants, GUIConstants {

    public String getTabLS() {
        return TAB_DATA_LOCATIONS_LS_PROPERTY;
    }

    public String getTabDescLS() {
        return TAB_DATA_LOCATIONS_DESCRIPTION_LS_PROPERTY;
    }

    protected void initSubMembers() {
        mediator = (TermEditFrame) subConstructorData[0];
    }

    protected void settingsShown() {
        dataPanel.setTermDefinitionLocationText(prefs.getTermDefinitionLocation());

        dataPanel.setTermValueLocationText(prefs.getTermValueLocation());

        dataPanel.setServerLocationText(prefs.getServerLocation());

        dataPanel.setUsesRemote(prefs.usesRemoteDataHandling());
    }

    protected void createComponents() {
        dataPanel = new DataLocationSettingsPanel(commandQueue, new JFrame());

        dataPanel.addDataPanelListener(new Listener());
    }

    protected void layoutPanel() {
        // layout the panel

        GUIUtilities.gridBagAdd(this, dataPanel,		0, 0, 1, 0, 1, 1, CENT, new Insets(0,0,0,0), BOTH);

        GUIUtilities.gridBagAdd(this, Box.createGlue(),	0, 1, 0, 1, 1, 1, CENT, new Insets(0,0,0,0), BOTH);
    }

    public TermEditDataHandlingSCP(CommandQueue queue, TermEditFrame mediator) {
        super(queue, new Object[] { mediator });
        prefs = Preferences.instance();
    }

    private Preferences prefs;

    private TermEditFrame mediator; // Not used

    private DataLocationSettingsPanel dataPanel; // Skip this as a variable, it's note used outside on gui-build



    private class Listener implements DataLocationSettingsPanel.DataLocationSettingsListener {
		public void localLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e) { /* Should do something? */ }

        public void serverLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e) {
            prefs.setServerLocation(e.getRequestedLocation());
        }

        public void useRemoteDataHandlingChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e) {
            prefs.setUseRemoteDataHandling(e.getRequestedUseFlag());
        }

        public void termDefinitionLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e) {
           prefs.setTermDefinitionLocation(e.getRequestedLocation());
        }

        public void termValueLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e) {
            prefs.setTermValueLocation(e.getRequestedLocation());
        }

		public void pdaDataLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e) {}

    }
}
