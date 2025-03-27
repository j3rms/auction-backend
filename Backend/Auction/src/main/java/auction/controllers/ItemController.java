package auction.controllers;

import auction.entities.RO.ItemRO;
import auction.entities.utils.MessageUtils;
import auction.entities.utils.ResponseUtils;
import auction.services.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> getAllItems() {
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.retrieveSuccess("Items"), itemService.getAll()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.retrieveSuccess("Item"), itemService.getItemById(id)
        ));
    }

    @PostMapping
    public ResponseEntity<?> createItem(@Valid @RequestBody ItemRO itemRO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseUtils.buildErrorResponse(
                    HttpStatus.BAD_REQUEST, MessageUtils.validationErrors(bindingResult)
            ));
        }
        itemService.save(itemRO);
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.CREATED, MessageUtils.saveSuccess("Item")
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRO itemRO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseUtils.buildErrorResponse(
                    HttpStatus.BAD_REQUEST, MessageUtils.validationErrors(bindingResult)
            ));
        }
        itemService.update(id, itemRO);
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.updateSuccess("Item")
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, MessageUtils.deleteSuccess("Item")
        ));
    }
}
