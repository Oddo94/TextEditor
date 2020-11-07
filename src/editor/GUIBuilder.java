package editor;

import java.util.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GUIBuilder {
    private JTextArea textArea;
    private JTextField searchInput;
    private JButton saveButton;
    private JButton loadButton;
    private JButton searchButton;
    private JButton previousMatchButton;
    private JButton nextMatchButton;
    public JCheckBox regexCheckBox;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu searchMenu;
    private JMenuItem loadMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem startSearchMenuItem;
    private JMenuItem previousMatchMenuItem;
    private JMenuItem nextMatchMenuItem;
    private JMenuItem useRegexMenuItem;
    private JFileChooser fileChooser;
    private JPanel fileManagementPanel;
    public TextEditor textEditor;
    private FileManager fileManager = new FileManager(textEditor);
    private SearchManager searchManager = new SearchManager(this);
    private File currentFile;
    public boolean regexMenuItemSelected;

    public GUIBuilder(TextEditor textEditor) {

        this.textEditor = textEditor;
    }

    public void setUpWindow() {
        createGUIComponents();
        addComponentsToMenuBar();
        addComponentsToFileManagementPanel();

        textEditor.add(createScrollableTextArea(textArea,"ScrollPane"),BorderLayout.CENTER);
        textEditor.add(fileManagementPanel,BorderLayout.NORTH);
        textEditor.add(fileChooser, BorderLayout.SOUTH);
        textEditor.setJMenuBar(menuBar);

        setSearchButtonsState(false);
        addActionListenersToManagementPanelItems();
        addActionListenersToFileMenuItems();
        addActionListenersToSearchMenuItems();

    }

    private void createGUIComponents() {
        menuBar = createMenuBar();
        fileManagementPanel = createFileManagementPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        textArea = createTextArea();
        fileChooser = createFileChooser("FileChooser");

        createMenuBarComponents();
        createFileManagementPanelComponents();
    }

    private void createMenuBarComponents() {
        fileMenu = createMenu("File");
        searchMenu = createMenu("Search");
        loadMenuItem = createMenuItem("Open", "MenuOpen");
        saveMenuItem = createMenuItem("Save","MenuSave");
        exitMenuItem = createMenuItem("Exit","MenuExit");
        startSearchMenuItem = createMenuItem("Start search","MenuStartSearch");
        previousMatchMenuItem = createMenuItem("Previous match","MenuPreviousMatch");
        nextMatchMenuItem = createMenuItem("Next match","MenuNextMatch");
        useRegexMenuItem = createMenuItem("Use regular expressions", "MenuUseRegExp");
    }

    private void createFileManagementPanelComponents() {
        searchInput = createFileNameInput("SearchField");

        ImageIcon saveIcon = createImageIcon("icons8-save-32.png");
        ImageIcon loadIcon = createImageIcon("icons8-open-file-folder-32.png");
        ImageIcon searchIcon = createImageIcon("icons8-search-32.png");
        ImageIcon previousMatchIcon = createImageIcon("icons8-less-than-32.png");
        ImageIcon nextMatchIcon = createImageIcon("icons8-more-than-32.png");

        saveButton = createButton("SaveButton");
        fitImageIconToButton(saveButton,saveIcon);

        loadButton = createButton("OpenButton");
        fitImageIconToButton(loadButton,loadIcon);

        searchButton = createButton("StartSearchButton");
        fitImageIconToButton(searchButton, searchIcon);

        previousMatchButton = createButton("PreviousMatchButton");
        fitImageIconToButton(previousMatchButton, previousMatchIcon);

        nextMatchButton = createButton("NextMatchButton");
        fitImageIconToButton(nextMatchButton, nextMatchIcon);

        regexCheckBox = new JCheckBox();
        regexCheckBox.setName("UseRegExCheckbox");
        regexCheckBox.setText("Use regex");
    }

    private void addComponentsToMenuBar() {
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);

        searchMenu.add(startSearchMenuItem);
        searchMenu.add(previousMatchMenuItem);
        searchMenu.add(nextMatchMenuItem);
        searchMenu.add(useRegexMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
    }

    private void addComponentsToFileManagementPanel() {
        fileManagementPanel.add(saveButton);
        fileManagementPanel.add(loadButton);
        fileManagementPanel.add(searchInput);
        fileManagementPanel.add(searchButton);
        fileManagementPanel.add(previousMatchButton);
        fileManagementPanel.add(nextMatchButton);
        fileManagementPanel.add(regexCheckBox);
    }

    public JTextArea createTextArea() {
        JTextArea textArea = new JTextArea(10, 10);
        textArea.setName("TextArea");

        return textArea;
    }

    public JScrollPane createScrollableTextArea(JTextArea textArea, String scrollPaneTitle) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setName(scrollPaneTitle);

        return scrollPane;
    }

    public JTextField createFileNameInput(String textFieldTitle) {
        JTextField fileNameInput = new JTextField(15);
        fileNameInput.setName(textFieldTitle);

        return fileNameInput;
    }

    public JButton createButton(String buttonName) {
        JButton newButton = new JButton();
        newButton.setName(buttonName);

        return newButton;
    }

    private ImageIcon createImageIcon(String fileName) {
        String iconFileLocation = "D:/IAP/Proiectele mele/Text Editor/Text Editor/task/resources/" + fileName;
        ImageIcon newIcon = new ImageIcon(iconFileLocation);

        return newIcon;
    }

    private void fitImageIconToButton(JButton button, ImageIcon imageIcon) {
        button.setIcon(imageIcon);
        Dimension prefferedSize = new Dimension(imageIcon.getIconWidth(),imageIcon.getIconHeight());
        button.setPreferredSize(prefferedSize);
    }

    public JMenuBar createMenuBar() {

        return new JMenuBar();
    }

    public JMenu createMenu(String menuTitle) {
        JMenu menu = new JMenu(menuTitle);
        menu.setName("Menu" + menuTitle);

        return menu;
    }

    public JMenuItem createMenuItem(String menuItemTitle, String menuItemName) {
        JMenuItem menuItem = new JMenuItem(menuItemTitle);
        menuItem.setName(menuItemName);

        return menuItem;
    }

    public JPanel createFileManagementPanel(LayoutManager layout) {

        return new JPanel(layout);
    }

    private JFileChooser createFileChooser(String fileChooserTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setName(fileChooserTitle);
        //fileChooser.showDialog(null,"Click");
        fileChooser.setVisible(false);

        return fileChooser;
    }


    private void addActionListenersToManagementPanelItems() {
        saveButton.addActionListener(actionEvent -> {
            fileChooser.setVisible(true);
            int fileChooserStatus = fileChooser.showDialog(null,"Save");

            if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                String content = textArea.getText();
                fileManager.writeFile(currentFile, content);

            } else if (fileChooserStatus == JFileChooser.CANCEL_OPTION) {
                fileChooser.setVisible(false);
            }
        });

        loadButton.addActionListener(actionEvent -> {
            fileChooser.setVisible(true);
            int fileChooserStatus = fileChooser.showDialog(null,"Open");

            if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {

                if (fileChooser.getSelectedFile().length() > 0L) {
                    setSearchButtonsState(true);
                }

                currentFile = fileChooser.getSelectedFile();
                String fileContent = fileManager.readFile(currentFile);

                textArea.setText(fileContent);

            } else if (fileChooserStatus == JFileChooser.CANCEL_OPTION) {
                fileChooser.setVisible(false);
            }
        });



        searchInput.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                String textAreaContent = textArea.getText();
                if(!"".equals(searchInput.getText()) && !"".equals(textAreaContent)) {
                    setSearchButtonsState(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                String textAreaContent = textArea.getText();
                if(!"".equals(searchInput.getText()) && !"".equals(textAreaContent)) {
                    setSearchButtonsState(true);
                } else {
                    setSearchButtonsState(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                if(!"".equals(searchInput.getText())) {
                    setSearchButtonsState(true);
                }
            }

        });

        searchButton.addActionListener(actionEvent -> {
            String item = searchInput.getText();
            String text = textArea.getText();

            searchManager.search(item, text);
        });

        previousMatchButton.addActionListener(actionEvent -> {
            ArrayList<Integer> matchesArray = SearchManager.matchesArray;

            if (SearchManager.currentMatch == 0) {
                SearchManager.currentMatch = matchesArray.size() - 1;
                highlightTextMatch(matchesArray.get(SearchManager.currentMatch), SearchManager.matchLength);
                return;
            }

            SearchManager.currentMatch -= 1;
            highlightTextMatch(matchesArray.get(SearchManager.currentMatch), SearchManager.matchLength);

        });

        nextMatchButton.addActionListener(actionEvent -> {
            ArrayList<Integer> matchesArray = SearchManager.matchesArray;

            if (SearchManager.currentMatch == matchesArray.size() - 1) {
                SearchManager.currentMatch = 0;
                highlightTextMatch(matchesArray.get(SearchManager.currentMatch), SearchManager.matchLength);
                return;
            }

            if (matchesArray.size() != 0 && SearchManager.currentMatch < matchesArray.size()) {
                SearchManager.currentMatch += 1;
                highlightTextMatch(matchesArray.get(SearchManager.currentMatch), SearchManager.matchLength);
            }
        });
    }


    private void addActionListenersToFileMenuItems() {
        loadMenuItem.addActionListener(actionEvent -> {
            fileChooser.setVisible(true);
            int fileChooserStatus = fileChooser.showDialog(null,"Open");

            if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {

                currentFile = fileChooser.getSelectedFile();
                String fileContent = fileManager.readFile(currentFile);

                textArea.setText(fileContent);

            } else if (fileChooserStatus == JFileChooser.CANCEL_OPTION) {
                fileChooser.setVisible(false);
            }
        });

        saveMenuItem.addActionListener(actionEvent -> {
            fileChooser.setVisible(true);
            int fileChooserStatus = fileChooser.showDialog(null,"Save");

            if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                String content = textArea.getText();
                fileManager.writeFile(currentFile, content);

            } else if (fileChooserStatus == JFileChooser.CANCEL_OPTION) {
                fileChooser.setVisible(false);
            }
        });

        exitMenuItem.addActionListener(actionEvent -> {
            System.exit(0);
        });
    }

    private void addActionListenersToSearchMenuItems() {
        startSearchMenuItem.addActionListener(actionEvent -> {
            if (canSearch()) {
                String item = searchInput.getText();
                String text = textArea.getText();

                searchManager.search(item, text);
            }
        });

        previousMatchMenuItem.addActionListener(actionEvent -> {
            ArrayList<Integer> matchesArray = SearchManager.matchesArray;

            if (SearchManager.currentMatch == 0) {
                SearchManager.currentMatch = matchesArray.size() - 1;
                highlightTextMatch(matchesArray.get(SearchManager.currentMatch), SearchManager.matchLength);
                return;
            }

            SearchManager.currentMatch -= 1;
            highlightTextMatch(matchesArray.get(SearchManager.currentMatch), SearchManager.matchLength);
        });

        nextMatchMenuItem.addActionListener(actionEvent -> {
            ArrayList<Integer> matchesArray = SearchManager.matchesArray;

            if (SearchManager.currentMatch == matchesArray.size() - 1) {
                SearchManager.currentMatch = 0;
                highlightTextMatch(matchesArray.get(SearchManager.currentMatch), SearchManager.matchLength);
                return;
            }


            if (matchesArray.size() != 0 && SearchManager.currentMatch < matchesArray.size()) {
                SearchManager.currentMatch += 1;
                highlightTextMatch(matchesArray.get(SearchManager.currentMatch), SearchManager.matchLength);
            }
        });

        useRegexMenuItem.addActionListener(actionEvent -> {
            if (!regexCheckBox.isSelected()) {
                regexCheckBox.setSelected(true);
            } else {
                regexCheckBox.setSelected(false);
            }
        });
    }

    public void highlightTextMatch(int startIndex, int matchLength) {
        int endIndex = startIndex + matchLength;
        textArea.select(startIndex, endIndex);
        textArea.grabFocus();
    }

    public void setCaretPosition(int startIndex, int matchLength) {
        int endIndex = startIndex + matchLength;
        textArea.setCaretPosition(endIndex);
    }


    private void setSearchButtonsState(boolean value) {
        searchButton.setEnabled(value);
        previousMatchButton.setEnabled(value);
        nextMatchButton.setEnabled(value);
        regexCheckBox.setEnabled(value);

    }

    private boolean canSearch() {
        if (!"".equals(searchInput.getText()) && !"".equals(textArea.getText())) {
            return true;
        }

        return false;
    }
}
