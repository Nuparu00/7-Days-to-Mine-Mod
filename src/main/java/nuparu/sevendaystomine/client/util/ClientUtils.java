package nuparu.sevendaystomine.client.util;

import net.minecraft.client.resources.language.I18n;

public class ClientUtils {
    /*
    Never, ever use on server side!
     */
    public static String localize(String unlocalized, Object... args) {
        return I18n.get(unlocalized, args);
    }
}
