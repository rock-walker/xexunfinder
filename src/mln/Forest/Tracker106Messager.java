package mln.Forest;

public class Tracker106Messager extends TrackerMessager implements
		IMessageProcess {
	
	public Tracker106Messager(){
		super();
	}
	
	public String getSettingFormat(){
		return PSWD + "t%s";
	}
	
	public String getBeginFormat(String resource){
		return PSWD + resource;
	}
	
	public String[] splitCoordsMsg(String sms){
		
		String[] res = sms.split(" ");
		
		if (res.length > 2 && res[0].indexOf(R.string.mGpsLat) > -1
				&& res[1].indexOf(R.string.mGpsLong) > -1)
			;
		else
			res = sms.split(",");
		
		return res;
	}
}
