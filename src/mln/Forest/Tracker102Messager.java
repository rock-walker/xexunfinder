package mln.Forest;

public class Tracker102Messager extends TrackerMessager implements
		IMessageProcess {
	
	public Tracker102Messager(){
		super();
	}
	
	public String getSettingFormat(){
		return "t%s" + PSWD;
	}
	
	public String getBeginFormat(String resource){
		return resource +  PSWD;
	}
}
