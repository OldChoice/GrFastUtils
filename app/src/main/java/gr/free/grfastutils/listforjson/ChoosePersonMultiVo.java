package gr.free.grfastutils.listforjson;

import java.util.List;

/**
 * 选择维修人多选
 */

public class ChoosePersonMultiVo {

    /**
     * resultInfo : {"list":[{"emp_id":"9999","emp_name":"门卫","sex":"男","org_name":"贵州行政部","headship_name":"总裁助理","org_pj_name":"贵州//贵州行政部"},{"emp_id":"9891","emp_name":"勒长贵","sex":"男","org_name":"售后服务部","headship_name":"售后","org_pj_name":"集团//营销中心//售后服务部"},{"emp_id":"98421","emp_name":"张杰","sex":"男","org_name":"营销部","headship_name":"业务员","org_pj_name":"迪纳声//营销部"},{"emp_id":"98419","emp_name":"祖济妙","sex":"男","org_name":"营销部","headship_name":"业务员","org_pj_name":"迪纳声//营销部"},{"emp_id":"98416","emp_name":"李刚","sex":"男","org_name":"营销部","headship_name":"业务员","org_pj_name":"迪纳声//营销部"},{"emp_id":"98414","emp_name":"盛佳韡","sex":"女","org_name":"生产管理","headship_name":"文员","org_pj_name":"迪纳声//生产部//生产管理"},{"emp_id":"98408","emp_name":"吴娇","sex":"女","org_name":"营销部","headship_name":"业务员","org_pj_name":"迪纳声//营销部"},{"emp_id":"98407","emp_name":"宋昌梅","sex":"女","org_name":"营销部","headship_name":"内勤","org_pj_name":"迪纳声//营销部"},{"emp_id":"98406","emp_name":"刘啸","sex":"女","org_name":"外贸部","headship_name":"外贸专员","org_pj_name":"迪纳声//外贸部"},{"emp_id":"98405","emp_name":"骆宏玉","sex":"女","org_name":"采购部","headship_name":"采购员","org_pj_name":"迪纳声//采购部"}],"pageCount":3843}
     * msg : 查询成功
     * isSuccess : true
     * code :
     */

    private ResultInfoBean resultInfo;
    private String msg;
    private boolean isSuccess;
    private String code;

    public ResultInfoBean getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(ResultInfoBean resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class ResultInfoBean {
        /**
         * list : [{"emp_id":"9999","emp_name":"门卫","sex":"男","org_name":"贵州行政部","headship_name":"总裁助理","org_pj_name":"贵州//贵州行政部"},{"emp_id":"9891","emp_name":"勒长贵","sex":"男","org_name":"售后服务部","headship_name":"售后","org_pj_name":"集团//营销中心//售后服务部"},{"emp_id":"98421","emp_name":"张杰","sex":"男","org_name":"营销部","headship_name":"业务员","org_pj_name":"迪纳声//营销部"},{"emp_id":"98419","emp_name":"祖济妙","sex":"男","org_name":"营销部","headship_name":"业务员","org_pj_name":"迪纳声//营销部"},{"emp_id":"98416","emp_name":"李刚","sex":"男","org_name":"营销部","headship_name":"业务员","org_pj_name":"迪纳声//营销部"},{"emp_id":"98414","emp_name":"盛佳韡","sex":"女","org_name":"生产管理","headship_name":"文员","org_pj_name":"迪纳声//生产部//生产管理"},{"emp_id":"98408","emp_name":"吴娇","sex":"女","org_name":"营销部","headship_name":"业务员","org_pj_name":"迪纳声//营销部"},{"emp_id":"98407","emp_name":"宋昌梅","sex":"女","org_name":"营销部","headship_name":"内勤","org_pj_name":"迪纳声//营销部"},{"emp_id":"98406","emp_name":"刘啸","sex":"女","org_name":"外贸部","headship_name":"外贸专员","org_pj_name":"迪纳声//外贸部"},{"emp_id":"98405","emp_name":"骆宏玉","sex":"女","org_name":"采购部","headship_name":"采购员","org_pj_name":"迪纳声//采购部"}]
         * pageCount : 3843
         */

        private int pageCount;
        private List<ListBean> list;

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * emp_id : 9999
             * emp_name : 门卫
             * sex : 男
             * org_name : 贵州行政部
             * headship_name : 总裁助理
             * org_pj_name : 贵州//贵州行政部
             */

            private String emp_id;
            private String emp_name;
            private String sex;
            private String org_name;
            private String headship_name;
            private String org_pj_name;
            private boolean isChecked;

            public boolean isChecked() {
                return isChecked;
            }

            public void setChecked(boolean checked) {
                isChecked = checked;
            }

            public String getEmp_id() {
                return emp_id;
            }

            public void setEmp_id(String emp_id) {
                this.emp_id = emp_id;
            }

            public String getEmp_name() {
                return emp_name;
            }

            public void setEmp_name(String emp_name) {
                this.emp_name = emp_name;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getOrg_name() {
                return org_name;
            }

            public void setOrg_name(String org_name) {
                this.org_name = org_name;
            }

            public String getHeadship_name() {
                return headship_name;
            }

            public void setHeadship_name(String headship_name) {
                this.headship_name = headship_name;
            }

            public String getOrg_pj_name() {
                return org_pj_name;
            }

            public void setOrg_pj_name(String org_pj_name) {
                this.org_pj_name = org_pj_name;
            }
        }
    }
}
