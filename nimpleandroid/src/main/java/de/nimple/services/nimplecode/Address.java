package de.nimple.services.nimplecode;

public class Address {
	private String postOffice = "";
	private String extended = "";
	private String street = "";
	private String locality = "";
	private String region = "";
	private String postalCode = "";
	private String country = "";

	private final int postOfficeId = 0;
	private final int extendedId = 1;
	private final int streetId = 2;
	private final int localityId = 3;
	private final int regionId = 4;
	private final int postalCodeId = 5;
	private final int countryId = 6;

	public String separator = ";";

	public Address() {
		super();
	}

	public Address(String street, String postal, String locality) {
		this();
		this.postalCode = postal;
		this.street = street;
		this.locality = locality;
	}

	public Address(String addrStr) throws IllegalArgumentException {
		this();

		if (addrStr == null) {
			throw new IllegalArgumentException("String is not allowed to be null for Address");
		}

		if (addrStr.equals("")) {
			return;
		}

		String[] tmpArr = addrStr.split(separator);
		// the length of the array depends on the amount of attributes it contains. this is variable
		// but the position of the attributes is fix
		int counter = 0;
		for (String item : tmpArr) {
			switch (counter) {
			case postOfficeId:
				postOffice = item;
				break;
			case extendedId:
				extended = item;
				break;
			case streetId:
				street = item;
				break;
			case localityId:
				locality = item;
				break;
			case regionId:
				region = item;
				break;
			case postalCodeId:
				postalCode = item;
				break;
			case countryId:
				country = item;
				break;
			default:
				// no action
			}
			counter++;
		}
	}

	public String getPostOffice() {
		return postOffice;
	}

	public void setPostOffice(String postOffice) {
		this.postOffice = postOffice;
	}

	public String getExtended() {
		return extended;
	}

	public void setExtended(String extended) {
		this.extended = extended;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCounty() {
		return country;
	}

	public void setCounty(String county) {
		this.country = county;
	}

	public boolean hasStreet() {
		return street != null && !street.equals("");
	}

	public boolean hasPostal() {
		return postalCode != null && !postalCode.equals("");
	}

	public boolean hasLocality() {
		return locality != null && !locality.equals("");
	}

	public boolean hasAddress() {
		if (hasStreet())
			return true;
		if (hasPostal())
			return true;
		if (hasLocality())
			return true;
		return false;
	}

	public String toVcard3Attr() {
		StringBuffer sb = new StringBuffer();
		sb.append(VCardConstants.PROPERTY_ADR);
		sb.append(VCardConstants.VALUE_SEPARATOR);
		sb.append(VCardConstants.PARAM_TYPE);
		sb.append(VCardConstants.ASSIGNMENT);
		sb.append(VCardConstants.PARAM_TYPE_HOME);
		sb.append(VCardConstants.DEF_SEPARATOR);
		sb.append(this.toString());
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(postOffice);
		sb.append(separator);
		sb.append(extended);
		sb.append(separator);
		sb.append(street);
		sb.append(separator);
		sb.append(locality);
		sb.append(separator);
		sb.append(region);
		sb.append(separator);
		sb.append(postalCode);
		sb.append(separator);
		sb.append(country);
		return sb.toString();
	}
}
