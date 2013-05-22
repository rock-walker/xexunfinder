package mln.Forest;

import android.app.Activity;

public interface IMessageProcess {
	
	public void InitSmsCenter(Activity map);
	
	public Message SentInit(Activity dogActivity);
	public void SentSetting(Activity activity);
	public Message ReceiveSMS(String sms);
}
