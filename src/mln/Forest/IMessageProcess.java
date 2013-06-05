package mln.Forest;

import android.app.Activity;

public interface IMessageProcess {
	
	public final static int PSWD = 123456;
	
	public void InitSmsCenter(Activity map);
	
	public Message SentInit(Activity dogActivity);
	public String getBeginFormat(String resource);
	
	public void SentSetting(Activity activity);
	public String getSettingFormat();
	
	public Message ReceiveSMS(String sms);
	public String[] splitCoordsMsg(String sms);
}
