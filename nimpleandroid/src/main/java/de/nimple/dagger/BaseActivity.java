package de.nimple.dagger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.config.Config;
import de.nimple.events.NoOpEvent;
import de.nimple.events.PurchasedEvent;
import de.nimple.services.upgrade.ProVersionHelper;
import de.nimple.ui.pro.ProActivatedActivity;
import de.nimple.util.Lg;

public abstract class BaseActivity extends Activity  implements BillingProcessor.IBillingHandler {
	@Inject
	EventBus eventBus;
    public Context ctx;
    public BillingProcessor billing;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((DaggerApplication) getApplication()).inject(this);
		eventBus.register(this);
        ctx = getApplicationContext();
        billing = new BillingProcessor(this, Config.GOOGLE_LICENSE_KEY, this);
	}

	@Override
	protected void onDestroy() {
		eventBus.unregister(this);
        if (billing != null)
            billing.release();
		super.onDestroy();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ButterKnife.inject(this);
	}

	public void onEvent(NoOpEvent ev) {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (!billing.handleActivityResult(requestCode, resultCode, intent))
            super.onActivityResult(requestCode, resultCode, intent);
    }

    // IBillingHandler implementation

    @Override
    public void onBillingInitialized() {
        /*
         * Called then BillingProcessor was initialized and its ready to purchase
         */
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called then requested PRODUCT ID was successfully purchased
         */
        Lg.d("Successfully purchased");
        ProVersionHelper.getInstance(ctx).setPro(true);
        Intent intent = new Intent(ctx, ProActivatedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
        EventBus.getDefault().post(new PurchasedEvent());
        finish();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called then some error occured. See Constants class for more details
         */
        Toast.makeText(ctx, ctx.getString(R.string.pro_purchase_err), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called then purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
        ProVersionHelper.getInstance(ctx).setPro(billing.isPurchased(Config.GOOGLE_PRODUCT_ID));
    }
}
