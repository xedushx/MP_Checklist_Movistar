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
public class par_categoria_insumo extends sis_pantalla {

    private pf_tabla tab_categoria_insumo = new pf_tabla();

    public par_categoria_insumo() {
        tab_categoria_insumo.setId("tab_categoria_insumo");
        tab_categoria_insumo.setIdCompleto("tab_tabulador:tab_categoria_insumo");
        tab_categoria_insumo.setTabla("mov_categoria_insumo", "cin_codigo", 1);
        tab_categoria_insumo.getColumna("cin_codigo").setNombreVisual("CODIGO");
        tab_categoria_insumo.getColumna("cin_nombre").setNombreVisual("NOMBRE");
        tab_categoria_insumo.dibujar();
        pf_panel_tabla pat_categoria_insumo = new pf_panel_tabla();
        pat_categoria_insumo.setHeader(new pf_etiqueta("CATEGORÍAS DE INSUMOS"));
        pat_categoria_insumo.setPanelTabla(tab_categoria_insumo);
        
        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabulador.agregarTab("CATEGORÍAS DE INSUMOS", pat_categoria_insumo);
        
        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir1(tab_tabulador);
        agregarComponente(div_division);

        bar_botones.quitarBotonsNavegacion();
    }

    @Override
    public void insertar() {
        if (tab_categoria_insumo.isFocus()) {
            tab_categoria_insumo.insertar();
        } 
        
    }

    @Override
    public void guardar() {
        tab_categoria_insumo.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_categoria_insumo.isFocus()) {
            tab_categoria_insumo.eliminar();
        } 
    }

    public pf_tabla getTab_categoria_insumo() {
        return tab_categoria_insumo;
    }

    public void setTab_categoria_insumo(pf_tabla tab_categoria_insumo) {
        this.tab_categoria_insumo = tab_categoria_insumo;
    }

    
    
}
