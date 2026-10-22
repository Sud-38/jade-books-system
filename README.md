# 📚 JADE Books System — Système multi-agents de bibliothèque

Système multi-agents développé avec la plateforme [JADE](https://jade.tilab.com/) qui simule une bibliothèque :
un **catalogue** vérifie la disponibilité des livres, un agent de **prêt** gère les emprunts, et des agents **lecteurs** demandent des livres.

Compilation et lancement **sans Maven** — Java + `jade.jar` uniquement.

---

## 🏗️ Architecture

| Agent | Rôle | Service DF |
|---|---|---|
| `CatalogueAgent` | Vérifie si un livre est au catalogue | `catalogue` |
| `PretAgent` | Accorde (ou refuse) le prêt d'un livre | `pret` |
| `LecteurAgent` | Demande un livre, puis son prêt si disponible | — |

### Flux de messages

```
Lecteur ──REQUEST(livre)──────────────▶ Catalogue
Lecteur ◀──INFORM(DISPONIBLE / INDISPONIBLE)── Catalogue
Lecteur ──REQUEST(livre)──────────────▶ Pret
Lecteur ◀──INFORM(PRET_OK / PRET_REFUSE)──── Pret
```

---

## 📦 Prérequis

- **JDK 8 ou plus récent** (`java -version` et `javac -version`)
- `lib/jade.jar` déjà présent dans le dépôt (fourni)

---

## 📂 Structure du projet

```
jade-books-system/
├── lib/
│   └── jade.jar
├── src/
│   ├── Agents/
│   │   ├── CatalogueAgent.java
│   │   ├── LecteurAgent.java
│   │   └── PretAgent.java
│   └── containers/
│       ├── Maincontainer.java
│       ├── CatalogueContainer.java
│       ├── PretContainer.java
│       └── MultiLecteurContainer.java
└── README.md
```

---

## 🚀 Compilation

### Linux / macOS

```bash
mkdir -p build
find src -name "*.java" > sources.txt
javac -cp "lib/jade.jar" -d build @sources.txt
```

### Windows (cmd)

```cmd
mkdir build
dir /s /b src\*.java > sources.txt
javac -cp "lib/jade.jar" -d build @sources.txt
```

---

## ▶️ Lancement (4 terminaux)

> ⚠️ Sur **Windows**, remplace `:` par `;` dans le classpath.

### 1. Main container (GUI JADE)

```bash
java -cp "build:lib/jade.jar" containers.Maincontainer
```

### 2. Catalogue container

```bash
java -cp "build:lib/jade.jar" containers.CatalogueContainer
```

### 3. Pret container

```bash
java -cp "build:lib/jade.jar" containers.PretContainer
```

### 4. Lecteurs (mode démo — 6 lecteurs en parallèle)

```bash
java -cp "build:lib/jade.jar" containers.MultiLecteurContainer
```

Ou **un seul lecteur personnalisé** :

```bash
java -cp "build:lib/jade.jar" containers.MultiLecteurContainer "1984" "Lecteur_X"
```

---

## 📖 Livres au catalogue

Le catalogue contient **10 livres** par défaut :

- Le petit prince
- Les misérables
- Notre-Dame de Paris
- L'étranger
- Madame Bovary
- 1984
- Harry Potter
- Le seigneur des anneaux
- Le comte de Monte-Cristo
- Vingt mille lieues sous les mers

> ➕ Pour **ajouter un livre** : modifier la liste `livres` dans `CatalogueAgent.java`.

---

## 🧪 Scénarios testés

| Lecteur | Livre demandé | Résultat attendu |
|---|---|---|
| Lecteur 1 | Le petit prince | ✅ `PRET_OK` |
| Lecteur 2 | Les misérables | ✅ `PRET_OK` |
| Lecteur 3 | 1984 | ✅ `PRET_OK` |
| Lecteur 4 | Harry Potter | ✅ `PRET_OK` |
| Lecteur 5 | Le petit prince | ❌ `PRET_REFUSE` (déjà prêté au Lecteur 1) |
| Lecteur 6 | Livre inexistant | ❌ `INDISPONIBLE` |

### Sortie console attendue

```
✔ 'Le petit prince' est au catalogue → je demande le prêt
✅ Prêt confirmé, je lis maintenant : Le petit prince
❌ Prêt refusé, 'Le petit prince' est déjà emprunté
✘ 'Le livre qui n'existe pas' n'est pas au catalogue → abandon
```

---

## 🧠 Concepts JADE utilisés

- **Agents** : classes héritant de `jade.core.Agent`
- **Comportements cycliques** : `CyclicBehaviour` (écoute permanente des messages)
- **Annuaire (DF)** : `DFService.register()` et `DFService.search()` pour découvrir les agents par service
- **Communication ACL** : messages `REQUEST` / `INFORM` avec `ACLMessage`
- **Containers** : chaque classe dans `containers/` démarre un conteneur JADE et y déploie ses agents

---

