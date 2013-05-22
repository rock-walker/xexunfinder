package mln.Forest;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GeneralMap implements IPositionable {

	private static final String LOC_TAG = "Location Error: ";
	public static final int ZOOM = 17;
	private static final int MAX_ZOOM = 20;
	
	private MapActivity gMap;
	private MapView mv;
	private MapController mc;
	//overlays
	private GpsItemOverlay phoneOwnerOverlay;
	private GpsItemOverlay trackerOverlay;
	private List<Overlay> mapOverlays;
	private List<GeoPoint> geoItems;
	private Drawable drPhoneOwner;
	private Drawable drTracker;
	
	private GeoPoint userPoint;
	private GeoPoint trackerPoint;
	
	public GeneralMap(MapView mv, MapActivity ma)
	{
		this.mv = mv;
		this.gMap = ma;
		mc = mv.getController();
		adjustMapView();
		initOverlays();
		geoItems = new ArrayList<GeoPoint>();
	}
	
	public void showSmsStatus()
	{
		
	}
	
	private void adjustMapView() {
		mv.setBuiltInZoomControls(true);            
        mv.setSatellite(true);
	}

	@Override
	public void ShowUserPosition(Location location, boolean isAnimate) {
		try{
	    	double[] coordinates = {location.getLatitude(), location.getLongitude()};
			userPoint = new GeoPoint((int)(coordinates[0] * 1E6), 
									 (int)(coordinates[1] * 1E6));
			showGeoPoint(userPoint, phoneOwnerOverlay);
			if (isAnimate)
				mc.animateTo(userPoint);
		}
		catch(Exception ex)
		{
			Toast.makeText(gMap.getBaseContext(), 
					R.string.GpsUserLocation, Toast.LENGTH_LONG).show();
			Log.i(LOC_TAG, ex.toString());
			//organize stop sending/receiving of SMS
			double[] coordinates = {53.229096, 26.668306};
			userPoint = new GeoPoint((int)(coordinates[0] * 1E6), 
									 (int)(coordinates[1] * 1E6));
			showGeoPoint(userPoint, phoneOwnerOverlay);
			mc.animateTo(userPoint);
			mc.setZoom(ZOOM);
		}
	}
	
	private void initOverlays()
	{
		drPhoneOwner = gMap.getResources().getDrawable(R.drawable.men);
		drTracker = gMap.getResources().getDrawable(R.drawable.dog);
		
		phoneOwnerOverlay = new GpsItemOverlay(drPhoneOwner);
		phoneOwnerOverlay.isTarget = false;
		
		trackerOverlay = new GpsItemOverlay(drTracker);
		trackerOverlay.isTarget = true;
	}
	 
	public void showGeoPoint(GeoPoint point, GpsItemOverlay overlay)
	{
		mapOverlays = mv.getOverlays();
		//mv.getOverlays().clear();
		OverlayItem overlayItem = new OverlayItem(point, "", "");
		if ((overlay.size() > 0) && (!overlay.isTarget))
		{
			mapOverlays.remove(overlay);
			overlay.removeOverlays();
		}
		overlay.addOverlay(overlayItem);
		mapOverlays.add(overlay);
		
		mv.invalidate();
	}
	
	@Override
	public void ApplyTrackerCoords(List<String> sCoords) {
		double[] dCoords = new double[2];
		
		if (sCoords != null)
		{
			dCoords[0] = Double.parseDouble(sCoords.get(0).replaceFirst("[n|e|s|w]", "")); 
			dCoords[1] = Double.parseDouble(sCoords.get(1).replaceFirst("[n|e|s|w]", ""));
			trackerPoint = new GeoPoint((int)(dCoords[0] * 1E6), 
								 (int)(dCoords[1] * 1E6));
			showGeoPoint(trackerPoint, trackerOverlay);
			
			spanItems();
			
			geoItems.remove(userPoint);
		}
	}

	private void spanItems() {
		double fitFactor = 1.5;
		int minLat = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLon = Integer.MIN_VALUE;
		geoItems.add(trackerPoint);
		geoItems.add(userPoint);
		
		for (GeoPoint item : geoItems) 
		{ 
		      int lat = item.getLatitudeE6();
		      int lon = item.getLongitudeE6();

		      maxLat = Math.max(lat, maxLat);
		      minLat = Math.min(lat, minLat);
		      maxLon = Math.max(lon, maxLon);
		      minLon = Math.min(lon, minLon);
		 }

		mc.zoomToSpan((int)(Math.abs(maxLat - minLat) * fitFactor), (int)(Math.abs(maxLon - minLon) * fitFactor));
		if (mv.getZoomLevel() > MAX_ZOOM)
			mc.setZoom(MAX_ZOOM);
		mc.animateTo(new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon)/2));
	}
}
