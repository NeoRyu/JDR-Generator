-- S'assurer que root@localhost a bien le mot de passe 'root'
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';

-- Créer un utilisateur dédié pour l'API (s'il n'existe pas déjà)
CREATE USER IF NOT EXISTS 'jdr_user'@'%' IDENTIFIED BY 'root';

-- Donner tous les privilèges à cet utilisateur sur la base de données 'jdr_generator_db'
GRANT ALL PRIVILEGES ON jdr_generator_db.* TO 'jdr_user'@'%';

-- Recharger les privilèges de MySQL pour que les changements soient pris en compte
FLUSH PRIVILEGES;