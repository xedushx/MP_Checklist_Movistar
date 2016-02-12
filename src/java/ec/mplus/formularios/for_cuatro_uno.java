/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.formularios;

import ec.mplus.sistema.sis_pantalla;
import ec.mplus.utilitarios.Base64;
import ec.xprime.componentes.*;
import ec.xprime.sistema.sis_soporte;
import java.io.*;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.component.column.Column;

/**
 *
 * @author user
 */
public class for_cuatro_uno extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(for_cuatro_uno.class.getName());
    private pf_tabla tab_cabecera = new pf_tabla();
    private pf_tabla tab_detalle = new pf_tabla();
    private pf_tabla tab_equipos_utilizados = new pf_tabla();
    private pf_tabla tab_insumos_utilizados = new pf_tabla();
    private pf_tabla tab_insumos_descartados = new pf_tabla();
    private pf_tabla tab_repuestos = new pf_tabla();
    private pf_tabla tab_fotografias = new pf_tabla();
    private pf_tabla tab_finalizar = new pf_tabla();
    private pf_calendario cal_fecha_elaboracion_i = new pf_calendario();
    private pf_calendario cal_fecha_elaboracion_f = new pf_calendario();
    private pf_combo com_ciudad = new pf_combo();
    private pf_combo com_provincia = new pf_combo();
    private pf_combo com_estacion = new pf_combo();
    private pf_combo com_proveedor = new pf_combo();
    private pf_combo com_grupo_usuario = new pf_combo();
    private pf_combo com_usuario = new pf_combo();
    private pf_combo com_tipo_mantenimiento = new pf_combo();
    private pf_imagen ima_foto = new pf_imagen();
    private pf_grid gri_cuerpo_foto = new pf_grid();
    private pf_dialogo dia_foto = new pf_dialogo();
    private pf_layout div_division = new pf_layout();
   

    public for_cuatro_uno() {

        bar_botones.quitarBotonInsertar();
        bar_botones.quitarBotonEliminar();

        //filtros
        cal_fecha_elaboracion_i.setFechaActual();
        cal_fecha_elaboracion_f.setFechaActual();

        com_ciudad.setId("com_ciudad");
        com_provincia.setId("com_provincia");
        com_estacion.setId("com_estacion");
        com_proveedor.setId("com_proveedor");
        com_grupo_usuario.setId("com_grupo_usuario");
        com_usuario.setId("com_usuario");
        com_tipo_mantenimiento.setId("com_tipo_mantenimiento");

        com_ciudad.setFilter(true);
        com_ciudad.setFilterMatchMode("contains");
        com_provincia.setFilter(true);
        com_provincia.setFilterMatchMode("contains");
        com_estacion.setFilter(true);
        com_estacion.setFilterMatchMode("contains");
        com_proveedor.setFilter(true);
        com_proveedor.setFilterMatchMode("contains");

        com_provincia.setCombo("select ees_codigo, ees_nombre from mov_estructura_estacion where ees_es_provincia = true and ees_estado = true and id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        com_tipo_mantenimiento.setCombo("select tma_codigo, tma_descripcion from mov_tipo_mantenimiento where id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        com_proveedor.setCombo("select prv_codigo, prv_nombre from mov_proveedores where id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

        com_provincia.setMetodo("cargarDDWCiudad");
        com_ciudad.setMetodo("cargarDDWEstacion");
        com_proveedor.setMetodo("cargarDDWGrupoUsuario");
        com_grupo_usuario.setMetodo("cargarDDWUsuario");


        pf_boton bot_filtrar = new pf_boton();
        bot_filtrar.setValue("Buscar");
        bot_filtrar.configurar_ActionListener("filtrarFormularios");
        bot_filtrar.setIcon("ui-icon-search");

//        pf_boton bot_ver_imagen = new pf_boton();
//        bot_ver_imagen.setValue("Ver Imagen");
//        bot_ver_imagen.setTitle("Ver imagen capturada desde el móvil");
//        bot_ver_imagen.setUpdate("div_division,ima_foto,gri_cuerpo_foto");
//        bot_ver_imagen.configurar_ActionListener("actualizarImagen");
//        //bot_ver_imagen.setAjax(false);
//        bot_ver_imagen.setIcon("ui-icon-image");

//        pf_enlace enl_exportar = new pf_enlace();
//        enl_exportar.setId("enl_exportar");
//        enl_exportar.setStyle(bot_ver_imagen.getStyle());

        pf_grid lgri_botones = new pf_grid();
        lgri_botones.setWidth("100%");
        lgri_botones.setColumns(6);

        lgri_botones.getChildren().add(new pf_etiqueta("Provincia:"));
        lgri_botones.getChildren().add(com_provincia);
        lgri_botones.getChildren().add(new pf_etiqueta("Ciudad:"));
        lgri_botones.getChildren().add(com_ciudad);
        lgri_botones.getChildren().add(new pf_etiqueta("Estación:"));
        lgri_botones.getChildren().add(com_estacion);

        pf_grid lgri_botones2 = new pf_grid();
        lgri_botones2.setWidth("100%");
        lgri_botones2.setColumns(7);

        lgri_botones2.getChildren().add(new pf_etiqueta("Proveedor:"));
        lgri_botones2.getChildren().add(com_proveedor);
        lgri_botones2.getChildren().add(new pf_etiqueta("Grupo:"));
        lgri_botones2.getChildren().add(com_grupo_usuario);
        lgri_botones2.getChildren().add(new pf_etiqueta("Técnico:"));
        lgri_botones2.getChildren().add(com_usuario);
        //lgri_botones2.getChildren().add(bot_ver_imagen);

        pf_grid lgri_botones3 = new pf_grid();
        lgri_botones3.setWidth("100%");
        lgri_botones3.setColumns(7);

        lgri_botones3.getChildren().add(new pf_etiqueta("Tipo de Mantenimiento:"));
        lgri_botones3.getChildren().add(com_tipo_mantenimiento);
        lgri_botones3.getChildren().add(new pf_etiqueta("Desde:"));
        lgri_botones3.getChildren().add(cal_fecha_elaboracion_i);
        lgri_botones3.getChildren().add(new pf_etiqueta("Hasta:"));
        lgri_botones3.getChildren().add(cal_fecha_elaboracion_f);
        lgri_botones3.getChildren().add(bot_filtrar);


        bar_botones.agregarComponente(lgri_botones);
//        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(lgri_botones2);
        bar_botones.agregarSeparador();
        bar_botones.agregarComponente(lgri_botones3);

        //tablas
        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_cabecera.setId("tab_cabecera");
        tab_cabecera.setTabla("mov_cab_cuatro_uno", "ccu_codigo", 1);
        tab_cabecera.agregarRelacion(tab_detalle);
        tab_cabecera.agregarRelacion(tab_equipos_utilizados);
        tab_cabecera.agregarRelacion(tab_insumos_utilizados);
        tab_cabecera.agregarRelacion(tab_insumos_descartados);
        tab_cabecera.agregarRelacion(tab_repuestos);
        tab_cabecera.agregarRelacion(tab_fotografias);

        //DDW
        tab_cabecera.getColumna("id_usuario").setCombo("tbl_usuario", "id_usuario", "nombre_completo", "");
        tab_cabecera.getColumna("prv_codigo").setCombo("mov_proveedores", "prv_codigo", "prv_nombre", "");
        tab_cabecera.getColumna("ees_codigo").setCombo("mov_estructura_estacion", "ees_codigo", "ees_nombre", "");
        tab_cabecera.getColumna("tma_codigo").setCombo("mov_tipo_mantenimiento", "tma_codigo", "tma_descripcion", "");
        tab_cabecera.getColumna("eaf_codigo").setCombo("mov_estado_aprobacion_formulario", "eaf_codigo", "eaf_descripcion", "");

        tab_cabecera.getColumna("ccu_codigo").setNombreVisual("CODIGO");
        tab_cabecera.getColumna("id_usuario").setNombreVisual("USUARIO");
        tab_cabecera.getColumna("prv_codigo").setNombreVisual("PROVEEDOR");
        tab_cabecera.getColumna("ees_codigo").setNombreVisual("ESTACION");
        tab_cabecera.getColumna("tma_codigo").setNombreVisual("TIPO MANTENIMIENTO");
        tab_cabecera.getColumna("eaf_codigo").setNombreVisual("APROBACION FORMULARIO");
        tab_cabecera.getColumna("ccu_fecha_elaboracion").setNombreVisual("FECHA ELABORACION");
        tab_cabecera.getColumna("ccu_fecha_sincronizacion").setNombreVisual("FECHA SINCRONIZACION");
        tab_cabecera.getColumna("ccu_fecha_inicio_evento").setNombreVisual("INICIO DEL EVENTO");
        tab_cabecera.getColumna("ccu_fecha_hora_aviso").setNombreVisual("FECHA-HORA AVISO");
        tab_cabecera.getColumna("ccu_fecha_hora_llegada").setNombreVisual("FECHA-HORA LLEGADA");
        tab_cabecera.getColumna("ccu_tiempo_reaccion").setNombreVisual("TIEMPO DE REACCION");
        tab_cabecera.getColumna("ccu_fecha_hora_reparacion").setNombreVisual("FECHA-HORA REPARACION");
        tab_cabecera.getColumna("ccu_soporte_tecnico").setNombreVisual("SOPORTE TECNICO");
        tab_cabecera.getColumna("ccu_solucionado").setNombreVisual("SOLUCIONADO");
        tab_cabecera.getColumna("ccu_tiempo_solucion").setNombreVisual("TIEMPO DE SOLUCION");
        tab_cabecera.getColumna("ccu_operador_noc").setNombreVisual("OPERADOR NOC");
        tab_cabecera.getColumna("ccu_ticket").setNombreVisual("TICKET");
        tab_cabecera.getColumna("ccu_accedio_sitio").setNombreVisual("ACCEDIO AL SITIO");
        tab_cabecera.getColumna("ccu_observaciones").setNombreVisual("OBSERVACIONES");
        tab_cabecera.getColumna("ccu_latitud").setNombreVisual("LATITUD");
        tab_cabecera.getColumna("ccu_longitud").setNombreVisual("LONGITUD");
        tab_cabecera.getColumna("ccu_imei").setNombreVisual("IMEI");
        tab_cabecera.getColumna("ccu_remedy").setNombreVisual("REMEDY");

        tab_cabecera.setRows(20);
        tab_cabecera.setCondicion("ccu_codigo = -1");
        tab_cabecera.dibujar();

        pf_panel_tabla pat_cabecera = new pf_panel_tabla();
        pat_cabecera.setPanelTabla(tab_cabecera);

        tab_detalle.setId("tab_detalle");
        tab_detalle.setIdCompleto("tab_tabulador:tab_detalle");
        tab_detalle.setTabla("mov_det_cuatro_uno", "dcu_codigo", 2);
        tab_detalle.setLectura(true);


        /**
         * Implementacion alex Cambio de nombres a los campos consultados de la
         * base de datos
         */
        tab_detalle.getColumna("dcu_codigo").setNombreVisual("CODIGO DETALLE");
        tab_detalle.getColumna("dcu_codigo").setAncho(5);

        tab_detalle.getColumna("dcu_fecha").setNombreVisual("FECHA DE CAPTURA");
        tab_detalle.getColumna("dcu_hora").setNombreVisual("HORA DE CAPTURA");
        tab_detalle.getColumna("dcu_accion").setNombreVisual("ACCION REALIZADA");
        tab_detalle.getColumna("dcu_detalle").setNombreVisual("DETALLE");
        tab_detalle.getColumna("dcu_foto").setNombreVisual("FOTOGRAFÍA");
        tab_detalle.getColumna("dcu_foto").setVisible(false);
        tab_detalle.getColumna("dcu_usuario").setVisible(false);

        tab_detalle.setRows(6);
        tab_detalle.dibujar();

        Column col_ver_foto = new Column();
        col_ver_foto.setHeaderText("VER FOTO");
        col_ver_foto.setWidth(70);
        pf_link lin_enlace_ver_foto = new pf_link();
        lin_enlace_ver_foto.agregarImagen("/imagenes/im_ver.png", "", "");
        lin_enlace_ver_foto.setMetodo("actualizarImagen");
        lin_enlace_ver_foto.setTitle("Ver foto");
        col_ver_foto.getChildren().add(lin_enlace_ver_foto);
        tab_detalle.getChildren().add(0, col_ver_foto);


        pf_panel_tabla pat_detalle = new pf_panel_tabla();
        pat_detalle.setPanelTabla(tab_detalle);


        tab_equipos_utilizados.setId("tab_equipos_utilizados");
        tab_equipos_utilizados.setIdCompleto("tab_tabulador:tab_equipos_utilizados");
        tab_equipos_utilizados.setTabla("mov_equipos_utilizados_cxu", "eut_codigo", 3);

        tab_equipos_utilizados.getColumna("eut_codigo").setNombreVisual("CODIGO");
        tab_equipos_utilizados.getColumna("eqm_codigo").setNombreVisual("EQUIPO");



        //DDW
        tab_equipos_utilizados.getColumna("eqm_codigo").setCombo("mov_equipos_mantenimiento", "eqm_codigo", "eqm_descripcion", "");

        tab_equipos_utilizados.setRows(20);
        tab_equipos_utilizados.dibujar();

        pf_panel_tabla pat_eu = new pf_panel_tabla();
        pat_eu.setPanelTabla(tab_equipos_utilizados);

        tab_insumos_utilizados.setId("tab_insumos_utilizados");
        tab_insumos_utilizados.setIdCompleto("tab_tabulador:tab_insumos_utilizados");
        tab_insumos_utilizados.setTabla("mov_insumos_utilizados_cxu", "iut_codigo", 4);

        tab_insumos_utilizados.getColumna("iut_codigo").setNombreVisual("CODIGO");
        tab_insumos_utilizados.getColumna("ins_codigo").setNombreVisual("INSUMO");
        tab_insumos_utilizados.getColumna("iut_cantidad").setNombreVisual("CANTIDAD");


        //DDW
        tab_insumos_utilizados.getColumna("ins_codigo").setCombo("mov_insumos", "ins_codigo", "ins_descripcion", "");

        tab_insumos_utilizados.setRows(20);
        tab_insumos_utilizados.dibujar();

        pf_panel_tabla pat_iu = new pf_panel_tabla();
        pat_iu.setPanelTabla(tab_insumos_utilizados);

        tab_insumos_descartados.setId("tab_insumos_descartados");
        tab_insumos_descartados.setIdCompleto("tab_tabulador:tab_insumos_descartados");
        tab_insumos_descartados.setTabla("mov_insumos_descartados_cxu", "ide_codigo", 5);

        tab_insumos_descartados.getColumna("ide_codigo").setNombreVisual("CODIGO");
        tab_insumos_descartados.getColumna("ins_codigo").setNombreVisual("INSUMO");
        tab_insumos_descartados.getColumna("ide_cantidad").setNombreVisual("CANTIDAD");

        //DDW
        tab_insumos_descartados.getColumna("ins_codigo").setCombo("mov_insumos", "ins_codigo", "ins_descripcion", "");

        tab_insumos_descartados.setRows(20);
        tab_insumos_descartados.dibujar();

        pf_panel_tabla pat_id = new pf_panel_tabla();
        pat_id.setPanelTabla(tab_insumos_descartados);

        tab_repuestos.setId("tab_repuestos");
        tab_repuestos.setIdCompleto("tab_tabulador:tab_repuestos");
        tab_repuestos.setTabla("mov_repuestos_cxu", "rep_codigo", 6);

        tab_repuestos.getColumna("rep_codigo").setNombreVisual("CODIGO");
        tab_repuestos.getColumna("rep_equipo_reemplazado").setNombreVisual("EQUIPO REEMPLAZADO");
        tab_repuestos.getColumna("rep_marca").setNombreVisual("MARCA");
        tab_repuestos.getColumna("rep_modelo").setNombreVisual("MODELO");
        tab_repuestos.getColumna("rep_serie_reemplazado").setNombreVisual("SERIE REEMPLAZADO");
        tab_repuestos.getColumna("rep_serie_nuevo").setNombreVisual("SERIE NUEVO");
        tab_repuestos.getColumna("rep_observaciones").setNombreVisual("OBSERVACIONES");

        tab_repuestos.setRows(20);
        tab_repuestos.dibujar();

        pf_panel_tabla pat_re = new pf_panel_tabla();
        pat_re.setPanelTabla(tab_repuestos);

        tab_fotografias.setId("tab_fotografias");
        tab_fotografias.setIdCompleto("tab_tabulador:tab_fotografias");
        tab_fotografias.setTabla("mov_fotografias_cxu", "fot_codigo", 7);

        tab_fotografias.getColumna("fot_codigo").setNombreVisual("CODIGO");
        tab_fotografias.getColumna("fot_descripcion").setNombreVisual("DESCRIPCION");
        tab_fotografias.getColumna("fot_foto").setNombreVisual("FOTOGRAFIA");
        tab_fotografias.getColumna("fot_foto").setVisible(false);

        tab_fotografias.setRows(20);
        tab_fotografias.dibujar();

        pf_panel_tabla pat_fo = new pf_panel_tabla();
        pat_fo.setPanelTabla(tab_fotografias);

        tab_finalizar.setId("tab_finalizar");
        tab_finalizar.setIdCompleto("tab_tabulador:tab_finalizar");
        tab_finalizar.setTabla("mov_finalizar_cxu", "fin_codigo", 8);

        tab_finalizar.getColumna("fin_codigo").setNombreVisual("CODIGO");
        tab_finalizar.getColumna("ccu_codigo").setVisible(false);
        tab_finalizar.getColumna("fin_observacion").setNombreVisual("OBSERVACION");

        tab_finalizar.setRows(20);
        tab_finalizar.dibujar();

        pf_panel_tabla pat_fin = new pf_panel_tabla();
        pat_fin.setPanelTabla(tab_finalizar);
        
        
         div_division.setId("div_division");
       
        div_division.dividir2(pat_cabecera, tab_tabulador, "30%", "H");
        agregarComponente(div_division);

        
        gri_cuerpo_foto.setId("gri_cuerpo_foto");
        gri_cuerpo_foto.setMensajeInfo("Fotografía");
        gri_cuerpo_foto.setStyle("width:100%;overflow: auto;display: block;");
        ima_foto.setId("ima_foto");
        gri_cuerpo_foto.getChildren().add(ima_foto);

        //panel para visualizar foto
        dia_foto.setId("dia_foto");
        dia_foto.setTitle("Fotografía");
        dia_foto.setWidth("60%");
        dia_foto.setHeight("80%");
        dia_foto.getBot_aceptar().setRendered(false);
        dia_foto.setResizable(false);

        dia_foto.setDialogo(gri_cuerpo_foto);
        agregarComponente(dia_foto);

        tab_tabulador.agregarTab("DETALLE", pat_detalle);
        tab_tabulador.agregarTab("EQUIPOS UTILIZADOS", pat_eu);
        tab_tabulador.agregarTab("INSUMOS UTILIZADOS", pat_iu);
        tab_tabulador.agregarTab("INSUMOS DESCARTADOS", pat_id);
        tab_tabulador.agregarTab("REPUESTOS", pat_re);
        tab_tabulador.agregarTab("FOTOGRAFÍAS", pat_fo);
        tab_tabulador.agregarTab("FINALIZAR", pat_fin);

       

    }

    private String getFiltros() {
        String lstr_filtro = "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa();
        return lstr_filtro;
    }

    public void filtrarFormularios() {
        String lstr_filtro = getFiltros();

        if (!lstr_filtro.isEmpty()) {
            tab_cabecera.setCondicion(lstr_filtro);
            tab_cabecera.ejecutarSql();
            sis_soporte.obtener_instancia_soporte().addUpdate("div_division");
        }
    }

    private void limpiarDDW(int aint_tipo) {
        switch (aint_tipo) {
            case 0:
                com_ciudad.setCombo("select ees_codigo, ees_nombre from mov_estructura_estacion where ees_codigo = -1");
                com_estacion.setCombo("select ees_codigo, ees_nombre from mov_estructura_estacion where ees_codigo = -1");
                break;
            case 1:
                com_estacion.setCombo("select ees_codigo, ees_nombre from mov_estructura_estacion where ees_codigo = -1");
                break;
            case 2:
                com_grupo_usuario.setCombo("select gru_codigo, gru_nombre from mov_grupos where prv_codigo = -1");
                com_usuario.setCombo("select id_usuario, nombre_completo from tbl_usuario where id_usuario = -1");
                break;
            case 3:
                com_usuario.setCombo("select id_usuario, nombre_completo from tbl_usuario where id_usuario = -1");
                break;
        }
    }

    public void cargarDDWCiudad() {
        Object lcom_provincia = com_provincia.getValue();
        String lstr_ees_codigo = "";

        if (lcom_provincia == null) {
            lstr_ees_codigo = "";
            limpiarDDW(0);
        } else {
            lstr_ees_codigo = lcom_provincia.toString();
            com_ciudad.setCombo("select ees_codigo, ees_nombre from mov_estructura_estacion where ees_es_ciudad = true and ees_estado = true and ees_codigo_2 = " + lstr_ees_codigo);
            limpiarDDW(1);
        }

        sis_soporte.obtener_instancia_soporte().addUpdate("com_ciudad,com_estacion");
    }

    public void cargarDDWEstacion() {
        Object lcom_ciudad = com_ciudad.getValue();
        String lstr_ees_codigo = "";

        if (lcom_ciudad == null) {
            lstr_ees_codigo = "";
            limpiarDDW(1);
        } else {
            lstr_ees_codigo = lcom_ciudad.toString();
            com_estacion.setCombo("select ees_codigo, ees_nombre from mov_estructura_estacion where ees_es_estacion = true and ees_estado = true and ees_codigo_2 = " + lstr_ees_codigo);
        }

        sis_soporte.obtener_instancia_soporte().addUpdate("com_estacion");
    }

    public void cargarDDWGrupoUsuario() {
        Object lcom_proveedor = com_proveedor.getValue();
        String lstr_prv_codigo = "";

        if (lcom_proveedor == null) {
            lstr_prv_codigo = "";
            limpiarDDW(2);
        } else {
            lstr_prv_codigo = lcom_proveedor.toString();
            com_grupo_usuario.setCombo("select gru_codigo, gru_nombre from mov_grupos where prv_codigo = " + lstr_prv_codigo);
            limpiarDDW(3);
        }

        sis_soporte.obtener_instancia_soporte().addUpdate("com_grupo_usuario,com_usuario");
    }

    public void cargarDDWUsuario() {
        Object lcom_grupo_usuario = com_grupo_usuario.getValue();
        String lstr_gru_codigo = "";

        if (lcom_grupo_usuario == null) {
            lstr_gru_codigo = "";
            limpiarDDW(3);
        } else {
            lstr_gru_codigo = lcom_grupo_usuario.toString();
            com_usuario.setCombo("select usu.id_usuario, usu.nombre_completo from tbl_usuario usu, mov_usuario_grupo ugr where usu.id_usuario = ugr.id_usuario and ugr.gru_codigo = " + lstr_gru_codigo);
        }

        sis_soporte.obtener_instancia_soporte().addUpdate("com_usuario");
    }

    @Override
    public void insertar() {
//        sis_soporte.obtener_instancia_soporte().getTablaisFocus().insertar();
    }

    @Override
    public void guardar() {
        if (tab_cabecera.guardar()) {
            tab_detalle.guardar();
            tab_equipos_utilizados.guardar();
            tab_insumos_utilizados.guardar();
            tab_insumos_descartados.guardar();
            tab_repuestos.guardar();
            tab_fotografias.guardar();
            tab_finalizar.guardar();
            guardarPantalla();
        }
    }

    public void actualizarImagen() {

        String lstr_imagen = tab_detalle.getValor("dcu_foto").trim();


        try {
            gri_cuerpo_foto.setStyle("width:" + (dia_foto.getAnchoPanel() - 10) + "px;height:" + dia_foto.getAltoPanel() + "px;overflow: auto;display: block;");
            if (lstr_imagen == null) {
                ima_foto.setValue("/imagenes/informacion.png");
                sis_soporte.obtener_instancia_soporte().agregarMensajeInfo("Atención!", "La guía seleccionada no posee foto.");
            } else {
                String lstr_nombre_imagen = tab_detalle.getValor("dcu_accion") + "_"
                        + sis_soporte.obtener_instancia_soporte().getFechaActual().replace("-", "")
                        + sis_soporte.obtener_instancia_soporte().getHoraActual().replace(":", "");

                InputStream stream = new ByteArrayInputStream(Base64.decode(lstr_imagen));
                ///Para el .war 
                ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
                String str_path = extContext.getRealPath("/upload/fotos_guia");
                File path = new File(str_path);
                path.mkdirs();//Creo el Directorio

                purgeDirectory(path);
                
                File result1 = new File(str_path + "/" + lstr_nombre_imagen + ".jpg");
                int BUFFER_SIZE = 6124;

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(result1);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bulk;
                    InputStream inputStream = stream;
                    while (true) {
                        bulk = inputStream.read(buffer);
                        if (bulk < 0) {
                            break;
                        }
                        fileOutputStream.write(buffer, 0, bulk);
                        fileOutputStream.flush();
                    }
                    fileOutputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    //Logger.getLogger("Error:  " + e);
                }

                ima_foto.setValue("/upload/fotos_guia/" + lstr_nombre_imagen + ".jpg");
                System.out.println("/upload/fotos_guia/" + lstr_nombre_imagen + ".jpg");
                sis_soporte.obtener_instancia_soporte().addUpdate("div_division,ima_foto,gri_cuerpo_foto");
                dia_foto.dibujar();

            }
        } catch (IOException e) {
            sis_soporte.obtener_instancia_soporte().agregarMensajeError("ERROR!", "No se puedo asignar foto. Detalle: " + e.getMessage());
            System.out.println("Exception error alex"+e);
        }

    }

    private void purgeDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }

    @Override
    public void eliminar() {
        sis_soporte.obtener_instancia_soporte().getTablaisFocus().eliminar();
    }

    public pf_tabla getTab_cabecera() {
        return tab_cabecera;
    }

    public void setTab_cabecera(pf_tabla tab_cabecera) {
        this.tab_cabecera = tab_cabecera;
    }

    public pf_tabla getTab_detalle() {
        return tab_detalle;
    }

    public void setTab_detalle(pf_tabla tab_detalle) {
        this.tab_detalle = tab_detalle;
    }

    public pf_tabla getTab_equipos_utilizados() {
        return tab_equipos_utilizados;
    }

    public void setTab_equipos_utilizados(pf_tabla tab_equipos_utilizados) {
        this.tab_equipos_utilizados = tab_equipos_utilizados;
    }

    public pf_tabla getTab_fotografias() {
        return tab_fotografias;
    }

    public void setTab_fotografias(pf_tabla tab_fotografias) {
        this.tab_fotografias = tab_fotografias;
    }

    public pf_tabla getTab_insumos_descartados() {
        return tab_insumos_descartados;
    }

    public void setTab_insumos_descartados(pf_tabla tab_insumos_descartados) {
        this.tab_insumos_descartados = tab_insumos_descartados;
    }

    public pf_tabla getTab_insumos_utilizados() {
        return tab_insumos_utilizados;
    }

    public void setTab_insumos_utilizados(pf_tabla tab_insumos_utilizados) {
        this.tab_insumos_utilizados = tab_insumos_utilizados;
    }

    public pf_tabla getTab_repuestos() {
        return tab_repuestos;
    }

    public void setTab_repuestos(pf_tabla tab_repuestos) {
        this.tab_repuestos = tab_repuestos;
    }

    public pf_tabla getTab_finalizar() {
        return tab_finalizar;
    }

    public void setTab_finalizar(pf_tabla tab_finalizar) {
        this.tab_finalizar = tab_finalizar;
    }

    public pf_imagen getIma_foto() {
        return ima_foto;
    }

    public void setIma_foto(pf_imagen ima_foto) {
        this.ima_foto = ima_foto;
    }
    public pf_grid getGri_cuerpo_foto() {
        return gri_cuerpo_foto;
    }

    public void setGri_cuerpo_foto(pf_grid gri_cuerpo_foto) {
        this.gri_cuerpo_foto = gri_cuerpo_foto;
    }
     public pf_layout getDiv_division() {
        return div_division;
    }

    public void setDiv_division(pf_layout div_division) {
        this.div_division = div_division;
    }
    
}
