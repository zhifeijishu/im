package com.tksflysun.hi.common;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tksflysun.hi.bean.User;
import com.tksflysun.hi.constant.HiConstants;
import com.tksflysun.hi.net.Api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static volatile Retrofit instance;
    private static volatile OkHttpClient okHttpClient;

    public static Retrofit getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance =
                            new Retrofit.Builder().baseUrl(Api.BASE_URL).client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

                }
            }
        }
        return instance;
    }

    private static OkHttpClient getOkHttpClient() {

        if (okHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (okHttpClient == null) {
                    okHttpClient =
                            new OkHttpClient.Builder().connectTimeout(15,
                                    TimeUnit.SECONDS).addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String token = "";
                            User user = HiApplication.getInstance().getUser();
                            if (user != null) {
                                token = user.getToken();
                            }
                            Log.i("请求携带token", token);
                            Request.Builder builder =
                                    chain.request().newBuilder();
                            builder.addHeader("token", token);
                            return chain.proceed(builder.build());
                        }
                    }).build();
                }
            }
        }
        return okHttpClient;
    }

    private static SSLSocketFactory getsslsocket() {

        /**try {
         SSLContext sslContext = SSLContext.getInstance("TLS");
         X509TrustManager tm = new X509TrustManager() {
        @Override public void checkClientTrusted(X509Certificate[] chain,
        String authType) throws CertificateException {

        }

        @Override public void checkServerTrusted(X509Certificate[] chain,
        String authType) throws CertificateException {

        }

        @Override public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
        }
        };
         sslContext.init(null, new TrustManager[]{tm}, null);
         return sslContext.getSocketFactory();
         } catch (Exception e) {
         e.printStackTrace();
         }
         **/
        return null;

    }
}
