package com.component.fx.plugin_toutiao.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.component.fx.plugin_base.base.BaseApplication;
import com.component.fx.plugin_toutiao.db.tab.MediaChannelTable;
import com.component.fx.plugin_toutiao.db.tab.NewsChannelTable;
import com.component.fx.plugin_toutiao.db.tab.SearchHistoryTable;

public class TouTiaoDbHelp extends SQLiteOpenHelper {

    private static Integer Version = 1;
    private static String NAME = "TouTiao";
    private static TouTiaoDbHelp dbHelp;
    private static SQLiteDatabase database;


    private TouTiaoDbHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static synchronized TouTiaoDbHelp getInstance() {
        if (dbHelp == null) {
            dbHelp = new TouTiaoDbHelp(BaseApplication.getAppContext(), NAME, null, Version);
        }
        return dbHelp;
    }

    public static synchronized SQLiteDatabase getDatabase() {
        if (database == null) {
            // 真正创建 / 打开数据库
            database = getInstance().getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String create = "create table person(id integer primary key autoincrement ,name varchar(64) ,address varchar(64))";
//        db.execSQL(create);

        // 注：数据库实际上是没被创建 / 打开的（因该方法还没调用）
        // 直到getWritableDatabase() / getReadableDatabase() 第一次被调用时才会进行创建 / 打开

        db.execSQL(NewsChannelTable.CREATE_TABLE);
        db.execSQL(MediaChannelTable.CREATE_TABLE);
        db.execSQL(SearchHistoryTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
