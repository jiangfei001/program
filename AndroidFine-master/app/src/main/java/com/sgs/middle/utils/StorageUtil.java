package com.sgs.middle.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import com.zhangke.zlog.ZLog;


import com.sgs.AppContext;
import com.sgs.middle.model.SDCardInfo;
import com.sgs.middle.model.StorageSize;
import com.jf.fine.R;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Nick Song
 * @version 1.0
 * @Description StorageUtil
 * @date 2018/1/17
 */
public final class StorageUtil {

    private static final int FLAG_DEFAULT = 0;
    private static final int FLAG_SHORTER = 1 << 0;
    private static final int FLAG_CALCULATE_ROUNDED = 1 << 1;

    private StorageUtil() {
        // Constructor
    }

    private static class MeasureUnit {
        private static final String RES_PREFIX = "measure_";
        private static final MeasureUnit BYTE = new MeasureUnit("B");
        private static final MeasureUnit KILOBYTE = new MeasureUnit("KB");
        private static final MeasureUnit MEGABYTE = new MeasureUnit("MB");
        private static final MeasureUnit GIGABYTE = new MeasureUnit("GB");
        private static final MeasureUnit TERABYTE = new MeasureUnit("TB");
        private static final MeasureUnit PETABYTE = new MeasureUnit("PB");

        String type;

        private MeasureUnit(String type) {
            this.type = type;
        }
    }

    private static class RoundedBytesResult {
        public final float value;
        private final MeasureUnit units;
        private final int fractionDigits;
        private final long roundedBytes;

        private RoundedBytesResult(float value, MeasureUnit units, int fractionDigits, long roundedBytes) {
            this.value = value;
            this.units = units;
            this.fractionDigits = fractionDigits;
            this.roundedBytes = roundedBytes;
        }

        /**
         * Returns a RoundedBytesResult object based on the input size in bytes and the rounding
         * flags. The result can be used for formatting.
         */
        static RoundedBytesResult roundBytes(long sizeBytes, int flags) {
            final boolean isNegative = (sizeBytes < 0);
            float result = isNegative ? -sizeBytes : sizeBytes;
            MeasureUnit units = MeasureUnit.BYTE;
            long mult = 1;
            if (result > 900) {
                units = MeasureUnit.KILOBYTE;
                mult = 1000;
                result = result / 1000;
            }
            if (result > 900) {
                units = MeasureUnit.MEGABYTE;
                mult *= 1000;
                result = result / 1000;
            }
            if (result > 900) {
                units = MeasureUnit.GIGABYTE;
                mult *= 1000;
                result = result / 1000;
            }
            if (result > 900) {
                units = MeasureUnit.TERABYTE;
                mult *= 1000;
                result = result / 1000;
            }
            if (result > 900) {
                units = MeasureUnit.PETABYTE;
                mult *= 1000;
                result = result / 1000;
            }
            // Note we calculate the rounded long by ourselves, but still let NumberFormat compute
            // the rounded value. NumberFormat.format(0.1) might not return "0.1" due to floating
            // point errors.
            final int roundFactor;
            final int roundDigits;
            if (mult == 1 || result >= 100) {
                roundFactor = 1;
                roundDigits = 0;
            } else if (result < 1) {
                roundFactor = 100;
                roundDigits = 2;
            } else if (result < 10) {
                if ((flags & FLAG_SHORTER) != 0) {
                    roundFactor = 10;
                    roundDigits = 1;
                } else {
                    roundFactor = 100;
                    roundDigits = 2;
                }
            } else { // 10 <= result < 100
                if ((flags & FLAG_SHORTER) != 0) {
                    roundFactor = 1;
                    roundDigits = 0;
                } else {
                    roundFactor = 100;
                    roundDigits = 2;
                }
            }

            if (isNegative) {
                result = -result;
            }

            // Note this might overflow if abs(result) >= Long.MAX_VALUE / 100, but that's like
            // 80PB so it's okay (for now)...
            final long roundedBytes =
                    (flags & FLAG_CALCULATE_ROUNDED) == 0 ? 0
                            : (((long) Math.round(result * roundFactor)) * mult / roundFactor);

            return new RoundedBytesResult(result, units, roundDigits, roundedBytes);
        }
    }


    public static String formatSizeWithOS(Context context, long sizeBytes) {
        RoundedBytesResult roundedBytesResult = RoundedBytesResult.roundBytes(sizeBytes, FLAG_DEFAULT);
        Resources resources = context.getResources();
        Locale locale = resources.getConfiguration().locale;
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
        numberFormatter.setMinimumFractionDigits(roundedBytesResult.fractionDigits);
        numberFormatter.setMaximumFractionDigits(roundedBytesResult.fractionDigits);
        numberFormatter.setGroupingUsed(false);
        if (numberFormatter instanceof DecimalFormat) {
            // We do this only for DecimalFormat, since in the general NumberFormat case, calling
            // setRoundingMode may throw an exception.
            numberFormatter.setRoundingMode(RoundingMode.HALF_UP);
        }
        String size = numberFormatter.format(roundedBytesResult.value);

        String unit = resources.getString(resources.getIdentifier(MeasureUnit.RES_PREFIX + roundedBytesResult.units.type, "string", context.getPackageName()));

        return context.getString(R.string.fileSizeSuffix, size, unit);
    }

    /**
     * 参考 {@link #formatSizeWithOS}
     *
     * @param size
     * @return
     */
    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    /**
     * 参考 {@link #formatSizeWithOS}
     *
     * @param size
     * @return
     */
    public static StorageSize convertStorageSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        StorageSize sto = new StorageSize();
        if (size >= gb) {
            sto.suffix = "GB";
            sto.value = (float) size / gb;
            return sto;
        } else if (size >= mb) {
            sto.suffix = "MB";
            sto.value = (float) size / mb;
            return sto;
        } else if (size >= kb) {
            sto.suffix = "KB";
            sto.value = (float) size / kb;
            return sto;
        } else {
            sto.suffix = "B";
            sto.value = (float) size;
            return sto;
        }
    }

    public static float convertStorageWithMb(long size) {
        long mb = 1024L * 1024L;
        return (float) size / mb;
    }

    public static SDCardInfo getSDCardInfo() {
        if (Environment.isExternalStorageRemovable()) {
            String sDcString = Environment.getExternalStorageState();
            if (sDcString.equals(Environment.MEDIA_MOUNTED)) {
                File pathFile = Environment.getExternalStorageDirectory();
                try {
                    StatFs statfs = new StatFs(pathFile.getPath());
                    // 获取SDCard上BLOCK总数
                    long nTotalBlocks = statfs.getBlockCount();
                    // 获取SDCard上每个block的SIZE
                    long nBlocSize = statfs.getBlockSize();
                    // 获取可供程序使用的Block的数量
                    long nAvailBlock = statfs.getAvailableBlocks();
                    SDCardInfo info = new SDCardInfo();
                    // 计算SDCard 总容量大小MB
                    info.total = nTotalBlocks * nBlocSize;
                    // 计算 SDCard 剩余大小MB
                    info.free = nAvailBlock * nBlocSize;
                    return info;
                } catch (IllegalArgumentException e) {
                    ZLog.e("E", "StringUtil MD5 exception");
                }
            }
        }
        return null;
    }

    public static SDCardInfo getSystemSpaceInfo() {
        long blockSize = 0L;
        long totalBlocks = 0L;
        long availableBlocks = 0L;
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
            availableBlocks = stat.getAvailableBlocks();
        } catch (Exception e) {
            ZLog.e("E", "StringUtil MD5 exception");
        }

        long totalSize = blockSize * totalBlocks;
        long availSize = availableBlocks * blockSize;
        SDCardInfo info = new SDCardInfo();
        info.total = totalSize;
        info.free = availSize;
        return info;
    }

    public static SDCardInfo getRootSpaceInfo() {
        long blockSize = 0L;
        long totalBlocks = 0L;
        long availableBlocks = 0L;
        try {
            File path = Environment.getRootDirectory();
            StatFs stat = new StatFs(path.getPath());
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
            availableBlocks = stat.getAvailableBlocks();
        } catch (Exception e) {
            ZLog.e("E", "StringUtil MD5 exception");
        }

        long totalSize = blockSize * totalBlocks;
        long availSize = availableBlocks * blockSize;

        SDCardInfo info = new SDCardInfo();
        // 计算SDCard 总容量大小MB
        info.total = totalSize;

        // 计算 SDCard 剩余大小MB
        info.free = availSize;
        return info;
    }

    /**
     * 获取外置SD卡路径
     *
     * @return
     */
    private static String getExternalSdCardPath() {
        String sdPath = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            try {
                StorageManager storageManager = (StorageManager) AppContext.getInstance().getSystemService(Context.STORAGE_SERVICE);
                Method volumeList = storageManager.getClass().getMethod("getVolumeList");
                Class sdStorageVolume = Class.forName("android.os.storage.StorageVolume");
                Method isRemovable = sdStorageVolume.getMethod("isRemovable");
                Method getPath = sdStorageVolume.getMethod("getPath");
                Object volumes = volumeList.invoke(storageManager);
                int index = Array.getLength(volumes);
                for (int i = 0; i < index; i++) {
                    Object volume = Array.get(volumes, i);
                    String path = (String) getPath.invoke(volume);
                    boolean isOutSd = (boolean) isRemovable.invoke(volume);
                    if (isOutSd) {
                        //外部存储
                        sdPath = path;
                    }
                }
            } catch (Exception e) {
                ZLog.e("E", "StringUtil MD5 exception");
            }
        } else {
            // 外置sd路径
            sdPath = System.getenv("SECONDARY_STORAGE");
        }

        ZLog.i("StorageUtil", "StorageUtil: " + sdPath);
        return sdPath;
    }

    /**
     * 判断外置SD卡是否存在
     *
     * @return
     */
    private static boolean isExternalSdCardExist() {
        return (!StringUtil.isEmpty(getExternalSdCardPath()));
    }
}
