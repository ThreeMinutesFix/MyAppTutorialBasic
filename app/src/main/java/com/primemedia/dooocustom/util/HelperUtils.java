package com.primemedia.dooocustom.util;


import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.graphics.ColorUtils;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dooo.android.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.primemedia.dooocustom.ui.Splash;
import com.primemedia.dooocustom.ui.constant.AppConfig;
import com.primemedia.dooocustom.ui.payments.Subscription;
import com.primemedia.dooocustom.ui.registers.LoginSignup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import es.dmoral.toasty.Toasty;

public class HelperUtils {
    private Activity activity;

    public HelperUtils(Activity activity) {
        this.activity = activity;
    }

    public boolean isVpnConnectionAvailable(){
        String iface = "";
        try {
            for (NetworkInterface networkInst : Collections.list(NetworkInterface.getNetworkInterfaces())){
                if (networkInst.isUp())
                    iface = networkInst.getName();
                if ( iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }


        }catch (SocketException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean cr(Activity activity, boolean allowRoot) {
        if(!allowRoot) {
            for(String pathDir : System.getenv("PATH").split(":")){
                if(new File(pathDir, "su").exists()) {
                    return true;
                } else {
                    ApplicationInfo restrictedApp = getRootApp(activity);
                    if (restrictedApp != null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static ApplicationInfo getRootApp(Activity activity) {
        ApplicationInfo restrictPackageInfo = null;
        final PackageManager pm = activity.getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals("com.thirdparty.superuser") ||
                    packageInfo.packageName.equals("eu.chainfire.supersu") ||
                    packageInfo.packageName.equals("com.noshufou.android.su") ||
                    packageInfo.packageName.equals("com.koushikdutta.superuser") ||
                    packageInfo.packageName.equals("com.zachspong.temprootremovejb") ||
                    packageInfo.packageName.equals("com.ramdroid.appquarantine") ||
                    packageInfo.packageName.equals("com.topjohnwu.magisk") ||
                    packageInfo.packageName.equals("me.weishu.kernelsu")
            ) {
                //restrictPackageName = packageInfo.packageName;
                //restrictPackageName = packageInfo.loadLabel(activity.getPackageManager()).toString();
                restrictPackageInfo = packageInfo;
            }
        }

        return restrictPackageInfo;
    }

    public ApplicationInfo getRestrictApp() {
        ApplicationInfo restrictPackageInfo = null;
        final PackageManager pm = activity.getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals("com.guoshi.httpcanary") ||
                    packageInfo.packageName.equals("app.greyshirts.sslcapture") ||
                    packageInfo.packageName.equals("com.guoshi.httpcanary.premium") ||
                    packageInfo.packageName.equals("com.minhui.networkcapture.pro") ||
                    packageInfo.packageName.equals("com.minhui.networkcapture") ||
                    packageInfo.packageName.equals("com.egorovandreyrm.pcapremote") ||
                    packageInfo.packageName.equals("com.packagesniffer.frtparlak") ||
                    packageInfo.packageName.equals("jp.co.taosoftware.android.packetcapture") ||
                    packageInfo.packageName.equals("com.emanuelef.remote_capture") ||
                    packageInfo.packageName.equals("com.minhui.wifianalyzer") ||
                    packageInfo.packageName.equals("com.evbadroid.proxymon") ||
                    packageInfo.packageName.equals("com.evbadroid.wicapdemo") ||
                    packageInfo.packageName.equals("com.evbadroid.wicap") ||
                    packageInfo.packageName.equals("com.luckypatchers.luckypatcherinstaller") ||
                    packageInfo.packageName.equals("ru.UbLBBRLf.jSziIaUjL")
            ) {
                //restrictPackageName = packageInfo.packageName;
                //restrictPackageName = packageInfo.loadLabel(activity.getPackageManager()).toString();
                restrictPackageInfo = packageInfo;
            }
        }

        return restrictPackageInfo;
    }

    public boolean isForeground( String myPackage){
        ActivityManager manager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        Log.e("test", "Background Apps: " + componentInfo.getPackageName());
        return componentInfo != null && componentInfo.getPackageName().equals(myPackage);
    }

    public boolean isAppRunning(Context context, String packageName){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        if (processInfos != null){
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos){
                if (processInfo.processName.equals(packageName)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void showWarningDialog(Activity context, String title, String message, int Animation) {
        MaterialDialog mDialog = new MaterialDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setAnimation(Animation)
                .setPositiveButton("Exit", R.drawable.ic_baseline_exit, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        System.exit(0);
                        context.finish();
                    }
                })
                .build();

        // Show dialog
        mDialog.show();
    }

    public void showMsgDialog(Activity context, String title, String message, String animation) {
        /*MaterialDialog mDialog = new MaterialDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setAnimation(Animation)
                .setPositiveButton("Dismiss", R.drawable.close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show dialog
        mDialog.show();*/
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.initial_msg_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);

        if(animation.equals("")) {
            lottieAnimationView.setVisibility(View.GONE);
        } else {
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.setAnimationFromUrl(animation);
        }

        TextView msgTitle = dialog.findViewById(R.id.msgTitle);
        msgTitle.setText(title);

        TextView msgBody = dialog.findViewById(R.id.msgBody);
        msgBody.setText(message);

        Button dialogClose = dialog.findViewById(R.id.Dialog_Close);
        dialogClose.setOnClickListener(v1 -> dialog.dismiss());

        dialog.show();
    }

    public void Buy_Premium_Dialog(Activity context, String title, String message, int Animation) {
        MaterialDialog mDialog = new MaterialDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setAnimation(Animation)
                .setPositiveButton("Upgrade", new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", MODE_PRIVATE);
                        if (sharedPreferences.getString("UserData", null) != null) {
                            dialogInterface.dismiss();
                            Intent subscriptionActivity = new Intent(context, Subscription.class);
                            context.startActivity(subscriptionActivity);
                        } else {
                            dialogInterface.dismiss();
                            Intent loginSingupActivity = new Intent(context, LoginSignup.class);
                            context.startActivity(loginSingupActivity);
                        }

                    }
                })
                .setNegativeButton("Cancel", R.drawable.baseline_close_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show dialog
        mDialog.show();
    }


    public boolean isInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (Exception ex)
        {
            return false;
        }
    }

    public static String getYearFromDate(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = null;
        try {
            parsedDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(parsedDate);
    }

    public static void setWatchLog(Context context, String user_id, int content_id, int content_type, String apiKey) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.url + "addwatchlog", response -> {
            try
            {
                Integer.parseInt(response);
                Log.d("test", "Watch Log Added!");
            } catch (NumberFormatException ex)
            {
                Log.d("test", "Watch Log Not Added!");
            }

        }, error -> {
            // Do nothing because There is No Error if error It will return 0
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("content_id", String.valueOf(content_id));
                params.put("content_type", String.valueOf(content_type));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-api-key", apiKey);
                return params;
            }
        };
        queue.add(sr);
    }

    public static void setViewLog(Context context, String user_id, int content_id, int content_type, String apiKey) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.url + "addviewlog", response -> {
            try
            {
                Integer.parseInt(response);
                Log.d("test", "View Log Added!");
            } catch (NumberFormatException ex)
            {
                Log.d("test", "View Log Not Added!");
            }

        }, error -> {
            // Do nothing because There is No Error if error It will return 0
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("content_id", String.valueOf(content_id));
                params.put("content_type", String.valueOf(content_type));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-api-key", apiKey);
                return params;
            }
        };
        queue.add(sr);
    }

    public static boolean checkStoragePermission(Context context) {
        if (checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("test", "Permission is granted");
            return true;
        } else {
            Log.d("test", "Permission is revoked");
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    public String getStringBetweenTwoChars(String input, String startChar, String endChar) {
        try {
            int start = input.indexOf(startChar);
            if (start != -1) {
                int end = input.indexOf(endChar, start + startChar.length());
                if (end != -1) {
                    return input.substring(start + startChar.length(), end);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input; // return null; || return "" ;
    }

    public static void resetPassword(Context context) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.forgot_password);

        TextView forgetPasswordEmailEditText = bottomSheetDialog.findViewById(R.id.Forget_Password_Email_EditText);
        View sendOtp = bottomSheetDialog.findViewById(R.id.Send_OTP);
        //---------Send OTP----------//
        sendOtp.setOnClickListener(view1 -> {
            if(forgetPasswordEmailEditText.getText().toString().matches("")) {
                Toasty.warning(context, "Please Enter Your Email Address Correctly.", Toast.LENGTH_SHORT, true).show();
            } else {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.url +"passwordResetMail", response -> {
                    switch (response) {
                        case "Email Not Registered":
                            Toasty.warning(context, "Email Not Registered", Toast.LENGTH_SHORT, true).show();
                            break;
                        case "Message has been sent":
                            Toasty.success(context, "Code Sended To the Mail Address!", Toast.LENGTH_SHORT, true).show();
                            bottomSheetDialog.dismiss();
                            checkPassword(context);
                            break;
                        case "":
                        case "Something Went Wrong!":
                            Toasty.error(context, "Something Went Wrong!", Toast.LENGTH_SHORT, true).show();
                            break;
                        default:
                            Toasty.error(context, "Something Went Wrong!", Toast.LENGTH_SHORT, true).show();
                            break;
                    }

                }, error -> {
                    // Do nothing
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("x-api-key", AppConfig.apiKey);
                        return params;
                    }
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("mail",forgetPasswordEmailEditText.getText().toString().trim());
                        return params;
                    }
                };
                sr.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(sr);
            }
        });

        bottomSheetDialog.show();
    }

    public static void checkPassword(Context context) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.reset_password_otp);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText editText1 = bottomSheetDialog.findViewById(R.id.editText1);
        EditText editText2 = bottomSheetDialog.findViewById(R.id.editText2);
        EditText editText3 = bottomSheetDialog.findViewById(R.id.editText3);
        EditText editText4 = bottomSheetDialog.findViewById(R.id.editText4);
        editText1.addTextChangedListener(new GenericTextWatcher(editText1, editText2));
        editText2.addTextChangedListener(new GenericTextWatcher(editText2, editText3));
        editText3.addTextChangedListener(new GenericTextWatcher(editText3, editText4));
        editText4.addTextChangedListener(new GenericTextWatcher(editText4, null));

        editText2.setOnKeyListener(new GenericKeyEvent(editText2, editText1));
        editText3.setOnKeyListener(new GenericKeyEvent(editText3, editText2));
        editText4.setOnKeyListener(new GenericKeyEvent(editText4, editText3));

        View verifyOtp = bottomSheetDialog.findViewById(R.id.verifyOtp);
        verifyOtp.setOnClickListener(view->{
            if(editText1.getText().toString().matches("") && editText2.getText().toString().matches("")
             && editText3.getText().toString().matches("") && editText4.getText().toString().matches("")) {
                Toasty.warning(context, "Please Enter a valid OTP.", Toast.LENGTH_SHORT, true).show();
            } else {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.url + "passwordResetCheckCode", response -> {
                    switch (response) {
                        case "Expired":
                            Toasty.warning(context, "Code already Expired!", Toast.LENGTH_SHORT, true).show();
                            break;
                        case "valid Code":
                            bottomSheetDialog.dismiss();
                            changePassword(context, editText1.getText().toString() + editText2.getText().toString() + editText3.getText().toString() + editText4.getText().toString());
                            break;
                        case "Used Code":
                            Toasty.warning(context, "Code already Used!", Toast.LENGTH_SHORT, true).show();
                            break;
                        case "Invalid Request":
                            Toasty.error(context, "Something Went Wrong!", Toast.LENGTH_SHORT, true).show();
                            break;
                        default:
                            Toasty.error(context, "Something Went Wrong!", Toast.LENGTH_SHORT, true).show();
                            break;
                    }

                }, error -> {
                    //Do Nothing
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("x-api-key", AppConfig.apiKey);
                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("code", editText1.getText().toString() + editText2.getText().toString() + editText3.getText().toString() + editText4.getText().toString());
                        return params;
                    }
                };
                queue.add(sr);
            }
        });

        bottomSheetDialog.show();
    }

    public static void changePassword(Context context, String code) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.reset_password);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText newPassEditText = bottomSheetDialog.findViewById(R.id.newPassEditText);
        EditText newConfirmPassEditText = bottomSheetDialog.findViewById(R.id.newConfirmPassEditText);

        View resetPass = bottomSheetDialog.findViewById(R.id.resetPass);
        //---------Send OTP----------//
        resetPass.setOnClickListener(view1 -> {
            if(newPassEditText.getText().toString().matches("") && newConfirmPassEditText.getText().toString().matches("")
             && newPassEditText.getText().toString().equals(newConfirmPassEditText.getText().toString())) {
                Toasty.warning(context, "Please Enter a Valid Password!", Toast.LENGTH_SHORT, true).show();
            } else {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest sr = new StringRequest(Request.Method.POST, AppConfig.url +"passwordResetPassword", response -> {
                    switch (response) {
                        case "Password Updated successfully":
                            Toasty.success(context, "Password Updated successfully!", Toast.LENGTH_SHORT, true).show();
                            bottomSheetDialog.dismiss();
                            SharedPreferences settings = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
                            settings.edit().clear().apply();

                            Intent splashActivity = new Intent(context, Splash.class);
                            splashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(splashActivity);
                            break;
                        case "":
                        case "Invalid Request":
                        case "Something Went Wrong!":
                            Toasty.error(context, "Something Went Wrong!", Toast.LENGTH_SHORT, true).show();
                            break;
                        default:
                            Toasty.success(context, "Something Went Wrong!", Toast.LENGTH_SHORT, true).show();
                            break;
                    }

                }, error -> {
                    //Do Nothing
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("x-api-key", AppConfig.apiKey);
                        return params;
                    }
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<>();
                        params.put("code",code);
                        params.put("pass",Utils.getMd5(newPassEditText.getText().toString()));
                        return params;
                    }
                };
                queue.add(sr);
            }
        });

        bottomSheetDialog.show();
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isColorDark(int color){
        return ColorUtils.calculateLuminance(color) < 0.5;
    }

    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }

    public static Boolean isFirstOpen(Context context){
        TinyDB tinyDB = new TinyDB(context);
        Boolean isFirstRun = tinyDB.getBoolean("isFirstRun");
        if(!isFirstRun) {
            tinyDB.putBoolean("isFirstRun", true);
            return true;
        } else {
            return false;
        }
    }

    public static String getApplicationName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static String getAlphaColor(int value) {
        Map<Integer, String> map = new HashMap<>();
        map.put(303, "FF");
        map.put(302, "FF");
        map.put(301, "FF");
        map.put(300, "FC");
        map.put(299, "FC");
        map.put(298, "FC");
        map.put(297, "FA");
        map.put(296, "FA");
        map.put(295, "FA");
        map.put(294, "F7");
        map.put(293, "F7");
        map.put(292, "F7");
        map.put(291, "F5");
        map.put(290, "F5");
        map.put(289, "F5");
        map.put(288, "F2");
        map.put(287, "F2");
        map.put(286, "F2");
        map.put(285, "F0");
        map.put(284, "F0");
        map.put(283, "F0");
        map.put(282, "ED");
        map.put(281, "ED");
        map.put(280, "ED");
        map.put(279, "EB");
        map.put(278, "EB");
        map.put(277, "EB");
        map.put(276, "E8");
        map.put(275, "E8");
        map.put(274, "E8");
        map.put(273, "E6");
        map.put(272, "E6");
        map.put(271, "E6");
        map.put(270, "E3");
        map.put(269, "E3");
        map.put(268, "E3");
        map.put(267, "E0");
        map.put(266, "E0");
        map.put(265, "E0");
        map.put(264, "DE");
        map.put(263, "DE");
        map.put(262, "DE");
        map.put(261, "DB");
        map.put(260, "DB");
        map.put(259, "DB");
        map.put(258, "D9");
        map.put(257, "D9");
        map.put(256, "D9");
        map.put(255, "D6");
        map.put(254, "D6");
        map.put(253, "D6");
        map.put(252, "D4");
        map.put(251, "D4");
        map.put(250, "D4");
        map.put(249, "D1");
        map.put(248, "D1");
        map.put(247, "D1");
        map.put(246, "CF");
        map.put(245, "CF");
        map.put(244, "CF");
        map.put(243, "CC");
        map.put(242, "CC");
        map.put(241, "CC");
        map.put(240, "C9");
        map.put(239, "C9");
        map.put(238, "C9");
        map.put(237, "C7");
        map.put(236, "C7");
        map.put(235, "C7");
        map.put(234, "C4");
        map.put(233, "C4");
        map.put(232, "C4");
        map.put(231, "C2");
        map.put(230, "C2");
        map.put(229, "C2");
        map.put(228, "BF");
        map.put(227, "BF");
        map.put(226, "BF");
        map.put(225, "BD");
        map.put(224, "BD");
        map.put(223, "BD");
        map.put(222, "BA");
        map.put(221, "BA");
        map.put(220, "BA");
        map.put(219, "B8");
        map.put(218, "B8");
        map.put(217, "B8");
        map.put(216, "B5");
        map.put(215, "B5");
        map.put(214, "B5");
        map.put(213, "B3");
        map.put(212, "B3");
        map.put(211, "B3");
        map.put(210, "B0");
        map.put(209, "B0");
        map.put(208, "B0");
        map.put(207, "AD");
        map.put(206, "AD");
        map.put(205, "AD");
        map.put(204, "AB");
        map.put(203, "AB");
        map.put(202, "AB");
        map.put(201, "A8");
        map.put(200, "A8");
        map.put(199, "A8");
        map.put(198, "A6");
        map.put(197, "A6");
        map.put(196, "A6");
        map.put(195, "A3");
        map.put(194, "A3");
        map.put(193, "A3");
        map.put(192, "A1");
        map.put(191, "A1");
        map.put(190, "A1");
        map.put(189, "9E");
        map.put(188, "9E");
        map.put(187, "9E");
        map.put(186, "9C");
        map.put(185, "9C");
        map.put(184, "9C");
        map.put(183, "99");
        map.put(182, "99");
        map.put(181, "99");
        map.put(180, "96");
        map.put(179, "96");
        map.put(178, "96");
        map.put(177, "94");
        map.put(176, "94");
        map.put(175, "94");
        map.put(174, "91");
        map.put(173, "91");
        map.put(172, "91");
        map.put(171, "8F");
        map.put(170, "8F");
        map.put(169, "8F");
        map.put(168, "8C");
        map.put(167, "8C");
        map.put(166, "8C");
        map.put(165, "8A");
        map.put(164, "8A");
        map.put(163, "8A");
        map.put(162, "87");
        map.put(161, "87");
        map.put(160, "87");
        map.put(159, "85");
        map.put(158, "85");
        map.put(157, "85");
        map.put(156, "82");
        map.put(155, "82");
        map.put(154, "82");
        map.put(153, "80");
        map.put(152, "80");
        map.put(151, "80");
        map.put(150, "7D");
        map.put(149, "7D");
        map.put(148, "7D");
        map.put(147, "7A");
        map.put(146, "7A");
        map.put(145, "7A");
        map.put(144, "78");
        map.put(143, "78");
        map.put(142, "78");
        map.put(141, "75");
        map.put(140, "75");
        map.put(139, "75");
        map.put(138, "73");
        map.put(137, "73");
        map.put(136, "73");
        map.put(135, "70");
        map.put(134, "70");
        map.put(133, "70");
        map.put(132, "6E");
        map.put(131, "6E");
        map.put(130, "6E");
        map.put(129, "6B");
        map.put(128, "6B");
        map.put(127, "6B");
        map.put(126, "69");
        map.put(125, "69");
        map.put(124, "69");
        map.put(123, "66");
        map.put(122, "66");
        map.put(121, "66");
        map.put(120, "63");
        map.put(119, "63");
        map.put(118, "63");
        map.put(117, "61");
        map.put(116, "61");
        map.put(115, "61");
        map.put(114, "5E");
        map.put(113, "5E");
        map.put(112, "5E");
        map.put(111, "5C");
        map.put(110, "5C");
        map.put(109, "5C");
        map.put(108, "59");
        map.put(107, "59");
        map.put(106, "59");
        map.put(105, "57");
        map.put(104, "57");
        map.put(103, "57");
        map.put(102, "54");
        map.put(101, "54");
        map.put(100, "54");
        map.put(99, "52");
        map.put(98, "52");
        map.put(97, "52");
        map.put(96, "4F");
        map.put(95, "4F");
        map.put(94, "4F");
        map.put(93, "4F");
        map.put(92, "4D");
        map.put(91, "4D");
        map.put(90, "4D");
        map.put(89, "4A");
        map.put(88, "4A");
        map.put(87, "4A");
        map.put(86, "47");
        map.put(85, "47");
        map.put(84, "47");
        map.put(83, "45");
        map.put(82, "45");
        map.put(81, "45");
        map.put(80, "42");
        map.put(79, "42");
        map.put(78, "42");
        map.put(77, "40");
        map.put(76, "40");
        map.put(75, "40");
        map.put(74, "3D");
        map.put(73, "3D");
        map.put(72, "3D");
        map.put(71, "3B");
        map.put(70, "3B");
        map.put(69, "3B");
        map.put(68, "38");
        map.put(67, "38");
        map.put(66, "38");
        map.put(65, "36");
        map.put(64, "36");
        map.put(63, "36");
        map.put(62, "33");
        map.put(61, "33");
        map.put(60, "33");
        map.put(59, "30");
        map.put(58, "30");
        map.put(57, "30");
        map.put(56, "2E");
        map.put(55, "2E");
        map.put(54, "2E");
        map.put(53, "2B");
        map.put(52, "2B");
        map.put(51, "2B");
        map.put(50, "29");
        map.put(49, "29");
        map.put(48, "29");
        map.put(47, "26");
        map.put(46, "26");
        map.put(45, "26");
        map.put(44, "24");
        map.put(43, "24");
        map.put(42, "24");
        map.put(41, "21");
        map.put(40, "21");
        map.put(39, "21");
        map.put(38, "1F");
        map.put(37, "1F");
        map.put(36, "1F");
        map.put(35, "1C");
        map.put(34, "1C");
        map.put(33, "1C");
        map.put(32, "1A");
        map.put(31, "1A");
        map.put(30, "1A");
        map.put(29, "17");
        map.put(28, "17");
        map.put(27, "17");
        map.put(26, "14");
        map.put(25, "14");
        map.put(24, "14");
        map.put(23, "12");
        map.put(22, "12");
        map.put(21, "12");
        map.put(20, "0F");
        map.put(19, "0F");
        map.put(18, "0F");
        map.put(17, "0D");
        map.put(16, "0D");
        map.put(15, "0D");
        map.put(14, "0A");
        map.put(13, "0A");
        map.put(12, "0A");
        map.put(11, "08");
        map.put(10, "08");
        map.put(9, "08");
        map.put(8, "05");
        map.put(7, "05");
        map.put(6, "05");
        map.put(5, "03");
        map.put(4, "03");
        map.put(3, "03");
        map.put(2, "00");
        map.put(1, "00");
        map.put(0, "00");



        if(map.get(value)!=null) {
            return map.get(value);
        } else {
            return map.get(303);
        }

    }

    public static String getCurrencyName(int value) {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "INR");
        map.put(1, "USD");
        map.put(2, "EUR");
        map.put(3, "GBP");
        map.put(4, "JPY");
        map.put(5, "CHF");
        map.put(6, "CAD");
        map.put(7, "AUD");
        map.put(8, "NZD");
        map.put(9, "CNY");
        map.put(10, "BRL");
        map.put(11, "RUB");
        map.put(12, "ZAR");
        map.put(13, "MXN");
        map.put(14, "SGD");
        map.put(15, "HKD");
        map.put(16, "NOK");
        map.put(17, "SEK");
        map.put(18, "DKK");
        map.put(19, "TRY");
        map.put(20, "AED");
        map.put(21, "SAR");
        map.put(22, "KRW");
        map.put(23, "IDR");
        map.put(24, "MYR");
        map.put(25, "THB");
        map.put(26, "PLN");
        map.put(27, "HUF");
        map.put(28, "CZK");
        map.put(29, "ISK");
        map.put(30, "CLP");
        map.put(31, "COP");
        map.put(32, "ARS");
        map.put(33, "PEN");
        map.put(34, "UAH");
        map.put(35, "QAR");
        map.put(36, "BHD");
        map.put(37, "OMR");
        map.put(38, "KWD");
        map.put(39, "DZD");
        map.put(40, "BBD");
        map.put(41, "BZD");
        map.put(42, "BMD");
        map.put(43, "BTN");
        map.put(44, "BOB");
        map.put(45, "BAM");
        map.put(46, "BWP");
        map.put(47, "BND");
        map.put(48, "BGN");
        map.put(49, "MMK");
        map.put(50, "BIF");
        map.put(51, "KHR");
        map.put(52, "XAF");
        map.put(53, "XOF");
        map.put(54, "CVE");
        map.put(55, "KYD");
        map.put(56, "XPF");
        map.put(57, "CLF");
        map.put(58, "CUP");
        map.put(59, "CUC");
        map.put(60, "DJF");
        map.put(61, "DOP");
        map.put(62, "EGP");
        map.put(63, "ERN");
        map.put(64, "ETB");
        map.put(65, "FKP");
        map.put(66, "FJD");
        map.put(67, "GMD");
        map.put(68, "GEL");
        map.put(69, "GHS");
        map.put(70, "GIP");
        map.put(71, "GTQ");
        map.put(72, "GNF");
        map.put(73, "GYD");
        map.put(74, "HTG");
        map.put(75, "HNL");
        map.put(76, "HRK");
        map.put(77, "HTG");
        map.put(78, "HNL");
        map.put(79, "HRK");
        map.put(80, "HTG");
        map.put(81, "HNL");
        map.put(82, "HRK");
        map.put(83, "HUF");
        map.put(84, "IDR");
        map.put(85, "ILS");
        map.put(86, "IMP");
        map.put(87, "IRR");
        map.put(88, "IQD");
        map.put(89, "IRT");
        map.put(90, "BDT");


        if(map.get(value)!=null) {
            return map.get(value);
        } else {
            return map.get("");
        }
    }

    public static String getCurrencyNameWithSymbol(int value) {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "INR (₹)");
        map.put(1, "USD ($)");
        map.put(2, "EUR (€)");
        map.put(3, "GBP (£)");
        map.put(4, "JPY (¥)");
        map.put(5, "CHF (CHF)");
        map.put(6, "CAD (CA$)");
        map.put(7, "AUD (A$)");
        map.put(8, "NZD (NZ$)");
        map.put(9, "CNY (¥)");
        map.put(10, "BRL (R$)");
        map.put(11, "RUB (₽)");
        map.put(12, "ZAR (R)");
        map.put(13, "MXN (Mex$)");
        map.put(14, "SGD (S$)");
        map.put(15, "HKD (HK$)");
        map.put(16, "NOK (kr)");
        map.put(17, "SEK (kr)");
        map.put(18, "DKK (kr)");
        map.put(19, "TRY (₺)");
        map.put(20, "AED (د.إ)");
        map.put(21, "SAR (﷼)");
        map.put(22, "KRW (₩)");
        map.put(23, "IDR (Rp)");
        map.put(24, "MYR (RM)");
        map.put(25, "THB (฿)");
        map.put(26, "PLN (zł)");
        map.put(27, "HUF (Ft)");
        map.put(28, "CZK (Kč)");
        map.put(29, "ISK (kr)");
        map.put(30, "CLP (CLP$)");
        map.put(31, "COP (COL$)");
        map.put(32, "ARS ($)");
        map.put(33, "PEN (S/.)");
        map.put(34, "UAH (₴)");
        map.put(35, "QAR (﷼)");
        map.put(36, "BHD (.د.ب)");
        map.put(37, "OMR (﷼)");
        map.put(38, "KWD (د.ك)");
        map.put(39, "DZD (DA)");
        map.put(40, "BBD (Bds$)");
        map.put(41, "BZD (BZ$)");
        map.put(42, "BMD (BD$)");
        map.put(43, "BTN (Nu.)");
        map.put(44, "BOB (Bs.)");
        map.put(45, "BAM (KM)");
        map.put(46, "BWP (P)");
        map.put(47, "BND (B$)");
        map.put(48, "BGN (лв)");
        map.put(49, "MMK (Ks)");
        map.put(50, "BIF (FBu)");
        map.put(51, "KHR (៛)");
        map.put(52, "XAF (FCFA)");
        map.put(53, "XOF (CFA)");
        map.put(54, "CVE (Esc)");
        map.put(55, "KYD (CI$)");
        map.put(56, "XPF (CFP)");
        map.put(57, "CLF (UF)");
        map.put(58, "CUP (CUP)");
        map.put(59, "CUC (CUC)");
        map.put(60, "DJF (Fdj)");
        map.put(61, "DOP (RD$)");
        map.put(62, "EGP (E£)");
        map.put(63, "ERN (Nfk)");
        map.put(64, "ETB (Br)");
        map.put(65, "FKP (£)");
        map.put(66, "FJD (FJ$)");
        map.put(67, "GMD (D)");
        map.put(68, "GEL (₾)");
        map.put(69, "GHS (GH₵)");
        map.put(70, "GIP (£)");
        map.put(71, "GTQ (Q)");
        map.put(72, "GNF (GNF)");
        map.put(73, "GYD (GY$)");
        map.put(74, "HTG (G)");
        map.put(75, "HNL (L)");
        map.put(76, "HRK (kn)");
        map.put(77, "HTG (G)");
        map.put(78, "HNL (L)");
        map.put(79, "HRK (kn)");
        map.put(80, "HTG (G)");
        map.put(81, "HNL (L)");
        map.put(82, "HRK (kn)");
        map.put(83, "HUF (Ft)");
        map.put(84, "IDR (Rp)");
        map.put(85, "ILS (₪)");
        map.put(86, "IMP (£)");
        map.put(87, "IRR (﷼)");
        map.put(88, "IQD (ع.د)");
        map.put(89, "IRT (﷼)");
        map.put(90, "BDT (৳)");


        if(map.get(value)!=null) {
            return map.get(value);
        } else {
            return map.get("");
        }
    }
}
