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
public class pro_aprobacion_insumos3 extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(pro_aprobacion_insumos3.class.getName());
    private pf_tabla tab_cab_aprobacion = new pf_tabla();
    private pf_tabla tab_det_aprobacion = new pf_tabla();
    private pf_combo com_kit = new pf_combo();
    private int iint_id_perfil_aprueba = 6;
    private int iint_id_perfil_aprueba_supervisor = 5;

    public pro_aprobacion_insumos3() {

        pf_boton bot_agregar_kit = new pf_boton();
        bot_agregar_kit.setId("bot_agregar_kit");
        bot_agregar_kit.setValue("Agregar Insumos del Kit");
        bot_agregar_kit.setIcon("ui-icon-document");
        bot_agregar_kit.setMetodo("agregarInsumosKit");

        bar_botones.agregarBoton(bot_agregar_kit);

        com_kit.setCombo("Select cki_codigo,cki_descripcion from mov_cabecera_kit where cki_estado = true");
        bar_botones.agregarComponente(com_kit);

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
        tab_det_aprobacion.setTabla("mov_det_aprobacion_solicitudes", "das_codigo", 2);

        tab_det_aprobacion.getColumna("das_codigo").setNombreVisual("CÓDIGO");
        tab_det_aprobacion.getColumna("ins_codigo").setNombreVisual("INSUMO");
        tab_det_aprobacion.getColumna("uni_codigo").setNombreVisual("UNIDAD");
        tab_det_aprobacion.getColumna("das_precio").setNombreVisual("PRECIO");
        tab_det_aprobacion.getColumna("das_cantidad").setNombreVisual("CANTIDAD");
        tab_det_aprobacion.getColumna("das_precio_total").setNombreVisual("PRECIO TOTAL");
        tab_det_aprobacion.getColumna("das_estado_aprobacion").setNombreVisual("ESTADO");
        tab_det_aprobacion.getColumna("das_estado_aprobacion_supervisor").setNombreVisual("ESTADO SUPERVISOR");

        tab_det_aprobacion.getColumna("ins_codigo").setCombo("mov_insumos", "ins_codigo", "ins_descripcion", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        tab_det_aprobacion.getColumna("uni_codigo").setCombo("mov_unidades", "uni_codigo", "uni_simbologia", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

        tab_det_aprobacion.getColumna("das_fecha_aprobacion_supervisor").setVisible(false);
        tab_det_aprobacion.getColumna("das_fecha_aprobacion").setVisible(false);

//        tab_det_aprobacion.getColumna("das_precio").setLectura(true);
        tab_det_aprobacion.getColumna("uni_codigo").setLectura(true);
        tab_det_aprobacion.getColumna("das_precio_total").setLectura(true);

        tab_det_aprobacion.getColumna("das_precio").setMetodoChange("obtenerPrecioTotal");
        tab_det_aprobacion.getColumna("das_cantidad").setMetodoChange("obtenerPrecioTotal");
        tab_det_aprobacion.getColumna("ins_codigo").setMetodoChange("obtenerPrecioUnidad");

        if (lint_id_perfil == iint_id_perfil_aprueba) {
            tab_det_aprobacion.getColumna("das_estado_aprobacion").setLectura(false);
            tab_det_aprobacion.getColumna("das_estado_aprobacion_supervisor").setLectura(true);
        } else if (lint_id_perfil == iint_id_perfil_aprueba_supervisor) {
            tab_det_aprobacion.getColumna("das_estado_aprobacion").setLectura(true);
            tab_det_aprobacion.getColumna("das_estado_aprobacion_supervisor").setLectura(false);
        } else {
            tab_det_aprobacion.getColumna("das_estado_aprobacion").setLectura(true);
            tab_det_aprobacion.getColumna("das_estado_aprobacion_supervisor").setLectura(true);
        }

        tab_det_aprobacion.setColumnaSuma("das_precio_total");

//        tab_det_aprobacion.setRows(15);

        tab_det_aprobacion.dibujar();

        pf_panel_tabla pat_panel_grupos = new pf_panel_tabla();
        pat_panel_grupos.setPanelTabla(tab_det_aprobacion);

        tab_tabulador.agregarTab("DETALLE DE INSUMOS", pat_panel_grupos);

        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, tab_tabulador, "50%", "H");
        agregarComponente(div_division);
    }

    public void agregarInsumosKit() {
        try {
            Object objCkiCodigo = com_kit.getValue();
            if (objCkiCodigo != null) {
                String cki_codigo = (String) objCkiCodigo;

                pf_tabla mov_detalle_kit = sis_soporte.obtener_instancia_soporte().consultarTabla("Select * from mov_detalle_kit where cki_codigo = " + cki_codigo);

                int totalFilas = mov_detalle_kit.getTotalFilas();

                if (totalFilas > 0) {

                    for (int i = 0; i < totalFilas; i++) {
                        tab_det_aprobacion.insertar();

                        tab_det_aprobacion.setValor("das_precio", mov_detalle_kit.getValor(i, "dki_precio"));
                        tab_det_aprobacion.setValor("das_cantidad", mov_detalle_kit.getValor(i, "dki_cantidad"));
                        tab_det_aprobacion.setValor("das_precio_total", mov_detalle_kit.getValor(i, "dki_precio_total"));
                        tab_det_aprobacion.setValor("ins_codigo", mov_detalle_kit.getValor(i, "ins_codigo"));
                        tab_det_aprobacion.setValor("uni_codigo", mov_detalle_kit.getValor(i, "uni_codigo"));
                    }

                    sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabulador:tab_det_aprobacion");

                } else {
                    sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("Atención", "No se encontraron detalles para el Kit seleccionado.");
                }


            } else {
                sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("Atención", "Debe seleccionar un Kit");
            }


        } catch (Exception e) {
            sis_soporte.obtener_instancia_soporte().agregarMensajeError("Error al agregar Insumos del Kit", "Detalle: " + e.getMessage());
        }
    }

    public void obtenerPrecioTotal(AjaxBehaviorEvent evt) {

        try {
            tab_det_aprobacion.modificar(evt);
            String lstr_precio = tab_det_aprobacion.getValor("das_precio");
            String lstr_cantidad = tab_det_aprobacion.getValor("das_cantidad");

            String lstr_precio_final = "0.0";

            if (!lstr_cantidad.isEmpty()) {
                double precio = Double.parseDouble(lstr_precio);
                double cantidad = Double.parseDouble(lstr_cantidad);

                lstr_precio_final = String.valueOf(precio * cantidad);
            }

            tab_det_aprobacion.setValor("das_precio_total", lstr_precio_final);
            sis_soporte.obtener_instancia_soporte().addUpdate("tab_tabulador:tab_det_aprobacion");
        } catch (Exception e) {
            sis_soporte.obtener_instancia_soporte().agregarMensajeError("ERROR EVENTO", "No se pudo calcular el precio final: " + e.getMessage());
        }

    }

    public void obtenerPrecioUnidad(AjaxBehaviorEvent evt) {

        try {
            tab_det_aprobacion.modificar(evt);
            String lstr_precio = "0";
            String lstr_uni_codigo = "0";
            String ins_codigo = tab_det_aprobacion.getValor("ins_codigo");

            //obtiene precio y unidad
            List lis_consulta = sis_soporte.obtener_instancia_soporte().obtener_conexion().consultar("SELECT uni_codigo, ins_precio_final FROM mov_insumos WHERE ins_codigo = " + ins_codigo);
            if (lis_consulta.size() > 0) {
                Object fila[] = (Object[]) lis_consulta.get(0);
                int lint_uni_codigo = (Integer) fila[0];
                BigDecimal ldou_precio = (BigDecimal) fila[1];

                lstr_precio = "" + ldou_precio;
                lstr_uni_codigo = "" + lint_uni_codigo;
            }

            tab_det_aprobacion.setValor("das_precio", lstr_precio);
            tab_det_aprobacion.setValor("uni_codigo", lstr_uni_codigo);
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
                tab_det_aprobacion.actualizar();
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
        String usuario = tab_cab_aprobacion.getValor("id_usuario").trim();
        String estacion = tab_cab_aprobacion.getValor("ees_codigo").trim();
        String trabajo = tab_cab_aprobacion.getValor("cas_tipo_trabajo").trim();
        String fecha = tab_cab_aprobacion.getValor("cas_fecha_trabajo").trim();
        try {
            if (!usuario.isEmpty() && !estacion.isEmpty() && !trabajo.isEmpty() && !fecha.isEmpty()) {
                tab_cab_aprobacion.guardar();
                tab_det_aprobacion.guardar();
                guardarPantalla();
            } else {
                sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("Validación!", "Ingrese información valida");
            }
        } catch (Exception e) {
            sis_soporte.obtener_instancia_soporte().agregarMensajeError("ERROR!", "Error al almacenar los datos, verifique la información ingresada");

        }
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

    public pf_combo getCom_kit() {
        return com_kit;
    }

    public void setCom_kit(pf_combo com_kit) {
        this.com_kit = com_kit;
    }
}
