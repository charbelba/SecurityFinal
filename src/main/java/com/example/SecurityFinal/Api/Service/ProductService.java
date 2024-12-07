package com.example.SecurityFinal.Api.Service;

import com.example.SecurityFinal.Api.Entity.Organization;
import com.example.SecurityFinal.Api.Entity.Project;
import com.example.SecurityFinal.Api.Exception.UserNotFoundException;
import com.example.SecurityFinal.Api.Model.Request.ProductRequestDTO;
import com.example.SecurityFinal.Api.Model.Response.ProductResponseDTO;
import com.example.SecurityFinal.Api.Repository.OrganizationRepository;
import com.example.SecurityFinal.Api.Repository.ProductRepository;
import com.example.SecurityFinal.Api.Repository.ProjectRepository;
import com.example.SecurityFinal.Api.Util.PagedResponse;
import com.example.SecurityFinal.Api.Util.ValidatorUtils;
import com.example.SecurityFinal.Api.Entity.Product;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;



    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Transactional
    public ProductResponseDTO create(String organizationUUID, ProductRequestDTO productRequestDTO) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        Product product = modelMapper.map(productRequestDTO, Product.class);
        product.setOrganization(organization);

        Product savedProduct = productRepository.save(product);



        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }

    @Transactional
    public ProductResponseDTO createToProject(String projectUUID, ProductRequestDTO productRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            throw new UserNotFoundException("User not found");
        }

        Project project = projectRepository.findByUuid(projectUUID)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        Product product = modelMapper.map(productRequestDTO, Product.class);
        product.setProject(project);
        product.setOrganization(project.getOrganization());

        Product savedProduct = productRepository.save(product);


        return modelMapper.map(savedProduct, ProductResponseDTO.class);
    }

    public PagedResponse<Product> getProducts(String organizationUUID, Pageable pageable) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        return PagedResponse.fromPage(productRepository.findByOrganizationId(organization.getId(), pageable));
    }

    public PagedResponse<Product> getProjectProducts(String projectUUID, Pageable pageable) {
        Project project = projectRepository.findByUuid(projectUUID)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        return PagedResponse.fromPage(productRepository.findByProjectId(project.getId(), pageable));
    }

    public void patchProduct(String organizationUUID, String productUUID, Map<String, Object> values) throws MethodArgumentNotValidException {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));


        Optional<Product> optionalProduct = productRepository.findByOrganization_UuidAndUuid(organizationUUID, productUUID);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            ValidatorUtils.validateAndApplyFields(product, new ProductRequestDTO(), values, Product.class, ProductRequestDTO.class);


            productRepository.save(product);
        } else {
            throw new EntityNotFoundException("Product not found with the given organizationUUID and productUUID");
        }
    }

    public void delete(String organizationUUID, String productUUID) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));
        Product product = productRepository.findByOrganization_UuidAndUuid(organizationUUID, productUUID)
                .orElseThrow(() -> new EntityNotFoundException("product not found"));
        productRepository.delete(product);
    }
}
