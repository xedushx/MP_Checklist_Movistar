/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.sistema;

import ec.xprime.componentes.*;
import ec.xprime.sistema.sis_soporte;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author HP
 */
public class sis_usuario extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_usuario.class.getName());
    private pf_tabla tab_tabla = new pf_tabla();
    private pf_tabla tab_usuario_grupo = new pf_tabla();
    private pf_dialogo dia_clave = new pf_dialogo();
    private pf_etiqueta eti_clave = new pf_etiqueta();
    private pf_entrada_texto tex_nick;
    private pf_reporte rep_reporte = new pf_reporte();
    private pf_seleccion_formato_reporte sel_rep = new pf_seleccion_formato_reporte();
    private Map map_parametros = new HashMap();

    public sis_usuario() {

        pf_boton bot_generar = new pf_boton();
        pf_boton bot_activar = new pf_boton();

        bot_generar.setValue("Generar Nueva Clave");
        bot_generar.setMetodo("abrirGenerarClave");
        bar_botones.agregarBoton(bot_generar);

        bot_activar.setValue("Activar / Desactivar");
        bot_activar.setMetodo("activarUsuario");
        bot_activar.setTitle("Activar / Desactivar al usuario actual");
        bar_botones.agregarBoton(bot_activar);

        String lstr_condicion = " id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa() + " and id_perfil > 1";

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTipoFormulario(true);
        tab_tabla.setTabla("tbl_usuario", "id_usuario", 1);
        tab_tabla.agregarRelacion(tab_usuario_grupo);
        tab_tabla.getColumna("id_perfil").setCombo("tbl_perfil", "id_perfil", "nombre", lstr_condicion);
        tab_tabla.getColumna("id_perfil").setRequerida(true);
        tab_tabla.getColumna("nombre_completo").setNombreVisual("NOMBRES");
        tab_tabla.getColumna("id_perfil").setNombreVisual("PERFIL");
        tab_tabla.getColumna("nombre").setNombreVisual("NICK NAME");
        tab_tabla.getColumna("nombre").setUnico(true);
        tab_tabla.getColumna("estado_sesion").setCheck();
        tab_tabla.getColumna("estado_sesion").setValorDefecto("false");
        tab_tabla.getColumna("estado_sesion").setVisible(false);
        tab_tabla.getColumna("id_usuario").setVisible(false);
        tab_tabla.getColumna("tema").setVisible(false);
        tab_tabla.getColumna("clave").setVisible(false);
        tab_tabla.getColumna("tema").setValorDefecto("sam");
        tab_tabla.getColumna("fecha_registro").setVisible(false);
        tab_tabla.getColumna("nombre").setMetodoChange("asignarClave");
        tab_tabla.getColumna("correo").setLongitud(150);
        tab_tabla.getColumna("correo2").setLongitud(150);
        tab_tabla.getColumna("ruta_foto").setNombreVisual("FOTOGRAFÍA");
        tab_tabla.getColumna("ruta_foto").setUpload("fotos_usuarios");
        tab_tabla.getColumna("ruta_foto").setImagen("100", "200");
        tab_tabla.setCondicion(lstr_condicion);
        tab_tabla.setFocus();
        tab_tabla.dibujar();

        pf_panel_tabla pat_panel1 = new pf_panel_tabla();
        pat_panel1.setMensajeWarn("Cuando se crean un usuario nuevo la clave es la misma que el valor del campo NICK NAME");
        pat_panel1.setPanelTabla(tab_tabla);

        tab_usuario_grupo.setId("tab_usuario_grupo");
        tab_usuario_grupo.setIdCompleto("tab_tabulador:tab_usuario_grupo");
        tab_usuario_grupo.setTabla("mov_usuario_grupo", "ugr_codigo", 2);
        tab_usuario_grupo.getColumna("gru_codigo").setCombo("mov_grupos", "gru_codigo", "gru_nombre", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        tab_usuario_grupo.getColumna("ugr_codigo").setNombreVisual("CODIGO");
        tab_usuario_grupo.getColumna("gru_codigo").setNombreVisual("GRUPO");
        tab_usuario_grupo.getColumna("ugr_estado").setNombreVisual("ESTADO");
        tab_usuario_grupo.dibujar();
        pf_panel_tabla pat_panel_grupos = new pf_panel_tabla();
        pat_panel_grupos.setHeader(new pf_etiqueta("GRUPO"));
        pat_panel_grupos.setPanelTabla(tab_usuario_grupo);

        tab_tabulador.agregarTab("GRUPOS", pat_panel_grupos);

        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, tab_tabulador, "50%", "H");
        agregarComponente(div_division);

        dia_clave.setId("dia_clave");
        dia_clave.setTitle("Generar Nueva Clave");
        dia_clave.setWidth("40%");
        dia_clave.setHeight("18%");
        dia_clave.getBot_aceptar().setMetodo("aceptarClave");
        dia_clave.setResizable(false);
        pf_panel_grupo gru_cuerpo = new pf_panel_grupo();
        pf_etiqueta eti_mensaje = new pf_etiqueta();
        eti_mensaje.setValue("El sistema generó una nueva clave para el usuario seleccionado, para asignar la clave presionar el botón aceptar");
        eti_mensaje.setStyle("font-size: 13px;border: none;text-shadow: 0px 2px 3px #ccc;background: none;");
        eti_clave.setStyle("font-size: 25px;");
        pf_grid gri_clave = new pf_grid();
        gri_clave.setWidth("100%");
        gri_clave.setStyle("text-align: center;");
        gri_clave.getChildren().add(eti_clave);
        gru_cuerpo.getChildren().add(eti_mensaje);
        gru_cuerpo.getChildren().add(gri_clave);

        dia_clave.setDialogo(gru_cuerpo);
        agregarComponente(dia_clave);

        bar_botones.agregarReporte();
        rep_reporte.setId("rep_reporte");
        rep_reporte.getBot_aceptar().setMetodo("aceptarReporte");
        agregarComponente(rep_reporte);

        sel_rep.setId("sel_rep");
        agregarComponente(sel_rep);

        //Para activar o desactivar el campo nick	
        tex_nick = ((pf_entrada_texto) sis_soporte.obtener_instancia_soporte().getComponente(tab_tabla.getColumna("nombre").getId()));
        cambiarEstadoNick();

    }

    @Override
    public void aceptarReporte() {
        // TODO Auto-generated method stub
        if (rep_reporte.getReporteSelecionado().equals("Reporte de Usuarios")) {
            if (rep_reporte.isVisible()) {
                map_parametros = new HashMap();
                map_parametros.put("titulo", "REPORTE DE USUARIOS");
                sel_rep.setSeleccionFormatoReporte(map_parametros, rep_reporte.getPath());
                sel_rep.dibujar();
                rep_reporte.cerrar();
                sis_soporte.obtener_instancia_soporte().addUpdate("rep_reporte");
            }
        }

    }

    @Override
    public void abrirListaReportes() {
        // TODO Auto-generated method stub
        rep_reporte.dibujar();
    }

    /**
     * Activa o desactiva el cuadro de texto del nick
     */
    private void cambiarEstadoNick() {
        if (tab_tabla.isFilaInsertada()) {
            //si la fila es insertada activo el cuadro de texto
            tex_nick.setDisabled(false);
        } else {
            tex_nick.setDisabled(true);
        }
    }

    @Override
    public void inicio() {
        // TODO Auto-generated method stub
        super.inicio();
        cambiarEstadoNick();
    }

    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
        super.siguiente();
        cambiarEstadoNick();
    }

    @Override
    public void atras() {
        // TODO Auto-generated method stub
        super.atras();
        cambiarEstadoNick();
    }

    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
        cambiarEstadoNick();
    }

    @Override
    public void actualizar() {
        // TODO Auto-generated method stub
        super.actualizar();
        cambiarEstadoNick();
    }

    @Override
    public void aceptarBuscar() {
        // TODO Auto-generated method stub
        super.aceptarBuscar();
        cambiarEstadoNick();
    }

    /**
     * Activa el usuario seleccionado
     */
    public void activarUsuario() {
        if (tab_tabla.getValor("estado_sesion").equals("true")) {
            sis_soporte.obtener_instancia_soporte().obtener_conexion().desactivarUsuario(tab_tabla.getValor("id_usuario"));
            sis_soporte.obtener_instancia_soporte().agregarMensaje("Se desactivó al usuario " + tab_tabla.getValor("nombre_completo"), "");
            tab_tabla.setValor("estado_sesion", "false");
            sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabla");
        } else {
            sis_soporte.obtener_instancia_soporte().obtener_conexion().activarUsuario(tab_tabla.getValor("id_usuario"));
            sis_soporte.obtener_instancia_soporte().agregarMensaje("Se activó al usuario " + tab_tabla.getValor("nombre_completo"), "");
            tab_tabla.setValor("estado_sesion", "true");
            sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabla");
        }
    }

    @Override
    public void insertar() {
        if (tab_tabla.isFocus()) {
            if (tab_tabla.isFilaInsertada() == false) {
                tab_tabla.getColumna("fecha_registro").setValorDefecto(sis_soporte.obtener_instancia_soporte().getFechaActual() + " " + sis_soporte.obtener_instancia_soporte().getHoraActual());
                tab_tabla.insertar();
            } else {
                sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("No se puede Insertar",
                        "Debe guardar el Usuario actual");
            }
        } else if (tab_usuario_grupo.isFocus()) {
            tab_usuario_grupo.insertar();
        }

        cambiarEstadoNick();
    }

    public void asignarClave(AjaxBehaviorEvent evt) {
        tab_tabla.modificar(evt);
        if (tab_tabla.isFilaInsertada()) {
            tab_tabla.setValor("clave", sis_soporte.obtener_instancia_soporte().obtener_conexion().getEncriptar(tab_tabla.getValor("nombre")));
            sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabla");
        }
    }

    @Override
    public void guardar() {
        // valida la longitud minima del campo nick si inserto o modifico
        if (tab_tabla.isFilaInsertada() || tab_tabla.isFilaModificada()) {
            if (tab_tabla.getValor("nombre") == null) {
                sis_soporte.obtener_instancia_soporte().agregarMensajeError("No se puede guardar",
                        "El campo de login debe estar ingresado");
                return;
            }
        }

        tab_tabla.guardar();
        tab_usuario_grupo.guardar();
        guardarPantalla();
        cambiarEstadoNick();
    }

    @Override
    public void eliminar() {
        if (tab_tabla.isFocus()) {
            if (tab_tabla.isFilaInsertada()) {
                tab_tabla.eliminar();
            } else {
                sis_soporte.obtener_instancia_soporte().obtener_conexion().desactivarUsuario(tab_tabla.getValor("id_usuario"));
                tab_tabla.setValor("estado_sesion", "true");
                sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("Usuario deshabilitado", "");
            }
        } else if (tab_usuario_grupo.isFocus()){
            tab_usuario_grupo.eliminar();
        }
        cambiarEstadoNick();
    }

    public void abrirGenerarClave() {
        if (!tab_tabla.isFilaInsertada()) {
            eti_clave.setValue(sis_soporte.obtener_instancia_soporte().obtener_conexion().getGenerarClave());
            dia_clave.dibujar();
        } else {
            sis_soporte.obtener_instancia_soporte().agregarMensaje(
                    "No se puede generar una nueva clave a usuarios nuevos",
                    "");
        }
    }

    public void aceptarClave() {
        // Resetea la Clave
        sis_soporte.obtener_instancia_soporte().obtener_conexion().resetearClave(tab_tabla.getValorSeleccionado(),
                tab_tabla.getValor("nombre"), eti_clave.getValue().toString(), tab_tabla.getValor("correo"), "M2M Movistar");
        sis_soporte.obtener_instancia_soporte().agregarMensaje("Cambio de clave",
                "La clave ha sido cambiada correctamente");
        sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabla");
        dia_clave.cerrar();
    }

    public pf_dialogo getDia_clave() {
        return dia_clave;
    }

    public void setDia_clave(pf_dialogo dia_clave) {
        this.dia_clave = dia_clave;
    }

    public pf_tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(pf_tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }

    public pf_reporte getRep_reporte() {
        return rep_reporte;
    }

    public void setRep_reporte(pf_reporte rep_reporte) {
        this.rep_reporte = rep_reporte;
    }

    public pf_seleccion_formato_reporte getSel_rep() {
        return sel_rep;
    }

    public void setSel_rep(pf_seleccion_formato_reporte sel_rep) {
        this.sel_rep = sel_rep;
    }

    public pf_tabla getTab_usuario_grupo() {
        return tab_usuario_grupo;
    }

    public void setTab_usuario_grupo(pf_tabla tab_usuario_grupo) {
        this.tab_usuario_grupo = tab_usuario_grupo;
    }
}
