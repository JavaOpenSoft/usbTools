package Writer;

import com.github.stephenc.javaisotools.loopfs.iso9660.Iso9660FileEntry;
import com.github.stephenc.javaisotools.loopfs.iso9660.Iso9660FileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Writer {
    static String inputDrive;
    public Writer(String inputDrive){
        Writer.inputDrive = inputDrive;
    }
    public static void format(){}
    public static void rename(){}
    public static void formatVolume(){}
    public static void writeIsoToDrive(String inputIsoPath) throws IOException {
        new WriteISO(new File(inputIsoPath), new File(inputDrive));
    }
}
class WriteISO {
    // Declaring a variable for the ISO Input
    Iso9660FileSystem discFs;
    Runtime runCommand = Runtime.getRuntime();

    public WriteISO(File isoToWrite, File saveLocation) throws IOException {


        try {
            //Give the file and mention if this is treated as a read only file.
            discFs = new Iso9660FileSystem(isoToWrite, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Make our saving folder if it does not exist
        if (!saveLocation.exists()) {
            saveLocation.mkdirs();
        }

        //Go through each file on the disc and save it.
        for (Iso9660FileEntry singleFile : discFs) {
            if (singleFile.isDirectory()) {
                new File(saveLocation, singleFile.getPath()).mkdirs();
            } else {
                File tempFile = new File(saveLocation, singleFile.getPath());
                try {
                    Files.copy(discFs.getInputStream(singleFile), tempFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getDD_Command(String input, String output) {
        return "dd if=" + input + " of=" + output + "status=progress";

    }
}
