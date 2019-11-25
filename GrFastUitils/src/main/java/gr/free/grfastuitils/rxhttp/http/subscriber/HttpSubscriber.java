package gr.free.grfastuitils.rxhttp.http.subscriber;

import com.google.gson.stream.MalformedJsonException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import gr.free.grfastuitils.rxhttp.http.callback.OnResultCallBack;
import gr.free.grfastuitils.rxhttp.http.exception.ApiException;
import gr.free.grfastuitils.rxhttp.http.progress.ProgressHandler;
import gr.free.grfastuitils.rxhttp.http.progress.ProgressRequestBody;
import gr.free.grfastuitils.rxhttp.http.progress.ProgressResponseBody;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;

/**
 * 继承观察者,并且自己给出接口输出成功或者失败的数据
 */
public class HttpSubscriber<T> implements Observer<T> {
    private OnResultCallBack mOnResultListener;
    private Disposable mDisposable;

    public HttpSubscriber(OnResultCallBack listener) {
        this.mOnResultListener = listener;
        // 为了统一我把所有数据集中发送都集中到这个里面,其实这个进度值在拦截器那里已经获取到了,这里也只是拦截器不断传过来的而已
        ProgressRequestBody.setProgressHandler(new ProgressHandler() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
//                Log.d("progress", "上传" + progress + "---" + total + done);
//                mOnResultListener.onProgress(progress, total, done);
            }
        });
        ProgressResponseBody.setProgressHandler(new ProgressHandler() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
//                Log.d("progress", "下载" + progress + "---" + total + done);
//                mOnResultListener.onProgress(progress, total, done);
            }
        });

    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        if (mOnResultListener != null) {
            mOnResultListener.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof CompositeException) {
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    mOnResultListener.onError(ApiException.Code_TimeOut, ApiException.SOCKET_TIMEOUT_EXCEPTION);
                } else if (throwable instanceof ConnectException) {
                    mOnResultListener.onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof UnknownHostException) {
                    mOnResultListener.onError(ApiException.Code_UnConnected, ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof MalformedJsonException) {
                    mOnResultListener.onError(ApiException.Code_MalformedJson, ApiException.MALFORMED_JSON_EXCEPTION);
                }
            }
        } else {
            String msg = e.getMessage();
            int code;
            if (msg.contains("#")) {
                code = Integer.parseInt(msg.split("#")[0]);
                mOnResultListener.onError(code, msg.split("#")[1]);
            } else {
                code = ApiException.Code_Default;
                mOnResultListener.onError(code, msg);
            }
        }
    }

    @Override
    public void onComplete() {
        unSubscribe();
    }

    /**
     * 这个地方我可以在做下载什么的时候让它提前结束掉这个线程,一般的时候可以不用它
     */
    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
