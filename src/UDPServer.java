import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class UDPServer {

    public static void main(String args[]) throws Exception {    	
    	DatagramSocket serverSocket = new DatagramSocket(9876, InetAddress.getByName("0.0.0.0"));

    	System.out.println("O servidor esta funcionando");
    	
        byte[] receivedData = new byte[1024];
        Map<InetAddress, Integer> playerChoices = new HashMap<>();

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
            serverSocket.receive(receivePacket);

            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            System.out.println("Enviando o endereço do servidor para o cliente...");
            String serverAddressString = serverSocket.getLocalAddress().getHostAddress();
            byte[] addressData = serverAddressString.getBytes();
            DatagramPacket addressPacket = new DatagramPacket(addressData, addressData.length, clientAddress, clientPort);
            serverSocket.send(addressPacket);
            
            System.out.println("Mensagem de " + clientAddress + ":" + clientPort + " recebida - " + receivedMessage);

            // Save player choice
            int playerChoice = Integer.parseInt(receivedMessage);
            playerChoices.put(clientAddress, playerChoice);

            // Respond to the user with a message
            String responseMessage = "Mensagem recebida";
            byte[] responseData = responseMessage.getBytes();
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
            serverSocket.send(responsePacket);
            
            // Check if all players have made their choices
            if (playerChoices.size() == 1) {
                // Determine the result
            	System.out.println("Entrou aqui");
                int result = checkWinner(playerChoices);

                // Send the result back to each player
                for (Map.Entry<InetAddress, Integer> entry : playerChoices.entrySet()) {
                    byte[] sendData = String.valueOf(result).getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, entry.getKey(), clientPort);
                    serverSocket.send(sendPacket);
                }

                // Clear choices for the next round
                playerChoices.clear();
            }
        }
    }

    private static int checkWinner(Map<InetAddress, Integer> playerChoices) {
        // Check if all players made the same choice
        if (playerChoices.values().stream().distinct().count() == 1) {
            return 0; // Tie
        } else {
            // Check for the player with the different choice
            int total = 0;
            for (int choice : playerChoices.values()) {
                total += choice;
            }
            return total % 2 == 0 ? 1 : 0; // 1 if even, 0 if odd
        }
    }
}
