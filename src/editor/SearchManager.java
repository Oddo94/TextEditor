package editor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SearchManager {
    private GUIBuilder builder;
    private String text;
    public static ArrayList<Integer> matchesArray;
    public static int currentMatch = 0;
    public static int matchLength = 0;

    public SearchManager(GUIBuilder builder) {
        this.builder = builder;
        matchesArray = new ArrayList<>();
    }

    public void search(String searchedItem, String text) {
        if (builder.regexCheckBox.isSelected() && !isRegex(searchedItem)) {
            JOptionPane.showMessageDialog(builder.textEditor,"Invalid regex pattern!" , "Warning!", JOptionPane.WARNING_MESSAGE );
            return;
        }
        Pattern pattern = null;
        Matcher matcher = null;

        try {
            pattern = Pattern.compile(searchedItem);
            matcher = pattern.matcher(text);

        } catch (PatternSyntaxException ex) {
            JOptionPane.showMessageDialog(builder.textEditor,"Please select the regex search mode when using regular expressions as input!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        matchesArray.clear();
        currentMatch = 0;
        matchLength = 0;

        if (matcher.find()) {
            int startIndex = matcher.start();
            matchesArray.add(startIndex);
            matchLength = matcher.end() - matcher.start();
            builder.setCaretPosition(startIndex, matchLength);
            builder.highlightTextMatch(startIndex,matchLength);
        } else {
            JOptionPane.showMessageDialog(builder.textEditor,"The searched item was not found!", "Text Editor", JOptionPane.INFORMATION_MESSAGE);
        }

        while (matcher.find()) {
            int currentIndex = matcher.start();
            matchesArray.add(currentIndex);
        }
    }

    private boolean isRegex(String regex) {
        boolean isRegex;

        try {
            Pattern.compile(regex);
            isRegex = true;
        } catch (PatternSyntaxException ex) {
            isRegex = false;
        }

        return isRegex;
    }

    public ArrayList<Integer> getMatchesArray(){

        return this.matchesArray;
    }


}
