package auction.controllers;

import auction.entities.RO.UserRO;
import auction.entities.User;
import auction.entities.enums.Role;
import auction.entities.utils.MessageUtils;
import auction.entities.utils.ResponseUtils;
import auction.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static auction.entities.utils.MessageUtils.RETRIEVE_SUCCESS;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.retrieveSuccess("Users"), userService.getAll()
        ));
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getUsersByRole(@RequestParam(required = false) Role role) {
        if (role == null) {
            return ResponseEntity.badRequest().body(ResponseUtils.buildErrorResponse(
                    HttpStatus.BAD_REQUEST, MessageUtils.invalidRequest("Role")
            ));
        }
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.retrieveSuccess("Users"), userService.getAllByFilter(role)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.retrieveSuccess("User"), userService.getUserById(id)
        ));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRO userRO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseUtils.buildErrorResponse(
                    HttpStatus.BAD_REQUEST, MessageUtils.validationErrors(bindingResult)
            ));
        }
        userService.save(userRO);
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.CREATED, MessageUtils.saveSuccess("User")
        ));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRO userRO, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseUtils.buildErrorResponse(
                    HttpStatus.BAD_REQUEST, MessageUtils.validationErrors(bindingResult)
            ));
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !loggedInUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseUtils.buildErrorResponse(
                    HttpStatus.FORBIDDEN, "You can only update your own account"
            ));
        }

        userService.update(id, userRO);
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.updateSuccess("User")
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.deleteSuccess("User")
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRO userRO, HttpSession session) {
        return userService.login(userRO.getUsername(), userRO.getPassword(), session);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(HttpStatus.OK, "Logged out successfully"));
    }
}