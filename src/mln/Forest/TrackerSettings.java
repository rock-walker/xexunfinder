package mln.Forest;

import android.content.SharedPreferences;

public class TrackerSettings implements ISettings {
	
	public static final int PSWD = 123456;
	
	private static final String LOG_TAG = "General Tracker Settings";
	
	private static final String PHONE = "Phone";
	private static final String FREQTIMES = "Times";
	private static final String FREQMINS = "Mins";
	private static final String FREQVAL = "TimeVal";
	private static final String TRACKER_MODEL = "TrackerModel";
	
	private String phoneNumber;
	private int password;
	private int frequencyTimes = 5;
	private int frequencyMins = 1;
	private String frequencyVal = "m";
	private TrackerModels trackerModel = TrackerModels.XEXUN102;
	
	public boolean isAutoTrackChanged = false;
	
	public static enum TrackerModels {
		XEXUN102,
		XEXUN106;
		
		 public static TrackerModels fromInteger(int x) {
	        switch(x) {
	        case 0:
	            return XEXUN102;
	        case 1:
	            return XEXUN106;
	        }
	        return XEXUN102;
	    }
		 
		 public static int toInteger(TrackerModels x) {
		        switch(x) {
		        case XEXUN102:
		            return 0;
		        case XEXUN106:
		            return 1;
		        }
		        return 0;
		    }
	}
	
	private SharedPreferences sPrefs;
	
	public void setPhoneNumber(String _phoneNumber) {
		phoneNumber = _phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setFrequencyTimes(int frequencyTimes) {
		if (this.frequencyTimes != frequencyTimes)
			this.isAutoTrackChanged = true;
		
		this.frequencyTimes = frequencyTimes;
	}

	public int getFrequencyTimes() {
		return frequencyTimes;
	}
	
	public void setFrequencyMins(int frequencyMins) {
		if (this.frequencyMins != frequencyMins)
			this.isAutoTrackChanged = true;
		
		this.frequencyMins = frequencyMins;
	}

	public String getFrequencyMins() {
		//to output float values
		return String.valueOf(frequencyMins);
	}
	
	public void setPassword(int password) {
		this.password = password;
	}

	public int getPassword() {
		return password;
	}
	
	// START: Singleton of Settings
	private static TrackerSettings instance;
	
	protected TrackerSettings(){
		this.setPassword(PSWD);
	}
	
	public static TrackerSettings getInstance(){
		if (instance == null)
		{
			synchronized (TrackerSettings.class) {
				if (instance == null)
					instance = new TrackerSettings();
			}
		}
		return instance;
	}
	// END: Singleton of Settings
	
	public void setFrequencyVal(String frequencyVal) {
		if (this.frequencyVal != frequencyVal)
			this.isAutoTrackChanged = true;
		
		this.frequencyVal = frequencyVal;
	}

	public String getFrequencyVal() {
		return frequencyVal;
	}
	
	public void setTrackerModel(TrackerModels model){
		this.trackerModel = model;
	}
	
	public TrackerModels getTrackerModel(){
		return this.trackerModel;
	}
	
	@Override
	public boolean save() {
		if (sPrefs == null) 
			return false;
		else
		{
			SharedPreferences.Editor spEditor = sPrefs.edit();
			spEditor.putString(PHONE, this.phoneNumber);
			spEditor.putInt(FREQTIMES, this.frequencyTimes);
			spEditor.putInt(FREQMINS, this.frequencyMins);
			spEditor.putString(FREQVAL, this.frequencyVal);
			spEditor.putInt(TRACKER_MODEL, TrackerModels.toInteger(this.trackerModel));
			spEditor.commit();
			
			return true;
		}
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean obtain(SharedPreferences sp) {
		try
		{
			this.sPrefs = sp;
			
			this.phoneNumber = sp.getString(PHONE, "");
			this.frequencyTimes = sp.getInt(FREQTIMES, 5);
			this.frequencyMins = sp.getInt(FREQMINS, 45);
			this.frequencyVal = sp.getString(FREQVAL, "s");
			
			this.trackerModel = TrackerModels.fromInteger(sp.getInt(TRACKER_MODEL, 0));
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

}
