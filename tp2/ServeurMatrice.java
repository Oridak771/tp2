import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServeurMatrice {

    private static final HashMap<String, String> credentials = new HashMap<>();
    static {
        // Ajoutez des noms d'utilisateur et des mots de passe à la HashMap
        credentials.put("user", "pass");
        credentials.put("user2", "pass2");
    }
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Le serveur est à l'écoute sur le port 1234...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion établie avec le client " + clientSocket.getInetAddress());
            
            // Créez un BufferedReader pour lire les informations d'identification envoyées par le client
            BufferedReader credentialsIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String username = credentialsIn.readLine();
            String password = credentialsIn.readLine();
            
            // Vérifiez les informations d'identification en utilisant la HashMap
            if (!credentials.containsKey(username) || !credentials.get(username).equals(password)) {
                // Si les informations d'identification sont incorrectes, fermez la connexion et passez au client suivant
                System.out.println("Informations d'identification incorrectes pour l'utilisateur " + username);
                credentialsIn.close();
                clientSocket.close();
                continue;
            }
                System.out.println("Connexion établie avec le client " + clientSocket.getInetAddress());
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du serveur : " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] inputs = inputLine.split(" ");
                int operation = Integer.parseInt(inputs[0]);
                int[][] matrixA = stringToMatrix(inputs[1]);
                int[][] matrixB = stringToMatrix(inputs[2]);
                int[][] result = null;
                switch (operation) {
                    case 1:
                        result = addMatrix(matrixA, matrixB);
                        break;
                    case 2:
                        result = substractMatrix(matrixA, matrixB);
                        break;
                    case 3:
                        result = multiplyMatrix(matrixA, matrixB);
                        break;
                    case 4:
                        result = divideMatrix(matrixA, matrixB);
                        break;
                    default:
                        out.println("Opération non reconnue.");
                        break;
                }
                if (result != null) {
                    out.println(matrixToString(result));
                }
            }
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la communication avec le client : " + e.getMessage());
        }
    }

    private static int[][] stringToMatrix(String matrixString) {
        String[] rows = matrixString.split(";");
        int[][] matrix = new int[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] elements = rows[i].split(",");
            matrix[i] = new int[elements.length];
            for (int j = 0; j < elements.length; j++) {
                matrix[i][j] = Integer.parseInt(elements[j]);
            }
        }
        return matrix;
    }

    private static String matrixToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sb.append(matrix[i][j]);
                if (j < matrix[i].length - 1) {
                    sb.append(",");
                }
            }
            if (i < matrix.length - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    private static int[][] addMatrix(int[][] matrixA, int[][] matrixB) {
        int rows = matrixA.length;
        int cols = matrixA[0].length;
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows;){
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
        return result;
    }

private static int[][] substractMatrix(int[][] matrixA, int[][] matrixB) {
    int rows = matrixA.length;
    int cols = matrixA[0].length;
    int[][] result = new int[rows][cols];
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            result[i][j] = matrixA[i][j] - matrixB[i][j];
        }
    }
    return result;
}

private static int[][] multiplyMatrix(int[][] matrixA, int[][] matrixB) {
    int rowsA = matrixA.length;
    int colsA = matrixA[0].length;
    int rowsB = matrixB.length;
    int colsB = matrixB[0].length;
    if (colsA != rowsB) {
        throw new IllegalArgumentException("Les matrices ne sont pas compatibles pour une multiplication.");
    }
    int[][] result = new int[rowsA][colsB];
    for (int i = 0; i < rowsA; i++) {
        for (int j = 0; j < colsB; j++) {
            for (int k = 0; k < colsA; k++) {
                result[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
    }
    return result;
}

private static int[][] divideMatrix(int[][] matrixA, int[][] matrixB) {
    // Implementation de la division de matrices
    throw new UnsupportedOperationException("La division de matrices n'est pas encore implémentée.");
}
}