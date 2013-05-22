package mln.Forest;

import android.content.SharedPreferences;

public class Tracker102Settings implements ISettings {

	private static final int PSWD = 123456;
	
	public static final String ST_FORMAT = "t%s" + PSWD;
	
	private static final String PHONE = "Phone";
	private static final String FREQTIMES = "Times";
	private static final String FREQMINS = "Mins";
	private static final String FREQVAL = "TimeVal";
	private String phoneNumber;
	private int password;
	private int frequencyTimes = 5;
	private int frequencyMins = 1;
	private String frequencyVal = "m";
	
	public boolean isAutoTrackChanged = false;
	
	private SharedPreferences sPrefs;
	
// START: Singleton of Settings
	private static Tracker102Settings instance;
	
	private Tracker102Settings(){
		this.setPassword(PSWD);
	}
	
	public static Tracker102Settings getInstance(){
		if (instance == null)
		{
			synchronized (Tracker102Settings.class) {
				if (instance == null)
					instance = new Tracker102Settings();
			}
		}
		return instance;
	}
// END: Singleton of Settings
	
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
	
	public void setFrequencyVal(String frequencyVal) {
		if (this.frequencyVal != frequencyVal)
			this.isAutoTrackChanged = true;
		
		this.frequencyVal = frequencyVal;
	}

	public String getFrequencyVal() {
		return frequencyVal;
	}

	@Override
	public boolean Save() {
		if (sPrefs == null) 
			return false;
		else
		{
			SharedPreferences.Editor spEditor = sPrefs.edit();
			spEditor.putString(PHONE, this.phoneNumber);
			spEditor.putInt(FREQTIMES, this.frequencyTimes);
			spEditor.putInt(FREQMINS, this.frequencyMins);
			spEditor.putString(FREQVAL, this.frequencyVal);
			spEditor.commit();
			
			return true;
		}
	}

	@Override
	public void Cancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean Obtain(SharedPreferences sp) {
		try
		{
			this.sPrefs = sp;
			
			this.phoneNumber = sp.getString(PHONE, "");
			this.frequencyTimes = sp.getInt(FREQTIMES, 5);
			this.frequencyMins = sp.getInt(FREQMINS, 45);
			this.frequencyVal = sp.getString(FREQVAL, "s");
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}