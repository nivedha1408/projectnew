package com.example.Quiznew.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizSubmissionDto {
    private Long userId;
    private List<Long> selectedOptionIds;

    public Long getUserId() {
        return userId; }
    public void setUserId(Long userId) {
        this.userId = userId; }

    public List<Long> getSelectedOptionIds() {
        return selectedOptionIds; }
    public void setSelectedOptionIds(List<Long> selectedOptionIds) {
        this.selectedOptionIds = selectedOptionIds; }
}