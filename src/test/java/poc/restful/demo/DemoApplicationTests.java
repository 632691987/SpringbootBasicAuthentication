package poc.restful.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import poc.restful.demo.dto.AdminDTO;
import poc.restful.demo.dto.CustomerDTO;
import poc.restful.demo.dto.OperatorDTO;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.test.properties")
public class DemoApplicationTests {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private RestTemplateBuilder builder;

	@Test
	public void givenOperatorProfile_whenCallOperator_Success() {
		ResponseEntity<OperatorDTO> response = restTemplate.withBasicAuth("operator", "operator_password").getForEntity("/operatorapi", OperatorDTO.class);
		Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);

		OperatorDTO operatorDTO = response.getBody();
		logger.info(operatorDTO.toString());
	}

	@Test
	public void givenAdminProfile_whenCallAdmin_Success() {
		ResponseEntity<AdminDTO> response = restTemplate.withBasicAuth("admin", "admin_password").getForEntity("/adminapi", AdminDTO.class);
		Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);

		AdminDTO adminDTO = response.getBody();
		logger.info(adminDTO.toString());
	}

	@Test
	public void givenCustomerProfile_whenCallCustomer_Success() {
		ResponseEntity<CustomerDTO> response = restTemplate.withBasicAuth("customer", "customer_password").getForEntity("/customerapi", CustomerDTO.class);
		Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);

		CustomerDTO customerDTO = response.getBody();
		logger.info(customerDTO.toString());
	}

	@Test
	public void givenAdminProfile_whenCallOperator_Fail() {
		ResponseEntity<ErrorDto> response = restTemplate.withBasicAuth("admin", "admin_password").getForEntity("/operatorapi", ErrorDto.class);
		Assert.assertTrue(response.getStatusCode() == HttpStatus.FORBIDDEN);
		logger.info(response.getBody().toString());
	}

	@Test(expected = AuthenticationServiceException.class)
	public void givenOpertorProfile_whenRemoteCall_thenCatchException() {
		String USER_NAME = "operator";
		String PASSWORD = "operator_password";
		String restAPI = "http://localhost:" + port + "/adminapi";

		HttpHeaders headers = new HttpHeaders();
		String auth = USER_NAME + ":" + PASSWORD;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		headers.set("Authorization", authHeader);
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		RestTemplate restTemplate = this.builder.errorHandler(new RestTemplateResponseErrorHandler()).build();
		restTemplate.exchange(restAPI, HttpMethod.GET, entity, String.class);
	}

	public void givenAdminProfile_whenRemoteCall_thenCatchException() throws IOException {
		String USER_NAME = "admin";
		String PASSWORD = "admin_password";
		String restAPI = "http://localhost:" + port + "/adminapi";

		HttpHeaders headers = new HttpHeaders();
		String auth = USER_NAME + ":" + PASSWORD;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		headers.set("Authorization", authHeader);
		headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		RestTemplate restTemplate = this.builder.errorHandler(new RestTemplateResponseErrorHandler()).build();
		ResponseEntity<String> responseEntity = restTemplate.exchange(restAPI, HttpMethod.GET, entity, String.class);

		ObjectMapper mapper = new ObjectMapper();
		AdminDTO adminDTO = mapper.readValue(responseEntity.getBody(), AdminDTO.class);
		Assert.assertThat(adminDTO.getAttribute4(), Matchers.notNullValue());
		Assert.assertThat(adminDTO.getAttribute5(), Matchers.notNullValue());
		Assert.assertThat(adminDTO.getAttribute6(), Matchers.notNullValue());
	}

}
