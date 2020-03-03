package com.sgs.next.comcourier.sfservice.h6.utils;

import android.os.Environment;

import com.sgs.next.comcourier.sfservice.fourlevel.FileUtil;

import java.io.File;
import java.util.Locale;


public class DeviceUtil {
    private DeviceUtil() {
    }

    public static final String SERVICES_DATABASE_NAME = "gat.db";

    private static File serviceDatabaseFile;

    public static synchronized File getServiceDatabaseFile() {
        if (serviceDatabaseFile == null) {
            serviceDatabaseFile = FileUtil.getFileInExternalStoragePath(SERVICES_DATABASE_NAME);
           /* DB_PATH = "/data"
                    + Environment.getDataDirectory().getAbsolutePath() + "/"
                    + PACKAGE_NAME;*/
        }
        return serviceDatabaseFile;
    }

    public static synchronized File getSdDirFile(String fileName) {
        return FileUtil.getFileInExternalStoragePath(fileName);
    }

    public static synchronized void releaseServiceDatabaseFile() {
        serviceDatabaseFile = null;
    }


    public static boolean isTraditionalChina() {
        return isTW() || isHK();
    }

    public static boolean isTW() {
        return Locale.getDefault().equals(Locale.TAIWAN);
    }

    public static boolean isHK() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        String language = locale.getLanguage();
        return "zh".equalsIgnoreCase(language) && "HK".equalsIgnoreCase(country);
    }

}
