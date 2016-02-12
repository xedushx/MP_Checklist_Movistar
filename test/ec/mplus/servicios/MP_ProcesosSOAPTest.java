/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.servicios;

import javax.ejb.embeddable.EJBContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epacheco
 */
public class MP_ProcesosSOAPTest {
    
    public MP_ProcesosSOAPTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of validarUsuario method, of class MP_ProcesosSOAP.
     */
    @Test
    public void testValidarUsuario() throws Exception {
        System.out.println("validarUsuario");
        String id_empresa = "1";
        String nombre = "movepacheco";
        String clave = "movepacheco";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        MP_ProcesosSOAP instance = (MP_ProcesosSOAP)container.getContext().lookup("java:global/classes/MP_ProcesosSOAP");
        String expResult = "OK;3";
        String result = instance.validarUsuario(id_empresa, nombre, clave);
        System.out.println(result);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of descargarCatalogos method, of class MP_ProcesosSOAP.
     */
    @Test
    public void testDescargarCatalogos() throws Exception {
        System.out.println("descargarCatalogos");
        String id_empresa = "1";
        String operacion = "PR";
        String id_usuario = "2";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        MP_ProcesosSOAP instance = (MP_ProcesosSOAP)container.getContext().lookup("java:global/classes/MP_ProcesosSOAP");
        String expResult = "";
        String result = instance.descargarCatalogos(id_empresa, operacion, id_usuario);
        System.out.println(result);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
