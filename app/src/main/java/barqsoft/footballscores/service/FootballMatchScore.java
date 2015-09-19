package barqsoft.footballscores.service;

public class FootballMatchScore {

    private String homeTeamName;
    private String awayTeamName;
    private String homeTeamScore;
    private String awayTeamScore;
    private String gameTime;

    public FootballMatchScore(String homeTeamName, String homeTeamScore, String awayTeamName, String awayTeamScore, String gameTime) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.gameTime = gameTime;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }


    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getHomeTeamScore() {
        return homeTeamScore;
    }


    public String getAwayTeamScore() {
        return awayTeamScore;
    }

    public String getGameTime() {
        return gameTime;
    }
}
