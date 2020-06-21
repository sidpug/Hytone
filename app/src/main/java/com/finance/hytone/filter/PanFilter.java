package com.finance.hytone.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class PanFilter implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end,
        Spanned dest, int dstart, int dend) {

            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (i < 5 || i == end - 1) {
                    if (Character.isLetter(c))
                        builder.append(Character.toUpperCase(c));
                } else {
                    if (Character.isDigit(c))
                        builder.append(c);
                }
            }
                // If all characters are valid, return null, otherwise only return the filtered characters
                boolean allCharactersValid = (builder.length() == 10);
                return allCharactersValid ? builder.toString(): null;

        }
}

