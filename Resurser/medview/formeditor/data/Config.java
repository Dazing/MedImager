/*
 * $Id: Config.java,v 1.3 2003/11/12 20:00:51 oloft Exp $
 *
 * Created on July 5, 2001, 10:13 PM
 *
 */

package medview.formeditor.data;

import java.io.*;
import java.util.*;

import medview.formeditor.interfaces.*;
import medview.formeditor.components.ConfigHandler;
import medview.formeditor.tools.*;


/**
 *
 * @author Nader, Nils
 * @version
 */

public class Config extends Object {
    
    private static HashMap mConfigMap = new HashMap();
    private static boolean mSaved = false;
    
    // private static Config mConfigInstance = new Config();
    private static String mConfigFileName;
    private static DatahandlingHandler mDH = DatahandlingHandler.getInstance();
    
    public static String Preview_Template  = "MRpreview_template";
    public static String Translator        = "MRtranslator";
    
    public static String TermDefLocation   = "MRtermLocation";
    public static String ValueDefLocation  = "MRvalueLocation";
    
    public static String PictureCategory   = "MRpicturecategory";
    public static String PictureTermName    = "MRPictureTermName";
    
    public static String UsePlaque         = "MRIsPlaqueUsed";

    public static String Language	   = "MRLanguage";
    public static String IsDocumentModeOn   = "MRIsDocumentModeOn";
    public static String LastOpenSaveFolder = "FELastOpenSaveFolder";

    // Used to be in Mr... MRConst which is being removd (not sure what the intended design is)
    private static final String DefaultLanguage = "English";
    private static final String DefaultIsDocumentModeOn = "false";
    private static final String DefaultIsPlaqueUsed = "false";
    private static final String DefaultImageCategoryName = "Images";
    private static final String DefaultImageTermName = "Photo";
    
    private static String[] listOfValues =
    {
        Translator,
        Preview_Template,

        TermDefLocation,
        ValueDefLocation,

        PictureCategory,
        PictureTermName,
        Language,
        IsDocumentModeOn,
        UsePlaque,
        LastOpenSaveFolder
    };
     
     private static String[] listOfInputValues =
     {
      Translator,
      Preview_Template,
      
      TermDefLocation,
      ValueDefLocation,
      
      PictureCategory,
      PictureTermName,
      Language,
      IsDocumentModeOn,
      UsePlaque};
      
      
      /** Creates new Config */
      private Config() {
         mConfigMap = new HashMap();
      }
      
      private static String makeDirectory(String pKeyName,String pMrConst){
          String lFileName =  "." + File.separatorChar + pMrConst;
          File aDir = new File(lFileName);
          if(aDir == null){
              Ut.error("Can not create a directory from <" + lFileName + ">");
              return null;
          }
          try{
              if(aDir.mkdir())
                  Ut.prt("Make MVD DIR The CanonicalPath is  <" + aDir.getCanonicalPath() + ">");
              else
                  Ut.prt("Can not create <" + aDir.getCanonicalPath() + "> The directory  exists already" );
          }
          catch(Exception e){
              Ut.prt("Can not convert <" + lFileName + "> to a directory");
              e.printStackTrace();
              return null;
          }
          String thePath = aDir.getAbsolutePath();
          
          // Ut.prt("The absolutePath  is  <" + thePath +        ">");
          // Ut.prt("The Name of dir   is  <" + aDir.getName() + ">");
          // Ut.prt("The getPath       is  <" + aDir.getPath() + ">");
          
          if(thePath == null){
              Ut.error("Can not read the path of the <" + aDir.getName() + "> directory");
              return null;
          }
          mDH.setProperty( pKeyName, thePath );
          mConfigMap.put(pKeyName.toUpperCase(),thePath);
          return thePath;
      }
      
            
      /**
       * 
       * @return value in properties or configmap if available, otherwise returns the pVal (default)
       */
      private static String makeValue(String pKey,String pVal){
          String aVal =  getValue(pKey);
          if(aVal == null) { // If no value available from getValue method
              mDH.setProperty( pKey, pVal ); // Store DEFAULT value in mDH // NE              
              mConfigMap.put(pKey.toUpperCase(),pVal); // Store DEFAULT value in configMap // NE
              aVal = pVal;
          }
          return aVal;
      }
      
      /**
       * Gets the stored MDH property value // NE
       */
      private static String getMDHPropertyValue(String pKey) {
          
          if (pKey == null) {
              Ut.error("Class Config mtd getMDHPropertyValue parameter is null");
              return null;
          }
          else {
              String aVal = mDH.getProperty(pKey);
              if( isNotNullOrEmpty(aVal)) {
                  return (String) aVal;
              }
          }
          return null;
      }
      
      /**
       * Look up a value in the mConfigMap.
       * If the value does not exist in the configMap, revert to the stored Proprety value for the same key.
       */
      private static String getValue(String pKey) {
          String aVal = (String) mConfigMap.get(pKey.toUpperCase());
          if (isNotNullOrEmpty(aVal)) {
              //System.out.println("getValue(" + pKey + "): found " + aVal + " in configMap");
              return (String)aVal;
          }
          
          //System.out.println("Did not find " + pKey + " in configmap (aVal=[" + aVal + "], doing getMDHPropertyValue");
          aVal = getMDHPropertyValue(pKey);
          if (isNotNullOrEmpty(aVal)) {
              mConfigMap.put(pKey.toUpperCase(),aVal); // Store the looked up property value in the configmap
              //System.out.println("MDHproperty gave value " + aVal);
              return (String) aVal;
          } else {
              Ut.prt("Config getValue(): Could not find config value for " + pKey + " at all!");
              return null;
          }          
      }

      /**
       * Get array of type { "key=value", "key2=value2" } // NE
       */
      public static String[] getValues(){
          String[] tmpArray = new String[listOfInputValues.length];
          
          for(int i=0; i< listOfInputValues.length; i++){
              String aKey = listOfInputValues[i];
              /*
              if(aKey.equals(TermDefLocation))
                  tmpArray[i] = aKey + "=" + mDH.getTermDefinitionLocation();                                
              else if(aKey.equals(ValueDefLocation))
                  tmpArray[i] = aKey + "=" + mDH.getTermValueLocation();              
              else */
                  tmpArray[i] = aKey + "=" + getMDHPropertyValue(aKey);
          }
          return tmpArray;
      }
      
      public static void showConfigHandler(){
          String[] tmpArray = getValues();
          ConfigHandler cfh = new ConfigHandler(tmpArray,true);
          cfh.setVisible(true);
      }
            
      public static String getPictureCategoryName() {
          return makeValue(PictureCategory,DefaultImageCategoryName);
      }
      
      public static String getPictureTermName() {
          return makeValue(PictureTermName,DefaultImageTermName);
      }
            
      public static boolean isPlaqueUsed(){
          String aVal = makeValue(UsePlaque,DefaultIsPlaqueUsed);
          
          if(aVal.equalsIgnoreCase("true")) return true;
          return false;
      }

      public static boolean isDocumentModeOn(){
          String aVal = makeValue(IsDocumentModeOn, DefaultIsDocumentModeOn);

          if(aVal.equalsIgnoreCase("true")) return true;
          return false;
      }

      public static String getTemplatePath() {
          return mDH.getProperty(LastOpenSaveFolder);
      }

      /**
      * Stores the path up to but not including the last path component.
       */
      public static void setTemplatePath(String aPath) {
          String thePath = aPath.substring(0,aPath.lastIndexOf(File.separator));
          mDH.setProperty(LastOpenSaveFolder, thePath);
      }
      
      public static String getPreviewTemplate() {
          String aVal = getValue(Preview_Template);
          if(aVal == null) return null;
          return aVal;
      }
      
      
      public static String getPathToTranslator() {
          String aVal = getValue(Translator);
          if(aVal == null) return null;
          
          String thePath = aVal.substring(0,aVal.lastIndexOf(File.separator));
          return thePath;
      }
      
      public static String getTranslator() {
          return  getValue(Translator);
      }
      
      /**
       * Loads the Terms location into the config map
       */
      public static String  getTermValueLocation() {
          String location = getValue(ValueDefLocation);
          if (isNotNullOrEmpty(location))
              return location;
          return null;
      }      
      
      private static boolean isNotNullOrEmpty(String val) {
          if (val == null)
            return false;
          if (val.trim().equals(""))
              return false;
          return true;
      }
      
      public static String  getTermDefinitionLocation() {          
          String location = getValue(TermDefLocation);
          if (isNotNullOrEmpty(location))
              return location;
          return null;
      }

      public static String getLanguage() {
          return makeValue(Language, DefaultLanguage);
      }
      
      /**
       * Checks listOfinputValues. 
       */
      private static void checkReading() throws IOException {
          for(int i=0; i< listOfInputValues.length; i++){
              String aStr = getValue(listOfInputValues[i]);
              if(aStr == null || aStr.length() == 0){
                  String term = listOfInputValues[i];
                  throw new IOException("Error: The term  (" + term + ") doesn't have a value.");                                    
              }
          }
      }            
      
      // Saves config content to properties
      public static void  save(String[] values){
          
          for(int i=0; i< values.length; i++){
          String  aRow    = values[i];
              int     indx    = aRow.indexOf('=');
              String aKey     = aRow.substring(0,indx);
              String aVal     = aRow.substring(indx+1);
                            
              if (aKey.equals(TermDefLocation)) {
                  //System.out.println("save: setTermDefinitionLocation(" + aVal +")");
                    mDH.setTermDefinitionLocation(aVal);
              } else if (aKey.equals(ValueDefLocation)) {
                  //System.out.println("save: setTermValueLocation(" + aVal +")");
                    mDH.setTermValueLocation(aVal);  
              }              
              
              //System.out.println("mDH.setProperty(" + aKey + ", " + aVal +")");
              mDH.setProperty( aKey, aVal );
              mConfigMap.put(aKey, aVal);
          }          
      }
     
      private static void checkFileReadable(String path) throws IOException {
          if (path == null)
              throw new IOException("One of the files is null");
          else if (path.trim().equals("")) 
              throw new IOException("One of the files names is empty");
          File file = new File(path);          
          
          if (! file.exists())
              throw new IOException("File " + path + " does not exist");
          if (! file.canRead())
              throw new IOException("Can not read file " + path);          
      }
      
      private static void checkDirectoryReadable(String path) throws IOException {
          checkFileReadable(path);
          
          if (! new File(path).isDirectory()) 
              throw new IOException(path + " is not a directory");
      }
      
      /*
      call from medrecord to initiate the information needed starting the application
      */
      public static void readConfigInfo() {
          
          //boolean all_ok = false; - OT
          //while (! all_ok) { - OT
              try {             
                  //all_ok = true; - OT
                  checkFileReadable(getTranslator());
                  checkFileReadable(getPreviewTemplate());
                  
                  String termDefLocation = getTermDefinitionLocation();
                  checkFileReadable(termDefLocation);
                  mDH.setTermDefinitionLocation(termDefLocation);
                  String termValueLocation = getTermValueLocation();
                  checkFileReadable(termValueLocation);
                  mDH.setTermValueLocation(termValueLocation);
                  
                  getPictureCategoryName();
                  getPictureTermName();
                  isPlaqueUsed();
                  
                  checkReading(); 
                                                      
              } catch (IOException ioe) {                  
                  // all_ok = false; // Keeps us in the while loop              
                  Ut.error( ioe.getMessage());   
                  showConfigHandler();
                  Ut.message("The application will be terminated. Changes have effect the next time it is started.");
                  System.exit(0); // OT
              }
          //} // OT
      }
      
        
  }
