package com.viniciusnaka.marketintegration;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ListView;
import com.br.adapter.ProductAdapter;
import com.br.adapter.ProductsCartAdapter;
import com.br.bean.ProductBean;
import com.br.dataBase.ProductDB;

import java.util.ArrayList;
import java.util.List;


public class ProductsCartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_cart);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

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

            //productBeanList = GenerateUtil.generateproductList();

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
}
