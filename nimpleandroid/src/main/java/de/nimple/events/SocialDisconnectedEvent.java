package de.nimple.events;

import de.nimple.enums.SocialNetwork;

public class SocialDisconnectedEvent {
	private SocialNetwork type;

	public SocialDisconnectedEvent(SocialNetwork type) {
		this.type = type;
	}

	public SocialNetwork getType() {
		return type;
	}
}