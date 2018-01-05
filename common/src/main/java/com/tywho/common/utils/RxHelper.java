package com.tywho.common.utils;

import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.RxActivity;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * ********************** Copyright (c) ***********************\
 * *
 * *              Copyright Reserved 2013-2016
 * *
 * *                 By(个人项目)
 * *                    www.tywho.com
 * *
 * *                       _oo0oo_
 * *                      o8888888o
 * *                      88" . "88
 * *                      (| -_- |)
 * *                      0\  =  /0
 * *                    ___/`---'\___
 * *                  .' \\|     |// '.
 * *                 / \\|||  :  |||// \
 * *                / _||||| -:- |||||- \
 * *               |   | \\\  -  /// |   |
 * *               | \_|  ''\---/''  |_/ |
 * *               \  .-\__  '-'  ___/-. /
 * *             ___'. .'  /--.--\  `. .'___
 * *          ."" '<  `.___\_<|>_/___.' >' "".
 * *         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * *         \  \ `_.   \_ __\ /__ _/   .-` /  /
 * *     =====`-.____`.___ \_____/___.-`___.-'=====
 * *                       `=---='
 * *
 * *
 * *     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * *
 * *               佛祖保佑         永无BUG
 * *
 * *
 * *                   南无本师释迦牟尼佛
 * *
 * ******************** Copyright (c) ***********************\
 * Created with IntelliJ IDEA.
 * User: limiter0417@foxmail.com
 * Date: 2017/9/27/0027
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class RxHelper<T> {
    //子线程运行，主线程回调
    public Observable.Transformer<T, T> io2main(final RxAppCompatActivity context) {
        return new Observable.Transformer<T, T>() {

            @Override
            public Observable<T> call(Observable<T> tObservable) {

                Observable<T> tObservable1 = (Observable<T>) tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxLifecycle.bindUntilEvent(context.lifecycle(), ActivityEvent.DESTROY));

                return tObservable1;

            }
        };
    }
}