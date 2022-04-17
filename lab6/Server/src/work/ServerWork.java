package work;

import Manager.CollectionManager;
import Manager.CommandReader;
import Manager.FileManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Manager.*;
import data.LabWork;

/**
 * Класс отвечающий за отправку и получение сообщения, выбора конкретной команды
 *
 * @version 1.0
 * @autor Sobolev Ivan
 * @date 07.04.2022
 */
public class ServerWork {
    /**
     * Поле исполнителя команд связанных с коллекцией
     */
    private CollectionManager collectionManager;
    /**
     * Поле исполнителя команд связанных с файлом
     */
    private FileManager fileManager;
    /**
     * Поле исполнителя команд связанных с вводом команд и их аргументов
     */
    private CommandReader commandReader;

    /**
     * Поле связанное с логикой работы команды при чтении из скрипта
     */
    private boolean interactiveMod;

    /**
     * Логгер
     */
    private Logger logger = Logger.getLogger("ServerLogger");

    /**
     * Конструктор - создание нового объекта с определенными значениями
     */
    public ServerWork(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
        this.commandReader = new CommandReader();
        this.interactiveMod = true;
    }

    /**
     * Метод получающий от клиента конкретную команду
     */
    public CommandDescription receiveCommand(SelectionKey key) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        SocketChannel channel = (SocketChannel) key.channel();

        for (int available = channel.read(buffer); available > 0; available = channel.read(buffer)) {
        }

        byte[] buf = buffer.array();
        ObjectInputStream serialize = new ObjectInputStream(new ByteArrayInputStream(buf));
        CommandDescription command = (CommandDescription) serialize.readObject();
        serialize.close();
        buffer.clear();
        key.interestOps(4);
        return command;
    }

    /**
     * Метод отправляющий клиенту результат исполнения
     */
    public void send(SelectionKey key, CommandDescription commandDescription) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream toClient = new ObjectOutputStream(baos);
        switch (commandDescription.getCommand()) {
            case HELP:
                toClient.writeObject(this.collectionManager.help());
                break;
            case EXECUTE_SCRIPT:
                toClient.writeObject(scriptMod(commandDescription.getArgs()));
                break;
            case REMOVE_AT:
                toClient.writeObject(this.collectionManager.removeAt((commandDescription.getArgs())));
                break;
            case REMOVE_FIRST:
                toClient.writeObject(this.collectionManager.removeFirst());
                break;
            case INFO:

                toClient.writeObject(this.collectionManager.info());
                break;
            case SAVE:
                toClient.writeObject( this.fileManager.save(this.collectionManager.getCollection()));
                break;
            case SHOW:
                toClient.writeObject(this.collectionManager.show());
                break;
            case UPDATE_ID:
                toClient.writeObject(this.collectionManager.update_id(commandDescription.getArgs()));
                break;
            case COUNT_BY_DIFFICULTY:
                toClient.writeObject(this.collectionManager.count_by_difficulty(commandDescription.getArgs()));
                break;
            case FILTER_GREATER_THAN_MINIMAL_POINT:
                toClient.writeObject(this.collectionManager.filter_greater_than_minimal_point(commandDescription.getArgs()));
                break;
            case ADD:
                toClient.writeObject(this.collectionManager.add(commandDescription.getWork()));
                break;
            case UPDATE:
                toClient.writeObject(this.collectionManager.update(commandDescription.getWork()));
                break;
            case MAX_BY_AUTHOR:
                toClient.writeObject(this.collectionManager.max_by_author());
                break;
            case REMOVE_BY_ID:
                toClient.writeObject(this.collectionManager.removeByID(commandDescription.getArgs()));
                break;
            case CLEAR:
                toClient.writeObject(this.collectionManager.clear());
                break;

            case ADD_IF_MAX:
                toClient.writeObject(this.collectionManager.addIfMax(commandDescription.getWork()));
                break;

            default:
                System.out.println("Invalid Command");
        }

        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());

        for (int available = channel.write(buffer); available > 0; available = channel.write(buffer)) {
        }

        baos.close();
        toClient.close();
        key.interestOps(1);
    }

    /**
     * Метод для обработки команд save и exit на сервере
     */
    public void serverMod() throws IOException {
        String s = "";
        CommandDescription command = this.commandReader.readCommand();
        switch (command.getCommand()) {
            case SAVE:
                this.fileManager.save(this.collectionManager.getCollection());
                logger.info("Коллекция успешно сохранена.");
                break;
            case EXIT:
                this.fileManager.save(this.collectionManager.getCollection());
                logger.info("Программа сервера успешно завершена.");
                System.exit(0);
                break;
            default:
                System.out.println("На сервере поддерживаются только команды save и exit.");

        }
    }

    /**
     * Метод обработки команды скрипт
     */
    public String scriptMod(String arg) {
        String arr = commandReader.readScript(arg);
        if (!Objects.equals(arr, "")) {
            String s = "";
            String[] Arr = arr.split(";");
            // System.out.println(Arrays.toString(Arr));
            //System.out.println(arr);
            for (String command : Arr) {
                String[] finalUserCommand = command.split(" ", 2);
                // System.out.println(Arrays.toString(finalUserCommand));
                // System.out.println(finalUserCommand);
                try {
                    switch (finalUserCommand[0]) {
                        case "":
                            break;
                        case "remove_first":
                            s += (this.collectionManager.removeFirst()) + "\n";
                            break;
                        case "add":
                            if (finalUserCommand[1] != null) {
                                s += this.collectionManager.add(this.collectionManager.script_add(finalUserCommand[1])) + "\n";
                            } else {
                                s += "Неверный ввод данных в скрипте. ";
                            }
                            break;
                        case "update":
                            if (finalUserCommand[1] != null) {
                                s += this.collectionManager.update(this.collectionManager.script_update(finalUserCommand[1])) + "\n";
                            } else {
                                s += "Неверный ввод данных в скрипте. ";
                            }
                            break;
                        case "remove_by_id":
                            s += this.collectionManager.removeByID(finalUserCommand[1].trim()) + "\n";
                            break;
                        case "remove_at":
                            s += this.collectionManager.removeAt((finalUserCommand[1].trim())) + "\n";
                            break;
                        case "show":
                            s += this.collectionManager.show();
                            break;
                        case "clear":
                            s += this.collectionManager.clear() + "\n";
                            break;
                        case "save":
                            s+=this.fileManager.save(this.collectionManager.getCollection());
                            break;
                        case "info":
                            s += this.collectionManager.info() + "\n";
                            break;
                        case "update_id":
                            s += this.collectionManager.update_id(finalUserCommand[1].trim()) + "\n";
                            break;
                        case "add_if_max":
                            s += this.collectionManager.addIfMax(this.collectionManager.script_add(finalUserCommand[1]));
                            break;
                        case "help":
                            s += this.collectionManager.help() + "\n";
                            break;
                        case "exit":
                            s += "\nПроцесс завершён." + "\n";
                            System.exit(0);
                            break;
                        case "max_by_author":
                            s += this.collectionManager.max_by_author() + "\n";
                            break;
                        case "execute_script":
                            s += "Рекурсия скрипта." + "\n";
                            break;
                        case "count_by_difficulty":
                            s += this.collectionManager.count_by_difficulty(finalUserCommand[1].trim()) + "\n";
                            break;
                        case "filter_greater_than_minimal_point":
                            s += this.collectionManager.filter_greater_than_minimal_point(finalUserCommand[1]) + "\n";
                            break;
                        default:
                            s += "Неопознанная команда. Наберите 'help' для справки." + "\n";
                    }

                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Отсутствует аргумент.");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.commandReader.interactiveMod();
            this.interactiveMod = true;
            return s;
        }
        return "Файл со скриптом пуст";
    }


    public Logger getLogger() {
        return logger;
    }
}
