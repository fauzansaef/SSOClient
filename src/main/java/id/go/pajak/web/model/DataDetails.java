/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.go.pajak.web.model;

import lombok.Data;

/**
 *
 * @author FAUZAN SAEF
 */
@Data
public class DataDetails {

    private UserAuth userAuth;
    private CheckToken checkToken;

    public DataDetails(UserAuth userAuth, CheckToken checkToken) {
        this.userAuth = userAuth;
        this.checkToken = checkToken;
    }
}
