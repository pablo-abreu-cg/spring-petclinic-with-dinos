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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

	private List<Dinosaur> dinosaurs;

	private Dinosaur dinosaur;

	@BeforeEach
	void setUp() throws IOException {
		dinosaurs = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/dinos.csv"))) {
			String line;
			// skip first line
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Dinosaur dinosaur = new Dinosaur();
				dinosaur.setId(Long.parseLong(values[0]));
				dinosaur.setName(values[1]);
				dinosaur.setSpecies(values[2]);
				dinosaur.setSex(values[3]);
				dinosaur.setCountryOfOrigin(values[4]);
				dinosaur.setNumberOfScales(Integer.parseInt(values[5]));
				dinosaurs.add(dinosaur);
			}
		}
		dinosaur = dinosaurs.get(0);
	}

	@Test
	void testGetAllDinosaurs() throws Exception {
		given(dinosaurService.findAll()).willReturn(dinosaurs);

		mockMvc.perform(get("/dinosaurs"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name", is(dinosaurs.get(0).getName())));
	}

	@Test
	void testGetDinosaurById() throws Exception {
		Dinosaur dinosaur = dinosaurs.get(0);
		given(dinosaurService.findById(dinosaur.getId())).willReturn(Optional.of(dinosaur));

		mockMvc.perform(get("/dinosaurs/" + dinosaur.getId()))
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