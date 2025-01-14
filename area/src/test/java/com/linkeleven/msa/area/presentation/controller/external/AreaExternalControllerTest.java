package com.linkeleven.msa.area.presentation.controller.external;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AreaExternalControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testFindAdministrativeRegion() throws Exception {
		mockMvc.perform(get("/external/areas/locations/coordinates")
				.param("latitude", "37.5665")
				.param("longitude", "126.9780"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.areaId").value(556028620837202474L));
	}

	@Test
	void testFindAddressRegion() throws Exception {
		mockMvc.perform(get("/external/areas/locations/address")
				.param("address", "서울특별시 강남구 강남대로 390"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.areaId").value(556028620837202474L));
	}

	@Test
	void testFindKeywordByRegion() throws Exception {
		mockMvc.perform(get("/external/areas/locations/keywords")
				.param("keyword", "강남"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.areaId").value(556028620837202474L));
	}
}
