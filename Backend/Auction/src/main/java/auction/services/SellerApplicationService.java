package auction.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.entities.RO.SellerApplicationRO;
import auction.entities.SellerApplication;
import auction.entities.User;
import auction.entities.enums.Role;
import auction.repositories.SellerApplicationRepository;
import auction.repositories.UserRepository;


@Service
public class SellerApplicationService {

    @Autowired
    private SellerApplicationRepository sellerApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SellerApplication> getAllApplications() {
        return sellerApplicationRepository.findAll();
    }

    public Optional<SellerApplication> getApplicationById(Long id) {
        return sellerApplicationRepository.findById(id);
    }

    public SellerApplication createApplication(SellerApplicationRO applicationRO) {
        SellerApplication application = applicationRO.toEntity();

        // Fetch the existing user (seller) from DB
        User user = userRepository.findById(applicationRO.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        application.setUser(user);

        // Fetch the existing admin if provided
        if (applicationRO.getAdmin() != null && applicationRO.getAdmin().getUserId() != null) {
            User admin = userRepository.findById(applicationRO.getAdmin().getUserId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            application.setAdmin(admin);
        }

        return sellerApplicationRepository.save(application);
    }


    public SellerApplication updateApplication(Long id, SellerApplicationRO applicationRO, Long adminId) {
        Optional<SellerApplication> existingApplication = sellerApplicationRepository.findById(id);
        if (existingApplication.isPresent()) {
            SellerApplication application = existingApplication.get();

            // Fetch the admin user
            User adminUser = userRepository.findById(adminId)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            // Only allow admins to approve applications
            if (!adminUser.getRole().equals(Role.ADMIN)) {
                throw new RuntimeException("Only admins can approve applications.");
            }

            // Update fields
            application.setStatus(applicationRO.getStatus());
            application.setAppliedAt(applicationRO.getAppliedAt());
            application.setApprovedAt(applicationRO.getApprovedAt());

            // Set the admin who approved the application
            application.setAdmin(adminUser);

            return sellerApplicationRepository.save(application);
        }
        throw new RuntimeException("Application not found");
    }



    public String deleteApplication(Long id) {
        if (!sellerApplicationRepository.existsById(id)) {
            throw new RuntimeException("Application not found.");
        }

        sellerApplicationRepository.deleteById(id);
        return "Seller application with ID " + id + " has been successfully deleted.";
    }

}