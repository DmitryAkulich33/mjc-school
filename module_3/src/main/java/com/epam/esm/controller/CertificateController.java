package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.view.CertificateView;
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

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate certificate) {
        Certificate createdCertificate = certificateService.createCertificate(certificate);
        return new ResponseEntity<>(createdCertificate, HttpStatus.CREATED);
    }

    @JsonView(CertificateView.Views.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateView> getCertificateById(@PathVariable Long id) {
        Certificate certificate = certificateService.getCertificateById(id);
        CertificateView certificateView = CertificateView.createForm(certificate);

        return new ResponseEntity<>(certificateView, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> updateCertificate(@RequestBody Certificate certificate,
                                                         @PathVariable Long id) {
        Certificate certificateToUpdate = certificateService.updateCertificate(certificate, id);
        return new ResponseEntity<>(certificateToUpdate, HttpStatus.OK);
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
