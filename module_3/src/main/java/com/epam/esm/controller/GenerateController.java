package com.epam.esm.controller;

import com.epam.esm.util.generator.CommonGenerate;
import com.epam.esm.view.TagView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/generate")
public class GenerateController {
    private CommonGenerate commonGenerate;

    @Autowired
    public GenerateController(CommonGenerate commonGenerate) {
        this.commonGenerate = commonGenerate;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity generate() {
        commonGenerate.execute();

        return ResponseEntity.ok().build();
    }
}
