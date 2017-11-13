package mancash_outs.cash_outs.p43z.mycash_outs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by p43z on 18/05/16.
 */
public class BD_cashouts extends SQLiteOpenHelper{


    String tabla="CREATE TABLE Persona(Id Text PRIMARY KEY,Icon INTEGER,Nombre Text,Apellido1 Text,Apellido2 Text,Telefono Text,Direccion Text)";
    String tabla2="CREATE TABLE Registro(Id INTEGER PRIMARY KEY AUTOINCREMENT,IdPersona Text,IdTarifa Text,Fecha Text,Modalidad Text,Cantidad INTEGER,Monto INTEGER,Tipo_modalidad Text,Detalle Text,Estado INTEGER,FOREIGN KEY(IdPersona) REFERENCES Persona(Id))";
    String tabla3="CREATE TABLE Tarifa(Id_tarifa Text PRIMARY KEY,Descripcion Text,Monto INTEGER)";


    public BD_cashouts(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tabla);
        db.execSQL(tabla2);
        db.execSQL(tabla3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }





}
