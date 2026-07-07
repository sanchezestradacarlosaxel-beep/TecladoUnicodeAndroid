package com.luisunicode.keyboard;

import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class UnicodeKeyboardService extends InputMethodService {

    private static class UnicodeKey {
        final String label;
        final String code;

        UnicodeKey(String label, String code) {
            this.label = label;
            this.code = code;
        }
    }

    /*
     * TECLAS PERSONALIZADAS.
     * Incluye números circulados 1-50, letras circuladas A-Z y el signo menos.
     */
    private final UnicodeKey[] unicodeKeys = new UnicodeKey[] {
            new UnicodeKey("①", "U+2460"),
            new UnicodeKey("②", "U+2461"),
            new UnicodeKey("③", "U+2462"),
            new UnicodeKey("④", "U+2463"),
            new UnicodeKey("⑤", "U+2464"),
            new UnicodeKey("⑥", "U+2465"),
            new UnicodeKey("⑦", "U+2466"),
            new UnicodeKey("⑧", "U+2467"),
            new UnicodeKey("⑨", "U+2468"),
            new UnicodeKey("⑩", "U+2469"),
            new UnicodeKey("⑪", "U+246A"),
            new UnicodeKey("⑫", "U+246B"),
            new UnicodeKey("⑬", "U+246C"),
            new UnicodeKey("⑭", "U+246D"),
            new UnicodeKey("⑮", "U+246E"),
            new UnicodeKey("⑯", "U+246F"),
            new UnicodeKey("⑰", "U+2470"),
            new UnicodeKey("⑱", "U+2471"),
            new UnicodeKey("⑲", "U+2472"),
            new UnicodeKey("⑳", "U+2473"),
            new UnicodeKey("㉑", "U+3251"),
            new UnicodeKey("㉒", "U+3252"),
            new UnicodeKey("㉓", "U+3253"),
            new UnicodeKey("㉔", "U+3254"),
            new UnicodeKey("㉕", "U+3255"),
            new UnicodeKey("㉖", "U+3256"),
            new UnicodeKey("㉗", "U+3257"),
            new UnicodeKey("㉘", "U+3258"),
            new UnicodeKey("㉙", "U+3259"),
            new UnicodeKey("㉚", "U+325A"),
            new UnicodeKey("㉛", "U+325B"),
            new UnicodeKey("㉜", "U+325C"),
            new UnicodeKey("㉝", "U+325D"),
            new UnicodeKey("㉞", "U+325E"),
            new UnicodeKey("㉟", "U+325F"),
            new UnicodeKey("㊱", "U+32B1"),
            new UnicodeKey("㊲", "U+32B2"),
            new UnicodeKey("㊳", "U+32B3"),
            new UnicodeKey("㊴", "U+32B4"),
            new UnicodeKey("㊵", "U+32B5"),
            new UnicodeKey("㊶", "U+32B6"),
            new UnicodeKey("㊷", "U+32B7"),
            new UnicodeKey("㊸", "U+32B8"),
            new UnicodeKey("㊹", "U+32B9"),
            new UnicodeKey("㊺", "U+32BA"),
            new UnicodeKey("㊻", "U+32BB"),
            new UnicodeKey("㊼", "U+32BC"),
            new UnicodeKey("㊽", "U+32BD"),
            new UnicodeKey("㊾", "U+32BE"),
            new UnicodeKey("㊿", "U+32BF"),
            new UnicodeKey("Ⓐ", "U+24B6"),
            new UnicodeKey("Ⓑ", "U+24B7"),
            new UnicodeKey("Ⓒ", "U+24B8"),
            new UnicodeKey("Ⓓ", "U+24B9"),
            new UnicodeKey("Ⓔ", "U+24BA"),
            new UnicodeKey("Ⓕ", "U+24BB"),
            new UnicodeKey("Ⓖ", "U+24BC"),
            new UnicodeKey("Ⓗ", "U+24BD"),
            new UnicodeKey("Ⓘ", "U+24BE"),
            new UnicodeKey("Ⓙ", "U+24BF"),
            new UnicodeKey("Ⓚ", "U+24C0"),
            new UnicodeKey("Ⓛ", "U+24C1"),
            new UnicodeKey("Ⓜ", "U+24C2"),
            new UnicodeKey("Ⓝ", "U+24C3"),
            new UnicodeKey("Ⓞ", "U+24C4"),
            new UnicodeKey("Ⓟ", "U+24C5"),
            new UnicodeKey("Ⓠ", "U+24C6"),
            new UnicodeKey("Ⓡ", "U+24C7"),
            new UnicodeKey("Ⓢ", "U+24C8"),
            new UnicodeKey("Ⓣ", "U+24C9"),
            new UnicodeKey("Ⓤ", "U+24CA"),
            new UnicodeKey("Ⓥ", "U+24CB"),
            new UnicodeKey("Ⓦ", "U+24CC"),
            new UnicodeKey("Ⓧ", "U+24CD"),
            new UnicodeKey("Ⓨ", "U+24CE"),
            new UnicodeKey("Ⓩ", "U+24CF"),
            new UnicodeKey("-", "U+002D")
    };

    @Override
    public View onCreateInputView() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.rgb(242, 242, 242));
        mainLayout.setPadding(dp(6), dp(6), dp(6), dp(6));

        TextView title = new TextView(this);
        title.setText("Teclado Unicode");
        title.setTextSize(14);
        title.setTextColor(Color.DKGRAY);
        title.setGravity(Gravity.CENTER);
        mainLayout.addView(title, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(26)
        ));

        ScrollView scrollView = new ScrollView(this);
        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(5);

        for (UnicodeKey key : unicodeKeys) {
            Button button = createKeyButton(key.label);
            button.setOnClickListener(v -> commitUnicode(key.code));
            grid.addView(button);
        }

        scrollView.addView(grid);
        mainLayout.addView(scrollView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
        ));

        LinearLayout controlRow = new LinearLayout(this);
        controlRow.setOrientation(LinearLayout.HORIZONTAL);
        controlRow.setGravity(Gravity.CENTER);

        Button backspaceButton = createControlButton("⌫");
        backspaceButton.setOnClickListener(v -> deleteOneCharacter());
        backspaceButton.setOnLongClickListener(v -> {
            deleteSeveralCharacters();
            return true;
        });

        Button spaceButton = createControlButton("Espacio");
        spaceButton.setOnClickListener(v -> commitText(" "));

        Button enterButton = createControlButton("↵");
enterButton.setOnClickListener(v -> commitText(System.lineSeparator()));

        controlRow.addView(backspaceButton);
        controlRow.addView(spaceButton);
        controlRow.addView(enterButton);

        mainLayout.addView(controlRow, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(58)
        ));

        return mainLayout;
    }

    private Button createKeyButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(22);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);
        button.setPadding(dp(2), dp(2), dp(2), dp(2));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = dp(68);
        params.height = dp(58);
        params.setMargins(dp(3), dp(3), dp(3), dp(3));
        button.setLayoutParams(params);
        return button;
    }

    private Button createControlButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(16);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);
        button.setPadding(dp(2), dp(2), dp(2), dp(2));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
        );
        params.setMargins(dp(3), dp(3), dp(3), dp(3));
        button.setLayoutParams(params);
        return button;
    }

    private void commitUnicode(String code) {
        try {
            String unicodeText = unicodeCodeToString(code);
            commitText(unicodeText);
        } catch (Exception ignored) {
            // Si un código está mal escrito, no inserta nada.
        }
    }

    private void commitText(String text) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            inputConnection.commitText(text, 1);
        }
    }

    private void deleteOneCharacter() {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            inputConnection.deleteSurroundingText(1, 0);
        }
    }

    private void deleteSeveralCharacters() {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            inputConnection.deleteSurroundingText(10, 0);
        }
    }

    private String unicodeCodeToString(String code) {
        String cleanCode = code
                .replace("U+", "")
                .replace("u+", "")
                .trim();
        int codePoint = Integer.parseInt(cleanCode, 16);
        return new String(Character.toChars(codePoint));
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }
}
