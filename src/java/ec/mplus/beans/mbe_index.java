/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.beans;

import ec.xprime.componentes.*;
import ec.xprime.persistencia.pf_tabla_generica;
import ec.xprime.sistema.sis_soporte;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class mbe_index {

    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = Logger.getLogger(mbe_index.class.getName());
    private HtmlForm formulario = new HtmlForm();
    private pf_panel_grupo dibuja = new pf_panel_grupo();
    private Object clase;
    private HtmlInputHidden ith_alto = new HtmlInputHidden(); //Alto disponible
    private HtmlInputHidden alto = new HtmlInputHidden(); //Alto Browser
    private HtmlInputHidden ancho = new HtmlInputHidden();//Ancho Browser
    private pf_panel_grupo mensajes = new pf_panel_grupo();
    private pf_error_sql error_sql = new pf_error_sql();
    private pf_buscar_tabla bus_buscar;
    private pf_terminal_tabla term_tabla;
    private String str_paquete;
    private String str_tipo;
    private String str_manual = "ayuda_vc.pdf";
    private String istr_titulo = "Inicio";
    private pf_formato_tabla fot_formato;
    private pf_importar_tabla imt_importar;
    private pf_dialogo dia_sucu_usuario;
    private pf_radio rad_sucursales;
    private pf_seleccion_archivo sar_upload;
    private String empresa;
    
    /*
     * Constructor
     */
    public mbe_index() {
        
        if (sis_soporte.obtener_instancia_soporte().obtener_conexion() != null && sis_soporte.obtener_instancia_soporte().obtener_variable("nombre_usuario") != null) {
            dibuja.setId("dibuja");
            dibuja.setStyleClass("ui-layout-unit-content ui-widget-content");
            dibuja.setTransient(true);
            formulario.setTransient(true);
            dibuja.setDibuja(true);
            pf_menu menu = new pf_menu();
            menu.dibujar();
            menu.setId("menu");
            formulario.getChildren().add(menu);
            formulario.getChildren().add(dibuja);
            ith_alto.setId("ith_alto");
            formulario.getChildren().add(ith_alto);
            alto.setId("alto");
            formulario.getChildren().add(alto);
            ancho.setId("ancho");
            formulario.getChildren().add(ancho);
            sis_soporte.obtener_instancia_soporte().crear_variable("ALTO_PANTALLA", "1000");

            mensajes.setId("mensajes");
            mensajes.setTransient(true);
            bus_buscar = new pf_buscar_tabla();
            bus_buscar.setId("bus_buscar");
            mensajes.getChildren().add(bus_buscar);
            
            term_tabla=new pf_terminal_tabla();            
            mensajes.getChildren().add(term_tabla);
            
            fot_formato = new pf_formato_tabla();
            mensajes.getChildren().add(fot_formato);
            
            sar_upload=new pf_seleccion_archivo();            
            mensajes.getChildren().add(sar_upload);

            imt_importar = new pf_importar_tabla();
            mensajes.getChildren().add(imt_importar);
            error_sql.setId("error_sql");
            mensajes.getChildren().add(error_sql);
            formulario.getChildren().add(mensajes);
            pf_notificacion not_notificacion = new pf_notificacion();
            formulario.getChildren().add(not_notificacion);
            
//            pf_confirmar con_guarda = new pf_confirmar();
//            con_guarda.setId("con_guarda");
//            con_guarda.setWidgetVar("con_guarda");
//            con_guarda.setMessage("Está seguro que desea guardar?");
//            con_guarda.getBot_aceptar().setOncomplete("con_guarda.hide();");
//            con_guarda.getBot_aceptar().setMetodo("guardar");
//            con_guarda.getBot_cancelar().setOnclick("con_guarda.hide();");
//
//            formulario.getChildren().add(con_guarda);

            pf_etiqueta eti_usuario = new pf_etiqueta();
            String lstr_usuario = sis_soporte.obtener_instancia_soporte().obtener_variable("nombre_usuario").toUpperCase();
            eti_usuario.setStyle("position:fixed;right:100px;top:10px;");
            eti_usuario.setValue(lstr_usuario);
            eti_usuario.setTitle("Usuario Login");
            formulario.getChildren().add(eti_usuario);
            
            pf_link lin_salir = new pf_link();
            lin_salir.agregarImagen("imagenes/im_salir_sistema.png", "32", "32");
            lin_salir.setMetodoRuta("mbe_login.salir");
            lin_salir.setTitle("Cerrar Sesión");
            lin_salir.setStyle("position:fixed;right:2px;top:1px;");
            formulario.getChildren().add(lin_salir);
            
            empresa = sis_soporte.obtener_instancia_soporte().obtener_variable("empresa");
            
//            empresa = sis_soporte.obtener_instancia_soporte().getPropiedad("ide_empr");
            
            cargar_menu(empresa);
            
        }
        
        LOGGER.log(Level.INFO, "mbe_index cargado");
    }
    
    private void cargar_menu(String ide_empresa) {
        dibuja.getChildren().clear();
        
        pf_tabla_generica tab_sucursal = sis_soporte.obtener_instancia_soporte().consultar("SELECT * FROM tbl_empresa WHERE id_empresa=" + ide_empresa);
        sis_soporte.obtener_instancia_soporte().crear_variable("empresa", tab_sucursal.getValor(0, "id_empresa"));
        
        pf_etiqueta eti_empresa = new pf_etiqueta();
        pf_etiqueta eti_representante = new pf_etiqueta();
        pf_etiqueta eti_direccion = new pf_etiqueta();
        pf_etiqueta eti_telefono = new pf_etiqueta();
        pf_imagen ima_empresa = new pf_imagen();
        
        eti_empresa.setValue(tab_sucursal.getValor(0, "nombre") + "<br/>");
        eti_representante.setValue(tab_sucursal.getValor(0, "representante") + "<br/>");
        eti_direccion.setValue(tab_sucursal.getValor(0, "direccion") + "<br/>");
        eti_telefono.setValue(tab_sucursal.getValor(0, "telefono") + "<br/>");
        ima_empresa.setValue(tab_sucursal.getValor(0, "logo_empr"));
        
//        pf_panel panel_empresa = new pf_panel();
//        panel_empresa.setStyle("width: 100%;font-size: 14px;text-align: center;margin-bottom:20px");
//        panel_empresa.setFooter(new pf_etiqueta("Message Plus Copyright 2014"));
        
        eti_empresa.setStyle("font-style: bold;border: none;text-shadow: 0px 2px 3px #ccc;background: none;text-transform: uppercase");
        
        pf_layout pan_empresa = new pf_layout();
        pan_empresa.setStyle("border:1px;");
        
        pf_grid gri_datos = new pf_grid();
        gri_datos.setStyle("width: 100%;font-size: 30px;text-align: center;font-weight: bold;");
        gri_datos.getChildren().add(new pf_espacio("100","100"));
        gri_datos.getChildren().add(eti_empresa);
        gri_datos.getChildren().add(ima_empresa);
        gri_datos.getChildren().add(eti_representante);
        gri_datos.getChildren().add(eti_direccion);
        gri_datos.getChildren().add(eti_telefono);
        
//        panel_empresa.getChildren().add(gri_datos);
        
        pf_grid gri_pie = new pf_grid();
        gri_pie.setStyle("width: 100%;font-size: 14px;text-align: center;");
        gri_pie.getChildren().add(new pf_etiqueta("Message Plus © Copyright 2015"));
        
        pan_empresa.dividir2(gri_datos, gri_pie, "55%", "H");
        
        pan_empresa.getDivision1().setResizable(false);
        pan_empresa.getDivision2().setResizable(false);
        
        dibuja.getChildren().add(pan_empresa);
        sis_soporte.obtener_instancia_soporte().addUpdate("dibuja");
    }
    
    public void cargar(ActionEvent evt) {
        sis_soporte.obtener_instancia_soporte().crear_variable("id_opcion", ((pf_item_menu) evt.getComponent()).getCodigo());
        istr_titulo = ((pf_item_menu) evt.getComponent()).getValue() + "";
        sis_soporte.obtener_instancia_soporte().crear_variable("ALTO_PANTALLA", ith_alto.getValue() + "");  //Alto disponible
        sis_soporte.obtener_instancia_soporte().crear_variable("ALTO", alto.getValue() + "");  //Alto browser
        sis_soporte.obtener_instancia_soporte().crear_variable("ANCHO", ancho.getValue() + ""); //Ancho browser    
        sis_soporte.obtener_instancia_soporte().crear_variable("OPCION", istr_titulo); //Opcion
        buscarOpcion();
        
        mensajes.getChildren().clear();
        mensajes.getChildren().add(error_sql);
        mensajes.getChildren().add(bus_buscar);
        mensajes.getChildren().add(term_tabla);
        mensajes.getChildren().add(fot_formato);
        mensajes.getChildren().add(imt_importar);
        mensajes.getChildren().add(sar_upload);
        dibuja.getChildren().clear();
        sis_soporte.obtener_instancia_soporte().obtener_conexion().setSqlPantalla(new ArrayList<String>());
        clase = sis_soporte.obtener_instancia_soporte().cargarPantalla(str_paquete, str_tipo);

        //Tacla Abajo y Arriba
        pf_metodo_remoto mer_abajo = new pf_metodo_remoto();
        mer_abajo.setName("teclaAbajo");
        mer_abajo.setMetodo("siguiente");
        dibuja.getChildren().add(mer_abajo);

        pf_metodo_remoto mer_arriba = new pf_metodo_remoto();
        mer_arriba.setName("teclaArriba");
        mer_arriba.setMetodo("atras");
        dibuja.getChildren().add(mer_arriba);

    }
    
    private void buscarOpcion() {
        pf_tabla_generica tab_opcion = sis_soporte.obtener_instancia_soporte().consultar("SELECT paquete,enlace,auditada "
                + "FROM tbl_opcion "
                + "WHERE id_opcion=" + sis_soporte.obtener_instancia_soporte().obtener_variable("id_opcion") + " AND "
                + "id_empresa = " + empresa);
        if (tab_opcion.getTotalFilas() > 0) {
            str_paquete = tab_opcion.getValor(0, "paquete");
            str_tipo = tab_opcion.getValor(0, "enlace");
            if (tab_opcion.getValor(0, "auditada") != null) {
                sis_soporte.obtener_instancia_soporte().crear_variable("auditada", tab_opcion.getValor(0, "auditada"));
            } else {
                sis_soporte.obtener_instancia_soporte().crear_variable("auditada", "false");
            }
            str_manual = "ayuda_vc.pdf";
        }
    }

    public void cerrarDialogo(ActionEvent evt) {
        UIComponent com_padre = evt.getComponent();
        while (com_padre != null) {
            com_padre = com_padre.getParent();
            if (com_padre.getRendererType() != null && com_padre.getRendererType().equals("org.primefaces.component.DialogRenderer")) {
                break;
            }
        }
        if (com_padre != null) {
            ((pf_dialogo) com_padre).cerrar();
        }
    }

    public void cerrarConfirmar(ActionEvent evt) {
        UIComponent com_padre = evt.getComponent();
        while (com_padre != null) {
            com_padre = com_padre.getParent();
            String str_render = com_padre.getRendererType();
            if (str_render != null && str_render.equals("org.primefaces.component.ConfirmDialogRenderer")) {
                break;
            }
        }
        if (com_padre != null) {
            ((pf_confirmar) com_padre).cerrar();
        }
    }
    
    public void cargar_inicio() {
        sis_soporte.obtener_instancia_soporte().crear_variable("ALTO_PANTALLA", ith_alto.getValue() + ""); //Alto disponible
        sis_soporte.obtener_instancia_soporte().crear_variable("ALTO", alto.getValue() + "");  //Alto browser
        sis_soporte.obtener_instancia_soporte().crear_variable("ANCHO", ancho.getValue() + ""); //Ancho browser        
        sis_soporte.obtener_instancia_soporte().crear_variable("id_opcion", "-1");
        istr_titulo = "Inicio";
        mensajes.getChildren().clear();
        dibuja.getChildren().clear();
        clase = sis_soporte.obtener_instancia_soporte().cargarPantalla("ec.mplus.sistema", "sis_admin_usuario");
    }
    
    public void cargar_correo() {
        sis_soporte.obtener_instancia_soporte().crear_variable("ALTO_PANTALLA", ith_alto.getValue() + ""); //Alto disponible
        sis_soporte.obtener_instancia_soporte().crear_variable("ALTO", alto.getValue() + "");  //Alto browser
        sis_soporte.obtener_instancia_soporte().crear_variable("ANCHO", ancho.getValue() + ""); //Ancho browser        
        sis_soporte.obtener_instancia_soporte().crear_variable("id_opcion", "-1");
        istr_titulo = "Soporte Mail";
        mensajes.getChildren().clear();
        dibuja.getChildren().clear();
        clase = sis_soporte.obtener_instancia_soporte().cargarPantalla("ec.mplus.mail", "mai_correo");
    }
    
    public void cerrarSql() {
        error_sql.setVisible(false);
        error_sql.limpiar();
    }

    public void ayuda() {
        //Cargar la ayuda de la pantalla     
        if (str_manual != null) {
//            sis_soporte.obtener_instancia_soporte().ejecutarJavaScript("window.open('" + sis_soporte.obtener_instancia_soporte().getURL() + "/manuales/" + str_paquete + "/" + str_manual + "','nuevo','directories=no,location=no,menubar=no,scrollbars=yes,statusbar=no,tittlebar=no,width=800,height=600')");
            sis_soporte.obtener_instancia_soporte().ejecutarJavaScript("window.open('" + sis_soporte.obtener_instancia_soporte().getURL() + "/manuales/" + str_manual + "','nuevo','directories=no,location=no,menubar=no,scrollbars=yes,statusbar=no,tittlebar=no,width=800,height=600')");
        }
    }
    
    public HtmlForm getFormulario() {
        return formulario;
    }

    public void setFormulario(HtmlForm formulario) {
        this.formulario = formulario;
    }

    public Object getClase() {
        return clase;
    }

    public void setClase(Object clase) {
        this.clase = clase;
    }

    public pf_buscar_tabla getBus_buscar() {
        return bus_buscar;
    }

    public void setBus_buscar(pf_buscar_tabla bus_buscar) {
        this.bus_buscar = bus_buscar;
    }

    public String getIstr_titulo() {
        return istr_titulo;
    }

    public void setIstr_titulo(String istr_titulo) {
        this.istr_titulo = istr_titulo;
    }

    public pf_importar_tabla getImt_importar() {
        return imt_importar;
    }

    public void setImt_importar(pf_importar_tabla imt_importar) {
        this.imt_importar = imt_importar;
    }

    public pf_formato_tabla getFot_formato() {
        return fot_formato;
    }

    public void setFot_formato(pf_formato_tabla fot_formato) {
        this.fot_formato = fot_formato;
    }

    public pf_dialogo getDia_sucu_usuario() {
        return dia_sucu_usuario;
    }

    public void setDia_sucu_usuario(pf_dialogo dia_sucu_usuario) {
        this.dia_sucu_usuario = dia_sucu_usuario;
    }

    public pf_terminal_tabla getTerm_tabla() {
        return term_tabla;
    }

    public void setTerm_tabla(pf_terminal_tabla term_tabla) {
        this.term_tabla = term_tabla;
    }     

    public pf_seleccion_archivo getSar_upload() {
        return sar_upload;
    }

    public void setSar_upload(pf_seleccion_archivo sar_upload) {
        this.sar_upload = sar_upload;
    }

    public HtmlInputHidden getAlto() {
        return alto;
    }

    public void setAlto(HtmlInputHidden alto) {
        this.alto = alto;
    }

    public HtmlInputHidden getAncho() {
        return ancho;
    }

    public void setAncho(HtmlInputHidden ancho) {
        this.ancho = ancho;
    }

    public pf_panel_grupo getDibuja() {
        return dibuja;
    }

    public void setDibuja(pf_panel_grupo dibuja) {
        this.dibuja = dibuja;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public pf_error_sql getError_sql() {
        return error_sql;
    }

    public void setError_sql(pf_error_sql error_sql) {
        this.error_sql = error_sql;
    }

    public HtmlInputHidden getIth_alto() {
        return ith_alto;
    }

    public void setIth_alto(HtmlInputHidden ith_alto) {
        this.ith_alto = ith_alto;
    }

    public pf_panel_grupo getMensajes() {
        return mensajes;
    }

    public void setMensajes(pf_panel_grupo mensajes) {
        this.mensajes = mensajes;
    }

    public String getStr_manual() {
        return str_manual;
    }

    public void setStr_manual(String str_manual) {
        this.str_manual = str_manual;
    }

    public String getStr_paquete() {
        return str_paquete;
    }

    public void setStr_paquete(String str_paquete) {
        this.str_paquete = str_paquete;
    }

    public String getStr_tipo() {
        return str_tipo;
    }

    public void setStr_tipo(String str_tipo) {
        this.str_tipo = str_tipo;
    }
}
