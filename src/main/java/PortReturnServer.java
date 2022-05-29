import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class PortReturnServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        //var execuor = Executors.newFixedThreadPool(500);

        log("Server start");

        try {
            while (true) {
                //  Ждем подключения клиента и получаем потоки для дальнейшей работы
                Socket clientSocket = serverSocket.accept();
                handle(clientSocket);
//                new Thread(null, () -> handle(socket)).start();
//                poolExecutor.submit(() -> handle(socket));
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            serverSocket.close();
        }

    }

    private static void handle(Socket socket) {
        log("client connected: " + socket.getRemoteSocketAddress());

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String name;

            while ((name = in.readLine()) != null) {
                // Пишем ответ
                System.out.println("New connection accepted");
                out.println(String.format("Hi %s, your port is %d", name, socket.getPort()));
                // Выход если от клиента получили end
                if (name.equals("end")) {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void log(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }
}
