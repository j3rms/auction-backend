package auction.controllers;

import java.util.List;

import auction.exceptions.ServiceException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import auction.entities.utils.ResponseUtils;


@RestController
@RequestMapping("/seller-applications")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
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
    public ResponseEntity<?> createApplication(
            @RequestBody SellerApplicationRO applicationRO, HttpSession session) {

        try {
            // Pass session to the service layer to check if the user is logged in
            SellerApplication createdApplication = sellerApplicationService.createApplication(applicationRO, session);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.buildSuccessResponse(
                HttpStatus.CREATED, "Seller application submitted successfully", createdApplication
            ));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.buildErrorResponse(
                HttpStatus.UNAUTHORIZED, "You must be logged in to submit a seller application: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "Failed to submit seller application: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerApplication> updateApplication(
            @PathVariable Long id,
            @RequestBody SellerApplicationRO applicationRO,
            @RequestParam Long adminId,
            HttpSession session) {

        try {
            // Pass adminId and session (to check if the user is an admin) to the service layer
            SellerApplication updatedApplication = sellerApplicationService.updateApplication(id, applicationRO, adminId, session);
            return ResponseEntity.ok(updatedApplication);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        String message = sellerApplicationService.deleteApplication(id);
        return ResponseEntity.ok(message);
    }

}