package com.br.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.br.bean.ProductBean;
import com.br.generate.GenerateUtil;
import com.viniciusnaka.marketintegration.ProductCrudActivity;
import com.viniciusnaka.marketintegration.R;

/**
 * Created by vinicius on 4/13/14.
 */
public class ProductAdapter extends BaseAdapter{

    private List<ProductBean> productList;
    private Context context;
    private LayoutInflater layoutInflater;
    
    public ProductAdapter(Context tela, List<ProductBean> productList){
        // armazeno o contexto da tela e a lista com os dados que o listView precisa
        this.context = tela;
        this.productList = productList;
        // carrego o objeto que eh capaz de carrega XML e montar um objeto na memoria
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_product, null);
        }

        Button btnCartAdd, btnEditProduct;
        TextView txtName, txtPrice;
        ImageView imgProduct = (ImageView) convertView.findViewById(R.id.imageProduct);

        txtName = (TextView) convertView.findViewById(R.id.txtNameProduct);
        txtPrice = (TextView) convertView.findViewById(R.id.txtPriceListProduct);

        btnCartAdd = (Button) convertView.findViewById(R.id.btnAddProduct);
        btnEditProduct = (Button) convertView.findViewById(R.id.btnEditProduct);

        ProductBean productBeanLoad = productList.get(position);
        
        // imgBtnCartAdd.setTag(productBeanLoad);
        // imgBtnCartAdd.setOnClickListener(this);
        btnCartAdd.setTag(productBeanLoad);
        btnCartAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductBean productBeanLoad = (ProductBean) view.getTag();
                cartAdd(view, productBeanLoad);
            }
        });

        btnEditProduct.setTag(productBeanLoad);
        btnEditProduct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductBean productBeanLoad = (ProductBean) view.getTag();
                Intent it = new Intent(context, ProductCrudActivity.class);
                it.putExtra("product", productBeanLoad);
                context.startActivity(it);
            }
        });
        
        txtName.setText(productBeanLoad.getName());
        txtPrice.setText("R$ "+productBeanLoad.getPrice());
        int idImage = convertView.getResources().getIdentifier("com.viniciusnaka.marketintegration:drawable/"
                + productBeanLoad.getImg(), null, null);
        //imgProduct.setImageResource(Integer.parseInt(productBeanLoad.getImg().replaceAll("[\"]","")));
        imgProduct.setImageResource(idImage);

        return convertView;
    }

    public void cartAdd(View view, ProductBean productBeanLoad){
        GenerateUtil.cart.add(productBeanLoad);
        Toast.makeText(context, "Adicionado " + productBeanLoad.getName() + " ao Carrinho", Toast.LENGTH_SHORT).show();
    }

    public void quantityUp(View view, ProductBean productBeanLoad){
        if(productBeanLoad.getQuantity() <= productBeanLoad.getStock()) {
            productBeanLoad.setQuantity(productBeanLoad.getQuantity() + 1);
        } else {
            Toast.makeText(context, "Disponível em Estoque: " + productBeanLoad.getStock(), Toast.LENGTH_SHORT).show();
        }
    }

    public void quantityDown(View view, ProductBean productBeanLoad){
        if(productBeanLoad.getQuantity() > 1){
            productBeanLoad.setQuantity(productBeanLoad.getQuantity()-1);
        } else {
            Toast.makeText(context, "Quantidade mínima: 1", Toast.LENGTH_SHORT).show();
        }
    }

}
