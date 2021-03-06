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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author HP
 */
public class pro_aprobacion_insumos4 extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(pro_aprobacion_insumos4.class.getName());
    private pf_tabla tab_cab_aprobacion = new pf_tabla();
    private pf_tabla tab_det_aprobacion = new pf_tabla();
    private pf_tabla tab_bitacora_aprobacion = new pf_tabla();
    private pf_combo com_kit = new pf_combo();
    private List<PerfilAprobacion> perfilesAprobacion = PerfilAprobacion.obtenerPerfilesAprobacion();
    private Integer idUsuarioActual = Integer.valueOf(sis_soporte.obtener_instancia_soporte().obtener_variable("id_usuario"));
    private Integer idPerfilActual = Integer.valueOf(sis_soporte.obtener_instancia_soporte().obtener_variable("id_perfil"));
    private Integer nivelAprobacionActual = Integer.valueOf(sis_soporte.obtener_instancia_soporte().obtener_variable("nivelAprobacionActual"));
    private static final String NOMBRE_CAMPO_APROBACION_GEN = "cas_aprobacion_";
    private static final String NOMBRE_CAMPO_APROBACION_GEN_DET = "das_aprobacion_";
    private Boolean puedeModificar = Boolean.FALSE;
    private Boolean puedeModificarDetalle = Boolean.FALSE;
    private Boolean esPerfilConsulta = Boolean.FALSE;
    private Integer numeroMaximoNivelesAprobacion = Integer.valueOf(sis_soporte.obtener_instancia_soporte().obtener_variable("numeroMaximoNivelesAprobacion"));
    private Integer numeroMinimoNivelesAprobacion = 1;

    public pro_aprobacion_insumos4() {

        pf_boton bot_agregar_kit = new pf_boton();
        bot_agregar_kit.setId("bot_agregar_kit");
        bot_agregar_kit.setValue("Agregar Insumos del Kit");
        bot_agregar_kit.setIcon("ui-icon-document");
        bot_agregar_kit.setMetodo("agregarInsumosKit");

        if (nivelAprobacionActual.intValue() == numeroMaximoNivelesAprobacion) {
            bar_botones.agregarBoton(bot_agregar_kit);

            com_kit.setCombo("Select cki_codigo,cki_descripcion from mov_cabecera_kit where cki_estado = true");
            bar_botones.agregarComponente(com_kit);
        }

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_cab_aprobacion.setId("tab_cab_aprobacion");
        tab_cab_aprobacion.setTabla("mov_cab_aprobacion_solicitudes", "cas_codigo", 1);
        tab_cab_aprobacion.onSelect("seleccionar_tabla1");
        tab_cab_aprobacion.agregarRelacion(tab_det_aprobacion);
        tab_cab_aprobacion.agregarRelacion(tab_bitacora_aprobacion);

        tab_cab_aprobacion.getColumna("cas_codigo").setNombreVisual("CÓDIGO");
        tab_cab_aprobacion.getColumna("id_usuario").setNombreVisual("USUARIO");
        tab_cab_aprobacion.getColumna("ees_codigo").setNombreVisual("ESTACIÓN");
        tab_cab_aprobacion.getColumna("cas_tipo_trabajo").setNombreVisual("TIPO DE TRABAJO");
        tab_cab_aprobacion.getColumna("cas_fecha_trabajo").setNombreVisual("FECHA DE TRABAJO");

        tab_cab_aprobacion.getColumna("cas_fecha_registro").setVisible(false);

        tab_cab_aprobacion.getColumna("cas_fecha_trabajo").setLongitud(30);

        tab_cab_aprobacion.getColumna("id_usuario").setCombo("tbl_usuario", "id_usuario", "nombre_completo", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

        tab_cab_aprobacion.getColumna("ees_codigo").setCombo("mov_estructura_estacion", "ees_codigo", "ees_nombre", "ees_es_estacion = true and id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

        tab_cab_aprobacion.getColumna("id_usuario").setLectura(true);

        tab_cab_aprobacion.setRows(15);

        //tabla de detalle de insumos
        tab_det_aprobacion.setId("tab_det_aprobacion");
        tab_det_aprobacion.setIdCompleto("tab_tabulador:tab_det_aprobacion");
        tab_det_aprobacion.setTabla("mov_det_aprobacion_solicitudes", "das_codigo", 2);
        tab_det_aprobacion.onSelect("seleccionar_tabla2");

        tab_det_aprobacion.getColumna("das_codigo").setNombreVisual("CÓDIGO");
        tab_det_aprobacion.getColumna("ins_codigo").setNombreVisual("INSUMO");
        tab_det_aprobacion.getColumna("uni_codigo").setNombreVisual("UNIDAD");
        tab_det_aprobacion.getColumna("das_precio").setNombreVisual("PRECIO");
        tab_det_aprobacion.getColumna("das_cantidad").setNombreVisual("CANTIDAD");
        tab_det_aprobacion.getColumna("das_precio_total").setNombreVisual("PRECIO TOTAL");

        tab_det_aprobacion.getColumna("ins_codigo").setCombo("mov_insumos", "ins_codigo", "ins_descripcion", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        tab_det_aprobacion.getColumna("uni_codigo").setCombo("mov_unidades", "uni_codigo", "uni_simbologia", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

//        tab_det_aprobacion.getColumna("das_precio").setLectura(true);
        tab_det_aprobacion.getColumna("uni_codigo").setLectura(true);
        tab_det_aprobacion.getColumna("das_precio_total").setLectura(true);

        tab_det_aprobacion.getColumna("das_precio").setMetodoChange("obtenerPrecioTotal");
        tab_det_aprobacion.getColumna("das_cantidad").setMetodoChange("obtenerPrecioTotal");
        tab_det_aprobacion.getColumna("ins_codigo").setMetodoChange("obtenerPrecioUnidad");

        tab_det_aprobacion.setColumnaSuma("das_precio_total");

        // permisos a los campos de aprobaciones
        StringBuilder sbuCondicionAprobacionCabecera = new StringBuilder();
        StringBuilder sbuCondicionAprobacionDetalle = new StringBuilder();

        if (perfilesAprobacion != null && idPerfilActual != null && nivelAprobacionActual.intValue() != 0 && nivelAprobacionActual <= numeroMaximoNivelesAprobacion) {
            int nivel = 1;
            int totalNivelesConfigurados = perfilesAprobacion.size();
            for (int countPerfil = 0; countPerfil < numeroMaximoNivelesAprobacion; countPerfil++) {

                String nombreCampoCabecera = NOMBRE_CAMPO_APROBACION_GEN;
                String nombreCampoDetalle = NOMBRE_CAMPO_APROBACION_GEN_DET;

                if (nivel <= totalNivelesConfigurados) {
                    PerfilAprobacion perfilAprobacion = perfilesAprobacion.get(countPerfil);
                    Integer idPerfilAprobacion = perfilAprobacion.getCodigoPerfilAprobacion();
                    String nombrePerfilAprobacion = perfilAprobacion.getNombrePerfilAprobacion();
                    Integer nivelAprobacion = perfilAprobacion.getNivelAprobacion();

                    tab_cab_aprobacion.getColumna(nombreCampoCabecera + nivel).setEstiloColumna("color:red;");
                    tab_det_aprobacion.getColumna(nombreCampoDetalle + nivel).setEstiloColumna("color:red;");

                    if (nivelAprobacion.intValue() == 1) {
                        tab_cab_aprobacion.getColumna(nombreCampoCabecera + nivel).setNombreVisual("ENVIAR SOLICITUD");
                        tab_det_aprobacion.getColumna(nombreCampoDetalle + nivel).setNombreVisual("ENVIAR SOLICITUD");
                    } else {
                        tab_cab_aprobacion.getColumna(nombreCampoCabecera + nivel).setNombreVisual("APROBAR");
                        tab_det_aprobacion.getColumna(nombreCampoDetalle + nivel).setNombreVisual("APROBAR");
                    }

                    tab_cab_aprobacion.getColumna(nombreCampoCabecera + nivel).setLongitud(40);
                    tab_det_aprobacion.getColumna(nombreCampoDetalle + nivel).setLongitud(40);

                    tab_cab_aprobacion.getColumna(nombreCampoCabecera + nivel).setCombo(PerfilAprobacion.getLISTA_ESTADOS_APROBACION());
                    tab_det_aprobacion.getColumna(nombreCampoDetalle + nivel).setCombo(PerfilAprobacion.getLISTA_ESTADOS_APROBACION());

                    nombreCampoCabecera = nombreCampoCabecera + nivelAprobacion.intValue();
                    nombreCampoDetalle = nombreCampoDetalle + nivelAprobacion.intValue();

                    if (idPerfilActual == idPerfilAprobacion.intValue() && nivelAprobacion.intValue() == nivelAprobacionActual) {

                        tab_cab_aprobacion.getColumna(nombreCampoCabecera).setVisible(true);
                        tab_det_aprobacion.getColumna(nombreCampoDetalle).setVisible(true);

                        tab_cab_aprobacion.getColumna(nombreCampoCabecera).setValorDefecto(PerfilAprobacion.ESTADO_PENDIENTE);
                        tab_det_aprobacion.getColumna(nombreCampoDetalle).setValorDefecto(PerfilAprobacion.ESTADO_PENDIENTE);

                        tab_cab_aprobacion.getColumna(nombreCampoCabecera).setMetodoChange("aprobarDetalles");

                    } else if (nivelAprobacion.intValue() < nivelAprobacionActual) {
                        sbuCondicionAprobacionCabecera.append(" and ").append(nombreCampoCabecera).append(" = '" + PerfilAprobacion.ESTADO_APROBADO + "' ");
                        sbuCondicionAprobacionDetalle.append(" and ").append(nombreCampoDetalle).append(" = '" + PerfilAprobacion.ESTADO_APROBADO + "' ");

                        tab_cab_aprobacion.getColumna(nombreCampoCabecera).setLectura(true);
                        tab_cab_aprobacion.getColumna(nombreCampoCabecera).setVisible(false);

                        tab_det_aprobacion.getColumna(nombreCampoDetalle).setLectura(true);
                        tab_det_aprobacion.getColumna(nombreCampoDetalle).setVisible(false);

                    } else if (nivelAprobacion.intValue() > nivelAprobacionActual) {
//                        sbuCondicionAprobacionCabecera.append(" and ").append(nombreCampoCabecera).append(" = 'false' ");
//                        sbuCondicionAprobacionDetalle.append(" and ").append(nombreCampoDetalle).append(" = 'false' ");

                        tab_cab_aprobacion.getColumna(nombreCampoCabecera).setLectura(true);
                        tab_cab_aprobacion.getColumna(nombreCampoCabecera).setVisible(false);
                        tab_cab_aprobacion.getColumna(nombreCampoCabecera).setValorDefecto(PerfilAprobacion.ESTADO_PENDIENTE);

                        tab_det_aprobacion.getColumna(nombreCampoDetalle).setLectura(true);
                        tab_det_aprobacion.getColumna(nombreCampoDetalle).setVisible(false);
                        tab_det_aprobacion.getColumna(nombreCampoDetalle).setValorDefecto(PerfilAprobacion.ESTADO_PENDIENTE);
                    }

                } else {
                    tab_cab_aprobacion.getColumna(nombreCampoCabecera + nivel).setLectura(true);
                    tab_cab_aprobacion.getColumna(nombreCampoCabecera + nivel).setVisible(false);
                    tab_cab_aprobacion.getColumna(nombreCampoCabecera + nivel).setValorDefecto(PerfilAprobacion.ESTADO_PENDIENTE);

                    tab_det_aprobacion.getColumna(nombreCampoDetalle + nivel).setLectura(true);
                    tab_det_aprobacion.getColumna(nombreCampoDetalle + nivel).setVisible(false);
                    tab_det_aprobacion.getColumna(nombreCampoDetalle + nivel).setValorDefecto(PerfilAprobacion.ESTADO_PENDIENTE);
                }

                nivel++;
            }

        } else {
            esPerfilConsulta = Boolean.TRUE;
            tab_cab_aprobacion.setLectura(true);
            tab_det_aprobacion.setLectura(true);
            bar_botones.quitarBotonsNavegacion();
            bar_botones.quitarBotonInsertar();
            bar_botones.quitarBotonEliminar();
            bar_botones.quitarBotonGuardar();
            sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("PConfiguración de Perfiles",
                    "La configuración de su perfil no le permite realizar cambios en esta pantalla.");
        }

        if (nivelAprobacionActual.intValue() == numeroMinimoNivelesAprobacion) {
            tab_cab_aprobacion.getColumna("ees_codigo").setLectura(false);
            tab_cab_aprobacion.getColumna("cas_tipo_trabajo").setLectura(false);
            tab_cab_aprobacion.getColumna("cas_fecha_trabajo").setLectura(false);

//            tab_det_aprobacion.getColumna("ins_codigo").setLectura(false);

            sbuCondicionAprobacionCabecera.append(" and id_usuario = ").append(idUsuarioActual.intValue());
        } else {
            tab_cab_aprobacion.getColumna("ees_codigo").setLectura(true);
            tab_cab_aprobacion.getColumna("cas_tipo_trabajo").setLectura(true);
            tab_cab_aprobacion.getColumna("cas_fecha_trabajo").setLectura(true);
        }

        tab_cab_aprobacion.setCondicion(" id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa() + sbuCondicionAprobacionCabecera.toString());
        tab_cab_aprobacion.dibujar();

        pf_panel_tabla pat_panel1 = new pf_panel_tabla();
        pat_panel1.setPanelTabla(tab_cab_aprobacion);

        tab_det_aprobacion.setCampoOrden("cas_codigo");
        tab_det_aprobacion.setCondicion(" das_codigo is not null " + sbuCondicionAprobacionDetalle.toString());
        tab_det_aprobacion.dibujar();

        //tabla de bitacora de aprobaciones
        tab_bitacora_aprobacion.setId("tab_bitacora_aprobacion");
        tab_bitacora_aprobacion.setIdCompleto("tab_tabulador:tab_bitacora_aprobacion");
        tab_bitacora_aprobacion.setTabla("mov_bitacora_aprobacion_solicitud", "bas_codigo", 3);

        tab_bitacora_aprobacion.getColumna("id_usuario").setNombreVisual("AUTORIZADO POR");
        tab_bitacora_aprobacion.getColumna("bas_fecha_registro").setNombreVisual("FECHA");
        tab_bitacora_aprobacion.getColumna("bas_estado").setNombreVisual("ESTADO");
        tab_bitacora_aprobacion.getColumna("bas_nivel_aprobacion").setNombreVisual("NIVEL");

        tab_bitacora_aprobacion.getColumna("bas_fecha_registro").setLongitud(70);
        tab_bitacora_aprobacion.getColumna("id_usuario").setLongitud(70);

        tab_bitacora_aprobacion.getColumna("id_usuario").setCombo("tbl_usuario", "id_usuario", "nombre_completo", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

        tab_bitacora_aprobacion.setCampoOrden("bas_fecha_registro desc");

        tab_bitacora_aprobacion.dibujar();

        pf_panel_tabla pat_panel_grupos = new pf_panel_tabla();
        pat_panel_grupos.setPanelTabla(tab_det_aprobacion);

        pf_panel_tabla pat_bitacora = new pf_panel_tabla();
        pat_bitacora.setPanelTabla(tab_bitacora_aprobacion);

        tab_tabulador.agregarTab("DETALLE DE INSUMOS", pat_panel_grupos);
        tab_tabulador.agregarTab("BITÁCORA DE APROBACIÓN DE SOLICITUDES", pat_bitacora);

        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir2(pat_panel1, tab_tabulador, "50%", "H");
        agregarComponente(div_division);

        configurarInicial();
    }

    private void configurarInicial() {
        if (!esPerfilConsulta) {
            for (int i = 0; i < tab_cab_aprobacion.getTotalFilasVisibles(); i++) {
                String estadoAprobacion = tab_cab_aprobacion.getValor(i, NOMBRE_CAMPO_APROBACION_GEN + nivelAprobacionActual);
                Boolean estaAprobada = Boolean.FALSE;

                if (estadoAprobacion.contains(PerfilAprobacion.ESTADO_APROBADO)) {
                    estaAprobada = Boolean.TRUE;
                }

                tab_cab_aprobacion.getFila(i).setLectura(estaAprobada.booleanValue());

                for (int j = 0; j < tab_det_aprobacion.getFilas().size(); j++) {
                    Boolean estaAprobadaDet = Boolean.FALSE;
                    String estadoAprobacionDetalle = tab_det_aprobacion.getValor(j, NOMBRE_CAMPO_APROBACION_GEN_DET + nivelAprobacionActual);

                    if (estadoAprobacionDetalle.contains(PerfilAprobacion.ESTADO_APROBADO)) {
                        estaAprobadaDet = Boolean.TRUE;
                    }

                    tab_det_aprobacion.getFila(j).setLectura(estaAprobadaDet.booleanValue());
                }
            }
        }

        for (int i = 0; i < tab_bitacora_aprobacion.getTotalFilas(); i++) {
            tab_bitacora_aprobacion.getFila(i).setLectura(true);
        }
    }

    private void validarPuedeModificar() {

        if (!tab_cab_aprobacion.isFilaInsertada()) {
            if (!esPerfilConsulta) {

                String estadoAprobacion = tab_cab_aprobacion.getValor(NOMBRE_CAMPO_APROBACION_GEN + nivelAprobacionActual);
                Boolean estaAprobada = Boolean.FALSE;

                if (estadoAprobacion.contains(PerfilAprobacion.ESTADO_APROBADO)) {
                    estaAprobada = Boolean.TRUE;
                }

                tab_cab_aprobacion.getFilaSeleccionada().setLectura(estaAprobada.booleanValue());

                puedeModificar = !estaAprobada;

                for (int j = 0; j < tab_det_aprobacion.getFilas().size(); j++) {
                    Boolean estaAprobadaDet = Boolean.FALSE;
                    String estadoAprobacionDetalle = tab_det_aprobacion.getValor(j, NOMBRE_CAMPO_APROBACION_GEN_DET + nivelAprobacionActual);

                    if (estadoAprobacionDetalle.contains(PerfilAprobacion.ESTADO_APROBADO)) {
                        estaAprobadaDet = Boolean.TRUE;
                    }

                    tab_det_aprobacion.getFila(j).setLectura(estaAprobadaDet.booleanValue());
                }

                for (int i = 0; i < tab_bitacora_aprobacion.getTotalFilas(); i++) {
                    tab_bitacora_aprobacion.getFila(i).setLectura(true);
                }

            }
        }
        
        sis_soporte.obtener_instancia_soporte().addUpdate("tab_cab_aprobacion, bar_botones, tab_tabulador:tab_det_aprobacion");
    }

    private void validarPuedeModificarDetalle() {

        if (!tab_det_aprobacion.isFilaInsertada()) {
            
            if (!esPerfilConsulta) {

                Boolean estaAprobadaDet = Boolean.FALSE;
                String estadoAprobacionDetalle = tab_det_aprobacion.getValor(NOMBRE_CAMPO_APROBACION_GEN_DET + nivelAprobacionActual);

                if (estadoAprobacionDetalle.contains(PerfilAprobacion.ESTADO_APROBADO)) {
                    estaAprobadaDet = Boolean.TRUE;
                }

                tab_det_aprobacion.getFilaSeleccionada().setLectura(estaAprobadaDet.booleanValue());

                puedeModificarDetalle = !estaAprobadaDet;
            }
        } else {
            puedeModificarDetalle = Boolean.TRUE;
            tab_det_aprobacion.getFila(tab_det_aprobacion.getFilaActual()).setLectura(false);
        }
        
        sis_soporte.obtener_instancia_soporte().addUpdate("bar_botones, tab_tabulador:tab_det_aprobacion");

    }

    public void seleccionar_tabla1(AjaxBehaviorEvent evt) {
        tab_cab_aprobacion.seleccionarFila(evt);
        validarPuedeModificar();
    }

    public void seleccionar_tabla2(AjaxBehaviorEvent evt) {
        tab_det_aprobacion.seleccionarFila(evt);
        validarPuedeModificarDetalle();
    }

    public void seleccionar_tabla1(SelectEvent evt) {
        tab_cab_aprobacion.seleccionarFila(evt);
        validarPuedeModificar();
    }

    public void seleccionar_tabla2(SelectEvent evt) {
        tab_det_aprobacion.seleccionarFila(evt);
        validarPuedeModificarDetalle();
    }

    public void aprobarDetalles(AjaxBehaviorEvent evt) {

        tab_cab_aprobacion.modificar(evt);

        String estadoAprobacion = tab_cab_aprobacion.getValor(NOMBRE_CAMPO_APROBACION_GEN + nivelAprobacionActual);

        for (int j = 0; j < tab_det_aprobacion.getTotalFilas(); j++) {
            tab_det_aprobacion.modificar(j);
            for (int i = 1; i <= nivelAprobacionActual; i++) {
                String estadoAprobacionActual = tab_det_aprobacion.getValor(j, NOMBRE_CAMPO_APROBACION_GEN_DET + i);
                if (!estadoAprobacionActual.contains(PerfilAprobacion.ESTADO_APROBADO)) {
                    tab_det_aprobacion.setFilaSeleccionada(tab_det_aprobacion.getFila(j));
                    tab_det_aprobacion.setFilaActual(j);
                    tab_det_aprobacion.setValor(tab_det_aprobacion.getFilaActual(), NOMBRE_CAMPO_APROBACION_GEN_DET + i, estadoAprobacion);
                }
            }
        }

        if (tab_bitacora_aprobacion.isFilaInsertada()) {
            tab_bitacora_aprobacion.actualizar();
        }

        tab_bitacora_aprobacion.insertar();
        tab_bitacora_aprobacion.setValor("id_usuario", String.valueOf(idUsuarioActual));
        tab_bitacora_aprobacion.setValor("bas_fecha_registro", sis_soporte.obtener_instancia_soporte().getFechaHoraActual());
        tab_bitacora_aprobacion.setValor("bas_estado", estadoAprobacion);
        tab_bitacora_aprobacion.setValor("bas_nivel_aprobacion", String.valueOf(nivelAprobacionActual));

        sis_soporte.obtener_instancia_soporte().addUpdate("tab_cab_aprobacion,tab_tabulador:tab_det_aprobacion,tab_tabulador:tab_bitacora_aprobacion");
    }

    public void agregarInsumosKit() {
        try {
            Object objCkiCodigo = com_kit.getValue();
            if (objCkiCodigo != null && puedeModificar) {
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

                        for (int j = 1; j <= numeroMaximoNivelesAprobacion; j++) {
                            tab_det_aprobacion.setValor(NOMBRE_CAMPO_APROBACION_GEN_DET + j, PerfilAprobacion.ESTADO_APROBADO);
                        }

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
        validarPuedeModificar();
    }

    @Override
    public void siguiente() {
        // TODO Auto-generated method stub
        super.siguiente();
        validarPuedeModificar();
    }

    @Override
    public void atras() {
        // TODO Auto-generated method stub
        super.atras();
        validarPuedeModificar();
    }

    @Override
    public void fin() {
        // TODO Auto-generated method stub
        super.fin();
        validarPuedeModificar();
    }

    @Override
    public void actualizar() {
        // TODO Auto-generated method stub
        super.actualizar();
        configurarInicial();
    }

    @Override
    public void aceptarBuscar() {
        // TODO Auto-generated method stub
        super.aceptarBuscar();
        configurarInicial();
    }

    @Override
    public void insertar() {
        if (tab_cab_aprobacion.isFocus() && nivelAprobacionActual == numeroMinimoNivelesAprobacion) {
            if (tab_cab_aprobacion.isFilaInsertada() == false) {
                tab_cab_aprobacion.getColumna("id_usuario").setValorDefecto(String.valueOf(idUsuarioActual.intValue()));
                tab_cab_aprobacion.getColumna("cas_fecha_registro").setValorDefecto(sis_soporte.obtener_instancia_soporte().getFechaActual() + " " + sis_soporte.obtener_instancia_soporte().getHoraActual());
                tab_cab_aprobacion.insertar();
                puedeModificar = Boolean.TRUE;
            } else {
                sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("No se puede Insertar",
                        "Debe guardar la solicitud actual");
            }
        } else if (tab_det_aprobacion.isFocus() && puedeModificar) {
            tab_det_aprobacion.insertar();

            for (int j = 1; j <= numeroMaximoNivelesAprobacion; j++) {
                if (j < nivelAprobacionActual) {
                    tab_det_aprobacion.setValor(NOMBRE_CAMPO_APROBACION_GEN_DET + j, PerfilAprobacion.ESTADO_APROBADO);
                } else {
                    tab_det_aprobacion.setValor(NOMBRE_CAMPO_APROBACION_GEN_DET + j, PerfilAprobacion.ESTADO_PENDIENTE);
                }
            }
        }
//        validarPuedeModificar();

    }

    @Override
    public void guardar() {
        // valida la longitud minima del campo nick si inserto o modifico
        tab_cab_aprobacion.guardar();
        tab_det_aprobacion.guardar();
        tab_bitacora_aprobacion.guardar();
        guardarPantalla();
        configurarInicial();
    }

    @Override
    public void eliminar() {
        if (tab_cab_aprobacion.isFocus() && puedeModificar) {
            tab_cab_aprobacion.eliminar();
        } else if (tab_det_aprobacion.isFocus() && puedeModificarDetalle) {
            tab_det_aprobacion.eliminar();
        }

        configurarInicial();
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

    public pf_tabla getTab_bitacora_aprobacion() {
        return tab_bitacora_aprobacion;
    }

    public void setTab_bitacora_aprobacion(pf_tabla tab_bitacora_aprobacion) {
        this.tab_bitacora_aprobacion = tab_bitacora_aprobacion;
    }
}
