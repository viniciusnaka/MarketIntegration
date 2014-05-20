package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.bean.ProductBean;
import com.helper.AppHelper;
import com.viniciusnaka.marketintegration.R;

import java.util.List;

/**
 * Created by vinicius on 4/13/14.
 */
public class CartAdapter extends BaseAdapter implements OnClickListener {

    private List<ProductBean> listProducts = AppHelper.cart;
    private Context context;
    private LayoutInflater inflater;
    private TextView txtNome, txtQtde, txtPreco;
    private ImageView imgProduct;
    private ImageButton imgBtnUpQtde, imgBtnDownQtde, imgBtnCartExclude;


    public CartAdapter(Context tela){
        // armazeno o contexto da tela e a lista com os dados que o listView precisa
        this.context = tela;
        // carrego o objeto que eh capaz de carrega XML e montar um objeto na memoria
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private ChangeItens changeItens;
    
    public interface ChangeItens{
        public void onRemoveItem(ProductBean productBean);
        public boolean quantityUp(int position);
        public boolean quantityDown(int position);
    } 
    
    public void setChangeItens(ChangeItens changeItens) {
		this.changeItens = changeItens;
	}

	@Override
    public int getCount() {
        return listProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return listProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // se a variavel convertView veio nula significa que o listView nao tinha um item
        // saindo da tela para eu reaproveitar, nesse caso eu preciso alocar um novo
        // item carregando do XML
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_cart, null);
        }

        // agora eu tenho certamente em convertView um objeto com todas as caracteristicas
        // do XML, puxo cada sub campo que eu necessite colocar dados
        txtNome = (TextView) convertView.findViewById(R.id.textView1);
        txtQtde = (TextView) convertView.findViewById(R.id.textView2);
        txtPreco = (TextView) convertView.findViewById(R.id.textView3);
        imgProduct = (ImageView) convertView.findViewById(R.id.imageView1);
        imgBtnUpQtde = (ImageButton) convertView.findViewById(R.id.imageButton1);
        imgBtnUpQtde.setImageResource(R.drawable.arrow_up);
        imgBtnDownQtde = (ImageButton) convertView.findViewById(R.id.imageButton2);
        imgBtnDownQtde.setImageResource(R.drawable.arrow_down);
        imgBtnCartExclude = (ImageButton) convertView.findViewById(R.id.imageButton3);
        imgBtnCartExclude.setImageResource(R.drawable.shopcartexclude);

        // puxo os dados do item que o listView pediu e preencho os dados do item
        ProductBean productBeanLoad = listProducts.get(position);

        imgBtnUpQtde.setTag(position);
        imgBtnUpQtde.setOnClickListener(this);
        
        imgBtnDownQtde.setTag(position);
        imgBtnDownQtde.setOnClickListener(this);
        
        imgBtnCartExclude.setTag(productBeanLoad);
        imgBtnCartExclude.setOnClickListener(this);

        txtNome.setText(productBeanLoad.getName());
        txtQtde.setText(productBeanLoad.getQuantity().toString());
        txtPreco.setText(productBeanLoad.getPrice().toString());
        int idImage = convertView.getResources().getIdentifier("com.viniciusnaka.marketintegration:drawable/"
                + productBeanLoad.getImg(), null, null);
        imgProduct.setImageResource(idImage);

        return convertView;

    }

    @Override
    public void onClick(View view) {
        if(changeItens != null){
        	int position;
            Boolean quantity;
        	switch (view.getId()){
	        	case R.id.imageButton1:
	        		position = (Integer) view.getTag();
	        		quantity = changeItens.quantityUp(position);
                    if(!quantity){
                        Toast.makeText(context, "Quantidade máxima disponível do produto", Toast.LENGTH_SHORT).show();
                    }
                    imgBtnUpQtde.setEnabled(quantity);
                    break;
	        	case R.id.imageButton2:
	        		position = (Integer) view.getTag();
	        		quantity = changeItens.quantityDown(position);
                    if(!quantity){
                        Toast.makeText(context, "Quantidade mínima: 1", Toast.LENGTH_SHORT).show();
                    }
                    imgBtnDownQtde.setEnabled(quantity);
                    break;
	        	case R.id.imageButton3:
	        		ProductBean productBeanLoad = (ProductBean) view.getTag();
	        		changeItens.onRemoveItem(productBeanLoad);
                    break;
        	}
            notifyDataSetChanged();
        }
    }
    
}
