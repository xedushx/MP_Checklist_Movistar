/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.procesos;

import ec.mplus.sistema.sis_pantalla;
import ec.xprime.componentes.*;
import ec.xprime.sistema.sis_soporte;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author HP
 */
public class pro_aprobacion_insumos2 extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(pro_aprobacion_insumos2.class.getName());
    private pf_tabla tab_cab_aprobacion = new pf_tabla();
    private pf_tabla tab_det_aprobacion = new pf_tabla();
    private int iint_id_perfil_aprueba = 6;
    private int iint_id_perfil_aprueba_supervisor = 5;

    public pro_aprobacion_insumos2() {
        
        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_cab_aprobacion.setId("tab_cab_aprobacion");
        tab_cab_aprobacion.setTabla("mov_cab_aprobacion_solicitudes", "cas_codigo", 1);
        tab_cab_aprobacion.agregarRelacion(tab_det_aprobacion);

        tab_cab_aprobacion.getColumna("cas_codigo").setNombreVisual("CÓDIGO");
        tab_cab_aprobacion.getColumna("id_usuario").setNombreVisual("USUARIO");
        tab_cab_aprobacion.getColumna("ees_codigo").setNombreVisual("ESTACIÓN");
        tab_cab_aprobacion.getColumna("cas_tipo_trabajo").setNombreVisual("TIPO DE TRABAJO");
        tab_cab_aprobacion.getColumna("cas_fecha_trabajo").setNombreVisual("FECHA DE TRABAJO");
        tab_cab_aprobacion.getColumna("cas_estado_aprobacion").setNombreVisual("ESTADO");
        tab_cab_aprobacion.getColumna("cas_estado_aprobacion_supervisor").setNombreVisual("ESTADO SUPERVISOR");

        tab_cab_aprobacion.getColumna("cas_fecha_registro").setVisible(false);
        tab_cab_aprobacion.getColumna("cas_fecha_aprobacion_sup").setVisible(false);
        tab_cab_aprobacion.getColumna("cas_fecha_aprobacion").setVisible(false);

        int lint_id_perfil = Integer.parseInt(sis_soporte.obtener_instancia_soporte().obtener_variable("id_perfil"));

        if (lint_id_perfil == iint_id_perfil_aprueba) {
            tab_cab_aprobacion.getColumna("cas_estado_aprobacion").setLectura(false);
            tab_cab_aprobacion.getColumna("cas_estado_aprobacion_supervisor").setLectura(true);

        } else if (lint_id_perfil == iint_id_perfil_aprueba_supervisor) {
            tab_cab_aprobacion.getColumna("cas_estado_aprobacion").setLectura(true);
            tab_cab_aprobacion.getColumna("cas_estado_aprobacion_supervisor").setLectura(false);
        } else {
            tab_cab_aprobacion.getColumna("cas_estado_aprobacion").setLectura(true);
            tab_cab_aprobacion.getColumna("cas_estado_aprobacion_supervisor").setLectura(true);
        }

        tab_cab_aprobacion.getColumna("id_usuario").setCombo("tbl_usuario", "id_usuario", "nombre_completo", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        tab_cab_aprobacion.getColumna("ees_codigo").setCombo("mov_estructura_estacion", "ees_codigo", "ees_nombre", "ees_es_estacion = true and id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

        tab_cab_aprobacion.setRows(15);

        tab_cab_aprobacion.dibujar();

        pf_panel_tabla pat_panel1 = new pf_panel_tabla();
        pat_panel1.setPanelTabla(tab_cab_aprobacion);

        tab_det_aprobacion.setId("tab_det_aprobacion");
        tab_det_aprobacion.setIdCompleto("tab_tabulador:tab_det_aprobacion");
        tab_det_aprobacion.setTabla("mov_det_aprobacion_solicitudes1", "daso_codigo", 2);

        tab_det_aprobacion.getColumna("daso_codigo").setNombreVisual("CÓDIGO");
        tab_det_aprobacion.getColumna("cki_codigo").setNombreVisual("KIT");
        tab_det_aprobacion.getColumna("daso_precio_total").setNombreVisual("PRECIO TOTAL");
        tab_det_aprobacion.getColumna("daso_estado_aprobacion").setNombreVisual("ESTADO");
        tab_det_aprobacion.getColumna("daso_estado_aprobacion_supervisor").setNombreVisual("ESTADO SUPERVISOR");

        tab_det_aprobacion.getColumna("cki_codigo").setCombo("mov_cabecera_kit", "cki_codigo", "cki_descripcion", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa() + " and cki_estado = true");
        
        tab_det_aprobacion.getColumna("daso_fecha_aprobacion_supervisor").setVisible(false);
        tab_det_aprobacion.getColumna("daso_fecha_aprobacion").setVisible(false);

        tab_det_aprobacion.getColumna("daso_precio_total").setLectura(true);

        tab_det_aprobacion.getColumna("cki_codigo").setMetodoChange("obtenerPrecioKit");

        if (lint_id_perfil == iint_id_perfil_aprueba) {
            tab_det_aprobacion.getColumna("daso_estado_aprobacion").setLectura(false);
            tab_det_aprobacion.getColumna("daso_estado_aprobacion_supervisor").setLectura(true);
        } else if (lint_id_perfil == iint_id_perfil_aprueba_supervisor) {
            tab_det_aprobacion.getColumna("daso_estado_aprobacion").setLectura(true);
            tab_det_aprobacion.getColumna("daso_estado_aprobacion_supervisor").setLectura(false);
        } else {
            tab_det_aprobacion.getColumna("daso_estado_aprobacion").setLectura(true);
            tab_det_aprobacion.getColumna("daso_estado_aprobacion_supervisor").setLectura(true);
        }

        tab_det_aprobacion.getColumna("daso_codigo").setOrden(1);
        tab_det_aprobacion.getColumna("cki_codigo").setOrden(2);
        tab_det_aprobacion.getColumna("daso_precio_total").setOrden(3);
        tab_det_aprobacion.getColumna("daso_estado_aprobacion").setOrden(4);
        tab_det_aprobacion.getColumna("daso_estado_aprobacion_supervisor").setOrden(5);
        
        tab_det_aprobacion.setColumnaSuma("daso_precio_total");

        tab_det_aprobacion.setRows(15);

        tab_det_aprobacion.dibujar();

        pf_panel_tabla pat_panel_grupos = new pf_panel_tabla();
        pat_panel_grupos.setPanelTabla(tab_det_aprobacion);
        
        tab_tabulador.agregarTab("KITS UTILIZADOS", pat_panel_grupos);
        
        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, tab_tabulador, "30%", "H");
        agregarComponente(div_division);
        
    }
    
    public void obtenerPrecioKit(AjaxBehaviorEvent evt) {

        try {
            tab_det_aprobacion.modificar(evt);
            double daso_precio_total = 0;
            String lstr_precio = "0";
            String cki_codigo = tab_det_aprobacion.getValor("cki_codigo");

            //obtiene precio
            List lis_consulta = sis_soporte.obtener_instancia_soporte().obtener_conexion().consultar("SELECT ins_codigo, dki_precio_total FROM mov_detalle_kit WHERE cki_codigo = " + cki_codigo);
            if (lis_consulta.size() > 0) {
                Object fila[] = (Object[]) lis_consulta.get(0);
                BigDecimal ldou_dki_precio_total = (BigDecimal) fila[1];

                daso_precio_total += ldou_dki_precio_total.doubleValue();
            }

            lstr_precio = String.valueOf(daso_precio_total);
            
            tab_det_aprobacion.setValor("daso_precio_total", lstr_precio);
            sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabulador:tab_det_aprobacion");
        } catch (Exception e) {
            sis_soporte.obtener_instancia_soporte().agregarMensajeError("ERROR EVENTO", "No se pudo obtener la información del insumo: " + e.getMessage());
        }

    }

    @Override
    public void aceptarReporte() {
    }

    @Override
    public void abrirListaReportes() {
    }

    @Override
    public void inicio() {
        // TODO Auto-generated method stub
        super.inicio();
    }

    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
        super.siguiente();
    }

    @Override
    public void atras() {
        // TODO Auto-generated method stub
        super.atras();
    }

    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
    }

    @Override
    public void actualizar() {
        // TODO Auto-generated method stub
        super.actualizar();
    }

    @Override
    public void aceptarBuscar() {
        // TODO Auto-generated method stub
        super.aceptarBuscar();
    }

    @Override
    public void insertar() {
        if (tab_cab_aprobacion.isFocus()) {
            if (tab_cab_aprobacion.isFilaInsertada() == false) {
                tab_cab_aprobacion.getColumna("cas_fecha_registro").setValorDefecto(sis_soporte.obtener_instancia_soporte().getFechaActual() + " " + sis_soporte.obtener_instancia_soporte().getHoraActual());
                tab_cab_aprobacion.insertar();
            } else {
                sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("No se puede Insertar",
                        "Debe guardar la solicitud actual");
            }
        } else if (tab_det_aprobacion.isFocus()) {
            tab_det_aprobacion.insertar();
        }

    }

    @Override
    public void guardar() {
        // valida la longitud minima del campo nick si inserto o modifico
        tab_cab_aprobacion.guardar();
        tab_det_aprobacion.guardar();
        guardarPantalla();
        actualizar();
    }

    @Override
    public void eliminar() {
        if (tab_cab_aprobacion.isFocus()) {
            tab_cab_aprobacion.eliminar();
        } else if (tab_det_aprobacion.isFocus()) {
            tab_det_aprobacion.eliminar();
        }
    }

    public pf_tabla getTab_cab_aprobacion() {
        return tab_cab_aprobacion;
    }

    public void setTab_cab_aprobacion(pf_tabla tab_cab_aprobacion) {
        this.tab_cab_aprobacion = tab_cab_aprobacion;
    }

    public pf_tabla getTab_det_aprobacion() {
        return tab_det_aprobacion;
    }

    public void setTab_det_aprobacion(pf_tabla tab_det_aprobacion) {
        this.tab_det_aprobacion = tab_det_aprobacion;
    }
}
