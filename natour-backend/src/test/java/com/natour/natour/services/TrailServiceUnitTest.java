package com.natour.natour.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.*;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.natour.natour.model.dto.TrailResponseDto;
import com.natour.natour.model.dto.util.TrailDtoConverter;
import com.natour.natour.model.entity.Trail;
import com.natour.natour.repositories.TrailRepository;
import com.natour.natour.services.trail.TrailService;
import com.natour.natour.services.trail.impl.TrailServiceImpl;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DisplayName("A TrailService")
public class TrailServiceUnitTest {
    
    @MockBean
    private TrailRepository trailRepository;

    @MockBean
    private TrailDtoConverter trailDtoConverter;

    @InjectMocks
    private TrailService trailService = new TrailServiceImpl();

    @BeforeEach
    public void mockTrailDtoConverter() {
        when(trailDtoConverter.convertTrailEntityToTrailResponseDto(any()))
            .thenAnswer(this::convertEntityTrailToTrailResponseDtoMock);
    }

    TrailResponseDto convertEntityTrailToTrailResponseDtoMock(
        InvocationOnMock invocation 
    ) {
        Trail trail = (Trail)invocation.getArgument(0);
        return new TrailResponseDto(trail.getId(), trail.getStars());
    }

    @Nested
    @DisplayName("When getting trails")
    class GetTrailMethodTest {

        @Test
        @DisplayName("should throw an IllegalArgumentException when passing a " + 
            "negative page")
        public void testNegativePage() {
            mockTrailRepositoryFindAllPageableMethod(0);

            assertThrows(
                IllegalArgumentException.class, 
                () -> trailService.getTrails(-1)
            );
        }

        @Test
        @DisplayName("should return an empty trail list when passing a " + 
            "zero page with no trails avaiable")
        public void testZeroPageWithNoTrailsAvaiable() {
            mockTrailRepositoryFindAllPageableMethod(0);

            final List<TrailResponseDto> trails = trailService.getTrails(0);

            assertThat(trails, hasSize(0));
        }

        @Test
        @DisplayName("should return a list of trails in the right order when " + 
            "passing a zero page with less than ten trails avaiable")
        public void testZeroPageWithLessThanTenTrailsAvailable() {
            mockTrailRepositoryFindAllPageableMethod(3);
            List<TrailResponseDto> expectedTrails = 
                getExpectedMockTrails(3);

            List<TrailResponseDto> trails = trailService.getTrails(0);

            assertThat(trails, is(equalTo(expectedTrails)));
        }

        @Test
        @DisplayName("should return a list of ten trails in the right order when " +
            "passing a zero page with ten trails avaiable")
        public void testZeroPageWithTenTrailsAvailable() {
            mockTrailRepositoryFindAllPageableMethod(10);
            List<TrailResponseDto> expectedTrails = 
                getExpectedMockTrails(10);

            List<TrailResponseDto> trails = trailService.getTrails(0);

            assertThat(trails, is(equalTo(expectedTrails)));
        }

        @Test
        @DisplayName("should return a list of ten trails in the right order when " +
            "passing a zero page with more than ten trails avaiable")
        public void testZeroPageWithMoreThanTenTrailsAvailable() {
            mockTrailRepositoryFindAllPageableMethod(15);
            List<TrailResponseDto> expectedTrails = 
                getExpectedMockTrails(15);

            List<TrailResponseDto> trails = trailService.getTrails(0);

            assertThat(trails, is(equalTo(expectedTrails)));
        }

        @Test
        @DisplayName("should return an empty trail list when passing a " + 
            "positive page with no trails avaiable")
        public void testPositivePageWithNoTrailsAvaiable() {
            mockTrailRepositoryFindAllPageableMethod(0);

            final List<TrailResponseDto> trails = trailService.getTrails(5);

            assertThat(trails, hasSize(0));
        }

        @Test
        @DisplayName("should return a list of trails in the right order when " + 
            "passing a positive page with less than ten trails avaiable")
        public void testPositivePageWithLessThanTenTrailsAvailable() {
            mockTrailRepositoryFindAllPageableMethod(6);
            List<TrailResponseDto> expectedTrails = 
                getExpectedMockTrails(6);

            List<TrailResponseDto> trails = trailService.getTrails(5);

            assertThat(trails, is(equalTo(expectedTrails)));
        }

        @Test
        @DisplayName("should return a list of ten trails in the right order when " +
            "passing a positive page with ten trails avaiable")
        public void testPositivePageWithTenTrailsAvailable() {
            mockTrailRepositoryFindAllPageableMethod(10);
            List<TrailResponseDto> expectedTrails = 
                getExpectedMockTrails(10);

            List<TrailResponseDto> trails = trailService.getTrails(5);
            System.out.println("HEY" + trails);

            assertThat(trails, is(equalTo(expectedTrails)));
        }

        @Test
        @DisplayName("should return a list of ten trails in the right order when " +
            "passing a positive page with more than ten trails avaiable")
        public void testPositivePageWithMoreThanTenTrailsAvailable() {
            mockTrailRepositoryFindAllPageableMethod(15);
            List<TrailResponseDto> expectedTrails = 
                getExpectedMockTrails(15);

            List<TrailResponseDto> trails = trailService.getTrails(7);
            System.out.println("HEY" + trails);

            assertThat(trails, is(equalTo(expectedTrails)));
        }

        private void mockTrailRepositoryFindAllPageableMethod(
            final int sizeContentPage
        ) {
            when(trailRepository.findAll(any(PageRequest.class)))
                .thenReturn(
                    sizeContentPage == 0 ? Page.empty()
                        : new PageImpl<>(
                            MockTrails.getMockTrails()
                                .stream()
                                .sorted(
                                    comparing(Trail::getStars)
                                        .reversed()
                                        .thenComparing(Trail::getId)
                                ).limit(sizeContentPage)
                                .collect(toList())
                        )
                );
        } 

        private List<TrailResponseDto> getExpectedMockTrails(
            final int sizeContentPage
        ) {
            return MockTrails.getMockTrails()
                .stream()
                .sorted(
                    comparing(Trail::getStars)
                        .reversed()
                        .thenComparing(Trail::getId)
                ).limit(sizeContentPage)
                .map(trail -> new TrailResponseDto(trail.getId(), trail.getStars()))
                .collect(toList());
        }
    }
}
