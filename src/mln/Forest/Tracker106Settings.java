package mln.Forest;

import android.content.SharedPreferences;

public class Tracker106Settings extends TrackerSettings
								implements ISettings {

	protected TrackerSettings baseTs;
	
	public Tracker106Settings() {
		super();
	}
	
	public Tracker106Settings(TrackerSettings settings) {
		baseTs = settings;
	}

	public void setPhoneNumber(String _phoneNumber) {
		if (baseTs != null)
			baseTs.setPhoneNumber(_phoneNumber);
	}

	public String getPhoneNumber() {
		return baseTs.getPhoneNumber();
	}
	
	public void setFrequencyTimes(int frequencyTimes) {
		if (baseTs == null)
			return;
		
		baseTs.setFrequencyTimes(frequencyTimes);
	}

	public int getFrequencyTimes() {
		return baseTs.getFrequencyTimes();
	}
	
	public void setFrequencyMins(int frequencyMins) {
		if (baseTs == null)
			return;
		
		baseTs.setFrequencyMins(frequencyMins);
	}

	public String getFrequencyMins() {
		return baseTs.getFrequencyMins();
	}
	
	public void setPassword(int password) {
		if (baseTs == null)
			return;
		
		baseTs.setPassword(password);
	}

	public int getPassword() {
		return baseTs.getPassword();
	}
	
	public void setFrequencyVal(String frequencyVal) {
		if (baseTs == null)
			return;
		
		baseTs.setFrequencyVal(frequencyVal);
	}

	public String getFrequencyVal() {
		return baseTs.getFrequencyVal();
	}
	
	public void setTrackerModel(TrackerModels model){
		baseTs.setTrackerModel(model);
	}
	
	public TrackerModels getTrackerModel(){
		return baseTs.getTrackerModel();
	}
	
	@Override
	public boolean save() {
		if (baseTs == null) 
			return false;
		
		return baseTs.save();
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
	}
	
	public boolean obtain(SharedPreferences sp) {
		if (baseTs == null) 
			return false;
		
		return baseTs.obtain(sp);
	}
}