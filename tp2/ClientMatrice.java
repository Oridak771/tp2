import java.io.*;
import java.net.*;

public class ClientMatrice {
    
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            // Send username and password to authenticate
            out.println("user");
            out.println("pass");
            
            // Send matrix operation
            out.println("1;1,2,3;4,5,6;7,8,9"); // Matrix A
            out.println("1;9,8,7;6,5,4;3,2,1"); // Matrix B
            out.println(); // End of input
            
            // Read the result from the server
            String result = in.readLine();
            System.out.println(result);
            
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la communication avec le serveur : " + e.getMessage());
        }
    }
}