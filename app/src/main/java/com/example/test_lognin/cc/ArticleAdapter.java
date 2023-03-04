package com.example.test_lognin.cc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.test_lognin.R;
import com.example.test_lognin.group.GroupRecyclerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

//recycleview Adapter
public class ArticleAdapter extends GroupRecyclerAdapter<String, Article> {



    private RequestManager mLoader;

    public ArticleAdapter(Context context) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<Article>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("跌倒影像", create(0));
        titles.add("跌倒影像");
        resetGroups(map,titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Article item, int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;
        h.mTextTitle.setText(item.getTitle());
        h.mTextContent.setText(item.getContent());
        mLoader.load(item.getImgUrl())
                .into(h.mImageView);
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle,
                mTextContent;
        private ImageView mImageView;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.tv_title);
            mTextContent = itemView.findViewById(R.id.tv_content);
            mImageView = itemView.findViewById(R.id.imageView);
        }
    }


    private static Article create(String title, String content, String imgUrl) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setImgUrl(imgUrl);
        return article;
    }

    private static List<Article> create(int p) {
        List<Article> list = new ArrayList<>();
        if (p == 0) {
            list.add(create("慈濟大學iCAN5",
                    "登入頁面",
                    "https://ican.tcu.edu.tw/login.aspx"));
            list.add(create("歡迎報名參加：土壤心法(初階+進階)系列課程-場次三(進階二)活動",
                    "人們對於一件事物的理解，原則上都是由感官接收訊息，然後經過大腦處理整合後，才能達成。",
                    "https://info.tcu.edu.tw/hot_news/news.asp?id=1100819003"));
        }

        return list;
    }
}
