package de.nimple.services.common;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.events.NimpleCodeChangedEvent;
import de.nimple.services.nimplecode.NimpleCodeHelper;
import de.nimple.util.NimpleCard;

/**
 * Created by Dennis on 22.12.2014.
 */
public class MultiCardBarExtender {
    private Fragment frag;
    private Context ctx;

    public MultiCardBarExtender(Fragment frag) {
        this.frag = frag;
        ctx = frag.getActivity().getApplicationContext();
        ButterKnife.inject(this, ((IGraphic) frag).getView());
    }

    @OnClick({R.id.ncard_listShow, R.id.ncard_name})
    public void showNCardList(){
        LayoutInflater layoutInflater
                = (LayoutInflater)frag.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ListPopupWindow popupDialog = new ListPopupWindow(frag.getActivity(),null);
        ArrayAdapter<NimpleCard> adaper = new ArrayAdapter<NimpleCard>(
                frag.getActivity(),
                R.layout.cards_popup_row, R.id.textView, NimpleCodeHelper.getCards(frag.getActivity()));
        popupDialog.setAdapter(adaper);
        popupDialog.setAnchorView(frag.getActivity().findViewById(R.id.tabs));
        Display display = frag.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        popupDialog.setWidth((int)(width * 0.8));
        popupDialog.setHorizontalOffset(-10);
        final float scale = ctx.getResources().getDisplayMetrics().density;
        int pixels = (int) (40 * scale + 0.5f);
        popupDialog.setVerticalOffset(pixels);
        popupDialog.setModal(true);
        for(int i = 0; i < adaper.getCount(); i++){
            if( ((NimpleCard)adaper.getItem(i)).getId() == NimpleCodeHelper.getCurrentId()){
                //TODO
                i = adaper.getCount();
            }
        }
        popupDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NimpleCard cards = (NimpleCard) parent.getAdapter().getItem(position);
                NimpleCodeHelper.setCurrentId(cards.getId());
                ((IGraphic)frag).refreshUi();
                EventBus.getDefault().post(new NimpleCodeChangedEvent());
                popupDialog.dismiss();
            }
        });
        popupDialog.show();
    }

    @OnClick({R.id.ncard_add})
    public void addCard(){
        int id = NimpleCodeHelper.addCard(ctx);
        final NimpleCodeHelper ncode = new NimpleCodeHelper(ctx);
        ncode.setCurrentId(id);
        ((IGraphic)frag).refreshUi();
        EventBus.getDefault().post(new NimpleCodeChangedEvent());
    }

    @OnClick({R.id.ncard_del})
    public void delCard(){
        final NimpleCodeHelper ncode = new NimpleCodeHelper(ctx);
        if(ncode.holder.id != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(frag.getActivity());
            builder.setMessage(frag.getString(R.string.del_ncard_question));
            builder.setCancelable(true);
            builder.setPositiveButton(frag.getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ncode.delete(ncode.holder);
                    ncode.setCurrentId(0);
                    ((IGraphic)frag).refreshUi();
                    EventBus.getDefault().post(new NimpleCodeChangedEvent());
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(frag.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(ctx, frag.getString(R.string.form_error_card_delete_last), Toast.LENGTH_SHORT).show();
        }
    }
}
