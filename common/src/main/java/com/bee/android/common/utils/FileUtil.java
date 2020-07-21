package com.bee.android.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bee.android.common.app.CommonApplication;
import com.bee.android.common.manager.CommonFileManager;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * 文件操作类
 */
public class FileUtil {
    public static final String TAG = "FileUtil";
    public static final String SEPARATOR = File.separator; // 路径分隔符
    private FileOutputStream fileOutputStream;

    private FileUtil() {
    }

    public static File newFile(@NonNull String path) {
        return new File(path);
    }

    public static File newFile(@NonNull File file, @NonNull String childName) {
        return new File(file, childName);
    }

    public static boolean fileExits(@NonNull String path) {
        return fileExits(newFile(path));
    }

    public static boolean fileExits(@NonNull File file) {
        return file.exists();
    }

    public static boolean mkdirs(@NonNull String path) {
        return mkdirs(newFile(path));
    }

    public static boolean mkdirs(@NonNull File file) {
        return file.exists() || file.mkdirs();
    }

    /**
     * 删除文件（非文件夹）
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(@NonNull String path) {
        return deleteFile(newFile(path));
    }

    /**
     * 同上
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(@NonNull File file) {
        return !file.exists() || (!file.isDirectory() && file.delete());
    }

    /**
     * 删除所有文件
     *
     * @param path
     * @return
     */
    public static boolean deleteAllFile(@NonNull String path) {
        return deleteAllFile(newFile(path), true);
    }

    /**
     * 同上
     *
     * @param file
     * @return
     */
    public static boolean deleteAllFile(@NonNull File file) {
        return deleteAllFile(file, true);
    }

    /**
     * 删除所有文件
     *
     * @param path
     * @param isDeleteFolder 是否删除目录
     * @return
     */
    public static boolean deleteAllFile(@NonNull String path, boolean isDeleteFolder) {
        return deleteAllFile(newFile(path), isDeleteFolder);
    }

    /**
     * 同上
     *
     * @param file
     * @param isDeleteFolder
     * @return
     */
    public static boolean deleteAllFile(@NonNull File file, boolean isDeleteFolder) {
        boolean isSuccess = true;
        if (!fileExits(file)) {
            return true;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    isSuccess &= deleteAllFile(f, isDeleteFolder);
                }
            }
            if (isDeleteFolder) {
                isSuccess &= file.delete();
            }
        } else {
            isSuccess &= file.delete();
        }
        return isSuccess;
    }

    /**
     * 获取外部私有存储文件路径
     * 果外部存储获取失败，则返回对应内部储存路径
     *
     * @param context
     * @param dir
     * @return
     */
    public static String getExternalFilePath(Context context, String dir) {
        String directoryPath = "";
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File baseDirFile = context.getExternalFilesDir(TextUtils.isEmpty(dir) ? "" : dir);
            if (baseDirFile != null) {
                directoryPath = baseDirFile.getAbsolutePath();
            }
        }
        if (TextUtils.isEmpty(directoryPath)) {
            directoryPath = getInnerFilePath(context, dir);
        }
        mkdirs(directoryPath);
        return directoryPath;
    }

    /**
     * 获取内部私有存储文件路径
     *
     * @param context
     * @param dir
     * @return
     */
    public static String getInnerFilePath(Context context, String dir) {
        File file = TextUtils.isEmpty(dir) ?
                context.getFilesDir() : newFile(context.getFilesDir(), dir);
        mkdirs(file);
        return file.getAbsolutePath();
    }

    /**
     * 获取某目录下的所有文件
     *
     * @param path
     * @return
     */
    public static List<String> getFiles(String path) {
        List<String> paths = new ArrayList<>();
        File[] allFiles = new File(path).listFiles();
        if (allFiles != null && allFiles.length > 0) {
            for (File file : allFiles) {
                paths.add(file.getAbsolutePath());
            }
        }
        return paths;
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file
     * @return
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (File aFileList : fileList) {
                    if (aFileList.isDirectory()) {
                        size = size + getFolderSize(aFileList);
                    } else {
                        size = size + aFileList.length();
                    }
                }
            } else {
                size += file.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取剩余存储，有外部取外部，没有去内部
     *
     * @return
     */
    public static long getMemorySize() {
        if (isSdcardExits()) {
            return getAvailableExternalMemorySize();
        } else {
            return getAvailableInternalMemorySize();
        }
    }

    /**
     * 获取sdcard剩余存储空间
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static long getAvailableExternalMemorySize() {
        if (isSdcardExits()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    @SuppressLint("NewApi")
    private static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     */
    @SuppressLint("NewApi")
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    /**
     * 读取某文件下的数据
     *
     * @param filePath
     * @return
     */
    public static byte[] readFile(String filePath) throws IOException {
        byte[] buffer;
        FileInputStream fin = null;

        try {
            fin = new FileInputStream(filePath);
            int length = fin.available();
            buffer = new byte[length];
            fin.read(buffer);
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
        return null;
    }

    /**
     * sdcard 是否存在
     *
     * @return
     */
    private static boolean isSdcardExits() {
        return Environment.getExternalStorageState().equals(MEDIA_MOUNTED);
    }

    /**
     * json 存文件
     *
     * @param file
     * @param o
     */
    public static void saveObjectByJson(File file, Object o) {
        if (file == null)
            return;
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File writeFile = newFile(file, "res.json");
        if (!writeFile.exists()) {
            try {
                writeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String json = GsonUtil.getInstance().getGson().toJson(o);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(writeFile);
            fileOutputStream.write(json.getBytes());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 取出文件json
     *
     * @param file
     * @param c
     * @return
     */
    public static Object getObjectByJson(File file, Class c) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            StringBuilder stringBuilder = new StringBuilder();
            int l = 0;
            byte[] bs = new byte[1024];
            while ((l = fis.read(bs)) != -1) {
                stringBuilder.append(new String(bs, 0, l));
            }

            return GsonUtil.getInstance().getGson().fromJson(stringBuilder.toString(), c);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 复制res/raw中的文件到指定目录
     *
     * @param context
     * @param id
     * @param fileName
     * @param storagePath
     */
    public static void copyFileFromRaw(Context context, int id, String fileName, String storagePath) {
        InputStream inputStream = context.getResources().openRawResource(id);
        File file = new File(storagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        readInputStream(storagePath + SEPARATOR + fileName, inputStream);
    }

    /**
     * 读取输入流中的数据写入输出流
     *
     * @param storagePath
     * @param inputStream
     */
    public static void readInputStream(String storagePath, InputStream inputStream) {
        File file = new File(storagePath);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[inputStream.available()];
                int length = 0;
                while ((length = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static String getGlideCacheAndCourseSize(Context context) {
        try {

            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)) +
                    getFolderSize(FileUtil.newFile(CommonFileManager.getExternalClearAbleFilePath(CommonApplication.app))) +
                    getFolderSize(FileUtil.newFile(CommonFileManager.getInnerClearAbleFilePath(CommonApplication.app))));

        } catch (Exception e) {
            Log.e(TAG, "getGlideCacheAndCourseSize error: " + e.getMessage());
        }
        return "";
    }

    public static String getFileNameWithoutSuffix(File file) {
        if (file == null || !file.exists()) {
            return "";
        }
        String fileName = file.getName();
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        if (fileName.lastIndexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }
}
