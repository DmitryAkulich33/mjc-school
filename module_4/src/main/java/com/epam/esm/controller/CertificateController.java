package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.view.CertificateView;
import com.epam.esm.view.CreateCertificateView;
import com.epam.esm.view.UpdateCertificateView;
import com.epam.esm.view.UpdatePartCertificateView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping(value = "/api/v1/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Certificate> deleteCertificate(@PathVariable @NotNull @Positive Long id) {
        certificateService.deleteCertificate(id);

        return ResponseEntity.ok().build();
    }

    @JsonView(CertificateView.Views.V1.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CertificateView> createCertificate(@Valid @RequestBody @JsonView(CreateCertificateView.Views.V1.class)
                                                                     CreateCertificateView createCertificateView, BindingResult result) {
        Certificate certificate = CreateCertificateView.createForm(createCertificateView);
        Certificate createdCertificate = certificateService.createCertificate(certificate);
        CertificateView certificateView = CertificateView.createForm(createdCertificate);

        certificateView.add(linkTo(methodOn(CertificateController.class).createCertificate(createCertificateView, null)).withSelfRel());

        return new ResponseEntity<>(certificateView, HttpStatus.CREATED);
    }

    @JsonView(CertificateView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CertificateView> getCertificateById(@PathVariable @NotNull @Positive Long id) {
        Certificate certificate = certificateService.getCertificateById(id);
        CertificateView certificateView = CertificateView.createForm(certificate);

        certificateView.add(linkTo(methodOn(CertificateController.class).getCertificateById(id)).withSelfRel());

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @PatchMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CertificateView> updatePartCertificate(@Valid @RequestBody @JsonView(UpdatePartCertificateView.Views.V1.class)
                                                                         UpdatePartCertificateView updatePartCertificateView,
                                                                 @PathVariable @NotNull @Positive Long id) {
        Certificate certificateFromQuery = UpdatePartCertificateView.createForm(updatePartCertificateView);
        Certificate certificateToUpdate = certificateService.updatePartCertificate(certificateFromQuery, id);
        CertificateView certificateView = CertificateView.createForm(certificateToUpdate);

        certificateView.add(linkTo(methodOn(CertificateController.class).updatePartCertificate(updatePartCertificateView, id)).withSelfRel());

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CertificateView> updateCertificate(@Valid @RequestBody @JsonView(UpdateCertificateView.Views.V1.class) UpdateCertificateView updateCertificateView,
                                                             @PathVariable @NotNull @Positive Long id) {
        Certificate certificateFromQuery = UpdateCertificateView.createForm(updateCertificateView);
        Certificate certificateToUpdate = certificateService.updateCertificate(certificateFromQuery, id);
        CertificateView certificateView = CertificateView.createForm(certificateToUpdate);

        certificateView.add(linkTo(methodOn(CertificateController.class).updateCertificate(updateCertificateView, id)).withSelfRel());

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<CertificateView>> getCertificates(@RequestParam(required = false) String tag,
                                                                            @RequestParam(required = false) String search,
                                                                            @RequestParam(required = false) String sort,
                                                                            @RequestParam(required = false) @Positive Integer pageNumber,
                                                                            @RequestParam(required = false) @Positive Integer pageSize) {
        List<Certificate> certificates = certificateService.getCertificates(tag, search, sort, pageNumber, pageSize);
        List<CertificateView> certificateView = CertificateView.createListForm(certificates);

        Link link = linkTo(methodOn(CertificateController.class).getCertificates(tag, search, sort, pageNumber, pageSize)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(certificateView, link), HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<CertificateView>> getCertificatesByTags(@RequestParam @NotNull @NotEmpty List<String> names,
                                                                                  @RequestParam(required = false) @Positive Integer pageNumber,
                                                                                  @RequestParam(required = false) @Positive Integer pageSize) {
        List<Certificate> certificates = certificateService.getCertificatesByTags(names, pageNumber, pageSize);
        List<CertificateView> certificateView = CertificateView.createListForm(certificates);

        Link link = linkTo(methodOn(CertificateController.class).getCertificatesByTags(names, pageNumber, pageSize)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(certificateView, link), HttpStatus.OK);
    }
}
