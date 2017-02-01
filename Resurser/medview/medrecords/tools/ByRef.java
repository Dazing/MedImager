/**
 *  $Id: ByRef.java,v 1.2 2002/10/21 10:54:03 nazari Exp $
 * Copyright:    Copyright (c) 2002
 * Company:      Göteborg University
 * @author Nader Nazari
 * @version 1.0
 */


package medview.medrecords.tools;

 /**
  * Used as call by refrence parameter in  methods .
  */
public class ByRef {
    public boolean   boolVal;
    public int       intVal;
    public double    doubleVal;
    public float     floatVal;
    public Object    objVal;
    public String    strVal;

    public ByRef() {
        boolVal = false;
        intVal = 0;
        doubleVal = 0d;
        floatVal = 0f;
        objVal = null;
        strVal = null;
    }
}