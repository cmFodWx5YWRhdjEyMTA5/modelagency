package com.talentnew.activities.agency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.talentnew.R;
import com.talentnew.activities.common.BaseImageActivity;
import com.talentnew.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UploadDocumentActivity extends BaseImageActivity {

    private Button button_upload_id_proof, button_upload_gst,button_submit;
    private int docType;
    private String gstFilePath, idFilePath;
    TextView tv_id_proof, tvGst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        initViews();
    }

    private void initViews(){
        button_upload_id_proof = findViewById(R.id.button_upload_id_proof);
        button_upload_gst = findViewById(R.id.button_upload_gst);
        tv_id_proof = findViewById(R.id.tv_id_proof);
        tvGst = findViewById(R.id.tv_gst);

        final String gstPic = sharedPreferences.getString(Constants.GST_DOC,"");
        final String idProofPic =sharedPreferences.getString(Constants.ID_PROOF_DOC, "");
        if(!TextUtils.isEmpty(gstPic))
            tvGst.setText("Verified");
        else {
            button_upload_id_proof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    docType = 2;
                    selectImage();
                }
            });
        }
        if(!TextUtils.isEmpty(idProofPic))
            tv_id_proof.setText("Verified");
        else {
            button_upload_gst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    docType = 1;
                    selectImage();
                }
            });
        }

        button_submit = findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(idProofPic) && !TextUtils.isEmpty(gstPic))
                    showMyDialog("All Documents are Uploaded.");
                else
                uploadDocuments();
            }
        });
    }

    private void uploadDocuments(){
        if(TextUtils.isEmpty(gstFilePath) && TextUtils.isEmpty(idFilePath)){
            showMyDialog("Please Upload Document.");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        if(!TextUtils.isEmpty(gstFilePath))
            params.put("gstPic", convertToBase64(new File(gstFilePath)));
        if(!TextUtils.isEmpty(idFilePath))
            params.put("idProofPic", convertToBase64(new File(idFilePath)));

        String url = getResources().getString(R.string.url)+Constants.UPLOAD_AGENCY_DOCUMENTS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"uploadDocuments");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try {
            if(apiName.equals("uploadDocuments")){
                if(jsonObject.getBoolean("status")){
                    if(jsonObject.getInt("statusCode") == 1){
                        editor.putString(Constants.GST_DOC,jsonObject.getJSONObject("result").getString("gstPic"));
                        editor.putString(Constants.ID_PROOF_DOC,jsonObject.getJSONObject("result").getString("idProofPic"));
                        editor.commit();
                        showMyDialog("Document Submitted Sucessfully");
                    }
                }else{
                    showMyDialog(jsonObject.getString("message"));
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    protected void imageAdded() {
        super.imageAdded();
        if(docType==1){
            File file = new File(imagePath);
            tvGst.setText(file.getName());
            gstFilePath = imagePath;
        }else {
            File file = new File(imagePath);

            tv_id_proof.setText(file.getName());
            idFilePath = imagePath;
        }
    }

    @Override
    public void onDialogPositiveClicked() {
        super.onDialogPositiveClicked();
        finish();
    }
}
