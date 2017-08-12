package com.qiushi.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.qiushi.R;
import com.qiushi.model.QiushiModel;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by matthew on 17-7-4.
 */

public class NewAdapter extends BaseAdapterHelper<QiushiModel.ItemsBean>{
    /**
     * 适配器:
     *   1. 选择适配器 (写一个适配器base)
     *   2. 继承适配器
     *   3. 数据是否分类 ? -- > 选择重写的方法
     *   4. 构造器进行初始化;
     *   5. viewHolder内部类.
     *   6. 优化;
     * */
    private final static int TYPE1 = 0, TYPE2 = 1;
    private Context mContext = null;
    private ViewHolderImg mViewHolderImg;
    private ViewHolder mViewHolder;
    private List<QiushiModel.ItemsBean> mItemsBeanList = null;

    public NewAdapter(List<QiushiModel.ItemsBean> list, Context context) {
        super(list, context);
        this.mContext = context;
        this.mItemsBeanList = list;
    }

    @Override
    public int getCount() {
        return mItemsBeanList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     *  主要应用:
     *    对数据类型进行区分:
     *      1. 有图
     *      2. 无图
     * */
    @Override
    public int getItemViewType(int position) {
        String imageUrl = getImageUrl(mItemsBeanList.get(position).getImage() + "");
        return imageUrl.equals("") ? TYPE2 : TYPE1;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent,
                            List<QiushiModel.ItemsBean> mItemsBeanList, LayoutInflater inflater) {
        int type = getItemViewType(position);
        if (convertView == null) {
           switch (type) {
               case TYPE1:
                   convertView = inflater.inflate(R.layout.item_listview_main_img, parent, false);
                   mViewHolderImg = new ViewHolderImg(convertView);
                   convertView.setTag(mViewHolderImg);
                   break;
               case TYPE2:
                   convertView = inflater.inflate(R.layout.item_listview_main, parent, false);
                   mViewHolder = new ViewHolder(convertView);
                   convertView.setTag(mViewHolder);
                   break;
           }
        } else {
            switch (type) {
                case TYPE1:
                    mViewHolderImg = (ViewHolderImg) convertView.getTag();
                    break;
                case TYPE2:
                    mViewHolder = (ViewHolder) convertView.getTag();
                    break;
            }
        }

        //给控件赋值
        switch (type) {
            case TYPE1:
                mViewHolderImg.textView_item_content.setText(mItemsBeanList.get(position).getContent());
                if (mItemsBeanList.get(position).getUser() != null) {
                    mViewHolderImg.textView_item_login.setText(mItemsBeanList.get(position).getUser().getLogin());
                }
                mViewHolderImg.textView_item_comments.setText(mItemsBeanList.get(position).getComments_count() + "");
                final String imageUrl = getImageUrl(mItemsBeanList.get(position).getImage() + "");
                // 使用Picasso框架加载图片
                Picasso.with(mContext).load(Uri.parse(imageUrl))
                        //无淡入淡出，快速加载
                        .noFade()
                        //下载图片的大小
                        //.resize(parent.getWidth(), 0)
                        //.resizeDimen(int targetWidthResId, int targetHeightResId)
                        //图片裁切
                        //.centerInside()
                        //占位图片，就是下载中的图片
                        .placeholder(R.mipmap.ic_launcher)
                        //错误图片
                        .error(R.mipmap.ic_launcher)
                        .into(mViewHolderImg.imageView_item_show);
                break;
            case TYPE2:
                mViewHolder.mTextView_item_content.setText(mItemsBeanList.get(position).getContent());
                if (mItemsBeanList.get(position).getUser() != null) {
                    mViewHolder.mTextView＿item_login.setText(mItemsBeanList.get(position).getUser().getLogin());
                }
                mViewHolder.mTextView_item_comments.setText(mItemsBeanList.get(position).getComments_count() + "");
                break;
        }
        return convertView;
    }
    // 根据图片的名称拼凑图片的网络访问地址
    private String getImageUrl(String imageName) {
        String urlFirst = "", urlSecond = "";
        if (imageName.indexOf('.') > 0) {
            StringBuilder sb = new StringBuilder();
            if (imageName.indexOf("app") == 0) {
                urlSecond = imageName.substring(3, imageName.indexOf('.'));
                switch (urlSecond.length()) {
                    case 8:
                        urlFirst = imageName.substring(3, 7);
                        break;
                    case 9:
                        urlFirst = imageName.substring(3, 8);
                        break;
                    case 10:
                        urlFirst = imageName.substring(3, 9);
                        break;
                }
            } else {
                urlSecond = imageName.substring(0, imageName.indexOf('.'));
                urlFirst = imageName.substring(0, 6);
            }
            sb.append("http://pic.qiushibaike.com/system/pictures/");
            sb.append(urlFirst);
            sb.append("/");
            sb.append(urlSecond);
            sb.append("/");
            sb.append("small/");
            sb.append(imageName);
            return sb.toString();
        } else {
            return "";
        }
    }

    public static class ViewHolderImg {
        private ImageView imageView_item_show; //图片
        private TextView textView_item_content; // 内容
        private TextView textView_item_login; // 登录名
        private TextView textView_item_comments; //评论

        public ViewHolderImg(View convertView) {
            imageView_item_show = (ImageView) convertView.findViewById(R.id.imageView_item_show);
            textView_item_content = (TextView) convertView.findViewById(R.id.textView_item_content);
            textView_item_login = (TextView) convertView.findViewById(R.id.textView_item_login);
            textView_item_comments = (TextView) convertView.findViewById(R.id.textView_item_commentscount);
        }
    }

    public static class ViewHolder {
        private TextView mTextView_item_content;
        private TextView mTextView＿item_login;
        private TextView mTextView_item_comments;

        public ViewHolder(View convertView) {
            mTextView_item_content = (TextView) convertView.findViewById(R.id.textView_item_content);
            mTextView＿item_login = (TextView) convertView.findViewById(R.id.textView_item_login);
            mTextView_item_comments = (TextView) convertView.findViewById(R.id.textView_item_commentscount);
        }
    }
}
