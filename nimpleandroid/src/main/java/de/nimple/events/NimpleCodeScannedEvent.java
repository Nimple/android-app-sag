package de.nimple.events;

public class NimpleCodeScannedEvent {
	private String data;

	public NimpleCodeScannedEvent(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}
}