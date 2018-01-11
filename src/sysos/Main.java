package sysos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import sysos.process_manager.process;

public class Main {
	
	public static Memory M = new Memory();
	public static process_manager T = new process_manager();
	public static FileSystem F = new FileSystem();
	public static int OBECNY_PROCES;
	public static schedulerr S = new schedulerr();
	
	public static void main(String[] args) {
		T.init();
		String string;
		do {
			System.out.println("command: ");
			
			Scanner in = new Scanner(System.in);
			string = in.nextLine();
			String[] tab = string.split(" ");
			
			
			if(tab[0].equals("DEL"))   ///////////////////////////////////////////////////////////////////usuwanie pliku
			{
				if(tab.length==2)
					F.deleteFile(tab[1]);
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			if(tab[0].equals("COPY")&&tab[1].equals("NULL"))  ////////////////////////////////////////////tworzenie pustego pliku
			{
				if(tab.length==3)
					F.createFile(tab[2]);
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			
			if(tab[0].equals("OVR"))  ///////////////////////////////////////////////////////////////////dopisywanie danych do pliku
			{
				if(tab.length>2)
				{
					String temp = "";
					for (int i = 2; i < tab.length; i++) {
						temp += tab[i];
						temp += " ";
					}
					F.writeFile(tab[1], temp);
					//System.out.println("dopisywanie danych do pliku");
				}
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			if(tab[0].equals("OPF"))   ///////////////////////////////////////////////////////////////////usuwanie pliku
			{
				if(tab.length==2)
					F.readFile(tab[1]);
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			
			}
			
			if(tab[0].equals("HELP"))   ////////////////////////////////////////////////////////////////////pomoc
			{
				if(tab.length==1) {
					System.out.println("DEL nazwa_pliku: usuwanie wskazanego pliku; ");
					System.out.println("COPY NULL nazwa_pliku: tworzenie pustego pliku o wskazanej nazwie; ");
					System.out.println("OVR nazwa_pliku: dopisanie danych do danego pliku; ");
					System.out.println("OPF nazwa_pliku: czytanie wskazanego pliku; ");
					System.out.println("MEMORY: sprawdzanie stanu pamieci; ");
					System.out.println("CHKDSK: sprawdzanie zawarto�ci dysku; ");
					System.out.println("LS: wyswietlenie plikow; ");
					System.out.println("TASKLIST: sprawdzanie listy procesow; ");
					System.out.println("TASKKILL: nazwa_procesu: zabijanie procesu; ");
					System.out.println("GO: kolejny krok wykonywanego procesu; ");
					System.out.println("START nazwa_procesu grupa_procesu nazwa_pliku: stworzenie procesu; ");
					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			
			if(tab[0].equals("MEMORY"))   //////////////////////////////////////////////////////////////////pami�� 
			{
				if(tab.length==1) {
					
					//funkcja pokazuj�ca stan pami�ci 
					System.out.println("stan pamieci");
					M.printMemory();
					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			
			if(tab[0].equals("CHKDSK"))   //////////////////////////////////////////////////////////////////dysk
			{
				if(tab.length==1) {
					
					//funkcja pokazuj�ca zawarto�� dysku 
					System.out.println("zawartosc dysku");
					F.printDisc();
					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			if(tab[0].equals("LS"))   //////////////////////////////////////////////////////////////////dysk
			{
				if(tab.length==1) {
					
					//funkcja pokazuj�ca zawarto�� dysku 
					System.out.println("Pliki");
					F.listAllFiles();					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			
			if(tab[0].equals("TASKLIST"))   ///////////////////////////////////////////////////////////////lista proces�w
			{
				if(tab.length==1) {
					T.INIT.show_list();
					
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			
			if(tab[0].equals("TASKKILL"))   /////////////////////////////////////////////////////////////////zabijanie procesu
			{
				if(tab.length==2)
					//tab[1] to nazwa procesu
					T.INIT.kill(T.find_name(tab[1]));
				
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			
			if(tab[0].equals("GO"))   /////////////////////////////////////////////////////////////////////kolejny krok w procesie
			{
				if(tab.length==1) {
					
					interpreter i =new interpreter(M,F);
					i.exe();
					//stan procesu po wykonaniu jednego kroku 
					System.out.println("kolejny krok w procesie");
				
					
				}
				else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
			
			
			if(tab[0].equals("START"))   //////////////////////////////////////////////////////////////////tworzenie procesu
			{
				if(tab.length==4) {
					/*
					 tab[1] nazwa procesu
					 tab[2] grupa procesu / rezerwoe miejsce
					 tab[3] nazwa pliku
					 */
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(tab[3]);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					//tworzenie procesu 
					process p = T.new process(tab[1]);
					p.exec(fis.toString(), "", Integer.valueOf(tab[2]));
					System.out.println("tworzenie procesu");
				
				}else
					System.out.println("nieprawidlowe wywolanie komendy");
			}
			
		}while(!string.equals("exit"));	
			
		
	}

}
