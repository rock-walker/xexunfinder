package mln.Forest;

import mln.Forest.TrackerSettings.TrackerModels;
import android.content.SharedPreferences;

public interface ISettings {
	public boolean obtain(SharedPreferences sp);
	public boolean save();
	public void cancel();
	
		
	public int getPassword();
	public void setPassword(int password);
	
	public String getFrequencyMins();
	public void setFrequencyMins(int frequencyMins);
	
	public int getFrequencyTimes();
	public void setFrequencyTimes(int frequencyTimes);
	
	public void setPhoneNumber(String _phoneNumber);
	public String getPhoneNumber();
	
	public void setFrequencyVal(String frequencyVal);
	public String getFrequencyVal();
	
	public TrackerModels getTrackerModel();
	public void setTrackerModel(TrackerModels model);
}
