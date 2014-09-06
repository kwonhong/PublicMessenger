package classes;

public class MessageExtractor {
	private String originalMessage;
	private String nextWord;
	private String restWord;
	
	public MessageExtractor(String message) {
		this.originalMessage = message;
		this.restWord = message;
	}
	
	public MessageExtractor() {
		
	}
	
	public String getOriginalMessage() {
		return originalMessage;
	}
	
	public void setMessage(String message) {
		this.originalMessage = message;
		this.restWord = message;
	}
	
	public String extractNextWord() {
		int i = restWord.indexOf('#');
		nextWord = restWord.substring(0, i);
		restWord = restWord.substring(i+1);
		
		return nextWord;
	}
	
}
