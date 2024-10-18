/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.owner;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link DinosaurController}
 *
 */
@WebMvcTest(DinosaurController.class)
@DisabledInNativeImage
@DisabledInAotMode
class DinoControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DinosaurService dinosaurService;

	private Dinosaur dinosaur;

	@BeforeEach
	void setUp() {
		dinosaur = new Dinosaur();
		dinosaur.setId(1L);
		dinosaur.setName("T-Rex");
		dinosaur.setSpecies("Tyrannosaurus");
		dinosaur.setSex("Male");
		dinosaur.setCountryOfOrigin("USA");
		dinosaur.setNumberOfScales(1000);
	}

	@Test
	void testGetAllDinosaurs() throws Exception {
		given(dinosaurService.findAll()).willReturn(Lists.newArrayList(dinosaur));

		mockMvc.perform(get("/dinosaurs"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name", is(dinosaur.getName())));
	}

	@Test
	void testGetDinosaurById() throws Exception {
		given(dinosaurService.findById(1L)).willReturn(Optional.of(dinosaur));

		mockMvc.perform(get("/dinosaurs/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(dinosaur.getName())));
	}

	@Test
	void testCreateDinosaur() throws Exception {
		given(dinosaurService.save(any(Dinosaur.class))).willReturn(dinosaur);

		mockMvc.perform(post("/dinosaurs").contentType("application/json")
			.content(
					"{\"name\":\"T-Rex\",\"species\":\"Tyrannosaurus\",\"sex\":\"Male\",\"countryOfOrigin\":\"USA\",\"numberOfScales\":1000}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(dinosaur.getName())));
	}

	@Test
	void testUpdateDinosaur() throws Exception {
		given(dinosaurService.findById(1L)).willReturn(Optional.of(dinosaur));
		given(dinosaurService.save(any(Dinosaur.class))).willReturn(dinosaur);

		mockMvc.perform(put("/dinosaurs/1").contentType("application/json")
			.content(
					"{\"name\":\"T-Rex\",\"species\":\"Tyrannosaurus\",\"sex\":\"Male\",\"countryOfOrigin\":\"USA\",\"numberOfScales\":1000}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(dinosaur.getName())));
	}

	@Test
	void testDeleteDinosaur() throws Exception {
		given(dinosaurService.findById(1L)).willReturn(Optional.of(dinosaur));

		mockMvc.perform(delete("/dinosaurs/1")).andExpect(status().isNoContent());
	}

}