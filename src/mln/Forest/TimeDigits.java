package mln.Forest;

public class TimeDigits {
	private int fHour;
	private int sHour;
	private int fMin;
	private int sMin;
	private int fSec;
	private int sSec;
	
	public TimeDigits(int m, int n, int i, int j, int k, int l) {
		fHour = m;
		sHour = n;
		fMin = i;
		sMin = j;
		fSec = k;
		sSec = l;
	}

	public static final TimeDigits getTimeDigits(int seconds) {      
	    final int SECOND_IN_MINUTE = 60;
	    final int SECOND_IN_HOUR = 3600;
	    if (seconds == 0) {
	        return new TimeDigits(0,0,0,0,0,0);
	    }
	    final int hours = seconds / SECOND_IN_HOUR;
	    final int secInHour = hours  * SECOND_IN_HOUR;
	    seconds = seconds - secInHour;
	    
	    final int minutes = seconds / SECOND_IN_MINUTE; //seconds / SECOND_IN_MINUTE;
	    final int secInMin = minutes * SECOND_IN_MINUTE;
	    final int leftOverSeconds = seconds - secInMin;
	   
	    int fH = 0;
	    int sH = 0;
	    int fM = 0;
	    int sM = 0;
	    int fS = 0;
	    int sS = 0;
	    if (hours >= 10) {
	    	String hString = (new Integer(hours).toString());
	    	fH = new Integer(Character.toString(hString.charAt(0))).intValue();
	    	sH = new Integer(Character.toString(hString.charAt(0))).intValue();
	    }
	    else {
	    	sH = hours;
	    }
	    	
	    if (minutes >= 10) {
	        String minString = (new Integer(minutes).toString());
	        fM = new Integer(Character.toString(minString.charAt(0))).intValue();
	        sM = new Integer(Character.toString(minString.charAt(1))).intValue();
	    } else {
	        sM = minutes;
	    }
	   
	    if (leftOverSeconds >= 10) {
	        String secString = (new Integer(leftOverSeconds)).toString();
	        fS = new Integer(Character.toString(secString.charAt(0))).intValue();
	        sS = new Integer(Character.toString(secString.charAt(1))).intValue();
	    } else {
	        sS = leftOverSeconds;
	    }
	    return new TimeDigits(fH, sH, fM,sM,fS,sS);
	}

	public String getMinutes() {
		return String.valueOf(fMin) + String.valueOf(sMin);
	}

	public String getSeconds() {
		return String.valueOf(fSec) + String.valueOf(sSec);
	}
	
	public String getHours() {
		return String.valueOf(fHour) + String.valueOf(sHour);
	}
	
	public String getFullTime(String separator) {
		return getHours() + separator + getMinutes() + separator + getSeconds();
	}
}
