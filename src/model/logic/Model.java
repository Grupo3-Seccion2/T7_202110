package model.logic;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import model.data_structures.ArregloDinamico;
import model.data_structures.GrafoListaAdyacencia;
import model.data_structures.ILista;
import model.data_structures.TablaHashLinearProbing;
import model.data_structures.Vertex;

public class Model 
{
	private GrafoListaAdyacencia<Integer,String > grafo;
	private TablaHashLinearProbing<String, Integer> puntosDeConexion;
	private Haversine haversine;
	
	public Model()
	{
		grafo = new GrafoListaAdyacencia<Integer,String >(5000);
		puntosDeConexion = new  TablaHashLinearProbing<>(5000, 0.5);
		haversine = new Haversine();
		
	}
	
	public void cargarLandingPoints() throws NumberFormatException, ParseException
	{
		int i = 1;
		try
		{
			
			Reader in = new FileReader("./data/landing_points.csv");
			Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
			for(CSVRecord record:records)
			{
				int id = Integer.parseInt(record.get(0));
				String idNom= record.get(1);
				String[] loc = record.get(2).split(",");
				String ciudad = loc[0];
				String pais = loc.length==2?loc[1].trim():loc[2].trim();
				double lat = Double.parseDouble(record.get(3));
				double longt = Double.parseDouble(record.get(4));
				
				LandingPointSub<Integer, String> actual = new LandingPointSub<>(id, idNom, pais, ciudad, lat, longt);
				puntosDeConexion.put(idNom, id);
				grafo.insertVertex(id, actual);
				i++;
			}
		}
		catch(Exception e)
		{
			System.out.println(i);
			e.printStackTrace();
		}
	}
	
	public void cargaConectionsLandingPointsSub() throws NumberFormatException, ParseException
	{
		
		try
		{
			Reader in = new FileReader("./data/connections.csv");
			Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
			for(CSVRecord record:records)
			{
				int idOrigin = Integer.parseInt(record.get(0));
				int idDestination = Integer.parseInt(record.get(1));
				LandingPointSub<Integer,String> origin = (LandingPointSub<Integer,String>)grafo.getVertex(idOrigin);
				LandingPointSub<Integer,String> dest =(LandingPointSub<Integer,String>) grafo.getVertex(idDestination);
				float distance = (float) haversine.distance(origin.darLatitud(), origin.darLongitud(), dest.darLatitud(), dest.darLongitud());
				grafo.addEdge(idOrigin, idDestination, distance);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void cargaCountries()throws NumberFormatException, ParseException
	{
		ILista<Vertex<Integer,String>> vertices = grafo.vertices();
		int i = 1;
		try
		{
			Reader in = new FileReader("./data/countries.csv");
			Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
			for(CSVRecord record:records)
			{
				if(!record.get(1).isBlank())
				{
					String pais = record.get(0);
					String capital = record.get(1);
					double lat = Double.parseDouble(record.get(2)); 
					double longt = Double.parseDouble(record.get(3));
					LandingPointTerr<Integer, String> actual = new LandingPointTerr<>(i, capital+"-"+pais, pais, capital, lat, longt);
					puntosDeConexion.put(capital+"-"+pais, i);
					grafo.insertVertex(i, actual);
					for(int j = 1;j<= vertices.size();j++)
					{
						LandingPointSub<Integer,String> actualV = (LandingPointSub<Integer,String>) vertices.getElement(j);
						String pais1 = actualV.darPais();
						if(pais.compareToIgnoreCase(pais1)==0)
						{
							float dis = (float) haversine.distance(actualV.darLatitud(), actualV.darLongitud(),lat, longt);
							grafo.addEdge(actualV.getId(), i, dis);
						}
					}
					i++;
				}
				System.out.println(grafo.numVertices());
			}
		}
		catch(Exception e)
		{
			System.out.println(i);
			e.printStackTrace();
		}
	}
	
	public GrafoListaAdyacencia<Integer, String> darGrafo()
	{
		return grafo;
	}
	public int darGradoConectividadDePunto(String nombre)
	{
		int llave = puntosDeConexion.get(nombre);
		Vertex<Integer, String> buscado = grafo.getVertex(llave);
		return buscado != null?buscado.vertices().size():0;
	}
}

