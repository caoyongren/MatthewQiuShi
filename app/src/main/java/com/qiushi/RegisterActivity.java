package com.qiushi;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiushi.constant.Constant;
import com.qiushi.util.OkHttp3Utils;
import com.qiushi.util.SDCardHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {
    private Context mContext = this;
    private static final String TAG = "RegisterActivity";
    private static final String URL_BASE = "http://192.168.0.103:8080/";
    private static final String URL_POST = URL_BASE + "/MyWeb/RegServlet";
    private static final String URL_UPLOAD = URL_BASE + "/MyWeb/UploadServlet";

    private EditText editText_username;
    private EditText editText_pwd;
    private EditText editText_age;
    private TextView textView_result;
    private ImageView imageView_login;
    private Handler handler = new Handler();
    private Call<ResponseBody> call = null;
    private QiushiServerInterface serverInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_pwd = (EditText) findViewById(R.id.editText_pwd);
        editText_age = (EditText) findViewById(R.id.editText_age);
        textView_result = (TextView) findViewById(R.id.textView_result);
        imageView_login = (ImageView) findViewById(R.id.imageView_login);

        serverInterface = getQiushiServerInterface();
    }

    public void clickButton(View view) {
        String username = editText_username.getText() + "";
        String pwd = editText_pwd.getText() + "";
        String age = editText_age.getText() + "";

        final Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", pwd);
        map.put("age", age);

        //获取图片名称
        String fileUrlString = Constant.URL_DOWNLOAD_IMAGE1;
        String filename = fileUrlString.substring(fileUrlString.lastIndexOf("/") + 1);
        //从SD卡私有目录中获取图片
        String filepath = SDCardHelper.getSDCardPrivateCacheDir(mContext) + File.separator + filename;

        switch (view.getId()) {
            case R.id.button_get_reg:
                //A .GET网络访问，参数为@QueryMap
                call = serverInterface.getRegInfo(map);
                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccess() && response.body() != null) {
                            try {
                                textView_result.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "加载失败！", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "--->t = [" + t + "]");
                    }
                });
                break;
            //用法1：post提交表单
            case R.id.button_post_field:
                //A .post同步提交
                call = serverInterface.postFormFields(username, pwd, age);
                //call = serverInterface.postFormFieldMap(map);
                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccess() && response.body() != null) {
                            try {
                                textView_result.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "加载失败！", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "--->t = [" + t + "]");
                    }
                });
                break;

            //用法2：post上传文件
            case R.id.button_upload_file:
                //1、只传一个文件
                File file = new File(filepath);
                RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                //上传文件
                call = serverInterface.postUploadFile(body);
                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccess() && response.body() != null) {
                            try {
                                textView_result.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "上传失败！", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "---->failure:" + t.toString());
                    }
                });
                break;
            case R.id.button_upload_multipart:
                //2、同时上传附件及其他表单数据
                //需要上传的File对象
                File[] files = new File[]{new File(filepath)};
                String[] formFieldName = new String[]{"uploadfile"};
                //获取post网络请求的MultipartBody对象
                MultipartBody multipartBody = buildRequestBody(map, files, formFieldName);

                //上传文件
                call = serverInterface.postUploadFilesMultipartBody(multipartBody);
                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccess() && response.body() != null) {
                            try {
                                textView_result.setText(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "上传失败！", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "---->failure:" + t.toString());
                    }
                });
                break;
        }
    }

    private QiushiServerInterface getQiushiServerInterface() {
        OkHttpClient client = OkHttp3Utils.getOkHttpSingletonInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(client)
                .build();

        return retrofit.create(QiushiServerInterface.class);
    }

    /**
     * 创建post上传附件的request对象
     * Post方式提交分块请求——上传文件及其它表单数据
     *
     * @param files
     * @param formFiledName
     * @param map
     * @return
     */
    private MultipartBody buildRequestBody(Map<String, String> map, File[] files, String[] formFiledName) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //往MultipartBuilder对象中添加普通input控件的内容
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //添加普通input块的数据
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                        RequestBody.create(null, entry.getValue()));
            }
        }
        //往MultipartBuilder对象中添加file input控件的内容
        if (files != null && formFiledName != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                //添加file input块的数据
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + formFiledName[i] + "\"; filename=\"" + fileName + "\""), requestBody);
            }
        }
        //生成RequestBody对象
        return builder.build();
    }

//    private static String getMimeType(String filename) {
//        FileNameMap fileNameMap = URLConnection.getFileNameMap();
//        String contentTypeFor = fileNameMap.getContentTypeFor(filename);
//        if (contentTypeFor == null) {
//            contentTypeFor = "application/octet-stream";
//        }
//        return contentTypeFor;
//    }

}
