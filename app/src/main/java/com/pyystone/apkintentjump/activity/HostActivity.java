package com.pyystone.apkintentjump.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.pyystone.apkintentjump.JumpTools;
import com.pyystone.apkintentjump.R;
import com.pyystone.apkintentjump.data.JumpHost;
import com.pyystone.apkintentjump.data.JumpScheme;
import com.pyystone.apkintentjump.ui.HostListAdapter;
import com.pyystone.apkintentjump.ui.ParamListAdapter;

/**
 * host详情页
 */
public class HostActivity extends AppCompatActivity implements View.OnClickListener, ParamListAdapter.UrlChangeListener {

    public static final String EXTRA_HOST_ID = "extra_scheme_uuid";

    private TextView mTvUrl;
    private JumpHost mJumpHost;
    private String mBaseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        initData();
        if (mJumpHost == null || TextUtils.isEmpty(mBaseUrl)) {
            finish();
            return;
        }
        initUI();
    }

    private void initData() {
        int hostId = getIntent().getIntExtra(EXTRA_HOST_ID,-1);
        mJumpHost = JumpHost.load(hostId);
        if (mJumpHost == null) {
            return;
        }
        JumpScheme scheme = JumpScheme.load(mJumpHost.getParentId());
        if (scheme == null) {
            return;
        }
        mBaseUrl = String.format("%s://%s",scheme.getScheme(),mJumpHost.getHost());
    }

    private void initUI() {
        ListView listView = (ListView) findViewById(R.id.listView);
        ParamListAdapter paramListAdapter = new ParamListAdapter(this, mJumpHost, this);
        listView.setAdapter(paramListAdapter);
        mTvUrl = (TextView) findViewById(R.id.tvUrl);
        initListener();
    }

    private void initListener() {
        findViewById(R.id.btnJump).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnJump) {
            JumpTools.urlJump(this,mTvUrl.getText().toString());
        }
    }

    @Override
    public void onValueChange(String url) {
        mTvUrl.setText(String.format("%s?%s",mBaseUrl,url));
    }
}
