package com.component.fx.plugin_toutiao.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.component.fx.plugin_base.base.BaseApplication;
import com.component.fx.plugin_toutiao.R;
import com.component.fx.plugin_toutiao.bean.NewsChannelBean;
import com.component.fx.plugin_toutiao.db.TouTiaoDbHelp;
import com.component.fx.plugin_toutiao.db.tab.NewsChannelTable;

import java.util.ArrayList;
import java.util.List;

public class NewsChannelDao {

    private final SQLiteDatabase db;

    public NewsChannelDao() {
        db = TouTiaoDbHelp.getDatabase();
    }

    public void initDao() {
        String[] newsId = BaseApplication.getAppContext().getResources().getStringArray(R.array.toutiao_news_id);
        String[] newsName = BaseApplication.getAppContext().getResources().getStringArray(R.array.toutiao_news_name);
        for (int i = 0; i < 8; i++) {
            add(newsId[i], newsName[i], NewsChannelTable.NEWS_CHANNEL_ENABLE, i);
        }

        for (int i = 8; i < newsId.length; i++) {
            add(newsId[i], newsName[i], NewsChannelTable.NEWS_CHANNEL_DISENABLE, i);
        }
    }

    public void add(String channelId, String channelName, int isEnable, int position) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsChannelTable.ID, channelId);
        contentValues.put(NewsChannelTable.NAME, channelName);
        contentValues.put(NewsChannelTable.IS_ENABLE, isEnable);
        contentValues.put(NewsChannelTable.POSITION, position);
        db.insert(NewsChannelTable.TABLENAME, null, contentValues);
    }

    public List<NewsChannelBean> query(int isEnable) {
        Cursor cursor = db.query(NewsChannelTable.TABLENAME, null, NewsChannelTable.IS_ENABLE + "=?", new String[]{isEnable + ""}, null, null, null);
        final ArrayList<NewsChannelBean> channelBeans = new ArrayList<>();
        while (cursor.moveToNext()) {
            NewsChannelBean bean = new NewsChannelBean();
            bean.channelId = cursor.getString(NewsChannelTable.ID_ID);
            bean.channelName = cursor.getString(NewsChannelTable.ID_NAME);
            bean.isEnable = cursor.getInt(NewsChannelTable.ID_ISENABLE);
            bean.position = cursor.getInt(NewsChannelTable.ID_POSITION);
            channelBeans.add(bean);
        }
        cursor.close();
        return channelBeans;
    }
}
