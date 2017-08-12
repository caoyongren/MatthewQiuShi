package com.qiushi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qiushi.adapter.NewAdapter;
import com.qiushi.constant.Constant;
import com.qiushi.model.QiushiModel;
import com.qiushi.util.OkHttp3Utils;
import com.qiushi.util.SDCardHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * 　　之后增加　－－＞　
 * 　　１．封装Ｌｏｇ
 *    2. 封装　Toast
 *    3. 美化和优化
 *    ４．提交第一版
 *    ５．换recyclerView
 *    6. javaRx 2.0
 *    7. mvp
 *    8 mvvm
 *    9.继续封装
 */

public class MainActivity extends AppCompatActivity {

    private Context mContext = this;
    private int curpage = 1;
    private ProgressBar mProBarMain;
    private NewAdapter mNewAdapterMain = null;
    private TextView mTextViewEmptyMain;
    private ListView mListViewMain;
    private List<QiushiModel.ItemsBean> mTotalist = new ArrayList<>();
    private Call<QiushiModel> mCallQiu = null;
    private Call<ResponseBody> mCallBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initRetrofit();
        initData();
    }

    private void initRetrofit() {
        /**
         *  第一步:
         *    创建Retrofit对象.
         *      1. 需要client
         *      2. converter-gson
         * */
        OkHttpClient client = OkHttp3Utils.getOkHttpSingletonInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL_BASE)
                /**
                 * 使用:GsonConverterFactory:
                 *  compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta4'
                 * */
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        /**
         * 第二步:
         *   创建API的请求:
         *     1. 创建 服务接口
         *     2. 建立关联
         *
         * */
        QiushiServerInterface serverInterface = retrofit.create(QiushiServerInterface.class);
        mCallQiu = serverInterface.getLatestList("latest", curpage);
        mCallBitmap = serverInterface.getNetworkData();
    }

    private void initView() {
        mListViewMain = (ListView) findViewById(R.id.listview_main);
        mTextViewEmptyMain = (TextView) findViewById(R.id.textView_empty);
        mProBarMain = (ProgressBar) findViewById(R.id.progressBar_main);
        mNewAdapterMain = new NewAdapter(mTotalist, mContext);
        mListViewMain.setAdapter(mNewAdapterMain);
        mListViewMain.setEmptyView(mTextViewEmptyMain);
    }

    private void initData() {
        // 网络访问方式之一
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<QiushiModel> respone = mCallQiu.execute();
                    final List<QiushiModel.ItemsBean> list = respone.body().getItems();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //隐藏进度条
                            mProBarMain.setVisibility(View.GONE);
                            //adapter 执行刷新
                            if (curpage == 1) {
                                mNewAdapterMain.reloadData(list, true);
                            } else {
                                mNewAdapterMain.reloadData(list, false);
                            }
                        }
                    });
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();*/
        /**
         * 第三步:
         *   发出请求
         *
         * */
        mCallQiu.enqueue(new Callback<QiushiModel>() { //实现异步访问
            @Override
            public void onResponse(Call<QiushiModel> call, Response<QiushiModel> response) {
                mProBarMain.setVisibility(View.GONE);
                if (response.isSuccess()) {
                    QiushiModel  qiushiModel = response.body();
                    if (qiushiModel != null) {
                        int entiry = qiushiModel.getCount();
                    }
                    if (qiushiModel != null) {
                        List<QiushiModel.ItemsBean> itemsBeanList = qiushiModel.getItems();
                        Log.i("MasterMan -- > DEBUG:", "itemsBeanList: " + itemsBeanList);
                        if (curpage == 1) {
                            mNewAdapterMain.reloadData(itemsBeanList, true);
                        } else {
                            mNewAdapterMain.reloadData(itemsBeanList, false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<QiushiModel> call, Throwable t) {
                Toast.makeText(mContext, R.string.load_failture, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_register:

                break;
            case R.id.action_download:
                //做法1：采用OkHttp来加载图片
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            byte[] result = OkHttp3Utils.getBytesFromURL(mContext , Constant.URL_DOWNLOAD_IMAGE1 , null);
                            Bitmap bm = BitmapFactory.decodeByteArray(result, 0, result.length);
                            //将bm保存进SD卡
                            String fileName = Constant.URL_DOWNLOAD_IMAGE1.substring(Constant.URL_DOWNLOAD_IMAGE1.lastIndexOf("/") + 1);
                            final boolean flag = SDCardHelper.saveFileToSDCardPrivateCacheDir(result, fileName, mContext);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "结果：" + flag, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();*/

                //采用Retrofit
                mCallBitmap.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //获取图片名称
                        String fileUriString = Constant.URL_DOWNLOAD_IMAGE;
                        String fileName = fileUriString.substring(fileUriString.lastIndexOf("/"));
                        //将下载的文件保存到ＳＤ卡
                        final boolean flag;
                        try {
                            flag = SDCardHelper.saveFileToSDCardPrivateCacheDir(response.body().bytes(), fileName, mContext);
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
