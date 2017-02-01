package medview.meduweb.search;

import medview.meduweb.datahandler.Case;
import java.util.*;

public class SearchQuery {

    //Used for recursion on two expressions
    private SearchQuery s1;
    private SearchQuery s2;

    //Used for the actual search
    private String attribute;
    private String value;
    
    //Used to check what kind of operator to use
    private boolean isExpr;
    private boolean isAND;
    private boolean isNOT;
    private boolean isOR;

    //Internal temporary representation used when parsing
    private String mainString;
    private boolean parenthesis;

    public SearchQuery() {}

    public SearchQuery (String searchString) throws Exception {
	isExpr = false;
	isAND = false;
	isOR = false;
	isNOT = false;
	s1 = null;
	s2 = null;
	value = null;
	attribute = null;
	this.mainString = new String(searchString);
	parse();
    }

    private void parse() throws Exception {
	try {
	//remove surrounding whitespaces
	while (mainString.length() > 0 && mainString.charAt(0) == ' ') {
	    mainString = mainString.substring(1);
	}
	while (mainString.length() > 0
	       && mainString.charAt(mainString.length()-1) == ' ') {
	    mainString = mainString.substring(0,mainString.length()-1);
	}
	} catch (Exception e) {
	    if (! e.getMessage().equals("Thrown in getElement")) {
		throw new Exception("Occured during init");
	    }
	}

	try {
	while (mainString != null && mainString.length() > 0) {
	    parenthesis = false;
	    String tmp = getElement();
	    //logic check first
	    if (tmp.length() == 1) {
		if (tmp.equals("|")) {
		    isOR = true;
		    s2 = new SearchQuery(mainString);
		    mainString = "";
		} else if (tmp.equals("&")) {
		    isAND = true;
		    s2 = new SearchQuery(mainString);
		    mainString = "";
		} else if (tmp.equals("!")) {
		    String tmp2 = getElement();
		    if (mainString.length() == 0) {
			//only this element, add to sh1 and set flag
			s1 = new SearchQuery(tmp2);
			isNOT = true;
		    } else {
			//more stuff after, apply only to first
			s1 = new SearchQuery("! " + tmp2);
		    }
		}
	    } else if (mainString.length() == 0 && ! parenthesis) {
		//bottom level, insert into attribute and value
		StringTokenizer st = new StringTokenizer(tmp);
		attribute = st.nextToken("=");
		value = st.nextToken("=");
		isExpr = true;
	    } else {
		//no logic, insert into sh1
		s1 = new SearchQuery(tmp);
	    }
	}
	} catch (Exception e) {
	    if (! e.getMessage().equals("Thrown in getElement")) {
		throw new Exception("Occured in parse");
	    }
	}
	    
    }

    private String getElement() throws Exception{
	try {
	String out = "";
	if (mainString != null && mainString.length() > 0) {
	    //remove leading spaces
	    while (mainString.length() > 0 && mainString.charAt(0) == ' ') {
		mainString = mainString.substring(1);
	    }
	    //remove leadout spaces
	    while (mainString.length() > 0
		   && mainString.charAt(mainString.length()-1) == ' ') {
		mainString = mainString.substring(0,mainString.length()-1);
	    }

	    if (mainString.length() > 0) {
		if (mainString.charAt(0) == '(') {
		    //Parenthesis
		    //Search for end of parenthesis
		    int noOfLeftParenthesis = 1;
		    int index = 1;
		    while (noOfLeftParenthesis != 0
			   && index < mainString.length()) {
			if (mainString.charAt(index) == '(') {
			    noOfLeftParenthesis++;
			} else if (mainString.charAt(index) == ')') {
			    noOfLeftParenthesis--;
			}
			index++;
		    }
		    //No matching parenthesis found, remove this one
		    if (noOfLeftParenthesis != 0) {
			mainString = mainString.substring(1);
			out = getElement();
		    } else {
			out = mainString.substring(1,index-1);
			//characters left
			if (index < mainString.length()) {
			    mainString = mainString.substring(index);
			    parenthesis = true;
			} else {
			    mainString = "";
			}
			
		    }

		    //Check for OR/ELLER
		} else if (mainString.length() > 1
			   && mainString.substring(0,2).equals("OR")) {
		    out = "|";
		    mainString = mainString.substring(2);
		} else if (mainString.length() > 4
			   && mainString.substring(0,5).equals("ELLER")) {
		    out = "|";
		    mainString = mainString.substring(5);

		    //Check for OCH/AND
		} else if ((mainString.length() > 2)
			   && (mainString.substring(0,3).equals("OCH")
			       || mainString.substring(0,3).equals("AND"))) {
		    out = "&";
		    mainString = mainString.substring(3);

		    //Check for NOT/INTE
		} else if (mainString.length() > 2
			   && mainString.substring(0,3).equals("NOT")) {
		    out = "!";
		    mainString = mainString.substring(3);
		} else if (mainString.length() > 3
			    && mainString.substring(0,4).equals("INTE")) {
		    out = "!";
		    mainString = mainString.substring(4);

		    //Single character, an operator
		} else if (mainString.charAt(1) == ' ') {
		    out = new String(mainString.substring(0,1));
		    mainString = mainString.substring(1);		    

		    //An expression
		} else {
		    //loop until special character or end of line
		    int index = 0;
		    boolean notFinished = true;
		    while (notFinished 
			   && mainString != null 
			   && index < mainString.length()) {
			char tmpChar = mainString.charAt(index);
			if (tmpChar== '&'
			    || tmpChar == '|'
			    || tmpChar == '!'
			    || (index + 1 < mainString.length()
				&& mainString.substring(index,index+2).equals("OR"))
			    || (index + 2 < mainString.length()
				&& (mainString.substring(index,index+3).equals("OCH")
				    || mainString.substring(index,index+3).equals("AND")
				    || mainString.substring(index,index+3).equals("NOT")))
			    || (index + 3 < mainString.length()
				&& mainString.substring(index,index+4).equals("INTE"))
			    || (index + 4 < mainString.length()
				&& mainString.substring(index,index+5).equals("ELLER"))) {
			    notFinished = false;
			} else {
			    index++;
			}
		    }
		    out = mainString.substring(0,index);
		    mainString = mainString.substring(index);
		}
	    }
	}
	return out;
	} catch (Exception e) {
	    throw new Exception("Thrown in getElement");
	}
    }

    

    public ArrayList eval() {
	Case cas = new Case();
	if (isNOT && s1!=null) {
	    ArrayList allCodes = cas.getPcodes();
	    return disjunction(allCodes, s1.eval());
	} else if (isAND && s1!=null && s2!=null) {
	    return intersection(s1.eval(), s2.eval());
	} else if (isOR && s1!=null && s2!=null) {
	    return join(s1.eval(), s2.eval());
	} else if (isExpr) {
	    return cas.searchAttribute(attribute, value);
	} else if (s1 != null) {
	    return s1.eval();
	} else {
	    return new ArrayList();
	}
    }
    
    private ArrayList disjunction(ArrayList list1, ArrayList list2) {
	ArrayList out = new ArrayList();
	if (list1 != null) {
	    if (list2 != null) {
		out = new ArrayList(list1);
		Iterator list2I = list2.iterator();
		while (list2I.hasNext()) {
		    String tmp = (String)list2I.next();
		    if (out.contains(tmp)) {
			Object rem = out.remove(out.indexOf(tmp));
		    }
		}
	    } else {
		return list1;
	    }
	}
	return out;
    }

    private ArrayList intersection(ArrayList list1, ArrayList list2) {
	ArrayList out = new ArrayList();
	if (list1 != null && list2 != null) {
	    Iterator list1I = list1.iterator();
	    while (list1I.hasNext()) {
		String tmp1 = (String)list1I.next();
		Iterator list2I = list2.iterator();
		while (list2I.hasNext()) {
		    String tmp2 = (String)list2I.next();
		    if (tmp1.equals(tmp2)) {
			out.add(tmp2);
		    }
		}
	    }
	}
	return out;
    }

    private ArrayList join(ArrayList list1, ArrayList list2) {
	ArrayList out = new ArrayList();
	if (list1 != null && list2 != null) {
	    out = new ArrayList(list1);
	    Iterator list2I = list2.iterator();
	    while (list2I.hasNext()) {
		String tmp = (String)list2I.next();
		if (! out.contains(tmp)) {
		    out.add(tmp);
		}
	    }
	}
	return out;
    }

}
