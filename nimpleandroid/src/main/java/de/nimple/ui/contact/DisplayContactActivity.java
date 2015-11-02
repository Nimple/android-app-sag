package de.nimple.ui.contact;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.nimple.R;
import de.nimple.dagger.BaseActivity;
import de.nimple.domain.Contact;
import de.nimple.events.ContactDeletedEvent;
import de.nimple.events.ContactTransferredEvent;
import de.nimple.events.SharedEvent;
import de.nimple.services.contacts.ContactsService;
import de.nimple.services.export.Export;
import de.nimple.services.export.IExportExtender;
import de.nimple.services.nimplecode.VCardHelper;
import de.nimple.services.upgrade.ProObservable;
import de.nimple.services.upgrade.ProVersionHelper;
import de.nimple.util.IntentHelper;
import de.nimple.util.Lg;
import de.nimple.util.fragment.MenuHelper;

public class DisplayContactActivity extends BaseActivity implements IExportExtender {
	@Inject
	ContactsService contactsService;
	private Context ctx;
	private Contact contact;

	// clickable intents
	@InjectView(R.id.mailTextView)
	TextView mailTextView;
	@InjectView(R.id.phoneHomeTextView)
	TextView phoneHomeTextView;
	@InjectView(R.id.phoneMobileTextView)
	TextView phoneMobileTextView;

	// created / notes
	@InjectView(R.id.contact_created)
	TextView createdText;
	@InjectView(R.id.notesText)
	TextView notesText;

	// contact details
	@InjectView(R.id.nameTextView)
	TextView nameTextView;
	@InjectView(R.id.companyTextView)
	TextView companyTextView;
	@InjectView(R.id.jobTextView)
	TextView jobTextView;
	@InjectView(R.id.websiteTextView)
	TextView websiteTextView;
	@InjectView(R.id.addressTextView)
	TextView addressTextView;

	// social icons
	@InjectView(R.id.facebookRoundIcon)
	ImageView facebookImageView;
	@InjectView(R.id.twitterRoundIcon)
	ImageView twitterImageView;
	@InjectView(R.id.xingRoundIcon)
	ImageView xingImageView;
	@InjectView(R.id.linkedinRoundIcon)
	ImageView linkedinImageView;

	// social details
	@InjectView(R.id.facebookProfile)
	TextView facebookProfile;
	@InjectView(R.id.twitterProfile)
	TextView twitterProfile;
	@InjectView(R.id.xingProfile)
	TextView xingProfile;
	@InjectView(R.id.linkedinProfile)
	TextView linkedinProfile;

    @InjectView(R.id.contact_export_button)
    Button btExport;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.display_contact_screen);
		setProgressBarIndeterminateVisibility(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ButterKnife.inject(this);

		ctx = getApplicationContext();

		long contactId = getIntent().getLongExtra("CONTACT_ID", -1);
		contact = contactsService.findContactById(contactId);

		getActionBar().setTitle("");

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		fillUi();

        checkIsPro();
	}

    private void checkIsPro(){
        if(!ProVersionHelper.getInstance(ctx).IsPro()) {
            btExport.setVisibility(View.GONE);
        }
    }

	private void save() {
		contact.setNote(notesText.getText().toString());
		contactsService.update(contact);
	}

    public void share() {
        MenuHelper.export(getExport(), this);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.display_contact, menu);
		boolean ret =  super.onCreateOptionsMenu(menu);
        ProVersionHelper.getInstance(ctx).addObserver(menu.findItem(R.id.menu_share), ProObservable.State.PRO);
        ProVersionHelper.getInstance(ctx).notifyObserver();
        return ret;
	}

	@Override
	protected void onPause() {
		save();
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.menu_finish:
				finish();
				return true;
			case R.id.menu_del:
                showDeleteContact();
                return true;
            case R.id.menu_share:
                share();
                return true;
			default:
				return false;
		}
	}

	@OnClick({R.id.contact_add_button})
	public void showAddContact() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.save_contact_question));
		builder.setCancelable(true);
		builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EventBus.getDefault().post(new ContactTransferredEvent());
				IntentHelper.addContactPopup(DisplayContactActivity.this, contact);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@OnClick({R.id.contact_export_button})
	public void showExportContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.export_contact));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MenuHelper.save(getExport(), getApplication().getApplicationContext());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
	}

	//@OnClick(R.id.contact_delete)
	public void showDeleteContact() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(String.format(getString(R.string.delete_contact_dialog), contact.getName()));
		builder.setCancelable(true);
		builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				contactsService.remove(contact);
				EventBus.getDefault().post(new ContactDeletedEvent(contact));
				finish();
			}
		});
		builder.setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void fillUi() {
		nameTextView.setText(contact.getName());
		jobTextView.setText(contact.getPosition());
		companyTextView.setText(contact.getCompany());

		final String email = contact.getEmail();
		if (email != null && email.length() != 0) {
			mailTextView.setText(email);
			mailTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					IntentHelper.sendMail(DisplayContactActivity.this, contact);
				}
			});
			mailTextView.setVisibility(View.VISIBLE);
		} else {
			mailTextView.setVisibility(View.INVISIBLE);
		}

		final String number = contact.getTelephoneHome();
		if (number != null && number.length() != 0) {
			phoneHomeTextView.setText(number);
            phoneHomeTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (contact.getCreated() == 0L) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(number));
						startActivity(browserIntent);
					} else {
						IntentHelper.callContact(DisplayContactActivity.this, contact);
					}
				}
			});
            phoneHomeTextView.setVisibility(View.VISIBLE);
		} else {
            phoneHomeTextView.setVisibility(View.INVISIBLE);
		}

		final String numberMobile = contact.getTelephoneMobile();
		if (numberMobile != null && numberMobile.length() != 0) {
            phoneMobileTextView.setText(numberMobile);
			phoneMobileTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (contact.getCreated() == 0L) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(numberMobile));
						startActivity(browserIntent);
					} else {
						IntentHelper.callContact(DisplayContactActivity.this, contact);
					}
				}
			});
            phoneMobileTextView.setVisibility(View.VISIBLE);
		} else {
            phoneMobileTextView.setVisibility(View.INVISIBLE);
		}

		final String website = contact.getWebsite();
		if (website != null && website.length() != 0) {
			websiteTextView.setText(website);
			websiteTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String w = website.trim();
					if (!w.startsWith("http")) {
						w = "http://" + w;
					}
					try {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(w));
						startActivity(browserIntent);
					} catch (Exception e) {
						// app might crash if url could not be parsed
						Lg.e(e.getMessage());
					}
				}
			});
			websiteTextView.setVisibility(View.VISIBLE);
		} else {
			websiteTextView.setVisibility(View.INVISIBLE);
		}

		if (contact.hasAddress()) {
			String address = "";
			if (contact.hasStreet()) {
				address = contact.getStreet() + "\n";
			}
			if (contact.hasPostal()) {
				address += contact.getPostal() + " ";
			}
			if (contact.hasCity()) {
				address += contact.getCity();
			}
			addressTextView.setText(address);
			addressTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String uri = "http://maps.google.com/maps?saddr=" + addressTextView.getText();
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
					startActivity(intent);
				}
			});
			addressTextView.setVisibility(View.VISIBLE);
		} else {
			addressTextView.setVisibility(View.INVISIBLE);
		}

		DateFormat f = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
		String formattedDate = f.format(new Date(contact.getCreated()));

		createdText.setText(formattedDate);
		notesText.setText(contact.getNote());

		fillSocialUi();
	}

	@OnClick({R.id.facebookRoundIcon, R.id.facebookProfile})
	public void onClickFacebook() {
		if (contact.getFacebookUrl().length() != 0) {
			IntentHelper.openFacebook(this, contact.getFacebookId(), contact.getFacebookUrl());
		}
	}

	@OnClick({R.id.twitterRoundIcon, R.id.twitterProfile})
	public void onClickTwitter() {
		if (contact.getTwitterUrl().length() != 0) {
			IntentHelper.openTwitter(this, contact.getTwitterId(), contact.getTwitterUrl());
		}
	}

	@OnClick({R.id.xingRoundIcon, R.id.xingProfile})
	public void onClickXing() {
		if (contact.getXingUrl().length() != 0) {
			IntentHelper.openXing(this, contact.getXingUrl());
		}
	}

	@OnClick({R.id.linkedinRoundIcon, R.id.linkedinProfile})
	public void onClickLinkedIn() {
		if (contact.getLinkedinUrl().length() != 0) {
			IntentHelper.openLinkedin(this, contact.getLinkedinUrl());
		}
	}

	/**
	 * Cleans the social network URLs to to not contain 'https://' or 'http://' or 'www'
	 *
	 * @param urlString
	 * @return
	 */
	private String normalizeUrl(String urlString) {
		String cleanUrlString = urlString.replace("https://", "");
		cleanUrlString = cleanUrlString.replace("http://", "");
		cleanUrlString = cleanUrlString.replace("www.", "");
		return cleanUrlString;
	}

	private void fillSocialUi() {
		if (contact.getFacebookUrl().length() != 0) {
			facebookProfile.setText(normalizeUrl(contact.getFacebookUrl()));
			facebookImageView.setAlpha(255);
		} else {
			facebookImageView.setAlpha(30);
		}

		if (contact.getTwitterUrl().length() != 0) {
			twitterProfile.setText(normalizeUrl(contact.getTwitterUrl()));
			twitterImageView.setAlpha(255);
		} else {
			twitterImageView.setAlpha(30);
		}

		if (contact.getXingUrl().length() != 0) {
			xingProfile.setText(normalizeUrl(contact.getXingUrl()));
			xingImageView.setAlpha(255);
		} else {
			xingImageView.setAlpha(30);
		}

		if (contact.getLinkedinUrl().length() != 0) {
			linkedinProfile.setText(normalizeUrl(contact.getLinkedinUrl()));
			linkedinImageView.setAlpha(255);
		} else {
			linkedinImageView.setAlpha(30);
		}
	}

    @Override
    public Export getExport() {
       EventBus.getDefault().post(new SharedEvent(SharedEvent.Type.Contact));
       return new Export<String>(VCardHelper.getCardFromContact(contact, ctx));
    }
}