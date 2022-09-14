package com.mopstream.server.utility;

import com.mopstream.common.interaction.Request;
import com.mopstream.common.utility.Outputer;

public class ShutdownHandling {

    public static void addCollectionSavingHook(RequestHandler requestHandler) {
        Thread savingHook = new Thread(() -> {
            requestHandler.handle(new Request("save", "", null));
            Outputer.println("\nКоллекция успешно сохранена. Сервер принял спок.");
        });
        Runtime.getRuntime().addShutdownHook(savingHook);
    }
}
