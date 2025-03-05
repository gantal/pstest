package hu.gantal.ps.task.client.utils;


public class NumberUtil {
	public static int getSafeInteger(Object obj) {
	    if (obj instanceof Number) {
	        return ((Number) obj).intValue();
	    } else if (obj instanceof String) {
	        try {
	            return Integer.parseInt((String) obj);
	        } catch (NumberFormatException e) {
	            System.out.println("HIBA: Nem sikerült konvertálni Integer típusra: " + obj);
	            return 0;
	        }
	    }
	    return 0;
	}

	public static double getSafeDouble(Object obj) {
	    if (obj instanceof Number) {
	        return ((Number) obj).doubleValue();
	    } else if (obj instanceof String) {
	        try {
	            return Double.parseDouble((String) obj);
	        } catch (NumberFormatException e) {
	            System.out.println("HIBA: Nem sikerült konvertálni Double típusra: " + obj);
	            return 0;
	        }
	    }
	    return 0;
	}
}
