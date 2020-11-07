package editor;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {
    TextEditor textEditor;

    public FileManager (TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    public String readFile(File fileToRead) {
        byte[] fileContent = null;

        try (BufferedReader bReader = new BufferedReader(new FileReader(fileToRead) )) {

            fileContent = Files.readAllBytes(Paths.get(fileToRead.getPath()));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(textEditor, "Cannot find the specified file!");
            e.printStackTrace();
            return "";
        }

        return new String(fileContent);
    }

    public void writeFile(File fileToWrite, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite))) {
            writer.write(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
