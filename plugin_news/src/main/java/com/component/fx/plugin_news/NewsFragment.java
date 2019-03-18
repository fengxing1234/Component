package com.component.fx.plugin_news;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.component.fx.plugin_base.WebViewActivity;
import com.component.fx.plugin_base.utils.ToastUtil;
import com.component.fx.plugin_news.adapter.NewsRecycleAdapter;
import com.component.fx.plugin_news.network.NewsEnum;
import com.component.fx.plugin_news.network.NewsRequestManager;
import com.component.fx.plugin_news.network.model.NewsModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {

    private static final String BUNDLE_NEWS_TYPE = "bundle_news_type";

    private static final String TAG = "NewsFragment";
    private String mNewsType;
    private RecyclerView mRecycleView;
    private NewsRecycleAdapter newsAdapter;

    public static NewsFragment getInstance(String type) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NEWS_TYPE, type);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mNewsType = bundle.getString(BUNDLE_NEWS_TYPE);
        }

        Log.d(TAG, "onCreate: " + mNewsType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + mNewsType);
        return inflater.inflate(R.layout.news_layout_news_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: " + mNewsType);
        mRecycleView = (RecyclerView) view.findViewById(R.id.news_recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsRecycleAdapter(this);
        mRecycleView.setAdapter(newsAdapter);
        newsAdapter.setOnNewsItemClickListener(new NewsRecycleAdapter.OnNewsItemClickListener() {
            @Override
            public void onNewsItemClick(View v, NewsRecycleAdapter.NewsViewHolder holder, int layoutPosition, NewsModel.DataBean dataBean) {
                WebViewActivity.startWebViewActivity(getContext(), dataBean.getUrl());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + mNewsType);
        NewsRequestManager.getNewsData(NewsEnum.getEnumByName(mNewsType), new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                String name = Thread.currentThread().getName();
                ToastUtil.toast(mNewsType + "数据请求成功" + "线程=" + name);
                newsAdapter.setNewsModel(response.body());
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                String str = "{\n" +
                        "\t\t\"stat\":\"1\",\n" +
                        "\t\t\"data\":[\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"c4b2e063f02b247ad638875a128bfc35\",\n" +
                        "\t\t\t\t\"title\":\"开心一刻笑话：今天早上穿了件有点露肩的连衣裙，化了妆准备出门\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 12:15\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"寂寞内涵\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318121524789.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_8950cd57331f403eaf5a27205dc685c0_1724_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_0f480fc7bfc04c5095afa614f5e53a30_6079_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_b765e4044b7d46499de8f02a1f9b352a_5577_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"e6877af331c50ea09343b711120af1e8\",\n" +
                        "\t\t\t\t\"title\":\"谢娜聊坐月子洗头，最后有谁注意她的表情变化？网友：找罪受！\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 12:14\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"追寻心中的梦想\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318121415975.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_27fc6c6a3b074bbf842735ed55836fe3_9340_cover_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_87f416994a9f415aad6850a29d48a522_9564_cover_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_f0595d7ba7c44a63b1a4cfce122a4876_4821_cover_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"caaabed0d0e59cb2e05169394bf9a8e1\",\n" +
                        "\t\t\t\t\"title\":\"原创 搞笑幽默段子：中午去买菜，感觉都不太新鲜了\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 12:09\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"笑一天\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318120917658.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/20190318120917_0bc89d81dabf53946e618e7bbcf88d4e_2_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/20190318120917_0bc89d81dabf53946e618e7bbcf88d4e_1_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/20190318120917_0bc89d81dabf53946e618e7bbcf88d4e_3_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"f0b6aa4e113f9bb573d219192a54f0ce\",\n" +
                        "\t\t\t\t\"title\":\"炒前“必须焯水”的5种蔬菜，再懒也别省这1步，对身体帮助很大\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 12:08\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"美食菌主\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318120850024.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318120850_5b4826088540149efcab2fe73e5afd14_4_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318120850_5b4826088540149efcab2fe73e5afd14_3_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318120850_5b4826088540149efcab2fe73e5afd14_1_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"e3ff8b3e41dba2cb9d1e82237e73ed0b\",\n" +
                        "\t\t\t\t\"title\":\"少时秀英EXO宣美元彬等韩星大片曝光 优雅浪漫演绎春季风尚\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 12:08\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"人民网\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318120831102.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318120831_ef8b5af4a0798e78064d183d26a0be6c_12_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318120831_ef8b5af4a0798e78064d183d26a0be6c_19_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318120831_ef8b5af4a0798e78064d183d26a0be6c_16_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"cb4913794eb7cf6848d7b74ea44e3cf1\",\n" +
                        "\t\t\t\t\"title\":\"日媒专访奥原希望：严格律己 奥运目标夺金\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 12:01\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"爱羽客羽毛球网\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318120140912.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318120140_89cdddf2278f3ca553c4f2bb9c4421e2_1_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318120140_89cdddf2278f3ca553c4f2bb9c4421e2_2_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"447dac33c25be479cda4d3b58b778ba7\",\n" +
                        "\t\t\t\t\"title\":\"搭载675的Redmi note7 pro适合你吗？真香的水桶备用机\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:58\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"百聊科技\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318115812055.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/03imgmini.eastday.com\\/mobile\\/20190318\\/20190318115812_3b79f5d985302a0568590eaf5b944432_8_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/03imgmini.eastday.com\\/mobile\\/20190318\\/20190318115812_3b79f5d985302a0568590eaf5b944432_1_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/03imgmini.eastday.com\\/mobile\\/20190318\\/20190318115812_3b79f5d985302a0568590eaf5b944432_7_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"5b4ecef0a1850042f65b52fea344be34\",\n" +
                        "\t\t\t\t\"title\":\"河北省曝光40家交通安全隐患突出的运输企业\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:58\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"长城网\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318115800320.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/20190318115800_622fc1fe295f0787ab7989d9d10d5248_1_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/20190318115800_622fc1fe295f0787ab7989d9d10d5248_2_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"773388672d6e3baad3e8c0af148fe3c9\",\n" +
                        "\t\t\t\t\"title\":\"减肥时必须吃的五种主食，不仅不会胖，还可以加速脂肪燃烧\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:57\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"养生保健小卫士\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318115744838.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318115744_a42f3415588a2780407114edf810c4ab_3_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318115744_a42f3415588a2780407114edf810c4ab_1_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318115744_a42f3415588a2780407114edf810c4ab_2_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"87aa54e7a2c4e67b99d1faa72274d05a\",\n" +
                        "\t\t\t\t\"title\":\"火箭15分大胜夜，里弗斯和法里埃德遭弃用了？德帅说出了真相！\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:57\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"小栋嘚吧嘚\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318115735373.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_ae15bdc2e32e499bae6723c2654c86eb_5901_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_8bc9d9ade65d4f48ab742d83a9db9398_8877_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_e9e9a60eebbe40e29a8411483258f8c8_8240_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"2a9f94d9d6142e2f492176ee115dbc46\",\n" +
                        "\t\t\t\t\"title\":\"幽默笑话段子：领导家有单身女性，碍于职场关系就去见了一面\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:57\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"星座密码\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318115724502.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_45676cfc00aa4892ac728bcc3ecf823e_2104_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_b13b47c51aa14b10845f2056f7151301_9533_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_f61635f3d3f9435f890b87c910cd4522_9892_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"05070f0248f4db85c26a14a3783efe67\",\n" +
                        "\t\t\t\t\"title\":\"街拍女神：小背心，小短裤，美女清凉的穿搭非常有女人味\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:54\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"美肤小霸王\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318115426175.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_845dea9ab3af4f7d9d86ae7436b90d68_4448_cover_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_3e8071ca6fac461588a9dc561265c865_4069_cover_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_5cc4eaff12a9444c920aa6ec5a47492b_7862_cover_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"2ad13c5029032b83657e3e5a0a0793a9\",\n" +
                        "\t\t\t\t\"title\":\"上海警方破获非法提供证券咨询案：推荐股票、发展会员两年获利4130万！这背后……\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:52\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"解放网\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318115224325.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318115224_03abc01e24b4014c2a474523ef426666_1_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318115224_03abc01e24b4014c2a474523ef426666_2_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318115224_03abc01e24b4014c2a474523ef426666_3_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"1f04440481ec935b5f1057d61cee77c6\",\n" +
                        "\t\t\t\t\"title\":\"800元！贵州茅台创股价纪录！但这还不是A股历史最高！\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:50\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"纵相新闻\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318115021274.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_23e62bb070fe40e68ec89d1b3f4d9eb0_9476_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_88db4e1bb17d4a50852893061c8ebc67_5237_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031812_af8fc1fab4f9432f8de0941274919fdf_1166_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"5faa2e1af27203b596b0e7d25c8e02d7\",\n" +
                        "\t\t\t\t\"title\":\"梅西欧洲金靴稳了！帽子戏法4-1大胜贝蒂斯，2回合狂轰5球\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:49\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"全能体育柳号\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318114954398.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/09imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_6e744792c1db49fb87132abb6cb7bcba_0201_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/09imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_6d391af2cfd14f278c847a1990bbda4a_2478_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/09imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_59210ea77846432684412da6ccb94d03_8130_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"ca4300ee45c81cb0a2573c38ce133657\",\n" +
                        "\t\t\t\t\"title\":\"15分大胜！火箭轰破73年记录，赛后保罗这话比25分还嚣张！\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:48\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"十号风球说\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318114846025.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_0fe5bd035bf543e7b0fe2daadfb8aa19_9404_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_e661ed1d89d84996bf5d78bd3b77265b_4397_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_d138e0dc13a545f08da79c2fea3c655f_7071_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"29d7fe3aec1b2acce3f933e957a74677\",\n" +
                        "\t\t\t\t\"title\":\"富光“茶马仕”品牌重磅发布 感受茶与器完美相逢\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:48\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"国际在线\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318114807641.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318114807_f8c62635cbf36c3c94714f6d2f3467ec_1_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318114807_f8c62635cbf36c3c94714f6d2f3467ec_4_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/20190318114807_f8c62635cbf36c3c94714f6d2f3467ec_2_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"44199322bdfd523490a8e35c36897cc9\",\n" +
                        "\t\t\t\t\"title\":\"《米饭薯条》是音乐会还是话剧？或是知识讲座？\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:47\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"海外网\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318114736470.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318114736_1c8cc30136c882ffc0c03897de809959_6_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318114736_1c8cc30136c882ffc0c03897de809959_3_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/20190318114736_1c8cc30136c882ffc0c03897de809959_1_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"8d8fa7966ecac66d3df3ff057131efde\",\n" +
                        "\t\t\t\t\"title\":\"斯坦福桥蓝魂不再，切尔西遭遇最大危机\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:47\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"有理球事\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318114708573.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/00imgmini.eastday.com\\/mobile\\/20190318\\/20190318114708_be9a8fd8bc6d2cd41cf83a5f896dd677_2_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/00imgmini.eastday.com\\/mobile\\/20190318\\/20190318114708_be9a8fd8bc6d2cd41cf83a5f896dd677_3_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/00imgmini.eastday.com\\/mobile\\/20190318\\/20190318114708_be9a8fd8bc6d2cd41cf83a5f896dd677_5_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"f3c719270220242d3a2faa0d648193aa\",\n" +
                        "\t\t\t\t\"title\":\"2019北京半马报名人数达58392人 中签结果将于3月底公布\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:44\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"千龙网\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318114453799.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/00imgmini.eastday.com\\/mobile\\/20190318\\/20190318114453_18e39218839ed0bf02b2980f7134d6f8_1_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"c7b8279f223fde29a80681b6530e405b\",\n" +
                        "\t\t\t\t\"title\":\"美国女子养了18年的猴子，现在越来越臭美，已经把自己当成人！\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:39\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"海外异闻\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113930127.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031715_5e633038b8014184ac858c65f96a81ee_7339_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031715_851d6d651ab14278b6b5a89aa4fe0a97_6348_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/07imgmini.eastday.com\\/mobile\\/20190318\\/2019031715_6d495995bdf849ed879f4f4e724168d3_7931_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"ce56683bd10cec18a068585165b4c268\",\n" +
                        "\t\t\t\t\"title\":\"票房黑马《无名之辈》小成本，大制作!网友：粉丝经济果然给力！\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:39\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"琪米影视界\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113907893.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_00a4e264395248ba9c6ba7949e2a92b6_5032_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_d81334e7070f4a429efe54c34ac063b0_3298_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/01imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_65ddf27c59754227b1246fb1742d9b6a_4970_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"4e32e2e2faf4d28af08f683b9a2bdf8b\",\n" +
                        "\t\t\t\t\"title\":\"伊万卡女儿真可爱！跟着妈妈小姨去看火鸡，害怕的拽紧妈妈的衣袖\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:39\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"远和时尚\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113907584.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/08imgmini.eastday.com\\/mobile\\/20190318\\/20190318113907_cef83c94279342c02f7184baf0efea46_6_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/08imgmini.eastday.com\\/mobile\\/20190318\\/20190318113907_cef83c94279342c02f7184baf0efea46_5_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/08imgmini.eastday.com\\/mobile\\/20190318\\/20190318113907_cef83c94279342c02f7184baf0efea46_3_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"2a6e775d087a5ae3ee099eedee28ea5b\",\n" +
                        "\t\t\t\t\"title\":\"意甲：C罗轮休 尤文0：2负热那亚不败金身告破\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:38\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"中国新闻网\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113859161.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/00imgmini.eastday.com\\/mobile\\/20190318\\/20190318113859_93f69b2fb117853d996eec95314905e2_1_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"53b7a93d79316241399ade4805e4092e\",\n" +
                        "\t\t\t\t\"title\":\"娱乐圈世界真小，郭京飞妻子竟然和她是亲姐妹！\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:38\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"娱乐二哈\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113835342.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/20190318113835_10e1527820e3b727a0dfd542ff314c69_8_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/20190318113835_10e1527820e3b727a0dfd542ff314c69_4_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/05imgmini.eastday.com\\/mobile\\/20190318\\/20190318113835_10e1527820e3b727a0dfd542ff314c69_1_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"62006d867ec09f525324a3f79b85d196\",\n" +
                        "\t\t\t\t\"title\":\"想给血管做个“大扫除”，每顿多吃1豆3菜，帮你清理血管细菌\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:38\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"梦笔三生\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113824110.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/06imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_ca6068cb92d1401caa65f1b2dd9b3989_0935_cover_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/06imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_7b7a3f22ba454db8b94f859f2a8164fa_1106_cover_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/06imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_b0e76ff4e9fe41a9b0bfee78b09d1bd0_9467_cover_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"c3b7673b825bca72b4c6af6ad07de00b\",\n" +
                        "\t\t\t\t\"title\":\"乔欣纠结是否跟公司续约，杨天真却指出乔欣的两个问题，很难解决\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:37\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"自娱自乐12号站\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113733178.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/03imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_23817af8020c4c2393c9b1534c7a1e3c_7068_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/03imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_74c3cbf46c664889b364dd0519f62d00_3192_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/03imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_96b26eefad1c464cad5c2509b81e8b4e_4232_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"04e90633d3b9d54b7fc59da6e03b7146\",\n" +
                        "\t\t\t\t\"title\":\"杨德龙：股民可关注科技龙头股，不要盲目投资科创板股票\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:37\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"零号财经\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113724925.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/03imgmini.eastday.com\\/mobile\\/20190318\\/20190318113724_e0e5fe7e2389393e83b191b2de3a917b_1_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"7a8633ae7802e738e4072ac90abdc7df\",\n" +
                        "\t\t\t\t\"title\":\"平均每2秒钟卖出一顶 中国假发成老外刚需的背后_看天下_时政新闻_浙江在线\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:35\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"浙江在线\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113542134.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/20190318113542_ac828ebb61b096c9cd6dd8146f722785_2_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/20190318113542_ac828ebb61b096c9cd6dd8146f722785_1_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/02imgmini.eastday.com\\/mobile\\/20190318\\/20190318113542_ac828ebb61b096c9cd6dd8146f722785_3_mwpm_03200403.jpg\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\t{\n" +
                        "\t\t\t\t\"uniquekey\":\"4ca9832f834c23ae05140fbbf68520b4\",\n" +
                        "\t\t\t\t\"title\":\"张译《红海行动》后又一军事大作，导演管虎，搭档李晨，又要飞？\",\n" +
                        "\t\t\t\t\"date\":\"2019-03-18 11:35\",\n" +
                        "\t\t\t\t\"category\":\"头条\",\n" +
                        "\t\t\t\t\"author_name\":\"景天娱记\",\n" +
                        "\t\t\t\t\"url\":\"http:\\/\\/mini.eastday.com\\/mobile\\/190318113516140.html\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s\":\"http:\\/\\/00imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_a04e8973215e4511bd650a57773dee2a_8374_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s02\":\"http:\\/\\/00imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_3802cdf457154f61b03d2408a9fe7fa1_8838_mwpm_03200403.jpg\",\n" +
                        "\t\t\t\t\"thumbnail_pic_s03\":\"http:\\/\\/00imgmini.eastday.com\\/mobile\\/20190318\\/2019031811_a026c20381d744958bd13a9425c8bff4_9401_mwpm_03200403.jpg\"\n" +
                        "\t\t\t}\n" +
                        "\t\t]\n" +
                        "\t}";

                Gson gson = new Gson();
                NewsModel newsModel = gson.fromJson(str, NewsModel.class);
                newsAdapter.setNewsModel(newsModel);
                String name = Thread.currentThread().getName();
                ToastUtil.toast(t.getMessage() + "  线程:" + name);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + mNewsType);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: " + mNewsType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: " + mNewsType);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + mNewsType);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + mNewsType);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: " + mNewsType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + mNewsType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + mNewsType);
    }
}
