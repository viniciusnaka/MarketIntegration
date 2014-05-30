package com.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.adapter.CartListAdapter;
import com.bean.CartBean;
import com.dataBase.CartDB;
import com.viniciusnaka.marketintegration.R;

import java.math.BigDecimal;
import java.util.List;


public class CartListFragment extends Fragment {

    private List<CartBean> cartBeanList;
    private ListView listViewCarts;
    private TextView txtTotalPriceCarts;
    private CartDB cartDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cartlist, container, false);

        cartDB = new CartDB(rootView.getContext());
        cartBeanList = cartDB.getCarts();

        txtTotalPriceCarts = (TextView) rootView.findViewById(R.id.txtTotalPriceCarts);
        txtTotalPriceCarts.setText("R$: "+getPriceCarts(cartBeanList).toString());
        listViewCarts = (ListView) rootView.findViewById(R.id.listViewCartList);

        // monto o adapter com o contexto da tela e os dados para o listView
        CartListAdapter adapter = new CartListAdapter(getActivity(), cartBeanList);
        listViewCarts.setAdapter(adapter);

        return rootView;
    }

    public BigDecimal getPriceCarts(List<CartBean> cartBeanList){
        BigDecimal price = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        for(CartBean cartBean : cartBeanList){
            price = price.add(cartBean.getPrice());
        }
        return price;
    }
}
