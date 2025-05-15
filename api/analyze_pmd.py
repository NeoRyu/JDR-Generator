import xml.etree.ElementTree as ET
import sys
import logging
import os # Import os module to use environ

# Configuration du logging
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')

def analyze_pmd_xml(xml_file_path):
    try:
        tree = ET.parse(xml_file_path)
        root = tree.getroot()

        priority_1_errors = []

        # Namespace handling (PMD XML uses a namespace)
        namespace = {'pmd': 'http://pmd.sourceforge.net/report/2.0.0'}

        for file_element in root.findall('pmd:file', namespace):
            logging.debug(f"Processing file: {file_element.get('name')}")
            for violation in file_element.findall('pmd:violation', namespace):
                priority = violation.get('priority')
                message = violation.text  # Correction : Utiliser .text pour obtenir le texte de l'élément

                logging.debug(f"  Found violation - priority: {priority}, message: {message}")

                if priority == '1':  # Traiter uniquement les erreurs de priorité 1
                    if message:
                        priority_1_errors.append(f"  - {message} (File: {file_element.get('name')}, Line: {violation.get('beginline')})")
                    else:
                        priority_1_errors.append(f"  - No message provided (File: {file_element.get('name')}, Line: {violation.get('beginline')})")

        logging.debug("\nPriority 1 PMD Errors:")
        if priority_1_errors: #check if the list is empty
            for error in priority_1_errors:
                logging.debug(error)
        else:
            logging.debug("  No Priority 1 PMD errors found")
        return len(priority_1_errors)

    except ET.ParseError as e:
        logging.error(f"Error parsing XML: {e}")
        sys.exit(1)
    except Exception as e:
        logging.error(f"An unexpected error occurred: {e}")
        logging.error(f"An unexpected error occurred: {e}")
        sys.exit(1)


if __name__ == "__main__":
    xml_file = "target/pmd.xml"  # Chemin relatif au fichier XML (depuis api/)
    p1_count = analyze_pmd_xml(xml_file)

    # Set GitHub Actions output variables
    if 'GITHUB_ACTIONS' in os.environ:
        print(f"::set-output name=priority1_errors::{p1_count}")