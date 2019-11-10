package objectRepository.common;

public enum State {
	MP("Madhya Pradesh"),
	MH("Maharashtra"),
	JH("Jharkhand");
	public String val;
	private State(String v){
		this.val=v;
	}
}
