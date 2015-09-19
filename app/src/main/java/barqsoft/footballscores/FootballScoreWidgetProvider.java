package barqsoft.footballscores;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.Calendar;

import barqsoft.footballscores.service.FootballScoresWidgetService;
import barqsoft.footballscores.service.myFetchService;

/**
 * Implementation of App Widget functionality.
 */
public class FootballScoreWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        System.out.println("ASDF onReceive");
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        System.out.println("FootballScoreWidgetProvider.updateAppWidget");
        System.out.println("context = " + context);

        Intent intent = new Intent(context, FootballScoresWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_stack_football_scores);
        rv.setRemoteAdapter(appWidgetId,R.id.stack_view, intent);
        rv.setEmptyView(R.id.stack_view, R.id.empty_view);

        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_initial_layout);
//        views.setTextViewText(R.id.home_team_name, "Harrisburg");
//        views.setTextViewText(R.id.away_team_name, "Albequerque");
//        views.setTextViewText(R.id.home_team_score, "4");
//        views.setTextViewText(R.id.away_team_score, "2");

        // Create an Intent to launch ExampleActivity
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_initial_layout);
//        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);


        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);

//        Intent service_start = new Intent(context, myFetchService.class);
//        context.startService(service_start);
    }

}

