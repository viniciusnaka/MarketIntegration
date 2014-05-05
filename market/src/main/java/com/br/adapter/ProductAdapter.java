package com.br.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.br.bean.ProductBean;
import com.viniciusnaka.marketintegration.ProductCrudActivity;
import com.viniciusnaka.marketintegration.R;

import java.util.List;

/**
 * Created by vinicius on 4/13/14.
 */
public class ProductAdapter extends BaseAdapter implements OnClickListener {

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

    private ChangeProduct changeProduct;

    public interface ChangeProduct{
        public void onDeleteProduct(ProductBean productBean);
        public void onEditProduct(ProductBean productBean);
    }

    public void setChangeProduct(ChangeProduct changeProduct) {
        this.changeProduct = changeProduct;
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

        TextView txtName = (TextView) convertView.findViewById(R.id.txtNameProduct);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPriceProduct);
        TextView txtQtde = (TextView) convertView.findViewById(R.id.txtQtdeProduct);

        ImageButton imgEditProduct = (ImageButton) convertView.findViewById(R.id.imgEditProduct);
        ImageButton imgDeleteProduct = (ImageButton) convertView.findViewById(R.id.imgDeleteProduct);

        ProductBean productBeanLoad = productList.get(position);

        imgEditProduct.setTag(productBeanLoad);
        imgEditProduct.setOnClickListener(this);

        imgDeleteProduct.setTag(productBeanLoad);
        imgDeleteProduct.setOnClickListener(this);

        ImageView imgProduct = (ImageView) convertView.findViewById(R.id.imgProduct);
        int idImage = convertView.getResources().getIdentifier("com.viniciusnaka.marketintegration:drawable/"
                + productBeanLoad.getImg(), null, null);
        imgProduct.setImageResource(idImage);

        txtName.setText(productBeanLoad.getName());
        txtQtde.setText(productBeanLoad.getStock().toString());
        txtPrice.setText(productBeanLoad.getPrice().toString());

        return convertView;
    }

    @Override
    public void onClick(View view) {
        if(changeProduct != null) {
            ProductBean productBeanLoad = (ProductBean) view.getTag();
            switch (view.getId()) {
                case R.id.imgDeleteProduct:
                    changeProduct.onDeleteProduct(productBeanLoad);
                    break;
                case R.id.imgEditProduct:
                    changeProduct.onEditProduct(productBeanLoad);
                    break;
            }
        }
    }

}
