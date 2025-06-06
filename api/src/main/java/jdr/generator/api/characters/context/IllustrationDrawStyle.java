package jdr.generator.api.characters.context;

import java.util.Arrays;

/**
 * Defines the available illustration drawing styles and their associated prompts.
 */
public enum IllustrationDrawStyle {

    PHOTO_REALISTIC("photoRealistic", "Create a close-up, photorealistic studio portrait " +
            "of a mature-looking individual, taken with a professional camera, in a heroic " +
            "fantasy setting. The lighting is dramatic and natural, emphasizing the character's " +
            "facial features and upper torso. The background is a smooth, dark gradient, " +
            "carefully blurred to keep focus on the subject. The overall mood is serious and " +
            "immersive, aiming for a high-resolution, unedited photograph."),

    OLD_SCHOOL("oldSchool", "Generate an old-school, black and white, " +
            "hand-drawn illustration with a classic fantasy feel. Emphasize strong lines, " +
            "cross-hatching, and a slightly rough texture, reminiscent of early RPG rulebook " +
            "art. The image should convey a sense of nostalgia and timeless adventure, focusing " +
            "on character outlines and shading rather than color details."),

    FANTASY_ART("fantasyArt", "Generate a highly detailed and artistic " +
            "illustration in a heroic-fantasy style, suitable for a role-playing game character " +
            "portrait. Focus on a close-up of the character's face and upper body. The style " +
            "should resemble digital painting with soft gradients and a focus on character " +
            "details, expression, and mood. The background should be subtly blurred and " +
            "complementary to the character, enhancing their presence."),

    ANIME("anime", "Create a studio portrait of a mature-looking individual in a " +
            "detailed anime style, focusing on the character's face and upper body. Emphasize " +
            "expressive eyes, dynamic hair, and vibrant colors. The background should be " +
            "minimalist or softly blurred to keep focus on the character, with a clean and " +
            "polished finish."),

    CARTOON("cartoon", "Generate a vibrant and expressive studio portrait of a " +
            "mature-looking individual in a cartoon style, focusing on the character's face and " +
            "upper body. Use bold lines, bright colors, and exaggerated features to convey " +
            "personality and humor. The background should be simple and colorful, enhancing the " +
            "playful mood."),

    SCI_FI("sciFi", "Create a studio portrait of a mature-looking individual in a " +
            "futuristic sci-fi style, focusing on the character's face and upper body. Incorporate " +
            "elements of advanced technology, glowing circuits, and sleek metallic textures. The " +
            "lighting should be sharp and vibrant, with a background suggesting a technological " +
            "environment or starscape, aiming for a high-tech and adventurous feel."),

    STEAMPUNK("steampunk", "Generate a detailed studio portrait of a " +
            "mature-looking individual in a steampunk aesthetic, focusing on the character's face " +
            "and upper body. Include intricate gears, cogs, brass, and leather textures. The " +
            "lighting should be warm and atmospheric, with a background hinting at Victorian-era " +
            "machinery or an industrial setting, conveying a sense of adventure and ingenuity."),

    GOTHIC("gothic", "Create a dark and dramatic studio portrait of a " +
            "mature-looking individual in a gothic style, focusing on the character's face and " +
            "upper body. Emphasize deep shadows, pale complexions, and intricate, ornate details " +
            "reminiscent of gothic architecture or Victorian fashion. The background should be " +
            "somber and atmospheric, conveying a sense of mystery and melancholy."),

    WATERCOLOUR("watercolour", "Generate a soft and ethereal studio portrait of a " +
            "mature-looking individual in a watercolour painting style, focusing on the character's " +
            "face and upper body. Use delicate washes, visible brushstrokes, and a translucent " +
            "quality. The background should be fluid and abstract, enhancing the dreamlike and " +
            "artistic feel."),

    PIXEL_ART("pixelArt", "Create a retro-inspired studio portrait of a " +
            "mature-looking individual in a detailed pixel art style, focusing on the character's " +
            "face and upper body. Ensure clear, distinct pixels and a vibrant color palette, " +
            "reminiscent of classic 8-bit or 16-bit video games. The background should be simple " +
            "and pixelated, maintaining a nostalgic and charming aesthetic."),

    COMIC_BOOK("comicBook", "Generate a dynamic and bold studio portrait of a " +
            "mature-looking individual in a comic book style, focusing on the character's face and " +
            "upper body. Use strong outlines, vibrant colors, and halftone dots (Ben-Day dots) for " +
            "shading. The background should be impactful, perhaps with action lines or a simple, " +
            "graphic pattern, conveying energy and heroism."),

    MINIMALIST("minimalist", "Create a clean and elegant studio portrait of a " +
            "mature-looking individual in a minimalist style, focusing on the character's face and " +
            "upper body. Use simple lines, limited color palette, and ample negative space. The " +
            "background should be plain and uncluttered, emphasizing form and simplicity."),

    ABSTRACT("abstract", "Generate an artistic and interpretive studio portrait of a " +
            "mature-looking individual in an abstract art style, focusing on the character's face " +
            "and upper body. Use non-representational forms, expressive colors, and unconventional " +
            "textures. The background should be equally abstract, creating a visually striking " +
            "and thought-provoking image."),

    OIL_PAINTING("oilPainting", "Create a rich and textured studio portrait of a " +
            "mature-looking individual in the style of a classical oil painting, focusing on the " +
            "character's face and upper body. Emphasize visible brushstrokes, deep colors, and " +
            "subtle blending. The lighting should be soft and chiaroscuro, with a dark, atmospheric " +
            "background, conveying depth and timelessness."),

    PENCIL_SKETCH("pencilSketch", "Generate a raw and expressive studio portrait of a " +
            "mature-looking individual as a pencil sketch, focusing on the character's face and " +
            "upper body. Emphasize intricate line work, varied pencil pressures, and subtle shading " +
            "to create depth and realism. The background should be blank or subtly textured, " +
            "highlighting the artistic process."),

    IMPRESSIONISTIC("impressionistic", "Create a vibrant and evocative studio portrait " +
            "of a mature-looking individual in an impressionistic painting style, focusing on the " +
            "character's face and upper body. Use loose brushstrokes, bright, unblended colors, " +
            "and a focus on light and atmosphere. The background should be blended and textured, " +
            "conveying a fleeting moment and sensory experience."),

    SURREALIST("surrealist", "Generate a dreamlike and imaginative studio portrait of " +
            "a mature-looking individual in a surrealist art style, focusing on the character's face " +
            "and upper body. Incorporate unexpected elements, distorted realities, and symbolic " +
            "imagery. The background should be equally bizarre and fantastical, creating a thought-" +
            "provoking and visually striking image."),

    CONCEPT_ART("conceptArt", "Create a dynamic and imaginative studio portrait of a " +
            "mature-looking individual in a concept art style, focusing on the character's face and " +
            "upper body. Emphasize bold design, detailed rendering of materials, and a sense of " +
            "world-building. The background should hint at an epic setting or a design brief, " +
            "conveying potential and narrative."),

    CHILDREN_BOOK("childrenBook", "Generate a charming and whimsical studio portrait " +
            "of a mature-looking individual in a children's book illustration style, focusing on " +
            "the character's face and upper body. Use soft colors, rounded shapes, and a friendly, " +
            "approachable aesthetic. The background should be simple and inviting, creating a " +
            "warm and enchanting mood."),

    UKIYO_E("ukiyoE", "Create a stylistic studio portrait of a mature-looking " +
            "individual in the Ukiyo-e Japanese woodblock print style, focusing on the character's " +
            "face and upper body. Use bold outlines, flat areas of vibrant color, and distinctive " +
            "patterns. The background should be minimalist with traditional motifs, conveying " +
            "elegance and cultural richness."),

    ART_NOUVEAU("artNouveau", "Generate an elegant and flowing studio portrait of a " +
            "mature-looking individual in the Art Nouveau style, focusing on the character's face " +
            "and upper body. Emphasize organic lines, natural forms (like flowers and vines), " +
            "and muted, sophisticated colors. The background should be decorative and harmonious, " +
            "creating a sense of beauty and grace."),

    DYSTOPIAN("dystopian", "Create a grim and atmospheric studio portrait of a " +
            "mature-looking individual in a dystopian style, focusing on the character's face and " +
            "upper body. Use desaturated colors, harsh lighting, and subtle details of wear and " +
            "oppression. The background should be industrial or desolate, conveying a sense of " +
            "hopelessness and struggle."),

    CYBER_PUNK("cyberPunk", "Create a studio portrait of a " +
            "mature-looking individual in a cyberpunk anime style, with neon lights, dark " +
            "tones and a futuristic vibe like Ghost in the Shell. Focus on the character's face and upper body. Suitable for a role-playing " +
            "game character portrait. Consider the overall mood and atmosphere.");


    private final String styleKey;
    private final String basePrompt;

    IllustrationDrawStyle(String styleKey, String basePrompt) {
        this.styleKey = styleKey;
        this.basePrompt = basePrompt;
    }

    public String getStyleKey() {
        return styleKey;
    }

    public String getBasePrompt() {
        return basePrompt;
    }

    /**
     * Returns the IllustrationDrawStyle corresponding to the given style key.
     * > IllustrationDrawStyle.fromKeyOrDefault(character_context.context_draw_style)
     * If the key is not found, returns a default style (e.g., PHOTO_REALISTIC).
     *
     * @param styleKey The string key representing the drawing style
     * @return The corresponding IllustrationDrawStyle enum, or a default if not found.
     */
    public static IllustrationDrawStyle fromKeyOrDefault(String styleKey) {
        return Arrays.stream(IllustrationDrawStyle.values())
                .filter(style -> style.getStyleKey().equalsIgnoreCase(styleKey))
                .findFirst()
                .orElse(PHOTO_REALISTIC);
    }
}