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
        db.execSQL("CREATE  TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, name VARCHAR NOT NULL, login VARCHAR NOT NULL, password VARCHAR NOT NULL, email VARCHAR NOT NULL, address VARCHAR NOT NULL, number VARCHAR NOT NULL, complement VARCHAR NULL, zipcode VARCHAR NOT NULL, neighborhood VARCHAR NOT NULL, city VARCHAR NOT NULL, state VARCHAR NOT NULL, date_create DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
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

        // inserindo os produtos
        db.execSQL("INSERT INTO product VALUES(NULL, 'Alcatra com Maminha(Kg)', 20.70, 1, 'alcatra_com_maminha', NULL, 10);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Alvejante Floral Brilhante 2l', 10.66, 5, 'alvejantefloralbrilhante_2l', NULL, 30);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Alvejante líquido sem cloro Vanish 3 litros', 19.98, 5, 'alvejanteliquidosemcloro_vanish_3l', NULL, 30);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Amaciante Classic Comfort 2l', 12.31, 5, 'amacianteclassicomfort_2l', NULL, 50);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Amaciante Clássico Mon Bijou 500ml', 5.50, 5, 'amacianteclassicomonbijou_500ml', NULL, 80);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Banana nanica(Kg)', 4.00, 4, 'banana_nanica', NULL, 20);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Batata doce rosada(Kg)', 2.00, 2, 'batata_doce', NULL, 10);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Batata Monalisa(Kg)', 5.59, 2, 'batata_monalisa', NULL, 30);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Berinjela(Kg)', 4.00, 2, 'berinjela', NULL, 10);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Bohemia lata 350ml', 2.29, 3, 'bohemia_350ml', NULL, 200);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Skol lata 350ml', 2.19, 3, 'skol_350ml', NULL, 300);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Itaipava lata 350ml', 1.97, 3, 'itaipava_350ml', NULL, 300);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Carambola (Kg)', 7.00, 4, 'carambola', NULL, 10);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Cebola (3Kg)', 9.57, 2, 'cebola', NULL, 30);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Cenoura (Kg)', 3.00, 2, 'cenoura', NULL, 30);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Chester congelado', 25.50, 1, 'chester_congelado', NULL, 10);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Coração de frango congelado Seara (Kg)', 14.50, 1, 'coracao_frango_congelado_seara', NULL, 10);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Couve flor (Kg)', 5.00, 2, 'couve_flor', NULL, 10);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Roast Beef(Coxão mole com toucinho - Kg)', 20.50, 1, 'roast_beef_coxaomole', NULL, 10);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Laranja lima (Kg)', 2.59, 4, 'laranja_lima', NULL, 30);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Desinfetante eucalipto Bak Ype 500ml', 2.09, 5, 'desinfetanteeucaliptobakype_500ml', NULL, 30);");
        db.execSQL("INSERT INTO product VALUES(NULL, 'Desinfetante brisa do mar Pinho Bril 500ml', 2.96, 5, 'desinfetantebrisadomar_pinhobril_500ml', NULL, 30);");

    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// metodo chamado apenas 1x quando o usuario atualiza o app e o banco requer novas tabelas
		// nesse momento posso criar novas tabelas e colunas de acordo com a versao que o usuario
		// tinha instalado(oldVersion) e atual

    }

}
