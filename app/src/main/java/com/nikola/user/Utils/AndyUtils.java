package com.nikola.user.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.nikola.user.R;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by getit on 8/5/2016.
 */
public class AndyUtils {
    public static Dialog mDialog, load_dialog;
    public static Dialog mProgressDialog;

    public static void appLog(String msg1, String msg2) {
        Log.d(msg1, msg2);
    }

    public static void showLongToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static String message_json(String message_id, String type, String driver_id,
                                      String client_id, String request_id, String message) {

        JSONObject jObject = new JSONObject();
        try {

            jObject.put("id", message_id);
            jObject.put("type", type);
            jObject.put("driver_id", driver_id);
            jObject.put("client_id", client_id);
            jObject.put("request_id", request_id);
            jObject.put("message", message);

            return jObject.toString();

        } catch (JSONException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getPlaceAutoCompleteUrl(String input) {

        StringBuilder sb = null;
        try {
            sb = new StringBuilder(Const.PLACES_API_BASE
                    + Const.TYPE_AUTOCOMPLETE + Const.OUT_JSON);
            sb.append("?sensor=false&key=" + Const.PLACES_AUTOCOMPLETE_API_KEY);

            sb.append("&radius=500");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("FINAL URL:::   ", sb.toString());
        return sb.toString();
    }

    public static LatLng getLatLngFromAddress(String strAddress, Context context) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 3);
            if (address == null) {
                return null;
            }
            if (address != null && address.size() > 0) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static boolean eMailValidation(String emailstring) {
        if (null == emailstring || emailstring.length() == 0) {
            return false;
        }
        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailstring);
        return emailMatcher.matches();
    }

    public static void generateKeyHAsh(Context con) {

        try {
            PackageInfo info = con.getPackageManager().getPackageInfo(
                    con.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
    }


    public static void showSimpleProgressDialog(Context context, String msg, boolean isCancelable) {
        if (context != null) {


            mProgressDialog = new Dialog(context, R.style.DialogSlideAnim_leftright);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setContentView(R.layout.animation_loading);
            TextView tv_title = (TextView) mProgressDialog.findViewById(R.id.tv_title);
            tv_title.setText(msg);

            mProgressDialog.show();
        }
    }


    public static ProgressDialog getSimpleProgressDialog(Context context, String msg, boolean isCancelable) {
//        mDialog = new Dialog(context,R.style.DialogImageTheme);
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.setContentView(R.layout.dialog_progressbar);
//        mProgressDialog = (ProgressBar) mDialog.findViewById(R.id.circular_progressbar);
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

//        TextView please_wait_text= (TextView) mDialog.findViewById(R.id.tv_please_wait);
//        please_wait_text.setText(msg);
//        mDialog.setCancelable(isCancelable);
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return mProgressDialog;
    }

    public static void removeProgressDialog() {
        if (null != mProgressDialog)
            mProgressDialog.dismiss();
    }


    public static void hideKeyBoard(Context con) {

        InputMethodManager imm = (InputMethodManager) con.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // if (!imm.isAcceptingText())
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static String saveToSdCard(Bitmap bitmap, String filename) {

        String stored = null;

        File sdcard = Environment.getExternalStorageDirectory();

        File folder = new File(sdcard.getAbsoluteFile(), "Social");//the dot makes this directory hidden to the user
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(), filename + ".jpg");
        if (file.exists())
            return stored;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + "/Social/" + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static boolean checkifImageExists(String imagename) {
        Bitmap b = null;
        File file = getImage("/" + imagename + ".jpg");
        String path = file.getAbsolutePath();

        if (path != null)
            b = BitmapFactory.decodeFile(path);

        if (b == null || b.equals("")) {
            return false;
        }
        return true;
    }


    public static void showloader(Context context) {
        load_dialog = new Dialog(context, R.style.DialogThemeforview);
        load_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        load_dialog.setCancelable(false);
        load_dialog.setContentView(R.layout.bounce_layout);
        final RippleBackground rippleBackground = (RippleBackground) load_dialog.findViewById(R.id.bounce_marker_content);
        ImageView bounce_marker = (ImageView) load_dialog.findViewById(R.id.bounce_marker);
        rippleBackground.startRippleAnimation();
        load_dialog.show();
    }

    public static void removeLoader() {
        if (null != load_dialog && load_dialog.isShowing()) {
            load_dialog.dismiss();
        }
    }

    public static boolean canDrawOverlays(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }else{
            return Settings.canDrawOverlays(context);
        }
    }


}
