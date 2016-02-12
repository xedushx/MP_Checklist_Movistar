/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.servicios;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Desarrollo
 */
@WebService(serviceName = "MP_ProcesosSOAPService")
public class MP_ProcesosSOAPService {
    @EJB
    private MP_ProcesosSOAP ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "validarUsuario")
    public String validarUsuario(@WebParam(name = "id_empresa") String id_empresa, @WebParam(name = "nombre") String nombre, @WebParam(name = "clave") String clave) {
        return ejbRef.validarUsuario(id_empresa, nombre, clave);
    }

    @WebMethod(operationName = "descargarCatalogos")
    public String descargarCatalogos(@WebParam(name = "id_empresa") String id_empresa, @WebParam(name = "operacion") String operacion, @WebParam(name = "id_usuario") String id_usuario) {
        return ejbRef.descargarCatalogos(id_empresa, operacion, id_usuario);
    }

    @WebMethod(operationName = "descargarCatalogosPendientes")
    public String descargarCatalogosPendientes() {
        return ejbRef.descargarCatalogosPendientes();
    }

    @WebMethod(operationName = "insertarFotosWizard4x1")
    public String insertarFotosWizard4x1(@WebParam(name = "dcu_fecha") String dcu_fecha, @WebParam(name = "dcu_hora") String dcu_hora, @WebParam(name = "dcu_accion") String dcu_accion, @WebParam(name = "dcu_detalle") String dcu_detalle, @WebParam(name = "dcu_foto") String dcu_foto, @WebParam(name = "dcu_usuario") String dcu_usuario) {
        return ejbRef.insertarFotosWizard4x1(dcu_fecha, dcu_hora, dcu_accion, dcu_detalle, dcu_foto, dcu_usuario);
    }

    @WebMethod(operationName = "insertarCabecera4x1")
    public String insertarCabecera4x1(@WebParam(name = "id_empresa") String id_empresa, @WebParam(name = "id_usuario") String id_usuario, @WebParam(name = "prv_codigo") String prv_codigo, @WebParam(name = "ees_codigo") String ees_codigo, @WebParam(name = "tma_para_codigo") String tma_para_codigo, @WebParam(name = "eaf_codigo") String eaf_codigo, @WebParam(name = "ccu_fecha_elaboracion") String ccu_fecha_elaboracion, @WebParam(name = "ccu_fecha_inicio_evento") String ccu_fecha_inicio_evento, @WebParam(name = "ccu_fecha_hora_aviso") String ccu_fecha_hora_aviso, @WebParam(name = "ccu_fecha_hora_llegada") String ccu_fecha_hora_llegada, @WebParam(name = "ccu_tiempo_reaccion") String ccu_tiempo_reaccion, @WebParam(name = "ccu_fecha_hora_reparacion") String ccu_fecha_hora_reparacion, @WebParam(name = "ccu_soporte_tecnico") String ccu_soporte_tecnico, @WebParam(name = "ccu_solucionado") String ccu_solucionado, @WebParam(name = "ccu_tiempo_solucion") String ccu_tiempo_solucion, @WebParam(name = "ccu_operador_noc") String ccu_operador_noc, @WebParam(name = "ccu_tipo") String ccu_tipo, @WebParam(name = "ccu_accedio_sitio") String ccu_accedio_sitio, @WebParam(name = "ccu_observaciones") String ccu_observaciones, @WebParam(name = "ccu_latitud") String ccu_latitud, @WebParam(name = "ccu_longitud") String ccu_longitud, @WebParam(name = "ccu_imei") String ccu_imei, @WebParam(name = "ccu_remedy") String ccu_remedy, @WebParam(name = "codigo_foto_temporal") String codigo_foto_temporal) {
        return ejbRef.insertarCabecera4x1(id_empresa, id_usuario, prv_codigo, ees_codigo, tma_para_codigo, eaf_codigo, ccu_fecha_elaboracion, ccu_fecha_inicio_evento, ccu_fecha_hora_aviso, ccu_fecha_hora_llegada, ccu_tiempo_reaccion, ccu_fecha_hora_reparacion, ccu_soporte_tecnico, ccu_solucionado, ccu_tiempo_solucion, ccu_operador_noc, ccu_tipo, ccu_accedio_sitio, ccu_observaciones, ccu_latitud, ccu_longitud, ccu_imei, ccu_remedy, codigo_foto_temporal);
    }

    @WebMethod(operationName = "insertarEquiposUtilizados4X1")
    public String insertarEquiposUtilizados4X1(@WebParam(name = "ccu_codigo") String ccu_codigo, @WebParam(name = "eqm_codigo") String eqm_codigo) {
        return ejbRef.insertarEquiposUtilizados4X1(ccu_codigo, eqm_codigo);
    }

    @WebMethod(operationName = "insertarRepuestos4x1")
    public String insertarRepuestos4x1(@WebParam(name = "ccu_codigo") String ccu_codigo, @WebParam(name = "rep_equipo_reemplazado") String rep_equipo_reemplazado, @WebParam(name = "rep_marca") String rep_marca, @WebParam(name = "rep_modelo") String rep_modelo, @WebParam(name = "rep_serie_reemplazado") String rep_serie_reemplazado, @WebParam(name = "rep_serie_nuevo") String rep_serie_nuevo, @WebParam(name = "rep_observaciones") String rep_observaciones) {
        return ejbRef.insertarRepuestos4x1(ccu_codigo, rep_equipo_reemplazado, rep_marca, rep_modelo, rep_serie_reemplazado, rep_serie_nuevo, rep_observaciones);
    }

    @WebMethod(operationName = "insertarInsumosUtilizados4x1")
    public String insertarInsumosUtilizados4x1(@WebParam(name = "ccu_codigo") String ccu_codigo, @WebParam(name = "ins_codigo") String ins_codigo, @WebParam(name = "iut_cantidad") String iut_cantidad) {
        return ejbRef.insertarInsumosUtilizados4x1(ccu_codigo, ins_codigo, iut_cantidad);
    }

    @WebMethod(operationName = "insertarInsumosDescartados4x1")
    public String insertarInsumosDescartados4x1(@WebParam(name = "ccu_codigo") String ccu_codigo, @WebParam(name = "ins_codigo") String ins_codigo, @WebParam(name = "ide_cantidad") String ide_cantidad) {
        return ejbRef.insertarInsumosDescartados4x1(ccu_codigo, ins_codigo, ide_cantidad);
    }

    @WebMethod(operationName = "insertarFotografia4x1")
    public String insertarFotografia4x1(@WebParam(name = "ccu_codigo") String ccu_codigo, @WebParam(name = "fot_descripcion") String fot_descripcion, @WebParam(name = "fot_foto") String fot_foto) {
        return ejbRef.insertarFotografia4x1(ccu_codigo, fot_descripcion, fot_foto);
    }

    @WebMethod(operationName = "insertarFinalizar4x1")
    public String insertarFinalizar4x1(@WebParam(name = "ccu_codigo") String ccu_codigo, @WebParam(name = "fin_observacion") String fin_observacion) {
        return ejbRef.insertarFinalizar4x1(ccu_codigo, fin_observacion);
    }
    
}
