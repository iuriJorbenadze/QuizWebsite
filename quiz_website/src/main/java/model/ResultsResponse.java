package model;

import model.ResultInfo;

import java.util.List;

public class ResultsResponse {
    private int totalScore;
    private List<ResultInfo> quizResults;

    public ResultsResponse( List<ResultInfo> quizResults, int totalScore) {
        this.totalScore = totalScore;
        this.quizResults = quizResults;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public List<ResultInfo> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(List<ResultInfo> quizResults) {
        this.quizResults = quizResults;
    }
}