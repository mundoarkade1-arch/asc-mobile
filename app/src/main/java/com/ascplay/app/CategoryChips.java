package com.ascplay.app;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Categorias em chips com rolagem horizontal (substitui a lista vertical do app antigo).
 * Aceita o mesmo adaptador e o mesmo clique de uma lista: posição do chip = posição da categoria.
 * O chip da categoria aberta fica selecionado (azul).
 */
public final class CategoryChips extends HorizontalScrollView {

    private final LinearLayout row;
    private BaseAdapter adapter;
    private AdapterView.OnItemClickListener clickListener;
    private int selected = 0;

    private final DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            rebuild();
        }

        @Override
        public void onInvalidated() {
            rebuild();
        }
    };

    public CategoryChips(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setClipToPadding(false);
        row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        addView(row, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    public void setAdapter(BaseAdapter newAdapter) {
        if (adapter != null) {
            adapter.unregisterDataSetObserver(observer);
        }
        adapter = newAdapter;
        if (adapter != null) {
            adapter.registerDataSetObserver(observer);
        }
        rebuild();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        clickListener = listener;
        rebuild();
    }

    /** Compatível com a lista antiga; o espaçamento vem do layout do chip. */
    public void setDividerHeight(int px) {
        // nada a fazer
    }

    /** Marca o chip da categoria aberta e o traz para a tela. */
    public void setSelectedIndex(int index) {
        selected = index;
        for (int i = 0; i < row.getChildCount(); i++) {
            row.getChildAt(i).setSelected(i == index);
        }
        scrollTo(index);
    }

    /** Só rola até o chip (sem mudar a seleção). */
    public void setSelection(int index) {
        scrollTo(index);
    }

    private void scrollTo(int index) {
        if (index < 0 || index >= row.getChildCount()) {
            return;
        }
        final View chip = row.getChildAt(index);
        post(() -> smoothScrollTo(Math.max(0, chip.getLeft() - dp(16)), 0));
    }

    private int dp(int v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    private void rebuild() {
        row.removeAllViews();
        if (adapter == null) {
            return;
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            final int position = i;
            final View chip = adapter.getView(i, null, row);
            chip.setSelected(i == selected);
            chip.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(null, v, position, position);
                }
            });
            row.addView(chip);
        }
    }
}
