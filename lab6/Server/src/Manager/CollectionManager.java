package Manager;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import data.*;


/**
 * Класс для работы с коллекцией
 *
 * @version 1.0
 * @autor Svytoq
 */
public class CollectionManager {

    /**
     * Поле коллекция
     */
    private Vector<LabWork> collection;
    /**
     * Поле дата создания
     */
    private Date creationDate;
    /**
     * Поле файл, в котором хранится коллекция
     */
    private File file;
    protected static HashMap<String, String> manual;

    /**
     * Конструктор - создание объекта
     *
     * @param collection - коллекция для сохранения объектов
     */
    public CollectionManager(Vector<LabWork> collection) {
        this.collection = collection;
        this.creationDate = new Date();
    }

    {
        manual = new HashMap<>();
        manual.put("remove_first", "удалить первый элемент из коллекции.");
        manual.put("add", "Добавить новый элемент в коллекцию.");
        manual.put("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении.");
        manual.put("clear", "Очистить коллекцию.");
        manual.put("update", "обновить значение элемента коллекции, id которого равен заданному.");
        manual.put("update_id", "обновить значение id элемента коллекции, id которого равен заданному.");
        manual.put("info", "Вывести в стандартный поток вывода информацию о коллекции.");
        manual.put("remove_at", "удалить элемент, находящийся в заданной позиции коллекции.");
        manual.put("remove_by_id", "удалить элемент из коллекции по его id.");
        manual.put("add_if_max", " добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.");
        manual.put("exit", "Сохранить коллекцию в файл и завершить работу программы.");
        manual.put("max_by_author", "вывести любой объект из коллекции, значение поля author которого является максимальным.");
        manual.put("count_by_difficulty", "вывести количество элементов, значение поля difficulty которых равно заданному.");
        manual.put("filter_greater_than_minimal_point", "вывести элементы, значение поля minimalPoint которых больше заданного.");
    }

    /**
     * Выводит на экран список доступных пользователю команд.
     */
    public String help() {
        String s = "Данные коллекции сохраняются автоматически после каждой успешной модификации.\n" + "Команды: " + manual.keySet();
        return s;

    }

    /**
     * Показывает объекты в коллекции со всеми полями
     */
    public String show() {
        String s = "";
        LabWork product;
        if (this.collection != null && !this.collection.isEmpty()) {
            for (Iterator<LabWork> var2 = this.collection.iterator(); var2.hasNext(); s = s + product.toString() + "\n") {
                product = (LabWork) var2.next();
            }
        } else {
            s = "В коллекции нет элементов";
        }

        return s;
    }

    /**
     * Показывает информацию о коллекции: тип, дата создания, размер
     */
    public String info() {
        return "Тип коллекции: " + this.collection.getClass().toString() + " дата создания:" + this.creationDate + " размер: " + this.collection.size();
    }


    /**
     * Добавляет новый элемент в коллекцию
     *
     * @param product : объект класса Product
     */
    public String add(LabWork product) {
        String s;

        try {
            if (product != null) {
                this.collection.add(product);
                this.collection.sort(new Comparator<LabWork>() {
                    @Override
                    public int compare(LabWork o1, LabWork o2) {
                        if (o1.getX() + o1.getY() == o2.getX() + o2.getY()) return 0;
                        else if (o1.getX() + o1.getY() > o2.getX() + o2.getY()) return 1;
                        else return -1;
                    }
                });
                s = "Объект успешно добавлен";
            } else {
                s = "ERROR! Значение поля неверно.";
            }
        } catch (IllegalArgumentException e) {
            s = "ERROR! Значение поля неверно.";
        } catch (NullPointerException e) {
            s = "ERROR! Значение полей неверно.";
        }
        return s;
    }

    /**
     * метод для обновления объекта с заданным Id
     *
     * @param work - новый объект
     */
    public String update(LabWork work) {
        String s = "";
        try {
            int id = work.getId();
            boolean flag = false;

            Iterator it = this.collection.iterator();
            if (work != null) {
                while (it.hasNext()) {
                    LabWork work1 = (LabWork) it.next();
                    if (work1.getId() == id) {
                        this.collection.remove(work1);
                        this.collection.add(work);
                        this.collection.sort(new Comparator<LabWork>() {
                            @Override
                            public int compare(LabWork o1, LabWork o2) {
                                if (o1.getX() + o1.getY() == o2.getX() + o2.getY()) return 0;
                                else if (o1.getX() + o1.getY() > o2.getX() + o2.getY()) return 1;
                                else return -1;
                            }
                        });
                        s = "Элемент с id = " + id + " успешно обновлён";
                        flag = true;
                        break;
                    }
                }
            } else s = "ERROR! Значение поля неверно.";

            if (!flag) {
                s = "Элемента с таким id нет в коллекции";
            }
        }
        catch (IllegalArgumentException e) {
            s = "ERROR! Значение поля неверно.";
        } catch (NullPointerException e) {
            s = "ERROR! Значение полей неверно.";
        }

        return s;
    }


    /**
     * Метод для удаления объекта с заданным id
     *
     * @param argument - id объекта, который надо удалить
     */
    public String removeByID(String argument) {
        int id = Integer.parseInt(argument);
        boolean flag = false;
        String s = "";
        Iterator it = this.collection.iterator();

        while (it.hasNext()) {
            LabWork product1 = (LabWork) it.next();
            if (product1.getId() == id) {
                this.collection.remove(product1);
                s = "Элемент с id = " + id + " успешно удален";
                flag = true;
                break;
            }
        }

        if (!flag) {
            s = "элемента с таким id не существует";
        }

        return s;
    }

    /**
     * Удаляет все объекты из коллекции
     */
    public String clear() {
        this.collection.clear();
        return "элементы коллекции успешно удалены";
    }

    /**
     * Метод для добавления объекта, если он больше наибольшего объекта в коллекции
     *
     * @param product - объект для добавления
     */
    public String addIfMax(LabWork product) {
        String s = "";
        LabWork competitor = Collections.max(collection);
        if (competitor.getName().length() < product.getName().length()) {
            this.collection.add(product);
            this.collection.sort(new Comparator<LabWork>() {
                @Override
                public int compare(LabWork o1, LabWork o2) {
                    if (o1.getX() + o1.getY() == o2.getX() + o2.getY()) return 0;
                    else if (o1.getX() + o1.getY() > o2.getX() + o2.getY()) return 1;
                    else return -1;
                }
            });
            s = "Элемент успешно добавлен в коллекцию";
        } else {
            s = "Элемент меньше чем наибольший элемент в коллекции";
        }

        return s;
    }

    /**
     * Выводит любой объект из коллекции, значение поля author которого является максимальным
     */
    public String max_by_author() {
        String s = null;
        Vector<LabWork> works = this.collection;
        if (works.size() != 0) {
            try {
                ArrayList names = new ArrayList<>();
                for (LabWork work : works) {
                    String a = work.getAuthor().getName();
                    names.add(a);
                }

                Comparable name = Collections.max(names);
                String Max_name = name.toString();
                for (LabWork work : works) {
                    if (work.getAuthor().getName() == Max_name) {
                        s = work.toString();
                    }
                }
            } catch (NoSuchElementException e) {
                s = "Элемент не с чем сравнивать. Коллекция пуста.";
            }
        } else s = "Элемент не с чем сравнивать. Коллекция пуста.";
        return s;
    }

    /**
     * Считает количество элементов, значение поля difficulty которых равно заданному
     */
    public String count_by_difficulty(String addCommand) {
        String s = null;
        Vector<LabWork> works = this.collection;
        if (works.size() != 0) {
            int c = 0;
            int n = 0;
            for (LabWork work : works) {
                if (work.getDifficulty() != null) {

                    if (Objects.equals(work.getDifficulty().toString(), addCommand) && work.getDifficulty() != null) {

                        c += 1;
                    }
                } else {
                    n += 1;
                    s += "У " + n + " элементов коллекции сложности нет.";
                }


            }
            if (!Objects.equals(addCommand, "")) {
                s = "Количество элементов со сложностью " + addCommand + "=" + c;
            } else s = "Вы не ввели сложность.";
        } else s = "Элемент не с чем сравнивать. Коллекция пуста.";
        return s;
    }

    /**
     * Выводит элементы, значение поля minimalPoint которых больше заданного
     *
     * @param point
     */
    public String filter_greater_than_minimal_point(String point) {
        String s = null;
        Vector<LabWork> works = this.collection;
        try {
            if (works.size() != 0) {
                for (LabWork work : works) {
                    if (work.getMinimalPoint() == Double.parseDouble(point)) {
                        return work.toString();
                    } else {
                        s = "Значения не равны.";
                    }
                }
            } else s = "Элемент не с чем сравнивать. Коллекция пуста.";
        } catch (NumberFormatException e) {
            s = "Неверный формат введенных данных.";
        }
        return s;
    }

    public static boolean isNumeric(String string) {
        int intValue;


        if (string == null || string.equals("")) {

            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {

        }
        return false;
    }

    public static boolean isDouble(String string) {
        double intValue;


        if (string == null || string.equals("")) {
            ;
            return false;
        }

        try {
            intValue = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {

        }
        return false;
    }

    public LabWork script_add(String line) throws ParseException {
        String[] args = line.split(",");
        if (isNumeric(args[1]) && isNumeric(args[2]) && isDouble(args[3])) {
            LabWork W = null;
            try {
                int id = create_id();
                String name = args[0];
                String pname = null;
                Color color = null;
                Country country = null;
                Date birth = null;
                Coordinates coordinates = new Coordinates(Long.parseLong(args[1]), Long.parseLong(args[2]));
                java.util.Date creationDate = java.util.Date.from(Instant.now());
                Double minimalPoint = Double.parseDouble(args[3]);
                // System.out.println(args[0]+args[1]+args[2]+args[3]);
                if (Objects.equals(args[4], "EASY") || Objects.equals(args[4], "HARD") || Objects.equals(args[4], "VERY_HARD") || Objects.equals(args[4], "HOPELESS")) {
                    Difficulty diff = Difficulty.valueOf(args[4]);
                    pname = args[5];
                    color = Color.valueOf(args[6]);
                    if (args.length > 7) {
                        country = Country.valueOf(args[7]);
                        if (args.length > 8) {
                            birth = new SimpleDateFormat("dd.MM.yyyy").parse(args[8]);
                            Person p = new Person(pname, birth, color, country);
                            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);

                        } else {
                            Person p = new Person(pname, color, country);
                            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);
                        }
                    } else {
                        Person p = new Person(pname, color);
                        W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);
                    }
                } else {
                    pname = args[4];
                    color = Color.valueOf(args[5]);
                    if (args.length > 6) {
                        country = Country.valueOf(args[6]);
                        if (args.length > 7) {
                            birth = new SimpleDateFormat("dd.MM.yyyy").parse(args[7]);
                            Person p = new Person(pname, birth, color, country);
                            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                        } else {
                            Person p = new Person(pname, color, country);
                            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                        }
                    } else {
                        Person p = new Person(pname, color);
                        W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                    }
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR! Значение поля неверно");
            } catch (NullPointerException e) {
                System.out.println("ERROR! Значение полей неверно");
            }
            return W;
        }
        return null;
    }

    public LabWork script_update(String line) throws ParseException {
        String[] args = line.split(",");

        if (isNumeric(args[0]) && isNumeric(args[2]) && isNumeric(args[3]) && isDouble(args[4])) {
            LabWork W = null;
            try {
                int id = Integer.parseInt(args[0].trim());
                String name = args[1];
                String pname = null;
                Color color = null;
                Country country = null;
                Date birth = null;
                Coordinates coordinates = new Coordinates(Long.parseLong(args[2]), Long.parseLong(args[3]));
                java.util.Date creationDate = java.util.Date.from(Instant.now());
                Double minimalPoint = Double.parseDouble(args[4]);
                // System.out.println(args[0]+args[1]+args[2]+args[3]);
                if (Objects.equals(args[5], "EASY") || Objects.equals(args[5], "HARD") || Objects.equals(args[5], "VERY_HARD") || Objects.equals(args[5], "HOPELESS")) {
                    Difficulty diff = Difficulty.valueOf(args[5]);
                    pname = args[6];
                    color = Color.valueOf(args[7]);
                    if (args.length > 8) {
                        country = Country.valueOf(args[8]);
                        if (args.length > 9) {
                            birth = new SimpleDateFormat("dd.MM.yyyy").parse(args[9]);
                            Person p = new Person(pname, birth, color, country);
                            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);

                        } else {
                            Person p = new Person(pname, color, country);
                            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);
                        }
                    } else {
                        Person p = new Person(pname, color);
                        W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);
                    }
                } else {
                    pname = args[5];
                    color = Color.valueOf(args[6]);
                    if (args.length > 7) {
                        country = Country.valueOf(args[7]);
                        if (args.length > 8) {
                            birth = new SimpleDateFormat("dd.MM.yyyy").parse(args[8]);
                            Person p = new Person(pname, birth, color, country);
                            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                        } else {
                            Person p = new Person(pname, color, country);
                            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                        }
                    } else {
                        Person p = new Person(pname, color);
                        W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                    }
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR! Значение поля неверно");
            } catch (NullPointerException e) {
                System.out.println("ERROR! Значение полей неверно");
            }
            return W;
        }
        return null;
    }

    public String script_add_if_max(LabWork W) {
        String s;
        if (!this.collection.isEmpty()) {
            LabWork competitor = Collections.max(this.collection);

            if (competitor.getName().length() < W.getName().length()) {
                this.collection.add(W);
                s = "Элемент успешно добавлен.";
            } else s = "Не удалось добавить элемент. Он меньше максимального.";
        } else s = "Элемент не с чем сравнивать. Коллекция пуста.";
        return s;
    }

    public String update_id(String t) {
        boolean flag = false;
        String s = null;
        Vector<LabWork> works = this.collection;
        if (works.size() != 0) {
            try {
                if (t != null) {
                    int new_id = Integer.parseInt(t);
                    for (LabWork p : works) {
                        if (p != null && p.getId() == new_id) {
                            int id = create_id();
                            p.setId(id);
                            s = "Id обновлено.";
                            flag = true;
                        }
                    }
                    if (!flag) {
                        s = "Нет такого id.";
                    }
                } else {
                    s = "Ошибка! Id не найдено.";
                }
            } catch (NoSuchElementException ex) {
                s = "Ошибка! Id не найдено.";
            }
        } else s = "Элемент не с чем сравнивать. Коллекция пуста.";

        return s;

    }

    /**
     * @return unique number.
     */
    public static int create_id() {
        return (int) Math.round(Math.random() * 32767 * 10);
    }

    /**
     * Метод для удаления объектов по индексу
     *
     * @param i - index
     */
    public String removeAt(String i) {
        try {
            int in = Integer.parseInt(i);
            collection.remove(in - 1);

            return "Элемент в коллекции удалён.";
        } catch (NoSuchElementException ex) {
            return "Вы не можете удалить элемент, так как коллекция пуста.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Элемента по данному индексу не существует.";
        }
    }

    /**
     * Метод для удаления первого элемента
     */
    public String removeFirst() {
        this.collection.remove(0);
        return "Элемент удален.";
    }


    /**
     * Метод для загрузки коллекции
     *
     * @param collection1 - коллекция загруженная из файла
     */
    public void load(Vector<LabWork> collection1) {
        if (collection1 != null) {
            this.collection.addAll(collection1);
        }

    }

    public Vector<LabWork> getCollection() {
        return this.collection;
    }
}
