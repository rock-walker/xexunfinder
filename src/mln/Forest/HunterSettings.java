package mln.Forest;

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
	
	//def controls
	private EditText edPhone;
	private EditText edFreqTime;
	private EditText edFreqMin;
	private Spinner  spinTimes;
	
	private Tracker102Settings t102Settings;
	
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
        btnSave = (Button)findViewById(R.id.btnSave);
        
        t102Settings = (Tracker102Settings)HunterDogActivity.currSettings;
        
        edPhone.setText(t102Settings.getPhoneNumber());
        edFreqTime.setText(String.valueOf(t102Settings.getFrequencyTimes()));
        edFreqMin.setText(String.valueOf(t102Settings.getFrequencyMins()));
        
        //START: init spinner
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
        		R.array.time_period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTimes.setAdapter(adapter);*/
        
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
        String time = t102Settings.getFrequencyVal();
        int position = 0;
        if (time.equals("m"))
        	position = 1;
        else if (time.equals("h"))
        	position = 2;
        
        spinTimes.setSelection(position);
        spinTimes.setOnItemSelectedListener(new TimesSelectedListener());
        
        //END: init spinner
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
		
		t102Settings.isAutoTrackChanged = false;
		t102Settings.setPhoneNumber(_phoneNumber);
		t102Settings.setFrequencyTimes(_freqTime);
		t102Settings.setFrequencyMins(_freqMin);
		t102Settings.setFrequencyVal(_freqVal);
		
		if (!HunterDogActivity.currSettings.Save())
			;//show error message;
	}
	
	@Override
    public void onDestroy() {
        // Remove AccountManager callback
        //AccountManager.get(this).removeOnAccountsUpdatedListener(this);
        super.onDestroy();
    }
}
