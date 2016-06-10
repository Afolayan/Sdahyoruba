package com.jcedar.sdahyoruba.io.jsonhandlers;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.jcedar.sdahyoruba.helper.Lists;
import com.jcedar.sdahyoruba.io.model.Student;
import com.jcedar.sdahyoruba.provider.DataContract;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Afolayan on 13/10/2015.
 */
public class StudentUpdateHandler extends JSONHandler{
    private static final String TAG = StudentUpdateHandler.class.getSimpleName();
    private int studentCount;
    public StudentUpdateHandler(Context context) {
        super(context);
    }

    @Override
    public ArrayList<ContentProviderOperation> parse(String json) throws IOException {
        if(json == null){
            return null;
        }
        Log.d(TAG, TextUtils.isEmpty(json) ? "Empty  Json" : json);

        final ArrayList<ContentProviderOperation> batch = Lists.newArrayList();

        Student[] currentStudent = Student.fromJson(json);
        studentCount = currentStudent.length;


        for ( Student student: currentStudent) {
            Log.e(TAG, "id is "+student.getId());
            try {
                Uri uri = DataContract.addCallerIsSyncAdapterParameter(
                        DataContract.Hymns.CONTENT_URI);
                ContentProviderOperation.Builder builder = ContentProviderOperation
                        .newInsert(uri)
                        .withValue(DataContract.Hymns.SONG_NAME, student.getName())
                        .withValue(DataContract.Hymns.SONG_TEXT, student.getGender())
                        .withValue(DataContract.Hymns.ENGLISH_VERSION, student.getPhoneNumber())
                        .withValue(DataContract.Hymns.UPDATED, String.valueOf( System.currentTimeMillis()));

                Log.d(TAG, "Data from Json" + student.getName() + " " + student.getChapter());

                batch.add(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return batch;
    }

    public int getStudentCount() {
        return studentCount;
    }
}
