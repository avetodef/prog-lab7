package file;


import dao.RouteDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Класс, который позволяет осуществлять корректную запись данных в файл
 */
public class FileWriter {


    private static String directory = System.getenv().get("collection.csv");
    private static final String TEMP_FILE = "C:\\collection.csv";
    File file = new File(directory);
    /**
     * Метод записи данных о коллекции в файл
     * @param routeDAO
     */

    public void write(RouteDAO routeDAO)  {
        try{
            if (!file.exists()) {
                if(!file.createNewFile())
                    System.out.println("Файл не создан");
            }
            FileOutputStream fos = new FileOutputStream(file);

            String toBeWritten = (routeDAO.getDescription());

            fos.write(toBeWritten.getBytes(StandardCharsets.UTF_8));

            fos.flush();
            fos.close();
            //System.out.println("элемент успешно сохранен");

        }
        catch (IOException e){
            //saveToTmp(routeDAO);
            System.out.println("не удалось сохранить: "+e.getMessage());
        }

    }

    public void save(RouteDAO dao) throws IOException{

        String filepath = directory;

        try
        {
            FileOutputStream stream = new FileOutputStream(filepath);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            String description = dao.getDescription();
            writer.write(description);
            write(dao);
        } catch (IOException exception){
            directory = TEMP_FILE;
            file = new File(directory);
            save(dao);
        }
    }

//    private static String saveToTmp(RouteDAO dao) {
//        List<File> tmpFiles = getTmpFiles();
//
//        String tmpToSave = null;
//
//        for (File f: tmpFiles) {
//            if (f.canWrite()) {
//                tmpToSave = f.getName();
//                break;
//            }
//        }
//
//        if (tmpToSave == null)
//            tmpToSave = createTmpFile();
//        try {
//            save(dao, tmpToSave);
//        } catch (IOException e){
//            //..
//        }
//        return tmpToSave;
//    }
//    public static List<File> getTmpFiles() {
//        File dir = new File(directory);
//
//        return Arrays.stream(dir.listFiles()).filter(File::isFile).filter(f -> f.getName().contains(".tmp")).toList();
//    }
//    public static String createTmpFile() {
//        String name = generateTmpFileName();
//        File dir = new File(directory, name);
//        try {
//            dir.createNewFile();
//            if(!dir.createNewFile())
//                System.out.println("Файл не создан");
//            return name;
//        } catch (IOException e) {
//            //...
//        }
//        return null;
//    }
//    private static String generateTmpFileName() {
//        Random r = new Random(System.currentTimeMillis());
//        List<File> tmpFiles = getTmpFiles();
//        while (true) {
//            String fileName = "%d.tmp".formatted(Math.abs(r.nextLong()));
//            if (tmpFiles.stream().noneMatch(f -> f.getName().contains(fileName)))
//                return fileName;
//        }
//    }


}
