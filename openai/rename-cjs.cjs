const fs = require('node:fs/promises');
const path = require('node:path');

async function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function renameJsToCjsRecursive(dir) {
    try {
        const items = await fs.readdir(dir);
        for (const item of items) {
            const itemPath = path.join(dir, item);
            const stats = await fs.stat(itemPath);

            if (stats.isDirectory()) {
                await renameJsToCjsRecursive(itemPath); // Récursion dans les sous-dossiers
            } else if (item.endsWith('.js')) {
                const newPath = itemPath.replace('.js', '.cjs');
                await fs.rename(itemPath, newPath);
                console.log(`Renamed ${itemPath}`);
                console.log(`-> to ${newPath}`);

            }
        }
    } catch (error) {
        console.error(`Error processing directory ${dir}:`, error);
    }
}

async function renameCjs() {
    const srcDistDir = path.join(__dirname, 'dist', 'src');
    const distDir = path.join(__dirname, 'dist');

    // Délai de 3 secondes (3000 millisecondes)
    await delay(3000);

    // Parcourir récursivement le dossier dist/src et renommer les .js en .cjs
    await renameJsToCjsRecursive(srcDistDir);

    // Renommer app.js en app.cjs dans le dossier dist
    const appJsPath = path.join(distDir, 'app.js');
    const appCjsPath = path.join(distDir, 'app.cjs');
    try {
        await fs.rename(appJsPath, appCjsPath);
        console.log(`Renamed app.js to app.cjs`);
    } catch (error) {
        if (error.code !== 'ENOENT') {
            console.error('Error renaming app.js:', error);
        }
    }
}

renameCjs().catch(error => {
    console.error('Unhandled promise rejection during rename:', error);
    process.exit(1);
});