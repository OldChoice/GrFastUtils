package gr.free.grfastuitils.rxhttp.http.progress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 下载时候实体类用来计算进度数
 */
public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody ) {
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                // 如果未声明就返回0立马停止
                if (mProgressHandler == null) {
                    return 0;
                }
                progressBean.setBytesRead(totalBytesRead);
                progressBean.setContentLength(responseBody.contentLength());
                progressBean.setDone( bytesRead == -1);
                mProgressHandler.sendMessage(progressBean);


                return bytesRead;
            }
        };
    }
    private static ProgressHandlerMannager mProgressHandler;
    //    进度
    private static ProgressBean progressBean = new ProgressBean();

    public static void setProgressHandler(ProgressHandlerMannager progressHandler) {
        mProgressHandler = progressHandler;
    }
}
