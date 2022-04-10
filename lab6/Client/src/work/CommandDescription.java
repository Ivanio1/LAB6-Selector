package work;

import data.LabWork;


import java.io.Serializable;

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

    private static final long serialVersionUID = 17L;

    private LabWork work;

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

    public void setCommand(CommandType command) {
        this.command = command;
    }

    /**
     * Метод получения поля типа команды
     * @return command - тип команды
     */
    public CommandType getCommand() {
        return command;
    }

    /**
     * Метод получения поля аргументов
     * @return command - аргументы
     */
    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public LabWork getWork() {
        return work;
    }

    public void setWork(LabWork work) {
        this.work = work;
    }
}
