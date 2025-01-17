package com.alice.activity;

import android.content.Intent;
import android.widget.TextView;

import com.alice.R;
import com.alice.activity.base.BaseActivity;
import com.alice.customView.BaseDialog;
import com.alice.customView.TransferDialog;
import com.alice.presenter.MainPresenter;
import com.alice.view.IMainView;

import org.web3j.crypto.Credentials;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * create by zhhr on 2019/09/18
 */
public class Main1Activity extends BaseActivity<MainPresenter> implements IMainView {
    @BindView(R.id.address)
    TextView mTvAddress;

    private TransferDialog transformDialog;
    private BaseDialog createDialog;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new MainPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    @OnClick({R.id.toRn})
    public void toRn() {
        startActivity(new Intent(Main1Activity.this,RnActivity.class));
    }

    @Override
    @OnClick({R.id.createWallet})
    public void createWallet() {
        mPresenter.checkWallet();
    }

    @Override
    @OnClick({R.id.importWallet})
    public void importWallet() {
        mPresenter.importWallet();
    }

    @Override
    @OnClick({R.id.checkBalances})
    public void checkBalances() {
        mPresenter.checkBalances();
    }

    @Override
    @OnClick({R.id.transfer})
    public void transfer() {
        if(transformDialog == null){
            transformDialog = new TransferDialog(Main1Activity.this);
            transformDialog.setOnClickConfirmListener(new TransferDialog.OnClickConfirmListener() {
                @Override
                public void onClickConfirm(String address, String value) {
                    mPresenter.transfer(address,value);
                }

                @Override
                public void onAddressError(String message) {
                    toast(message);
                }

                @Override
                public void onValueError(String message) {
                    toast(message);
                }
            });
        }
        transformDialog.show();
    }
    @OnClick({R.id.signMessage})
    public void signMessage(){
       Intent intent = new Intent(this,WebViewActivity.class);
       startActivity(intent);
    }

    @Override
    public void showContent(Credentials credentials) {
        mTvAddress.setText("address：" + credentials.getAddress());
    }

    @Override
    public void showCreateDialog() {
        if(createDialog == null){
            createDialog = new BaseDialog.Builder(this)
                    .setTitle("Replace the key")
                    .setMessage("You created the key already,Do you want to replace it？")
                    .setPositiveButton("Replace", v -> {
                        mPresenter.createWallet();
                        mTvAddress.setText("");
                        if(createDialog!=null){
                            createDialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", v -> {
                        if(createDialog!=null){
                            createDialog.dismiss();
                        }
                    })
                    .create();
        }
        createDialog.show();
    }

    @Override
    public void showToast(String t) {
        toast(t);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
