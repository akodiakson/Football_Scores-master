package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import barqsoft.footballscores.R;

public class FootballScoresWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballScoresStackRemoteViewsFactory(this, intent);
    }

    class FootballScoresStackRemoteViewsFactory implements  RemoteViewsService.RemoteViewsFactory{

        private Context mContext;
        private int mAppWidgetId;
        private List<FootballMatchScore> matchScores = new ArrayList<>();


        public FootballScoresStackRemoteViewsFactory(Context context, Intent intent){
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

            //TODO -- Remove: for test only
            for(int i = 0; i < 5; i++){
                matchScores.add(new FootballMatchScore("Harris", 0, "Austin", 3));
            }
        }

        @Override
        public void onDataSetChanged() {
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
            RemoteViews scoreView = new RemoteViews(mContext.getPackageName(), R.layout.app_widget_initial_layout);
            final FootballMatchScore matchScore = matchScores.get(position);
            scoreView.setTextViewText(R.id.home_team_name, matchScore.getHomeTeamName());
            scoreView.setTextViewText(R.id.home_team_score, String.valueOf(matchScore.getHomeTeamScore()));
            scoreView.setTextViewText(R.id.away_team_name, matchScore.getAwayTeamName());
            scoreView.setTextViewText(R.id.away_team_score, String.valueOf(matchScore.getAwayTeamScore()));
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
