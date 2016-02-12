/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.parametros;

import ec.mplus.sistema.sis_pantalla;
import ec.xprime.componentes.*;
import ec.xprime.sistema.sis_soporte;

/**
 *
 * @author xEdushx
 */
public class par_grupos extends sis_pantalla {

    private pf_tabla tab_tabla_grupos = new pf_tabla();

    public par_grupos() {
        
        tab_tabla_grupos.setId("tab_tabla_grupos");
        tab_tabla_grupos.setIdCompleto("tab_tabulador:tab_tabla_grupos");
        tab_tabla_grupos.setTabla("mov_grupos", "gru_codigo", 1);
        tab_tabla_grupos.getColumna("gru_codigo").setNombreVisual("CODIGO");
        tab_tabla_grupos.getColumna("gru_nombre").setNombreVisual("GRUPO");
        tab_tabla_grupos.getColumna("gru_nombre_supervisor").setNombreVisual("SUPERVISOR");
        tab_tabla_grupos.getColumna("prv_codigo").setNombreVisual("PROVEEDOR");
        tab_tabla_grupos.getColumna("prv_codigo").setCombo("mov_proveedores", "prv_codigo", "prv_nombre", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        tab_tabla_grupos.dibujar();
        
        pf_panel_tabla pat_panel_grupos = new pf_panel_tabla();
        pat_panel_grupos.setHeader(new pf_etiqueta("GRUPOS DE USUARIOS"));
        pat_panel_grupos.setPanelTabla(tab_tabla_grupos);

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabulador.agregarTab("GRUPOS DE USUARIOS", pat_panel_grupos);

        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir1(tab_tabulador);
        agregarComponente(div_division);

        bar_botones.quitarBotonsNavegacion();
    }

    @Override
    public void insertar() {
        if (tab_tabla_grupos.isFocus()) {
            tab_tabla_grupos.insertar();
        }
    }

    @Override
    public void guardar() {
        tab_tabla_grupos.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_tabla_grupos.isFocus()) {
            tab_tabla_grupos.eliminar();
        }
    }

    public pf_tabla getTab_tabla_grupos() {
        return tab_tabla_grupos;
    }

    public void setTab_tabla_grupos(pf_tabla tab_tabla_grupos) {
        this.tab_tabla_grupos = tab_tabla_grupos;
    }
}
