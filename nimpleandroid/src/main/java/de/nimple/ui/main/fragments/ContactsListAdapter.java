package de.nimple.ui.main.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.domain.Contact;
import de.nimple.events.ContactTransferredEvent;
import de.nimple.ui.contact.DisplayContactActivity;
import de.nimple.util.IntentHelper;

public class ContactsListAdapter extends ArrayAdapter<Contact> {
	private Activity act;
	private LayoutInflater inflater;

	public ContactsListAdapter(Activity act, int res, List<Contact> contacts, LayoutInflater inflater) {
		super(act, res, contacts);
		this.act = act;
		this.inflater = inflater;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		ViewHolder holder;

		if (view != null) {
			holder = (ViewHolder) view.getTag();
		} else {
			view = inflater.inflate(R.layout.single_contact, parent, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		}

		final Contact c = getItem(pos);


		holder.addContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder builder = new AlertDialog.Builder(act);
				builder.setMessage(act.getString(R.string.save_contact_question));
				builder.setCancelable(true);
				builder.setPositiveButton(act.getString(R.string.button_ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EventBus.getDefault().post(new ContactTransferredEvent());
						IntentHelper.addContactPopup(act, c);
						dialog.dismiss();
					}
				});
				builder.setNegativeButton(act.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		// display contact intent
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act.getApplicationContext(), DisplayContactActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("CONTACT_ID", c.getRowId());
				act.getApplicationContext().startActivity(intent);
			}
		});

		String name = c.getName();
		String company = c.getCompany();

		StringBuilder sb = new StringBuilder(name);
		if (company != null && company.length() != 0) {
			sb.append(" - " + company);
		}

		holder.nameField.setText(sb.toString());
        holder.nameField.setEllipsize(TextUtils.TruncateAt.END);

		final String email = c.getEmail();
		if (email != null && email.length() != 0) {
			holder.emailField.setText(email);
			holder.emailField.setVisibility(View.VISIBLE);
		} else {
			holder.emailField.setVisibility(View.GONE);
		}

        final String numberMobile = c.getTelephoneMobile();
        if (numberMobile != null && numberMobile.length() != 0) {
            holder.numberField.setText(numberMobile);
            holder.numberField.setVisibility(View.VISIBLE);
        } else {
            holder.numberField.setVisibility(View.GONE);
        }

		final int ALPHA_OFF = 20;
		final int ALPHA_ON = 255;

		holder.facebookField.setAlpha(ALPHA_OFF);
		holder.twitterField.setAlpha(ALPHA_OFF);
		holder.xingField.setAlpha(ALPHA_OFF);
		holder.linkedinField.setAlpha(ALPHA_OFF);

		if (c.getFacebookUrl() != null && c.getFacebookUrl().length() != 0) {
			holder.facebookField.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					IntentHelper.openFacebook(act, c.getFacebookId(), c.getFacebookUrl());
				}
			});
			holder.facebookField.setAlpha(ALPHA_ON);
		}

		if (c.getTwitterUrl() != null && c.getTwitterUrl().length() != 0) {
			holder.twitterField.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					IntentHelper.openTwitter(act, c.getTwitterId(), c.getTwitterUrl());
				}
			});
			holder.twitterField.setAlpha(ALPHA_ON);
		}

		if (c.getXingUrl() != null && c.getXingUrl().length() != 0) {
			holder.xingField.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					IntentHelper.openXing(act, c.getXingUrl());
				}
			});
			holder.xingField.setAlpha(ALPHA_ON);
		}

		if (c.getLinkedinUrl() != null && c.getLinkedinUrl().length() != 0) {
			holder.linkedinField.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					IntentHelper.openLinkedin(act, c.getLinkedinUrl());
				}
			});
			holder.linkedinField.setAlpha(ALPHA_ON);
		}

		return view;
	}

	static class ViewHolder {
		@InjectView(R.id.contact_name)
		TextView nameField;
		@InjectView(R.id.contact_email)
		TextView emailField;
		@InjectView(R.id.contact_number)
		TextView numberField;

		@InjectView(R.id.contact_add)
		ImageView addContact;

		@InjectView(R.id.contact_twitter)
		ImageView twitterField;
		@InjectView(R.id.contact_facebook)
		ImageView facebookField;
		@InjectView(R.id.contact_xing)
		ImageView xingField;
		@InjectView(R.id.contact_linkedin)
		ImageView linkedinField;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}
}