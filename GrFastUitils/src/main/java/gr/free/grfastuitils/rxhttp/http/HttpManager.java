package gr.free.grfastuitils.rxhttp.http;

import gr.free.grfastuitils.rxhttp.http.basehttp.RetrofitUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 管理网络连接的，这里是输出
 */
public class HttpManager extends RetrofitUtils {

    private volatile static HttpManager instance;

    /**
     * 单例模式
     */
    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取数据后这里要返回数据，并在这里判断后返回数据,网络在子线程中,观察者在主线程中
     */
    public <T> void toSubscribe(Observable<T> o, Observer s) {
        o.subscribeOn(Schedulers.io())
                .map(new Function<T, Object>() {
                    @Override
                    public Object apply(@NonNull T t) throws Exception {
                        return t;
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
