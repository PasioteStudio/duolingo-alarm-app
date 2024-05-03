/*
Copyright (C) 2024  Wrichik Basu (basulabs.developer@gmail.com)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package in.basulabs.shakealarmclock.backend;

import static android.content.Context.POWER_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Date;
import java.util.Objects;

import in.basulabs.shakealarmclock.R;
import in.basulabs.shakealarmclock.backend.ConstantsAndStatics;
import in.basulabs.shakealarmclock.backend.Service_RingAlarm;
import in.basulabs.shakealarmclock.backend.Service_SetAlarmsPostBoot;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Objects.equals(intent.getAction(), ConstantsAndStatics.ACTION_DELIVER_ALARM)) {
			PowerManager powerManager = (PowerManager) context.getSystemService(
				POWER_SERVICE);
			PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK,
				"in.basulabs.shakealarmclock::AlarmRingServiceWakelockTag");
			wakeLock.acquire(60000);

			Intent intent1 = new Intent(context, Service_RingAlarm.class)
				.putExtra(ConstantsAndStatics.BUNDLE_KEY_ALARM_DETAILS,
					Objects.requireNonNull(intent.getExtras())
						.getBundle(ConstantsAndStatics.BUNDLE_KEY_ALARM_DETAILS));
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ContextCompat.startForegroundService(context, intent1);


		} else if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED) ||
			Objects.equals(intent.getAction(), Intent.ACTION_LOCKED_BOOT_COMPLETED)) {

			PowerManager powerManager = (PowerManager) context.getSystemService(
				POWER_SERVICE);
			PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK,
				"in.basulabs.shakealarmclock::AlarmUpdateServiceWakelockTag");
			wakeLock.acquire(60000);

			Intent intent1 = new Intent(context, Service_SetAlarmsPostBoot.class);
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ContextCompat.startForegroundService(context, intent1);
		}

	}
}
