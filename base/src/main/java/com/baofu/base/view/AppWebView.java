
package com.baofu.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.baofu.base.R;


/**
 * Created by lihaiyi on 2018/5/22.
 */

public class AppWebView extends FrameLayout {

    Activity mActivity;

    WebView mWebView;

    private static final String tag = "AppWebView";

    WebViewListener mWebViewClientListener;

    boolean mIsActivity;

    public AppWebView(Activity activity) {
        super(activity);
        init(activity);
    }

    public AppWebView(Activity activity, boolean isActivity) {
        super(activity);
        mIsActivity = isActivity;
        init(activity);
    }

    public AppWebView(Activity activity, @Nullable AttributeSet attrs) {
        super(activity, attrs);
        init(activity);
    }

    public AppWebView(Activity activity, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        init(activity);
    }

    private void init(Activity activity) {
        mActivity = activity;

        mWebView = (WebView) LayoutInflater
                .from(mIsActivity ? mActivity : mActivity.getApplicationContext())
                .inflate(R.layout.view_webview, null);
        addView(mWebView);
        initSetting();


    }

    private void initSetting() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setTextZoom(100);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //webview截全图
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        // 是否允许file:// 协议下的js跨域加载http或者https的地址。
        settings.setPluginState(WebSettings.PluginState.ON);
        // settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDefaultTextEncodingName("UTF-8");

        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        settings.setLoadsImagesAutomatically(false);
//        settings.setBuiltInZoomControls(false);

        // 用于H5识别来源，是否来自客户端还是网页
//            settings.setUserAgentString(
//                    settings.getUserAgentString() + AppUtils.getUserAgentSuffix(mactivity));
        // 设置加载进来的页面自适应手机屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);// 允许接受 Cookie
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mWebView, true); //跨域cookie读取
        }
        mWebView.addJavascriptInterface(new JavaScriptLocalObj(), "call_android");

        mWebView.setWebContentsDebuggingEnabled(true);

        // 屏蔽某些手机长按事件,复制奔溃(另一种代替方法初始化webview传getApplicationactivity()替换成NewsDetailActivity.this)
        mWebView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        mWebView.setWebViewClient(new CustomWebViewClient());
        mWebView.setWebChromeClient(new CusTomWebChromeClient());
    }


    class CusTomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (!isActivityRunning())
                return;
            if (mWebViewClientListener != null) {
                mWebViewClientListener.onProgressChanged(view, newProgress);
            }
            if(newProgress==100){
//                if (Apputils.isDebug()) {
//                    Handler handler=new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            String html =
//                                    "javascript:var script = document.createElement('script');script.src = 'https://cdn.bootcss.com/vConsole/3.2.0/vconsole.min.js';document.body.appendChild(script);";
//                            mWebView.loadUrl(html);
//                        }
//                    },4000);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            String html =
//                                    "javascript: new VConsole();";
//                            mWebView.loadUrl(html);
//                        }
//                    },6000);
//                }
            }


//            view.loadUrl("javascript:window.Android.showSource("+ "document.getElementsByTagName('html')[0].innerHTML);");
        }
    }

    private class CustomWebViewClient extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            if (!isActivityRunning())
                return true;
            if (mWebViewClientListener != null) {
                mWebViewClientListener.shouldOverrideUrlLoading(view, url);
            }
            return true;
        }


        @Override
        public void onPageFinished(WebView view, final String url) {
            super.onPageFinished(view, url);
            if (!isActivityRunning())
                return;
            if (mWebViewClientListener != null) {
                mWebViewClientListener.onPageFinished(view, url);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (!isActivityRunning())
                return;
            if (mWebViewClientListener != null) {
                mWebViewClientListener.onPageStarted(view, url, favicon);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (!isActivityRunning())
                return;
            String message = null;
            if (error != null) {
                message = error.toString();
            }
            Log.e(tag, "文章请求失败：" + message);
            if (mWebViewClientListener != null) {
                mWebViewClientListener.onReceivedError(view, message);
            }
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler,
                                       SslError error) {
            if (!isActivityRunning())
                return;
            sslHandle(view, handler, error);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          WebResourceRequest request) {
            WebResourceResponse response = null;
            if (mWebViewClientListener != null) {
                response = mWebViewClientListener.shouldInterceptRequest(view, request);
            }
            if (response != null) {
                return response;
            }
            return super.shouldInterceptRequest(view, request);
        }


    }

    public void setWebViewListener(WebViewListener mWebViewClientListener) {
        this.mWebViewClientListener = mWebViewClientListener;
    }

    public WebView getWebView() {
        return mWebView;
    }

    private boolean isActivityRunning() {

        if (mWebView == null || mActivity == null)
            return false;
//        if (mactivity instanceof Activity) {
//            if (((Activity) mactivity).isDestroyed())
//                return false;
//        }
        return true;
    }


    public interface WebViewListener {
        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, String description);

        void shouldOverrideUrlLoading(WebView view, String url);

        WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request);

        void onProgressChanged(WebView view, int newProgress);
        public void onGetHtml(String url, String html);


    }

    public static class AppWebViewListener implements WebViewListener {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public void onReceivedError(WebView view, String description) {

        }

        @Override
        public void shouldOverrideUrlLoading(WebView view, String url) {

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return null;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onGetHtml(String url,String html) {

        }

    }


     class JavaScriptLocalObj {
        @JavascriptInterface
        public void onGetHtml(String url,String html) {
            if (mWebViewClientListener != null) {
                mWebViewClientListener.onGetHtml(url,html);
            }

        }
        @JavascriptInterface
        public void showSource(String html) {
            Log.d("asdf","====>html=" + html);

        }
    }

    public void sslHandle(final WebView view, final SslErrorHandler handler, final SslError error) {


        if (view == null || mActivity == null) {
            return;
        }
        try {
            if (mActivity.isFinishing()) {
                return;
            }
        } catch (Exception e) {

        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        AlertDialog dialog = null;
        String message = "SSL Certificate error.";
        switch (error.getPrimaryError()) {
            case SslError.SSL_UNTRUSTED:
                message = "The certificate authority is not trusted.";
                break;
            case SslError.SSL_EXPIRED:
                message = "The certificate has expired.";
                break;
            case SslError.SSL_IDMISMATCH:
                message = "The certificate Hostname mismatch.";
                break;
            case SslError.SSL_NOTYETVALID:
                message = "The certificate is not yet valid.";
                break;
        }
        message += " Do you want to continue anyway?";

        builder.setTitle("SSL Certificate Error");
        builder.setMessage(message);
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (view == null || mActivity == null) {
                    return;
                }
                if (handler != null) {
                    handler.proceed();
                }
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (view == null || mActivity == null) {
                    return;
                }
                if (handler != null) {
                    handler.proceed();
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}
