package com.example.VolunteerHub.service;

import com.example.VolunteerHub.dto.request.RequiredSkillRequest;
import com.example.VolunteerHub.dto.response.InterestResponse;
import com.example.VolunteerHub.dto.response.RequiredSkillResponse;
import com.example.VolunteerHub.entity.Interests;
import com.example.VolunteerHub.entity.RequiredSkills;
import com.example.VolunteerHub.exception.AppException;
import com.example.VolunteerHub.exception.ErrorCode;
import com.example.VolunteerHub.mapper.InterestMapper;
import com.example.VolunteerHub.mapper.RequiredSkillMapper;
import com.example.VolunteerHub.repository.RequiredSkillRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequiredSkillService {
    RequiredSkillRepository requiredSkillRepository;

    public List<RequiredSkillResponse> getAll() {
        List<RequiredSkills> requiredSkills = requiredSkillRepository.findAll();

        return requiredSkills.stream().map(RequiredSkillMapper::toResponse).toList();
    }

    public RequiredSkills getRequiredSkillByString(String stringText) {
        RequiredSkills requiredSkill = requiredSkillRepository.findByValue(stringText.toLowerCase());

        if (requiredSkill == null)
            throw new AppException(ErrorCode.REQUIRED_SKILL_NOT_FOUND);

        return requiredSkill;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createRequiredSkill(RequiredSkillRequest request) {
        if (requiredSkillRepository.existsByValue(request.getValue()))
            throw new AppException(ErrorCode.REQUIRED_SKILL_EXISTED);

        RequiredSkills requiredSkill = RequiredSkillMapper.toRequiredSkill(request);

        requiredSkillRepository.save(requiredSkill);
    }
}
