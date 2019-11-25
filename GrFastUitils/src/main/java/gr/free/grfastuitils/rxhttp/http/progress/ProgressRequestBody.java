package gr.free.grfastuitils.rxhttp.http.progress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 上传时候实体类用来计算进度数，由于下载的时候不分是谁的，所以做单个进度条处理
 */
public class ProgressRequestBody extends RequestBody {

    //实际的待包装请求体
    private final RequestBody requestBody;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {

            bufferedSink = Okio.buffer(sink(sink));
        }

        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
//                Log.d("progress", bytesWritten + "-++++===++++-" + contentLength );
                //增加当前写入的字节数
                bytesWritten += byteCount;

                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                // 如果未声明就返回立马停止
                if (mProgressHandler == null) {
                    return;
                }
                progressBean.setBytesRead(bytesWritten);
                progressBean.setContentLength(contentLength);
                progressBean.setDone(bytesWritten == contentLength);
                mProgressHandler.sendMessage(progressBean);
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
