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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/v1/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    private static final String PAGE_NUMBER_DEFAULT = "1";
    private static final String PAGE_SIZE_DEFAULT = "10";

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Certificate> deleteCertificate(@PathVariable Long id) {
        certificateService.deleteCertificate(id);

        return ResponseEntity.ok().build();
    }

    @JsonView(CertificateView.Views.V1.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateView> createCertificate(@RequestBody @JsonView(CreateCertificateView.Views.V1.class)
                                                                     CreateCertificateView createCertificateView) {
        Certificate certificate = CreateCertificateView.createForm(createCertificateView);
        Certificate createdCertificate = certificateService.createCertificate(certificate);
        CertificateView certificateView = CertificateView.createForm(createdCertificate);

        certificateView.add(linkTo(methodOn(CertificateController.class).createCertificate(createCertificateView)).withSelfRel());

        return new ResponseEntity<>(certificateView, HttpStatus.CREATED);
    }

    @JsonView(CertificateView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateView> getCertificateById(@PathVariable Long id) {
        Certificate certificate = certificateService.getCertificateById(id);
        CertificateView certificateView = CertificateView.createForm(certificate);

        certificateView.add(linkTo(methodOn(CertificateController.class).getCertificateById(id)).withSelfRel());

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @PatchMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateView> updatePartCertificate(@RequestBody @JsonView(UpdatePartCertificateView.Views.V1.class)
                                                                         UpdatePartCertificateView updatePartCertificateView,
                                                                 @PathVariable Long id) {
        Certificate certificateFromQuery = UpdatePartCertificateView.createForm(updatePartCertificateView);
        Certificate certificateToUpdate = certificateService.updatePartCertificate(certificateFromQuery, id);
        CertificateView certificateView = CertificateView.createForm(certificateToUpdate);

        certificateView.add(linkTo(methodOn(CertificateController.class).updatePartCertificate(updatePartCertificateView, id)).withSelfRel());

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateView> updateCertificate(@RequestBody @JsonView(UpdateCertificateView.Views.V1.class) UpdateCertificateView updateCertificateView,
                                                             @PathVariable Long id) {
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
                                                                            @RequestParam(required = false, defaultValue = PAGE_NUMBER_DEFAULT) Integer pageNumber,
                                                                            @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT) Integer pageSize) {
        List<Certificate> certificates = certificateService.getCertificates(tag, search, sort, pageNumber, pageSize);
        List<CertificateView> certificateView = CertificateView.createListForm(certificates);

        Link link = linkTo(methodOn(CertificateController.class).getCertificates(tag, search, sort, pageNumber, pageSize)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(certificateView, link), HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<CertificateView>> getCertificatesByTags(@RequestParam List<String> names,
                                                                                  @RequestParam(required = false, defaultValue = PAGE_NUMBER_DEFAULT) Integer pageNumber,
                                                                                  @RequestParam(required = false, defaultValue = PAGE_SIZE_DEFAULT) Integer pageSize) {
        List<Certificate> certificates = certificateService.getCertificatesByTags(names, pageNumber, pageSize);
        List<CertificateView> certificateView = CertificateView.createListForm(certificates);

        Link link = linkTo(methodOn(CertificateController.class).getCertificatesByTags(names, pageNumber, pageSize)).withSelfRel();

        return new ResponseEntity<>(CollectionModel.of(certificateView, link), HttpStatus.OK);
    }
}
