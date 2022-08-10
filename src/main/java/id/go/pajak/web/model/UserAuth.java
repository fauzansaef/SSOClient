package id.go.pajak.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UserAuth implements Serializable {

    private String id;
    private String access_token;
    private Integer refresh_token_iam_expired_time;
    private Integer token_iam_expired_time;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
    private String nip18;
    private String nama;
    private String user_identifier;
    private String pegawai_id;
    private String refresh_token_iam;
    private String nip9;
    private List<JabatanPegawai> jabatan_pegawai = new ArrayList<>();
    private String token_iam;
    private boolean pensiun;
}
