package com.example.SecurityFinal.Api.Controller;


import com.example.SecurityFinal.Api.Entity.Product;
import com.example.SecurityFinal.Api.Model.Request.ProductRequestDTO;
import com.example.SecurityFinal.Api.Model.Response.ProductResponseDTO;
import com.example.SecurityFinal.Api.Service.ProductService;
import com.example.SecurityFinal.Api.Util.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/product")
@Validated
@Tag(name = "Product Controller", description = "provides basic functionalities for product management")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "create a product",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },responses = {
            @ApiResponse(description = "product Created", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDTO.class))),
    })
    @PostMapping("/add/to/organization/{organizationUUID}")
    public ResponseEntity<ProductResponseDTO> create(@PathVariable @NotBlank String organizationUUID,
                                                     @RequestBody @Valid ProductRequestDTO productRequestDTO){
        return ResponseEntity.ok(productService.create(organizationUUID, productRequestDTO));
    }

    @Operation(summary = "create a product",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "product Created", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDTO.class))),
    })
    @PostMapping("/add/to/project/{projectUUID}")
    public ResponseEntity<ProductResponseDTO> createToProject(@PathVariable @NotBlank String projectUUID,
                                                     @RequestBody @Valid ProductRequestDTO productRequestDTO){
        return ResponseEntity.ok(productService.createToProject(projectUUID, productRequestDTO));
    }


    @Operation(summary = "get products",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "products retrieved", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pageable.class))),
    })
    @GetMapping("/organization/{organizationUUID}/products")
    public ResponseEntity<PagedResponse<Product>> getProducts(@PathVariable @NotBlank String organizationUUID,
                                                              Pageable pageable){
        return ResponseEntity.ok(productService.getProducts(organizationUUID, pageable));
    }


    @Operation(summary = "get products",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "products retrieved", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pageable.class))),
    })
    @GetMapping("/project/{projectUUID}/products")
    public ResponseEntity<PagedResponse<Product>> getProductsProject(@PathVariable @NotBlank String projectUUID,
                                                                     Pageable pageable){
        return ResponseEntity.ok(productService.getProjectProducts(projectUUID, pageable));
    }

    @Operation(summary = "patch product",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER),
                    @Parameter(name = "organizationUUID", description = "organizationUUID", required = true),

            },
            responses = {
                    @ApiResponse(description = "product patched", responseCode = "200"),
            })
    @PatchMapping("/{productUUID}")
    public ResponseEntity<Void> patchProduct(@PathVariable String productUUID,
                                                      @RequestParam("organizationUUID") String organizationUUID,
                                                      @RequestBody Map<String,Object> values) throws MethodArgumentNotValidException {
        productService.patchProduct(organizationUUID, productUUID, values);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "delete product",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER),
                    @Parameter(name = "organizationUUID", description = "organizationUUID", required = true)
            },
            responses = {
                    @ApiResponse(description = "product Deleted", responseCode = "200"),
            })
    @DeleteMapping("/{productUUID}")
    public ResponseEntity<Void> delete(@PathVariable String productUUID,
                                       @RequestParam("organizationUUID") String organizationUUID){
        productService.delete(organizationUUID, productUUID);
        return ResponseEntity.ok().build();
    }



}
