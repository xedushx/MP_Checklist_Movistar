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
public class par_proveedores extends sis_pantalla {

    private pf_tabla tab_proveedores = new pf_tabla();

    public par_proveedores() {

        tab_proveedores.setId("tab_proveedores");
        tab_proveedores.setIdCompleto("tab_tabulador:tab_proveedores");
        tab_proveedores.setTabla("mov_proveedores", "prv_codigo", 1);
        tab_proveedores.getColumna("prv_codigo").setNombreVisual("CODIGO");
        tab_proveedores.getColumna("prv_nombre").setNombreVisual("NOMBRE");
        tab_proveedores.getColumna("prv_contacto").setNombreVisual("CONTACTOS");
        tab_proveedores.getColumna("prv_telefonos").setNombreVisual("TELÉFONOS");
        tab_proveedores.getColumna("prv_datos_adicionales").setNombreVisual("DATOS ADICIONALES");
        tab_proveedores.dibujar();

        pf_panel_tabla pat_proveedores = new pf_panel_tabla();
        pat_proveedores.setHeader(new pf_etiqueta("PROVEEDORES"));
        pat_proveedores.setPanelTabla(tab_proveedores);

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabulador.agregarTab("PROVEEDORES", pat_proveedores);

        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir1(tab_tabulador);
        agregarComponente(div_division);

        bar_botones.quitarBotonsNavegacion();
    }

    @Override
    public void insertar() {
        if (tab_proveedores.isFocus()) {
            tab_proveedores.insertar();
        }
    }

    @Override
    public void guardar() {
        tab_proveedores.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        if (tab_proveedores.isFocus()) {
            tab_proveedores.eliminar();
        }
    }

    public pf_tabla getTab_proveedores() {
        return tab_proveedores;
    }

    public void setTab_proveedores(pf_tabla tab_proveedores) {
        this.tab_proveedores = tab_proveedores;
    }
}
