package com.example.minutebible9;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetDatabaseOpenHelper {

    private Context context;
    private String DB_NAME;

    public AssetDatabaseOpenHelper(Context context, String database_name) {
        this.context = context;
        this.DB_NAME = database_name;
    }

    /**
     * Copy database file from assets folder inside the apk to the system database path.
     * @param context Context
     * @param databaseName Database file name inside assets folder
     * @param overwrite True to rewrite on the database if exists
     * @return True if the database have copied successfully or if the database already exists without overwrite, false otherwise.
     */
    public boolean copyDatabaseFromAssets(Context context, String databaseName , boolean overwrite)  {

        File outputFile = context.getDatabasePath(databaseName);
        if (outputFile.exists() && !overwrite) {
            return true;
        }
        else{
            outputFile = context.getDatabasePath(databaseName + ".temp");
            outputFile.getParentFile().mkdirs();

            try {
                InputStream inputStream = context.getAssets().open(databaseName);
                OutputStream outputStream = new FileOutputStream(outputFile);


                // transfer bytes from the input stream into the output stream
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // Close the streams
                outputStream.flush();
                outputStream.close();
                inputStream.close();

                outputFile.renameTo(context.getDatabasePath(databaseName));

            } catch (IOException e) {
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                return false;
            }

            return true;}
    }
}