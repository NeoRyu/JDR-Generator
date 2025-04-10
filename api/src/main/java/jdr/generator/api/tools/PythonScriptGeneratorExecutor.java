package jdr.generator.api.tools;

import org.python.util.PythonInterpreter;

import java.io.FileWriter;
import java.io.IOException;

public class PythonScriptGeneratorExecutor {

    public static void main(String[] args) {
        String pythonCode = """
            print "Hello from Python!"
        """;

        String pythonFilePath = "C:\\Temp\\generated_script.py";

        try (FileWriter fileWriter = new FileWriter(pythonFilePath, false)) {
            fileWriter.write(pythonCode);
            System.out.println("Fichier Python généré avec succès : " + pythonFilePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier Python : " + e.getMessage());
            return;
        }

        // Exécuter le script Python généré
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.execfile(pythonFilePath);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution du script Python : " + e.getMessage());
        }
    }

}