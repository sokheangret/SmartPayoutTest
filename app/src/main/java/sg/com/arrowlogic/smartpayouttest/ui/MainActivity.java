package sg.com.arrowlogic.smartpayouttest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import sg.com.arrowlogic.smartpayouttest.network.ConnectionReq;
import sg.com.arrowlogic.smartpayouttest.R;
import sg.com.arrowlogic.smartpayouttest.network.MoneyReq;
import sg.com.arrowlogic.smartpayouttest.util.Converter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ConnectionReq.ConnectionCallback, MoneyReq.ConnectionCallback {

    private EditText etMachineName, etIpAddress, etPortNumber, etGroupNumber, etBuffer;
    private ConnectionReq connectionReq;
    private TextView tvStatus,tvMoneyRemain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_connect :
                connectionReq = new ConnectionReq( Converter.textFromEditText(etMachineName),
                        Converter.textFromEditText(etIpAddress),
                        Converter.stringToInt(Converter.textFromEditText(etPortNumber)),
                        Converter.stringToInt(Converter.textFromEditText(etGroupNumber)),
                        Converter.stringToInt(Converter.textFromEditText(etBuffer)),
                        this);
                connectionReq.execute();
                break;

            case R.id.bt_10_usd :
                reqInputMoney("*req-10$");
                break;

            case R.id.bt_12_usd :
                reqInputMoney("*req-12$");
                break;

            case R.id.bt_14_usd :
                reqInputMoney("*req-14$");
                break;

            case R.id.bt_16_usd :
                reqInputMoney("*req-16$");
                break;
        }
    }

    @Override
    public void onConnect(String data) {
        tvStatus.setText(data);
    }

    @Override
    public void onReceiveMoney(String data) {
        tvMoneyRemain.setText(data);
    }

    /**
     * Description: Method for initialize view object
     */
    private void initView() {
        etMachineName   =   findViewById(R.id.et_machine_name);
        etIpAddress     =   findViewById(R.id.et_ip_address);
        etPortNumber    =   findViewById(R.id.et_port_number);
        etGroupNumber   =   findViewById(R.id.et_group_number);
        etBuffer        =   findViewById(R.id.et_buffer);

        tvStatus        =   findViewById(R.id.tv_status);
        tvMoneyRemain   =   findViewById(R.id.tv_remain_money);

        findViewById(R.id.bt_connect).setOnClickListener(this);
        findViewById(R.id.bt_10_usd).setOnClickListener(this);
        findViewById(R.id.bt_12_usd).setOnClickListener(this);
        findViewById(R.id.bt_14_usd).setOnClickListener(this);
        findViewById(R.id.bt_16_usd).setOnClickListener(this);
    }

    /**
     * For send money request
     * @param reqMoney
     */
    private void reqInputMoney(String reqMoney){
        new MoneyReq(Converter.textFromEditText(etIpAddress),
                Converter.stringToInt(Converter.textFromEditText(etPortNumber)),
                reqMoney,
                this).execute();
    }
}
