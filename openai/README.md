# OpenAI Image Generation Module

## Description
This module is a Node.js/Express/TypeScript application designed to provide a REST API for generating images using the OpenAI DALL-E API. It serves as an alternative image generation backend to the Gemini module, allowing flexibility based on API availability and features.

## Features:
- Provides a `/illustrate` endpoint to generate images from a text prompt using DALL-E.
- Configurable via environment variables (PORT, API_KEY, DOWNLOAD_FOLDER).

## Configuration:

1. **Obtain an OpenAI API Key:**
    - Go to the OpenAI platform: [https://platform.openai.com/](https://platform.openai.com/)
    - Create a new secret key.

2. **Environment Variables:**
    - Create a `.env` file in the root of this module's directory or configure these variables in your Docker Compose file.
    - `PORT`: The port the Express server will listen on (e.g., 3000).
    - `API_KEY`: Your secret OpenAI API key.
    - `DOWNLOAD_FOLDER`: The subfolder name within `/app/downloads/` where generated images will be saved inside the container (should match the configuration in your API service and volume mounts).

3. **Install Dependencies:**
   ```bash
   npm install