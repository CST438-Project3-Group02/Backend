package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.entity.Activity;
import com.roomie.entity.ActivityComment;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ActivityCommentRepository;
import com.roomie.repository.ActivityRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActivityCommentServiceTest {

    @Mock
    private ActivityCommentRepository activityCommentRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ActivityCommentService activityCommentService;

    private Profile testProfile;
    private Activity testActivity;
    private ActivityComment testComment;

    @BeforeEach
    void setUp() {
        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        setField(testProfile, "profileId", 1L);

        testActivity = new Activity(1, false);
        setField(testActivity, "activityId", 1L);

        testComment = new ActivityComment("Great job!");
        setField(testComment, "ActivitiesCommentId", 1L);
        testComment.setProfile(testProfile);
        testComment.setActivity(testActivity);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getCommentsByActivity_returnsComments() {
        when(
            activityCommentRepository.findByActivity_ActivityId(1L)
        ).thenReturn(List.of(testComment));

        List<ActivityComment> result =
            activityCommentService.getCommentsByActivity(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getComment()).isEqualTo("Great job!");
    }

    @Test
    void getCommentsByActivity_returnsEmpty_whenNone() {
        when(
            activityCommentRepository.findByActivity_ActivityId(99L)
        ).thenReturn(List.of());

        List<ActivityComment> result =
            activityCommentService.getCommentsByActivity(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getCommentsByProfile_returnsComments() {
        when(activityCommentRepository.findByProfile_ProfileId(1L)).thenReturn(
            List.of(testComment)
        );

        List<ActivityComment> result =
            activityCommentService.getCommentsByProfile(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getCommentById_returnsComment_whenFound() {
        when(activityCommentRepository.findById(1L)).thenReturn(
            Optional.of(testComment)
        );

        ActivityComment result = activityCommentService.getCommentById(1L);

        assertThat(result.getComment()).isEqualTo("Great job!");
    }

    @Test
    void getCommentById_throws_whenNotFound() {
        when(activityCommentRepository.findById(99L)).thenReturn(
            Optional.empty()
        );

        assertThatThrownBy(() ->
            activityCommentService.getCommentById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addComment_savesAndReturnsComment() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(activityRepository.findById(1L)).thenReturn(
            Optional.of(testActivity)
        );
        when(activityCommentRepository.save(any())).thenReturn(testComment);

        ActivityComment result = activityCommentService.addComment(
            1L,
            1L,
            "Great job!"
        );

        assertThat(result.getComment()).isEqualTo("Great job!");
        verify(activityCommentRepository, times(1)).save(any());
    }

    @Test
    void addComment_throws_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            activityCommentService.addComment(99L, 1L, "comment")
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addComment_throws_whenActivityNotFound() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            activityCommentService.addComment(1L, 99L, "comment")
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateComment_updatesAndReturnsComment() {
        when(activityCommentRepository.findById(1L)).thenReturn(
            Optional.of(testComment)
        );
        when(activityCommentRepository.save(any())).thenReturn(testComment);

        ActivityComment result = activityCommentService.updateComment(
            1L,
            "Updated comment"
        );

        assertThat(result).isNotNull();
        verify(activityCommentRepository, times(1)).save(any());
    }

    @Test
    void updateComment_throws_whenNotFound() {
        when(activityCommentRepository.findById(99L)).thenReturn(
            Optional.empty()
        );

        assertThatThrownBy(() ->
            activityCommentService.updateComment(99L, "comment")
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteComment_deletesWhenExists() {
        when(activityCommentRepository.existsById(1L)).thenReturn(true);

        activityCommentService.deleteComment(1L);

        verify(activityCommentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteComment_throws_whenNotFound() {
        when(activityCommentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() ->
            activityCommentService.deleteComment(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(activityCommentRepository, never()).deleteById(any());
    }
}
