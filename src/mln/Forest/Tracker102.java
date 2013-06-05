package mln.Forest;

public final class Tracker102 extends Gadget {
	
	private Tracker102Settings settings;
	private TrackerMessager messager;
	
	public Tracker102(ISettings setting, IMessageProcess message)
	{
		this.settings = (Tracker102Settings)setting;
		this.messager = (TrackerMessager)message;
	}
}
