package com.qiushi;

import com.qiushi.model.QiushiModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by steven on 16/3/29.
 */
public interface QiushiServerInterface {
    /**
     * 通过注解设置请求头
     *
     * 通常:
     *   添加一个Header, 添加一个Query关键字, 访问API返回的数据格式:如下
     *
     * @return
     */
    @Headers({
            "Cache-Control: max-age=640000",
            "User-Agent: Mozilla/5.0 (Windows NT 6.1;"
    })

    ///////////////////////////////////////////////////////////////////////////
    // GET网络请求方式
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 作用：GET请求最简单的写法,无Path参数和Query参数
     * @param type
     * @param page
     * @return
     */
    @GET("article/list/latest?page=1")
    Call<ResponseBody> getLatestString();

    /**
     * 作用：GET请求，指定Path参数和Query参数
     *
     * @param type
     * @param page
     * @return
     *
     * 应用:
     *   call_qiushi = serverInterface.getLatestList("latest", curPage);
     */
    @GET("article/list/{type}?")
    Call<QiushiModel> getLatestList(@Path("type") String type, @Query("page") int page);


    @GET("http://img.265g.com/userup/1201/201201071126534773.jpg")
    Call<ResponseBody> getNetworkData();

    /**
     * 作用：GET请求提交数据
     *
     * @return
     */
    @GET("MyWeb/RegServlet")
    Call<ResponseBody> getRegInfo(@QueryMap Map<String, String> map);

    ///////////////////////////////////////////////////////////////////////////
    // Post网络请求方式
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 作用：POST请求，向服务器提交表单
     *
     * @param username
     * @param password
     * @param age
     * @return
     */
    @FormUrlEncoded
    @POST("MyWeb/RegServlet")
    Call<ResponseBody> postFormFields(@Field("username") String username,
                                      @Field("password") String password,
                                      @Field("age") String age);


    /**
     * 作用：POST请求，向服务器提交表单
     *
     * @param options
     * @return
     */
    @FormUrlEncoded
    @POST("MyWeb/RegServlet")
    Call<ResponseBody> postFormFieldMap(@FieldMap Map<String, String> options);


    /**
     * 作用：POST请求，向服务器上传文件
     * <p/>
     * 服务端将接收到如下字符串：
     * {uploadfile=[name=myimg.png,
     * StoreLocation=/var/folders/_q/kb5yz2f92nl8_t9ssplj92h40000gn/T/upload_61fb99e9_6ea9_4832_96b9_65bc77578c10_00000000.tmp,
     * size=23338 bytes, isFormField=false, FieldName=uploadfile]}
     *
     * @param
     * @return
     */
    @Multipart
    @POST("MyWeb/UploadServlet")
    Call<ResponseBody> postUploadFile(@Part(value = "uploadfile\";filename=\"myimg.png") RequestBody requestBody);
    //Call<ResponseBody> postUploadFile2(@Part("uploadfile\"; filename=\"image.png") RequestBody requestBody);


    /**
     * 作用：POST请求，向服务器上传文件及其他表单域数据
     * 通过@body作为参数来上传
     *
     * @param multipartBody MultipartBody包含多个Part
     */
    @POST("MyWeb/UploadServlet")
    Call<ResponseBody> postUploadFilesMultipartBody(@Body MultipartBody multipartBody);
}
