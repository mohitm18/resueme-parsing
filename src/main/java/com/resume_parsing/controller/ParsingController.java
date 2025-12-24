package com.resume_parsing.controller;

import com.resume_parsing.entity.Resume;
import com.resume_parsing.service.ParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ParsingController {
    @Autowired
    private ParsingService parsingService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> handleMultipleFileUpload(@RequestParam("files") MultipartFile[] files) throws Exception {
        List<String> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            Resume parsedResume = parsingService.parseAndSaveResume(file);
            responses.add("Parsed and saved: " + parsedResume.getCandidateName());
        }
        return ResponseEntity.ok(responses);
    }
}
