package auction.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auction.entities.RO.SellerApplicationRO;
import auction.entities.SellerApplication;
import auction.services.SellerApplicationService;
import auction.services.UserService;


@RestController
@RequestMapping("/seller-applications")
public class SellerApplicationController {

    @Autowired
    private SellerApplicationService sellerApplicationService;

    @Autowired
    private UserService userService; // ✅ Inject UserService

    @GetMapping
    public List<SellerApplication> getAllApplications() {
        return sellerApplicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public SellerApplication getApplicationById(@PathVariable Long id) {
        return sellerApplicationService.getApplicationById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    @PostMapping
    public SellerApplication createApplication(@RequestBody SellerApplicationRO applicationRO) {
        return sellerApplicationService.createApplication(applicationRO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerApplication> updateApplication(
            @PathVariable Long id,
            @RequestBody SellerApplicationRO applicationRO,
            @RequestParam Long adminId) {

        // Pass adminId (Long) instead of User object
        SellerApplication updatedApplication = sellerApplicationService.updateApplication(id, applicationRO, adminId);
        return ResponseEntity.ok(updatedApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        String message = sellerApplicationService.deleteApplication(id);
        return ResponseEntity.ok(message);
    }

}