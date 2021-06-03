package idosa.huji.postpc.sandwich_stand;

import android.app.Application;

public class SandwichStandApp extends Application {
    private static SandwichStandApp instance = null;
    private static LocalDb localDb;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        localDb = new LocalDb(this);
    }

    public SandwichStandApp getInstance() {
        return instance;
    }

    public static LocalDb getLocalDb() {
        return localDb;
    }
}
