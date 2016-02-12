/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.sistema;

import ec.xprime.componentes.pf_layout;
import ec.xprime.componentes.pf_panel_tabla;
import ec.xprime.componentes.pf_tabla;
import ec.xprime.componentes.pf_tabulador;
import ec.xprime.sistema.sis_soporte;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class sis_permiso extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_permiso.class.getName());
    private pf_tabla tab_tabla_pri = new pf_tabla();
    private pf_tabla tab_tabla_sec = new pf_tabla();
    private pf_tabla tab_tabla_reporte = new pf_tabla();

    public sis_permiso() {
        LOGGER.log(Level.INFO, "sis_permiso cargando");

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla_pri.setId("tab_tabla_pri");
        tab_tabla_pri.setTabla("tbl_perfil", "id_perfil", 1);
        tab_tabla_pri.getColumna("id_perfil2").setCombo("tbl_perfil", "id_perfil", "nombre", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_variable("empresa"));
        tab_tabla_pri.agregarRelacion(tab_tabla_sec);
        tab_tabla_pri.agregarRelacion(tab_tabla_reporte);
        tab_tabla_pri.setRows(20);
        tab_tabla_pri.dibujar();
        pf_panel_tabla pat_panel_pri = new pf_panel_tabla();
        pat_panel_pri.setPanelTabla(tab_tabla_pri);

        tab_tabla_sec.setId("tab_tabla_sec");
        tab_tabla_sec.setIdCompleto("tab_tabulador:tab_tabla_sec");
        tab_tabla_sec.setTabla("tbl_perfil_opcion", "id_perfil_opcion", 2);
        tab_tabla_sec.getColumna("id_opcion").setCombo("select a.id_opcion,a.nombre, "
                + "(case when b.id_opcion_2 is null then 'PANTALLA' else 'MENU' end) as nuevo "
                + "from tbl_opcion a left join (select DISTINCT id_opcion_2 from tbl_opcion "
                + "where id_opcion_2 in (select id_opcion from tbl_opcion where id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_variable("empresa") + ")) b on a.id_opcion=b.id_opcion_2 "
                + "where a.id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_variable("empresa") + " order by a.nombre");
        tab_tabla_sec.getColumna("lectura").setCheck();
        tab_tabla_sec.getColumna("id_opcion").setAutoCompletar();
        tab_tabla_sec.setRows(20);
        tab_tabla_sec.dibujar();
        pf_panel_tabla pat_panel_sec = new pf_panel_tabla();
        pat_panel_sec.setPanelTabla(tab_tabla_sec);

        tab_tabla_reporte.setId("tab_tabla_reporte");
        tab_tabla_reporte.setIdCompleto("tab_tabulador:tab_tabla_reporte");
        tab_tabla_reporte.setTabla("tbl_perfil_reporte", "id_perfil_reporte", 3);
        tab_tabla_reporte.getColumna("id_reporte").setCombo("select rep.id_reporte, "
                + "concat(op.nombre ,concat(' - ' , rep.nombre)) as nuevo "
                + "from tbl_reporte rep, tbl_opcion op "
                + "where rep.id_opcion = op.id_opcion "
                + "and op.id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_variable("empresa") + " "
                + "order by op.nombre, rep.nombre");
        tab_tabla_reporte.setRows(20);
        tab_tabla_reporte.dibujar();
        pf_panel_tabla pat_panel_reporte = new pf_panel_tabla();
        pat_panel_reporte.setPanelTabla(tab_tabla_reporte);
//        
        tab_tabulador.agregarTab("OPCIONES", pat_panel_sec);
        tab_tabulador.agregarTab("REPORTES", pat_panel_reporte);

//        pf_layout div_division_permisos = new pf_layout();
//        div_division_permisos.dividir2(pat_panel_sec, pat_panel_reporte, "50%", "V");

        pf_layout div_division = new pf_layout();
        div_division.dividir2(pat_panel_pri, tab_tabulador, "30%", "H");
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        sis_soporte.obtener_instancia_soporte().getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla_pri.guardar()) {
            if (tab_tabla_sec.guardar()) {
                if (tab_tabla_reporte.guardar()) {
                    guardarPantalla();
                }
            }
        }
    }

    @Override
    public void eliminar() {
        sis_soporte.obtener_instancia_soporte().getTablaisFocus().eliminar();
    }

    public pf_tabla getTab_tabla_pri() {
        return tab_tabla_pri;
    }

    public void setTab_tabla_pri(pf_tabla tab_tabla_pri) {
        this.tab_tabla_pri = tab_tabla_pri;
    }

    public pf_tabla getTab_tabla_sec() {
        return tab_tabla_sec;
    }

    public void setTab_tabla_sec(pf_tabla tab_tabla_sec) {
        this.tab_tabla_sec = tab_tabla_sec;
    }

    public pf_tabla getTab_tabla_reporte() {
        return tab_tabla_reporte;
    }

    public void setTab_tabla_reporte(pf_tabla tab_tabla_reporte) {
        this.tab_tabla_reporte = tab_tabla_reporte;
    }
}
