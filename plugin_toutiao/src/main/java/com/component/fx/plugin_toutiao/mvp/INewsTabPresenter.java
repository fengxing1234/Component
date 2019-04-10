package com.component.fx.plugin_toutiao.mvp;

import android.text.TextUtils;

import com.component.fx.plugin_base.network.TouTiaoRetrofit;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.api.IMobileNewsApi;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBean;
import com.component.fx.plugin_toutiao.bean.MultiNewsArticleBeanData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class INewsTabPresenter implements INewsTabContract.Presenter {

    private INewsTabContract.View view;

    private Gson gson = new Gson();

    private List<MultiNewsArticleBeanData> mList = new ArrayList<>();

    private String type;

    public INewsTabPresenter(INewsTabContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    /**
     * 刷新数据 清除旧数据
     *
     * @param category
     */
    @Override
    public void doLoadData(String... category) {
        if (category.length != 0) {
            type = category[0];
        }

        if (mList.size() != 0) {
            mList.clear();
        }
        view.onShowLoading();
        getData(type);
    }

    @Override
    public void doLoadMoreData() {
        view.onShowLoading();
        getData(type);
    }


    public void getData(String category) {
        IMobileNewsApi mobileNewsApi = TouTiaoRetrofit.getRetrofit().create(IMobileNewsApi.class);
        Observable<MultiNewsArticleBean> observable = mobileNewsApi.getNewsArticle(category, getCurrentTimeStamp());
        observable
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<MultiNewsArticleBean, ObservableSource<MultiNewsArticleBeanData>>() {
                    @Override
                    public ObservableSource<MultiNewsArticleBeanData> apply(MultiNewsArticleBean multiNewsArticleBean) throws Exception {//数据转换  MultiNewsArticleBean中的字符串转成bean对象
                        ArrayList<MultiNewsArticleBeanData> list = new ArrayList<>();
                        for (MultiNewsArticleBean.DataBean bean : multiNewsArticleBean.getData()) {
                            list.add(gson.fromJson(bean.getContent(), MultiNewsArticleBeanData.class));
                        }
                        return Observable.fromIterable(list);
                    }
                })
                .filter(new Predicate<MultiNewsArticleBeanData>() {
                    @Override
                    public boolean test(MultiNewsArticleBeanData data) throws Exception {

                        int time = data.getBehot_time();

                        if (TextUtils.isEmpty(data.getSource())) {
                            return false;
                        }

                        try {
                            // 过滤头条问答新闻
                            if (data.getSource().contains("头条问答")
                                    || data.getTag().contains("ad")
                                    || data.getSource().contains("悟空问答")) {
                                return false;
                            }
                            // 过滤头条问答新闻
                            //阅读数量0  媒体名字 不存在
                            if (data.getRead_count() == 0 || TextUtils.isEmpty(data.getMedia_name())) {

                                String title = data.getTitle();
                                if (title.lastIndexOf("?") == title.length() - 1) {//问号结尾？
                                    return false;
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        //过滤重复新闻

                        for (MultiNewsArticleBeanData beanData : mList) {
                            if (beanData.getTitle().equals(data.getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .map(new Function<List<MultiNewsArticleBeanData>, List<MultiNewsArticleBeanData>>() {
                    @Override
                    public List<MultiNewsArticleBeanData> apply(List<MultiNewsArticleBeanData> list) throws Exception {
                        //过滤重复新闻(与本次刷新的数据比较,因为使用了2个请求,数据会有重复)
                        for (int i = 0; i < list.size() - 1; i++) {
                            for (int j = list.size() - 1; j > i; j--) {
                                if (list.get(j).getTitle().equals(list.get(i).getTitle())) {
                                    list.remove(j);
                                }
                            }
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(view.<List<MultiNewsArticleBeanData>>bindAutoDispose())
                .subscribe(new Consumer<List<MultiNewsArticleBeanData>>() {
                    @Override
                    public void accept(List<MultiNewsArticleBeanData> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            mList.addAll(list);
                            view.onSetAdapter(mList);
                            view.onHideLoading();
                        } else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.onHideLoading();
                        view.onShowNetError();
                        ToastUtil.toast(throwable.getMessage());
                    }
                });
    }

    public String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
}
