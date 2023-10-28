package pruebas;

import java.time.LocalDate;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import dominio.Administrador;
import dominio.ResumenSitioTuristico;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;


public class PruebaEntrega1 {

	public static void main(String[] args) throws RepositorioException, EntidadNoEncontrada, SAXException, ParserConfigurationException {
		Administrador admin=new Administrador("admin", "jefe", "admin@gmail.com", "password", "612345678", LocalDate.now());
		String idEstacion = admin.DarDeAltaEstacion("Murcia Centro", 10, "30020", "38", "-1");
		System.out.println("El administrador ha creado la estacion con id: " + idEstacion);
		
		List<ResumenSitioTuristico> sitios = admin.getSitiosTuristicos(idEstacion);
		System.out.println();
		System.out.println("Lista de resúmenes de sitios turisticos extraida usando el api geoNames { ");
		System.out.println();
		for(ResumenSitioTuristico sitio : sitios) {
			System.out.println("   " + sitio.toString());
		}
		System.out.println();	
		System.out.println("}");
		
		
		admin.EstablecerSitioTuristico(idEstacion, sitios);
		
		if(admin.getEstacionamiento(idEstacion).getSitiosTuristicos().equals(sitios))
			System.out.println();
			System.out.println("Las listas de sitios de getSitiosTuristicos() es igual a la lista ");
			System.out.println("que almacena el método establecerSitiosTuristicos() en el estacionamiento");
		
		
		
		
	}

}
