package ClearTrip.Utilities;

import java.util.ArrayList;
import java.util.List;

public class Conversion {
	public static String convertIntToCurrencyString(int i) {
		List<String> s = new ArrayList<>();
		String res="";
		String amt = String.valueOf(i);
	    int length = amt.length();
	    if(length>3) {
	    	String last3 = amt.substring(length-3);
	    	String digits = amt.substring(0,length-3);
	    	s.add(last3);
	    	for(int j=digits.length();j>0;j--) {
	    		if(j==1) {
	    			s.add(digits);
	    		}else {
	    			String d = digits.substring(j-2);
		    		s.add(d);
		    		digits = digits.substring(0,j-2);
		    		j--;
	    		}
	    	}
	    	
		    for(int j=s.size()-1;j>=0;j--) {
		    	res=res+s.get(j)+",";
		    }
		    res="₹"+res.substring(0,res.length()-1);
	    }
	    else {
	    	res = "₹"+amt;
	    }
	    return res;
	}

	public static int convertCurrencyStringToInt(String cur) {
		String strMain = cur;
		strMain=strMain.substring(1);
	    String[] arrSplit = strMain.split(",");
	    
	    String amt="";
	    for (int i=0; i < arrSplit.length; i++)
	    {
	    	amt=amt+arrSplit[i];
	    }
	    int i=Integer.parseInt(amt);
	    return i;
	}
}
