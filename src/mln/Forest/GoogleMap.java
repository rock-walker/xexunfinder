package mln.Forest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class GoogleMap extends MapActivity implements LocationListener{
	
	private MapView mapView;
	private TextView nextCoord;
	private IMessageProcess iMessager;
	private Message returnMsg;
	private GeneralMap genMap;
	
	//location var's
	private LocationManager locMngr;
	private String bestProvider;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		setContentView( (HunterDogActivity.isDebug) ? R.layout.map 
											 : R.layout.map_release);
        mapView = (MapView)findViewById(R.id.mapview);
        setGenMap(new GeneralMap(mapView, this));
        //
        mapView.getController().setZoom(GeneralMap.ZOOM);
        //
        initLocationListener();
        
        bootstrapMap();
	}
	
	private void initLocationListener() {
		locMngr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setSpeedRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		
		bestProvider = locMngr.getBestProvider(criteria, false);
		
		if ( !locMngr.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			turnGPSOn();
	        //buildAlertMessageNoGps();
		}
	    if ( !locMngr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
	    	Toast.makeText(getBaseContext(), 
	    			R.string.GpsAUserLocation, Toast.LENGTH_LONG).show();
	    	bestProvider = LocationManager.GPS_PROVIDER;
	    }
		Location initLoc = locMngr.getLastKnownLocation(bestProvider);
		if (initLoc == null) // if Newtork_provider return null
			initLoc = locMngr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		genMap.ShowUserPosition(initLoc, true);
	}

	private void buildAlertMessageNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(R.string.GpsDisabled)//("Yout GPS seems to be disabled, do you want to enable it?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                   startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}

	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

	public void bootstrapMap(){
		iMessager = HunterDogActivity.iMessager;
		returnMsg = HunterDogActivity.returnMsg;
		
		if (iMessager != null)
			iMessager.SentSetting(this);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (locMngr != null)
			locMngr.requestLocationUpdates(bestProvider, 5000, 2, this);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		if (locMngr != null)
			locMngr.removeUpdates(this);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		turnGPSOff();
		//stop here sms receiving
	}

	private void setGenMap(GeneralMap genMap) {
		this.genMap = genMap;
	}

	public GeneralMap getGenMap() {
		return genMap;
	}

	@Override
	public void onLocationChanged(Location location) {
		genMap.ShowUserPosition(location, false);		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private void turnGPSOn(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	    if (Build.VERSION.SDK_INT > 8)
	    	buildAlertMessageNoGps();
	}
	
	private void turnGPSOff(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);
	    }
	}


	public CountDownTimer showTimer(String period, String type) {
		nextCoord = (TextView)findViewById(R.id.nextCoord);
		int iPeriod = Integer.parseInt(period);
		
		if (type.equals("m"))
			iPeriod = iPeriod * 60;
		else if (type.equals("h"))
			iPeriod = iPeriod * 3600;
		
		final int countDown = iPeriod;
		final int waitSmsTime = 180;
		
		
		CountDownTimer timer = new CountDownTimer(countDown * 1000, 1000) {
			String txtTime;
			TimeDigits timer;
			int startTime = countDown;
			@Override
			public void onTick(long millisUntilFinished) {
				timer = TimeDigits.getTimeDigits(startTime);
				txtTime = timer.getFullTime(":");
				nextCoord.setText(String.format(getResources().getString(R.string.infoNextCoord), txtTime), BufferType.NORMAL);
				startTime--;
			}
			
			@Override
			public void onFinish() {
				nextCoord.setText(R.string.infoWaitCoord);
				returnMsg.isCoordRecieved = false;
				
				new CountDownTimer(waitSmsTime * 1000, 1000) {

					 boolean firstTick = false;
					
				     public void onTick(long millisUntilFinished) {
				         if (returnMsg.isCoordRecieved)
				         {
				        	 Log.i("Google Map", "helper tick happened");
				        	 if(!firstTick)
				        	 {
				        		nextCoord.setText("");
				        	 	firstTick = true;
				        	 }
				        	 //cancel doesn't work in this case;
				        	 cancel();
				         }
				     }

				     public void onFinish() {
				         if (!returnMsg.isCoordRecieved &&
				        		 !returnMsg.isSettingDelievered)
				         {
				        	 Toast.makeText(getBaseContext(), R.string.iCoordNotReceived,
										Toast.LENGTH_LONG).show();
				        	 nextCoord.setText("");
				        	 iMessager.SentSetting(null);
				         }
				     }
				  }.start();
			}
			
		}.start();
		
		return timer;
	}
}
