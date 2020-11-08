package com.epam.esm.controller;

import com.epam.esm.domain.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.view.View;
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

    @JsonView(View.V1.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate certificate) {
        Certificate createdCertificate = certificateService.createCertificate(certificate);
        return new ResponseEntity<>(createdCertificate, HttpStatus.CREATED);
    }

    @JsonView(View.V1.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> getCertificateById(@PathVariable Long id) {
        Certificate certificate = certificateService.getCertificateById(id);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @JsonView(View.V1.class)
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Certificate> updateCertificate(@RequestBody Certificate certificate,
                                                         @PathVariable Long id) {
        Certificate certificateToUpdate = certificateService.updateCertificate(certificate, id);
        return new ResponseEntity<>(certificateToUpdate, HttpStatus.OK);
    }

    @JsonView(View.V1.class)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Certificate>> getCertificates(@RequestParam(required = false) String tag,
                                                             @RequestParam(required = false) String search,
                                                             @RequestParam(required = false) String sort) {
        List<Certificate> certificates = certificateService.getCertificates(tag, search, sort);
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }
}
