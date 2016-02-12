/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.parametros;

import ec.mplus.sistema.sis_pantalla;
import ec.xprime.componentes.*;

/**
 *
 * @author xEdushx
 */
public class par_unidades extends sis_pantalla {

    private pf_tabla tab_tabla_unidades = new pf_tabla();

    public par_unidades() {
        tab_tabla_unidades.setId("tab_tabla_unidades");
        tab_tabla_unidades.setIdCompleto("tab_tabulador:tab_tabla_unidades");
        tab_tabla_unidades.setTabla("mov_unidades", "uni_codigo", 1);
        tab_tabla_unidades.getColumna("uni_codigo").setNombreVisual("CODIGO");
        tab_tabla_unidades.getColumna("uni_nombre").setNombreVisual("NOMBRE");
        tab_tabla_unidades.getColumna("uni_simbologia").setNombreVisual("SIMBOLOGÍA");
        tab_tabla_unidades.dibujar();
        pf_panel_tabla pat_panel_unidades = new pf_panel_tabla();
        pat_panel_unidades.setHeader(new pf_etiqueta("UNIDADES DE MEDIDA"));
        pat_panel_unidades.setPanelTabla(tab_tabla_unidades);
        
        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabulador.agregarTab("UNIDADES DE MEDIDA", pat_panel_unidades);
        
        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir1(tab_tabulador);
        agregarComponente(div_division);

        bar_botones.quitarBotonsNavegacion();
    }

    @Override
    public void insertar() {
        if (tab_tabla_unidades.isFocus()) {
            tab_tabla_unidades.insertar();
        } 
    }

    @Override
    public void guardar() {
        tab_tabla_unidades.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_tabla_unidades.isFocus()) {
            tab_tabla_unidades.eliminar();
        } 
    }

    public pf_tabla getTab_tabla_unidades() {
        return tab_tabla_unidades;
    }

    public void setTab_tabla_unidades(pf_tabla tab_tabla_unidades) {
        this.tab_tabla_unidades = tab_tabla_unidades;
    }

    
    
}
