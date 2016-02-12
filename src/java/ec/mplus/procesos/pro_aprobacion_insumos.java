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
public class pro_aprobacion_insumos extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(pro_aprobacion_insumos.class.getName());
    private pf_tabla tab_cab_aprobacion = new pf_tabla();
    private pf_tabla tab_det_aprobacion = new pf_tabla();
    private pf_tabla tab_det_kit = new pf_tabla();
    private pf_combo com_kit = new pf_combo();
    private int iint_id_perfil_aprueba = 6;
    private int iint_id_perfil_aprueba_supervisor = 5;
    pf_boton bot_filtrar = new pf_boton();

    public pro_aprobacion_insumos() {

        com_kit.setCombo("Select * from mov_kits");


        bot_filtrar.setValue("Buscar");
        bot_filtrar.configurar_ActionListener("filtrarFormularios");
        bot_filtrar.setIcon("ui-icon-search");
        bot_filtrar.setUpdate("");


        pf_grid lgri_botones = new pf_grid();
        lgri_botones.setWidth("100%");
        lgri_botones.setColumns(3);

        lgri_botones.getChildren().add(new pf_etiqueta("KIT's"));
        lgri_botones.getChildren().add(com_kit);
        lgri_botones.getChildren().add(bot_filtrar);

        bar_botones.agregarComponente(lgri_botones);
        bar_botones.quitarBotonsNavegacion();

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

        tab_det_aprobacion.setRows(15);

        tab_det_aprobacion.dibujar();

        pf_panel_tabla pat_panel_grupos = new pf_panel_tabla();
        pat_panel_grupos.setPanelTabla(tab_det_aprobacion);


        tab_det_kit.setId("tab_det_kit");
        tab_det_kit.setIdCompleto("tab_tabulador:tab_det_kit");
        //tab_det_kit.setTabla("mov_insumos", "ins_codigo", 2);
        tab_det_kit.setSql("select i.ins_codigo_externo,i.ins_lpu, i.ins_descripcion,"
                + " i.ins_precio_referencial, i.ins_precio_final from mov_insumos i,"
                + " mov_kit_insumo m where m.ins_codigo = i.ins_codigo");
        tab_det_kit.getColumna("ins_codigo_externo").setNombreVisual("CODIGO");
        tab_det_kit.getColumna("ins_lpu").setNombreVisual("LPU");
        tab_det_kit.getColumna("ins_descripcion").setNombreVisual("DESCRIPCION");
        tab_det_kit.getColumna("ins_precio_referencial").setNombreVisual("PRECIO REFERENCIAL");
        tab_det_kit.getColumna("ins_precio_referencial").setAncho(200);
        tab_det_kit.getColumna("ins_precio_final").setNombreVisual("PRECIO FINAL");
        tab_det_kit.ejecutarSql();
        tab_det_kit.dibujar();


        pf_panel_tabla pat_panel_kits = new pf_panel_tabla();
        pat_panel_kits.setPanelTabla(tab_det_kit);


        tab_tabulador.agregarTab("DETALLE DE INSUMOS", pat_panel_grupos);
        tab_tabulador.agregarTab("KITS", pat_panel_kits);

        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, tab_tabulador, "50%", "H");
        agregarComponente(div_division);



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

    public void filtrarFormularios() {
        String lstr_filtro;
        Object kit = com_kit.getValue();

        lstr_filtro = " and m.kit_codigo= '" + kit.toString().trim()+"'";
        try {
            if (!lstr_filtro.isEmpty()) {
                //tab_det_kit.setCondicion(lstr_filtro);
                tab_det_kit.setSql(
                "select i.ins_codigo_externo,i.ins_lpu, i.ins_descripcion,i.uni_codigo,"
                + " i.ins_precio_referencial, i.ins_precio_final from mov_insumos i,"
                + " mov_kit_insumo m where m.ins_codigo = i.ins_codigo "+lstr_filtro);
                tab_det_kit.dibujar();
                System.out.print(kit);
                System.out.println(lstr_filtro);
                tab_det_kit.ejecutarSql();
                bot_filtrar.setUpdate("pat_panel_kits");
                //bot_filtrar.setUpdate("pat_panel_kits");

                sis_soporte.obtener_instancia_soporte().addUpdate("pat_panel_kits,tab_det_kit");

            }
        } catch (Exception e) {
            System.out.println("" + e);
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

    public pf_tabla getTab_det_kit() {
        return tab_det_kit;
    }

    public void setTab_det_kit(pf_tabla tab_det_kit) {
        this.tab_det_kit = tab_det_kit;
    }
}
