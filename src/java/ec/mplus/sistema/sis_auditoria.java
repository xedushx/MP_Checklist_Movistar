/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.sistema;

import ec.xprime.componentes.*;
import ec.xprime.sistema.sis_soporte;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author epacheco
 */
public class sis_auditoria extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_auditoria.class.getName());
    private pf_tabla tab_auditoria = new pf_tabla();
    private pf_layout div_division = new pf_layout();
    private pf_calendario cal_fecha_inicial = new pf_calendario();
    private pf_calendario cal_fecha_final = new pf_calendario();
    private pf_hora hor_inicial = new pf_hora();
    private pf_hora hor_final = new pf_hora();

    public sis_auditoria() {
        LOGGER.log(Level.INFO, "sis_auditoria cargando");
        bar_botones.limpiar();

        bar_botones.agregarComponente(new pf_etiqueta("Fecha Inicial :"));
        cal_fecha_inicial.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_inicial);

        bar_botones.agregarComponente(new pf_etiqueta("Fecha Final :"));
        cal_fecha_final.setFechaActual();
        bar_botones.agregarComponente(cal_fecha_final);

        bar_botones.agregarComponente(new pf_etiqueta("Desde :"));
        hor_inicial.setValue(sis_soporte.obtener_instancia_soporte().getHora(sis_soporte.obtener_instancia_soporte().obtener_formato_hora("07:00:00")));
        bar_botones.agregarComponente(hor_inicial);

        bar_botones.agregarComponente(new pf_etiqueta("Hasta :"));
        hor_final.setValue(sis_soporte.obtener_instancia_soporte().getHora(sis_soporte.obtener_instancia_soporte().obtener_formato_hora("18:00:00")));
        bar_botones.agregarComponente(hor_final);

        pf_boton bot_filtrar = new pf_boton();
        bot_filtrar.setValue("Actualizar");
        bot_filtrar.configurar_ActionListener("actualizarAuditoria");
        bot_filtrar.setIcon("ui-icon-refresh");
        bar_botones.agregarBoton(bot_filtrar);

        tab_auditoria.setId("tab_auditoria");
        tab_auditoria.setTabla("tbl_auditoria", "id_auditoria", 1);
        tab_auditoria.setLectura(true);
        tab_auditoria.getColumna("id_usuario").setCombo("tbl_usuario", "id_usuario", "nombre_completo", "");
        tab_auditoria.getColumna("id_opcion").setCombo("tbl_opcion", "id_opcion", "nombre", "");
        tab_auditoria.getColumna("id_accion").setCombo("tbl_accion", "id_accion", "nombre", "");
        tab_auditoria.setRows(20);
        tab_auditoria.getColumna("id_usuario").setFiltro(true);
        tab_auditoria.getColumna("id_accion").setFiltro(true);
        tab_auditoria.getColumna("id_opcion").setFiltro(true);
        tab_auditoria.getColumna("valor").setLongitud(300);
        tab_auditoria.setCondicion(getFiltrosAuditoria());
        tab_auditoria.setCampoOrden("id_auditoria desc");
        tab_auditoria.dibujar();

        pf_panel_tabla pat_auditoria = new pf_panel_tabla();
        pat_auditoria.setId("pat_auditoria");
        tab_auditoria.setHeader("Transacciones realizadas en el sistema");
        pat_auditoria.setPanelTabla(tab_auditoria);

        div_division.setId("div_division");
        div_division.dividir1(pat_auditoria);

        agregarComponente(div_division);
    }

    @Override
    public void insertar() {
        
    }

    @Override
    public void guardar() {
        
    }

    @Override
    public void eliminar() {
        
    }

    private String getFiltrosAuditoria() {
        // Forma y valida las condiciones de fecha y hora
        String str_filtros = "";
        if (cal_fecha_inicial.getValue() != null
                && cal_fecha_final.getValue() != null
                && hor_inicial.getValue() != null
                && hor_final.getValue() != null) {

            str_filtros = " fecha BETWEEN "
                    + sis_soporte.obtener_instancia_soporte().getFormatoFechaSQL(cal_fecha_inicial.getFecha()) + " AND "
                    + sis_soporte.obtener_instancia_soporte().getFormatoFechaSQL(cal_fecha_final.getFecha());
            str_filtros += " AND hora BETWEEN "
                    + sis_soporte.obtener_instancia_soporte().getFormatoHoraSQL(hor_inicial.getHora())
                    + " AND "
                    + sis_soporte.obtener_instancia_soporte().getFormatoHoraSQL(hor_final.getHora());

        } else {
            sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("Filtros no válidos",
                    "Debe ingresar los fitros de fechas y horas");
        }
        return str_filtros;
    }

    public void actualizarAuditoria() {
        if (!getFiltrosAuditoria().isEmpty()) {
            tab_auditoria.setCondicion(getFiltrosAuditoria());
            tab_auditoria.ejecutarSql();
            sis_soporte.obtener_instancia_soporte().addUpdate("div_division");
        }
    }

    public pf_tabla getTab_auditoria() {
        return tab_auditoria;
    }

    public void setTab_auditoria(pf_tabla tab_auditoria) {
        this.tab_auditoria = tab_auditoria;
    }
}
