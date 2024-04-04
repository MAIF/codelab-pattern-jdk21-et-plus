# Illustration des patterns java 21 

## Les nouveautés du jdk 

### Les records 

Les records sont un nouveau type de class qui permet de représenter un type produit. 
On peut les utiliser en remplacement des traditionnels POJO, on peut également les voir comme un tuple auquel on aurait donné un nom. 

Pour déclarer un record : 

```java
record Chien(String nom, Integer age) { }
```

Il est également possible de faire des choses à la construction de l'instance 

```java
record Chien(String nom, Integer age) {
    public Chien { // ici on est pas obligé de rappeler les attributs 
        nom = Objects.requireNonNullElse(nom, "Médor");
        Objects.requireNonNull(age, "l'age est obligatoire");
    }
    
    // On peut également écrire d'autres constructeurs 
    public Chien(Integer age) {
        this("Médor", age);
    }
}
```

Le record est immutable (les attributs sont final). Pour modifier un attribut il faudra créer une nouvelle instance : 

```java
record Chien(String nom, Integer age) {
    public Chien nom(String nom) {
        return new Chien(nom, age);
    } 
}
```

Un equals et hash code est automatiquement généré à partir de tous les attributs.

Des accesseurs sont également générés :

```java
Chien medor = new Chien("Médor", 5);

String nom = medor.nom();
```

Le record est une classe finale et ne peut pas être étendu. 

### Les interfaces scellées 

### Le pattern matching 



### Utiliser l'API

```bash 
curl -XGET http://localhost:8080/api/v1/colis | jq 
curl -XGET http://localhost:8080/api/v2/colis | jq 
```


```bash 
curl -XPOST http://localhost:8080/api/v1/colis -H 'Content-Type:application/json' -d '{
    "type": "NouveauColis", 
    "email": "jdusse@maif.fr",
    "adresse": {
        "type": "AdresseBtoC", 
        "ligne1": "Jean Claude Dusse", 
        "ligne4": "10 rue de la rue",
        "ligne6": "79000 Niort"
    }
}' 
```

Invalide:

```bash
curl -XPOST http://localhost:8080/api/v1/colis -H 'Content-Type:application/json' -d '{
    "type": "NouveauColis",
    "email": "jdussemaiffr",
    "adresse": {
        "type": "AdresseBtoC",
        "ligne1": "Jean Claude Dusse qui habite dans une rue qui va bien finir par dépasser les 38 caractères autorisés",
        "ligne4": "10 rue de la rue",
        "ligne6": "79000 Niort"
    }
}' | jq
```


```bash 
curl -XPUT http://localhost:8080/api/v2/colis/4bcdeac1-3aa7-4a7a-91a4-b5d3e40adefa -H 'Content-Type:application/json' -d '{
    "reference": "4bcdeac1-3aa7-4a7a-91a4-b5d3e40adefa",
    "type": "ColisEnCoursDAcheminement", 
    "email": "jdusse@maif.fr",
    "dateDEnvoi": "2021-11-08T11:59:09.933828",
    "position": {
        "latitude": 44,
        "longitude": 60
    },
    "adresse": {
        "type": "AdresseBtoC", 
        "ligne1": "Jean Claude Dusse", 
        "ligne4": "10 rue de la rue",
        "ligne6": "79000 Niort"
    }
}' --include
```


```bash 
curl -XPUT http://localhost:8080/api/colis/v2/4bcdeac1-3aa7-4a7a-91a4-b5d3e40adefa -H 'Content-Type:application/json' -d '{
  "reference": "4bcdeac1-3aa7-4a7a-91a4-b5d3e40adefa",
  "type": "ColisRecu",
  "dateDEnvoi": "2021-11-08T11:59:09.933828",
  "dateDeReception": "2021-11-08T14:40:00.000000",
  "email": "jdusse@maif.fr",
  "adresse": {
    "type": "AdresseBtoC",
    "ligne1": "Jean Claude Dusse",
    "ligne4": "10 RUE DE LA RUE",
    "ligne6": "79000 NIORT"
  }
}' --include
```