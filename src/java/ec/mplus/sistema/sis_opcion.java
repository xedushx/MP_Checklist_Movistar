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
 * @author user
 */
public class sis_opcion extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(sis_opcion.class.getName());
    private pf_tabla tab_tabla = new pf_tabla();
    private pf_tabla tab_tabla2 = new pf_tabla();
    private pf_arbol arb_arbol = new pf_arbol();

    public sis_opcion() {
        LOGGER.log(Level.INFO, "sis_opcion cargando");

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabla.setId("tab_tabla");
        tab_tabla.setTabla("tbl_opcion", "id_opcion", 1);
        tab_tabla.setCampoPadre("id_opcion_2");
        tab_tabla.setCampoNombre("nombre");
        tab_tabla.agregarRelacion(tab_tabla2);
        tab_tabla.getColumna("auditada").setCheck();
        tab_tabla.agregarArbol(arb_arbol);
        tab_tabla.dibujar();
        pf_panel_tabla pat_panel1 = new pf_panel_tabla();
        pat_panel1.setPanelTabla(tab_tabla);

        arb_arbol.setId("arb_arbol");
        arb_arbol.dibujar();

        tab_tabla2.setId("tab_tabla2");
        tab_tabla2.setIdCompleto("tab_tabulador:tab_tabla2");
        tab_tabla2.setTabla("tbl_reporte", "id_reporte", 2);
        tab_tabla2.dibujar();
        pf_panel_tabla pat_paneltab2 = new pf_panel_tabla();
        pat_paneltab2.setPanelTabla(tab_tabla2);

        tab_tabulador.agregarTab("REPORTES", pat_paneltab2);

        pf_layout div3 = new pf_layout(); //UNE OPCION Y DIV 2
        div3.dividir2(pat_panel1, tab_tabulador, "60%", "H");
        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(arb_arbol, div3, "21%", "V");  //arbol y div3
        agregarComponente(div_division);

    }

    @Override
    public void insertar() {
        sis_soporte.obtener_instancia_soporte().getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_tabla.guardar()) {
            tab_tabla2.guardar();
        }
        guardarPantalla();
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

    public pf_tabla getTab_tabla2() {
        return tab_tabla2;
    }

    public void setTab_tabla2(pf_tabla tab_tabla2) {
        this.tab_tabla2 = tab_tabla2;
    }
    
}
