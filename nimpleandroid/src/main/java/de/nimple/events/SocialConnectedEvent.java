package de.nimple.events;

import org.json.JSONObject;

import de.nimple.enums.SocialNetwork;

public class SocialConnectedEvent {
	private SocialNetwork type;
	private JSONObject response;

	public SocialConnectedEvent(SocialNetwork type, JSONObject response) {
		this.type = type;
		this.response = response;
	}

	public SocialNetwork getType() {
		return type;
	}

	public JSONObject getResponse() {
		return response;
	}
}