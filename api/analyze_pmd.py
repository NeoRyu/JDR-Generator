import xml.etree.ElementTree as ET
import sys
import logging

# Configuration du logging
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')

def analyze_pmd_xml(xml_file_path):
    try:
        tree = ET.parse(xml_file_path)
        root = tree.getroot()

        priority_1_errors = []
        priority_2_errors = []
        priority_3_errors = []

        # Namespace handling (PMD XML uses a namespace)
        namespace = {'pmd': 'http://pmd.sourceforge.net/report/2.0.0'}

        for file_element in root.findall('pmd:file', namespace):
            logging.debug(f"Processing file: {file_element.get('name')}")
            for violation in file_element.findall('pmd:violation', namespace):
                priority = violation.get('priority')
                message = violation.get('message')

                logging.debug(f"  Found violation - priority: {priority}, message: {message}")

                if priority == '1' and message:  # Vérifier que le message n'est pas None
                    priority_1_errors.append(f"  - {message}")
                elif priority == '2' and message:
                    priority_2_errors.append(f"  - {message}")
                elif priority == '3' and message:
                    priority_3_errors.append(f"  - {message}")

        print("\nPriority 1 PMD Errors:")
        for error in priority_1_errors:
            print(error)

        print("\nPriority 2 PMD Errors:")
        for error in priority_2_errors:
            print(error)

        print("\nPriority 3 PMD Errors:")
        for error in priority_3_errors:
            print(error)

        return len(priority_1_errors), len(priority_2_errors), len(priority_3_errors)

    except ET.ParseError as e:
        print(f"Error parsing XML: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        print(f"An unexpected error occurred: {e}")
        sys.exit(1)


if __name__ == "__main__":
    xml_file = "target/pmd.xml"  # Chemin relatif au fichier XML (depuis api/)
    p1_count, p2_count, p3_count = analyze_pmd_xml(xml_file)

    # Set GitHub Actions output variables
    print(f"::set-output name=priority1_errors::{p1_count}")
    print(f"::set-output name=priority2_errors::{p2_count}")
    print(f"::set-output name=priority3_errors::{p3_count}")import xml.etree.ElementTree as ET
import sys
import logging

# Configuration du logging
logging.basicConfig(level=logging.DEBUG, format='%(asctime)s - %(levelname)s - %(message)s')

def analyze_pmd_xml(xml_file_path):
    try:
        tree = ET.parse(xml_file_path)
        root = tree.getroot()

        priority_1_errors = []
        priority_2_errors = []
        priority_3_errors = []

        # Namespace handling (PMD XML uses a namespace)
        namespace = {'pmd': 'http://pmd.sourceforge.net/report/2.0.0'}

        for file_element in root.findall('pmd:file', namespace):
            logging.debug(f"Processing file: {file_element.get('name')}")
            for violation in file_element.findall('pmd:violation', namespace):
                priority = violation.get('priority')
                message = violation.text.strip() # Extraire le texte entre les balises

                logging.debug(f"  Found violation - priority: {priority}, message: {message}")

                if priority == '1' and message:  # Vérifier que le message n'est pas vide
                    priority_1_errors.append(f"  - {message}")
                elif priority == '2' and message:
                    priority_2_errors.append(f"  - {message}")
                elif priority == '3' and message:
                    priority_3_errors.append(f"  - {message}")

        print("\nPriority 1 PMD Errors:")
        for error in priority_1_errors:
            print(error)

        print("\nPriority 2 PMD Errors:")
        for error in priority_2_errors:
            print(error)

        print("\nPriority 3 PMD Errors:")
        for error in priority_3_errors:
            print(error)

        return len(priority_1_errors), len(priority_2_errors), len(priority_3_errors)

    except ET.ParseError as e:
        print(f"Error parsing XML: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        sys.exit(1)


if __name__ == "__main__":
    xml_file = "target/pmd.xml"  # Chemin relatif au fichier XML (depuis api/)
    p1_count, p2_count, p3_count = analyze_pmd_xml(xml_file)

    # Set GitHub Actions output variables
    print(f"::set-output name=priority1_errors::{p1_count}")
    print(f"::set-output name=priority2_errors::{p2_count}")
    print(f"::set-output name=priority3_errors::{p3_count}")