package com.example.internetapp;

import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PageLoader extends Thread {
        private final String siteName = "https://rutube.ru/";
    private final WebView webView;
    private View button0;
    private View buttons;

    public PageLoader(View button0, View buttons, WebView webView) {
        this.webView = webView;
        this.button0 = button0;
        this.buttons = buttons;
    }

    @Override
    public void run() {
        try {
            String content = getContent(siteName);
            webView.post(new Runnable() {
                public void run() {
                    webView.loadDataWithBaseURL(siteName, content, "text/html", "UTF-8", siteName);
                    button0.setVisibility(View.INVISIBLE);
                    buttons.setVisibility(View.VISIBLE);
                }
            });
        } catch (IOException ex) {
            Toast.makeText(webView.getContext(), "Ошибка " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getContent(String path) throws IOException {
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(path);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return (buf.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
