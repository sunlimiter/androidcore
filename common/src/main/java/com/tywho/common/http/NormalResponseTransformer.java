package com.tywho.common.http;

import com.tywho.common.base.bean.BaseBean;
import com.tywho.common.exception.ApiException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limit on 2017/3/30.
 * 标准输出，basebean封装结果，统一返回
 */

public class NormalResponseTransformer<T> implements Observable.Transformer<BaseBean<T>, T> {
    @Override
    public Observable<T> call(Observable<BaseBean<T>> CommonResponseObservable) {
        return CommonResponseObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .lift(new Observable.Operator<T, BaseBean<T>>() {
                    @Override
                    public Subscriber<? super BaseBean<T>> call(final Subscriber<? super T> subscriber) {
                        return new Subscriber<BaseBean<T>>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                subscriber.onError(throwable);
                            }

                            @Override
                            public void onNext(BaseBean<T> tBaseBean) {
                                if (tBaseBean.isSuccess()) {
                                    subscriber.onNext(tBaseBean.data);
                                } else {
                                    subscriber.onError(new ApiException(tBaseBean));
                                }
                            }
                        };
                    }
                });
    }
}