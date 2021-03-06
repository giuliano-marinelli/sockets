package servidorclima;

import java.io.*;
import java.net.*;
import java.util.Random;

public class ServidorClimaHilo extends Thread {

    private Socket clientSocket;
    private DataInputStream inBuffer;
    private DataOutputStream outBuffer;
    private int idSession;

    public ServidorClimaHilo(Socket clientSocket, int idSession) {
        this.clientSocket = clientSocket;
        this.idSession = idSession;
        //inicializa los buffers para comunicarse con el cliente
        try {
            inBuffer = new DataInputStream(clientSocket.getInputStream());
            outBuffer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Servidor Clima> " + ex.getMessage());
        }
    }

    public void desconnectar() {
        //cierra la conexion con cliente
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println("Servidor Clima> " + ex.getMessage());
        }
        System.out.println("Cliente[" + idSession + "]> Se desconecto.");
    }

    @Override
    public void run() {
        try {
            String query = "";
            String answer = "";

            //bloquea hasta recibir una consulta
            query = inBuffer.readUTF();

            System.out.println("Cliente[" + idSession + "]> Consulta: " + query);

            answer = giveWeather(query);

            System.out.println("Servidor Clima> Respuesta Cliente[" + idSession + "]: " + answer);

            //envia la respuesta segun lo consultado
            outBuffer.writeUTF(answer);

        } catch (IOException ex) {
            System.err.println("Servidor Clima> " + ex.getMessage());
        } finally {
            desconnectar();
        }
    }

    public String giveWeather(String date) {
        /*switch (date) {
            case "03/05/2018":
                return "Soleado";
            case "04/05/2018":
                return "Lluvioso";
            case "06/05/2018":
                return "Ventoso";
            case "07/05/2018":
                return "Nublado";
            default:
                return "sin registros de ese dia";
        }*/
        
        String[] weathers = {"Soleado", "Lluvioso", "Ventoso", "Nublado"};
        String[] dmy = date.split("/");
        int seed = Integer.parseInt(dmy[0])
                + Integer.parseInt(dmy[1])
                + Integer.parseInt(dmy[2]) / 1000;
        Random random = new Random(seed);
        return weathers[random.nextInt(3)];
    }

}
