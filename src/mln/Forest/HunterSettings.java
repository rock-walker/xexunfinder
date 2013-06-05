package mln.Forest;

import mln.Forest.TrackerSettings.TrackerModels;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class HunterSettings extends Activity {
	
	//def variables
	private String _phoneNumber;
	private int _freqTime;
	private int _freqMin;
	private String _freqVal;
	private TrackerModels _trackerModel;
	
	//def controls
	private EditText edPhone;
	private EditText edFreqTime;
	private EditText edFreqMin;
	private Spinner  spinTimes;
	private Spinner  spinModels;
	
	private TrackerSettings trackerSettings;
	
	private Button btnSave;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        //init the edit fields
        edPhone = (EditText)findViewById(R.id.editPhone);
        edFreqTime = (EditText)findViewById(R.id.editTimes);
        edFreqTime.setFilters(new InputFilter[] { new ValidatorMinMax(0, 255)});
        
        edFreqMin = (EditText)findViewById(R.id.editMin);
        edFreqMin.setFilters(new InputFilter[] { new ValidatorMinMax(0, 255)});
        spinTimes = (Spinner)findViewById(R.id.spinTimes);
        spinModels = (Spinner)findViewById(R.id.spinModels);
        btnSave = (Button)findViewById(R.id.btnSave);
        
        
        trackerSettings = (TrackerSettings)HunterDogActivity.currSettings;
        
        edPhone.setText(trackerSettings.getPhoneNumber());
        edFreqTime.setText(String.valueOf(trackerSettings.getFrequencyTimes()));
        edFreqMin.setText(String.valueOf(trackerSettings.getFrequencyMins()));
        
        class TimesSelectedListener implements OnItemSelectedListener{

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				String time = "s";
				switch (position)
				{
					case 1:
						time = "m";
						break;
					case 2:
						time = "h";
						break;
				};
				_freqVal = time;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        };
        
        //adjust time freq
        String time = trackerSettings.getFrequencyVal();
        int position = 0;
        if (time.equals("m"))
        	position = 1;
        else if (time.equals("h"))
        	position = 2;
        
        spinTimes.setSelection(position);
        spinTimes.setOnItemSelectedListener(new TimesSelectedListener());
        
        //adjust model
        TrackerModels model = trackerSettings.getTrackerModel();
        class ModelsSelectedListener implements OnItemSelectedListener{

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				TrackerModels model = TrackerModels.XEXUN102;
				switch (position)
				{
					case 1:
						model = TrackerModels.XEXUN106;
						break;
				};
				_trackerModel = model;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        };
        spinModels.setSelection(TrackerModels.toInteger(model));
        spinModels.setOnItemSelectedListener(new ModelsSelectedListener());
        
        btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OnSaveButtonClicked();
				finish();
			}
		});
    }
	
	private void OnSaveButtonClicked(){
		_phoneNumber = edPhone.getText().toString();
		_freqTime = Integer.parseInt(edFreqTime.getText().toString());
		_freqMin = Integer.parseInt(edFreqMin.getText().toString());
		
		
		trackerSettings.isAutoTrackChanged = false;
		trackerSettings.setPhoneNumber(_phoneNumber);
		trackerSettings.setFrequencyTimes(_freqTime);
		trackerSettings.setFrequencyMins(_freqMin);
		trackerSettings.setFrequencyVal(_freqVal);
		trackerSettings.setTrackerModel(_trackerModel);
		
		if (!HunterDogActivity.currSettings.save())
			;//show error message;
		else
			HunterDogActivity.applySettings(_trackerModel, (TrackerSettings)HunterDogActivity.currSettings);
	}
	
	@Override
    public void onDestroy() {
        // Remove AccountManager callback
        //AccountManager.get(this).removeOnAccountsUpdatedListener(this);
        super.onDestroy();
    }
}
