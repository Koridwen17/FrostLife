package me.koridwen.frostlife.util;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class StringManipulator {
    public StringManipulator() {
    }

    public static boolean isNumeric(String string) {
        if (string != null && !string.equals("")) {
            try {
                int intValue = Integer.parseInt(string);
                return true;
            } catch (NumberFormatException var3) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static List<String> getIndividualBlocks(String text) {
        List<String> words = new ArrayList();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();

        while(-1 != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != -1 && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                words.add(text.substring(firstIndex, lastIndex));
            }
        }

        return words;
    }

    public static String removeLastChar(String s) {
        return s != null && s.length() != 0 ? s.substring(0, s.length() - 1) : null;
    }
}
