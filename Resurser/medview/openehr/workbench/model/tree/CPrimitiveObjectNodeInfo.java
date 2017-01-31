//
//  CPrimitiveObjectNodeInfo.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-09.
//
//  $Id: CPrimitiveObjectNodeInfo.java,v 1.9 2008/12/30 22:31:40 oloft Exp $
//
package medview.openehr.workbench.model.tree;

import java.util.*;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.CPrimitiveObject;

import br.com.zilics.archetypes.models.am.archetype.constraintmodel.primitive.*;
import br.com.zilics.archetypes.models.rm.datatypes.quantity.datetime.DvDuration;
import br.com.zilics.archetypes.models.rm.support.basic.*;

public class CPrimitiveObjectNodeInfo extends CObjectNodeInfo {

    private CPrimitiveObject cPrimObj;

    public CPrimitiveObjectNodeInfo(CPrimitiveObject c) {
        cPrimObj = c;
    }

    @Override
    public String getImageName() {
        return "c_primitive_object.gif";
    }

    @Override
    public String toString() {

        String display = cPrimObj.getNodeName(getLanguage());

        String occurs = cPrimObj.getOccurrences().toString();

        if (display == null) {
            display = cPrimObj.getRmTypeName();
        }

        CPrimitive item = cPrimObj.getItem();

        if (item instanceof CBoolean) {
            return toString((CBoolean) item);
        } else if (item instanceof CDate) {
            return toString((CDate) item);
        } else if (item instanceof CDateTime) {
            return toString((CDateTime) item);
        } else if (item instanceof CDuration) {
            return toString((CDuration) item);
        } else if (item instanceof CInteger) {
            return toString((CInteger) item);
        } else if (item instanceof CReal) {
            return toString((CReal) item);
        } else if (item instanceof CString) {
            return toString((CString) item);
        } else if (item instanceof CTime) {
            return toString((CTime) item);
        } else {
            return "Unknown CPrimitiveObject: " + /*cPrimObj.getItem().toString() +*/ ">" + display + " (CP, " + occurs + ")";
        }

    }

    private String toString(CBoolean cb) {
        String display = "";

        Boolean isTrueValid = cb.isTrueValid();
        Boolean isFalseValid = cb.isFalseValid();

        if (isTrueValid) {
            display = "True";
            if (isFalseValid) {
                display = display + ",";
            }
        }
        if (isFalseValid) {
            display = display + "False";
        }
        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = cb.getType() + " " + display;
        }
        return display;
    }

    private String toString(CDate cd) {
        String display = cd.getPattern();

        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = cd.getType() + " " + display;
        }
        return display;
    }

    private String toString(CString s) {
        String pat = s.getPattern();
        List<String> list = s.getList();
        String display;

        if (pat != null) {
            display = pat;
        } else if (list != null) {
            display = list.toString();
        } else {
            display = "CString both pattern and list null";
        }
        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = s.getType() + " " + display;
        }
        return display;

    }

    private String toString(CDuration cd) {
        String display = cd.getPattern();

        if (display == null) {
            Interval<DvDuration> interval= cd.getRange();
            if (interval != null) {
                display = interval.toString();
            }
        }
        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = cd.getType() + " " + display;
        }
        return display;
    }

    private String toString(CDateTime cdt) {
        String display = cdt.getPattern();

        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = cdt.getType() + " " + display;
        }
        return display;
    }

    private String toString(CInteger cd) {
        Interval<Integer> range = cd.getRange();
        String display = null;

        if (range != null) {
            display = range.toString();
        } else {
            List<Integer> list = cd.getList();

            if (list != null) {
                display = list.toString();
            }
        }
        if (display == null) {
            display = cd.toString();
        }
        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = cd.getType() + " " + display;
        }
        return display;
    }

    private String toString(CReal cd) {
        Interval<Double> range = cd.getRange();
        String display = null;

        if (range != null) {
            display = range.toString();
        } else {
            List<Double> list = cd.getList();

            if (list != null) {
                display = list.toString();
            }
        }
        if (display == null) {
            display = cd.toString();
        }
        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = cd.getType() + " " + display;
        }
        return display;
    }

    private String toString(CTime cd) {
        String display = "CTime: " + cd.getPattern();
        if (getDisplayMode() == DisplayMode.TECHNICAL) {
            display = cd.getType() + " " + display;
        }
        return display;

    }
}

