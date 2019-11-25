package gr.free.grfastutils.adapter;

import androidx.recyclerview.widget.RecyclerView;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import gr.free.grfastutils.R;
import gr.free.grfastutils.listforjson.ChoosePersonMultiVo;

/**
 * 维修人选择多选
 */
public class ChoosePersonMultiAdapter extends BGARecyclerViewAdapter<ChoosePersonMultiVo.ResultInfoBean.ListBean> {

    public ChoosePersonMultiAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.listitem_choosepersonmulti);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, ChoosePersonMultiVo.ResultInfoBean.ListBean vo) {
        viewHolderHelper.setText(R.id.choosepersonmulti_item_tv_name, "姓名:" + vo.getEmp_name());
        viewHolderHelper.setText(R.id.choosepersonmulti_item_tv_tel, "手机号码:" + vo.getSex());
        viewHolderHelper.setChecked(R.id.choosepersonmulti_item_cb_select, vo.isChecked());

    }

    @Override
    public void setItemChildListener(BGAViewHolderHelper viewHolderHelper, int viewType) {
        viewHolderHelper.setItemChildClickListener(R.id.choosepersonmulti_item_cb_select);
    }

}