package mln.Forest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HunterDogActivity extends Activity {
	
	private final static String APP_PREFERENCES = "HunterDogPreferences";
	private static final int TIMER_BEGIN_SMS = 20;
	private static final String LOG_GM = "Log_GoogleMap";
	
	public static ISettings currSettings;
	public static IMessageProcess iMessager;
	public static Message returnMsg;
	
	public static boolean isDebug;
	
	private TextView mTxtError;
	private static Button mStartUp;
	private Button mSettings;
	private static Button mConnect;
	private ImageButton mReload;
	private static ProgressBar mProgressConnecting;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        
        initSettings();
        
        mStartUp = (Button)findViewById(R.id.btnStartup);
        mSettings = (Button)findViewById(R.id.btnSettings);
        mConnect = (Button)findViewById(R.id.btnConnect);
        mReload = (ImageButton)findViewById(R.id.btnReload);
        mTxtError = (TextView)findViewById(R.id.txtError);
        
        mProgressConnecting = (ProgressBar)findViewById(R.id.progressConnecting);
        
        mStartUp.setEnabled(false);
        mTxtError.setVisibility(View.GONE);
        mProgressConnecting.setVisibility(View.GONE);
        
    	try {
			isDebug = isDebugBuild();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isDebug = false;
		}
        
        mConnect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iMessager = new Tracker102Messager();
				
				if (((Tracker102Settings)currSettings).getPhoneNumber().length() > 0)
		        	initTracker();
		        else
		        	mTxtError.setVisibility(View.VISIBLE);	
		        
		        mConnect.setEnabled(false);
			}
		});
        
        mStartUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchMap();
			}
        });
        
        mSettings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchSettings();					
			}
		});
        
        mReload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mStartUp.setEnabled(false);
				mConnect.setEnabled(true);
				returnMsg = null;
				mProgressConnecting.setVisibility(View.GONE);
			}
		});
    }
    
    @Override
	protected void onResume()
	{
    	super.onResume();
    	
    	if (currSettings != null &&
    			((Tracker102Settings)currSettings).getPhoneNumber().length() > 0)
    	{
    		mTxtError.setVisibility(View.GONE);
    		mConnect.setEnabled(true);
    	}
	}
    
    private void launchMap()
    {
    	Intent i = new Intent(this, GoogleMap.class);
    	startActivity(i);
    }
    
    private void launchSettings()
    {
    	Intent i = new Intent(this, HunterSettings.class);
    	startActivity(i);
    }
    
    private void initSettings()
    {
    	currSettings = Tracker102Settings.getInstance();
    	if (!HunterDogActivity.currSettings.Obtain(getSharedPreferences(APP_PREFERENCES, 0)))
    		Toast.makeText(getBaseContext(), 
                    "Obtaining of preferences has been failed", 
                    Toast.LENGTH_SHORT).show();
    }
    
    private void initTracker(){
		iMessager.InitSmsCenter(this);
		returnMsg = iMessager.SentInit(this);
		mProgressConnecting.setVisibility(View.VISIBLE);
	}
	
	public  void callBackSmsDeliever(){
		SmsReceiver smsFromTk;
		if (returnMsg.isSuccessSent && returnMsg.isSuccessDelievered){
			//then we can proceed to receive coordinates from Tracker 102
			Toast.makeText(getBaseContext(), returnMsg.messageText,
						Toast.LENGTH_SHORT).show();
			Log.i(LOG_GM, String.format("CallBackSMSDeliever %s", returnMsg.messageText));
			smsFromTk = SmsReceiver.getInstance();
			smsFromTk.InitReceiver(returnMsg.gadgetPhone, iMessager);
			
		}
		else
			Toast.makeText(getBaseContext(),"Receiving of coordinates from Tracker is stopped. Tracker is not available: \n" +
					"- SIM is blocked (no money);\n" +
					"- SIM signal is not covered by cellular operator",
						Toast.LENGTH_LONG).show();
	}
	
	public void callBackSmsSend() {
		if (returnMsg.isSuccessSent)
			new CountDownTimer(TIMER_BEGIN_SMS * 1000, 1000) {

		     public void onTick(long millisUntilFinished) {
		         //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
		     }

		     public void onFinish() {
		         if (!returnMsg.isSuccessDelievered){
		        	 Toast.makeText(getBaseContext(), R.string.mSmsNotDelievered,
								Toast.LENGTH_LONG).show();
		        	 mProgressConnecting.setVisibility(View.GONE);
		         }
		     }
		  }.start();
	}
	
	public static void beginPassed()
	{
		mConnect.setEnabled(false);
		mStartUp.setEnabled(true);
		mProgressConnecting.setVisibility(View.GONE);
	}
    
    @Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
    
    private boolean isDebugBuild() throws Exception
	{
	   PackageManager pm = getPackageManager();
	   PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);

	   return ((pi.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
	}
}