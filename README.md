# chatLocal

Application de chat en local pour les étudiants d'Ambam : messagerie instantanée et partage de documents scolaires (cours, TD, TP, annales) sur le réseau local de l'établissement, sans dépendre d'Internet.

Ce dépôt contient la **version 1** : comptes utilisateurs, chat privé en temps réel, envoi et téléchargement de documents.

## Organisation du dépôt

```
chatLocal/
├── backend/    API Spring Boot (Java)
└── frontend/   Interface web
```

## Technologies

- Backend : Java, Spring Boot (Web, WebSocket, Security, Data JPA, Validation)
- Base de données : PostgreSQL
- Frontend : application web adaptée aux téléphones

## Lancer le backend

Prérequis : Java 21, PostgreSQL.

1. Créer une base PostgreSQL et un utilisateur pour le projet.
2. Définir les variables d'environnement de connexion à la base (voir la liste ci-dessous).
3. Depuis le dossier `backend/`, lancer l'application avec Maven.

Variables d'environnement : *à compléter*

## Structure du backend

Le code est organisé par couche, dans le package `cm.chatlocal` :

| Package | Rôle |
|---|---|
| `controller` | Routes REST |
| `service` | Logique métier |
| `repository` | Accès aux données (Spring Data) |
| `entity` | Classes JPA, issues du diagramme de classes |
| `dto` | Objets échangés avec le frontend |
| `config` | Configuration (sécurité, WebSocket) |
| `security` | JWT et filtres d'authentification |
| `websocket` | Gestion des sessions de chat |
| `exception` | Erreurs personnalisées et gestionnaire global |

## Règles de code

1. Tout le code reste dans le package `cm.chatlocal` ou ses sous-packages.
2. Un `controller` appelle un `service`, qui appelle un `repository`. Un controller ne parle jamais directement à un repository.
3. On ne renvoie jamais une entité au frontend : on utilise un DTO.
4. Nommage : `UtilisateurService`, `UtilisateurRepository`, `AuthController`.
5. Aucun mot de passe ni clé secrète dans le code ou dans le dépôt.

## Travailler avec Git

- On ne pousse jamais directement sur `main`.
- Une branche par fonctionnalité, nommée `feature/<sujet>` (exemple : `feature/inscription`).
- Messages de commit courts, en français, à l'impératif : `Ajoute l'inscription`.
- Avant de commencer à travailler : `git pull`.
- Une fois la fonctionnalité prête, ouvrir une Pull Request. Harisson la relit avant la fusion.

## Équipe

| Membre | Rôle |
|---|---|
| Harisson | Backend : architecture et messagerie temps réel |
| Mehebou | Frontend |
| Lélie | Backend : comptes et sécurité |
| Loïc | Backend : documents |
| Vidouelle | Backend : base de données |
| Bill | Backend : fiabilité, déploiement et documentation |

Créer la base et l'utilisateur PostgreSQL (une fois par machine) :

    CREATE USER chatlocal WITH PASSWORD 'votre_mot_de_passe';
    CREATE DATABASE chatlocal OWNER chatlocal;

Variable d'environnement obligatoire :

| Variable | Rôle | Valeur par défaut |
|---|---|---|
| `DB_PASSWORD` | Mot de passe de l'utilisateur `chatlocal` | aucune |
| `DB_URL` | URL JDBC de la base | `jdbc:postgresql://localhost:5432/chatlocal` |
| `DB_USER` | Utilisateur de la base | `chatlocal` |