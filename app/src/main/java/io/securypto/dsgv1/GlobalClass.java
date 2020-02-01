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



    private String tmp_contact_Pub_key;
    public String get_tmp_contact_Pub_key() {
        return tmp_contact_Pub_key;
    }
    public void set_tmp_contact_Pub_key(String atmp_contact_Pub_key) {
        tmp_contact_Pub_key = atmp_contact_Pub_key;
    }


    private String tmp_data1;
    public String get_tmp_data1() {
        return tmp_data1;
    }
    public void set_tmp_data1(String atmp_data1) {
        tmp_data1 = atmp_data1;
    }


    private String tmp_data2;
    public String get_tmp_data2() {
        return tmp_data2;
    }
    public void set_tmp_data2(String atmp_data2) {
        tmp_data2 = atmp_data2;
    }



}