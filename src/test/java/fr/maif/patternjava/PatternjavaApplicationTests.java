package fr.maif.patternjava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.maif.patternjava.app.PatternjavaApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PatternjavaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PatternjavaApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@DisplayName("Cycle de vie du colis")
	@Test
	public void enregistrerUnColis() throws JsonProcessingException {
		ResponseEntity<JsonNode> nouveauColisResponse = restTemplate.postForEntity("/api/v1/colis", objectMapper.readTree("""
				{
				     "type": "NouveauColis",
				     "email": "jdusse@maif.fr",
				     "adresse": {
				         "type": "AdresseBtoC",
				         "civiliteNomPrenom": "Jean Claude Dusse",
				         "numeroLibelleVoie": "10 rue de la rue",
				         "pays": "79000 Niort"
				     }
				 }
				"""), JsonNode.class);

		JsonNode nouveauColis = nouveauColisResponse.getBody();
		System.out.println(nouveauColis);
		assertThat(nouveauColisResponse.getStatusCode().value()).isEqualTo(200);
		String reference = nouveauColis.path("reference").asText();
		assertThat(nouveauColis.path("type").asText()).isEqualTo("ColisPrisEnCharge");

		String dateDEnvoi = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		ResponseEntity<JsonNode> colisEnCoursDAcheminementResponse = restTemplate.exchange("/api/v1/colis/" + reference, HttpMethod.PUT, new HttpEntity<>(objectMapper.readTree("""
				{
				      "reference": "%s",
				      "type": "ColisEnCoursDAcheminement",
				      "email": "jdusse@maif.fr",
				      "dateDEnvoi": "%s",
					  "latitude": 44,
					  "longitude": 60,
				      "adresse": {
				          "type": "AdresseBtoC",
				          "civiliteNomPrenom": "Jean Claude Dusse",
				          "numeroLibelleVoie": "10 rue de la rue",
				          "pays": "79000 Niort"
				      }
				  }
				""".formatted(reference, dateDEnvoi))), JsonNode.class);

		JsonNode colisEnCoursDAcheminement = colisEnCoursDAcheminementResponse.getBody();
		System.out.println(colisEnCoursDAcheminement);
		assertThat(colisEnCoursDAcheminementResponse.getStatusCode().value()).isEqualTo(200);
		assertThat(colisEnCoursDAcheminement.path("type").asText()).isEqualTo("ColisEnCoursDAcheminement");

		String dateReception = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		ResponseEntity<JsonNode> colisRecuResponse = restTemplate.exchange("/api/v1/colis/"+reference, HttpMethod.PUT, new HttpEntity<>(objectMapper.readTree("""
				{
				      "reference": "%s",
				      "type": "ColisRecu",
				      "email": "jdusse@maif.fr",
				      "dateDEnvoi": "%s",
				      "dateReception": "%s",
				      "adresse": {
				          "type": "AdresseBtoC",
				          "civiliteNomPrenom": "Jean Claude Dusse",
				          "numeroLibelleVoie": "10 rue de la rue",
				          "pays": "79000 Niort"
				      }
				  }
				""".formatted(reference, dateDEnvoi, dateReception))), JsonNode.class);

		JsonNode colisRecu = colisRecuResponse.getBody();
		System.out.println(colisRecu);
		assertThat(colisRecuResponse.getStatusCode().value()).isEqualTo(200);
		assertThat(colisRecu.path("type").asText()).isEqualTo("ColisRecu");
	}



	@DisplayName("Erreur si les données ne respectent pas le format")
	@Test
	public void validation() throws JsonProcessingException {
		ResponseEntity<JsonNode> nouveauColisResponse = restTemplate.postForEntity("/api/v1/colis", objectMapper.readTree("""
				{
				     "type": "NouveauColis",
				     "email": "azertyuio",
				     "adresse": {
				         "type": "AdresseBtoC",
				         "civiliteNomPrenom": "Jean Claude Dusse",
				         "numeroLibelleVoie": "10 rue de la rue 10 rue de la rue 10 rue de la rue 10 rue de la rue 10 rue de la rue 10 rue de la rue 10 rue de la rue 10 rue de la rue",
				         "pays": "79000 Niort"
				     }
				 }
				"""), JsonNode.class);

		JsonNode nouveauColis = nouveauColisResponse.getBody();
		System.out.println(nouveauColis);
		assertThat(nouveauColisResponse.getStatusCode().value()).isEqualTo(400);
	}


	@DisplayName("Il n'est pas possible de passer le colis en reçu si il est au status pris en charge. ")
	@Test
	public void incoherence1() throws JsonProcessingException {
		ResponseEntity<JsonNode> nouveauColisResponse = restTemplate.postForEntity("/api/v1/colis", objectMapper.readTree("""
				{
				     "type": "NouveauColis",
				     "email": "jdusse@maif.fr",
				     "adresse": {
				         "type": "AdresseBtoC",
				         "civiliteNomPrenom": "Jean Claude Dusse",
				         "numeroLibelleVoie": "10 rue de la rue",
				         "pays": "79000 Niort"
				     }
				 }
				"""), JsonNode.class);

		JsonNode nouveauColis = nouveauColisResponse.getBody();
		System.out.println(nouveauColis);
		assertThat(nouveauColisResponse.getStatusCode().value()).isEqualTo(200);
		String reference = nouveauColis.path("reference").asText();
		assertThat(nouveauColis.path("type").asText()).isEqualTo("ColisPrisEnCharge");

		String dateDEnvoi = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		String dateReception = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());

		ResponseEntity<JsonNode> colisRecuResponse = restTemplate.exchange("/api/v1/colis/"+reference, HttpMethod.PUT, new HttpEntity<>(objectMapper.readTree("""
				{
				      "reference": "%s",
				      "type": "ColisRecu",
				      "email": "jdusse@maif.fr",
				      "dateDEnvoi": "%s",
				      "dateReception": "%s",
				      "adresse": {
				          "type": "AdresseBtoC",
				          "civiliteNomPrenom": "Jean Claude Dusse",
				          "numeroLibelleVoie": "10 rue de la rue",
				          "pays": "79000 Niort"
				      }
				  }
				""".formatted(reference, dateDEnvoi, dateReception))), JsonNode.class);

		JsonNode colisRecu = colisRecuResponse.getBody();
		System.out.println(colisRecu);
		assertThat(colisRecuResponse.getStatusCode().value()).isEqualTo(400);
	}

	@DisplayName("Il n'est pas possible de passer le colis en nouveau si il est au status pris en charge. ")
	@Test
	public void incoherence2() throws JsonProcessingException {
		ResponseEntity<JsonNode> nouveauColisResponse = restTemplate.postForEntity("/api/v1/colis", objectMapper.readTree("""
				{
				     "type": "NouveauColis",
				     "email": "jdusse@maif.fr",
				     "adresse": {
				         "type": "AdresseBtoC",
				         "civiliteNomPrenom": "Jean Claude Dusse",
				         "numeroLibelleVoie": "10 rue de la rue",
				         "pays": "79000 Niort"
				     }
				 }
				"""), JsonNode.class);

		JsonNode nouveauColis = nouveauColisResponse.getBody();
		System.out.println(nouveauColis);
		assertThat(nouveauColisResponse.getStatusCode().value()).isEqualTo(200);
		String reference = nouveauColis.path("reference").asText();
		assertThat(nouveauColis.path("type").asText()).isEqualTo("ColisPrisEnCharge");

		String dateDEnvoi = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		String dateReception = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());

		ResponseEntity<JsonNode> colisRecuResponse = restTemplate.exchange("/api/v1/colis/"+reference, HttpMethod.PUT, new HttpEntity<>(objectMapper.readTree("""
				{
				      "reference": "%s",
				      "type": "NouveauColis",
				      "email": "jdusse@maif.fr",
				      "dateDEnvoi": "%s",
				      "dateReception": "%s",
				      "adresse": {
				          "type": "AdresseBtoC",
				          "civiliteNomPrenom": "Jean Claude Dusse",
				          "numeroLibelleVoie": "10 rue de la rue",
				          "pays": "79000 Niort"
				      }
				  }
				""".formatted(reference, dateDEnvoi, dateReception))), JsonNode.class);

		JsonNode colisRecu = colisRecuResponse.getBody();
		System.out.println(colisRecu);
		assertThat(colisRecuResponse.getStatusCode().value()).isEqualTo(400);
	}

	@DisplayName("Il n'est pas possible de passer le colis en nouveau si il est au status en cours d'acheminement. ")
	@Test
	public void incoherence3() throws JsonProcessingException {
		ResponseEntity<JsonNode> nouveauColisResponse = restTemplate.postForEntity("/api/v1/colis", objectMapper.readTree("""
				{
				     "type": "NouveauColis",
				     "email": "jdusse@maif.fr",
				     "adresse": {
				         "type": "AdresseBtoC",
				         "civiliteNomPrenom": "Jean Claude Dusse",
				         "numeroLibelleVoie": "10 rue de la rue",
				         "pays": "79000 Niort"
				     }
				 }
				"""), JsonNode.class);

		JsonNode nouveauColis = nouveauColisResponse.getBody();
		System.out.println(nouveauColis);
		assertThat(nouveauColisResponse.getStatusCode().value()).isEqualTo(200);
		String reference = nouveauColis.path("reference").asText();
		assertThat(nouveauColis.path("type").asText()).isEqualTo("ColisPrisEnCharge");

		String dateDEnvoi = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		ResponseEntity<JsonNode> colisEnCoursDAcheminementResponse = restTemplate.exchange("/api/v1/colis/" + reference, HttpMethod.PUT, new HttpEntity<>(objectMapper.readTree("""
				{
				      "reference": "%s",
				      "type": "ColisEnCoursDAcheminement",
				      "email": "jdusse@maif.fr",
				      "dateDEnvoi": "%s",
					  "latitude": 44,
					  "longitude": 60,
				      "adresse": {
				          "type": "AdresseBtoC",
				          "civiliteNomPrenom": "Jean Claude Dusse",
				          "numeroLibelleVoie": "10 rue de la rue",
				          "pays": "79000 Niort"
				      }
				  }
				""".formatted(reference, dateDEnvoi))), JsonNode.class);

		JsonNode colisEnCoursDAcheminement = colisEnCoursDAcheminementResponse.getBody();
		System.out.println(colisEnCoursDAcheminement);
		assertThat(colisEnCoursDAcheminementResponse.getStatusCode().value()).isEqualTo(200);
		assertThat(colisEnCoursDAcheminement.path("type").asText()).isEqualTo("ColisEnCoursDAcheminement");

		String dateReception = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		ResponseEntity<JsonNode> colisRecuResponse = restTemplate.exchange("/api/v1/colis/"+reference, HttpMethod.PUT, new HttpEntity<>(objectMapper.readTree("""
				{
				      "reference": "%s",
				      "type": "NouveauColis",
				      "email": "jdusse@maif.fr",
				      "dateDEnvoi": "%s",
				      "dateReception": "%s",
				      "adresse": {
				          "type": "AdresseBtoC",
				          "civiliteNomPrenom": "Jean Claude Dusse",
				          "numeroLibelleVoie": "10 rue de la rue",
				          "pays": "79000 Niort"
				      }
				  }
				""".formatted(reference, dateDEnvoi, dateReception))), JsonNode.class);

		JsonNode colisRecu = colisRecuResponse.getBody();
		System.out.println(colisRecu);
		assertThat(colisRecuResponse.getStatusCode().value()).isEqualTo(400);
	}

	@DisplayName("Il n'est pas possible de passer le colis en pris en charge si il est au status en cours d'acheminement. ")
	@Test
	public void incoherence4() throws JsonProcessingException {
		ResponseEntity<JsonNode> nouveauColisResponse = restTemplate.postForEntity("/api/v1/colis", objectMapper.readTree("""
				{
				     "type": "NouveauColis",
				     "email": "jdusse@maif.fr",
				     "adresse": {
				         "type": "AdresseBtoC",
				         "civiliteNomPrenom": "Jean Claude Dusse",
				         "numeroLibelleVoie": "10 rue de la rue",
				         "pays": "79000 Niort"
				     }
				 }
				"""), JsonNode.class);

		JsonNode nouveauColis = nouveauColisResponse.getBody();
		System.out.println(nouveauColis);
		assertThat(nouveauColisResponse.getStatusCode().value()).isEqualTo(200);
		String reference = nouveauColis.path("reference").asText();
		assertThat(nouveauColis.path("type").asText()).isEqualTo("ColisPrisEnCharge");

		String dateDEnvoi = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		ResponseEntity<JsonNode> colisEnCoursDAcheminementResponse = restTemplate.exchange("/api/v1/colis/" + reference, HttpMethod.PUT, new HttpEntity<>(objectMapper.readTree("""
				{
				      "reference": "%s",
				      "type": "ColisEnCoursDAcheminement",
				      "email": "jdusse@maif.fr",
				      "dateDEnvoi": "%s",
					  "latitude": 44,
					  "longitude": 60,
				      "adresse": {
				          "type": "AdresseBtoC",
				          "civiliteNomPrenom": "Jean Claude Dusse",
				          "numeroLibelleVoie": "10 rue de la rue",
				          "pays": "79000 Niort"
				      }
				  }
				""".formatted(reference, dateDEnvoi))), JsonNode.class);

		JsonNode colisEnCoursDAcheminement = colisEnCoursDAcheminementResponse.getBody();
		System.out.println(colisEnCoursDAcheminement);
		assertThat(colisEnCoursDAcheminementResponse.getStatusCode().value()).isEqualTo(200);
		assertThat(colisEnCoursDAcheminement.path("type").asText()).isEqualTo("ColisEnCoursDAcheminement");

		String dateReception = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		ResponseEntity<JsonNode> colisRecuResponse = restTemplate.exchange("/api/v1/colis/"+reference, HttpMethod.PUT, new HttpEntity<>(objectMapper.readTree("""
				{
				      "reference": "%s",
				      "type": "ColisPrisEnCharge",
				      "email": "jdusse@maif.fr",
				      "dateDEnvoi": "%s",
				      "dateReception": "%s",
				      "adresse": {
				          "type": "AdresseBtoC",
				          "civiliteNomPrenom": "Jean Claude Dusse",
				          "numeroLibelleVoie": "10 rue de la rue",
				          "pays": "79000 Niort"
				      }
				  }
				""".formatted(reference, dateDEnvoi, dateReception))), JsonNode.class);

		JsonNode colisRecu = colisRecuResponse.getBody();
		System.out.println(colisRecu);
		assertThat(colisRecuResponse.getStatusCode().value()).isEqualTo(400);
	}

}
