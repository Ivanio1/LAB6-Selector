package work;


/**
 * Входная точка в программу
 * @autor Sobolev Ivan
 * @date 07.04.2022
 * @version 1.0
 */
public class ClientSide {
    public static void main(String[] args) {
        ClientConnection clientConnection = new ClientConnection();
        clientConnection.connection();
    }
}
