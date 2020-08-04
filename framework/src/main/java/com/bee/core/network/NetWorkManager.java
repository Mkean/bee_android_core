package com.bee.core.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bee.core.network.config.okhttp.OkConfig;
import com.bee.core.network.config.ssl.SSLState;
import com.bee.core.network.config.ssl.TrustAllCerts;
import com.bee.core.network.config.ssl.TrustDoubleCerts;
import com.bee.core.network.config.ssl.TrustHostnameVerifier;
import com.bee.core.network.config.ssl.TrustSingleCerts;
import com.dianping.logan.BuildConfig;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络库初始化类
 */
public class NetWorkManager {

    private static final String TAG = NetWorkManager.class.getSimpleName();
    private static volatile NetWorkManager INSTANCE;

    private OkHttpClient.Builder okBuilder;

    private OkConfig okConfig;

    private OkHttpClient client;

    private String defaultUrl;
    private HashMap<String, Object> serviceMap;

    private HashMap<String, Retrofit> retrofitMap;

    private NetWorkManager() {
        okBuilder = new OkHttpClient.Builder();
        serviceMap = new HashMap<>();
        retrofitMap = new HashMap<>();
    }

    public static NetWorkManager getInstance() {
        if (INSTANCE == null) {
            synchronized (NetWorkManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetWorkManager();
                }
            }
        }
        return INSTANCE;
    }

    public NetWorkManager initOkHttpClient(Context context, String baseUrl) {
        if (context == null) {
            throw new IllegalStateException("context not be null");
        }
        if (!retrofitMap.isEmpty()) {
            throw new IllegalStateException("Already exists, You can initialize it once");
        }
        // 如果业务层传递OkConfig为空，使用默认OkConfig

        if (okConfig == null) {
            okConfig = OkConfig.getOkConfig();
        }
        if (okConfig.getConnectTimeOut() > 0) {
            okBuilder.connectTimeout(okConfig.getConnectTimeOut(), TimeUnit.SECONDS);
        } else {
            throw new IllegalArgumentException("connectTimeOut must be greater than 0");
        }

        if (okConfig.getWriteTimeOut() > 0) {
            okBuilder.writeTimeout(okConfig.getWriteTimeOut(), TimeUnit.SECONDS);
        } else {
            throw new IllegalArgumentException("writeTimeOut must be greater than 0");
        }

        if (okConfig.getReadTimeOut() > 0) {
            okBuilder.readTimeout(okConfig.getReadTimeOut(), TimeUnit.SECONDS);
        } else {
            throw new IllegalArgumentException("readTimeOut must be greater than 0");
        }

        if (!TextUtils.isEmpty(okConfig.getCacheDirPath())) {
            File cacheFile = new File(context.getExternalCacheDir() + okConfig.getCacheDirPath());
            if (okConfig.getCacheSize() > 1024 * 1024) {
                okBuilder.cache(new Cache(cacheFile, okConfig.getCacheSize()));
            } else {
                throw new IllegalArgumentException("cacheSize must be greater than 1M");
            }
        } else {
            throw new NullPointerException("cacheDirPath not be null");
        }

        if (okConfig.getInterceptor() != null && okConfig.getInterceptor().size() > 0) {
            for (Interceptor interceptor : okConfig.getInterceptor()) {
                if (interceptor != null) {
                    okBuilder.addInterceptor(interceptor);
                }
            }
        }

        if (okConfig.getNetworkInterceptor() != null && okConfig.getNetworkInterceptor().size() > 0) {
            for (Interceptor interceptor : okConfig.getNetworkInterceptor()) {
                if (interceptor != null) {
                    okBuilder.addNetworkInterceptor(interceptor);
                }
            }
        }


        // 添加网络日志
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(message -> {
                Log.d(TAG, message);
            });
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(logInterceptor);
        }

        if (okConfig.getSSLState() != null) {
            // 支持信任所有证书https
            if (okConfig.getSSLState() == SSLState.ALL) {
                okBuilder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
            }

            // 支持https信任指定服务器的证书---单向认证
            if (okConfig.getSSLState() == SSLState.SINGLE) {
                if (TextUtils.isEmpty(okConfig.getServerCer())) {
                    throw new IllegalArgumentException("ServerCer path not be null");
                }
                try {
                    SSLContext sslContext = new TrustSingleCerts().setCertificates(context.getAssets().open(okConfig.getServerCer()));
                    okBuilder.sslSocketFactory(sslContext.getSocketFactory(), new TrustSingleCerts());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 支持https信任指定服务器的证书，服务器同时验证客户端证书----双向认证
            if (okConfig.getSSLState() == SSLState.DOUBLE) {
                if (TextUtils.isEmpty(okConfig.getClientCer())) {
                    throw new IllegalArgumentException("ClientCer path not be null");
                }
                if (TextUtils.isEmpty(okConfig.getServerCer())) {
                    throw new IllegalArgumentException("ServerCer path bot be null");
                }

                try {
                    KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                    clientKeyStore.load(context.getAssets().open(okConfig.getClientCer()), okConfig.getClientPassword().toCharArray());
                    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    keyManagerFactory.init(clientKeyStore, okConfig.getClientPassword().toCharArray());
                    SSLContext sslContext = new TrustDoubleCerts().setCertificates(keyManagerFactory, context.getAssets().open(okConfig.getServerCer()));
                    okBuilder.sslSocketFactory(sslContext.getSocketFactory(), new TrustDoubleCerts());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            okBuilder.hostnameVerifier(new TrustHostnameVerifier());
        }

        if (okConfig.getCookiesManager(context) != null) {
            okBuilder.cookieJar(okConfig.getCookiesManager(context));
        }

        client = okBuilder.build();

        if (okConfig.getMaxRequests() > 4) {
            client.dispatcher().setMaxRequests(okConfig.getMaxRequests());
        } else {
            throw new IllegalArgumentException("maxRequests must be greater than 4");
        }
        // 保存第一次初始化时传入的 baseUrl 作为默认 url
        this.defaultUrl = baseUrl;
        // 根据传入的baseUrl构建初始化Retrofit
        createRetrofit(baseUrl);

        return INSTANCE;
    }

    /**
     * 获取全局的OkHttpClient
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        if (client == null) {
            throw new NullPointerException("You should call NetworkManager.init() first");
        }
        return client;
    }

    /**
     * 根据baseUrl构建Retrofit实例
     *
     * @param baseUrl
     */
    private Retrofit createRetrofit(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        retrofitMap.put(baseUrl, retrofit);
        return retrofit;
    }

    /**
     * 根据baseUrl 获取 Retrofit 实例
     *
     * @param baseUrl
     * @return
     */
    private Retrofit getRetrofit(String baseUrl) {
        if (baseUrl == null) {
            throw new NullPointerException("baseUrl must not be empty");
        }
        Retrofit retrofit;
        if (retrofitMap.containsKey(baseUrl)) {
            retrofit = retrofitMap.get(baseUrl);
        } else {
            retrofit = createRetrofit(baseUrl);
        }
        return retrofit;
    }


    /**
     * 通过动态代理构建业务需要的ApiService
     *
     * @param service
     * @param <T>
     * @return
     */
    protected <T> T createService(Class<T> service) {
        return createService(defaultUrl, service);
    }

    /**
     * 通过动态代理构建业务需要的ApiService
     *
     * @param baseUrl
     * @param service
     * @param <T>
     * @return
     */
    protected <T> T createService(String baseUrl, Class<T> service) {
        if (retrofitMap.isEmpty()) {
            throw new NullPointerException("You should call NetworkManager.init() first");
        }
        // 根据service从缓存中查找

        if (serviceMap.containsKey(baseUrl + service.getName())) {
            return (T) serviceMap.get(baseUrl + service.getName());
        } else {
            // 根据baseUrl获取retrofit
            Retrofit retrofit = retrofitMap.get(baseUrl);
            T t = retrofit.create(service);
            serviceMap.put(baseUrl + service.getName(), t);
            return t;
        }
    }
}
