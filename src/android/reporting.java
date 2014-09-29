package android;

import android.app.*;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(
    formKey = "", // This is required for backward compatibility but not used
    //formUri = "http://www.backendofyourchoice.com/reportpath"
    mailTo = "deefactorial@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT }, 
    resToastText = R.string.crash_toast_text
)
public class reporting extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }

	public void sendErrorReport(Exception caughtException) {
		ACRA.getErrorReporter().handleException(caughtException);
	}
}


