package com.ascplay.app;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Preenche o card de canal (item_channel.xml): selo de qualidade, AGORA com barra de progresso
 * e A SEGUIR. Sem guia (lista M3U ou canal sem EPG), mostra só nome e categoria.
 */
final class ChannelRow {

    private static final Pattern QUALITY = Pattern.compile("\\b(4K|FHD|HD|SD)\\b");
    private static final Pattern H24 = Pattern.compile("\\b24\\s?H(S|ORAS)?\\b");
    private static final int BLUE_LIGHT = 0xFF5AAAFF;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int SOFT = 0xFFBED2F5;

    private ChannelRow() {
    }

    /** HD, FHD, SD ou 4K, se o nome do canal disser; senão null. */
    static String quality(String name) {
        if (name == null) {
            return null;
        }
        java.util.regex.Matcher m = QUALITY.matcher(name.toUpperCase(Locale.ROOT));
        return m.find() ? m.group(1) : null;
    }

    static boolean is24h(String name) {
        return name != null && H24.matcher(name.toUpperCase(Locale.ROOT)).find();
    }

    /** Parte fixa do card: selo de qualidade; guia escondido até chegar. */
    static void bind(View card, String name) {
        TextView quality = card.findViewById(R.id.tv_quality);
        String q = quality(name);
        if (quality != null) {
            quality.setVisibility(q == null ? View.GONE : View.VISIBLE);
            if (q != null) {
                quality.setText(q);
            }
        }
        reset(card);
    }

    static void reset(View card) {
        setVisible(card, R.id.tv_agora, false);
        setVisible(card, R.id.tv_now, false);
        setVisible(card, R.id.pb_progress, false);
        setVisible(card, R.id.tv_next, false);
        View bar = card.findViewById(R.id.pb_progress);
        if (bar != null) {
            bar.setAlpha(1f);
        }
    }

    /** epg pode ser null: então mostra só a categoria (ou "24h" nos canais 24 horas). */
    static void showEpg(View card, XtreamApi.Epg epg, String name, String category) {
        TextView agora = card.findViewById(R.id.tv_agora);
        TextView now = card.findViewById(R.id.tv_now);
        ProgressBar bar = card.findViewById(R.id.pb_progress);
        TextView next = card.findViewById(R.id.tv_next);
        if (now == null || bar == null) {
            return;
        }
        if (epg != null && epg.title != null && !epg.title.isEmpty()) {
            agora.setVisibility(View.VISIBLE);
            now.setVisibility(View.VISIBLE);
            now.setTextColor(WHITE);
            now.setText(epg.title);
            long span = epg.end - epg.start;
            if (span > 0) {
                long done = System.currentTimeMillis() - epg.start;
                bar.setProgress((int) Math.max(0, Math.min(1000, done * 1000L / span)));
                bar.setAlpha(1f);
                bar.setVisibility(View.VISIBLE);
            } else {
                bar.setVisibility(View.GONE);
            }
            if (epg.nextTitle != null && !epg.nextTitle.isEmpty() && epg.nextStart > 0) {
                String hour = new SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(new Date(epg.nextStart));
                SpannableStringBuilder sb = new SpannableStringBuilder("A SEGUIR");
                sb.setSpan(new ForegroundColorSpan(BLUE_LIGHT), 0, sb.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new StyleSpan(Typeface.BOLD), 0, sb.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append("  ").append(hour).append("  ").append(epg.nextTitle);
                next.setText(sb);
                next.setVisibility(View.VISIBLE);
            } else {
                next.setVisibility(View.GONE);
            }
            return;
        }
        agora.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        if (is24h(name)) {
            now.setVisibility(View.VISIBLE);
            now.setTextColor(SOFT);
            now.setText("24h");
            bar.setProgress(1000);
            bar.setAlpha(0.35f);
            bar.setVisibility(View.VISIBLE);
            return;
        }
        bar.setVisibility(View.GONE);
        if (category != null && !category.isEmpty()) {
            now.setVisibility(View.VISIBLE);
            now.setTextColor(SOFT);
            now.setText(category);
        } else {
            now.setVisibility(View.GONE);
        }
    }

    private static void setVisible(View card, int id, boolean visible) {
        View v = card.findViewById(id);
        if (v != null) {
            v.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
}
