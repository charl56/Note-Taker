# Application Android de prise de notes avec sauvegarde sur le cloud

Cette application Android permet aux utilisateurs de prendre et sauvegarder des notes sur le cloud. Elle propose différentes fonctionnalités selon que l'utilisateur soit connecté ou non.

## Fonctionnalités

- **Utilisateurs non connectés :**
    - Visualisation uniquement des notes existantes
    - Aucune possibilité de modification des notes

- **Utilisateurs connectés :**
    - Création de compte pour ajouter de nouvelles notes
    - Modification et suppression des notes personnelles uniquement

## Installation

1. **Cloner le projet** :
   ```bash
   git clone https://github.com/charl56/Note-Taker
   ```

2. **Démarrer l'application** :
    - Ouvrir le projet dans Android Studio
    - Exécuter la classe `MainActivity.kt`

## Points à finaliser

- Lors de la déconnexion pour changer de compte, l'ancien compte reste dans les données, empêchant la création d'une nouvelle note avec le nouveau compte.
- Il est possible de supprimer toutes les notes, y compris celles des autres utilisateurs, ce qui nécessite une correction des permissions.

