package com.a6dig.digitalsignage.integration.controller;

import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.service.LayoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class LayoutControllerTest {



    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutSlotRepository layoutSlotRepository;


    @Autowired
    private LayoutServiceImpl layoutServiceImpl;

    @BeforeEach
    void cleanUp() {
        this.layoutRepository.deleteAll();
        this.layoutSlotRepository.deleteAll();
    }


    // helper methods
    private Layout buildLayout(Long id, String name, int col, int row) {
        Layout layout = new Layout();
        layout.setId(id);
        layout.setName(name);
        layout.setCols(col);
        layout.setRows(row);
        layout.setCreatedAt(LocalDateTime.now());
        layout.setUpdatedAt(LocalDateTime.now());
        layout.setSlots(new ArrayList<>());
        return layout;
    }

    private LayoutResponseDto buildLayoutResponseDto(Long id, String name, int col, int row) {
        LayoutResponseDto dto = new LayoutResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setCols(col);
        dto.setRows(row);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setSlots(new ArrayList<>());
        return dto;
    }



    private LayoutRequestDto buildLayoutRequestDto(String name, int col, int row) {
        LayoutRequestDto dto = new LayoutRequestDto();
        dto.setName(name);
        dto.setCols(col);
        dto.setRows(row);
        dto.setSlots(new ArrayList<>());
        return dto;
    }




    private LayoutSlot buildLayoutSlot(Layout layout, Long moduleId, int col, int row, int colSpan, int rowSpan, int zIndex) {
        LayoutSlot slot = new LayoutSlot(layout);
        slot.setModuleId(moduleId);
        slot.setColPos(col);
        slot.setRowPos(row);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);
        return slot;
    }

    private LayoutSlotRequestDto buildLayoutSlotRequestDto(Long moduleId, int col, int row, int colSpan, int rowSpan, int zIndex){
        LayoutSlotRequestDto slot = new LayoutSlotRequestDto();

        slot.setModuleId(moduleId);
        slot.setColPos(col);
        slot.setRowPos(row);
        slot.setColSpan(colSpan);
        slot.setRowSpan(rowSpan);
        slot.setzIndex(zIndex);

        return slot;
    }

    // GET
//
//    @Test
//    void shouldGetLayoutById() {
//        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 1, 1);
//        ResponseEntity<APIResponse<LayoutResponseDto>> created = restTemplate.exchange(
//                "/api/layouts",
//                HttpMethod.POST,
//                new HttpEntity<>(buildLayoutRequestDto("Main Layout", 1, 1)),
//                new ParameterizedTypeReference<APIResponse<LayoutResponseDto>>() {}
//        );
//
//
//        Long id = Objects.requireNonNull(created.getBody()).getData().getId();
//
//
//        ResponseEntity<APIResponse<LayoutResponseDto>> response = restTemplate.exchange(
//                "/api/layouts/" + id,
//                HttpMethod.GET,
//                new HttpEntity<>(buildLayoutRequestDto("Main Layout", 1, 1)),
//                new ParameterizedTypeReference<APIResponse<LayoutResponseDto>>() {}
//        );
//
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertNotNull(response.getBody().getData());
//        assertEquals(200, response.getBody().getStatus());
//        assertEquals("", response.getBody().getMessage());
//        assertEquals("Main Layout", response.getBody().getData().getName());
//        assertEquals(1, response.getBody().getData().getLayoutCol());
//        assertEquals(1, response.getBody().getData().getLayoutCol());
//        assertTrue(response.getBody().getErrors().isEmpty());
//
//
//    }
}
