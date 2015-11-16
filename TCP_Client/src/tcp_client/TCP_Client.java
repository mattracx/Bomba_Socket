package tcp_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.Inet4Address;
import java.util.Random;
import java.util.Scanner;

public class TCP_Client {
    public static void main(String[] args) throws IOException {
        try(Socket clientSocket = new Socket("localhost", 5555)) {
            BufferedWriter out;
            try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                // Viene prelevato l'IP locale:
                String localhost = Inet4Address.getLocalHost().toString();
                out.write(localhost);
                // Nuova riga
                out.newLine();
                // Forza l'invio dei dati verso il server
                out.flush();
                // Messaggio di benvenuto:
                System.out.println(in.readLine());
                // Viene generato il numero random e viene inviato al server:
                Random random = new Random();
                int numero_random = random.nextInt((30 - 10) + 1) + 10; // (Max - min + 1) + min
                out.write(Integer.toString(numero_random));
                out.newLine();
                out.flush();
                // Inizio turni:
                int sottr;
                System.out.println("Attendi il tuo turno...");
                int numero = Integer.parseInt(in.readLine());
                do {
                    sottr = -1;
                    Scanner input = new Scanner(System.in);
                    System.out.println("");
                    while(true) {
                        System.out.println("Inserire un numero da sottrarre [1-5]: ");
                        if(input.hasNextInt()) {
                            sottr = input.nextInt();
                            if(1 <= sottr && sottr <= 5)
                                break;
                            else
                                continue;
                        }
                        input.nextLine();
                    }
                    // Invio dei dati al client:
                    out.write(Integer.toString(numero-sottr));
                    out.newLine();
                    out.flush();
                    if(numero-sottr <= 0) {
                        System.out.println("[INFO]: Congratulazioni, hai vinto!");
                        break;
                    }
                    System.out.println("Attendi il tuo turno...");
                    // Ottenimento del numero inviato dal server:
                    numero = Integer.parseInt(in.readLine());
                    if(numero <= 0) {
                        System.out.println("[INFO]: Hai perso!");
                        break;
                    }
                } while(numero > 0);
                in.close();
                out.close();
                clientSocket.close();
            }
        }
    }
    
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        return true;
    }
}
