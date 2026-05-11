package hu.bme.aut.widgetdemo

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.util.Date


class SimpleAppWidget : AppWidgetProvider() {
    private val ACTION_SIMPLEAPPWIDGET = "ACTION_BROADCASTWIDGETSAMPLE"


    fun updateAppWidget(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        val intent = Intent(context, SimpleAppWidget::class.java)
        intent.action = ACTION_SIMPLEAPPWIDGET
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.tvWidget, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (ACTION_SIMPLEAPPWIDGET == intent.action) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.tvWidget,
                Date(System.currentTimeMillis()).toString())
            val appWidget = ComponentName(context, SimpleAppWidget::class.java)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.updateAppWidget(appWidget, views)
        }
    }
}