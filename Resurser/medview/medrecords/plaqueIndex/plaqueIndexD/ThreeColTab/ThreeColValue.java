/*
 * ThreeColValue.java
 *
 * Created on den 28 november 2002, 17:55
 *
 * $Id: ThreeColValue.java,v 1.4 2004/10/01 16:39:51 lindahlf Exp $
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab;
    import medview.medrecords.data.*;

/**
 * @author  nader
 *
 * @modified by Fredrik
 */
public class ThreeColValue
{
	// MEMBERS

    protected   Object[]  mValues;

	// CONSTRUCTORS

    public ThreeColValue()
    {
        mValues         = new Object[3] ;

        for(int i = 0; i < mValues.length ; i++)
        {
            mValues[i] =  new Integer(-1);
        }
    }

     public ThreeColValue(Object pDefVal)
     {
        mValues         = new Object[3] ;

        for(int i = 0; i < mValues.length ; i++)
        {
            mValues[i] = pDefVal ;
        }
    }

	// METHODS

    public Object getCell(int aCol)
    {
        return mValues[aCol] ;
    }

    public void setCell(int aCol, Object aValue)
    {
        mValues [aCol] = aValue;
    }
}
