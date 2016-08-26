package com.chenyong.jeff.myshoppingcart.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenyong.jeff.myshoppingcart.R;
import com.chenyong.jeff.myshoppingcart.model.bean.GoodsInfo;
import com.chenyong.jeff.myshoppingcart.model.bean.StoreInfo;
import com.chenyong.jeff.myshoppingcart.model.biz.ShoppingCartBiz;
import com.chenyong.jeff.myshoppingcart.presenter.ShoppingCartPresenter;
import com.chenyong.jeff.myshoppingcart.view.adapter.ShopCartAdapter;
import com.chenyong.jeff.myshoppingcart.view.interfaceview.IShoppingCartView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车
 * Created by chenyong on 2016/8/25.
 */
public class ShoppingCartActivity extends AppCompatActivity implements IShoppingCartView,ShopCartAdapter.ModifyCountInterface,
        ShopCartAdapter.CheckInterface,View.OnClickListener {
    private List<StoreInfo> groups = new ArrayList<>();// 组元素数据列表
    private Map<String, List<GoodsInfo>> children = new HashMap<>();//
    private ShopCartAdapter adapter;
    private ShoppingCartPresenter presenter;
    private CheckBox allCheckbox;
    private TextView tvTotalPrice;
    private TextView tvGoToPay;
    private TextView tvTitle;
    private TextView subtitle;
    private LinearLayout llCart;
    private LinearLayout cart_empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart);
        ShoppingCartBiz shoppingCartBiz = new ShoppingCartBiz();
        presenter = new ShoppingCartPresenter(this,shoppingCartBiz);
        groups=presenter.initGroups();
        children = presenter.initChildrens();
        initView();
        presenter.setCartNum(allCheckbox.isChecked());
    }
  private void initView(){
      ExpandableListView listView = (ExpandableListView) findViewById(R.id.exListView);
      adapter = new ShopCartAdapter(groups,children,this);
      adapter.setCheckInterface(this);
      adapter.setModifyCountInterface(this);
      assert listView != null;
      listView.setAdapter(adapter);
      for (int i = 0; i < adapter.getGroupCount(); i++) {
          listView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
      }
      allCheckbox = (CheckBox) findViewById(R.id.all_chekbox);
      assert allCheckbox != null;
      allCheckbox.setOnClickListener(this);

      tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
      tvGoToPay = (TextView) findViewById(R.id.tv_go_to_pay);
      tvTitle = (TextView) findViewById(R.id.title);
      subtitle = (TextView) findViewById(R.id.subtitle);
      llCart = (LinearLayout) findViewById(R.id.ll_cart);
      cart_empty = (LinearLayout) findViewById(R.id.layout_cart_empty);
  }


    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        presenter.doIncrease((GoodsInfo) adapter.getChild(groupPosition,childPosition),showCountView);
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        presenter.doDecrease((GoodsInfo) adapter.getChild(groupPosition,childPosition),showCountView);
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        presenter.DeleteChild(groupPosition,childPosition);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        presenter.checkedGroup(groupPosition,isChecked);
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
       presenter.chechedChild(groupPosition,isChecked);
    }

    @Override
    public void showCurrentCount(int currentCount,View showCountView) {
        ((TextView) showCountView).setText(String.valueOf(currentCount));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAllCheck(boolean isAllCheck) {
        if (isAllCheck)
            allCheckbox.setChecked(true);
        else
            allCheckbox.setChecked(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showText(double totalPrice, int totalCount) {
        tvTotalPrice.setText(String.format("￥%.2f",totalPrice));
        tvGoToPay.setText(String.format("去支付(%d)",totalCount));
    }

    @Override
    public void showTitle(int count) {
        if (count == 0 ){
            tvTitle.setText("购物车" + "(" + count + ")");
            subtitle.setVisibility(View.GONE);
            llCart.setVisibility(View.GONE);
            cart_empty.setVisibility(View.VISIBLE);
        }else {
            tvTitle.setText("购物车" + "(" + count + ")");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.all_chekbox:
                presenter.doCheckAll(allCheckbox.isChecked() );
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
