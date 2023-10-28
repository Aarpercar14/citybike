package servicio;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;

import repositorio.RepositorioException;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dominio.DistanciaCoordenadas;
import dominio.RepositorioSitiosTuristicosJSON;
import dominio.ResumenSitioTuristico;
import dominio.SitioTuristico;

public class ServicioSitiosTuristicos implements IServicioSitiosTuristicos {

	private RepositorioSitiosTuristicosJSON repositorioJSON = FactoriaRepositorios
			.getRepositorio(SitioTuristico.class);

	public String crear(String uRL) {
		URL url;
		System.out.println(uRL);
		try {
		String regex = "https://en\\.wikipedia\\.org/wiki/(.*)";
		String nuevaUrlBase = "https://es.dbpedia.org/data/";
		String nuevaUrl = uRL.replaceFirst(regex, nuevaUrlBase + "$1");
		url = new URL(nuevaUrl+".json");
		
		InputStreamReader objeto = new InputStreamReader(url.openStream());
		JsonReader jsonReader = Json.createReader(objeto);
		JsonObject obj = jsonReader.readObject();
		String regex2="https://es.dbpedia.org/data/(.*)";
		String nuevoRegex2="http://es.dbpedia.org/resource/";
		String nuevaUrl2=nuevaUrl.replaceFirst(regex2, nuevoRegex2+"$1");
		JsonObject obj2= obj.getJsonObject(nuevaUrl2);
		if(obj.isEmpty()) { 
			return  "Sitio sin pagina en dbpedia\n";
		}
		JsonObject nombreObj = obj2.getJsonArray("http://www.w3.org/2000/01/rdf-schema#label").getJsonObject(0).asJsonObject();
		JsonObject resumenObj = obj2.getJsonArray("http://dbpedia.org/ontology/abstract").getJsonObject(0).asJsonObject();
		JsonArray categoriasObj = obj2.getJsonArray("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		
		JsonObject enlaceExternoObj = obj2.getJsonArray("http://dbpedia.org/ontology/wikiPageExternalLink").getJsonObject(0).asJsonObject();		
		JsonObject imagenObj = obj2.getJsonArray("http://es.dbpedia.org/property/imagen").getJsonObject(0).asJsonObject();

		String nombre = nombreObj.getString("value");
		String resumen = resumenObj.getString("value");
		String categorias = "";
		for(int i =0;i<categoriasObj.size();i++) {
			categorias=categorias + categoriasObj.getJsonObject(i).asJsonObject().getString("value")+"; ";
		}
		String enlaceExterno = enlaceExternoObj.getString("value");
		String imagen = imagenObj.getString("datatype","no hay imagen");

		SitioTuristico sitio = new SitioTuristico(nombre,resumen,enlaceExterno,categorias,imagen,nuevaUrl);			
		try {
			String id = repositorioJSON.add(sitio);
			return id;
		} catch(RepositorioException e) {
			e.printStackTrace();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public List<ResumenSitioTuristico> getSitiosInteres(String cordX1, String cordY1) throws SAXException, ParserConfigurationException {
		String sitios = "http://api.geonames.org/findNearbyWikipedia?lat=" + cordX1 + "&lng=" + cordY1
				+ "&username=aadd";
		try {
			URL url = new URL(sitios);
			try {
				InputStream is = url.openStream();
				DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
				DocumentBuilder analizador = factoria.newDocumentBuilder();
				InputSource inputSource = new InputSource(is);
				Document documento = analizador.parse(inputSource);
				NodeList nodos = documento.getElementsByTagName("entry");
				LinkedList<ResumenSitioTuristico> listaResumen = new LinkedList<ResumenSitioTuristico>();
				for (int i = 0; i < nodos.getLength(); i++) {
					Element elemento = (Element) nodos.item(i);
					double cordX2 = Double.parseDouble(elemento.getElementsByTagName("lat").item(0).getTextContent());
					double cordY2 = Double.parseDouble(elemento.getElementsByTagName("lng").item(0).getTextContent());
					DistanciaCoordenadas distancia = DistanciaCoordenadas.obtenerDistancia(Double.parseDouble(cordX1),
							Double.parseDouble(cordY1), cordX2, cordY2);
					ResumenSitioTuristico s = new ResumenSitioTuristico(
							elemento.getElementsByTagName("title").item(0).getTextContent(),
							elemento.getElementsByTagName("summary").item(0).getTextContent(), distancia,
							elemento.getElementsByTagName("wikipediaUrl").item(0).getTextContent());
					listaResumen.add(s);
				}
				return listaResumen;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getInfoSitio(String id) {
		try {
			return repositorioJSON.getById(id).toString();
		} catch (RepositorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntidadNoEncontrada e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}