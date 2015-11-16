package tcp_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCP_Server {
    public static void main(String[] args) throws IOException {
        // Viene settata la porta di ascolto del server:
        int porta = 5555;
        ServerSocket serverSocket = new ServerSocket(porta);
        System.out.println("Server in ascolto sulla porta: " + serverSocket.getLocalPort());
        System.out.println("In attesa di connessioni...");
        while(true) {
            try(Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
                out.write("[SERVER]: Connessione stabilita!");
                out.newLine();
                out.flush();
                System.out.println("[SERVER]: Connessione in entrata da: " + in.readLine());
                // Ricezione del numero random da parte del client:             
                int numero = Integer.parseInt(in.readLine());
                // Inizio turni:
                int sottr;
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
            break;
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
