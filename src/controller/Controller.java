package controller;

import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.xml.internal.ws.util.StringUtils;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.arrays.IteratorAction;
import model.data_structures.DoubleLinkedList;
import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.Nodo;
import model.data_structures.Queue;
import model.data_structures.Stack;
import model.vo.VOMovingViolations;
import model.vo.VOViolationCode;
import sun.reflect.generics.tree.VoidDescriptor;
import view.MovingViolationsManagerView;

/**
 * Esta clase representa el controlador de los datos
 */
public class Controller {
	
	//---------------------------------------------------------------------------------------------------
	// Constantes
	// --------------------------------------------------------------------------------------------------

	/**
	 * Constante que representa los datos de las infracciones realizadas en Enero
	 */
	public static final String DATOS_ENERO = "./data/Moving_Violations_Issued_in_January_2018.json";
	
	/**
	 * Constante que representa los datos de las infracciones realizadas en Febrero
	 */
	public static final String DATOS_FEBRERO = "./data/Moving_Violations_Issued_in_February_2018.json";
	
	/**
	 * Constante que representa los datos de las infracciones realizadas en Marzo
	 */
	public static final String DATOS_MARZO = "./data/Moving_Violations_Issued_in_March_2018.json";
	
	/**
	 * Constante que representa los datos de las infracciones realizadas en Abril
	 */
	public static final String DATOS_ABRIL = "./data/Moving_Violations_Issued_in_April_2018.json";
	
	/**
	 * Constante que representa los datos de las infracciones realizadas en Mayo
	 */
	public static final String DATOS_MAYO = "./data/Moving_Violations_Issued_in_May_2018.json";
	
	/**
	 * Constante que representa los datos de las infracciones realizadas en Junio
	 */
	public static final String DATOS_JUNIO = "./data/Moving_Violations_Issued_in_June_2018.json";
	
	//--------------------------------------------------------------------------------------------------
	// Atributos
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * Es el formato que se usara para las fechas
	 */
	//private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	/**
	 * Es la referencia al view
	 */
	private MovingViolationsManagerView view;
	
	/**
	 * Lista donde se van a cargar los datos de los archivos
	 */
	private DoubleLinkedList<VOMovingViolations> movingViolationsList;
	
	/**
	 * Cautrimestre del cual se subiran los datos - 1(Enero - Abril), 2(Mayo - Agosto) o 3(Septiembre - Diciembre)
	 */
	public int cuatrimestre;
	
	//-----------------------------------------------------------------------------------------------
	// Constructores
	// ----------------------------------------------------------------------------------------------
	
	/**
	 * Construye un nuevo controlador con una pila y una cola.
	 * <b>post:</b> se construyo  un nuevo controlador con una cola y una pila
	 * La pila esta vacía <br>
	 * La cola esta vacía <br> 
	 */
	public Controller() {
		view = new MovingViolationsManagerView();
		
		movingViolationsList = null;
	}
	
	/**
	 * Lee la opcion que el usuario escoja para el desarrollo del programa
	 * @throws Exception 
	 */
	public void run() throws Exception {
		Scanner sc = new Scanner(System.in);
		boolean fin=false;
		Controller controller = new Controller();
		
		while(!fin)
		{
			view.printMenu();
			
			int option = sc.nextInt();
			
			switch(option)
			{
				case 0:
					view.printMessage("Ingrese el cuatrimestre (1, 2 o 3)");
					int numeroCuatrimestre = sc.nextInt();
					controller.loadMovingViolations(numeroCuatrimestre);
					break;
				
				case 1:
					boolean isUnique = controller.verifyObjectIDIsUnique();
					view.printMessage("El objectId es Ãºnico: " + isUnique);
					break;
					
				case 2:
					
					view.printMessage("Ingrese la fecha con hora inicial (Ej : 28/03/2017T15:30:20)");
					LocalDateTime fechaInicialReq2A = convertirFecha_Hora_LDT(sc.next());
					
					view.printMessage("Ingrese la fecha con hora final (Ej : 28/03/2017T15:30:20)");
					LocalDateTime fechaFinalReq2A = convertirFecha_Hora_LDT(sc.next());
					
					IQueue<VOMovingViolations> resultados2 = controller.getMovingViolationsInRange(fechaInicialReq2A, fechaFinalReq2A);
					
					view.printMovingViolationsReq2(resultados2);
					
					break;
					
				case 3:
					
					view.printMessage("Ingrese el VIOLATIONCODE (Ej : T210)");
					String violationCode3 = sc.next();
					
					double [] promedios3 = controller.avgFineAmountByViolationCode(violationCode3);
					
					view.printMessage("FINEAMT promedio sin accidente: " + promedios3[0] + ", con accidente:" + promedios3[1]);
					break;
						
					
				case 4:
					
					view.printMessage("Ingrese el ADDRESS_ID");
					String addressId4 = sc.next();

					view.printMessage("Ingrese la fecha con hora inicial (Ej : 28/03/2017)");
					LocalDate fechaInicialReq4A = convertirFecha(sc.next());
					
					view.printMessage("Ingrese la fecha con hora final (Ej : 28/03/2017)");
					LocalDate fechaFinalReq4A = convertirFecha(sc.next());
					
					IStack<VOMovingViolations> resultados4 = controller.getMovingViolationsAtAddressInRange(addressId4, fechaInicialReq4A, fechaFinalReq4A);
					
					view.printMovingViolationsReq4(resultados4);
					
					break;
					
				case 5:
					view.printMessage("Ingrese el limite inferior de FINEAMT  (Ej: 50)");
					double limiteInf5 = sc.nextDouble();
					
					view.printMessage("Ingrese el limite superior de FINEAMT  (Ej: 50)");
					double limiteSup5 = sc.nextDouble();
					
					IQueue<VOViolationCode> violationCodes = controller.violationCodesByFineAmt(limiteInf5, limiteSup5);
					view.printViolationCodesReq5(violationCodes);
					break;
				
				case 6:
					
					view.printMessage("Ingrese el limite inferior de TOTALPAID (Ej: 200)");
					double limiteInf6 = sc.nextDouble();
					
					view.printMessage("Ingrese el limite superior de TOTALPAID (Ej: 200)");
					double limiteSup6 = sc.nextDouble();
					
					view.printMessage("Ordenar Ascendentmente: (Ej: true)");
					boolean ascendente6 = sc.nextBoolean();				
					
					IStack<VOMovingViolations> resultados6 = controller.getMovingViolationsByTotalPaid(limiteInf6, limiteSup6, ascendente6);
					view.printMovingViolationReq6(resultados6);
					break;
					
				case 7:
					
					view.printMessage("Ingrese la hora inicial (Ej: 23)");
					int horaInicial7 = sc.nextInt();
					
					view.printMessage("Ingrese la hora final (Ej: 23)");
					int horaFinal7 = sc.nextInt();
					
					IQueue<VOMovingViolations> resultados7 = controller.getMovingViolationsByHour(horaInicial7, horaFinal7);
					view.printMovingViolationsReq7(resultados7);
					break;
					
				case 8:
					
					view.printMessage("Ingrese el VIOLATIONCODE (Ej : T210)");
					String violationCode8 = sc.next();
					
					double [] resultado8 = controller.avgAndStdDevFineAmtOfMovingViolation(violationCode8);
					
					view.printMessage("FINEAMT promedio: " + resultado8[0] + ", desviaciÃ³n estandar:" + resultado8[1]);
					break;
					
				case 9:
					
					view.printMessage("Ingrese la hora inicial (Ej: 23)");
					int horaInicial9 = sc.nextInt();
					
					view.printMessage("Ingrese la hora final (Ej: 23)");
					int horaFinal9 = sc.nextInt();
					
					int resultado9 = controller.countMovingViolationsInHourRange(horaInicial9, horaFinal9);
					
					view.printMessage("NÃºmero de infracciones: " + resultado9);
					break;
				
				case 10:
					view.printMovingViolationsByHourReq10(getGraficaPorcentaje());
					break;
					
				case 11:
					view.printMessage("Ingrese la fecha inicial (Ej : 28/03/2017)");
					LocalDate fechaInicial11 = convertirFecha(sc.next());
					
					view.printMessage("Ingrese la fecha final (Ej : 28/03/2017)");
					LocalDate fechaFinal11 = convertirFecha(sc.next());
					
					double resultados11 = controller.totalDebt(fechaInicial11, fechaFinal11);
					view.printMessage("Deuda total "+ resultados11);
					break;
				
				case 12:	
					view.printTotalDebtbyMonthReq12(null);
					
					break;
					
				case 13:	
					fin=true;
					sc.close();
					break;
			}
		}

	}

	
	/**
	 * Carga los datos segun el cuatrimestre escogido por el usuario
	 * @param pCuatrimestre Numero de cuatrimestre escogido por el usuario. pCuatrimestre = 1 | pCuatrimestre = 2 | pCuatrimestre = 3
	 * @throws Exception si no pudo cargar los datos.
	 */
	public void loadMovinViolations(String pJson) {
		JsonParser parser = new JsonParser();
		int i = 0;
		
		try {
			JsonArray array = (JsonArray) parser.parse(new FileReader(pJson));
			Iterator iter = array.iterator();
			
			while(iter.hasNext()) {
				JsonObject object = (JsonObject) array.get(i);
				
				String objectId = " ";
				if(object.get("OBJECTID") != null) {
					objectId = object.get("OBJECTID").toString();
				}
				
				String row = " ";
				if(object.get("ROW_") != null) {
					row = object.get("ROW_").toString();
				}
				
				String location = " ";
				if(object.get("LOCATION") != null) {
					location = object.get("LOCATION").toString();
				}
				
				String addressId = " ";
				if(object.get("ADDRESS_ID") != null) {
					addressId = object.get("ADDRESS_ID").toString();
				}
				
				String streetSegId = " ";
				if(object.get("STREETSEGID") != null) {
					streetSegId = object.get("STREETSEGID").toString();
				}
				
				String xCoord = " ";
				if(object.get("XCOORD") != null) {
					xCoord = object.get("XCOORD").toString();
				}
				
				String yCoord = " ";
				if(object.get("YCOORD") != null) {
					yCoord = object.get("YCOORD").toString();
				}
				
				String ticketType = " ";
				if(object.get("TICKETTYPE") != null) {
					ticketType = object.get("TICKETTYPE").toString();
				}
				
				String fineAMT = " ";
				if(object.get("FINEAMT") != null) {
					fineAMT = object.get("FINEAMT").toString();
				}
				
				String totalPaid = " ";
				if(object.get("TOTALPAID") != null) {
					totalPaid = object.get("TOTALPAID").toString();
				}
				
				String penalty1 = " ";
				if(object.get("PENALTY1") != null) {
					penalty1 = object.get("PENALTY1").toString();
				}
				
				String penalty2 = " ";
				if(object.get("PENALTY2") != null) {
					penalty2 = object.get("PENALTY2").toString();
				}
				
				String accidentIndicator = " ";
				if(object.get("ACCIDENTINDICATOR") != null) {
					accidentIndicator = object.get("ACCIDENTINDICATOR").toString();
				}
				
				String ticketIssueDate = " ";
				if(object.get("TICKETISSUEDATE") != null) {
					ticketIssueDate = object.get("TICKETISSUEDATE").toString();
				}
				
				String violationCode = " ";
				if(object.get("VIOLATIONCODE") != null) {
					violationCode = object.get("VIOLATIONCODE").toString();
				}
				
				String violationDesc = " ";
				if(object.get("VIOLATIONDESC") != null) {
					violationDesc = object.get("VIOLATIONDESC").toString();
				}
				
				String rowId = " ";
				if(object.get("ROW_ID") != null) {
					rowId = object.get("ROW_ID").toString();
				}
				
				movingViolationsList.agregar(new VOMovingViolations(Integer.parseInt(objectId), location, Integer.parseInt(addressId), Integer.parseInt(streetSegId), Integer.parseInt(xCoord), Integer.parseInt(yCoord), ticketType, Integer.parseInt(fineAMT), Integer.parseInt(totalPaid), Integer.parseInt(penalty1), Integer.parseInt(penalty2), accidentIndicator, ticketIssueDate, violationCode, violationDesc, Integer.parseInt(rowId)));
				
				iter.next();
				i++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------------------------------------------------
	// Métodos
	//------------------------------------------------------------------------------------------
	
	/**
	 * Verifica si existen objetos que tienen el mismo ObjectId
	 * @return True||false
	 */
	public boolean verifyObjectIDIsUnique() {
		Nodo<VOMovingViolations> x = movingViolationsQueue.getNodoFirst();
		Nodo<VOMovingViolations> aux = null;
		VOMovingViolations item = x.item;
		VOMovingViolations auxItem = null;
		int id = 0;
		int auxId = 0;
		boolean hay = false;
		
		while(x.siguiente!=null) {
			aux = x.siguiente;
			id = item.getObjectId();
			boolean ya = false;
			while(aux.siguiente != null || !ya) {
				auxItem = aux.item;
				auxId = auxItem.getObjectId();
				if(id==auxId) {
					ya = true;
					hay = true;
				}
			}
			if(!hay) {
				System.out.println(id);
			}
			x = x.siguiente;
		}
		return hay;
	}
	
	/**
	 * Busca las infracciones dentro del rango de fecha inicial y final establecido.
	 * @param pFecha1 Fecha inicial para establecer la busqueda.
	 * @param pFecha2 Fecha final paara terminar la busqueda.
	 * @return Cola de infracciones.
	 */
	public IQueue<VOMovingViolations> getMovingViolationsInRange(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
		// TODO Auto-generated method stub
		IQueue<VOMovingViolations> queue = new Queue<VOMovingViolations>();
		Iterator<VOMovingViolations> it = movingViolationsQueue.iterator();
		VOMovingViolations x = null;
		
		while(it.hasNext()) {
			x = it.next();
			if(fechaInicial.isBefore(convertirFecha_Hora_LDT(x.getTicketIssueDate())) && fechaFinal.isAfter(convertirFecha_Hora_LDT(x.getTicketIssueDate())) || fechaInicial.isEqual(convertirFecha_Hora_LDT(x.getTicketIssueDate())) || fechaFinal.isEqual(convertirFecha_Hora_LDT(x.getTicketIssueDate()))) {
				queue.enqueue(x);
			}
		}
		return queue;
	}
	
	/**
	 * Busca la infraccion con el codigo recibido y retorna el FINEAMT promedio cuando hubo y no hubo accidente.
	 * @param violationCode Codigo de la infracción a buscar. pViolationCode != null && pViolationCode != "".
	 * @return FINEAMTs promedio.
	 */
	public double[] avgFineAmountByViolationCode(String violationCode) {
		double[] resultado = new double[2];
		Iterator<VOMovingViolations> it = movingViolationsQueue.iterator();
		VOMovingViolations x = null;
		double accidente = 0.0;
		int a = 0;
		double noAccidente = 0.0;
		int nA = 0;
		
		while(it.hasNext()) {
			x = it.next();
			if(x.getViolationCode().equals(violationCode)) {
				if(x.getAccidentIndicator().equals("Yes")) {
					a++;
					accidente += x.getFineAMT();
				}
				if(x.getAccidentIndicator().equals("No")) {
					nA++;
					noAccidente += x.getFineAMT();
				}
			}
		}
		resultado[1] = accidente/a;
		resultado[0] = noAccidente/nA;
		
		return resultado;
	}
	
	/**
	 * Busca las infracciones que se cometieron en una dirección, dentro de un rango de fechas y las retorna en una pila.
	 * @param addressId Dirección en la que se desea hacer realizar la busqueda.
	 * @param fechaInicial Fecha inicial en la que se desea realizar la busqueda. pFecha1 != null && pFecha2 != ""
	 * @param fechaFinal Fecha final en la que se desea realizar la busqueda. pFecha2 != null && pFecha2 != ""
	 * @return Pila de infracciones.
	 */
	public IStack<VOMovingViolations> getMovingViolationsAtAddressInRange(String addressId, LocalDate fechaInicial, LocalDate fechaFinal) {
		// TODO Auto-generated method stub
		IStack<VOMovingViolations> stack = new Stack<VOMovingViolations>();
		Nodo<VOMovingViolations> x = movingViolationsQueue.getNodoFirst();
		VOMovingViolations item = null;
		
		while(x.siguiente!=null) {
			item = x.item;
			String sFecha = item.getTicketIssueDate();
			String[] string = sFecha.split("\\'");
			LocalDate fecha = convertirFecha(string[0]);
			if(item.getAdressId() == Integer.parseInt(addressId)) {
				if(fechaInicial.isBefore(fecha) && fechaInicial.isBefore(fecha) || fechaInicial.isEqual(fecha)) 
				{
					stack.push(item);
				}
			}
			x = x.siguiente;
		}
		return stack;
		
		//incompleto
	}
	
	/**
	 * Busca las infracciones que se encuentran dentro del rango de FINEAMT y las agrega a la cola.
	 * @param limiteMin FINEAMT minimo para agregar la infracción en la cola. minFINEAMT > 0
	 * @param limiteMax FINEAMT maximo para agregar la infracción en la cola. maxFINEAMT > minFINEAMT
	 * @return Cola de infracciones.
	 */
	public IQueue<VOViolationCode> violationCodesByFineAmt(double limiteMin, double limiteMax) {
		// TODO Auto-generated method stub
		IQueue<VOViolationCode> cumplen = new Queue<VOViolationCode>();
		Nodo<VOMovingViolations> x = movingViolationsQueue.getNodoFirst();
		VOMovingViolations n;
		while(x.siguiente!=null) {
			n = x.item;
			Nodo<VOMovingViolations> sig = x.siguiente;
			VOMovingViolations m = sig.item;
			VOViolationCode it = new VOViolationCode(n.getViolationCode(), n.getFineAMT());
			VOViolationCode aComp = new VOViolationCode(m.getViolationCode(), m.getFineAMT());
			if(m.compareTo(n)>0){
				if(aComp.getFINEAMTProm() >= limiteMin && aComp.getFINEAMTProm() <= limiteMax){ cumplen.enqueue(aComp);}
				if(it.getFINEAMTProm() >= limiteMin && it.getFINEAMTProm()<= limiteMax){ cumplen.enqueue(it);}
			}
			else if(m.compareTo(n)<0){
				if(it.getFINEAMTProm() >= limiteMin && it.getFINEAMTProm() <= limiteMax){ cumplen.enqueue(it);}
				if(aComp.getFINEAMTProm() >= limiteMin && aComp.getFINEAMTProm()<= limiteMax){ cumplen.enqueue(aComp);}
			}
			else if(m.compareTo(n)==0){
				if(aComp.getFINEAMTProm() >= limiteMin && aComp.getFINEAMTProm() <= limiteMax){ cumplen.enqueue(aComp);}
				if(it.getFINEAMTProm() >= limiteMin && it.getFINEAMTProm()<= limiteMax){ cumplen.enqueue(it);}
			}
			x = sig;
		}
		return cumplen;	}
	
	/**
	 * Busca las infracciones por el total pagado, dentro de un rango de pago.
	 * @param limiteMin Cantidad minima del TOTALPAID para agregar la infraccion en la pila. minTotalPaid > 0
	 * @param limiteMax Cantidad máxima del TOTALPAID para agregar la infracción en la pila. maxTotalPaid > minTotalPaid
	 * @param ascendente Indica si la pila se ordena ascendentemente o no.
	 * @return Pila de infracciones.
	 */
	public IStack<VOMovingViolations> getMovingViolationsByTotalPaid(double limiteMin, double limiteMax, boolean ascendente) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Busca las infracciones realizadas en un rango de hora final e inicial y las agrega en una cola.
	 * @param horaInicial Hora inicial del rango. horaInicial >= 0
	 * @param horaFinal Hora final del rango. horaFinal > horaInicial && horaInicial <= 24
	 * @return Cola de infracciones.
	 */
	public IQueue<VOMovingViolations> getMovingViolationsByHour(int horaInicial, int horaFinal) {
		// TODO Auto-generated method stub
		IQueue<VOMovingViolations> inRange = new Queue<VOMovingViolations>();
		Nodo<VOMovingViolations> x = movingViolationsQueue.getNodoFirst();
		VOMovingViolations item;
		
		while(x.siguiente!=null) {
			item = x.item;
			String sFecha = item.getTicketIssueDate();
			String[] string = sFecha.split("T");
			String[] str = string[1].split(":"); 
			int hora = Integer.parseInt(str[0]);
			if(hora >= horaInicial && hora <= horaFinal) {
				inRange.enqueue(item);
			}
			x = x.siguiente;
		}
		return inRange;
	}  
	
	/**
	 * Busca una infracción por su codigo y retorna su FINEAMT promedio y su desviación estandar de este.
	 * @param violationCode Codigo de la infracción a buscar. pViolationCode != null && pViolationCode != ""
	 * @return Arreglo con FINEAMTpromedio y la desviacion estandar.
	 */
	public double[] avgAndStdDevFineAmtOfMovingViolation(String violationCode) {
		// TODO Auto-generated method stub
		
		return new double [] {0.0 , 0.0};
	}
	
	/**
	 * Retorna el numero de infracciones cometidas dentro de un rango de horas.
	 * @param hora1 Hora en la que se inicia la búsqueda. pHora1 >= 0
	 * @param hora2 Hora en la que se termina la búsqueda. pHora2 > pHora1 && pHora2 <= 24
	 * @return Número de infracciones.
	 */
	public int countMovingViolationsInHourRange(int hora1, int hora2) {
		// TODO Auto-generated method stub
		Nodo<VOMovingViolations> x = movingViolationsQueue.getNodoFirst();
		VOMovingViolations item = null;
		int nInfracciones = 0;
		
		while(x.siguiente!=null) {
			item = x.item;
			String date[] = item.getTicketIssueDate().split("T");
			String hour[] = date[1].split(":");
			int hora = Integer.parseInt(hour[0]);
			if(hora >= hora1 && hora <= hora2) {
				nInfracciones++;
			}
			x = x.siguiente;
		}
		return nInfracciones;
	}
	
	/**
	 * Genera una grafica ASCII con los porcentajes correspondientes a las infracciones que tuvieron accidentes
	 * dentro del rango de horas del dia. El rango se define por valores enteros en [0, 24]
	 * @param args
	 */
	public String[] getGraficaPorcentaje () {
		String[] prints = new String[24];
		Nodo<VOMovingViolations> nodo = movingViolationsQueue.getNodoFirst();
		VOMovingViolations item = null;
		int i = 0;
		int nInfracciones = 0;
		double porcentaje = 0.0;
		while(i < 24) {
			item = nodo.item;
			String[] date = item.getTicketIssueDate().split("T");
			String[] horas = date[1].split(":");
			int hora = Integer.parseInt(horas[0]);
			
			if(hora == i) {
				nInfracciones++;
				if(item.getAccidentIndicator().equals("Yes")) {
					porcentaje++;
				}
			}
			if(nodo.siguiente==null) {
				porcentaje = porcentaje * 100 / nInfracciones;
				int escribir = 0;
				String x = "";
				while(escribir != porcentaje) {
					porcentaje = Math.round(porcentaje / 5);
					x += "X";
					prints[i] = x;
					escribir++;
					nodo = movingViolationsQueue.getNodoFirst();
				}
				i++;
			}
			nodo = nodo.siguiente;
		}
		return prints;
	}
	
	/**
	 * Calcula la deuda total por infracciones que se dieron en un rango de fechas
	 * @param fechaInicial Fecha desde la cual se quiere realizar la busqueda
	 * @param fechaFinal Fecha en la cual se termina la busqueda
	 * @return Deuda total.
	 */
	public double totalDebt(LocalDate fechaInicial, LocalDate fechaFinal) {
		// TODO Auto-generated method stub
		double deudaTotal = 0.0;
		Nodo<VOMovingViolations> nodo = movingViolationsQueue.getNodoFirst();
		VOMovingViolations item = null;
		
		while(nodo.siguiente!=null) {
			item = nodo.item;
			String[] date = item.getTicketIssueDate().split("T");
			String fecha = date[0];
			if(convertirFecha(fecha).isAfter(fechaInicial) && convertirFecha(fecha).isBefore(fechaFinal) || convertirFecha(fecha).equals(fechaInicial) || convertirFecha(fecha).isEqual(fechaFinal)) {
				deudaTotal += item.getTotalPaid() + item.getFineAMT() + item.getPenalty1() + item.getPenalty2();
			}
			nodo = nodo.siguiente;
		}
		return deudaTotal;
	}
	
	/**
	 * Genera una grafica ASCII con la deuda acumulada por infracciones en el cuatrimestre
	 */
	public String[] geGraficaASCIIdeudaAcumuladaTotal () {
		String[] prints = new String[4];
		
		int mes1 = 0;
		int mes2 = 0;
		int mes3 = 0;
		int mes4 = 0;
		
		if(cuatrimestre == 1) {
			mes1 = 1;
			mes2 = 2;
			mes3 = 3;
			mes4 = 4;
		}
		if(cuatrimestre == 2) {
			mes1 = 5;
			mes2 = 6;
			mes3 = 7;
			mes4 = 8;
		}
		if(cuatrimestre == 3) {
			mes1 = 9;
			mes2 = 10;
			mes3 = 11;
			mes4 = 12;
		}
		
		double deuda1 = totalDebt(convertirFecha("2018-" + mes1 + "-1"), convertirFecha("2018-" + mes1 + "-31"));
		double deuda2 = totalDebt(convertirFecha("2018-" + mes2 + "-1"), convertirFecha("2018-" + mes2 + "-31"));
		double deuda3 = totalDebt(convertirFecha("2018-" + mes3 + "-1"), convertirFecha("2018-" + mes3 + "-31"));
		double deuda4 = totalDebt(convertirFecha("2018-" + mes4 + "-1"), convertirFecha("2018-" + mes4 + "-31"));
		
		int i = 0;
		
		while(i != Math.round(deuda1 / 1500)) {
			i++;
			prints[0] += "X";
		}
		i = 0;
		
		while(i != Math.round(deuda2 / 1500)) {
			i++;
			prints[1] += "X";
		}
		i = 0;
		
		while(i != Math.round(deuda3 / 1500)) {
			i++;
			prints[2] += "X";
		}
		i = 0;
		
		while(i != Math.round(deuda4 / 1500)) {
			i++;
			prints[3] += "X";
		}
		
		return prints;
	}
	
	/**
	 * Convertir fecha a un objeto LocalDate
	 * @param fecha fecha en formato dd/mm/aaaa con dd para dia, mm para mes y aaaa para agno
	 * @return objeto LD con fecha
	 */
	private static LocalDate convertirFecha(String fecha)
	{
		return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyy-MM-dd"));
	}

	
	/**
	 * Convertir fecha y hora a un objeto LocalDateTime
	 * @param fecha fecha en formato dd/mm/aaaaTHH:mm:ss con dd para dia, mm para mes y aaaa para agno, HH para hora, mm para minutos y ss para segundos
	 * @return objeto LDT con fecha y hora integrados
	 */
	private static LocalDateTime convertirFecha_Hora_LDT(String fechaHora)
	{
		return LocalDateTime.parse(fechaHora, DateTimeFormatter.ofPattern("yyyy-MM-dd"+" T "+"kk:mm:ss"));
	}
}
