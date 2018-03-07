package android.arch.persistence.db.sqlcipher;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

/**
 * @author Guang1234567
 * @date 2018/3/6 9:17
 */

public class SqlcipherUtils {

    private SqlcipherUtils() {
        throw new AssertionError("No instances.");
    }

    public static void exportEncryption(SQLiteDatabase db, String password, File dstFile) {
        String sql = "ATTACH DATABASE '" + dstFile.getPath() + "' AS encrypted KEY '" + password + "';";
        db.execSQL(sql);

        db.beginTransaction();
        try {
            sql = "SELECT sqlcipher_export('encrypted');";
            db.execSQL(sql);
        } finally {
            db.endTransaction();
        }

        sql = "DETACH DATABASE encrypted;";
        db.execSQL(sql);
    }

    public static void exportDecryption(SQLiteDatabase db, File dstFile) {
        String sql = "ATTACH DATABASE '" + dstFile.getPath() + "' AS plaintext KEY '';";
        db.execSQL(sql);

        db.beginTransaction();
        try {
            sql = "SELECT sqlcipher_export('plaintext');";
            db.execSQL(sql);
        } finally {
            db.endTransaction();
        }

        sql = "DETACH DATABASE plaintext;";
        db.execSQL(sql);
    }
}
