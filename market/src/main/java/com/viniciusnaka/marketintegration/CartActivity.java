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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.br.adapter.CartAdapter;
import com.br.bean.CartBean;
import com.br.bean.ProductBean;
import com.br.dataBase.CartDB;
import com.br.dataBase.ProductDB;
import com.br.generate.GenerateUtil;


public class CartActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.containerCart, new PlaceholderFragment()).commit();
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);
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

        private List<ProductBean> listProducts;
        private ListView listViewProdutos;
        private TextView txtValue, txtQtdeItens, txtCurrency;
        private Button btnBuy;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cart,
                    container, false);

            listViewProdutos = (ListView) rootView.findViewById(R.id.listViewCart);
            txtQtdeItens = (TextView) rootView.findViewById(R.id.txtQtde);
            txtValue = (TextView) rootView.findViewById(R.id.txtValue);
            txtCurrency = (TextView) rootView.findViewById(R.id.txtCurrency);
            btnBuy = (Button) rootView.findViewById(R.id.btnBuy);

            // pego os dados que aparecerao na tela
            listProducts = GenerateUtil.cart;

            // monto o adapter com o contexto da tela e os dados para o listView
            CartAdapter adapter = new CartAdapter(getActivity());
            
            adapter.setChangeItens(new CartAdapter.ChangeItens() {
				
				@Override
				public void quantityUp(int position) {
					ProductBean productBeanLoad = GenerateUtil.cart.get(position);
					productBeanLoad.setQuantity(productBeanLoad.getQuantity()+1);
					txtQtdeItens.setText("Qtde Itens("+ GenerateUtil.getQtdeItens() +")");
                    txtValue.setText(GenerateUtil.getValueCart().toString());
					//return GenerateUtil.getValueCart();
				}
				
				@Override
				public void quantityDown(int position) {
					ProductBean productBeanLoad = GenerateUtil.cart.get(position);
					if(productBeanLoad.getQuantity() > 1){
			            productBeanLoad.setQuantity(productBeanLoad.getQuantity()-1);
			            txtQtdeItens.setText("Qtde Itens("+ GenerateUtil.getQtdeItens() +")");
			            txtValue.setText(GenerateUtil.getValueCart().toString());
			        } else {
			            Toast.makeText(getActivity(), "Quantidade mínima: 1", Toast.LENGTH_SHORT).show();
			        }										
					//return GenerateUtil.getValueCart();
				}
				
				@Override
				public void onRemoveItem(ProductBean productBean) {
					GenerateUtil.cart.remove(productBean);
					txtQtdeItens.setText("Qtde Itens("+ GenerateUtil.getQtdeItens() +")");
                    txtValue.setText(GenerateUtil.getValueCart().toString());
			        Toast.makeText(getActivity(), "Produto " + productBean.getName() + " removido do Carrinho", Toast.LENGTH_SHORT).show();
					//return GenerateUtil.getValueCart();
				}
			});
            
            txtQtdeItens.setText("Qtde Itens("+ GenerateUtil.getQtdeItens() +")");
            txtCurrency.setText("R$: ");
            txtValue.setText(GenerateUtil.getValueCart().toString());

            btnBuy.setOnClickListener(this);

            // passo o adapter que fara o listView funcionar
            listViewProdutos.setAdapter(adapter);

            return rootView;
        }


        @Override
        public void onClick(View v) {
            CartBean cartBean = new CartBean();
            cartBean.setPrice(new BigDecimal(txtValue.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            cartBean.setProductList(listProducts);

            CartDB cartDB = new CartDB(getActivity());
            if(cartDB.salvar(cartBean)){
                ProductDB productDB = new ProductDB(getActivity());
                productDB.updateProductAfterCart(cartBean.getProductList());

                Toast.makeText(getActivity(), "Compra efetuada com sucesso!", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(getActivity(), CartListActivity.class);
                startActivity(it);
                getActivity().finish();

            } else {
                Toast.makeText(getActivity(), "Erro ao tentar efetuar a compra!", Toast.LENGTH_SHORT).show();
            }

        }
    }

}