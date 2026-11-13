package com.ascplay.app;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Janela de pesquisa em vidro, com o teclado do próprio celular.
 * Mesma ideia do antigo teclado de TV: devolve o texto em onSearch (2 a 40 letras)
 * e avisa onCancel quando a pessoa sai sem buscar.
 */
public final class SearchKeyboard {

    public interface Listener {
        /** A pessoa tocou em Buscar. A janela já foi fechada. */
        void onSearch(String text);

        /** A pessoa saiu sem buscar. */
        void onCancel();
    }

    private static final int MAX_LEN = 40;
    private static final int MIN_LEN = 2;

    private final Activity activity;
    private final Listener listener;
    private Dialog dialog;
    private EditText input;
    private TextView message;
    private boolean searched = false;

    private SearchKeyboard(Activity activity, Listener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    /** Abre a janela e o teclado. */
    public static SearchKeyboard show(Activity activity, String title, Listener listener) {
        SearchKeyboard k = new SearchKeyboard(activity, listener);
        k.build(title);
        if (!activity.isFinishing() && !activity.isDestroyed()) {
            k.dialog.show();
        }
        return k;
    }

    private void build(String title) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(d -> {
            if (!searched && listener != null) {
                listener.onCancel();
            }
        });

        FrameLayout outer = new FrameLayout(activity);
        outer.setPadding(dp(16), dp(16), dp(16), dp(16));

        LinearLayout box = new LinearLayout(activity);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(20), dp(20), dp(20), dp(20));
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0xF2203A66, 0xF20F1E38});
        bg.setCornerRadius(dp(26));
        bg.setStroke(dp(1), 0x9996B9EB);
        box.setBackground(bg);
        outer.addView(box, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView tvTitle = new TextView(activity);
        tvTitle.setText(title);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(20);
        tvTitle.setTypeface(tvTitle.getTypeface(), android.graphics.Typeface.BOLD);
        box.addView(tvTitle, wrap());

        input = new EditText(activity);
        input.setHint("Digite o nome");
        input.setHintTextColor(0xFF8FA6CC);
        input.setTextColor(Color.WHITE);
        input.setTextSize(17);
        input.setSingleLine(true);
        input.setMinHeight(dp(52));
        input.setPadding(dp(18), 0, dp(18), 0);
        input.setBackgroundResource(R.drawable.bg_field);
        input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LEN)});
        input.setOnEditorActionListener((v, actionId, event) -> {
            submit();
            return true;
        });
        LinearLayout.LayoutParams lpInput = wrap();
        lpInput.topMargin = dp(14);
        box.addView(input, lpInput);

        message = new TextView(activity);
        message.setTextColor(0xFFFF6B78);
        message.setTextSize(14);
        message.setVisibility(android.view.View.GONE);
        LinearLayout.LayoutParams lpMsg = wrap();
        lpMsg.topMargin = dp(8);
        box.addView(message, lpMsg);

        LinearLayout row = new LinearLayout(activity);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.END);
        LinearLayout.LayoutParams lpRow = wrap();
        lpRow.topMargin = dp(16);
        box.addView(row, lpRow);

        Button cancel = button("Cancelar", R.drawable.bg_glass_pill);
        cancel.setOnClickListener(v -> dialog.cancel());
        row.addView(cancel, buttonParams(0));

        Button search = button("Buscar", R.drawable.bg_button);
        search.setOnClickListener(v -> submit());
        row.addView(search, buttonParams(dp(10)));

        dialog.setContentView(outer);
        Window w = dialog.getWindow();
        if (w != null) {
            w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            w.setGravity(Gravity.TOP);
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        input.requestFocus();
    }

    private void submit() {
        String text = input.getText().toString().trim();
        if (text.length() < MIN_LEN) {
            message.setText("Digite pelo menos " + MIN_LEN + " letras.");
            message.setVisibility(android.view.View.VISIBLE);
            return;
        }
        searched = true;
        dialog.dismiss();
        if (listener != null) {
            listener.onSearch(text);
        }
    }

    private Button button(String label, int background) {
        Button b = new Button(activity);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextColor(Color.WHITE);
        b.setTextSize(16);
        b.setBackgroundResource(background);
        b.setMinWidth(dp(110));
        b.setMinimumHeight(dp(48));
        b.setPadding(dp(20), 0, dp(20), 0);
        return b;
    }

    private LinearLayout.LayoutParams buttonParams(int startMargin) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(48));
        lp.leftMargin = startMargin;
        return lp;
    }

    private LinearLayout.LayoutParams wrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private int dp(int value) {
        return Math.round(value * activity.getResources().getDisplayMetrics().density);
    }
}
