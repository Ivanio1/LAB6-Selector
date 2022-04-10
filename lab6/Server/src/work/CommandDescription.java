package work;

import java.io.Serializable;

import data.LabWork;


/**
 * Класс инкапсулирующий в себя тип команды и её аргументы
 * @autor Sobolev Ivan
 * @date 07.04.2022
 * @version 1.0
 */
public class CommandDescription implements Serializable {

    /** Поле типа команды */
    private CommandType command;
    /** Поле аргументов */
    private String args;
    /** Поле объекта коллекции */
    private LabWork work;
    /** константа для сериализации*/
    private static final long serialVersionUID = 17L;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param command - тип команды
     * @param args - аргументы команды
     */
    public CommandDescription(CommandType command, String args) {
        this.command = command;
        this.args = args;
    }

    public CommandDescription(CommandType command, LabWork product) {
        this.command = command;
        this.work = product;
    }

    public CommandDescription(CommandType command, String args, LabWork product) {
        this.command = command;
        this.args = args;
        this.work = product;
    }

    public CommandDescription() {
    }


    public CommandType getCommand() {
        return this.command;
    }

    public void setCommand(CommandType command) {
        this.command = command;
    }

    public String getArgs() {
        return this.args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public LabWork getProduct() {
        return work;
    }

    public void setProduct(LabWork product) {
        this.work = product;
    }
}
