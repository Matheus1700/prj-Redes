import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    public static void main(String args[]) throws Exception {
    	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();

        // Receber o endereço IP do servidor
        byte[] receivedData = new byte[1024];
        DatagramPacket receiveAddressPacket = new DatagramPacket(receivedData, receivedData.length);
        clientSocket.receive(receiveAddressPacket);

        String serverAddressString = new String(receiveAddressPacket.getData(), 0, receiveAddressPacket.getLength());
        InetAddress serverAddress = InetAddress.getByName(serverAddressString);

        while (true) {
            System.out.println("Digite 0 ou 1:");
            String userInput = inFromUser.readLine();

            byte[] sendData = userInput.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9877);
            clientSocket.send(sendPacket);
            
            System.out.println("Mensagem enviada para o servidor!");

            DatagramPacket responsePacket = new DatagramPacket(receivedData, receivedData.length);
            clientSocket.receive(responsePacket);

            String serverResponse = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Resposta do servidor: " + serverResponse);
        }
    }
}

