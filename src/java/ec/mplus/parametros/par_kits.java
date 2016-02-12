/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.parametros;

import com.google.gson.JsonObject;
import ec.mplus.servicios.MP_ProcesosSOAP;
import ec.xprime.componentes.*;
import ec.xprime.sistema.sis_soporte;
import ec.mplus.sistema.sis_pantalla;
import ec.xprime.persistencia.cla_conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Desarrollo
 */
public class par_kits extends sis_pantalla {

    private final static Logger LOGGER = Logger.getLogger(MP_ProcesosSOAP.class.getName());
    private pf_entrada_texto txt_nombreKit = new pf_entrada_texto();
    private pf_tabla tab_insumos = new pf_tabla();
    int lint_codigo;
    int cantidad_insumos = 1;

    public par_kits() {

        txt_nombreKit.setLabel("Inserte El nombre del Kit");

        pf_grid lgri_botones = new pf_grid();
        lgri_botones.setWidth("100%");
        lgri_botones.setColumns(3);

        lgri_botones.getChildren().add(new pf_etiqueta("KIT's"));
        lgri_botones.getChildren().add(txt_nombreKit);
        //lgri_botones.getChildren().add(bot_filtrar);

        bar_botones.agregarComponente(lgri_botones);
        bar_botones.quitarBotonsNavegacion();

        pf_tabulador tab_tabulador = new pf_tabulador();
        tab_tabulador.setId("tab_tabulador");

        tab_insumos.setId("tab_insumos");
        tab_insumos.setIdCompleto("tab_tabulador:tab_insumos");
        tab_insumos.setTabla("mov_insumos", "ins_codigo", 1);
        tab_insumos.getColumna("ins_codigo").setNombreVisual("ID");
        tab_insumos.getColumna("cin_codigo").setNombreVisual("CATEGORÍA");
        tab_insumos.getColumna("ins_codigo_externo").setNombreVisual("CÓDIGO");
        tab_insumos.getColumna("ins_codigo_externo").setVisible(false);
        tab_insumos.getColumna("ins_lpu").setNombreVisual("LPU");
        tab_insumos.getColumna("ins_lpu").setVisible(false);
        tab_insumos.getColumna("ins_descripcion").setNombreVisual("DESCRIPCIÓN");
        tab_insumos.getColumna("ins_descripcion").setAncho(200);
        tab_insumos.getColumna("uni_codigo").setNombreVisual("UNIDAD");
        tab_insumos.getColumna("uni_codigo").setVisible(false);
        tab_insumos.getColumna("ins_precio_referencial").setNombreVisual("PRECIO REFERENCIAL");
        tab_insumos.getColumna("ins_precio_final").setNombreVisual("PRECIO FINAL");
        tab_insumos.getColumna("ins_factor").setNombreVisual("FACTOR");
        tab_insumos.getColumna("ins_factor").setVisible(false);

        tab_insumos.getColumna("cin_codigo").setCombo("mov_categoria_insumo", "cin_codigo", "cin_nombre", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());
        tab_insumos.getColumna("uni_codigo").setCombo("mov_unidades", "uni_codigo", "uni_simbologia", "id_empresa = " + sis_soporte.obtener_instancia_soporte().obtener_empresa());

        tab_insumos.getColumna("ins_codigo").setOrden(1);
        tab_insumos.getColumna("ins_codigo_externo").setOrden(2);
        tab_insumos.getColumna("ins_lpu").setOrden(3);
        tab_insumos.getColumna("cin_codigo").setOrden(4);
        tab_insumos.getColumna("ins_descripcion").setOrden(5);
        tab_insumos.getColumna("uni_codigo").setOrden(6);
        tab_insumos.getColumna("ins_precio_referencial").setOrden(7);
        tab_insumos.getColumna("ins_factor").setOrden(8);
        tab_insumos.getColumna("ins_precio_final").setOrden(9);
        tab_insumos.setRows(15);
        tab_insumos.dibujar();

        pf_panel_tabla pat_panel_kits = new pf_panel_tabla();
        pat_panel_kits.setPanelTabla(tab_insumos);
        tab_tabulador.agregarTab("INSUMOS", pat_panel_kits);

        pf_layout div_division = new pf_layout();
        div_division.setId("div_division");
        div_division.dividir1(tab_tabulador);
        agregarComponente(div_division);

        bar_botones.quitarBotonsNavegacion();
    }

    @Override
    public void insertar() {
        tab_insumos.insertar();
        cantidad_insumos++;
    }

    @Override
    public void guardar() {
        lint_codigo = guardar_kit();
        guardar_insumo_kit(lint_codigo);

    }

    @Override
    public void eliminar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public pf_tabla getTab_insumos() {
        return tab_insumos;
    }

    public void setTab_det_insumos(pf_tabla tab_insumos) {
        this.tab_insumos = tab_insumos;
    }

    public int guardar_kit() {
        String lstr_resultado;
        cla_conexion conexion = new cla_conexion();
        int kit_codigo = 0;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        String SQL_INSERT = "INSERT INTO mov_kits("
                // + "ccu_codigo,"
                + "kit_descripcion)"
                + "VALUES(?)";

        try {
            conexion.getConnection().setAutoCommit(false);
            statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            //statement.setInt(1, );
            statement.setString(1, txt_nombreKit.getValue().toString());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                lstr_resultado = "Error al guardar el kit";
                conexion.getConnection().rollback();
                LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
            } else {
                conexion.getConnection().commit();
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    kit_codigo = generatedKeys.getInt(1);
                    lstr_resultado = String.valueOf(kit_codigo);
                } else {
                    lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                    kit_codigo = 0;
                }
            }
        } catch (SQLException ex) {
            try {
                conexion.getConnection().rollback();
                lstr_resultado = "Error al insertar el Kit: " + ex.getMessage();
                LOGGER.log(Level.SEVERE, "Error al insertar el kit: Causa--> {0}", ex);
            } catch (SQLException ex1) {
                lstr_resultado = "Error al insertar el Kit: " + ex1.getMessage();
                LOGGER.log(Level.SEVERE, "Error al insertar el kit: Causa--> {0}", ex1);
            }

        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException logOrIgnore) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException logOrIgnore) {
                }
            }
        }
        return kit_codigo;
    }

    public void guardar_insumo_kit(int kit_codigo) {
        String lstr_resultado;
        cla_conexion conexion = new cla_conexion();
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        for (int i = 0; i <= cantidad_insumos; i++) {


            String SQL_INSERT = "INSERT INTO mov_kit_insumo("
                    // + "ccu_codigo,"
                    + "kit_codigo,ins_codigo)"
                    + "VALUES(?,?)";

            try {
                conexion.getConnection().setAutoCommit(false);
                statement = conexion.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                //statement.setInt(1, );
                statement.setString(1, String.valueOf(kit_codigo));
                statement.setString(2, tab_insumos.getValor(i, "ins_codigo"));

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    lstr_resultado = "Error al guardar el kit";
                    conexion.getConnection().rollback();
                    LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);
                } else {
                    conexion.getConnection().commit();
                    generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {

                        lstr_resultado = String.valueOf(kit_codigo);
                    } else {
                        lstr_resultado = "Error al insertar el wizard, no se obtuvo el id generado.";
                        LOGGER.log(Level.SEVERE, "SQLERR: Causa--> {0}", lstr_resultado);

                    }
                }
            } catch (SQLException ex) {
                try {
                    conexion.getConnection().rollback();
                    lstr_resultado = "Error al insertar el Kit: " + ex.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el kit: Causa--> {0}", ex);
                } catch (SQLException ex1) {
                    lstr_resultado = "Error al insertar el Kit: " + ex1.getMessage();
                    LOGGER.log(Level.SEVERE, "Error al insertar el kit: Causa--> {0}", ex1);
                }

            } finally {
                if (generatedKeys != null) {
                    try {
                        generatedKeys.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException logOrIgnore) {
                    }
                }
            }
        }
    }
}
