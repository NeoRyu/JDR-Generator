import xml.etree.ElementTree as ET
import sys

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
            for violation in file_element.findall('pmd:violation', namespace):
                priority = violation.get('priority')
                message = violation.get('message')

                if priority == '1':
                    priority_1_errors.append(f"  - {message}")
                elif priority == '2':
                    priority_2_errors.append(f"  - {message}")
                elif priority == '3':
                    priority_3_errors.append(f"  - {message}")

        print("Priority 1 PMD Errors:")
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
    xml_file = "api/target/pmd.xml"  # Chemin relatif au fichier XML
    p1_count, p2_count, p3_count = analyze_pmd_xml(xml_file)

    # Set GitHub Actions output variables
    print(f"::set-output name=priority1_errors::{p1_count}")
    print(f"::set-output name=priority2_errors::{p2_count}")
    print(f"::set-output name=priority3_errors::{p3_count}")