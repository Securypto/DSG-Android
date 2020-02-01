package io.securypto.dsgv1;

import android.content.Intent;

//import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

//import org.bitcoinj.core.Address;
//import org.bitcoinj.core.DumpedPrivateKey;
//import org.bitcoinj.core.ECKey;
//import org.bitcoinj.core.NetworkParameters;
//import org.bitcoinj.core.Address;
//import org.bitcoinj.core.NetworkParameters;

/*
import org.pivxj.core.Address;
import org.pivxj.core.Block;
import org.pivxj.core.Coin;
import org.pivxj.core.FilteredBlock;
import org.pivxj.core.InsufficientMoneyException;
import org.pivxj.core.NetworkParameters;
import org.pivxj.core.Peer;
import org.pivxj.core.Transaction;
import org.pivxj.core.TransactionConfidence;
import org.pivxj.core.listeners.AbstractPeerDataEventListener;
import org.pivxj.core.listeners.PeerConnectedEventListener;
import org.pivxj.core.listeners.PeerDataEventListener;
import org.pivxj.core.listeners.PeerDisconnectedEventListener;
import org.pivxj.core.listeners.TransactionConfidenceEventListener;
import org.pivxj.params.MainNetParams;
import org.pivxj.wallet.DeterministicSeed;
import org.pivxj.wallet.SendRequest;
import org.pivxj.wallet.UnreadableWalletException;
import org.pivxj.wallet.Wallet;
//import org.pivxj.wallet.exceptions.RequestFailedErrorcodeException;
import org.pivxj.wallet.listeners.WalletCoinsReceivedEventListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
*/

/*
//import org.bitcoinj.core.Address;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.UnsafeByteArrayOutputStream;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.store.UnreadableWalletException;
import org.bitcoinj.wallet.DeterministicSeed;




import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.params.MainNetParams;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;

 */
//import java.security.interfaces.ECKey;




import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;




import io.securypto.DSGV1.R;


public class cryptowallet extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
       // babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_cryptowallet);

        //check login otherwise go to firstpage
        //babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);


/*

//werkt

        String seedCode = "brass news detail pretty foster exit roof guard until security undo sleep";
        long creationtime = 1409478661L;
        DeterministicSeed seed = null;
        try {
            seed = new DeterministicSeed(seedCode,null,"",creationtime);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }


        NetworkParameters params = MainNetParams.get();

        Wallet wallet = Wallet.fromSeed(params, seed);
        Address address = wallet.currentReceiveAddress();

        TextView textViewmsg = findViewById(R.id.textveld);
        textViewmsg.setText("Address:\n"+address);

*/








/*
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");


// Specify the Network Parameters for mainnet or testnet
        NetworkParameters params = NetworkParameters.prodNet();

// Provide the public key from which you want to derive addresses
        String xPub = "xpub6Cw8YA6Mko3xfkYpMQDZjGjgDTWUrJr87NBSiDPXqcmcSJTgxLXm3VCw3iQs4iC5ZrwpY3M21a43DZmiMzDXWzzhF1n7yxSXDnEHjJN6jwK";

// Create watching wallet, with the help of Wallet class
        Wallet wallet = Wallet.fromWatchingKeyB58(params, xPub, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);

// Print the very first derived address from provided public key
        System.out.println("Receiving Address : " + wallet.currentReceiveAddress());

*/




        // either test or production net are possible
   //     final NetworkParameters netParams = MainNetParams.get();


        // Provide the public key from which you want to derive addresses
  //      String xPub = "xpub6Cw8YA6Mko3xfkYpMQDZjGjgDTWUrJr87NBSiDPXqcmcSJTgxLXm3VCw3iQs4iC5ZrwpY3M21a43DZmiMzDXWzzhF1n7yxSXDnEHjJN6jwK";

        // Create watching wallet, with the help of Wallet class
     //     Wallet walletoutput = Wallet.fromWatchingKey(netParams, xPub, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);


        // Toast.makeText(getApplicationContext(), addressFromKey.toString(), Toast.LENGTH_SHORT).show();

        // Toast.makeText(getApplicationContext(), privkey, Toast.LENGTH_SHORT).show();



//werkt

/*

        ECKey key = new ECKey();

        // either test or production net are possible
        final NetworkParameters netParams = MainNetParams.get();

        //compressed
        Address addressFromKey = key.toAddress(netParams);

        //compressed wif
        String privkey = key.getPrivateKeyAsWiF(netParams);

        String privkey2 = key.getPrivateKeyAsHex();

        TextView textViewmsg = findViewById(R.id.textveld);
        textViewmsg.setText("Address:\n"+addressFromKey.toString()+"\n\n\nPrivkey:\n"+privkey+"\n\n\nPrivkey HEX:\n"+privkey2+"\n\n\nFrom Xpub:\n");

*/




        //babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));








    }








    @Override
    public void onResume(){
        super.onResume();
      //  babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //check login otherwise go to firstpage
       // babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);
    }






    static private String adjustTo64(String s) {
        switch(s.length()) {
            case 62: return "00" + s;
            case 63: return "0" + s;
            case 64: return s;
            default:
                throw new IllegalArgumentException("not a valid key: " + s);
        }
    }


    private static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashInBytes.length; i++) {
            sb.append(Integer.toString((hashInBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }








































    public void gotologin66(View View){
        Intent myIntentgotologinsuccesafterwalllet = new Intent(getBaseContext(),   login_succes.class);
        startActivity(myIntentgotologinsuccesafterwalllet);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }








}