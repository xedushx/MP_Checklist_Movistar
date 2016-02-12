/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.parametros;

import ec.mplus.sistema.sis_pantalla;
import ec.xprime.componentes.pf_arbol;
import ec.xprime.componentes.pf_layout;
import ec.xprime.componentes.pf_panel_tabla;
import ec.xprime.componentes.pf_tabla;
import ec.xprime.sistema.sis_soporte;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class par_tiers extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(par_tiers.class.getName());
    private pf_tabla tab_tabla = new pf_tabla();
    private pf_arbol arb_arbol = new pf_arbol();

    public par_tiers() {

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("mov_tiers", "tie_codigo", 1);
        tab_tabla.setCampoPadre("tie_codigo_2");
        tab_tabla.setCampoNombre("tie_nombre");
        tab_tabla.getColumna("tie_codigo").setNombreVisual("CODIGO");
        tab_tabla.getColumna("tie_nombre").setNombreVisual("DESCRIPCIÓN");
        tab_tabla.getColumna("tie_estado").setNombreVisual("ESTADO");
        tab_tabla.getColumna("tie_relacion_indisponibilidad").setNombreVisual("RELACIÓN INDISPONIBILIDAD");
        tab_tabla.getColumna("tie_criticidad_pendiente").setNombreVisual("CRITICIDAD");
        
        tab_tabla.getColumna("tie_codigo").setOrden(1);
        tab_tabla.getColumna("tie_nombre").setOrden(2);
        tab_tabla.getColumna("tie_relacion_indisponibilidad").setOrden(3);
        tab_tabla.getColumna("tie_estado").setOrden(4);
        tab_tabla.getColumna("tie_criticidad_pendiente").setOrden(5);
        
        tab_tabla.agregarArbol(arb_arbol);
        tab_tabla.dibujar();
        pf_panel_tabla pat_panel1 = new pf_panel_tabla();
        pat_panel1.setPanelTabla(tab_tabla);

        arb_arbol.setId("arb_arbol");
        arb_arbol.dibujar();

        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, pat_panel1, "21%", "V");  //arbol y div3
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        sis_soporte.obtener_instancia_soporte().getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            guardarPantalla();
        }

    }

    @Override
    public void eliminar() {
        sis_soporte.obtener_instancia_soporte().getTablaisFocus().eliminar();
    }

    public pf_arbol getArb_arbol() {
        return arb_arbol;
    }

    public void setArb_arbol(pf_arbol arb_arbol) {
        this.arb_arbol = arb_arbol;
    }

    public pf_tabla getTab_tabla() {
        return tab_tabla;
    }

    public void setTab_tabla(pf_tabla tab_tabla) {
        this.tab_tabla = tab_tabla;
    }
    
}
