package com.kbe.service.user;

import com.google.gson.Gson;
import com.kbe.service.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Here we test if the functionality returns
 * the correct result after a user was registered or logged in
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

	String BASEURL = "http://localhost:8084";
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private User user;

	/*****************Register-Tests****************/
	/**
	 * check if saving user to database works
	 *
	 * @throws Exception
	 */
	@Test
	void addUserToDatabaseTest() throws Exception {
		String name = "suheib";
		String email = "testmail@test.de";
		String password = "sicher";

		user = new User(UUID.randomUUID().toString(), name, email, password);
		Gson gson = new Gson();

		RequestBuilder request = MockMvcRequestBuilders.post(BASEURL+"/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(user))
				.characterEncoding("utf-8");

		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(gson.toJson(user), result.getResponse().getContentAsString());
	}

	/**
	 * test if adding another user with the same email
	 * returns an error response
	 *
	 * @throws Exception
	 */
	@Test
	void duplicateEmailToDatabaseTest() throws Exception {
		String name = "suheib";
		String email = "duplicateMail@test.de";
		String password = "sicher";

		user = new User(UUID.randomUUID().toString(), name, email, password);
		Gson gson = new Gson();

		RequestBuilder request = MockMvcRequestBuilders.post(BASEURL+"/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(user))
				.characterEncoding("utf-8");

		mockMvc.perform(request).andReturn();
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals("user already exists", result.getResponse().getContentAsString());
	}

	/*****************Login-Tests****************/

	/**
	 * test if login functionality works
	 *
	 * @throws Exception
	 */
	@Test
	void loginTest() throws Exception {
		String name = "suheib";
		String email = "suheibLogin@test.de";
		String password = "sicher";

		user = new User(UUID.randomUUID().toString(), name, email, password);
		Gson gson = new Gson();

		RequestBuilder request = MockMvcRequestBuilders.post(BASEURL+"/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(user))
				.characterEncoding("utf-8");

		mockMvc.perform(request).andReturn();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("email",email);
		params.add("password", password);

		RequestBuilder request2 = MockMvcRequestBuilders.get(BASEURL+"/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.params(params)
				.characterEncoding("utf-8");

		MvcResult result = mockMvc.perform(request2).andReturn();
		assertEquals(gson.toJson(user), result.getResponse().getContentAsString());
	}

	/**
	 * test if adding another user with the same email
	 * returns an error response
	 *
	 * @throws Exception
	 */
	@Test
	void loginWithUnknownEmailTest() throws Exception {
		String name = "suheib";
		String email = "unknownUser@test.de";
		String password = "sicher";

		user = new User(UUID.randomUUID().toString(), name, email, password);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("email",email);
		params.add("password", password);

		RequestBuilder request = MockMvcRequestBuilders.get(BASEURL+"/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.param("email", email)
				.params(params)
				.characterEncoding("utf-8");

		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals("No user found", result.getResponse().getContentAsString());
	}

	@Test
	void contextLoads() {
	}
}
