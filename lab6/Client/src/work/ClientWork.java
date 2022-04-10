package work;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;


/**
 * Класс отвечающий за отправку и получение команд от сервера, выборки конкретной команды
 *
 * @version 1.0
 * @autor Sobolev Ivan
 * @date 07.04.2022
 */
public class ClientWork {

    /**
     * Поле исполнителя команд связанных с вводом команд и их аргументов
     */
    private final CommandReader commandReader = new CommandReader();

    /**
     * Метод обрабатывает команды
     *
     * @throws IOException
     */
    public void work(Socket socket) throws IOException {
        try {
            if (commandReader.hasNextLine()) {
                CommandDescription command = commandReader.readCommand();
                switch (command.getCommand()) {
                    case HELP:
                    case INFO:
                    case SHOW:
                    case EXECUTE_SCRIPT:
                    case CLEAR:
                    case MAX_BY_AUTHOR:
                    case REMOVE_BY_ID:
                    case COUNT_BY_DIFFICULTY:
                    case FILTER_GREATER_THAN_MINIMAL_POINT:
                    case REMOVE_FIRST:
                    case REMOVE_AT:
                        sendCommand(socket, command);
                        getAnswer(socket);
                        break;
                    case ADD:
                    case ADD_IF_MAX:
                        command.setWork(commandReader.readWork());
                        sendCommand(socket, command);
                        getAnswer(socket);
                        break;
                    case UPDATE_ID:
                        try {
                            int id = commandReader.readId();
                            command.setArgs(Integer.toString(id));
                            sendCommand(socket, command);
                            getAnswer(socket);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Неверный аргумент у команды, для справки введите команду help");
                        }
                        break;
                    case EXIT:
                        System.out.println("Программа клиента успешно завершена.");
                        System.exit(0);
                        break;

                    default:
                        commandReader.invalidCommand();
                }
            }
        } catch (ArrayIndexOutOfBoundsException | ClassNotFoundException e) {
            System.out.println("Отсутствует аргумент");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод отправляет команду на сервер
     *
     * @param socket
     * @param answer
     * @throws IOException
     */
    public void sendCommand(Socket socket, CommandDescription answer) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream toServer = new ObjectOutputStream(baos);
        baos.flush();
        toServer.writeObject(answer);
        byte[] out = baos.toByteArray();
        socket.getOutputStream().write(out);
    }

    /**
     * Метод получает результат от сервера
     *
     * @param socket
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void getAnswer(Socket socket) throws IOException, ClassNotFoundException {
        String answer;
        ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
        answer = (String) fromServer.readObject();
        if (answer.equals("exit")) {
            System.exit(0);
        } else {
            System.out.println(answer);
        }
    }
}
