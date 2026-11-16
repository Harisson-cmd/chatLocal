# Contrat d'API – chatLocal V1

Document commun backend / frontend. Toute modification passe par Harisson et est signalée à Mehebou.

## Conventions

- URL de base : `http://<adresse-du-serveur>:8080`
- Format : JSON, encodage UTF-8, dates au format ISO 8601 (`2026-11-16T09:30:00`)
- Authentification : jeton JWT dans l'en-tête `Authorization: Bearer <token>`, sur toutes les routes sauf `/api/auth/*`
- Rôles : `ETUDIANT`, `ENSEIGNANT`, `ADMINISTRATEUR`
- Statut de compte : `ACTIF`, `SUSPENDU`
- Statut de message : `EN_ATTENTE` (côté client, pas encore confirmé par le serveur), `ENVOYE` (enregistré par le serveur)

### Format des erreurs

```json
{ "status": 400, "erreur": "Message lisible en français", "champs": { "identifiant": "obligatoire" } }
```

`champs` n'est présent que pour les erreurs de validation.

| Code | Signification |
|---|---|
| 400 | Données invalides |
| 401 | Jeton absent, invalide ou expiré, ou identifiants incorrects |
| 403 | Compte suspendu, ou accès interdit |
| 404 | Ressource introuvable |
| 409 | Conflit (identifiant déjà utilisé) |
| 413 | Fichier trop volumineux |
| 415 | Type de fichier refusé |

## Authentification

### POST `/api/auth/register`

Requête :
```json
{ "identifiant": "jdupont", "motDePasse": "secret123", "nom": "Jean Dupont", "filiere": "GSI", "niveau": "L3" }
```
Réponse `201` :
```json
{ "id": 1, "identifiant": "jdupont", "nom": "Jean Dupont", "filiere": "GSI", "niveau": "L3", "role": "ETUDIANT" }
```
Erreurs : `400`, `409`.

### POST `/api/auth/login`

Requête :
```json
{ "identifiant": "jdupont", "motDePasse": "secret123" }
```
Réponse `200` :
```json
{ "token": "eyJhbGciOi...", "utilisateur": { "id": 1, "identifiant": "jdupont", "nom": "Jean Dupont", "filiere": "GSI", "niveau": "L3", "role": "ETUDIANT" } }
```
Erreurs : `401` (identifiants incorrects), `403` (compte suspendu).

## Utilisateurs

### GET `/api/users`

Liste des autres utilisateurs actifs, pour démarrer une conversation. Paramètre facultatif : `?q=jean` (recherche sur le nom).

Réponse `200` :
```json
[ { "id": 2, "identifiant": "mkamga", "nom": "Marie Kamga", "filiere": "GSI", "niveau": "L3", "role": "ETUDIANT" } ]
```

### GET `/api/users/me`

Retourne l'utilisateur connecté (même objet que ci-dessus).

## Conversations privées

### POST `/api/conversations`

Crée la conversation avec un utilisateur, ou renvoie celle qui existe déjà.

Requête :
```json
{ "destinataireId": 2 }
```
Réponse `200` (existante) ou `201` (créée) :
```json
{ "id": 10, "dateCreation": "2026-11-16T09:00:00", "interlocuteur": { "id": 2, "nom": "Marie Kamga" }, "dernierMessage": null }
```

### GET `/api/conversations`

Les conversations de l'utilisateur connecté, la plus récente en premier. Même objet, avec `dernierMessage` renseigné (objet message, voir plus bas).

### GET `/api/conversations/{id}/messages?page=0&size=50`

Historique. Les messages d'une page sont triés du **plus récent au plus ancien** ; le frontend les inverse pour l'affichage. `page` commence à 0.

Réponse `200` :
```json
{ "page": 0, "size": 50, "total": 120, "messages": [ { ...message... } ] }
```
Erreurs : `403` si l'utilisateur ne participe pas à la conversation, `404`.

### Objet message

```json
{
  "id": 501,
  "idClient": "b5f1c7a2-5a1e-4c8e-9b0f-3d2c9a1e7f10",
  "conversationId": 10,
  "auteurId": 1,
  "contenu": "Bonjour",
  "dateEnvoi": "2026-11-16T09:30:00",
  "statut": "ENVOYE",
  "document": null
}
```
`document` vaut `null` ou `{ "id": 7, "nomFichier": "TD1.pdf", "typeMime": "application/pdf", "tailleOctets": 120345 }`.

## Documents

### POST `/api/documents`

Dépôt d'un fichier. Requête `multipart/form-data` avec un champ `fichier`.

Réponse `201` :
```json
{ "id": 7, "nomFichier": "TD1.pdf", "typeMime": "application/pdf", "tailleOctets": 120345, "dateDepot": "2026-11-16T09:20:00", "auteurId": 1 }
```
Limites proposées (à confirmer) : 20 Mo par fichier ; types acceptés : PDF, DOC, DOCX, PPT, PPTX, XLS, XLSX, TXT, JPG, PNG. Erreurs : `413`, `415`.

### GET `/api/documents?page=0&size=20`

Bibliothèque commune, du plus récent au plus ancien.
```json
{ "page": 0, "size": 20, "total": 35, "documents": [ { ...document... } ] }
```

### GET `/api/documents/{id}/download`

Retourne le fichier (flux binaire) avec `Content-Type` et `Content-Disposition: attachment; filename="..."`. Un lien `<a>` ne peut pas envoyer l'en-tête `Authorization` : le frontend télécharge avec `fetch`, récupère un `Blob`, puis déclenche le téléchargement.

Pour envoyer un document dans une conversation : déposer le fichier (`POST /api/documents`), puis envoyer un message avec son `documentId`.

## Temps réel (WebSocket)

- Adresse : `ws://<adresse-du-serveur>:8080/ws?token=<jwt>`
- WebSocket natif avec des messages JSON (pas de STOMP, pas de bibliothèque côté frontend). Le jeton passe dans l'URL car le navigateur ne permet pas d'ajouter d'en-tête.
- Connexion refusée si le jeton est invalide ou le compte suspendu.

### Client vers serveur : envoyer un message

```json
{ "type": "MESSAGE", "idClient": "b5f1c7a2-5a1e-4c8e-9b0f-3d2c9a1e7f10", "conversationId": 10, "contenu": "Bonjour", "documentId": null }
```
- `idClient` : UUID généré par le client, **obligatoire**. Si le même `idClient` est renvoyé (après une coupure), le serveur ne crée pas de doublon et renvoie le même `ACK`.
- `documentId` : facultatif.

### Serveur vers client

Confirmation à l'expéditeur, une fois le message enregistré :
```json
{ "type": "ACK", "idClient": "b5f1c7a2-...", "id": 501, "dateEnvoi": "2026-11-16T09:30:00", "statut": "ENVOYE" }
```

Nouveau message pour le destinataire (et pour les autres appareils de l'expéditeur) :
```json
{ "type": "MESSAGE", "message": { ...objet message... } }
```

Erreur sur un envoi :
```json
{ "type": "ERREUR", "idClient": "b5f1c7a2-...", "message": "Conversation introuvable" }
```

### Reconnexion

1. À la connexion, le serveur envoie les messages reçus pendant que l'utilisateur était hors ligne, un par un, au format `MESSAGE`.
2. Le client renvoie ensuite les messages restés `EN_ATTENTE` dans sa file locale, avec leur `idClient` d'origine.
3. Le client ignore tout `MESSAGE` dont l'`id` est déjà affiché.
4. Reconnexion automatique recommandée avec un délai croissant (1 s, 2 s, 4 s, jusqu'à 30 s).

## Hors V1

Groupes, matières et classement des documents, statuts `RECU` et `LU`, présence en ligne, modération, photo de profil, gestion des comptes par l'administrateur.
