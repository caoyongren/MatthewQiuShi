package com.qiushi.constant;

/**
 * Created by Matthew on 2015/12/8 0008.
 */
public class Constant {
    //baseurl
    public final static String URL_BASE = "http://m2.qiushibaike.com/";
    // 最新
    public final static String URL_LATEST = "http://m2.qiushibaike.com/article/list/latest?page=%d";

    // 图片
    public final static String URL_PIC= "http://m2.qiushibaike.com/article/list/pic?page=%d";

    // 视频
    public final static String URL_VIDEO = "http://m2.qiushibaike.com/article/list/video?page=%d";

    // 文本
    public final static String URL_TEXT = "http://m2.qiushibaike.com/article/list/text?page=%d";

    //头像获取(+ id掉后4位 + "/" + id + "/thumb/" + icon图片名.jpg)
    //userIcon======http://pic.qiushibaike.com/system/avtnew/1499/14997026/thumb/20140404194843.jpg
    public final static String URL_USER_ICON="http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";

    //内容图片获取(+图片名所有数字去掉后4位+"/"+图片名从数字开始数全部+"/"+"/"+small或者medium+"/"+图片名)
    //====图片Url=http://pic.qiushibaike.com/system/pictures/7128/71288069/small/app71288069.jpg
    public final static String URL_IMAGE= "http://pic.qiushibaike.com/system/pictures/%s/%s/%s/%s";

    public final static String URL_DOWNLOAD_IMAGE1 = "http://img.265g.com/userup/1201/201201071126534773.jpg";

    //原始图片大小：242k
    public final static String URL_DOWNLOAD_IMAGE = "http://img.61gequ.com/allimg/2013-02/2167-130201101H7.jpg";
}
