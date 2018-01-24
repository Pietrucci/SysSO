package sysos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

import sysos.process_manager.process;

public class Main {
		
	public static Memory M = new Memory();
	public static process_manager T = new process_manager();
	public static FileSystem F = new FileSystem();
	public static int OBECNY_PROCES;
	public static schedulerr S = new schedulerr();
	
	public static void main(String[] args) {
		//systemLoad();
		//systemLoadLite();
		T.init();
		S.runningProcess=T.INIT;
		String string;
		Scanner in = new Scanner(System.in);
		do {
			System.out.print("user@sos:~$ ");
			
			string = in.nextLine();
			String[] tab = string.split(" ");
			if(tab.length > 0) {
				tab[0] = tab[0].toUpperCase();
			}
			
			if(tab[0].equals("HELP"))
			{
				if(tab.length==1) {
					System.out.println("DF [nazwa_pliku]         : usuwanie wskazanego pliku");
					System.out.println("CF [nazwa_pliku]         : tworzenie pustego pliku o wskazanej nazwie");
					System.out.println("WF [nazwa_pliku]         : dopisanie danych do danego pliku");
					System.out.println("RF [nazwa_pliku]         : czytanie wskazanego pliku");
					System.out.println("LS                       : wyswietlenie plikow");
					System.out.println("PD                       : sprawdzanie zawartosci dysku");
					System.out.println("PB [nr_bloku]            : sprawdzenie pojedynczego bloku dysku");
					System.out.println("MEMORY                   : sprawdzanie stanu pamieci; ");
					System.out.println("TASKLIST                 : sprawdzanie listy procesow; ");
					System.out.println("TASKKILL [nazwa_procesu] : zabijanie procesu; ");
					System.out.println("GO                       : kolejny krok wykonywanego procesu; ");
					System.out.println("GOM [n]                  : n kolejnych krokow wykonywanego procesu; ");
					System.out.println("START [nazwa_procesu rezerwowane_miejsce nazwa_pliku]\n"
									 + "                         : stworzenie procesu; ");
					System.out.println("SREG                     : wyswietla rejestry; ");
					System.out.println("exit                     : wyjscie; ");
					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("DF"))
			{
				if(tab.length==2)
					F.deleteFile(tab[1]);
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("CF"))
			{
				if(tab.length==2)
					F.createFile(tab[1]);
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			
			else if(tab[0].equals("WF"))
			{
				if(tab.length>2)
				{
					String temp = "";
					for (int i = 2; i < tab.length; i++) {
						temp += tab[i];
						temp += " ";
					}
					F.writeFile(tab[1], temp);
				}
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("RF"))
			{
				if(tab.length==2)
					F.readFile(tab[1]);
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			
			}
			
			else if(tab[0].equals("PD"))
			{
				if(tab.length==1) {
					F.printDisc();
					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("PB"))
			{
				if(tab.length==2) {
					try {
						int blockI = Integer.parseInt(tab[1]);
						F.printSector(blockI);
					} catch (NumberFormatException e) {
						System.out.println("nieprawidlowe wywolanie komendy");
					}

					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("LS"))
			{
				if(tab.length==1) {
					System.out.println("Pliki");
					F.listAllFiles();					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("MEMORY"))
			{
				if(tab.length==1) {
					M.printMemory();
					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("TASKLIST"))
			{
				if(tab.length==1) {
					T.INIT.show_list();
					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("TASKKILL"))
			{
				if(tab.length==2)
					//tab[1] to nazwa procesu
					T.INIT.kill(T.find_name(tab[1]));
				
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("GO"))
			{
				if(tab.length==1) {
					interpreter i =new interpreter(M,F);
					i.exe();
					//stan procesu po wykonaniu jednego kroku 
				}
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("GOM")) {
				if(tab.length == 2) {
					for(int j=0;j < Integer.parseInt(tab[1]); j++) {
						interpreter i =new interpreter(M,F);
						i.exe();
						//stan procesu po wykonaniu jednego kroku 
					}
				}
				else {
					System.out.println("nieprawidlowe wywolanie komendy");
				}
			}
			
			else if(tab[0].equals("START"))
			{
				if(tab.length==4) {
					/*
					 tab[1] nazwa procesu
					 tab[2] rezerwoe miejsce
					 tab[3] nazwa pliku
					 */
					String kod = new String();
					Scanner scan;
					try {
						scan = new Scanner(new File(tab[3]));
						kod = scan.nextLine();
						int size = Integer.parseInt(tab[2]);
						T.INIT.fork(tab[1]);
						int x = T.find_name(tab[1]);
						S.check(T);
						T.find(x).exec(kod, tab[3], kod.length()+size);
						scan.close();
					} catch (FileNotFoundException e) {
						System.out.println("Nie znaleziono pliku");
					}
				
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			else if(tab[0].equals("SREG")) {
				if(tab.length == 1) {
					System.out.println("Rejestry: A|" + S.runningProcess.A + " B|" + S.runningProcess.B + " C|" + S.runningProcess.C + " D|" + S.runningProcess.D);
				}
				else {
					System.out.println("nieprawidlowe wywolanie komendy");
				}
			}
			
			else if(!string.equals("exit")){
				System.out.println("nie ma takiej komendy");
			}
			
		}while(!string.equals("exit"));	
			in.close();	
	}
	
	private static void systemLoad() {
		StringBuilder barPattern = new StringBuilder("[░░░░░░░░░░]\r");
		for(int i=0; i<10; i++) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			barPattern.setCharAt(i+1,'▓');
			System.out.print(barPattern.toString());
		}
		System.out.println("Welcome in: ");
		//java.awt.Toolkit.getDefaultToolkit().beep();
		
		System.out.println("                           ********           ********");
		System.out.println("                          **//////           **////// ");
		System.out.println("                         /**         ****** /**       ");
		System.out.println("                         /********* **////**/*********");
		System.out.println("                         ////////**/**   /**////////**");
		System.out.println("                                /**/**   /**       /**");
		System.out.println("                          ******** //******  ******** ");
		System.out.println("                         ////////   //////  ////////  ");
		System.out.println();
		//credits: http://www.network-science.de/ascii/
	}
	
	private static void systemLoadLite() {
		System.out.print("[");
		for(int i=0; i<10; i++) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print("▓");
		}
		System.out.println("]");
		System.out.println("Welcome in: ");
		//java.awt.Toolkit.getDefaultToolkit().beep();
		System.out.println("                           ********           ********");
		System.out.println("                          **//////           **////// ");
		System.out.println("                         /**         ****** /**       ");
		System.out.println("                         /********* **////**/*********");
		System.out.println("                         ////////**/**   /**////////**");
		System.out.println("                                /**/**   /**       /**");
		System.out.println("                          ******** //******  ******** ");
		System.out.println("                         ////////   //////  ////////  ");
		System.out.println();
		//credits: http://www.network-science.de/ascii/
		
	}

}
