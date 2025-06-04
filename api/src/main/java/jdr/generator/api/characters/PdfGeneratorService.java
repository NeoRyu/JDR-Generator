package jdr.generator.api.characters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextService;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsService;
import jdr.generator.api.characters.illustration.CharacterIllustrationEntity;
import jdr.generator.api.characters.illustration.CharacterIllustrationService;
import jdr.generator.api.characters.stats.CharacterJsonDataEntity;
import jdr.generator.api.characters.stats.CharacterJsonDataService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


@Service("pdfGeneratorService")
@RequiredArgsConstructor
public class PdfGeneratorService {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ObjectMapper objectMapper;
    private final String CRLF = "\n"; // Ligne vide

    private final CharacterContextService characterContextService;
    private final CharacterDetailsService characterDetailsService;
    private final CharacterIllustrationService characterIllustrationService;
    private final CharacterJsonDataService characterJsonDataService;


    public byte[] generateCharacterPdf(Long characterId) throws DocumentException {
        CharacterDetailsEntity c_details =
                this.characterDetailsService.findById(characterId);
        CharacterContextEntity c_context =
                this.characterContextService.findById(c_details.getContextId());
        CharacterIllustrationEntity c_illustration = characterIllustrationService
                .findByCharacterDetailsId(characterId);
        CharacterJsonDataEntity c_json_data = characterJsonDataService
                        .findByCharacterDetailsId(characterId)
                        .orElse(null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Ajout du portrait du personnage
            try {
                if (c_illustration != null && c_illustration.getImageBlob() != null
                        && c_illustration.getImageBlob().length > 0 ) {
                    Image characterImage = Image.getInstance(c_illustration.getImageBlob());
                    // Ajuster la taille de l'image pour qu'elle tienne sur la page
                    float documentWidth = document.getPageSize().getWidth()
                            - document.leftMargin() - document.rightMargin();
                    float documentHeight = document.getPageSize().getHeight()
                            - document.topMargin() - document.bottomMargin();

                    // Si l'image est trop grande, la réduire
                    if (characterImage.getScaledWidth() > documentWidth
                            || characterImage.getScaledHeight() > documentHeight) {
                        // Réduit à 1/3 de la hauteur de page :
                        characterImage.scaleToFit(documentWidth, documentHeight / 3);
                        // Centrer l'image :
                        characterImage.setAlignment(Image.MIDDLE | Image.TEXTWRAP);
                    }
                    document.add(characterImage);
                    document.add(new Paragraph(CRLF));
                }
            } catch (IOException e) {
                LOGGER.error("Impossible d'ajouter l'image au PDF: " + e.getMessage());
                document.add(new Paragraph("Illustration non disponible (erreur de chargement de l'image)."));
                document.add(new Paragraph(CRLF));
            }

            // Ajout du titre du personnage
            document.add(new Paragraph("Fiche de Personnage : " + c_details.getName()));
            document.add(new Paragraph(CRLF));

            document.add(new Paragraph("Age : " + c_details.getAge()));
            document.add(new Paragraph("Sexe : " + c_context.getPromptGender()));
            document.add(new Paragraph("Race : " + c_context.getPromptRace()));
            document.add(new Paragraph("Classe : " + c_context.getPromptClass()));
            document.add(new Paragraph("Univers : " + c_context.getPromptSystem()));
            document.add(new Paragraph("Description : " + c_context.getPromptDescription()));
            document.add(new Paragraph(CRLF));

            // DETAILS
            document.add(new Paragraph("Lieu de naissance : "
                    + c_details.getBirthPlace()));
            document.add(new Paragraph("Lieu de résidence : "
                    + c_details.getResidenceLocation()));
            document.add(new Paragraph("Raison de la résidence : "
                    + c_details.getReasonForResidence()));
            document.add(new Paragraph("Climat : "
                    + c_details.getClimate()));
            document.add(new Paragraph("Problèmes communs : "
                    + c_details.getCommonProblems()));
            document.add(new Paragraph("Routine journalière : "
                    + c_details.getDailyRoutine()));
            document.add(new Paragraph("Parents en vie ? : "
                    + c_details.getParentsAlive()));
            document.add(new Paragraph("Détails sur les parents : "
                    + c_details.getDetailsAboutParents()));
            document.add(new Paragraph("Sentiments par rapport aux parents : "
                    + c_details.getFeelingsAboutParents()));
            document.add(new Paragraph("Fraternité ? : "
                    + c_details.getSiblings()));
            document.add(new Paragraph("Enfance du personnage : "
                    + c_details.getChildhoodStory()));
            document.add(new Paragraph("Amis d'enfance : "
                    + c_details.getYouthFriends()));
            document.add(new Paragraph("Animal de compagnie : "
                    + c_details.getPet()));
            document.add(new Paragraph("État matrimonial : "
                    + c_details.getMaritalStatus()));
            document.add(new Paragraph("Type de conjoint : "
                    + c_details.getTypeOfLover()));
            document.add(new Paragraph("Histoire conjugale : "
                    + c_details.getConjugalHistory()));
            document.add(new Paragraph("Nombre d'enfants : "
                    + c_details.getChildren()));
            document.add(new Paragraph("Education : "
                    + c_details.getEducation()));
            document.add(new Paragraph("Profession : "
                    + c_details.getProfession()));
            document.add(new Paragraph("Raison de la profession : "
                    + c_details.getReasonForProfession()));
            document.add(new Paragraph("Préférences de travail : "
                    + c_details.getWorkPreferences()));
            document.add(new Paragraph("Changement souhaité dans le monde : "
                    + c_details.getChangeInWorld()));
            document.add(new Paragraph("Souhait personnel : "
                    + c_details.getChangeInSelf()));
            document.add(new Paragraph("Objectif de vie : "
                    + c_details.getGoal()));
            document.add(new Paragraph("Raison de l'objectif : "
                    + c_details.getReasonForGoal()));
            document.add(new Paragraph("Obstacle majeur : "
                    + c_details.getBiggestObstacle()));
            document.add(new Paragraph("Surmonter les obstacles : "
                    + c_details.getOvercomingObstacle()));
            document.add(new Paragraph("Plan en cas de succès : "
                    + c_details.getPlanIfSuccessful()));
            document.add(new Paragraph("Plan en cas d'échec : "
                    + c_details.getPlanIfFailed()));
            document.add(new Paragraph("Description de la personne : "
                    + c_details.getSelfDescription()));
            document.add(new Paragraph("Signe distinctif : "
                    + c_details.getDistinctiveTrait()));
            document.add(new Paragraph("Description Physique : "
                    + c_details.getPhysicalDescription()));
            document.add(new Paragraph("Préférence vestimentaire : "
                    + c_details.getClothingPreferences()));
            document.add(new Paragraph("Phobies : "
                    + c_details.getFears()));
            document.add(new Paragraph("Nourriture favorite : "
                    + c_details.getFavoriteFood()));
            document.add(new Paragraph("Loisirs : "
                    + c_details.getHobbies()));
            document.add(new Paragraph("Activités de loisir : "
                    + c_details.getLeisureActivities()));
            document.add(new Paragraph("Compagnons idéaux : "
                    + c_details.getIdealCompany()));
            document.add(new Paragraph("Attitude envers le groupe : "
                    + c_details.getAttitudeTowardsGroups()));
            document.add(new Paragraph("Vision du monde : "
                    + c_details.getAttitudeTowardsWorld()));
            document.add(new Paragraph("Attitude envers les autres : "
                    + c_details.getAttitudeTowardsPeople()));
            document.add(new Paragraph(CRLF));

            // TODO : gestion de character_json_data.json_data
            if (c_json_data != null && c_json_data.getJsonData() != null && !c_json_data.getJsonData().isEmpty()) {
                document.add(new Paragraph("--- Statistiques et Caractéristiques Spécifiques ---"));
                document.add(new Paragraph(CRLF));
                try {
                    // Parse le JSON en une Map pour une itération facile
                    Map<String, Object> jsonDataMap = objectMapper.readValue(c_json_data.getJsonData(), Map.class);
                    addJsonDataToPdf(document, jsonDataMap, 0); // Appelle la méthode récursive
                } catch (JsonProcessingException e) {
                    LOGGER.error("Erreur lors du parsing du JSON des statistiques : " + e.getMessage());
                    document.add(new Paragraph("Impossible d'afficher les statistiques (erreur de format JSON)."));
                }
                document.add(new Paragraph(CRLF));
            }

            // TODO : gestion de character_illustration.image_blob

            document.close();
        } catch (DocumentException e) {
            LOGGER.error(e.getMessage());
        }

        return baos.toByteArray();
    }

    /**
     * Méthode récursive pour ajouter les données JSON au document PDF.
     * Imite la logique d'affichage hiérarchique du frontend.
     */
    private void addJsonDataToPdf(Document document, Map<String, Object> jsonData, int level) throws DocumentException {
        if (jsonData == null || jsonData.isEmpty()) {
            return;
        }

        String indent = "  ".repeat(level); // Indentation pour la hiérarchie

        for (Map.Entry<String, Object> entry : jsonData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                // C'est un objet imbriqué (catégorie)
                document.add(new Paragraph(indent + "➠ " + key.substring(0, 1).toUpperCase() + key.substring(1) + " :"));
                addJsonDataToPdf(document, (Map<String, Object>) value, level + 1); // Appel récursif
            } else if (value instanceof List) {
                // C'est une liste (array)
                document.add(new Paragraph(indent + "➠ " + key.substring(0, 1).toUpperCase() + key.substring(1) + " :"));
                List<?> list = (List<?>) value;
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    if (item instanceof Map) {
                        // Si l'élément de la liste est un objet, affichons ses propriétés
                        document.add(new Paragraph(indent + "  - Élément " + (i + 1) + " :"));
                        addJsonDataToPdf(document, (Map<String, Object>) item, level + 2); // Indentation supplémentaire
                    } else {
                        // Sinon, c'est une valeur simple dans la liste
                        document.add(new Paragraph(indent + "  - " + String.valueOf(item)));
                    }
                }
            } else {
                // C'est une valeur simple (clé-valeur)
                document.add(new Paragraph(indent + key.substring(0, 1).toUpperCase() + key.substring(1) + " : " + String.valueOf(value)));
            }
        }
    }

}