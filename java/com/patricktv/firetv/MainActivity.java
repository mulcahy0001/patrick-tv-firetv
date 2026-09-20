package com.patricktv.firetv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private static final String PATRICK_TV_URL = "https://patrick-tv-hub.mulcahy001.chatgpt.site/";
    private WebView webView;
    private long lastBackPress;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enterTvMode();

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(7, 16, 31));
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setUserAgentString(settings.getUserAgentString() + " PatrickTV/1.0 FireTV");

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.requestFocus();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    view.loadDataWithBaseURL(null,
                        "<body style='margin:0;background:#07101f;color:white;font-family:sans-serif;display:grid;place-items:center;height:100vh'>" +
                        "<div style='text-align:center'><h1>Patrick TV</h1><p>Unable to connect. Check the Fire TV internet connection, then press SELECT.</p></div></body>",
                        "text/html", "UTF-8", null);
                }
            }
        });

        setContentView(webView);
        if (savedInstanceState == null) webView.loadUrl(PATRICK_TV_URL);
        else webView.restoreState(savedInstanceState);
    }

    private void enterTvMode() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    private void sendBrowserKey(String key) {
        String safeKey = key.replace("'", "\\'");
        webView.evaluateJavascript(
            "var g=document.getElementById('guideScroll');" +
            "var t=g||document;if(g){g.focus();}" +
            "t.dispatchEvent(new KeyboardEvent('keydown',{key:'" + safeKey + "',bubbles:true,cancelable:true}));",
            null
        );
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN) return super.dispatchKeyEvent(event);

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP: sendBrowserKey("ArrowUp"); return true;
            case KeyEvent.KEYCODE_DPAD_DOWN: sendBrowserKey("ArrowDown"); return true;
            case KeyEvent.KEYCODE_DPAD_LEFT: sendBrowserKey("ArrowLeft"); return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT: sendBrowserKey("ArrowRight"); return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER: sendBrowserKey("Enter"); return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE: sendBrowserKey("MediaPlayPause"); return true;
            case KeyEvent.KEYCODE_BACK:
                long now = System.currentTimeMillis();
                if (now - lastBackPress < 1500) finish();
                else {
                    lastBackPress = now;
                    sendBrowserKey("BrowserBack");
                }
                return true;
            default: return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enterTvMode();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }
}
