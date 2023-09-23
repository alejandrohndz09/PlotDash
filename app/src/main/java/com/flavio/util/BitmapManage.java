package com.flavio.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flavio.plotdash.MainActivity;
import com.flavio.plotdash.R;

public abstract class  BitmapManage {
    public interface ImageConversionCallback {
        void onImageConverted(Drawable drawable);
    }

    public static void convertGlideImageToDrawableOrBitmap(Context context, String url, ImageConversionCallback callback) {
        Glide.with(context)
                .asDrawable()
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                        // Conversion de la imagen a drawable a bitmap
                        Bitmap bitmap = drawableToBitmap(drawable);
                        //Expansion de bitmap
                       // Bitmap expandBitMap= expandBitmap(bitmap, 400, 400);
                        // Conversion de bitmap a bitmap arreglado
                        Bitmap roundedBitmap= getRoundedBitmapWithDoubleBorder(bitmap, 50, Color.parseColor("#1c3245"));
                        // Llama al método de devolución de llamada con el Drawable o el Bitmap
                        Bitmap aj = addDrawableToBitmap(roundedBitmap, context.getResources().getDrawable(R.drawable.baseline_settings_24));
                        callback.onImageConverted(new BitmapDrawable(aj));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Método opcional que se llama cuando la carga se ha cancelado o borrado
                    }
                });
    }
    public static Bitmap expandBitmap(Bitmap originalBitmap, int expandedWidth, int expandedHeight) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        // Calcular el desplazamiento del bitmap original dentro del nuevo bitmap
        int offsetX = (expandedWidth - originalWidth) / 2;
        int offsetY = (expandedHeight - originalHeight) / 2;

        // Crear un nuevo bitmap con el fondo ampliado
        Bitmap expandedBitmap = Bitmap.createBitmap(expandedWidth, expandedHeight, Bitmap.Config.ARGB_8888);

        // Crear un canvas para dibujar en el nuevo bitmap
        Canvas canvas = new Canvas(expandedBitmap);

        // Dibujar el fondo ampliado
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#00000000"));
        canvas.drawRect(0, 0, expandedWidth, expandedHeight, backgroundPaint);

        // Dibujar el bitmap original en el centro del nuevo bitmap
        canvas.drawBitmap(originalBitmap, offsetX, offsetY, null);

        return expandedBitmap;
    }
    public static Bitmap addDrawableToBitmap(Bitmap bitmap, Drawable drawable) {
        // Crear un nuevo bitmap con las mismas dimensiones que el bitmap original
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // Dibujar el bitmap original en el nuevo bitmap
        canvas.drawBitmap(bitmap, 0, 0, null);

        // Calcular la posición para dibujar el drawable (tuerca) en la esquina inferior derecha
        int drawableSize = (int) (Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2.35);
        int margin = drawableSize / 2;
        int x = bitmap.getWidth() - drawableSize - margin+500;
        int y = bitmap.getHeight() - drawableSize - margin+370;

        // Establecer los límites del drawable
        drawable.setBounds(x, y, x + drawableSize, y + drawableSize);

        // Dibujar el drawable en el bitmap
        drawable.draw(canvas);

        return output;
    }


    public static Bitmap getRoundedBitmapWithDoubleBorder(Bitmap bitmap, int borderWidth, int borderColor) {
        // Dimensiones del bitmap original
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calcular el radio del círculo para el recorte
        int radius = Math.min(width, height) / 2;

        // Crear un nuevo bitmap para la imagen recortada y el borde
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // Configurar el Paint para el recorte de círculo
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        // Dibujar el círculo recortado
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        // Configurar el Paint para el borde
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth * 2);
        borderPaint.setColor(borderColor);
        borderPaint.setAntiAlias(true);

        // Dibujar el borde exterior
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth, borderPaint);

        // Dibujar el borde interior
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth * 2, borderPaint);

        return output;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

}
