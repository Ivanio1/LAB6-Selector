package Manager;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.*;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.LabWork;


/**
 * Класс отвечающий за работу с файлами
 *
 * @version 1.0
 * @autor Svytoq
 */
public class FileManager {
    /**
     * Поле путя к файлу
     */
    String nameFile;
    private Vector<LabWork> works = new Vector<>();
    private File jsonCollection;
    private File outPut;
    private Date initDate;
    private Type collectionType;
    private Gson gson = new Gson();
    protected static HashMap<String, String> manual;
    private static final Logger logger = Logger.getLogger("Logger");

    /**
     * Конструктор - создание нового объекта
     */
    public FileManager() {
    }

    /**
     * Десериализует коллекцию из файла json.
     *
     * @return
     * @throws IOException если файл пуст или защищён.
     */
    public Vector<LabWork> load(String collectionPath, String output1) throws IOException {
        this.outPut = new File(output1);
        try {
            if (collectionPath == null) throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "Путь к файлу должен быть задан через командную строку.");
            System.exit(1);
        }
        jsonCollection = new File(collectionPath);
        try {
            String extension = jsonCollection.getAbsolutePath().substring(jsonCollection.getAbsolutePath().lastIndexOf(".") + 1);
            if (!jsonCollection.exists() | !extension.equals("json")) throw new FileNotFoundException();
            if (jsonCollection.length() == 0) throw new Exception("Файл пуст");
            if (!jsonCollection.canRead() || !jsonCollection.canWrite()) throw new SecurityException();
            try (BufferedReader collectionReader = new BufferedReader(new FileReader(jsonCollection))) {
                logger.log(Level.INFO, "Загрузка коллекции " + jsonCollection.getAbsolutePath());
                String nextLine;
                StringBuilder result = new StringBuilder();
                while ((nextLine = collectionReader.readLine()) != null) {
                    result.append(nextLine);

                }


                //System.out.println(result);

                Type collectionType = new com.google.gson.reflect.TypeToken<Vector<LabWork>>() {
                }.getType();
                try {
                    works = gson.fromJson(result.toString(), collectionType);
                    logger.log(Level.INFO, "Коллекция успешно загружена. Добавлено " + (works.size()) + " элементов.");

                    // System.out.println(works);
                    for (LabWork element : works) {
                        int id = create_id();
                        element.setId(id);
                        if (element.getDate() != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);
                            Date cdate = formatter.parse(element.getDate());
                            element.setCreationDate(cdate);

                        }

                    }
                    works.sort(new Comparator<LabWork>() {
                        @Override
                        public int compare(LabWork o1, LabWork o2) {
                            if (o1.getX() + o1.getY() == o2.getX() + o2.getY()) return 0;
                            else if (o1.getX() + o1.getY() > o2.getX() + o2.getY()) return 1;
                            else return -1;
                        }
                    });

                } catch (JsonSyntaxException ex) {
                    logger.log(Level.SEVERE, "Ошибка синтаксиса Json. Коллекция не может быть загружена.");
                    System.exit(1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Файл по указанному пути не найден или он пуст.");
            System.exit(1);
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "Файл защищён от чтения.");
            System.exit(1);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Что-то не так с файлом.");
            System.exit(1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Файл пуст");
            System.exit(1);
        }
        return works;
    }

    /**
     * Сериализует коллекцию в файл json.
     */
    public String save(Vector<LabWork> works) throws IOException {
        String s="";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((outPut))))) {
            writer.write(gson.toJson(works));
            s="Коллекция успешно сохранена.";
        } catch (Exception ex) {
            s="Возникла непредвиденная ошибка. Коллекция не может быть записана в файл.";
        }
        return s;
    }


    /**
     * @return unique number.
     */
    public static int create_id() {
        return (int) Math.round(Math.random() * 32767 * 10);
    }

    /**
     * @return current date.
     */
    public static Date create_date() {

        Date date = new Date();
        return date;
    }
}

