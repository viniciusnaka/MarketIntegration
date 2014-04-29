package com.br.generate;

import com.br.bean.CategoryBean;
import com.br.bean.ProductBean;
import com.viniciusnaka.marketintegration.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinicius on 4/12/14.
 */
public class GenerateUtil {

    // guarda na memoria todos os produtos do carrinho
    public static List<ProductBean> cart = new ArrayList<ProductBean>();
    public static List<CategoryBean> categories = new ArrayList<CategoryBean>();

    public static BigDecimal getValueCart(){
    	BigDecimal value = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);
    	for(ProductBean p : cart){
    		for(int i=1; i <= p.getQuantity(); i++){
    			value = value.add(p.getPrice());
    		}    		
    	}
    	return value;
    }
    
    public static int getQtdeItens(){
    	int qtdeItens = 0;
    	for(ProductBean p : cart){
    		for(int i=1; i <= p.getQuantity(); i++){
    			qtdeItens += 1;
    		}    		
    	}
    	return qtdeItens;
    }

}
