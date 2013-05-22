package mln.Forest;

public class Message {
	
	public String mobilePhone;
	public String gadgetPhone = "+";
	public String messageText;
	private String sentError;
	public boolean isSuccessSent;
	public boolean isSuccessDelievered;
	public boolean isCoordRecieved;
	public boolean isSettingDelievered;
	
	public int GetState()
	{
		return 0;
	}

	public void setSentError(String sentError) {
		this.sentError = sentError;
	}

	public String getSentError() {
		return sentError;
	}
}
