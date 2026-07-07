package com.luisunicode.keyboard;

import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;

public class UnicodeKeyboardService extends InputMethodService {

    private LinearLayout keysContainer;
    private Button numbersTab;
    private Button lettersTab;

    private final String[][] numberRows = new String[][]{
            {"①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩"},
            {"⑪", "⑫", "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳"},
            {"㉑", "㉒", "㉓", "㉔", "㉕", "㉖", "㉗", "㉘", "㉙", "㉚"},
            {"㉛", "㉜", "㉝", "㉞", "㉟", "㊱", "㊲", "㊳", "㊴", "㊵"},
            {"㊶", "㊷", "㊸", "㊹", "㊺", "㊻", "㊼", "㊽", "㊾", "㊿"}
    };

    private final String[][] letterRows = new String[][]{
            {"Ⓠ", "Ⓦ", "Ⓔ", "Ⓡ", "Ⓣ", "Ⓨ", "Ⓤ", "Ⓘ", "Ⓞ", "Ⓟ"},
            {"Ⓐ", "Ⓢ", "Ⓓ", "Ⓕ", "Ⓖ", "Ⓗ", "Ⓙ", "Ⓚ", "Ⓛ"},
            {"Ⓩ", "Ⓧ", "Ⓒ", "Ⓥ", "Ⓑ", "Ⓝ", "Ⓜ"}
    };

    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }

    @Override
    public View onCreateInputView() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.rgb(235, 235, 235));
        mainLayout.setPadding(dp(5), dp(4), dp(5), dp(6));
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout tabRow = new LinearLayout(this);
        tabRow.setOrientation(LinearLayout.HORIZONTAL);
        tabRow.setGravity(Gravity.CENTER);

        numbersTab = createTabButton("Números");
        lettersTab = createTabButton("Letras");

        numbersTab.setOnClickListener(v -> showNumbers());
        lettersTab.setOnClickListener(v -> showLetters());

        tabRow.addView(numbersTab, weightedParams(1f, dp(36)));
        tabRow.addView(lettersTab, weightedParams(1f, dp(36)));
        mainLayout.addView(tabRow, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(40)
        ));

        keysContainer = new LinearLayout(this);
        keysContainer.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(keysContainer, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout controlRow = new LinearLayout(this);
        controlRow.setOrientation(LinearLayout.HORIZONTAL);
        controlRow.setGravity(Gravity.CENTER);

        Button minusButton = createControlButton("-");
        minusButton.setOnClickListener(v -> commitText("-"));

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

        Button hideButton = createControlButton("▼");
        hideButton.setOnClickListener(v -> requestHideSelf(0));

        controlRow.addView(minusButton, weightedParams(1.0f, dp(46)));
        controlRow.addView(backspaceButton, weightedParams(1.2f, dp(46)));
        controlRow.addView(spaceButton, weightedParams(3.6f, dp(46)));
        controlRow.addView(enterButton, weightedParams(1.2f, dp(46)));
        controlRow.addView(hideButton, weightedParams(1.0f, dp(46)));

        mainLayout.addView(controlRow, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(50)
        ));

        showNumbers();
        return mainLayout;
    }

    private void showNumbers() {
        setTabState(true);
        buildRows(numberRows, new float[]{0f, 0f, 0f, 0f, 0f});
    }

    private void showLetters() {
        setTabState(false);
        buildRows(letterRows, new float[]{0f, 0.5f, 1.5f});
    }

    private void setTabState(boolean numbersSelected) {
        if (numbersTab == null || lettersTab == null) {
            return;
        }
        numbersTab.setEnabled(!numbersSelected);
        lettersTab.setEnabled(numbersSelected);
    }

    private void buildRows(String[][] rows, float[] sideWeights) {
        keysContainer.removeAllViews();

        for (int i = 0; i < rows.length; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER);

            float sideWeight = sideWeights != null && i < sideWeights.length ? sideWeights[i] : 0f;
            if (sideWeight > 0f) {
                row.addView(createSpacer(), weightedParams(sideWeight, dp(42)));
            }

            for (String keyText : rows[i]) {
                Button keyButton = createKeyButton(keyText);
                keyButton.setOnClickListener(v -> commitText(((Button) v).getText().toString()));
                row.addView(keyButton, weightedParams(1f, dp(42)));
            }

            if (sideWeight > 0f) {
                row.addView(createSpacer(), weightedParams(sideWeight, dp(42)));
            }

            keysContainer.addView(row, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(46)
            ));
        }
    }

    private Button createKeyButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(19);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);
        button.setPadding(0, 0, 0, 0);
        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        return button;
    }

    private Button createControlButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);
        button.setPadding(0, 0, 0, 0);
        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        return button;
    }

    private Button createTabButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(14);
        button.setAllCaps(false);
        button.setGravity(Gravity.CENTER);
        button.setPadding(0, 0, 0, 0);
        button.setMinHeight(0);
        button.setMinimumHeight(0);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        return button;
    }

    private Space createSpacer() {
        Space space = new Space(this);
        space.setVisibility(View.INVISIBLE);
        return space;
    }

    private LinearLayout.LayoutParams weightedParams(float weight, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                height,
                weight
        );
        params.setMargins(dp(2), dp(2), dp(2), dp(2));
        return params;
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

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }
}
