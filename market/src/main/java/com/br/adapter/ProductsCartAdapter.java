package com.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.br.bean.ProductBean;
import com.br.helper.AppHelper;
import com.viniciusnaka.marketintegration.R;

import java.util.List;

/**
 * Created by vinicius on 29/04/14.
 */
public class ProductsCartAdapter extends BaseAdapter {

    private List<ProductBean> productList;
    private Context context;
    private LayoutInflater layoutInflater;

    public ProductsCartAdapter(Context tela, List<ProductBean> productList){
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
            convertView = layoutInflater.inflate(R.layout.item_products_cart, null);
        }

        Button btnCartAdd;
        TextView txtName, txtPrice;
        ImageView imgProduct = (ImageView) convertView.findViewById(R.id.imgProductsCart);

        txtName = (TextView) convertView.findViewById(R.id.txtNameProductsCart);
        txtPrice = (TextView) convertView.findViewById(R.id.txtPriceProductsCart);
        btnCartAdd = (Button) convertView.findViewById(R.id.btnAddProductsCart);

        ProductBean productBeanLoad = productList.get(position);

        // imgBtnCartAdd.setTag(productBeanLoad);
        // imgBtnCartAdd.setOnClickListener(this);
        btnCartAdd.setTag(productBeanLoad);
        btnCartAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductBean productBeanLoad = (ProductBean) view.getTag();
                cartAdd(view, productBeanLoad);
            }
        });

        txtName.setText(productBeanLoad.getName());
        txtPrice.setText(productBeanLoad.getPrice().toString());
        int idImage = convertView.getResources().getIdentifier("com.viniciusnaka.marketintegration:drawable/"
                + productBeanLoad.getImg(), null, null);
        //imgProduct.setImageResource(Integer.parseInt(productBeanLoad.getImg().replaceAll("[\"]","")));
        imgProduct.setImageResource(idImage);

        return convertView;

    }

    public void cartAdd(View view, ProductBean productBeanLoad){
        AppHelper.cart.add(productBeanLoad);
        Toast.makeText(context, "Adicionado " + productBeanLoad.getName() + " ao Carrinho", Toast.LENGTH_SHORT).show();
    }

}
