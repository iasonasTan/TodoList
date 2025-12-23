package com.app.storage;

import com.app.task.TaskData;
import java.io.*;
import java.util.Properties;

@SuppressWarnings("all")
public abstract class DataStorage {
    private static final String DIR_NAME = ".todo_list_data";
    private static final File dir;

    static {
        String home = System.getProperty("user.home");
        dir = new File(home, DIR_NAME);
        if (!dir.exists()||!dir.isDirectory()) {
            try {
                dir.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean updateFile (File f) {
        try {
            if (!f.exists()||!f.isFile()) {
                f.createNewFile();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // TASKS FILES
    private static final String TASKS_FILE_NAME = "tasks.dat";
    private static File tasksFile;

    static {
        tasksFile = new File(dir, TASKS_FILE_NAME);
        updateFile(tasksFile);
    }

    public static void writeTasks_data(TaskData[] tasks_data) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tasksFile));
            oos.writeObject(tasks_data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TaskData[] loadTasks_data() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tasksFile));
            TaskData[] out = (TaskData[]) ois.readObject();
            ois.close();
            return out;
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("File was probably empty");
            return new TaskData[]{};
        }
    }

    // CONFIGURATION
    private static final String CONFIGURATIONS_FILE_NAME = "configuration.properties";
    private static final File configurationsFile;

    static {
        configurationsFile = new File(dir, CONFIGURATIONS_FILE_NAME);
        updateFile(configurationsFile);
    }

    public static void writeProperties(Properties propertiesToWrite) {
        try {
            FileOutputStream fos = new FileOutputStream(configurationsFile);
            propertiesToWrite.store(fos, "updated settings");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties loadProperties () {
        Properties out = defaultProperties();
        try {
            FileInputStream fis = new FileInputStream(configurationsFile);
            out.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    private static Properties defaultProperties () {
        Properties out = new Properties();
        out.setProperty("theme", "dark");
        out.setProperty("win-width", "450");
        out.setProperty("win-height", "700");
        return out;
    }

}
