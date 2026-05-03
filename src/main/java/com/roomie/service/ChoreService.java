package com.roomie.service;

import com.roomie.dto.ChoreDTO;
import com.roomie.entity.Chore;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ChoreRepository;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChoreService {

    private final ChoreRepository choreRepository;
    private final ProfileRepository profileRepository;
    private final HouseholdRepository householdRepository;

    public ChoreService(
        ChoreRepository choreRepository,
        ProfileRepository profileRepository,
        HouseholdRepository householdRepository
    ) {
        this.choreRepository = choreRepository;
        this.profileRepository = profileRepository;
        this.householdRepository = householdRepository;
    }

    public ChoreDTO toDTO(Chore chore) {
        return new ChoreDTO(chore);
    }

    public List<ChoreDTO> getAllChores() {
        return choreRepository
            .findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public ChoreDTO getChoreById(Long id) {
        Chore chore = choreRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Chore", id));
        return toDTO(chore);
    }

    public List<ChoreDTO> getChoresByHousehold(Long householdId) {
        return choreRepository
            .findByHousehold_HouseholdId(householdId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<ChoreDTO> getChoresByProfile(Long profileId) {
        return choreRepository
            .findByProfile_ProfileId(profileId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<ChoreDTO> getChoresByHouseholdAndStatus(
        Long householdId,
        boolean isCompleted
    ) {
        return choreRepository
            .findByHousehold_HouseholdIdAndIsCompleted(householdId, isCompleted)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<ChoreDTO> getChoresByProfileAndStatus(
        Long profileId,
        boolean isCompleted
    ) {
        return choreRepository
            .findByProfile_ProfileIdAndIsCompleted(profileId, isCompleted)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public Chore createChore(Long profileId, Long householdId, Chore chore) {
        Profile profile = profileRepository
            .findById(profileId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Profile", profileId)
            );
        Household household = householdRepository
            .findById(householdId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Household", householdId)
            );

        chore.setProfile(profile);
        chore.setHousehold(household);
        return choreRepository.save(chore);
    }

    public ChoreDTO updateChore(Long id, Chore updatedChore) {
        Chore existing = choreRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Chore", id));
        existing.setChoreName(updatedChore.getChoreName());
        existing.setChoreDescription(updatedChore.getChoreDescription());
        existing.setRepeatInterval(updatedChore.getRepeatInterval());
        existing.setIsCompleted(updatedChore.isCompleted());
        existing.setCompleteBy(updatedChore.getCompleteBy());
        return toDTO(choreRepository.save(existing));
    }

    public ChoreDTO markComplete(Long id) {
        Chore existing = choreRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Chore", id));
        existing.setIsCompleted(true);
        return toDTO(choreRepository.save(existing));
    }

    public void deleteChore(Long id) {
        if (!choreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Chore", id);
        }
        choreRepository.deleteById(id);
    }
}
