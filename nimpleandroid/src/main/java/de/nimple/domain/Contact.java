package de.nimple.domain;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table contacts.
 */
public class Contact {

    private Long rowId;
    private String name;
    private String email;
    private String telephoneHome;
    private String telephoneMobile;
    private String website;
    private String street;
    private String postal;
    private String city;
    private String company;
    private String position;
    private String facebookId;
    private String facebookUrl;
    private String twitterId;
    private String twitterUrl;
    private String xingUrl;
    private String linkedinUrl;
    private String hash;
    private Long created;
    private String note;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Contact() {
    }

    public Contact(Long rowId) {
        this.rowId = rowId;
    }

    public Contact(Long rowId, String name, String email, String telephoneHome, String telephoneMobile, String website, String street, String postal, String city, String company, String position, String facebookId, String facebookUrl, String twitterId, String twitterUrl, String xingUrl, String linkedinUrl, String hash, Long created, String note) {
        this.rowId = rowId;
        this.name = name;
        this.email = email;
        this.telephoneHome = telephoneHome;
        this.telephoneMobile = telephoneMobile;
        this.website = website;
        this.street = street;
        this.postal = postal;
        this.city = city;
        this.company = company;
        this.position = position;
        this.facebookId = facebookId;
        this.facebookUrl = facebookUrl;
        this.twitterId = twitterId;
        this.twitterUrl = twitterUrl;
        this.xingUrl = xingUrl;
        this.linkedinUrl = linkedinUrl;
        this.hash = hash;
        this.created = created;
        this.note = note;
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneHome() {
        return telephoneHome;
    }

    public void setTelephoneHome(String telephoneHome) {
        this.telephoneHome = telephoneHome;
    }

    public String getTelephoneMobile() {
        return telephoneMobile;
    }

    public void setTelephoneMobile(String telephoneMobile) {
        this.telephoneMobile = telephoneMobile;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getXingUrl() {
        return xingUrl;
    }

    public void setXingUrl(String xingUrl) {
        this.xingUrl = xingUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // KEEP METHODS - put your custom methods here
	public String getAddress() {
		StringBuilder sb = new StringBuilder();
		if (hasStreet()) {
			sb.append(street);
			sb.append(", ");
		}
		if (hasPostal()) {
			sb.append(postal);
			sb.append(" ");
		}
		if (hasCity()) {
			sb.append(city);
		}
		return sb.toString();
	}

	public boolean hasStreet() {
		return street != null && !street.equals("");
	}

	public boolean hasPostal() {
		return postal != null && !postal.equals("");
	}

	public boolean hasCity() {
		return city != null && !city.equals("");
	}

	public boolean hasAddress() {
		if (hasStreet())
			return true;
		if (hasPostal())
			return true;
		if (hasCity())
			return true;
		return false;
	}

	public boolean hasNote() {
		return note != null && !note.isEmpty();
	}

	public boolean hasWebsite() {
		return website != null && !website.isEmpty();
	}
    // KEEP METHODS END

}