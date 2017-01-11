package in.gripxtech.recyclerviewscrolldemo.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public abstract class BasePrefs {

    public static final String RowId;
    private static final String Table;

    static {
        Table = "table";
        RowId = "_id";
    }

    private final String TAG;
    private Context mContext;
    private SharedPreferences mPreferences;

    /**
     * @param TAG     name for SharedPreferences
     * @param context context
     */
    public BasePrefs(@NonNull final String TAG, @NonNull final Context context) {
        this.TAG = TAG;
        mContext = context;
        mPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public String getTAG() {
        return TAG;
    }

    public Context getContext() {
        return mContext;
    }

    protected boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    protected boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    protected void putBoolean(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    protected int getInt(String key) {
        return mPreferences.getInt(key, -1);
    }

    protected int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    protected void putInt(String key, int value) {
        mPreferences.edit().putInt(key, value).apply();
    }

    protected long getLong(String key) {
        return mPreferences.getLong(key, -1L);
    }

    protected long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    protected void putLong(String key, long value) {
        mPreferences.edit().putLong(key, value).apply();
    }

    protected String getString(String key) {
        return mPreferences.getString(key, null);
    }

    protected String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    protected void putString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    protected String getTable() {
        return getString(Table, new JSONArray().toString());
    }

    protected void setTable(String table) {
        putString(Table, table);
    }

    protected int getId() {
        return getInt(RowId, 0);
    }

    protected void setId(int rowId) {
        putInt(RowId, rowId);
    }

    private int incrementId() {
        setId(getId() + 1);
        return getId();
    }

    private int decrementId() {
        setId(getId() - 1);
        return getId();
    }

    public int insert(@NonNull JSONObject row) {
        Log.d(TAG, "insert() called with: row = " + row);
        if (row.has("_id")) {
            throw new UnsupportedOperationException("Unexpected key \"_id\" found");
        }
        int id = incrementId();
        try {
            JSONArray table = new JSONArray(getTable());
            row.put(RowId, id);
            table.put(row);
            setTable(table.toString());
            return id;
        } catch (JSONException e) {
            Log.e(TAG, "insert: ", e);
            decrementId();
        }
        return -1;
    }

    public boolean delete(int id) {
        Log.d(TAG, "delete() called with: _id = [" + id + "]");
        // There's no remove so, copy jsonArray into Other JsonArray except the row with _id
        try {
            JSONArray table = new JSONArray(getTable());
            JSONArray newTable = new JSONArray();
            for (int i = 0; i < table.length(); i++) {
                JSONObject row = table.getJSONObject(i);
                if (row.getInt(RowId) != id) {
                    newTable.put(row);
                }
            }
            setTable(newTable.toString());
            return true;
        } catch (JSONException e) {
            Log.e(TAG, "delete: ", e);
        }
        return false;
    }

    public int update(@NonNull JSONObject row) {
        // There's no remove so, copy jsonArray into Other JsonArray except the row with _id
        Log.d(TAG, "update() called with: row = " + row);
        if (!row.has("_id")) {
            throw new UnsupportedOperationException("Expected key \"_id\" not found");
        }
        try {
            int rowId = row.getInt(RowId);
            JSONArray table = new JSONArray(getTable());
            JSONArray newTable = new JSONArray();
            for (int i = 0; i < table.length(); i++) {
                JSONObject row1 = table.getJSONObject(i);
                if (row1.getInt(RowId) == rowId) {
                    for (Iterator<String> iterator = row.keys(); iterator.hasNext(); ) {
                        String key = iterator.next();
                        row1.put(key, row.get(key));
                    }
                }
                newTable.put(row1);
            }
            setTable(newTable.toString());
            return rowId;
        } catch (JSONException e) {
            Log.e(TAG, "update: ", e);
        }
        return -1;
    }

    public JSONArray selectAll() {
        try {
            JSONArray table = new JSONArray(getTable());
            Log.d(TAG, "selectAll() returned with: table = [" + table + "]");
            return table;
        } catch (JSONException e) {
            Log.e(TAG, "selectAll: ", e);
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        mPreferences.edit().clear().apply();
    }
}
