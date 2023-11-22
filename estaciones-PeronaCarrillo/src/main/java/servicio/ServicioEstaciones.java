package servicio;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import repositorio.RepositorioMemoriaEstacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import dominio.Bicicleta;
import dominio.Estacionamiento;
import dominio.Historico;
import dominio.EntradaHistorico;

public class ServicioEstaciones implements IServicioEstaciones {

	private Repositorio<Estacionamiento, String> repositorioEstacion = FactoriaRepositorios
			.getRepositorio(Estacionamiento.class);
	private Repositorio<Bicicleta, String> repositorioBicicletas = FactoriaRepositorios.getRepositorio(Bicicleta.class);
	private Repositorio<Historico, String> repositorioHistorico = FactoriaRepositorios.getRepositorio(Historico.class);

	@Override
	public String crear(String nombre, int numPuestos, String postal, double cordX, double cordY) {
		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("nombre: no debe ser nulo ni vacio");
		if (numPuestos < 5)
			throw new IllegalArgumentException("numero de puestos: una estacion tiene 5 puestos como mínimo");
		if (postal == null || postal.isEmpty())
			throw new IllegalArgumentException("posta: no debe ser nulo ni vacio");

		Estacionamiento estacion = new Estacionamiento(nombre, numPuestos, postal, cordX, cordY);

		try {
			String id = repositorioEstacion.add(estacion);
			return id;
		} catch (RepositorioException e) {
			e.printStackTrace();
		}
		return "";

	}

	@Override
	public Estacionamiento getEstacion(String id) throws RepositorioException, EntidadNoEncontrada {

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacío");

		return repositorioEstacion.getById(id);

	}

	@Override
	public void eliminar(String id) throws RepositorioException, EntidadNoEncontrada {
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id: no debe ser nulo ni vacio");

		Estacionamiento estacion = repositorioEstacion.getById(id);
		repositorioEstacion.delete(estacion);
	}

	@Override
	public String altaDeUnaBici(String modelo, Estacionamiento estacion) {
		String id = UUID.randomUUID().toString();
		Bicicleta bici = new Bicicleta(id, modelo, estacion.getId());
		try {
			repositorioBicicletas.add(bici);
		} catch (RepositorioException e) {
			e.printStackTrace();
		}
		this.estacionarUnaBicileta(id, estacion.getId());
		try {
			Historico historico = new Historico(bici.getId());
			bici.setIdHistorico(repositorioHistorico.add(historico));
		} catch (RepositorioException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public void estacionarUnaBicileta(String idBici) {
		try {
			String idEstacion = RepositorioMemoriaEstacion.getEstacionLibre(repositorioEstacion);
			Bicicleta bici = repositorioBicicletas.getById(idBici);
			Estacionamiento estacion = repositorioEstacion.getById(idEstacion);
			estacion.estacionarBici(bici);
			Historico historico = repositorioHistorico.getById(bici.getIdHistorico());
			historico.añadirEntrada(new EntradaHistorico(idBici, idEstacion));
			repositorioHistorico.update(historico);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			e.printStackTrace();
		}

	}

	@Override
	public void estacionarUnaBicileta(String idBici, String idEstacion) {
		try {
			Bicicleta bici = repositorioBicicletas.getById(idBici);
			Estacionamiento estacion = repositorioEstacion.getById(idEstacion);
			estacion.estacionarBici(bici);
			Historico historico = repositorioHistorico.getById(bici.getIdHistorico());
			historico.añadirEntrada(new EntradaHistorico(idBici, idEstacion));
			repositorioHistorico.update(historico);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void retirarUnaBicleta(String idBici) {
		try {
			Bicicleta bici = repositorioBicicletas.getById(idBici);
			Historico historico = repositorioHistorico.getById(bici.getIdHistorico());
			Estacionamiento estacion = repositorioEstacion.getById(historico.getUltimaEstacion());
			estacion.sacarBici(idBici);
			repositorioEstacion.update(estacion);
			historico.salidaUltimaEstacion();
			repositorioHistorico.update(historico);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void darDeBajaUnaBici(String idBici, String motivo) {
		try {
			Bicicleta bici = repositorioBicicletas.getById(idBici);
			this.retirarUnaBicleta(idBici);
			bici.cambioEstadoBici("no disponible");
			bici.setFechaBaja(LocalDateTime.now());
			Historico historico = repositorioHistorico.getById(bici.getIdHistorico());
			repositorioHistorico.delete(historico);
		} catch (RepositorioException | EntidadNoEncontrada e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<Bicicleta> recuperarBiciEstacionadaPosicion(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Estacionamiento> recuperarEstacionSitiosTuristicos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encontrarEstacionLibre() throws RepositorioException {
		String idEstacion = RepositorioMemoriaEstacion.getEstacionLibre(repositorioEstacion);
		return idEstacion;

	}

}
