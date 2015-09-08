package Monticulo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.NoSuchElementException;


/******************************************* IMPLEMENTACION DE UN NONTICULO BINOMIAL *****************************************************/

public class Monticulo_binomial {
	
	//la cabeza
	private Nodo head;
	
	//Constructor del Monticulo Binomial
	public Monticulo_binomial(){
		head = null; // inicializamos head a null
	}
	
	/**Clase interna Nodo que contiene: 
	 * clave - key
	 * grado - degree 
	 * nodo padre - p
	 * nodo hijo - child 
	 * nodo hermano - sibling
	 * enlace - enlace del nodo asociado*/
	private static class Nodo{
		
		int key; // clave del nodo
		int degree; // grado del nodo - cuantos hijos tiene
		Nodo p; //padre del nodo
		Nodo child; // hijo del nodo
		Nodo sibling; // hermano del nodo
		Enlace enlace;// enlace que tiene asociado un nodo
		
		//constructora Nodo con un parámetro
		public Nodo(int k)
		{
			key = k; //la clave es la que le pasamos al nodo
			degree = 0; // inicializamos el grado a 0
			// nodo padre, hijo y hermano lo inicializamos a null
			p = null;
			child = null;
			sibling = null;
			//creamos un nuevo enlace del nodo
			enlace = new Enlace(this);
		}
	} 
	
	/**Hacemos un clase interna llamada Enlace, 
	 * que guarda un enlace de un nodo a otro nodo, el enlace establece la conexión entre dos nodos*/
	private static class Enlace{
		@SuppressWarnings("unused")
		Nodo nodo;	
		// Constructora de Enlace, le pasamos el nodo sucesor
		public Enlace(Nodo n){
			nodo = n; 
		}
	}

	/*************************************** OPERACIONES DE UN MONTICULO BINOMIAL **************************************************/
	
	
	/**Recorre la lista de árboles comparando el valor de minimo hasta entonces 
	 * con el siguiente, en este caso el hermano, y cuando termina devuelve 
	 * el que tiene la clave mínima*/
	public int minimo(){
		//Comprueba que head no sea null, si es null devolvemos error si no buscamos el nodo con la clave minima y lo devolvemos
		if (head == null) throw new NoSuchElementException("No hay mínimo, monticulo vacio");
		else
		{
			Nodo minimo = head; // ponemos como minimo el head
			Nodo x = minimo.sibling; // hermano del minimo
			while(x != null)
			{
				// comparamos si es menor y intercambiamos, si no es que ya ha encontrado el nodo con la clave minima
				if (x.key < minimo.key) 
					minimo = x;
				x = x.sibling;
			}
		
			return minimo.key;	//devolvemos la clave minima
		}
	}
	
	/**Enlazar dos monticulos binomiales*/
	private void binomial_Link(Nodo y, Nodo z)
	{
		y.p = z; // z padre de Y 
		y.sibling = z.child; // el hermano de Y es el hijo de Z
		z.child = y; // Y es hijo de Z
		z.degree = z.degree + 1; // aumentamos el grado en uno 
	}
	
	
	/**Union de dos monticulos binomiales*/
	private Monticulo_binomial union(Monticulo_binomial monticulo) 
	{
		//creamos un nuevo monticulo
		Monticulo_binomial nuevo_monticulo = new Monticulo_binomial();
		
		//hacemos merge de los dos monticulos
		nuevo_monticulo.head = Merge(this,monticulo);
		
		//libero objetos
		head = null; 
		monticulo.head = null;
		
		// si es null devolvemos el monticulo
		if (nuevo_monticulo.head == null) return nuevo_monticulo;
		
		// si no es null comprobamos cada caso del monticulo para poder unirlo
		casos(nuevo_monticulo);
		
		return nuevo_monticulo; // devolvemos la union de los dos monticulos
	}
	
	/**Verifica cada uno de los casos, mientras el siguiente al nodo actual no sea nul */
	private void casos(Monticulo_binomial nuevo_monticulo){
		Nodo prev_x = null; // predecesor de X
		Nodo x = nuevo_monticulo.head; // X actual
		Nodo siguiente_x = x.sibling; // siguiente de X
		
		//mientras el siguiente nodo X sea distinto de null
		while(siguiente_x != null)
		{
			//entra cuando no tienen el mismo grado el nodo actual y el siguiente, pero si entra si tienen el mismo grado el nodo actual y el hermano del 
			//siguiente nodo, siempre y cuando el hermano del nodo siguiente no sea null
			if(x.degree != siguiente_x.degree || (siguiente_x.sibling !=null && siguiente_x.sibling.degree == x.degree)){
				prev_x = x; 			// predecesor es ahora X															Caso 1 y 2
				x = siguiente_x; 		//avanzamos X																		Caso 1 y 2
			}
			else {
				// si el nodo actual es menor o igual que el siguiente nodo, entonces el nodo x pasa a hacer hijo del siguiente nodo
				if(x.key <= siguiente_x.key){ 
						x.sibling = siguiente_x.sibling; 																	//Caso 3
						binomial_Link(siguiente_x,x); 																		//Caso 3
				} 
				//si es mayor el siguiente nodo pasa a hacer hijo del nodo x 
				else{
					//si el predecesor es null, el head del nuevo monticulo es siguiente de X
					if(prev_x == null) nuevo_monticulo.head = siguiente_x; 													//Caso 4
					
					else prev_x.sibling=siguiente_x; 																		//Caso 4
					
					binomial_Link(x,siguiente_x); 																			//Caso 4
					x=siguiente_x; 																							//Caso 4
				 }
			}
			siguiente_x = x.sibling; 
		}
		
	}
	
	
	/**Ordena los dos monticulos binomiales en uno solo. Devuele la cabeza del monticulo ordenado*/
	private static Nodo Merge(Monticulo_binomial m, Monticulo_binomial mont) {
		
		//Comprobar si los dos monticulos están vacios
		if (m.head == null) return mont.head;
		else if(mont.head == null) return m.head;
		else { // si no estan vacios
			Nodo h; //head
			Nodo ultimo; //ultimo nodo del monticulo ordenado
			Nodo sig_m = m.head;
			Nodo sig_mont = mont.head;
			
			//Comprobamos si el grado del head es menor o mayor, se actualiza el head con el monticulo que 
			//tenga el menor grado de head
			if(m.head.degree <= mont.head.degree){
				h = m.head;
				sig_m = sig_m.sibling;
			} else {
				h = mont.head;
				sig_mont = sig_mont.sibling;
			}
			ultimo = h; //pone en el ultimo nodo el head del monticulo
			//ir a través de dos monticulos
			aux(sig_m, sig_mont, ultimo);
			return h; //devuelve head
		}
		
	}
	/**Ir a través de los dos monticulos, mientras ninguno sea null */
	public static void aux(Nodo sig_m, Nodo sig_mont, Nodo ultimo){

		while(sig_m !=null && sig_mont !=null)
		{
			//comprobamos los grados, si es menor actualizamos el hermano del ultimo
			if(sig_m.degree <= sig_mont.degree)
			{
				ultimo.sibling = sig_m;
				sig_m = sig_m.sibling;
			}
			else{ 
				ultimo.sibling = sig_mont;
				sig_mont = sig_mont.sibling;
			}
			ultimo = ultimo.sibling;
		}
		if(sig_m !=null) ultimo.sibling = sig_m;
		else ultimo.sibling = sig_mont;
	}

	/**Extraer el minimo de la lista*/
	public int extraerMinimo(){
		
		if (head == null)  throw new NoSuchElementException("Monticulo Vacio");
		
		//Buscar la raiz x con la clave minima en la lista
		Nodo x = head; // nodo con la clave minima
		Nodo y = x.sibling; // nodo actual
		Nodo pred_y = x; //predecesor de y
		Nodo pred_x = null; // predecesor de x
		
		while(y != null)
		{
			// si la clave de y es menor que la de x, se intercambian 
			if (y.key < x.key) 
			{
				x = y;
				pred_x = pred_y;
			}
			pred_y = y;
			y = y.sibling;		
		}
		//eliminar x de la lista
		eliminarNodo(x,pred_x);
		
		//quitamos el enlace asociado al minimo
		x.enlace = null;
		System.out.println("Extraemos el mínimo: " + x.key);
		return x.key;// devolvemos la clave del nodo x
		
	}
	
	/**Insertar un elemento en un monticulo binomial*/
	public Object insertar(int k)
	{
		Monticulo_binomial monticulo = new Monticulo_binomial(); //creamos un nuevo monticulo binomial para insertar
		Nodo x = new Nodo(k); // creamos un nuevo nodo
		monticulo.head = x; // a head le asignamos el nodo x
		Monticulo_binomial m = (Monticulo_binomial) this.union(monticulo); // unimos los monticulos 
		head = m.head; // actualizamos el valor del head
		//Mostramos los nodos insertados
		System.out.println("Key: " + x.key + "; " + "Degree: " + x.degree + "\n");
		if(x.p!= null) { System.out.println("Padre: " + x.p.key + "\n");
			System.out.println("**** Nuevo montículo ****" + "\n" + "Head: " + m.head.key + "\n" + "Primer Hijo: " + m.head.child.key + "\n");
		}
		return x.enlace; // devolvemos el enlace del nodo 
		
	}
	
	/**decrementamos la clave del nodo de un monticulo binomial*/
	public void decrease_Key(Nodo n,int k) throws Exception
	{
		Nodo nodo_x = n;
		Nodo nodo_y, nodo_z;
		
		// si k es mayor que la clave del nodo x lanzamos error
		if (k > nodo_x.key) throw new Exception("La nueva clave es mayor que clave actual");
		nodo_x.key = k;
		
		nodo_y = nodo_x;
		nodo_z = nodo_y.p;
		
		// intercambiamos la clave de Y y de Z, y si requiere actualizamos sus enlaces
		while(nodo_z != null && nodo_y.key < nodo_z.key)
		{
			//para intercambiar guardamos las claves de Y en una auxiliar
			int aux_y = nodo_y.key;
			nodo_y.key = nodo_z.key;
			nodo_z.key = aux_y;
			
			//enlazamos los nodos
			nodo_y.enlace.nodo = nodo_z;
			nodo_z.enlace.nodo = nodo_y;
			
			//creamos nuevo enlace porque se intercambian
			Enlace enlace_y= nodo_y.enlace;
			nodo_y.enlace= nodo_z.enlace;
			nodo_z.enlace = enlace_y;
		
		
			nodo_y = nodo_z;
			nodo_z = nodo_y.p;
		}
	}

	/**Eliminar el nodo de la lista*/
	private void eliminarNodo(Nodo x, Nodo pred_x) 
	{
		//Comprueba que x sea head, y actualiza para que head tome el valor del hermano
		if (x == head) head = x.sibling;
		else pred_x.sibling= x.sibling;
		//Creamos un nuevo monticulo binomial
		Monticulo_binomial monticulo = new Monticulo_binomial();
		//invertir el orden de los hijos de X
		Nodo y = x.child; //y guarda el valor del hijo de X
		while(y!=null)
		{
			Nodo siguiente = y.sibling; 
			y.sibling = monticulo.head; 
			monticulo.head = y;
			y = siguiente;
		}
		//Unimos los monticulos
		Monticulo_binomial nuevo_monticulo = (Monticulo_binomial) this.union(monticulo);
		//en head asignamos el head del nuevo monticulo que es creado con los hijos de X
		head = nuevo_monticulo.head;	
		
	}

	
	/**Creamos un método de búsqueda que nos devuelve uno, con una clave específica**/
	public static Nodo buscarNodo(Nodo nodo, int k)
	{	//Si head es null lanzamos un error
		if (nodo == null) throw new NoSuchElementException("No hay nodo"); 
		//Vamos añadiendo los nodos a la lista enlazada
		else {
			LinkedList<Nodo> listanodos = new LinkedList<Nodo>();
			Nodo n;
			listanodos.addLast(nodo); //añadido head a la lista
			while(listanodos.size() != 0)
			{	//eliminamos el primero
				n = listanodos.removeFirst();
				//si coincide con el valor que buscamos, devolvemos el nodo
				if(n.key == k) return n;
				// si el hermano del nodo no es null, añadimos a la lista
				// si el la clave del nodo es menor y tiene al menos un hijo, tambien añadimos a la lista
				else {
					if(n.sibling != null) listanodos.addLast(n.sibling);
					if((n.key < k) && (n.child != null)) listanodos.addLast(n.child);	
				}
			}
			
		}
		
		return null; // si no lo encontramos, devolvemos null
	}
	
	
	/**
	 * Probamos las instrucciones creadas 
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
			BufferedReader in =new BufferedReader(new InputStreamReader(System.in)); 
			int valor = 0;
			int num_insertar, num_dec, nodo_dec, num;
			Monticulo_binomial heap = new Monticulo_binomial(); // creamos un nuevo monticulo
				
			//Menu principal
				do{
					System.out.println("Elige una opción: ");
					System.out.println("1.- Insertar");
					System.out.println("2.- Mínimo");
					System.out.println("3.- Borrar el mínimo");
					System.out.println("4.- Decrecer clave");
					System.out.println("5.- Cálculo de Tiempos");
					System.out.println("6.- Salir");
					valor = Integer.parseInt(in.readLine());
					
					switch(valor)
					{
					case 1:	//Insertar un elemento en el monticulo binomial
						{ 
						  System.out.println("Introduce el número que deseas insertar: ");
						  num_insertar = Integer.parseInt(in.readLine());
						  heap.insertar(num_insertar);
						  break;
						}
					case 2 : { //Valor del minimo 
						
						System.out.println("Mínimo del montículo: " + heap.minimo() + "\n");
						break;
						
					}
					case 3: { //Borrar el minimo del monticulo
						System.out.println("Borrando el mínimo del montículo....");
						heap.extraerMinimo();
						break;
					}
					case 4: { //decrementar clave de un nodo cualquiera
						System.out.println("¿Qué nodo del montículo deseas decrecer? (Si se introduce un nodo que no existe, se mostrará un error)");
						nodo_dec = Integer.parseInt(in.readLine());
						Nodo n = buscarNodo(heap.head, nodo_dec);
						System.out.println("A qué número desea decrecer: ");
						num_dec = Integer.parseInt(in.readLine());
						heap.decrease_Key(n, num_dec);
						break;
					}
					case 5:{
					
						int i;
						System.out.println("Cuántos números deseas insertar? ");
						num = Integer.parseInt(in.readLine());
						long inicio = System.currentTimeMillis();
						for(i = 1; i <= num; i++)
						{	heap.insertar(i); }
						long tiempo = System.currentTimeMillis()- inicio;
						
						
						System.out.println("Borramos los mínimos: ");
						long inicio2 = System.currentTimeMillis();
						for(i = 1; i <= num; i++)
						{	heap.extraerMinimo(); }
						long tiempo2 = System.currentTimeMillis()- inicio2;
						
						System.out.println("\nHa tardado al insertar los elementos : " + (tiempo)/ (num + 0.0) + " milisegundos");
						System.out.println("Ha tardado al borrar los mínimos: " + (tiempo2)/ (num + 0.0) + " milisegundos\n");
						
						break;
					}
					
					//Salir del programa
					default: System.out.println("¡Hasta Luego!");
			
					}
					
				}while(valor<6);

	}

}
