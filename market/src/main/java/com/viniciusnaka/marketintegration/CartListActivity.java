package com.viniciusnaka.marketintegration;

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
import android.widget.ListView;
import android.widget.TextView;
import com.br.adapter.CartAdapter;
import com.br.adapter.CartListAdapter;
import com.br.bean.CartBean;
import com.br.dataBase.CartDB;

import java.math.BigDecimal;
import java.util.List;


public class CartListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_list, menu);
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

        private List<CartBean> cartBeanList;
        private ListView listViewCarts;
        private TextView txtTotalPriceCarts;
        private CartDB cartDB;

        public PlaceholderFragment() {
        }

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
}
