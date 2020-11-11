package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.view.CertificateView;
import com.epam.esm.view.CreateCertificateView;
import com.epam.esm.view.UpdateCertificateView;
import com.epam.esm.view.UpdatePartCertificateView;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/certificates")
public class CertificateController {
    private final CertificateService certificateService;

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

        return new ResponseEntity<>(certificateView, HttpStatus.CREATED);
    }

    @JsonView(CertificateView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateView> getCertificateById(@PathVariable Long id) {
        Certificate certificate = certificateService.getCertificateById(id);
        CertificateView certificateView = CertificateView.createForm(certificate);

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

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateView> updateCertificate(@RequestBody @JsonView(UpdateCertificateView.Views.V1.class) UpdateCertificateView updateCertificateView,
                                                             @PathVariable Long id) {
        Certificate certificateFromQuery = UpdateCertificateView.createForm(updateCertificateView);
        Certificate certificateToUpdate = certificateService.updateCertificate(certificateFromQuery, id);
        CertificateView certificateView = CertificateView.createForm(certificateToUpdate);

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @JsonView(CertificateView.Views.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CertificateView>> getCertificates(@RequestParam(required = false) String tag,
                                                                 @RequestParam(required = false) String search,
                                                                 @RequestParam(required = false) String sort) {
        List<Certificate> certificates = certificateService.getCertificates(tag, search, sort);
        List<CertificateView> certificateView = CertificateView.createListForm(certificates);

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }
}
