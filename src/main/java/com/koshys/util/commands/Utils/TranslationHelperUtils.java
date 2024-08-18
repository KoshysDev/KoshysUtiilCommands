package com.koshys.util.commands.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class TranslationHelperUtils {

    public static String getTranslationForMob(String mobName) {
        return switch (mobName.toLowerCase()) {
            case "allay" -> "служку";
            case "armadillo" -> "броненосця";
            case "axolotl" -> "аксолотля";
            case "bat" -> "кажана";
            case "bee" -> "бджолу";
            case "blaze" -> "блейза";
            case "bogged breeze" -> "болотного бриза"; // Assuming this is a custom mob
            case "camel" -> "верблюда";
            case "cat" -> "кота";
            case "cave spider" -> "печерного павука";
            case "chicken" -> "курку";
            case "cod" -> "тріску";
            case "cow" -> "корову";
            case "creeper" -> "крипера";
            case "dolphin" -> "дельфіна";
            case "donkey" -> "осла";
            case "drowned" -> "утопленика";
            case "elder guardian" -> "стародавнього охоронця";
            case "ender dragon" -> "ендер дракона";
            case "enderman" -> "ендермена";
            case "endermite" -> "ендермайта";
            case "evoker" -> "викликача";
            case "fox" -> "лисицю";
            case "frog" -> "жабу";
            case "ghast" -> "гаста";
            case "glow squid" -> "світного кальмара";
            case "goat" -> "козу";
            case "guardian" -> "охоронця";
            case "hoglin" -> "хогліна";
            case "horse" -> "коня";
            case "husk" -> "шкідника";
            case "iron golem" -> "залізного голема";
            case "llama" -> "ламу";
            case "magma cube" -> "магма куб";
            case "mooshroom" -> "грибну корову";
            case "mule" -> "мула";
            case "ocelot" -> "оцелота";
            case "panda" -> "панду";
            case "parrot" -> "папугу";
            case "phantom" -> "фантома";
            case "pig" -> "свиню";
            case "piglin" -> "пігліна";
            case "piglin brute" -> "пігліна-звіра";
            case "pillager" -> "грабіжника";
            case "polar bear" -> "білого ведмедя";
            case "pufferfish" -> "рибу-їжака";
            case "rabbit" -> "кролика";
            case "ravager" -> "руйнатора";
            case "salmon" -> "лосося";
            case "sheep" -> "вівцю";
            case "shulker" -> "шалкера";
            case "silverfish" -> "срібну рибку";
            case "skeleton" -> "скелета";
            case "skeleton horse" -> "скелета-коня";
            case "slime" -> "слизня";
            case "sniffer" -> "нюхача";
            case "snow golem" -> "снігового голема";
            case "spider" -> "павука";
            case "squid" -> "кальмара";
            case "stray" -> "блукача";
            case "strider" -> "страйдера";
            case "tadpole" -> "пуголовка";
            case "trader llama" -> "торговельну ламу";
            case "tropical fish" -> "тропічну рибку";
            case "turtle" -> "черепаху";
            case "vex" -> "злого духа";
            case "villager" -> "селянина";
            case "vindicator" -> "захисника";
            case "wandering trader" -> "мандрівного торговця";
            case "warden" -> "вартового";
            case "witch" -> "відьму";
            case "wither" -> "візера";
            case "wither skeleton" -> "візер-скелета";
            case "wolf" -> "вовка";
            case "zoglin" -> "зогліна";
            case "zombie" -> "зомбі";
            case "zombie villager" -> "зомбі-селянина";
            case "zombified piglin" -> "зомбіфікованого пігліна";
            default -> mobName; // Return the original name if not found
        };
    }
}