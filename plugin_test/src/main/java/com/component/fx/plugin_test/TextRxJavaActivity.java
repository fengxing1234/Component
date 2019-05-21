package com.component.fx.plugin_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * concat(localData, remoteData)
 * local 直接返回 remote 延迟5秒返回
 */
public class TextRxJavaActivity extends AppCompatActivity {

    private static final String TAG = "TextRxJavaActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_rxjava_activity);
        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }


    public void test() {
        Flowable<List<String>> localData = testLocalData();
        Flowable<List<String>> remoteData = testRemoteData();

        Disposable subscribe = Flowable
                .concat(remoteData, remoteData)
                .switchIfEmpty(new Flowable<List<String>>() {
                    @Override
                    protected void subscribeActual(Subscriber<? super List<String>> s) {
                        s.onError(new NoSuchElementException());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> s) throws Exception {
                        Log.d(TAG, "onNext: " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "onError: ");
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public Flowable<List<String>> testRemoteData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("100");
        list.add("200");
//        return Flowable
//                .fromIterable(list)
        return Flowable
                .create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                        Log.d(TAG, "emit 1");
                        emitter.onNext("1");
                        Log.d(TAG, "emit 2");
                        emitter.onNext("2");
                        emitter.onError(new Throwable("sss"));
                        Log.d(TAG, "emit 3");
                        emitter.onNext("3");
                        Log.d(TAG, "emit complete");
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .delay(5, TimeUnit.SECONDS)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                })
                .toList()
                .toFlowable()
                .doOnNext(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> data) throws Exception {

                    }
                })
                .onErrorResumeNext(new Function<Throwable, Publisher<? extends List<String>>>() {
                    @Override
                    public Publisher<? extends List<String>> apply(Throwable throwable) throws Exception {
                        return Flowable.empty();
                    }
                })
                .subscribeOn(Schedulers.io())
                ;


    }


    public Flowable<List<String>> testLocalData() {
        return
//                Flowable.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
//                Log.d(TAG, "emit 1");
//                emitter.onNext("1");
//                Log.d(TAG, "emit 2");
//                emitter.onNext("2");
//                emitter.onError(new Throwable("sss"));
//                Log.d(TAG, "emit 3");
//                emitter.onNext("3");
//                Log.d(TAG, "emit complete");
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER)
                Flowable.just("4", "5", "6")

                        .toList()
                        .toFlowable()
                        .subscribeOn(Schedulers.io());
    }
}
