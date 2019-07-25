package io.securypto.dsgv1;

import android.app.Application;

public class GlobalClass extends Application {





    private String vault_passwd;
    public String get_vault_passwd() {
        return vault_passwd; }
    public void set_vault_passwd(String avault_passwd) {

        vault_passwd = avault_passwd;
    }



    private String vault_name;
    public String get_vault_name() {
        return vault_name;
    }
    public void set_vault_name(String avault_name) {

        vault_name = avault_name;
    }



    private String vault_name_short;
    public String get_vault_name_short() {
        return vault_name_short;
    }
    public void set_vault_name_short(String avault_name_short) {

        vault_name_short = avault_name_short;
    }


    private String current_valt_Pub_key;
    public String get_current_valt_Pub_key() {
        return current_valt_Pub_key;
    }
    public void set_current_valt_Pub_key(String acurrent_valt_Pub_key) {
        current_valt_Pub_key = acurrent_valt_Pub_key;
    }



    private String current_valt_Priv_key;
    public String get_current_valt_Priv_key() {
        return current_valt_Priv_key;
    }
    public void set_current_valt_Priv_key(String acurrent_valt_Priv_key) {
        current_valt_Priv_key = acurrent_valt_Priv_key;
    }





    private int current_data_array_part;
    public int get_current_data_array_part() {
        return current_data_array_part;
    }
    public void set_current_data_array_part(int acurrent_data_array_part) {
        current_data_array_part = acurrent_data_array_part;
    }


    private String current_data_msg_for_qr;
    public String get_current_data_msg_for_qr() { return current_data_msg_for_qr; }
    public void set_current_data_msg_for_qr(String acurrent_data_msg_for_qr) {
        current_data_msg_for_qr = acurrent_data_msg_for_qr;
    }




}