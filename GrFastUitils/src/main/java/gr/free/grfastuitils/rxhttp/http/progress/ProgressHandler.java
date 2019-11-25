package gr.free.grfastuitils.rxhttp.http.progress;

import android.os.Looper;
import android.os.Message;

/**
 * 上传时候进度的传输管理，由于下载的时候不分是谁的，所以做单个进度条处理
 */
public abstract class ProgressHandler extends ProgressHandlerMannager {
    //暂未定义,留用扩展
    private static final int TRANSMISSION = 0;
    protected ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());

    @Override
    public void sendMessage(ProgressBean progressBean) {
        mHandler.obtainMessage(TRANSMISSION, progressBean).sendToTarget();

    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case TRANSMISSION: {
                ProgressBean progressBean = (ProgressBean) message.obj;
                onProgress(progressBean.getBytesRead(), progressBean.getContentLength(), progressBean.isDone());
            }

        }
    }

}
