package medview.aggregator;

import javax.swing.tree.*;
import java.util.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class  SortedValues {

	static final int PCodeFirst     =  0;
	static final int PCodeMiddle    =  1;
	static final int PCodeLast      =  2;

	static void reSortPcode(DefaultMutableTreeNode aNode,int aFlag ){

		int len = aNode.getChildCount() ;
		if ( len <= 1 ) return;

		for (int i = 0; i < len -1 ; i++){
		    DefaultMutableTreeNode minChild = (DefaultMutableTreeNode)aNode.getChildAt(i);

			for(int j = i + 1; j < len; j++){
				DefaultMutableTreeNode tmpChild = (DefaultMutableTreeNode)aNode.getChildAt(j);
				if(lessThan(tmpChild,minChild,aFlag)){
					aNode.insert(tmpChild,i);
					aNode.insert(minChild,j);
					minChild = tmpChild;
				}
			}
		}
	}
	private static boolean lessThan(DefaultMutableTreeNode fstChild,
									DefaultMutableTreeNode secChild,int pCode){
		String fstElem = fstChild.toString();
		String secElem = secChild.toString();
		try{
			switch (pCode) {
				case PCodeFirst:
					break;
				case PCodeLast:
					fstElem = fstElem.substring(8,9) + fstElem.substring(0,8);
					secElem = secElem.substring(8,9) + secElem.substring(0,8);
					break;
				case PCodeMiddle:
					fstElem = fstElem.substring(6,8) + fstElem.substring(0,6);  // crash
					secElem = secElem.substring(6,8) + secElem.substring(0,6);
					break;
			}
			if(fstElem.compareTo(secElem) < 0)
				return true;
			else return false;
		}
		catch(Exception e){
			Ut.prt(e.getMessage() );
		    return false;
		}
	}

























}