package com.br.dataBase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.br.bean.CategoryBean;
import com.br.bean.ProductBean;
import com.br.bean.UserBean;

public class ProductDB {

	private SQLiteDatabase sqlLiteDatabase;
	
	public ProductDB(Context context){
		// uso o bancoHelper para criar um objeto que acessa o banco de dados
        sqlLiteDatabase = new DBHelper(context).getWritableDatabase();
	}
	
	public boolean salvar(ProductBean productBean){
		boolean success = false;
        sqlLiteDatabase.beginTransaction();
		
		try {
			
			boolean insert = true;
			
			ContentValues fields = new ContentValues();
			
			if(productBean.getId() != null){
				insert = false;
				fields.put("id", productBean.getId());
			}			
			
			fields.put("name", productBean.getName());
			fields.put("price", productBean.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			fields.put("id_category", productBean.getCategoryBean().getId());
            fields.put("img", productBean.getImg());
            fields.put("stock", productBean.getStock());

            long result;
			
			if(insert){
				result = sqlLiteDatabase.insert("product", null, fields);
			} else {
				result = sqlLiteDatabase.update("product", fields, "id = " + productBean.getId(), null);
			}
			
			if(result != -1){
                sqlLiteDatabase.setTransactionSuccessful();
                success = true;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            sqlLiteDatabase.endTransaction();
		}
		
		return success;
	}
	
	public void updateProductAfterCart(List<ProductBean> productBeanList){
        sqlLiteDatabase.beginTransaction();
        try {
            for(ProductBean productBean : productBeanList){

                ContentValues fields = new ContentValues();
                fields.put("stock", (productBean.getStock() - productBean.getQuantity()));

                sqlLiteDatabase.update("product", fields, "id = " + productBean.getId(), null);
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            sqlLiteDatabase.endTransaction();
        }
    }

    public List<ProductBean> getProducts(){
        List<ProductBean> listProductBeans = new ArrayList<ProductBean>();

        // faco a consulta no banco usando um comando SQL puro
        Cursor cursor = sqlLiteDatabase.rawQuery("SELECT id, name, price, id_category, img, stock FROM product where stock > ? order by name", new String[]{"0"});
        // percorro cada linha da consulta e armazeno na lista
        while(cursor.moveToNext()){
            ProductBean productBean = new ProductBean();
            productBean.setId(cursor.getInt(0));
            productBean.setName(cursor.getString(1));
            productBean.setPrice(new BigDecimal(cursor.getDouble(2)).setScale(2, RoundingMode.HALF_UP));
            CategoryBean categoryBean = new CategoryBean();
            categoryBean.setId(cursor.getInt(3));
            productBean.setCategoryBean(categoryBean);
            productBean.setImg(cursor.getString(4));
            productBean.setQuantity(1);
            productBean.setStock(cursor.getInt(5));

            listProductBeans.add(productBean);
        }
        cursor.close();

        return listProductBeans;
    }

    public List<CategoryBean> getCategories(){
        List<CategoryBean> categoryBeanList = new ArrayList<CategoryBean>();

        Cursor cursor = sqlLiteDatabase.rawQuery("SELECT id, name FROM category ORDER BY name", null);
        while(cursor.moveToNext()){
            categoryBeanList.add(new CategoryBean(cursor.getInt(0), cursor.getString(1)));
        }
        cursor.close();

        return categoryBeanList;
    }

	public boolean delete(ProductBean productBean){
		boolean success = false;
        sqlLiteDatabase.beginTransaction();
		
		try {
		
			long result = sqlLiteDatabase.delete("product", "id = " + productBean.getId(), null);
			
			if(result != -1){
                sqlLiteDatabase.setTransactionSuccessful();
                success = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            sqlLiteDatabase.endTransaction();
		}
		
		return success;
	}
}
