package gr.free.grfastuitils.tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 图像处理工具类
 *
 * @author WGC 2014-03-20 头像是84*84 166*166 数据库最多限定多少K
 */
public class ImageHelper {
    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     */
    public static Bitmap getSmallBitmap(String filePath, int w, int h) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, w, h);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 质量压缩
     *
     * @param image 传入位图
     * @param kb    图片限制大小（单位KB）
     * @return Bitmap 返回位图
     */
    public static Bitmap compressImage(Bitmap image, int kb) {
        Log.v("Image", "质量压缩图片：" + kb);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = kb / (baos.toByteArray().length / 1024) * 100;
        if (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于px
            // kb,大于继续压缩
            float op = (baos.toByteArray().length) / 1024;
            op = kb / op;
            op = op * 100;
            options = (int) op;
            Log.v("Image", "百分比：" + op);
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            // options -= 10;//每次都减少5
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 按比例压缩
     *
     * @param srcPath 图片路径
     * @param height  最大高度
     * @param width   最大宽度
     * @return Bitmap 位图
     */
    public static Bitmap compressImagePx(String srcPath, float height, float width) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float height = 800f;//这里设置高度
        // float width = 480f;//这里设置宽度
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / width);
        } else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        int i = baos.toByteArray().length / 1024;
        i = i++;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            i = baos.toByteArray().length / 1024;
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options > 10) {
                options -= 10;// 每次都减少10
            } else if (options > 0) {
                options -= 1;
            } else {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        baos.reset();
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 读取图片(以文件流方式读取)
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getBitmapFromFile(String srcPath) {
        Bitmap bm;
        FileInputStream fis;
        try {
            fis = new FileInputStream(srcPath);
            bm = BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
            return bm;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按比例压缩图片
     *
     * @param image  位图
     * @param height 最大高度
     * @param width  最大宽度
     * @return Bitmap 位图
     */
    public static Bitmap compressImagePx(Bitmap image, float height, float width) {
        Log.v("Image", "比例像素压缩图片：" + height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, 90, baos);// 这里压缩90%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = height;// 这里设置高度为800f
        float ww = width;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;
    }

    /**
     * 拉伸（缩放）图片
     *
     * @param bitmap
     * @param w      宽
     * @param h      高
     * @return
     */
    public static Bitmap extrudeImage(Bitmap bitmap, int w, int h) {
        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, w, h, true);
        return bitmap2;
    }

    /**
     * 放大或缩小(裁剪)
     *
     * @param bm
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bm, int w, int h) {
        Bitmap BitmapOrg = bm;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
    }

    public static Bitmap resizeBitmap(Bitmap bm, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    /**
     * 保存图片
     *
     * @param bitmap 位图
     * @param file   保存地址（包含文件名）
     */
    public static boolean saveImage(Bitmap bitmap, File file) {
        // File file = new File(getExternalCacheDir()+"/big_ico.jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
            Log.v("File", file.getPath() + "创建失败" + e1.toString());
            return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            Log.v("File", file.getPath() + "打开文件流失败" + e.toString());
            return false;
        }
        if (bitmap == null) {
            return false;
        }
        CompressFormat format = file.getAbsolutePath().toLowerCase().endsWith(".jpg") ? CompressFormat.JPEG
                : CompressFormat.PNG;

        bitmap.compress(format, 100, fos);
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 保存图片到文件
     *
     * @param bitmap   图片
     * @param filePath 文件路径
     * @return 是否保存成功
     */
    public static boolean saveImage(Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        return saveImage(bitmap, file);
    }

    /**
     * 将位图转换为byte[]
     *
     * @param bitmap 位图
     * @return byte[]
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, CompressFormat format) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, 100, stream);
        return stream.toByteArray();
    }

    /**
     * 将byte[]转换为位图
     *
     * @return Bitmap 位图
     */
    public static Bitmap bytesToBitmap(byte[] b) {
        ByteArrayInputStream stream = new ByteArrayInputStream(b);
        Bitmap bit = BitmapFactory.decodeStream(stream);
        return bit;
    }

    /**
     * 将位图转换为base64编码字符串
     *
     * @param bitmap 位图
     * @return String Base64字符串
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, stream);
        byte[] b = stream.toByteArray();
        // 将图片流以字符串形式存储下来
        String bs64 = Base64.encodeToString(b, Base64.DEFAULT);
        return bs64;
    }

    /**
     * 将base64编码字符串转换为位图
     *
     * @param string Base64字符串
     * @return Bitmap 位图
     */
    public static Bitmap base64ToBitmap(String string) {
        if (string != null) {
            byte[] b = Base64.decode(string, Base64.DEFAULT);
            ByteArrayInputStream stream = new ByteArrayInputStream(b);
            Bitmap bit = BitmapFactory.decodeStream(stream);
            return bit;
        }
        return null;
    }

    /**
     * 将位图转换为base64编码字符串
     * bitmap
     * 位图
     *
     * @return String Base64字符串
     * @throws IOException
     */
    public static String fileToBase64(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] b = new byte[1024];
        String bs64 = "";
        while (fileInputStream.read(b) > 0) {
            bs64 += Base64.encodeToString(b, Base64.DEFAULT);
        }
        // 将图片流以字符串形式存储下来
        return bs64;
    }

    /**
     * 将base64编码字符串转换为位图
     *
     * @param string Base64字符串
     * @return Bitmap 位图
     */
    public static boolean base64ToFile(String string, File file) {
        if (string != null) {
            byte[] b = Base64.decode(string, Base64.DEFAULT);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(b);
                fileOutputStream.close();
                return true;
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        return false;
    }

    /**
     * 检查是否存在SDCard
     *
     * @return boolean
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查SDCard是否可读写
     *
     * @return boolean
     */
    public static boolean isReadWrite() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {// 已经插入了sd卡，并且可以读写
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {// 已经插入了sd卡，但是是只读的情况
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // 其他错误的状态。外部存储设备可能在其他的装备，但是我们要知道，在这一种情况下，我们不能对其进行读写。
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    /**
     * 由图片路径生成相应的 Bitmap
     *
     * @param big_ico_path 联系人头像路径
     * @return 联系人头像的 Bitmap
     */
    public static Bitmap getBitmapFromPicturePath(String big_ico_path) {
        File file = new File(big_ico_path);
        if (file.exists()) {
            Bitmap headPicture = BitmapFactory.decodeFile(file.toString());
            return headPicture;
        }
        return null;
    }

    /**
     * 从Url获取图片
     *
     * @param url 图片的url
     * @return 下载的图片
     */
    public static Bitmap getBitmapFromUrl(String url) {
        URL u = null;
        InputStream is = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            is = u.openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return BitmapFactory.decodeStream(is);
    }

    /**
     * 从Url获取图片，并保存到文件
     *
     * @param url    图片url
     * @param saveTo 保存路径
     * @return 是否成功
     */
    public static boolean saveBitmapFromUrl(String url, String saveTo) {
        Bitmap bmp = getBitmapFromUrl(url);

        if (bmp != null) {
            return saveImage(bmp, saveTo);
        }

        return false;
    }

    /**
     * 拉伸
     *
     * @param bmp
     * @param width
     * @param height
     * @return
     */
    public static Bitmap transparentImage(Bitmap bmp, int width, int height) {
        int m_ImageWidth, m_ImageHeight;
        m_ImageWidth = bmp.getWidth();
        m_ImageHeight = bmp.getHeight();
        float fX = m_ImageWidth / width;
        float fY = m_ImageHeight / height;
        float x, y;
        int i, j;
        int[] s_px = new int[m_ImageWidth * m_ImageHeight];
        bmp.getPixels(s_px, 0, m_ImageWidth, 0, 0, m_ImageWidth, m_ImageHeight);
        int[] px = new int[width * height];
        for (j = 0; j < height; j++) {
            for (i = 0; i < width; i++) {

                float point = i * fX + (j * fY) * m_ImageWidth;
                int pointt = (int) point;
                // Log.v("Image", "Point:"+point+" pointt:"+pointt);
                int temp = s_px[pointt];
                px[i + j * width] = temp;
                // Log.v("Image", "temp:"+temp);
            }
        }
        bmp.setPixels(px, 0, width, 0, 0, width, height);

        return bmp;

    }

    /**
     * public static boolean save(Bitmap bitmap, File file) { try { if
     * (file.exists()) { file.delete(); } file.createNewFile(); FileOutputStream
     * fileOutputStream = null; fileOutputStream = new FileOutputStream(file);
     * bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
     * fileOutputStream.close(); } catch (Exception e) { // TODO: handle
     * exception return false; } return true; }
     * <p>
     * public static boolean save(Bitmap bitmap, String file) { return
     * save(bitmap, new File(file)); }
     * <p>
     * /** 使用文件通道的方式复制文件
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */
    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();// 得到对应的文件通道
            out = fo.getChannel();// 得到对应的文件通道
            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过降低图片的质量来压缩图片
     * <p>
     * bmp
     * 要压缩的图片
     *
     * @param maxSize 压缩后图片大小的最大值,单位KB
     * @return 压缩后的图片
     */
    public static Bitmap compressByQuality(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        System.out.println("图片压缩前大小：" + baos.toByteArray().length + "byte");
        while (baos.toByteArray().length / 1024 > maxSize) {
            quality -= 10;
            baos.reset();
            bitmap.compress(CompressFormat.JPEG, quality, baos);
            System.out.println("质量压缩到原来的" + quality + "%时大小为：" + baos.toByteArray().length + "byte");
        }
        System.out.println("图片压缩后大小：" + baos.toByteArray().length + "byte");
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
        return bitmap;
    }

    /**
     * 通过降低图片的质量来压缩图片
     * <p>
     * bmp
     * 要压缩的图片
     *
     * @param maxSize 压缩后图片大小的最大值,单位KB
     * @return 压缩后的图片
     */
    public static byte[] compressSize(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        System.out.println("图片压缩前大小：" + baos.toByteArray().length + "byte");
        while (baos.toByteArray().length / 1024 > maxSize) {
            quality -= 10;
            baos.reset();
            bitmap.compress(CompressFormat.JPEG, quality, baos);
            System.out.println("质量压缩到原来的" + quality + "%时大小为：" + baos.toByteArray().length + "byte");
        }
        System.out.println("图片压缩后大小：" + baos.toByteArray().length + "byte");
        // bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
        // baos.toByteArray().length);
        return baos.toByteArray();
    }

    public static boolean compressSizeToFile(Bitmap bitmap, int maxSize, File outFile) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length / 1024 > maxSize) {
            quality -= 10;
            baos.reset();
            bitmap.compress(CompressFormat.JPEG, quality, baos);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            fileOutputStream.write(baos.toByteArray());
            fileOutputStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 通过压缩图片的尺寸来压缩图片大小
     *
     * @param pathName     图片的完整路径
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     */
    public static Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    /**
     * 通过压缩图片的尺寸来压缩图片大小
     *
     * @param bitmap       要压缩图片
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     */
    public static Bitmap compressBySize(Bitmap bitmap, int targetWidth, int targetHeight) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, opts);
        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        if (widthRatio > 1 && widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内存；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, opts);
        return bitmap;
    }

    /**
     * 通过压缩图片的尺寸来压缩图片大小，通过读入流的方式，可以有效防止网络图片数据流形成位图对象时内存过大的问题；
     * <p>
     * InputStream
     * 要压缩图片，以流的形式传入
     *
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     * @throws IOException 读输入流的时候发生异常
     */
    public static Bitmap compressBySize(InputStream is, int targetWidth, int targetHeight) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }

        byte[] data = baos.toByteArray();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        if (widthRatio > 1 && widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内存；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        return bitmap;
    }

    public static void gzipFile(String file, String zipFile) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file)); // 图片文件地址
            BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(zipFile))); // 压缩后的文件名
            System.out.println("Writing file");
            int c;
            while ((c = in.read()) != -1)
                out.write(c);
            in.close();
            out.close();
            System.out.println("Reading file");
            BufferedReader in2 = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(new FileInputStream(zipFile))));
            String s;
            while ((s = in2.readLine()) != null)
                System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getCachePath(Context context) {
        // 坚持SD卡是否存在
        if (ImageHelper.isReadWrite()) {
            // 如果存在
            return context.getExternalCacheDir();
        } else {
            // 不存在
            return context.getCacheDir();
        }
    }

    public static boolean isImage(String pathName) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        BitmapFactory.decodeFile(pathName, opts);
        if (opts.outMimeType == null)
            return false;
        return opts.outMimeType.contains("image");
    }

    /**
     * 根据uri发送指定宽度的图片
     *
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap imagezoom(Context context, Uri uri) {
        Bitmap bmp = null;
        int height;
        int width;
        try {
            BitmapFactory.Options factory = new BitmapFactory.Options();
            factory.inJustDecodeBounds = false;
            // 得到原图
            Bitmap bmp1 = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, factory);

            factory.inJustDecodeBounds = true;// 如果设置为true，允许查询图片不是按照像素分配给内存
            bmp = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, factory);
            // 原图宽高
            height = factory.outHeight;
            width = factory.outWidth;
            float zoomRatio1 = (float) width / (float) 360;
            float zoomHidth = height / zoomRatio1;
            // 得到缩放到指定宽度的图片
            bmp = extrudeImage(bmp1, 360, (int) zoomHidth);

            // bmp=compressBySize(uri.getPath(), 360, (int) zoomHidth);

            // 缩放到指定宽度的后图片的宽高
            height = bmp.getHeight();
            width = bmp.getWidth();
            System.out.println("截取前=" + height);
            System.out.println("截取前=" + width);
            if (height > 480) {
                // 开始截取的高度位置
                int beyondH = height - 480;
                bmp = Bitmap.createBitmap(bmp, 0, beyondH / 2, 360, 480);
            } else if (height < 88) {
                float zoomRatio2 = (float) 88 / (float) height;
                float zoomWidth = width * zoomRatio2;
                // 得到放大到指定高度的图片
                bmp = extrudeImage(bmp, (int) zoomWidth, 88);
                System.out.println("放大后" + bmp.getHeight());
                System.out.println("放大后" + bmp.getWidth());
                // 开始截取的宽度位置
                int beyondW = bmp.getWidth() - 360;
                bmp = Bitmap.createBitmap(bmp, beyondW / 2, 0, 360, 88);
            }
            // 最终得到的满足条件的图片的宽高
            height = bmp.getHeight();
            width = bmp.getWidth();
            System.out.println("截取后=" + height);
            System.out.println("截取后=" + width);
            // formclient_chat_item_in_picture.setImageBitmap(bmp);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * 根据Bitmap发送指定宽度的图片
     *
     * @param context uri
     * @return
     */
    public static Bitmap imagezoomByBitmap(Context context, Bitmap b) {

        final float scale = context.getResources().getDisplayMetrics().density;
        int w = (int) (180 * scale + 0.5f);
        int h = (int) (170 * scale + 0.5f);

        Bitmap bmp = null;
        int height;
        int width;
        // 得到原图
        Bitmap bmp1 = b;

        // 原图宽高
        height = bmp1.getHeight();
        width = bmp1.getWidth();
        float zoomRatio1 = (float) width / (float) w;
        float zoomHidth = height / zoomRatio1;
        // 得到缩放到指定宽度的图片
        bmp = extrudeImage(bmp1, w, (int) zoomHidth);
        // 缩放到指定宽度的后图片的宽高
        height = bmp.getHeight();
        width = bmp.getWidth();
        if (height > 480) {
            // 开始截取的高度位置
            int beyondH = height - 480;
            bmp = Bitmap.createBitmap(bmp, 0, beyondH / 2, w, 480);
        } else if (height < 88) {
            float zoomRatio2 = (float) 88 / (float) height;
            float zoomWidth = width * zoomRatio2;
            // 得到放大到指定高度的图片
            bmp = extrudeImage(bmp, (int) zoomWidth, 88);
            // 开始截取的宽度位置
            int beyondW = bmp.getWidth() - w;
            bmp = Bitmap.createBitmap(bmp, beyondW / 2, 0, w, 88);
        }
        // 最终得到的满足条件的图片的宽高
        height = bmp.getHeight();
        width = bmp.getWidth();
        // formclient_chat_item_in_picture.setImageBitmap(bmp);

        return bmp;
    }

    /***
     * 根据资源文件获取Bitmap
     *
     * @param context
     * @param drawableId
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int drawableId, int screenWidth, int screenHight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return getBitmap(bitmap, screenWidth, screenHight);
    }

    /***
     * 等比例压缩图片
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth, int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;

        // scale = scale < scale2 ? scale : scale2;

        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    /***
     * 等比例压缩图片
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmapHS(Context context, Bitmap bitmap, int screenWidth, int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = 0;
        Configuration mConfiguration = context.getResources().getConfiguration(); // 获取设置的配置信息
        int ori = mConfiguration.orientation; // 获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            scale = (float) screenHight / h;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            System.out.println("竖");
            scale = (float) screenWidth / w;
        }
        // scale = scale < scale2 ? scale : scale2;
        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    /**
     * 设置图片圆角
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 保存图片到手机相册文件夹
     *
     * @param context
     * @param path
     * @param name
     * @return
     */
    public static String saveImageToPhotoAlbum(Context context, String path, String name) {
        String fliepath;
        ContentValues values = new ContentValues(3);
        values.put(Images.Media.TITLE, name);
        values.put(Images.Media.DATA, path);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        Uri dataUri = context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
        if (dataUri == null) {
            return null;
        } else {
            fliepath = dataUri.getPath();
        }
        return fliepath;
    }

    /**
     * 把保存的图片加入到手机相册显示
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (null == c) {
            throw new IllegalArgumentException("Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() != 1) || !c.moveToFirst()) {
            } else {
                filePath = c.getString(c.getColumnIndexOrThrow(MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }

    /**
     * 保存图片到相册
     *
     * @param cxt
     * @param imagepath
     */
    public static void saveImageToPhoto(Context cxt, String imagepath) {
        try {
            ContentResolver cr = cxt.getContentResolver();

            String s = Images.Media.insertImage(cr, imagepath, "" + System.currentTimeMillis(), "");
            String s1 = ImageHelper.getFilePathByContentResolver(cxt, Uri.parse(s));
            String path = ImageHelper.saveImageToPhotoAlbum(cxt, s1, "" + System.currentTimeMillis());
            if (path != null) {
            } else {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存图片到相册
     *
     * @param cxt
     * @param imagepath
     */
    public static void saveImage(Context cxt, String imagepath) {
        String fileName = "";
        if (imagepath.contains("cache/")) {
            fileName = imagepath.substring(imagepath.indexOf("cache/") + 6, imagepath.length()) + ".jpg";
        } else {
            // MyToast.showToastLong(cxt, "保存失败");
            return;
        }
        String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
        // fileName =imagepath.substring(imagepath.indexOf("cache/"),
        // imagepath.length()) + ".jpg";
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
        ImageHelper.saveImage(bitmap, myCaptureFile);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(ALBUM_PATH + fileName));
        intent.setData(uri);
        cxt.sendBroadcast(intent);
    }

    /**
     * 保存图片到相册
     *
     * @param cxt
     * @param imagepath
     */
    public static void saveImageCamear(Context cxt, String imagepath) {
        String fileName = "";
        String ALBUM_PATH;
        if (imagepath.contains("cache/")) {
            fileName = imagepath.substring(imagepath.indexOf("cache/") + 6, imagepath.length()) + ".jpg";
        } else {
            return;
        }

        String path1 = Environment.getExternalStorageDirectory() + "/Camera/";
        File file = new File(path1);
        // 如果文件存在
        if (file.exists()) {
            ALBUM_PATH = Environment.getExternalStorageDirectory() + "/Camera/";
        } else {
            ALBUM_PATH = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
            File dirFile = new File(ALBUM_PATH);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
        ImageHelper.saveImage(bitmap, myCaptureFile);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(ALBUM_PATH + fileName));
        intent.setData(uri);
        cxt.sendBroadcast(intent);
    }

    /**
     * 获取图片缩小的图片
     *
     * @param src
     * @return
     */
    public static Bitmap scaleBitmap(String src) {
        // 获取图片的高和宽
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这一个设置使 BitmapFactory.decodeFile获得的图片是空的,但是会将图片信息写到options中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src, options);
        options.inSampleSize = 1;
        // 设置可以获取数据
        options.inJustDecodeBounds = false;
        // 获取图片
        return BitmapFactory.decodeFile(src, options);
    }

    /**
     * 添加文字到图片，类似水印文字。
     *
     * @param gContext gResId
     * @param time
     * @param address
     * @return
     */
    public static Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap, String time, String address) {

        Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Config.RGB_565;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.WHITE);
        // text size in pixels
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) gContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
        // paint.setTextSize((int) (4 * metric.density * 2));
        paint.setTextSize((int) (15));
        // //设置阴影
        // paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center

        Rect bounds = new Rect();
        if (time != null && !"".equals(time)) {
            paint.getTextBounds(time, 0, time.length(), bounds);
            // int x = (bitmap.getWidth() - bounds.width()) / 2;
            // int y = (bitmap.getHeight() + bounds.height()) / 2;
            // draw text to the bottom
            int x = (bitmap.getWidth() - bounds.width()) / 20 * 19;
            int y = (bitmap.getHeight() + bounds.height()) / 20 * 17;
            canvas.drawText(time, x, y, paint);
        }
        if (address != null && !"".equals(address)) {
            paint.getTextBounds(address, 0, address.length(), bounds);
            int x1 = (bitmap.getWidth() - bounds.width()) / 20 * 19;
            int y1 = (bitmap.getHeight() + bounds.height()) / 20 * 19;
            canvas.drawText(address, x1, y1, paint);
        }

        return bitmap;
    }

    /**
     * 图片压缩
     */
    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 如果是30
        // 是压缩率，表示压缩70%;
        // 如果不压缩是100，表示压缩率为0
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片压缩 只压缩宽高
     */
    public static Bitmap bitCompressWH(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        float ww = 540;// 这里设置宽度为450f
        float hh = 960;// 这里设置高度为800f
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        // 判断图片是横屏还是竖屏拍照然后配置相应数据,上面针对于竖屏正常拍照的,下面是针对于横屏
        if (w > h) {
            ww = 960;
            hh = 540;
        }
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            // 如果宽大于高为真，是横屏的，就以宽
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            // System.out.println(scale+"---"+log+"---"+logCeil);
            size = (int) Math.pow(2, logCeil);
        }
        // System.out.println(size + "---" + w + "---" + h + "---" + ww + "---"
        // + hh);
        opts.inSampleSize = size;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, opts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 直接压缩到50K以下，通过两种方法
     */
    public static void bitCompress(String path) {
        compressSize(bitCompressSize(path), path);
    }

    /**
     * 根据路径尺寸压缩,避免通过bitmap尺寸压缩时的大缓存
     */
    public static Bitmap bitCompressSize(String path) {
        int width = 960;
        int height = 540;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空
        int srcWidth = newOpts.outWidth;
        int srcHeigh = newOpts.outHeight;
        int srcSideMax = Math.max(srcWidth, srcHeigh);
        int sideMax = Math.max(width, height);
        int dstWidth = srcWidth > srcHeigh ? Math.max(width, height) : Math.min(width, height);
        int dstHeigh = srcHeigh > srcWidth ? Math.max(width, height) : Math.min(width, height);

        newOpts.inJustDecodeBounds = false;

        newOpts.inSampleSize = srcSideMax > sideMax ? (int) Math.rint(srcSideMax / sideMax) : 1;
        bitmap = BitmapFactory.decodeFile(path, newOpts);
        bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeigh, true);

        return bitmap;
    }

    /**
     * 修改图片质量压缩在50K以内
     */
    public static void compressSize(Bitmap bitmap, String srcPath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length > 50 * 1024) {
            // 重置数据,防止多次压缩图片质量太低
            baos.reset();
            bitmap.compress(CompressFormat.JPEG, quality, baos);
            // 控制质量大于0,其实等于0的时候就已经很小了,但是当前手机厂商定制的代码对于压缩不一样,导致即使为0压缩也大于50K
            quality -= 10;
            if (quality < 20) {
                break;
            }
            // System.out.println(baos.toByteArray().length + "----" + quality);
        }
        try {
            baos.writeTo(new FileOutputStream(srcPath));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bitmap.recycle();
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改图片质量压缩在200K以内
     */
    public static Bitmap compressSize200(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length > 50 * 1024) {
            // 重置数据,防止多次压缩图片质量太低
            baos.reset();
            bitmap.compress(CompressFormat.JPEG, quality, baos);
            // 控制质量大于0,其实等于0的时候就已经很小了,但是当前手机厂商定制的代码对于压缩不一样,导致即使为0压缩也大于50K
            quality -= 10;
            if (quality < 20) {
                break;
            }
//            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//            Bitmap bitmap1 = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        }
        return bitmap;

    }
}
