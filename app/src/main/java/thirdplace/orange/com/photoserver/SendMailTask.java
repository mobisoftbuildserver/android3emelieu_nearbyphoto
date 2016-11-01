package thirdplace.orange.com.photoserver;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SendMailTask extends AsyncTask<RequestMail, Void,Void> {
    private final Activity mActivity;
    private com.google.api.services.gmail.Gmail mService = null;
    private Exception mLastError = null;

    private GoogleAccountCredential mCredential;

    public SendMailTask(GoogleAccountCredential credential,Activity activity) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mCredential = credential;
        mActivity = activity;
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Gmail API Android Quickstart")
                .build();
    }

    /**
     * Background task to call Gmail API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(RequestMail... params) {
        try {
            sendEmail(params[0]);
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
        }
        return null;
    }

    /**
     * Fetch a list of Gmail labels attached to the specified account.
     * @return List of Strings labels.
     * @throws IOException
     */
    private void sendEmail(RequestMail reqMail) throws IOException {
        // Get the labels in the user's account.
        String user = "me";
        //TODO
        try {
            MimeMessage mimeMessage = MailUtil.createEmailWithAttachment(reqMail.getEmail(),reqMail.getSender(),"photo","nouvelle photo",reqMail.getFile());
            com.google.api.services.gmail.model.Message mess = MailUtil.createMessageWithEmail(mimeMessage);
            mCredential.setSelectedAccountName(reqMail.getSender());
            mService.users().messages().send(user, mess).execute();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onCancelled() {
        if (mLastError != null) {
           if (mLastError instanceof UserRecoverableAuthIOException) {
                mActivity.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        MainActivity.REQUEST_AUTHORIZATION);
            } else {
            }
        } else {
        }
    }
}