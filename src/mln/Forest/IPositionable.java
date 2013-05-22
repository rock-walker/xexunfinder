package mln.Forest;

import java.util.List;

import android.location.Location;

public interface IPositionable {
	public void ShowUserPosition(Location location, boolean isAnimate);
	public void ApplyTrackerCoords(List<String> coords);
}
