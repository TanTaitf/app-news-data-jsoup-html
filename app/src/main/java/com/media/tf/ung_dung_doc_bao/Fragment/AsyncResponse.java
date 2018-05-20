package com.media.tf.ung_dung_doc_bao.Fragment;

import android.view.KeyEvent;

/**
 * Created by Windows 8.1 Ultimate on 23/09/2017.
 */

public interface AsyncResponse {
    boolean onKeyDown (int keyCode, KeyEvent event);

    void processFinish(String output);
}

