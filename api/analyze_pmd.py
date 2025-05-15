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
                message = violation.text
                if message:
                    message = message.replace('\n', ' ').replace('\r', '').strip()

                if priority == '1':
                    logging.debug(f"|  Found violation ["
                                  f"{violation.get('beginline')}:{violation.get('begincolumn')}"
                                  f" to {violation.get('endline')}:{violation.get('endcolumn')}"
                                  f"] into {violation.get('class')}.{violation.get('method')}"
                                  f"\n|  > priority: {priority} / rule: {violation.get('rule')} :: {violation.get('externalInfoUrl')}"
                                  f"\n|  > message: {message}")
                    priority_1_errors.append(f"  Found violation - priority: {priority}, message: {message}, file : {file_element.get('name')}")

        return len(priority_1_errors)

    except ET.ParseError as e:
        logging.error(f"Error parsing XML: {e}")
        sys.exit(1)
    except Exception as e:
        logging.error(f"An unexpected error occurred: {e}")
        sys.exit(1)


if __name__ == "__main__":
    xml_file = "target/pmd.xml"  # Chemin relatif au fichier XML (depuis api/)
    p1_count = analyze_pmd_xml(xml_file)

    # Set GitHub Actions output variables
    if 'GITHUB_ACTIONS' in os.environ:
        print(f"::set-output name=priority1_errors::{p1_count}")