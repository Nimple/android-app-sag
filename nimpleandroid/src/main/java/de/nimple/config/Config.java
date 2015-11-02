package de.nimple.config;

import de.nimple.BuildConfig;

final public class Config {
    public static final String MIXPANEL_TOKEN;
    public static final String GOOGLE_LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA23K" +
            "oHklzSAHKC8nhUxgvZqXZVRayTj+4IPuSL5iPpoWPiPsXsRR33IDiDLYfiww8k2j6yNoVWLB5GI4uG18DX310mP" +
            "AAUh66V2TKHCsE5P5eEEpQC2X2iiD6uLJqHfjidwIKuIJe2RkxQS2F7kK3EHt9Gil+o1g97ss9e7JG96RuFmtGr" +
            "nfDpcQWRYdcdwEFXLAbJs9NZAXOAMm9L+rG+MQOR62ZrwPXGsDO0MrpGJ8aKu6w4yMaF8A2frkdulFS3wyacoLA" +
            "tpHVeyJe0czOuse3ojO7jlOvvLIl1NJ6LRUl+m4wDVWd0MmlPsTQxaAnH3KNF6yGjBxQqawj74YMEwIDAQAB";
   // public static final String GOOGLE_PRODUCT_ID = "android.test.purchased"; //simulate app was purchased
   // public static final String GOOGLE_PRODUCT_ID = "android.test.canceled"; //simulate app  purchased was canceled
   // public static final String GOOGLE_PRODUCT_ID = "android.test.refunded"; //simulate purchased was refunded
   // public static final String GOOGLE_PRODUCT_ID = "android.test.item_unavailable"; //simulate app is unavailable
    public static final String GOOGLE_PRODUCT_ID = "de.nimple";
    static {
        if (BuildConfig.DEBUG) {
            // dev token
            MIXPANEL_TOKEN = "6e3eeca24e9b2372e8582b381295ca0c";
        } else {
            // prod token
            MIXPANEL_TOKEN = "c0d8c866df9c197644c6087495151c7e";
        }
    }
}