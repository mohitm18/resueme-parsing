package com.resume_parsing.service;

import com.resume_parsing.dao.ParsingRepository;
import com.resume_parsing.entity.Resume;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ParsingService {

    @Autowired
    private ParsingRepository parsingRepository;

    public Resume parseAndSaveResume(MultipartFile file) throws Exception {
        String text = extractTextFromFile(file);
        Resume resume = new Resume();

        // Extract and set fields
      //  resume.setName(extractName(text));
        resume.setEmail(extractEmail(text));
     //   resume.setPhoneNumber(extractPhoneNumber(text));
        resume.setLinkedIn(extractLinkedIn(text));
       // resume.setSkills(extractSkills(text));
        resume.setExperience(extractExperienceYears(text));
        resume.setEducation(extractEducation(text));
      //  resume.setLocation(extractLocation(text));

        return parsingRepository.save(resume);
    }

    // ---------------- CORE METHODS ----------------

    private String extractTextFromFile(MultipartFile file) throws IOException, TikaException {
        Tika tika = new Tika();
        return tika.parseToString(file.getInputStream());
    }

    private String extractEmail(String text) {
        Matcher matcher = Pattern.compile("\\b[\\w.%-]+@[\\w.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? matcher.group() : null;
    }

    private String extractPhoneNumber(String text) {
        Matcher matcher = Pattern.compile("\\b(?:\\+91)?[\\s\\-]?(\\d[\\d\\s\\-]{8,15})\\b").matcher(text);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    private String extractName(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.length() > 40 || line.matches("(?i)(resume|curriculum vitae|cv).*")) continue;
            if (line.matches("^[A-Z][a-z]+(\\s+[A-Z][a-z]+)+$")) return line;
            if (line.matches("^[A-Z]+(\\s+[A-Z]+)+$")) return line;
        }
        return null;
    }

    private String extractLinkedIn(String text) {
        Matcher matcher = Pattern.compile("(https?://)?(www\\.)?linkedin\\.com/[A-Za-z0-9\\-_/]+", Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? matcher.group() : null;
    }

    private String extractSkills(String text) {
        List<String> knownSkills = Arrays.asList("Java", "Python", "JavaScript", "SQL", "AWS", "Selenium", "JMeter", "Git", "Jira");
        return knownSkills.stream()
                .filter(skill -> text.toLowerCase().contains(skill.toLowerCase()))
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private Float extractExperienceYears(String text) {
        Matcher matcher = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(years|yrs)", Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? Float.parseFloat(matcher.group(1)) : null;
    }

    private String extractEducation(String text) {
        // Simple placeholder logic – refine based on your needs
        Pattern pattern = Pattern.compile("(?i)(Bachelor|B\\.Tech|BSc|Master|M\\.Tech|MSc|PhD|MBA)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String extractLocation(String text) {
        String city = extractCity(text);
        String state = extractState(text);
        String country = extractCountry(text);
        return String.join(", ",
                Arrays.asList(city, state, country).stream()
                        .filter(part -> part != null && !part.isBlank())
                        .collect(Collectors.toList()));
    }

    private String extractCity(String text) {
        Matcher matcher = Pattern.compile("\\b(Pune|Mumbai|Delhi|Bangalore|Hyderabad|Chennai|Nashik)\\b", Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractState(String text) {
        Matcher matcher = Pattern.compile("\\b(Maharashtra|Karnataka|Telangana|Delhi|California|Texas)\\b", Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractCountry(String text) {
        Matcher matcher = Pattern.compile("\\b(India|USA|UK|Canada|Australia)\\b", Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }
}
