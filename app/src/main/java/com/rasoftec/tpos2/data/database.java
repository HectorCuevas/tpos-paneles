package com.rasoftec.tpos2.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rasoftec.ApplicationTpos;
import com.rasoftec.tpos2.Nodo_tarea;
import com.rasoftec.tpos2.Beans.FormatoFactura;
import com.rasoftec.tpos2.Beans.detalleFactura;
import com.rasoftec.tpos2.Beans.factura_encabezado;
import com.rasoftec.tpos2.nodo_producto;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class database extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "basetar760.db";
    int cont=0 ;

    public database(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    public database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                " CREATE TABLE producto(" +
                        " cod_producto INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "  cod_articulo VARCHAR(100) NOT NULL," +
                        "  descripcion  VARCHAR(100) NOT NULL," +
                        "  precio DOUBLE NOT NULL," +
                        "  stock  double NOT NULL" +
                        ")");

        db.execSQL("  CREATE TABLE cliente(\n" +
                "  \"cod_cliente\" VARCHAR(100) PRIMARY KEY NOT NULL,\n" +
                "  \"credito\" DOUBLE,\n" +
                "  \"descripcion\" VARCHAR(100),\n" +
                "  \"direccion\" VARCHAR(100),\n" +
                "  \"telefono\" VARCHAR(100),\n" +
                "  \"fax\" VARCHAR(50),\n" +
                "  \"encargado\" VARCHAR(45),\n" +
                "  \"enruta\" INTEGER\n" +
                ")  ;            ");
//        Tabla para el Manejo de Clientes Actuales
        db.execSQL("CREATE TABLE \"factura\"(\n" +
                "  \"cod_factura\" INTEGER PRIMARY KEY NOT NULL,\n" +
                "  \"monto\" DOUBLE NOT NULL,\n" +
                "  \"fecha\" VARCHAR(45) NOT NULL,\n" +
                "  \"pago\" DOUBLE NOT NULL DEFAULT 0,\n" +
                "  \"estado\" INTEGER NOT NULL DEFAULT 0,\n" +
                "  \"cliente\" INTEGER NOT NULL\n" +
                ")");

//        Area de la Tabla para el Manejo de la configuracion global del sistema
        db.execSQL("CREATE TABLE \"configuracion\"(\n" +
                "  \"codigo\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"telefono\" VARCHAR(300) NOT NULL,\n" +
                "  \"ruta\" VARCHAR(100) NOT NULL,\n" +
                "  \"usuario\" VARCHAR(100) NOT NULL,\n" +
                "  \"actual_movilizandome\" INTEGER NOT NULL DEFAULT 0 ,\n" +
                "  \"recarga\" VARCHAR(100) NOT NULL,\n" +
                "  \"saldo_pdv\" VARCHAR(100) NOT NULL,\n" +
                "  \"saldo_ss\" VARCHAR(100) NOT NULL,\n" +
                "  \"f_orga\" DOUBLE NOT NULL DEFAULT 0,\n" +
                "  \"estado\" INTEGER NOT NULL DEFAULT 0 \n" +

                ");");
//        Area de Venta
        db.execSQL("CREATE TABLE venta (\n" +
                "    cod_venta     INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
                "                                NOT NULL,\n" +
                "    cod_cli       VARCHAR (100),\n" +
                "    movilizandome INTEGER       NOT NULL\n" +
                "                                DEFAULT 0,\n" +
                "    usuario       VARCHAR (45),\n" +
                "    forma_pag     VARCHAR (45),\n" +
                "    total         DOUBLE,\n" +
                "    cobrado       DOUBLE        DEFAULT (0),\n" +
                "    procesado     VARCHAR (45),\n" +
                "    fact_num2     INTEGER,\n" +
                "    cobrado2      DOUBLE        DEFAULT (0),\n" +
                "    tipo          INTEGER       DEFAULT 0,\n" +
                "    estado        INTEGER       DEFAULT 0,\n" +
                "    fecha         DATE          DEFAULT (date('now', 'localtime') ) \n" +
                ");\n");
//        Area de Cobro
        db.execSQL("CREATE TABLE \"cobro\"(\n" +
                " cod_cobro INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "  \"cod_factura\" INTEGER  NOT NULL,\n" +
                "  \"monto\" DOUBLE NOT NULL,\n" +
                "  \"fecha\" VARCHAR(45) NOT NULL,\n" +
                "  \"pago\" DOUBLE NOT NULL DEFAULT 0,\n" +
                "  \"cliente\" INTEGER NOT NULL,\n" +
                "  \"estado\" INTEGER NOT NULL DEFAULT 0\n" +
                ")");

//        Area de Cobros y Ventas Temporales
        db.execSQL("CREATE TABLE \"encabezado\"(\n" +
                "  \"cod_encabezado\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"usuario\" VARCHAR(45) NOT NULL,\n" +
                "  \"co_cli\" VARCHAR(45) NOT NULL,\n" +
                "  \"forma_pag\" VARCHAR(45) NOT NULL,\n" +
                "  \"total\" DOUBLE,\n" +
                "  \"cobrado\" DOUBLE,\n" +
                "  \"procesado\" VARCHAR(45) DEFAULT 'S',\n" +
                "  \"fact_num2\" INTEGER DEFAULT 0,\n" +
                "  \"cobrado2\" DOUBLE DEFAULT 0,\n" +
                "  \"tipo\" integer DEFAULT 0,\n" +
                "  \"estado\" INTEGER NOT NULL DEFAULT 0\n" +
                ")");

        db.execSQL(" CREATE TABLE \"detalle\"(\n" +
                "  \"cod_cabezado\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"articulo\" VARCHAR(100),\n" +
                "  \"prec_vta\" DOUBLE,\n" +
                "  \"cantidad\" INTEGER,\n" +
                "  \"total_art\" DOUBLE,\n" +
                "  \"numero_cel\" INTEGER DEFAULT 0,\n" +
                "  \"encabezado\" integer,\n" +
                "  \"estado\" INTEGER DEFAULT 0\n" +
                "); ");
        db.execSQL(" CREATE TABLE \"factura_encabezado\"(\n" +
                "  \"cod_encabezado\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "  \"usuario_movilizandome\" INTEGER,\n" +
                "  \"cod_cliente\" VARCHAR(100),\n" +
                "  \"forma_pag\" VARCHAR(50),\n" +
                "  \"total\" DOUBLE,\n" +
                "  \"cobrado\" DOUBLE,\n" +
                "  \"procesado\" VARCHAR(45) DEFAULT 'S',\n" +
                "  \"numFactura\" INTEGER,\n" +
                "  \"fecha\" DATE DEFAULT (date('now', 'localtime') ), \n" +
                "  \"dpi\" INTEGER,\n" +
                "  \"nombre\" VARCHAR(50),\n" +
                "  \"nit\" integer,\n" +
                "  \"direccion\" VARCHAR(50),\n" +
                "  \"municipio\" VARCHAR(50),\n" +
                "  \"departamento\" VARCHAR(50),\n" +
                "  \"zona\" VARCHAR(50),\n" +
                "  \"email\" VARCHAR(50),\n" +
                "  \"latitud\" VARCHAR(50),\n" +
                "  \"longitud\" VARCHAR(50)\n" +
                "); ");
        db.execSQL(" CREATE TABLE \"factura_encabezado_enviar\"(\n" +
                "  \"cod_encabezado\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,\n" +
                "  \"usuario_movilizandome\" INTEGER,\n" +
                "  \"cod_cliente\" VARCHAR(100),\n" +
                "  \"forma_pag\" VARCHAR(50),\n" +
                "  \"total\" DOUBLE,\n" +
                "  \"cobrado\" DOUBLE,\n" +
                "  \"procesado\" VARCHAR(45) DEFAULT 'S',\n" +
                "  \"numFactura\" INTEGER,\n" +
                "  \"fecha\" DATE DEFAULT (date('now', 'localtime') ), \n" +
                "  \"dpi\" INTEGER,\n" +
                "  \"nombre\" VARCHAR(50),\n" +
                "  \"nit\" integer,\n" +
                "  \"direccion\" VARCHAR(50),\n" +
                "  \"municipio\" VARCHAR(50),\n" +
                "  \"departamento\" VARCHAR(50),\n" +
                "  \"zona\" VARCHAR(50),\n" +
                "  \"email\" VARCHAR(50),\n" +
                "  \"latitud\" VARCHAR(50),\n" +
                "  \"longitud\" VARCHAR(50)\n" +
                "); ");
        db.execSQL("CREATE TABLE detalle_venta (\n" +
                "    cod_detalle INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
                "                              NOT NULL,\n" +
                "    articulo    VARCHAR (100) NOT NULL,\n" +
                "    prec_vta    DOUBLE        NOT NULL,\n" +
                "    cantidad    INTEGER,\n" +
                "    total_art   DOUBLE,\n" +
                "    estado      INTEGER       DEFAULT 0,\n" +
                "    venta       INTEGER       NOT NULL,\n" +
                "    nombre      VARCHAR (100) NOT NULL,\n" +
                "    numero_cel  INTEGER       DEFAULT 0 \n" +
                ")");
        /*** Esta tabla es la que almacena las facturas que estan pendientes de envio consume el sp_insert_det_P ***/
        db.execSQL("CREATE TABLE detalle_factura (\n" +
                "    cod_detalle INTEGER       PRIMARY KEY AUTOINCREMENT \n" +
                "                              NOT NULL,\n" +
                "    usuario_mov    VARCHAR (100) NOT NULL,\n" +
                "    co_art    VARCHAR (100) NOT NULL,\n" +
                "    prec_vta    VARCHAR(100)        NOT NULL,\n" +
                "    cantidad    INTEGER,\n" +
                "    total_art   VARCHAR(100),\n" +
                "    serial      VARCHAR (100) NOT NULL,\n" +
                "    numero_cel  INTEGER       DEFAULT 0,\n" +
                "    cod_encabezado  INTEGER    ,\n" +
                "    CONSTRAINT fk_detalle_factura_encabezado FOREIGN KEY (\n" +
                "        cod_encabezado \n" +
                "    ) \n" +
                "    REFERENCES factura_encabezado_enviar (cod_encabezado) \n" +
                ")");

    }

    public ArrayList<factura_encabezado> getEncabezadoFacturasPorEnviar() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT *  FROM factura_encabezado_enviar", null);
        ArrayList<factura_encabezado> facturas = new ArrayList<>();
        while (puntero.moveToNext()) {
            int codEncabezado = puntero.getInt(puntero.getColumnIndex("cod_encabezado"));
            String usuarioMov = puntero.getString(puntero.getColumnIndex("usuario_movilizandome"));
            String cod_cliente = puntero.getString(puntero.getColumnIndex("cod_cliente"));
            Double total = puntero.getDouble(puntero.getColumnIndex("total"));
            String dpi = puntero.getString(puntero.getColumnIndex("dpi"));
            String nombre = puntero.getString(puntero.getColumnIndex("nombre"));
            String nit = puntero.getString(puntero.getColumnIndex("nit"));
            String direccion = puntero.getString(puntero.getColumnIndex("direccion"));
            String municipio = puntero.getString(puntero.getColumnIndex("municipio"));
            String depto = puntero.getString(puntero.getColumnIndex("departamento"));
            String zona = puntero.getString(puntero.getColumnIndex("zona"));
//            String usuarioMovDet = puntero.getString(puntero.getColumnIndex("usuario_mov"));

            factura_encabezado factura_encabezado = new factura_encabezado(
                    dpi,
                    total.toString(),
                    nombre,
                    direccion,
                    depto,
                    municipio,
                    zona,
                    codEncabezado
            );
            facturas.add(factura_encabezado);
        }

        puntero.close();
        return facturas;
    }

    /*** REESCRIBIR ESTA FUNCION PARA QUE SOLO DEVUELVA LO QUE NECESITO ***/
    public ArrayList<FormatoFactura> getFacturasPorEnviar(long codF) {
        SQLiteDatabase db = this.getReadableDatabase();
        long cod =codF;
        //Cursor puntero = db.rawQuery("SELECT *  FROM factura_encabezado_enviar" , null);
        Cursor puntero = db.rawQuery("SELECT *  FROM factura_encabezado_enviar inner join detalle_factura on " +
                "factura_encabezado_enviar.cod_encabezado = detalle_factura.cod_encabezado " +
                "where factura_encabezado_enviar.cod_encabezado = ?", new String[]{String.valueOf(cod)});
        ArrayList<FormatoFactura> envio = new ArrayList<>();

        while (puntero.moveToNext()) {


                    String co_art = puntero.getString(puntero.getColumnIndex("co_art"));
            String prec_vta = puntero.getString(puntero.getColumnIndex("prec_vta"));
            int cantidad = puntero.getInt(puntero.getColumnIndex("cantidad"));
            String totalArt = puntero.getString(puntero.getColumnIndex("total_art"));
            String serial = puntero.getString(puntero.getColumnIndex("serial"));
            int numero = puntero.getInt(puntero.getColumnIndex("numero_cel"));
            String usuarioMovDet = puntero.getString(puntero.getColumnIndex("usuario_mov"));

            detalleFactura detalleFactura = new detalleFactura();

           detalleFactura.setUsuarioMovilizandome(usuarioMovDet);
           detalleFactura.setCodigoArticulo(co_art);
           detalleFactura.setPrecioArticulo(Double.parseDouble(prec_vta));
           detalleFactura.setCantidad(cantidad);
           detalleFactura.setTotalFactura(Double.parseDouble(totalArt));
           detalleFactura.setNumeroCel(numero);

           ArrayList<detalleFactura> detalleFacturas = new ArrayList<detalleFactura>();

            int codEncabezado = puntero.getInt(puntero.getColumnIndex("cod_encabezado"));
            String usuarioMov = puntero.getString(puntero.getColumnIndex("usuario_movilizandome"));
            String cod_cliente = puntero.getString(puntero.getColumnIndex("cod_cliente"));
            String forma_pag = puntero.getString(puntero.getColumnIndex("forma_pag"));
            Double total = puntero.getDouble(puntero.getColumnIndex("total"));
            Double cobrado = puntero.getDouble(puntero.getColumnIndex("cobrado"));
            String procesado = puntero.getString(puntero.getColumnIndex("procesado"));
            String fecha =  puntero.getString(puntero.getColumnIndex("fecha"));

            //  puntero.getInt(puntero.getColumnIndex("fecha")),

            String dpi = puntero.getString(puntero.getColumnIndex("dpi"));
            String nombre = puntero.getString(puntero.getColumnIndex("nombre"));
            String nit = puntero.getString(puntero.getColumnIndex("nit"));
            String direccion = puntero.getString(puntero.getColumnIndex("direccion"));
            String municipio = puntero.getString(puntero.getColumnIndex("municipio"));
            String depto = puntero.getString(puntero.getColumnIndex("departamento"));
            String zona = puntero.getString(puntero.getColumnIndex("zona"));
            String email = puntero.getString(puntero.getColumnIndex("email"));
            String lat = puntero.getString(puntero.getColumnIndex("longitud"));
            String lon = puntero.getString(puntero.getColumnIndex("latitud"));

           // String usuarioMovDet = puntero.getString(puntero.getColumnIndex("usuario_mov"));


            FormatoFactura formatoFactura = new FormatoFactura(dpi, nombre, nit, direccion,
                    depto, municipio, zona, email, lat, lon, usuarioMov, co_art,
                    Double.parseDouble(prec_vta), cantidad, total, numero, detalleFacturas, cod_cliente, fecha);
            envio.add(formatoFactura);
        }
        puntero.close();
        return envio;
    }
    public ArrayList<FormatoFactura> getFacturasPorEnviar() {
        SQLiteDatabase db = this.getReadableDatabase();
       // long cod =codF;
        //Cursor puntero = db.rawQuery("SELECT *  FROM factura_encabezado_enviar" , null);
        Cursor puntero = db.rawQuery("SELECT *  FROM factura_encabezado_enviar inner join detalle_factura on " +
                "factura_encabezado_enviar.cod_encabezado = detalle_factura.cod_encabezado ", null);
               // "where factura_encabezado_enviar.cod_encabezado = ?", new String[]{String.valueOf(cod)});
        ArrayList<FormatoFactura> envio = new ArrayList<>();

        while (puntero.moveToNext()) {


            String co_art = puntero.getString(puntero.getColumnIndex("co_art"));
            String prec_vta = puntero.getString(puntero.getColumnIndex("prec_vta"));
            int cantidad = puntero.getInt(puntero.getColumnIndex("cantidad"));
            String totalArt = puntero.getString(puntero.getColumnIndex("total_art"));
            String serial = puntero.getString(puntero.getColumnIndex("serial"));
            int numero = puntero.getInt(puntero.getColumnIndex("numero_cel"));
            String usuarioMovDet = puntero.getString(puntero.getColumnIndex("usuario_mov"));
            ArrayList<detalleFactura> detalleFacturas = new ArrayList<detalleFactura>();
            detalleFactura detalleFactura = new detalleFactura();

            detalleFactura.setUsuarioMovilizandome(usuarioMovDet);
            detalleFactura.setCodigoArticulo(co_art);
            detalleFactura.setPrecioArticulo(Double.parseDouble(prec_vta));
            detalleFactura.setCantidad(cantidad);
            detalleFactura.setTotalFactura(Double.parseDouble(totalArt));
            detalleFactura.setNumeroCel(numero);

            detalleFacturas.add(detalleFactura);


            int codEncabezado = puntero.getInt(puntero.getColumnIndex("cod_encabezado"));
            String usuarioMov = puntero.getString(puntero.getColumnIndex("usuario_movilizandome"));
            String cod_cliente = puntero.getString(puntero.getColumnIndex("cod_cliente"));
            String forma_pag = puntero.getString(puntero.getColumnIndex("forma_pag"));
            Double total = puntero.getDouble(puntero.getColumnIndex("total"));
            Double cobrado = puntero.getDouble(puntero.getColumnIndex("cobrado"));
            String procesado = puntero.getString(puntero.getColumnIndex("procesado"));
            String fecha =  puntero.getString(puntero.getColumnIndex("fecha"));
            //  puntero.getInt(puntero.getColumnIndex("fecha")),

            String dpi = puntero.getString(puntero.getColumnIndex("dpi"));
            String nombre = puntero.getString(puntero.getColumnIndex("nombre"));
            String nit = puntero.getString(puntero.getColumnIndex("nit"));
            String direccion = puntero.getString(puntero.getColumnIndex("direccion"));
            String municipio = puntero.getString(puntero.getColumnIndex("municipio"));
            String depto = puntero.getString(puntero.getColumnIndex("departamento"));
            String zona = puntero.getString(puntero.getColumnIndex("zona"));
            String email = puntero.getString(puntero.getColumnIndex("email"));
            String lat = puntero.getString(puntero.getColumnIndex("longitud"));
            String lon = puntero.getString(puntero.getColumnIndex("latitud"));

            // String usuarioMovDet = puntero.getString(puntero.getColumnIndex("usuario_mov"));


            FormatoFactura formatoFactura = new FormatoFactura(dpi, nombre, nit, direccion,
                    depto, municipio, zona, email, lat, lon, usuarioMov, co_art,
                    Double.parseDouble(prec_vta), cantidad, total, numero, detalleFacturas, cod_cliente, fecha);
            envio.add(formatoFactura);
        }
        puntero.close();
        return envio;
    }

    public long getLastInsertId() {
        long index = 0;
        SQLiteDatabase sdb = getReadableDatabase();
        Cursor cursor = sdb.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"factura_encabezado_enviar"},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            index = cursor.getLong(cursor.getColumnIndex("seq"));
        }
        cursor.close();
        return index;
    }

    public void limpiarEnvioFacturas(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "factura_encabezado_enviar";
        db.delete(query, null, null);
        query = "detalle_factura";
        db.delete(query, null, null);
    }

    public void addEntry(String name, byte[] image) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("key_name", name);
        cv.put("key_image", image);
        database.insert("imagenes", null, cv);
    }

    public void insertFacturaSinEnviar(ArrayList<detalleFactura> detalle1Fact, factura_encabezado encabezadoFact) throws SQLiteException {
       // cont++;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues encabezado = new ContentValues();
        ContentValues detalle = new ContentValues();
       // encabezado.put("cod_encabezado", getCodigoMovilizandome()+cont);
        encabezado.put("usuario_movilizandome", encabezadoFact.getUsuarioMov());
        encabezado.put("cod_cliente", encabezadoFact.getCodCliente());
        encabezado.put("forma_pag", encabezadoFact.getForma_pag());
        encabezado.put("total", encabezadoFact.getTotalFact());
        encabezado.put("cobrado", encabezadoFact.getCobrado());
        encabezado.put("procesado", encabezadoFact.getProcesado());
        encabezado.put("numFactura", 00);
        // encabezado.put("fecha", jsonObject.getString("fecha"));
        if (get_estado() == 3) {
            encabezado.put("fecha", fecha_actual2());
        }
        encabezado.put("dpi", encabezadoFact.getDpi());
        encabezado.put("nombre", encabezadoFact.getNombre());
        encabezado.put("nit", encabezadoFact.getNit());
        encabezado.put("direccion", encabezadoFact.getDireccion());
        encabezado.put("municipio", encabezadoFact.getMunicipio());
        encabezado.put("departamento", encabezadoFact.getDepto());
        encabezado.put("zona", encabezadoFact.getZona());
        encabezado.put("email", encabezadoFact.getEmail());
        encabezado.put("latitud", encabezadoFact.getLatitude());
        encabezado.put("longitud", encabezadoFact.getLongitude());
        db.insert("factura_encabezado_enviar", null, encabezado);
        int i=1;
        Iterator<detalleFactura> tem = detalle1Fact.iterator();
        while (tem.hasNext()) {
            detalleFactura tem14 = tem.next();
           // detalle.put("cod_detalle", getCodigoMovilizandome()+cont+i);
            detalle.put("usuario_mov", encabezadoFact.getUsuarioMov());
            detalle.put("co_art", tem14.getCodigoArticulo());
            detalle.put("prec_vta", tem14.getPrecioArticulo());
            detalle.put("cantidad", tem14.getCantidad()); // int
            detalle.put("total_art", tem14.getTotalFactura());
            detalle.put("serial", "123456");
            detalle.put("numero_cel", tem14.getNumeroCel());
            detalle.put("cod_encabezado", getLastInsertId());
            db.insert("detalle_factura", null, detalle);
            i++;
        }
    }

    //    Limpiar Cierre
    public void limpiar_cierre() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "cliente";
        db.delete(query, null, null);
        query = "factura";
        db.delete(query, null, null);
        if (get_estado() != -1 && get_estado() < 3) {
            query = "detalle_venta";
            db.delete(query, null, null);
            query = "venta where fecha=" + "'" + fecha_actual() + "'";
        }
        db.delete(query, null, null);
        query = "cobro";
        db.delete(query, null, null);
        query = "encabezado";
        db.delete(query, null, null);
        query = "detalle";
        db.delete(query, null, null);


    }

    // Area de replicacion al server
    public void asignar_pago(nodo_factura tem, String i) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE cobro SET monto=monto-" + tem.getPago() + "  where cod_factura=" + i;
        db.execSQL(query);

    }


    public int getCodigoMovilizandome() throws SQLiteException {
        int ultimo = get_encabezado();
        int actual = ultimo + 1;
        ApplicationTpos.codigoMovilizandome = actual;
        return actual;
    }
    public void set_venta(int i, int y) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE venta SET estado=1 , movilizandome=" + y + " where cod_venta=" + i;
        db.execSQL(query);

    }

    public void set_factura(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE cobro SET estado=1  where cod_factura=" + i;
        db.execSQL(query);

    }

    public void set_encabezado(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE encabezado SET estado=1  where cod_encabezado=" + i;
        db.execSQL(query);
    }
    public int setVenta(ArrayList<nodo_producto> detalleFact, JSONObject jsonObject) throws JSONException {
        int ultimo = get_encabezado();
        int actual = ultimo + 1;
        //New database instance
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues encabezado = new ContentValues();
        ApplicationTpos.codigoMovilizandome = actual;
        //Add each column to each json object
        encabezado.put("cod_encabezado", actual);
        encabezado.put("usuario_movilizandome", jsonObject.getString("usuario_movilizandome"));
        encabezado.put("cod_cliente", jsonObject.getString("cod_cliente"));
        encabezado.put("forma_pag", jsonObject.getString("forma_pago"));
        encabezado.put("total", jsonObject.getDouble("total"));
        encabezado.put("cobrado", jsonObject.getDouble("cobrado"));
        encabezado.put("procesado", jsonObject.getString("procesado"));
        encabezado.put("numFactura", jsonObject.getString("num_factura"));
        // encabezado.put("fecha", jsonObject.getString("fecha"));
        if (get_estado() == 3) {
            encabezado.put("fecha", fecha_actual2());
        }
        encabezado.put("dpi", jsonObject.getString("dpi"));
        encabezado.put("nombre", jsonObject.getString("nombre_cliente"));
        encabezado.put("nit", jsonObject.getString("nit"));
        encabezado.put("direccion", jsonObject.getString("direccion"));
        encabezado.put("municipio", jsonObject.getString("municipio"));
        encabezado.put("departamento", jsonObject.getString("departamento"));
        encabezado.put("zona", jsonObject.getString("zona"));
        encabezado.put("email", jsonObject.getString("email"));
        encabezado.put("latitud", jsonObject.getString("zona"));
        encabezado.put("longitud", jsonObject.getString("email"));
        db.insert("factura_encabezado", null, encabezado);

        //insert detail
        Iterator<nodo_producto> tem = detalleFact.iterator();
        while (tem.hasNext()) {
            nodo_producto tem14 = tem.next();
            detalleFactura detalle = new detalleFactura();
            detalle.setIdMovilizandome(actual);
            detalle.setUsuarioMovilizandome(ApplicationTpos.usuarioMovilizandome);
            detalle.setCodigoArticulo(tem14.getCodigo());
            detalle.setPrecioArticulo(tem14.getPrecio());
            detalle.setCantidad(tem14.getCompra());
            detalle.setTotalFactura(tem14.getPrecio() * tem14.getCompra());
            detalle.setNumeroCel(tem14.getNumerocel());
            ApplicationTpos.detalleFactura.add(detalle);

         /*   ContentValues detalle = new ContentValues();
            detalle.put("venta", actual);
            detalle.put("prec_vta", tem14.getPrecio());
            detalle.put("cantidad", tem14.getCompra());
            detalle.put("total_art", tem14.getPrecio() * tem14.getCompra());
            detalle.put("articulo", tem14.getCodigo());
            detalle.put("nombre", tem14.getDescripcion());
            detalle.put("numero_cel", tem14.getNumerocel());
            db.insert("detalle_venta", null, detalle);*/
        }
        return actual;
    }

    public int set_venta(ArrayList<nodo_producto> detalle, JSONObject encabezado) throws JSONException {

        int ultimo = get_encabezado();
        int actual = ultimo + 1;
//        Ingresamos el encabezado

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contenido = new ContentValues();
        contenido.put("cod_venta", actual);
        contenido.put("usuario", encabezado.getString("usuario_movilizandome"));
        contenido.put("cod_cli", encabezado.getString("co_cli"));
        contenido.put("forma_pag", encabezado.getString("forma_pag"));
        contenido.put("total", encabezado.getDouble("total"));
        contenido.put("cobrado", encabezado.getDouble("cobrado"));
        contenido.put("procesado", encabezado.getString("Procesado"));
        contenido.put("fact_num2", 0);
        contenido.put("cobrado2", 0.0);
        contenido.put("tipo", 1);
        if (get_estado() == 3) {
            contenido.put("fecha", fecha_actual2());
        }
        db.insert("venta", null, contenido);
// ingresamos los detalles
        Iterator<nodo_producto> tem = detalle.iterator();
        while (tem.hasNext()) {
            nodo_producto tem14 = tem.next();
            contenido = new ContentValues();
            contenido.put("venta", actual);
            contenido.put("prec_vta", tem14.getPrecio());
            contenido.put("cantidad", tem14.getCompra());
            contenido.put("total_art", tem14.getPrecio() * tem14.getCompra());
            contenido.put("articulo", tem14.getCodigo());
            contenido.put("nombre", tem14.getDescripcion());
            contenido.put("numero_cel", tem14.getNumerocel());
            db.insert("detalle_factura", null, contenido);
        }
        return actual;
    }

    public ArrayList<encabezado> get_encabezado_venta() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT *  FROM venta where tipo=1 and estado=0 ", null);
        ArrayList<encabezado> envio = new ArrayList<>();
        while (puntero.moveToNext()) {
            encabezado t = new encabezado(puntero.getString(puntero.getColumnIndex("usuario")),
                    puntero.getInt(puntero.getColumnIndex("cod_cli")),
                    puntero.getString(puntero.getColumnIndex("forma_pag")),
                    puntero.getDouble(puntero.getColumnIndex("total")),
                    puntero.getDouble(puntero.getColumnIndex("cobrado")),
                    puntero.getString(puntero.getColumnIndex("procesado")),
                    puntero.getInt(puntero.getColumnIndex("fact_num2")),
                    puntero.getDouble(puntero.getColumnIndex("cobrado2")),
                    puntero.getInt(puntero.getColumnIndex("cod_venta"))
            );
            int enca = puntero.getInt(puntero.getColumnIndex("cod_venta"));
            Cursor puntero2 = db.rawQuery("SELECT *  FROM detalle_venta  where venta=" + enca, null);
//            Area de detalle
            while (puntero2.moveToNext()) {
                detalle t2 = new detalle(
                        puntero2.getString(puntero2.getColumnIndex("articulo")),
                        puntero2.getDouble(puntero2.getColumnIndex("prec_vta")),
                        puntero2.getInt(puntero2.getColumnIndex("cantidad")),
                        puntero2.getDouble(puntero2.getColumnIndex("total_art")),
                        puntero2.getInt(puntero2.getColumnIndex("numero_cel"))
                );
                t.detalle.add(t2);
            }
            puntero2.close();
            envio.add(t);
        }
        puntero.close();
        return envio;
    }

    public int existe_sincronizar() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT count(*) as existe  FROM encabezado where  estado=0", null);
        int ruta = 0;

        while (puntero.moveToNext()) {
            ruta = puntero.getInt(puntero.getColumnIndex("existe"));
            break;


        }

        puntero.close();

        return ruta;


    }

    public ArrayList<encabezado> get_encabezado_cobro() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT *  FROM venta where tipo=0 and estado=0 ", null);
        ArrayList<encabezado> envio = new ArrayList<>();

        while (puntero.moveToNext()) {

            encabezado t = new encabezado(puntero.getString(puntero.getColumnIndex("usuario")),
                    puntero.getInt(puntero.getColumnIndex("cod_cli")),
                    puntero.getString(puntero.getColumnIndex("forma_pag")),
                    puntero.getDouble(puntero.getColumnIndex("total")),
                    puntero.getDouble(puntero.getColumnIndex("cobrado")),
                    puntero.getString(puntero.getColumnIndex("procesado")),
                    puntero.getInt(puntero.getColumnIndex("fact_num2")),
                    puntero.getDouble(puntero.getColumnIndex("cobrado2")),
                    puntero.getInt(puntero.getColumnIndex("cod_venta"))

            );

            envio.add(t);


        }

        puntero.close();

        return envio;

    }

    private int get_encabezado() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT max(cod_venta) as existe FROM venta ", null);
        int ruta = 0;

        while (puntero.moveToNext()) {
            String tem = puntero.getString(puntero.getColumnIndex("existe"));
            Log.i("Valor de Nueva Venta", "" + tem);
            if (tem != "NULL") ruta = puntero.getInt(puntero.getColumnIndex("existe"));
            break;


        }

        puntero.close();

        return ruta;
    }


    public void add_factura(nodo_factura t15) {

        SQLiteDatabase db = this.getWritableDatabase();


        String rut = get_ruta();


        ContentValues contenido = new ContentValues();
        contenido.put("usuario", rut);
        Log.i("Actual cliente ingreso", "" + t15.getCliente());
        contenido.put("cod_cli", t15.getCliente());
        contenido.put("forma_pag", "CRED");
        contenido.put("total", 0);
        contenido.put("cobrado", 0);

        contenido.put("fact_num2", t15.getCodigo_factura());
        contenido.put("cobrado2", t15.getPago());
        contenido.put("procesado", "S");
        db.insert("venta", null, contenido);


    }


//    Area de movimientos

    public ArrayList<nodo_factura> get_cobro() {
        ArrayList<nodo_factura> t12 = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM cobro";
        Cursor puntero = db.rawQuery(query, null);
        while (puntero.moveToNext()) {

            nodo_factura te = new nodo_factura(
                    puntero.getString(puntero.getColumnIndex("cod_factura")),
                    puntero.getString(puntero.getColumnIndex("fecha")),
                    puntero.getDouble(puntero.getColumnIndex("monto")),
                    puntero.getInt(puntero.getColumnIndex("cliente")),
                    puntero.getDouble(puntero.getColumnIndex("pago"))
            );
            t12.add(te);

        }
        puntero.close();
        db.close();


        return t12;
    }

    public ArrayList<nodo_factura> get_cobro(String cliente) {

        ArrayList<nodo_factura> t12 = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM cobro where cliente=? and estado=0";
        Cursor puntero = db.rawQuery(query, new String[]{cliente});
        while (puntero.moveToNext()) {

            nodo_factura te = new nodo_factura(
                    puntero.getString(puntero.getColumnIndex("cod_factura")),
                    puntero.getString(puntero.getColumnIndex("fecha")),
                    puntero.getDouble(puntero.getColumnIndex("monto")),
                    puntero.getInt(puntero.getColumnIndex("cliente")),
                    puntero.getDouble(puntero.getColumnIndex("pago")));

            Log.i("CEstado", puntero.getString(puntero.getColumnIndex("estado")));
            t12.add(te);

        }
        puntero.close();
        db.close();


        return t12;

    }

    public double get_cobro_total() {
        double t12 = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM cobro ";
        Cursor puntero = db.rawQuery(query, null);
        while (puntero.moveToNext()) {


            t12 = t12 + puntero.getDouble(puntero.getColumnIndex("monto"));


        }
        puntero.close();
        db.close();


        return t12;
    }

    public double get_cobro_total(String cliente) {

        double t12 = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM cobro where cliente=?";
        Cursor puntero = db.rawQuery(query, new String[]{cliente});
        while (puntero.moveToNext()) {


            t12 = t12 + puntero.getDouble(puntero.getColumnIndex("pago"));


        }
        puntero.close();
        db.close();


        return t12;

    }

    public void cobro(JSONObject cobro_activo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contenido = new ContentValues();

        try {
            Date t12 = new Date();
            contenido.put("cod_factura", cobro_activo.getInt("codigo"));
            contenido.put("monto", cobro_activo.getDouble("monto"));
            contenido.put("fecha", t12.getDate() + "-" + (t12.getMonth() + 1) + "-" + (1900 + t12.getYear()));
            contenido.put("cliente", cobro_activo.getInt("cliente"));
            contenido.put("pago", cobro_activo.getDouble("pago"));
            db.insert("cobro", null, contenido);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void cobro(ArrayList<nodo_factura> factural) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contenido = new ContentValues();
        Iterator<nodo_factura> t1 = factural.iterator();
        while (t1.hasNext()) {
            nodo_factura t2 = t1.next();
            contenido.put("cod_factura", t2.getCodigo_factura());
            contenido.put("monto", t2.getSaldo());
            contenido.put("fecha", t2.getFecha());
            contenido.put("cliente", t2.getCliente());
            contenido.put("pago", t2.getPago());
            Log.i("informaicon actual", t2.getCliente() + " " + t2.getSaldo());
            db.insert("cobro", null, contenido);
        }


    }

    public String get_producto(String codigo) {
        String nombre = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT descripcion as nombre FROM producto where cod_articulo=?";
        Cursor puntero = db.rawQuery(query, new String[]{codigo});
        while (puntero.moveToNext()) {
            nombre = puntero.getString(puntero.getColumnIndex("nombre"));


        }
        puntero.close();
        db.close();
        return nombre;


    }

    private String fecha_actual2() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select date('now', 'localtime','+1 day')  as actual";
        Cursor puntero = db.rawQuery(query, null);
        String fecha = "";
        while (puntero.moveToNext()) {
            fecha = puntero.getString(puntero.getColumnIndex("actual"));
        }
        puntero.close();

        return fecha;
    }

    private String fecha_actual() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select date('now', 'localtime')  as actual";
        Cursor puntero = db.rawQuery(query, null);
        String fecha = "";
        while (puntero.moveToNext()) {
            fecha = puntero.getString(puntero.getColumnIndex("actual"));
        }
        puntero.close();

        return fecha;
    }

    public double get_venta_total() {
        double tem_venta = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT cod_venta as codigo,cod_cli as cliente,movilizandome FROM venta where fecha=" + "'" + fecha_actual() + "'";
        Cursor puntero = db.rawQuery(query, null);
        while (puntero.moveToNext()) {

            venta te = new venta(puntero.getInt(puntero.getColumnIndex("codigo")), puntero.getString(puntero.getColumnIndex("cliente")), puntero.getInt(puntero.getColumnIndex("movilizandome")));
            String query2 = "SELECT * FROM detalle_venta where venta=" + puntero.getInt(puntero.getColumnIndex("codigo"));
            Cursor puntero2 = db.rawQuery(query2, null);
            while (puntero2.moveToNext()) {

                tem_venta += puntero2.getInt(puntero2.getColumnIndex("cantidad")) * puntero2.getDouble(puntero2.getColumnIndex("prec_vta"));


            }

            puntero2.close();
        }
        puntero.close();
        db.close();
        return tem_venta;

    }

    public double get_venta_total_piso() {
        double tem_venta = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT cod_venta as codigo,cod_cli as cliente,movilizandome FROM venta where fecha=" + "'" + fecha_actual2() + "'";
        Cursor puntero = db.rawQuery(query, null);
        while (puntero.moveToNext()) {

            venta te = new venta(puntero.getInt(puntero.getColumnIndex("codigo")), puntero.getString(puntero.getColumnIndex("cliente")), puntero.getInt(puntero.getColumnIndex("movilizandome")));
            String query2 = "SELECT * FROM detalle_venta where venta=" + puntero.getInt(puntero.getColumnIndex("codigo"));
            Cursor puntero2 = db.rawQuery(query2, null);
            while (puntero2.moveToNext()) {

                tem_venta += puntero2.getInt(puntero2.getColumnIndex("cantidad")) * puntero2.getDouble(puntero2.getColumnIndex("prec_vta"));


            }

            puntero2.close();
        }
        puntero.close();
        db.close();
        return tem_venta;

    }

    public ArrayList<venta> get_movimiento(String codigo) {
        ArrayList<venta> tem_venta = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT cod_venta as codigo,cod_cli as cliente,movilizandome FROM venta where  cod_cli=?";
        Cursor puntero = db.rawQuery(query, new String[]{codigo});
        while (puntero.moveToNext()) {

            venta te = new venta(puntero.getInt(puntero.getColumnIndex("codigo")), puntero.getString(puntero.getColumnIndex("cliente")), puntero.getInt(puntero.getColumnIndex("movilizandome")));
            String query2 = "SELECT * FROM detalle_venta where venta=" + puntero.getInt(puntero.getColumnIndex("codigo"));
            Cursor puntero2 = db.rawQuery(query2, null);
            while (puntero2.moveToNext()) {

                venta_detalle t2 = new venta_detalle(puntero2.getString(puntero2.getColumnIndex("nombre")), puntero2.getString(puntero2.getColumnIndex("articulo")), puntero2.getInt(puntero2.getColumnIndex("cantidad")), puntero2.getDouble(puntero2.getColumnIndex("prec_vta")));
                te.detalles.add(t2);

            }
            tem_venta.add(te);
            puntero2.close();
        }
        puntero.close();
        db.close();
        return tem_venta;

    }

    public ArrayList<venta2> get_venta(venta_detalle reviso) {
        ArrayList<venta2> tem_venta;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> ventap = new ArrayList<>();
        String query = "SELECT * FROM detalle_venta where articulo=?";
        Cursor puntero2 = db.rawQuery(query, new String[]{reviso.getProducto()});
        while (puntero2.moveToNext()) {
            String venta_actual = puntero2.getString(puntero2.getColumnIndex("venta"));
            ventap.add(venta_actual);


        }


        puntero2.close();
        db.close();

        tem_venta = ventas(ventap, reviso.getProducto());


        return tem_venta;

    }

    private ArrayList<venta2> ventas(ArrayList<String> ventap, String producto) {
        ArrayList<venta2> tem_venta = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Iterator<String> t2 = ventap.iterator();
        while (t2.hasNext()) {
            String t4 = t2.next();
            String query = "SELECT cod_venta as codigo, cod_cli as cliente,movilizandome FROM venta where cod_venta=? and fecha=" + "'" + fecha_actual() + "'";
            Cursor puntero = db.rawQuery(query, new String[]{t4});
            while (puntero.moveToNext()) {

                venta2 te = new venta2(puntero.getInt(puntero.getColumnIndex("codigo")), puntero.getString(puntero.getColumnIndex("cliente")), puntero.getInt(puntero.getColumnIndex("movilizandome")));
                String query2 = "SELECT * FROM detalle_venta where venta=? and articulo=?";
                Cursor puntero2 = db.rawQuery(query2, new String[]{String.valueOf(puntero.getInt(puntero.getColumnIndex("codigo"))), producto});
                while (puntero2.moveToNext()) {

                    venta_detalle t6 = new venta_detalle(puntero2.getString(puntero2.getColumnIndex("nombre")), puntero2.getString(puntero2.getColumnIndex("articulo")), puntero2.getInt(puntero2.getColumnIndex("cantidad")), puntero2.getDouble(puntero2.getColumnIndex("prec_vta")));
                    te.detalles.add(t6);

                }
                tem_venta.add(te);
                puntero2.close();
            }
            puntero.close();
        }
        db.close();
        return tem_venta;


    }

    public ArrayList<venta_detalle> get_detalle() {
        ArrayList<venta_detalle> tem_venta = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM detalle_venta";
        Cursor puntero2 = db.rawQuery(query, null);
        while (puntero2.moveToNext()) {
            venta_detalle exi = existe(tem_venta, puntero2.getString(puntero2.getColumnIndex("articulo")));
            if (exi == null) {
                venta_detalle t2 = new venta_detalle(puntero2.getString(puntero2.getColumnIndex("nombre")), puntero2.getString(puntero2.getColumnIndex("articulo")), puntero2.getInt(puntero2.getColumnIndex("cantidad")), puntero2.getDouble(puntero2.getColumnIndex("prec_vta")));
                tem_venta.add(t2);

            } else {
                exi.setCantidad(exi.getCantidad() + puntero2.getInt(puntero2.getColumnIndex("cantidad")));

            }


        }
        puntero2.close();
        db.close();
        return tem_venta;

    }

    private venta_detalle existe(ArrayList<venta_detalle> tem_venta, String producto) {
        Iterator<venta_detalle> taq = tem_venta.iterator();
        venta_detalle envio = null;
        while (taq.hasNext()) {
            venta_detalle tem14 = taq.next();
            if (tem14.getProducto().equals(producto)) {
                envio = tem14;
                break;
            }

        }
        return envio;
    }

    public ArrayList<venta> get_movimiento() {
        ArrayList<venta> tem_venta = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT cod_venta as codigo,cod_cli as cliente,movilizandome FROM venta where fecha=" + "'" + fecha_actual() + "'";
        Cursor puntero = db.rawQuery(query, null);
        while (puntero.moveToNext()) {

            venta te = new venta(puntero.getInt(puntero.getColumnIndex("codigo")), puntero.getString(puntero.getColumnIndex("cliente")), puntero.getInt(puntero.getColumnIndex("movilizandome")));
            String query2 = "SELECT * FROM detalle_venta where venta=" + puntero.getInt(puntero.getColumnIndex("codigo"));
            Cursor puntero2 = db.rawQuery(query2, null);
            while (puntero2.moveToNext()) {

                venta_detalle t2 = new venta_detalle(puntero2.getString(puntero2.getColumnIndex("nombre")), puntero2.getString(puntero2.getColumnIndex("articulo")), puntero2.getInt(puntero2.getColumnIndex("cantidad")),
                        puntero2.getDouble(puntero2.getColumnIndex("prec_vta")));
                te.detalles.add(t2);

            }
            tem_venta.add(te);
            puntero2.close();
        }
        puntero.close();
        db.close();
        return tem_venta;

    }

    public ArrayList<venta> get_venta_piso() {
        ArrayList<venta> tem_venta = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT cod_venta as codigo,cod_cli as cliente,movilizandome FROM venta where fecha=" + "'" + fecha_actual2() + "'";
        Cursor puntero = db.rawQuery(query, null);
        while (puntero.moveToNext()) {

            venta te = new venta(puntero.getInt(puntero.getColumnIndex("codigo")), puntero.getString(puntero.getColumnIndex("cliente")), puntero.getInt(puntero.getColumnIndex("movilizandome")));
            String query2 = "SELECT * FROM detalle_venta where venta=" + puntero.getInt(puntero.getColumnIndex("codigo"));
            Cursor puntero2 = db.rawQuery(query2, null);
            while (puntero2.moveToNext()) {

                venta_detalle t2 = new venta_detalle(puntero2.getString(puntero2.getColumnIndex("nombre")), puntero2.getString(puntero2.getColumnIndex("articulo")), puntero2.getInt(puntero2.getColumnIndex("cantidad")),
                        puntero2.getDouble(puntero2.getColumnIndex("prec_vta")));
                te.detalles.add(t2);

            }
            tem_venta.add(te);
            puntero2.close();
        }
        puntero.close();
        db.close();
        return tem_venta;

    }

    public int venta_codigo() {
        int ccodigo = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT max(cod_venta) as actual FROM venta";
        Cursor puntero = db.rawQuery(query, null);
        while (puntero.moveToNext()) {
            ccodigo = puntero.getInt(puntero.getColumnIndex("actual"));


        }
        puntero.close();
        db.close();
        return ccodigo;

    }


//    Area de Factura del Ingreso


    public void limpiar_factura() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "factura";
        db.delete(query, null, null);

    }

    public void limpiar_cobro() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "cobro";
        db.delete(query, null, null);

    }

    public void limpiar_producto() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "producto";
        db.delete(query, null, null);

    }

    public void limpiar_cliente() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "cliente";
        db.delete(query, null, null);

    }

    public void limpiar_configuracion() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "configuracion";
        db.delete(query, null, null);

    }

    public boolean insert_configuracion(JSONObject factura) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contenido = new ContentValues();

        try {
            contenido.put("usuario", factura.getString("Usuario_Id"));
            contenido.put("ruta", factura.getString("Ruta"));
            contenido.put("telefono", factura.getString("Telefono"));
            contenido.put("recarga", factura.getString("E_Recarga"));
            contenido.put("saldo_pdv", factura.getString("Saldo_PDV"));
            contenido.put("saldo_ss", factura.getString("Saldo_SS"));
            contenido.put("f_orga", factura.getDouble("F_ORGA"));
            contenido.put("estado", factura.getInt("Estado"));


            db.insert("configuracion", null, contenido);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return true;
    }

    public boolean insert_factura(JSONObject factura) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contenido = new ContentValues();

        try {
            contenido.put("cod_factura", factura.getInt("fact_num"));
            contenido.put("monto", factura.getDouble("saldo"));
            contenido.put("fecha", factura.getString("fec_emis"));
            contenido.put("cliente", factura.getInt("co_cli"));
            db.insert("factura", null, contenido);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return true;
    }

// Area de obtener las facturas

    public ArrayList<nodo_factura> get_factura_activa() {
        ArrayList<nodo_factura> tem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM factura pago>0", null);


        while (puntero.moveToNext()) {
            int codigo = puntero.getInt(puntero.getColumnIndex("cod_factura"));
            Double monto = puntero.getDouble(puntero.getColumnIndex("monto"));
            String fecha = puntero.getString(puntero.getColumnIndex("fecha"));
            Double pago = puntero.getDouble(puntero.getColumnIndex("pago"));
            int cliente_v = puntero.getInt(puntero.getColumnIndex("cliente"));


            nodo_factura tem2 = new nodo_factura(String.valueOf(codigo), fecha, monto, cliente_v, pago);
            tem.add(tem2);


        }

        puntero.close();
        db.close();


        return tem;
    }


    public ArrayList<nodo_factura> get_factura() {
        ArrayList<nodo_factura> tem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM factura", null);


        while (puntero.moveToNext()) {
            int codigo = puntero.getInt(puntero.getColumnIndex("cod_factura"));
            Double monto = puntero.getDouble(puntero.getColumnIndex("monto"));
            String fecha = puntero.getString(puntero.getColumnIndex("fecha"));
            Double pago = puntero.getDouble(puntero.getColumnIndex("pago"));
            int cliente_v = puntero.getInt(puntero.getColumnIndex("cliente"));


            nodo_factura tem2 = new nodo_factura(String.valueOf(codigo), fecha, monto, cliente_v, pago);
            tem.add(tem2);


        }

        puntero.close();
        db.close();


        return tem;
    }


    public boolean insertcliente(JSONObject info) throws JSONException {

        if (existe_cliente(info.get("co_cli").toString().trim())) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contenido = new ContentValues();
            contenido.put("cod_cliente", info.get("co_cli").toString().trim());
            double credito = Double.parseDouble(info.get("Credito").toString().trim());
            contenido.put("credito", credito);
            contenido.put("descripcion", info.get("cli_des").toString().trim());
            contenido.put("direccion", info.get("Direccion").toString().trim());
            contenido.put("telefono", info.get("telefonos").toString().trim());
            contenido.put("fax", info.get("fax").toString().trim());
            contenido.put("encargado", info.get("respons").toString().trim());
            int ruta = Integer.parseInt(info.get("EnRuta").toString().trim());
            contenido.put("enruta", ruta);

            db.insert("cliente", null, contenido);
//            Log.i("cliente nuevo",contenido.toString());
        } else {

            return false;
        }


        return true;
    }

    private boolean existe_cliente(String cod_cliente) {
        boolean existe_f = true;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT count(cod_cliente) as existe FROM cliente where cod_cliente=?";
        Cursor puntero = db.rawQuery(query, new String[]{cod_cliente});
        while (puntero.moveToNext()) {
            int ccodigo = puntero.getInt(puntero.getColumnIndex("existe"));

            if (ccodigo > 0) {
                existe_f = false;
                break;

            }

        }
        puntero.close();
        db.close();
        return existe_f;

    }

    public boolean insertproducto(JSONObject info) throws JSONException {

        if (existe_producto(info.get("co_art").toString().trim())) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contenido = new ContentValues();
            contenido.put("cod_articulo", info.get("co_art").toString().trim());
            contenido.put("descripcion", info.get("art_des").toString().trim());
            double precio = Double.parseDouble(info.get("prec_vta1").toString().trim());
            double stock = Double.parseDouble(info.get("stock_act").toString().trim());
            contenido.put("precio", precio);
            contenido.put("stock", stock);
            db.insert("producto", null, contenido);
        } else {

            return false;
        }


        return true;
    }

    private boolean existe_producto(String codigo) {
        boolean existe_f = true;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT count(cod_articulo) as existe FROM producto where cod_articulo=?";
        Cursor puntero = db.rawQuery(query, new String[]{codigo});
        while (puntero.moveToNext()) {
            int ccodigo = puntero.getInt(puntero.getColumnIndex("existe"));

            if (ccodigo > 0) {
                existe_f = false;
                break;

            }

        }
        puntero.close();
        db.close();
        return existe_f;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public ArrayList<nodo_producto> getall() {
        ArrayList<nodo_producto> tem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM producto where stock>0", null);


        while (puntero.moveToNext()) {
            String codigo = puntero.getString(puntero.getColumnIndex("cod_articulo"));
            String descripcion = puntero.getString(puntero.getColumnIndex("descripcion"));
            Double precio = puntero.getDouble(puntero.getColumnIndex("precio"));
            int stock = puntero.getInt(puntero.getColumnIndex("stock"));
//            Log.i("Valor Actual",codigo);

            nodo_producto tem2 = new nodo_producto(codigo, descripcion, stock, precio, 0, 0);
            tem.add(tem2);


        }

        puntero.close();
        db.close();
        return tem;

    }

    public Nodo_tarea getcliente_actual_fuera(String codigo2) {
        SQLiteDatabase db = this.getReadableDatabase();
        Nodo_tarea tem = null;
        Cursor puntero = db.rawQuery("SELECT * FROM cliente where enruta=0 and  cod_cliente=? and telefono!='tarjetas'", new String[]{codigo2});
        while (puntero.moveToNext()) {
            String codigo = puntero.getString(puntero.getColumnIndex("cod_cliente"));
            Double credito = puntero.getDouble(puntero.getColumnIndex("credito"));
            String descripcion = puntero.getString(puntero.getColumnIndex("descripcion"));
            String direccion = puntero.getString(puntero.getColumnIndex("direccion"));
            String telefono = puntero.getString(puntero.getColumnIndex("telefono"));
            String fax = puntero.getString(puntero.getColumnIndex("fax"));
            String encargado = puntero.getString(puntero.getColumnIndex("encargado"));


            tem = new Nodo_tarea(codigo, credito, descripcion, direccion, telefono, fax, encargado);


        }

        puntero.close();
        db.close();
        return tem;

    }

    public Nodo_tarea getcliente_actual(String codigo2) {
        SQLiteDatabase db = this.getReadableDatabase();
        Nodo_tarea tem = null;
        Cursor puntero = db.rawQuery("SELECT * FROM cliente where cod_cliente=?", new String[]{codigo2});
        while (puntero.moveToNext()) {
            String codigo = puntero.getString(puntero.getColumnIndex("cod_cliente"));
            Double credito = puntero.getDouble(puntero.getColumnIndex("credito"));
            String descripcion = puntero.getString(puntero.getColumnIndex("descripcion"));
            String direccion = puntero.getString(puntero.getColumnIndex("direccion"));
            String telefono = puntero.getString(puntero.getColumnIndex("telefono"));
            String fax = puntero.getString(puntero.getColumnIndex("fax"));
            String encargado = puntero.getString(puntero.getColumnIndex("encargado"));


            tem = new Nodo_tarea(codigo, credito, descripcion, direccion, telefono, fax, encargado);


        }

        puntero.close();
        db.close();
        return tem;

    }

    public ArrayList<Nodo_tarea> getallcliente() {
        ArrayList<Nodo_tarea> tem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM cliente where enruta>0", null);


        while (puntero.moveToNext()) {
            String codigo = puntero.getString(puntero.getColumnIndex("cod_cliente"));
            Double credito = puntero.getDouble(puntero.getColumnIndex("credito"));
            String descripcion = puntero.getString(puntero.getColumnIndex("descripcion"));
            String direccion = puntero.getString(puntero.getColumnIndex("direccion"));
            String telefono = puntero.getString(puntero.getColumnIndex("telefono"));
            String fax = puntero.getString(puntero.getColumnIndex("fax"));
            String encargado = puntero.getString(puntero.getColumnIndex("encargado"));


            Nodo_tarea tem2 = new Nodo_tarea(codigo, credito, descripcion, direccion, telefono, fax, encargado);
            tem.add(tem2);


        }

        puntero.close();
        db.close();
        return tem;


    }

    public ArrayList<Nodo_tarea> getallcliente_fuera() {
        ArrayList<Nodo_tarea> tem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM cliente where enruta=0 and telefono!='tarjeta'", null);


        while (puntero.moveToNext()) {
            String codigo = puntero.getString(puntero.getColumnIndex("cod_cliente"));
            Double credito = puntero.getDouble(puntero.getColumnIndex("credito"));
            String descripcion = puntero.getString(puntero.getColumnIndex("descripcion"));
            String direccion = puntero.getString(puntero.getColumnIndex("direccion"));
            String telefono = puntero.getString(puntero.getColumnIndex("telefono"));
            String fax = puntero.getString(puntero.getColumnIndex("fax"));
            String encargado = puntero.getString(puntero.getColumnIndex("encargado"));


            Nodo_tarea tem2 = new Nodo_tarea(codigo, credito, descripcion, direccion, telefono, fax, encargado);
            tem.add(tem2);


        }

        puntero.close();
        db.close();
        return tem;


    }

    public ArrayList<Nodo_tarea> getallcliente_fac() {
        ArrayList<Nodo_tarea> tem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM cliente where enruta=1", null);


        while (puntero.moveToNext()) {
            String codigo = puntero.getString(puntero.getColumnIndex("cod_cliente"));
            Double credito = puntero.getDouble(puntero.getColumnIndex("credito"));
            String descripcion = puntero.getString(puntero.getColumnIndex("descripcion"));
            String direccion = puntero.getString(puntero.getColumnIndex("direccion"));
            String telefono = puntero.getString(puntero.getColumnIndex("telefono"));
            String fax = puntero.getString(puntero.getColumnIndex("fax"));
            String encargado = puntero.getString(puntero.getColumnIndex("encargado"));


            Nodo_tarea tem2 = new Nodo_tarea(codigo, credito, descripcion, direccion, telefono, fax, encargado);
            tem.add(tem2);


        }

        puntero.close();
        db.close();
        return tem;


    }

    public void rebaja_inventario(ArrayList<nodo_producto> carrito) {
        SQLiteDatabase db = this.getWritableDatabase();

        Iterator<nodo_producto> tu = carrito.iterator();
        while (tu.hasNext()) {
            nodo_producto t = tu.next();
            String query = "UPDATE producto SET stock = stock -" + t.getCompra() + " " + "WHERE cod_articulo =" + '"' + t.getCodigo() + '"';
            db.execSQL(query);

        }
    }

    public void update_factura(double pago, String codigo_factura) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE factura SET pago = " + pago + " " + "WHERE cod_factura =" + Integer.parseInt(codigo_factura);
        db.execSQL(query);

    }

    public ArrayList<nodo_producto> getall_fuera() {
        ArrayList<nodo_producto> tem = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM producto where stock>0 and cod_articulo='ORGA.01' or cod_articulo='TAE RUTAS'", null);


        while (puntero.moveToNext()) {
            String codigo = puntero.getString(puntero.getColumnIndex("cod_articulo"));
            String descripcion = puntero.getString(puntero.getColumnIndex("descripcion"));
            Double precio = puntero.getDouble(puntero.getColumnIndex("precio"));
            int stock = puntero.getInt(puntero.getColumnIndex("stock"));
//            Log.i("Valor Actual",codigo);
            nodo_producto tem2 = new nodo_producto(codigo, descripcion, stock, precio, 0, 0);
            tem.add(tem2);
        }

        puntero.close();
        db.close();
        return tem;

    }

    public String get_ruta() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT ruta FROM configuracion ", null);
        String ruta = "";
        while (puntero.moveToNext()) {
            ruta = puntero.getString(puntero.getColumnIndex("ruta"));


        }
        puntero.close();
        return ruta;

    }

    public int get_estado() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT estado FROM configuracion ", null);
        int ruta = -1;

        while (puntero.moveToNext()) {
            ruta = puntero.getInt(puntero.getColumnIndex("estado"));


        }

        puntero.close();

        return ruta;

    }

    public String[] get_access() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT count(*) as existe,estado FROM configuracion ", null);
        String[] ruta = new String[2];

        while (puntero.moveToNext()) {
            ruta[0] = puntero.getString(puntero.getColumnIndex("existe"));
            ruta[1] = puntero.getString(puntero.getColumnIndex("estado"));


        }

        puntero.close();
        db.close();
        return ruta;

    }

    public void estado(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE configuracion SET estado = " + i;
        db.execSQL(query);

    }


    public String get_telefono() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT telefono FROM configuracion ", null);
        String ruta = "";

        while (puntero.moveToNext()) {
            ruta = puntero.getString(puntero.getColumnIndex("telefono"));


        }

        puntero.close();
        db.close();
        return ruta;
    }


    public void ver_codigo_encabezado() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM encabezado ", null);

        while (puntero.moveToNext()) {
            Log.i("Codigo de Venta para envio", puntero.getString(puntero.getColumnIndex("cod_encabezado")));


        }

        puntero.close();


    }


    public void ver_detalle() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM detalle ", null);
        while (puntero.moveToNext()) {
            Log.i("revisar articulo", puntero.getString(puntero.getColumnIndex("articulo")));
            Log.i("revisar prec_vta", puntero.getString(puntero.getColumnIndex("prec_vta")));
            Log.i("revisar cantidad", puntero.getString(puntero.getColumnIndex("cantidad")));
            Log.i("revisar total", puntero.getString(puntero.getColumnIndex("total_art")));
            Log.i("revisar numero_cel", puntero.getString(puntero.getColumnIndex("numero_cel")));
            Log.i("revisar encabezado", puntero.getString(puntero.getColumnIndex("encabezado")));
        }
        puntero.close();
    }


    public void setcliente(String cod_cliente) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT * FROM cliente where cod_cliente=" + '"' + cod_cliente + '"', null);

        int tipo_usuario = -1;
        while (puntero.moveToNext()) {

            tipo_usuario = puntero.getInt(puntero.getColumnIndex("enruta"));

        }

        puntero.close();

        if (tipo_usuario == 1) {
            String query = "UPDATE cliente SET enruta=0  where cod_cliente=" + '"' + cod_cliente + '"';
            db.execSQL(query);


        }

        db.close();

    }

    public String get_saldo_ss() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT saldo_ss FROM configuracion ", null);
        String ruta = "";

        while (puntero.moveToNext()) {
            ruta = puntero.getString(puntero.getColumnIndex("saldo_ss"));


        }

        puntero.close();
        db.close();
        return ruta;

    }

    public String saldo_pdv() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT saldo_pdv FROM configuracion ", null);
        String ruta = "";

        while (puntero.moveToNext()) {
            ruta = puntero.getString(puntero.getColumnIndex("saldo_pdv"));


        }

        puntero.close();
        db.close();
        return ruta;


    }

    public String get_recarga() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT recarga FROM configuracion ", null);
        String ruta = "";

        while (puntero.moveToNext()) {
            ruta = puntero.getString(puntero.getColumnIndex("recarga"));


        }

        puntero.close();
        db.close();
        return ruta;


    }

    public double get_f_orga() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor puntero = db.rawQuery("SELECT f_orga FROM configuracion ", null);
        double ruta = 0;

        while (puntero.moveToNext()) {
            ruta = puntero.getDouble(puntero.getColumnIndex("f_orga"));


        }

        puntero.close();
        db.close();
        return ruta;

    }

}
