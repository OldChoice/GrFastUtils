package gr.free.grfastuitils.rxhttp.http.callback;

/**
 * 使用输入或输出成功或失败接口
 */
public interface OnResultCallBack<T> {
    /**
     * 返回的数据在OnNext中实现
     *
     * @param t 虚类,在输出的时候自己定义参数
     */
    void onSuccess(T t);

    /**
     * 无论在哪里遇到错误都会返回这种信息
     *
     * @param code     错误码
     * @param errorMsg 错误信息
     */
    void onError(int code, String errorMsg);

    /**
     * 上传下载时候的进度，由于这个框架导致做不出来多进度条，所以废弃进度接口，只是在需要进度条的地方添加方法
     * 单任务下载使用分开判断当前是下载还是上传
     *
     * @param bytesRead     进度
     * @param contentLength 文件总大小
     * @param done          是否完成
     */
//    void onProgress(long bytesRead, long contentLength, boolean done);
}
