package de.nimple.greendao;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Generator {
    public static void main(String args[]) throws IOException, Exception {
        Schema schema = new Schema(4, "de.nimple.domain");

        defineContact(schema);

        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void defineContact(Schema schema) {
        Entity contact = schema.addEntity("Contact");
        contact.setTableName("contacts");

        contact.addLongProperty("rowId").columnName("rowId").primaryKey();

        contact.addStringProperty("name").columnName("name");
        contact.addStringProperty("email").columnName("email");
        contact.addStringProperty("telephoneHome").columnName("telephoneHome");
        contact.addStringProperty("telephoneMobile").columnName("telephoneMobile");
        contact.addStringProperty("website").columnName("website");

        contact.addStringProperty("street").columnName("street");
        contact.addStringProperty("postal").columnName("postal");
        contact.addStringProperty("city").columnName("city");

        contact.addStringProperty("company").columnName("company");
        contact.addStringProperty("position").columnName("position");

        contact.addStringProperty("facebookId").columnName("facebookId");
        contact.addStringProperty("facebookUrl").columnName("facebookUrl");

        contact.addStringProperty("twitterId").columnName("twitterId");
        contact.addStringProperty("twitterUrl").columnName("twitterUrl");

        contact.addStringProperty("xingUrl").columnName("xingUrl");

        contact.addStringProperty("linkedinUrl").columnName("linkedinUrl");

        contact.addStringProperty("hash").columnName("hash");
        contact.addLongProperty("created").columnName("created");

        contact.addStringProperty("note").columnName("note");

        contact.setHasKeepSections(true);
        contact.setSkipTableCreation(false);
    }
}
