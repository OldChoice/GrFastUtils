package gr.free.grfastuitils.rxhttp.http.basehttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gr.free.grfastuitils.GrUtilsInstance;
import gr.free.grfastuitils.rxhttp.DaoFilePahtUtils;
import gr.free.grfastuitils.rxhttp.http.cookie.PersistentCookieStore;
import gr.free.grfastuitils.rxhttp.http.progress.ProgressResponseBody;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 网络框架的访问缓存过滤等（包括过滤进度），设置缓存地址超时等等详细参数，这里是输入第二层框架
 */
public class OkHttp3Utils {

    private static OkHttpClient mOkHttpClient;

    //设置缓存目录
    private static File cacheDirectory = new File(DaoFilePahtUtils.getDBPath(), "NetCache");
    //控制缓存的大小,如果没有缓存对于现在也没有什么影响，如果以后要做图片等缓存下载，如果短时间没问题就直接缓存
    private static Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);

    /**
     * 获取OkHttpClient对象
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {

        if (null == mOkHttpClient) {

            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //设置一个自动管理cookies的管理器
                    .cookieJar(new CookiesManager())
                    //添加拦截器
                    //  .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))//打印获取值日志等，下面自己过滤了
                    .addInterceptor(baseInterceptor)//获取本地缓存还是直接读取网络数据的判断
                    .addNetworkInterceptor(rewriteCacheControlInterceptor)//有网络时网络多久间隔访问网络读取缓存
                    //设置请求读写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
//                    .cache(cache)//以后如果使用缓存机制，很久更新一次的再打开并使用Get进行
                    .build();
        }

        return mOkHttpClient;
    }

    /**
     * 在没有网络的时候直接读取本地缓存,只有get才能使用本地缓存功能
     */
    private static Interceptor baseInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!isNetworkReachable(GrUtilsInstance.getInstance().getApplicationContext())) {
                /**
                 * 离线缓存控制  总的缓存时间=在线缓存时间+设置离线缓存时间
                 */
                int maxStale = 60 * 60 * 24 * 30; // 离线时缓存保存30天,单位:秒
                CacheControl tempCacheControl = new CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(maxStale, TimeUnit.SECONDS)
                        .build();
                request = request.newBuilder()
                        .cacheControl(tempCacheControl)
                        .build();
            }
            // response用于访问，使用几次chain.proceed(request)就直接访问几次服务器
            Response response = chain.proceed(request);
            //添加打印服务器返回的数据
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            BufferedSource source = responseBody.source();
            source.request(Integer.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            //让服务器返回参数访问地址和返回数据
            if (contentLength != 0) {
                String s = buffer.clone().readString(Charset.forName("UTF-8"));
//                Log.d("网络返回数据", "response返回参数" + response.toString() + "\n" +
//                        "服务器返回数据" + buffer.clone().readString(Charset.forName("UTF-8")));
                try {
                    JSONObject dataJson = new JSONObject(s);
                    Log.d("网络返回数据", "response返回参数" + response.toString() + "\n" +
                            "服务器返回数据" + dataJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    };
    /**
     * 设置有网络的时候,与上一次多久访问一次网络，这里设置1秒还能防止多次点击操作浪费流量
     * <li/>添加拦截器，自定义ResponseBody，添加下载进度
     */
    private static Interceptor rewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            int maxAge = 1; // 在线缓存在1秒内可读取 单位:秒,从网络获取数据两次之间间隔时间

            return originalResponse.newBuilder().body(
                    new ProgressResponseBody(originalResponse.body()))//网络访问时添加过滤并输出进度
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();

        }
    };

    /**
     * 自动管理Cookies
     */
    private static class CookiesManager implements CookieJar {
        private final PersistentCookieStore cookieStore = new PersistentCookieStore(GrUtilsInstance.getInstance().getApplicationContext());

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }

    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    public static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }
}
