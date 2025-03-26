package com.intask.Task.controller;

import com.intask.Task.DTO.InsuranceDTO;
import com.intask.Task.DTO.PurchaseDTO;
import com.intask.Task.DTO.PurchaseRequestDTO;
import com.intask.Task.DTO.SaveInsuranceDTO;
import com.intask.Task.Service.InsuranceService;
import com.intask.Task.Service.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;
    public final UserServices userServices;

    // Get All Insurances
    @GetMapping
    public ResponseEntity<?> getAllInsurances() {
        ResponseEntity<List<?>> allInsurances = insuranceService.getAllInsurances();
        return ResponseEntity.ok(allInsurances);
    }

    // Purchase Insurance
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseInsurance(@RequestBody PurchaseRequestDTO request) {
        ResponseEntity<?> purchaseDTO = insuranceService.purchaseInsurance(request);
        return ResponseEntity.ok(purchaseDTO);
    }

    @PostMapping("/addAll")
    public ResponseEntity<?> addAllInsurances(@RequestBody List<SaveInsuranceDTO> request) {
        ResponseEntity<?> responseEntity = insuranceService.AddInsurance(request);

        return ResponseEntity.ok(responseEntity);
    }
    @GetMapping("/download/{purchaseId}")
    public ResponseEntity<Resource> downloadReceipt(@PathVariable UUID purchaseId) {
        try {
            Path filePath = Paths.get("receipts/" + purchaseId + ".pdf");
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + purchaseId + ".pdf")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



//    @GetMapping("/{UserId}")
//    public ResponseEntity<?> getInsuranceById(@PathVariable Long UserId) {}

}

