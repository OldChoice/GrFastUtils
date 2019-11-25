package gr.free.grfastuitils.rxhttp.http.progress;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 进度的总类,作为管理留用扩展，由于下载的时候不分是谁的，所以做单个进度条处理
 */
public abstract class ProgressHandlerMannager {

    public abstract void sendMessage(ProgressBean progressBean);

    public abstract void handleMessage(Message message);

    public abstract void onProgress(long progress, long total, boolean done);

    public static class ResponseHandler extends Handler {

        private ProgressHandlerMannager mProgressHandler;

        public ResponseHandler(ProgressHandlerMannager mProgressHandler, Looper looper) {
            super(looper);
            this.mProgressHandler = mProgressHandler;
        }

        @Override
        public void handleMessage(Message msg) {
            mProgressHandler.handleMessage(msg);
        }
    }

}
