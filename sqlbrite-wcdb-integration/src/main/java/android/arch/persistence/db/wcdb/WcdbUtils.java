package android.arch.persistence.db.wcdb;

import android.arch.persistence.db.SupportSQLiteDatabase;

import com.tencent.wcdb.DatabaseUtils;

import java.io.File;

/**
 * @author Guang1234567
 * @date 2018/3/6 9:34
 */

public class WcdbUtils {

    private WcdbUtils() {
        throw new AssertionError("No instances.");
    }

    public static void exportDecryption(SupportSQLiteDatabase db, File dstFile) throws Exception {
        // https://github.com/Tencent/wcdb/issues/36
        // 先open了加密DB，得到 "db" 对象
        // 将非加密DB挂载到 "db", "old" 代表着非加密DB
        String sql = String.format("ATTACH DATABASE %s AS old KEY '';",
                DatabaseUtils.sqlEscapeString(dstFile.getPath()));
        db.execSQL(sql);

        // 将数据从 "main"(加密db) 迁移到 "old"(非加密db)
        db.beginTransaction();
        try {
            db.query("SELECT sqlcipher_export('old', 'main');");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        // 将old脱离
        db.execSQL("DETACH DATABASE old;");
    }
}
