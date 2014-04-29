package com.br.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public DBHelper(Context context){
		// chamo o construtor do banco que recebe o nome do bd a ser aberto e em que versao ele esta
		super(context, "Integration_Market", null, 1);
		// o ultimo parametro eh a versao atual do bd, se eu subir uma versao no google play com um numero maior que 
		// o anterior o android chama automaticamente o onUpgrade a primeira vez que o usuario abrir o app
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// metodo chamado apenas uma vez, quando o usuario abre o app pela primeira vez
		// nesse momento devo rodar comandos que criem as tabelas e linhas necessarias para o app funcionar

        // criando a tabela user
        db.execSQL("CREATE  TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, name VARCHAR NOT NULL, password VARCHAR NOT NULL, email VARCHAR NOT NULL)");
        // criando a tabela product
        db.execSQL("CREATE TABLE product(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, name VARCHAR NOT NULL, price NUMERIC NOT NULL, id_category INTEGER NOT NULL, img VARCHAR NOT NULL, quantity INTEGER NULL, stock INTEGER NOT NULL)");
        // criando a tabela sub_category
        db.execSQL("CREATE TABLE sub_category(id_subcategory INTEGER PRIMARY KEY NOT NULL UNIQUE, id_category INTEGER NOT NULL)");
        // criando a tabela category
        db.execSQL("CREATE TABLE category(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, name VARCHAR NOT NULL)");
        // criando a tabela cart
        db.execSQL("CREATE TABLE cart(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, id_user INTEGER NOT NULL, price NUMERIC NOT NULL, date_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
        // criando a tabela cart_itens - ASSOCIATIVA(Cart x Product)
        db.execSQL("CREATE TABLE cart_item(id_cart INTEGER NOT NULL, id_product INTEGER NOT NULL, price NUMERIC NOT NULL, quantity INTEGER NOT NULL)");

        // inserindo as categorias
        db.execSQL("INSERT INTO category VALUES(1, 'Carnes');");
        db.execSQL("INSERT INTO category VALUES(2, 'Legumes e Verduras');");
        db.execSQL("INSERT INTO category VALUES(3, 'Cervejas');");
        db.execSQL("INSERT INTO category VALUES(4, 'Frutas');");
        db.execSQL("INSERT INTO category VALUES(5,'Limpeza');");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// metodo chamado apenas 1x quando o usuario atualiza o app e o banco requer novas tabelas
		// nesse momento posso criar novas tabelas e colunas de acordo com a versao que o usuario
		// tinha instalado(oldVersion) e atual

    }

}
