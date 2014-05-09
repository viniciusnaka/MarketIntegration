package com.br.helper;

import com.br.bean.ProductBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinicius on 4/12/14.
 */
public class AppHelper {

    // guarda na memoria todos os produtos do carrinho
    public static List<ProductBean> cart = new ArrayList<ProductBean>();

    public static String getClassError(Class<?> tClass){
        return "ERROR - " + tClass.getSimpleName();
    }

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

    public static List<String> getStates(){
        List<String> states = new ArrayList<String>();

        states.add("Estado");
        states.add("AC");
        states.add("AL");
        states.add("AP");
        states.add("AM");
        states.add("BA");
        states.add("CE");
        states.add("DF");
        states.add("ES");
        states.add("GO");
        states.add("MA");
        states.add("MT");
        states.add("MS");
        states.add("MG");
        states.add("PA");
        states.add("PB");
        states.add("PR");
        states.add("PE");
        states.add("PI");
        states.add("RJ");
        states.add("RN");
        states.add("RS");
        states.add("RO");
        states.add("RR");
        states.add("SC");
        states.add("SP");
        states.add("SE");
        states.add("TO");

        return states;
    }

}
