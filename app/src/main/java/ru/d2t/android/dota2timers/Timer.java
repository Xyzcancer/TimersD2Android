package ru.d2t.android.dota2timers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lylc.widget.circularprogressbar.CircularProgressBar;

import java.util.HashMap;
import java.util.TimerTask;

/**
 * Created by Violetta on 30.08.15.
 */
public class Timer {
    java.util.Timer timer;
    Activity activity;
    int res;
    Integer t;
    HashMap<Integer, String> notifications;
    HashMap<Integer, String> notificationsOrigin;

    public Timer (Activity activity, Integer res, Integer t, HashMap<Integer, String> notifications) {
        this.activity = activity;
        this.res = res;
        this.t = t;
        this.notifications = notifications;
        this.notificationsOrigin = notifications;
    }
    public void Start() {
        timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Update();
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public void Stop() {
        timer.cancel();
        timer.purge();
    }

    public void Update() {

    }

    public void Display() {
//добавить во view timer.xml, подставив в него картинку res и время t
        LinearLayout mainLayout = (LinearLayout) activity.findViewById(R.id.main_layout);
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View timerLayout = inflater.inflate(R.layout.timer, mainLayout, false);
        final CircularProgressBar c2 = (CircularProgressBar) timerLayout.findViewById(R.id.circularprogressbar2);
        c2.incrementProgressBy(0);
        c2.setMax(t * 60000);
        c2.setTitle("0:00");
        ImageView pic = (ImageView) timerLayout.findViewById(R.id.timerImage);
        final CircularProgressBar.ProgressAnimationListener l = new CircularProgressBar.ProgressAnimationListener() {

            @Override
            public void onAnimationStart() {
                c2.inProgress = true;
                c2.setProgress(0);
                notifications = (HashMap) notificationsOrigin.clone();
            }


            @Override
            public void onAnimationProgress(int progress) {
                int min =(int) Math.floor(progress/60000);
                int sec =(int) Math.floor((progress - min*60000)/1000);
                String secString = String.valueOf(sec);
                if (secString.length()<2) {
                    secString = "0" + secString;
                }
                if (c2.inProgress) c2.setTitle(String.valueOf(min)+":"+secString);
                if (notifications!=null && notifications.containsKey(min) && sec == 0) {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(activity)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle("Dota2Timers")
                                    .setContentText(notifications.get(min))
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setAutoCancel(true);
// Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(activity, MainActivity.class);
                    resultIntent.setAction(Intent.ACTION_MAIN);
                    resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    //resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent resultPendingIntent =PendingIntent.getActivity(activity, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                    mNotificationManager.notify(1, mBuilder.build());
                    final Toast toast = Toast.makeText(activity,
                            notifications.get(min), Toast.LENGTH_LONG);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 3500);
                    notifications.remove(min);
                }
            }

            @Override
            public void onAnimationFinish() {
                c2.inProgress = false;
            }
        };


        pic.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (c2.inProgress) {
                    c2.inProgress = false;
                    c2.setProgress(0);
                    c2.setTitle("0:00");
                    notifications = (HashMap) notificationsOrigin.clone();
                    c2.animateProgressTo(0, t, l);
                } else {
                    c2.inProgress = true;
                    c2.animateProgressTo(0, t, l);
                }
            }
        });
        pic.setImageResource(res);
        mainLayout.addView(timerLayout);
    }
}
