package work;

import java.io.IOException;

/**
 * Входная точка в программу
 * @autor Sobolev Ivan
 * @date 07.04.2022
 * @version 1.0
 */
public class ServerSide {

    public ServerSide() {
    }

    public static void main(String[] args) {
        ServerConnection serverConnection = new ServerConnection();

        try {
            if (args != null && args.length != 0) {
                serverConnection.load(args[0],args[1]);

                serverConnection.connection();
            } else {
                System.out.println("Не был передан путь к файлу при запуске программы");
                System.exit(0);
            }

        } catch (IOException var3) {
            System.out.println("Поток ввода закрыт");
        }

    }
}
