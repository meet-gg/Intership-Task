package com.intask.Task.Service;

import com.intask.Task.DTO.InsuranceDTO;
import com.intask.Task.DTO.PurchaseDTO;
import com.intask.Task.DTO.PurchaseRequestDTO;
import com.intask.Task.DTO.SaveInsuranceDTO;
import com.intask.Task.Entity.Insurance;
import com.intask.Task.Entity.Purchase;
import com.intask.Task.Entity.User;
import com.intask.Task.Exceptions.ResourceNotFoundException;
import com.intask.Task.Repository.InsuranceRepo;
import com.intask.Task.Repository.PurchaseRepo;
import com.intask.Task.Repository.UserRepo;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


//import javax.swing.text.Document;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private static final Logger log = LoggerFactory.getLogger(InsuranceService.class);
    private final InsuranceRepo insuranceRepository;
    private final UserRepo userRepository;
    private final PurchaseRepo purchaseRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ResponseEntity<?> AddInsurance(List<SaveInsuranceDTO> insuranceDTO) {
        List<Insurance> collect = insuranceDTO.stream().map(insurance -> modelMapper.map(insurance, Insurance.class)).collect(Collectors.toList());
        insuranceRepository.saveAll(collect);
        return new ResponseEntity<>("Add all insurance",HttpStatus.CREATED);
    }

    // Get All Insurances
    public ResponseEntity<List<?>> getAllInsurances() {
            List<InsuranceDTO> insuranceDTOList = insuranceRepository.findAll()
                    .stream()
                    .map(insurance -> modelMapper.map(insurance, InsuranceDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(insuranceDTOList,HttpStatus.OK);
    }

    // Purchase Insurance
    public ResponseEntity<?> purchaseInsurance(PurchaseRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND,"User not found with id :"+request.getUserId()));

        Insurance insurance = insuranceRepository.findById(request.getInsuranceId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND,"Insurance not found id :"+request.getInsuranceId()));

        // for display value
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setInsurance(insurance);
        purchase.setPurchaseDate(LocalDateTime.now());

        Purchase savedPurchase = purchaseRepository.save(purchase);
        PurchaseDTO purchaseDTO = modelMapper.map(savedPurchase, PurchaseDTO.class);

        // for download pdf use this file path
        purchaseDTO.setFilepath(convertPdf(savedPurchase.getId()));
        purchaseDTO.setPremiumPrice(insurance.getPremiumPrice());

        return new ResponseEntity<>(purchaseDTO,HttpStatus.ACCEPTED);
    }
    public String convertPdf(Long pId) {
        try {
            UUID uniquePdfId = UUID.randomUUID(); // Generate unique ID
            String filePath = "receipts/" + uniquePdfId + ".pdf";
            Purchase request = purchaseRepository.findById(pId).orElseThrow(()->new ResourceNotFoundException(HttpStatus.NOT_FOUND,"Purchase id not found"));

            // Create receipt folder if not exists
            File directory = new File("receipts");
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Generate PDF Receipt
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Insurance Purchase Receipt"));
            document.add(new Paragraph("Purchase ID: " + uniquePdfId));
            document.add(new Paragraph("User ID: " + request.getUser().getId()));
            document.add(new Paragraph("Insurance ID: " + request.getInsurance().getId()));
            document.add(new Paragraph("Premium Price: ₹" + request.getInsurance().getPremiumPrice()));
            document.add(new Paragraph("Coverage Amount: ₹" + request.getInsurance().getCoverageAmount()));
            document.add(new Paragraph("Payment Frequency: " + request.getInsurance().getPaymentFrequency()));
            document.add(new Paragraph("Purchase Date: " + request.getPurchaseDate()));

            document.close();
            return String.valueOf(uniquePdfId); // Return path to the stored PDF

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF receipt", e);
        }
    }
    public ResponseEntity<List<InsuranceDTO>> getCuratedInsurances(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND,"User not found"));
        log.info(user.toString());
        int age = user.getAge();
        String gender = user.getGender();
        double income = user.getIncome();

        List<Insurance> filteredInsurances = insuranceRepository.findCuratedInsurances(age, gender, income);

        List<InsuranceDTO> insuranceDTOs = filteredInsurances.stream()
                .map(insurance -> new ModelMapper().map(insurance, InsuranceDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(insuranceDTOs);
    }
//    public ResponseEntity<?> getInsuranceByUserId(Long userId) {
//        List<Purchase> byUserId = purchaseRepository.findByUserId(userId);
//        byUserId.stream().map(purchase -> insuranceRepository.purchase.getUser().getId())
//    }
}
