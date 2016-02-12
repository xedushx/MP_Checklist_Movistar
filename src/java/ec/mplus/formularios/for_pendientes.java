/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.formularios;

import ec.mplus.sistema.sis_pantalla;
import ec.xprime.componentes.pf_boton;
import ec.xprime.componentes.pf_dialogo;
import ec.xprime.componentes.pf_entrada_texto;
import ec.xprime.componentes.pf_etiqueta;
import ec.xprime.componentes.pf_grid;
import ec.xprime.componentes.pf_layout;
import ec.xprime.componentes.pf_panel_tabla;
import ec.xprime.componentes.pf_subir_archivo;
import ec.xprime.componentes.pf_tabla;
import ec.xprime.sistema.sis_soporte;
import jxl.Sheet;
import jxl.Workbook;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author epacheco
 */
public class for_pendientes extends sis_pantalla {

    private pf_tabla mov_pendientes = new pf_tabla();
    private pf_tabla mov_pendientes_tmp = new pf_tabla();
    private pf_layout div_division = new pf_layout();
    private pf_dialogo dia_carga_masiva = new pf_dialogo();
    private pf_grid gri_cuerpo = new pf_grid();
    private pf_grid gri_datos = new pf_grid();
    private pf_entrada_texto tex_columna = new pf_entrada_texto();
    private pf_entrada_texto tex_inicio = new pf_entrada_texto();
    private pf_entrada_texto tex_fin = new pf_entrada_texto();
    private pf_entrada_texto tex_numero_hoja = new pf_entrada_texto();
    private pf_subir_archivo upl_importarxls = new pf_subir_archivo();
    private int int_inicio = 0;
    private int int_fin = -1;
    private int int_columna = 0;
    private int int_numero_hoja = -1;
    private Sheet hoja = null;

    public for_pendientes() {

        pf_boton bot_cargar_archivo = new pf_boton();
        bot_cargar_archivo.setId("bot_cargar_archivo");
        bot_cargar_archivo.setValue("Cargar Pendientes desde Archivo Excel");
        bot_cargar_archivo.setIcon("ui-icon-document");
        bot_cargar_archivo.setMetodo("desplegarCargaArchivo");

        bar_botones.agregarBoton(bot_cargar_archivo);

        mov_pendientes.setId("mov_pendientes");
        mov_pendientes.setTabla("mov_pendiente", "id", 1);

        mov_pendientes.setRows(15);
        mov_pendientes.dibujar();

        pf_panel_tabla pat_mov_pendientes = new pf_panel_tabla();
        pat_mov_pendientes.setId("pat_mov_pendientes");
        pat_mov_pendientes.setPanelTabla(mov_pendientes);

        div_division.setId("div_division");
        div_division.dividir1(pat_mov_pendientes);
        agregarComponente(div_division);

        //panel para crear pendientes desde archivo
        dia_carga_masiva.setId("dia_carga_masiva");
        dia_carga_masiva.setTitle("Importar Pendientes");
        dia_carga_masiva.setWidth("90%");
        dia_carga_masiva.setHeight("90%");
        dia_carga_masiva.getBot_aceptar().setMetodo("aceptarCargaArchivo");
        dia_carga_masiva.getBot_aceptar().setAjax(false);
        dia_carga_masiva.setResizable(false);

        gri_cuerpo.setMensajeInfo("Configuración del Archivo xls");
        gri_datos.setStyle("width:" + (dia_carga_masiva.getAnchoPanel() - 20) + "px;");
        pf_grid gri_archivo = new pf_grid();
        gri_archivo.setWidth("80%");
        gri_archivo.setColumns(3);

        pf_grid gri_matriz = new pf_grid();
        gri_matriz.setStyle("width:" + (dia_carga_masiva.getAnchoPanel() - 40) + "px;");
        gri_matriz.setColumns(4);

        tex_columna.setValue(int_columna);
        tex_inicio.setValue(int_inicio);
        tex_fin.setValue(int_fin);
        tex_numero_hoja.setValue(int_numero_hoja);
        
        gri_matriz.getChildren().add(new pf_etiqueta("Fila Nombres de Campos"));
        tex_columna.setSize(5);
        tex_columna.setSoloEnteros();
        gri_matriz.getChildren().add(tex_columna);
        gri_matriz.getChildren().add(new pf_etiqueta("Primera Fila de Datos"));
        tex_inicio.setSize(5);
        tex_inicio.setSoloEnteros();
        gri_matriz.getChildren().add(tex_inicio);
        gri_matriz.getChildren().add(new pf_etiqueta("Última Fila de Datos"));
        tex_fin.setSize(5);
        tex_fin.setSoloEnteros();

        gri_matriz.getChildren().add(tex_fin);

        gri_matriz.getChildren().add(new pf_etiqueta("Número de la Hoja"));
        tex_numero_hoja.setSoloEnteros();
        gri_matriz.getChildren().add(tex_numero_hoja);

        pf_boton bot_configurar = new pf_boton();
        bot_configurar.setValue("Configurar Parámetros");
        bot_configurar.setIcon("ui-icon-gear");
        bot_configurar.setMetodo("configurarParametros");
        
        gri_matriz.getChildren().add(bot_configurar);
        
        gri_archivo.getChildren().add(gri_matriz);

        upl_importarxls.setId("upl_importarxls");
        upl_importarxls.setAllowTypes("/(\\.|\\/)(xls)$/");
        upl_importarxls.setMetodo("validar");
        upl_importarxls.setUploadLabel("Validar");
        upl_importarxls.setAuto(false);
        
        gri_matriz.setFooter(upl_importarxls);

        gri_datos.getChildren().add(gri_archivo);

        gri_cuerpo.setId("gri_cuerpo_importar2");
        gri_cuerpo.setStyle("width:" + (dia_carga_masiva.getAnchoPanel() - 10) + "px;height:" + dia_carga_masiva.getAltoPanel() + "px;overflow: auto;display: block;");
        
        dia_carga_masiva.setDialogo(gri_cuerpo);
        agregarComponente(dia_carga_masiva);
        
    }
    
    public void configurarParametros(){
        
        if (tex_columna.getValue() != null){
            int_columna = Integer.parseInt(tex_columna.getValue().toString());
        }
        
        if (tex_inicio.getValue() != null){
            int_inicio = Integer.parseInt(tex_inicio.getValue().toString());
        }
        
        if (tex_fin.getValue() != null && !tex_fin.getValue().toString().isEmpty()){
            int_fin = Integer.parseInt(tex_fin.getValue().toString());
        }
        
        if (tex_numero_hoja.getValue() != null){
            int_numero_hoja = Integer.parseInt(tex_numero_hoja.getValue().toString());
        }
        
    }

    public void desplegarCargaArchivo() {
        upl_importarxls.limpiar();
        gri_cuerpo.getChildren().clear();
        gri_cuerpo.getChildren().add(gri_datos);

        //construye la tabla preview
        //hace un preview de los datos a importar
        mov_pendientes_tmp = new pf_tabla();
        mov_pendientes_tmp.setId("mov_pendientes_tmp");
        mov_pendientes_tmp.setTabla("mov_pendiente", "id", 2);
        mov_pendientes_tmp.setCondicion("id = -1");
        mov_pendientes_tmp.setRows(10);
        mov_pendientes_tmp.dibujar();

        dia_carga_masiva.dibujar();
    }

    public void validar(FileUploadEvent event) {
        try {
            Workbook archivoExcel = Workbook.getWorkbook(event.getFile().getInputstream());

//            //Abro la hoja indicada
//            int int_numero_hoja = -1;
//            try {
//                int_numero_hoja = Integer.parseInt(tex_numero_hoja.getValue() + "");
//            } catch (Exception e) {
//                int_numero_hoja = -1;
//            }

            if (int_numero_hoja == -1) {
                String[] hojas = archivoExcel.getSheetNames();
                for (int i = 0; hojas.length < 10; i++) {
                    if (hojas[i].equalsIgnoreCase(tex_numero_hoja.getValue() + "")) {
                        hoja = archivoExcel.getSheet(i);
                        break;
                    }
                }
            } else {
                hoja = archivoExcel.getSheet(int_numero_hoja);
            }

            if (hoja == null) {
                sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("La Hoja: " + tex_numero_hoja.getValue() + " no existe en el Archivo seleccionado", "");
                return;
            }

//            try {
//                int_inicio = Integer.parseInt(tex_inicio.getValue() + "");
//                int_inicio = int_inicio - 1;
//            } catch (Exception e) {
//                int_inicio = -1;
//            }
//
//            try {
//                int_fin = Integer.parseInt(tex_fin.getValue() + "");
//            } catch (Exception e) {
//                int_fin = -1;
//            }

//            if (tex_fin.getValue() == null || tex_fin.getValue().toString().isEmpty()) {
//                //Si no hay valor en fin le pongo a el total de filas de la Hoja
//                int_fin = hoja.getRows();
//            }
            
            if (int_fin == -1) {
                //Si no hay valor en fin le pongo a el total de filas de la Hoja
                int_fin = hoja.getRows();
            }

            if ((int_inicio == -1 || int_fin == -1) || (int_inicio > int_fin)) {
                sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("Los valores ingresados en Primera o Última fila de datos no son válidos", "");
                return;
            }

//            int int_fila_columna = -1;
//
//            try {
//                int_fila_columna = Integer.parseInt(tex_columna.getValue() + "");
//                int_fila_columna = int_fila_columna - 1;
//            } catch (Exception e) {
//                int_fila_columna = -1;
//            }
//
//            if (int_fila_columna == -1) {
//                sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("El valor de Fila Nombres de Campos no es válido", "");
//                return;
//            }

            gri_cuerpo.getChildren().clear();
            gri_cuerpo.getChildren().add(gri_datos);

            //carga los datos del excel a la tabla mov_pendientes_tmp
            mov_pendientes_tmp.limpiar();

            //llenar la tabla preview        
            for (int j = int_inicio; j < int_fin; j++) {
                try {
                    String estacion_id = obtenerIdEstacion(hoja.getCell(1, j).getContents());

                    if (!estacion_id.equals("-1")) {
                        String tipo_estacion = hoja.getCell(2, j).getContents();
                        String fecha = sis_soporte.obtener_instancia_soporte().obtener_formato_fecha(hoja.getCell(6, j).getContents());
                        String semana = hoja.getCell(7, j).getContents();
                        String ticket = hoja.getCell(8, j).getContents();
                        String provincia = hoja.getCell(10, j).getContents();
                        String region = hoja.getCell(11, j).getContents();
                        String partner = hoja.getCell(12, j).getContents();
                        String grupo = hoja.getCell(13, j).getContents();
                        String seccion = hoja.getCell(14, j).getContents();
                        String tier1 = hoja.getCell(16, j).getContents();
                        String tier2 = hoja.getCell(17, j).getContents();
                        String tier3 = hoja.getCell(18, j).getContents();
                        String criticidad_pendiente = hoja.getCell(19, j).getContents();
                        String tecnico_responsable = hoja.getCell(20, j).getContents();
                        String responsable = hoja.getCell(21, j).getContents();
                        String fecha_cierre_pendiente = sis_soporte.obtener_instancia_soporte().obtener_formato_fecha(hoja.getCell(22, j).getContents());
                        String observacion1 = hoja.getCell(15, j).getContents();
                        String supervisor_huawei = hoja.getCell(24, j).getContents();
                        String fecha_mp = sis_soporte.obtener_instancia_soporte().obtener_fecha_actual();
                        String mes_cerrado = "";
                        String pend_obser = hoja.getCell(25, j).getContents();
                        String estatus = hoja.getCell(26, j).getContents();
                        String indisponibilidad = hoja.getCell(28, j).getContents();
                        String estatus_pendiente = "1";

                        mov_pendientes_tmp.insertar();

                        mov_pendientes_tmp.setValor("estacion_id", estacion_id);
                        mov_pendientes_tmp.setValor("tipo_estacion", tipo_estacion);
                        mov_pendientes_tmp.setValor("fecha", fecha);
                        mov_pendientes_tmp.setValor("semana", semana);
                        mov_pendientes_tmp.setValor("ticket", ticket);
                        mov_pendientes_tmp.setValor("provincia", provincia);
                        mov_pendientes_tmp.setValor("region", region);
                        mov_pendientes_tmp.setValor("partner", partner);
                        mov_pendientes_tmp.setValor("grupo", grupo);
                        mov_pendientes_tmp.setValor("seccion", seccion);
                        mov_pendientes_tmp.setValor("tier1", tier1);
                        mov_pendientes_tmp.setValor("tier2", tier2);
                        mov_pendientes_tmp.setValor("tier3", tier3);
                        mov_pendientes_tmp.setValor("criticidad_pendiente", criticidad_pendiente);
                        mov_pendientes_tmp.setValor("tecnico_responsable", tecnico_responsable);
                        mov_pendientes_tmp.setValor("responsable", responsable);
                        mov_pendientes_tmp.setValor("fecha_cierre_pendiente", fecha_cierre_pendiente);
                        mov_pendientes_tmp.setValor("observacion1", observacion1);
                        mov_pendientes_tmp.setValor("supervisor_huawei", supervisor_huawei);
                        mov_pendientes_tmp.setValor("fecha_mp", fecha_mp);
                        mov_pendientes_tmp.setValor("mes_cerrado", mes_cerrado);
                        mov_pendientes_tmp.setValor("pend_obser", pend_obser);
                        mov_pendientes_tmp.setValor("estatus", estatus);
                        mov_pendientes_tmp.setValor("indisponibilidad", indisponibilidad);
                        mov_pendientes_tmp.setValor("estatus_pendiente", estatus_pendiente);
                    }

                } catch (Exception e) {
                    sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("Fallo al leer columnas del archivo xls :" + e.getMessage(), "");
                    break;
                }
            }
            
            gri_cuerpo.getChildren().add(mov_pendientes_tmp);
            upl_importarxls.setNombreReal(event.getFile().getFileName());
            archivoExcel.close();
            
            sis_soporte.obtener_instancia_soporte().addUpdate("gri_cuerpo_importar2");

        } catch (Exception e) {
            sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("No se puede validar el archivo", e.getMessage());
        }
    }

    private String obtenerIdEstacion(String strNombreEstacion) {
        String strId = "1";
        try {

            pf_tabla mov_estructura_estacion = sis_soporte.obtener_instancia_soporte().consultarTabla("select * from mov_estructura_estacion where ees_nombre like '%" + strNombreEstacion + "%';");

            int total_filas = mov_estructura_estacion.getTotalFilas();
            if (total_filas > 0) {
                strId = mov_estructura_estacion.getValor("ees_codigo");
            } else {
                strId = "-1";
            }
        } catch (Exception e) {
            System.out.println("ERROR obtenerIdEstacion: " + e.getMessage());
        }

        return strId;
    }

    public void aceptarCargaArchivo() {
        try {

            int totalFilas = mov_pendientes_tmp.getTotalFilas();

            if (totalFilas > 0) {

                for (int i = 0; i < totalFilas; i++) {
                    mov_pendientes.insertar();

                    mov_pendientes.setValor("estacion_id", mov_pendientes_tmp.getValor(i, "estacion_id"));
                    mov_pendientes.setValor("tipo_estacion", mov_pendientes_tmp.getValor(i, "tipo_estacion"));
                    mov_pendientes.setValor("fecha", mov_pendientes_tmp.getValor(i, "fecha"));
                    mov_pendientes.setValor("semana", mov_pendientes_tmp.getValor(i, "semana"));
                    mov_pendientes.setValor("ticket", mov_pendientes_tmp.getValor(i, "ticket"));
                    mov_pendientes.setValor("provincia", mov_pendientes_tmp.getValor(i, "provincia"));
                    mov_pendientes.setValor("region", mov_pendientes_tmp.getValor(i, "region"));
                    mov_pendientes.setValor("partner", mov_pendientes_tmp.getValor(i, "partner"));
                    mov_pendientes.setValor("grupo", mov_pendientes_tmp.getValor(i, "grupo"));
                    mov_pendientes.setValor("seccion", mov_pendientes_tmp.getValor(i, "seccion"));
                    mov_pendientes.setValor("tier1", mov_pendientes_tmp.getValor(i, "tier1"));
                    mov_pendientes.setValor("tier2", mov_pendientes_tmp.getValor(i, "tier2"));
                    mov_pendientes.setValor("tier3", mov_pendientes_tmp.getValor(i, "tier3"));
                    mov_pendientes.setValor("criticidad_pendiente", mov_pendientes_tmp.getValor(i, "criticidad_pendiente"));
                    mov_pendientes.setValor("tecnico_responsable", mov_pendientes_tmp.getValor(i, "tecnico_responsable"));
                    mov_pendientes.setValor("responsable", mov_pendientes_tmp.getValor(i, "responsable"));
                    mov_pendientes.setValor("fecha_cierre_pendiente", mov_pendientes_tmp.getValor(i, "fecha_cierre_pendiente"));
                    mov_pendientes.setValor("observacion1", mov_pendientes_tmp.getValor(i, "observacion1"));
                    mov_pendientes.setValor("supervisor_huawei", mov_pendientes_tmp.getValor(i, "supervisor_huawei"));
                    mov_pendientes.setValor("fecha_mp", mov_pendientes_tmp.getValor(i, "fecha_mp"));
                    mov_pendientes.setValor("mes_cerrado", mov_pendientes_tmp.getValor(i, "mes_cerrado"));
                    mov_pendientes.setValor("pend_obser", mov_pendientes_tmp.getValor(i, "pend_obser"));
                    mov_pendientes.setValor("estatus", mov_pendientes_tmp.getValor(i, "estatus"));
                    mov_pendientes.setValor("indisponibilidad", mov_pendientes_tmp.getValor(i, "indisponibilidad"));
                    mov_pendientes.setValor("estatus_pendiente", mov_pendientes_tmp.getValor(i, "estatus_pendiente"));

                }

                //mov_pendientes_tmp.limpiar();
                
                //sis_soporte.obtener_instancia_soporte().addUpdate("div_division");
                
            }

        } catch (Exception e) {
            sis_soporte.obtener_instancia_soporte().agregar_mensaje_error("No se puede realizar la importación de los datos", e.getMessage());
        }

        dia_carga_masiva.cerrar();
        guardar();
        actualizar();
    }

    public pf_tabla getMov_pendientes() {
        return mov_pendientes;
    }

    public void setMov_pendientes(pf_tabla mov_pendientes) {
        this.mov_pendientes = mov_pendientes;
    }

    public pf_tabla getMov_pendientes_tmp() {
        return mov_pendientes_tmp;
    }

    public void setMov_pendientes_tmp(pf_tabla mov_pendientes_tmp) {
        this.mov_pendientes_tmp = mov_pendientes_tmp;
    }

    public pf_layout getDiv_division() {
        return div_division;
    }

    public void setDiv_division(pf_layout div_division) {
        this.div_division = div_division;
    }

    public pf_dialogo getDia_carga_masiva() {
        return dia_carga_masiva;
    }

    public void setDia_carga_masiva(pf_dialogo dia_carga_masiva) {
        this.dia_carga_masiva = dia_carga_masiva;
    }

    public pf_grid getGri_cuerpo() {
        return gri_cuerpo;
    }

    public void setGri_cuerpo(pf_grid gri_cuerpo) {
        this.gri_cuerpo = gri_cuerpo;
    }

    public pf_grid getGri_datos() {
        return gri_datos;
    }

    public void setGri_datos(pf_grid gri_datos) {
        this.gri_datos = gri_datos;
    }

    public pf_subir_archivo getUpl_importarxls() {
        return upl_importarxls;
    }

    public void setUpl_importarxls(pf_subir_archivo upl_importarxls) {
        this.upl_importarxls = upl_importarxls;
    }

    @Override
    public void insertar() {
        mov_pendientes.insertar();
    }

    @Override
    public void guardar() {
        mov_pendientes.guardar();
        guardarPantalla();
    }

    @Override
    public void eliminar() {
        mov_pendientes.eliminar();
    }
}
