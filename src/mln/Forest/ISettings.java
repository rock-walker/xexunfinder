package mln.Forest;

import android.content.SharedPreferences;

public interface ISettings {
	public boolean Obtain(SharedPreferences sp);
	public boolean Save();
	public void Cancel();
}
