package jdr.generator.api.characters;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import jdr.generator.api.characters.context.CharacterContextEntity;
import jdr.generator.api.characters.context.CharacterContextService;
import jdr.generator.api.characters.details.CharacterDetailsEntity;
import jdr.generator.api.characters.details.CharacterDetailsService;
import jdr.generator.api.characters.illustration.CharacterIllustrationEntity;
import jdr.generator.api.characters.illustration.CharacterIllustrationService;
import jdr.generator.api.characters.stats.CharacterJsonDataEntity;
import jdr.generator.api.characters.stats.CharacterJsonDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PdfGeneratorServiceTest {

  @Mock private CharacterContextService characterContextService;
  @Mock private CharacterDetailsService characterDetailsService;
  @Mock private CharacterIllustrationService characterIllustrationService;
  @Mock private CharacterJsonDataService characterJsonDataService;

  private ObjectMapper objectMapper;

  @InjectMocks private PdfGeneratorService pdfGeneratorService;

  private CharacterDetailsEntity testCharacterDetails;
  private CharacterContextEntity testCharacterContext;
  private CharacterJsonDataEntity testCharacterJsonData;
  private CharacterIllustrationEntity testCharacterIllustration;

  private Long TEST_CHARACTER_ID = 1L;

  @BeforeEach
  void setUp() throws IOException {
    objectMapper = new ObjectMapper();
    pdfGeneratorService =
        new PdfGeneratorService(
            objectMapper,
            characterContextService,
            characterDetailsService,
            characterIllustrationService,
            characterJsonDataService);

    testCharacterDetails = new CharacterDetailsEntity();
    testCharacterDetails.setId(TEST_CHARACTER_ID);
    testCharacterDetails.setContextId(TEST_CHARACTER_ID);
    testCharacterDetails.setName("Alan de Wylye");
    testCharacterDetails.setAge(20);
    testCharacterDetails.setBirthPlace("Vagon");
    testCharacterDetails.setResidenceLocation("Vagon");
    testCharacterDetails.setReasonForResidence("Chevalier en formation");
    testCharacterDetails.setClimate("Tempéré");
    testCharacterDetails.setCommonProblems("Anarchie ambiante");
    testCharacterDetails.setDailyRoutine("Entraînement, études, patrouilles");
    testCharacterDetails.setParentsAlive(true);
    testCharacterDetails.setDetailsAboutParents("Fils d'un chevalier de petite terre");
    testCharacterDetails.setFeelingsAboutParents("Respect et fierté");
    testCharacterDetails.setSiblings("1");
    testCharacterDetails.setChildhoodStory("Enfance au château, formations précoces");
    testCharacterDetails.setYouthFriends("7 autres écuyers");
    testCharacterDetails.setPet("Cheval");
    testCharacterDetails.setMaritalStatus("Célibataire");
    testCharacterDetails.setTypeOfLover("Noble et spirituelle");
    testCharacterDetails.setConjugalHistory("Aucune");
    testCharacterDetails.setChildren(0);
    testCharacterDetails.setEducation("Chevalerie et lettres");
    testCharacterDetails.setProfession("Écuyer");
    testCharacterDetails.setReasonForProfession("Tradition familiale");
    testCharacterDetails.setWorkPreferences("Action et aventure");
    testCharacterDetails.setChangeInWorld("Rétablir la paix et la justice");
    testCharacterDetails.setChangeInSelf("Maîtriser son impulsivité");
    testCharacterDetails.setGoal("Devenir un chevalier digne");
    testCharacterDetails.setReasonForGoal("Héritage familial et honneur");
    testCharacterDetails.setBiggestObstacle("Son tempérament fier et impulsif");
    testCharacterDetails.setOvercomingObstacle("Discipline et mentorat");
    testCharacterDetails.setPlanIfSuccessful("Servir le roi Arthur");
    testCharacterDetails.setPlanIfFailed("Rejoindre les moines");
    testCharacterDetails.setSelfDescription("Fier, impulsif, loyal");
    testCharacterDetails.setDistinctiveTrait("Cicatrice sur le bras");
    testCharacterDetails.setPhysicalDescription("Jeune homme aux cheveux bruns et yeux bleus");
    testCharacterDetails.setClothingPreferences("Tunique et chausses");
    testCharacterDetails.setFears("Échec et déshonneur");
    testCharacterDetails.setFavoriteFood("Viande rôtie");
    testCharacterDetails.setHobbies("Chasse et joute");
    testCharacterDetails.setLeisureActivities("Musique et lecture");
    testCharacterDetails.setIdealCompany("Chevaliers courageux et dames nobles");
    testCharacterDetails.setAttitudeTowardsGroups(
        "Prend les devants, mais valorise le travail d'équipe");
    testCharacterDetails.setAttitudeTowardsWorld("Mélange d'idéalisme et de cynisme");
    testCharacterDetails.setAttitudeTowardsPeople(
        "Amical avec les respectés, méfiant envers les inconnus");

    testCharacterContext = new CharacterContextEntity();
    testCharacterContext.setId(TEST_CHARACTER_ID);
    testCharacterContext.setPromptGender("Male");
    testCharacterContext.setPromptRace("Human");
    testCharacterContext.setPromptClass("Knight");
    testCharacterContext.setPromptSystem("Pendragon");
    testCharacterContext.setPromptDescription("Jeune écuyer fier et impulsif du château de Vagon.");

    testCharacterJsonData = new CharacterJsonDataEntity();
    testCharacterJsonData.setId(TEST_CHARACTER_ID);
    testCharacterJsonData.setCharacterDetails(testCharacterDetails);
    testCharacterJsonData.setJsonData(
        """
                {
                  "attributes": {
                    "TAI": 12, "DEX": 14, "FOR": 12, "CON": 12,
                    "APP": 10, "PV": 24, "INC": 6, "Dég": 4,
                    "Vit": 3
                  },
                  "skills": {
                    "Art": 3, "Soins": 6, "Chasse": 10, "Combat": 12,
                    "Survie": 8, "Lecture": 5, "Musique": 3,
                    "Religion": 7, "Courtoisie": 8, "Equitation": 14,
                    "Agriculture": 4
                  },
                  "traits": {
                    "Fier": 15, "Impulsif": 15, "Raillerie": 12
                  },
                  "culture": "Bretonne",
                  "virtues": {
                    "Courage": 13, "Justice": 8
                  }
                }
                """);

    testCharacterIllustration = new CharacterIllustrationEntity();
    testCharacterIllustration.setId(TEST_CHARACTER_ID);
    testCharacterIllustration.setCharacterDetails(testCharacterDetails);

    String testImageFileName = "test-image.png";

    try (InputStream is = getClass().getClassLoader().getResourceAsStream(testImageFileName)) {
      if (is != null) {
        byte[] imageBytes = is.readAllBytes();
        testCharacterIllustration.setImageBlob(imageBytes);
        // NOUVEAU: Vérification de la taille du BLOB
        if (imageBytes.length > 0) {
          System.out.println(
              "Image de test '"
                  + testImageFileName
                  + "' chargée. Taille: "
                  + imageBytes.length
                  + " octets.");
        } else {
          System.err.println("L'image '" + testImageFileName + "' est vide ou corrompue");
        }
      } else {
        System.err.println(
            "Fichier image non trouvé: "
                + testImageFileName
                + ". Assurez-vous qu'il est dans 'api/src/test/resources/'.");
        testCharacterIllustration.setImageBlob(null);
      }
    }

    when(characterDetailsService.findById(anyLong())).thenReturn(testCharacterDetails);
    when(characterContextService.findById(anyLong())).thenReturn(testCharacterContext);
    when(characterJsonDataService.findByCharacterDetailsId(anyLong()))
        .thenReturn(Optional.of(testCharacterJsonData));
    when(characterIllustrationService.findByCharacterDetailsId(anyLong()))
        .thenReturn(testCharacterIllustration);
  }

  @Test
  void testGenerateCharacterPdf_Success() throws DocumentException, IOException {
    byte[] pdfBytes = pdfGeneratorService.generateCharacterPdf(TEST_CHARACTER_ID);

    assertNotNull(pdfBytes);

    String testOutputDir = "target/test-output/pdf";
    File outputDir = new File(testOutputDir);
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    File pdfFile =
        new File(outputDir, "generated_character_" + TEST_CHARACTER_ID + "_" + timestamp + ".pdf");

    try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
      fos.write(pdfBytes);
    }
    // Ouvrez ce fichier pour vérifier le PDF manuellement.
    System.out.println("PDF généré à : " + pdfFile.getAbsolutePath());
  }
}
