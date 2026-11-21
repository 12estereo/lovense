package com.miaivideo.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int PICK_IMAGE = 41;
    private String mode = "Texto a video";
    private Uri selectedImage;
    private EditText prompt;
    private TextView imageStatus;
    private Button textMode, imageMode, chooseImage, generate;

    private int bg = Color.rgb(12, 17, 32);
    private int panel = Color.rgb(25, 34, 56);
    private int accent = Color.rgb(142, 91, 255);
    private int white = Color.rgb(245, 247, 255);
    private int muted = Color.rgb(174, 185, 207);

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        buildScreen();
    }

    private int dp(float v) {
        return (int)(v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private LinearLayout.LayoutParams lp(int w, int h) {
        return new LinearLayout.LayoutParams(w < 0 ? w : dp(w), h < 0 ? h : dp(h));
    }

    private void buildScreen() {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(bg);
        LinearLayout page = column();
        page.setPadding(dp(20), dp(24), dp(20), dp(28));
        scroll.addView(page);

        TextView brand = text("MI AI VIDEO", 25, white, true);
        page.addView(brand);
        TextView subtitle = text("Tu pequeño estudio de creación con inteligencia artificial", 13, muted, false);
        LinearLayout.LayoutParams subLp = lp(-1, -2);
        subLp.topMargin = dp(5);
        page.addView(subtitle, subLp);

        TextView tag = text("VERSIÓN 0.1 · PROTOTIPO ANDROID", 11, Color.rgb(178, 153, 255), true);
        LinearLayout.LayoutParams tagLp = lp(-1, -2);
        tagLp.topMargin = dp(18);
        page.addView(tag, tagLp);

        TextView heading = text("¿Qué video imaginamos hoy?", 20, white, true);
        LinearLayout.LayoutParams hLp = lp(-1, -2);
        hLp.topMargin = dp(25);
        page.addView(heading, hLp);

        TextView modeLabel = text("TIPO DE CREACIÓN", 11, muted, true);
        LinearLayout.LayoutParams mlp = lp(-1, -2);
        mlp.topMargin = dp(17);
        page.addView(modeLabel, mlp);

        LinearLayout modes = new LinearLayout(this);
        modes.setOrientation(LinearLayout.HORIZONTAL);
        textMode = new Button(this);
        imageMode = new Button(this);
        textMode.setText("Texto a video");
        imageMode.setText("Imagen a video");
        modes.addView(textMode, new LinearLayout.LayoutParams(0, dp(48), 1));
        LinearLayout.LayoutParams imLp = new LinearLayout.LayoutParams(0, dp(48), 1);
        imLp.leftMargin = dp(8);
        modes.addView(imageMode, imLp);
        LinearLayout.LayoutParams modesLp = lp(-1, -2);
        modesLp.topMargin = dp(8);
        page.addView(modes, modesLp);

        textMode.setOnClickListener(v -> setMode("Texto a video"));
        imageMode.setOnClickListener(v -> setMode("Imagen a video"));
        setMode(mode);

        TextView promptLabel = text("DESCRIBE TU VIDEO", 11, muted, true);
        LinearLayout.LayoutParams plp = lp(-1, -2);
        plp.topMargin = dp(20);
        page.addView(promptLabel, plp);

        prompt = new EditText(this);
        prompt.setTextColor(white);
        prompt.setHintTextColor(Color.rgb(130, 144, 169));
        prompt.setHint("Ejemplo: un carro deportivo recorriendo una ciudad futurista de noche, luces de neón, estilo cinematográfico...");
        prompt.setGravity(Gravity.TOP | Gravity.START);
        prompt.setTextSize(14);
        prompt.setMinLines(5);
        prompt.setPadding(dp(13), dp(13), dp(13), dp(13));
        prompt.setBackgroundTintList(android.content.res.ColorStateList.valueOf(accent));
        prompt.setBackgroundColor(panel);
        LinearLayout.LayoutParams promptLp = lp(-1, 135);
        promptLp.topMargin = dp(8);
        page.addView(prompt, promptLp);

        chooseImage = new Button(this);
        chooseImage.setText("ELEGIR IMAGEN");
        LinearLayout.LayoutParams chooseLp = lp(-1, 48);
        chooseLp.topMargin = dp(10);
        page.addView(chooseImage, chooseLp);
        chooseImage.setOnClickListener(v -> pickImage());

        imageStatus = text("No has seleccionado una imagen.", 12, muted, false);
        LinearLayout.LayoutParams isLp = lp(-1, -2);
        isLp.topMargin = dp(5);
        page.addView(imageStatus, isLp);

        TextView settings = text("CONFIGURACIÓN", 11, muted, true);
        LinearLayout.LayoutParams setLp = lp(-1, -2);
        setLp.topMargin = dp(22);
        page.addView(settings, setLp);

        LinearLayout config = column();
        config.setPadding(dp(14), dp(13), dp(14), dp(13));
        config.setBackgroundColor(panel);
        TextView resolution = text("Resolución: 720p HD", 14, white, true);
        TextView format = text("Formato: vertical 9:16", 14, white, true);
        TextView duration = text("Duración inicial: 5 segundos", 14, white, true);
        config.addView(resolution);
        config.addView(format, lp(-1, -2));
        config.addView(duration, lp(-1, -2));
        LinearLayout.LayoutParams configLp = lp(-1, -2);
        configLp.topMargin = dp(8);
        page.addView(config, configLp);

        generate = new Button(this);
        generate.setText("PREPARAR GENERACIÓN");
        LinearLayout.LayoutParams genLp = lp(-1, 54);
        genLp.topMargin = dp(22);
        page.addView(generate, genLp);
        generate.setOnClickListener(v -> showGenerationNotice());

        TextView footer = text("Los motores gratuitos pueden tener colas, límites o disponibilidad variable. Esta versión es la base de la aplicación; la conexión de generación se añadirá y probará en la siguiente etapa.", 11, muted, false);
        LinearLayout.LayoutParams footLp = lp(-1, -2);
        footLp.topMargin = dp(18);
        page.addView(footer, footLp);

        setContentView(scroll);
        updateModeControls();
    }

    private void setMode(String newMode) {
        mode = newMode;
        if (textMode != null) updateModeControls();
    }

    private void updateModeControls() {
        boolean image = mode.equals("Imagen a video");
        textMode.setAlpha(image ? 0.65f : 1f);
        imageMode.setAlpha(image ? 1f : 0.65f);
        chooseImage.setVisibility(image ? View.VISIBLE : View.GONE);
        imageStatus.setVisibility(image ? View.VISIBLE : View.GONE);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            imageStatus.setText("Imagen seleccionada: " + (selectedImage != null ? selectedImage.getLastPathSegment() : ""));
        }
    }

    private void showGenerationNotice() {
        String description = prompt.getText().toString().trim();
        if (description.isEmpty()) {
            Toast.makeText(this, "Escribe primero una descripción para tu video.", Toast.LENGTH_LONG).show();
            return;
        }
        if (mode.equals("Imagen a video") && selectedImage == null) {
            Toast.makeText(this, "Selecciona una imagen primero.", Toast.LENGTH_LONG).show();
            return;
        }
        new AlertDialog.Builder(this)
            .setTitle("Siguiente etapa: conectar el motor")
            .setMessage("Tu proyecto ya tiene la interfaz y la selección de contenido. Todavía no genera videos: falta conectar y probar un proveedor de IA con acceso gratuito. No te mostraremos una falsa barra de progreso ni diremos que creó un video cuando aún no está conectado.")
            .setPositiveButton("Entendido", null)
            .show();
    }
}
