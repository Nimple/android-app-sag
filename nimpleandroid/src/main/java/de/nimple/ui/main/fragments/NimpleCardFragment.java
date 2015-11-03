package de.nimple.ui.main.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.events.NimpleCardChangedEvent;
import de.nimple.events.NimpleCodeChangedEvent;
import de.nimple.events.SharedEvent;
import de.nimple.services.common.IGraphic;
import de.nimple.services.common.MultiCardBarExtender;
import de.nimple.services.export.Export;
import de.nimple.services.export.IExportExtender;
import de.nimple.services.nimplecode.Address;
import de.nimple.services.nimplecode.NimpleCodeHelper;
import de.nimple.services.nimplecode.VCardHelper;
import de.nimple.ui.edit.EditNimpleCodeActivity;
import de.nimple.util.fragment.MenuHelper;

public class NimpleCardFragment extends Fragment implements IExportExtender, IGraphic {
    public static final NimpleCardFragment newInstance() {
        return new NimpleCardFragment();
    }

    private MultiCardBarExtender multiCardBarExt;

    @InjectView(R.id.nameTextView)
    TextView nameTextView;
    @InjectView(R.id.mailTextView)
    TextView mailTextView;
    @InjectView(R.id.phoneTextView)
    TextView phoneTextView;
    @InjectView(R.id.phoneWorkTextView)
    TextView phoneWorkTextView;
    @InjectView(R.id.companyTextView)
    TextView companyTextView;
    @InjectView(R.id.jobTextView)
    TextView positionTextView;
    @InjectView(R.id.websiteTextView)
    TextView websiteTextView;
    @InjectView(R.id.addressStreetTextView)
    TextView addressStreetTextView;
    @InjectView(R.id.addressCityTextView)
    TextView addressCityTextView;
    @InjectView(R.id.facebookRoundIcon)
    ImageView facebookImageView;
    @InjectView(R.id.twitterRoundIcon)
    ImageView twitterImageView;
    @InjectView(R.id.xingRoundIcon)
    ImageView xingImageView;
    @InjectView(R.id.linkedinRoundIcon)
    ImageView linkedinImageView;
    @InjectView(R.id.ncard_name)
    TextView nCardName;
    @InjectView(R.id.card_dropdown)
    LinearLayout linearLayout;

    private Context ctx;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.main_ncard_fragment, container, false);
        ButterKnife.inject(this, view);
        multiCardBarExt = new MultiCardBarExtender(this);
        EventBus.getDefault().register(this);
        refreshUi();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.card_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuHelper.selectMenuItem(item, this);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(NimpleCodeChangedEvent ev) {
        refreshUi();
    }

    public void onEvent(NimpleCardChangedEvent ev) {
        NimpleCodeHelper ncode = new NimpleCodeHelper(ctx);
        nCardName.setText(ncode.holder.cardName);
    }

    @OnClick(R.id.edit_nimple_card)
    public void startEditNimpleCodeActivity() {
        Intent intent = new Intent(ctx, EditNimpleCodeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public void refreshUi() {
        NimpleCodeHelper ncode = new NimpleCodeHelper(ctx);

        if (!ncode.isInitialState()) {
            String name = ncode.holder.firstname + " " + ncode.holder.lastname;
            String phone_home = ncode.holder.phone_home;
            String phone_mobile = ncode.holder.phone_mobile;
            String mail = ncode.holder.mail;
            String company = ncode.holder.company;
            String position = ncode.holder.position;
            String website = ncode.holder.websiteUrl;
            Address address = ncode.holder.address;

            nameTextView.setText(name);
            phoneTextView.setText(phone_home);
            phoneWorkTextView.setText(phone_mobile);
            mailTextView.setText(mail);
            companyTextView.setText(company);
            positionTextView.setText(position);
            websiteTextView.setText(website);

            addressStreetTextView.setText(address.getStreet());
            addressCityTextView.setText(address.getPostalCode() + " " + address.getLocality());
        }

        nCardName.setText(ncode.holder.cardName);

        this.checkIfTextViewIsEmpty(ncode.holder.show.phone_home, phoneTextView);
        this.checkIfTextViewIsEmpty(ncode.holder.show.phone_mobile, phoneWorkTextView);
        this.checkIfTextViewIsEmpty(ncode.holder.show.mail, mailTextView);
        this.checkIfTextViewIsEmpty(ncode.holder.show.company, companyTextView);
        this.checkIfTextViewIsEmpty(ncode.holder.show.position, positionTextView);
        this.checkIfTextViewIsEmpty(ncode.holder.show.website, websiteTextView);
        this.checkIfTextViewIsEmpty(ncode.holder.show.address, addressStreetTextView);
        this.checkIfTextViewIsEmpty(ncode.holder.show.address, addressCityTextView);

        this.checkIfSocialMediaIsEmpty(ncode.holder.show.facebook, ncode.holder.facebookUrl, facebookImageView);
        this.checkIfSocialMediaIsEmpty(ncode.holder.show.twitter, ncode.holder.twitterUrl, twitterImageView);
        this.checkIfSocialMediaIsEmpty(ncode.holder.show.xing, ncode.holder.xingUrl, xingImageView);
        this.checkIfSocialMediaIsEmpty(ncode.holder.show.linkedin, ncode.holder.linkedinUrl, linkedinImageView);
    }

    private void checkIfTextViewIsEmpty(boolean isShown, TextView vi) {
        if (isShown) {
            vi.setTextColor(getResources().getColor(R.color.nimple_default_text_color));
        } else {
            vi.setTextColor(getResources().getColor(R.color.nimple_text_color_hint));
        }
    }

    private void checkIfSocialMediaIsEmpty(boolean isShown, String content, ImageView iv) {
        if (isShown && content.length() != 0) {
            iv.setAlpha(255);
        } else {
            iv.setAlpha(30);
        }
    }

    @Override
    public Export getExport() {
        EventBus.getDefault().post(new SharedEvent(SharedEvent.Type.Card));
        return new Export<String>(VCardHelper.getCardFromSharedPrefs(ctx));
    }

    public View getView() {
        return this.view;
    }
}