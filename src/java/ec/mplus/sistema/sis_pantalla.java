/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.sistema;

import ec.xprime.componentes.*;
import ec.xprime.persistencia.cla_conexion;
import ec.xprime.sistema.sis_soporte;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Diego
 */
public abstract class sis_pantalla {

    public pf_barra_boton bar_botones = new pf_barra_boton();
    public pf_panel_grupo gru_pantalla = new pf_panel_grupo();

    public sis_pantalla() {
        gru_pantalla.setTransient(true);
        gru_pantalla.getChildren().add(bar_botones);
        crearPantalla(gru_pantalla);
    }

    /**
     * Crea la pantalla en el navegador web
     *
     * @param pantalla Grupo que contiene todos los componentes de la pantalla
     */
    private void crearPantalla(pf_panel_grupo pantalla) {
        pf_panel_grupo pan_dibuja = sis_soporte.obtener_instancia_soporte().getPantalla();
        if (pan_dibuja != null) {
            pan_dibuja.getChildren().add(pantalla);
        } else {
            System.out.println("No se pudo crear la Pantalla");
        }
    }

    public cla_conexion getConexion() {
        return sis_soporte.obtener_instancia_soporte().obtener_conexion();
    }

    protected void agregarComponente(UIComponent componente) {
        gru_pantalla.getChildren().add(componente);
    }

    /**
     * Ejecuta todos las sentencias sql generadas en la pantalla
     *
     * @return String con un mensaje si ocurrio algun error al ejecutar todas
     * las sentecnias, caso contrario retorna vacio
     */
    protected String guardarPantalla() {
        return sis_soporte.obtener_instancia_soporte().obtener_conexion().guardarPantalla();
    }

    /**
     * Ejecuta este método cuando se da click en el boton insertar
     */
    public abstract void insertar();

    /**
     * Ejecuta este método cuando se da click en el boton guardar
     */
    public abstract void guardar();

    /**
     * Ejecuta este método cuando se da click en el boton eliminar
     */
    public abstract void eliminar();

    /**
     * Ejecuta cuando da click en el boton de Reportes de la Barra de Botones
     */
    public void abrirListaReportes() {
    }

    /**
     * Ejecuta cuando se selecciona un reporte de la lista
     */
    public void aceptarReporte() {
    }

    /**
     * Ejecuta cuando da click en el boton Calendario de la Barra de Botones
     */
    public void abrirRangoFecha() {
    }

    public pf_barra_boton getBar_botones() {
        return bar_botones;
    }

    public void setBar_botones(pf_barra_boton bar_botones) {
        this.bar_botones = bar_botones;
    }

    /**
     * Cuando se presiona en el boton inicio de la barra de navegación
     */
    public void inicio() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.inicio();
        }
    }

    /**
     * Cuando se presiona en el boton fin de la barra de navegación
     */
    public void fin() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.fin();
        }
    }

    /**
     * Cuando se presiona en el boton siguiente de la barra de navegación
     */
    public void siguiente() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.siguiente();
        }
    }

    /**
     * Cuando se presiona en el boton atras de la barra de navegación
     */
    public void atras() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.atras();
        }
    }

    /**
     * Cuando se presiona el item buscar del menu contextual
     */
    public void buscar() {
        //Busca en la tabla que tiene el foco
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        pf_buscar_tabla bus_buscar = sis_soporte.obtener_instancia_soporte().getBuscaTabla();
        if (bus_buscar != null && tabla_foco != null) {
            bus_buscar.setBuscar(tabla_foco);
        }
    }

    /**
     * Cuando se presiona el item actualizar del menu contextual
     */
    public void actualizar() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.actualizar();
        }
    }

    /**
     * Cuando se presiona el item exportarXLS del menu contextual
     */
    public void exportarXLS() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        if (tabla_foco != null) {
            tabla_foco.exportarXLS();
        }
    }

    public void terminal() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        pf_terminal_tabla terminal = sis_soporte.obtener_instancia_soporte().getTerminal();
        if (terminal != null && tabla_foco != null) {
            terminal.setTerminalTabla(tabla_foco);
            terminal.dibujar();
        }
    }

    /**
     * Cuando se presiona el item importarXLS del menu contextual
     */
    public void importarXLS() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        pf_importar_tabla imt_importar = sis_soporte.obtener_instancia_soporte().getImportarTabla();
        if (imt_importar != null && tabla_foco != null) {
            imt_importar.setImportarTabla(tabla_foco);
            imt_importar.dibujar();
        }
    }

    /**
     * Cuando se presiona el item formatoTabla del menu contextual
     */
    public void formatoTabla() {
        pf_tabla tabla_foco = sis_soporte.obtener_instancia_soporte().getTablaisFocus();
        pf_formato_tabla fot_formato = sis_soporte.obtener_instancia_soporte().getFormatoTabla();
        if (fot_formato != null && tabla_foco != null) {
            fot_formato.setFormatoTabla(tabla_foco);
            fot_formato.dibujar();
        }
    }

    /**
     * Cuando se acepta la busqueda
     */
    public void aceptarBuscar() {
        pf_buscar_tabla bus_buscar = sis_soporte.obtener_instancia_soporte().getBuscaTabla();
        if (bus_buscar != null) {
            bus_buscar.aceptarBuscar();
        }
    }

    /**
     * Cuando se necesitan subir archivos
     */
    public void abrirSeleccionarArchivo(ActionEvent evt) {
        pf_seleccion_archivo sar_upload = sis_soporte.obtener_instancia_soporte().getSeleccionArchivo();
        if (sar_upload != null) {
            pf_boton bot_subir = (pf_boton) evt.getComponent();
            if (bot_subir.getDir() != null) {
                sar_upload.getUpload().setSoloImagenes();
               
            } else {
                sar_upload.limpiar();
            }
            sar_upload.getUpload().setFileUploadListener(bot_subir.getUploadMetodo());
            sar_upload.getUpload().setUpdate(sis_soporte.obtener_instancia_soporte().getTablaisFocus().getIdCompleto());
            try {               
                ((pf_tabla) bot_subir.getParent().getParent().getParent()).setFocus();
            } catch (Exception e) {
            }
            sar_upload.dibujar();
        }
    }
}
