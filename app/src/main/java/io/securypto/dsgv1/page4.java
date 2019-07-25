package io.securypto.dsgv1;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import io.securypto.DSGV1.R;


public class page4 extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String user_passwd  = globalVariable.get_vault_passwd();



        //        android:drawableLeft="@mipmap/ic_launcher_round"
        //        android:paddingLeft="30dip"



        //String uitkomst = babak.myMethods();


//share a text
        //Context context_share_txt= firstpage.this;
        //babak.sharetxt(context_share_txt, "DigiSafeGuard", "dit is een text", "Share DSG");


        //        Context context_write_external = getApplicationContext();
//        babak.write_external(context_write_external, "testv11.txt","blabla");


        //schrijf een file
        //String texttoschrijven = "kkkkkkkkkk";
       // String filenametowrite = "hello.txt";
      //  Context context2 = getApplicationContext();
     //   babak.write(context2, filenametowrite,texttoschrijven);


        //lees een file
       // String filenametoread = "hello.txt";
       // Context context1 = getApplicationContext();
       // String uitkomst = babak.read_file(context1, filenametoread);


      //  String cryptoPass="ffffff";
      //  String uitkomst2 = io.securypto.myapp1.enckeys.encryptIt (cryptoPass, uitkomst);
     //   String uitkomst3 = io.securypto.myapp1.enckeys.decryptIt (cryptoPass, uitkomst2);



        //gebruik je eigenlijk niet
       // String strNormalText="fffsss";
      //  String uitkomst3 = AESUtils.encrypeme(strNormalText);
     //   String uitkomst4 = AESUtils.decrypeme(uitkomst3);






String account_name_sended="";

        //lees_pubic_key
        Context context_lees_pubic_key= getApplicationContext();

        String publicKeyBytesBase64_gelezen = encclass.read_pubic_key(user_passwd, context_lees_pubic_key, account_name_sended);


        //lees_private_key
        Context context_lees_private_key= getApplicationContext();
        String privateKeyBytesBase64_gelezen = encclass.read_private_key(user_passwd, context_lees_private_key, account_name_sended);




            String dataToEncrypt ="DigiSafeGuard";

            // enc a text
            String encrypted = encclass.encryptRSAToString(publicKeyBytesBase64_gelezen, dataToEncrypt);

            // dec a text
            String decrypted = encclass.decryptRSAToString(privateKeyBytesBase64_gelezen, encrypted);




        //Context context_write_external = getApplicationContext();
       // babak.write_external(context_write_external, "testv11.txt","blabla");



        //String originalString = "howtodoinjava.com";

        //String secretKey = "boooooooooom!!!!";
        //String encryptedString = AESCrypt.encrypt(secretKey, originalString) ;

        //String secretKey2 = "boooooooooom!!!!";
        //String decryptedString = AESCrypt.decrypt(secretKey2, encryptedString) ;


        //System.out.println("thank" + originalString);
        //System.out.println("thank" + encryptedString);
        //System.out.println("thank" + decryptedString);



    //    System.out.println("thank" + privateKeyBytesBase64_gelezen);
    //    TextView veldvooroutput = findViewById(R.id.textView4);
    //    veldvooroutput.setText(decrypted);



        String qr_text= "blabla";
      //  int size = 500;


        ImageView qrholder;
        Bitmap bitmap ;
        qrholder = (ImageView)findViewById(R.id.qrholder);
        try {

            Context contextqr= getApplicationContext();

            bitmap = babak.texttoqr(contextqr, qr_text, 500);

            qrholder.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }












    }





























}


