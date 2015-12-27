package kaist.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class TestActivity extends Fragment {
    Context mContext;

    public TestActivity(Context context)
    {
        mContext = context;
    }
}
