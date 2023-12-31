package servicio;

import java.util.List;

import dominio.Bicicleta;
import dominio.Estacionamiento;
import dto.EstacionesDTO;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioEstaciones {
	
	String crear(String nombre, int numPuestos, String postal, double cordX, double cordY);
	
	Estacionamiento getEstacion(String id) throws RepositorioException, EntidadNoEncontrada;
	
	void eliminar(String id) throws RepositorioException, EntidadNoEncontrada;
	
	String altaDeUnaBici(String modelo, Estacionamiento estacion);
	
	void estacionarUnaBicileta(String idBici);
	
	void estacionarUnaBicileta(String idBici, String idEstacion);
	
	void retirarUnaBicleta(String idBici);
	
	void darDeBajaUnaBici(String idBici, String motivo);
	
	List<Bicicleta> recuperarBiciEstacionadaPosicion(double x, double y);
	
	List<Estacionamiento> recuperarEstacionSitiosTuristicosDeMayorAMenor();
	
	public String encontrarEstacionLibre() throws RepositorioException;
	
	public Bicicleta obtenerBici(String idBici) throws RepositorioException, EntidadNoEncontrada;

	EstacionesDTO getById(String id) throws RepositorioException; 
	
}
