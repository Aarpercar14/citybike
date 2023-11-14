package servicio;

import java.util.List;

import dominio.Bicicleta;
import dominio.Incidencia;

public interface IServicioIncidencias {
	
	Incidencia crear(String idBici,String descripcionIncidencia);
	
	void gestionDeLasIncidencias(String cierre, String operario, String incidencia);
	
	List<Incidencia> recuperarIncidencia();

}
