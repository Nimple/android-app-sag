package de.nimple.services.nimplecode;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.nimple.R;
import de.nimple.dto.NimpleCode;
import de.nimple.util.NimpleCard;
import de.nimple.util.SharedPrefHelper;

public class NimpleCodeHelper implements NimpleCodeService {
	public NimpleCode holder;
	private Context ctx;
	private static int m_curId = 0; //NimpleCodeHelper.NC_CARD_1;

	public NimpleCodeHelper(Context ctx) {
		this.ctx = ctx;
        load();
	}

    @Override
	public NimpleCode load() {
		String curId = "";

		//Necessary to support versions with one card
		if (m_curId != 0) {
			curId = String.valueOf(m_curId);
		}

		holder = new NimpleCode();
		holder.cardName = SharedPrefHelper.getString(NC_CARD_NAME + curId, ctx);
        holder.id = SharedPrefHelper.getInt(NC_CARD_ID + curId, ctx);
		holder.firstname = SharedPrefHelper.getString(NC_VALUE_FIRSTNAME + curId, ctx);
		holder.lastname = SharedPrefHelper.getString(NC_VALUE_LASTNAME + curId, ctx);
		holder.mail = SharedPrefHelper.getString(NC_VALUE_MAIL + curId, ctx);
		holder.phone_home = SharedPrefHelper.getString(NC_VALUE_PHONE_HOME + curId, ctx);
        holder.phone_mobile = SharedPrefHelper.getString(NC_VALUE_PHONE_MOBILE + curId, ctx);

		holder.company = SharedPrefHelper.getString(NC_VALUE_COMPANY + curId, ctx);
		holder.position = SharedPrefHelper.getString(NC_VALUE_POSITION + curId, ctx);
		holder.address = new Address(SharedPrefHelper.getString(NC_VALUE_ADDRESS + curId, ctx));

		holder.websiteUrl = SharedPrefHelper.getString(NC_VALUE_URL_WEBSITE + curId, ctx);

		holder.facebookId = SharedPrefHelper.getString(NC_VALUE_ID_FACEBOOK + curId, ctx);
		holder.facebookUrl = SharedPrefHelper.getString(NC_VALUE_URL_FACEBOOK + curId, ctx);

		holder.twitterId = SharedPrefHelper.getString(NC_VALUE_ID_TWITTER + curId, ctx);
		holder.twitterUrl = SharedPrefHelper.getString(NC_VALUE_URL_TWITTER + curId, ctx);

		holder.xingUrl = SharedPrefHelper.getString(NC_VALUE_URL_XING + curId, ctx);

		holder.linkedinUrl = SharedPrefHelper.getString(NC_VALUE_URL_LINKEDIN + curId, ctx);

		holder.show.mail = SharedPrefHelper.getBoolean(NC_SHOW_MAIL + curId, true, ctx);
		holder.show.phone_home = SharedPrefHelper.getBoolean(NC_SHOW_PHONE_HOME + curId, true, ctx);
        holder.show.phone_mobile = SharedPrefHelper.getBoolean(NC_SHOW_PHONE_MOBILE + curId, true, ctx);
		holder.show.company = SharedPrefHelper.getBoolean(NC_SHOW_COMPANY + curId, true, ctx);
		holder.show.position = SharedPrefHelper.getBoolean(NC_SHOW_POSITION + curId, true, ctx);
		holder.show.address = SharedPrefHelper.getBoolean(NC_SHOW_ADDRESS + curId, true, ctx);

		holder.show.website = SharedPrefHelper.getBoolean(NC_SHOW_WEBSITE + curId, true, ctx);
		holder.show.facebook = SharedPrefHelper.getBoolean(NC_SHOW_FACEBOOK + curId, true, ctx);
		holder.show.twitter = SharedPrefHelper.getBoolean(NC_SHOW_TWITTER + curId, true, ctx);
		holder.show.xing = SharedPrefHelper.getBoolean(NC_SHOW_XING + curId, true, ctx);
		holder.show.linkedin = SharedPrefHelper.getBoolean(NC_SHOW_LINKEDIN + curId, true, ctx);

		return holder;
	}

	@Override
	public void save(NimpleCode nimpleCode) {
        holder = nimpleCode;

        String curId = "";
        SharedPrefHelper.putBoolean(NC_INIT, true, ctx);

        //Necessary to support versions with one card
        if (m_curId != 0) {
            curId = String.valueOf(m_curId);
        }
        
        SharedPrefHelper.putString(NC_CARD_NAME + curId, holder.cardName, ctx);
        SharedPrefHelper.putString(NC_VALUE_FIRSTNAME + curId, holder.firstname, ctx);
        SharedPrefHelper.putString(NC_VALUE_LASTNAME + curId, holder.lastname, ctx);
        SharedPrefHelper.putString(NC_VALUE_MAIL + curId, holder.mail, ctx);
        SharedPrefHelper.putString(NC_VALUE_PHONE_HOME + curId, holder.phone_home, ctx);
        SharedPrefHelper.putString(NC_VALUE_PHONE_MOBILE + curId, holder.phone_mobile, ctx);

        SharedPrefHelper.putString(NC_VALUE_COMPANY + curId, holder.company, ctx);
        SharedPrefHelper.putString(NC_VALUE_POSITION + curId, holder.position, ctx);
        SharedPrefHelper.putString(NC_VALUE_ADDRESS + curId, holder.address.toString(), ctx);

        SharedPrefHelper.putString(NC_VALUE_URL_WEBSITE + curId, holder.websiteUrl, ctx);
        SharedPrefHelper.putString(NC_VALUE_ID_FACEBOOK + curId, holder.facebookId, ctx);
        SharedPrefHelper.putString(NC_VALUE_URL_FACEBOOK + curId, holder.facebookUrl, ctx);
        SharedPrefHelper.putString(NC_VALUE_ID_TWITTER + curId, holder.twitterId, ctx);
        SharedPrefHelper.putString(NC_VALUE_URL_TWITTER + curId, holder.twitterUrl, ctx);
        SharedPrefHelper.putString(NC_VALUE_URL_XING + curId, holder.xingUrl, ctx);
        SharedPrefHelper.putString(NC_VALUE_URL_LINKEDIN + curId, holder.linkedinUrl, ctx);

        // Show
        SharedPrefHelper.putBoolean(NC_SHOW_MAIL + curId, holder.show.mail, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_PHONE_HOME + curId, holder.show.phone_home, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_PHONE_MOBILE + curId, holder.show.phone_mobile, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_COMPANY + curId, holder.show.company, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_POSITION + curId, holder.show.position, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_ADDRESS + curId, holder.show.address, ctx);

        SharedPrefHelper.putBoolean(NC_SHOW_WEBSITE + curId, holder.show.website, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_FACEBOOK + curId, holder.show.facebook, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_TWITTER + curId, holder.show.twitter, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_XING + curId, holder.show.xing, ctx);
        SharedPrefHelper.putBoolean(NC_SHOW_LINKEDIN + curId, holder.show.linkedin, ctx);

	}

    @Override
    public void delete(NimpleCode nimpleCode) {
        String curId = "";

        //Necessary to support versions with one card
        if (nimpleCode.id != 0) {
            curId = String.valueOf(nimpleCode.id);
            SharedPrefHelper.remove(NC_CARD_NAME + curId, ctx);
        }
        SharedPrefHelper.remove(NC_CARD_NAME + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_FIRSTNAME + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_LASTNAME + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_MAIL + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_PHONE_HOME + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_PHONE_MOBILE + curId, ctx);

        SharedPrefHelper.remove(NC_VALUE_COMPANY + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_POSITION + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_ADDRESS + curId, ctx);

        SharedPrefHelper.remove(NC_VALUE_URL_WEBSITE + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_ID_FACEBOOK + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_URL_FACEBOOK + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_ID_TWITTER + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_URL_TWITTER + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_URL_XING + curId, ctx);
        SharedPrefHelper.remove(NC_VALUE_URL_LINKEDIN + curId, ctx);

        // Show
        SharedPrefHelper.remove(NC_SHOW_MAIL + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_PHONE_HOME + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_PHONE_MOBILE + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_COMPANY + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_POSITION + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_ADDRESS + curId, ctx);

        SharedPrefHelper.remove(NC_SHOW_WEBSITE + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_FACEBOOK + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_TWITTER + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_XING + curId, ctx);
        SharedPrefHelper.remove(NC_SHOW_LINKEDIN + curId, ctx);
    }

	public boolean isInitialState() {
		return !SharedPrefHelper.getBoolean(NC_INIT, ctx);
	}

    public static void initCardNameFunctionality(Context ctx){
        if(SharedPrefHelper.getString(NC_CARD_NAME, ctx).equals("")){
            SharedPrefHelper.putString(NC_CARD_NAME , "1. " + ctx.getString(R.string.nimpleCards_defaultName), ctx);
            SharedPrefHelper.putInt(NC_CARD_ID, 0, ctx);
            SharedPrefHelper.putInt(NC_CARDS_GLOBALE_ID_RIDER , 1 , ctx);
            setCurrentId(0);
        }
    }

    public static void update(Context ctx){
        //Only if the app was updated this value is set.
        if(!SharedPrefHelper.getString(NC_VALUE_PHONE_OLD, ctx).equals("")) {
            SharedPrefHelper.putString(NC_VALUE_PHONE_MOBILE, SharedPrefHelper.getString(NC_VALUE_PHONE_OLD, ctx), ctx);
            SharedPrefHelper.remove(NC_VALUE_PHONE_OLD, ctx);
        }
    }

    public static List<NimpleCard> getCards(Context ctx){
        List<NimpleCard> cards = new ArrayList<NimpleCard>();
        int rider = SharedPrefHelper.getInt(NC_CARDS_GLOBALE_ID_RIDER, ctx);
        for (int i = 0; i < rider; i++) {
            String name;
            int id;

            if (i != 0) {
                name = SharedPrefHelper.getString(NC_CARD_NAME + i, ctx);
                id =  SharedPrefHelper.getInt(NC_CARD_ID + i, ctx);
            } else {
                name = SharedPrefHelper.getString(NC_CARD_NAME, ctx);
                id =  SharedPrefHelper.getInt(NC_CARD_ID, ctx);
            }

            if (name != null && !name.equals("")) {
                cards.add(new NimpleCard(id, name));
            }
        }

        return cards;
    }

    public static int addCard(Context ctx){
        int id = SharedPrefHelper.getInt(NC_CARDS_GLOBALE_ID_RIDER, ctx);
        String cardName =  (id + 1) + ". " + ctx.getString(R.string.nimpleCards_defaultName);
        SharedPrefHelper.putString(NC_CARD_NAME + id , cardName, ctx);
        SharedPrefHelper.putInt(NC_CARD_ID + id, id, ctx);
        SharedPrefHelper.putString(NC_VALUE_FIRSTNAME + id, SharedPrefHelper.getString(NC_VALUE_FIRSTNAME, ctx), ctx);
        SharedPrefHelper.putString(NC_VALUE_LASTNAME + id, SharedPrefHelper.getString(NC_VALUE_LASTNAME, ctx), ctx);
        id++;
        SharedPrefHelper.putInt(NC_CARDS_GLOBALE_ID_RIDER , id , ctx);
        return --id;
    }

    public static void setCurrentId(int  id){
         m_curId = id;
    }

    public static int getCurrentId(){
        return m_curId;
    }


	// ////////////////////////// constants /////////////////////////////
    private static final String NC_CARDS_GLOBALE_ID_RIDER = "nimple_cards_id";
    private static final String NC_CARD_NAME = "nimple_card_name";
    private static final String NC_CARD_ID = "nimple_card_id";
    //Update logic
    private static final String NC_VALUE_PHONE_OLD = "nimple_code_phone";
    private static final String NC_VALUE_PHONE_MOBILE = "nimple_code_phone_mobile";

	private final String NC_INIT = "nimple_code_init";

	private static final String NC_VALUE_FIRSTNAME = "nimple_code_firstname";
	private static final String NC_VALUE_LASTNAME = "nimple_code_lastname";
	private final String NC_VALUE_MAIL = "nimple_code_mail";
	private final String NC_VALUE_PHONE_HOME = "nimple_code_phone_home";
	private final String NC_VALUE_ADDRESS = "nimple_code_address";
	private final String NC_VALUE_POSITION = "nimple_code_position";
	private final String NC_VALUE_COMPANY = "nimple_code_company";

	private final String NC_VALUE_URL_LINKEDIN = "nimple_code_linkedin_url";
	private final String NC_VALUE_URL_XING = "nimple_code_xing_url";
	private final String NC_VALUE_URL_TWITTER = "nimple_code_twitter_url";
	private final String NC_VALUE_URL_FACEBOOK = "nimple_code_facebook_url";
	private final String NC_VALUE_URL_WEBSITE = "nimple_code_website_url";
	private final String NC_VALUE_ID_FACEBOOK = "nimple_code_facebook_id";
	private final String NC_VALUE_ID_TWITTER = "nimple_code_twitter_id";

	// Show
	private final String NC_SHOW_MAIL = "nimple_code_mail_show";
	private final String NC_SHOW_PHONE_HOME = "nimple_code_phone_home_show";
    private final String NC_SHOW_PHONE_MOBILE = "nimple_code_phone_work_show";
	private final String NC_SHOW_COMPANY = "nimple_code_company_show";
	private final String NC_SHOW_POSITION = "nimple_code_position_show";
	private final String NC_SHOW_ADDRESS = "nimple_code_address_show";
	private final String NC_SHOW_LINKEDIN = "nimple_code_linkedin_show";
	private final String NC_SHOW_XING = "nimple_code_xing_show";
	private final String NC_SHOW_TWITTER = "nimple_code_twitter_show";
	private final String NC_SHOW_FACEBOOK = "nimple_code_facebook_show";
	private final String NC_SHOW_WEBSITE = "nimple_code_website_show";
}