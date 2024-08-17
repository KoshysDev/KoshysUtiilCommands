package com.koshys.util.commands.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class TranslationHelperUtils {

    public static String getTranslationForMob(String mobName) {
        return switch (mobName.toLowerCase()) {
            case "ghost" -> "гаста";
            case "wither skeleton" -> "візер-скелета";
            case "witch" -> "відьму";
            case "shulker" -> "шалкера";
            case "silverfish" -> "срібну рибку";
            case "cave spider" -> "печерного павука";
            case "witherskelton" -> "візер-скелета";
            case "wither" -> "візера";
            case "iron golem" -> "залізного голема";
            case "snow golem" -> "снігового голема";
            case "phantom" -> "фантома";
            case "drowned" -> "утопленика";
            case "cod" -> "тріску";
            case "salmon" -> "лосося";
            case "tropical fish" -> "тропічну рибку";
            case "pufferfish" -> "рибу-їжака";
            case "turtle" -> "черепаху";
            case "polar bear" -> "білого ведмедя";
            case "rabbit" -> "кролика";
            case "endermite" -> "ендермайта";
            case "bat" -> "кажана";
            case "squid" -> "кальмара";
            case "guardian" -> "охоронця";
            case "elder guardian" -> "стародавнього охоронця";
            case "vex" -> "злого духа";
            case "vindicator" -> "захисника";
            case "evoker" -> "викликача";
            case "pillager" -> "грабіжника";
            case "ravager" -> "руйнатора";
            case "husk" -> "шкідника";
            case "stray" -> "блукача";
            case "llama" -> "ламу";
            case "donkey" -> "осла";
            case "mule" -> "мула";
            case "horse" -> "коня";
            case "panda" -> "панду";
            case "parrot" -> "папугу";
            case "cat" -> "кота";
            case "ocelot" -> "оцелота";
            case "villager" -> "селянина";
            case "magma cube" -> "магма куб";
            case "blaze" -> "блейза";
            case "ghast" -> "гаста";
            case "slime" -> "слизня";
            case "enderman" -> "ендермена";
            case "spider" -> "павука";
            case "creeper" -> "крипера";
            case "skeleton" -> "скелета";
            case "zombie" -> "зомбі";
            case "wolf" -> "вовка";
            case "sheep" -> "овцю";
            case "chicken" -> "курку";
            case "cow" -> "корову";
            case "pig" -> "свиню";
            default -> mobName; // Return the original name if not found
        };
    }
}