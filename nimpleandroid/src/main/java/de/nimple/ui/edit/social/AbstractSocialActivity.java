package de.nimple.ui.edit.social;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.nimple.R;
import de.nimple.util.Lg;

/**
 * Generic social network connection class.
 *
 * @param <T> Scribe Provider API
 * @author Ben John
 */
public abstract class AbstractSocialActivity<T extends Api> extends AppCompatActivity {
    @InjectView(R.id.webview_callback)
    WebView webView;
    @InjectView(R.id.webview_progress)
    RelativeLayout progressBar;

    // vars for scribe
    Class<T> provider;
    OAuthService service;
    Token requestToken;
    String apiKey, apiSecret, apiCallUrl, apiCallback;

    // handler for callbacks
    Handler h = new Handler(Looper.getMainLooper());

    public AbstractSocialActivity(Class<T> T) {
        super();
        this.provider = T;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.webview);
        ButterKnife.inject(this);

        String name = bundle.getString("name");
        apiKey = bundle.getString("apiKey");
        apiSecret = bundle.getString("apiSecret");
        apiCallUrl = bundle.getString("apiCallUrl");
        apiCallback = bundle.getString("apiCallback");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.social_header), name));

        service = new ServiceBuilder().provider(provider).apiKey(apiKey).apiSecret(apiSecret).callback(apiCallback).build();
        initWebView();
        loadWebView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Lg.d("back");
                finish();
                return true;
            default:
                return false;
        }
    }

    protected abstract void failCallback();

    protected abstract void finishCallback(JSONObject json);

    private void doFailCallback() {
        // run on ui thread
        Runnable r = new Runnable() {
            @Override
            public void run() {
                failCallback();
            }
        };
        h.post(r);
    }

    private void doFinishCallback(final JSONObject json) {
        // run on ui thread
        Runnable r = new Runnable() {
            @Override
            public void run() {
                finishCallback(json);
            }
        };
        h.post(r);
    }

    private void initWebView() {
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);

        // webView.clearCache(true);
        // webView.clearHistory();

        // attach WebViewClient to intercept callback
        webView.setWebViewClient(new WebViewClient() {
            boolean toggleProgress = true;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (toggleProgress) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // check for our custom callback protocol otherwise use default behavior
                if (url.startsWith("oauth")) {
                    // authorization complete hide webView
                    webView.setVisibility(View.GONE);

                    Uri uri = Uri.parse(url);
                    String verifier = uri.getQueryParameter("oauth_verifier");

                    Lg.d("received verifier=" + verifier + ", proceeding...");

                    if (verifier == null || verifier.length() == 0) {
                        doFailCallback();
                        return false;
                    } else {
                        toggleProgress = false;
                        processVerifier(uri, verifier);
                        return true;
                    }
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void loadWebView() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... params) {
                String authUrl = "";
                try {
                    requestToken = service.getRequestToken();
                    authUrl = service.getAuthorizationUrl(requestToken);
                    Lg.d("received token=" + requestToken.getToken() + ", authUrl=" + authUrl);
                } catch (Exception e) {
                    Lg.e("error obtaining requestToken: " + e.toString());
                    cancel(true);
                    doFailCallback();
                }
                return authUrl;
            }

            @Override
            protected void onCancelled() {
                doFailCallback();
            }

            @Override
            protected void onPostExecute(String authUrl) {
                // send user to authorization page
                webView.loadUrl(authUrl);
            }

        }.execute();
    }

    private void processVerifier(final Uri uri, final String verifier) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                Verifier v = new Verifier(verifier);
                Token accessToken = service.getAccessToken(requestToken, v);

                // host detected from callback
                String callbackHost = apiCallback.replace("oauth://", "");
                if (uri.getHost().equals(callbackHost)) {
                    OAuthRequest req = new OAuthRequest(Verb.GET, apiCallUrl);
                    service.signRequest(accessToken, req);
                    Response response = req.send();
                    try {
                        JSONObject json = new JSONObject(response.getBody());
                        doFinishCallback(json);
                        return null;
                    } catch (JSONException e) {
                        Lg.e("could not fetch json body: " + e.toString());
                    }
                }

                doFailCallback();
                return null;
            }
        }.execute();
    }
}