package fr.openium.examplepam.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import fr.openium.examplepam.model.Call;

@Database(entities = {Call.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase sInstance;

    public abstract CallDao callDao();


    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ex")
                    .allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }
}
