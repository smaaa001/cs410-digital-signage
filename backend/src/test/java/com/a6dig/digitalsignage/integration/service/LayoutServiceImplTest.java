package com.a6dig.digitalsignage.integration.service;

import com.a6dig.digitalsignage.dto.*;
import com.a6dig.digitalsignage.entity.Layout;
import com.a6dig.digitalsignage.entity.LayoutSlot;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import com.a6dig.digitalsignage.exception.LayoutSlotNotFoundException;
import com.a6dig.digitalsignage.repository.LayoutRepository;
import com.a6dig.digitalsignage.repository.LayoutSlotRepository;
import com.a6dig.digitalsignage.service.LayoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
//@Transactional
public class LayoutServiceImplTest {

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

    // create

    @Test
    void shouldCreateLayoutAndPersistToDatabase() {
        LayoutRequestDto requestDto = this.buildLayoutRequestDto("Main Layout", 2, 2);

        LayoutResponseDto result = this.layoutServiceImpl.createLayout(requestDto);

        assertNotNull(result.getId());
        assertEquals("Main Layout", result.getName());
        assertEquals(2, result.getCols());
        assertEquals(2, result.getRows());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        assertTrue(this.layoutRepository.existsById(result.getId()));
    }


    @Test
    void shouldThrowErrorWhenCreateLayoutWithInvalidData(){
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 0, 1);
        request.setSlots(null);
        assertThrows(InvalidLayoutException.class, () -> this.layoutServiceImpl.createLayout(request));
    }

    @Test
    void shouldCreateLayoutWIthSlotsAndPersistToDB() {
        LayoutRequestDto requestDto = this.buildLayoutRequestDto("Main Layout", 2, 2);

        requestDto.setSlots(List.of(
                this.buildLayoutSlotRequestDto(null, 1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(null,1 , 2, 1, 1, 0)
        ));

        LayoutResponseDto result = this.layoutServiceImpl.createLayout(requestDto);

        assertNotNull(result.getId());
        assertEquals("Main Layout", result.getName());
        assertEquals(2, result.getCols());
        assertEquals(2, result.getRows());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        assertTrue(this.layoutRepository.existsById(result.getId()));

        assertEquals(2, result.getSlots().size());
        assertEquals(result.getId(), result.getSlots().get(0).getLayoutId());
        assertEquals(result.getId(), result.getSlots().get(1).getLayoutId());

    }

    // Read

    @Test
    void shouldGetLayoutById(){
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 1, 1);
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);

        LayoutResponseDto result = this.layoutServiceImpl.getLayoutById(created.getId());

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
        assertEquals("Main Layout", result.getName());
        assertEquals(1, result.getCols());
        assertEquals(1, result.getRows());
    }

    @Test
    void shouldThrowErrorWhenLayoutNotFound() {
        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.getLayoutById(1000000L));
    }


    @Test
    void shouldGetAllLayouts() {
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("First Layout", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Second Layout", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Third Layout", 1, 1));

        List<LayoutResponseDto> result = this.layoutServiceImpl.getAllLayouts();

        assertEquals(3, result.size());
    }


    @Test
    void shouldReturnEmptyListWhenNoLayoutsFound() {
        List<LayoutResponseDto> result = this.layoutServiceImpl.getAllLayouts();
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenLayoutExists() {
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 1, 1);
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);

        assertTrue(this.layoutServiceImpl.layoutExist(created.getId()));
    }

    @Test
    void shouldReturnFalseWhenLayoutNotExist() {
        assertFalse(this.layoutServiceImpl.layoutExist(10000L));
    }

    @Test
    void shouldCountLayouts() {

        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("First Layout", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Second Layout", 1, 1));
        this.layoutServiceImpl.createLayout(this.buildLayoutRequestDto("Third Layout", 1, 1));

        assertEquals(3, this.layoutRepository.count());
    }

    @Test
    void shouldGetLayoutWithSlots() {

        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 2, 2);

        request.setSlots(List.of(
                this.buildLayoutSlotRequestDto(null,1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(null,1, 2, 1, 1, 0)
        ));

        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);

        LayoutResponseDto result = this.layoutServiceImpl.getLayoutById(created.getId());

        assertEquals(2, result.getSlots().size());
        assertEquals(result.getId(), result.getSlots().get(0).getLayoutId());
        assertEquals(result.getId(), result.getSlots().get(1).getLayoutId());

    }

    // Update

    @Test
    void shouldUpdateLayout() {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        LayoutRequestDto updateRequest = new LayoutRequestDto();
        updateRequest.setName("Updated Layout");
        updateRequest.setCols(created.getCols());
        updateRequest.setRows(created.getRows());

        LayoutResponseDto updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);

        assertEquals("Updated Layout", updated.getName());
        assertEquals(1, updated.getCols());
        assertEquals(1, updated.getRows());

        Layout fromDb = this.layoutRepository.findById(created.getId()).orElseThrow();
        assertEquals("Updated Layout", fromDb.getName());
    }

    @Test
    void shouldUpdateUpdatedAtOnUpdate() throws InterruptedException {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        Thread.sleep(200);

        LayoutRequestDto updateRequest = new LayoutRequestDto();
        updateRequest.setName("Updated Layout");
        updateRequest.setCols(2);
        updateRequest.setRows(2);

        LayoutResponseDto updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);

        System.out.println(updated.getUpdatedAt());
        System.out.println(created.getUpdatedAt());
        assertTrue(updated.getUpdatedAt().isAfter(created.getUpdatedAt()));

    }


    @Test
    void shouldNotChangeCreatedAtOnUpdate() throws InterruptedException {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        Thread.sleep(200);

        LayoutRequestDto updateRequest = new LayoutRequestDto();
        updateRequest.setName("Updated Layout");
        updateRequest.setCols(created.getCols());
        updateRequest.setRows(created.getRows());

        LayoutResponseDto updated = this.layoutServiceImpl.updateLayout(created.getId(), updateRequest);

        assertEquals(created.getCreatedAt(), updated.getCreatedAt());

    }

    @Test
    void shouldThrowWhenUpdatingNonExistentLayout() {
        LayoutRequestDto updateRequest = new LayoutRequestDto();
        updateRequest.setName("Main Layout");
        updateRequest.setCols(1);
        updateRequest.setRows(1);

        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.updateLayout(1000L, updateRequest));
    }



    @Test
    void shouldThrowErrorWhenUpdateLayoutWithInvalidData(){
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1,1)
        );

        LayoutRequestDto updateRequest = new LayoutRequestDto();
        updateRequest.setName("Updated Layout");
        updateRequest.setCols(0);
        updateRequest.setRows(created.getRows());


        assertThrows(InvalidLayoutException.class, () -> layoutServiceImpl.updateLayout(created.getId(), updateRequest));
    }


    // Delete

    @Test
    void shouldDeleteLayout() {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );

        this.layoutServiceImpl.deleteLayout(created.getId());

        assertFalse(this.layoutRepository.existsById(created.getId()));

    }

    @Test
    void shouldDeleteLayoutAndCascadeSlots() {

        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 2, 2);

        request.setSlots(List.of(
                this.buildLayoutSlotRequestDto(null,1, 1, 1, 1, 0),
                this.buildLayoutSlotRequestDto(null,1, 2, 1, 1, 0)
        ));

        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);

        List<Long> slotIds = created.getSlots().stream().map(LayoutSlotResponseDto::getId).toList();

        this.layoutServiceImpl.deleteLayout(created.getId());

        assertFalse(this.layoutRepository.existsById(created.getId()));

        slotIds.forEach(slotId -> {
            assertFalse(this.layoutSlotRepository.existsById(slotId));
        });

    }

    @Test
    void shouldThrowErrorWhenDeletingNonExistingLayout() {
        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.deleteLayout(10000L));
    }


    // Dependencies - Slots

    @Test
    @Transactional
    void shouldAddSlotToExistingLayout() {
        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );

        LayoutResponseDto result = this.layoutServiceImpl.addLayoutSlotToLayout(
                created.getId(),
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1,0)
        );

        assertEquals(1, result.getSlots().size());

        Layout fromDb = this.layoutRepository.findById(created.getId()).orElseThrow();
        assertEquals(1, fromDb.getSlots().size());
    }

    @Test
    @Transactional
    void shouldRemoveSlotFromLayout() {
        LayoutRequestDto request = this.buildLayoutRequestDto("Main Layout", 3, 3);
        request.setSlots(List.of(
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1, 0),
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1, 0)
        ));

        LayoutResponseDto created = this.layoutServiceImpl.createLayout(request);
        Long slotId = created.getSlots().get(0).getId();

        this.layoutServiceImpl.removeLayoutSlotFromLayout(created.getId(), slotId);

        Layout fromDb = this.layoutRepository.findById(created.getId()).orElseThrow();
        assertEquals(1, fromDb.getSlots().size());
        assertFalse(this.layoutSlotRepository.existsById(slotId));


    }

    @Test
    void shouldThrowWhenAddingSlotToNonExistingLayout() {
        assertThrows(LayoutNotFoundException.class, () -> this.layoutServiceImpl.addLayoutSlotToLayout(1000L,
                this.buildLayoutSlotRequestDto(1L, 1,1,1,1, 0)
                ));
    }

    @Test
    void shouldThrowWhenRemovingNonExistingLayoutSlot() {

        LayoutResponseDto created = this.layoutServiceImpl.createLayout(
                this.buildLayoutRequestDto("Main Layout", 1, 1)
        );

        assertThrows(LayoutSlotNotFoundException.class, () -> this.layoutServiceImpl.removeLayoutSlotFromLayout(created.getId(),100L));
    }



}
