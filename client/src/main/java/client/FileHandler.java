package client;

import java.io.*;

public class FileHandler {
    private File file;
    BufferedWriter bw;
    BufferedReader br;

    FileHandler(String nick) {
        try  {
            file = new File("client/src/main/storage/", "history_" + nick + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(file, true));

        } catch (IOException e) {
            System.out.println("Проблема с созданием файла!");
        }
    }

    public void setMessage(String text) {
        try {
            bw.write(text);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка записи");
        }
    }

    public String readMessage() {
        try {
            return br.readLine();
        } catch (IOException e) {
            System.out.println("Ошибка чтения");
            return null;
        }
    }

    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            System.out.println("Проблема с акрытием потока файла!");
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                System.out.println("Проблема с акрытием потока файла!");
            }
        }
    }

}
