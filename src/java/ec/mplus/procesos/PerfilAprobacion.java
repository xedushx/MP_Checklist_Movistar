/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.procesos;

import ec.xprime.persistencia.cla_conexion;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author epacheco
 */
public class PerfilAprobacion {

    private final static Logger LOGGER = Logger.getLogger(PerfilAprobacion.class.getName());
    private Integer codigoPerfilAprobacion;
    private String nombrePerfilAprobacion;
    private Boolean estado;
    private Integer nivelAprobacion;

    public PerfilAprobacion(Integer codigoPerfilAprobacion, String nombrePerfilAprobacion, Boolean estado, Integer nivelAprobacion) {
        this.codigoPerfilAprobacion = codigoPerfilAprobacion;
        this.nombrePerfilAprobacion = nombrePerfilAprobacion;
        this.estado = estado;
        this.nivelAprobacion = nivelAprobacion;
    }

    public Integer getCodigoPerfilAprobacion() {
        return codigoPerfilAprobacion;
    }

    public void setCodigoPerfilAprobacion(Integer codigoPerfilAprobacion) {
        this.codigoPerfilAprobacion = codigoPerfilAprobacion;
    }

    public String getNombrePerfilAprobacion() {
        return nombrePerfilAprobacion;
    }

    public void setNombrePerfilAprobacion(String nombrePerfilAprobacion) {
        this.nombrePerfilAprobacion = nombrePerfilAprobacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getNivelAprobacion() {
        return nivelAprobacion;
    }

    public void setNivelAprobacion(Integer nivelAprobacion) {
        this.nivelAprobacion = nivelAprobacion;
    }

    public static List<PerfilAprobacion> obtenerPerfilesAprobacion() {

        List<PerfilAprobacion> perfiles = new ArrayList<PerfilAprobacion>();

        try {
            cla_conexion conexion = new cla_conexion();
            List lista = conexion.consultar("Select id_perfil, nombre, estado, nivel_aprobacion "
                    + "from tbl_perfil "
                    + "where estado = true "
                    + "and nivel_aprobacion is not null "
                    + "order by nivel_aprobacion");

            if (lista.size() > 0) {
                Object[] campos;
                for (int i = 0; i < lista.size(); i++) {
                    campos = (Object[]) lista.get(i);
                    perfiles.add(new PerfilAprobacion((Integer) campos[0], (String) campos[1], (Boolean) campos[2], (Integer) campos[3]));
                }
            } else {
                perfiles = null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error al obtener perfiles de aprobacion {0}", e);
        }

        return perfiles;
    }
}
