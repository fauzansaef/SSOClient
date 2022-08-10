/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.go.pajak.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ryff
 */
public class JabatanPegawai implements Serializable {

    private String jabatanPegawai_iri;
    private String jabatan_name;
    private String kantor_name;
    private String unit_name;
    private String tipeJabatan_name;
    private List<String> roles = new ArrayList<>();

    public JabatanPegawai() {
    }

    public JabatanPegawai(String jabatanPegawai_iri, String jabatan_name, String kantor_name, String unit_name, String tipeJabatan_name) {
        this.jabatanPegawai_iri = jabatanPegawai_iri;
        this.jabatan_name = jabatan_name;
        this.kantor_name = kantor_name;
        this.unit_name = unit_name;
        this.tipeJabatan_name = tipeJabatan_name;
    }

    public String getJabatanPegawai_iri() {
        return jabatanPegawai_iri;
    }

    public void setJabatanPegawai_iri(String jabatanPegawai_iri) {
        this.jabatanPegawai_iri = jabatanPegawai_iri;
    }

    public String getJabatan_name() {
        return jabatan_name;
    }

    public void setJabatan_name(String jabatan_name) {
        this.jabatan_name = jabatan_name;
    }

    public String getKantor_name() {
        return kantor_name;
    }

    public void setKantor_name(String kantor_name) {
        this.kantor_name = kantor_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getTipeJabatan_name() {
        return tipeJabatan_name;
    }

    public void setTipeJabatan_name(String tipeJabatan_name) {
        this.tipeJabatan_name = tipeJabatan_name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
