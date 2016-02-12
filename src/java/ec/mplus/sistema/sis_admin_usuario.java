/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.sistema;

import ec.xprime.componentes.*;
import ec.xprime.sistema.sis_soporte;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.component.menu.Menu;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.themeswitcher.ThemeSwitcher;

/**
 *
 * @author HP
 */
public final class sis_admin_usuario extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_admin_usuario.class.getName());
    private pf_tabla tab_form_usuario = new pf_tabla();
    private pf_panel pan_opcion = new pf_panel();
    private int int_opcion = 1;
    private ThemeSwitcher ths_temas = new ThemeSwitcher();
    private pf_calendario cal_calendario = new pf_calendario();
    private pf_clave cla_clave_actual = new pf_clave();
    private pf_clave cla_nueva = new pf_clave();
    private pf_clave cla_confirmar = new pf_clave();
    private pf_efecto efecto = new pf_efecto();
    private pf_imagen ima_usuario = new pf_imagen();
    
    public sis_admin_usuario() {
        
        pf_item_menu mit_datos = new pf_item_menu();
        mit_datos.setIcon("ui-icon-person");
        mit_datos.setValue("Datos del Usuario");
        mit_datos.setUpdate("pan_opcion");
        mit_datos.setMetodo("dibujarDatosUusario");

        pf_item_menu mit_clave = new pf_item_menu();
        mit_clave.setValue("Cambiar Clave ");
        mit_clave.setIcon("ui-icon-key");
        mit_clave.setUpdate("pan_opcion");
        mit_clave.setMetodo("dibujarCambiarClave");

        pf_item_menu mit_tema = new pf_item_menu();
        mit_tema.setValue("Cambiar Tema ");
        mit_tema.setIcon("ui-icon-image");
        mit_tema.setUpdate("pan_opcion");
        mit_tema.setMetodo("dibujarTemas");

        Menu men_menu = new Menu();
        men_menu.getChildren().add(mit_datos);
        men_menu.getChildren().add(mit_clave);
        men_menu.getChildren().add(mit_tema);

        Panel pan_menu = new Panel();
        pan_menu.setHeader("OPCIONES");
        pan_menu.getChildren().add(men_menu);

        cal_calendario.setMode("inline");
        Panel pan_calendario = new Panel();
        pan_calendario.setHeader("CALENDARIO");
        pan_calendario.getChildren().add(cal_calendario);
        pf_panel_grupo gru_panel_izquierda = new pf_panel_grupo();
        gru_panel_izquierda.getChildren().add(pan_menu);
        gru_panel_izquierda.getChildren().add(pan_calendario);

        Panel pan_empresa = new Panel();
        pan_empresa.setHeader("EMPRESA");
        pf_imagen ima_empresa = new pf_imagen();
        ima_empresa.setValue((sis_soporte.obtener_instancia_soporte().obtener_conexion().consultar("SELECT logo_empr from tbl_empresa where id_empresa=" + sis_soporte.obtener_instancia_soporte().obtener_variable("empresa")).get(0)));
        ima_empresa.setWidth("200");
        ima_empresa.setHeight("100");
        pan_empresa.getChildren().add(ima_empresa);
        pf_etiqueta eti_sucursal = new pf_etiqueta();
        eti_sucursal.setStyle("width: 100%;font-size: 13px;text-align: center;font-weight: bold;");
        eti_sucursal.setValue("<br/>" + sis_soporte.obtener_instancia_soporte().obtener_conexion().consultar("SELECT nombre from tbl_empresa where id_empresa=" + sis_soporte.obtener_instancia_soporte().obtener_variable("empresa")).get(0));
        pan_empresa.getChildren().add(eti_sucursal);
        gru_panel_izquierda.getChildren().add(pan_empresa);
        pan_opcion.setId("pan_opcion");
        pan_opcion.setTransient(true);
        pan_opcion.setStyle("overflow: auto");

        efecto.setType("drop");
        efecto.setSpeed(150);
        efecto.setPropiedad("mode", "'show'");
        efecto.setEvent("load");

        pan_opcion.getChildren().add(efecto);

        dibujarDatosUusario();
        
        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(gru_panel_izquierda, pan_opcion, "20%", "V");
        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();
        bar_botones.quitarBotonsNavegacion();

        agregarComponente(div_division);

        ima_usuario.setId("ima_usuario");
    }

    public void dibujarTemas() {
        pan_opcion.setHeader("Tema del Usuario");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);
        pf_panel_grupo gru_panel = new pf_panel_grupo();
        gru_panel.setStyle("overflow:auto; display:block;");
        pf_grid gri_tema = new pf_grid();
        gri_tema.setColumns(2);
        gri_tema.getChildren().add(new pf_etiqueta("Tema"));
        List<String> lis_temas = new ArrayList<String>();
        lis_temas.add("afterdark");
        lis_temas.add("afternoon");
        lis_temas.add("afterwork");
        lis_temas.add("aristo");
        lis_temas.add("black-tie");
        lis_temas.add("blitzer");
        lis_temas.add("bluesky");
        lis_temas.add("bootstrap");
        lis_temas.add("casablanca");
        lis_temas.add("cupertino");
        lis_temas.add("eggplant");
        lis_temas.add("excite-bike");
        lis_temas.add("flick");
        lis_temas.add("glass-x");
        lis_temas.add("home");
        lis_temas.add("hot-sneaks");
        lis_temas.add("humanity");
        lis_temas.add("le-frog");
        lis_temas.add("midnight");
        lis_temas.add("mint-choc");
        lis_temas.add("overcast");
        lis_temas.add("pepper-grinder");
        lis_temas.add("redmond");
        lis_temas.add("rocket");
        lis_temas.add("sam");
        lis_temas.add("smoothness");
        lis_temas.add("south-street");
        lis_temas.add("start");
        lis_temas.add("sunny");
        lis_temas.add("swanky-purse");
        lis_temas.add("trontastic");
        lis_temas.add("ui-lightness");
        lis_temas.add("vader");

        pf_grid gri_imagenes = new pf_grid();
        gri_imagenes.setColumns(3);
        gri_imagenes.setWidth("100%");
        gri_imagenes.setStyle("font-size: 14px;text-align: center;");

        for (int i = 0; i < lis_temas.size(); i++) {
            pf_item_opcion ito_tema = new pf_item_opcion();
            ito_tema.setItemValue(lis_temas.get(i));
            ito_tema.setItemLabel(lis_temas.get(i) + "");
            ths_temas.getChildren().add(ito_tema);
            pf_grid gri_actual = new pf_grid();
            gri_actual.getChildren().add(new pf_etiqueta(lis_temas.get(i) + ""));
            pf_imagen ima_tema = new pf_imagen();
            ima_tema.setValue("/imagenes/temas/" + lis_temas.get(i) + ".png");
            gri_actual.getChildren().add(ima_tema);
            gri_imagenes.getChildren().add(gri_actual);
        }

        ths_temas.setStyle("width:180px");
        ths_temas.setValue(sis_soporte.obtener_instancia_soporte().obtener_variable("tema"));
        gri_tema.getChildren().add(ths_temas);
        gru_panel.getChildren().add(gri_tema);
        gru_panel.getChildren().add(gri_imagenes);
        pan_opcion.getChildren().add(gru_panel);
        int_opcion = 3;

    }

    public void dibujarDatosUusario() {
        pan_opcion.setHeader("Datos del Usuario");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);

        tab_form_usuario = new pf_tabla();
        tab_form_usuario.setTipoFormulario(true);
        tab_form_usuario.setMostrarNumeroRegistros(false);
        tab_form_usuario.setId("tab_form_usuario");
        tab_form_usuario.setTabla("tbl_usuario", "id_usuario", 0);
        tab_form_usuario.getColumna("id_usuario").setVisible(false);
        tab_form_usuario.getColumna("id_usuario").setLectura(true);
        tab_form_usuario.getColumna("id_perfil").setNombreVisual("PERFIL");
        tab_form_usuario.getColumna("id_perfil").setLectura(true);
        tab_form_usuario.getColumna("id_perfil").setCombo("tbl_perfil", "id_perfil", "nombre", "");
        tab_form_usuario.getColumna("estado_sesion").setVisible(false);
        tab_form_usuario.getColumna("tema").setVisible(false);
        tab_form_usuario.getColumna("fecha_registro").setVisible(false);
        tab_form_usuario.getColumna("tema").setNombreVisual("TEMA");
        tab_form_usuario.getColumna("nombre_completo").setNombreVisual("NOMBRE COMPLETO");
        tab_form_usuario.getColumna("nombre").setNombreVisual("NOMBRE LOGIN");
        tab_form_usuario.getColumna("correo").setNombreVisual("CORREO PRINCIPAL");
        tab_form_usuario.getColumna("correo2").setNombreVisual("CORREO ADICIONAL");
        tab_form_usuario.getColumna("clave").setVisible(false);
        tab_form_usuario.getColumna("ruta_foto").setVisible(false);
//        tab_form_usuario.getColumna("ruta_foto").setNombreVisual("FOTOGRAFÍA");
//        tab_form_usuario.getColumna("ruta_foto").setUpload("fotos_usuarios");
//        tab_form_usuario.getColumna("ruta_foto").setImagen("20", "20");
//        tab_form_usuario.getColumna("ruta_foto").setMetodoChange("actualizarFoto");
        tab_form_usuario.getColumna("nombre").setLectura(true);
//        tab_form_usuario.setLectura(true);
        tab_form_usuario.setCondicion("id_usuario=" + sis_soporte.obtener_instancia_soporte().obtener_variable("id_usuario"));
        tab_form_usuario.dibujar();

//        ima_usuario.setValue((sis_soporte.obtener_instancia_soporte().obtener_conexion().consultar("SELECT ruta_foto from tbl_usuario where id_usuario=" + sis_soporte.obtener_instancia_soporte().obtener_variable("id_usuario")).get(0)));
//        ima_usuario.setWidth("150");
//        ima_usuario.setHeight("200");

        actualizarFoto();
        
        pf_grid gri_tabla = new pf_grid();
        gri_tabla.setColumns(2);
        gri_tabla.getChildren().add(ima_usuario);
        gri_tabla.getChildren().add(tab_form_usuario);

        pan_opcion.getChildren().add(gri_tabla);
        int_opcion = 1;
    }

    public void actualizarFoto() {
        ima_usuario.setValue((sis_soporte.obtener_instancia_soporte().obtener_conexion().consultar("SELECT ruta_foto from tbl_usuario where id_usuario=" + sis_soporte.obtener_instancia_soporte().obtener_variable("id_usuario")).get(0)));
        ima_usuario.setWidth("150");
        ima_usuario.setHeight("200");
        sis_soporte.obtener_instancia_soporte().addUpdate("ima_usuario,tab_form_usuario");
    }
    
    public void dibujarCambiarClave() {
        pan_opcion.setHeader("Cambiar la clave");
        pan_opcion.getChildren().clear();
        pan_opcion.getChildren().add(efecto);
        cla_clave_actual.setValue("");
        cla_confirmar.setValue("");
        cla_nueva.setValue("");
        cla_nueva.setImmediate(false);
        cla_nueva.setFeedback(true);
        cla_confirmar.setFeedback(true);
        cla_confirmar.setImmediate(false);
        pf_grid gri_matriz = new pf_grid();
        gri_matriz.setColumns(2);
        gri_matriz.getChildren().add(new pf_etiqueta("<strong>CLAVE ACTUAL :</strong>"));
        gri_matriz.getChildren().add(cla_clave_actual);
        gri_matriz.getChildren().add(new pf_etiqueta("<strong>CLAVE NUEVA :</strong>"));
        gri_matriz.getChildren().add(cla_nueva);
        gri_matriz.getChildren().add(new pf_etiqueta("<strong>CONFIRMAR CLAVE NUEVA :</strong>"));
        gri_matriz.getChildren().add(cla_confirmar);
        pan_opcion.getChildren().add(gri_matriz);
        int_opcion = 2;
    }

    private void guardarClave() {
        LOGGER.log(Level.INFO, "Guardando clave");
        if (cla_clave_actual.getValue() != null && !cla_clave_actual.getValue().toString().isEmpty()) {
            if ((cla_nueva.getValue() != null && !cla_nueva.getValue().toString().isEmpty()) && (cla_confirmar.getValue() != null && !cla_confirmar.getValue().toString().isEmpty())) {

                if (cla_nueva.getValue().equals(cla_confirmar.getValue())) {
                    String lstr_clave_actual = (String) sis_soporte.obtener_instancia_soporte().obtener_conexion().consultar("SELECT clave FROM tbl_usuario WHERE id_usuario=" + sis_soporte.obtener_instancia_soporte().obtener_variable("id_usuario").replace("'", "")).get(0);
                    if (lstr_clave_actual.toString().equals(cla_clave_actual.getValue())) {
                        String sql = "UPDATE tbl_usuario SET clave='" + cla_nueva.getValue() + "' WHERE id_usuario=" + sis_soporte.obtener_instancia_soporte().obtener_variable("id_usuario").replace("'", "");
                        getConexion().ejecutarSql(sql);
                        try {
                            getConexion().getConnection().commit();
                        } catch (SQLException ex) {
                            Logger.getLogger(sis_admin_usuario.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        sis_soporte.obtener_instancia_soporte().agregar_mensaje("Cambió clave", "La clave ha sido cambiada correctamente");
                    } else {
                        sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("Error", "La clave es incorrecta");
                    }
                } else {
                    sis_soporte.obtener_instancia_soporte().agregar_mensaje_alerta("Validación", "Las claves deben ser iguales.");
                }

            } else {
                sis_soporte.obtener_instancia_soporte().agregar_mensaje_alerta("Validación", "Es necesario ingresar un valor a la nueva clave");
            }
        } else {
            sis_soporte.obtener_instancia_soporte().agregar_mensaje_alerta("Validación", "Es necesario ingresar la clave actual");
        }
    }

    private void guardarDatosUsuario() {
        tab_form_usuario.guardar();
        guardarPantalla();
    }

    @Override
    public void guardar() {
        if (int_opcion == 1) {
            actualizarFoto();
            guardarDatosUsuario();
        } else if (int_opcion == 2) {
            guardarClave();
        } else if (int_opcion == 3) {
            //Guarda si cambio de tema
            if (sis_soporte.obtener_instancia_soporte().obtener_variable("tema") == null || !sis_soporte.obtener_instancia_soporte().obtener_variable("tema").equals(ths_temas.getValue())) {
                String sql = "UPDATE tbl_usuario SET tema='" + ths_temas.getValue() + "' WHERE id_usuario=" + sis_soporte.obtener_instancia_soporte().obtener_variable("id_usuario");
                getConexion().ejecutarSql(sql);
                sis_soporte.obtener_instancia_soporte().agregarMensaje("Cambió Tema", "El tema se cambió correctamente");
                sis_soporte.obtener_instancia_soporte().crear_variable("tema", ths_temas.getValue() + "");
                if (int_opcion == 1) {
                    dibujarDatosUusario();
                }
            }
        }
    }

    @Override
    public void insertar() {
    }

    @Override
    public void eliminar() {
    }

    public pf_tabla getTab_form_usuario() {
        return tab_form_usuario;
    }

    public void setTab_form_usuario(pf_tabla tab_form_usuario) {
        this.tab_form_usuario = tab_form_usuario;
    }

    public pf_imagen getIma_usuario() {
        return ima_usuario;
    }

    public void setIma_usuario(pf_imagen ima_usuario) {
        this.ima_usuario = ima_usuario;
    }
}