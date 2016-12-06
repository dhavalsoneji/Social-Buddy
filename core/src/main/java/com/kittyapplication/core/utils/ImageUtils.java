package com.kittyapplication.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kittyapplication.core.CoreApp;
import com.kittyapplication.core.R;
import com.kittyapplication.core.utils.constant.MimeType;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageUtils {

    public static final int GALLERY_REQUEST_CODE = 183;
    public static final int CAMERA_REQUEST_CODE = 212;

    private static final String CAMERA_FILE_NAME_PREFIX = "CAMERA_";

    public static <T> void displayImage(Context context, T image, ImageView imageView) {
        Glide.with(context)
                .load(image)
                .crossFade()
                .into(imageView);
    }

    /*public static void displayImage(Context context, String strImageUrl, ImageView imageView) {
        Glide.with(context)
                .load(strImageUrl)
                .crossFade()
                .into(imageView);
    }

    public static void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .crossFade()
                .into(imageView);
    }*/

    public static <T> void displayImage(final Context context, T strImageUrl, final ImageView imageView,
                                        final ProgressBar progressBar, int errorDrawable) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(strImageUrl)
                .error(errorDrawable)
                .listener(new RequestListener<T, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, T model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, T model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageView.setImageDrawable(resource);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .crossFade()
                .into(imageView);
    }

    private ImageUtils() {
    }

    public static String saveUriToFile(Uri uri) throws Exception {
        ParcelFileDescriptor parcelFileDescriptor = CoreApp.getInstance().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        InputStream inputStream = new FileInputStream(fileDescriptor);
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        File parentDir = StorageUtils.getAppExternalDataDirectoryFile();
        String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
        File resultFile = new File(parentDir, fileName);

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(resultFile));

        byte[] buf = new byte[2048];
        int length;

        try {
            while ((length = bis.read(buf)) > 0) {
                bos.write(buf, 0, length);
            }
        } catch (Exception e) {
            throw new IOException("Can\'t save Storage API bitmap to a file!", e);
        } finally {
            parcelFileDescriptor.close();
            bis.close();
            bos.close();
        }

        return resultFile.getAbsolutePath();
    }

    public static void startImagePicker(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(MimeType.IMAGE_MIME);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.dlg_choose_image_from)), GALLERY_REQUEST_CODE);
    }

    public static void startImagePicker(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(MimeType.IMAGE_MIME);
        fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(R.string.dlg_choose_image_from)), GALLERY_REQUEST_CODE);
    }

    public static void startCameraForResult(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) == null) {
            return;
        }

        File photoFile = getTemporaryCameraFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public static void startCameraForResult(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(CoreApp.getInstance().getPackageManager()) == null) {
            return;
        }

        File photoFile = getTemporaryCameraFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        fragment.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public static File getTemporaryCameraFile() {
        File storageDir = StorageUtils.getAppExternalDataDirectoryFile();
        File file = new File(storageDir, getTemporaryCameraFileName());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File getLastUsedCameraFile() {
        File dataDir = StorageUtils.getAppExternalDataDirectoryFile();
        File[] files = dataDir.listFiles();
        List<File> filteredFiles = new ArrayList<>();
        for (File file : files) {
            if (file.getName().startsWith(CAMERA_FILE_NAME_PREFIX)) {
                filteredFiles.add(file);
            }
        }

        Collections.sort(filteredFiles);
        if (!filteredFiles.isEmpty()) {
            return filteredFiles.get(filteredFiles.size() - 1);
        } else {
            return null;
        }
    }

    private static String getTemporaryCameraFileName() {
        return CAMERA_FILE_NAME_PREFIX + System.currentTimeMillis() + ".jpg";
    }

    public static int getHalfOfDeviceWidth(Context context) {
        return (getDeviceWidth(context) / 2);
    }

    public static int getOneFourthOfDeviceWidth(Context context) {
        return (getDeviceWidth(context) / 4);
    }

    public static int getOneFifthOfDeviceHeight(Context context) {
        return ((getHalfOfDeviceWidth(context) * 3) / 5);
    }

    public static int getOneFourthOfDeviceHeight(Context context) {
        return ((getHalfOfDeviceWidth(context) * 3) / 4);
    }

    public static int getOnHalfOfDeviceHeight(Context context) {
        return (getDeviceHeight(context) / 2);
    }

    public static int getDeviceWidth(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        try {
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(displaymetrics);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return displaymetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        try {
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(displaymetrics);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return displaymetrics.heightPixels;
    }
}
