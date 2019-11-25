package gr.free.grfastuitils.rxhttp.http.subscriber;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import gr.free.grfastuitils.rxhttp.DaoFilePahtUtils;
import okhttp3.ResponseBody;

/**
 * 类描述：用于下载时快速保存文件到某一位置
 * 创建人：guorui
 * 创建时间：2017/9/27 14:02
 * 修改人：guorui
 * 修改时间：2017/9/27 14:02
 * 修改备注：
 */

public class ResponseSaveFile {

    public void SaveFile(ResponseBody body, HttpSubscriber subscriber, String fileName) {

        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = body.byteStream();
            File file = new File(DaoFilePahtUtils.getDBPath(), fileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            subscriber.unSubscribe(); //onCompleted();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                Log.e("saveFile", e.getMessage());
            }
        }
    }


}
