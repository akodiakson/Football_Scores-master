package barqsoft.footballscores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class scoresAdapter extends CursorAdapter
{
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    public scoresAdapter(Context context,Cursor cursor,int flags)
    {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @SuppressLint("NewApi")
    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        final String homeTeamName = cursor.getString(COL_HOME);
        final String awayTeamName = cursor.getString(COL_AWAY);
        final int homeTeamGoals = cursor.getInt(COL_HOME_GOALS);
        final int awayTeamGoals = cursor.getInt(COL_AWAY_GOALS);
        final String matchTime = cursor.getString(COL_MATCHTIME);
        final String space = " ";

        mHolder.home_name.setText(homeTeamName);
        mHolder.home_name.setContentDescription(context.getString(R.string.a11y_home_team_name, homeTeamName));
        mHolder.away_name.setText(awayTeamName);
        mHolder.away_name.setContentDescription(context.getString(R.string.a11y_away_team_name, awayTeamName));
        mHolder.date.setText(matchTime);



        boolean gameHasStarted = Utilies.hasGameStarted(homeTeamGoals, awayTeamGoals);

        if(gameHasStarted){
            mHolder.home_team_score.setText(String.valueOf(homeTeamGoals));
            mHolder.away_team_score.setText(String.valueOf(awayTeamGoals));
            mHolder.home_team_score.setContentDescription(context.getString(R.string.game_score) + homeTeamGoals);
            mHolder.away_team_score.setContentDescription(context.getString(R.string.game_score) + awayTeamGoals);
            mHolder.date.setContentDescription(context.getString(R.string.game_time) + space + matchTime);
            highlightWinningTeam(mHolder, homeTeamGoals, awayTeamGoals);
        } else {
            final String noScore = String.valueOf(0);
            mHolder.home_team_score.setText(noScore);
            mHolder.away_team_score.setText(noScore);
            mHolder.home_team_score.setContentDescription(context.getString(R.string.game_score) + noScore);
            mHolder.away_team_score.setContentDescription(context.getString(R.string.game_score) + noScore);
            mHolder.date.setContentDescription(context.getString(R.string.game_time) + space + matchTime + space + context.getString(R.string.game_has_not_started));
        }
        mHolder.match_id = cursor.getDouble(COL_ID);
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if(mHolder.match_id == detail_match_id)
        {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");


            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_VIEW_FOCUSED);
            event.setContentDescription(context.getResources().getString(R.string.match_details));
            container.requestSendAccessibilityEvent(container.getChildAt(0), event);
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utilies.getMatchDay(cursor.getInt(COL_MATCHDAY),
                    cursor.getInt(COL_LEAGUE)));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utilies.getLeague(context, cursor.getInt(COL_LEAGUE)));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(
                            mHolder.home_name.getText()+ space
                            +mHolder.home_team_score.getText()+ space
                            +mHolder.away_name.getText() + space + mHolder.away_team_score.getText()));
                }
            });
        } else
        {
            container.removeAllViews();
        }

    }

    private void highlightWinningTeam(ViewHolder mHolder, int homeTeamGoals, int awayTeamGoals) {
        if(homeTeamGoals > awayTeamGoals){
            mHolder.home_name.setTextColor(Color.BLACK);
            mHolder.home_team_score.setTextColor(Color.BLACK);
            mHolder.away_name.setTextColor(Color.DKGRAY);
            mHolder.away_team_score.setTextColor(Color.DKGRAY);
        } else if(awayTeamGoals > homeTeamGoals){
            mHolder.away_name.setTextColor(Color.BLACK);
            mHolder.away_team_score.setTextColor(Color.BLACK);
            mHolder.home_name.setTextColor(Color.DKGRAY);
            mHolder.home_team_score.setTextColor(Color.DKGRAY);
        } else {
            mHolder.away_name.setTextColor(Color.DKGRAY);
            mHolder.away_team_score.setTextColor(Color.DKGRAY);
            mHolder.home_name.setTextColor(Color.DKGRAY);
            mHolder.home_team_score.setTextColor(Color.DKGRAY);
        }
    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + " " + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

}
