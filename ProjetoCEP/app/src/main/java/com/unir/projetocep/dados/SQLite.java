package com.unir.projetocep.dados;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.unir.projetocep.encapsulamento.CEP;

import java.util.ArrayList;
import java.util.List;

public class SQLite extends SQLiteOpenHelper {

    private SQLiteDatabase  leitura;
    private SQLiteDatabase  escrita;

    private static final String DATABASE_NAME = "enderecos";
    private static final int DATABASE_VERSION = 4;
    private final String CREATE_TABLE_ENDERECO =
            "CREATE TABLE endereco ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "bairro TEXT, "
                    + "cep TEXT,"
                    + "localidade TEXT ,"
                    + "logradouro TEXT);";

    public SQLite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        leitura = this.getReadableDatabase();
        escrita = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ENDERECO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS endereco");
        onCreate(sqLiteDatabase);
    }

    //public long inserir(ContentValues cv){
    public long inserir(CEP cep){
        /*SQLiteDatabase db = this.getWritableDatabase();

        Log.i("val","finaro: "+cv.toString());

        long id = db.insert("endereco", null, cv);//*/

        ContentValues contentValues = new ContentValues();
        contentValues.put("bairro", cep.getBairro());
        contentValues.put("cep", cep.getCep());
        contentValues.put("localidade", cep.getLocalidade());
        contentValues.put("logradouro", cep.getLogradouro());




        long id = escrita.insert("endereco", null, contentValues);
        return id;
    }

    public List<ContentValues> pesquiarPorTitulo(String titulo){
        String sql = "SELECT * FROM endereco WHERE titulo LIKE ?";
        String where[] = new String[]{"%"+titulo+"%"};
        return pesquisar(sql, where);
    }

    public List<ContentValues> pesquiarPorAno(int ano){
        String sql = "SELECT * FROM endereco WHERE ano LIKE ?";
        String where[] = new String[]{String.valueOf(ano)};
        return pesquisar(sql, where);
    }

    public List<ContentValues> pesquiarPorTodos(){
        String sql = "SELECT * FROM endereco ORDER BY id";
        String where[] = null;
        return pesquisar(sql, where);
    }

    @SuppressLint("Range")
    private List<ContentValues> pesquisar(String sql, String where[]){
        List<ContentValues> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, where);

        if (c.moveToFirst()){
            do{
                ContentValues cv = new ContentValues();
                cv.put("id", c.getString(c.getColumnIndex("id")));
                cv.put("logradouro", c.getString(c.getColumnIndex("logradouro")));
                cv.put("cep", c.getString(c.getColumnIndex("cep")));
                cv.put("bairro", c.getString(c.getColumnIndex("bairro")));
                cv.put("localidade", c.getString(c.getColumnIndex("localidade")));
                lista.add(cv);
            }while(c.moveToNext());
        }

        return lista;
    }

    public void alterarRegistro(int id, String logradouro, String cep, int bairro, String localidade){
        ContentValues valores = new ContentValues();
        String where;
        SQLiteDatabase db = this.getWritableDatabase();
        where = "id=" + id;
        valores.put("logradouro", logradouro);
        valores.put("cep", cep);
        valores.put("bairro", bairro);
        valores.put("localidade", localidade);
        db.update("endereco", valores, where, null);
        db.close();
    }

    public void deletarRegistro(int id){
        String where = "id=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("endereco", where, null);
        db.close();
    }


}