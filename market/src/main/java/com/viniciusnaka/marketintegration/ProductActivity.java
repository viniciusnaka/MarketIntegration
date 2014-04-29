package com.viniciusnaka.marketintegration;

import android.content.Intent;	
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;
import com.br.adapter.ProductAdapter;
import com.br.bean.ProductBean;
import com.br.dataBase.ProductDB;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.containerProduct, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product, menu);
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
        private ListView listViewProdutos;
        private Button btnCart, btnAdd, btnCartList;
        private ProductDB productDB;
        private static final int PAGE_ADD = 1;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_product, container,
                    false);

            listViewProdutos = (ListView) rootView.findViewById(R.id.listView1);
            // pego os dados que aparecerao na tela
            productDB = new ProductDB(rootView.getContext());
            productBeanList = productDB.getProducts();

            //productBeanList = GenerateUtil.generateproductList();

            btnCart = (Button) rootView.findViewById(R.id.btnCart);
            btnCart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(view.getContext(), CartActivity.class);
                    startActivity(it);
                }
            });

            btnAdd = (Button) rootView.findViewById(R.id.btnAddProduct);
            btnAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(view.getContext(), ProductCrudActivity.class);
                    startActivityForResult(it, PAGE_ADD);
                }
            });

            btnCartList = (Button) rootView.findViewById(R.id.btnCartList);
            btnCartList.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(view.getContext(), CartListActivity.class);
                    startActivity(it);
                }
            });

            // monto o adapter com o contexto da tela e os dados para o listView
            ProductAdapter adapter = new ProductAdapter(rootView.getContext(), productBeanList);
            // passo o adapter que fara o listView funcionar
            listViewProdutos.setAdapter(adapter);

            // trato clique nos itens da lista
            //listViewProdutos.setOnItemClickListener(this);

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent it) {
            if (requestCode == PAGE_ADD){
                Bundle param = it != null ? it.getExtras() : null;
                if(param != null){
                    Toast.makeText(getActivity(), param.get("msg").toString() , Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            if(listViewProdutos != null){
                productBeanList = productDB.getProducts();
                ProductAdapter adapter = new ProductAdapter(getActivity(), productBeanList);
                listViewProdutos.setAdapter(adapter);
            }
        }
    }
}
