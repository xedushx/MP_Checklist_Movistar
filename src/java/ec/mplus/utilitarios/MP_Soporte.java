/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.utilitarios;

import ec.xprime.componentes.pf_error_sql;
import ec.xprime.componentes.pf_notificacion;
import ec.xprime.componentes.pf_seleccion_archivo;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.primefaces.component.menubar.Menubar;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.context.RequestContext;

/**
 *
 * @author xEdushx
 */
public class MP_Soporte {

    private static final String ISTR_NOMBRE_ARCHIVO_PROPERTIE = "/config.properties";
    private SimpleDateFormat formato_fecha = new SimpleDateFormat(FORMATO_FECHA);
    public final static String FORMATO_FECHA = "yyyy-MM-dd";
    public final static String FORMATO_HORA = "HH:mm:ss";
    public final static String FORMATO_FECHA_HORA = "yyyy-MM-dd HH:mm:ss";
    //private MP_Conexion conexion;

    /**
     * Obtiene un valor de propiedad
     *
     * @param nombre ==>> String con el nombre de la propiedad a retornar
     * @return String con el valos de la propiedad
     */
    public String getPropiedad(String nombre) {
        String valor = null;
        try {
            Properties propiedades = new Properties();
            propiedades.load(this.getClass().getResource(ISTR_NOMBRE_ARCHIVO_PROPERTIE).openStream());
            valor = propiedades.getProperty(nombre);
        } catch (Exception e) {
        }
        return valor;
    }

    public String getFechaHoraActual() {
        return getFechaActual() + " " + getHoraActual();
    }

    public String getFechaActual() {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
        return formatoFecha.format(fecha);
    }

    public String getFechaActualFormato(String formato) {
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat(formato);
        return formatoFecha.format(fecha);
    }

    public String getHoraActual() {
        Date fecha = new Date();
        SimpleDateFormat formatoHora = new SimpleDateFormat(FORMATO_HORA);
        return formatoHora.format(fecha);
    }

    public String getHoraActualFormato(String formato) {
        Date fecha = new Date();
        SimpleDateFormat formatoHora = new SimpleDateFormat(formato);
        return formatoHora.format(fecha);
    }

    public String getFormatoFechaSimple(String fecha) {
        try {
            fecha = fecha.split(" ")[0];
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
            return formatoFecha.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public String getFormatoHoraSimple(String fecha) {
        String hora = "";
        try {
            hora = fecha.split(" ")[1];
            SimpleDateFormat formatoFecha = new SimpleDateFormat("HHmmss");
            return formatoFecha.format(hora);
        } catch (Exception e) {
        }
        return hora;
    }

    public String obtener_formato_fecha(Object fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
            return formatoFecha.format(fecha);
        } catch (Exception e) {
        }
        return (String) fecha;
    }

    public int getAnio(String fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getFecha(fecha));
        return cal.get(Calendar.YEAR);
    }

    public Date getFecha(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA);
            Date dat_fecha = new Date();
            dat_fecha = formatoFecha.parse(fecha);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

    public Date getFechaHora(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(FORMATO_FECHA_HORA);
            Date dat_fecha = new Date();
            dat_fecha = formatoFecha.parse(fecha);
            return dat_fecha;
        } catch (Exception e) {
        }
        return null;
    }

//    public MP_Conexion obtener_conexion() {
//        MP_Conexion conexion = (MP_Conexion) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("CONEXION");
//        return conexion;
//    }

    /**
     * Retorna el componente seleccionarArchivo
     *
     * @return
     */
    public pf_seleccion_archivo getSeleccionArchivo() {
        return (pf_seleccion_archivo) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:sar_upload");
    }

    public void agregar_mensaje(String titulo, String detalle) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalle));
    }

    public void agregar_mensaje_error(String titulo, String detalle) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalle));
    }

    public void agregar_mensaje_alerta(String titulo, String detalle) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, detalle));
    }

    public void addUpdate(String update) {
        //Actualiza un componente 
        if (update == null) {
            return;
        }
        RequestContext requestContext = RequestContext.getCurrentInstance();
        update = formarUpdate(update);
        update = update.replace(":formulario:", "formulario:");
        if (update.indexOf(",") > 0) {
            String componentes[] = update.split(",");
            for (int i = 0; i < componentes.length; i++) {
                requestContext.update(componentes[i]);
            }
        } else {
            requestContext.update(update);
        }

    }

    public String formarUpdate(String update) {
        if (update == null) {
            update = "";
        }
        StringBuilder str_update = new StringBuilder();
        if (update.indexOf(",") > 0) {
            update = update + ",";

            String str_aux = "";
            for (int i = 0; i < update.length(); i++) {
                if (update.charAt(i) != ',') {
                    str_aux = str_aux.concat(String.valueOf(update.charAt(i)));
                } else {
                    if (!str_aux.isEmpty() && !str_aux.equals("@all") && !str_aux.equals("@form") && !str_aux.equals("@this") && !str_aux.equals("@none") && !str_aux.equals("@parent")) {
                        str_aux = ":formulario:".concat(str_aux);
                    }
                    str_update.append(str_aux);
                    str_aux = "";
                    if (i != update.length() - 1) {
                        str_update.append(",");
                    }
                }
            }
        } else {
            if (!update.isEmpty() && !update.equals("@all") && !update.equals("@form") && !update.equals("@this") && !update.equals("@none") && !update.equals("@parent")) {
                str_update.append(":formulario:").append(update);
            } else {
                str_update.append(update);
            }
        }
        if (str_update.length() == 0) {
            return null;
        }
        return str_update.toString();
    }

    public void crear_variable(String nombre, String valor) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombre.toUpperCase(), valor);
    }

    public String obtener_variable(String nombre) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nombre.toUpperCase());
    }

//    public void cerrarSesion() {
//        try {
//            if (obtener_conexion() != null) {
//                obtener_conexion().desconectar();
//            }
//            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
//            Runtime basurero = Runtime.getRuntime();
//            basurero.gc();
//        } catch (Exception e) {
//        }
//    }

//    public String obtenerSaldo(String astr_usuario, String astr_clave) {
//        String lstr_saldo = "0";
////        MP_Servicio_Movilway mP_Servicio_Movilway = new MP_Servicio_Movilway();
////
////        GetBalanceResponse getBalanceResponse = mP_Servicio_Movilway.obtenerBalance(astr_usuario, astr_clave);
////
////        lstr_saldo = getBalanceResponse.getResponseMessage().getValue();
////        System.out.println("responseCode: " + getBalanceResponse.getResponseCode());
////        System.out.println("responseMessage: " + lstr_saldo);
//        conexion = obtener_conexion();
//        List lis_consulta = conexion.consultar("Select tra_codigo, tra_saldo from mp_transaccion where emp_prefijo = '" + obtener_variable("prefijo_empresa") + "' order by tra_codigo desc limit 1");
//        if (lis_consulta.size() > 0) {
//            for (int i = 0; i < lis_consulta.size(); i++) {
//                Object fila[] = (Object[]) lis_consulta.get(i);
//                lstr_saldo = (String) fila[1];
//            }
//        }
//
//        return lstr_saldo;
//    }

    public void crearError(String mensaje, String ubicacion, Exception e) {
        try {
            pf_error_sql error_sql = (pf_error_sql) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:error_sql");
            if (error_sql != null && !error_sql.isVisible()) {
                error_sql.setErrorSQL(mensaje, ubicacion, e);
                error_sql.dibujar();
                addUpdate("error_sql");
            }
        } catch (Exception ex) {
        }

    }

    public Object cargarPantalla(String paquete, String tipo_opcion) {
        Object clase = null;
        try {
            try {
                Runtime basurero = Runtime.getRuntime();
                basurero.gc();
            } catch (Exception e) {
            }
            Class pantalla = Class.forName(paquete + "." + tipo_opcion);
            clase = pantalla.newInstance();
            //buscarPermisosObjetos();
        } catch (Exception ex) {
            crearError(ex.getMessage(), "mbe_index en el método cargar() ", ex);
        }
//        //1
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        UIViewRoot newView = FacesContext.getCurrentInstance().getApplication().getViewHandler().createView(FacesContext.getCurrentInstance(),
                "/index.jsf");
//        //1
        newView.setViewId(viewId);
        FacesContext.getCurrentInstance().setViewRoot(newView);
        return clase;
    }

//    public Menubar obtenerMenu() {
//        Menubar menu = new Menubar();
//        menu.setAutoDisplay(false);
//
//        String prefijo_empresa = obtener_variable("prefijo_empresa");
//        String id_perfil = obtener_variable("id_perfil");
//
//        String lstr_usuario = obtener_variable("nombre_usuario").toUpperCase();
//
//        Submenu home = new Submenu();
//        home.setLabel("Bienvenid@ " + lstr_usuario + "");
//        home.setIcon("ui-icon-home");
//
//        pf_menu_item mei_salir = new pf_menu_item();
//        mei_salir.setValue("Cerrar Sesión");
//        mei_salir.setActionListenerRuta("mbe_login.salir");
//        mei_salir.setIcon("ui-icon-close");
//
//        home.getChildren().add(mei_salir);
//        menu.getChildren().add(home);
//
//        Submenu recargas = new Submenu();
//        recargas.setLabel("Recargas");
//        recargas.setIcon("ui-icon-document");
//
//        //Para la pantalla de envio simple
//        pf_menu_item mei_rep_transaccion = new pf_menu_item();
//        mei_rep_transaccion.setValue("Reporte de Transacciones");
//        mei_rep_transaccion.setCodigo("1");
//        mei_rep_transaccion.setActionListenerRuta("mbe_index.cargar");
//        mei_rep_transaccion.setMenuUpdate("dibuja,:fortitulo");
//        mei_rep_transaccion.setIcon("ui-pantalla");
//        mei_rep_transaccion.setOnclick("dimensionesDisponibles()");
//
//        recargas.getChildren().add(mei_rep_transaccion);
//
//        int usu_puede_ver_reportes = Integer.valueOf(obtener_variable("usu_puede_ver_reportes"));
//        int usu_puede_solicitar_recargas_saldo = Integer.valueOf(obtener_variable("usu_puede_solicitar_recargas_saldo"));
//
//        if (usu_puede_solicitar_recargas_saldo == 1) {
//
//            //Para la pantalla de envio simple
//            pf_menu_item mei_sol_recarga = new pf_menu_item();
//            mei_sol_recarga.setValue("Solicitudes de Recarga");
//            mei_sol_recarga.setCodigo("2");
//            mei_sol_recarga.setActionListenerRuta("mbe_index.cargar");
//            mei_sol_recarga.setMenuUpdate("dibuja,:fortitulo");
//            mei_sol_recarga.setIcon("ui-pantalla");
//            mei_sol_recarga.setOnclick("dimensionesDisponibles()");
//
//            recargas.getChildren().add(mei_sol_recarga);
//
//        }
//
//        if (usu_puede_ver_reportes == 1) {
//            //Para la pantalla de envio simple
//            pf_menu_item mei_rep_consolidado_empresa = new pf_menu_item();
//            mei_rep_consolidado_empresa.setValue("Consolidado por Empresa");
//            mei_rep_consolidado_empresa.setCodigo("3");
//            mei_rep_consolidado_empresa.setActionListenerRuta("mbe_index.cargar");
//            mei_rep_consolidado_empresa.setMenuUpdate("dibuja,:fortitulo");
//            mei_rep_consolidado_empresa.setIcon("ui-pantalla");
//            mei_rep_consolidado_empresa.setOnclick("dimensionesDisponibles()");
//
//            recargas.getChildren().add(mei_rep_consolidado_empresa);
//        }
//        
//        menu.getChildren().add(recargas);
//
//        return menu;
//    }
//
//    public pf_panel_grupo getPantalla() {
//        pf_panel_grupo pan_dibuja = (pf_panel_grupo) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:dibuja");
//        return pan_dibuja;
//    }

    public void ejecutarJavaScript(String javaScript) {
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute(javaScript);
    }

    /**
     * Agrega un mensaje de notificación que bloquea el menu y es necesario que
     * el usuario la cierre
     *
     * @param titulo
     * @param mensaje
     */
    public void agregarNotificacionInfo(String titulo, String mensaje) {
        pf_notificacion not_notificacion = (pf_notificacion) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:not_notificacion");
        if (not_notificacion != null) {
            not_notificacion.setNotificacion(titulo, mensaje, "");
            addUpdate("not_notificacion");
            ejecutarJavaScript("not_notificacion.show();");
        }
    }

    /**
     * Agrega un mensaje de notificación que bloquea el menu y es necesario que
     * el usuario la cierre
     *
     * @param titulo
     * @param mensaje
     * @param pathImagen ruta de imagen
     */
    public void agregarNotificacion(String titulo, String mensaje, String pathImagen) {
        pf_notificacion not_notificacion = (pf_notificacion) FacesContext.getCurrentInstance().getViewRoot().findComponent("formulario:not_notificacion");
        if (not_notificacion != null) {
            not_notificacion.setNotificacion(titulo, mensaje, pathImagen);
            addUpdate("not_notificacion");
            ejecutarJavaScript("not_notificacion.show();");
        }
    }

    public String getFormatoNumero(Object numero) {
        DecimalFormat formatoNumero;
        DecimalFormatSymbols idfs_simbolos = new DecimalFormatSymbols();
        idfs_simbolos.setDecimalSeparator('.');
        formatoNumero = new DecimalFormat("#.##", idfs_simbolos);
        formatoNumero.setMaximumFractionDigits(2);
        formatoNumero.setMinimumFractionDigits(2);
        try {
            double ldob_valor = Double.parseDouble(numero.toString());
            return formatoNumero.format(ldob_valor);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Retorna en letras con dolares y centavos una cantidad numérica
     *
     * @param numero
     * @return
     */
    public String getLetrasDolarNumero(Object numero) {
        String letras = getFormatoNumero(numero);
        if (letras != null) {
            try {
                String centavos = (Integer.parseInt(letras.substring((letras.lastIndexOf(".") + 1), letras.length()))) + "";
                if (centavos.trim().length() == 1) {
                    centavos = "0" + centavos;
                }
                letras = recursivoNumeroLetras(Integer.parseInt(letras.substring(0, letras.lastIndexOf(".")))) + " CON " + centavos + "/100 ";
                letras = letras.toUpperCase();
                letras = letras.trim();
            } catch (Exception e) {
            }
        }
        return letras;
    }

    /**
     * Metodo recursivo que calcula en letras una cantidad numerica
     *
     * @param numero
     * @return
     */
    private String recursivoNumeroLetras(int numero) {
        String cadena = new String();
        // Aqui identifico si lleva millones
        if ((numero / 1000000) > 0) {
            if ((numero / 1000000) == 1) {
                cadena = " Un Millon" + recursivoNumeroLetras(numero % 1000000);
            } else {
                cadena = recursivoNumeroLetras(numero / 1000000) + " Millones" + recursivoNumeroLetras(numero % 1000000);
            }
        } else {
            // Aqui identifico si lleva Miles
            if ((numero / 1000) > 0) {

                if ((numero / 1000) == 1) {
                    cadena = " Mil" + recursivoNumeroLetras(numero % 1000);
                } else {
                    cadena = recursivoNumeroLetras(numero / 1000) + " Mil" + recursivoNumeroLetras(numero % 1000);
                }
            } else {
                // Aqui identifico si lleva cientos
                if ((numero / 100) > 0) {
                    if ((numero / 100) == 1) {
                        if ((numero % 100) == 0) {
                            cadena = " Cien";
                        } else {
                            cadena = " Ciento" + recursivoNumeroLetras(numero % 100);
                        }
                    } else {
                        if ((numero / 100) == 5) {
                            cadena = " Quinientos" + recursivoNumeroLetras(numero % 100);
                        } else {
                            if ((numero / 100) == 9) {
                                cadena = " Novecientos" + recursivoNumeroLetras(numero % 100);
                            } else {
                                cadena = recursivoNumeroLetras(numero / 100) + "cientos" + recursivoNumeroLetras(numero % 100);
                            }
                        }
                    }
                } // Aqui se identifican las Decenas
                else {
                    if ((numero / 10) > 0) {
                        switch (numero / 10) {
                            case 1:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Diez";
                                        break;
                                    case 1:
                                        cadena = " Once";
                                        break;
                                    case 2:
                                        cadena = " Doce";
                                        break;
                                    case 3:
                                        cadena = " Trece";
                                        break;
                                    case 4:
                                        cadena = " Catorce";
                                        break;
                                    case 5:
                                        cadena = " Quince";
                                        break;
                                    default:
                                        cadena = " Diez y " + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 2:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Veinte";
                                        break;
                                    default:
                                        cadena = " Veinti" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 3:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Treinta";
                                        break;
                                    default:
                                        cadena = " Treinta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 4:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Cuarenta";
                                        break;
                                    default:
                                        cadena = " Cuarenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 5:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Cincuenta";
                                        break;
                                    default:
                                        cadena = " Cincuenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 6:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Sesenta";
                                        break;
                                    default:
                                        cadena = " Sesenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 7:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Setenta";
                                        break;
                                    default:
                                        cadena = " Setenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 8:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Ochenta";
                                        break;
                                    default:
                                        cadena = " Ochenta y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                            case 9:
                                switch (numero % 10) {
                                    case 0:
                                        cadena = " Noventa";
                                        break;
                                    default:
                                        cadena = " Noventa y" + recursivoNumeroLetras(numero % 10);
                                        break;
                                }
                                break;
                        }
                    } else {
                        switch (numero) {
                            case 1:
                                cadena = " Uno";
                                break;
                            case 2:
                                cadena = " Dos";
                                break;
                            case 3:
                                cadena = " Tres";
                                break;
                            case 4:
                                cadena = " Cuatro";
                                break;
                            case 5:
                                cadena = " Cinco";
                                break;
                            case 6:
                                cadena = " Seis";
                                break;
                            case 7:
                                cadena = " Siete";
                                break;
                            case 8:
                                cadena = " Ocho";
                                break;
                            case 9:
                                cadena = " Nueve";
                                break;
                            case 0:
                                //      cadena = " Cero";
                                break;
                        }
                    }
                }
            }
        }
        return cadena;
    }
}
