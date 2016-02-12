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
public class par_insumos extends sis_pantalla {

    private pf_tabla tab_tabla_insumos = new pf_tabla();
    

    public par_insumos() {
        tab_tabla_insumos.setId("tab_tabla_insumos");
        tab_tabla_insumos.setIdCompleto("tab_tabulador:tab_tabla_insumos");
        tab_tabla_insumos.setTabla("mov_insumos", "ins_codigo", 1);
        tab_tabla_insumos.getColumna("ins_codigo").setNombreVisual("ID");
        tab_tabla_insumos.getColumna("cin_codigo").setNombreVisual("CATEGORÍA");
        tab_tabla_insumos.getColumna("ins_codigo_externo").setNombreVisual("CÓDIGO");
        tab_tabla_insumos.getColumna("ins_lpu").setNombreVisual("LPU");
        tab_tabla_insumos.getColumna("ins_descripcion").setNombreVisual("DESCRIPCIÓN");
        tab_tabla_insumos.getColumna("uni_codigo").setNombreVisual("UNIDAD");
        tab_tabla_insumos.getColumna("ins_precio_referencial").setNombreVisual("PRECIO REFERENCIAL");
        tab_tabla_insumos.getColumna("ins_precio_final").setNombreVisual("PRECIO FINAL");
        tab_tabla_insumos.getColumna("ins_factor").setNombreVisual("FACTOR");
        
        tab_tabla_insumos.getColumna("cin_codigo").setCombo("mov_categoria_insumo", "cin_codigo", "cin_nombre", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        tab_tabla_insumos.getColumna("uni_codigo").setCombo("mov_unidades", "uni_codigo", "uni_simbologia", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        
        tab_tabla_insumos.getColumna("ins_codigo").setOrden(1);
        tab_tabla_insumos.getColumna("ins_codigo_externo").setOrden(2);
        tab_tabla_insumos.getColumna("ins_lpu").setOrden(3);
        tab_tabla_insumos.getColumna("cin_codigo").setOrden(4);
        tab_tabla_insumos.getColumna("ins_descripcion").setOrden(5);
        tab_tabla_insumos.getColumna("uni_codigo").setOrden(6);
        tab_tabla_insumos.getColumna("ins_precio_referencial").setOrden(7);
        tab_tabla_insumos.getColumna("ins_factor").setOrden(8);
        tab_tabla_insumos.getColumna("ins_precio_final").setOrden(9);
        
        tab_tabla_insumos.dibujar();
        pf_panel_tabla pat_panel_grupos = new pf_panel_tabla();
        pat_panel_grupos.setHeader(new pf_etiqueta("INSUMOS"));
        pat_panel_grupos.setPanelTabla(tab_tabla_insumos);
        
        
      
        
        
        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabulador.agregarTab("INSUMOS", pat_panel_grupos);
       
        
        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir1(tab_tabulador);
        agregarComponente(div_division);

        bar_botones.quitarBotonsNavegacion();
    }

    @Override
    public void insertar() {
        if (tab_tabla_insumos.isFocus()) {
            tab_tabla_insumos.insertar();
        } 
    }

    @Override
    public void guardar() {
        tab_tabla_insumos.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_tabla_insumos.isFocus()) {
            tab_tabla_insumos.eliminar();
        } 
    }

    public pf_tabla getTab_tabla_insumos() {
        return tab_tabla_insumos;
    }

    public void setTab_tabla_insumos(pf_tabla tab_tabla_insumos) {
        this.tab_tabla_insumos = tab_tabla_insumos;
    }
    
    
}
