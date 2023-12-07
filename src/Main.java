
public class Main {
    public static void main(String[] args) {
    	if (args.length != 1) {
            System.out.println("Usage: java Main [client|server]");
            return;
        }

        String mode = args[0].toLowerCase();

        try {
            if (mode.equals("client")) {
                UDPClient.main(args);
            } else if (mode.equals("server")) {
                UDPServer.main(args);
            } else {
                System.out.println("Invalid mode. Use 'client' or 'server'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}