package com.chenyong.jeff.myshoppingcart.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenyong.jeff.myshoppingcart.R;
import com.chenyong.jeff.myshoppingcart.model.bean.GoodsInfo;
import com.chenyong.jeff.myshoppingcart.model.bean.StoreInfo;

import java.util.List;
import java.util.Map;

/**
 * 适配器
 * Created by Administrator on 2016/8/25.
 */
public class ShopCartAdapter extends BaseExpandableListAdapter {
    private List<StoreInfo> groups;
    private Map<String, List<GoodsInfo>> children;
    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
//    private GroupEdtorListener mListener;

    public ShopCartAdapter(List<StoreInfo> groups, Map<String, List<GoodsInfo>> children, Context context) {
        this.groups = groups;
        this.children = children;
        this.context = context;
    }
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }
//    public void setmListener(GroupEdtorListener mListener) {
//        this.mListener = mListener;
//    }
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }
    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {

        return children.get(groups.get(i).getId()).size();
    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return children.get(groups.get(i).getId()).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View convertView, ViewGroup viewGroup) {
        final GroupViewHolder gholder;
        if (convertView == null) {
            gholder = new GroupViewHolder();
            convertView = View.inflate(context, R.layout.item_shopcart_group, null);
            gholder.cb_check = (CheckBox) convertView.findViewById(R.id.determine_chekbox);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_source_name);
            gholder.store_edtor = (Button) convertView.findViewById(R.id.tv_store_edtor);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(i);

        gholder.tv_group_name.setText(group.getName());
        // 复选框勾选的效果
        gholder.cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(i, ((CheckBox) v).isChecked());// 暴露组选接口
            }
        });
        gholder.cb_check.setChecked(group.isChoosed());
        if (group.isEdtor()) {
            gholder.store_edtor.setText("完成");
        } else {
            gholder.store_edtor.setText("编辑");
        }
        gholder.store_edtor.setOnClickListener(new GroupViewClick(i,gholder.store_edtor,group));
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildViewHolder cholder;
        if (convertView == null) {
            cholder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.item_shopcart_product, null);
            cholder.cb_check = (CheckBox) convertView.findViewById(R.id.check_box);
            cholder.tv_product_desc = (TextView) convertView.findViewById(R.id.tv_intro);
            cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            cholder.iv_increase = (TextView) convertView.findViewById(R.id.tv_add);
            cholder.iv_decrease = (TextView) convertView.findViewById(R.id.tv_reduce);
            cholder.tv_count = (TextView) convertView.findViewById(R.id.tv_num);
            cholder.rl_no_edtor = (RelativeLayout) convertView.findViewById(R.id.rl_no_edtor);
            cholder.tv_buy_num = (TextView) convertView.findViewById(R.id.tv_buy_num);
            cholder.ll_edtor = (LinearLayout) convertView.findViewById(R.id.ll_edtor);
            cholder.tv_colorsize = (TextView) convertView.findViewById(R.id.tv_colorsize);
            cholder.tv_goods_delete = (TextView) convertView.findViewById(R.id.tv_goods_delete);
            cholder.iv_adapter_list_pic= (ImageView) convertView.findViewById(R.id.iv_adapter_list_pic);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag();
        }
        if (groups.get(i).isEdtor()) {
            cholder.ll_edtor.setVisibility(View.VISIBLE);
            cholder.rl_no_edtor.setVisibility(View.GONE);
        } else {
            cholder.ll_edtor.setVisibility(View.GONE);
            cholder.rl_no_edtor.setVisibility(View.VISIBLE);
        }
        final GoodsInfo goodsInfo = (GoodsInfo) getChild(i, i1);
        if (goodsInfo != null) {
            cholder.tv_product_desc.setText(goodsInfo.getName());
            cholder.tv_price.setText("￥" + goodsInfo.getPrice() + "");
            cholder.tv_count.setText(goodsInfo.getCount() + "");
            cholder.iv_adapter_list_pic.setImageResource(goodsInfo.getGoodsImg());
            cholder.tv_buy_num.setText("x" + goodsInfo.getCount());
            cholder.cb_check.setChecked(goodsInfo.isChoosed());

            cholder.cb_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsInfo.setChoosed(((CheckBox) v).isChecked());
                    cholder.cb_check.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(i, i1, ((CheckBox) v).isChecked());// 暴露子选接口
                }
            });
            //编辑 加减数量
            cholder.iv_increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(i, i1, cholder.tv_count, cholder.cb_check.isChecked());// 暴露增加接口
                }
            });
            cholder.iv_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(i, i1, cholder.tv_count, cholder.cb_check.isChecked());// 暴露删减接口
                }
            });
            //删除 购物车
            cholder.tv_goods_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alert = new AlertDialog.Builder(context).create();
                    alert.setTitle("操作提示");
                    alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    return;
                                }
                            });
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modifyCountInterface.childDelete(i, i1);

                                }
                            });
                    alert.show();

                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    /**
     * 商家组元素
     */
    private class GroupViewHolder {
        CheckBox cb_check;
        TextView tv_group_name;
        Button store_edtor;
    }
    /**
     * 子元素绑定器
     */
    private class ChildViewHolder {
        CheckBox cb_check;
        ImageView iv_adapter_list_pic;
        TextView tv_product_name;
        TextView tv_product_desc;
        TextView tv_price;
        TextView iv_increase;
        TextView tv_count;
        TextView iv_decrease;
        RelativeLayout rl_no_edtor;
        TextView tv_buy_num;
        LinearLayout ll_edtor;
        TextView tv_colorsize;
        TextView tv_goods_delete;
    }


    /**
     * 使某个组处于编辑状态
     * <p>
     * groupPosition组的位置
     */
    class GroupViewClick implements View.OnClickListener {
        private int groupPosition;
        private Button edtor;
        private StoreInfo group;

        public GroupViewClick(int groupPosition, Button edtor, StoreInfo group) {
            this.groupPosition = groupPosition;
            this.edtor = edtor;
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            int groupId = v.getId();
            if (groupId == edtor.getId()) {
                if (group.isEdtor()) {
                    group.setIsEdtor(false);
                } else {
                    group.setIsEdtor(true);

                }
                notifyDataSetChanged();
            }
        }
    }


    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }
    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子item
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }
}
