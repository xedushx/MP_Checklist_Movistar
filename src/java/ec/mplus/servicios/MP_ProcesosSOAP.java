/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.servicios;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ec.xprime.geocode.GoogleGeoCodeHelper;
import ec.xprime.geocode.GoogleGeoCodeResponse;
import ec.xprime.persistencia.cla_conexion;
import ec.xprime.sistema.sis_soporte;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author xEdushx
 */
@Stateless
@LocalBean
public class MP_ProcesosSOAP {

    private final static Logger LOGGER = Logger.getLogger(MP_ProcesosSOAP.class.getName());
    private int ccu_codigo;

    /**
     * Sirve para validar el acceso del usuario móvil a la aplicación
     *
     * @param nombre ==>> nombre del usuario
     * @param clave ==>> clave del usuario
     * @return respuesta en formato JSON: responseCode; responseMessage
     */
    public String validarUsuario(String id_empresa, String nombre, String clave) {
        JsonObject ljo_respuesta = new JsonObject();

        cla_conexion conexion = new cla_conexion();

        if (!id_empresa.isEmpty() && !nombre.isEmpty() && !clave.isEmpty()) {

            //validar usuario
            if (conexion.validar_usuario_movil(id_empresa, nombre, clave)) {
                ljo_respuesta.addProperty("responseCode", 0);
                ljo_respuesta.addProperty("responseMessage", "OK;" + conexion.obtener_id_usuario(id_empresa, nombre, clave));
            } else {
                ljo_respuesta.addProperty("responseCode", 100);
                ljo_respuesta.addProperty("responseMessage", "Usuario o Clave Incorrectas");
            }
        }

        conexion.desconectar();

        return ljo_respuesta.toString();
    }

    /**
     * Sirve para obtener los catálogos de la app
     *
     * @param id_empresa ==>> código de la empresa
     * @param operacion ==>> operación a ejecutar: G para guias;
     * @param id_usuario ==>> código del usuario
     * @return retorna el catálogo en formato JSON
     */
    public String descargarCatalogos(String id_empresa, String operacion, String id_usuario) {

        JsonArray lja_respuesta = new JsonArray();
        JsonObject ljo_objeto;
        cla_conexion conexion = new cla_conexion();
        try {
            if (operacion.toUpperCase().equals("EB")) {

                List lista = conexion.consultar("Select ees_codigo,ees_nombre, "
                        + "CASE WHEN ees_latitud is null THEN '0,0' "
                        + "WHEN ees_latitud = '' THEN '0,0' "
                        + "ELSE ees_latitud "
                        + "END, "
                        + "CASE WHEN ees_longitud is null THEN '0,0' "
                        + "WHEN ees_longitud = '' THEN '0,0' "
                        + "ELSE ees_longitud "
                        + "END, "
                        + "ees_codigo_2 "
                        + "from mov_estructura_estacion "
                        + "where id_empresa = " + id_empresa
                        + " and ees_es_estacion = TRUE "
                        + "and ees_estado = TRUE");

                if (lista.size() > 0) {
                    Object[] campos;
                    for (int i = 0; i < lista.size(); i++) {
                        campos = (Object[]) lista.get(i);

                        ljo_objeto = new JsonObject();
                        ljo_objeto.addProperty("ees_codigo", (Integer) campos[0]);
                        ljo_objeto.addProperty("ees_nombre", (String) campos[1]);
                        ljo_objeto.addProperty("ees_latitud", (String) campos[2]);
                        ljo_objeto.addProperty("ees_longitud", (String) campos[3]);
                        ljo_objeto.addProperty("ees_codigo_2", (Integer) campos[4]);

                        lja_respuesta.add(ljo_objeto);
                    }
                } else {
                    ljo_objeto = null;
                }
            } else if (operacion.toUpperCase().equals("PR")) {

                List lista = conexion.consultar("Select ees_codigo,"
                        + "ees_nombre, "
                        + "ees_codigo_2 "
                        + "from mov_estructura_estacion "
                        + "where id_empresa = " + id_empresa
                        + " and ees_es_provincia = TRUE "
                        + "and ees_estado = TRUE");

                if (lista.size() > 0) {
                    Object[] campos;
                    for (int i = 0; i < lista.size(); i++) {
                        campos = (Object[]) lista.get(i);

                        ljo_objeto = new JsonObject();
                        ljo_objeto.addProperty("ees_codigo", (Integer) campos[0]);
                        ljo_objeto.addProperty("ees_nombre", (String) campos[1]);
                        ljo_objeto.addProperty("ees_codigo_2", (Integer) campos[2]);

                        lja_respuesta.add(ljo_objeto);
                    }
                } else {
                    ljo_objeto = null;
                }
            } else if (operacion.toUpperCase().equals("CI")) {

                List lista = conexion.consultar("Select ees_codigo,"
                        + "ees_nombre, "
                        + "ees_codigo_2 "
                        + "from mov_estructura_estacion "
                        + "where id_empresa = " + id_empresa
                        + " and ees_es_ciudad = TRUE "
                        + "and ees_estado = TRUE");

                if (lista.size() > 0) {
                    Object[] campos;
                    for (int i = 0; i < lista.size(); i++) {
                        campos = (Object[]) lista.get(i);

                        ljo_objeto = new JsonObject();
                        ljo_objeto.addProperty("ees_codigo", (Integer) campos[0]);
                        ljo_objeto.addProperty("ees_nombre", (String) campos[1]);
                        ljo_objeto.addProperty("ees_codigo_2", (Integer) campos[2]);

                        lja_respuesta.add(ljo_objeto);
                    }
                } else {
                    ljo_objeto = null;
                }
            } else if (operacion.toUpperCase().equals("EQ")) {

                List lista = conexion.consultar("Select eqm_codigo,"
                        + "eqm_descripcion, "
                        + "eqm_marca, "
                        + "eqm_modelo, "
                        + "eqm_serie "
                        + "from mov_equipos_mantenimiento "
                        + "where id_empresa = " + id_empresa);

                if (lista.size() > 0) {
                    Object[] campos;
                    for (int i = 0; i < lista.size(); i++) {
                        campos = (Object[]) lista.get(i);

                        ljo_objeto = new JsonObject();
                        ljo_objeto.addProperty("eqm_codigo", (Integer) campos[0]);
                        ljo_objeto.addProperty("eqm_descripcion", (String) campos[1]);
                        ljo_objeto.addProperty("eqm_marca", (String) campos[2]);
                        ljo_objeto.addProperty("eqm_modelo", (String) campos[3]);
                        ljo_objeto.addProperty("eqm_serie", (String) campos[4]);

                        lja_respuesta.add(ljo_objeto);
                    }
                } else {
                    ljo_objeto = null;
                }
            } else if (operacion.toUpperCase().equals("IN")) {

                List lista = conexion.consultar("Select i.ins_codigo,"
                        + "c.cin_nombre, "
                        + "i.ins_codigo_externo, "
                        + "i.ins_lpu, "
                        + "i.ins_descripcion, "
                        + "u.uni_simbologia, "
                        + "i.ins_precio_referencial, "
                        + "i.ins_factor "
                        + "from mov_insumos i, mov_unidades u, mov_categoria_insumo c "
                        + "where i.id_empresa = " + id_empresa
                        + " and i.cin_codigo = c.cin_codigo "
                        + "and i.uni_codigo = u.uni_codigo");

                if (lista.size() > 0) {
                    Object[] campos;
                    for (int i = 0; i < lista.size(); i++) {
                        campos = (Object[]) lista.get(i);

                        ljo_objeto = new JsonObject();
                        ljo_objeto.addProperty("ins_codigo", (Integer) campos[0]);
                        ljo_objeto.addProperty("cin_nombre", (String) campos[1]);
                        ljo_objeto.addProperty("ins_codigo_externo", (String) campos[2]);
                        ljo_objeto.addProperty("ins_lpu", (String) campos[3]);
                        ljo_objeto.addProperty("ins_descripcion", (String) campos[4]);
                        ljo_objeto.addProperty("uni_simbologia", (String) campos[5]);
                        ljo_objeto.addProperty("ins_precio_referencial", ((BigDecimal) campos[6]).toString());
                        ljo_objeto.addProperty("ins_factor", ((BigDecimal) campos[7]).toString());

                        lja_respuesta.add(ljo_objeto);
                    }
                } else {
                    ljo_objeto = null;
                }
            } else if (operacion.toUpperCase().equals("PRV")) {
                List lista = conexion.consultar("Select * from mov_proveedores");
                if (lista.size() > 0) {
                    Object[] campos;
                    for (int i = 0; i < lista.size(); i++) {
                        campos = (Object[]) lista.get(i);

                        ljo_objeto = new JsonObject();
                        ljo_objeto.addProperty("prv_codigo", (Integer) campos[0]);
                        ljo_objeto.addProperty("id_empresa", (Integer) campos[1]);
                        ljo_objeto.addProperty("prv_nombre", (String) campos[2]);
                        ljo_objeto.addProperty("prv_contacto", (String) campos[3]);
                        ljo_objeto.addProperty("prv_telefonos", (String) campos[4]);
                        ljo_objeto.addProperty("prv_datos_adicionales", (String) campos[5]);

                        lja_respuesta.add(ljo_objeto);
                    }
                } else {
                    ljo_objeto = null;
                }
            }
//            } else if (operacion.toUpperCase().equals("TMA")) {
//                List lista = conexion.consultar("Select * from mov_tipo_mantenimiento");
//                if (lista.size() > 0) {
//                    Object[] campos;
//                    for (int i = 0; i < lista.size(); i++) {
//                        campos = (Object[]) lista.get(i);
//
//                        ljo_objeto = new JsonObject();
//                        ljo_objeto.addProperty("tma_codigo", (Integer) campos[0]);
//                        ljo_objeto.addProperty("tma_nombre", (String) campos[1]);
//
//                        lja_respuesta.add(ljo_objeto);
//                    }
//                } else {
//                    ljo_objeto = null;
//                } funcio
        } catch (Exception e) {
            Logger.getLogger("Error: descarga catalogos " + e);
            return e.toString();
        }

        conexion.desconectar();

        return lja_respuesta.toString();
    }

    /**
     * Sirve para obtener los catálogos de la app
     *
     * @param id_empresa ==>> código de la empresa
     * @param operacion ==>> operación a ejecutar: G para guias;
     * @param id_usuario ==>> código del usuario
     * @return retorna el catálogo en formato JSON
     */
    public String descargarCatalogosPendientes() {

        JsonArray lja_respuesta = new JsonArray();
        JsonObject ljo_objeto;
        cla_conexion conexion = new cla_conexion();
        try {

            List listaCat = conexion.consultar("Select * from catalogo_maestro");

            if (listaCat.size() > 0) {
                Object[] campos;
                for (int i = 0; i < listaCat.size(); i++) {
                    campos = (Object[]) listaCat.get(i);

                    ljo_objeto = new JsonObject();
                    ljo_objeto.addProperty("id", (Integer) campos[0]);
                    ljo_objeto.addProperty("nombre", (String) campos[1]);
                    ljo_objeto.addProperty("codigo", (String) campos[2]);
                    ljo_objeto.addProperty("valor", (String) campos[3]);
                    ljo_objeto.addProperty("nombrepadre", (String) campos[4]);
                    ljo_objeto.addProperty("codigopadre", (String) campos[5]);

                    lja_respuesta.add(ljo_objeto);
                }
            } else {
                ljo_objeto = null;
            }
            
            List listaPen = conexion.consultar("Select * from pendientes");

            if (listaPen.size() > 0) {
                Object[] campos;
                for (int i = 0; i < listaPen.size(); i++) {
                    campos = (Object[]) listaPen.get(i);

                    ljo_objeto = new JsonObject();
                    ljo_objeto.addProperty("id", (Integer) campos[0]);
                    ljo_objeto.addProperty("estacion_id", (Integer) campos[1]);
                    ljo_objeto.addProperty("tipo_estacion", (String) campos[2]);
                    ljo_objeto.addProperty("fecha", (String) campos[3].toString());
                    ljo_objeto.addProperty("semana", (Integer) campos[4]);
                    ljo_objeto.addProperty("ticket", (String) campos[5]);
                    ljo_objeto.addProperty("provincia", (String) campos[6]);
                    ljo_objeto.addProperty("region", (String) campos[7]);
                    ljo_objeto.addProperty("partner", (String) campos[8]);
                    ljo_objeto.addProperty("grupo", (String) campos[9]);
                    ljo_objeto.addProperty("seccion", (String) campos[10]);
                    ljo_objeto.addProperty("tier1", (String) campos[11]);
                    ljo_objeto.addProperty("tier2", (String) campos[12]);
                    ljo_objeto.addProperty("tier3", (String) campos[13]);
                    ljo_objeto.addProperty("criticidad_pendiente", (String) campos[14]);
                    ljo_objeto.addProperty("tecnico_responsable", (String) campos[15]);
                    ljo_objeto.addProperty("responsable", (String) campos[16]);
                    ljo_objeto.addProperty("fecha_cierre_pendiente", (String) campos[17]);
                    ljo_objeto.addProperty("observacion1", (String) campos[18]);              
                    ljo_objeto.addProperty("supervisor_huawei", (String) campos[19]);
                    ljo_objeto.addProperty("fecha_mp", (String) campos[20].toString());
                    ljo_objeto.addProperty("mes_cerrado", (String) campos[21]);
                    ljo_objeto.addProperty("pend_obser", (String) campos[22]);
                    ljo_objeto.addProperty("estatus", (String) campos[23]);
                    ljo_objeto.addProperty("indisponibilidad", (String) campos[24]);
                    ljo_objeto.addProperty("estatus_pendiente", (String) campos[25]);

                    
                    

                    lja_respuesta.add(ljo_objeto);
                }
            } else {
                ljo_objeto = null;
            }

//            } else if (operacion.toUpperCase().equals("TMA")) {
//                List lista = conexion.consultar("Select * from mov_tipo_mantenimiento");
//                if (lista.size() > 0) {
//                    Object[] campos;
//                    for (int i = 0; i < lista.size(); i++) {
//                        campos = (Object[]) lista.get(i);
//
//                        ljo_objeto = new JsonObject();
//                        ljo_objeto.addProperty("tma_codigo", (Integer) campos[0]);
//                        ljo_objeto.addProperty("tma_nombre", (String) campos[1]);
//
//                        lja_respuesta.add(ljo_objeto);
//                    }
//                } else {
//                    ljo_objeto = null;
//                } funcio
        } catch (Exception e) {
            Logger.getLogger("Error: descarga catalogos " + e);
            return e.toString();
        }

        conexion.desconectar();

        return lja_respuesta.toString();
    }

    /*
     * Metodo del web service que ingresa los datos iniciales del formulario 4X1
     * Devuelve un valor string de inserción
     */
    public String insertarFotosWizard4x1(
            String dcu_fecha,
            String dcu_hora,
            String dcu_accion,
            String dcu_detalle,
            String dcu_foto,
            String dcu_usuario) {

        JsonObject ljo_respuesta = new JsonObject();
        cla_conexion conexion = new cla_conexion();
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        LOGGER.log(Level.INFO,dcu_foto );
        int gui_codigo = -1;
        String lstr_resultado = "";
        if (!dcu_foto.isEmpty()) {
            Gson gson = new Gson();

            //inserta los datos en la tabla
            String SQL_INSERT = "INSERT INTO mov_det_cuatro_uno("
                    // + "ccu_codigo,"
                    + "dcu_fecha, "
                    + "dcu_hora,"
                    + "dcu_accion,"
                    + "dcu_detalle,"
                    + "dcu_foto,"
                    + "dcu_usuario)"
                    + "VALUES(?,?,?,?,?,?)";

            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                //statement.setInt(1, );
                statement.setString(1, dcu_fecha);
                statement.setString(2, dcu_hora);
                statement.setString(3, dcu_accion);
                statement.setString(4, dcu_detalle);
                statement.setString(5, dcu_foto);
                statement.setString(6, dcu_usuario);


                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al insertar el wizard";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        ccu_codigo = generatedKeys.getInt(1);
                        lstr_resultado = String.valueOf(ccu_codigo);
                    } else {
                        lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar el wizard: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar el wizard: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
            ljo_respuesta.addProperty("responseCode", 0);
            ljo_respuesta.addProperty("responseMessage", lstr_resultado);

            if (ccu_codigo > 0) {

                ljo_respuesta.addProperty("responseCode", 0);
                ljo_respuesta.addProperty("responseMessage", "OK");
            } else {
                ljo_respuesta.addProperty("responseCode", 400);
                ljo_respuesta.addProperty("responseMessage", lstr_resultado);
            }

        } else {
            ljo_respuesta.addProperty("responseCode", 100);
            ljo_respuesta.addProperty("responseMessage", "Debe ingresar la empresa");
        }


        conexion.desconectar();

        return ljo_respuesta.toString();
    }

    public String insertarCabecera4x1(
            String id_empresa,
            String id_usuario,
            String prv_codigo,
            String ees_codigo,
            String tma_para_codigo,
            //Se envia el tipo mantenimiento para obtener el código y poderlo ingresar en la base
            String eaf_codigo,
            String ccu_fecha_elaboracion,
            String ccu_fecha_inicio_evento,
            String ccu_fecha_hora_aviso,
            String ccu_fecha_hora_llegada,
            String ccu_tiempo_reaccion,
            String ccu_fecha_hora_reparacion,
            String ccu_soporte_tecnico,
            String ccu_solucionado,
            String ccu_tiempo_solucion,
            String ccu_operador_noc,
            String ccu_tipo,
            String ccu_accedio_sitio,
            String ccu_observaciones,
            String ccu_latitud,
            String ccu_longitud,
            String ccu_imei,
            String ccu_remedy,
            String codigo_foto_temporal) {

        JsonObject ljo_respuesta = new JsonObject();
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        cla_conexion conexion = new cla_conexion();

        String lstr_resultado = "";
        String lstr_latitud = ccu_latitud.replace(",", ".");
        String lstr_longitud = ccu_longitud.replace(",", ".");

        if (!id_empresa.isEmpty()) {


            Gson gson = new Gson();
            GoogleGeoCodeResponse result;


            String ccu_fecha_sincronizacion = sis_soporte.obtener_instancia_soporte().getFechaHoraActual();

            //se necesita obtener el codigo del tipo mantenimiento y el 
            //tiempo reacción que se lo calcula de la fecha_hora_llegada- fecha_hora aviso
            List lstr_tma_codigo = conexion.consultar("Select tma_codigo, tma_descripcion from mov_tipo_mantenimiento where tma_descripcion like '" + tma_para_codigo.trim() + "'");
            //akí usas el Object[]
            String tma_codigo = "";
            if (lstr_tma_codigo.size() > 0) {
                Object[] campos;
                campos = (Object[]) lstr_tma_codigo.get(0);
                tma_codigo = campos[0].toString();
            }


            System.out.println("********* TMA_CODIGO: " + tma_codigo);
            String ccu_ticket_consulta = "";
            List lstr_ccu_ticket = conexion.consultar("Select ccu_codigo,id_empresa from mov_cab_cuatro_uno order by ccu_codigo desc limit 1");
            if (lstr_ccu_ticket.size() > 0) {
                Object[] campos;
                campos = (Object[]) lstr_ccu_ticket.get(0);
                ccu_ticket_consulta = campos[0].toString();
            }

            String ccu_ticket = ccu_tipo.trim() + "-" + (Integer.parseInt(ccu_ticket_consulta) + 1);

            //inserta los datos en la tabla
            String SQL_INSERT = "INSERT INTO mov_cab_cuatro_uno("
                    + "id_empresa,"
                    + "id_usuario,"
                    + "prv_codigo,"
                    + "ees_codigo,"
                    + "tma_codigo,"
                    + "eaf_codigo,"
                    + "ccu_fecha_elaboracion,"
                    + "ccu_fecha_sincronizacion,"
                    + "ccu_fecha_inicio_evento,"
                    + "ccu_fecha_hora_aviso,"
                    + "ccu_fecha_hora_llegada,"
                    + "ccu_tiempo_reaccion,"
                    + "ccu_fecha_hora_reparacion,"
                    + "ccu_soporte_tecnico,"
                    + "ccu_solucionado,"
                    + "ccu_tiempo_solucion,"
                    + "ccu_operador_noc,"
                    + "ccu_ticket,"
                    + "ccu_accedio_sitio,"
                    + "ccu_observaciones,"
                    + "ccu_latitud,"
                    + "ccu_longitud,"
                    + "ccu_imei,"
                    + "ccu_remedy) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, Integer.parseInt(id_empresa));
                statement.setInt(2, Integer.parseInt(id_usuario));
                statement.setInt(3, Integer.parseInt(prv_codigo));
                statement.setInt(4, Integer.parseInt(ees_codigo));
                statement.setInt(5, Integer.parseInt(tma_codigo));
                statement.setInt(6, Integer.parseInt(eaf_codigo));
                statement.setString(7, ccu_fecha_elaboracion);
                statement.setString(8, ccu_fecha_sincronizacion);
                statement.setString(9, ccu_fecha_inicio_evento);
                statement.setString(10, ccu_fecha_hora_aviso);
                statement.setString(11, ccu_fecha_hora_llegada);
                statement.setString(12, ccu_tiempo_reaccion);
                statement.setString(13, ccu_fecha_hora_reparacion);
                statement.setString(14, ccu_soporte_tecnico);
                statement.setString(15, ccu_solucionado);
                statement.setString(16, ccu_tiempo_solucion);
                statement.setString(17, ccu_operador_noc);
                statement.setString(18, ccu_ticket);
                statement.setString(19, ccu_accedio_sitio);
                statement.setString(20, ccu_observaciones);
                statement.setString(21, lstr_latitud);
                statement.setString(22, lstr_longitud);
                statement.setString(23, ccu_imei);
                statement.setString(24, ccu_remedy);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al insertar un Formulario";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        ccu_codigo = generatedKeys.getInt(1);
                        lstr_resultado = String.valueOf(ccu_codigo);
                    } else {
                        lstr_resultado = "Error al insertar la cabecera, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar un Formulario cabecera: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar un Formulario cabecera: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar un Formulario cabecera: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar un Formulario cabecera: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
            String lstr_update = "UPDATE mov_det_cuatro_uno SET "
                    + "ccu_codigo = " + ccu_codigo + ", "
                    + "dcu_usuario = ' ' "
                    + "WHERE dcu_usuario LIKE '" + codigo_foto_temporal + "' ";

            System.out.println("***MP_ENTREGAS: SQL UPDATE GUIA: " + lstr_update);

            String lstr_res_update = conexion.ejecutarSqlExterno(lstr_update);

            ljo_respuesta.addProperty("responseCode", 0);
            ljo_respuesta.addProperty("responseMessage", lstr_resultado);

            if (ccu_codigo > 0) {

                ljo_respuesta.addProperty("responseCode", 0);
                ljo_respuesta.addProperty("responseMessage", ccu_codigo);
            } else {
                ljo_respuesta.addProperty("responseCode", 400);
                ljo_respuesta.addProperty("responseMessage", lstr_resultado);
            }

        } else {
            ljo_respuesta.addProperty("responseCode", 100);
            ljo_respuesta.addProperty("responseMessage", "Debe ingresar la empresa");
        }

        conexion.desconectar();

        return ljo_respuesta.toString();
    }


    /*
     * Método del web service que permite la inserción de los datos en la tabla
     * mov_equipos_utilizados_cxu
     */
    public String insertarEquiposUtilizados4X1(
            String ccu_codigo,
            String eqm_codigo) {

        JsonObject ljo_respuesta = new JsonObject();
        cla_conexion conexion = new cla_conexion();
        int gui_codigo = -1;
        String lstr_resultado = "";
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;


        if (!eqm_codigo.isEmpty()) {
            Gson gson = new Gson();
            //inserta los datos en la tabla
            String SQL_INSERT = "insert into mov_equipos_utilizados_cxu("
                    + "ccu_codigo,"
                    + "eqm_codigo)"
                    + "VALUES(?,?) ";

            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                //statement.setInt(1, );
                statement.setInt(1, Integer.parseInt(ccu_codigo));
                statement.setInt(2, Integer.parseInt(eqm_codigo));



                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al insertar el wizard";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {

                        lstr_resultado = String.valueOf(ccu_codigo);
                    } else {
                        lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar el wizard: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar el wizard: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
            ljo_respuesta.addProperty("responseCode", 0);
            ljo_respuesta.addProperty("responseMessage", "OK");



        } else {
            ljo_respuesta.addProperty("responseCode", 100);
            ljo_respuesta.addProperty("responseMessage", "Debe ingresar la empresa");
        }


        conexion.desconectar();

        return ljo_respuesta.toString();
    }

    public String insertarRepuestos4x1(
            String ccu_codigo,
            String rep_equipo_reemplazado,
            String rep_marca,
            String rep_modelo,
            String rep_serie_reemplazado,
            String rep_serie_nuevo,
            String rep_observaciones) {

        JsonObject ljo_respuesta = new JsonObject();
        cla_conexion conexion = new cla_conexion();
        int gui_codigo = -1;
        String lstr_resultado = "";
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        if (!ccu_codigo.isEmpty()) {
            Gson gson = new Gson();

            //inserta los datos en la tabla
            String SQL_INSERT = "insert into mov_repuestos_cxu("
                    + "ccu_codigo,"
                    + "rep_equipo_reemplazado, "
                    + "rep_marca,"
                    + "rep_modelo,"
                    + "rep_serie_reemplazado,"
                    + "rep_serie_nuevo,"
                    + "rep_observaciones)"
                    + "VALUES(?,?,?,?,?,?,?) ";



            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                //statement.setInt(1, );
                statement.setInt(1, Integer.parseInt(ccu_codigo));
                statement.setString(2, rep_equipo_reemplazado);
                statement.setString(3, rep_marca);
                statement.setString(4, rep_modelo);
                statement.setString(5, rep_serie_reemplazado);
                statement.setString(6, rep_serie_nuevo);
                statement.setString(7, rep_observaciones);



                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al insertar el wizard";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {

                        lstr_resultado = String.valueOf(ccu_codigo);
                    } else {
                        lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar el wizard: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar el wizard: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
            ljo_respuesta.addProperty("responseCode", 0);
            ljo_respuesta.addProperty("responseMessage", "OK");

        } else {
            ljo_respuesta.addProperty("responseCode", 100);
            ljo_respuesta.addProperty("responseMessage", "Debe ingresar la empresa");
        }

        conexion.desconectar();

        return ljo_respuesta.toString();
    }

    public String insertarInsumosUtilizados4x1(
            String ccu_codigo,
            String ins_codigo,
            String iut_cantidad) {

        JsonObject ljo_respuesta = new JsonObject();
        cla_conexion conexion = new cla_conexion();
        int gui_codigo = -1;
        String lstr_resultado = "";
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        if (!ccu_codigo.isEmpty()) {
            Gson gson = new Gson();

            //inserta los datos en la tabla
            String SQL_INSERT = "insert into mov_insumos_utilizados_cxu("
                    + "ccu_codigo,"
                    + "ins_codigo, "
                    + "iut_cantidad)"
                    + "VALUES( ?,?,?)";
//                + " " + ccu_codigo + ", "
//                + " " + ins_codigo + ", "
//                + " '" + iut_cantidad + "')";

            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                //statement.setInt(1, );
                statement.setInt(1, Integer.parseInt(ccu_codigo));
                statement.setInt(2, Integer.parseInt(ins_codigo));
                statement.setInt(3, Integer.parseInt(iut_cantidad));




                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al insertar el wizard";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {

                        lstr_resultado = String.valueOf(ccu_codigo);
                    } else {
                        lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar el wizard: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar el wizard: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
            ljo_respuesta.addProperty("responseCode", 0);
            ljo_respuesta.addProperty("responseMessage", "OK");

        } else {
            ljo_respuesta.addProperty("responseCode", 100);
            ljo_respuesta.addProperty("responseMessage", "Debe ingresar la empresa");
        }


        conexion.desconectar();

        return ljo_respuesta.toString();
    }

    public String insertarInsumosDescartados4x1(
            String ccu_codigo,
            String ins_codigo,
            String ide_cantidad) {

        JsonObject ljo_respuesta = new JsonObject();
        cla_conexion conexion = new cla_conexion();
        int gui_codigo = -1;
        String lstr_resultado = "";
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        if (!ccu_codigo.isEmpty()) {
            Gson gson = new Gson();
            //inserta los datos en la tabla
            String SQL_INSERT = "insert into mov_insumos_descartados_cxu("
                    + "ccu_codigo,"
                    + "ins_codigo, "
                    + "ide_cantidad)"
                    + "VALUES(?,?,?) ";
//                    + " " + ccu_codigo + ", "
//                    + " " + ins_codigo + ", "
//                    + " '" + ide_cantidad + "')";



            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                //statement.setInt(1, );
                statement.setInt(1, Integer.parseInt(ccu_codigo));
                statement.setInt(2, Integer.parseInt(ins_codigo));
                statement.setInt(3, Integer.parseInt(ide_cantidad));




                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al insertar el wizard";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {

                        lstr_resultado = String.valueOf(ccu_codigo);
                    } else {
                        lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar el wizard: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar el wizard: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
            ljo_respuesta.addProperty("responseCode", 0);
            ljo_respuesta.addProperty("responseMessage", "OK");

        } else {
            ljo_respuesta.addProperty("responseCode", 100);
            ljo_respuesta.addProperty("responseMessage", "Debe ingresar el ccu_codigo");
        }

        conexion.desconectar();

        return ljo_respuesta.toString();
    }

    public String insertarFotografia4x1(
            String ccu_codigo,
            String fot_descripcion,
            String fot_foto) {

        JsonObject ljo_respuesta = new JsonObject();
        cla_conexion conexion = new cla_conexion();
        int gui_codigo = -1;
        String lstr_resultado = "";
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        if (!ccu_codigo.isEmpty()) {
            Gson gson = new Gson();


            //inserta los datos en la tabla
            String SQL_INSERT = "insert into mov_fotografias_cxu("
                    + "ccu_codigo,"
                    + "fot_descripcion, "
                    + "fot_foto)"
                    + "VALUES(?,?,?)";
//                + " " + ccu_codigo + ", "
//                + " '" + fot_descripcion + "', "
//                + " '" + fot_foto + "')";



            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                //statement.setInt(1, );
                statement.setInt(1, Integer.parseInt(ccu_codigo));
                statement.setString(2, fot_descripcion);
                statement.setString(3, fot_foto);




                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al insertar el wizard";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {

                        lstr_resultado = String.valueOf(ccu_codigo);
                    } else {
                        lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar el wizard: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar el wizard: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
            ljo_respuesta.addProperty("responseCode", 0);
            ljo_respuesta.addProperty("responseMessage", "OK");

        } else {
            ljo_respuesta.addProperty("responseCode", 100);
            ljo_respuesta.addProperty("responseMessage", "Debe ingresar el ccu_codigo");
        }
        conexion.desconectar();

        return ljo_respuesta.toString();
    }

    public String insertarFinalizar4x1(
            String ccu_codigo,
            String fin_observacion) {

        JsonObject ljo_respuesta = new JsonObject();
        cla_conexion conexion = new cla_conexion();
        int gui_codigo = -1;
        String lstr_resultado = "";
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        if (!ccu_codigo.isEmpty()) {
            Gson gson = new Gson();


            //inserta los datos en la tabla
            String SQL_INSERT = "insert into mov_finalizar_cxu("
                    + "ccu_codigo,"
                    + "fin_observacion)"
                    + "VALUES(?,?)";
//                + " " + ccu_codigo + ", "
//                + " '" + fot_descripcion + "', "
//                + " '" + fot_foto + "')";



            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                //statement.setInt(1, );
                statement.setInt(1, Integer.parseInt(ccu_codigo));
                statement.setString(2, fin_observacion);

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al insertar el finalizar";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {

                        lstr_resultado = String.valueOf(ccu_codigo);
                    } else {
                        lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar el wizard: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar el wizard: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el wizard: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
            ljo_respuesta.addProperty("responseCode", 0);
            ljo_respuesta.addProperty("responseMessage", "OK");

        } else {
            ljo_respuesta.addProperty("responseCode", 100);
            ljo_respuesta.addProperty("responseMessage", "Debe ingresar el ccu_codigo");
        }
        conexion.desconectar();

        return ljo_respuesta.toString();
    }
}
