# Illustration des patterns java 21 



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