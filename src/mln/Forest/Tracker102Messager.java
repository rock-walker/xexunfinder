package mln.Forest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class Tracker102Messager implements IMessageProcess {

	public final static String LOG_TAG = "T102Messager";
	
	public final static String SENT = "SMS_SENT";
    public final static String DELIVERED = "SMS_DELIVERED";
	
	private Tracker102Settings settings;
	private Message message;
	private GoogleMap gMap;
	private HunterDogActivity hdActivity;
	private Activity resourceActivity;
	private CountDownTimer timer;
	
	
	private SmsManager smsMngr = SmsManager.getDefault();
	private PendingIntent sentPI;
	private PendingIntent deliveredPI;
	
	private int repeatTimerCount = 0;
	
	@Override
	public void InitSmsCenter(final Activity map) {
		//---when the SMS has been sent---
  	    map.registerReceiver(new BroadcastReceiver() {
       	@Override
       	public void onReceive(Context arg0, Intent arg1) {
               switch (getResultCode())
               {
                   case Activity.RESULT_OK:
                	   message.isSuccessSent = true;
                       break;
                   case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                	   message.isSuccessSent = false;
                	   message.setSentError("This phone number does not exist");
                       break;
                   case SmsManager.RESULT_ERROR_NO_SERVICE:
                	   message.isSuccessSent = false;
                	   message.setSentError("No service");
                       break;
                   case SmsManager.RESULT_ERROR_NULL_PDU:
                	   message.isSuccessSent = false;
                	   message.setSentError("Null PDU");
                       break;
                   case SmsManager.RESULT_ERROR_RADIO_OFF:
                	   message.isSuccessSent = false;
                	   message.setSentError("Radio off");
                       break;
               }
               if (!message.isSuccessSent){
            	   Toast.makeText(arg0, message.getSentError(), 
            			   Toast.LENGTH_SHORT).show();
            	   Log.i(LOG_TAG, String.format("Message %s sending failed", message.messageText));
            	   //((GoogleMap)map).callBackSmsDeliever();
            	   ((HunterDogActivity)map).callBackSmsDeliever();
            	   
               }
               else// TODO only for test purposes - delete after release
               {
            	   if (HunterDogActivity.isDebug)
            	   {
	            	   Toast.makeText(arg0, "OK", 
	            			   Toast.LENGTH_SHORT).show();
	            	   message.isSuccessSent = true;
	            	   message.isSuccessDelievered = true;
	            	   ((HunterDogActivity)map).callBackSmsDeliever();
            	   }
            	   else
            	   {
            		   ((HunterDogActivity)map).callBackSmsSend(); 
            		   Log.i(LOG_TAG, String.format("Message %s is succesfully sent", message.messageText));
            	   }
               }
           }
       }, new IntentFilter(SENT));
  	    
  	    //---when the SMS has been delivered---
  	  map.registerReceiver(new BroadcastReceiver(){
           @Override
           public void onReceive(Context arg0, Intent arg1) {
               switch (getResultCode())
               {
                   case Activity.RESULT_OK:
                   	message.isSuccessDelievered = true;
                       break;
                   case Activity.RESULT_CANCELED:
                   	message.isSuccessDelievered = false;
                   	message.setSentError("SMS not delivered");
                       break;                        
               }
               if (message.isSuccessDelievered)
               {
            	   /*Toast.makeText(arg0, message.messageText, 
            			   Toast.LENGTH_SHORT).show();*/
            	   Log.i(LOG_TAG, String.format("Message %s is succesfully deliviered to T102", message.messageText));
               }
               else
               {
            	   Toast.makeText(arg0, message.getSentError(),
            			   Toast.LENGTH_SHORT).show();
            	   Log.i(LOG_TAG, String.format("Message %s is not deliviered to T102", message.messageText));
               };

               //((GoogleMap)map).callBackSmsDeliever();
               ((HunterDogActivity)map).callBackSmsDeliever();
           }
       }, new IntentFilter(DELIVERED));
		
	}
	
	@Override
	public Message SentInit(Activity startActivity) {
		settings = (Tracker102Settings)HunterDogActivity.currSettings;
		resourceActivity = startActivity;
		hdActivity = (HunterDogActivity)startActivity;
		
		//gMap = startActivity;
		message = new Message();
		message.gadgetPhone += settings.getPhoneNumber();
		message.messageText = startActivity.getResources().getString(R.string.mBegin) + String.valueOf(settings.getPassword());
		message.isSettingDelievered = false;
		
		sentPI = PendingIntent.getBroadcast(startActivity, 0,
	   	         new Intent(SENT), 0);
	   	 
   	    deliveredPI = PendingIntent.getBroadcast(startActivity, 0,
	   	         new Intent(DELIVERED), 0);
   	    
        // this is the function that does all the magic
   	    smsMngr.sendTextMessage(message.gadgetPhone, null, message.messageText, sentPI, deliveredPI);
   	    
   	    Log.i(LOG_TAG, String.format("'SentInit' %s is realized to T102", message.messageText));
   	    
		return message;
	}

	private void GetInit(String result)
	{
		if (result.equalsIgnoreCase(resourceActivity.getResources().getString(R.string.mOk)))
		{
			Toast.makeText(hdActivity,
					resourceActivity.getResources().getString(R.string.GpsInited), Toast.LENGTH_SHORT).show();
			
			Log.i(LOG_TAG, String.format("Initialization %s of T102 is finished", message.messageText));
			
			HunterDogActivity.beginPassed();
			
			/*if (settings.isAutoTrackChanged)
			{
				SentSetting();
				settings.isAutoTrackChanged = false;
			}
			SentSetting();*/
			//gMap.showTimer(settings.getFrequencyMins(), settings.getFrequencyVal());
		}
		else if (result.equalsIgnoreCase(resourceActivity.getResources().getString(R.string.mFail)))
		{
			Toast.makeText(hdActivity, 
					resourceActivity.getResources().getString(R.string.GpsConnectionFailed), Toast.LENGTH_SHORT).show();
			
			Log.i(LOG_TAG, String.format("Initialization %s of T102 is failed", message.messageText));
		}
	}
	
	public void SentSetting(Activity map) {
		//setting up period
		if (map != null)
			gMap = (GoogleMap)map;
		String period = String.valueOf(settings.getFrequencyTimes());
		if (period.equals("0"))
			period = "***";
		if (period.length() == 1)
			period = "00" + period;
		else if (period.length() == 2)
			period = "0" + period;
		period += "n";
		
		//settings up time
		String time = String.valueOf(settings.getFrequencyMins());
		if (time.length() == 1)
			time = "00" + time;
		else if (time.length() == 2)
			time = "0" + time;
		
		time += settings.getFrequencyVal();
		
		repeatTimerCount = settings.getFrequencyTimes();
		
		message.messageText = String.format(Tracker102Settings.ST_FORMAT, time + period);
		
		smsMngr.sendTextMessage(message.gadgetPhone, null, message.messageText, sentPI, deliveredPI);
		
		Log.i(LOG_TAG, String.format("Sent Setting %s to T102", message.messageText));
	}

	public void GetSetting(String result) {
		if (result.equalsIgnoreCase(gMap.getResources().getString(R.string.mOk)))
		{
			Toast.makeText(gMap, 
					gMap.getResources().getString(R.string.GpsSetted), Toast.LENGTH_SHORT).show();
			
			//repeatTimerCount = settings.getFrequencyTimes() - 1;
			message.isSettingDelievered = true;
			timer = gMap.showTimer(settings.getFrequencyMins(), settings.getFrequencyVal());
			
			Log.i(LOG_TAG, String.format("Setting %s of T102 is successfully finished", message.messageText));
		}
		else if (result.equalsIgnoreCase(gMap.getResources().getString(R.string.mFail)))
		{
			Toast.makeText(gMap, 
					gMap.getResources().getString(R.string.GpsStFailed), Toast.LENGTH_SHORT).show();
			
			Log.i(LOG_TAG, String.format("Setting %s of T102 is failed", message.messageText));
		}
	}

	private void ReceiveCoords(String sms) {
		sms = sms.replace(": ", ":");
		String[] smsVals = sms.split(" ");
		List<String> coords = new ArrayList<String>(); 
		String bat, signal = "";
		try{
			for (String val : smsVals)
			{
				if ((val.contains(gMap.getResources().getString(R.string.mGpsLat)) ||
						val.contains(gMap.getResources().getString(R.string.mGpsLong))) &&
						!val.endsWith(":"))
					coords.add(val.split(":")[1]);
				if (val.contains(gMap.getResources().getString(R.string.mGpsBat)))
				{
					bat = val.split(":")[1];
					if (!bat.equals("f"))
						Toast.makeText(gMap, 
								gMap.getResources().getString(R.string.GpsBatLow), Toast.LENGTH_SHORT).show();
				}
				if (val.contains(gMap.getResources().getString(R.string.mGpsSignal)))
				{
					signal = val.split(":")[1];
					if (!signal.equals("f"))
						Toast.makeText(gMap, 
								gMap.getResources().getString(R.string.GpsSignalLow), Toast.LENGTH_SHORT).show();
				}
			}
			if (coords.size() != 2)
			{
				coords = null;
				Toast.makeText(gMap, 
						gMap.getResources().getString(R.string.GpsCoordsFailed), Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(gMap, 
						gMap.getResources().getString(R.string.GpsCoordsRecvd), Toast.LENGTH_SHORT).show();
				GeneralMap gm = gMap.getGenMap();
				gm.ApplyTrackerCoords(coords);
				
				repeatTimerCount--;
				if (repeatTimerCount > 0){
					timer.cancel();
					timer = gMap.showTimer(settings.getFrequencyMins(), settings.getFrequencyVal());
				}

				message.isCoordRecieved = true;
			}
		}
		catch (Exception e)
		{
			Log.e(LOG_TAG, String.format("'ReceiveCoords' %s", e.getMessage()));
		}
	}

	@Override
	public Message ReceiveSMS(String sms) {
		
		Log.i(LOG_TAG, String.format("SMS %s is received from T102", sms));
		try{
			if (sms.contains(resourceActivity.getResources().getString(R.string.mBegin)))
				GetInit(sms.split(" ")[1]);
			else if (sms.startsWith(resourceActivity.getResources().getString(R.string.mGpsLat)))
			{
				ReceiveCoords(sms);
			}
			else if (sms.toLowerCase().startsWith("t"))
				GetSetting(sms.split(" ")[1]);
		}
		catch(Exception e)
		{
			Log.e(LOG_TAG, String.format("'ReceiveSMS': %s", e.getMessage()));
		};
		
		return null;
	}
}
