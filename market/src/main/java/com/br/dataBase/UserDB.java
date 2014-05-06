package com.br.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.br.bean.CategoryBean;
import com.br.bean.ProductBean;
import com.br.bean.UserBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vinicius on 03/05/14.
 */
public class UserDB {

    private SQLiteDatabase sqlLiteDatabase;

    public UserDB(Context context){
        sqlLiteDatabase = new DBHelper(context).getWritableDatabase();
    }

    public List<UserBean> getUsers(){
        List<UserBean> userBeanList = new ArrayList<UserBean>();

        // faco a consulta no banco usando um comando SQL puro
        Cursor cursor = sqlLiteDatabase.rawQuery("SELECT id, name, login, password, email, address, number, complement, zipcode, neighborhood, city, state, date_create FROM user", null);
        // percorro cada linha da consulta e armazeno na lista
        while(cursor.moveToNext()){
            try {
                UserBean userBean = new UserBean();
                userBean.setId(cursor.getInt(0));
                userBean.setUserName(cursor.getString(1));
                userBean.setLogin(cursor.getString(2));
                userBean.setPassword(cursor.getString(3));
                userBean.setEmail(cursor.getString(4));
                userBean.setAddress(cursor.getString(5));
                userBean.setNumberAddress(cursor.getString(6));
                userBean.setComplement(cursor.getString(7));
                userBean.setZipCode(cursor.getString(8));
                userBean.setNeighborhood(cursor.getString(9));
                userBean.setCity(cursor.getString(10));
                userBean.setState(cursor.getString(11));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userBean.setDateCreate(dateFormat.parse(cursor.getString(12)));

                userBeanList.add(userBean);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        cursor.close();

        return userBeanList;
    }

    public UserBean salvar(UserBean userBean){

        sqlLiteDatabase.beginTransaction();

        try {

            boolean insert = true;

            ContentValues fields = new ContentValues();

            if(userBean.getId() != null){
                insert = false;
                fields.put("id", userBean.getId());
            }

            fields.put("name", userBean.getUserName());
            fields.put("login", userBean.getLogin());
            fields.put("password", userBean.getPassword());
            fields.put("email", userBean.getEmail());
            fields.put("address", userBean.getAddress());
            fields.put("number", userBean.getNumberAddress());
            fields.put("complement", userBean.getComplement());
            fields.put("zipcode", userBean.getZipCode());
            fields.put("neighborhood", userBean.getNeighborhood());
            fields.put("city", userBean.getCity());
            fields.put("state", userBean.getState());
            //fields.put("date_create", new Date());

            long result;

            if(insert){
                result = sqlLiteDatabase.insert("user", null, fields);
            } else {
                result = sqlLiteDatabase.update("user", fields, "id = " + userBean.getId(), null);
            }

            if(result != -1){
                if(insert) {
                    userBean.setId((int) result);
                }
                sqlLiteDatabase.setTransactionSuccessful();
            } else {
                userBean = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlLiteDatabase.endTransaction();
        }

        return userBean;
    }

    public boolean delete(UserBean userBeanLoad) {
        boolean success = false;
        
        sqlLiteDatabase.beginTransaction();
        
        try {

            long result = sqlLiteDatabase.delete("user", "id = " + userBeanLoad.getId(), null);
            if(result != -1){
                sqlLiteDatabase.setTransactionSuccessful();
                success = true;
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            sqlLiteDatabase.endTransaction();
        }

        return success;
    }
}
