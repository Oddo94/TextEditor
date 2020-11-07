package editor;
import javax.swing.*;
import java.awt.*;

public class TextEditor extends JFrame {

    private FileManager fileManager = new FileManager(this);
    private GUIBuilder builder = new GUIBuilder(this);

    public TextEditor() {
        setTitle("Text Editor");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        builder.setUpWindow();

        setVisible(true);
    }
}
