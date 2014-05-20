package com.viniciusnaka.marketintegration;

import android.content.Intent;	
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;

import com.adapter.ProductAdapter;
import com.bean.CategoryBean;
import com.bean.ProductBean;
import com.dataBase.ProductDB;

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
        private static final int PAGE_EDIT = 2;
        //private ProductAdapter adapter;
        private Spinner spinnerCategories;

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
            spinnerCategories = (Spinner) rootView.findViewById(R.id.spinnerCategories);

            // monto o adapter com o contexto da tela e os dados para o listView
            ProductAdapter adapter = createProductAdapter();
            //new ProductAdapter(getActivity(), productBeanList);

            ArrayAdapter arrayAdapterCategories = new ArrayAdapter<CategoryBean>(getActivity(), android.R.layout.simple_spinner_item, productDB.getCategories());
            arrayAdapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategories.setAdapter(arrayAdapterCategories);

            // change sort by category
            spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CategoryBean categoryBeanLoad = (CategoryBean) spinnerCategories.getSelectedItem();
                    getProductBeanListByCategory(categoryBeanLoad);
                    ProductAdapter adapter = createProductAdapter();
                            //new ProductAdapter(getActivity(), productBeanList);
                    listViewProdutos.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            btnAdd.setOnClickListener(this);

            // passo o adapter que fara o listView funcionar
            listViewProdutos.setAdapter(adapter);

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent it) {
            Bundle param = it != null ? it.getExtras() : null;
            if(param != null){
                ProductBean productBeanLoad = (ProductBean) param.get("product");
                ProductAdapter adapter = (ProductAdapter) listViewProdutos.getAdapter();
                // insert
                if(requestCode == PAGE_ADD){
                    productBeanList.add(productBeanLoad);
                } else {
                    // update
                    if(!productBeanList.get(0).getCategoryBean().equals(productBeanLoad.getCategoryBean())){
                        for(ProductBean p : productBeanList){
                            if(p.getId().equals(productBeanLoad.getId())){
                                productBeanList.remove(p);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), param.get("msg").toString() , Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onClick(View view) {
            Intent it = new Intent(view.getContext(), ProductCrudActivity.class);
            startActivityForResult(it, PAGE_ADD);
        }

        private void getProductBeanListByCategory(CategoryBean categoryBean){
            productBeanList = new ArrayList<ProductBean>();
            for (ProductBean productBeanLoad : productDB.getProducts()){
                if(productBeanLoad.getCategoryBean().equals(categoryBean)){
                    productBeanList.add(productBeanLoad);
                }
            }
        }

        private ProductAdapter createProductAdapter(){
            ProductAdapter adapter = new ProductAdapter(getActivity(), productBeanList);
            // delete product
            adapter.setChangeProduct(new ProductAdapter.ChangeProduct() {
                @Override
                public void onDeleteProduct(ProductBean productBeanLoad) {
                    if(productDB.delete(productBeanLoad)){
                        productBeanList.remove(productBeanLoad);
                        ProductAdapter adapter = (ProductAdapter) listViewProdutos.getAdapter(); //new ProductAdapter(getActivity(), productBeanList);
                        //listViewProdutos.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Produto "+productBeanLoad.getName() + " deletado com sucesso!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Erro ao deletar o Produto "+productBeanLoad.getName()+ ".",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onEditProduct(ProductBean productBeanLoad) {
                    Intent it = new Intent(getActivity(), ProductCrudActivity.class);
                    it.putExtra("product", productBeanLoad);
                    startActivityForResult(it, PAGE_EDIT);
                }
            });
            return adapter;
        }
    }
}
