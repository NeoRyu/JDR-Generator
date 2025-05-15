type ImageUrlOrBase64 = string;

export interface CharacterIllustration {
  id: number;
  imageLabel: string;
  imageBlob: ImageUrlOrBase64;
}
