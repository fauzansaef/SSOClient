package id.go.pajak.web.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CheckToken implements Serializable {

    private String nip18;
    private String pegawai_id;
    private String user_name;
    private List<JabatanPegawai> jabatan_pegawai = new ArrayList<>();
    private String[] authorities;
    private String client_id;
    private String[] aud;
    private String nama;
    private String user_identifier;
    private String refresh_token_iam;
    private Integer refresh_token_iam_expired_time;
    private Integer token_iam_expired_time;
    private String[] scope;
    private String nip9;
    private String id;
    private String exp;
    private String token_iam;
    private boolean pensiun;
    
    

}
