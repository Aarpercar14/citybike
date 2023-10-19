package servicio;


import dominio.Estacionamiento;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioEstaciones {
	
	String crear(String nombre, int numPuestos, String postal, String cordX, String cordY);
	
	Estacionamiento getEstacion(String id) throws RepositorioException, EntidadNoEncontrada;
	
	void eliminar(String id) throws RepositorioException, EntidadNoEncontrada;
	
	
}