/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.procesos;

import ec.mplus.sistema.sis_pantalla;
import ec.xprime.componentes.pf_layout;
import ec.xprime.componentes.pf_panel_tabla;
import ec.xprime.componentes.pf_tabla;
import ec.xprime.componentes.pf_tabulador;
import ec.xprime.sistema.sis_soporte;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author epacheco
 */
public class pro_creacion_kit extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(pro_creacion_kit.class.getName());
    private pf_layout div_division = new pf_layout();
    private pf_tabla mov_cabecera_kit = new pf_tabla();
    private pf_tabla mov_detalle_kit = new pf_tabla();

    public pro_creacion_kit() {

        mov_cabecera_kit.setId("mov_cabecera_kit");
        mov_cabecera_kit.setTabla("mov_cabecera_kit", "cki_codigo", 1);
        mov_cabecera_kit.agregarRelacion(mov_detalle_kit);

        mov_cabecera_kit.getColumna("cki_codigo").setNombreVisual("CODIGO");
        mov_cabecera_kit.getColumna("cki_fecha_registro").setNombreVisual("FECHA REGISTRO");
        mov_cabecera_kit.getColumna("cki_descripcion").setNombreVisual("DESCRIPCIÓN");
        mov_cabecera_kit.getColumna("cki_estado").setNombreVisual("ESTADO");
        mov_cabecera_kit.getColumna("id_usuario").setNombreVisual("USUARIO");

        mov_cabecera_kit.getColumna("id_usuario").setVisible(false);
        mov_cabecera_kit.getColumna("id_empresa").setVisible(false);
        mov_cabecera_kit.getColumna("cki_fecha_registro").setVisible(false);

        mov_cabecera_kit.getColumna("cki_codigo").setOrden(1);
        mov_cabecera_kit.getColumna("cki_descripcion").setOrden(2);
        mov_cabecera_kit.getColumna("cki_estado").setOrden(3);

        mov_cabecera_kit.setRows(15);
        mov_cabecera_kit.dibujar();

        pf_panel_tabla pat_mov_cabecera_kit = new pf_panel_tabla();
        pat_mov_cabecera_kit.setPanelTabla(mov_cabecera_kit);

        mov_detalle_kit.setId("mov_detalle_kit");
        mov_detalle_kit.setIdCompleto("tab_tabulador:mov_detalle_kit");
        mov_detalle_kit.setTabla("mov_detalle_kit", "dki_codigo", 2);

        mov_detalle_kit.getColumna("dki_codigo").setNombreVisual("CODIGO");
        mov_detalle_kit.getColumna("dki_precio").setNombreVisual("PRECIO UNITARIO");
        mov_detalle_kit.getColumna("dki_cantidad").setNombreVisual("CANTIDAD");
        mov_detalle_kit.getColumna("dki_precio_total").setNombreVisual("PRECIO TOTAL");
        mov_detalle_kit.getColumna("ins_codigo").setNombreVisual("INSUMO");
        mov_detalle_kit.getColumna("uni_codigo").setNombreVisual("UNIDAD");

        mov_detalle_kit.getColumna("dki_precio").setLectura(true);
        mov_detalle_kit.getColumna("uni_codigo").setLectura(true);
        mov_detalle_kit.getColumna("dki_precio_total").setLectura(true);

        mov_detalle_kit.getColumna("dki_codigo").setOrden(1);
        mov_detalle_kit.getColumna("ins_codigo").setOrden(2);
        mov_detalle_kit.getColumna("dki_precio").setOrden(3);
        mov_detalle_kit.getColumna("dki_cantidad").setOrden(4);
        mov_detalle_kit.getColumna("uni_codigo").setOrden(5);
        mov_detalle_kit.getColumna("dki_precio_total").setOrden(6);

        mov_detalle_kit.getColumna("ins_codigo").setCombo("mov_insumos", "ins_codigo", "ins_descripcion", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        mov_detalle_kit.getColumna("uni_codigo").setCombo("mov_unidades", "uni_codigo", "uni_simbologia", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

        mov_detalle_kit.getColumna("dki_precio").setMetodoChange("obtenerPrecioTotal");
        mov_detalle_kit.getColumna("dki_cantidad").setMetodoChange("obtenerPrecioTotal");
        mov_detalle_kit.getColumna("ins_codigo").setMetodoChange("obtenerPrecioUnidad");

        mov_detalle_kit.setColumnaSuma("dki_precio_total");

        mov_detalle_kit.setRows(15);
        mov_detalle_kit.dibujar();

        pf_panel_tabla pat_mov_detalle_kit = new pf_panel_tabla();
        pat_mov_detalle_kit.setPanelTabla(mov_detalle_kit);

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_tabulador.agregarTab("INSUMOS REQUERIDOS", pat_mov_detalle_kit);
        
        div_division.setId("div_division");

        div_division.dividir2(pat_mov_cabecera_kit, tab_tabulador, "30%", "H");
        agregarComponente(div_division);

    }

    /**
     * Calcula automaticamente el precio total del kit cuando el precio unitario
     * o la cantidad son modificados.
     *
     * @param evt
     */
    public void obtenerPrecioTotal(AjaxBehaviorEvent evt) {

        try {
            mov_detalle_kit.modificar(evt);
            String lstr_precio = mov_detalle_kit.getValor("dki_precio");
            String lstr_cantidad = mov_detalle_kit.getValor("dki_cantidad");

            String lstr_precio_final = "0.0";

            if (!lstr_cantidad.isEmpty()) {
                double precio = Double.parseDouble(lstr_precio);
                double cantidad = Double.parseDouble(lstr_cantidad);

                lstr_precio_final = String.valueOf(precio * cantidad);
            }

            mov_detalle_kit.setValor("dki_precio_total", lstr_precio_final);
            sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabulador:mov_detalle_kit");
        } catch (Exception e) {
            sis_soporte.obtener_instancia_soporte().agregarMensajeError("ERROR EVENTO", "No se pudo calcular el precio final: " + e.getMessage());
        }

    }

    /**
     * Obtiene el precio unitario y la unidad de media del insumo cuando es
     * seleccionado del DDW
     *
     * @param evt
     */
    public void obtenerPrecioUnidad(AjaxBehaviorEvent evt) {

        try {
            mov_detalle_kit.modificar(evt);
            String lstr_precio = "0";
            String lstr_uni_codigo = "0";
            String ins_codigo = mov_detalle_kit.getValor("ins_codigo");

            //obtiene precio y unidad
            List lis_consulta = sis_soporte.obtener_instancia_soporte().obtener_conexion().consultar("SELECT uni_codigo, ins_precio_final FROM mov_insumos WHERE ins_codigo = " + ins_codigo);
            if (lis_consulta.size() > 0) {
                Object fila[] = (Object[]) lis_consulta.get(0);
                int lint_uni_codigo = (Integer) fila[0];
                BigDecimal ldou_precio = (BigDecimal) fila[1];

                lstr_precio = "" + ldou_precio;
                lstr_uni_codigo = "" + lint_uni_codigo;
            }

            mov_detalle_kit.setValor("dki_precio", lstr_precio);
            mov_detalle_kit.setValor("uni_codigo", lstr_uni_codigo);
            sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabulador:mov_detalle_kit");
        } catch (Exception e) {
            sis_soporte.obtener_instancia_soporte().agregarMensajeError("ERROR EVENTO", "No se pudo obtener la información del insumo: " + e.getMessage());
        }

    }

    public pf_tabla getMov_cabecera_kit() {
        return mov_cabecera_kit;
    }

    public void setMov_cabecera_kit(pf_tabla mov_cabecera_kit) {
        this.mov_cabecera_kit = mov_cabecera_kit;
    }

    public pf_tabla getMov_detalle_kit() {
        return mov_detalle_kit;
    }

    public void setMov_detalle_kit(pf_tabla mov_detalle_kit) {
        this.mov_detalle_kit = mov_detalle_kit;
    }

    public pf_layout getDiv_division() {
        return div_division;
    }

    public void setDiv_division(pf_layout div_division) {
        this.div_division = div_division;
    }


    @Override
    public void insertar() {
        if (mov_cabecera_kit.isFocus()) {
            if (mov_cabecera_kit.isFilaInsertada() == false) {
                mov_cabecera_kit.getColumna("cki_fecha_registro").setValorDefecto(sis_soporte.obtener_instancia_soporte().getFechaActual() + " " + sis_soporte.obtener_instancia_soporte().getHoraActual());
                mov_cabecera_kit.getColumna("id_usuario").setValorDefecto(sis_soporte.obtener_instancia_soporte().obtener_variable("id_usuario"));
                mov_cabecera_kit.insertar();
            } else {
                sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("No se puede Insertar",
                        "Debe guardar el kit actual");
            }
        } else if (mov_detalle_kit.isFocus()) {
            mov_detalle_kit.insertar();
        }
    }

    @Override
    public void guardar() {
        mov_cabecera_kit.guardar();
        mov_detalle_kit.guardar();
        guardarPantalla();
        actualizar();
    }

    @Override
    public void eliminar() {
        if (mov_cabecera_kit.isFocus()) {
            mov_cabecera_kit.eliminar();
        } else if (mov_detalle_kit.isFocus()) {
            mov_detalle_kit.eliminar();
        }
    }
}
