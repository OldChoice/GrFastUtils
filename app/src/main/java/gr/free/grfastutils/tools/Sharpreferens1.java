package gr.free.grfastutils.tools;

import gr.free.grfastuitils.tools.SharedPreferencesHelper;

public class Sharpreferens1 {

    private static SharedPreferencesHelper sp = new SharedPreferencesHelper("GrFastUtils");
    private static String UserName;

    public static String getUserName() {
        return sp.get(UserName, "");
    }

    public static void setUserName(String userName) {
        sp.put(UserName, userName);
    }

}
