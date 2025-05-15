import xml.etree.ElementTree as ET
import sys

def analyze_pmd_report(xml_file_path):
    """
    Analyse un rapport PMD XML pour compter les erreurs par priorité.

    Args:
        xml_file_path (str): Le chemin du fichier XML du rapport PMD.

    Returns:
        dict: Un dictionnaire contenant le nombre d'erreurs pour chaque priorité.
    """

    try:
        tree = ET.parse(xml_file_path)
        root = tree.getroot()
    except ET.ParseError as e:
        print(f"Erreur lors de l'analyse du fichier XML : {e}")
        sys.exit(1)
    except FileNotFoundError:
        print(f"Erreur : Le fichier {xml_file_path} n'a pas été trouvé.")
        sys.exit(1)

    priority1_count = 0
    priority2_count = 0
    priority3_count = 0

    for file_element in root.findall(".//file"):
        for violation_element in file_element.findall(".//violation"):
            priority = violation_element.get("priority")
            if priority == "1":
                priority1_count += 1
            elif priority == "2":
                priority2_count += 1
            elif priority == "3":
                priority3_count += 1

    return {
        "priority1": priority1_count,
        "priority2": priority2_count,
        "priority3": priority3_count
    }

if __name__ == "__main__":
    xml_file = "target/pmd.xml"  # Chemin par défaut, assure-toi qu'il est correct

    results = analyze_pmd_report(xml_file)

    print(f"Nombre d'erreurs Priorité 1: {results['priority1']}")
    print(f"Nombre d'erreurs Priorité 2: {results['priority2']}")
    print(f"Nombre d'erreurs Priorité 3: {results['priority3']}")

    # Formatte les sorties pour GitHub Actions (si nécessaire)
    # Note :  à adapter selon le contexte d'exécution
    if 'GITHUB_ACTIONS' in os.environ:
        print(f"::set-output name=priority1_errors::{results['priority1']}")
        print(f"::set-output name=priority2_errors::{results['priority2']}")
        print(f"::set-output name=priority3_errors::{results['priority3']}")