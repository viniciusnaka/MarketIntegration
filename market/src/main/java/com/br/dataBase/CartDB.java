package com.br.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.br.bean.CartBean;
import com.br.bean.CategoryBean;
import com.br.bean.ProductBean;
import com.br.bean.UserBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vinicius on 26/04/14.
 */
public class CartDB {

    private SQLiteDatabase sqlLiteDatabase;

    public CartDB(Context context){
        // uso o bancoHelper para criar um objeto que acessa o banco de dados
        sqlLiteDatabase = new DBHelper(context).getWritableDatabase();
    }

    public boolean salvar(CartBean cartBean){
        boolean success = false;

        sqlLiteDatabase.beginTransaction();

        boolean insert = true;

        try{
            ContentValues fieldsCart = new ContentValues();

            if(cartBean.getId() != null){
                insert = false;
                fieldsCart.put("id", cartBean.getId());
            }

            fieldsCart.put("id_user", 1);
            fieldsCart.put("price", cartBean.getPrice().toString());
            fieldsCart.put("date_create", new Date().toString());

            if(insert){
                long resultCart = sqlLiteDatabase.insert("cart", null, fieldsCart);
                success = true;
                if(resultCart != -1){
                    for(ProductBean productBean : cartBean.getProductList()){
                        ContentValues fieldsCartItem = new ContentValues();
                        fieldsCartItem.put("id_cart", resultCart);
                        fieldsCartItem.put("id_product", productBean.getId());
                        fieldsCartItem.put("price", productBean.getPrice().toString());
                        fieldsCartItem.put("quantity", productBean.getQuantity());

                        long resultCartItem = sqlLiteDatabase.insert("cart_item", null, fieldsCartItem);
                        if(resultCartItem != -1){
                            sqlLiteDatabase.setTransactionSuccessful();
                        }
                    }
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            sqlLiteDatabase.endTransaction();
        }

        return success;
    }

    public List<CartBean> getCarts(){
        List<CartBean> listCartBeans = new ArrayList<CartBean>();

        // faco a consulta no banco usando um comando SQL puro
        Cursor cursor = sqlLiteDatabase.rawQuery("SELECT id, id_user, price, date_create FROM cart", null);
        // percorro cada linha da consulta e armazeno na lista
        while(cursor.moveToNext()){
            CartBean cartBean = new CartBean();
            cartBean.setId(cursor.getInt(0));
            UserBean userBean = new UserBean();
            userBean.setId(cursor.getInt(1));
            cartBean.setUser(userBean);
            cartBean.setPrice(new BigDecimal(cursor.getDouble(2)).setScale(2, RoundingMode.HALF_UP));
            //cartBean.setDateCreate(new Date(cursor.getLong(3)));

            listCartBeans.add(cartBean);
        }
        cursor.close();

        return listCartBeans;
    }

    private String getProductsIds(List<ProductBean> productBeanList){
        StringBuilder ids = new StringBuilder();
        for(int i = 0; i < productBeanList.size(); i++){
            ProductBean p = productBeanList.get(i);
            if(i == productBeanList.size()){
                ids.append(p.getId());
            } else {
                ids.append(p.getId()+",");
            }
        }
        return ids.toString();
    }



}
