package de.nimple.ui.main.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.events.NimpleCardChangedEvent;
import de.nimple.events.NimpleCodeChangedEvent;
import de.nimple.events.SharedEvent;
import de.nimple.services.common.IGraphic;
import de.nimple.services.common.MultiCardBarExtender;
import de.nimple.services.export.Export;
import de.nimple.services.export.IExportExtender;
import de.nimple.services.nimplecode.NimpleCodeHelper;
import de.nimple.services.nimplecode.QRCodeCreator;
import de.nimple.services.nimplecode.VCardHelper;
import de.nimple.services.upgrade.ProMenuExtender;
import de.nimple.util.DensityHelper;
import de.nimple.util.SharedPrefHelper;
import de.nimple.util.VersionResolver;
import de.nimple.util.fragment.MenuHelper;

public class NimpleCodeFragment extends Fragment implements IExportExtender, IGraphic {


    public static final NimpleCodeFragment newInstance() {
		return new NimpleCodeFragment();
	}

    private ProMenuExtender proMenuExt;
    private MultiCardBarExtender multiCardBarExt;

	private Context ctx;
	private View view;

	@InjectView(R.id.no_ncode_layout)
	RelativeLayout noNimpleCode;
	@InjectView(R.id.ncode_layout)
	RelativeLayout nimpleCode;

	@InjectView(R.id.arrow_up)
	ImageView arrowUp;

    @InjectView(R.id.ncard_name)
    TextView nCardName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = getActivity().getApplicationContext();
		view = inflater.inflate(R.layout.main_ncode_fragment, container, false);
		ButterKnife.inject(this, view);
        multiCardBarExt = new MultiCardBarExtender(this);
		EventBus.getDefault().register(this);
        onBootstrap();
		refreshUi();
        setHasOptionsMenu(true);
        proMenuExt = new ProMenuExtender(this);
        return view;
    }

    private void onBootstrap(){
        //is necassary for compatibility
        NimpleCodeHelper.initCardNameFunctionality(ctx);
        NimpleCodeHelper.update(ctx);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.code_fragment, menu);
        ProMenuExtender.registerMenu(menu,ctx);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuHelper.selectMenuItem(item, this);
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	public void onEvent(NimpleCodeChangedEvent ev) {
		refreshUi();
	}

    public void onEvent(NimpleCardChangedEvent ev) {
        NimpleCodeHelper ncode = new NimpleCodeHelper(ctx);
        nCardName.setText(ncode.holder.cardName);
    }

	public void refreshUi() {
		// if nimple code exists create and show qr code
		if (checkForNimpleCode()) {
			ImageView imgQRCode = (ImageView) view.findViewById(R.id.codeImageView);
			String ownNimpleCode = VCardHelper.getCardFromSharedPrefs(ctx);
			Bitmap bitmap = QRCodeCreator.generateQrCode(ownNimpleCode);

			// S3 and S4 get confused about displaying bitmaps without a specific density,
			// and fail to scale them properly. Setting the density to this value helps
			// (is possible ANY density helps, but haven't tested this)
			bitmap.setDensity(DisplayMetrics.DENSITY_HIGH);

			imgQRCode.setImageBitmap(bitmap);
            NimpleCodeHelper ncode = new NimpleCodeHelper(ctx);
            nCardName.setText(ncode.holder.cardName);
		}

		// if nimple code does NOT exist show "fill out nimple code text and button"
		toggleNimpleCode();
	}

	private void toggleNimpleCode() {
		if (!checkForNimpleCode()) {
			noNimpleCode.setVisibility(View.VISIBLE);
			nimpleCode.setVisibility(View.GONE);
			setCorrectMarginOfArrowUp();
		} else {
			noNimpleCode.setVisibility(View.GONE);
			nimpleCode.setVisibility(View.VISIBLE);
		}
	}

	private static boolean hasSoftNavigation(Context context) {
		return !ViewConfiguration.get(context).hasPermanentMenuKey();
	}

	private void setCorrectMarginOfArrowUp() {
		// default actionBarHeight in dp
		int actionBarHeight = 30;
		double factor = 0.5;

		if (VersionResolver.isAtLeastICS()) {
			if (hasSoftNavigation(ctx)) {
				factor = 1.5;
				actionBarHeight = 48;
			}
		}

		int arrowUpPaddingRight = (int) Math.round(actionBarHeight * factor);
		arrowUpPaddingRight = (int) DensityHelper.convertDpToPixel(arrowUpPaddingRight, ctx);

		RelativeLayout.LayoutParams layoutParams = (LayoutParams) arrowUp.getLayoutParams();
		layoutParams.setMargins(0, 0, arrowUpPaddingRight, 0);
		arrowUp.setLayoutParams(layoutParams);
	}

	private boolean checkForNimpleCode() {
		return SharedPrefHelper.getBoolean("nimple_code_init", ctx);
	}

	@Override
	public Export getExport() {
        EventBus.getDefault().post(new SharedEvent(SharedEvent.Type.Code));
		return new Export<Bitmap>(QRCodeCreator.generateQrCode(VCardHelper.getCardFromSharedPrefs(ctx)));
	}

    public View getView(){
        return this.view;
    }
}