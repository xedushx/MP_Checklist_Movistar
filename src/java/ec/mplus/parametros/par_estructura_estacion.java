/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.parametros;

import ec.mplus.sistema.sis_pantalla;
import ec.xprime.componentes.*;
import ec.xprime.sistema.sis_soporte;
import java.util.logging.Logger;
import org.primefaces.component.gmap.GMap;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author user
 */
public class par_estructura_estacion extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(par_estructura_estacion.class.getName());
    private pf_tabla tab_tabla = new pf_tabla();
    private pf_tabla tab_detalle_estructura = new pf_tabla();
    private pf_arbol arb_arbol = new pf_arbol();
    private pf_dialogo dia_mapa = new pf_dialogo();
    private GMap mapa = new GMap();
    private MapModel advancedModel;
    private String istr_latitud = "";
    private String istr_longitud = "";
    private String istr_nombre_cliente = "";

    public par_estructura_estacion() {

        pf_boton bot_posicion = new pf_boton();
        bot_posicion.setValue("Posicionamiento");
        bot_posicion.setMetodo("desplegar_mapa");
        bot_posicion.setIcon("ui-icon-search");
        bar_botones.agregarBoton(bot_posicion);

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("mov_estructura_estacion", "ees_codigo", 1);
        tab_tabla.agregarRelacion(tab_detalle_estructura);
        tab_tabla.setCampoPadre("ees_codigo_2");
        tab_tabla.setCampoNombre("ees_nombre");
        tab_tabla.getColumna("ees_codigo").setNombreVisual("CODIGO");
        tab_tabla.getColumna("ees_nombre").setNombreVisual("DESCRIPCIÓN");
        tab_tabla.getColumna("ees_estado").setNombreVisual("ESTADO");
        tab_tabla.getColumna("ees_es_pais").setNombreVisual("ES PAÍS");
        tab_tabla.getColumna("ees_es_provincia").setNombreVisual("ES PROVINCIA");
        tab_tabla.getColumna("ees_es_ciudad").setNombreVisual("ES CIUDAD ");
        tab_tabla.getColumna("ees_es_estacion").setNombreVisual("ES ESTACIÓN");
        tab_tabla.getColumna("ees_latitud").setNombreVisual("LATITUD");
        tab_tabla.getColumna("ees_longitud").setNombreVisual("LONGITUD");
        tab_tabla.agregarArbol(arb_arbol);
        tab_tabla.dibujar();
        pf_panel_tabla pat_panel1 = new pf_panel_tabla();
        pat_panel1.setPanelTabla(tab_tabla);

        arb_arbol.setId("arb_arbol");
        arb_arbol.dibujar();

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_detalle_estructura.setId("tab_detalle_estructura");
        tab_detalle_estructura.setIdCompleto("tab_tabulador:tab_detalle_estructura");
        tab_detalle_estructura.setTabla("mov_detalle_estructura", "des_codigo", 2);
        tab_detalle_estructura.getColumna("des_codigo").setNombreVisual("CÓDIGO");
        tab_detalle_estructura.getColumna("des_tecnologia").setNombreVisual("TECNOLOGÍA");
        tab_detalle_estructura.getColumna("des_marca").setNombreVisual("MARCA");
        tab_detalle_estructura.getColumna("prv_codigo").setNombreVisual("PROVEEDOR");
        tab_detalle_estructura.getColumna("prv_codigo").setCombo("mov_proveedores", "prv_codigo", "prv_nombre", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        tab_detalle_estructura.dibujar();

        pf_panel_tabla pat_detalle_estructura = new pf_panel_tabla();
        pat_detalle_estructura.setHeader(new pf_etiqueta("DETALLE ESTRUCTURA"));
        pat_detalle_estructura.setPanelTabla(tab_detalle_estructura);

        tab_tabulador.agregarTab("DETALLE ESTRUCTURA", pat_detalle_estructura);

        pf_layout div3 = new pf_layout(); //UNE OPCION Y DIV 2
        div3.dividir2(pat_panel1, tab_tabulador, "60%", "H");
        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, div3, "21%", "V");  //arbol y div3
        agregarComponente(div_division);

        dia_mapa.setId("dia_mapa");
        dia_mapa.setTitle("Obtener Posicionamiento");
        dia_mapa.setWidth("60%");
        dia_mapa.setHeight("60%");
        dia_mapa.getBot_aceptar().setMetodo("aceptar_posicion");
        dia_mapa.setResizable(false);

        mapa.setModel(advancedModel);
        mapa.setCenter("-0.1814445,-78.4676171");
        mapa.setZoom(10);
        mapa.setType("hybrid");
        mapa.setStreetView(true);
        mapa.setStyle("width:" + (Integer.valueOf(dia_mapa.getWidth()) - 50) + "px;height:" + (Integer.valueOf(dia_mapa.getHeight()) - 100) + "px;");

        pf_ajax ajax_punto = new pf_ajax();
        ajax_punto.configurar_metodo("onPointSelect");
        mapa.addClientBehavior("pointSelect", ajax_punto);

        pf_panel_grupo gru_cuerpo = new pf_panel_grupo();

        pf_etiqueta eti_mensaje = new pf_etiqueta();
        eti_mensaje.setValue("De un click sobre el punto en el mapa donde se encuentre la radio base ");
        eti_mensaje.setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");

        pf_grid gri_cabecera = new pf_grid();
        gri_cabecera.setWidth("100%");
        gri_cabecera.setColumns(1);
        gri_cabecera.getChildren().add(mapa);

        gru_cuerpo.getChildren().add(eti_mensaje);
        gru_cuerpo.getChildren().add(gri_cabecera);

        dia_mapa.setDialogo(gru_cuerpo);

        agregarComponente(dia_mapa);

    }

    public void desplegar_mapa() {

        if (tab_tabla.getTotalFilas() > 0) {
            advancedModel = new DefaultMapModel();
            istr_latitud = tab_tabla.getValor("ees_latitud");
            istr_longitud = tab_tabla.getValor("ees_longitud");

            if (!(istr_latitud.equalsIgnoreCase("NULL") || istr_latitud.isEmpty())
                    && !(istr_longitud.equalsIgnoreCase("NULL") || istr_longitud.isEmpty())) {

                LatLng latlng = new LatLng(Double.valueOf(istr_latitud), Double.valueOf(istr_longitud));
                LatLng coord1 = new LatLng(latlng.getLat(), latlng.getLng());

                Marker marker = new Marker(coord1, istr_nombre_cliente);
                advancedModel.addOverlay(marker);
            }

            mapa.setModel(advancedModel);
            istr_nombre_cliente = tab_tabla.getValor("ees_nombre");
            dia_mapa.dibujar();
        } else {
            sis_soporte.obtener_instancia_soporte().agregarMensaje("Atención!", "Para asignar una posición debe ingresar un cliente.");
        }

    }

    public void aceptar_posicion() {
        tab_tabla.setValor("ees_latitud", istr_latitud);
        tab_tabla.setValor("ees_longitud", istr_longitud);
        sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabla");
        dia_mapa.cerrar();
    }

    public void onPointSelect(PointSelectEvent event) {

        LatLng latlng = event.getLatLng();

        //Shared coordinates
        istr_latitud = "" + latlng.getLat();
        istr_longitud = "" + latlng.getLng();

        LatLng coord1 = new LatLng(latlng.getLat(), latlng.getLng());

        Marker marker = new Marker(coord1, istr_nombre_cliente);
        advancedModel.addOverlay(marker);

//            mapa.setCenter(latlng.getLat() + "," + latlng.getLng());
//            mapa.setType("hybrid");
//            mapa.setStreetView(true);
//            mapa.setZoom(18);

        mapa.setModel(advancedModel);
//        sis_soporte.obtener_instancia_soporte().agregarMensaje("Punto seleccionado", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng());
        sis_soporte.obtener_instancia_soporte().addUpdate("dia_mapa");

    }

    @Override
    public void insertar() {
        sis_soporte.obtener_instancia_soporte().getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            tab_detalle_estructura.guardar();
        }
        guardarPantalla();

    }

    @Override
    public void eliminar() {
        sis_soporte.obtener_instancia_soporte().getTablaisFocus().eliminar();
    }

    public pf_arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(pf_arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public pf_tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(pf_tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public pf_tabla getTab_detalle_estructura() {
        return tab_detalle_estructura;
    }

    public void setTab_detalle_estructura(pf_tabla tab_detalle_estructura) {
        this.tab_detalle_estructura = tab_detalle_estructura;
    }

    public MapModel getAdvancedModel() {
        return advancedModel;
    }

    public void setAdvancedModel(MapModel advancedModel) {
        this.advancedModel = advancedModel;
    }

    public pf_dialogo getDia_mapa() {
        return dia_mapa;
    }

    public void setDia_mapa(pf_dialogo dia_mapa) {
        this.dia_mapa = dia_mapa;
    }

    public String getIstr_latitud() {
        return istr_latitud;
    }

    public void setIstr_latitud(String istr_latitud) {
        this.istr_latitud = istr_latitud;
    }

    public String getIstr_longitud() {
        return istr_longitud;
    }

    public void setIstr_longitud(String istr_longitud) {
        this.istr_longitud = istr_longitud;
    }

    public String getIstr_nombre_cliente() {
        return istr_nombre_cliente;
    }

    public void setIstr_nombre_cliente(String istr_nombre_cliente) {
        this.istr_nombre_cliente = istr_nombre_cliente;
    }

    public GMap getMapa() {
        return mapa;
    }

    public void setMapa(GMap mapa) {
        this.mapa = mapa;
    }
    
    
}
