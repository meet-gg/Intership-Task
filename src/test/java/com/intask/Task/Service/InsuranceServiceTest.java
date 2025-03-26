//package com.intask.Task.Service;
//
//import com.intask.Task.DTO.InsuranceDTO;
//import com.intask.Task.DTO.PurchaseDTO;
//import com.intask.Task.DTO.PurchaseRequestDTO;
//import com.intask.Task.Entity.Insurance;
//import com.intask.Task.Entity.Purchase;
//import com.intask.Task.Entity.User;
//import com.intask.Task.Exceptions.ResourceNotFoundException;
//import com.intask.Task.Repository.InsuranceRepo;
//import com.intask.Task.Repository.PurchaseRepo;
//import com.intask.Task.Repository.UserRepo;
//import com.intask.Task.TestContainerConfiguration;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.ResponseEntity;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@Import(TestContainerConfiguration.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ExtendWith(MockitoExtension.class)
//class InsuranceServiceTest {
//
//    @InjectMocks
//    private InsuranceService insuranceService;
//
//    @Mock
//    private InsuranceRepo insuranceRepo;
//
//    @Mock
//    private UserRepo userRepo;
//
//    @Mock
//    private PurchaseRepo purchaseRepo;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    private Insurance insurance;
//    private User user;
//    private Purchase purchase;
//
//    @BeforeEach
//    void setUp() {
//        insurance = new Insurance();
//        insurance.setId(1L);
//        insurance.setPremiumPrice(5000.00);
//        insurance.setCoverageAmount(500000.00);
//
//        user = new User();
//        user.setId(1L);
//
//        purchase = new Purchase();
//        purchase.setId(1L);
//        purchase.setUser(user);
//        purchase.setInsurance(insurance);
//        purchase.setPurchaseDate(LocalDateTime.now());
//    }
//
//    @Test
//    void testAddInsurance() {
//        InsuranceDTO insuranceDTO = new InsuranceDTO();
//        List<InsuranceDTO> insuranceDTOList = Arrays.asList(insuranceDTO);
//
//        when(modelMapper.map(any(InsuranceDTO.class), eq(Insurance.class))).thenReturn(insurance);
//
//        ResponseEntity<?> response = insuranceService.AddInsurance(insuranceDTOList);
//
//        assertEquals(201, response.getStatusCodeValue());
//        verify(insuranceRepo, times(1)).saveAll(any());
//    }
//
//    @Test
//    void testGetAllInsurances() {
//        when(insuranceRepo.findAll()).thenReturn(Arrays.asList(insurance));
//        when(modelMapper.map(any(Insurance.class), eq(InsuranceDTO.class))).thenReturn(new InsuranceDTO());
//
//        ResponseEntity<List<?>> response = insuranceService.getAllInsurances();
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void testPurchaseInsurance_Success() {
//        // Create DTO
//        PurchaseRequestDTO requestDTO = new PurchaseRequestDTO();
//        requestDTO.setUserId(1L);
//        requestDTO.setInsuranceId(1L);
//
//        // Mock repository responses
//        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
//        when(insuranceRepo.findById(1L)).thenReturn(Optional.of(insurance));
//        when(purchaseRepo.save(any())).thenReturn(purchase);
//        when(modelMapper.map(any(Purchase.class), eq(PurchaseDTO.class))).thenReturn(new PurchaseDTO());
//
//        // Call the service method
//        try {
//            ResponseEntity<?> response = insuranceService.purchaseInsurance(requestDTO);
//
//            // Print debug information
//            System.out.println("Response Body: " + response.getBody());
//            System.out.println("Response Status Code: " + response.getStatusCode());
//
//            // Assertions
//            assertEquals(202, response.getStatusCodeValue());
//            verify(purchaseRepo, times(1)).save(any());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail("Exception thrown: " + e.getMessage());
//        }
//    }
//
//
//    @Test
//    void testPurchaseInsurance_UserNotFound() {
//        PurchaseRequestDTO requestDTO = new PurchaseRequestDTO();
//        requestDTO.setUserId(1L);
//        requestDTO.setInsuranceId(1L);
//
//        when(userRepo.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            insuranceService.purchaseInsurance(requestDTO);
//        });
//    }
//
//    @Test
//    void testPurchaseInsurance_InsuranceNotFound() {
//        PurchaseRequestDTO requestDTO = new PurchaseRequestDTO();
//        requestDTO.setUserId(1L);
//        requestDTO.setInsuranceId(1L);
//
//        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
//        when(insuranceRepo.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            insuranceService.purchaseInsurance(requestDTO);
//        });
//    }
//
//    @Test
//    void testConvertPdf_Success() {
//        when(purchaseRepo.findById(1L)).thenReturn(Optional.of(purchase));
//
//        String pdfPath = insuranceService.convertPdf(1L);
//
//        assertNotNull(pdfPath);
//
//        // Validate that returned path is a valid UUID (not ".pdf")
//        assertDoesNotThrow(() -> UUID.fromString(pdfPath));
//    }
//
//    @Test
//    void testConvertPdf_PurchaseNotFound() {
//        when(purchaseRepo.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            insuranceService.convertPdf(1L);
//        });
//    }
//}
