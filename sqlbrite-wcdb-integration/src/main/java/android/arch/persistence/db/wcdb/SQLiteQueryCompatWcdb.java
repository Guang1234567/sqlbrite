package android.arch.persistence.db.wcdb;

import com.tencent.wcdb.CursorWindow;
import com.tencent.wcdb.database.SQLiteCursor;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteDatabaseCorruptException;
import com.tencent.wcdb.database.SQLiteException;
import com.tencent.wcdb.database.SQLiteProgram;
import com.tencent.wcdb.database.SQLiteQuery;
import com.tencent.wcdb.support.CancellationSignal;
import com.tencent.wcdb.support.Log;
import com.tencent.wcdb.support.OperationCanceledException;

/**
 *
 * Copy from {@link SQLiteQuery}
 *
 * Represents a query that reads the resulting rows into a {@link SQLiteQueryCompatWcdb}.
 * This class is used by {@link SQLiteCursorCompatWcdb} and isn't useful itself.
 * <p>
 * This class is not thread-safe.
 * </p>
 */
final class SQLiteQueryCompatWcdb extends SQLiteProgram {
    private static final String TAG = "WCDB.SupportSQLiteQuery";

    private final CancellationSignal mCancellationSignal;

    SQLiteQueryCompatWcdb(SQLiteDatabase db, String query, Object[] bindArgs, CancellationSignal cancellationSignal) {
        super(db, query, bindArgs, cancellationSignal);

        mCancellationSignal = cancellationSignal;
    }

    /**
     * Reads rows into a buffer.
     *
     * @param window The window to fill into
     * @param startPos The start position for filling the window.
     * @param requiredPos The position of a row that MUST be in the window.
     * If it won't fit, then the query should discard part of what it filled.
     * @param countAllRows True to count all rows that the query would
     * return regardless of whether they fit in the window.
     * @return Number of rows that were enumerated.  Might not be all rows
     * unless countAllRows is true.
     *
     * @throws SQLiteException if an error occurs.
     * @throws OperationCanceledException if the operation was canceled.
     */
    int fillWindow(CursorWindow window, int startPos, int requiredPos, boolean countAllRows) {
        acquireReference();
        try {
            window.acquireReference();
            try {
                int numRows = getSession().executeForCursorWindow(getSql(), getBindArgs(),
                        window, startPos, requiredPos, countAllRows, getConnectionFlags(),
                        mCancellationSignal);
                return numRows;
            } catch (SQLiteDatabaseCorruptException ex) {
                onCorruption();
                throw ex;
            } catch (SQLiteException ex) {
                Log.e(TAG, "exception: " + ex.getMessage() + "; query: " + getSql());
                throw ex;
            } finally {
                window.releaseReference();
            }
        } finally {
            releaseReference();
        }
    }

    @Override
    public String toString() {
        return "SupportSQLiteQuery: " + getSql();
    }
}
