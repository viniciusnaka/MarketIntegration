package com.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.br.bean.ProductBean;
import com.br.generate.GenerateUtil;
import com.viniciusnaka.marketintegration.R;

import java.util.List;

/**
 * Created by vinicius on 4/13/14.
 */
public class CartAdapter extends BaseAdapter implements OnClickListener {

    private List<ProductBean> listProducts = GenerateUtil.cart;
    private Context context;
    private LayoutInflater inflater;
    
    public CartAdapter(Context tela){
        // armazeno o contexto da tela e a lista com os dados que o listView precisa
        this.context = tela;
        // carrego o objeto que eh capaz de carrega XML e montar um objeto na memoria
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private ChangeItens changeItens;
    
    public interface ChangeItens{
        public void onRemoveItem(ProductBean productBean);
        public void quantityUp(int position);
        public void quantityDown(int position);
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
        TextView txtNome = (TextView) convertView.findViewById(R.id.textView1);
        TextView txtQtde = (TextView) convertView.findViewById(R.id.textView2);        
        TextView txtPreco = (TextView) convertView.findViewById(R.id.textView3);
        ImageView imgProduct = (ImageView) convertView.findViewById(R.id.imageView1);
        ImageButton imgBtnUpQtde = (ImageButton) convertView.findViewById(R.id.imageButton1);
        ImageButton imgBtnDownQtde = (ImageButton) convertView.findViewById(R.id.imageButton2);
        ImageButton imgBtnCartExclude = (ImageButton) convertView.findViewById(R.id.imageButton3);

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
        	switch (view.getId()){
	        	case R.id.imageButton1:
	        		position = (Integer) view.getTag();
	        		changeItens.quantityUp(position);
                    break;
	        	case R.id.imageButton2:
	        		position = (Integer) view.getTag();
	        		changeItens.quantityDown(position);
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
