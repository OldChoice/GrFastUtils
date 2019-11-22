package gr.free.grfastuitils.tools;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.free.grfastuitils.R;

/**
 * 类描述：
 * 创建人：guorui
 * 创建时间：2017/8/21 09:51
 * 修改人：guorui
 * 修改时间：2017/8/21 09:51
 * 修改备注：
 */

public class LoadUtils {
    public SweetAlertDialog showLoadingDialog(Context context, SweetAlertDialog mLoadingDialog) {

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mLoadingDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.grblue));
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setTitleText("数据加载中...");
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public SweetAlertDialog showLoadingDialogLogin(Context context, SweetAlertDialog mLoadingDialog) {

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mLoadingDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.grblue));
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setTitleText("正在登录中...");
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public SweetAlertDialog showBaseDialog(Context context, SweetAlertDialog mLoadingDialog, String msg) {

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        mLoadingDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.grblue));
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setTitleText(msg);
        mLoadingDialog.show();
        return mLoadingDialog;
    }

}
