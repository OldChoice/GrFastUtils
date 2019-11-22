package gr.free.grfastuitils.tools;


/**
 * Create by guorui on 2019/4/17
 * Last update 2019/4/17
 * Description:扫描二维码处理工具
 */
public class ScanUrlTools {

    /**
     * 字符串判断后获得序列号
     * 一种是直接序列号，另一种是截取其中一部分比如下面截取b=后面的参数
     * https://ms.s-water.cn/ewm/?a=ctsb&b=519030698
     */
    public static String getSerial(String serials) {
        //如果字符串包含类似地址部分，就截取字符串只保留b后面的字符串,否则直接返回所有
        if (serials.contains("https://")) {
            //相应匹配的如果有这段就继续截取
            if (serials.indexOf("&b=") > 0) {
                return serials.substring(serials.indexOf("&b=") + 3);
            } else {
                return serials;
            }
        } else {
            return serials;
        }
    }

}
