
/*
******************************************************************************************
* PROCESO PARA AUTORIZACIÓN DE INSUMOS													 *
*																						 *
* 1 .Crear niveles dinámicos hasta de cinco opciones									 *												
* 2. El primer nivel es el que ingresa los insumos.
* 3. Los siguientes niveles son los que aprueban 
* 4. Una vez aprobado los insumos por el nivel superior 
*	 los niveles inferiores solo pueden visualizar la información.
* 5. Una vez aprobado por el nivel inferior solo en ese caso puede 
*	 visualizar la información el siguiente subnivel
* 6. Los niveles superiores pueden tener la opción de insertar eliminar 
*	 borrar la información una vez aprobada solo pueden visualizar la información.
* 7. Sí hay más de 3 niveles, se mantiene la misma filosofía de aprobación, 
*	 es decir, el nivel 4 y 5, pueden insertar, borrar y aprobar. 
*    Cuando el último nivel configurado aprueba, el registro está visible para 
*	 todos los niveles.
* 8. En la carga de archivos aprobados estos se subirán con el máximo nivel y no se 
*	 podrá realizar ninguna modificación. La carga masiva de insumos, sólo lo puede 
*	 realizar el administrador de la herramienta web, ya son aprobados con anterioridad, 
*    entonces no existe la necesidad de un estado de aprobación, ni un flujo. 
*    El momento que se suben es porque están aprobados y son parte del inventario.
******************************************************************************************/

-- agrega el campo estado a la tabla tbl_perfil
ALTER TABLE tbl_perfil
  ADD COLUMN nivel_aprobacion integer;
ALTER TABLE tbl_perfil
  ADD COLUMN estado boolean;
COMMENT ON COLUMN tbl_perfil.nivel_aprobacion IS 'permite registrar el nivel de aprobación de insumos.';
COMMENT ON COLUMN tbl_perfil.estado IS 'indica si el perfil está activo para realizar aprobaciones de insumos';


-- elimina y agrega los campos de aprobaciones de insumos
ALTER TABLE mov_cab_aprobacion_solicitudes
  DROP COLUMN cas_fecha_aprobacion_sup;
ALTER TABLE mov_cab_aprobacion_solicitudes
  DROP COLUMN cas_fecha_aprobacion;
ALTER TABLE mov_cab_aprobacion_solicitudes
  DROP COLUMN cas_estado_aprobacion;
ALTER TABLE mov_cab_aprobacion_solicitudes
  DROP COLUMN cas_estado_aprobacion_supervisor;
ALTER TABLE mov_cab_aprobacion_solicitudes
  ADD COLUMN cas_aprobacion_1 boolean;
ALTER TABLE mov_cab_aprobacion_solicitudes
  ADD COLUMN cas_aprobacion_2 boolean;
ALTER TABLE mov_cab_aprobacion_solicitudes
  ADD COLUMN cas_aprobacion_3 boolean;
ALTER TABLE mov_cab_aprobacion_solicitudes
  ADD COLUMN cas_aprobacion_4 boolean;
ALTER TABLE mov_cab_aprobacion_solicitudes
  ADD COLUMN cas_aprobacion_5 boolean;
COMMENT ON COLUMN mov_cab_aprobacion_solicitudes.cas_aprobacion_1 IS 'estado de aprobación nivel 1';
COMMENT ON COLUMN mov_cab_aprobacion_solicitudes.cas_aprobacion_2 IS 'estado de aprobación nivel 2';
COMMENT ON COLUMN mov_cab_aprobacion_solicitudes.cas_aprobacion_3 IS 'estado de aprobación nivel 3';


-- elimina y agrega columnas de aprobaciones de detalles
ALTER TABLE mov_det_aprobacion_solicitudes
  DROP COLUMN das_fecha_aprobacion_supervisor;
ALTER TABLE mov_det_aprobacion_solicitudes
  DROP COLUMN das_fecha_aprobacion;
ALTER TABLE mov_det_aprobacion_solicitudes
  DROP COLUMN das_estado_aprobacion_supervisor;
ALTER TABLE mov_det_aprobacion_solicitudes
  DROP COLUMN das_estado_aprobacion;
ALTER TABLE mov_det_aprobacion_solicitudes
  ADD COLUMN das_aprobacion_1 boolean;
ALTER TABLE mov_det_aprobacion_solicitudes
  ADD COLUMN das_aprobacion_2 boolean;
ALTER TABLE mov_det_aprobacion_solicitudes
  ADD COLUMN das_aprobacion_3 boolean;
ALTER TABLE mov_det_aprobacion_solicitudes
  ADD COLUMN das_aprobacion_4 boolean;
ALTER TABLE mov_det_aprobacion_solicitudes
  ADD COLUMN das_aprobacion_5 boolean;
COMMENT ON COLUMN mov_det_aprobacion_solicitudes.das_aprobacion_1 IS 'estado de aprobación nivel 1 detalle';
COMMENT ON COLUMN mov_det_aprobacion_solicitudes.das_aprobacion_2 IS 'estado de aprobación nivel 2 detalle';
COMMENT ON COLUMN mov_det_aprobacion_solicitudes.das_aprobacion_3 IS 'estado de aprobación nivel 3 detalle';

-- agrega el campo para el nivel máximo de los niveles de aprobaciones de solicitudes
ALTER TABLE tbl_empresa
  ADD COLUMN numero_maximo_niveles_aprobacion integer;
COMMENT ON COLUMN tbl_empresa.numero_maximo_niveles_aprobacion IS 'indica el número máximo para los niveles de aprobación de solicitudes de insumos.';


--usuarios desarrollo
-- nivel1: cvillacresSOL
-- nivel2: cvillacresAPR
-- nivel3: movepacheco
-- nivel4: adminmplus
-- nivel5: cvillacresADM
