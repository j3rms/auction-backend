package auction.controllers;

import auction.entities.Category;
import auction.entities.Item;
import auction.entities.RO.ItemRO;
import auction.entities.enums.ItemStatus;
import auction.entities.response.SuccessResponse;
import auction.entities.utils.MessageUtils;
import auction.entities.utils.ResponseUtils;
import auction.services.ItemService;
import auction.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

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

    @GetMapping("/filter")
    public ResponseEntity<?> getItemsByFilter(
            @RequestParam(required = false) ItemStatus status,
            @RequestParam(required = false) Long categoryId) {

        if (categoryId != null && categoryService.getById(categoryId) == null) {
            return ResponseEntity.badRequest().body(ResponseUtils.buildErrorResponse(
                    HttpStatus.BAD_REQUEST, "Invalid category ID"
            ));
        }

        List<Item> items = itemService.getAllByFilter(status, categoryId);

        return ResponseEntity.ok(ResponseUtils.buildSuccessResponse(
                HttpStatus.OK, "Filtered items retrieved successfully", items
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
