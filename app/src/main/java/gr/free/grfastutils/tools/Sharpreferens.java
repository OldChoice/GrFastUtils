package gr.free.grfastutils.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class Sharpreferens {

    private Context context;
    private SharedPreferences sp;
    private String UserName;


    public Sharpreferens(Context context) {

        this.setContext(context);
        sp = context.getSharedPreferences("GrFastUtils", Context.MODE_PRIVATE);

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUserName() {
        String UserName = sp.getString("UserName", "");
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
        sp.edit().putString("UserName", UserName).commit();
    }


}
