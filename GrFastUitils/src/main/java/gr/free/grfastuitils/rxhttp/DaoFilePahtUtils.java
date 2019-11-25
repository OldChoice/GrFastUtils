package gr.free.grfastuitils.rxhttp;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import gr.free.grfastuitils.GrUtilsInstance;
import gr.free.grfastuitils.tools.PackageUtils;

/**
 * Create by guorui on 2019/11/25
 * Last update 2019/11/25
 * Description:获取文件或文件夹生成路径
 */
public class DaoFilePahtUtils {
    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().toString();
        } else {
            return "";
        }
    }

    public static String getDBPath() {
        String sdCardPath = getSDPath();
        if (TextUtils.isEmpty(sdCardPath)) {
            return "";
        } else {
            //写数据库基础路径(后面添加当前APP的名字)
            return sdCardPath + File.separator + PackageUtils.getAppName(GrUtilsInstance.getInstance());
        }
    }
}
