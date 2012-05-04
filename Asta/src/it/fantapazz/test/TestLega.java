package it.fantapazz.test;

import java.util.Arrays;

class Partita {

	private int A;
	private int B;

	public Partita(int a, int b) {
		super();
		A = a;
		B = b;
	}

	public void invert() {
		int tmp = A;
		A = B;
		B = tmp;
	}

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public boolean contains(int s) {
		if ( s == A)
			return true;
		if ( s == B)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "( " + A + ", " + B + " )";
	}

}

class Giornata {

	private Partita[] partite;

	public Giornata(int size) {
		partite = new Partita[size];
	}

	public boolean contains(Partita p) {
		for ( int i = 0; i < partite.length; i++) {
			if ( partite[i] == null ) {
				continue;
			}
			if ( partite[i].contains(p.getA()))
				return true;
			if ( partite[i].contains(p.getB()))
				return true;
		}
		return false;
	}

	public boolean add(Partita p) {
		boolean canAdd = true; 
		int index = -1;
		for ( int i = 0; i < partite.length; i++) {
			if ( partite[i] == null ) {
				if ( index == -1 ) {
					index = i;
				}
				continue;
			}
			if ( partite[i].contains(p.getA())) {
				canAdd = false;
				continue;
			}
			if ( partite[i].contains(p.getB())) {
				canAdd = false;
				continue;
			}
		}
		if ( canAdd && index >= 0 ) {
			partite[index] = p;
			return true;
		}
		return false;
	}

	public Partita getPartita(int a) {
		for ( Partita p : getPartite()) {
			if (p.contains(a))
				return p;
		}
		return null;
	}

	public Partita[] getPartite() {
		return partite;
	}

	@Override
	public String toString() {
		return Arrays.toString(partite);
	}

}

public class TestLega {

	public static void dump(Giornata[] giornate) {
		for ( int i = 0; i < giornate.length; i ++) {
			System.out.println(giornate[i]);
		}
		System.out.println("------------ " + evaluate(giornate) + " -------------");
		for ( int i = 0; i < giornate.length + 1; i ++ ) {
			int count = 0;
			String ret = "";
			for ( int j = 0; j < giornate.length; j ++) {
				if ( giornate[j].getPartita(i).getA() == i ) {
					count++;
					ret += "1";
				}
				else {
					ret += "0";					
				}
			}
			System.out.println(i + ": " + count + " - " + ret);
		}
	}

	public static Giornata[] makeLega(int size) {

		Giornata[] giornate = new Giornata[size - 1];
		for ( int i = 0; i < size - 1; i ++ )
			giornate[i] = new Giornata(size / 2);
		for ( int a = 0; a < size; a ++ ) {
			for ( int b = a + 1; b < size; b ++) {
				if ( a == b ) {
					continue;
				}
				Partita partita = new Partita(a, b);
				for ( int i = 0; i < size - 1; i ++ ) {
					if (!giornate[i].contains(partita)) {
						giornate[i].add(partita);
						break;
					}
				}
			}
		}
		return giornate;
	}

	public static int diff(Giornata a, Giornata b, int size) {
		int count = 0;
		for ( int i = 0; i < size; i ++ ) {
			Partita p1 = a.getPartita(i);
			Partita p2 = b.getPartita(i);
			if ( p1.getA() == p2.getA()) {
				count ++;
			}
		}
		return count;
	}

	public static int evaluate(Giornata[] giornate) {
		int count = 0;
		for ( int i = 0; i < giornate.length - 1; i ++ ) {
			Giornata a = giornate[i];
			Giornata b = giornate[i + 1];
			count += diff(a, b, giornate.length + 1);
		}
		return count;
	}

	public static void invert(Giornata[] giornate, int a, int b) {
		Giornata p = giornate[a];
		giornate[a] = giornate[b];
		giornate[b] = p;
	}

	public static void minimize(Giornata[] giornate) {

		while ( true ) {

			int count = evaluate(giornate);			
			// System.out.println("MINIMIZE: " + count);

			// Action = 0: invert g, i; 1: switch giornate[g], giornate[b]; 
			int action = -1;
			int minG = -1;
			int minB = -1;
			int minI = -1;
			int min = count; 

			for ( int g = 0; g < giornate.length; g ++ ) {

				for ( int i = 0; i < giornate.length + 1; i ++ ) {

					Partita p = giornate[g].getPartita(i);

					p.invert();

					int v = evaluate(giornate);

					if ( v < min ) {

						if ( v < min ) {
							minI = i;
							minG = g;
							min = v;
							action = 0;
						}

					}

					p.invert();
				}

				//			for ( int b = g + 1; b < giornate.length; b ++ ) {
				//
				//				if ( g == b ) {
				//					continue;
				//				}
				//
				//				Giornata tmp = giornate[g];
				//				giornate[g] = giornate[b];
				//				giornate[b] = tmp;
				//
				//				int v = evaluate(giornate);
				//				
				//				if ( v < min ) {
				//					
				//					v = minimize(giornate);
				//
				//					if ( v < min ) {
				//						minG = g;
				//						minB = b;
				//						min = v;
				//						action = 1;
				//					}
				//				}
				//
				//				tmp = giornate[g];
				//				giornate[g] = giornate[b];
				//				giornate[b] = tmp;
				//
				//			}

			}

			if ( action != -1 ) {

				count = min;

				if ( action == 0 ) {
					// System.out.println("Go to: " + count + " INVERT");
					giornate[minG].getPartita(minI).invert();
				}
				//			if ( action == 1 ) {
				//				System.out.println("Go to: " + count + " SWITCH");
				//				Giornata tmp = giornate[minG];
				//				giornate[minG] = giornate[minB];
				//				giornate[minB] = tmp;
				//			}

			}
			else {
				break;
			}

		}
		
	}

	//	public static void minimizeSwitch(Giornata[] giornate) {
	//		
	//		int count = evaluate(giornate);
	//		// System.out.println("MIN SWITCH Start from: " + count);
	//		
	//		while ( true ) {
	//		
	//			int minA = -1;
	//			int minB = -1;
	//			int min = count; 
	//
	//			for ( int a = 0; a < giornate.length - 1; a ++ ) {
	//				for ( int b = a + 1; b < giornate.length; b ++ ) {
	//					
	//					if ( a == b ) {
	//						continue;
	//					}
	//					
	//					Giornata tmp = giornate[a];
	//					giornate[a] = giornate[b];
	//					giornate[b] = tmp;
	//					
	//					int v = evaluate(giornate);
	//					
	//					if ( v < min ) {
	//						minA = a;
	//						minB = b;
	//						min = v;
	//					}
	//					
	//					tmp = giornate[a];
	//					giornate[a] = giornate[b];
	//					giornate[b] = tmp;
	//
	//				}
	//			}
	//
	//			if ( minA != -1 ) {
	//				
	//				Giornata tmp = giornate[minA];
	//				giornate[minA] = giornate[minB];
	//				giornate[minB] = tmp;
	//				
	//				count = min;
	//				// System.out.println("Go to: " + count);
	//				
	//			}
	//			else {
	//				break;
	//			}
	//
	//		}
	//		
	//	}
	
	// A: [( 2, 0 ), ( 1, 3 )]    	(0,1),(0,3),(1,2),(2,3)
	// B: [( 0, 1 ), ( 3, 2 )]		(0,2),(0,3),(1,2),(1,3)
	// C: [( 3, 0 ), ( 2, 1 )]		(0,1),(0,2),(1,3),(2,3)
	
	// A -> B: (0,3)->(1,2); (1,2)->(0,3)
	// B -> C: (0,2)->(1,3); (1,3)->(0,2)

	// A: (0,2);(3,1)
	// B: (1,0);(2,3)
	// C: (3,0);(2,1)
	
	// A, B, C 
	// A, C, B
	// B, A, C
	// B, C, A
	// C, A, B
	// C, B, A
	  
	public static void main(String[] args) {

		int n = 16;

		Giornata[] giornate = makeLega(n);

		dump(giornate);

		System.out.println("\n\n");

		System.out.println("----------- Minimize -----------");

		minimize(giornate);
		
		dump(giornate);

		//		System.out.println("----------- Minimize switch -----------");
		//		minimizeSwitch(giornate);
		//		dump(giornate);

	}

}
