package com.tinyblack.framework.log;

import android.content.Context;
import android.os.Environment;

import com.tinyblack.framework.log.printer.AndroidPrinter;
import com.tinyblack.framework.log.printer.ConsolePrinter;
import com.tinyblack.framework.log.printer.Printer;
import com.tinyblack.framework.log.printer.file.FilePrinter;
import com.tinyblack.framework.log.printer.file.backup.NeverBackupStrategy;
import com.tinyblack.framework.log.printer.file.naming.DateFileNameGenerator;

import java.io.File;

/**
 * @author yubiao
 */
public class LogConfig {

    public static void init(Context context, String filePath, boolean isDebug) {
        LogConfiguration config = new LogConfiguration.Builder()
                // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
                .logLevel(isDebug ? LogLevel.ALL
                        : LogLevel.NONE)
                // Specify TAG, default: "T-LOG"
                .tag("DF_Charge_SDK")
                // Enable thread info, disabled by default
                .enableThreadInfo()
                // Enable stack trace info with depth 2, disabled by default
                .enableStackTrace(2)
                // Enable border, disabled by default
                .enableBorder()
                .build();

        // Printer that print the log using android.util.Log
        Printer androidPrinter = new AndroidPrinter(true);
        // Printer that print the log to console using System.out
//        Printer consolePrinter = new ConsolePrinter();
        // Printer that print(save) the log to file
        Printer filePrinter = new FilePrinter
                // Specify the directory path of log file(s)
                .Builder(filePath)
                // Default: ChangelessFileNameGenerator("log")
                .fileNameGenerator(new DateFileNameGenerator())
                // Default: FileSizeBackupStrategy(1024 * 1024)
                .backupStrategy(new NeverBackupStrategy())
                .build();

        // Initialize TLog
        TLog.init(
                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
                config,
                // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
                androidPrinter,
//                consolePrinter,
                filePrinter);

        TLog.d("Log存储路径--->" + filePath);
    }


}
