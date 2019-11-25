package gr.free.grfastutils.getdata;


import java.util.List;

import gr.free.grfastutils.listforjson.ChoosePersonMultiVo;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 指定地址参数获取
 */
public interface ApiService {
    /**
     * 获取选择维修人多选列表
     */
    @Multipart
    @POST("api/RiZhi/GetSelUserList")
    Observable<ChoosePersonMultiVo> getChoosePersonMuiltiLists(@Part List<MultipartBody.Part> list);



}
