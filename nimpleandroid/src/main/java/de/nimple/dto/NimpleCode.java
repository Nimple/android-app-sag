package de.nimple.dto;

import de.nimple.services.nimplecode.Address;

/**
 * Created by bjohn on 23/09/14.
 */
public class NimpleCode {
	public Show show = new Show();
	public String cardName;
    public int id;

	public String firstname;
	public String lastname;
	public String mail;
	public String phone_home;
	public String phone_mobile;
	public String company;
	public String position;
	public Address address;

	public String websiteUrl;

	public String facebookUrl;
	public String facebookId;

	public String twitterUrl;
	public String twitterId;

	public String xingUrl;

	public String linkedinUrl;

	public class Show {
		public boolean mail;
		public boolean phone_home;
		public boolean phone_mobile;
		public boolean company;
		public boolean position;
		public boolean address;

		public boolean website;
		public boolean facebook;
		public boolean twitter;
		public boolean xing;
		public boolean linkedin;
	}
}
