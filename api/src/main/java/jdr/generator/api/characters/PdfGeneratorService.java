package jdr.generator.api.characters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private final String CRLF = "\n"; // Saut de ligne
    private final Font infoError = FontFactory.getFont(FontFactory.HELVETICA, 16, Color.RED);

    private final CharacterContextService characterContextService;
    private final CharacterDetailsService characterDetailsService;
    private final CharacterIllustrationService characterIllustrationService;
    private final CharacterJsonDataService characterJsonDataService;

    private static class BackgroundPageEvent extends PdfPageEventHelper {
        private Image backgroundImage;

        // Le constructeur prend l'image à utiliser comme arrière-plan
        public BackgroundPageEvent(Image image) {
            this.backgroundImage = image;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                // Obtenir un calque de contenu sous le texte et les autres éléments
                PdfContentByte canvas = writer.getDirectContentUnder();
                // Redimensionne l'image pour qu'elle couvre toute la page
                backgroundImage.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());
                // Positionne l'image en bas à gauche de la page (0,0)
                backgroundImage.setAbsolutePosition(0, 0);
                // Ajoute l'image au canevas
                canvas.addImage(backgroundImage);
            } catch (DocumentException e) {
                // Log l'erreur si l'image ne peut pas être ajoutée
                LOGGER.error("Erreur lors de l'ajout de l'image de fond au PDF: " + e.getMessage());
            }
        }
    }

    private static class CharacterPdfPageEvent extends PdfPageEventHelper {
        private Image backgroundImage;
        private CharacterDetailsEntity characterDetails;
        private CharacterContextEntity characterContext; // Pour l'univers
        private PdfTemplate totalPagesTemplate; // Pour le nombre total de pages

        public CharacterPdfPageEvent(Image image, CharacterDetailsEntity details, CharacterContextEntity context) {
            this.backgroundImage = image;
            this.characterDetails = details;
            this.characterContext = context;
        }

        // Méthode est appelée quand le document est fermé, c'est le moment de remplir le PdfTemplate
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            if (totalPagesTemplate != null) {
                try {
                    BaseFont bf = FontFactory.getFont(FontFactory.HELVETICA,
                            10, Color.BLACK).getBaseFont();
                    totalPagesTemplate.beginText();
                    totalPagesTemplate.setFontAndSize(bf, 10);
                    totalPagesTemplate.showText(String.valueOf(writer.getPageNumber() - 1));
                    totalPagesTemplate.endText();
                } catch (DocumentException e) {
                    LOGGER.error("Erreur lors du remplissage du PdfTemplate " +
                            "pour le nombre total de pages: " + e.getMessage());
                }
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder(); // Pour le fond
            PdfContentByte directContent = writer.getDirectContent(); // (en-tête/pied de page)

            // --- 1. Dessin de l'arrière-plan (comme avant) ---
            try {
                if (backgroundImage != null) {
                    backgroundImage.scaleAbsolute(document.getPageSize().getWidth(),
                            document.getPageSize().getHeight());
                    backgroundImage.setAbsolutePosition(0, 0);
                    canvas.addImage(backgroundImage);
                }
            } catch (DocumentException e) {
                LOGGER.error("Erreur lors de l'ajout de l'image de fond au PDF: " + e.getMessage());
            }

            // --- 2. En-tête (à partir de la page 2) ---
            if (
                    writer.getPageNumber() > 1 &&
                    characterDetails != null &&
                    characterContext != null
            ) {
                String headerText = characterDetails.getName()
                        + " / " + characterContext.getPromptSystem();
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,
                        10, Color.BLACK);
                // Calcul de la position : en haut, centré
                float x = document.getPageSize().getWidth() / 2;
                float y = document.getPageSize().getTop() - 20; // 20 points depuis le haut
                ColumnText.showTextAligned(directContent, Element.ALIGN_CENTER,
                        new Phrase(headerText, headerFont), x, y, 0);
            }

            // --- 3. Pied de page (numérotation "Page X sur Y") ---
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            String pageNumText = "[JDR-Generator] Page " + (writer.getPageNumber()) + " sur ";

            float x = document.getPageSize().getWidth() / 2;
            float y = document.getPageSize().getBottom() + 15;

            directContent.beginText();
            directContent.setFontAndSize(footerFont.getBaseFont(), footerFont.getSize());

            // Calcul de la largeur du texte "Page X sur "
            float pageNumTextWidth = footerFont.getBaseFont().getWidthPoint(pageNumText, footerFont.getSize());

            // Calcul de la largeur estimée du nombre total de pages (pour centrage global)
            // Une estimation plus généreuse pour éviter les problèmes de superposition si le nombre est grand
            float estimatedTotalPagesWidth = footerFont.getBaseFont().getWidthPoint("999", footerFont.getSize()); // Estime pour un nombre à 3 chiffres
            float totalFooterWidth = pageNumTextWidth + estimatedTotalPagesWidth;

            // Position initiale ajustée pour le centrage global du pied de page
            directContent.setTextMatrix(x - (totalFooterWidth / 2), y);

            directContent.showText(pageNumText);

            if (totalPagesTemplate == null) {
                // Créez le template une seule fois. Sa taille doit être suffisante pour le nombre max de pages.
                // La taille du template doit être suffisante pour contenir le texte du nombre total de pages.
                // 50x10 est une bonne taille générale pour 3-4 chiffres.
                totalPagesTemplate = writer.getDirectContent().createTemplate(50, 10);
            }

            // Place le template après le texte "Page X sur "
            float xTotalPages = directContent.getXTLM() + pageNumTextWidth;
            directContent.addTemplate(totalPagesTemplate, xTotalPages, y);

            directContent.endText();
        }
    }

    private String getIndent(final int level) {
        String indent = " ".repeat(level);
        switch (level) {
            case 0: indent += ""; break;
            case 1: indent += "• "; break;
            case 2: indent += " - "; break;
            default: indent += "  ◦ "; break;
        }
        return indent;
    }

    private Paragraph context (
            final String label,
            final String text,
            final float fontSize
    ) {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(5f);
        p.setLeading(20f);
        p.add(" ");
        p.add(contextContentLabel(label, fontSize));
        p.add(" : ");
        p.add(contextContent(text, fontSize));
        return p;
    }
    private Phrase contextContentLabel(final String label, final float fontSize) {
        final Font underlinedLabelFont = FontFactory.getFont(FontFactory.COURIER_BOLD,
                fontSize, Font.UNDERLINE, Color.BLACK);
        if (label == null || label.isEmpty()) {
            return new Phrase("", underlinedLabelFont);
        }
        return new Phrase(label.substring(0, 1).toUpperCase()
                + label.substring(1), underlinedLabelFont);
    }
    private Phrase contextContent(final String text, final float fontSize) {
        final Font infoFont = FontFactory.getFont(FontFactory.COURIER_BOLD,
                fontSize, Color.BLACK);
        if (text == null || text.isEmpty()) {
            return new Phrase("", infoFont);
        }
        return new Phrase(text.substring(0, 1).toUpperCase()
                + text.substring(1), infoFont);
    }

    private Paragraph paragraph (
            final int level,
            final String label,
            final String text,
            final float fontSize
    ) {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(5f);
        p.setLeading(20f);
        p.add(getIndent(level));
        p.add(paragraphContentLabel(label, fontSize));
        p.add(" : ");
        p.add(paragraphContent(text, fontSize));
        return p;
    }
    private Phrase paragraphContentLabel(final String label, final float fontSize) {
        final Font underlinedLabelFont = FontFactory.getFont(FontFactory.COURIER_BOLD,
                fontSize, Font.UNDERLINE, Color.BLACK);
        if (label == null || label.isEmpty()) {
            return new Phrase("", underlinedLabelFont);
        }
        return new Phrase(label.substring(0, 1).toUpperCase()
                + label.substring(1), underlinedLabelFont);
    }
    private Phrase paragraphContent(final String text, final float fontSize) {
        final Font infoFont = FontFactory.getFont(FontFactory.COURIER_OBLIQUE,
                fontSize, Color.BLACK);
        if (text == null || text.isEmpty()) {
            return new Phrase("", infoFont);
        }
        return new Phrase(text.substring(0, 1).toUpperCase()
                + text.substring(1), infoFont);
    }

    private Phrase category(final int level, final String label, final float fontSize) {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(5f);
        p.setLeading(20f);
        if (label != null || !label.isEmpty()) {
            p.add(getIndent(level));
            final Font underlinedLabelFont = FontFactory.getFont(FontFactory.COURIER_BOLD,
                    fontSize, Font.UNDERLINE, Color.BLACK);
            p.add(new Phrase(label.substring(0, 1).toUpperCase()
                    + label.substring(1), underlinedLabelFont));
        }
        p.add(" : ");
        return p;
    }
    private float calcFontSize(final float fontSize, final int level) {
        return fontSize - (level * 2);
    }

    public byte[] generateCharacterPdf(Long characterId) throws DocumentException {
        // RECUPERATION DES DONNEES
        CharacterDetailsEntity c_details =
                this.characterDetailsService.findById(characterId);
        CharacterContextEntity c_context =
                this.characterContextService.findById(c_details.getContextId());
        CharacterIllustrationEntity c_illustration = characterIllustrationService
                .findByCharacterDetailsId(characterId);
        CharacterJsonDataEntity c_json_data = characterJsonDataService
                        .findByCharacterDetailsId(characterId)
                        .orElse(null);

        // -- DEBUT DE CREATION DU DOCUMENT PDF --
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            // AJOUT DE L'IMAGE DE FOND
            Image parchmentImage = null;
            try {
                // Charge l'image "pdf_background.jpg" depuis les ressources
                final String backgroundImgName = "pdf-background.jpg";
                InputStream imageStream = getClass().getClassLoader()
                        .getResourceAsStream(backgroundImgName);
                if (imageStream != null) {
                    parchmentImage = Image.getInstance(imageStream.readAllBytes());
                    // On définie l'événement de page AVANT d'ouvrir le document
                    writer.setPageEvent(new BackgroundPageEvent(parchmentImage));
                } else {
                    LOGGER.warn("Image de fond '" + backgroundImgName + "' non trouvée dans " +
                            "les ressources. Le PDF sera généré sans fond.");
                }
            } catch (IOException e) {
                LOGGER.error("Erreur lors du chargement de l'image de fond: " + e.getMessage());
            }

            // Instanciation de l'event HEADER / FOOTER
            CharacterPdfPageEvent event = new CharacterPdfPageEvent(
                    parchmentImage, c_details, c_context);
            writer.setPageEvent(event);

            document.open();

            // Création d'une table à deux colonnes (60% / 40%)
            PdfPTable headerTable = new PdfPTable(new float[]{60f, 40f});
            headerTable.setWidthPercentage(100); // La table prend 100% de la largeur de la page
            headerTable.setSpacingAfter(10); // Espace après la table pour le contenu suivant

            // --- Cellule de gauche (Texte) ---
            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(PdfPCell.NO_BORDER);
            textCell.setVerticalAlignment(Element.ALIGN_TOP);

            // NOM DU PERSONNAGE
            Paragraph title = new Paragraph(c_details.getName().toUpperCase(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.BLACK));
            title.setAlignment(Element.ALIGN_CENTER);
            textCell.addElement(title);
            textCell.addElement(new Paragraph(CRLF));
            textCell.addElement(new Paragraph(CRLF));

            // INFOS PRINCIPALES (CONTEXTE + AGE)
            textCell.addElement(context("Univers",
                    c_context.getPromptSystem(), 14));
            textCell.addElement(new Paragraph(CRLF));
            textCell.addElement(context("Classe",
                    c_context.getPromptClass(), 14));
            textCell.addElement(context("Race",
                    c_context.getPromptRace(), 14));
            textCell.addElement(context("Sexe",
                    c_context.getPromptGender(), 14));
            textCell.addElement(context("Age",
                    String.valueOf(c_details.getAge()), 14));
            textCell.addElement(new Paragraph(CRLF));

            // Ajout de la cellule de texte à la table
            headerTable.addCell(textCell);

            // --- Cellule de droite (Image) ---
            PdfPCell imageCell = new PdfPCell();
            imageCell.setBorder(PdfPCell.NO_BORDER);
            imageCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            imageCell.setVerticalAlignment(Element.ALIGN_TOP);

            // Ajout du portrait du personnage
            try {
                if (c_illustration != null && c_illustration.getImageBlob() != null
                        && c_illustration.getImageBlob().length > 0 ) {
                    Image characterImage = Image.getInstance(c_illustration.getImageBlob());
                    // Ajuster la taille de l'image pour qu'elle tienne sur la page
                    float documentWidth = document.getPageSize().getWidth() / 10 * 4
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
                    imageCell.addElement(characterImage);
                } else {
                    imageCell.addElement(new Paragraph(
                            "Aucun portrait n'a été généré...", infoError));
                }
            } catch (IOException e) {
                LOGGER.error("Impossible d'ajouter l'image au PDF: " + e.getMessage());
                imageCell.addElement(new Paragraph(
                        "ERREUR : Portrait non disponible.", infoError));
            }

            // Ajout de la cellule d'image à la table
            headerTable.addCell(imageCell);
            // Ajout de la table d'en-tête au document
            document.add(headerTable);

            // CONTEXTE : DESCRIPTION
            document.add(paragraph(0, "Description",
                    c_context.getPromptDescription(), 12));
            document.newPage();

            // DETAILS
            document.add(paragraph(0, "Lieu de naissance", 
                    c_details.getBirthPlace(), 9));
            document.add(paragraph(0, "Lieu de résidence", 
                    c_details.getResidenceLocation(), 9));
            document.add(paragraph(0, "Raison de la résidence", 
                    c_details.getReasonForResidence(), 9));
            document.add(paragraph(0, "Climat", 
                    c_details.getClimate(), 9));
            document.add(paragraph(0, "Problèmes communs", 
                    c_details.getCommonProblems(), 9));
            document.add(paragraph(0, "Routine journalière", 
                    c_details.getDailyRoutine(), 9));
            document.add(paragraph(0, "Parents en vie ?", 
                    c_details.getParentsAlive() ? "Oui" : "Non", 12));
            document.add(paragraph(0, "Détails sur les parents", 
                    c_details.getDetailsAboutParents(), 9));
            document.add(paragraph(0, "Sentiments par rapport aux parents", 
                    c_details.getFeelingsAboutParents(), 9));
            document.add(paragraph(0, "Fraternité ?", 
                    c_details.getSiblings(), 9));
            document.add(paragraph(0, "Enfance du personnage", 
                    c_details.getChildhoodStory(), 9));
            document.add(paragraph(0, "Amis d'enfance", 
                    c_details.getYouthFriends(), 9));
            document.add(paragraph(0, "Animal de compagnie", 
                    c_details.getPet(), 9));
            document.add(paragraph(0, "État matrimonial", 
                    c_details.getMaritalStatus(), 9));
            document.add(paragraph(0, "Type de conjoint",
                    c_details.getTypeOfLover(), 9));
            document.add(paragraph(0, "Histoire conjugale",
                    c_details.getConjugalHistory(), 9));
            document.add(paragraph(0, "Nombre d'enfants",
                    c_details.getChildren().toString(), 9));
            document.add(paragraph(0, "Education",
                    c_details.getEducation(), 9));
            document.add(paragraph(0, "Profession",
                    c_details.getProfession(), 9));
            document.add(paragraph(0, "Raison de la profession",
                    c_details.getReasonForProfession(), 9));
            document.add(paragraph(0, "Préférences de travail",
                    c_details.getWorkPreferences(), 9));
            document.add(paragraph(0, "Changement souhaité dans le monde",
                    c_details.getChangeInWorld(), 9));
            document.add(paragraph(0, "Souhait personnel",
                    c_details.getChangeInSelf(), 9));
            document.add(paragraph(0, "Objectif de vie",
                    c_details.getGoal(), 9));
            document.add(paragraph(0, "Raison de l'objectif",
                    c_details.getReasonForGoal(), 9));
            document.add(paragraph(0, "Obstacle majeur",
                    c_details.getBiggestObstacle(), 9));
            document.add(paragraph(0, "Surmonter les obstacles",
                    c_details.getOvercomingObstacle(), 9));
            document.add(paragraph(0, "Plan en cas de succès",
                    c_details.getPlanIfSuccessful(), 9));
            document.add(paragraph(0, "Plan en cas d'échec",
                    c_details.getPlanIfFailed(), 9));
            document.add(paragraph(0, "Description de la personne",
                    c_details.getSelfDescription(), 9));
            document.add(paragraph(0, "Signe distinctif",
                    c_details.getDistinctiveTrait(), 9));
            document.add(paragraph(0, "Description Physique",
                    c_details.getPhysicalDescription(), 9));
            document.add(paragraph(0, "Préférence vestimentaire",
                    c_details.getClothingPreferences(), 9));
            document.add(paragraph(0, "Phobies",
                    c_details.getFears(), 9));
            document.add(paragraph(0, "Nourriture favorite",
                    c_details.getFavoriteFood(), 9));
            document.add(paragraph(0, "Loisirs",
                    c_details.getHobbies(), 9));
            document.add(paragraph(0, "Activités de loisir",
                    c_details.getLeisureActivities(), 9));
            document.add(paragraph(0, "Compagnons idéaux",
                    c_details.getIdealCompany(), 9));
            document.add(paragraph(0, "Attitude envers le groupe",
                    c_details.getAttitudeTowardsGroups(), 9));
            document.add(paragraph(0, "Vision du monde",
                    c_details.getAttitudeTowardsWorld(), 9));
            document.add(paragraph(0, "Attitude envers les autres",
                    c_details.getAttitudeTowardsPeople(), 9));

            // STATISTIQUES
            if (
                    c_json_data != null &&
                    c_json_data.getJsonData() != null &&
                    !c_json_data.getJsonData().isEmpty()
            ) {
                document.newPage();
                document.add(new Paragraph(
                        "--- Statistiques et Caractéristiques Spécifiques ---"));
                document.add(new Paragraph(CRLF));
                try {
                    // Parse le JSON en une Map pour une itération facile
                    Map<String, Object> jsonDataMap =
                            objectMapper.readValue(c_json_data.getJsonData(), Map.class);
                    addJsonDataToPdf(document, jsonDataMap, 0); // Méthode récursive
                } catch (JsonProcessingException e) {
                    LOGGER.error("Erreur lors du parsing du JSON des statistiques"
                            + e.getMessage());
                    document.add(new Paragraph("Format du JSON incorrect", infoError));
                }
            }

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
    private void addJsonDataToPdf(
            Document document,
            Map<String, Object> jsonData,
            int level
    ) throws DocumentException {
        if (jsonData == null || jsonData.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : jsonData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof ArrayList<?>) {
                // C'est une liste (array)
                if (key != null || !key.isEmpty()) {
                    document.add(category(level, key.substring(0, 1).toUpperCase()
                            + key.substring(1), calcFontSize(14, level)));
                }
                List<?> list = (List<?>) value;
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    if (item instanceof Map) {
                        // Si l'élément de la liste est un objet, affichons ses propriétés
                        document.add(new Paragraph(CRLF));
                        document.add(category(level, "→ " + (i + 1), 14));
                        addJsonDataToPdf(document, (Map<String, Object>) item, level + 2);
                    } else {
                        // Sinon, c'est une valeur simple dans la liste
                        document.add(paragraph(level, String.valueOf(item),
                                "", calcFontSize(14, level)));
                    }
                }
            } else if (value instanceof Map) {
                // C'est un objet imbriqué (catégorie)
                if (key != null || !key.isEmpty()) {
                    document.add(category(level,
                            key.substring(0, 1).toUpperCase() + key.substring(1) + " :",
                            calcFontSize(14, level)));
                }
                // Appel récursif
                addJsonDataToPdf(document, (Map<String, Object>) value, level + 1);
            } else {
                // C'est une valeur simple (clé-valeur)
                document.add(paragraph(level, key.substring(0, 1).toUpperCase()
                        + key.substring(1), String.valueOf(value),
                        calcFontSize(14, level)));
            }
        }
    }

}

