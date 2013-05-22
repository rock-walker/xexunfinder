package mln.Forest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver{

	private static final String LOG_SMS = "SmsMobileReceiver";
	
	private String phoneNumber; 
	private IMessageProcess gpsDevice;
	
	private static SmsReceiver instance;
	
	public void InitReceiver(String pNumber, IMessageProcess device)
	{
		// TODO clear before release
		phoneNumber = (!HunterDogActivity.isDebug) ? pNumber : "1555521" + pNumber.substring(1);
		gpsDevice = device;
	}
	
	public static SmsReceiver getInstance()
	{
		if (instance == null)
		{
			synchronized (SmsReceiver.class) {
				if (instance == null)
					instance = new SmsReceiver();
			}
		}
		return instance;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String messageText = ""; 
        String originalAddr = "";
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]); 
                messageText = msgs[i].getMessageBody().toString();
                originalAddr = msgs[i].getOriginatingAddress();
            } 
            //---display the new SMS message---
            if (originalAddr.equalsIgnoreCase(instance.phoneNumber))
            {
            	instance.gpsDevice.ReceiveSMS(messageText.toLowerCase());
            	Log.i(LOG_SMS, String.format("SMS %s from T102 received", messageText));
            }
            
            else //only for tested purposed
            	/*Toast.makeText(context, "ignore this message "+ originalAddr + " gadget phone: " + instance.phoneNumber, Toast.LENGTH_SHORT).show();*/
            	Log.i(LOG_SMS, String.format("SMS %s from not authorized number", messageText));
        }
	}
}
