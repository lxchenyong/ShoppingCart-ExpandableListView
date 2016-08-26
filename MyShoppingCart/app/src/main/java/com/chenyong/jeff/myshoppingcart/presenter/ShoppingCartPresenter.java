package com.chenyong.jeff.myshoppingcart.presenter;

import android.view.View;

import com.chenyong.jeff.myshoppingcart.model.bean.GoodsInfo;
import com.chenyong.jeff.myshoppingcart.model.bean.StoreInfo;
import com.chenyong.jeff.myshoppingcart.model.biz.ShoppingCartBiz;
import com.chenyong.jeff.myshoppingcart.view.adapter.ShopCartAdapter;
import com.chenyong.jeff.myshoppingcart.view.interfaceview.IShoppingCartView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车 presenter
 * Created by Administrator on 2016/8/25.
 */
public class ShoppingCartPresenter {
    private ShoppingCartBiz shoppingCartBiz;
    private IShoppingCartView iShoppingCartView;

    private List<StoreInfo> groups = new ArrayList<>();// 组元素数据列表
    private Map<String, List<GoodsInfo>> children = new HashMap<>();// 子元素数据列表
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量

    public ShoppingCartPresenter(IShoppingCartView iShoppingCartView, ShoppingCartBiz shoppingCartBiz) {
        this.iShoppingCartView = iShoppingCartView;
        this.shoppingCartBiz = shoppingCartBiz;
    }

    //得到所有商家
    public List<StoreInfo> initGroups() {
        groups = shoppingCartBiz.initGroups();
        return groups;
    }

    //得到商家下所有商品
    public Map<String, List<GoodsInfo>> initChildrens() {
        children = shoppingCartBiz.initChildrens();
        return children;
    }

    /**
     * 设置购物车产品数量
     */
    public void setCartNum(boolean isCheched) {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(isCheched);
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (GoodsInfo goodsInfo : childs) {
                count += 1;
            }
        }
        iShoppingCartView.showTitle(count);
    }
    /**
     * 增加数量
     */
    public void doIncrease(GoodsInfo goodsInfo, View showCountView) {
        GoodsInfo product = goodsInfo;
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        iShoppingCartView.showCurrentCount(currentCount, showCountView);
        calculate();
    }

    /**
     * 减少数量
     */
    public void doDecrease(GoodsInfo goodsInfo, View showCountView) {
        GoodsInfo product = goodsInfo;
        int currentCount = product.getCount();
        if (currentCount == 1)
            return;
        currentCount--;
        product.setCount(currentCount);
        iShoppingCartView.showCurrentCount(currentCount, showCountView);
        calculate();
    }

    /**
     * 商家复选框选着
     *
     * @param groupPosition 商家索引
     * @param isChecked     是否勾选
     */
    public void checkedGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        iShoppingCartView.showAllCheck(isAllCheck());
        calculate();
    }

    /**
     * 商品复选框的选着
     *
     * @param groupPosition 商家索引
     * @param isChecked     是否勾选
     */
    public void chechedChild(int groupPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }
        iShoppingCartView.showAllCheck(isAllCheck());
        calculate();
    }

    /**
     * 删除商品
     * @param groupPosition 商家索引
     * @param childPosition 商品索引
     */
    public void DeleteChild(int groupPosition, int childPosition){
        children.get(groups.get(groupPosition).getId()).remove(childPosition);
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        if (childs.size() == 0) {
            groups.remove(groupPosition);
        }
        calculate();
    }


    /**
     * @return 判断全选是否勾选
     */
    private boolean isAllCheck() {
        for (StoreInfo group : groups) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 全选与反选
     */
    public void doCheckAll(boolean isCheck) {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(isCheck);
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(isCheck);
            }
        }
        calculate();
    }
    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                GoodsInfo product = childs.get(j);
                if (product.isChoosed()) {
                    totalCount++;
                    totalPrice += product.getPrice() * product.getCount();
                }
            }
        }

        iShoppingCartView.showText(totalPrice,totalCount);
        //计算购物车的金额为0时候清空购物车的视图
//        if(totalCount==0){
//            setCartNum(isCheck);
//        }
//        else{
//            title.setText("购物车" + "(" + totalCount + ")");
//        }
    }
}
