package com.example.internetapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Network;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private PopupMenu popupMenu;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.swiperefresh);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient());
        Button btnFetch = findViewById(R.id.downloadBtn);
        popupMenu = new PopupMenu(this, findViewById(R.id.button3));
        popupMenu.inflate(R.menu.popupmenu);

        btnFetch.setOnClickListener(v -> {
            if (btnFetch.getText().equals("START"))

                btnFetch.setText("На главную");
            new PageLoader(findViewById(R.id.downloadBtn), findViewById(R.id.linearLayout), webView).start();
        });
        refreshLayout.setOnRefreshListener(
                () -> {
                    refreshLayout.setRefreshing(false);
                    if (btnFetch.getText().equals("START"))
                        Toast.makeText(MainActivity.this.getApplicationContext(),
                                "Обновление невозможно", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(MainActivity.this.getApplicationContext(),
                                "Обновляю...", Toast.LENGTH_SHORT).show();
                        webView.reload();
                    }
                }
        );
    }

    public void back(View v) {
        webView.goBack();
    }

    public void forward(View v) {
        webView.goForward();
    }

    public void showPopupMenu(View v) {
        popupMenu
                .setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu1:
                            Toast.makeText(MainActivity.this.getApplicationContext(),
                                    "Обновление...", Toast.LENGTH_SHORT).show();
                            webView.reload();
                            return true;

                        case R.id.menu3:
                            String url = webView.getUrl();
                            ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("", url);
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(MainActivity.this.getApplicationContext(),
                                    "URL " + url + " скопирован в буфер обмена", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.menu4:
                            webView.clearCache(true);
                            return true;
                        default:
                            return false;
                    }
                });
        popupMenu.show();
    }
}