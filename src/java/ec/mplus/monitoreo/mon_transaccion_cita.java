 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.monitoreo;

import ec.mplus.sistema.sis_pantalla;
import ec.xprime.componentes.*;
import ec.xprime.persistencia.cla_conexion;
import ec.xprime.persistencia.pf_tabla_generica;
import ec.xprime.sistema.sis_soporte;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.component.colorpicker.ColorPicker;
import org.primefaces.component.gmap.GMap;
import org.primefaces.component.gmap.GMapInfoWindow;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.panel.Panel;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.*;

/**
 *
 * @author xEdushx
 */
public class mon_transaccion_cita extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(mon_transaccion_cita.class.getName());
    private pf_calendario cal_fecha_inicial = new pf_calendario();
    private pf_calendario cal_fecha_final = new pf_calendario();
    private GMap mapa = new GMap();
    private MapModel advancedModel;
    private Polygon polygon = new Polygon();
    private Marker marker;
    private GMapInfoWindow info_mapa = new GMapInfoWindow();
    private OutputPanel panel_info = new OutputPanel();
    private String istr_texto = "";
    private String istr_id_adm_formulario = "";
    private String istr_id_usuario = "";
    private pf_combo com_cliente = new pf_combo();
    private pf_combo com_usuario = new pf_combo();
    private pf_layout div_division = new pf_layout();
    private Panel pan_mapa = new Panel();
    private String lstr_perfil_movil = "3";
    private String istr_nombre_geocerca = "";
    private String istr_descripcion_geocerca = "";
    private String istr_color_geocerca = "";
    private pf_entrada_texto tex_nombre = new pf_entrada_texto();
    private pf_entrada_texto tex_descripcion = new pf_entrada_texto();
    private ColorPicker color = new ColorPicker();
    private pf_dialogo dia_geocerca = new pf_dialogo();
    private boolean es_nueva = false;

    public mon_transaccion_cita() {
        LOGGER.log(Level.INFO, "map_transacciones cargando");

//        bar_botones.getBot_insertar().setValue("Crear Geocerca");
//        bar_botones.getBot_insertar().setTitle("Crear Geocerca");
//
//        bar_botones.getBot_guardar().setValue("Guardar Geocerca");
//        bar_botones.getBot_guardar().setTitle("Guardar Geocerca");

        bar_botones.quitarBotonsNavegacion();

        //configuracion filtros
        //bar_botones.limpiar();

//        bar_botones.agregarComponente(new pf_etiqueta("Cliente :"));
//        com_cliente.setCombo("select cli_codigo,cli_nombre from mp_vc_clientes where cli_estado = true and id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
//        bar_botones.agregarComponente(com_cliente);

        bar_botones.agregarComponente(new pf_etiqueta("Usuario :"));
        com_usuario.setCombo("select id_usuario,nombre_completo from tbl_usuario where estado_sesion = true and id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        bar_botones.agregarComponente(com_usuario);

        bar_botones.agregarComponente(new pf_etiqueta("Fecha Inicial :"));
        cal_fecha_inicial.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_inicial);

        bar_botones.agregarComponente(new pf_etiqueta("Fecha Final :"));
        cal_fecha_final.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_final);

        pf_boton bot_filtrar = new pf_boton();
        bot_filtrar.setValue("Actualizar");
        bot_filtrar.setMetodo("filtrar");
//        bot_filtrar.setUpdate("div_division");
        bot_filtrar.setIcon("ui-icon-refresh");
        bar_botones.agregarBoton(bot_filtrar);

        //configuracion del mapa
        configurar_mapa(" us.id_usuario = 0 and ");

        mapa.setCenter("-0.1814445,-78.4676171");
        mapa.setZoom(8);
        mapa.setType("hybrid");
        mapa.setStreetView(true);
        mapa.setModel(advancedModel);
        mapa.setStyle("width:auto;height:" + (Integer.parseInt(sis_soporte.obtener_instancia_soporte().obtener_variable("ALTO_PANTALLA")) - 50) + "px");

        pf_ajax ajax = new pf_ajax();
        ajax.configurar_metodo("onMarkerSelect");
        mapa.addClientBehavior("overlaySelect", ajax);

        pf_ajax ajax_punto = new pf_ajax();
        ajax_punto.configurar_metodo("onPointSelect");
        mapa.addClientBehavior("pointSelect", ajax_punto);

        panel_info.setStyle("text-align:center;display:block;margin:auto");

        info_mapa.getChildren().add(panel_info);
        mapa.getChildren().add(info_mapa);

        pan_mapa.setId("pan_mapa");
        pan_mapa.setStyle("text-align:center");
        pan_mapa.setHeader("MAPA DE POSICIONAMIENTO GPS");
        pan_mapa.getChildren().add(mapa);

        div_division.setId("div_division");
        div_division.dividir1(pan_mapa);

//        bar_botones.quitarBotonInsertar();
//        bar_botones.quitarBotonGuardar();
        bar_botones.quitarBotonEliminar();

        agregarComponente(div_division);

        dia_geocerca.setId("dia_geocerca");
        dia_geocerca.setTitle("Generar Nueva Geocerca");
        dia_geocerca.setWidth("40%");
        dia_geocerca.setHeight("30%");
        dia_geocerca.getBot_aceptar().setMetodo("aceptar_geocerca");
        dia_geocerca.setResizable(false);

        pf_panel_grupo gru_cuerpo = new pf_panel_grupo();

        pf_etiqueta eti_mensaje = new pf_etiqueta();
        eti_mensaje.setValue("Ingrese los valores solicitados, luego dar click sobre el mapa dibujando cada punto de la Geocerca.");
        eti_mensaje.setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");

        pf_grid gri_cabecera = new pf_grid();
        gri_cabecera.setWidth("100%");
//        gri_cabecera.setStyle("text-align: center;");

//        pf_grid gri_cabecera = new pf_grid();
        gri_cabecera.setColumns(2);
        gri_cabecera.getChildren().add(new pf_etiqueta("Nombre: "));
        tex_nombre.setValue(istr_nombre_geocerca);
        gri_cabecera.getChildren().add(tex_nombre);
        gri_cabecera.getChildren().add(new pf_etiqueta("Descripción: "));
        tex_descripcion.setValue(istr_descripcion_geocerca);
        gri_cabecera.getChildren().add(tex_descripcion);
        gri_cabecera.getChildren().add(new pf_etiqueta("Color: "));
        color.setValue(istr_color_geocerca);
        gri_cabecera.getChildren().add(color);

        gru_cuerpo.getChildren().add(eti_mensaje);
        gru_cuerpo.getChildren().add(gri_cabecera);

        dia_geocerca.setDialogo(gru_cuerpo);
        agregarComponente(dia_geocerca);
    }

    public void filtrar() {
        LOGGER.log(Level.INFO, "Filtrando mapa");
        Object ldat_fecha_inicial = cal_fecha_inicial.getValue();
        Object ldat_fecha_final = cal_fecha_final.getValue();
        Object lcom_cliente = com_cliente.getValue();
        Object lcom_usuario = com_usuario.getValue();
        String lstr_condicion;

        if (ldat_fecha_inicial != null && ldat_fecha_inicial != null) {
            String lstr_fecha_inicial = sis_soporte.obtener_instancia_soporte().obtener_formato_fecha(ldat_fecha_inicial);
            String lstr_fecha_final = sis_soporte.obtener_instancia_soporte().obtener_formato_fecha(ldat_fecha_final);

            if (lcom_cliente == null) {
                istr_id_adm_formulario = "";
            } else {
                istr_id_adm_formulario = lcom_cliente.toString();
            }

            if (lcom_usuario == null) {
                istr_id_usuario = "";
            } else {
                istr_id_usuario = lcom_usuario.toString();
            }

            if (!istr_id_adm_formulario.isEmpty() && !istr_id_adm_formulario.equalsIgnoreCase("null") && !istr_id_usuario.isEmpty() && !istr_id_usuario.equalsIgnoreCase("null")) {
                lstr_condicion = "gc.gci_fecha >='" + lstr_fecha_inicial + " 00:00:00' "
                        + "and gc.gci_fecha <='" + lstr_fecha_final + " 23:59:59' "
                        + "and cl.cli_codigo = " + istr_id_adm_formulario + " "
                        + "and us.id_usuario = " + istr_id_usuario + " and ";
            } else if (!istr_id_adm_formulario.isEmpty() && !istr_id_adm_formulario.equalsIgnoreCase("null") && (istr_id_usuario.isEmpty() || istr_id_usuario.equalsIgnoreCase("null"))) {
                lstr_condicion = "gc.gci_fecha >='" + lstr_fecha_inicial + " 00:00:00' "
                        + "and gc.gci_fecha <='" + lstr_fecha_final + " 23:59:59' "
                        + "and cl.cli_codigo = " + istr_id_adm_formulario + " and ";
            } else if ((istr_id_adm_formulario.isEmpty() || istr_id_adm_formulario.equalsIgnoreCase("null")) && !istr_id_usuario.isEmpty() && !istr_id_usuario.equalsIgnoreCase("null")) {
                lstr_condicion = "gc.gci_fecha >='" + lstr_fecha_inicial + " 00:00:00' "
                        + "and gc.gci_fecha <='" + lstr_fecha_final + " 23:59:59' "
                        + "and us.id_usuario = " + istr_id_usuario + " and ";
            } else {
                lstr_condicion = "gc.gci_fecha >='" + lstr_fecha_inicial + " 00:00:00' "
                        + "and gc.gci_fecha <='" + lstr_fecha_final + " 23:59:59' and ";
            }

            configurar_mapa(lstr_condicion);
            desplegar_geocercas();
            sis_soporte.obtener_instancia_soporte().addUpdate("pan_mapa");

        } else {
            sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("Atención!", "Debe ingresar Fechas");
        }

    }

    public void configurar_mapa(String astr_condicion) {
        advancedModel = new DefaultMapModel();

        pf_tabla_generica tbl_puntos_gps = new pf_tabla_generica();
        tbl_puntos_gps.setSql("SELECT "
                + "gc.gci_codigo as ID, "
                + "em.nombre as EMPRESA, "
                + "cl.cli_nombre as CLIENTE, "
                + "ind.ind_nombre as INDUSTRIA, "
                + "pro.pro_nombre as PRODUCTO, "
                + "us.nombre_completo as USUARIO, "
                + "gc.gci_fecha as FECHA, "
                + "gc.gci_latitud as LATITUD, "
                + "gc.gci_longitud as LONGITUD, "
                + "cit.cit_observacion as OBSERVACION "
                + "FROM "
                + "mp_vc_cita cit, "
                + "mp_vc_gestion_cita gc,"
                + "tbl_empresa em, "
                + "mp_vc_clientes cl, "
                + "tbl_usuario us, "
                + "mp_vc_industria ind, "
                + "mp_vc_producto pro "
                + "where em.id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa() + " and "
                + astr_condicion
                + "cit.cit_codigo = gc.cit_codigo and "
                + "cit.ind_codigo = ind.ind_codigo and "
                + "cit.pro_codigo = pro.pro_codigo and "
                + "em.id_empresa = cit.id_empresa and "
                + "cl.cli_codigo = cit.cli_codigo and "
                + "us.id_usuario = cit.id_usuario and "
                + "us.estado_sesion = true and "
                + "cl.cli_estado = true "
                + "order by empresa, cliente, usuario, fecha");
        tbl_puntos_gps.ejecutarSql();

        for (int aint = 0; aint < tbl_puntos_gps.getTotalFilas(); aint++) {
            LatLng coordenada = new LatLng(Double.parseDouble(tbl_puntos_gps.getValor(aint, "LATITUD").replace(",", ".")),
                    Double.parseDouble(tbl_puntos_gps.getValor(aint, "LONGITUD").replace(",", ".")));

            String lstr_titulo = "Transacción";

            Panel pan_puntos = new Panel();
            pan_puntos.setHeader(tbl_puntos_gps.getValor(aint, "USUARIO"));

            pf_panel_tabla pan_datos = new pf_panel_tabla();
            pan_datos.setColumns(2);

            pan_datos.getChildren().add(new pf_etiqueta("Fecha:"));
            pan_datos.getChildren().add(new pf_etiqueta(tbl_puntos_gps.getValor(aint, "FECHA")));

            pan_datos.getChildren().add(new pf_etiqueta("Empresa:"));
            pan_datos.getChildren().add(new pf_etiqueta(tbl_puntos_gps.getValor(aint, "EMPRESA")));

            pan_datos.getChildren().add(new pf_etiqueta("Cliente:"));
            pan_datos.getChildren().add(new pf_etiqueta(tbl_puntos_gps.getValor(aint, "CLIENTE")));

            if (sis_soporte.obtener_instancia_soporte().obtener_empresa().equals("1")) {
                pan_datos.getChildren().add(new pf_etiqueta("Industria:"));
            } else {
                pan_datos.getChildren().add(new pf_etiqueta("Segmento:"));
            }
            pan_datos.getChildren().add(new pf_etiqueta(tbl_puntos_gps.getValor(aint, "INDUSTRIA")));

            pan_datos.getChildren().add(new pf_etiqueta("Producto:"));
            pan_datos.getChildren().add(new pf_etiqueta(tbl_puntos_gps.getValor(aint, "PRODUCTO")));

            pan_datos.getChildren().add(new pf_etiqueta("Observación:"));
            pan_datos.getChildren().add(new pf_etiqueta(tbl_puntos_gps.getValor(aint, "OBSERVACION")));

            pan_puntos.getChildren().add(pan_datos);

            advancedModel.addOverlay(new Marker(coordenada, lstr_titulo, pan_puntos, "http://maps.google.com/mapfiles/ms/micons/yellow-dot.png"));
        }


        //Shared coordinates
        LatLng coord1 = new LatLng(-0.1814445, -78.4676171);
        //Icons and Data
        Panel pan_puntos = new Panel();
        pan_puntos.setHeader("ECUADOR");
        advancedModel.addOverlay(new Marker(coord1, "ECUADOR", pan_puntos, "http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));

        mapa.setModel(advancedModel);
    }

    public void abrir_crear_geocerca() {
        if (!es_nueva) {
            polygon = new Polygon();
            polygon.setStrokeColor("#FF9900");
            polygon.setFillColor("#FF9900");
            polygon.setStrokeOpacity(0.7);
            polygon.setFillOpacity(0.1);
            istr_nombre_geocerca = "";
            istr_descripcion_geocerca = "";
            istr_color_geocerca = "";
            dia_geocerca.dibujar();
        } else {
            sis_soporte.obtener_instancia_soporte().agregarMensaje(
                    "Guarde la geocerca actual con la que está trabajando",
                    "");
        }
    }

    public void desplegar_geocercas() {
        pf_tabla_generica tab_cabecera_geocerca = new pf_tabla_generica();
        String lstr_sql = "select * from mp_cabecera_geocerca where id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa();
        tab_cabecera_geocerca.setSql(lstr_sql);
        tab_cabecera_geocerca.ejecutarSql();

        for (int i = 0; i < tab_cabecera_geocerca.getTotalFilas(); i++) {
            pf_tabla_generica tab_detalle_geocerca = new pf_tabla_generica();
            lstr_sql = "select dge_latitud, dge_longitud from mp_detalle_geocerca where cge_codigo = " + tab_cabecera_geocerca.getValor(i, "cge_codigo");
            tab_detalle_geocerca.setSql(lstr_sql);
            tab_detalle_geocerca.ejecutarSql();

            polygon = new Polygon();
            Panel pan_geocerca = new Panel();
            pan_geocerca.setHeader(tab_cabecera_geocerca.getValor(i, "cge_nombre"));

//            pf_panel_tabla pan_datos = new pf_panel_tabla();
//
////            pan_datos.getChildren().add(new pf_etiqueta("Descripción:"));
//            pan_datos.getChildren().add(new pf_etiqueta(tab_cabecera_geocerca.getValor(i, "cge_descripcion")));

            pan_geocerca.getChildren().add(new pf_etiqueta(tab_cabecera_geocerca.getValor(i, "cge_descripcion")));

            polygon.setData(pan_geocerca);
            polygon.setStrokeColor(tab_cabecera_geocerca.getValor(i, "cge_color"));
            polygon.setFillColor(tab_cabecera_geocerca.getValor(i, "cge_color"));
            polygon.setStrokeOpacity(0.7);
            polygon.setFillOpacity(0.1);

            for (int j = 0; j < tab_detalle_geocerca.getTotalFilas(); j++) {
                //Shared coordinates
                LatLng coord1 = new LatLng(Double.parseDouble(tab_detalle_geocerca.getValor(j, "dge_latitud")),
                        Double.parseDouble(tab_detalle_geocerca.getValor(j, "dge_longitud")));

                //Polygon
                polygon.getPaths().add(coord1);
            }

            advancedModel.addOverlay(polygon);
        }

//        sis_soporte.obtener_instancia_soporte().addUpdate("pan_mapa");
    }

    public void aceptar_geocerca() {
        es_nueva = true;

        istr_nombre_geocerca = tex_nombre.getValue().toString();
        istr_descripcion_geocerca = tex_descripcion.getValue().toString();
        istr_color_geocerca = "#" + color.getValue().toString().toUpperCase();

        System.out.println("COLOR: " + istr_color_geocerca);

        polygon.setStrokeColor(istr_color_geocerca);
        polygon.setFillColor(istr_color_geocerca);
        dia_geocerca.cerrar();
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        boolean esMarcador = true;
        try {
            marker = (Marker) event.getOverlay();
            if (marker == null) {
                istr_texto = "";
            } else {
                istr_texto = marker.getTitle();
            }

        } catch (Exception e) {
            System.out.println("NO ES MARCADOR... " + e.getMessage());
            esMarcador = false;
            try {
                polygon = (Polygon) event.getOverlay();
                panel_info.getChildren().clear();
                Panel panel_mapa = (Panel) polygon.getData();
                sis_soporte.obtener_instancia_soporte().agregarMensaje(panel_mapa.getHeader(), ((pf_etiqueta) panel_mapa.getChildren().get(0)).getValue().toString());
//                panel_info.getChildren().add(panel_mapa);
            } catch (Exception ex) {
                System.out.println("NO ES POLIGONO... " + ex.getMessage());
            }
        }

        if (esMarcador) {
            panel_info.getChildren().clear();
            Panel panel_mapa = (Panel) marker.getData();
            panel_info.getChildren().add(panel_mapa);
        }

    }

//    public void onPolygonSelect(OverlaySelectEvent event) {
//        polygon = (Polygon) event.getOverlay();
//        panel_info.getChildren().clear();
//        Panel panel_mapa = (Panel) polygon.getData();
//        panel_info.getChildren().add(panel_mapa);
//    }
    public void onPointSelect(PointSelectEvent event) {
        if (es_nueva) {
            LatLng latlng = event.getLatLng();

            //Shared coordinates
            LatLng coord1 = new LatLng(latlng.getLat(), latlng.getLng());

            //agrega las geocercas guardadas previamente
            //cargar_geocercas();

            //Polygon
            polygon.getPaths().add(coord1);

            advancedModel.addOverlay(polygon);

            System.out.println("CENTRO: " + mapa.getCenter());
            System.out.println("ZOOM: " + mapa.getZoom());

            mapa.setCenter(latlng.getLat() + "," + latlng.getLng());
            mapa.setType("hybrid");
            mapa.setStreetView(true);
            mapa.setZoom(18);

            System.out.println("N CENTRO: " + mapa.getCenter());
            System.out.println("N ZOOM: " + mapa.getZoom());

            sis_soporte.obtener_instancia_soporte().agregarMensaje("Punto seleccionado", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng());
            sis_soporte.obtener_instancia_soporte().addUpdate("pan_mapa");
        }

    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public void setAdvancedModel(MapModel advancedModel) {
        this.advancedModel = advancedModel;
    }

    public GMap getMapa() {
        return mapa;
    }

    public void setMapa(GMap mapa) {
        this.mapa = mapa;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public GMapInfoWindow getInfo_mapa() {
        return info_mapa;
    }

    public void setInfo_mapa(GMapInfoWindow info_mapa) {
        this.info_mapa = info_mapa;
    }

    public String getIstr_texto() {
        return istr_texto;
    }

    public void setIstr_texto(String istr_texto) {
        this.istr_texto = istr_texto;
    }

    public OutputPanel getPanel_info() {
        return panel_info;
    }

    public void setPanel_info(OutputPanel panel_info) {
        this.panel_info = panel_info;
    }

    public pf_combo getCom_cliente() {
        return com_cliente;
    }

    public void setCom_cliente(pf_combo com_cliente) {
        this.com_cliente = com_cliente;
    }

    public pf_combo getCom_usuario() {
        return com_usuario;
    }

    public void setCom_usuario(pf_combo com_usuario) {
        this.com_usuario = com_usuario;
    }

    public String getIstr_id_adm_formulario() {
        return istr_id_adm_formulario;
    }

    public void setIstr_id_adm_formulario(String istr_id_adm_formulario) {
        this.istr_id_adm_formulario = istr_id_adm_formulario;
    }

    public String getIstr_id_usuario() {
        return istr_id_usuario;
    }

    public void setIstr_id_usuario(String istr_id_usuario) {
        this.istr_id_usuario = istr_id_usuario;
    }

    public pf_calendario getCal_fecha_final() {
        return cal_fecha_final;
    }

    public void setCal_fecha_final(pf_calendario cal_fecha_final) {
        this.cal_fecha_final = cal_fecha_final;
    }

    public pf_calendario getCal_fecha_inicial() {
        return cal_fecha_inicial;
    }

    public void setCal_fecha_inicial(pf_calendario cal_fecha_inicial) {
        this.cal_fecha_inicial = cal_fecha_inicial;
    }

    public pf_layout getDiv_division() {
        return div_division;
    }

    public void setDiv_division(pf_layout div_division) {
        this.div_division = div_division;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public String getIstr_nombre_geocerca() {
        return istr_nombre_geocerca;
    }

    public void setIstr_nombre_geocerca(String istr_nombre_geocerca) {
        this.istr_nombre_geocerca = istr_nombre_geocerca;
    }

    public pf_dialogo getDia_geocerca() {
        return dia_geocerca;
    }

    public void setDia_geocerca(pf_dialogo dia_geocerca) {
        this.dia_geocerca = dia_geocerca;
    }

    public String getIstr_color_geocerca() {
        return istr_color_geocerca;
    }

    public void setIstr_color_geocerca(String istr_color_geocerca) {
        this.istr_color_geocerca = istr_color_geocerca;
    }

    public String getIstr_descripcion_geocerca() {
        return istr_descripcion_geocerca;
    }

    public void setIstr_descripcion_geocerca(String istr_descripcion_geocerca) {
        this.istr_descripcion_geocerca = istr_descripcion_geocerca;
    }

    @Override
    public void insertar() {
        abrir_crear_geocerca();
    }

    @Override
    public void guardar() {
        es_nueva = false;

        String lstr_respuesta = "";

        cla_conexion conexion = sis_soporte.obtener_instancia_soporte().obtener_conexion();

        //inserta la cabecera y obtiene el id de la transaccion
        int id_cabecera_geocerca = conexion.insertar_cabecera_geocerca(istr_nombre_geocerca, istr_descripcion_geocerca, istr_color_geocerca);

        if (id_cabecera_geocerca == -1) {
            lstr_respuesta = "No se pudo registrar la cabecera de la geocerca";
        } else {
            //inserta el detalle de la geocerca
            List<LatLng> lista_geocerca = polygon.getPaths();
            boolean inserto_detalle = conexion.insertar_detalle_geocerca(id_cabecera_geocerca, lista_geocerca);

            if (inserto_detalle) {
                lstr_respuesta = "La Geocerca " + istr_nombre_geocerca + " se ha creado correctamente.";
            } else {
                lstr_respuesta = "La Geocerca " + istr_nombre_geocerca + " no se pudo crear. Detalle: No se registraron detalles.";
            }
        }

        sis_soporte.obtener_instancia_soporte().agregarMensaje("Atención!", lstr_respuesta);

        filtrar();

    }

    @Override
    public void eliminar() {
    }
}
