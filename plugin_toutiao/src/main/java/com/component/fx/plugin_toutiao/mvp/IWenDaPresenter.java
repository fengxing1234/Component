package com.component.fx.plugin_toutiao.mvp;

import android.text.TextUtils;

import com.component.fx.plugin_base.network.TouTiaoRetrofit;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_toutiao.api.IMobileWenDaApi;
import com.component.fx.plugin_toutiao.bean.WendaArticleBean;
import com.component.fx.plugin_toutiao.bean.WendaArticleBeanData;
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

public class IWenDaPresenter implements IWenDaContract.Presenter {


    private IWenDaContract.View view;
    private Gson gson = new Gson();
    private List<WendaArticleBeanData> mList = new ArrayList<>();


    public IWenDaPresenter(IWenDaContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void doLoadData(String... category) {
        if (mList.size() != 0) {
            mList.clear();
        }
        view.onShowLoading();
        getWenDaNewsData();
    }

    @Override
    public void doLoadMoreData() {
        view.onShowLoading();
        getWenDaNewsData();
    }

    public void getWenDaNewsData() {

        IMobileWenDaApi mobileNewsApi = TouTiaoRetrofit.getRetrofit().create(IMobileWenDaApi.class);
        Observable<WendaArticleBean> observable = mobileNewsApi.getWendaArticle(getCurrentTimeStamp());
        observable
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<WendaArticleBean, ObservableSource<WendaArticleBeanData>>() {
                    @Override
                    public ObservableSource<WendaArticleBeanData> apply(WendaArticleBean wendaArticleBean) throws Exception {
                        List<WendaArticleBean.DataBean> beanList = wendaArticleBean.getData();
                        final ArrayList<WendaArticleBeanData> list = new ArrayList<>();
                        for (WendaArticleBean.DataBean dataBean : beanList) {
                            WendaArticleBeanData data = gson.fromJson(dataBean.getContent(), WendaArticleBeanData.class);
                            list.add(data);
                        }
                        return Observable.fromIterable(list);
                    }
                })
                .filter(new Predicate<WendaArticleBeanData>() {
                    @Override
                    public boolean test(WendaArticleBeanData wendaArticleBeanData) throws Exception {
                        return !TextUtils.isEmpty(wendaArticleBeanData.getQuestion());
                    }
                })
                .map(new Function<WendaArticleBeanData, WendaArticleBeanData>() {
                    @Override
                    public WendaArticleBeanData apply(WendaArticleBeanData wendaArticleBeanData) throws Exception {
                        WendaArticleBeanData.ExtraBean extraBean = gson.fromJson(wendaArticleBeanData.getExtra(), WendaArticleBeanData.ExtraBean.class);
                        WendaArticleBeanData.QuestionBean questionBean = gson.fromJson(wendaArticleBeanData.getQuestion(), WendaArticleBeanData.QuestionBean.class);
                        WendaArticleBeanData.AnswerBean answerBean = gson.fromJson(wendaArticleBeanData.getAnswer(), WendaArticleBeanData.AnswerBean.class);
                        wendaArticleBeanData.setExtraBean(extraBean);
                        wendaArticleBeanData.setQuestionBean(questionBean);
                        wendaArticleBeanData.setAnswerBean(answerBean);

                        String time = wendaArticleBeanData.getBehot_time();
                        return wendaArticleBeanData;
                    }
                })
                .filter(new Predicate<WendaArticleBeanData>() {
                    @Override
                    public boolean test(WendaArticleBeanData wendaArticleBeanData) throws Exception {
                        for (WendaArticleBeanData bean : mList) {
                            if (bean.getQuestionBean().getTitle().equals(wendaArticleBeanData.getQuestionBean().getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .as(view.<List<WendaArticleBeanData>>bindAutoDispose())
                .subscribe(new Consumer<List<WendaArticleBeanData>>() {
                    @Override
                    public void accept(List<WendaArticleBeanData> wendaArticleBeanData) throws Exception {
                        mList.addAll(wendaArticleBeanData);
                        view.onSetAdapter(mList);
                        view.onHideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        ToastUtil.toast(throwable.getMessage());
                        view.onHideLoading();
                    }
                });
    }


    public String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

}
