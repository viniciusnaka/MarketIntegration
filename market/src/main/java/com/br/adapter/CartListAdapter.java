package com.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.br.bean.CartBean;
import com.viniciusnaka.marketintegration.R;

import java.util.List;

/**
 * Created by vinicius on 26/04/14.
 */
public class CartListAdapter extends BaseAdapter {

    private List<CartBean> cartList;
    private Context context;
    private LayoutInflater layoutInflater;

    public CartListAdapter(Context context, List<CartBean> cartList){
        this.context = context;
        this.cartList = cartList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_cartlist, null);
        }

        ImageButton imgBtnCartList;
        TextView txtIdCartList, txtDateCreateCartList, txtPriceCartList;
        ImageView imgViewCartList;

        imgBtnCartList = (ImageButton) convertView.findViewById(R.id.imgBtnCartList);
        txtIdCartList = (TextView) convertView.findViewById(R.id.txtIdCartList);
        txtDateCreateCartList = (TextView) convertView.findViewById(R.id.txtDateCreateCartList);
        txtPriceCartList = (TextView) convertView.findViewById(R.id.txtPriceCartList);
        imgViewCartList = (ImageView) convertView.findViewById(R.id.imgViewCartList);

        CartBean cartBeanLoad = cartList.get(position);

        imgViewCartList.setImageResource(R.drawable.ic_launcher);
        txtIdCartList.setText(cartBeanLoad.getId().toString());
        txtDateCreateCartList.setText("26/04/2014");
        txtPriceCartList.setText(cartBeanLoad.getPrice().toString());
        imgBtnCartList.setImageResource(R.drawable.ic_launcher);

        return convertView;
    }



}
