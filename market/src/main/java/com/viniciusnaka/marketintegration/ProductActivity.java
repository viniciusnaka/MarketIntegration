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
	public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        public PlaceholderFragment() {
        }

        private List<ProductBean> productBeanList;
        private ListView listViewProdutos;
        private Button btnAdd;
        private ProductDB productDB;
        private static final int PAGE_ADD = 1;
        private ProductAdapter adapter;

        public ProductAdapter getAdapter() {
            return adapter;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_product, container,
                    false);

            listViewProdutos = (ListView) rootView.findViewById(R.id.listViewProduct);
            // pego os dados que aparecerao na tela
            productDB = new ProductDB(rootView.getContext());
            productBeanList = productDB.getProducts();
            btnAdd = (Button) rootView.findViewById(R.id.btnAddProduct);

            // monto o adapter com o contexto da tela e os dados para o listView
            adapter = new ProductAdapter(getActivity(), productBeanList);

            adapter.setChangeProduct(new ProductAdapter.ChangeProduct() {
                @Override
                public void onDeleteProduct(ProductBean productBeanLoad) {
                    if(productDB.delete(productBeanLoad)){
                        Toast.makeText(getActivity(), "Produto "+productBeanLoad.getName() + " deletado com sucesso!",
                                Toast.LENGTH_SHORT).show();
                        productBeanList.remove(productBeanLoad);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Erro ao deletar o Produto "+productBeanLoad.getName()+ ".",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnAdd.setOnClickListener(this);

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
                    ProductBean productBean = (ProductBean) param.get("product");
                    productBeanList.add(productBean);
                    listViewProdutos.setAdapter(adapter = new ProductAdapter(getActivity(), productBeanList));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), param.get("msg").toString() , Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        /*@Override
        public void onResume() {
            super.onResume();
            if(listViewProdutos != null){
                productBeanList = productDB.getProducts();
                ProductAdapter adapter = new ProductAdapter(getActivity(), productBeanList);
                listViewProdutos.setAdapter(adapter);
            }
        }*/

        @Override
        public void onClick(View view) {
            Intent it = new Intent(view.getContext(), ProductCrudActivity.class);
            startActivityForResult(it, PAGE_ADD);
        }
    }
}
