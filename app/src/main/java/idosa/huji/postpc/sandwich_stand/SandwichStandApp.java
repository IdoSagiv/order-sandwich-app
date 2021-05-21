package idosa.huji.postpc.sandwich_stand;

import android.app.Application;

public class SandwichStandApp extends Application {
    private static SandwichStandApp instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // todo: lunch local and remote DBs
    }

    public SandwichStandApp getInstance() {
        return instance;
    }
}
