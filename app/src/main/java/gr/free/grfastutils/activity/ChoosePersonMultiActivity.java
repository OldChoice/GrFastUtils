package gr.free.grfastutils.activity;


import android.os.Bundle;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.myview.DeletableEditText;
import gr.free.grfastuitils.myview.DividerDecoration;
import gr.free.grfastuitils.rxhttp.http.callback.OnResultCallBack;
import gr.free.grfastuitils.rxhttp.http.subscriber.HttpSubscriber;
import gr.free.grfastuitils.tools.LoadUtils;
import gr.free.grfastuitils.tools.MyToast;
import gr.free.grfastutils.R;
import gr.free.grfastutils.adapter.ChoosePersonMultiAdapter;
import gr.free.grfastutils.getdata.HttpManagerMeter;
import gr.free.grfastutils.listforjson.ChoosePersonMultiVo;
import gr.free.grfastutils.tools.Sharpreferens;
import okhttp3.MultipartBody;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Create by guorui on 2020/1/3
 * Last update 2020/1/3
 * Description:使用网络访问功能
 */
public class ChoosePersonMultiActivity extends BaseActivity implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnItemChildClickListener, BGAOnRVItemClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private Button btnSubmit;
    private BGARefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private ChoosePersonMultiAdapter choosePersonMultiAdapter;
    private String name = "";
    private SweetAlertDialog loadingDialog;
    private DeletableEditText etSearch;
    private Sharpreferens sp;

    //    是否可以加载更多
    private boolean isLoadMore = false;
    private int pagerCount = 0;
    private int allCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_person_multi);
        initValues();
        findView();
        setView();
        setListener();
        mRefreshLayout.beginRefreshing();


    }

    @Override
    public void initValues() {
        sp = new Sharpreferens(getApplicationContext());
        sp.setUserName("4441");
    }

    @Override
    public void findView() {
        ivBack = findViewById(R.id.activity_topback_submit_back);
        tvTitle = findViewById(R.id.activity_topback_submit_titile);
        btnSubmit = findViewById(R.id.activity_topback_submit_primit);
        mRefreshLayout = findViewById(R.id.choosepersionpersonmulti_refresh);
        etSearch = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.choosepersionpersonmulti_recycle);
        choosePersonMultiAdapter = new ChoosePersonMultiAdapter(recyclerView);

    }

    @Override
    public void setView() {
        tvTitle.setText("选择人员功能");

        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(ChoosePersonMultiActivity.this, true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        setRecycle();

    }

    @Override
    public void setListener() {
        ivBack.setOnClickListener(this);
        choosePersonMultiAdapter.setOnItemChildClickListener(this);
        choosePersonMultiAdapter.setOnRVItemClickListener(this);
        etSearch.setOnKeyListener(keyListener);
        btnSubmit.setOnClickListener(this);

    }

    private void setRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChoosePersonMultiActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setAdapter(choosePersonMultiAdapter);
        recyclerView.addItemDecoration(new DividerDecoration(ChoosePersonMultiActivity.this, LinearLayoutManager.VERTICAL, Color.parseColor("#f4f4f4"), 10, 0, 0));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_topback_submit_back:
                finish();
                break;
            case R.id.activity_topback_submit_primit:
                //确定选择维修人
                String names = "";
                String userids = "";
                for (ChoosePersonMultiVo.ResultInfoBean.ListBean vo : choosePersonMultiAdapter.getData()) {
                    if (vo.isChecked()) {
                        names = names + vo.getEmp_name() + ",";
                        userids = userids + vo.getEmp_id() + ",";
                    }
                }
                if (names.length() > 1) {
                    names = names.substring(0, names.length() - 1);
                    userids = userids.substring(0, userids.length() - 1);
                }
//                System.out.println(names + "---" + userids);
//                Bundle mBundle = new Bundle();
//                mBundle.putString("names", names);
//                mBundle.putString("userids", userids);
//                getIntent().putExtras(mBundle);
//                setResult(DispatchAddActivity.REQUEST_GET_REPAIR_MULTI, getIntent());
//                finish();

                break;


        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        pagerCount = 0;
        loadingDialog = new LoadUtils().showLoadingDialog(ChoosePersonMultiActivity.this, loadingDialog);
        HttpManagerMeter.getChoosePersonMuiltiList(getParams(), new HttpSubscriber<ChoosePersonMultiVo>(new OnResultCallBack<ChoosePersonMultiVo>() {
            @Override
            public void onSuccess(ChoosePersonMultiVo tb) {
                loadingDialog.dismiss();
                mRefreshLayout.endRefreshing();
                if (!tb.isIsSuccess()) {
                    MyToast.showShort(tb.getMsg());
                } else {
                    allCount = tb.getResultInfo().getPageCount() / 11;
                    if (allCount == pagerCount) {
                        pagerCount = 0;
                        isLoadMore = false;
                    } else {
                        pagerCount = 1;
                        isLoadMore = true;
                    }
                    choosePersonMultiAdapter.setData(tb.getResultInfo().getList());
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                loadingDialog.dismiss();
                System.out.println("失败:" + "onError: code:" + code + "  errorMsg:" + errorMsg);
                mRefreshLayout.endRefreshing();
            }

        }));

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        mRefreshLayout.endLoadingMore();
        if (!isLoadMore) {
            MyToast.showShort("已加载全部数据");
            return false;
        }
        loadingDialog = new LoadUtils().showLoadingDialog(ChoosePersonMultiActivity.this, loadingDialog);
        HttpManagerMeter.getChoosePersonMuiltiList(getParams(), new HttpSubscriber<ChoosePersonMultiVo>(new OnResultCallBack<ChoosePersonMultiVo>() {
            @Override
            public void onSuccess(ChoosePersonMultiVo tb) {
                loadingDialog.dismiss();
                mRefreshLayout.endLoadingMore();
                if (!tb.isIsSuccess()) {
                    MyToast.showShort(tb.getMsg());
                } else {
                    allCount = tb.getResultInfo().getPageCount() / 11;
                    if (allCount == pagerCount) {
                        isLoadMore = false;
                    } else {
                        pagerCount++;
                        isLoadMore = true;
                    }
                    choosePersonMultiAdapter.addMoreData(tb.getResultInfo().getList());
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
                loadingDialog.dismiss();
                System.out.println("失败:" + "onError: code:" + code + "  errorMsg:" + errorMsg);
                mRefreshLayout.endLoadingMore();
            }

        }));

        return true;
    }

    /**
     * 搜索触发事件
     */
    private View.OnKeyListener keyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                name = etSearch.getText().toString();
                pagerCount = 0;
                mRefreshLayout.beginRefreshing();
                clearInputMethod();
            }
            return false;
        }
    };

    /**
     * 让软键盘消失
     */
    protected void clearInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private List<MultipartBody.Part> getParams() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        builder.addFormDataPart("isearch", sp.getUserName() + "");
        builder.addFormDataPart("selDept", "");
        builder.addFormDataPart("pageIndex", pagerCount + 1 + "");
        builder.addFormDataPart("pageSize", "10");

        return builder.build().parts();
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {

        if (childView.getId() == R.id.choosepersonmulti_item_cb_select) {
            //点击CheckBox后设置当前为是否点击选中状态
            if (choosePersonMultiAdapter.getItem(position).isChecked()) {
                choosePersonMultiAdapter.getItem(position).setChecked(false);
            } else {
                choosePersonMultiAdapter.getItem(position).setChecked(true);
            }
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    }

}
