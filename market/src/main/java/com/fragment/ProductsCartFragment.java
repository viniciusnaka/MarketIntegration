package com.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.adapter.ProductsCartAdapter;
import com.bean.ProductBean;
import com.dataBase.ProductDB;
import com.viniciusnaka.marketintegration.CartActivity;
import com.viniciusnaka.marketintegration.R;

import java.util.ArrayList;
import java.util.List;


public class ProductsCartFragment extends Fragment {

    private List<ProductBean> productBeanList = new ArrayList<ProductBean>();
    private ListView listViewProductsCart;
    private Button btnCart;
    private ProductDB productDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products_cart, container, false);

        listViewProductsCart = (ListView) rootView.findViewById(R.id.listViewProductsCart);
        // pego os dados que aparecerao na tela
        productDB = new ProductDB(rootView.getContext());
        productBeanList = productDB.getProducts();

        //productBeanList = AppHelper.generateproductList();

        btnCart = (Button) rootView.findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(view.getContext(), CartActivity.class);
                startActivity(it);
            }
        });

        // monto o adapter com o contexto da tela e os dados para o listView
        ProductsCartAdapter adapter = new ProductsCartAdapter(rootView.getContext(), productBeanList);
        // passo o adapter que fara o listView funcionar
        listViewProductsCart.setAdapter(adapter);

        return rootView;
    }
}

