package de.nimple.services.upgrade;

import android.app.Fragment;
import android.content.Context;
import android.view.Menu;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.nimple.R;
import de.nimple.services.common.IGraphic;

/**
 * Created by Dennis on 22.12.2014.
 */
public class ProMenuExtender {

    private ProVersionHelper proHelp;
    private Context ctx;

    @InjectView(R.id.card_dropdown)
    LinearLayout linearLayout;

    public ProMenuExtender(Fragment frag){
        ctx = frag.getActivity().getApplicationContext();
        ButterKnife.inject(this, ((IGraphic) frag).getView());
        proHelp = ProVersionHelper.getInstance(ctx);
        proHelp.addObserver(linearLayout, ProObservable.State.PRO);
        ProVersionHelper.getInstance(ctx).notifyObserver();
    }

    public static void registerMenu(Menu menu, Context ctx){
        ProVersionHelper proHelp = ProVersionHelper.getInstance(ctx);
        proHelp.addObserver(menu.findItem(R.id.menu_export), ProObservable.State.PRO);
        proHelp.addObserver(menu.findItem(R.id.menu_save), ProObservable.State.PRO);
        proHelp.addObserver(menu.findItem(R.id.menu_proVersion), ProObservable.State.BASIC);
        proHelp.addObserver(menu.findItem(R.id.menu_proVersion2), ProObservable.State.BASIC);
        ProVersionHelper.getInstance(ctx).notifyObserver();
    }
}
