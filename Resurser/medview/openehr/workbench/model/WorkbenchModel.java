//
//  Model.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-11-17.
//
//  $Id: WorkbenchModel.java,v 1.15 2009/01/05 11:59:25 oloft Exp $
//
package medview.openehr.workbench.model;

import java.io.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import java.util.*;

import br.com.zilics.archetypes.models.rm.RMObject;
import br.com.zilics.archetypes.models.rm.common.archetyped.Locatable;
import br.com.zilics.archetypes.models.rm.utils.xml.*;
import br.com.zilics.archetypes.models.rm.utils.path.context.PathEvaluationContext;

import br.com.zilics.archetypes.models.adl.parser.*;
import br.com.zilics.archetypes.models.adl.serializer.ADLSerializer;
import br.com.zilics.archetypes.models.am.archetype.Archetype;
import br.com.zilics.archetypes.models.am.archetype.constraintmodel.*;
import br.com.zilics.archetypes.models.am.openehrprofile.datatypes.basic.*;
import br.com.zilics.archetypes.models.am.openehrprofile.datatypes.quantity.*;
import br.com.zilics.archetypes.models.am.openehrprofile.datatypes.text.*;
import br.com.zilics.archetypes.models.rm.datatypes.quantity.*;
import br.com.zilics.archetypes.models.am.template.openehrprofile.Template;
import br.com.zilics.archetypes.models.am.archetype.assertion.*;
import br.com.zilics.archetypes.models.rm.exception.*;

import medview.datahandling.*;

import medview.openehr.workbench.model.tree.*;
import medview.openehr.workbench.model.exceptions.*;

public class WorkbenchModel {

    private DefaultTreeModel archetypesTreeModel = null;
    private DefaultTreeModel templatesTreeModel = null;
    private DefaultTreeModel singleArchetypeTreeModel = null;
	private ArchetypeOntologyTableModel atCodesTableModel = null;
	private ArchetypeConstraintDefinitionTableModel acCodesTableModel = null;
    
    private File selectedFile = null;
	private RMObject selectedObject = null;
	private String selectedLanguage = ArchetypeUtilities.DEFAULT_LANGUAGE;
	private ArchetypeNodeInfo.DisplayMode selectedDisplayMode = ArchetypeNodeInfo.DisplayMode.DOMAIN;
	
	private final static String ADL_FILE_ENDING = "." + ArchetypeUtilities.ADL_EXTENSION;
	
	private MedViewDataHandler mVDH;
	private List<WorkbenchModelListener> modelListeners;
	 
	public WorkbenchModel() {
	
		mVDH = MedViewDataHandler.instance();
		mVDH.addMedViewPreferenceListener(new PreferenceListener());
		modelListeners = new ArrayList();
		
	}
	
	public void addWorkbenchModelListener(WorkbenchModelListener wbl) {
        modelListeners.add(wbl);
    }
	
    public TreeModel getArchetypesTreeModel() {
        if (archetypesTreeModel == null) {
            initArchetypesTreeModel();
        }
        return archetypesTreeModel;
    }

    public TreeModel getTemplatesTreeModel() {
        if (templatesTreeModel == null) {
            initTemplatesTreeModel();
        }
        return templatesTreeModel;
    }

    // currently selected file
    public TreeModel getTreeModel() {
        if (singleArchetypeTreeModel == null) {
            initSingleArchetypeTreeModel();
        }
        return singleArchetypeTreeModel;
    }

	public TableModel getAtCodesTableModel () {
		if (atCodesTableModel == null) {
			atCodesTableModel = new ArchetypeOntologyTableModel();
		}
		return atCodesTableModel;
	}

    public TableModel getAcCodesTableModel () {
		if (acCodesTableModel == null) {
			acCodesTableModel = new ArchetypeConstraintDefinitionTableModel();
		}
		return acCodesTableModel;
	}

    public String getTextRepresentation(File f) {
        String name = f.getName();

        try {
            if (name.endsWith(ADL_FILE_ENDING)) {
                Archetype archetype = null;
                ADLParser parser = new ADLParser(f);

                archetype = parser.parse();

                System.out.println("Succesfully parsed " + name);

                ADLSerializer serializer = new ADLSerializer();

                setSelectedObject(archetype);

                return serializer.output(archetype);
            } else if (name.endsWith(".xml") || name.endsWith(".oet")) {
                RMObject rmObject = XmlParser.parseXml(new FileInputStream(f));

                setSelectedObject(rmObject);

                return XmlSerializer.serializeXml(rmObject, null);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());

            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Another exption for " + name);

            System.out.println("Exception: " + e.getMessage());

            e.printStackTrace();

        }
        return "";
    }

    public String getTextRepresentation() throws ArchetypeSerializerException {
		
		RMObject rmo = getSelectedObject();
		
		if ((rmo != null) && (rmo instanceof Archetype)) {
			try {
				ADLSerializer serializer = new ADLSerializer();
				
				return serializer.output((Archetype)rmo);
			}
			catch (IOException e) {
				throw new ArchetypeSerializerException(e);
			}
		}
		return "";
    }

    public String getTextRepresentation(String path) {
        return "";
    }

	public void validateSelectedArchetype() throws ArchetypeValidationException {
		RMObject rmo = getSelectedObject();
		
		if ((rmo != null) && (rmo instanceof Archetype)) {
			try {
			rmo.validate();
			}
			catch (ValidateException e) {
				throw new ArchetypeValidationException(e);
			}
		}
	}
	
    public String getValidationResult() {
        RMObject obj = getSelectedObject();
        String valOK = "Validation OK";

        String result = "Non archetype";

        if (obj instanceof Archetype) {
            try {
                obj.validate();

                result = valOK;

            } catch (Exception ex) {
                ex.printStackTrace();
                result = "Exception while validating: " + ex;
            }
        } /* else if (obj instanceof Template) {
        try {
        ((Template) obj).resolveArchetypes(archetypes);
        obj.validate();
        } catch(Exception ex) {
        error("Exception while validating: "  + inputFile + "\n" + ex);
        }
        } else if (obj instanceof Locatable) {
        Locatable l = (Locatable) obj;
        try {
        l.validate();
        if (l.getArchetypeDetails().getTemplateId() != null && templates != null) {
        Template t = templates.get(l.getArchetypeDetails().getTemplateId().getValue());
        t.semanticValidation(l);
        } else if (l.getArchetypeDetails().getArchetypeId() != null && archetypes != null) {
        Archetype a = archetypes.get(l.getArchetypeDetails().getArchetypeId().getValue());
        a.semanticValidation(l);
        }
        } catch(Exception ex) {
        error("Exception while validating: "  + inputFile + "\n" + ex);
        }
        }*/
        return result;

    }

    public String evaluateAPathExpression(String expr) {
        PathEvaluationContext context = null;

        String result = "";
        RMObject selObj = getSelectedObject();

        if (selObj != null) {
            if (selObj instanceof Archetype) {
                Archetype archetype = (Archetype) selObj;
                try {
                    archetype.validate();
                    context = archetype.getPathEvaluatorContext();

                    result = context.parseAndEvaluate(expr).toString();
                } catch (Exception e) {
                    e.printStackTrace();

                    result = "Error evaluating " + expr + ": " + e.getMessage();
                }
            } else if (selObj instanceof Template) {
                result = "Templates not implemented yet";
            } else if (selObj instanceof Locatable) {
                result = "Locatables not implemented yet";
            }
        } else {
            result = "No context for evaluation given. Please, select an archetype or template";
        }
        return result;
    }

    public String getXMLRepresentation() throws ArchetypeSerializerException {

        if (getSelectedObject() != null) {
            try {
                return XmlSerializer.serializeXml(getSelectedObject(), null);
            } catch (Exception e) {
                e.printStackTrace();
				throw new ArchetypeSerializerException(e);
            }
        }
        return "";
    }

	public List<String> getAvailableLanguages() {
		RMObject rmo = getSelectedObject();
		
		if (rmo instanceof Archetype) {
			return ArchetypeUtilities.getAvailableLanguages((Archetype)rmo);
		}
		return new ArrayList();
	}
	
    public RMObject getSelectedObject() {
        return selectedObject;
    }

	public void setLanguage(String lang) {
		selectedLanguage = lang;
		atCodesTableModel.setLanguage(lang);
        acCodesTableModel.setLanguage(lang);
		setSingleArchetypeTreeLanguage(lang);
	}
	
	public ArchetypeNodeInfo.DisplayMode getDisplayMode() {
		return selectedDisplayMode;
	}
	
	public void setDisplayMode(ArchetypeNodeInfo.DisplayMode mode) {
			selectedDisplayMode = mode;
			setSingleArchetypeTreeDisplayMode(mode);
			fireDisplayModeChanged();
	}
	
	public void setSelectedArchetypeFile(File f) throws ArchetypeParseException {
		selectedFile = f;
		
		String name = f.getName();
		
        try {
            if (name.endsWith(ADL_FILE_ENDING)) {
				System.out.println("Will parse " + name);

                Archetype archetype = null;
                ADLParser parser = new ADLParser(f);
				
				// System.out.println("Parser created for " + name);

                archetype = parser.parse();
				
                System.out.println("Succesfully parsed " + name);
				
                setSelectedObject(archetype);
				setLanguage(ArchetypeUtilities.getOriginalLanguage(archetype));
            }
		} catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
			
            e.printStackTrace();
			
			throw new ArchetypeParseException(e);
			
         }  catch (br.com.zilics.archetypes.models.adl.parser.ParseException e) {
			
			System.out.println("Exception: " + e.getMessage());
			
			e.printStackTrace();
			
			throw new ArchetypeParseException(e);

		} catch (Exception e) {
			System.out.println("Another exption for " + name);
			
			System.out.println("Exception: " + e.getMessage());
			
			throw new ArchetypeParseException(e);
		}
    }
	
    private void setSelectedObject(RMObject rmo) {
        // System.out.println("setSelectedObject: " + rmo.toString());

        singleArchetypeTreeModel = null;

        selectedObject = rmo;
		
		if (rmo instanceof Archetype) {
		
			ArchetypeOntologyTableModel tm = (ArchetypeOntologyTableModel)getAtCodesTableModel();
			
			tm.setArchetype((Archetype)rmo);

            ArchetypeConstraintDefinitionTableModel ctm = (ArchetypeConstraintDefinitionTableModel )getAcCodesTableModel();
			
			ctm.setArchetype((Archetype)rmo);
		}
		
    }

	private void fireArchetypesLocationChanged() {
        int count = modelListeners.size();
        for (int i=0; i<count; i++) {
            WorkbenchModelListener ml = modelListeners.get(i);
            ml.archetypesLocationChanged(new WorkbenchModelEvent(this));
        }
    }

	private void fireTemplatesLocationChanged() {
        int count = modelListeners.size();
        for (int i=0; i<count; i++) {
            WorkbenchModelListener ml = modelListeners.get(i);
            ml.templatesLocationChanged(new WorkbenchModelEvent(this));
        }
    }

	private void fireDisplayModeChanged() {
        int count = modelListeners.size();
        for (int i=0; i<count; i++) {
            WorkbenchModelListener ml = modelListeners.get(i);
            ml.displayModeChanged(new WorkbenchModelEvent(this));
        }
    }

    private void initArchetypesTreeModel() {
        DefaultMutableTreeNode rootNode = null;

        String path = Preferences.instance().getArchetypesLocation();

        File f = new File(path);

        if (f == null) {
            rootNode = new DefaultMutableTreeNode("Archetype Tree Root");
        } else {
            rootNode = new FileTreeNode(f);
            populateFileTree(rootNode, ArchetypeUtilities.ADL_EXTENSION);
        }

        archetypesTreeModel = new DefaultTreeModel(rootNode);
        archetypesTreeModel.nodeChanged(rootNode);

    }

    private void initTemplatesTreeModel() {
		DefaultMutableTreeNode rootNode = null;
		
        String path = Preferences.instance().getTemplatesLocation();
		
        File f = new File(path);
		
        if (f == null) {
            rootNode = new DefaultMutableTreeNode("Template Tree Root");
        } else {
            rootNode = new FileTreeNode(f);
            populateFileTree(rootNode, ArchetypeUtilities.OET_EXTENSION);
        }
		
        templatesTreeModel = new DefaultTreeModel(rootNode);
        templatesTreeModel.nodeChanged(rootNode);
    }
	
    private void initSingleArchetypeTreeModel() {

        System.out.println("Start building tree");

        singleArchetypeTreeModel = null;

        if (getSelectedObject() != null && getSelectedObject() instanceof Archetype) {
            Archetype archetype = (Archetype) getSelectedObject();

            try {
                archetype.validate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            DefaultMutableTreeNode archetypeNode = new DefaultMutableTreeNode(archetype);

            singleArchetypeTreeModel = new DefaultTreeModel(archetypeNode);

            populateArchetypeTree(archetypeNode, archetype.getDefinition());

            singleArchetypeTreeModel.nodeChanged(archetypeNode);
			setSingleArchetypeTreeDisplayMode(selectedDisplayMode);

        } else {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Archetype");
            singleArchetypeTreeModel = new DefaultTreeModel(rootNode);
            singleArchetypeTreeModel.nodeChanged(rootNode);
        }

        System.out.println("End building tree");
    }

	private void setSingleArchetypeTreeLanguage(String lang) {
		if (singleArchetypeTreeModel != null) {
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)singleArchetypeTreeModel.getRoot();
			
			Enumeration treeEnum = rootNode.breadthFirstEnumeration();
			
			while(treeEnum.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeEnum.nextElement();
				
				Object userObject = node.getUserObject();
				
				if (userObject instanceof ArchetypeNodeInfo) {
					ArchetypeNodeInfo ani = (ArchetypeNodeInfo)userObject;
					ani.setLanguage(lang);
				}
				singleArchetypeTreeModel.nodeChanged(node);
			}
			
			singleArchetypeTreeModel.nodeChanged(rootNode);
			
		}
	}

	private void setSingleArchetypeTreeDisplayMode(ArchetypeNodeInfo.DisplayMode mode) {
		if (singleArchetypeTreeModel != null) {
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)singleArchetypeTreeModel.getRoot();
			
			Enumeration treeEnum = rootNode.breadthFirstEnumeration();
			
			while(treeEnum.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeEnum.nextElement();
				
				Object userObject = node.getUserObject();
				
				if (userObject instanceof ArchetypeNodeInfo) {
					ArchetypeNodeInfo ani = (ArchetypeNodeInfo)userObject;
					// System.out.println("Setting mode: " + mode.toString());
					ani.setDisplayMode(mode);
				}
				singleArchetypeTreeModel.nodeChanged(node);
			}
			
			singleArchetypeTreeModel.nodeChanged(rootNode);
			
		}
	}
	
    private void populateFileTree(DefaultMutableTreeNode parent, String extension) {
		
        // System.out.println(parent.toString());
        
        if (parent instanceof FileTreeNode){
            FileTreeNode fp = (FileTreeNode)parent;
            Object o = fp.getUserObject();
			
			File p = (File)o;
			
			if (!p.isHidden()) {
				if (p.isDirectory()) {
					
					File[] files = p.listFiles();
					
					for (int i = 0; i < files.length; i++) {
						File thisFile = (File) files[i];
						
						if (!thisFile.isHidden()) {
							if (thisFile.getName().endsWith(extension)) {
								FileTreeNode child = new FileTreeNode(thisFile);
								
								parent.add(child);
							}
							if (thisFile.isDirectory()) {
								FileTreeNode child = new FileTreeNode(thisFile);
								
								parent.add(child);
								populateFileTree(child, extension);
							}
						}
					}
                }
            }
        }
	}
	
   
    

    private void populateArchetypeTree(DefaultMutableTreeNode parent, CObject cObject) {

        //System.out.println("pat cObject: " + cObject.toString());

        if (cObject instanceof CComplexObject) {
		
            buildcComplexObjectSubTree(parent, (CComplexObject) cObject);
			
        } else if (cObject instanceof CPrimitiveObject) {

            buildCPrimitiveObjectSubTree(parent, (CPrimitiveObject) cObject);

        } else if (cObject instanceof CCodePhrase) {

            buildcCodePhraseSubTree(parent, (CCodePhrase) cObject);

        } else if (cObject instanceof CDvQuantity) {

            buildCDvQuantitySubTree(parent, (CDvQuantity) cObject);

        } else if (cObject instanceof CDvOrdinal) {

            buildCDvOrdinalSubTree(parent, (CDvOrdinal) cObject);

        } else if (cObject instanceof CDvState) {

            buildCDvStateSubTree(parent, (CDvState) cObject);

        } else if (cObject instanceof ArchetypeInternalRef) {
		
            buildArchetypeInternalRefSubTree(parent, (ArchetypeInternalRef) cObject);

        } else if (cObject instanceof ArchetypeSlot) {

            buildArchetypeSlotSubTree(parent, (ArchetypeSlot) cObject);

        } else if (cObject instanceof ConstraintRef) {

            buildConstraintRefSubTree(parent, (ConstraintRef) cObject);
        } else {
            DefaultMutableTreeNode aNode = new DefaultMutableTreeNode("??? " + cObject.toString());
            parent.add(aNode);
            System.out.println("   else");
        }

    }

    private void buildcComplexObjectSubTree(DefaultMutableTreeNode parent, CComplexObject cComplexObj) {

        Map<String, CAttribute> attributes = cComplexObj.getAttributes();

        //System.out.println("bco: "+ cComplexObj.toXml());

        DefaultMutableTreeNode cNode = new DefaultMutableTreeNode(new CComplexObjectNodeInfo(cComplexObj));
        parent.add(cNode);

        if (attributes != null) {

            Set<String> keySet = attributes.keySet();
            Iterator<String> keyIterator = keySet.iterator();

            //System.out.println("path " + cComplexObj.getCanonicalPath());
            //System.out.println("obj " + cComplexObj.toString());

            while (keyIterator.hasNext()) {
                String key = keyIterator.next();

                Boolean skip = key.equals("value") && cComplexObj.getRmTypeName().equals("DV_CODED_TEXT");

                // System.out.println("       key " + key + ", skip " + skip);
                //ArchetypeViewTreeNode aNode = new ArchetypeViewTreeNode(key);

                //cNode.add(aNode);

                if (!skip) {
                                 buildcAttributeSubTree(cNode, attributes.get(key));
                }
   
            }

        }
    }

    private void buildcAttributeSubTree(DefaultMutableTreeNode parent, CAttribute cAttribute) {

        DefaultMutableTreeNode aNode;

        if (cAttribute instanceof CSingleAttribute) {

            aNode = new DefaultMutableTreeNode(new CSingleAttributeNodeInfo((CSingleAttribute) cAttribute));
            parent.add(aNode);

            CSingleAttribute cSingleAttr = (CSingleAttribute) cAttribute;

            // System.out.println("cSingleA: " + cSingleAttr.toString());

            List<CObject> alternatives = cSingleAttr.getAlternatives();

            if (alternatives != null) {
                Iterator<CObject> altIterator = alternatives.iterator();

                // System.out.println("   Alternatives");
                while (altIterator.hasNext()) {
                    CObject cObject = altIterator.next();

                    // System.out.println("         Alt: " + cObject.toString());

                    populateArchetypeTree(aNode, cObject);

                }
            } else {
                System.out.println("   No alternatives");
            }
        } else if (cAttribute instanceof CMultipleAttribute) {

            aNode = new DefaultMutableTreeNode(new CMultipleAttributeNodeInfo((CMultipleAttribute) cAttribute));
            parent.add(aNode);

            CMultipleAttribute cMultipleAttr = (CMultipleAttribute) cAttribute;
            List<CObject> members = cMultipleAttr.getMembers();

            if (members != null) {
                Iterator<CObject> memIterator = members.iterator();

                while (memIterator.hasNext()) {
                    CObject cObject = memIterator.next();
                    populateArchetypeTree(aNode, cObject);

                }
            }

        }
    }

    private void buildcCodePhraseSubTree(DefaultMutableTreeNode parent, CCodePhrase cCodePhrase) {

        Archetype archetype = cCodePhrase.getOwnerArchetype();

        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(new CCodePhraseNodeInfo(cCodePhrase));
        parent.add(aNode);

        List<String> codeList = cCodePhrase.getCodeList();

        if (codeList != null) {
            Iterator<String> iterator = codeList.iterator();
            while (iterator.hasNext()) {
                String code = iterator.next();
                // String text = ArchetypeUtilities.getTextValue(archetype, code, ArchetypeUtilities.DEFAULT_LANGUAGE);

                //aNode.add(new DefaultMutableTreeNode("! code: " + code));
                aNode.add(new DefaultMutableTreeNode(new TermNodeInfo(code, archetype)));
            }
        }

    }

    private void buildCDvQuantitySubTree(DefaultMutableTreeNode parent, CDvQuantity cDvQuantity) {

        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(new CDvQuantityNodeInfo(cDvQuantity));
        parent.add(aNode);

        List<CQuantityItem> quantityList = cDvQuantity.getList();

        if (quantityList != null) {
            Iterator<CQuantityItem> iterator = quantityList.iterator();
            while (iterator.hasNext()) {
                CQuantityItem qItem = iterator.next();

                aNode.add(new DefaultMutableTreeNode(new CQuantityItemNodeInfo(qItem)));
            }
        }
    }

    private void buildCDvOrdinalSubTree(DefaultMutableTreeNode parent, CDvOrdinal cDvOrdinal) {

        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(new CDvOrdinalNodeInfo(cDvOrdinal));
        parent.add(aNode);

        Set<DvOrdinal> ordinalList = cDvOrdinal.getList();
        if (ordinalList != null) {
            Object assumed = null;
            if (cDvOrdinal.hasAssumedValue()) {
                assumed = cDvOrdinal.getAssumedValue();
            }
            ArrayList<DvOrdinal> list = new ArrayList(ordinalList);

            Collections.sort(list);

            Iterator<DvOrdinal> iterator = list.iterator();
            while (iterator.hasNext()) {
                DvOrdinal oItem = iterator.next();
                Boolean isAssumed = (assumed != null) && oItem.equals(assumed);
                
                aNode.add(new DefaultMutableTreeNode(new DvOrdinalNodeInfo(oItem, cDvOrdinal.getOwnerArchetype(), isAssumed)));
            }
        }
    }

    private void buildCDvStateSubTree(DefaultMutableTreeNode parent, CDvState cDvState) {

        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(new CDvStateNodeInfo(cDvState));
        parent.add(aNode);
    }

    private void buildCPrimitiveObjectSubTree(DefaultMutableTreeNode parent, CPrimitiveObject cPrimitiveObj) {

        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(new CPrimitiveObjectNodeInfo(cPrimitiveObj));
        parent.add(aNode);

        //System.out.println("buildCPrimitiveObjectSubTree, path: " + cPrimitiveObj.getCanonicalPath());
        //System.out.println("buildCPrimitiveObjectSubTree, item: " + cPrimitiveObj.getItem().toString());

    //  aNode.add(new DefaultMutableTreeNode(cPrimitiveObj.getItem()));

    }

    private void buildArchetypeInternalRefSubTree(DefaultMutableTreeNode parent, ArchetypeInternalRef aiRef) {

        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(new ArchetypeInternalRefNodeInfo(aiRef));
        parent.add(aNode);

    }

    private void buildArchetypeSlotSubTree(DefaultMutableTreeNode parent, ArchetypeSlot as) {

        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(new ArchetypeSlotNodeInfo(as));
        parent.add(aNode);

        List<Assertion> assertions = as.getIncludes();
        if (assertions != null) {

            Iterator<Assertion> iterator = assertions.iterator();
            while (iterator.hasNext()) {
                Assertion a = iterator.next();

                aNode.add(new DefaultMutableTreeNode(new IncludesAssertionNodeInfo(a)));
            }

        }
        assertions = as.getExcludes();
        if (assertions != null) {

            Iterator<Assertion> iterator = assertions.iterator();
            while (iterator.hasNext()) {
                Assertion a = iterator.next();

                aNode.add(new DefaultMutableTreeNode(new ExcludesAssertionNodeInfo(a)));
            }
        }
    }

    private void buildConstraintRefSubTree(DefaultMutableTreeNode parent, ConstraintRef cRef) {

        DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(new ConstraintRefNodeInfo(cRef));
        parent.add(aNode);

    }
	
	  /**
     * Listens to the datahandling layer for changes that could
     * affect various resources used in the application.
     */
    private class PreferenceListener implements MedViewPreferenceListener {
	
        public void userPreferenceChanged(MedViewPreferenceEvent e) {
		
			String prefsKey = e.getPreferenceName();
			
            if (prefsKey.equals(Preferences.ArchetypesLocation)) {
				initArchetypesTreeModel();
				fireArchetypesLocationChanged();
               
			} else if (prefsKey.equals(Preferences.TemplatesLocation)) {
				initTemplatesTreeModel();
                fireTemplatesLocationChanged();
			}
        }
        
        public void systemPreferenceChanged(MedViewPreferenceEvent e) {
        }
    }

}