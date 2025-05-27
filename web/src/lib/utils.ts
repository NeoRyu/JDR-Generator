import {type ClassValue, clsx} from "clsx";
import {twMerge} from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Converts an ArrayBuffer to a Base64 string suitable for image display.
 * @param buffer The ArrayBuffer to convert.
 * @returns A Promise that resolves with the Base64 string.
 */
export const arrayBufferToBase64 = (buffer: ArrayBuffer): Promise<string> => {
  return new Promise((resolve, reject) => {
    const blob = new Blob([buffer], { type: 'image/png' });
    const reader = new FileReader();
    reader.onload = () => {
      if (typeof reader.result === 'string') {
        // Supprime le préfixe "data:image/png;base64," que readAsDataURL ajoute
        const base64String = reader.result.split(',')[1];
        resolve(base64String);
      } else {
        reject(new Error("FileReader n'a pas retourné une chaîne de caractères."));
      }
    };
    reader.onerror = () => reject(new Error(reader.error?.message || 'FileReader encountered an unknown error.'));
    reader.readAsDataURL(blob);
  });
};