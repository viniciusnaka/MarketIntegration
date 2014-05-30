package com.viniciusnaka.marketintegration;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.adapter.CartAdapter;
import com.bean.CartBean;
import com.bean.ProductBean;
import com.dataBase.CartDB;
import com.dataBase.ProductDB;
import com.fragment.CartListFragment;
import com.helper.AppHelper;

import java.math.BigDecimal;
import java.util.List;


public class CartActivity extends ActionBarActivity implements View.OnClickListener{

    private List<ProductBean> listProducts;
    private ListView listViewProdutos;
    private TextView txtValue, txtQtdeItens, txtCurrency;
    private Button btnBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);

        listViewProdutos = (ListView) findViewById(R.id.listViewCart);
        txtQtdeItens = (TextView) findViewById(R.id.txtQtde);
        txtValue = (TextView) findViewById(R.id.txtValue);
        txtCurrency = (TextView) findViewById(R.id.txtCurrency);
        btnBuy = (Button) findViewById(R.id.btnBuy);

        // pego os dados que aparecerao na tela
        listProducts = AppHelper.cart;

        // monto o adapter com o contexto da tela
        CartAdapter adapter = new CartAdapter(this);

        adapter.setChangeItens(new CartAdapter.ChangeItens() {

            @Override
            public boolean quantityUp(int position) {
                boolean quantityOK = true;
                ProductBean productBeanLoad = AppHelper.cart.get(position);
                if(productBeanLoad.getQuantity() < productBeanLoad.getStock()){
                    productBeanLoad.setQuantity(productBeanLoad.getQuantity()+1);
                    txtQtdeItens.setText("Qtde Itens("+ AppHelper.getQtdeItens() +")");
                    txtValue.setText(AppHelper.getValueCart().toString());
                } else {
                    quantityOK = false;
                }
                return quantityOK;
            }

            @Override
            public boolean quantityDown(int position) {
                boolean quantityOK = true;
                ProductBean productBeanLoad = AppHelper.cart.get(position);
                if(productBeanLoad.getQuantity() > 1){
                    productBeanLoad.setQuantity(productBeanLoad.getQuantity()-1);
                    txtQtdeItens.setText("Qtde Itens("+ AppHelper.getQtdeItens() +")");
                    txtValue.setText(AppHelper.getValueCart().toString());
                } else {
                    quantityOK = false;
                }
                return quantityOK;
            }

            @Override
            public void onRemoveItem(ProductBean productBean) {
                AppHelper.cart.remove(productBean);
                txtQtdeItens.setText("Qtde Itens("+ AppHelper.getQtdeItens() +")");
                txtValue.setText(AppHelper.getValueCart().toString());
                Toast.makeText(getApplicationContext(), "Produto " + productBean.getName() + " removido do Carrinho", Toast.LENGTH_SHORT).show();
                //return AppHelper.getValueCart();
            }
        });

        txtQtdeItens.setText("Qtde Itens("+ AppHelper.getQtdeItens() +")");
        txtCurrency.setText("R$: ");
        txtValue.setText(AppHelper.getValueCart().toString());

        btnBuy.setOnClickListener(this);

        // passo o adapter que fara o listView funcionar
        listViewProdutos.setAdapter(adapter);

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

    @Override
    public void onClick(View v) {
        CartBean cartBean = new CartBean();
        cartBean.setPrice(new BigDecimal(txtValue.getText().toString()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        cartBean.setProductList(listProducts);

        CartDB cartDB = new CartDB(this);
        if(cartDB.salvar(cartBean)){
            ProductDB productDB = new ProductDB(this);
            productDB.updateProductAfterCart(cartBean.getProductList());

            Toast.makeText(this, "Compra efetuada com sucesso!", Toast.LENGTH_SHORT).show();

            this.finish();

        } else {
            Toast.makeText(this, "Erro ao tentar efetuar a compra!", Toast.LENGTH_SHORT).show();
        }

    }
}

