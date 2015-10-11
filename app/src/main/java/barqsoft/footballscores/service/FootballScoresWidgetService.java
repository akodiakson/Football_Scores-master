package barqsoft.footballscores.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

public class FootballScoresWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballScoresStackRemoteViewsFactory(this);
    }

    class FootballScoresStackRemoteViewsFactory implements  RemoteViewsService.RemoteViewsFactory{

        public static final String DATE_FORMAT = "yyyy-MM-dd";
        private Context mContext;
        private List<FootballMatchScore> matchScores = new ArrayList<>();


        public FootballScoresStackRemoteViewsFactory(Context context){
            mContext = context;
        }

        @Override
        public void onCreate() {
            //Intentionally left blank
        }

        @Override
        public void onDataSetChanged() {
            Uri uri = DatabaseContract.scores_table.buildScoreWithDate();

            Date currentDate = new Date();
            String formattedDate = new SimpleDateFormat(DATE_FORMAT).format(currentDate);

            final Cursor query = mContext.getContentResolver().query(uri, null, DatabaseContract.scores_table.DATE_COL, new String[]{formattedDate}, null);
            if(query.moveToFirst()){
                matchScores.clear();

                while(query.moveToNext()){
                    final int homeTeamNameIndex = query.getColumnIndex(DatabaseContract.scores_table.HOME_COL);
                    final int awayTeamNameIndex = query.getColumnIndex(DatabaseContract.scores_table.AWAY_COL);
                    final int homeTeamScoreIndex = query.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL);
                    final int awayTeamScoreIndex = query.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL);
                    final int gameTimeIndex = query.getColumnIndex(DatabaseContract.scores_table.TIME_COL);

                    final String homeTeam = query.getString(homeTeamNameIndex);
                    final String awayTeam = query.getString(awayTeamNameIndex);
                    final String homeTeamScore = query.getString(homeTeamScoreIndex);
                    final String awayTeamScore = query.getString(awayTeamScoreIndex);
                    final String gameTime = query.getString(gameTimeIndex);
                    matchScores.add(new FootballMatchScore(homeTeam, homeTeamScore, awayTeam, awayTeamScore, gameTime));
                }
            }
            query.close();
        }

        @Override
        public void onDestroy() {
            matchScores.clear();
        }

        @Override
        public int getCount() {
            return matchScores.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(matchScores.size() == 0){
                return null;
            }

            RemoteViews scoreView = new RemoteViews(mContext.getPackageName(), R.layout.app_widget_layout);

            final FootballMatchScore matchScore = matchScores.get(position);
            scoreView.setTextViewText(R.id.home_team_name, matchScore.getHomeTeamName());
            scoreView.setTextViewText(R.id.away_team_name, matchScore.getAwayTeamName());

            //If the game hasn't started yet, show the time instead.
            final int awayScore = Integer.parseInt(matchScore.getAwayTeamScore());
            final int homeScore = Integer.parseInt(matchScore.getHomeTeamScore());

            //The game hasn't started yet
            if(awayScore == -1 || homeScore == -1){
                //So hide the scores and show the time
                scoreView.setViewVisibility(R.id.score_column, View.GONE);
                scoreView.setViewVisibility(R.id.time_column, View.VISIBLE);
                scoreView.setTextViewText(R.id.game_time, matchScore.getGameTime());
            } else {
                scoreView.setViewVisibility(R.id.score_column, View.VISIBLE);
                scoreView.setViewVisibility(R.id.time_column, View.GONE);
                scoreView.setTextViewText(R.id.home_team_score, matchScore.getHomeTeamScore());
                scoreView.setTextViewText(R.id.away_team_score, matchScore.getAwayTeamScore());

                //The game is in progress or finished, so highlight the winning team
                final int tieGameId = -1;
                final boolean homeTeamIsWinning = homeScore > awayScore;
                final boolean awayTeamIsWinning = awayScore > homeScore;
                int winningTeamScoreId = homeTeamIsWinning ? R.id.home_team_score: awayTeamIsWinning ? R.id.away_team_score : tieGameId;
                int winningTeamNameId = homeTeamIsWinning ? R.id.home_team_name: awayTeamIsWinning ? R.id.away_team_name : tieGameId;
                scoreView.setTextColor(winningTeamScoreId, Color.BLACK);
                scoreView.setTextColor(winningTeamNameId, Color.BLACK);
            }

            return scoreView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
