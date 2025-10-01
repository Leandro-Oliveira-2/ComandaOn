package com.lanchonete.lanchon.models.category.controller;

import com.lanchonete.lanchon.models.category.dto.CategoryResponseDTO;
import com.lanchonete.lanchon.models.category.dto.CreateCategoryDTO;
import com.lanchonete.lanchon.models.category.dto.UpdateCategoryDTO;
import com.lanchonete.lanchon.models.category.service.CategoryService;
import com.lanchonete.lanchon.models.product.dto.ProductResponseDTO;
import com.lanchonete.lanchon.models.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categorias", description = "Gerencie categorias de produtos antes de criar itens e pedidos.")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Operation(summary = "Criar categoria", description = "Cadastra uma nova categoria para organizar o cardapio.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos")
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        CategoryResponseDTO category = categoryService.create(createCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(summary = "Atualizar categoria", description = "Atualiza nome, ordem ou status da categoria.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada"),
            @ApiResponse(responseCode = "404", description = "Categoria nao encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryDTO updateCategoryDTO) {
        CategoryResponseDTO category = categoryService.update(id, updateCategoryDTO);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Listar categorias", description = "Retorna somente os dados da categoria sem carregar produtos.")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Buscar categoria", description = "Recupera uma categoria especifica pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria nao encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @Operation(summary = "Listar produtos da categoria", description = "Lista produtos associados sem carregar categorias vizinhas.")
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findByCategoryId(id));
    }

    @Operation(summary = "Remover categoria", description = "Remove uma categoria que nao esteja em uso.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria removida"),
            @ApiResponse(responseCode = "404", description = "Categoria nao encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
