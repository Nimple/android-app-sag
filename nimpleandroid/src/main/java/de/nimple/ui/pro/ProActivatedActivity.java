package de.nimple.ui.pro;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.nimple.R;
import de.nimple.dagger.BaseActivity;

/**
 * Created by dennis on 19.10.2014.
 */
public class ProActivatedActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_activated);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.nimple_button_buy_pro)
    public void purchasedProduct() {
        finish();
    }
}
