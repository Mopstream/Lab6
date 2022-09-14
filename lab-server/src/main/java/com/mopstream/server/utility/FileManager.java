package com.mopstream.server.utility;

import java.io.*;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.NoSuchElementException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mopstream.common.data.LabWork;
import com.mopstream.common.utility.Outputer;

/**
 * Operates the file for saving/loading collection.
 */
public class FileManager {
    private final Gson gson = new Gson();
    private final String envVariable;

    public FileManager(String envVariable) {
        this.envVariable = envVariable;
    }

    /**
     * Writes collection to a file.
     *
     * @param collection Collection to write.
     */
    public void writeCollection(Collection<?> collection) {
        if (System.getenv().get(envVariable) != null) {
            try (FileWriter collectionFileWriter = new FileWriter(System.getenv().get(envVariable))) {
                collectionFileWriter.write(gson.toJson(collection));
                ResponseOutputer.appendln("Коллекция успешно сохранена в файл.");
            } catch (IOException exception) {
                ResponseOutputer.appenderror("Загрузочный файл является директорией/не может быть открыт!");
            }
        } else ResponseOutputer.appenderror("Системная переменная с загрузочным файлом не найдена!");
    }

    /**
     * Reads collection from a file.
     *
     * @return Read collection.
     */
    public PriorityQueue<LabWork> readCollection() {
        if (System.getenv().get(envVariable) != null) {
            try (BufferedReader collectionFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(System.getenv().get(envVariable))))) {
                PriorityQueue<LabWork> collection = new PriorityQueue<>();
                Type collectionType = new TypeToken<LabWork>() {
                }.getType();
                String strCollection = collectionFileReader.readLine().trim();
                if (strCollection.length() > 2) {
                    String[] MasCollectiion = strCollection.substring(2, strCollection.length() - 2).split("\\}\\s*,\\s*\\{");
                    for (int i = 0; i < MasCollectiion.length; ++i) {
                        try {
                            MasCollectiion[i] = '{' + MasCollectiion[i] + '}';
                            LabWork temp = gson.fromJson(MasCollectiion[i], collectionType);
                            for (LabWork labWork : collection) {
                                if (labWork.getId().equals(temp.getId())) {
                                    throw new Exception();
                                }
                            }
                            if ((temp.getId() <= 0) || (temp.getName().equals("")) || (temp.getCoordinates().getY() > 334) || (temp.getMinimalPoint() <= 0) || (temp.getDiscipline().getName().equals("")))
                                throw new Exception();
                            collection.add(temp);
                        } catch (Exception exception) {
                            Outputer.println("Пропуск объекта");
                        }
                    }
                } else throw new NullPointerException();
                ResponseOutputer.appendln("Коллекция успешна загружена!");
                return collection;
            } catch (FileNotFoundException exception) {
                Outputer.printerror("Загрузочный файл не найден!");
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Загрузочный файл пуст!");
            } catch (JsonParseException | NullPointerException exception) {
                Outputer.printerror("В загрузочном файле не обнаружена необходимая коллекция!");
            } catch (IllegalStateException | IOException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        } else Outputer.printerror("Системная переменная с загрузочным файлом не найдена!");
        return new PriorityQueue<LabWork>();
    }

    @Override
    public String toString() {
        return "FileManager (класс для работы с загрузочным файлом)";
    }
}