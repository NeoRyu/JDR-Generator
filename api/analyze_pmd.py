import xml.etree.ElementTree as ET
import sys
import logging
import os  # Import du module os

# Configuration du logging
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')

def analyze_pmd_xml(xml_file_path):
    """
    Analyse un rapport PMD XML et extrait les erreurs de priorité 1 avec leurs messages.

    Args:
        xml_file_path (str): Le chemin du fichier XML du rapport PMD.

    Returns:
        int: Le nombre d'erreurs de priorité 1.
    """
    try:
        tree = ET.parse(xml_file_path)
        root = tree.getroot()

        priority_1_errors = []

        # Namespace handling (PMD XML utilise un namespace)
        namespace = {'pmd': 'http://pmd.sourceforge.net/report/2.0.0'}

        for file_element in root.findall('pmd:file', namespace):
            logging.debug(f"Processing file: {file_element.get('name')}")
            for violation in file_element.findall('pmd:violation', namespace):
                priority = violation.get('priority')

                if priority == '1':
                    message = violation.text
                    if message:
                        message = message.strip()  # Supprime les espaces
                        if message:  # Vérifie après le strip()
                            priority_1_errors.append(
                                f"  - {message} (File: {file_element.get('name')}, Line: {violation.get('beginline')})"
                            )
                        else:
                            logging.warning(
                                f"  Violation with empty message (File: {file_element.get('name')}, Line: {violation.get('beginline')})"
                            )
                            priority_1_errors.append(
                                f"  - No message provided (File: {file_element.get('name')}, Line: {violation.get('beginline')})"
                            )
                    else:
                        logging.warning(
                            f"  Violation with no message (File: {file_element.get('name')}, Line: {violation.get('beginline')})"
                        )
                        priority_1_errors.append(
                            f"  - No message provided (File: {file_element.get('name')}, Line: {violation.get('beginline')})"
                        )

        print("\nPriority 1 PMD Errors:")
        if priority_1_errors:
            for error in priority_1_errors:
                print(error)
        else:
            print("  Aucune erreur de priorité 1 trouvée.")

        return len(priority_1_errors)

    except ET.ParseError as e:
        print(f"Error parsing XML: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        sys.exit(1)


if __name__ == "__main__":
    xml_file = "target/pmd.xml"  # Chemin relatif au fichier XML (depuis api/)
    p1_count = analyze_pmd_xml(xml_file)

    # Set GitHub Actions output variables
    if 'GITHUB_ACTIONS' in os.environ:
        print(f"::set-output name=priority1_errors_count::{p1_count}")

        # Optionnel:  Si tu veux aussi les messages complets en sortie (peut être très long)
        print(f"::set-output name=priority1_errors_messages::{'\\n'.join(priority_1_errors)}")