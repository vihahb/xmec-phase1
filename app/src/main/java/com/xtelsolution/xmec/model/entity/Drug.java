package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/27/2017
 * Email: leconglongvu@gmail.com
 */
public class Drug implements Serializable{
    @Expose
    private Integer id;
    @Expose
    private String tenThuoc;
    @Expose
    private String dangBaoChe;
    @Expose
    private String nhomDuocLy;
    @Expose
    private String thanhPhan;
    @Expose
    private String chiDinh;
    @Expose
    private String chongChiDinh;
    @Expose
    private String tuongTacThuoc;
    @Expose
    private String tacDungPhu;
    @Expose
    private String chuYDePhong;
    @Expose
    private String lieuLuong;
    @Expose
    private String baoQuan;

    public Drug() {
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public String getDangBaoChe() {
        return dangBaoChe;
    }

    public void setDangBaoChe(String dangBaoChe) {
        this.dangBaoChe = dangBaoChe;
    }

    public String getNhomDuocLy() {
        return nhomDuocLy;
    }

    public void setNhomDuocLy(String nhomDuocLy) {
        this.nhomDuocLy = nhomDuocLy;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public void setThanhPhan(String thanhPhan) {
        this.thanhPhan = thanhPhan;
    }

    public String getChiDinh() {
        return chiDinh;
    }

    public void setChiDinh(String chiDinh) {
        this.chiDinh = chiDinh;
    }

    public String getChongChiDinh() {
        return chongChiDinh;
    }

    public void setChongChiDinh(String chongChiDinh) {
        this.chongChiDinh = chongChiDinh;
    }

    public String getTuongTacThuoc() {
        return tuongTacThuoc;
    }

    public void setTuongTacThuoc(String tuongTacThuoc) {
        this.tuongTacThuoc = tuongTacThuoc;
    }

    public String getTacDungPhu() {
        return tacDungPhu;
    }

    public void setTacDungPhu(String tacDungPhu) {
        this.tacDungPhu = tacDungPhu;
    }

    public String getChuYDePhong() {
        return chuYDePhong;
    }

    public void setChuYDePhong(String chuYDePhong) {
        this.chuYDePhong = chuYDePhong;
    }

    public String getLieuLuong() {
        return lieuLuong;
    }

    public void setLieuLuong(String lieuLuong) {
        this.lieuLuong = lieuLuong;
    }

    public String getBaoQuan() {
        return baoQuan;
    }

    public void setBaoQuan(String baoQuan) {
        this.baoQuan = baoQuan;
    }
}