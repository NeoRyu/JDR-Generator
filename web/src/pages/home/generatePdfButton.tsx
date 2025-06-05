// src/pages/home/generatePdfButton.tsx
import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Loader2, FileDown } from "lucide-react";
import { CharacterFull } from "@/components/model/character-full.model.tsx";
import { useGeneratePdf } from "@/services/generatePdf.service.ts";
import { useToast } from "@/components/hooks/use-toast";

interface GeneratePdfButtonProps {
    character: CharacterFull;
}

export const GeneratePdfButton: React.FC<GeneratePdfButtonProps> = ({ character }) => {
    const { toast } = useToast();
    const generatePdfMutation = useGeneratePdf();
    const [isLoadingPdf, setIsLoadingPdf] = useState(false);

    const handleGeneratePdf = async () => {
        if (!character?.details?.id) {
            console.error("Character ID is missing for PDF generation.");
            toast({
                title: "Erreur",
                description: "ID du personnage manquant pour la génération du PDF.",
                variant: "destructive",
            });
            return;
        }

        setIsLoadingPdf(true);
        try {
            const response = await generatePdfMutation.mutateAsync(character.details.id);
            const blob = new Blob([response.data], { type: "application/pdf" });

            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            const fileName = `JDR-Generator - ${character.details.name || character.details.id}.pdf`;
            link.setAttribute('download', fileName);

            document.body.appendChild(link);
            link.click();

            link.remove();
            window.URL.revokeObjectURL(url);

            toast({
                title: "Succès",
                description: "Le PDF a été généré et téléchargé.",
            });
        } catch (error) {
            console.error("Erreur lors de la génération du PDF:", error);
            toast({
                title: "Erreur",
                description: "Échec de la génération du PDF. Veuillez réessayer.",
                variant: "destructive",
            });
        } finally {
            setIsLoadingPdf(false);
        }
    };

    return (
        <Button
            type="button"
            variant="outline"
            className="button-bubble-aura"
            style={{ color: "#e36804!important"}}
            onClick={handleGeneratePdf}
            disabled={isLoadingPdf}
            title="Générer la fiche PDF du personnage"
        >
            {isLoadingPdf ? (
                <Loader2 className="h-4 w-4 animate-spin" />
            ) : (
                <FileDown />
            )}
        </Button>
    );
};