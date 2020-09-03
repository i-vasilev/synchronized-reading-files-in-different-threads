import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static final String SYNC_FILE = "statuses.txt";

    public static void main(String[] args) {
        int i = -1;
        while (i == -1) {
            i = getSyncronized();
            if (i != -1) {
                changeStatus(i, true);
                for (int k = 0; k < 100; k++) {
                    try (FileWriter writer = new FileWriter(i + ".txt", true)) {
                        writer.append(String.valueOf(k));
                        Thread.sleep(1000);
                    } catch (IOException | InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                changeStatus(i, false);
            }
        }
    }

    private static int getSyncronized() {
        Map<Integer, Boolean> config;
        try (FileInputStream fis = new FileInputStream(SYNC_FILE)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            config = (Map<Integer, Boolean>) ois.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            return -1;
        }
        for (Map.Entry<Integer, Boolean> entry :
                config.entrySet()) {
            if (!entry.getValue()) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private static void changeStatus(int i, boolean status) {
        Map<Integer, Boolean> config;
        try (FileInputStream fis = new FileInputStream(SYNC_FILE)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            config = (Map<Integer, Boolean>) ois.readObject();
            config.put(i, status);

            saveConfigToFile(config);

        } catch (IOException | ClassNotFoundException ex) {
        }
    }

    private static void saveConfigToFile(Map<Integer, Boolean> config) {
        try (FileOutputStream fos = new FileOutputStream(SYNC_FILE)) {
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(config);
        } catch (IOException ex) {

        }
    }
}
