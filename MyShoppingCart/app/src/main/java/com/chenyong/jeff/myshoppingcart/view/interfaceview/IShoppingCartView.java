package com.chenyong.jeff.myshoppingcart.view.interfaceview;

import android.view.View;

/**
 * 购物车 interface view
 * Created by chenyong on 2016/8/25.
 */
public interface IShoppingCartView {

    void showCurrentCount(int currentCount, View showCountView);

    void showAllCheck(boolean isAllCheck);

    void showText(double totalPrice, int totalCount);

    void showTitle(int count);
}
