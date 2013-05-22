package mln.Forest;

public final class Tracker102 extends Gadget {
	
	private Tracker102Settings settings;
	private Tracker102Messager messager;
	
	public Tracker102(ISettings setting, IMessageProcess message)
	{
		this.settings = (Tracker102Settings)setting;
		this.messager = (Tracker102Messager)message;
	}
}
