package gr.free.grfastuitils.rxhttp.http.progress;

/**
 * 上传进度时候的参数,进度总长度,是否完成等
 */
public class ProgressBean {

    private long bytesRead;//进度
    private long contentLength;//总长度
    private boolean done;//是否完成

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
