package com.viniciusnaka.marketintegration;

import java.math.BigDecimal;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

import com.br.bean.CategoryBean;
import com.br.bean.ProductBean;
import com.br.dataBase.ProductDB;

public class ProductCrudActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_crud);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_crud, menu);
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
	public static class PlaceholderFragment extends Fragment implements OnClickListener {
		
		private EditText editName, editPrice, editStock, editImage;
		private Button btnSave;
        private Spinner spinnerCategories;
        private CategoryBean categoryBean;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_product_crud,
					container, false);

            try{
                editName = (EditText) rootView.findViewById(R.id.editText1);
                editPrice = (EditText) rootView.findViewById(R.id.editText2);
                editStock = (EditText) rootView.findViewById(R.id.editText3);
                editImage = (EditText) rootView.findViewById(R.id.editText4);
                spinnerCategories = (Spinner) rootView.findViewById(R.id.spinnerProductCrud);
                btnSave = (Button) rootView.findViewById(R.id.button1);

                ProductDB productDB = new ProductDB(getActivity());
                List<CategoryBean> categories = productDB.getCategories();
                categories.add(0, new CategoryBean(0, "Categoria"));
                ArrayAdapter arrayAdapterCategories = new ArrayAdapter<CategoryBean>(getActivity(), R.layout.support_simple_spinner_dropdown_item, categories);
                spinnerCategories.setAdapter(arrayAdapterCategories);
                spinnerCategories.setPromptId(R.string.spinner_categories);
                spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        categoryBean = (CategoryBean) spinnerCategories.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getActivity(), "É necessário selecionar uma Categoria", Toast.LENGTH_SHORT).show();
                    }

                });

                // update
                if(getActivity().getIntent().getSerializableExtra("product") != null){
                    ProductBean productBeanLoad = (ProductBean) getActivity().getIntent().getSerializableExtra("product");
                    editName.setText(productBeanLoad.getName());
                    editPrice.setText(productBeanLoad.getPrice().toString());
                    editStock.setText(productBeanLoad.getStock().toString());
                    editImage.setText(productBeanLoad.getImg().toString());
                    spinnerCategories.setSelection(categories.indexOf(productBeanLoad.getCategoryBean()));
                    btnSave.setTag(productBeanLoad);
                }

                btnSave.setOnClickListener(this);

                return rootView;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }

		}

		@Override
		public void onClick(View view) {
			
			boolean create = true;
			ProductBean productBean;
			
			if(view.getTag() != null){
				create = false;
				productBean = (ProductBean) view.getTag();
			} else {
				productBean = new ProductBean();
			}
			
			productBean.setName(editName.getText().toString());
			productBean.setPrice(new BigDecimal(Double.parseDouble(editPrice.getText().toString())));
			productBean.setStock(Integer.parseInt(editStock.getText().toString()));
			productBean.setImg(editImage.getText().toString());
            productBean.setCategoryBean(categoryBean);
			
			ProductDB productDB = new ProductDB(getActivity());
            productBean = productDB.salvar(productBean);
			if(productBean != null){
				Intent it = new Intent();
                it.putExtra("msg", create ? "Produto salvo com sucesso!" : "Produto atualizado com sucesso!");
                it.putExtra("product", productBean);
                getActivity().setResult(RESULT_OK, it);
                getActivity().finish();
			}else{
				Intent it = new Intent();
                it.putExtra("msg", create ? "Falha ao cadastrar o Produto!" : "Falha ao atualizar o Produto!");
                getActivity().setResult(RESULT_CANCELED, it);
                getActivity().finish();
			}
			
		}


	}

}
