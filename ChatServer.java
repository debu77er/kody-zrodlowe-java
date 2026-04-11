import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatServer extends JFrame {
    private ServerSocket serverSocket;
    private final java.util.List<ClientHandler> clients = Collections.synchronizedList(new java.util.ArrayList<>());
    private JTextArea chatArea;
    private JTextField portField;
    private JButton startButton;
    private JButton stopButton;

    public ChatServer() {
        setTitle("Chat Server");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        portField = new JTextField("12345", 5);
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);
        topPanel.add(startButton);
        topPanel.add(stopButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());
    }

    private void startServer() {
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid port number");
            return;
        }

        try {
            serverSocket = new ServerSocket(port);
            chatArea.append("Server started on port " + port + "\n");
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            Thread acceptThread = new Thread(() -> {
                while (!serverSocket.isClosed()) {
                    try {
                        Socket socket = serverSocket.accept();
                        ClientHandler handler = new ClientHandler(socket);
                        clients.add(handler);
                        handler.start();
                        broadcast("User connected: " + socket.getRemoteSocketAddress());
                        appendChat("User connected: " + socket.getRemoteSocketAddress());
                    } catch (IOException e) {
                        if (serverSocket.isClosed()) break;
                        e.printStackTrace();
                    }
                }
            });
            acceptThread.start();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not start server: " + e.getMessage());
        }
    }

    private void stopServer() {
        try {
            for (ClientHandler client : clients) {
                client.close();
            }
            clients.clear();
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            chatArea.append("Server stopped.\n");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    private void appendChat(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
        });
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    String formattedMessage = "User " + socket.getRemoteSocketAddress() + ": " + message;
                    appendChat(formattedMessage);
                    broadcast(formattedMessage);
                }
            } catch (IOException e) {
                // Client disconnected
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
                broadcast("User disconnected: " + socket.getRemoteSocketAddress());
                appendChat("User disconnected: " + socket.getRemoteSocketAddress());
            }
        }

        public void close() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatServer server = new ChatServer();
            server.setVisible(true);
        });
    }
}
