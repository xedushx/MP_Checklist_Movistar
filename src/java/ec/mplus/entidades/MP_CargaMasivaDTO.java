/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mplus.entidades;

/**
 *
 * @author Desarrollo
 */
public class MP_CargaMasivaDTO {
    
    String fecha;
    String codigo;
    String noLpu;
    String categoria;
    String descripcion;
    String estacion;
    String cantidad;
    String unidad;
    double valorUnitario;
    double subtotal;
    double iva;
    double total;
    String region;
    String proveedor;
    String grupo;
    String insumoServicio;
    String tipo;
    String tipoMP;
    boolean aprobacion;

    public boolean isAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(boolean aprobacion) {
        this.aprobacion = aprobacion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getInsumoServicio() {
        return insumoServicio;
    }

    public void setInsumoServicio(String insumoServicio) {
        this.insumoServicio = insumoServicio;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public String getNoLpu() {
        return noLpu;
    }

    public void setNoLpu(String noLpu) {
        this.noLpu = noLpu;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoMP() {
        return tipoMP;
    }

    public void setTipoMP(String tipoMP) {
        this.tipoMP = tipoMP;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public MP_CargaMasivaDTO(String fecha, String codigo, String noLpu, String categoria, String descripcion,
            String estacion, String cantidad, String unidad, double valorUnitario, double subtotal, double iva,
            double total, String region, String proveedor, String grupo, String insumoServicio, String tipo, String tipoMP,
            boolean aprobacion) {
        this.fecha = fecha;
        this.codigo = codigo;
        this.noLpu = noLpu;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.estacion = estacion;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.valorUnitario = valorUnitario;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.region = region;
        this.proveedor = proveedor;
        this.grupo = grupo;
        this.insumoServicio = insumoServicio;
        this.tipo = tipo;
        this.tipoMP = tipoMP;
        this.aprobacion = aprobacion;
    }

    @Override
    public String toString() {
        return "MP_CargaMasivaDTO{" + "fecha=" + fecha + ", codigo=" + codigo + ", noLpu=" + noLpu + ","
                + " categoria=" + categoria + ", descripcion=" + descripcion + ", estacion=" + estacion + ","
                + " cantidad=" + cantidad + ", unidad=" + unidad + ", valorUnitario=" + valorUnitario + ","
                + " subtotal=" + subtotal + ", iva=" + iva + ", total=" + total + ", region=" + region + ", "
                + "proveedor=" + proveedor + ", grupo=" + grupo + ", insumoServicio=" + insumoServicio + ", tipo=" + tipo + ", "
                + "tipoMP=" + tipoMP + ", aprobacion=" + aprobacion + '}';
    }
    
    
    
    
    
}
