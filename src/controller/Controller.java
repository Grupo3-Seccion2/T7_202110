package controller;

import java.util.Scanner;

import model.data_structures.GrafoListaAdyacencia;
import model.logic.Model;
import view.View;

public class Controller 
{
	/**
	 *  Instancia del Modelo
	 */
	private Model modelo;
	
	/**
	 *  Instancia de la Vista
	 */
	private View view;
	
	/**
	 * Crear la vista y el modelo del proyecto
	 */
	public Controller ()
	{
		modelo = new Model();
		view = new View();
		
	}

	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		view.printMenu();
		while( !fin )
			{
				int option = lector.nextInt();
				switch(option)
				{
					case 1:
						try
						{
							
							modelo.cargarLandingPoints();
							modelo.cargaConectionsLandingPointsSub();
							modelo.cargaCountries();
							view.printMessage("Termino la carga de datos!!");
							GrafoListaAdyacencia<Integer, String> grafo = modelo.darGrafo();
							view.printMessage("Se tiene una totalidad de arcos de "+grafo.numEdges());
							view.printMessage("Se tiene una totalidad de puntos de conexion de "+grafo.numVertices());
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						break;
					case 2:
						view.printMessage("Ingresar el nombre del punto de conexion:");
						String nombre = lector.next();
						int num = modelo.darGradoConectividadDePunto(nombre);
						view.printMessage("El punto de conexion tiene "+ num+" puntos de conexion adjacentes");
						break;
							
					case 3:
						view.printMessage("--------- \n Hasta pronto !! \n---------"); 
						lector.close();
						fin = true;
						break;
				
				}
			}
		
	}

}
