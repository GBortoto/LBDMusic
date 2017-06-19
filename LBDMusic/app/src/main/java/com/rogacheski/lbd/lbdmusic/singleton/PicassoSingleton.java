package com.rogacheski.lbd.lbdmusic.singleton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

public class PicassoSingleton implements Callback {

    private static PicassoSingleton instance;
    private static WeakReference<Context> contextWeakRef;
    private static WeakReference<PicassoCallbacksInterface> listenerWeakRef;

    public static PicassoSingleton getInstance(WeakReference<Context> context,
                                               WeakReference<PicassoCallbacksInterface> listener) {
        contextWeakRef = context;
        listenerWeakRef = listener;
        return instance == null ? instance = new PicassoSingleton() : instance;
    }

    private PicassoSingleton() {
    }

    /**
     *
     * @param imageView
     * @param photoUrl
     */
    public void setPhotoAsync(final ImageView imageView, String photoUrl, Drawable defaultIcon) {
        Picasso
                .with(contextWeakRef.get())
                .load(photoUrl)
                .placeholder(defaultIcon)
                .fit()
                .centerCrop()
                .into(imageView, this);
    }

    /**
     *
     * @param imageView
     * @param profilePictUrl
     */
    public void setProfilePictureAsync(final ImageView imageView, String profilePictUrl, Drawable defaultIcon) {
        Picasso
                .with(contextWeakRef.get())
                .load(profilePictUrl)
                .transform(new CircleTransform())
                .placeholder(defaultIcon)
                .fit()
                .centerCrop()
                .into(imageView, this);
    }

    public void setPostPictureAsync(final ImageView imageView, String profilePictUrl, Drawable defaultIcon) {
        Picasso
                .with(contextWeakRef.get())
                .load(profilePictUrl)
                .placeholder(defaultIcon)
                .fit()
                .centerCrop()
                .into(imageView, this);
    }

    public void setZoomPictureAsync(final ImageView imageView, String profilePictUrl, Drawable defaultIcon) {
        Picasso
                .with(contextWeakRef.get())
                .load(profilePictUrl)
                .placeholder(defaultIcon)
                .into(imageView, this);

    }

    /**
     *
     * @param imageView
     * @param profilePictUrl
     */
    public void setProfilePictureAsyncLoading(final ImageView imageView, String profilePictUrl, final ProgressBar progress, final Drawable defaultIcon) {
        Picasso
                .with(contextWeakRef.get())
                .load(profilePictUrl)
                .transform(new CircleTransform())
                .fit()
                .centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        imageView.setImageDrawable(defaultIcon);
                        progress.setVisibility(View.GONE);

                    }
                });
    }

    @Override
    public void onSuccess() {
        if (listenerWeakRef != null &&
                listenerWeakRef.get() != null) {
            listenerWeakRef.get().onPicassoSuccessCallback();
        }
    }

    @Override
    public void onError() {
        if (listenerWeakRef != null &&
                listenerWeakRef.get() != null) {
            listenerWeakRef.get().onPicassoErrorCallback();
        }

    }

    /**
     * picasso callbacks interface
     */
    public interface PicassoCallbacksInterface {
        void onPicassoSuccessCallback();
        void onPicassoErrorCallback();
    }

    /**
     *
     */
    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}



